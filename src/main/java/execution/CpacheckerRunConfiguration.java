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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CpacheckerRunConfiguration extends RunConfigurationBase {// implements RunProfileWithCompileBeforeLaunchOption {
    public ApplicationConfiguration programParameters;

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

}
