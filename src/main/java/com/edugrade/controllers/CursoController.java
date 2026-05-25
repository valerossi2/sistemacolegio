package com.edugrade.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import theme.ThemeManager;
import util.LanguageManager;

public class CursoController {

    private static final String[] GRADOS = {"5to", "4to", "3ro", "2do", "1ro"};
    private static final String[] ENCARGADOS = {"Prof. Laura Mendez", "Prof. Carlos Ruiz", "Prof. Elena Torres", "Prof. Ana Silva", "Prof. Miguel Soto"};
    private static final String[] SECCIONES = {"E", "A", "B", "C", "A"};
    private static final String[] ALUMNOS = {"32", "29", "31", "28", "35"};
    private static final String[] RENDIMIENTOS = {"91%", "88%", "86%", "82%", "79%"};

    @FXML private Text pageTitle;
    @FXML private Text pageSubtitle;
    @FXML private Label lblTotalCursos;
    @FXML private Label lblPaginacion;
    @FXML private TableView<CourseRow> cursosTable;
    @FXML private TableColumn<CourseRow, String> colGrado;
    @FXML private TableColumn<CourseRow, String> colEncargado;
    @FXML private TableColumn<CourseRow, String> colSeccion;
    @FXML private TableColumn<CourseRow, String> colAlumnos;
    @FXML private TableColumn<CourseRow, String> colRendimiento;
    @FXML private TableColumn<CourseRow, String> colEstado;
    @FXML private Button filterTodos;
    @FXML private Button filterActivos;
    @FXML private Button btnPage1;
    @FXML private Button btnPage2;
    @FXML private Button btnPage3;
    @FXML private Button btnPageNext;

    private final ObservableList<CourseRow> allCourses = FXCollections.observableArrayList();
    private LanguageManager lang;
    private ThemeManager theme;

    @FXML
    private void initialize() {
        lang = LanguageManager.getInstance();
        theme = new ThemeManager();

        buildSampleData();
        configureCourseTable();
        cursosTable.setItems(allCourses);
        updateTexts();
        updateCourseCount(allCourses.size());

        lang.addListener(this::onLanguageChanged);
    }

    public void setThemeManager(ThemeManager theme) {
        this.theme = theme;
    }

    private void onLanguageChanged() {
        Platform.runLater(() -> {
            updateTexts();
            cursosTable.refresh();
        });
    }

    private void updateTexts() {
        pageTitle.setText(lang.get("courses.pageTitle", "Listado Completo de Cursos"));
        pageSubtitle.setText(lang.get("courses.pageSubtitle", "Gestiona los cursos activos del instituto."));
        filterTodos.setText(lang.get("courses.filterAll", "Todos"));
        filterActivos.setText(lang.get("courses.filterActive", "Activos"));
        colGrado.setText(lang.get("courses.colGrado", "Grado"));
        colEncargado.setText(lang.get("courses.colEncargado", "Encargado"));
        colSeccion.setText(lang.get("courses.colSeccion", "Sección"));
        colAlumnos.setText(lang.get("courses.colAlumnos", "Alumnos"));
        colRendimiento.setText(lang.get("courses.colRendimiento", "Rendimiento Promedio"));
        colEstado.setText(lang.get("courses.colEstado", "Estado"));
        updateCourseCount(allCourses.size());
    }

    private void buildSampleData() {
        allCourses.clear();
        for (int i = 0; i < GRADOS.length; i++) {
            allCourses.add(new CourseRow(
                    GRADOS[i],
                    ENCARGADOS[i],
                    SECCIONES[i],
                    ALUMNOS[i],
                    RENDIMIENTOS[i],
                    "Activo"
            ));
        }
    }

    @FXML
    private void onFilterTodos() {
        cursosTable.setItems(allCourses);
        updateCourseCount(allCourses.size());
        filterTodos.getStyleClass().add("filter-pill--active");
        filterActivos.getStyleClass().remove("filter-pill--active");
    }

    @FXML
    private void onFilterActivos() {
        ObservableList<CourseRow> active = allCourses.filtered(c -> "Activo".equals(c.estado()));
        cursosTable.setItems(active);
        updateCourseCount(active.size());
        filterActivos.getStyleClass().add("filter-pill--active");
        filterTodos.getStyleClass().remove("filter-pill--active");
    }

    @FXML
    private void onPage1() { lblPaginacion.setText("Página 1"); }

    @FXML
    private void onPage2() { lblPaginacion.setText("Página 2"); }

    @FXML
    private void onPage3() { lblPaginacion.setText("Página 3"); }

    @FXML
    private void onPageNext() { lblPaginacion.setText("Siguiente página"); }

    private void configureCourseTable() {
        colGrado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().grado()));
        colGrado.setCellFactory(styledCell("cell-grado"));

        colEncargado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().encargado()));
        colEncargado.setCellFactory(styledCell("cell-teacher-name"));

        colSeccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().seccion()));
        colSeccion.setCellFactory(styledCell("cell-seccion"));

        colAlumnos.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().alumnos()));
        colAlumnos.setCellFactory(alumnosCell());

        colRendimiento.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().rendimiento()));
        colRendimiento.setCellFactory(styledCell("cell-rendimiento"));

        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().estado()));
        colEstado.setCellFactory(statusCell());
    }

    private Callback<TableColumn<CourseRow, String>, TableCell<CourseRow, String>> styledCell(String styleClass) {
        return col -> new TableCell<>() {
            private final Label label = new Label();
            {
                label.getStyleClass().add(styleClass);
                setGraphic(label);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    label.setText(null);
                } else {
                    label.setText(item);
                }
            }
        };
    }

    private Callback<TableColumn<CourseRow, String>, TableCell<CourseRow, String>> alumnosCell() {
        return col -> new TableCell<>() {
            private final VBox box = new VBox(0);
            private final Label count = new Label();
            private final Label lbl = new Label();
            {
                count.getStyleClass().add("cell-alumnos-count");
                lbl.getStyleClass().add("cell-alumnos-label");
                box.getChildren().addAll(count, lbl);
                setGraphic(box);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    count.setText(null);
                    lbl.setText(null);
                } else {
                    count.setText(item);
                    lbl.setText(lang.get("courses.studentsLabel", "Estudiantes"));
                }
            }
        };
    }

    private Callback<TableColumn<CourseRow, String>, TableCell<CourseRow, String>> statusCell() {
        return col -> new TableCell<>() {
            private final Label label = new Label();
            {
                setGraphic(label);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    label.setText(null);
                    label.getStyleClass().removeAll("status-pill", "status-pill--active", "status-pill--break");
                } else {
                    label.setText(item);
                    label.getStyleClass().add("status-pill");
                    if ("Activo".equals(item)) {
                        label.getStyleClass().add("status-pill--active");
                    } else {
                        label.getStyleClass().add("status-pill--break");
                    }
                }
            }
        };
    }

    private void updateCourseCount(int count) {
        String pag = lang.get("courses.pagination", "Mostrando {0} de {1} cursos");
        pag = pag.replace("{0}", String.valueOf(count)).replace("{1}", String.valueOf(allCourses.size()));
        lblTotalCursos.setText(lang.get("courses.totalLabel", "Todos los Cursos ({0})").replace("{0}", String.valueOf(count)));
        lblPaginacion.setText(pag);
    }

    public record CourseRow(String grado, String encargado, String seccion, String alumnos, String rendimiento, String estado) {}
}
