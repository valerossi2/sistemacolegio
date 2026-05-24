package com.edugrade.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Locale;

public class CursoController {

    @FXML
    private TextField searchField;

    @FXML
    private Label lblTotalCursos;

    @FXML
    private Label lblPaginacion;

    @FXML
    private Label breadcrumbCursoActual;

    @FXML
    private Label pageTitle;

    @FXML
    private Label lblTutorPrincipal;

    @FXML
    private Label totalStudents;

    @FXML
    private Label totalTeachers;

    @FXML
    private Label assignedRoom;

    @FXML
    private Label studentsSubtitle;

    @FXML
    private ScrollPane vistaListaCursos;

    @FXML
    private ScrollPane vistaDetalleCurso;

    @FXML
    private TableView<CourseRow> cursosTable;

    @FXML
    private TableColumn<CourseRow, String> colGrado;

    @FXML
    private TableColumn<CourseRow, String> colEncargado;

    @FXML
    private TableColumn<CourseRow, String> colSeccion;

    @FXML
    private TableColumn<CourseRow, String> colAlumnos;

    @FXML
    private TableColumn<CourseRow, String> colRendimiento;

    @FXML
    private TableColumn<CourseRow, String> colEstado;

    private final ObservableList<CourseRow> allCourses = FXCollections.observableArrayList(
            new CourseRow("5to", "Prof. Laura Mendez", "E", "32", "91%", "Activo"),
            new CourseRow("4to", "Prof. Carlos Ruiz", "A", "29", "88%", "Activo"),
            new CourseRow("3ro", "Prof. Elena Torres", "B", "31", "86%", "Activo"),
            new CourseRow("2do", "Prof. Ana Silva", "C", "28", "82%", "Activo"),
            new CourseRow("1ro", "Prof. Miguel Soto", "A", "35", "79%", "Activo")
    );

    @FXML
    private void initialize() {
        configureCourseTable();
        cursosTable.setItems(allCourses);
        updateCourseCount(allCourses.size());

        cursosTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && cursosTable.getSelectionModel().getSelectedItem() != null) {
                showCourseDetail(cursosTable.getSelectionModel().getSelectedItem());
            }
        });
    }

    @FXML
    private void onSearch(KeyEvent event) {
        String query = searchField.getText();
        if (query == null || query.isBlank()) {
            cursosTable.setItems(allCourses);
            updateCourseCount(allCourses.size());
            return;
        }

        String normalizedQuery = query.toLowerCase(Locale.ROOT);
        ObservableList<CourseRow> filtered = allCourses.filtered(course ->
                course.grado().toLowerCase(Locale.ROOT).contains(normalizedQuery)
                        || course.encargado().toLowerCase(Locale.ROOT).contains(normalizedQuery)
                        || course.seccion().toLowerCase(Locale.ROOT).contains(normalizedQuery)
                        || course.estado().toLowerCase(Locale.ROOT).contains(normalizedQuery)
        );
        cursosTable.setItems(filtered);
        updateCourseCount(filtered.size());
    }

    @FXML
    private void onFilterTodos(ActionEvent event) {
        cursosTable.setItems(allCourses);
        updateCourseCount(allCourses.size());
    }

    @FXML
    private void onFilterActivos(ActionEvent event) {
        ObservableList<CourseRow> activeCourses = allCourses.filtered(course -> "Activo".equals(course.estado()));
        cursosTable.setItems(activeCourses);
        updateCourseCount(activeCourses.size());
    }

    @FXML
    private void onBreadcrumbCursos(MouseEvent event) {
        vistaDetalleCurso.setVisible(false);
        vistaDetalleCurso.setManaged(false);
        vistaListaCursos.setVisible(true);
        vistaListaCursos.setManaged(true);
    }

    @FXML
    private void onEditarCurso(ActionEvent event) {
        showInfo("Editar curso", "La edicion de cursos todavia no esta implementada.");
    }

    @FXML
    private void onImprimirReporte(ActionEvent event) {
        showInfo("Reporte", "La impresion del reporte todavia no esta implementada.");
    }

    @FXML
    private void onVerTodosDocentes(ActionEvent event) {
        showInfo("Docentes", "La lista completa de docentes todavia no esta implementada.");
    }

    @FXML
    private void onCargarMas(ActionEvent event) {
        showInfo("Estudiantes", "No hay mas estudiantes para cargar en esta vista de ejemplo.");
    }

    @FXML
    private void onPage1(ActionEvent event) {
        lblPaginacion.setText("Mostrando pagina 1");
    }

    @FXML
    private void onPage2(ActionEvent event) {
        lblPaginacion.setText("Mostrando pagina 2");
    }

    @FXML
    private void onPage3(ActionEvent event) {
        lblPaginacion.setText("Mostrando pagina 3");
    }

    @FXML
    private void onPageNext(ActionEvent event) {
        lblPaginacion.setText("Mostrando pagina siguiente");
    }

    @FXML
    private void onNavinicio(MouseEvent event) throws IOException {
        navigate(event, "/fxml/Admin/AdminDashboard.fxml");
    }

    @FXML
    private void onNavCursos(MouseEvent event) {
        // Already on courses.
    }

    @FXML
    private void onNavEstudiantes(MouseEvent event) throws IOException {
        navigate(event, "/fxml/Admin/AdminEstudiantes.fxml");
    }

    @FXML
    private void onNavHorario(MouseEvent event) throws IOException {
        navigate(event, "/fxml/Admin/AdminHorario.fxml");
    }

    @FXML
    private void onNavConfig(MouseEvent event) throws IOException {
        navigate(event, "/fxml/Admin/AdminConfiguracion.fxml");
    }

    @FXML
    private void onLogout(ActionEvent event) throws IOException {
        navigate(event, "/fxml/Login.fxml");
    }

    private void configureCourseTable() {
        colGrado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().grado()));
        colEncargado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().encargado()));
        colSeccion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().seccion()));
        colAlumnos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().alumnos()));
        colRendimiento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().rendimiento()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().estado()));
    }

    private void showCourseDetail(CourseRow course) {
        vistaListaCursos.setVisible(false);
        vistaListaCursos.setManaged(false);
        vistaDetalleCurso.setVisible(true);
        vistaDetalleCurso.setManaged(true);

        String courseName = course.grado() + " " + course.seccion();
        breadcrumbCursoActual.setText(courseName);
        pageTitle.setText("Detalles del Curso: " + courseName);
        lblTutorPrincipal.setText(course.encargado());
        totalStudents.setText(course.alumnos());
        totalTeachers.setText("8");
        assignedRoom.setText("Pabellon B - Aula 204");
        studentsSubtitle.setText(course.alumnos() + " alumnos inscritos en el ciclo actual");
    }

    private void updateCourseCount(int count) {
        lblTotalCursos.setText("Todos los Cursos(" + count + ")");
        lblPaginacion.setText("Mostrando " + count + " de " + allCourses.size() + " cursos");
    }

    private void navigate(ActionEvent event, String resource) throws IOException {
        navigate((Node) event.getSource(), resource);
    }

    private void navigate(MouseEvent event, String resource) throws IOException {
        navigate((Node) event.getSource(), resource);
    }

    private void navigate(Node source, String resource) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resource));
        source.getScene().setRoot(root);
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private record CourseRow(
            String grado,
            String encargado,
            String seccion,
            String alumnos,
            String rendimiento,
            String estado
    ) {
    }
}
