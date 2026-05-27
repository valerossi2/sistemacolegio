package controller;

import config.Hibernate_config;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Usuario;
import org.hibernate.Session;
import security.PasswordService;
import theme.ThemeManager;
import java.util.prefs.Preferences;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisible;
    @FXML private CheckBox rememberCheck;
    @FXML private Label errorLabel;
    @FXML private ImageView heroImage;

    private Stage stage;
    private Preferences prefs = Preferences.userNodeForPackage(controller.Configuracion.class);

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Por favor ingresa tu correo y contraseña.");
            return;
        }

        try (Session session = Hibernate_config.getSessionFactory().openSession()) {
            Usuario user = session.createQuery(
                    "from Usuario where email = :email", Usuario.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (user == null) {
                showError("Correo electrónico no registrado.");
                return;
            }

            if (!user.getActivo()) {
                showError("Cuenta desactivada. Contacta al administrador.");
                return;
            }

            String storedHash = user.getPasswordHash();
            if (storedHash == null || storedHash.isBlank()) {
                showError("Esta cuenta no tiene contraseña configurada. Usa Google para iniciar sesión.");
                return;
            }

            if (!PasswordService.verify(password, storedHash)) {
                showError("Contraseña incorrecta.");
                return;
            }

            hideError();

            // ── Store user in preferences for MainController ──
            prefs.put("userFirstName", user.getNombre());
            prefs.put("userLastName", user.getApellido());
            prefs.put("userRole", user.getRol().getNombre());
            prefs.putInt("userId", user.getId());

            // ── Navigate to dashboard ──
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_view.fxml"));
            Parent root = loader.load();
            MainController mainCtrl = loader.getController();
            mainCtrl.setStage(stage);
            mainCtrl.setupEverything();

            Scene scene = new Scene(root);
            ThemeManager theme = ThemeManager.getInstance();
            scene.getStylesheets().add(theme.isDark()
                ? getClass().getResource("/css/dark.css").toExternalForm()
                : getClass().getResource("/css/light.css").toExternalForm());

            theme.addListener(() -> {
                scene.getStylesheets().clear();
                scene.getStylesheets().add(theme.isDark()
                    ? getClass().getResource("/css/dark.css").toExternalForm()
                    : getClass().getResource("/css/light.css").toExternalForm());
            });

            stage.setMinWidth(700);
            stage.setMinHeight(450);
            stage.setScene(scene);
            stage.setTitle("Lumina Academy - Admin Portal");

        } catch (Exception e) {
            showError("Error de conexión. Intenta de nuevo.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registro.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 820, 620);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/base.css").toExternalForm());
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onForgotPassword() {
        showError("Función no disponible aún. Contacta al administrador.");
    }

    @FXML
    private void onGoogle() {
        showError("Inicio de sesión con Google no configurado.");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}
