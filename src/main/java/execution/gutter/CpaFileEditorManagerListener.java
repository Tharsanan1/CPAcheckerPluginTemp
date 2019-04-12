package execution.gutter;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.codehaus.plexus.util.PathTool.getRelativePath;

public class CpaFileEditorManagerListener  implements FileEditorManagerListener {

    public static String METHOD_REGEX;
    private List<String> openedFiles = new ArrayList<>();
    private List<CpaDocumentListener> documentListeners = new ArrayList<>();


    public CpaFileEditorManagerListener(Project project) {

    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        if (!file.getExtension().toLowerCase().equals("c")) {  // check whether its a c file
            return;
        }

        String pathOldFile = getRelativePath(file.getPath());
        if (openedFiles.stream().anyMatch(f -> f.equals(pathOldFile))) {
            openedFiles.remove(pathOldFile);

            Optional<CpaDocumentListener> optionalListener = documentListeners.stream().filter(l -> Objects.equals(l.getFileName(), pathOldFile)).findAny();
            optionalListener.ifPresent(cpaDocumentListener -> documentListeners.remove(cpaDocumentListener));
        }
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        VirtualFile fileNew = event.getNewFile();
        Editor rootEditor = event.getManager().getSelectedTextEditor();
        if (rootEditor == null) {
            return;
        }

        Document rootDocument = rootEditor.getDocument();
        if (fileNew != null) {

            if(!fileNew.getExtension().toLowerCase().equals("c")){  // need to check whether not a c file
                return;
            }
            String pathNewFile = getRelativePath(fileNew.getPath());

            if (openedFiles.stream().noneMatch(f -> f.equals(fileNew.getPath()))) {
                openedFiles.add(fileNew.getPath());

                for (int i = 0; i < rootDocument.getLineCount(); i++) {
                    int startOffset = rootDocument.getLineStartOffset(i);
                    int endOffset = rootDocument.getLineEndOffset(i);

                    String lineText = rootDocument.getText(new TextRange(startOffset, endOffset)).trim();
                    if(!isFunctionLine(lineText)){
                        continue;
                    }
                    Icon icon = AllIcons.RunConfigurations.TestState.Run;

                    CpaIconRenderer cpaIconRenderer = new CpaIconRenderer(rootEditor.getProject(), pathNewFile);
                    cpaIconRenderer.setLine(i);
                    cpaIconRenderer.setIcon(icon);

                    RangeHighlighter rangeHighlighter = createRangeHighlighter(rootDocument, rootEditor, i, i, new TextAttributes());
                    rangeHighlighter.setGutterIconRenderer(cpaIconRenderer);
                }

                CpaDocumentListener cpaDocumentListener = new CpaDocumentListener(pathNewFile, rootEditor);
                rootDocument.addDocumentListener(cpaDocumentListener);
            }
        }

    }

    private String getRelativePath(String absolutePath) {
        String base = "";
        return new File(base).toURI().relativize(new File(absolutePath).toURI()).getPath();
    }

    private RangeHighlighter createRangeHighlighter(Document document, Editor editor, int fromLine, int toLine, TextAttributes attributes) {
        int lineStartOffset = document.getLineStartOffset(Math.max(0, fromLine));
        int lineEndOffset = document.getLineEndOffset(Math.max(0, toLine));

        return editor.getMarkupModel().addRangeHighlighter(
                lineStartOffset, lineEndOffset, 3333, attributes, HighlighterTargetArea.LINES_IN_RANGE
        );
    }



    private boolean isFunctionLine(String line){
        Boolean bool = line.matches(".* .*[(](.* .*(,.* .*)*)?[)]( *)[{](.*)");
        return bool;
    }
}



