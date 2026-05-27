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
import util.LanguageManager;

import java.net.URL;
import java.util.ResourceBundle;

public class GradesController implements Initializable {

    @FXML private VBox root;
    @FXML private HBox topBar;
    @FXML private VBox gradesCard;
    @FXML private TextField searchField;
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
    @FXML private Button btnGuardarTop;
    @FXML private Button btnGuardarBottom;
    @FXML private Button btnCancelar;

    private LanguageManager lang;
    private ThemeManager theme;
    private Runnable onBack;

    private final ObservableList<Student> masterData =
        FXCollections.observableArrayList(
            new Student("Alejandro García",  "alejandro.g@student.edu", "STU-2023-045", 8.5, Gender.MALE),
            new Student("Lucia Martinez",    "lucia.m@student.edu",     "STU-2023-089", 8.5, Gender.FEMALE),
            new Student("Juan Pérez",        "juan.p@student.edu",      "STU-2023-112", 7.5, Gender.MALE),
            new Student("Valeria Ramirez",   "valeria.r@student.edu",   "STU-2023-156", 9.0, Gender.FEMALE)
        );

    private FilteredList<Student> filteredData;

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

        btnGuardarTop.setOnAction(e    -> handleSave());
        btnGuardarBottom.setOnAction(e -> handleSave());
        btnCancelar.setOnAction(e      -> { handleCancel(); if (onBack != null) onBack.run(); });

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String q = newVal == null ? "" : newVal.toLowerCase().trim();
            filteredData.setPredicate(s -> {
                if (q.isEmpty()) return true;
                return s.getName().toLowerCase().contains(q)
                    || s.getStudentId().toLowerCase().contains(q)
                    || s.getEmail().toLowerCase().contains(q);
            });
        });

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);

        comboPeriodo.getItems().addAll("Periodo 1", "Periodo 2", "Periodo 3");
        comboTipoEval.getItems().addAll("Examen Parcial", "Examen Final", "Tareas", "Participación");
        comboPeriodo.getSelectionModel().selectFirst();
        comboTipoEval.getSelectionModel().selectFirst();

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
        colNuevaCalif.setText(lang.get("grades.colNuevaCalif", "NUEVA NOTA"));
        btnGuardarTop.setText(lang.get("grades.guardar", "Guardar Cambios"));
        btnGuardarBottom.setText(lang.get("grades.guardar", "Guardar Cambios"));
        btnCancelar.setText(lang.get("grades.cancelar", "Cancelar"));
        searchField.setPromptText(lang.get("attendance.search", "Buscar estudiantes..."));
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
                System.out.printf("Guardando → %s  |  Nueva calificación: %s%n",
                    s.getName(), ng);
            }
        }
    }

    private void handleCancel() {
        masterData.forEach(s -> s.newGradeProperty().set(""));
    }
}
