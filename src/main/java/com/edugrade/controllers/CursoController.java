package com.edugrade.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import util.DataStore;
import util.LanguageManager;

public class CursoController {

    private static final String[] AVATAR_COLORS = {
        "#3B82F6", "#8B5CF6", "#EC4899", "#F59E0B", "#10B981", "#EF4444", "#6366F1", "#14B8A6"
    };
    private static final String[] NAMES = {
        "Dr. Roberto", "Dra. Elena", "Prof. Juan Carlos", "Dra. Elena",
        "Dr. Roberto", "Prof. Juan Carlos", "Mtra. Sofia", "Dr. Roberto"
    };
    private static final String[] SURNAMES = {
        "Sánchez", "Méndez", "Rico", "Méndez",
        "Sánchez", "Rico", "Valdéz", "Sánchez"
    };

    private LanguageManager lang;
    private ThemeManager theme;

    @FXML private VBox cursosRoot;
    @FXML private VBox cursosCard;
    @FXML private VBox headerBox;
    @FXML private HBox countBox;
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

    private static final Insets HEADER_PADDING_DEFAULT = new Insets(32, 40, 0, 40);
    private static final Insets HEADER_PADDING_COMPACT = new Insets(16, 16, 0, 16);
    private static final Insets COUNT_PADDING_DEFAULT = new Insets(24, 40, 16, 40);
    private static final Insets COUNT_PADDING_COMPACT = new Insets(12, 16, 8, 16);
    private static final double COMPACT_THRESHOLD = 700;

    private final ObservableList<CourseRow> allCourses = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        buildSampleData();
        DataStore.setCourses(allCourses);
        configureCourseTable();
        cursosTable.setItems(allCourses);
        updateTexts();
        updateCourseCount(allCourses.size());

        Platform.runLater(() -> Platform.runLater(() -> {
            applyTheme();
            if (cursosRoot.getParent() != null && cursosRoot.getParent().getParent() instanceof ScrollPane sp)
                cursosRoot.minHeightProperty().bind(sp.viewportBoundsProperty().map(b -> b.getHeight()));
        }));

        setupResponsive();
        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);
    }

    private void setupResponsive() {
        cursosRoot.widthProperty().addListener((obs, oldVal, newVal) -> {
            double w = newVal.doubleValue();
            
            if (w < 600) {
                colSeccion.setVisible(false);
                colAlumnos.setVisible(false);
                colRendimiento.setVisible(false);
                colEstado.setVisible(false);
            } else if (w < 800) {
                colSeccion.setVisible(true);
                colAlumnos.setVisible(false);
                colRendimiento.setVisible(false);
                colEstado.setVisible(true);
            } else if (w < 1000) {
                colSeccion.setVisible(true);
                colAlumnos.setVisible(true);
                colRendimiento.setVisible(false);
                colEstado.setVisible(true);
            } else {
                colSeccion.setVisible(true);
                colAlumnos.setVisible(true);
                colRendimiento.setVisible(true);
                colEstado.setVisible(true);
            }

            boolean compact = w < COMPACT_THRESHOLD;
            boolean wasCompact = oldVal.doubleValue() < COMPACT_THRESHOLD;
            if (compact == wasCompact) return;
            if (compact) {
                cursosRoot.getStyleClass().add("cursos-compact");
                headerBox.setPadding(HEADER_PADDING_COMPACT);
                countBox.setPadding(COUNT_PADDING_COMPACT);
            } else {
                cursosRoot.getStyleClass().remove("cursos-compact");
                headerBox.setPadding(HEADER_PADDING_DEFAULT);
                countBox.setPadding(COUNT_PADDING_DEFAULT);
            }
        });
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
        cursosCard.getStyleClass().removeAll("cursos-card-dark");
        if (dark) {
            cursosRoot.getStyleClass().add("cursos-root-dark");
            cursosCard.getStyleClass().add("cursos-card-dark");
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
        DataStore.seedIfEmpty();
        allCourses.clear();
        List<DataStore.CourseInfo> stored = DataStore.getCourses();
        for (DataStore.CourseInfo c : stored) {
            allCourses.add(new CourseRow(null, c.grado(), c.profesorIdx(), c.seccion(), c.alumnos(), 3, c.rendimiento(), c.estado()));
        }
    }

    private void configureCourseTable() {
        colGrado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().grado()));
        colGrado.setCellFactory(gradoCell());

        colEncargado.setCellValueFactory(d -> new SimpleStringProperty(""));
        colEncargado.setCellFactory(encargadoCell());

        colSeccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().seccion()));
        colSeccion.setCellFactory(seccionCell());

        colAlumnos.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().alumnos())));
        colAlumnos.setCellFactory(alumnosCell());

        colRendimiento.setCellValueFactory(d -> new SimpleStringProperty(String.format("%.2f", d.getValue().rendimiento())));
        colRendimiento.setCellFactory(rendimientoCell());

        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().estado()));
        colEstado.setCellFactory(statusCell());

        colAcciones.setCellValueFactory(d -> new SimpleStringProperty(""));
        colAcciones.setCellFactory(accionesCell());

        // right-align ACCIONES header label
        colAcciones.setStyle("-fx-alignment: CENTER;");
        colAcciones.getStyleClass().add("col-acciones-header");
    }

    private TableCell<CourseRow, String> makeCell(String styleClass, Pos alignment, Insets padding) {
        return new TableCell<>() {
            private final Label label = new Label();
            {
                label.getStyleClass().add(styleClass);
                setGraphic(label);
                setAlignment(alignment);
                if (padding != null) setPadding(padding);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                label.setText(empty || item == null ? null : item);
            }
        };
    }

    private Callback<TableColumn<CourseRow, String>, TableCell<CourseRow, String>> gradoCell() {
        return col -> new TableCell<>() {
            private final Label label = new Label();
            {
                label.getStyleClass().add("cell-grado");
                setGraphic(label);
                setPadding(new Insets(8, 16, 8, 32));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                label.setText(empty || item == null ? null : item);
            }
        };
    }

    private Callback<TableColumn<CourseRow, String>, TableCell<CourseRow, String>> seccionCell() {
        return col -> new TableCell<>() {
            private final Label label = new Label();
            {
                label.getStyleClass().add("cell-seccion");
                setGraphic(label);
                setPadding(new Insets(8, 16, 8, 16));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                label.setText(empty || item == null ? null : item);
            }
        };
    }

    private Callback<TableColumn<CourseRow, String>, TableCell<CourseRow, String>> rendimientoCell() {
        return col -> new TableCell<>() {
            private final Label label = new Label();
            {
                label.getStyleClass().add("cell-rendimiento");
                setGraphic(label);
                setPadding(new Insets(8, 16, 8, 16));
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
                initials.setFill(Color.WHITE);
                initials.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                avatarContainer.getChildren().addAll(avatarCircle, initials);
                firstName.getStyleClass().add("cell-encargado-name");
                lastName.getStyleClass().add("cell-encargado-surname");
                nameBox.getChildren().addAll(firstName, lastName);
                box.getChildren().addAll(avatarContainer, nameBox);
                setGraphic(box);
                setPadding(new Insets(8, 16, 8, 16));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                var tv = getTableView();
                if (empty || tv == null || getIndex() < 0 || getIndex() >= tv.getItems().size()) {
                    firstName.setText(null);
                    lastName.setText(null);
                    initials.setText(null);
                } else {
                    CourseRow row = tv.getItems().get(getIndex());
                    int idx = row.teacherIdx();
                    firstName.setText(NAMES[idx % NAMES.length]);
                    lastName.setText(SURNAMES[idx % NAMES.length]);
                    avatarCircle.setFill(Color.web(AVATAR_COLORS[idx % AVATAR_COLORS.length]));
                    initials.setText(NAMES[idx % NAMES.length].substring(0, 1));
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
                setPadding(new Insets(8, 16, 8, 16));
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
                setPadding(new Insets(8, 16, 8, 16));
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
                    label.setAlignment(Pos.CENTER);
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
                setAlignment(Pos.CENTER_LEFT);
                setPadding(new Insets(8, 16, 8, 16));
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

    private final List<Node> cursosCardChildren = new java.util.ArrayList<>();

    private void handleVerDetalles(CourseRow course) {
        try {
            if (cursosCardChildren.isEmpty())
                cursosCardChildren.addAll(cursosCard.getChildren());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/AdminDetallesCursos.fxml"));
            Node detailView = loader.load();
            DetalleCursoController ctrl = loader.getController();
            ctrl.setCourse(course, allCourses, this::handleVerDetalles, () ->
                cursosCard.getChildren().setAll(cursosCardChildren));
            cursosCard.getChildren().setAll(detailView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCourseCount(int count) {
        lblTotalCursos.setText(lang.get("courses.totalLabel", "Todos los Cursos ({0})").replace("{0}", String.valueOf(count)));
    }

    public record CourseRow(Integer cursoId, String grado, int teacherIdx, String seccion, int alumnos, int profesores, double rendimiento, String estado) {}
}
