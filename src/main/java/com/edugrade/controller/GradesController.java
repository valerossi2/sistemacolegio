package com.edugrade.controller;

import com.edugrade.cell.GradeInputCell;
import com.edugrade.cell.StudentCell;
import com.edugrade.model.Student;
import com.edugrade.model.Student.Gender;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class GradesController implements Initializable {

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
    private final ObservableList<Student> studentData =
        FXCollections.observableArrayList(
            new Student("Alejandro García",  "alejandro.g@student.edu", "STU-2023-045", 8.5, Gender.MALE),
            new Student("Lucia Martinez",    "lucia.m@student.edu",     "STU-2023-089", 8.5, Gender.FEMALE),
            new Student("Juan Pérez",        "juan.p@student.edu",      "STU-2023-112", 7.5, Gender.MALE),
            new Student("Valeria Ramirez",   "valeria.r@student.edu",   "STU-2023-156", 9.0, Gender.FEMALE)
        );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureColumns();
        gradesTable.setItems(studentData);

        btnGuardarTop.setOnAction(e    -> handleSave());
        btnGuardarBottom.setOnAction(e -> handleSave());
        btnCancelar.setOnAction(e      -> handleCancel());
    }

    private void configureColumns() {
        colEstudiante.setCellValueFactory(
            data -> new SimpleObjectProperty<>(data.getValue())
        );
        colEstudiante.setCellFactory(col -> new StudentCell());

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
