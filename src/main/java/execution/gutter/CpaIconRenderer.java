package execution.gutter;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import execution.CpacheckerRunConfiguration;
import execution.linux_cmd.CmdLinuxExecution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CpaIconRenderer extends GutterIconRenderer {

    private Project project;
    private Icon icon = AllIcons.RunConfigurations.TestState.Run;
    private String fileName;
    private int line;
    private Document document;

    public CpaIconRenderer(Project project, String fileName, Document document) {
        this.project = project;
        this.fileName = fileName;
        this.document = document;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CpaIconRenderer)) {
            return false;
        }

        CpaIconRenderer cpaIconRenderer = (CpaIconRenderer) obj;
        return cpaIconRenderer.icon.equals(icon) && cpaIconRenderer.line == line;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + icon.hashCode();
        result = 31 * result + line;
        return result;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return icon;
    }

    @Nullable
    @Override
    public AnAction getClickAction() {
        return new AnAction() {
            @Override
            public void actionPerformed(AnActionEvent e) {
                process();
            }
        };
    }

    @Override
    public String getTooltipText() {
        return "Run CPAchecker on this function" ;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setLine(int i) {
        line = i;
    }

    public void process() {
        int closeCurlyCount = 0;
        int openCurlyCount = 0;
        int startOffset = document.getLineStartOffset(line);
        int endOffset = document.getLineEndOffset(line);

        String lineText = document.getText(new TextRange(startOffset, endOffset)).trim();
        String functionString = "";
        functionString += lineText;
        for (int i = line + 1; i < document.getLineCount(); i++) {
            startOffset = document.getLineStartOffset(i);
            endOffset = document.getLineEndOffset(i);

            lineText = document.getText(new TextRange(startOffset, endOffset)).trim();
            functionString += lineText + "\n";
            if(lineText.contains("{")){
                openCurlyCount++;
            }
            if(lineText.contains("}")){
                closeCurlyCount++;
            }
            if(openCurlyCount + 1 == closeCurlyCount){
                break;
            }
        }
        CmdLinuxExecution cmdLinuxExecution = new CmdLinuxExecution(CpacheckerRunConfiguration.getCpacheckerRunConfiguration(), project, false);
        cmdLinuxExecution.setTempFile(functionString);
        cmdLinuxExecution.process();
    }
}
