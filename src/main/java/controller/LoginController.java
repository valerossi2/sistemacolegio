package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.AppSession;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisible;
    @FXML private CheckBox rememberCheck;
    @FXML private ImageView heroImage;

    private Runnable onLoginSuccess;

    public void setOnLoginSuccess(Runnable r) { this.onLoginSuccess = r; }

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
    private void onLogin() {
        String email = emailField.getText();
        if (email == null || email.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campo requerido");
            alert.setHeaderText(null);
            alert.setContentText("Por favor ingresa un correo electrónico.");
            alert.showAndWait();
            return;
        }
        AppSession.getInstance().login(email);
        if (onLoginSuccess != null) {
            onLoginSuccess.run();
        }
    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Registro.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(root, 820, 620);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onForgotPassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Recuperar Contraseña");
        dialog.setHeaderText("Ingresa tu correo electrónico");
        dialog.setContentText("Correo:");
        dialog.showAndWait().ifPresent(email -> {
            if (!email.isBlank()) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Correo Enviado");
                info.setHeaderText(null);
                info.setContentText("Se ha enviado un enlace de recuperación a:\n" + email);
                info.showAndWait();
            }
        });
    }

    @FXML
    private void onGoogle() {
    }
}
