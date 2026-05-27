package com.edugrade.controller;

import com.edugrade.cell.GradeInputCell;
import com.edugrade.cell.StudentCell;
import com.edugrade.controllers.CursoController;
import com.edugrade.model.Student;
import com.edugrade.model.Student.Gender;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import theme.ThemeManager;
import util.LanguageManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class GradesController implements Initializable {

    @FXML private VBox root;
    @FXML private HBox topBar;
    @FXML private VBox gradesCard;
    @FXML private Text pageTitle;
    @FXML private Label breadcrumbCursos;
    @FXML private Label breadcrumbCursoActual;
    @FXML private Label lblPeriodo;
    @FXML private Label lblTipoEval;
    @FXML private TableView<Student> gradesTable;
    @FXML private TableColumn<Student, Student> colEstudiante;
    @FXML private TableColumn<Student, String>  colMatricula;
    @FXML private TableColumn<Student, Double>  colCalifActual;
    @FXML private TableColumn<Student, String>  colNuevaCalif;
    @FXML private ComboBox<String> comboPeriodo;
    @FXML private ComboBox<String> comboTipoEval;
    @FXML private Button btnGuardarBottom;
    @FXML private Button btnCancelar;

    private LanguageManager lang;
    private ThemeManager theme;
    private Runnable onBack;
    private Runnable onBackToList;
    private List<CursoController.CourseRow> allCourses;
    private CursoController.CourseRow currentCourse;

    private final ObservableList<Student> masterData = FXCollections.observableArrayList();
    private FilteredList<Student> filteredData;

    private static final String[] FIRST_NAMES = {
        "Liam","Emma","Noah","Olivia","Mateo","Isabella","Santiago","Sophia",
        "Lucas","Mía","Benjamín","Valentina","Sebastián","Camila","Daniel","Gabriela",
        "Joaquín","Abigail","Thiago","Emily","Samuel","Luna","Diego","Victoria",
        "Tomás","Martina","Gabriel","Sara","Emiliano","Alice","Leo","Julia"
    };
    private static final String[] LAST_NAMES = {
        "Castillo","Rodríguez","García","Martínez","Hernández","López","Pérez",
        "González","Fernández","Torres","Ramírez","Morales","Ortiz","Cruz","Reyes","Vargas",
        "Flores","Díaz","Mendoza","Álvarez","Rojas","Castro","Delgado","Peña","Navarro",
        "Gutiérrez","Medina","Aguilar","Guerrero","Salazar"
    };

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        loadStudentsForCourse();
        filteredData = new FilteredList<>(masterData, p -> true);

        configureColumns();
        gradesTable.setItems(filteredData);
        updateTexts();
        Platform.runLater(() -> Platform.runLater(() -> {
            applyTheme();
            if (root.getParent() != null && root.getParent().getParent() instanceof ScrollPane sp)
                root.minHeightProperty().bind(sp.viewportBoundsProperty().map(b -> b.getHeight()));
        }));

        btnGuardarBottom.setOnAction(e -> handleSave());
        btnCancelar.setOnAction(e      -> { handleCancel(); if (onBack != null) onBack.run(); });

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);

        comboPeriodo.getItems().addAll("Periodo 1", "Periodo 2", "Periodo 3", "Periodo 4");
        comboTipoEval.getItems().addAll("Examen Parcial", "Examen Final", "Tareas", "Participación");
        comboPeriodo.getSelectionModel().selectFirst();
        comboTipoEval.getSelectionModel().selectFirst();

        comboTipoEval.getSelectionModel().selectedItemProperty().addListener((obs, old, neu) -> {
            masterData.forEach(s -> s.newGradeProperty().set(""));
        });

        if (currentCourse != null) {
            breadcrumbCursoActual.setText(currentCourse.grado() + " " + currentCourse.seccion());
            pageTitle.setText(lang.get("grades.pageTitle", "Gestión de Calificaciones") + " - " + currentCourse.grado() + " " + currentCourse.seccion());
        }

        setupResponsive();
    }

    private void loadStudentsForCourse() {
        masterData.clear();
        int count = currentCourse != null ? currentCourse.alumnos() : 12;
        var rng = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[rng.nextInt(FIRST_NAMES.length)];
            String lastName  = LAST_NAMES[rng.nextInt(LAST_NAMES.length)];
            String name      = firstName + " " + lastName;
            String email     = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@student.edu";
            String stuId     = String.format("STU-%04d", rng.nextInt(1, 9999));
            double grade     = 0;
            Gender gender    = rng.nextBoolean() ? Gender.MALE : Gender.FEMALE;
            Student student = new Student(name, email, stuId, grade, gender);

            student.newGradeProperty().addListener(new javafx.beans.value.ChangeListener<>() {
                private boolean busy = false;
                @Override
                public void changed(javafx.beans.value.ObservableValue<? extends String> obs, String old, String val) {
                    if (busy) return;
                    if (val == null || val.isBlank()) return;
                    busy = true;
                    try {
                        double g = Double.parseDouble(val);
                        String evalType = comboTipoEval.getSelectionModel().getSelectedItem();
                        if (evalType != null) {
                            student.addEvalGrade(evalType, Math.min(100, Math.max(0, g)));
                            gradesTable.refresh();
                        }
                    } catch (NumberFormatException ignored) {}
                    student.newGradeProperty().set("");
                    busy = false;
                }
            });

            masterData.add(student);
        }
    }

    private void switchToCourse(CursoController.CourseRow course) {
        currentCourse = course;
        loadStudentsForCourse();
        filteredData = new FilteredList<>(masterData, p -> true);
        gradesTable.setItems(filteredData);
        String label = course.grado() + " " + course.seccion();
        breadcrumbCursoActual.setText(label);
        pageTitle.setText(lang.get("grades.pageTitle", "Gestión de Calificaciones") + " - " + label);
    }

    private void setupResponsive() {
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            double w = newVal.doubleValue();
            boolean compact = w < 700;
            boolean wasCompact = oldVal.doubleValue() < 700;
            if (compact == wasCompact) return;
            if (compact) {
                root.getStyleClass().add("cursos-compact");
            } else {
                root.getStyleClass().remove("cursos-compact");
            }
        });
    }

    private void onLanguageChanged() {
        Platform.runLater(() -> {
            updateTexts();
            gradesTable.refresh();
        });
    }

    private void onThemeChanged() {
        Platform.runLater(this::applyTheme);
    }

    private void applyTheme() {
        boolean dark = theme.isDark();
        root.getStyleClass().removeAll("cursos-root-dark", "root-dark");
        gradesCard.getStyleClass().removeAll("cursos-card-dark");
        if (dark) {
            root.getStyleClass().addAll("cursos-root-dark", "root-dark");
            gradesCard.getStyleClass().add("cursos-card-dark");
        }
    }

    private void updateTexts() {
        String courseLabel = currentCourse != null ? currentCourse.grado() + " " + currentCourse.seccion() : "5to E";
        pageTitle.setText(lang.get("grades.pageTitle", "Gestión de Calificaciones") + " - " + courseLabel);
        lblPeriodo.setText(lang.get("grades.periodo", "Periodo"));
        lblTipoEval.setText(lang.get("grades.tipoEval", "Tipo de Evaluación"));
        colEstudiante.setText(lang.get("grades.colEstudiante", "ESTUDIANTE"));
        colMatricula.setText(lang.get("grades.colMatricula", "MATRÍCULA"));
        colCalifActual.setText(lang.get("grades.colCalifActual", "NOTA FINAL"));
        colNuevaCalif.setText(lang.get("grades.colNuevaCalif", "NUEVA NOTA"));
        btnGuardarBottom.setText(lang.get("grades.guardar", "Guardar Cambios"));
        btnCancelar.setText(lang.get("grades.cancelar", "Cancelar"));
        breadcrumbCursos.setText(lang.get("detalle.breadcrumbListado", "Cursos"));
    }

    public void attachSearchField(TextField externalSearchField) {
        externalSearchField.clear();
        externalSearchField.setPromptText(lang.get("attendance.search", "Buscar estudiantes..."));
        externalSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            String q = newValue == null ? "" : newValue.toLowerCase().trim();
            filteredData.setPredicate(s -> {
                if (q.isEmpty()) return true;
                return s.getName().toLowerCase().contains(q)
                    || s.getStudentId().toLowerCase().contains(q)
                    || s.getEmail().toLowerCase().contains(q);
            });
        });
    }

    public void setCourseData(CursoController.CourseRow course, List<CursoController.CourseRow> allCourses,
                              Runnable onBackToList) {
        this.currentCourse = course;
        this.allCourses = allCourses;
        this.onBackToList = onBackToList;
        if (breadcrumbCursoActual != null) {
            breadcrumbCursoActual.setText(course.grado() + " " + course.seccion());
            String courseLabel = course.grado() + " " + course.seccion();
            pageTitle.setText(lang.get("grades.pageTitle", "Gestión de Calificaciones") + " - " + courseLabel);
        }
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
            item.setOnAction(e -> switchToCourse(c));
            menu.getItems().add(item);
        }
        menu.show(((Node) event.getSource()), Side.BOTTOM, 0, 0);
    }

    private void configureColumns() {
        colEstudiante.setCellValueFactory(
            data -> new SimpleObjectProperty<>(data.getValue())
        );
        colEstudiante.setCellFactory(col -> new StudentCell());

        colMatricula.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colMatricula.setCellFactory(col -> new TableCell<>() {
            private final Label lbl = new Label();
            {
                lbl.setStyle("-fx-text-fill: #475569; -fx-font-size: 14px; -fx-font-weight: 500;");
                setGraphic(lbl);
                setPadding(new Insets(8, 16, 8, 16));
            }
            @Override
            protected void updateItem(String id, boolean empty) {
                super.updateItem(id, empty);
                lbl.setText(empty || id == null ? null : id);
            }
        });

        colCalifActual.setCellValueFactory(new PropertyValueFactory<>("currentGrade"));
        colCalifActual.setCellFactory(col -> new TableCell<>() {
            private final Label lbl = new Label();
            {
                lbl.getStyleClass().add("cell-rendimiento");
                setGraphic(lbl);
                setPadding(new Insets(8, 16, 8, 16));
            }
            @Override
            protected void updateItem(Double grade, boolean empty) {
                super.updateItem(grade, empty);
                lbl.setText(empty || grade == null ? null : String.format("%.1f", grade));
            }
        });

        colNuevaCalif.setCellValueFactory(new PropertyValueFactory<>("newGrade"));
        colNuevaCalif.setCellFactory(col -> new GradeInputCell());
        colNuevaCalif.setEditable(true);
        gradesTable.setEditable(true);
    }

    private void handleSave() {
        for (Student s : masterData) {
            String ng = s.getNewGrade();
            if (ng != null && !ng.isBlank()) {
                System.out.printf("Guardando → %s  |  Nueva calificación: %s%n",
                    s.getName(), ng);
            }
        }
    }

    private void handleCancel() {
        masterData.forEach(s -> {
            s.clearEvalGrades();
            s.newGradeProperty().set("");
        });
    }
}
