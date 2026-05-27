package com.edugrade.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import theme.ThemeManager;
import util.LanguageManager;

public class DetalleProfesorController implements Initializable {

    private static final String[] AVATAR_COLORS = {
        "#3B82F6", "#8B5CF6", "#EC4899", "#F59E0B", "#10B981", "#EF4444", "#6366F1", "#14B8A6"
    };

    private LanguageManager lang;
    private ThemeManager theme;
    private Runnable onBackToList;
    private MaestrosController.TeacherRow currentTeacher;
    private java.time.LocalTime now = java.time.LocalTime.now();

    @FXML private VBox root;
    @FXML private ScrollPane scrollPane;
    @FXML private Label breadcrumbListado;
    @FXML private Label breadcrumbActual;
    @FXML private Label pageTitle;
    @FXML private StackPane avatarContainer;
    @FXML private Label professorName;
    @FXML private Label professorEmail;
    @FXML private Label professorSection;
    @FXML private Label professorStatus;
    @FXML private Label lblAsistenciaTitle;
    @FXML private TableView<AsistenciaRow> attendanceTable;
    @FXML private TableColumn<AsistenciaRow, String> colFecha;
    @FXML private TableColumn<AsistenciaRow, String> colEntrada;
    @FXML private TableColumn<AsistenciaRow, String> colSalida;
    @FXML private TableColumn<AsistenciaRow, String> colEstado;
    @FXML private VBox scheduleCard;
    @FXML private Label lblScheduleTitle;
    @FXML private VBox scheduleRows;

    public void setTeacher(MaestrosController.TeacherRow teacher, Runnable onBackToList) {
        this.currentTeacher = teacher;
        this.onBackToList = onBackToList;
        loadTeacherData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lang = LanguageManager.getInstance();
        theme = ThemeManager.getInstance();

        loadStylesheets();
        updateTexts();
        applyTheme();
        setupAttendanceTable();

        lang.addListener(this::onLanguageChanged);
        theme.addListener(this::onThemeChanged);
    }

    private void loadStylesheets() {
        var baseUrl = getClass().getResource("/css/base.css");
        var detalleUrl = getClass().getResource("/css/DetallesCurso.css");
        var profUrl = getClass().getResource("/css/Profesores.css");
        if (baseUrl != null) root.getStylesheets().add(baseUrl.toExternalForm());
        if (detalleUrl != null) root.getStylesheets().add(detalleUrl.toExternalForm());
        if (profUrl != null) root.getStylesheets().add(profUrl.toExternalForm());
    }

    private void loadTeacherData() {
        if (currentTeacher == null) return;
        professorName.setText(currentTeacher.nombre());
        professorEmail.setText(currentTeacher.email());
        professorSection.setText(currentTeacher.seccion());
        professorStatus.setText(currentTeacher.estado());
        breadcrumbActual.setText(currentTeacher.nombre());
        pageTitle.setText(lang.get("teachers.detallesTitle", "Detalles del Profesor") + ": " + currentTeacher.nombre());
        injectAvatar(currentTeacher.nombre(), currentTeacher.avatarIdx());
        loadSampleAttendance();
        loadSchedule();
    }

    private void injectAvatar(String nombre, int avatarIdx) {
        Circle circle = new Circle(40);
        circle.setFill(Color.web(AVATAR_COLORS[avatarIdx % AVATAR_COLORS.length]));
        Text initials = new Text(nombre.substring(0, 1).toUpperCase());
        initials.setFill(Color.WHITE);
        initials.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");
        avatarContainer.getChildren().setAll(circle, initials);
        StackPane.setAlignment(circle, javafx.geometry.Pos.CENTER);
        StackPane.setAlignment(initials, javafx.geometry.Pos.CENTER);
    }

    private void setupAttendanceTable() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<>("entrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<>("salida"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(col -> new TableCell<>() {
            private final Label label = new Label();
            {
                setGraphic(label);
                setPadding(new Insets(8, 16, 8, 16));
            }
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                label.getStyleClass().removeAll("badge-presente", "badge-tardanza", "badge-ausente");
                if (empty || estado == null) {
                    label.setText(null);
                } else {
                    String cssClass = switch (estado.toLowerCase()) {
                        case "presente" -> "badge-presente";
                        case "tardanza" -> "badge-tardanza";
                        default -> "badge-ausente";
                    };
                    label.getStyleClass().add(cssClass);
                    label.setText(estado);
                    label.setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });
        attendanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void loadSampleAttendance() {
        ObservableList<AsistenciaRow> data = FXCollections.observableArrayList(
            new AsistenciaRow("14/05/2024", "08:55 AM", "01:45 PM", lang.get("detalle.presente", "Presente")),
            new AsistenciaRow("13/05/2024", "09:00 AM", "01:30 PM", lang.get("detalle.presente", "Presente")),
            new AsistenciaRow("10/05/2024", "08:58 AM", "01:40 PM", lang.get("detalle.presente", "Presente")),
            new AsistenciaRow("09/05/2024", "09:15 AM", "01:35 PM", "Tardanza"),
            new AsistenciaRow("08/05/2024", "08:50 AM", "01:50 PM", lang.get("detalle.presente", "Presente")),
            new AsistenciaRow("07/05/2024", "09:10 AM", "01:35 PM", lang.get("detalle.presente", "Presente")),
            new AsistenciaRow("06/05/2024", "08:55 AM", "01:45 PM", lang.get("detalle.presente", "Presente")),
            new AsistenciaRow("03/05/2024", "08:45 AM", "01:55 PM", lang.get("detalle.presente", "Presente")),
            new AsistenciaRow("02/05/2024", "08:50 AM", "01:40 PM", lang.get("detalle.presente", "Presente"))
        );
        attendanceTable.setItems(data);
    }

    private void loadSchedule() {
        scheduleRows.getChildren().clear();

        String[][] data = {
            {"07:00", "08:30", "Matematicas"},
            {"08:45", "10:15", currentTeacher != null ? currentTeacher.materia() : "Historia"},
            {"10:30", "12:00", "Laboratorio"},
            {"13:00", "14:30", "Tutorias"},
            {"14:45", "16:15", "Arte"}
        };

        String trackColor = c("#E2E8F0", "#334155");
        String fillColor = c("#2563EB", "#3B82F6");
        String currentColor = "#F59E0B";
        String inactiveColor = c("#CBD5E1", "#475569");
        String labelColor = c("#0F172A", "#F8FAFC");
        String labelMuted = c("#475569", "#94A3B8");
        String labelDim = c("#94A3B8", "#64748B");

        // find last active end for fill height
        String lastActiveEnd = "07:00";
        for (var row : data) {
            var end = java.time.LocalTime.parse(row[1]);
            if (!now.isAfter(end)) {
                if (!now.isBefore(java.time.LocalTime.parse(row[0])) && !now.isAfter(end)) {
                    lastActiveEnd = row[1];
                    break;
                }
            } else {
                lastActiveEnd = row[1];
            }
        }
        var earliest = java.time.LocalTime.parse("07:00");
        var latest = java.time.LocalTime.parse("18:00");
        double totalMinutes = java.time.temporal.ChronoUnit.MINUTES.between(earliest, latest);
        double fillPct = java.time.temporal.ChronoUnit.MINUTES.between(earliest, java.time.LocalTime.parse(lastActiveEnd)) / totalMinutes;
        if (fillPct < 0) fillPct = 0;

        double rowHeight = 60;
        double barTop = 10;
        double barBottom = 10;
        double barHeight = data.length * rowHeight - barTop - barBottom;
        double centerX = 60;

        Pane barPane = new Pane();
        barPane.setPrefHeight(data.length * rowHeight);
        barPane.setMinHeight(data.length * rowHeight);
        barPane.setMaxWidth(Double.MAX_VALUE);

        // vertical track
        Rectangle track = new Rectangle(centerX - 2, barTop, 4, barHeight);
        track.setFill(Color.web(trackColor));
        track.setArcWidth(4);
        track.setArcHeight(4);

        // vertical fill
        double fillH = barHeight * fillPct;
        if (fillH > barHeight) fillH = barHeight;
        Rectangle fill = new Rectangle(centerX - 2, barTop, 4, fillH);
        fill.setFill(Color.web(fillColor));
        fill.setArcWidth(4);
        fill.setArcHeight(4);

        barPane.getChildren().addAll(track, fill);

        for (int i = 0; i < data.length; i++) {
            String startStr = data[i][0];
            String endStr = data[i][1];
            String courseName = data[i][2];

            var start = java.time.LocalTime.parse(startStr);
            var end = java.time.LocalTime.parse(endStr);
            boolean isPast = now.isAfter(end);
            boolean isCurrent = !now.isBefore(start) && !now.isAfter(end);
            boolean isActive = isPast || isCurrent;

            double yCenter = i * rowHeight + rowHeight / 2.0;

            // dot
            Circle dot = new Circle(centerX, yCenter, 5);
            if (isCurrent) {
                dot.setFill(Color.web(currentColor));
                dot.setStroke(Color.web(currentColor));
                dot.setStrokeWidth(2);
                dot.setRadius(6);
            } else if (isPast) {
                dot.setFill(Color.web(fillColor));
            } else {
                dot.setFill(Color.web(inactiveColor));
            }
            barPane.getChildren().add(dot);

            // time label (left of bar)
            Label timeLabel = new Label(startStr);
            timeLabel.setFont(Font.font("Plus Jakarta Sans", 10));
            timeLabel.setTextFill(Color.web(isActive ? labelMuted : labelDim));
            timeLabel.setLayoutX(4);
            timeLabel.setLayoutY(yCenter - 8);
            timeLabel.setMinWidth(48);
            timeLabel.setAlignment(Pos.CENTER_RIGHT);
            barPane.getChildren().add(timeLabel);

            // course name (right of bar)
            Label nameLabel = new Label(courseName);
            nameLabel.setFont(Font.font("Plus Jakarta Sans", FontWeight.SEMI_BOLD, 12));
            nameLabel.setTextFill(Color.web(isActive ? labelColor : labelDim));
            nameLabel.setLayoutX(centerX + 14);
            nameLabel.setLayoutY(yCenter - 8);
            barPane.getChildren().add(nameLabel);
        }

        scheduleRows.getChildren().add(barPane);
    }

    @FXML
    private void onBreadcrumbListado() {
        if (onBackToList != null) onBackToList.run();
    }

    private void onLanguageChanged() {
        Platform.runLater(() -> {
            updateTexts();
            if (attendanceTable.getItems() != null) {
                loadSampleAttendance();
            }
        });
    }

    private void onThemeChanged() {
        Platform.runLater(() -> {
            applyTheme();
            loadSchedule();
        });
    }

    private void applyTheme() {
        boolean dark = theme.isDark();
        root.getStyleClass().removeAll("root-dark");
        if (dark) root.getStyleClass().add("root-dark");

        String cardBg = dark ? "#1E293B" : "#FFFFFF";
        String cardBorder = dark ? "#334155" : "#E2E8F0";
        scheduleCard.setStyle("-fx-background-color: " + cardBg + "; -fx-background-radius: 24; -fx-border-color: " + cardBorder + "; -fx-border-radius: 24; -fx-border-width: 1;");
    }

    private void updateTexts() {
        String titleBase = lang.get("teachers.detallesTitle", "Detalles del Profesor");
        if (currentTeacher != null) {
            pageTitle.setText(titleBase + ": " + currentTeacher.nombre());
        } else {
            pageTitle.setText(titleBase);
        }
        breadcrumbListado.setText(lang.get("detalleProfesor.breadcrumbListado", "Profesores"));
        lblAsistenciaTitle.setText(lang.get("detalleProfesor.asistenciaTitle", "Registro de Asistencia"));
        lblScheduleTitle.setText(lang.get("detalleProfesor.scheduleTitle", "Horario"));
        colFecha.setText(lang.get("detalle.colFecha", "FECHA"));
        colEntrada.setText(lang.get("detalleProfesor.colEntrada", "ENTRADA"));
        colSalida.setText(lang.get("detalleProfesor.colSalida", "SALIDA"));
        colEstado.setText(lang.get("detalle.colAsistencia", "ESTADO"));
        professorEmail.setText(currentTeacher != null ? currentTeacher.email() : "");
        professorSection.setText(currentTeacher != null ? currentTeacher.seccion() : "");
        professorStatus.setText(currentTeacher != null ? currentTeacher.estado() : "");
    }

    private String c(String light, String dark) {
        return theme.isDark() ? dark : light;
    }

    public static class AsistenciaRow {
        private final StringProperty fecha;
        private final StringProperty entrada;
        private final StringProperty salida;
        private final StringProperty estado;

        public AsistenciaRow(String fecha, String entrada, String salida, String estado) {
            this.fecha = new SimpleStringProperty(fecha);
            this.entrada = new SimpleStringProperty(entrada);
            this.salida = new SimpleStringProperty(salida);
            this.estado = new SimpleStringProperty(estado);
        }

        public String getFecha() { return fecha.get(); }
        public StringProperty fechaProperty() { return fecha; }

        public String getEntrada() { return entrada.get(); }
        public StringProperty entradaProperty() { return entrada; }

        public String getSalida() { return salida.get(); }
        public StringProperty salidaProperty() { return salida; }

        public String getEstado() { return estado.get(); }
        public StringProperty estadoProperty() { return estado; }
    }
}
