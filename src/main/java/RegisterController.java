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

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisible;

    @FXML
    private PasswordField confirmField;

    @FXML
    private TextField confirmVisible;

    @FXML
    private CheckBox termsCheck;

    @FXML
    private ImageView heroImage;

    @FXML
    private void initialize() {
        passwordVisible.textProperty().bindBidirectional(passwordField.textProperty());
        confirmVisible.textProperty().bindBidirectional(confirmField.textProperty());
    }

    @FXML
    private void togglePassword(MouseEvent event) {
        toggleField(passwordField, passwordVisible);
    }

    @FXML
    private void toggleConfirm(MouseEvent event) {
        toggleField(confirmField, confirmVisible);
    }

    @FXML
    private void onTerms(ActionEvent event) {
        showInfo("Terminos y condiciones", "Los terminos y condiciones todavia no estan implementados.");
    }

    @FXML
    private void onRegister(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmField.getText();

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()
                || confirmPassword == null || confirmPassword.isBlank()) {
            showInfo("Campos requeridos", "Completa todos los campos.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showInfo("Contrasenas distintas", "La contrasena y la confirmacion no coinciden.");
            return;
        }

        if (!termsCheck.isSelected()) {
            showInfo("Terminos requeridos", "Debes aceptar los terminos y condiciones.");
            return;
        }

        showInfo("Registro", "El registro de usuario todavia no esta implementado.");
    }

    @FXML
    private void goToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    private void onGoogle(ActionEvent event) {
        showInfo("Google", "El registro con Google todavia no esta implementado.");
    }

    private void toggleField(PasswordField hiddenField, TextField visibleField) {
        boolean showPassword = !visibleField.isVisible();

        visibleField.setVisible(showPassword);
        visibleField.setManaged(showPassword);
        hiddenField.setVisible(!showPassword);
        hiddenField.setManaged(!showPassword);
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
