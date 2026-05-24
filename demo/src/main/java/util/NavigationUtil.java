package util;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public final class NavigationUtil {

    private NavigationUtil() {}

    /**
     * Carga una vista FXML dentro del área de contenido del MainLayout.
     * Se usa para navegar entre vistas sin cambiar la ventana principal.
     */
    public static void cargarVista(StackPane contentArea, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    NavigationUtil.class.getResource(fxmlPath)
            );
            Parent vista = loader.load();
            contentArea.getChildren().setAll(vista);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la vista: " + fxmlPath, e);
        }
    }

    /**
     * Abre una vista FXML en una ventana nueva (diálogo modal).
     * Se usa para formularios de crear/editar y detalles.
     */
    public static void abrirDialogo(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    NavigationUtil.class.getResource(fxmlPath)
            );
            Parent vista = loader.load();

            Stage dialogo = new Stage();
            dialogo.setTitle(titulo);
            dialogo.setScene(new Scene(vista));
            dialogo.initModality(Modality.APPLICATION_MODAL);
            dialogo.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo abrir el diálogo: " + fxmlPath, e);
        }
    }

    /**
     * Abre un diálogo modal y retorna el controlador.
     * Útil cuando necesitas pasar datos al formulario antes de abrirlo.
     */
    public static <T> T abrirDialogoConControlador(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    NavigationUtil.class.getResource(fxmlPath)
            );
            Parent vista = loader.load();

            Stage dialogo = new Stage();
            dialogo.setTitle(titulo);
            dialogo.setScene(new Scene(vista));
            dialogo.initModality(Modality.APPLICATION_MODAL);
            dialogo.showAndWait();

            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo abrir el diálogo: " + fxmlPath, e);
        }
    }
}
