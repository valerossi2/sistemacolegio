import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisible;

    @FXML
    private CheckBox rememberCheck;

    @FXML
    private ImageView heroImage;

    @FXML
    private void initialize() {
        passwordVisible.textProperty().bindBidirectional(passwordField.textProperty());
    }

    @FXML
    private void togglePassword(MouseEvent event) {
        boolean showPassword = !passwordVisible.isVisible();

        passwordVisible.setVisible(showPassword);
        passwordVisible.setManaged(showPassword);
        passwordField.setVisible(!showPassword);
        passwordField.setManaged(!showPassword);
    }

    @FXML
    private void onForgotPassword(ActionEvent event) {
        showInfo("Recuperar contrasena", "La recuperacion de contrasena todavia no esta implementada.");
    }

    @FXML
    private void onLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            showInfo("Campos requeridos", "Ingresa tu correo y contrasena.");
            return;
        }

        showInfo("Inicio de sesion", "La validacion de usuario todavia no esta implementada.");
    }

    @FXML
    private void goToRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Registro.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    private void onGoogle(ActionEvent event) {
        showInfo("Google", "El inicio de sesion con Google todavia no esta implementado.");
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
