package ui;

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

    private ThemeManager theme = new ThemeManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_view.fxml"));
            Parent rootNode = loader.load();
            MainController controller = loader.getController();
            controller.setThemeManager(theme);
            controller.setStage(primaryStage);
            controller.setupEverything();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double width = Math.min(1050, screenBounds.getWidth() * 0.7);
            double height = Math.min(700, screenBounds.getHeight() * 0.7);
            Scene scene = new Scene(rootNode, width, height);

            primaryStage.setMinWidth(700);
            primaryStage.setMinHeight(450);

            theme.addListener(() -> {
                scene.getStylesheets().clear();
                scene.getStylesheets().add(theme.isDark()
                    ? getClass().getResource("/css/dark.css").toExternalForm()
                    : getClass().getResource("/css/light.css").toExternalForm());
            });

            scene.getStylesheets().add(theme.isDark()
                ? getClass().getResource("/css/dark.css").toExternalForm()
                : getClass().getResource("/css/light.css").toExternalForm());

            primaryStage.setTitle("Lumina Academy - Admin Portal");
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
