package com.edugrade.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class MaestrosController {

    private static final String[] AVATAR_COLORS = {
        "#3B82F6", "#8B5CF6", "#EC4899", "#F59E0B", "#10B981", "#EF4444", "#6366F1", "#14B8A6"
    };

    private LanguageManager lang;
    private ThemeManager theme;

    @FXML private VBox root;
    @FXML private VBox maestrosCard;
    @FXML private Text pageTitle;
    @FXML private Text pageSubtitle;
    @FXML private Label lblTotalMaestros;
    @FXML private TableView<TeacherRow> maestrosTable;
    @FXML private TableColumn<TeacherRow, String> colNombre;
    @FXML private TableColumn<TeacherRow, String> colEmail;
    @FXML private TableColumn<TeacherRow, String> colMateria;
    @FXML private TableColumn<TeacherRow, String> colSeccion;
    @FXML private TableColumn<TeacherRow, String> colEstado;
    @FXML private TableColumn<TeacherRow, String> colAcciones;

    private final ObservableList<TeacherRow> allTeachers = FXCollections.observableArrayList(
        new TeacherRow("Prof. Laura Méndez", "laura.mendez@edu.com", "Matemáticas", "5to E", "Activo", 0),
        new TeacherRow("Prof. Carlos Ruiz", "carlos.ruiz@edu.com", "Historia", "4to A", "Activo", 1),
        new TeacherRow("Prof. Elena Torres", "elena.torres@edu.com", "Lenguaje", "3ro B", "Activo", 2),
        new TeacherRow("Prof. Ana Silva", "ana.silva@edu.com", "Ciencias", "2do C", "Activo", 3),
        new TeacherRow("Prof. Miguel Soto", "miguel.soto@edu.com", "Inglés", "1ro A", "Inactivo", 4)
    );

    @FXML
    private void initialize() {
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        configureTable();
        maestrosTable.setItems(allTeachers);
        updateTexts();
        updateCount(allTeachers.size());

        Platform.runLater(() -> Platform.runLater(() -> {
            applyTheme();
            if (root.getParent() != null && root.getParent().getParent() instanceof ScrollPane sp)
                root.minHeightProperty().bind(sp.viewportBoundsProperty().map(b -> b.getHeight()));
        }));

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);
    }

    private void onLanguageChanged() {
        Platform.runLater(() -> {
            updateTexts();
            maestrosTable.refresh();
        });
    }

    private void onThemeChanged() {
        Platform.runLater(this::applyTheme);
    }

    private void applyTheme() {
        boolean dark = theme.isDark();
        root.getStyleClass().removeAll("cursos-root-dark");
        maestrosCard.getStyleClass().removeAll("cursos-card-dark");
        if (dark) {
            root.getStyleClass().add("cursos-root-dark");
            maestrosCard.getStyleClass().add("cursos-card-dark");
        }
    }

    private void updateTexts() {
        pageTitle.setText(lang.get("teachers.pageTitle", "Listado de Profesores"));
        pageSubtitle.setText(lang.get("teachers.pageSubtitle", "Gestiona los profesores activos del instituto."));
        colNombre.setText(lang.get("teachers.colNombre", "NOMBRE"));
        colEmail.setText(lang.get("teachers.colEmail", "EMAIL"));
        colMateria.setText(lang.get("teachers.colMateria", "MATERIA"));
        colSeccion.setText(lang.get("teachers.colSeccion", "SECCIÓN"));
        colEstado.setText(lang.get("teachers.colEstado", "ESTADO"));
        colAcciones.setText(lang.get("teachers.colAcciones", "ACCIONES"));
    }

    private void configureTable() {
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().nombre()));
        colNombre.setCellFactory(nombreCell());

        colEmail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().email()));
        colEmail.setCellFactory(emailCell());

        colMateria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().materia()));
        colMateria.setCellFactory(materiaCell());

        colSeccion.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().seccion()));
        colSeccion.setCellFactory(seccionCell());

        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().estado()));
        colEstado.setCellFactory(statusCell());

        colAcciones.setCellValueFactory(d -> new SimpleStringProperty(""));
        colAcciones.setCellFactory(accionesCell());

        colAcciones.setStyle("-fx-alignment: CENTER-RIGHT;");
        colAcciones.getStyleClass().add("col-acciones-header");
    }

    private Callback<TableColumn<TeacherRow, String>, TableCell<TeacherRow, String>> nombreCell() {
        return col -> new TableCell<>() {
            private final HBox box = new HBox(12);
            private final StackPane avatarContainer = new StackPane();
            private final Circle avatarCircle = new Circle(16);
            private final Text initials = new Text();
            private final Label nameLabel = new Label();
            {
                nameLabel.getStyleClass().add("cell-encargado-name");
                avatarCircle.getStyleClass().add("avatar-circle");
                initials.getStyleClass().add("avatar-initials");
                avatarContainer.getChildren().addAll(avatarCircle, initials);
                box.getChildren().addAll(avatarContainer, nameLabel);
                setGraphic(box);
                setPadding(new Insets(8, 16, 8, 32));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                var tv = getTableView();
                if (empty || tv == null || getIndex() < 0 || getIndex() >= tv.getItems().size()) {
                    nameLabel.setText(null);
                    initials.setText(null);
                } else {
                    TeacherRow row = tv.getItems().get(getIndex());
                    nameLabel.setText(row.nombre());
                    String initial = row.nombre().substring(0, 1).toUpperCase();
                    initials.setText(initial);
                    avatarCircle.setFill(Color.web(AVATAR_COLORS[row.avatarIdx() % AVATAR_COLORS.length]));
                }
            }
        };
    }

    private Callback<TableColumn<TeacherRow, String>, TableCell<TeacherRow, String>> emailCell() {
        return col -> new TableCell<>() {
            private final Label label = new Label();
            {
                label.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 14px;");
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

    private Callback<TableColumn<TeacherRow, String>, TableCell<TeacherRow, String>> materiaCell() {
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

    private Callback<TableColumn<TeacherRow, String>, TableCell<TeacherRow, String>> seccionCell() {
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

    private Callback<TableColumn<TeacherRow, String>, TableCell<TeacherRow, String>> statusCell() {
        return col -> new TableCell<>() {
            private final Label label = new Label();
            {
                setGraphic(label);
                setPadding(new Insets(8, 16, 8, 16));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                label.getStyleClass().removeAll("status-pill", "status-pill--active", "status-pill--inactive");
                if (empty || item == null) {
                    label.setText(null);
                } else {
                    label.getStyleClass().add("status-pill");
                    if ("Activo".equals(item)) {
                        label.getStyleClass().add("status-pill--active");
                    } else {
                        label.getStyleClass().add("status-pill--inactive");
                    }
                    label.setText(item);
                    label.setAlignment(Pos.CENTER);
                }
            }
        };
    }

    private Callback<TableColumn<TeacherRow, String>, TableCell<TeacherRow, String>> accionesCell() {
        return col -> new TableCell<>() {
            private final Button btn = new Button();
            {
                btn.getStyleClass().add("btn-ver-detalles");
                btn.setOnAction(e -> {
                    var tv = getTableView();
                    int idx = getIndex();
                    if (tv != null && idx >= 0 && idx < tv.getItems().size()) {
                        handleVerDetalles(tv.getItems().get(idx));
                    }
                });
                setGraphic(btn);
                setAlignment(Pos.CENTER_RIGHT);
                setPadding(new Insets(8, 32, 8, 16));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    btn.setText(null);
                    btn.setVisible(false);
                } else {
                    btn.setText(lang.get("teachers.btnVerDetalles", "Ver detalles"));
                    btn.setVisible(true);
                }
            }
        };
    }

    private void handleVerDetalles(TeacherRow teacher) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("teachers.detallesTitle", "Detalles del Profesor"));
        alert.setHeaderText(teacher.nombre());
        alert.setContentText(
            lang.get("teachers.colEmail", "Email") + ": " + teacher.email()
            + "\n" + lang.get("teachers.colMateria", "Materia") + ": " + teacher.materia()
            + "\n" + lang.get("teachers.colSeccion", "Sección") + ": " + teacher.seccion()
            + "\n" + lang.get("teachers.colEstado", "Estado") + ": " + teacher.estado()
        );
        alert.showAndWait();
    }

    private void updateCount(int count) {
        lblTotalMaestros.setText(lang.get("teachers.totalLabel", "Todos los Profesores ({0})").replace("{0}", String.valueOf(count)));
    }

    public record TeacherRow(String nombre, String email, String materia, String seccion, String estado, int avatarIdx) {}
}
