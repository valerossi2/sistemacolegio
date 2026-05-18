import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

    public class SISApplication extends Application {

        @Override
        public void start(Stage stage) {

            // Prueba simple para verificar que JavaFX funciona
            Label label = new Label("✅ SIS Colegio funcionando!");
            label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            StackPane root = new StackPane(label);
            root.setStyle("-fx-background-color: #1a3a5c;");
            label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

            Scene scene = new Scene(root, 800, 500);

            stage.setTitle("SIS Colegio");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }

