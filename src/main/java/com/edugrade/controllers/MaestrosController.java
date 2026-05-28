package com.edugrade.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MaestrosController {

    private static final String[] AVATAR_COLORS = {
        "#3B82F6", "#8B5CF6", "#EC4899", "#F59E0B", "#10B981", "#EF4444", "#6366F1", "#14B8A6"
    };

    private LanguageManager lang;
    private ThemeManager theme;

    @FXML private VBox root;
    @FXML private VBox maestrosCard;
    @FXML private VBox headerBox;
    @FXML private HBox countBox;
    @FXML private Text pageTitle;
    @FXML private Text pageSubtitle;
    @FXML private Label lblTotalMaestros;
    @FXML private Button btnPrint;
    @FXML private TableView<TeacherRow> maestrosTable;
    @FXML private TableColumn<TeacherRow, String> colNombre;
    @FXML private TableColumn<TeacherRow, String> colEmail;
    @FXML private TableColumn<TeacherRow, String> colMateria;
    @FXML private TableColumn<TeacherRow, String> colSeccion;
    @FXML private TableColumn<TeacherRow, String> colRol;
    @FXML private TableColumn<TeacherRow, String> colEstado;
    @FXML private TableColumn<TeacherRow, String> colAcciones;

    private Consumer<TeacherRow> onVerDetalles;

    public void setOnVerDetalles(Consumer<TeacherRow> callback) {
        this.onVerDetalles = callback;
    }

    private static final Insets HEADER_PADDING_DEFAULT = new Insets(32, 40, 0, 40);
    private static final Insets HEADER_PADDING_COMPACT = new Insets(16, 16, 0, 16);
    private static final Insets COUNT_PADDING_DEFAULT = new Insets(24, 40, 16, 40);
    private static final Insets COUNT_PADDING_COMPACT = new Insets(12, 16, 8, 16);
    private static final double COMPACT_THRESHOLD = 700;

    private final ObservableList<TeacherRow> allTeachers = FXCollections.observableArrayList();
    private final Map<String, String> teacherRoles = new HashMap<>();

    @FXML
    private void initialize() {
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        DataStore.seedIfEmpty();
        AppSession session = AppSession.getInstance();
        int filterIdx = session.getFilterTeacherIdx();
        for (DataStore.TeacherInfo t : DataStore.getTeachers()) {
            if (filterIdx >= 0 && t.avatarIdx() != filterIdx) continue;
            allTeachers.add(new TeacherRow(t.nombre(), t.email(), t.materia(), t.seccion(), t.estado(), t.avatarIdx()));
            teacherRoles.put(t.nombre(), "Profesor");
        }
        if (filterIdx >= 0 && teacherRoles.containsKey("Jose Sierra")) {
            teacherRoles.put("Jose Sierra", "Admin");
        }

        configureTable();
        maestrosTable.setItems(allTeachers);
        updateTexts();
        updateCount(allTeachers.size());

        Platform.runLater(() -> Platform.runLater(() -> {
            applyTheme();
            if (root.getParent() != null && root.getParent().getParent() instanceof ScrollPane sp)
                root.minHeightProperty().bind(sp.viewportBoundsProperty().map(b -> b.getHeight()));
        }));

        setupResponsive();
        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);
    }

    private void setupResponsive() {
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            double w = newVal.doubleValue();
            
            if (w < 600) {
                colEmail.setVisible(false);
                colSeccion.setVisible(false);
                colRol.setVisible(false);
                colEstado.setVisible(false);
            } else if (w < 800) {
                colEmail.setVisible(false);
                colSeccion.setVisible(true);
                colRol.setVisible(false);
                colEstado.setVisible(true);
            } else {
                colEmail.setVisible(true);
                colSeccion.setVisible(true);
                colRol.setVisible(true);
                colEstado.setVisible(true);
            }

            boolean compact = w < COMPACT_THRESHOLD;
            boolean wasCompact = oldVal.doubleValue() < COMPACT_THRESHOLD;
            if (compact == wasCompact) return;
            if (compact) {
                root.getStyleClass().add("cursos-compact");
                headerBox.setPadding(HEADER_PADDING_COMPACT);
                countBox.setPadding(COUNT_PADDING_COMPACT);
            } else {
                root.getStyleClass().remove("cursos-compact");
                headerBox.setPadding(HEADER_PADDING_DEFAULT);
                countBox.setPadding(COUNT_PADDING_DEFAULT);
            }
        });
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
        colRol.setText(lang.get("teachers.colRol", "ROL"));
        colEstado.setText(lang.get("teachers.colEstado", "ESTADO"));
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

        colRol.setCellValueFactory(d -> new SimpleStringProperty(teacherRoles.getOrDefault(d.getValue().nombre(), "Profesor")));
        colRol.setCellFactory(rolCell());

        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().estado()));
        colEstado.setCellFactory(statusCell());

        colAcciones.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().nombre()));
        colAcciones.setCellFactory(accionesCell());
        colAcciones.setStyle("-fx-alignment: CENTER-LEFT;");
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

    private Callback<TableColumn<TeacherRow, String>, TableCell<TeacherRow, String>> rolCell() {
        return col -> new TableCell<>() {
            private final ComboBox<String> combo = new ComboBox<>();
            {
                combo.getItems().addAll("Profesor", "Admin");
                combo.getStyleClass().add("btn-ver-detalles");
                combo.setMinWidth(100);
                setGraphic(combo);
                setPadding(new Insets(8, 16, 8, 16));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                var tv = getTableView();
                if (empty || tv == null || getIndex() < 0 || getIndex() >= tv.getItems().size()) {
                    combo.setVisible(false);
                } else {
                    TeacherRow row = tv.getItems().get(getIndex());
                    combo.setVisible(true);
                    String current = teacherRoles.getOrDefault(row.nombre(), "Profesor");
                    combo.setValue(current);
                    combo.setOnAction(e -> {
                        teacherRoles.put(row.nombre(), combo.getValue());
                    });
                }
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
            private final HBox box = new HBox(8);
            private final Button btnVer = new Button();
            private final Button btnQR = new Button();
            {
                btnVer.getStyleClass().add("btn-ver-detalles");
                btnQR.getStyleClass().add("btn-ver-detalles");
                btnQR.setText("QR");
                btnQR.setMinWidth(40);
                box.getChildren().addAll(btnVer, btnQR);
                setGraphic(box);
                setAlignment(Pos.CENTER_LEFT);
                setPadding(new Insets(8, 16, 8, 16));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                var tv = getTableView();
                if (empty || tv == null || getIndex() < 0 || getIndex() >= tv.getItems().size()) {
                    btnVer.setText(null);
                    btnVer.setVisible(false);
                    btnQR.setVisible(false);
                } else {
                    TeacherRow row = tv.getItems().get(getIndex());
                    btnVer.setText(lang.get("teachers.btnVerDetalles", "Ver detalles"));
                    btnVer.setVisible(true);
                    btnVer.setOnAction(e -> {
                        if (onVerDetalles != null) onVerDetalles.accept(row);
                    });
                    btnQR.setVisible(true);
                    btnQR.setOnAction(e -> showQRDialog(row));
                }
            }
        };
    }

    private void showQRDialog(TeacherRow teacher) {
        try {
            String data = teacher.nombre() + "\n" + teacher.email() + "\n" + teacher.materia() + "\n" + teacher.seccion();
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 250, 250);
            WritableImage image = new WritableImage(250, 250);
            PixelWriter pw = image.getPixelWriter();
            for (int y = 0; y < 250; y++) {
                for (int x = 0; x < 250; x++) {
                    pw.setArgb(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            ImageView qrView = new ImageView(image);
            qrView.setFitWidth(200);
            qrView.setFitHeight(200);
            VBox content = new VBox(12, qrView, new Label(teacher.nombre()));
            content.setAlignment(Pos.CENTER);
            content.setPadding(new Insets(20));
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Código QR - " + teacher.nombre());
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void updateCount(int count) {
        lblTotalMaestros.setText(lang.get("teachers.totalLabel", "Todos los Profesores ({0})").replace("{0}", String.valueOf(count)));
    }

    @FXML
    private void onImprimirReporte() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(lang.get("report.title", "Reporte"));
        alert.setHeaderText(null);
        alert.setContentText(lang.get("report.msg", "Impresión de reporte no implementada."));
        alert.showAndWait();
    }

    public record TeacherRow(String nombre, String email, String materia, String seccion, String estado, int avatarIdx) {}
}
