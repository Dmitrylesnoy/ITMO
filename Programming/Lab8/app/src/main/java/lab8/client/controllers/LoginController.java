package lab8.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lab8.client.utils.Handler;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Label statusLabel;

    private String username;
    private String password;
    private boolean loginStatus = false;

    @FXML
    private void loginClick(ActionEvent event) {
        username = loginField.getText();
        password = passwordField.getText();
        if (username==null || password==null || username.isBlank() || password.isBlank()) {
            setStatusText("Login or password cannot be empty");
            return;
        }
        setStatusText("Trying login...");

        System.out.println("Login: " + username);
        System.out.println("Password: " + password);
        if (Handler.tryLogin(username, password)) {
            // nextWindow();
            setStatusText("Logged in!");
            ((Stage) loginBtn.getScene().getWindow()).close();
            ToolbarController.getInstance().setUser(username);
            ToolbarController.getInstance().openTerminal(event);
            loginStatus = true;
            return;
        } else
            setStatusText("Wrong login or password");
        // nextWindow();
    }



    public static Stage open() {
        return ToolbarController.getInstance().openWindow("/fxml/login.fxml","login",900,700);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public void setStatusText(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }

}
