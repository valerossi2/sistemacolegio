package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisible;
    @FXML private PasswordField confirmField;
    @FXML private TextField confirmVisible;
    @FXML private CheckBox termsCheck;
    @FXML private ImageView heroImage;

    @FXML
    public void initialize() {
    }

    @FXML
    private void togglePassword() {
        if (passwordField.isVisible()) {
            passwordVisible.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);
        } else {
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
        }
    }

    @FXML
    private void toggleConfirm() {
        if (confirmField.isVisible()) {
            confirmVisible.setText(confirmField.getText());
            confirmField.setVisible(false);
            confirmField.setManaged(false);
            confirmVisible.setVisible(true);
            confirmVisible.setManaged(true);
        } else {
            confirmField.setText(confirmVisible.getText());
            confirmField.setVisible(true);
            confirmField.setManaged(true);
            confirmVisible.setVisible(false);
            confirmVisible.setManaged(false);
        }
    }

    @FXML
    private void onRegister() {
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
            Scene scene = new Scene(root, 820, 560);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onTerms() {
    }

    @FXML
    private void onGoogle() {
    }
}
