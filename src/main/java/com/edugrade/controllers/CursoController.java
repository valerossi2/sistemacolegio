package com.edugrade.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;
import theme.ThemeManager;
import util.LanguageManager;

public class CursoController {

    private static final String[] AVATAR_COLORS = {
        "#3B82F6", "#8B5CF6", "#EC4899", "#F59E0B", "#10B981", "#EF4444", "#6366F1", "#14B8A6"
    };

    private LanguageManager lang;
    private ThemeManager theme;

    @FXML private VBox cursosRoot;
    @FXML private Text pageTitle;
    @FXML private Text pageSubtitle;
    @FXML private Label lblTotalCursos;
    @FXML private TableView<CourseRow> cursosTable;
    @FXML private TableColumn<CourseRow, String> colGrado;
    @FXML private TableColumn<CourseRow, String> colEncargado;
    @FXML private TableColumn<CourseRow, String> colSeccion;
    @FXML private TableColumn<CourseRow, String> colAlumnos;
    @FXML private TableColumn<CourseRow, String> colRendimiento;
    @FXML private TableColumn<CourseRow, String> colEstado;
    @FXML private TableColumn<CourseRow, String> colAcciones;

    private VBox cardContainer;
    private final ObservableList<CourseRow> allCourses = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        lang = LanguageManager.getInstance();
        theme = new ThemeManager();

        buildSampleData();
        configureCourseTable();
        cursosTable.setItems(allCourses);
        updateTexts();
        updateCourseCount(allCourses.size());

        Platform.runLater(() -> {
            for (var child : cursosRoot.getChildrenUnmodifiable()) {
                if (child instanceof VBox vb && vb.getStyleClass().contains("cursos-card")) {
                    cardContainer = vb;
                    break;
                }
            }
            applyTheme();
        });

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);
    }

    public void setThemeManager(ThemeManager tm) {
        this.theme = tm;
    }

    private void onLanguageChanged() {
        Platform.runLater(() -> {
            updateTexts();
            cursosTable.refresh();
        });
    }

    private void onThemeChanged() {
        Platform.runLater(this::applyTheme);
    }

    private void applyTheme() {
        boolean dark = theme.isDark();
        cursosRoot.getStyleClass().removeAll("cursos-root-dark");
        if (dark) cursosRoot.getStyleClass().add("cursos-root-dark");
        pageTitle.setFill(Color.web(dark ? "#F8FAFC" : "#1F2937"));
        pageSubtitle.setFill(Color.web(dark ? "#94A3B8" : "#6B7280"));
        lblTotalCursos.setTextFill(Color.web(dark ? "#F8FAFC" : "#1F2937"));
        if (cardContainer != null) {
            cardContainer.setStyle(dark
                ? "-fx-background-color: #1E293B; -fx-background-radius: 24px; -fx-border-color: #334155; -fx-border-radius: 24px; -fx-border-width: 1px;"
                : null);
        }
    }

    private void updateTexts() {
        pageTitle.setText(lang.get("courses.pageTitle", "Listado Completo de Cursos"));
        pageSubtitle.setText(lang.get("courses.pageSubtitle", "Bienvenido de nuevo. Aquí tienes un resumen del estado institucional hoy."));
        colGrado.setText(lang.get("courses.colGrado", "GRADO"));
        colEncargado.setText(lang.get("courses.colEncargado", "ENCARGADO"));
        colSeccion.setText(lang.get("courses.colSeccion", "SECCIÓN"));
        colAlumnos.setText(lang.get("courses.colAlumnos", "ALUMNOS"));
        colRendimiento.setText(lang.get("courses.colRendimiento", "RENDIMIENTO PROMEDIO"));
        colEstado.setText(lang.get("courses.colEstado", "ESTADO"));
        colAcciones.setText(lang.get("courses.colAcciones", "ACCIONES"));
    }

    private void buildSampleData() {
        allCourses.clear();
        allCourses.add(new CourseRow("5to", "Dr. Roberto", "Sánchez", "E", 32, 8.5, "En clase"));
        allCourses.add(new CourseRow("5to", "Dra. Elena", "Méndez", "A", 28, 8.5, "Descanso"));
        allCourses.add(new CourseRow("5to", "Prof. Juan Carlos", "Rico", "C", 40, 8.5, "En clase"));
        allCourses.add(new CourseRow("4to", "Dra. Elena", "Méndez", "A", 24, 9.2, "Descanso"));
        allCourses.add(new CourseRow("6to", "Dr. Roberto", "Sánchez", "E", 32, 7.8, "En clase"));
        allCourses.add(new CourseRow("4to", "Prof. Juan Carlos", "Rico", "A", 40, 8.5, "Descanso"));
        allCourses.add(new CourseRow("3ro", "Mtra. Sofia", "Valdéz", "C", 24, 8.8, "En clase"));
        allCourses.add(new CourseRow("5to", "Dr. Roberto", "Sánchez", "E", 24, 9.2, "Descanso"));
    }

    private void configureCourseTable() {
        colGrado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().grado()));
        colGrado.setCellFactory(styledCell("cell-grado"));

        colEncargado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().nombre() + " " + d.getValue().apellido()));
        colEncargado.setCellFactory(encargadoCell());

        colSeccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().seccion()));
        colSeccion.setCellFactory(styledCell("cell-seccion"));

        colAlumnos.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().alumnos())));
        colAlumnos.setCellFactory(alumnosCell());

        colRendimiento.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().rendimiento())));
        colRendimiento.setCellFactory(styledCell("cell-rendimiento"));

        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().estado()));
        colEstado.setCellFactory(statusCell());

        colAcciones.setCellValueFactory(d -> new SimpleStringProperty(""));
        colAcciones.setCellFactory(accionesCell());
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
                label.setText(empty || item == null ? null : item);
            }
        };
    }

    private Callback<TableColumn<CourseRow, String>, TableCell<CourseRow, String>> encargadoCell() {
        return col -> new TableCell<>() {
            private final HBox box = new HBox(12);
            private final StackPane avatarContainer = new StackPane();
            private final Circle avatarCircle = new Circle(16);
            private final Text initials = new Text();
            private final VBox nameBox = new VBox(0);
            private final Text firstName = new Text();
            private final Text lastName = new Text();
            {
                avatarCircle.setFill(Color.web(AVATAR_COLORS[0]));
                initials.setFill(Color.WHITE);
                initials.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                avatarContainer.getChildren().addAll(avatarCircle, initials);
                firstName.getStyleClass().add("cell-encargado-name");
                lastName.getStyleClass().add("cell-encargado-surname");
                nameBox.getChildren().addAll(firstName, lastName);
                box.getStyleClass().add("cell-encargado-box");
                box.getChildren().addAll(avatarContainer, nameBox);
                setGraphic(box);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    firstName.setText(null);
                    lastName.setText(null);
                    initials.setText(null);
                } else {
                    int row = getIndex();
                    avatarCircle.setFill(Color.web(AVATAR_COLORS[row % AVATAR_COLORS.length]));
                    CourseRow course = getTableView().getItems().get(row);
                    firstName.setText(course.nombre());
                    lastName.setText(course.apellido());
                    initials.setText(course.nombre().substring(0, 1));
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
                label.getStyleClass().removeAll("status-pill", "status-pill--active", "status-pill--break");
                if (empty || item == null) {
                    label.setText(null);
                } else {
                    label.getStyleClass().add("status-pill");
                    if ("En clase".equals(item)) {
                        label.getStyleClass().add("status-pill--active");
                    } else {
                        label.getStyleClass().add("status-pill--break");
                    }
                    label.setText(item);
                }
            }
        };
    }

    private Callback<TableColumn<CourseRow, String>, TableCell<CourseRow, String>> accionesCell() {
        return col -> new TableCell<>() {
            private final Button btn = new Button();
            {
                btn.getStyleClass().add("btn-ver-detalles");
                btn.setOnAction(e -> {
                    int idx = getIndex();
                    if (idx >= 0 && idx < getTableView().getItems().size()) {
                        handleVerDetalles(getTableView().getItems().get(idx));
                    }
                });
                setGraphic(btn);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    btn.setText(null);
                    btn.setVisible(false);
                } else {
                    btn.setText(lang.get("courses.btnVerDetalles", "Ver detalles"));
                    btn.setVisible(true);
                }
            }
        };
    }

    private void handleVerDetalles(CourseRow course) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("courses.detallesTitle", "Detalles del Curso"));
        alert.setHeaderText(course.grado() + " " + lang.get("courses.colSeccion", "SECCIÓN") + " " + course.seccion());
        alert.setContentText(
            lang.get("courses.colGrado", "Grado") + ": " + course.grado()
            + "\n" + lang.get("courses.colEncargado", "Encargado") + ": " + course.nombre() + " " + course.apellido()
            + "\n" + lang.get("courses.colAlumnos", "Alumnos") + ": " + course.alumnos()
            + "\n" + lang.get("courses.colRendimiento", "Rendimiento Promedio") + ": " + course.rendimiento()
            + "\n" + lang.get("courses.colEstado", "Estado") + ": " + course.estado()
        );
        alert.showAndWait();
    }

    private void updateCourseCount(int count) {
        lblTotalCursos.setText(lang.get("courses.totalLabel", "Todos los Cursos ({0})").replace("{0}", String.valueOf(count)));
    }

    public record CourseRow(String grado, String nombre, String apellido, String seccion, int alumnos, double rendimiento, String estado) {}
}
