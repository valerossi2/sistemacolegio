package com.edugrade.controllers;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;
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
    @FXML private Button btnCalificaciones;
    @FXML private GridPane bentoGrid;
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
    @FXML private Label lblPromedioGeneral;
    @FXML private Label lblAprobadosReprobados;
    @FXML private Label lblStatsTitle;
    @FXML private Label lblTotalStudentsLabel;
    @FXML private Label lblTotalTeachersLabel;
    @FXML private Label lblPromedioGeneralLabel;
    @FXML private Label lblAprobadosReprobadosLabel;
    @FXML private Label lblPromedioMateriaLabel;
    @FXML private Label lblDistribucionGeneroLabel;
    @FXML private Label lblEquipoDocenteCard;
    @FXML private Label lblListaEstudiantesCard;
    @FXML private Label lblPromedioMateria;
    @FXML private PieChart genderChart;

    private CursoController.CourseRow currentCourse;
    private List<CursoController.CourseRow> allCourses;
    private Consumer<CursoController.CourseRow> navigateToCourse;
    private Runnable onBackToList;

    public void setCourse(CursoController.CourseRow course, List<CursoController.CourseRow> allCourses,
                          Consumer<CursoController.CourseRow> navigator, Runnable onBackToList) {
        this.currentCourse = course;
        this.allCourses = allCourses;
        this.navigateToCourse = navigator;
        this.onBackToList = onBackToList;
        String label = course.grado() + " " + course.seccion();
        breadcrumbCursoActual.setText(label);
        pageTitle.setText(lang.get("detalle.pageTitle", "Detalles del Curso") + ": " + label);
        loadRealData();
    }

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
        initStats();

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);
    }

    private static final String[] AVATAR_COLORS = {
        "#3B82F6", "#8B5CF6", "#EC4899", "#F59E0B", "#10B981", "#EF4444", "#6366F1", "#14B8A6"
    };
    private static final double TEACHER_ROW_HEIGHT = 48;
    private static final double TEACHER_HEADER_HEIGHT = 32;
    private static final int INITIAL_TEACHER_COUNT = 4;
    private static final int TOTAL_TEACHER_COUNT = 12;
    private static final double STUDENT_ROW_HEIGHT = 48;
    private static final double STUDENT_HEADER_HEIGHT = 32;
    private static final int INITIAL_STUDENT_COUNT = 5;
    private static final int TOTAL_STUDENT_COUNT = 10;

    private void initTeacherTable() {
        colTeacherName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().nombre()));
        colTeacherName.setCellFactory(nombreCell());
        colTeacherSubject.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().materia()));
    }

    private Callback<TableColumn<TeacherRow, String>, TableCell<TeacherRow, String>> nombreCell() {
        return col -> new TableCell<>() {
            private final HBox box = new HBox(12);
            private final StackPane avatarContainer = new StackPane();
            private final Circle avatarCircle = new Circle(16);
            private final Text initials = new Text();
            private final Label nameLabel = new Label();
            {
                avatarCircle.getStyleClass().add("avatar-circle");
                initials.getStyleClass().add("avatar-initials");
                avatarContainer.getChildren().addAll(avatarCircle, initials);
                nameLabel.getStyleClass().add("cell-docente-name");
                box.getChildren().addAll(avatarContainer, nameLabel);
                setGraphic(box);
                setPadding(new Insets(8, 16, 8, 16));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                var tv = getTableView();
                if (empty || tv == null || getIndex() < 0 || getIndex() >= tv.getItems().size()) {
                    nameLabel.setText(null);
                    initials.setText(null);
                } else {
                    TeacherRow row = tv.getItems().get(getIndex());
                    nameLabel.setText(row.nombre());
                    String initial = row.nombre().substring(0, 1).toUpperCase();
                    initials.setText(initial);
                    avatarCircle.setFill(Color.web(AVATAR_COLORS[row.avatarIdx() % AVATAR_COLORS.length]));
                }
            }
        };
    }

    private void initTeacherData() {
        fullTeacherData.setAll(
            new TeacherRow("Prof. Laura Méndez", "laura.mendez@edu.com", "Matemáticas", "5to E", "Activo", 0),
            new TeacherRow("Prof. Carlos Ruiz", "carlos.ruiz@edu.com", "Historia", "4to A", "Activo", 1),
            new TeacherRow("Prof. Elena Torres", "elena.torres@edu.com", "Lenguaje", "3ro B", "Activo", 2),
            new TeacherRow("Prof. Ana Silva", "ana.silva@edu.com", "Ciencias", "2do C", "Activo", 3),
            new TeacherRow("Prof. Miguel Soto", "miguel.soto@edu.com", "Inglés", "1ro A", "Inactivo", 4),
            new TeacherRow("Prof. Diana Ríos", "diana.rios@edu.com", "Arte", "5to B", "Activo", 5),
            new TeacherRow("Prof. Pedro Lima", "pedro.lima@edu.com", "Educación Física", "4to B", "Activo", 6),
            new TeacherRow("Prof. Sofía Vega", "sofia.vega@edu.com", "Música", "3ro A", "Activo", 7),
            new TeacherRow("Prof. Luis Paz", "luis.paz@edu.com", "Filosofía", "6to A", "Inactivo", 0),
            new TeacherRow("Prof. Carmen Rojas", "carmen.rojas@edu.com", "Biología", "5to C", "Activo", 1),
            new TeacherRow("Prof. Andrés Cruz", "andres.cruz@edu.com", "Química", "4to C", "Activo", 2),
            new TeacherRow("Prof. Valeria Solís", "valeria.solis@edu.com", "Historia del Arte", "6to B", "Activo", 3)
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
            private final Label lbl = new Label();
            {
                lbl.getStyleClass().add("attendance-btn");
            }

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    lbl.getStyleClass().removeAll(
                        "attendance-btn--presente", "attendance-btn--ausente", "attendance-btn--excusa"
                    );
                    switch (status) {
                        case "presente" -> {
                            lbl.setText("Presente");
                            lbl.getStyleClass().add("attendance-btn--presente");
                        }
                        case "ausente" -> {
                            lbl.setText("Ausente");
                            lbl.getStyleClass().add("attendance-btn--ausente");
                        }
                        case "excusa" -> {
                            lbl.setText("Excusa");
                            lbl.getStyleClass().add("attendance-btn--excusa");
                        }
                    }
                    setGraphic(lbl);
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

    private void loadRealData() {
        if (currentCourse == null) return;
        var rng = ThreadLocalRandom.current();

        // ── Students ──
        fullStudentData.clear();
        displayedStudentData.clear();
        String[] firstNames = {"Liam","Emma","Noah","Olivia","Mateo","Isabella","Santiago","Sophia",
            "Lucas","Mía","Benjamín","Valentina","Sebastián","Camila","Daniel","Gabriela"};
        String[] lastNames = {"Castillo","Rodríguez","García","Martínez","Hernández","López","Pérez",
            "González","Fernández","Torres","Ramírez","Morales","Ortiz","Cruz","Reyes","Vargas"};
        String[] statuses = {"presente","ausente","excusa"};
        int studentCount = currentCourse.alumnos();
        for (int i = 0; i < studentCount; i++) {
            String name = firstNames[rng.nextInt(firstNames.length)] + " " + lastNames[rng.nextInt(lastNames.length)];
            String mat = String.format("MAT-%03d", rng.nextInt(1, 999));
            String status = statuses[rng.nextInt(statuses.length)];
            fullStudentData.add(new StudentRow(name, mat, status));
        }
        int shownStudents = Math.min(INITIAL_STUDENT_COUNT, fullStudentData.size());
        displayedStudentData.addAll(fullStudentData.subList(0, shownStudents));
        studentsTable.setItems(displayedStudentData);
        updateStudentTableHeight(shownStudents);
        totalStudents.setText(String.valueOf(fullStudentData.size()));

        // ── Teachers ──
        fullTeacherData.clear();
        displayedTeacherData.clear();
        String[] teacherNames = {"Prof. Laura Méndez","Prof. Carlos Ruiz","Prof. Elena Torres",
            "Prof. Ana Silva","Prof. Miguel Soto","Prof. Diana Ríos","Prof. Pedro Lima",
            "Prof. Sofía Vega","Prof. Luis Paz"};
        String[] subjects = {"Matemáticas","Historia","Lenguaje","Ciencias","Inglés","Arte",
            "Educación Física","Música","Filosofía","Biología","Química"};
        int teacherCount = currentCourse.profesores();
        for (int i = 0; i < teacherCount; i++) {
            String subj = subjects[rng.nextInt(subjects.length)];
            String estado = rng.nextBoolean() ? "Activo" : "Inactivo";
            fullTeacherData.add(new TeacherRow(teacherNames[i % teacherNames.length],
                teacherNames[i % teacherNames.length].toLowerCase().replace(" ",".").replace("á","a").replace("é","e") + "@edu.com",
                subj, currentCourse.seccion(), estado, i));
        }
        int shownTeachers = Math.min(INITIAL_TEACHER_COUNT, fullTeacherData.size());
        displayedTeacherData.addAll(fullTeacherData.subList(0, shownTeachers));
        teacherTable.setItems(displayedTeacherData);
        updateTeacherTableHeight(shownTeachers);
        totalTeachers.setText(String.valueOf(fullTeacherData.size()));

        // ── Gender distribution ──
        int masc = rng.nextInt(studentCount + 1);
        int fem = studentCount - masc;
        genderChart.setData(FXCollections.observableArrayList(
            new PieChart.Data("Masculino", masc),
            new PieChart.Data("Femenino", fem)
        ));
        initGenderChartInteraction();

        // ── Stats ──
        double avg = currentCourse.rendimiento();
        int aprobados = (int)(studentCount * (avg / 10.0 * 0.8));
        int reprobados = studentCount - aprobados;
        lblPromedioGeneral.setText(String.format("%.1f", avg));
        lblAprobadosReprobados.setText(aprobados + " / " + reprobados);
    }

    private void initStats() {
        lblPromedioGeneral.setText("85.4");

        lblAprobadosReprobados.setText("28 / 4");

        lblPromedioMateria.setText("Matemáticas: 72.3 (La más baja)");

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Masculino", 18),
            new PieChart.Data("Femenino", 12)
        );
        genderChart.setData(pieData);
        initGenderChartInteraction();
    }

    private static final String GRAY_PIE = "#CBD5E1";

    private void initGenderChartInteraction() {
        genderChart.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                genderChart.applyCss();
                genderChart.layout();
                Platform.runLater(this::attachPieHandlers);
            }
        });
    }

    private void attachPieHandlers() {
        for (PieChart.Data data : genderChart.getData()) {
            Node node = data.getNode();
            if (node != null) {
                String label = switch (data.getName()) {
                    case "Masculino" -> "varones";
                    case "Femenino"  -> "mujeres";
                    default          -> "otros";
                };
                int count = (int) data.getPieValue();
                Tooltip tip = new Tooltip(count + " " + label);
                tip.getStyleClass().add("pie-tooltip");
                Tooltip.install(node, tip);

                node.setOnMouseEntered(e -> {
                    for (PieChart.Data other : genderChart.getData()) {
                        Node otherNode = other.getNode();
                        if (other != data && otherNode != null) {
                            otherNode.setStyle("-fx-pie-color: " + GRAY_PIE + ";");
                        }
                    }
                });
                node.setOnMouseExited(e -> {
                    for (PieChart.Data other : genderChart.getData()) {
                        Node otherNode = other.getNode();
                        if (otherNode != null) {
                            otherNode.setStyle("");
                        }
                    }
                });
            }
        }
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
        if (btnCalificaciones != null) btnCalificaciones.setText(lang.get("detalle.btnCalificaciones", "Calificaciones"));
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
        if (lblStatsTitle != null) lblStatsTitle.setText(lang.get("detalle.statsTitle", "Estad\u00edsticas y Rendimiento"));
        if (lblTotalStudentsLabel != null) lblTotalStudentsLabel.setText(lang.get("detalle.totalStudentsLabel", "Total Estudiantes"));
        if (lblTotalTeachersLabel != null) lblTotalTeachersLabel.setText(lang.get("detalle.totalTeachersLabel", "Total Profesores"));
        if (lblPromedioGeneralLabel != null) lblPromedioGeneralLabel.setText(lang.get("detalle.promedioGeneralLabel", "Promedio General"));
        if (lblAprobadosReprobadosLabel != null) lblAprobadosReprobadosLabel.setText(lang.get("detalle.aprobadosReprobadosLabel", "Aprobados / Reprobados"));
        if (lblPromedioMateriaLabel != null) lblPromedioMateriaLabel.setText(lang.get("detalle.promedioMateriaLabel", "Promedio por Materia"));
        if (lblDistribucionGeneroLabel != null) lblDistribucionGeneroLabel.setText(lang.get("detalle.distribucionGeneroLabel", "Distribuci\u00f3n por G\u00e9nero"));
        if (lblEquipoDocenteCard != null) lblEquipoDocenteCard.setText(lang.get("detalle.equipoDocenteCard", "Equipo Docente"));
        if (lblListaEstudiantesCard != null) lblListaEstudiantesCard.setText(lang.get("detalle.listaEstudiantesCard", "Lista de Estudiantes"));
    }

    @FXML
    private void onBreadcrumbCursos(MouseEvent event) {
        if (onBackToList != null)
            onBackToList.run();
    }

    @FXML
    private void onBreadcrumbCursoActual(MouseEvent event) {
        if (allCourses != null && !allCourses.isEmpty())
            showCourseMenu(event);
    }

    private void showCourseMenu(MouseEvent event) {
        ContextMenu menu = new ContextMenu();
        for (CursoController.CourseRow c : allCourses) {
            String name = c.grado() + " " + c.seccion();
            MenuItem item = new MenuItem(name);
            if (currentCourse != null && name.equals(currentCourse.grado() + " " + currentCourse.seccion()))
                item.setDisable(true);
            item.setOnAction(e -> {
                if (navigateToCourse != null)
                    navigateToCourse.accept(c);
            });
            menu.getItems().add(item);
        }
        menu.show(((Node) event.getSource()), Side.BOTTOM, 0, 0);
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
    private void onCalificaciones(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("detalle.calificacionesTitle", "Calificaciones"));
        alert.setHeaderText(null);
        alert.setContentText(lang.get("detalle.calificacionesMsg", "Gestión de calificaciones no implementada."));
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
        int total = fullTeacherData.size();
        int count = showingAllTeachers ? total : Math.min(INITIAL_TEACHER_COUNT, total);
        displayedTeacherData.setAll(
            showingAllTeachers ? fullTeacherData : fullTeacherData.subList(0, count)
        );
        updateTeacherTableHeight(count);
        btnVerTodosDocentes.setText(lang.get(
            showingAllTeachers ? "detalle.verMenos" : "detalle.verTodos"
        ));
    }

    @FXML
    private void onCargarMas(ActionEvent event) {
        showingAllStudents = !showingAllStudents;
        int total = fullStudentData.size();
        int count = showingAllStudents ? total : Math.min(INITIAL_STUDENT_COUNT, total);
        displayedStudentData.setAll(
            showingAllStudents ? fullStudentData : fullStudentData.subList(0, count)
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

    public record TeacherRow(String nombre, String email, String materia, String seccion, String estado, int avatarIdx) {}
}
