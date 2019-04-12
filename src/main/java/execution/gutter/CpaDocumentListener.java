package execution.gutter;


import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.TextRange;

import javax.swing.*;

public class CpaDocumentListener  implements DocumentListener {
    private String fileName;
    private Editor editor;

    public CpaDocumentListener(String fileName, Editor editor) {
        this.fileName = fileName;
        this.editor = editor;
    }

    @Override
    public void beforeDocumentChange(DocumentEvent documentEvent) {

    }

    @Override
    public void documentChanged(DocumentEvent documentEvent) {
        try {
            RangeHighlighter[] highlighters = editor.getMarkupModel().getAllHighlighters();
            for (RangeHighlighter highlighter : highlighters) {

                if (highlighter.getGutterIconRenderer() instanceof CpaIconRenderer) {
                    editor.getMarkupModel().removeHighlighter(highlighter);
                }
            }


            for (int i = 0; i < documentEvent.getDocument().getLineCount(); i++) {
                int startOffset = documentEvent.getDocument().getLineStartOffset(i);
                int endOffset = documentEvent.getDocument().getLineEndOffset(i);

                String lineText = documentEvent.getDocument().getText(new TextRange(startOffset, endOffset)).trim();
                if(!isFunctionLine(lineText)){
                    continue;
                }
                Icon icon = AllIcons.RunConfigurations.TestState.Run;

                CpaIconRenderer cpaIconRenderer = new CpaIconRenderer(editor.getProject(), fileName, documentEvent.getDocument());
                cpaIconRenderer.setLine(i);
                cpaIconRenderer.setIcon(icon);

                RangeHighlighter rangeHighlighter = createRangeHighlighter(documentEvent.getDocument(), editor, i, i, new TextAttributes());
                rangeHighlighter.setGutterIconRenderer(cpaIconRenderer);
            }



        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public String getFileName() {
        return fileName;
    }

    private RangeHighlighter createRangeHighlighter(Document document, Editor editor, int fromLine, int toLine, TextAttributes attributes) {
        int lineStartOffset = document.getLineStartOffset(Math.max(0, fromLine));
        int lineEndOffset = document.getLineEndOffset(Math.max(0, toLine));

        return editor.getMarkupModel().addRangeHighlighter(
                lineStartOffset, lineEndOffset, 3333, attributes, HighlighterTargetArea.LINES_IN_RANGE
        );
    }

    private boolean isFunctionLine(String line){
        Boolean bool = line.matches("[a-zA-Z]+ [a-zA-Z]+[0-9]*[ ]*[(](.* .*(,.* .*)*)?[)]( *)[{](.*)");
        return bool;
    }
}
