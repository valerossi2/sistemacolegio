package com.edugrade.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import theme.ThemeManager;
import util.LanguageManager;

public class DetalleCursoController {

    private LanguageManager lang;
    private ThemeManager theme;
    private boolean showingAllTeachers = false;

    private final ObservableList<TeacherRow> fullTeacherData = FXCollections.observableArrayList();
    private final ObservableList<TeacherRow> displayedTeacherData = FXCollections.observableArrayList();

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
    @FXML private TableView<TeacherRow> teacherTable;
    @FXML private TableColumn<TeacherRow, String> colTeacherName;
    @FXML private TableColumn<TeacherRow, String> colTeacherSubject;
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
        initTeacherTable();
        initTeacherData();

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);
    }

    private void initTeacherTable() {
        colTeacherName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTeacherSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
    }

    private void initTeacherData() {
        fullTeacherData.setAll(
            new TeacherRow("Carlos Ruiz", "Matemáticas Avanzadas"),
            new TeacherRow("Elena Torres", "Física y Química"),
            new TeacherRow("Miguel Ángel Soto", "Literatura Hispanoam."),
            new TeacherRow("Ana Silva", "Historia Universal"),
            new TeacherRow("Roberto Sánchez", "Programación I"),
            new TeacherRow("Laura Méndez", "Biología Molecular"),
            new TeacherRow("Pedro García", "Inglés Avanzado"),
            new TeacherRow("Sofía Valdez", "Arte y Diseño"),
            new TeacherRow("Diego Ramírez", "Educación Física"),
            new TeacherRow("Carmen Vega", "Filosofía"),
            new TeacherRow("Luis Torres", "Química Orgánica"),
            new TeacherRow("María Paz", "Geografía Mundial")
        );
        displayedTeacherData.addAll(fullTeacherData.subList(0, 4));
        teacherTable.setItems(displayedTeacherData);
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
        btnVerTodosDocentes.setText(lang.get(
            showingAllTeachers ? "detalle.verMenos" : "detalle.verTodos"
        ));
        btnLoadMore.setText(lang.get("detalle.cargarMas", "Cargar más estudiantes"));
        studentsSubtitle.setText(lang.get("detalle.studentsSubtitle", "32 alumnos inscritos en el ciclo actual"));
        btnViewTable.setText(lang.get("detalle.viewTable", "Tabla"));
        btnViewGrid.setText(lang.get("detalle.viewGrid", "Cuadrícula"));
        colTeacherName.setText(lang.get("detalle.colDocente", "DOCENTE"));
        colTeacherSubject.setText(lang.get("detalle.colMateria", "MATERIA"));
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
        showingAllTeachers = !showingAllTeachers;
        displayedTeacherData.setAll(
            showingAllTeachers ? fullTeacherData : fullTeacherData.subList(0, 4)
        );
        btnVerTodosDocentes.setText(lang.get(
            showingAllTeachers ? "detalle.verMenos" : "detalle.verTodos"
        ));
    }

    @FXML
    private void onCargarMas(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("detalle.estudiantesTitle", "Estudiantes"));
        alert.setHeaderText(null);
        alert.setContentText(lang.get("detalle.estudiantesMsg", "Carga de más estudiantes no implementada."));
        alert.showAndWait();
    }

    public static class TeacherRow {
        private final StringProperty name;
        private final StringProperty subject;

        public TeacherRow(String name, String subject) {
            this.name = new SimpleStringProperty(name);
            this.subject = new SimpleStringProperty(subject);
        }

        public String getName() { return name.get(); }
        public void setName(String n) { name.set(n); }
        public StringProperty nameProperty() { return name; }

        public String getSubject() { return subject.get(); }
        public void setSubject(String s) { subject.set(s); }
        public StringProperty subjectProperty() { return subject; }
    }
}
