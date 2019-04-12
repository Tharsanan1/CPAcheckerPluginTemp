package execution.gutter;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CpaIconRenderer extends GutterIconRenderer {

    private Project project;
    private Icon icon = AllIcons.RunConfigurations.TestState.Run;
    private String fileName;
    private int line;

    public CpaIconRenderer(Project project, String fileName) {
        this.project = project;
        this.fileName = fileName;
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

            }
        };
    }

    @Override
    public String getTooltipText() {
        return "Run CPAchecker in this function" ;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setLine(int i) {
        line = i;
    }

}
