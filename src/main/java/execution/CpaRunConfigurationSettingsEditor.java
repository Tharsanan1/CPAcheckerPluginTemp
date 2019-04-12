package execution;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CpaRunConfigurationSettingsEditor extends SettingsEditor<CpacheckerRunConfiguration> {
    private JPanel configWindow;
    private JLabel cpaCheckerRevisionLabel;
    private JLabel configurationLabel;
    private JLabel propertiesOrSpecificationLabel;
    private JLabel witnessValidation;
    private JLabel logLevelLabel;
    private JLabel machineModel;
    private JLabel timeLimitation;
    private JLabel memoryLimitation;
    private JLabel cpuCoreLimitation;
    private JLabel executionType;
    private JLabel graphicOutput;
    String[] revisionString = { "trunk:HEAD", "....", "....", "...." };
    JComboBox revisionCombo = new JComboBox(revisionString);
    String[] configString = { "default", "default--overflow", "valueAnalysis-proofcheck-multiedges-defaultprop", "...." };
    JComboBox configCombo = new JComboBox(configString);
    String[] specString = { "Plain text", "CPAchecker", "file", "sv-repo" };
    JComboBox specCombo = new JComboBox(specString);
    JTextArea specTextArea = new JTextArea();
    JRadioButton on = new JRadioButton("ON");
    JRadioButton off = new JRadioButton("OFF");
    String[] logLevelString = { "All", "Finest", "Finer", "Fine", "Info", "Warning", "Severe", "Off"};
    JComboBox logLevelCombo = new JComboBox(logLevelString);
    String[] machineModelString = { "Linux 32", "Linux 64", "...."};
    JComboBox machineModelCombo = new JComboBox(machineModelString);
    JTextField timeLimitationField = new JTextField();
    JTextField memoryLimitationField = new JTextField();
    JTextField coreLimitationField = new JTextField();
    JRadioButton cloud = new JRadioButton("Cloud");
    JRadioButton local = new JRadioButton("Local");
    JFileChooser cpaDirChooser;
    JButton chooseFile = new JButton("Browse CPA");
    JRadioButton on2 = new JRadioButton("ON");
    JRadioButton off2 = new JRadioButton("OFF");
    String cpaDir = "/";

    ButtonGroup onOffButtonGroup = new ButtonGroup();
    ButtonGroup onOffButtonGroup2 = new ButtonGroup();
    ButtonGroup cloudLocalButtonGroup = new ButtonGroup();


    @Override
    protected void resetEditorFrom(CpacheckerRunConfiguration config) {
        timeLimitationField.setText(config.getTimeLimitationFieldText());
        revisionCombo.setSelectedItem(config.getRevisionString());
        configCombo.setSelectedItem(config.getConfigString());
        specCombo.setSelectedItem(config.getSpecComboString());
        specTextArea.setText(config.getSpecAreaText());
        if(config.isOn()){
            on.setSelected(true);
        }
        else{
            on.setSelected(false);
        }
        if(config.isOn2()){
            on2.setSelected(true);
        }
        else{
            on2.setSelected(false);
        }
        if(config.isCloud()){
            cloud.setSelected(true);
        }
        else{
            cloud.setSelected(false);
        }

        logLevelCombo.setSelectedItem(config.getLogLevelString());
        machineModelCombo.setSelectedItem(config.getMachineModelString());
        memoryLimitationField.setText(config.getMemoryLimitationString());
        coreLimitationField.setText(config.getCoreLimitationString());
        cpaDir = config.getCpaDir();

    }

    @Override
    protected void applyEditorTo(CpacheckerRunConfiguration config) throws ConfigurationException {
        try {
            config.setTimeLimitationFieldText(timeLimitationField.getText());
            config.setRevisionString(revisionCombo.getSelectedItem().toString());
            config.setConfigString(configCombo.getSelectedItem().toString());
            config.setSpecComboString(specCombo.getSelectedItem().toString());
            config.setSpecAreaText(specTextArea.getText());
            if (onOffButtonGroup.getSelection() != null && onOffButtonGroup.getSelection().getActionCommand().equals("on")) {
                config.setOn(true);
            } else {
                off.setSelected(true);
                config.setOn(false);
            }
            if (onOffButtonGroup2.getSelection() != null && onOffButtonGroup2.getSelection().getActionCommand().equals("on")) {
                config.setOn2(true);
            } else {
                off2.setSelected(true);
                config.setOn2(false);
            }
            if (cloudLocalButtonGroup.getSelection() != null && cloudLocalButtonGroup.getSelection().getActionCommand().equals("cloud")) {
                config.setCloud(true);
            } else {
                local.setSelected(true);
                config.setCloud(false);
            }
            config.setLogLevelString(logLevelCombo.getSelectedItem().toString());
            config.setMachineModelString(machineModelCombo.getSelectedItem().toString());
            config.setMemoryLimitationString(memoryLimitationField.getText());
            config.setCoreLimitationString(coreLimitationField.getText());
            config.setCpaDir(cpaDir);
        }
        catch(Exception e){
            System.out.println("error: " + e.getMessage());
        }


    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        configWindow = new JPanel();
        configWindow.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        cpaCheckerRevisionLabel = new JLabel("CPAChecker Revision");
        configurationLabel = new JLabel("Configuration");
        propertiesOrSpecificationLabel = new JLabel("Properties/Specification             ");
        witnessValidation = new JLabel("Witness Validation");
        logLevelLabel = new JLabel("Log Level");
        machineModel = new JLabel("Machine Model");
        timeLimitation = new JLabel("Time Limitation");
        memoryLimitation = new JLabel("Memory Limitation");
        cpuCoreLimitation = new JLabel("CPU Core Limitation");
        executionType = new JLabel("Execution Type");
        graphicOutput = new JLabel("Graphic Output");

        on.setActionCommand("on");
        off.setActionCommand("off");
        on2.setActionCommand("on");
        off2.setActionCommand("off");
        cloud.setActionCommand("cloud");
        local.setActionCommand("local");

        onOffButtonGroup.add(on);
        onOffButtonGroup.add(off);
        onOffButtonGroup2.add(on2);
        onOffButtonGroup2.add(off2);
        cloudLocalButtonGroup.add(cloud);
        cloudLocalButtonGroup.add(local);

        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cpaDirChooser = new JFileChooser();
                System.out.println(System.getProperty("os.name"));
                if(cpaDir != null) {
                    cpaDirChooser.setCurrentDirectory(new java.io.File(cpaDir));
                }
                else {
                    if(System.getProperty("os.name").toLowerCase().equals("linux")) {
                        cpaDirChooser.setCurrentDirectory(new java.io.File("/home"));
                    }
                    else{
                        cpaDirChooser.setCurrentDirectory(new java.io.File("C:/")); //need to implement later
                    }
                }
                cpaDirChooser.setDialogTitle("Choose CPA folder");
                cpaDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //
                // disable the "All files" option.
                //
                cpaDirChooser.setAcceptAllFileFilterUsed(false);
                //
                if (cpaDirChooser.showOpenDialog(configWindow) == JFileChooser.APPROVE_OPTION) {
                    cpaDir = cpaDirChooser.getSelectedFile().toString();

                }
                else {
                    System.out.println("No Selection ");
                }
            }
        });


        c.weightx = 1.0;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.ipadx = 10;
        configWindow.add(cpaCheckerRevisionLabel,c);

        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 4;
        c.ipadx = 4;
        configWindow.add(revisionCombo,c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(configurationLabel,c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 4;
        c.ipadx = 4;
        configWindow.add(configCombo,c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(propertiesOrSpecificationLabel,c);

        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 4;
        c.ipadx = 4;
        configWindow.add(specCombo,c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 8;
        c.ipadx = 4;
        c.ipady = 80;
        c.weighty = 1;
        configWindow.add(specTextArea,c);

        c.ipady = 0;
        int count = 1;
        c.gridx = 0;
        c.gridy = 3+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(witnessValidation,c);

        c.gridx = 3;
        c.gridy = 3+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(on,c);

        c.gridx = 5;
        c.gridy = 3+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(off,c);

        c.gridx = 0;
        c.gridy = 4+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(logLevelLabel,c);

        c.gridx = 3;
        c.gridy = 4+count;
        c.gridwidth = 4;
        c.ipadx = 4;
        configWindow.add(logLevelCombo,c);

        c.gridx = 0;
        c.gridy = 5+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(machineModel,c);

        c.gridx = 3;
        c.gridy = 5+count;
        c.gridwidth = 4;
        c.ipadx = 4;
        configWindow.add(machineModelCombo,c);

        c.gridx = 0;
        c.gridy = 6+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(timeLimitation,c);

        c.gridx = 3;
        c.gridy = 6+count;
        c.gridwidth = 4;
        c.ipadx = 4;
        configWindow.add(timeLimitationField,c);

        c.gridx = 0;
        c.gridy = 7+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(cpuCoreLimitation,c);

        c.gridx = 3;
        c.gridy = 7+count;
        c.gridwidth = 4;
        c.ipadx = 4;
        configWindow.add(coreLimitationField,c);

        c.gridx = 0;
        c.gridy = 8+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(memoryLimitation,c);

        c.gridx = 3;
        c.gridy = 8+count;
        c.gridwidth = 4;
        c.ipadx = 4;
        configWindow.add(memoryLimitationField,c);

        c.gridx = 0;
        c.gridy = 9+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(executionType,c);

        c.gridx = 3;
        c.gridy = 9+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(cloud,c);

        c.gridx = 5;
        c.gridy = 9+count;
        c.gridwidth = 1;
        c.ipadx = 4;
        configWindow.add(local,c);

        c.gridx = 6;
        c.gridy = 9+count;
        c.gridwidth = 1;
        c.ipadx = 4;
        configWindow.add(chooseFile,c);

        c.gridx = 0;
        c.gridy = 10+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(graphicOutput,c);

        c.gridx = 3;
        c.gridy = 10+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(on2,c);

        c.gridx = 5;
        c.gridy = 10+count;
        c.gridwidth = 2;
        c.ipadx = 4;
        configWindow.add(off2,c);

        return configWindow;
    }
}
