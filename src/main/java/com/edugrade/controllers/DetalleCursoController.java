package com.edugrade.controllers;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.edugrade.controller.GradesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Callback;
import theme.ThemeManager;
import util.LanguageManager;

public class DetalleCursoController {

    private LanguageManager lang;
    private ThemeManager theme;
    private String currentCourseLabel = "";
    private boolean showingAllTeachers = false;
    private boolean showingAllStudents = false;
    private boolean editMode = false;
    private List<Node> savedCardChildren;

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
    @FXML private Button btnCancelEdit;
    @FXML private Button btnCalificaciones;
    @FXML private VBox editFormsPanel;
    @FXML private Label lblAddStudentTitle;
    @FXML private Label lblAddTeacherTitle;
    @FXML private TextField studentNameField;
    @FXML private TextField studentIdField;
    @FXML private Button addStudentBtn;
    @FXML private TextField teacherSearchField;
    @FXML private Button addTeacherBtn;
    @FXML private GridPane bentoGrid;
    @FXML private Label totalStudents;
    @FXML private Label totalTeachers;
    @FXML private Button btnVerTodosDocentes;
    @FXML private TableView<TeacherRow> teacherTable;
    @FXML private TableColumn<TeacherRow, String> colTeacherName;
    @FXML private TableColumn<TeacherRow, String> colTeacherSubject;
    @FXML private TableView<StudentRow> studentsTable;
    @FXML private TableColumn<StudentRow, String> colStudent;
    @FXML private TableColumn<StudentRow, String> colGenero;
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
    private final Popup teacherSuggestPopup = new Popup();
    private final Popup studentNamePopup = new Popup();
    private final Popup studentIdPopup = new Popup();

    public void setCourse(CursoController.CourseRow course, List<CursoController.CourseRow> allCourses,
                          Consumer<CursoController.CourseRow> navigator, Runnable onBackToList) {
        this.currentCourse = course;
        this.allCourses = allCourses;
        this.navigateToCourse = navigator;
        this.onBackToList = onBackToList;
        currentCourseLabel = course.grado() + " " + course.seccion();
        breadcrumbCursoActual.setText(currentCourseLabel);
        pageTitle.setText(lang.get("detalle.pageTitle", "Detalles del Curso") + ": " + currentCourseLabel);
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
        initTeacherAutocomplete();
        initStudentAutocomplete();
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
        colTeacherName.setCellValueFactory(d -> d.getValue().nombreProperty());
        colTeacherName.setCellFactory(nombreCell());
        colTeacherSubject.setCellValueFactory(d -> d.getValue().materiaProperty());
    }

    private Callback<TableColumn<TeacherRow, String>, TableCell<TeacherRow, String>> defaultSubjectCell() {
        return col -> new TableCell<>() {
            private final Label label = new Label();
            { setGraphic(label); setPadding(new Insets(8, 16, 8, 16)); }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                var tv = getTableView();
                if (empty || tv == null || getIndex() < 0 || getIndex() >= tv.getItems().size()) {
                    label.setText(null);
                } else {
                    label.setText(tv.getItems().get(getIndex()).getMateria());
                    label.getStyleClass().add("cell-docente-name");
                }
            }
        };
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
                    nameLabel.setText(row.getNombre());
                    String initial = row.getNombre().substring(0, 1).toUpperCase();
                    initials.setText(initial);
                    avatarCircle.setFill(Color.web(AVATAR_COLORS[row.avatarIdx() % AVATAR_COLORS.length]));
                }
            }
        };
    }

    private void initTeacherData() {
        String activo = lang.get("detalle.activo", "Activo");
        String inactivo = lang.get("detalle.inactivo", "Inactivo");
        String _m = lang.get("detalle.subj.mathematics");
        String _h = lang.get("detalle.subj.history");
        String _l = lang.get("detalle.subj.language");
        String _s = lang.get("detalle.subj.science");
        String _e = lang.get("detalle.subj.english");
        String _a = lang.get("detalle.subj.art");
        String _p = lang.get("detalle.subj.pe");
        String _mu = lang.get("detalle.subj.music");
        String _ph = lang.get("detalle.subj.philosophy");
        String _b = lang.get("detalle.subj.biology");
        String _c = lang.get("detalle.subj.chemistry");
        String _ah = lang.get("detalle.subj.artHistory");
        fullTeacherData.setAll(
            new TeacherRow("Prof. Laura Méndez", "laura.mendez@edu.com", _m, "5to E", activo, 0),
            new TeacherRow("Prof. Carlos Ruiz", "carlos.ruiz@edu.com", _h, "4to A", activo, 1),
            new TeacherRow("Prof. Elena Torres", "elena.torres@edu.com", _l, "3ro B", activo, 2),
            new TeacherRow("Prof. Ana Silva", "ana.silva@edu.com", _s, "2do C", activo, 3),
            new TeacherRow("Prof. Miguel Soto", "miguel.soto@edu.com", _e, "1ro A", inactivo, 4),
            new TeacherRow("Prof. Diana Ríos", "diana.rios@edu.com", _a, "5to B", activo, 5),
            new TeacherRow("Prof. Pedro Lima", "pedro.lima@edu.com", _p, "4to B", activo, 6),
            new TeacherRow("Prof. Sofía Vega", "sofia.vega@edu.com", _mu, "3ro A", activo, 7),
            new TeacherRow("Prof. Luis Paz", "luis.paz@edu.com", _ph, "6to A", inactivo, 0),
            new TeacherRow("Prof. Carmen Rojas", "carmen.rojas@edu.com", _b, "5to C", activo, 1),
            new TeacherRow("Prof. Andrés Cruz", "andres.cruz@edu.com", _c, "4to C", activo, 2),
            new TeacherRow("Prof. Valeria Solís", "valeria.solis@edu.com", _ah, "6to B", activo, 3)
        );
        displayedTeacherData.addAll(fullTeacherData.subList(0, INITIAL_TEACHER_COUNT));
        teacherTable.setItems(displayedTeacherData);
        updateTeacherTableHeight(INITIAL_TEACHER_COUNT);
    }

    private void initStudentTable() {
        colStudent.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colGenero.setCellFactory(col -> new TableCell<StudentRow, String>() {
            private final Label lbl = new Label();
            { lbl.setAlignment(Pos.CENTER); setGraphic(lbl); setPadding(new Insets(8, 8, 8, 8)); }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { lbl.setText(null); }
                else { lbl.setText(item); lbl.getStyleClass().add("cell-docente-name"); }
            }
        });
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
                            lbl.setText(lang.get("detalle.presente", "Presente"));
                            lbl.getStyleClass().add("attendance-btn--presente");
                        }
                        case "ausente" -> {
                            lbl.setText(lang.get("detalle.ausente", "Ausente"));
                            lbl.getStyleClass().add("attendance-btn--ausente");
                        }
                        case "excusa" -> {
                            lbl.setText(lang.get("detalle.excusa", "Excusa"));
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
            {"Liam Castillo",     "M", "MAT-001"},
            {"Emma Rodríguez",    "F", "MAT-002"},
            {"Noah García",       "M", "MAT-003"},
            {"Olivia Martínez",   "F", "MAT-004"},
            {"Mateo Hernández",   "M", "MAT-005"},
            {"Isabella López",    "F", "MAT-006"},
            {"Santiago Pérez",    "M", "MAT-007"},
            {"Sophia González",   "F", "MAT-008"},
            {"Lucas Fernández",   "M", "MAT-009"},
            {"Mía Torres",        "F", "MAT-010"},
        };
        String[] statuses = {"presente", "ausente", "excusa"};
        for (var r : raw) {
            String s = statuses[ThreadLocalRandom.current().nextInt(statuses.length)];
            fullStudentData.add(new StudentRow(r[0], r[1], r[2], s));
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

        String courseKey = currentCourse.grado() + " " + currentCourse.seccion();
        java.util.Map<String, controller.AdminAttendanceView.AttendanceStatus> savedAttendance =
            controller.AdminAttendanceView.getSavedAttendance(courseKey);
        int studentCount;

        if (savedAttendance != null && !savedAttendance.isEmpty()) {
            studentCount = fullStudentData.size();
            for (var entry : savedAttendance.entrySet()) {
                String status;
                switch (entry.getValue()) {
                    case PRESENT -> status = "presente";
                    case ABSENT -> status = "ausente";
                    case EXCUSE -> status = "excusa";
                    default -> status = "presente";
                }
                fullStudentData.add(new StudentRow(entry.getKey(), rng.nextBoolean() ? "M" : "F", "MAT-000", status));
            }
            displayedStudentData.addAll(fullStudentData);
            studentsTable.setItems(displayedStudentData);
            updateStudentTableHeight(fullStudentData.size());
            totalStudents.setText(String.valueOf(fullStudentData.size()));
        } else {
        studentCount = currentCourse.alumnos();
        String[] firstNames = {"Liam","Emma","Noah","Olivia","Mateo","Isabella","Santiago","Sophia",
            "Lucas","Mía","Benjamín","Valentina","Sebastián","Camila","Daniel","Gabriela"};
        String[] lastNames = {"Castillo","Rodríguez","García","Martínez","Hernández","López","Pérez",
            "González","Fernández","Torres","Ramírez","Morales","Ortiz","Cruz","Reyes","Vargas"};
        String[] statuses = {"presente","ausente","excusa"};
        for (int i = 0; i < studentCount; i++) {
            String name = firstNames[rng.nextInt(firstNames.length)] + " " + lastNames[rng.nextInt(lastNames.length)];
            String mat = String.format("MAT-%03d", rng.nextInt(1, 999));
            String status = statuses[rng.nextInt(statuses.length)];
            String genero = rng.nextBoolean() ? "M" : "F";
            fullStudentData.add(new StudentRow(name, genero, mat, status));
        }
        int shownStudents = Math.min(INITIAL_STUDENT_COUNT, fullStudentData.size());
        displayedStudentData.addAll(fullStudentData.subList(0, shownStudents));
        studentsTable.setItems(displayedStudentData);
        updateStudentTableHeight(shownStudents);
        totalStudents.setText(String.valueOf(fullStudentData.size()));
        }

        // ── Teachers ──
        fullTeacherData.clear();
        displayedTeacherData.clear();
        String[] teacherNames = {"Prof. Laura Méndez","Prof. Carlos Ruiz","Prof. Elena Torres",
            "Prof. Ana Silva","Prof. Miguel Soto","Prof. Diana Ríos","Prof. Pedro Lima",
            "Prof. Sofía Vega","Prof. Luis Paz"};
        String[] subjects = {
            lang.get("detalle.subj.mathematics"),
            lang.get("detalle.subj.history"),
            lang.get("detalle.subj.language"),
            lang.get("detalle.subj.science"),
            lang.get("detalle.subj.english"),
            lang.get("detalle.subj.art"),
            lang.get("detalle.subj.pe"),
            lang.get("detalle.subj.music"),
            lang.get("detalle.subj.philosophy"),
            lang.get("detalle.subj.biology"),
            lang.get("detalle.subj.chemistry")
        };
        String activo = lang.get("detalle.activo", "Activo");
        String inactivo = lang.get("detalle.inactivo", "Inactivo");
        int teacherCount = currentCourse.profesores();
        for (int i = 0; i < teacherCount; i++) {
            String subj = subjects[rng.nextInt(subjects.length)];
            String estado = rng.nextBoolean() ? activo : inactivo;
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
            new PieChart.Data(lang.get("detalle.masculino", "Masculino"), masc),
            new PieChart.Data(lang.get("detalle.femenino", "Femenino"), fem)
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

        lblPromedioMateria.setText(lang.get("detalle.subj.mathematics") + ": 72.3");

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data(lang.get("detalle.masculino", "Masculino"), 18),
            new PieChart.Data(lang.get("detalle.femenino", "Femenino"), 12)
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
                    case "Masculino" -> lang.get("detalle.varones", "varones");
                    case "Femenino"  -> lang.get("detalle.mujeres", "mujeres");
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
        Platform.runLater(() -> {
            updateTexts();
            initTeacherData();
            initStats();
        });
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
        String titleBase = lang.get("detalle.pageTitle", "Detalles del Curso");
        pageTitle.setText(currentCourseLabel.isEmpty() ? titleBase : titleBase + ": " + currentCourseLabel);
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
        colGenero.setText(lang.get("detalle.colGenero", "GÉNERO"));
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
        if (lblAddStudentTitle != null) lblAddStudentTitle.setText(lang.get("detalle.addStudentTitle", "Agregar Estudiante"));
        if (lblAddTeacherTitle != null) lblAddTeacherTitle.setText(lang.get("detalle.addTeacherTitle", "Agregar Profesor"));
        if (studentNameField != null) studentNameField.setPromptText(lang.get("detalle.studentNameLabel", "Nombre del estudiante"));
        if (studentIdField != null) studentIdField.setPromptText(lang.get("detalle.studentIdLabel", "ID Matr\u00edcula"));
        if (teacherSearchField != null) teacherSearchField.setPromptText(lang.get("detalle.teacherSearchLabel", "Buscar profesor..."));
        if (addStudentBtn != null) addStudentBtn.setText(lang.get("detalle.btnAdd", "Agregar"));
        if (addTeacherBtn != null) addTeacherBtn.setText(lang.get("detalle.btnAdd", "Agregar"));
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
        if (!editMode) {
            enterEditMode();
        } else {
            saveEditMode();
        }
    }

    private void enterEditMode() {
        editMode = true;
        savedCardChildren = new java.util.ArrayList<>(courseOverviewCard.getChildren());

        btnEditCourse.setText(lang.get("detalle.btnGuardar", "Guardar"));
        btnCancelEdit.setVisible(true);
        btnCancelEdit.setManaged(true);

        editFormsPanel.setVisible(true);
        editFormsPanel.setManaged(true);

        // Make student table editable (name + matrícula)
        studentsTable.setEditable(true);
        colStudent.setCellFactory(TextFieldTableCell.forTableColumn());
        colStudent.setOnEditCommit(e -> {
            StudentRow row = e.getRowValue();
            row.setName(e.getNewValue());
            studentsTable.refresh();
        });
        colMatricula.setCellFactory(TextFieldTableCell.forTableColumn());
        colMatricula.setOnEditCommit(e -> {
            StudentRow row = e.getRowValue();
            row.setMatricula(e.getNewValue());
            studentsTable.refresh();
        });

        // Make teacher table editable
        teacherTable.setEditable(true);
        colTeacherName.setCellFactory(TextFieldTableCell.forTableColumn());
        colTeacherName.setOnEditCommit(e -> {
            TeacherRow row = e.getRowValue();
            row.setNombre(e.getNewValue());
        });
        colTeacherSubject.setCellFactory(TextFieldTableCell.forTableColumn());
        colTeacherSubject.setOnEditCommit(e -> {
            TeacherRow row = e.getRowValue();
            row.setMateria(e.getNewValue());
        });
    }

    private void saveEditMode() {
        exitEditMode();
    }

    @FXML
    private void onCancelarEdicion(ActionEvent event) {
        exitEditMode();
    }

    private void exitEditMode() {
        editMode = false;
        if (savedCardChildren != null) {
            courseOverviewCard.getChildren().setAll(savedCardChildren);
            savedCardChildren = null;
        }
        btnEditCourse.setText(lang.get("detalle.btnEdit", "Editar Curso"));
        btnCancelEdit.setVisible(false);
        btnCancelEdit.setManaged(false);

        studentsTable.setEditable(false);
        colStudent.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStudent.setCellFactory(col -> new TableCell<>() {
            private final Label lbl = new Label();
            { setGraphic(lbl); setPadding(new Insets(8, 16, 8, 16)); }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                var tv = getTableView();
                if (empty || tv == null || getIndex() < 0 || getIndex() >= tv.getItems().size()) {
                    lbl.setText(null);
                } else {
                    lbl.setText(tv.getItems().get(getIndex()).getName());
                    lbl.getStyleClass().add("cell-docente-name");
                }
            }
        });
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colGenero.setCellFactory(col -> new TableCell<StudentRow, String>() {
            private final Label lbl = new Label();
            { lbl.setAlignment(Pos.CENTER); setGraphic(lbl); setPadding(new Insets(8, 8, 8, 8)); }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { lbl.setText(null); }
                else { lbl.setText(item); lbl.getStyleClass().add("cell-docente-name"); }
            }
        });

        teacherTable.setEditable(false);
        colTeacherName.setCellFactory(nombreCell());
        colTeacherSubject.setCellFactory(defaultSubjectCell());
        teacherTable.refresh();
        studentsTable.refresh();
        editFormsPanel.setVisible(false);
        editFormsPanel.setManaged(false);
    }

    @FXML
    private void onAddStudent(ActionEvent event) {
        String name = studentNameField.getText().trim();
        String id = studentIdField.getText().trim();
        if (name.isEmpty() || id.isEmpty()) return;

        String[] statuses = {"presente", "ausente", "excusa"};
        String status = statuses[ThreadLocalRandom.current().nextInt(statuses.length)];
        String genero = "M";
        StudentRow row = new StudentRow(name, genero, id, status);
        fullStudentData.add(row);
        displayedStudentData.add(row);
        int count = displayedStudentData.size();
        updateStudentTableHeight(count);
        totalStudents.setText(String.valueOf(fullStudentData.size()));
        studentNameField.clear();
        studentIdField.clear();
    }

    @FXML
    private void onAddTeacher(ActionEvent event) {
        String search = teacherSearchField.getText().trim();
        if (search.isEmpty()) return;

        // find first matching teacher from DataStore
        var teachers = util.DataStore.getTeachers();
        if (teachers == null || teachers.isEmpty()) {
            // fallback: reuse the internal fullTeacherData
            for (var t : fullTeacherData) {
                if (t.getNombre().toLowerCase().contains(search.toLowerCase())) {
                    addTeacherToCourse(t);
                    teacherSearchField.clear();
                    return;
                }
            }
            return;
        }
        String q = search.toLowerCase();
        for (var t : teachers) {
            if (t.nombre().toLowerCase().contains(q)) {
                TeacherRow newRow = new TeacherRow(t.nombre(), t.email(), t.materia(),
                    currentCourse != null ? currentCourse.seccion() : "", t.estado(), t.avatarIdx());
                addTeacherToCourse(newRow);
                teacherSearchField.clear();
                teacherSuggestPopup.hide();
                return;
            }
        }
    }

    private void addTeacherToCourse(TeacherRow row) {
        fullTeacherData.add(row);
        displayedTeacherData.add(row);
        int count = displayedTeacherData.size();
        updateTeacherTableHeight(count);
        totalTeachers.setText(String.valueOf(fullTeacherData.size()));
    }

    private void initTeacherAutocomplete() {
        teacherSuggestPopup.setAutoHide(true);
        teacherSuggestPopup.setHideOnEscape(true);

        teacherSearchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                teacherSuggestPopup.hide();
                return;
            }
            String q = newVal.trim().toLowerCase();
            VBox list = new VBox();
            list.getStyleClass().add("search-suggestion-popup");
            list.getStylesheets().add(getClass().getResource("/css/DetallesCurso.css").toExternalForm());

            var teachers = util.DataStore.getTeachers();
            if (teachers != null) {
                for (var t : teachers) {
                    if (t.nombre().toLowerCase().contains(q) || t.materia().toLowerCase().contains(q)) {
                        Label item = new Label(t.nombre() + "  —  " + t.materia());
                        item.getStyleClass().add("search-suggestion-item");
                        item.setMaxWidth(Double.MAX_VALUE);
                        item.setOnMouseClicked(e -> {
                            TeacherRow newRow = new TeacherRow(t.nombre(), t.email(), t.materia(),
                                currentCourse != null ? currentCourse.seccion() : "", t.estado(), t.avatarIdx());
                            addTeacherToCourse(newRow);
                            teacherSearchField.clear();
                            teacherSuggestPopup.hide();
                        });
                        list.getChildren().add(item);
                    }
                }
            }

            if (list.getChildren().isEmpty()) {
                Label empty = new Label(lang.get("detalle.noTeachersFound", "No se encontraron profesores"));
                empty.getStyleClass().add("search-suggestion-item");
                empty.setMaxWidth(Double.MAX_VALUE);
                list.getChildren().add(empty);
            }

            teacherSuggestPopup.getContent().setAll(list);
            if (!teacherSuggestPopup.isShowing()) {
                teacherSuggestPopup.show(teacherSearchField, teacherSearchField.localToScreen(0, teacherSearchField.getHeight()).getX(),
                    teacherSearchField.localToScreen(0, teacherSearchField.getHeight()).getY());
            }
        });

        teacherSearchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                Platform.runLater(() -> teacherSuggestPopup.hide());
            }
        });
    }

    private void initStudentAutocomplete() {
        // ── Name field autocomplete ──
        studentNamePopup.setAutoHide(true);
        studentNamePopup.setHideOnEscape(true);
        studentNameField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                studentNamePopup.hide();
                return;
            }
            String q = newVal.trim().toLowerCase();
            VBox list = new VBox();
            list.getStyleClass().add("search-suggestion-popup");
            list.getStylesheets().add(getClass().getResource("/css/DetallesCurso.css").toExternalForm());

            for (var s : fullStudentData) {
                if (s.getName().toLowerCase().contains(q)) {
                    Label item = new Label(s.getName() + "  —  " + s.getMatricula());
                    item.getStyleClass().add("search-suggestion-item");
                    item.setMaxWidth(Double.MAX_VALUE);
                    item.setOnMouseClicked(e -> {
                        studentNameField.setText(s.getName());
                        studentIdField.setText(s.getMatricula());
                        studentNamePopup.hide();
                    });
                    list.getChildren().add(item);
                }
            }

            if (!list.getChildren().isEmpty()) {
                studentNamePopup.getContent().setAll(list);
                if (!studentNamePopup.isShowing()) {
                    studentNamePopup.show(studentNameField,
                        studentNameField.localToScreen(0, studentNameField.getHeight()).getX(),
                        studentNameField.localToScreen(0, studentNameField.getHeight()).getY());
                }
            } else {
                studentNamePopup.hide();
            }
        });
        studentNameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) Platform.runLater(() -> studentNamePopup.hide());
        });

        // ── ID field autocomplete ──
        studentIdPopup.setAutoHide(true);
        studentIdPopup.setHideOnEscape(true);
        studentIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.trim().isEmpty()) {
                studentIdPopup.hide();
                return;
            }
            String q = newVal.trim().toLowerCase();
            VBox list = new VBox();
            list.getStyleClass().add("search-suggestion-popup");
            list.getStylesheets().add(getClass().getResource("/css/DetallesCurso.css").toExternalForm());

            for (var s : fullStudentData) {
                if (s.getMatricula().toLowerCase().contains(q)) {
                    Label item = new Label(s.getMatricula() + "  —  " + s.getName());
                    item.getStyleClass().add("search-suggestion-item");
                    item.setMaxWidth(Double.MAX_VALUE);
                    item.setOnMouseClicked(e -> {
                        studentNameField.setText(s.getName());
                        studentIdField.setText(s.getMatricula());
                        studentIdPopup.hide();
                    });
                    list.getChildren().add(item);
                }
            }

            if (!list.getChildren().isEmpty()) {
                studentIdPopup.getContent().setAll(list);
                if (!studentIdPopup.isShowing()) {
                    studentIdPopup.show(studentIdField,
                        studentIdField.localToScreen(0, studentIdField.getHeight()).getX(),
                        studentIdField.localToScreen(0, studentIdField.getHeight()).getY());
                }
            } else {
                studentIdPopup.hide();
            }
        });
        studentIdField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) Platform.runLater(() -> studentIdPopup.hide());
        });
    }

    @FXML
    private void onCalificaciones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Mestros/Calificaciones.fxml"));
            Node gradesView = loader.load();
            GradesController ctrl = loader.getController();

            VBox cursosCard = (VBox) root.getParent().getParent().getParent();
            List<Node> savedChildren = new java.util.ArrayList<>(cursosCard.getChildrenUnmodifiable());

            ctrl.setOnBack(() -> cursosCard.getChildren().setAll(savedChildren));
            cursosCard.getChildren().setAll(gradesView);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        private final StringProperty genero;
        private final StringProperty matricula;
        private final StringProperty status;

        public StudentRow(String name, String genero, String matricula, String status) {
            this.name = new SimpleStringProperty(name);
            this.genero = new SimpleStringProperty(genero);
            this.matricula = new SimpleStringProperty(matricula);
            this.status = new SimpleStringProperty(status);
        }

        public String getName() { return name.get(); }
        public void setName(String n) { name.set(n); }
        public StringProperty nameProperty() { return name; }

        public String getGenero() { return genero.get(); }
        public void setGenero(String g) { genero.set(g); }
        public StringProperty generoProperty() { return genero; }

        public String getMatricula() { return matricula.get(); }
        public void setMatricula(String m) { matricula.set(m); }
        public StringProperty matriculaProperty() { return matricula; }

        public String getStatus() { return status.get(); }
        public void setStatus(String s) { status.set(s); }
        public StringProperty statusProperty() { return status; }
    }

    public static class TeacherRow {
        private final StringProperty nombre;
        private final StringProperty email;
        private final StringProperty materia;
        private final StringProperty seccion;
        private final StringProperty estado;
        private final int avatarIdx;

        public TeacherRow(String nombre, String email, String materia, String seccion, String estado, int avatarIdx) {
            this.nombre = new SimpleStringProperty(nombre);
            this.email = new SimpleStringProperty(email);
            this.materia = new SimpleStringProperty(materia);
            this.seccion = new SimpleStringProperty(seccion);
            this.estado = new SimpleStringProperty(estado);
            this.avatarIdx = avatarIdx;
        }

        public String getNombre() { return nombre.get(); }
        public void setNombre(String n) { nombre.set(n); }
        public StringProperty nombreProperty() { return nombre; }

        public String getEmail() { return email.get(); }
        public void setEmail(String e) { email.set(e); }
        public StringProperty emailProperty() { return email; }

        public String getMateria() { return materia.get(); }
        public void setMateria(String m) { materia.set(m); }
        public StringProperty materiaProperty() { return materia; }

        public String getSeccion() { return seccion.get(); }
        public void setSeccion(String s) { seccion.set(s); }
        public StringProperty seccionProperty() { return seccion; }

        public String getEstado() { return estado.get(); }
        public void setEstado(String e) { estado.set(e); }
        public StringProperty estadoProperty() { return estado; }

        public int avatarIdx() { return avatarIdx; }
    }
}
