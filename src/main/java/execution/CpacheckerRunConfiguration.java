package execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import execution.linux_cmd.CmdLinuxExecution;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CpacheckerRunConfiguration extends RunConfigurationBase {// implements RunProfileWithCompileBeforeLaunchOption {
    public ApplicationConfiguration programParameters;
    private String timeLimitationFieldText;
    private String revisionString;
    private String configString;
    private String specComboString;
    private String specAreaText;
    private boolean isOn;
    private String logLevelString;
    private String machineModelString;
    private String memoryLimitationString;
    private String coreLimitationString;
    private String cpaDir;
    private boolean isCloud;
    private boolean isOn2;

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        timeLimitationFieldText = JDOMExternalizer.readString(element, "timeLimitationFieldText");
        revisionString = JDOMExternalizer.readString(element, "revisionString");
        configString = JDOMExternalizer.readString(element, "configString");
        specComboString = JDOMExternalizer.readString(element, "specComboString");
        specAreaText = JDOMExternalizer.readString(element, "specAreaText");
        isOn = JDOMExternalizer.readBoolean(element, "isOn");
        logLevelString = JDOMExternalizer.readString(element, "logLevelString");
        machineModelString = JDOMExternalizer.readString(element, "machineModelString");
        memoryLimitationString = JDOMExternalizer.readString(element, "memoryLimitationString");
        coreLimitationString = JDOMExternalizer.readString(element, "coreLimitationString");
        isCloud = JDOMExternalizer.readBoolean(element, "isCloud");
        isOn2 = JDOMExternalizer.readBoolean(element, "isOn2");
        cpaDir =  JDOMExternalizer.readString(element, "cpaDir");

    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        JDOMExternalizer.write(element, "timeLimitationFieldText", timeLimitationFieldText);
        JDOMExternalizer.write(element, "revisionString", revisionString);
        JDOMExternalizer.write(element, "configString", configString);
        JDOMExternalizer.write(element, "specComboString", specComboString);
        JDOMExternalizer.write(element, "specAreaText", specAreaText);
        JDOMExternalizer.write(element, "isOn", isOn);
        JDOMExternalizer.write(element, "logLevelString", logLevelString);
        JDOMExternalizer.write(element, "machineModelString", machineModelString);
        JDOMExternalizer.write(element, "memoryLimitationString", memoryLimitationString);
        JDOMExternalizer.write(element, "coreLimitationString", coreLimitationString);
        JDOMExternalizer.write(element, "isCloud", isCloud);
        JDOMExternalizer.write(element, "isOn2", isOn2);
        JDOMExternalizer.write(element, "cpaDir", cpaDir);
    }

    public String getRevisionString() {
        return revisionString;
    }

    public void setRevisionString(String revisionString) {
        this.revisionString = revisionString;
    }

    public String getConfigString() {
        return configString;
    }

    public void setConfigString(String configString) {
        this.configString = configString;
    }

    public String getSpecComboString() {
        return specComboString;
    }

    public void setSpecComboString(String specComboString) {
        this.specComboString = specComboString;
    }

    public String getSpecAreaText() {
        return specAreaText;
    }

    public void setSpecAreaText(String specAreaText) {
        this.specAreaText = specAreaText;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public String getLogLevelString() {
        return logLevelString;
    }

    public void setLogLevelString(String logLevelString) {
        this.logLevelString = logLevelString;
    }

    public String getMachineModelString() {
        return machineModelString;
    }

    public void setMachineModelString(String machineModelString) {
        this.machineModelString = machineModelString;
    }

    public String getMemoryLimitationString() {
        return memoryLimitationString;
    }

    public void setMemoryLimitationString(String memoryLimitationString) {
        this.memoryLimitationString = memoryLimitationString;
    }

    public String getCoreLimitationString() {
        return coreLimitationString;
    }

    public void setCoreLimitationString(String coreLimitationString) {
        this.coreLimitationString = coreLimitationString;
    }

    public boolean isCloud() {
        return isCloud;
    }

    public void setCloud(boolean cloud) {
        isCloud = cloud;
    }

    public boolean isOn2() {
        return isOn2;
    }

    public void setOn2(boolean on2) {
        isOn2 = on2;
    }

    public CpacheckerRunConfiguration(String name, Project project, ConfigurationFactoryEx configurationFactory) {
        super(project, configurationFactory, name);
        this.programParameters = new ApplicationConfiguration(name, project, ApplicationConfigurationType.getInstance());
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new CpaRunConfigurationSettingsEditor();

    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull final ExecutionEnvironment env) throws ExecutionException {
        System.out.println(System.getProperty("os.name"));
        if(System.getProperty("os.name").toLowerCase().equals("linux")) {
            CmdLinuxExecution cmdLinuxExecution = new CmdLinuxExecution(this, env.getProject());
            cmdLinuxExecution.process();
        }
        return null;
    }

    public void setTimeLimitationFieldText(String timeLimitationFieldText) {
        this.timeLimitationFieldText = timeLimitationFieldText;
    }

    public String getTimeLimitationFieldText() {
        return timeLimitationFieldText;
    }

    public String getCpaDir() {
        return cpaDir;
    }

    public void setCpaDir(String cpaDir) {
        this.cpaDir = cpaDir;
    }
}
