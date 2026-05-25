package com.edugrade.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class MaestrosController {

    @FXML
    private Text pageTitle;

    @FXML
    private Text pageSubtitle;

    @FXML
    private Label lblTotalMaestros;

    @FXML
    private TableView<TeacherRow> maestrosTable;

    @FXML
    private TableColumn<TeacherRow, String> colNombre;

    @FXML
    private TableColumn<TeacherRow, String> colEmail;

    @FXML
    private TableColumn<TeacherRow, String> colMateria;

    @FXML
    private TableColumn<TeacherRow, String> colSeccion;

    @FXML
    private TableColumn<TeacherRow, String> colEstado;

    private final ObservableList<TeacherRow> allTeachers = FXCollections.observableArrayList(
            new TeacherRow("Prof. Laura Mendez",   "laura.mendez@edu.com",   "Matematicas",  "5to E",  "Activo"),
            new TeacherRow("Prof. Carlos Ruiz",    "carlos.ruiz@edu.com",    "Historia",     "4to A",  "Activo"),
            new TeacherRow("Prof. Elena Torres",   "elena.torres@edu.com",   "Lenguaje",     "3ro B",  "Activo"),
            new TeacherRow("Prof. Ana Silva",      "ana.silva@edu.com",      "Ciencias",     "2do C",  "Activo"),
            new TeacherRow("Prof. Miguel Soto",    "miguel.soto@edu.com",    "Ingles",       "1ro A",  "Inactivo")
    );

    @FXML
    private void initialize() {
        configureTable();
        maestrosTable.setItems(allTeachers);
        updateCount(allTeachers.size());
    }

    private void configureTable() {
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().nombre()));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().email()));
        colMateria.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().materia()));
        colSeccion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().seccion()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().estado()));
    }

    private void updateCount(int count) {
        lblTotalMaestros.setText("Todos los Profesores (" + count + ")");
    }

    private record TeacherRow(
            String nombre,
            String email,
            String materia,
            String seccion,
            String estado
    ) {
    }
}
