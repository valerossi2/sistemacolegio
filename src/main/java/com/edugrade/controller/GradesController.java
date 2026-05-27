package com.edugrade.controller;

import com.edugrade.cell.GradeInputCell;
import com.edugrade.cell.StudentCell;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import theme.ThemeManager;
import util.DataStore;
import util.LanguageManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class GradesController implements Initializable {

    @FXML private VBox root;
    @FXML private VBox gradesCard;
    @FXML private Text pageTitle;
    @FXML private HBox filterBox;
    @FXML private Label lblPeriodo;
    @FXML private Label lblTipoEval;
    @FXML private TableView<Student> gradesTable;
    @FXML private TableColumn<Student, Student> colEstudiante;
    @FXML private TableColumn<Student, String>  colMatricula;
    @FXML private TableColumn<Student, Double>  colCalifActual;
    @FXML private TableColumn<Student, String>  colNuevaCalif;
    @FXML private ComboBox<String> comboPeriodo;
    @FXML private ComboBox<String> comboTipoEval;
    @FXML private Button btnVolver;
    @FXML private Button btnGuardar;

    private LanguageManager lang;
    private ThemeManager theme;
    private Runnable onBack;
    private String courseKey = "";

    private double currentMaxGrade = 100.0;

    private static final java.util.Map<String, Double> EVAL_MAX = java.util.Map.of(
        "Examen Parcial", 25.0,
        "Examen Final", 35.0,
        "Tareas", 25.0,
        "Participación", 15.0
    );

    private final ObservableList<Student> masterData = FXCollections.observableArrayList();

    private FilteredList<Student> filteredData;

    public void setCourseKey(String key) {
        this.courseKey = key;
        loadStudentsFromDataStore();
    }

    private void loadStudentsFromDataStore() {
        DataStore.seedIfEmpty();
        masterData.clear();
        var students = DataStore.getStudentsForCourse(courseKey);
        for (var s : students) {
            double grade = 5.0 + ThreadLocalRandom.current().nextDouble(5.0);
            Gender g = ThreadLocalRandom.current().nextBoolean() ? Gender.MALE : Gender.FEMALE;
            masterData.add(new Student(s.nombre(), s.nombre().toLowerCase().replace(" ",".") + "@student.edu", s.matricula(), Math.round(grade * 10.0) / 10.0, g));
        }
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        filteredData = new FilteredList<>(masterData, p -> true);

        configureColumns();
        gradesTable.setItems(filteredData);
        updateTexts();
        Platform.runLater(() -> Platform.runLater(() -> {
            applyTheme();
            if (root.getParent() != null && root.getParent().getParent() instanceof ScrollPane sp)
                root.minHeightProperty().bind(sp.viewportBoundsProperty().map(b -> b.getHeight()));
        }));

        btnVolver.setOnAction(e -> { if (onBack != null) onBack.run(); });

        btnGuardar.setOnAction(e -> handleSave());

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);

        comboPeriodo.getItems().addAll("Periodo 1", "Periodo 2", "Periodo 3");
        comboTipoEval.getItems().addAll("Examen Parcial", "Examen Final", "Tareas", "Participación");
        comboPeriodo.getSelectionModel().selectFirst();
        comboTipoEval.getSelectionModel().selectFirst();

        comboTipoEval.valueProperty().addListener((obs, ov, nv) -> {
            currentMaxGrade = EVAL_MAX.getOrDefault(nv, 100.0);
            colNuevaCalif.setCellFactory(col -> {
                GradeInputCell cell = new GradeInputCell();
                cell.setMaxGrade(currentMaxGrade);
                return cell;
            });
            gradesTable.refresh();
        });
        currentMaxGrade = EVAL_MAX.getOrDefault(comboTipoEval.getValue(), 100.0);

        setupResponsive();
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
        pageTitle.setText(lang.get("grades.pageTitle", "Gestión de Calificaciones") + " - 5to E");
        lblPeriodo.setText(lang.get("grades.periodo", "Periodo"));
        lblTipoEval.setText(lang.get("grades.tipoEval", "Tipo de Evaluación"));
        colEstudiante.setText(lang.get("grades.colEstudiante", "ESTUDIANTE"));
        colMatricula.setText(lang.get("grades.colMatricula", "MATRÍCULA"));
        colCalifActual.setText(lang.get("grades.colCalifActual", "NOTA ACTUAL"));
        colNuevaCalif.setText(lang.get("grades.colFinal", "CALIF. FINAL"));
        btnVolver.setText(lang.get("grades.cancelar", "Volver"));
        btnGuardar.setText(lang.get("grades.guardar", "Guardar Cambios"));
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
                lbl.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px; -fx-font-weight: 500;");
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
        colNuevaCalif.setCellFactory(col -> {
            GradeInputCell cell = new GradeInputCell();
            cell.setMaxGrade(currentMaxGrade);
            return cell;
        });
        colNuevaCalif.setOnEditCommit(event -> {
            Student s = event.getRowValue();
            s.newGradeProperty().set(event.getNewValue());
        });
        colNuevaCalif.setEditable(true);
        gradesTable.setEditable(true);
    }

    private void handleSave() {
        for (Student s : masterData) {
            String ng = s.getNewGrade();
            if (ng != null && !ng.isBlank()) {
                double increment = Double.parseDouble(ng);
                double current = s.getCurrentGrade();
                String tipo = comboTipoEval.getValue();
                double max = EVAL_MAX.getOrDefault(tipo, 100.0);
                if (increment > max) {
                    System.out.printf("\u26a0 %s: +%.1f supera el m\u00e1ximo de %.0f para %s%n",
                        s.getName(), increment, max, tipo);
                    continue;
                }
                double finalGrade = current + increment;
                System.out.printf("\u2713 %s: %.1f + %.1f = %.1f / %.0f (%s)%n",
                    s.getName(), current, increment, finalGrade, max, tipo);
            }
        }
    }
}
