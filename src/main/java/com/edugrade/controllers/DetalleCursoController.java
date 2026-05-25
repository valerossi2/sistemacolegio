package com.edugrade.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import theme.ThemeManager;
import util.LanguageManager;

public class DetalleCursoController {

    private LanguageManager lang;
    private ThemeManager theme;

    @FXML private VBox root;
    @FXML private VBox courseOverviewCard;
    @FXML private Label breadcrumbCursos;
    @FXML private Label breadcrumbCursoActual;
    @FXML private Label pageTitle;
    @FXML private Button btnPrint;
    @FXML private Button btnEditCourse;
    @FXML private GridPane bentoGrid;
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
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        loadStylesheets();
        updateTexts();
        applyTheme();

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);
    }

    private void loadStylesheets() {
        var baseUrl = getClass().getResource("/css/base.css");
        var detalleUrl = getClass().getResource("/css/DetallesCurso.css");
        if (baseUrl != null) root.getStylesheets().add(baseUrl.toExternalForm());
        if (detalleUrl != null) root.getStylesheets().add(detalleUrl.toExternalForm());
    }

    private void onLanguageChanged() {
        Platform.runLater(this::updateTexts);
    }

    private void onThemeChanged() {
        Platform.runLater(this::applyTheme);
    }

    private void applyTheme() {
        boolean dark = theme.isDark();
        root.getStyleClass().removeAll("root-dark");
        courseOverviewCard.getStyleClass().removeAll("cursos-card-dark");
        if (dark) {
            root.getStyleClass().add("root-dark");
            courseOverviewCard.getStyleClass().add("cursos-card-dark");
        }
    }

    private void updateTexts() {
        pageTitle.setText(lang.get("detalle.pageTitle", "Detalles del Curso"));
        breadcrumbCursos.setText(lang.get("detalle.breadcrumbCursos", "Cursos"));
        btnPrint.setText(lang.get("detalle.btnPrint", "Imprimir Reporte"));
        btnEditCourse.setText(lang.get("detalle.btnEdit", "Editar Curso"));
        badgeNivel.setText(lang.get("detalle.badgeNivel", "SECUNDARIA"));
        lblTutorPrincipal.setText(lang.get("detalle.tutorPrincipal", "Prof. Laura Méndez"));
        btnVerTodosDocentes.setText(lang.get("detalle.verTodos", "Ver Todos"));
        btnLoadMore.setText(lang.get("detalle.cargarMas", "Cargar más estudiantes"));
        studentsSubtitle.setText(lang.get("detalle.studentsSubtitle", "32 alumnos inscritos en el ciclo actual"));
        btnViewTable.setText(lang.get("detalle.viewTable", "Tabla"));
        btnViewGrid.setText(lang.get("detalle.viewGrid", "Cuadrícula"));
    }

    @FXML
    private void onBreadcrumbCursos(MouseEvent event) {
    }

    @FXML
    private void onImprimirReporte(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("detalle.reporteTitle", "Reporte"));
        alert.setHeaderText(null);
        alert.setContentText(lang.get("detalle.reporteMsg", "Impresión de reporte no implementada."));
        alert.showAndWait();
    }

    @FXML
    private void onEditarCurso(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("detalle.editarTitle", "Editar curso"));
        alert.setHeaderText(null);
        alert.setContentText(lang.get("detalle.editarMsg", "Edición de curso no implementada."));
        alert.showAndWait();
    }

    @FXML
    private void onVerTodosDocentes(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("detalle.docentesTitle", "Docentes"));
        alert.setHeaderText(null);
        alert.setContentText(lang.get("detalle.docentesMsg", "Lista completa de docentes no implementada."));
        alert.showAndWait();
    }

    @FXML
    private void onCargarMas(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("detalle.estudiantesTitle", "Estudiantes"));
        alert.setHeaderText(null);
        alert.setContentText(lang.get("detalle.estudiantesMsg", "Carga de más estudiantes no implementada."));
        alert.showAndWait();
    }
}
