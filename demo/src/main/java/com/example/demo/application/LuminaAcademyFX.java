package com.example.demo.application;

import com.example.demo.theme.ThemeManager;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/main_view.fxml"));
            Parent rootNode = loader.load();
            com.example.demo.controller.MainController controller = loader.getController();
            controller.setThemeManager(theme);
            controller.setStage(primaryStage);
            controller.setupEverything();

            primaryStage.initStyle(StageStyle.UNDECORATED);

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double width = Math.min(1400, screenBounds.getWidth());
            double height = Math.min(900, screenBounds.getHeight());
            Scene scene = new Scene(rootNode, width, height);

            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(700);

            theme.addListener(() -> {
                scene.getStylesheets().clear();
                scene.getStylesheets().add(theme.isDark()
                    ? getClass().getResource("/com/example/demo/styles/dark.css").toExternalForm()
                    : getClass().getResource("/com/example/demo/styles/light.css").toExternalForm());
            });

            scene.getStylesheets().add(theme.isDark()
                ? getClass().getResource("/com/example/demo/styles/dark.css").toExternalForm()
                : getClass().getResource("/com/example/demo/styles/light.css").toExternalForm());

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
