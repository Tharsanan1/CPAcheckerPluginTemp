package execution.linux_cmd;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import execution.CpacheckerRunConfiguration;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CmdLinuxExecution {
    CpacheckerRunConfiguration cpacheckerRunConfiguration;
    public String PATH_FOR_TEMP_FILE = System.getProperty("user.dir");
    Project project;
    String filePath;
    String tempFile;
    boolean runMainFile;
    public CmdLinuxExecution(CpacheckerRunConfiguration cpacheckerRunConfiguration, Project project, boolean runMainFile){
        this.cpacheckerRunConfiguration = cpacheckerRunConfiguration;
        this.project = project;
        this.runMainFile = runMainFile;
        this.PATH_FOR_TEMP_FILE = System.getProperty("user.dir");
    }

    public void process(){
        if(runMainFile) {
            String projectName = project.getName();
            VirtualFile[] vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
            String sourceRootsList = Arrays.stream(vFiles).map(VirtualFile::getUrl).collect(Collectors.joining("\n"));

            Messages.showInfoMessage("Source roots for the " + projectName + " plugin:\n" + sourceRootsList, "Project Properties");
            ProjectFileIndex.SERVICE.getInstance(project).iterateContent(new ContentIterator() {
                @Override
                public boolean processFile(VirtualFile fileOrDir) {
                    if (fileOrDir.getExtension() != null && fileOrDir.getExtension().toLowerCase().equals("c")) {
                        if (fileOrDir.getName().toLowerCase().equals("main.c")) {
                            filePath = fileOrDir.getPath();
                        }
                    }
                    return true;
                }
            });
        }
        else{
            File file = new File(PATH_FOR_TEMP_FILE + "/temp.c");
            try {
                file.createNewFile();
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(tempFile);
                filePath = PATH_FOR_TEMP_FILE + "/temp.c";
                output.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        runCommand();

    }

    private String prepareCommand(){
        String commandString = "";
        String whiteSpace = " ";
        String logLevelProp = "-setprop log.consoleLevel=" + cpacheckerRunConfiguration.getLogLevelString();
        String machineModelProp = "-setprop analysis.machineModel=" + cpacheckerRunConfiguration.getMachineModelString();
        String configurationProp = "-" + cpacheckerRunConfiguration.getConfigString();
        String cpa = cpacheckerRunConfiguration.getCpaDir() + "/" + "scripts/cpa.sh";
        commandString += cpa + whiteSpace + logLevelProp + whiteSpace + machineModelProp + whiteSpace + configurationProp + whiteSpace + filePath;
        return commandString;
    }

    private void runCommand(){
        try {
            GeneralCommandLine command = new GeneralCommandLine();
            Process p = Runtime.getRuntime().exec(prepareCommand());
            if (true) {
                ToolWindowManager manager = ToolWindowManager.getInstance(project);
                String id = "CPAchecker";
                TextConsoleBuilderFactory factory = TextConsoleBuilderFactory.getInstance();
                TextConsoleBuilder builder = factory.createBuilder(project);
                ConsoleView view = builder.getConsole();

                ColoredProcessHandler handler = new ColoredProcessHandler(p, command.getPreparedCommandLine());
                handler.startNotify();
                view.attachToProcess(handler);

                ToolWindow window = manager.getToolWindow(id);

                if (window == null) {
                    window = manager.registerToolWindow(id, true, ToolWindowAnchor.BOTTOM);
                }

                ContentFactory cf = window.getContentManager().getFactory();
                Content c = cf.createContent(view.getComponent(), "Run " + (window.getContentManager().getContentCount() + 1), true);

                window.getContentManager().addContent(c);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTempFile(String tempFile) {
        this.tempFile = tempFile;
    }


}
