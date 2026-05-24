// ═══════════════════════════════════════════════════════════════════
// FILE 1:  model/Student.java
// ═══════════════════════════════════════════════════════════════════

package com.example.demo.controller;

import javafx.beans.property.*;

/**
 * Domain model for a student row in the grade table.
 * Uses JavaFX properties so TableView can observe changes automatically.
 */
public class Student {

    public enum Gender { MALE, FEMALE }

    private final StringProperty  name          = new SimpleStringProperty();
    private final StringProperty  email         = new SimpleStringProperty();
    private final StringProperty  studentId     = new SimpleStringProperty();
    private final DoubleProperty  currentGrade  = new SimpleDoubleProperty();
    private final StringProperty  newGrade      = new SimpleStringProperty("");
    private final ObjectProperty<Gender> gender = new SimpleObjectProperty<>();

    // ── Constructor ────────────────────────────────────────────────
    public Student(String name, String email, String studentId,
                   double currentGrade, Gender gender) {
        this.name.set(name);
        this.email.set(email);
        this.studentId.set(studentId);
        this.currentGrade.set(currentGrade);
        this.gender.set(gender);
    }

    // ── Property Accessors (required by TableView) ──────────────────
    public StringProperty  nameProperty()         { return name; }
    public StringProperty  emailProperty()        { return email; }
    public StringProperty  studentIdProperty()    { return studentId; }
    public DoubleProperty  currentGradeProperty() { return currentGrade; }
    public StringProperty  newGradeProperty()     { return newGrade; }
    public ObjectProperty<Gender> genderProperty(){ return gender; }

    // ── Plain Getters ───────────────────────────────────────────────
    public String  getName()         { return name.get(); }
    public String  getEmail()        { return email.get(); }
    public String  getStudentId()    { return studentId.get(); }
    public double  getCurrentGrade() { return currentGrade.get(); }
    public String  getNewGrade()     { return newGrade.get(); }
    public Gender  getGender()       { return gender.get(); }
}


// ═══════════════════════════════════════════════════════════════════
// FILE 2:  cell/StudentCell.java
// ═══════════════════════════════════════════════════════════════════

package com.edugrade.cell;

import com.edugrade.model.Student;
import com.edugrade.model.Student.Gender;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;

/**
 * Custom TableCell that renders:
 *   [SVG Avatar]  Student Name
 *                 student@email.edu
 *
 * The avatar SVG changes depending on the student's gender:
 *   MALE   → person/man SVG path  (blue tint)
 *   FEMALE → person/woman SVG path (pink tint)
 */
public class StudentCell extends TableCell<Student, Student> {

    // ── SVG path strings ────────────────────────────────────────────
    // Generic person silhouette (used for both; differentiated by color only)
    private static final String SVG_MALE =
        // Person with flat top (male icon)
        "M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12z" +
        "M12 14.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z";

    private static final String SVG_FEMALE =
        // Person with slightly curved silhouette (female icon)
        "M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12z" +
        "M12 14.4c-3.2 0-9.6 1.6-9.6 4.8v1.2c0 .7.5 1.2 1.2 1.2h16.8c.7 0 1.2-.5 " +
        "1.2-1.2v-1.2c0-3.2-6.4-4.8-9.6-4.8z";

    // ── Reusable node structure ──────────────────────────────────────
    private final HBox      root       = new HBox(12);
    private final StackPane avatarPane = new StackPane();
    private final SVGPath   avatarIcon = new SVGPath();
    private final VBox      textBox    = new VBox(2);
    private final Label     nameLabel  = new Label();
    private final Label     emailLabel = new Label();

    // ── Constructor ──────────────────────────────────────────────────
    public StudentCell() {
        super();
        setPadding(new Insets(0));
        setGraphic(null);

        // Avatar container
        avatarPane.setMinSize(40, 40);
        avatarPane.setPrefSize(40, 40);
        avatarPane.getChildren().add(avatarIcon);

        // Text block
        nameLabel.getStyleClass().add("student-name-label");
        emailLabel.getStyleClass().add("student-email-label");
        textBox.setAlignment(Pos.CENTER_LEFT);
        textBox.getChildren().addAll(nameLabel, emailLabel);

        // Row layout
        root.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(avatarPane, textBox);
    }

    // ── Update logic ─────────────────────────────────────────────────
    @Override
    protected void updateItem(Student student, boolean empty) {
        super.updateItem(student, empty);

        if (empty || student == null) {
            setGraphic(null);
            return;
        }

        // ── Bind text ───────────────────────────────────────────────
        nameLabel.setText(student.getName());
        emailLabel.setText(student.getEmail());

        // ── Configure avatar by gender ───────────────────────────────
        boolean isMale = student.getGender() == Gender.MALE;

        // Clear old style classes
        avatarPane.getStyleClass().removeAll(
            "student-avatar-container",
            "student-avatar-male",
            "student-avatar-female"
        );
        avatarIcon.getStyleClass().removeAll(
            "student-avatar-icon-male",
            "student-avatar-icon-female"
        );

        // Apply gender-specific styles
        avatarPane.getStyleClass().addAll(
            "student-avatar-container",
            isMale ? "student-avatar-male" : "student-avatar-female"
        );
        avatarIcon.getStyleClass().add(
            isMale ? "student-avatar-icon-male" : "student-avatar-icon-female"
        );

        // Set the SVG path content
        avatarIcon.setContent(isMale ? SVG_MALE : SVG_FEMALE);

        // Scale the SVG to fit the 40×40 container
        double scale = 40.0 / 24.0;   // SVG viewBox is 24×24
        avatarIcon.setScaleX(scale * 0.75);
        avatarIcon.setScaleY(scale * 0.75);

        setGraphic(root);
    }
}


// ═══════════════════════════════════════════════════════════════════
// FILE 3:  cell/GradeInputCell.java
// ═══════════════════════════════════════════════════════════════════

package com.edugrade.cell;

import com.edugrade.model.Student;
import javafx.geometry.Insets;
import javafx.scene.control.*;

/**
 * Editable TableCell that shows a TextField for entering a new grade.
 * The value is committed back to the Student model's newGradeProperty()
 * on focus-lost or Enter key.
 */
public class GradeInputCell extends TableCell<Student, String> {

    private final TextField input = new TextField();

    public GradeInputCell() {
        super();
        input.getStyleClass().add("grade-input");
        input.setPromptText("0.0 – 10.0");

        // Commit on Enter or Tab
        input.setOnAction(e -> commitEdit(input.getText()));

        // Commit on focus loss
        input.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (wasFocused && !isFocused) {
                commitEdit(input.getText());
            }
        });

        // Allow only numeric input + decimal point
        input.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                input.setText(oldVal);
            }
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
            return;
        }

        input.setText(item == null ? "" : item);
        setGraphic(input);
        setPadding(new Insets(0, 16, 0, 16));
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        // Propagate back to the model
        Student student = getTableView().getItems().get(getIndex());
        if (student != null) {
            student.newGradeProperty().set(newValue);
        }
    }
}


// ═══════════════════════════════════════════════════════════════════
// FILE 4:  controller/GradesController.java
// ═══════════════════════════════════════════════════════════════════

package com.edugrade.controller;

import com.edugrade.cell.GradeInputCell;
import com.edugrade.cell.StudentCell;
import com.edugrade.model.Student;
import com.edugrade.model.Student.Gender;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller wired to preview.fxml.
 * Populates the TableView with sample data and configures all columns.
 */
public class GradesController implements Initializable {

    // ── FXML injected nodes ─────────────────────────────────────────
    @FXML private TableView<Student>      gradesTable;
    @FXML private TableColumn<Student, Student> colEstudiante;
    @FXML private TableColumn<Student, String>  colMatricula;
    @FXML private TableColumn<Student, Double>  colCalifActual;
    @FXML private TableColumn<Student, String>  colNuevaCalif;

    @FXML private ComboBox<String> comboPeriodo;
    @FXML private ComboBox<String> comboTipoEval;
    @FXML private Button           btnGuardarTop;
    @FXML private Button           btnGuardarBottom;
    @FXML private Button           btnCancelar;
    @FXML private TextField        searchField;

    // ── Sample data ─────────────────────────────────────────────────
    private final ObservableList<Student> studentData =
        FXCollections.observableArrayList(
            new Student("Alejandro García",  "alejandro.g@student.edu", "STU-2023-045", 8.5, Gender.MALE),
            new Student("Lucia Martinez",    "lucia.m@student.edu",     "STU-2023-089", 8.5, Gender.FEMALE),
            new Student("Juan Pérez",        "juan.p@student.edu",      "STU-2023-112", 7.5, Gender.MALE),
            new Student("Valeria Ramirez",   "valeria.r@student.edu",   "STU-2023-156", 9.0, Gender.FEMALE)
        );

    // ── Initialise ───────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureColumns();
        gradesTable.setItems(studentData);

        // Button handlers
        btnGuardarTop.setOnAction(e    -> handleSave());
        btnGuardarBottom.setOnAction(e -> handleSave());
        btnCancelar.setOnAction(e      -> handleCancel());
    }

    // ── Column configuration ─────────────────────────────────────────
    private void configureColumns() {

        // ── Student column: custom StudentCell ──────────────────────
        //   The column value IS the entire Student object so the cell
        //   can access both name, email, and gender.
        colEstudiante.setCellValueFactory(
            data -> new SimpleObjectProperty<>(data.getValue())
        );
        colEstudiante.setCellFactory(col -> new StudentCell());

        // ── ID Matrícula ────────────────────────────────────────────
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colMatricula.setCellFactory(col -> {
            TableCell<Student, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String id, boolean empty) {
                    super.updateItem(id, empty);
                    if (empty || id == null) { setGraphic(null); setText(null); return; }
                    Label lbl = new Label(id);
                    lbl.getStyleClass().add("student-id-label");
                    setGraphic(lbl);
                    setText(null);
                }
            };
            return cell;
        });

        // ── Current Grade ────────────────────────────────────────────
        colCalifActual.setCellValueFactory(new PropertyValueFactory<>("currentGrade"));
        colCalifActual.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double grade, boolean empty) {
                super.updateItem(grade, empty);
                if (empty || grade == null) { setGraphic(null); setText(null); return; }
                Label lbl = new Label(String.format("%.1f", grade));
                lbl.getStyleClass().add("grade-current-label");
                setGraphic(lbl);
                setText(null);
            }
        });

        // ── New Grade (editable TextField) ────────────────────────────
        colNuevaCalif.setCellValueFactory(new PropertyValueFactory<>("newGrade"));
        colNuevaCalif.setCellFactory(col -> new GradeInputCell());
        colNuevaCalif.setOnEditCommit(event -> {
            Student s = event.getRowValue();
            s.newGradeProperty().set(event.getNewValue());
        });
        colNuevaCalif.setEditable(true);
        gradesTable.setEditable(true);
    }

    // ── Handlers ────────────────────────────────────────────────────
    private void handleSave() {
        for (Student s : studentData) {
            String ng = s.getNewGrade();
            if (ng != null && !ng.isBlank()) {
                System.out.printf("Guardando → %s  |  Nueva calificación: %s%n",
                    s.getName(), ng);
            }
        }
        // TODO: persist to database / service layer
    }

    private void handleCancel() {
        studentData.forEach(s -> s.newGradeProperty().set(""));
    }
}
