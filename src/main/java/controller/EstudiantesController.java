package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import util.LanguageManager;

public class EstudiantesController {

    private LanguageManager lang = LanguageManager.getInstance();

    @FXML private Button btnPrint;

    @FXML
    private void initialize() {
        lang.addListener(this::onLanguageChanged);
    }

    private void onLanguageChanged() {}

    @FXML
    private void onImprimirReporte() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("report.title", "Reporte"));
        alert.setHeaderText(null);
        alert.setContentText(lang.get("report.msg", "Impresión de reporte no implementada."));
        alert.showAndWait();
    }
}
