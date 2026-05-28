package ui;

import config.Hibernate_config;
import controller.MainController;
import theme.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import java.io.IOException;

public class LuminaAcademyFX extends Application {

    private ThemeManager theme = ThemeManager.getInstance();

    @Override
    public void start(Stage primaryStage) {
        try { Hibernate_config.init(); } catch (Exception e) { System.err.println("Hibernate init falló: " + e.getMessage()); }

        primaryStage.initStyle(StageStyle.UNDECORATED);

        // Navigation helper to switch to main view after login
        java.util.function.Consumer<Stage> showMainView = (Stage st) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_view.fxml"));
                Parent rootNode = loader.load();
                MainController controller = loader.getController();
                controller.setStage(st);
                controller.setupEverything();

                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                double width = Math.min(1050, screenBounds.getWidth() * 0.7);
                double height = Math.min(700, screenBounds.getHeight() * 0.7);
                Scene scene = new Scene(rootNode, width, height);

                st.setMinWidth(700);
                st.setMinHeight(450);

                theme.addListener(() -> {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(theme.isDark()
                        ? getClass().getResource("/css/dark.css").toExternalForm()
                        : getClass().getResource("/css/light.css").toExternalForm());
                });

                scene.getStylesheets().add(theme.isDark()
                    ? getClass().getResource("/css/dark.css").toExternalForm()
                    : getClass().getResource("/css/light.css").toExternalForm());

                st.setTitle("Lumina Academy - Admin Portal");
                st.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent rootNode = loader.load();
            controller.LoginController loginCtrl = loader.getController();
            loginCtrl.setOnLoginSuccess(() -> showMainView.accept(primaryStage));

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double width = Math.min(820, screenBounds.getWidth());
            double height = Math.min(620, screenBounds.getHeight());
            Scene scene = new Scene(rootNode, width, height);

            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            primaryStage.setTitle("Lumina Academy - Iniciar Sesión");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
