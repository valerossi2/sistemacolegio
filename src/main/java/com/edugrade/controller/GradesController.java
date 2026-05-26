package com.edugrade.controller;

import com.edugrade.cell.GradeInputCell;
import com.edugrade.cell.StudentCell;
import com.edugrade.model.Student;
import com.edugrade.model.Student.Gender;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML private VBox gradesCard;
    @FXML private Text pageTitle;
    @FXML private Text pageSubtitle;
    @FXML private Label lblPeriodo;
    @FXML private Label lblTipoEval;
    @FXML private Label lblListaEstudiantes;
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

    private final ObservableList<Student> studentData =
        FXCollections.observableArrayList(
            new Student("Alejandro García",  "alejandro.g@student.edu", "STU-2023-045", 8.5, Gender.MALE),
            new Student("Lucia Martinez",    "lucia.m@student.edu",     "STU-2023-089", 8.5, Gender.FEMALE),
            new Student("Juan Pérez",        "juan.p@student.edu",      "STU-2023-112", 7.5, Gender.MALE),
            new Student("Valeria Ramirez",   "valeria.r@student.edu",   "STU-2023-156", 9.0, Gender.FEMALE)
        );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        configureColumns();
        gradesTable.setItems(studentData);
        updateTexts();
        Platform.runLater(() -> Platform.runLater(() -> {
            applyTheme();
            if (root.getParent() != null && root.getParent().getParent() instanceof ScrollPane sp)
                root.minHeightProperty().bind(sp.viewportBoundsProperty().map(b -> b.getHeight()));
        }));

        btnGuardarTop.setOnAction(e    -> handleSave());
        btnGuardarBottom.setOnAction(e -> handleSave());
        btnCancelar.setOnAction(e      -> handleCancel());

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);

        comboPeriodo.getItems().addAll("Periodo 1", "Periodo 2", "Periodo 3");
        comboTipoEval.getItems().addAll("Examen Parcial", "Examen Final", "Tareas", "Participación");
        comboPeriodo.getSelectionModel().selectFirst();
        comboTipoEval.getSelectionModel().selectFirst();
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
        root.getStyleClass().removeAll("cursos-root-dark");
        gradesCard.getStyleClass().removeAll("cursos-card-dark");
        if (dark) {
            root.getStyleClass().add("cursos-root-dark");
            gradesCard.getStyleClass().add("cursos-card-dark");
        }
    }

    private void updateTexts() {
        pageTitle.setText(lang.get("grades.pageTitle", "Gestión de Calificaciones"));
        pageSubtitle.setText(lang.get("grades.pageSubtitle", "Ingresa y gestiona las calificaciones de los estudiantes."));
        lblPeriodo.setText(lang.get("grades.periodo", "Periodo"));
        lblTipoEval.setText(lang.get("grades.tipoEval", "Tipo de Evaluación"));
        lblListaEstudiantes.setText(lang.get("grades.listaEstudiantes", "Lista de Estudiantes"));
        colEstudiante.setText(lang.get("grades.colEstudiante", "ESTUDIANTE"));
        colMatricula.setText(lang.get("grades.colMatricula", "ID MATRÍCULA"));
        colCalifActual.setText(lang.get("grades.colCalifActual", "CALIFICACIÓN"));
        colNuevaCalif.setText(lang.get("grades.colNuevaCalif", "NUEVA NOTA"));
        btnGuardarTop.setText(lang.get("grades.guardar", "Guardar Cambios"));
        btnGuardarBottom.setText(lang.get("grades.guardar", "Guardar Cambios"));
        btnCancelar.setText(lang.get("grades.cancelar", "Cancelar"));
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
        colNuevaCalif.setCellFactory(col -> new GradeInputCell());
        colNuevaCalif.setOnEditCommit(event -> {
            Student s = event.getRowValue();
            s.newGradeProperty().set(event.getNewValue());
        });
        colNuevaCalif.setEditable(true);
        gradesTable.setEditable(true);
    }

    private void handleSave() {
        for (Student s : studentData) {
            String ng = s.getNewGrade();
            if (ng != null && !ng.isBlank()) {
                System.out.printf("Guardando → %s  |  Nueva calificación: %s%n",
                    s.getName(), ng);
            }
        }
    }

    private void handleCancel() {
        studentData.forEach(s -> s.newGradeProperty().set(""));
    }
}
