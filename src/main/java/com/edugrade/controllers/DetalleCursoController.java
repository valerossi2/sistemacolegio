package com.edugrade.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Callback;
import theme.ThemeManager;
import util.LanguageManager;
import util.DataStore;

public class DetalleCursoController {

    private static final java.util.Set<String> femaleNames = java.util.Set.of(
        "Emma","Olivia","Isabella","Sophia","Mía","Valentina","Camila","Gabriela","Valeria","Sofía"
    );

    private LanguageManager lang;
    private ThemeManager theme;
    private boolean showingAllTeachers = false;
    private boolean showingAllStudents = false;
    private boolean editMode = false;
    private List<Node> savedCardChildren;
    private HBox teacherAddBar;
    private HBox studentAddBar;
    private TextField teacherAddField;
    private TextField studentAddField;
    private Popup teacherAutoPopup;
    private Popup studentAutoPopup;
    private final List<TeacherRow> availableTeacherPool = new java.util.ArrayList<>();
    private final List<StudentRow> availableStudentPool = new java.util.ArrayList<>();

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
    @FXML private TableColumn<StudentRow, String> colGenero;
    @FXML private TableColumn<StudentRow, String> colAsistencia;
    @FXML private Button btnLoadMore;
    @FXML private Label lblPromedioGeneral;
    @FXML private Label lblAprobadosReprobados;
    @FXML private Label lblStatsTitle;
    @FXML private Label lblTotalStudentsLabel;
    @FXML private Label lblTotalTeachersLabel;
    @FXML private Label lblPromedioGeneralLabel;
    @FXML private Label lblAprobadosReprobadosLabel;
    @FXML private Label lblDistribucionGeneroLabel;
    @FXML private Label lblEquipoDocenteCard;
    @FXML private Label lblListaEstudiantesCard;
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
        availableTeacherPool.clear();
        availableTeacherPool.addAll(fullTeacherData);
        displayedTeacherData.addAll(fullTeacherData.subList(0, INITIAL_TEACHER_COUNT));
        teacherTable.setItems(displayedTeacherData);
        updateTeacherTableHeight(INITIAL_TEACHER_COUNT);
    }

    private void initStudentTable() {
        colStudent.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colGenero.setCellFactory(col -> new TableCell<StudentRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle(item.equals("M")
                        ? "-fx-text-fill: #3B82F6; -fx-font-weight: bold;"
                        : "-fx-text-fill: #EC4899; -fx-font-weight: bold;");
                }
            }
        });
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
            {"Liam Castillo",     "MAT-001", "M"},
            {"Emma Rodríguez",    "MAT-002", "F"},
            {"Noah García",       "MAT-003", "M"},
            {"Olivia Martínez",   "MAT-004", "F"},
            {"Mateo Hernández",   "MAT-005", "M"},
            {"Isabella López",    "MAT-006", "F"},
            {"Santiago Pérez",    "MAT-007", "M"},
            {"Sophia González",   "MAT-008", "F"},
            {"Lucas Fernández",   "MAT-009", "M"},
            {"Mía Torres",        "MAT-010", "F"},
        };
        String[] statuses = {"presente", "ausente", "excusa"};
        for (var r : raw) {
            String s = statuses[ThreadLocalRandom.current().nextInt(statuses.length)];
            fullStudentData.add(new StudentRow(r[0], r[1], r[2], s));
        }
        availableStudentPool.clear();
        availableStudentPool.addAll(fullStudentData);
        displayedStudentData.addAll(fullStudentData.subList(0, INITIAL_STUDENT_COUNT));
        studentsTable.setItems(displayedStudentData);
        updateStudentTableHeight(INITIAL_STUDENT_COUNT);
    }

    private void loadRealData() {
        if (currentCourse == null) return;

        // ── Students ──
        fullStudentData.clear();
        displayedStudentData.clear();
        String courseKey = currentCourse.grado() + " " + currentCourse.seccion();
        List<DataStore.StudentInfo> dbStudents = DataStore.getStudentsForCourse(courseKey);
        Map<String, String> attendance = DataStore.getAttendanceForCourse(courseKey);
        for (var s : dbStudents) {
            String status = attendance.getOrDefault(s.matricula(), "presente");
            fullStudentData.add(new StudentRow(s.nombre(), s.matricula(), s.genero(), status));
        }
        int shownStudents = Math.min(INITIAL_STUDENT_COUNT, fullStudentData.size());
        displayedStudentData.addAll(fullStudentData.subList(0, shownStudents));
        studentsTable.setItems(displayedStudentData);
        updateStudentTableHeight(shownStudents);
        totalStudents.setText(String.valueOf(fullStudentData.size()));

        // ── Teachers ──
        fullTeacherData.clear();
        displayedTeacherData.clear();
        List<Integer> teacherIndices = DataStore.getTeacherIndicesForCourse(courseKey);
        for (int i = 0; i < teacherIndices.size(); i++) {
            int idx = teacherIndices.get(i);
            DataStore.TeacherInfo t = DataStore.getTeachers().get(idx);
            String estado = "Activo";
            fullTeacherData.add(new TeacherRow(t.nombre(), t.email(), t.materia(),
                currentCourse.seccion(), estado, idx));
        }
        int shownTeachers = Math.min(INITIAL_TEACHER_COUNT, fullTeacherData.size());
        displayedTeacherData.addAll(fullTeacherData.subList(0, shownTeachers));
        teacherTable.setItems(displayedTeacherData);
        updateTeacherTableHeight(shownTeachers);
        totalTeachers.setText(String.valueOf(fullTeacherData.size()));

        // ── Gender distribution ──
        int studentCount = fullStudentData.size();
        int masc = (int) fullStudentData.stream().filter(s -> "M".equals(s.getGenero())).count();
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
        colGenero.setText(lang.get("detalle.colGenero", "GÉNERO"));
        colAsistencia.setText(lang.get("detalle.colAsistencia", "ASISTENCIA"));
        if (lblStatsTitle != null) lblStatsTitle.setText(lang.get("detalle.statsTitle", "Estad\u00edsticas y Rendimiento"));
        if (lblTotalStudentsLabel != null) lblTotalStudentsLabel.setText(lang.get("detalle.totalStudentsLabel", "Total Estudiantes"));
        if (lblTotalTeachersLabel != null) lblTotalTeachersLabel.setText(lang.get("detalle.totalTeachersLabel", "Total Profesores"));
        if (lblPromedioGeneralLabel != null) lblPromedioGeneralLabel.setText(lang.get("detalle.promedioGeneralLabel", "Promedio General"));
        if (lblAprobadosReprobadosLabel != null) lblAprobadosReprobadosLabel.setText(lang.get("detalle.aprobadosReprobadosLabel", "Aprobados / Reprobados"));
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

        VBox editForm = new VBox(12);
        editForm.setPadding(new Insets(20));
        editForm.setSpacing(12);

        Label title = new Label(lang.get("detalle.editarTitle", "Editar Curso"));
        title.getStyleClass().add("card__title");

        Button btnGuardar = new Button(lang.get("detalle.btnGuardar", "Guardar"));
        btnGuardar.getStyleClass().add("btn-primary");
        Button btnCancelar = new Button(lang.get("detalle.btnCancelar", "Cancelar"));
        btnCancelar.getStyleClass().add("btn-outlined");
        HBox btnRow = new HBox(8, btnGuardar, btnCancelar);

        editForm.getChildren().addAll(title, btnRow);
        courseOverviewCard.getChildren().setAll(editForm);

        btnGuardar.setOnAction(e -> exitEditMode());
        btnCancelar.setOnAction(e -> exitEditMode());

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

        setupEditModeAddBars();
    }

    private void saveEditMode() {
        exitEditMode();
    }

    private void exitEditMode() {
        editMode = false;
        if (savedCardChildren != null) {
            courseOverviewCard.getChildren().setAll(savedCardChildren);
            savedCardChildren = null;
        }
        btnEditCourse.setText(lang.get("detalle.btnEdit", "Editar Curso"));

        teacherTable.setEditable(false);
        colTeacherName.setCellFactory(nombreCell());
        colTeacherSubject.setCellFactory(defaultSubjectCell());
        teacherTable.refresh();
        studentsTable.refresh();

        removeEditModeAddBars();
    }

    private void removeEditModeAddBars() {
        if (teacherAddBar != null && teacherAddBar.getParent() instanceof VBox tc) {
            tc.getChildren().remove(teacherAddBar);
            teacherAddBar = null;
        }
        if (studentAddBar != null && studentAddBar.getParent() instanceof VBox sc) {
            sc.getChildren().remove(studentAddBar);
            studentAddBar = null;
        }
        if (teacherAutoPopup != null) teacherAutoPopup.hide();
        if (studentAutoPopup != null) studentAutoPopup.hide();
    }

    private void setupEditModeAddBars() {
        VBox teacherCard = (VBox) teacherTable.getParent();
        VBox studentCard = (VBox) studentsTable.getParent();

        teacherAddField = new TextField();
        teacherAddField.setPromptText("Buscar profesor...");
        teacherAddField.getStyleClass().add("input-field");
        Button btnAddTeacher = new Button("Agregar");
        btnAddTeacher.getStyleClass().add("btn-primary");
        teacherAddBar = new HBox(8, teacherAddField, btnAddTeacher);
        teacherAddBar.setPadding(new Insets(8, 16, 8, 16));

        int teacherTableIdx = teacherCard.getChildren().indexOf(teacherTable);
        teacherCard.getChildren().add(teacherTableIdx, teacherAddBar);

        teacherAutoPopup = new Popup();
        teacherAutoPopup.setAutoHide(true);
        teacherAutoPopup.setHideOnEscape(true);
        teacherAddField.textProperty().addListener((obs, ov, nv) -> {
            if (nv == null || nv.isBlank()) { teacherAutoPopup.hide(); return; }
            showTeacherAutoComplete(nv);
        });
        teacherAddField.focusedProperty().addListener((obs, ov, nv) -> {
            if (!nv) teacherAutoPopup.hide();
        });
        btnAddTeacher.setOnAction(e -> {
            String name = teacherAddField.getText().trim();
            if (name.isEmpty()) return;
            for (TeacherRow t : availableTeacherPool) {
                if (t.getNombre().equalsIgnoreCase(name)) {
                    addTeacherToCourse(t);
                    teacherAddField.clear();
                    return;
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Selecciona un profesor de la lista de sugerencias.");
            alert.showAndWait();
        });

        studentAddField = new TextField();
        studentAddField.setPromptText("Buscar o agregar estudiante...");
        studentAddField.getStyleClass().add("input-field");
        Button btnAddStudent = new Button("Agregar");
        btnAddStudent.getStyleClass().add("btn-primary");
        studentAddBar = new HBox(8, studentAddField, btnAddStudent);
        studentAddBar.setPadding(new Insets(8, 16, 8, 16));

        int studentTableIdx = studentCard.getChildren().indexOf(studentsTable);
        studentCard.getChildren().add(studentTableIdx, studentAddBar);

        studentAutoPopup = new Popup();
        studentAutoPopup.setAutoHide(true);
        studentAutoPopup.setHideOnEscape(true);
        studentAddField.textProperty().addListener((obs, ov, nv) -> {
            if (nv == null || nv.isBlank()) { studentAutoPopup.hide(); return; }
            showStudentAutoComplete(nv);
        });
        studentAddField.focusedProperty().addListener((obs, ov, nv) -> {
            if (!nv) studentAutoPopup.hide();
        });
        btnAddStudent.setOnAction(e -> {
            String name = studentAddField.getText().trim();
            if (name.isEmpty()) return;
            for (StudentRow s : availableStudentPool) {
                if (s.getName().equalsIgnoreCase(name)) {
                    addStudentToCourse(s);
                    studentAddField.clear();
                    return;
                }
            }
            addNewStudent(name);
            studentAddField.clear();
        });
    }

    private void showTeacherAutoComplete(String query) {
        String q = query.toLowerCase(Locale.ROOT);
        List<TeacherRow> matches = new java.util.ArrayList<>();
        for (TeacherRow t : availableTeacherPool) {
            boolean alreadyAssigned = false;
            for (TeacherRow dt : fullTeacherData) {
                if (dt.getNombre().equals(t.getNombre())) {
                    alreadyAssigned = true;
                    break;
                }
            }
            if (!alreadyAssigned && t.getNombre().toLowerCase(Locale.ROOT).contains(q)) {
                matches.add(t);
                if (matches.size() >= 5) break;
            }
        }
        if (matches.isEmpty()) { teacherAutoPopup.hide(); return; }

        VBox container = new VBox();
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        container.setPrefWidth(250);

        for (TeacherRow t : matches) {
            HBox row = new HBox(8);
            row.setPadding(new Insets(8, 12, 8, 12));
            row.setAlignment(Pos.CENTER_LEFT);
            Label name = new Label(t.getNombre());
            name.setFont(Font.font("Plus Jakarta Sans", FontWeight.MEDIUM, 13));
            Label subject = new Label(t.getMateria());
            subject.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px;");
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            row.getChildren().addAll(name, spacer, subject);
            row.setOnMouseClicked(e -> {
                addTeacherToCourse(t);
                teacherAutoPopup.hide();
                teacherAddField.clear();
            });
            row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #F3F4F6;"));
            row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent;"));
            container.getChildren().add(row);
        }

        teacherAutoPopup.getContent().setAll(container);
        Bounds b = teacherAddField.localToScreen(teacherAddField.getBoundsInLocal());
        if (b != null) teacherAutoPopup.show(teacherAddField, b.getMinX(), b.getMaxY());
    }

    private void showStudentAutoComplete(String query) {
        String q = query.toLowerCase(Locale.ROOT);
        List<StudentRow> matches = new java.util.ArrayList<>();
        for (StudentRow s : availableStudentPool) {
            boolean alreadyAssigned = false;
            for (StudentRow ds : fullStudentData) {
                if (ds.getName().equals(s.getName())) {
                    alreadyAssigned = true;
                    break;
                }
            }
            if (!alreadyAssigned && s.getName().toLowerCase(Locale.ROOT).contains(q)) {
                matches.add(s);
                if (matches.size() >= 5) break;
            }
        }

        VBox container = new VBox();
        container.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-radius: 8; -fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        container.setPrefWidth(250);

        for (StudentRow s : matches) {
            HBox row = new HBox(8);
            row.setPadding(new Insets(8, 12, 8, 12));
            row.setAlignment(Pos.CENTER_LEFT);
            Label name = new Label(s.getName());
            name.setFont(Font.font("Plus Jakarta Sans", FontWeight.MEDIUM, 13));
            Label mat = new Label(s.getMatricula());
            mat.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 11px;");
            row.getChildren().addAll(name, mat);
            row.setOnMouseClicked(e -> {
                addStudentToCourse(s);
                studentAutoPopup.hide();
                studentAddField.clear();
            });
            row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #F3F4F6;"));
            row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent;"));
            container.getChildren().add(row);
        }

        boolean showAddNew = !query.isBlank();
        if (showAddNew) {
            if (!matches.isEmpty()) {
                Region sep = new Region();
                sep.setStyle("-fx-border-color: transparent transparent #E5E7EB transparent; -fx-border-width: 1;");
                sep.setMinHeight(1);
                container.getChildren().add(sep);
            }
            HBox addRow = new HBox(8);
            addRow.setPadding(new Insets(8, 12, 8, 12));
            addRow.setAlignment(Pos.CENTER_LEFT);
            Label addLabel = new Label("+ Agregar \"" + query + "\"");
            addLabel.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 13));
            addLabel.setTextFill(Color.web("#2563EB"));
            addRow.getChildren().add(addLabel);
            addRow.setOnMouseClicked(e -> {
                addNewStudent(query);
                studentAutoPopup.hide();
                studentAddField.clear();
            });
            addRow.setOnMouseEntered(e -> addRow.setStyle("-fx-background-color: #EFF6FF;"));
            addRow.setOnMouseExited(e -> addRow.setStyle("-fx-background-color: transparent;"));
            container.getChildren().add(addRow);
        }

        if (!matches.isEmpty() || showAddNew) {
            studentAutoPopup.getContent().setAll(container);
            Bounds b = studentAddField.localToScreen(studentAddField.getBoundsInLocal());
            if (b != null) studentAutoPopup.show(studentAddField, b.getMinX(), b.getMaxY());
        } else {
            studentAutoPopup.hide();
        }
    }

    private void addTeacherToCourse(TeacherRow teacher) {
        fullTeacherData.add(teacher);
        displayedTeacherData.add(teacher);
        updateTeacherTableHeight(displayedTeacherData.size());
        totalTeachers.setText(String.valueOf(fullTeacherData.size()));
        if (!showingAllTeachers) {
            showingAllTeachers = true;
            btnVerTodosDocentes.setText(lang.get("detalle.verMenos", "Ver menos"));
        }
    }

    private void addStudentToCourse(StudentRow student) {
        fullStudentData.add(student);
        displayedStudentData.add(student);
        updateStudentTableHeight(displayedStudentData.size());
        totalStudents.setText(String.valueOf(fullStudentData.size()));
        if (!showingAllStudents) {
            showingAllStudents = true;
            btnLoadMore.setText(lang.get("detalle.verMenos", "Ver menos"));
        }
    }

    private void addNewStudent(String name) {
        String mat = String.format("MAT-%03d", ThreadLocalRandom.current().nextInt(1, 999));
        String firstName = name.split(" ")[0];
        String genero = femaleNames.contains(firstName) ? "F" : "M";
        StudentRow s = new StudentRow(name, mat, genero, "presente");
        addStudentToCourse(s);
        availableStudentPool.add(s);
    }

    @FXML
    private void onCalificaciones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Mestros/Calificaciones.fxml"));
            Node gradesView = loader.load();
            GradesController ctrl = loader.getController();
            String courseKey = currentCourse.grado() + " " + currentCourse.seccion();
            ctrl.setCourseKey(courseKey);

            Node p = root;
            while (p != null && !"cursosCard".equals(p.getId())) p = p.getParent();
            if (p == null) return;
            VBox cursosCard = (VBox) p;
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
        private final StringProperty matricula;
        private final StringProperty genero;
        private final StringProperty status;

        public StudentRow(String name, String matricula, String genero, String status) {
            this.name = new SimpleStringProperty(name);
            this.matricula = new SimpleStringProperty(matricula);
            this.genero = new SimpleStringProperty(genero);
            this.status = new SimpleStringProperty(status);
        }

        public String getName() { return name.get(); }
        public void setName(String n) { name.set(n); }
        public StringProperty nameProperty() { return name; }

        public String getMatricula() { return matricula.get(); }
        public void setMatricula(String m) { matricula.set(m); }
        public StringProperty matriculaProperty() { return matricula; }

        public String getGenero() { return genero.get(); }
        public void setGenero(String g) { genero.set(g); }
        public StringProperty generoProperty() { return genero; }

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
