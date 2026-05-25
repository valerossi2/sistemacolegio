package com.edugrade.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DetalleCursoController {

    @FXML private Label breadcrumbCursos;
    @FXML private Label breadcrumbCursoActual;
    @FXML private Label pageTitle;
    @FXML private Button btnPrint;
    @FXML private Button btnEditCourse;
    @FXML private GridPane bentoGrid;
    @FXML private VBox courseOverviewCard;
    @FXML private Label badgeNivel;
    @FXML private Label lblTutorPrincipal;
    @FXML private Label totalStudents;
    @FXML private Label totalTeachers;
    @FXML private Label assignedRoom;
    @FXML private Button btnVerTodosDocentes;
    @FXML private TableView<?> studentsTable;
    @FXML private TableColumn<?, ?> colStudent;
    @FXML private TableColumn<?, ?> colMatricula;
    @FXML private TableColumn<?, ?> colAsistencia;
    @FXML private TableColumn<?, ?> colAcciones;
    @FXML private Button btnLoadMore;
    @FXML private Label studentsSubtitle;
    @FXML private ToggleButton btnViewTable;
    @FXML private ToggleButton btnViewGrid;

    @FXML
    private void initialize() {
    }

    @FXML
    private void onBreadcrumbCursos(MouseEvent event) {
    }

    @FXML
    private void onImprimirReporte(ActionEvent event) {
        showInfo("Reporte", "Impresion de reporte no implementada.");
    }

    @FXML
    private void onEditarCurso(ActionEvent event) {
        showInfo("Editar curso", "Edicion de curso no implementada.");
    }

    @FXML
    private void onVerTodosDocentes(ActionEvent event) {
        showInfo("Docentes", "Lista completa de docentes no implementada.");
    }

    @FXML
    private void onCargarMas(ActionEvent event) {
        showInfo("Estudiantes", "Carga de mas estudiantes no implementada.");
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
