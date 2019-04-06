package execution;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.ContainerUtil;

public class CpacheckerRunConfigurationType extends ConfigurationTypeBase {
    public CpacheckerRunConfigurationType() {
        super("executeSpecs", "CPA checker", "Execute CPA checker", AllIcons.Actions.Execute);
        final ConfigurationFactory scenarioConfigFactory = new ConfigurationFactoryEx(this) {
            @Override
            public RunConfiguration createTemplateConfiguration(Project project) {
                return new CpacheckerRunConfiguration("CPA checker", project, this);
            }
        };

        addFactory(scenarioConfigFactory);
    }


    public CpacheckerRunConfigurationType getInstance() {
        return ContainerUtil.findInstance(Extensions.getExtensions(CONFIGURATION_TYPE_EP), CpacheckerRunConfigurationType.class);
    }
}
