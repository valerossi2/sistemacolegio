package controller;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for detalle_profesor.fxml
 *
 * Responsibilities:
 *  1. Load Profesor model data into the UI labels.
 *  2. Inject the correct gender-based SVG avatar dynamically.
 *  3. Populate the attendance TableView with styled status badges.
 */
public class DetalleProfesorController implements Initializable {

    // ─────────────────────── FXML Injections ───────────────────────────────

    @FXML private TextField     searchField;

    /** Circular container where the avatar SVG is injected at runtime. */
    @FXML private StackPane     avatarContainer;

    @FXML private Label         professorName;
    @FXML private Label         professorSubject;

    @FXML private TableView<Asistencia>   attendanceTable;
    @FXML private TableColumn<Asistencia, String> colFecha;
    @FXML private TableColumn<Asistencia, String> colEntrada;
    @FXML private TableColumn<Asistencia, String> colSalida;
    @FXML private TableColumn<Asistencia, String> colEstado;

    // ─────────────────────── SVG Path Constants ────────────────────────────

    /**
     * Material Design "person" silhouette — used for MALE professors.
     * Viewbox: 0 0 24 24  →  scaled to fit a 96 px circle via CSS.
     */
    private static final String SVG_MALE =
            "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4z" +
            "M12 14c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z";

    /**
     * Slightly different silhouette with shoulder-length hair — used for FEMALE professors.
     * Same 24×24 grid, visually distinct curve on the head region.
     */
    private static final String SVG_FEMALE =
            "M12 2a5 5 0 0 1 5 5c0 1.65-.8 3.1-2.03 4.02C17.42 12.37 20 15.19 20 18v2H4v-2" +
            "c0-2.81 2.58-5.63 5.03-6.98A5 5 0 0 1 12 2z" +
            "M12 4a3 3 0 1 0 0 6 3 3 0 0 0 0-6z";

    // ─────────────────────── Lifecycle ─────────────────────────────────────

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Demo: build a Profesor instance and load it.
        // In production, this would be injected via setProfesor(profesor) before
        // the stage is shown (e.g. from a list controller).
        Profesor demo = new Profesor(
                "Dr. Roberto Sánchez",

                "Profesor de Matemáticas y Cálculo",
                Profesor.Genero.MASCULINO
        );
        loadProfesor(demo);
        setupAttendanceTable();
        loadSampleAttendance();
    }

    // ─────────────────────── Public API (called by parent controller) ───────

    /**
     * Call this from the parent/list controller BEFORE showing the stage.
     *
     * <pre>
     *   FXMLLoader loader = new FXMLLoader(getClass().getResource("detalle_profesor.fxml"));
     *   Parent root = loader.load();
     *   DetalleProfesorController ctrl = loader.getController();
     *   ctrl.loadProfesor(selectedProfesor);
     * </pre>
     */
    public void loadProfesor(Profesor profesor) {
        professorName.setText(profesor.getNombreCompleto());
        professorSubject.setText(profesor.getEspecialidad());
        injectAvatarSVG(profesor.getGenero());
    }

    // ─────────────────────── Avatar Injection ──────────────────────────────

    /**
     * Creates a gender-appropriate SVGPath node and injects it into the
     * circular {@code avatarContainer} StackPane declared in the FXML.
     *
     * @param genero  {@link Profesor.Genero#MASCULINO} or {@link Profesor.Genero#FEMENINO}
     */
    private void injectAvatarSVG(Profesor.Genero genero) {
        SVGPath avatar = new SVGPath();

        if (genero == Profesor.Genero.FEMENINO) {
            avatar.setContent(SVG_FEMALE);
            avatar.getStyleClass().add("avatar-svg-female");
        } else {
            // Default to masculine silhouette for MASCULINO or unknown
            avatar.setContent(SVG_MALE);
            avatar.getStyleClass().add("avatar-svg-male");
        }

        // Explicit fill as fallback if CSS is not loaded
        avatar.setFill(Color.web("#475569"));

        // The CSS classes avatar-svg-male / avatar-svg-female apply:
        //   -fx-scale-x: 3.2; -fx-scale-y: 3.2;
        // which scales the 24×24 path to roughly fit the 96 px circle.

        avatarContainer.getChildren().clear();
        avatarContainer.getChildren().add(avatar);
        StackPane.setAlignment(avatar, Pos.CENTER);
    }

    // ─────────────────────── Table Setup ───────────────────────────────────

    private void setupAttendanceTable() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));

        // Custom cell factory for the Estado column — renders a compact badge
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(buildStatusBadge(estado));
            }
        });

        // Remove the extra empty column JavaFX adds by default
        attendanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    /**
     * Builds a compact, pill-shaped status badge for the attendance table.
     *
     * @param estado  "Presente" | "Tardanza" | "Ausente"
     * @return        An HBox containing an SVGPath icon + Label, styled via CSS.
     */
    private HBox buildStatusBadge(String estado) {
        SVGPath icon = new SVGPath();
        Label   lbl  = new Label(estado);

        switch (estado.toLowerCase()) {
            case "presente" -> {
                // Check-circle path
                icon.setContent(
                    "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2z" +
                    "m-2 14.5l-4.5-4.5 1.41-1.41L10 13.67l7.59-7.59L19 7.5l-9 9z"
                );
                icon.getStyleClass().addAll("badge-icon", "badge-presente-icon");
                lbl.getStyleClass().add("badge-presente");
            }
            case "tardanza" -> {
                // Clock path
                icon.setContent(
                    "M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2z" +
                    "M12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67V7z"
                );
                icon.getStyleClass().addAll("badge-icon", "badge-tardanza-icon");
                lbl.getStyleClass().add("badge-tardanza");
            }
            default -> {
                // X-circle for Ausente
                icon.setContent(
                    "M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2z" +
                    "m5 13.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z"
                );
                lbl.getStyleClass().add("badge-ausente");
            }
        }

        HBox badge = new HBox(6, icon, lbl);
        badge.setAlignment(Pos.CENTER_LEFT);
        badge.setStyle("-fx-padding: 0 4 0 0;"); // micro adjustment
        return badge;
    }

    // ─────────────────────── Sample Data ───────────────────────────────────

    private void loadSampleAttendance() {
        attendanceTable.getItems().addAll(
            new Asistencia("14/05/2024", "08:55 AM", "01:45 PM", "Presente"),
            new Asistencia("13/05/2024", "09:00 AM", "01:30 PM", "Presente"),
            new Asistencia("10/05/2024", "08:58 AM", "01:40 PM", "Presente"),
            new Asistencia("09/05/2024", "09:15 AM", "01:35 PM", "Tardanza")
        );
    }

    // ─────────────────────── Action Handlers ───────────────────────────────

    @FXML
    private void handleBack() {
        // Navigate back to the professors list.
        // In a real app you would call your NavigationService or close this stage.
        System.out.println("[DetalleProfesor] handleBack() — navigate to professor list");
    }
}


/* ═══════════════════════════════════════════════════════════════════════════
   MODEL CLASSES  (typically in separate files under com.lumina.model)
   Included here for completeness — move to their own .java files in production.
   ═══════════════════════════════════════════════════════════════════════════ */

// ─────────────────────────────────────────────────────────────────────────────
// File: com/lumina/model/Profesor.java
// ─────────────────────────────────────────────────────────────────────────────

class Profesor {

    public enum Genero { MASCULINO, FEMENINO }

    private final String nombreCompleto;
    private final String especialidad;
    private final Genero genero;

    public Profesor(String nombreCompleto, String especialidad, Genero genero) {
        this.nombreCompleto = nombreCompleto;
        this.especialidad   = especialidad;
        this.genero         = genero;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getEspecialidad()   { return especialidad; }
    public Genero getGenero()         { return genero; }
}


// ─────────────────────────────────────────────────────────────────────────────
// File: com/lumina/model/AsistenciaRecord.java
// ─────────────────────────────────────────────────────────────────────────────

class Asistencia {

    private final String fecha;
    private final String horaEntrada;
    private final String horaSalida;
    private final String estado;

    public Asistencia(String fecha, String horaEntrada,
                       String horaSalida, String estado) {
        this.fecha       = fecha;
        this.horaEntrada = horaEntrada;
        this.horaSalida  = horaSalida;
        this.estado      = estado;
    }

    // JavaFX TableView resolves these via PropertyValueFactory
    public String getFecha()       { return fecha; }
    public String getHoraEntrada() { return horaEntrada; }
    public String getHoraSalida()  { return horaSalida; }
    public String getEstado()      { return estado; }
}
