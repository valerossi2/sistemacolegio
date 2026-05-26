package com.edugrade.controllers;

import java.io.IOException;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.Curso;
import model.Usuario;
import repository.AsignacionRepository;
import repository.UsuarioRepositry;
import java.util.List;
import theme.ThemeManager;
import util.DataStore;
import util.LanguageManager;

public class MaestrosController {

    private static final String[] AVATAR_COLORS = {
        "#3B82F6", "#8B5CF6", "#EC4899", "#F59E0B", "#10B981", "#EF4444", "#6366F1", "#14B8A6"
    };

    private final UsuarioRepositry usuarioRepo = new UsuarioRepositry();
    private final AsignacionRepository asigRepo = new AsignacionRepository();

    private LanguageManager lang;
    private ThemeManager theme;

    @FXML private VBox root;
    @FXML private VBox maestrosCard;
    @FXML private VBox headerBox;
    @FXML private HBox countBox;
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

    private void initTeacherData() {
        String activo = lang.get("teachers.activo", "Activo");
        String inactivo = lang.get("teachers.inactivo", "Inactivo");
        String _m = lang.get("teachers.subj.mathematics");
        String _h = lang.get("teachers.subj.history");
        String _l = lang.get("teachers.subj.language");
        String _s = lang.get("teachers.subj.science");
        String _e = lang.get("teachers.subj.english");
        String _a = lang.get("teachers.subj.art");
        String _p = lang.get("teachers.subj.pe");
        String _mu = lang.get("teachers.subj.music");
        String _ph = lang.get("teachers.subj.philosophy");
        String _b = lang.get("teachers.subj.biology");
        String _c = lang.get("teachers.subj.chemistry");
        String _ah = lang.get("teachers.subj.artHistory");
        allTeachers.setAll(
            new TeacherRow("Prof. Laura Méndez", "laura.mendez@edu.com", _m, "5to E", activo, 0),
            new TeacherRow("Prof. Carlos Ruiz", "carlos.ruiz@edu.com", _h, "4to A", activo, 1),
            new TeacherRow("Prof. Elena Torres", "elena.torres@edu.com", _l, "3ro B", activo, 2),
            new TeacherRow("Prof. Ana Silva", "ana.silva@edu.com", _s, "2do C", activo, 3),
            new TeacherRow("Prof. Miguel Soto", "miguel.soto@edu.com", _e, "1ro A", inactivo, 4),
            new TeacherRow("Prof. Diana Ríos", "diana.rios@edu.com", _a, "5to B", activo, 5),
            new TeacherRow("Prof. Pedro Lima", "pedro.lima@edu.com", _p, "4to B", activo, 6),
            new TeacherRow("Prof. Sofía Vega", "sofia.vega@edu.com", _mu, "3ro A", activo, 7),
            new TeacherRow("Prof. Luis Paz", "luis.paz@edu.com", _ph, "6to A", inactivo, 0),
            new TeacherRow("Prof. Carmen Rojas", "carmen.rojas@edu.com", _b, "5to C", activo, 1),
            new TeacherRow("Prof. Andrés Cruz", "andres.cruz@edu.com", _c, "4to C", activo, 2),
            new TeacherRow("Prof. Valeria Solís", "valeria.solis@edu.com", _ah, "6to B", activo, 3)
        );
    }

    private static final Insets HEADER_PADDING_DEFAULT = new Insets(32, 40, 0, 40);
    private static final Insets HEADER_PADDING_COMPACT = new Insets(16, 16, 0, 16);
    private static final Insets COUNT_PADDING_DEFAULT = new Insets(24, 40, 16, 40);
    private static final Insets COUNT_PADDING_COMPACT = new Insets(12, 16, 8, 16);
    private static final double COMPACT_THRESHOLD = 700;

    private final ObservableList<TeacherRow> allTeachers = FXCollections.observableArrayList();
    private final List<Node> maestrosCardChildren = new java.util.ArrayList<>();

    @FXML
    private void initialize() {
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        initTeacherData();
        DataStore.setTeachers(allTeachers);
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

        loadTeachersFromDB();
    }

    private void loadTeachersFromDB() {
        new Thread(() -> {
            try {
                List<Usuario> maestros = usuarioRepo.findAllMaestros();
                Platform.runLater(() -> {
                    allTeachers.clear();
                    for (Usuario u : maestros) {
                        String materia = "";
                        String seccion = "";
                        List<Curso> cursos = asigRepo.findCursosByMaestro(u.getId());
                        if (!cursos.isEmpty()) {
                            Curso c = cursos.get(0);
                            materia = c.getMateria().getNombre();
                            seccion = c.getSeccion().getGrado().getNombre() + " " + c.getSeccion().getNombre();
                        }
                        String estado = Boolean.TRUE.equals(u.getActivo()) ? "Activo" : "Inactivo";
                        int avatarIdx = u.getId() % AVATAR_COLORS.length;
                        allTeachers.add(new TeacherRow(u.getNombreCompleto(), u.getEmail(), materia, seccion, estado, avatarIdx));
                    }
                    updateCount(allTeachers.size());
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    allTeachers.clear();
                    allTeachers.add(new TeacherRow("Error al cargar: " + e.getMessage(), "", "", "", "", 0));
                    updateCount(0);
                });
            }
        }).start();
    }

    private void setupResponsive() {
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            double w = newVal.doubleValue();
            
            if (w < 600) {
                colEmail.setVisible(false);
                colSeccion.setVisible(false);
                colEstado.setVisible(false);
            } else if (w < 800) {
                colEmail.setVisible(false);
                colSeccion.setVisible(true);
                colEstado.setVisible(true);
            } else {
                colEmail.setVisible(true);
                colSeccion.setVisible(true);
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
            initTeacherData();
            DataStore.setTeachers(allTeachers);
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
                    if (lang.get("teachers.activo", "Activo").equals(item)) {
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
                    btn.setText(lang.get("teachers.btnVerDetalles", "Ver detalles"));
                    btn.setVisible(true);
                }
            }
        };
    }

    private void handleVerDetalles(TeacherRow teacher) {
        try {
            if (maestrosCardChildren.isEmpty())
                maestrosCardChildren.addAll(maestrosCard.getChildren());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/AdminDetalleProfesor.fxml"));
            Node detailView = loader.load();
            DetalleProfesorController ctrl = loader.getController();
            ctrl.setTeacher(teacher, () ->
                maestrosCard.getChildren().setAll(maestrosCardChildren));
            maestrosCard.getChildren().setAll(detailView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCount(int count) {
        lblTotalMaestros.setText(lang.get("teachers.totalLabel", "Todos los Profesores ({0})").replace("{0}", String.valueOf(count)));
    }

    public record TeacherRow(String nombre, String email, String materia, String seccion, String estado, int avatarIdx) {}
}
