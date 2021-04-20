package view.windows;

import logic.facades.DatabaseFacade;
import view.components.MdcFrame;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.AbstractAction;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class ConnectWindow extends MdcFrame {

    private MdcFrame startingWindow;

    public ConnectWindow(MdcFrame window){

        this.startingWindow = window;
        final String title = "Connect";
        final int width = 400;
        final int height = 400;

        setSize(width,height);
        setTitle(title);

        createWidgets();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void createWidgets() {

        JPanel mainPanel = createGridPanel(7,2,20,20,20);

        JLabel hostLabel = createLabel("Host:");
        JTextField hostTextField = createTextField("localhost");

        JLabel portLabel = createLabel("Port:");
        JTextField portTextField = createTextField("3306");

        JLabel databaseNameLabel = createLabel("Database name:");

        JTextField databaseNameField = createTextField("mdc_db");

        JLabel usernameLabel = createLabel("Username:");

        JTextField usernameField = createTextField("root");

        JLabel passwordLabel = createLabel("Password:");

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(new Color(105,105,105));
        passwordField.setForeground(Color.WHITE);

        JCheckBox hidePasswordCheckBox = new JCheckBox();
        hidePasswordCheckBox.setSelected(true);
        hidePasswordCheckBox.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hidePasswordCheckBox.isSelected()){

                    passwordField.setEchoChar('*');

                }else{
                    passwordField.setEchoChar((char)0);
                }
            }
        });
        hidePasswordCheckBox.setText("hide password");
        hidePasswordCheckBox.setBackground(new Color(67,67,67));
        hidePasswordCheckBox.setForeground(Color.WHITE);

        JButton connectButton = createButton("Connect", event-> connect(
                hostTextField.getText(),
                databaseNameField.getText(),
                portTextField.getText(),
                usernameField.getText(),
                String.valueOf(passwordField.getPassword())
        ),true);

        JButton cancelButton = createButton("Cancel", event-> new WarningWindow("Are you sure you want to exit?", subEvent-> dispose(), null), true);

        mainPanel.add(hostLabel);
        mainPanel.add(hostTextField);
        mainPanel.add(portLabel);
        mainPanel.add(portTextField);
        mainPanel.add(databaseNameLabel);
        mainPanel.add(databaseNameField);
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(createLabel(null));
        mainPanel.add(hidePasswordCheckBox);
        mainPanel.add(connectButton);
        mainPanel.add(cancelButton);

        add(mainPanel);
    }

    @Override
    public JTextField createTextField(String text) {
        Color textFieldColor = new Color(105,105,105);
        JTextField textField = new JTextField(text);
        textField.setBackground(textFieldColor);
        textField.setForeground(Color.WHITE);
        return textField;
    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }

    public void connect(String host, String databaseName, String port, String username, String password){

        try {
            DatabaseFacade databaseFacade = new DatabaseFacade(host, port, databaseName, username, password);
            databaseFacade.importDatabase();
            new MainWindow(databaseFacade);
            if(startingWindow!=null)
                startingWindow.dispose();
            dispose();

        } catch (SQLException ignored) {
            new WarningWindow("Connecting failed. Do you want to try again?", event ->
                connect(host, databaseName, port, username, password), null);

        }
    }
}
