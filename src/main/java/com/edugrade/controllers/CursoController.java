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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private static final String[] AVATAR_URLS = {
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCQ1PTsYb5FwYshsNuv94PBvrtMNWdst81MNjXx1h5B1SS7QFawNd3r-s8IZ0EXi3iQVW6s_5zfAqoXPoSJDhTRNfax4q2GwkT5ApH3rRVrVGXXagSiwG6_EQwlAueKDAwm_R8wUYgAiukxu0rOSyVJNiApiJ6HtcNBqTRpO1Ia-dhOIX2-FAIJFsoiPP9vO5-3zx3qTtb0Rwddz5ymTerKCJJn-qNIVFKn2tQRLJNBrVzwCsDFfDpW8DS0N9fa6Kd1Qs_UtM3RIQ4",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAGbgOtu_Ipy-VHGbMycVz56ojLXO3b6Aq01UtHWZTRIj4r6-3FoRYJff4bUpcNvNC2oOYt0qVPr1nRDPY6ZA66wmkry0ENA0IHtAtdEuC2H8ThhULTCp6LXZAs67M3oCz5rDZeegJCC22hIdzX_McphQlIn0UZ0P_RFzUQ4cyOFpAjUgwCwofMtPLG246PH1FCMUvVRIUk-nwCnPFzK9tfvKXHyjB64CbyPFOC6JnPPOODgEIzax2mHrAZVzFSssGUCvBZdGMhopE",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAS9roibZ799DqZlZgI8eiSeGMrLC4kuTkf9cZ7xqPHE7nUPADGowjKJEvst9Lm9rb60-YiBh8xaGuRj3yz4bgwL82eUTIbIV8POabLJJFYttX7O6S911p9gGZSxelk8UZgVL3MsHf5pts_etfE89-WRzHDtvRIUAvM6CAK712nK2GgBZsW7vqD3FrUuEMmFvfDmsDm9faEqVGULWYgOkbWsq2Xq5kPSqJ-LChpnwcgJxhUrUsq_qC6Il5S4s1YMqKqWcpkwsYfolo",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuBfHAf-bf-aExKHcnLG-qVBwEXj4V8HlbQBBZWWo3brrUN7Qk5ZpNS4GifjMGrarsQtG6vyjjnq2FGBvG80HzV4dYf-tCJCMIzuHikbDnMGyA1B97xL-pxB-XW0PF9C1OC9NmDgZN37Llo8XpX-iyVQH1LAeKhQ_jVbc0Wd2cojC1K_41piEJ6P8tkVhSiG64ONtt20OElA82FXg-URA-cgZ8Rcrw8CbThvyYRH3S9SjZi4oDtPrM2gVSKA2oe7GN4xlIL8MgaXTTQ",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCA-mMLfPx8WBH4n_BGGM5lseQnYmDZJoVz6eteAnPeFtMYnDF5zw_1KY_qamEeEdfLfNdvFOAr78uInjzUwLeY0eDXU0AItGpaqqU_xf9_WBPY-cFjCQeYN0tjNATgZr7jCkJWz_ALQuY2Q_szBvsc5skN1IJugV56RytIZ9KRufuYzZwD-UO-MSr41-A97JExNXrwIymK2xLkewFi5DI5kZ99isi1kYPMk0FWFKin_A_jMXDJmRN25zp0SYZpxA3xjm4D_6AqIWA",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCo3WaOUzTdhDlU6hl1PaieRR5dILFiIFXPe6pBil1OppaTuQuvORhOtIqhVUgmUCSphWywq1Wl3pvSEhGPdLIKdpRlXn6_N1c7AWHVy_XunZLvADe0Ykxv0rEDprxcC1_K-Z9t7BLKymFaQEjsI-ASKH-lqG41uRLXiivOpJJZNql40jeX8OmRRINgE5H7H9rpRJX1_RAZjQm1DKay-8rqWG4olXTaoNutZbiA1ElxGV-3yX1QpovNlb5K2y-PAS-rDvf_MHidYlc",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuDAXDZx9crd8_Hqv3UhCwtMRf7nJs3YR-royFo5FzRxbpKDzcvuVAPqyK1GVTAalqm-fELQ3Pewq2oXd7sRMRf1m-tDflLU-5-PHJlgBtl3VzzqhV8uU8EiLZDbQS_wUZceIMrSIH_SbnC_6BPyb130QnfCtunKH5kW-1K70FrLo_hTrUYmZmRXBxbYnGMTiWl9xWypjowWOKD3pzJnWagakxiM5ST4uDK9SQiKzYNYjgHHO9mO7clYkUuEdPvYDg2zpCLwtglwUZ8",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAmcMSnBdcxERybqyUEnaP_CDsBvegFmSitku7nGnrxh8sq24yejBq5I4OoJ7YqgovWNEIDnofQ8x9tW1ftAi_ao6MLvJvhcQ9CmG4IUWbtJLGxNioHGWW7YDKpyWUcXFM4uKmOlZJN_garVqdb0BwUDt-nXd4jya-HXopnQNSIPC-rJ8kKw33KmOVemOC4-U04Hf4592g30Nt1WvUByVtKnEJyjy7mZyqsYM9e7yVLoF1XBAR2WFpyJJN1f-UmfmasuDnyYfJDDBk"
    };
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
        allCourses.clear();
        var rng = ThreadLocalRandom.current();
        String[] grados = {"1ro","2do","3ro","4to","5to","6to"};
        String[] secciones = {"A","B","C","D","E"};
        String[] estados = {"En clase","Descanso"};
        for (int i = 0; i < 16; i++) {
            int alumnos = rng.nextInt(1, 41);
            int profesores = rng.nextInt(1, 10);
            allCourses.add(new CourseRow(null,
                grados[rng.nextInt(grados.length)],
                i % 8,
                secciones[rng.nextInt(secciones.length)],
                alumnos, profesores,
                5.0 + rng.nextDouble() * 5.0,
                estados[rng.nextInt(estados.length)]));
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
            private final ImageView avatarImage = new ImageView();
            private final Text initials = new Text();
            private final VBox nameBox = new VBox(0);
            private final Text firstName = new Text();
            private final Text lastName = new Text();
            {
                avatarImage.setFitWidth(32);
                avatarImage.setFitHeight(32);
                avatarImage.setPreserveRatio(true);
                initials.setFill(Color.WHITE);
                initials.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
                avatarContainer.getChildren().addAll(avatarCircle, avatarImage, initials);
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
                    avatarImage.setImage(null);
                } else {
                    CourseRow row = tv.getItems().get(getIndex());
                    int idx = row.teacherIdx();
                    firstName.setText(NAMES[idx]);
                    lastName.setText(SURNAMES[idx]);
                    avatarCircle.setFill(Color.web(AVATAR_COLORS[idx % AVATAR_COLORS.length]));
                    initials.setText(NAMES[idx].substring(0, 1));

                    Image img = new Image(AVATAR_URLS[idx % AVATAR_URLS.length], 32, 32, true, true, true);
                    avatarImage.setImage(img);
                    img.progressProperty().addListener((obs, p, p1) -> {
                        if (p1.doubleValue() >= 1 && img.getException() == null) {
                            avatarCircle.setVisible(false);
                            initials.setVisible(false);
                        }
                    });
                    img.exceptionProperty().addListener((obs, ex, ex1) -> {
                        avatarCircle.setVisible(true);
                        initials.setVisible(true);
                    });
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
