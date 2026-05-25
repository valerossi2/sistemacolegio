package com.edugrade.controllers;

import java.util.concurrent.ThreadLocalRandom;

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
    private boolean showingAllStudents = false;

    private final ObservableList<TeacherRow> fullTeacherData = FXCollections.observableArrayList();
    private final ObservableList<TeacherRow> displayedTeacherData = FXCollections.observableArrayList();
    private final ObservableList<StudentRow> fullStudentData = FXCollections.observableArrayList();
    private final ObservableList<StudentRow> displayedStudentData = FXCollections.observableArrayList();

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
    @FXML private Button btnVerTodosDocentes;
    @FXML private TableView<TeacherRow> teacherTable;
    @FXML private TableColumn<TeacherRow, String> colTeacherName;
    @FXML private TableColumn<TeacherRow, String> colTeacherSubject;
    @FXML private TableView<StudentRow> studentsTable;
    @FXML private TableColumn<StudentRow, String> colStudent;
    @FXML private TableColumn<StudentRow, String> colMatricula;
    @FXML private TableColumn<StudentRow, String> colAsistencia;
    @FXML private Button btnLoadMore;

    @FXML
    private void initialize() {
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        loadStylesheets();
        updateTexts();
        applyTheme();
        initTeacherTable();
        initTeacherData();
        initStudentTable();
        initStudentData();

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);
    }

    private static final double TEACHER_ROW_HEIGHT = 48;
    private static final double TEACHER_HEADER_HEIGHT = 32;
    private static final int INITIAL_TEACHER_COUNT = 4;
    private static final int TOTAL_TEACHER_COUNT = 12;
    private static final double STUDENT_ROW_HEIGHT = 48;
    private static final double STUDENT_HEADER_HEIGHT = 32;
    private static final int INITIAL_STUDENT_COUNT = 5;
    private static final int TOTAL_STUDENT_COUNT = 10;

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
        displayedTeacherData.addAll(fullTeacherData.subList(0, INITIAL_TEACHER_COUNT));
        teacherTable.setItems(displayedTeacherData);
        updateTeacherTableHeight(INITIAL_TEACHER_COUNT);
    }

    private void initStudentTable() {
        colStudent.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colAsistencia.setCellValueFactory(new PropertyValueFactory<>("status"));
        colAsistencia.setCellFactory(col -> new TableCell<StudentRow, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Button btn = new Button();
                    switch (status) {
                        case "presente" -> {
                            btn.setText("Presente");
                            btn.setStyle("-fx-background-color: #22c55e; -fx-text-fill: #fff; " +
                                "-fx-background-radius: 6px; -fx-font-size: 12px; -fx-font-weight: 600; " +
                                "-fx-padding: 4 12; -fx-cursor: hand; -fx-border-width: 0;");
                        }
                        case "ausente" -> {
                            btn.setText("Ausente");
                            btn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: #fff; " +
                                "-fx-background-radius: 6px; -fx-font-size: 12px; -fx-font-weight: 600; " +
                                "-fx-padding: 4 12; -fx-cursor: hand; -fx-border-width: 0;");
                        }
                        case "excusa" -> {
                            btn.setText("Excusa");
                            btn.setStyle("-fx-background-color: #eab308; -fx-text-fill: #fff; " +
                                "-fx-background-radius: 6px; -fx-font-size: 12px; -fx-font-weight: 600; " +
                                "-fx-padding: 4 12; -fx-cursor: hand; -fx-border-width: 0;");
                        }
                    }
                    setGraphic(btn);
                }
            }
        });
    }

    private void initStudentData() {
        String[][] raw = {
            {"Liam Castillo",     "MAT-001"},
            {"Emma Rodríguez",    "MAT-002"},
            {"Noah García",       "MAT-003"},
            {"Olivia Martínez",   "MAT-004"},
            {"Mateo Hernández",   "MAT-005"},
            {"Isabella López",    "MAT-006"},
            {"Santiago Pérez",    "MAT-007"},
            {"Sophia González",   "MAT-008"},
            {"Lucas Fernández",   "MAT-009"},
            {"Mía Torres",        "MAT-010"},
        };
        String[] statuses = {"presente", "ausente", "excusa"};
        for (var r : raw) {
            String s = statuses[ThreadLocalRandom.current().nextInt(statuses.length)];
            fullStudentData.add(new StudentRow(r[0], r[1], s));
        }
        displayedStudentData.addAll(fullStudentData.subList(0, INITIAL_STUDENT_COUNT));
        studentsTable.setItems(displayedStudentData);
        updateStudentTableHeight(INITIAL_STUDENT_COUNT);
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
        btnLoadMore.setText(lang.get(
            showingAllStudents ? "detalle.verMenos" : "detalle.cargarMas"
        ));
        colTeacherName.setText(lang.get("detalle.colDocente", "DOCENTE"));
        colTeacherSubject.setText(lang.get("detalle.colMateria", "MATERIA"));
        colStudent.setText(lang.get("detalle.colStudent", "ESTUDIANTE"));
        colMatricula.setText(lang.get("detalle.colMatricula", "ID MATRÍCULA"));
        colAsistencia.setText(lang.get("detalle.colAsistencia", "ASISTENCIA"));
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

    private void updateTeacherTableHeight(int rowCount) {
        teacherTable.setPrefHeight(TEACHER_HEADER_HEIGHT + rowCount * TEACHER_ROW_HEIGHT + 2);
    }

    private void updateStudentTableHeight(int rowCount) {
        studentsTable.setPrefHeight(STUDENT_HEADER_HEIGHT + rowCount * STUDENT_ROW_HEIGHT + 2);
    }

    @FXML
    private void onVerTodosDocentes(ActionEvent event) {
        showingAllTeachers = !showingAllTeachers;
        int count = showingAllTeachers ? TOTAL_TEACHER_COUNT : INITIAL_TEACHER_COUNT;
        displayedTeacherData.setAll(
            showingAllTeachers ? fullTeacherData : fullTeacherData.subList(0, INITIAL_TEACHER_COUNT)
        );
        updateTeacherTableHeight(count);
        btnVerTodosDocentes.setText(lang.get(
            showingAllTeachers ? "detalle.verMenos" : "detalle.verTodos"
        ));
    }

    @FXML
    private void onCargarMas(ActionEvent event) {
        showingAllStudents = !showingAllStudents;
        int count = showingAllStudents ? TOTAL_STUDENT_COUNT : INITIAL_STUDENT_COUNT;
        displayedStudentData.setAll(
            showingAllStudents ? fullStudentData : fullStudentData.subList(0, INITIAL_STUDENT_COUNT)
        );
        updateStudentTableHeight(count);
        btnLoadMore.setText(lang.get(
            showingAllStudents ? "detalle.verMenos" : "detalle.cargarMas"
        ));
    }

    public static class StudentRow {
        private final StringProperty name;
        private final StringProperty matricula;
        private final StringProperty status;

        public StudentRow(String name, String matricula, String status) {
            this.name = new SimpleStringProperty(name);
            this.matricula = new SimpleStringProperty(matricula);
            this.status = new SimpleStringProperty(status);
        }

        public String getName() { return name.get(); }
        public void setName(String n) { name.set(n); }
        public StringProperty nameProperty() { return name; }

        public String getMatricula() { return matricula.get(); }
        public void setMatricula(String m) { matricula.set(m); }
        public StringProperty matriculaProperty() { return matricula; }

        public String getStatus() { return status.get(); }
        public void setStatus(String s) { status.set(s); }
        public StringProperty statusProperty() { return status; }
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
