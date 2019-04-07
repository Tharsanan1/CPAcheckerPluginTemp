package execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
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
        return null;
    }

    public void setTimeLimitationFieldText(String timeLimitationFieldText) {
        this.timeLimitationFieldText = timeLimitationFieldText;
    }

    public String getTimeLimitationFieldText() {
        return timeLimitationFieldText;
    }
}
