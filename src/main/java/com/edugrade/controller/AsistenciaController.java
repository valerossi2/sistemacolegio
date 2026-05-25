package com.edugrade.controller;

// ═══════════════════════════════════════════════════════════════
//  AsistenciaController.java
//  Fragmento de código completo:  Controller + CellFactories
//  Incluye:
//   • Modelo de datos  EstudianteModel
//   • CellFactory para columna "Estudiante" con avatar SVG por género
//   • CellFactory para columna "Asistencia" con botones de estado
//   • Inicialización de la tabla con datos de ejemplo
// ═══════════════════════════════════════════════════════════════

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AsistenciaController implements Initializable {

    // ─── FXML Injections ────────────────────────────────────────
    @FXML private TextField         searchField;
    @FXML private ComboBox<String>  gradeSelector;
    @FXML private ComboBox<String>  sectionSelector;
    @FXML private Label             currentDateValue;
    @FXML private Label             presentesCount;
    @FXML private Label             ausentesCount;
    @FXML private Label             excusasCount;
    @FXML private Label             enrolledCountLabel;
    @FXML private TableView<EstudianteModel>        attendanceTable;
    @FXML private TableColumn<EstudianteModel, EstudianteModel> colEstudiante;
    @FXML private TableColumn<EstudianteModel, String>          colMatricula;
    @FXML private TableColumn<EstudianteModel, String>          colFecha;
    @FXML private TableColumn<EstudianteModel, EstudianteModel> colAsistencia;
    @FXML private Button            saveAttendanceBtn;
    @FXML private Button            loadMoreBtn;

    // ─── Data ────────────────────────────────────────────────────
    private final ObservableList<EstudianteModel> estudiantes = FXCollections.observableArrayList();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ════════════════════════════════════════════════════════════
    //  initialize
    // ════════════════════════════════════════════════════════════
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentDateValue.setText(LocalDate.now().format(FMT));
        gradeSelector.setItems(FXCollections.observableArrayList("5to", "4to", "3ro"));
        gradeSelector.getSelectionModel().selectFirst();
        sectionSelector.setItems(FXCollections.observableArrayList("E", "A", "B"));
        sectionSelector.getSelectionModel().selectFirst();
        loadSampleData();
        configurarColumnas();
        attendanceTable.setItems(estudiantes);
        actualizarResumen();
    }

    // ════════════════════════════════════════════════════════════
    //  Carga de datos de ejemplo
    // ════════════════════════════════════════════════════════════
    private void loadSampleData() {
        estudiantes.addAll(
            new EstudianteModel("Garcia, Alejandro",  "alejandro.g@student.edu", "STU-2023-045", Genero.MASCULINO),
            new EstudianteModel("Martinez, Lucia",    "lucia.m@student.edu",     "STU-2023-089", Genero.FEMENINO),
            new EstudianteModel("Perez, Carlos",      "carlos.p@student.edu",    "STU-2023-112", Genero.MASCULINO),
            new EstudianteModel("Lopez, Sofia",       "sofia.l@student.edu",     "STU-2023-134", Genero.FEMENINO),
            new EstudianteModel("Ramirez, Diego",     "diego.r@student.edu",     "STU-2023-158", Genero.MASCULINO),
            new EstudianteModel("Torres, Valentina",  "valentina.t@student.edu", "STU-2023-177", Genero.FEMENINO),
            new EstudianteModel("Herrera, Sebastian", "sebastian.h@student.edu", "STU-2023-201", Genero.MASCULINO),
            new EstudianteModel("Morales, Isabella",  "isabella.m@student.edu",  "STU-2023-223", Genero.FEMENINO)
        );
    }

    // ════════════════════════════════════════════════════════════
    //  Configuración de columnas
    // ════════════════════════════════════════════════════════════
    private void configurarColumnas() {

        // ── Columna Estudiante ──────────────────────────────────
        colEstudiante.setCellValueFactory(data ->
            new SimpleObjectProperty<>(data.getValue()));
        colEstudiante.setCellFactory(col -> new EstudianteCellFactory());

        // ── Columna ID Matrícula ────────────────────────────────
        colMatricula.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getMatricula()));
        colMatricula.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); return; }
                Label lbl = new Label(item);
                lbl.getStyleClass().add("cell-matricula");
                setGraphic(lbl);
                setText(null);
            }
        });

        // ── Columna Fecha ───────────────────────────────────────
        colFecha.setCellValueFactory(data ->
            new SimpleStringProperty(LocalDate.now().format(FMT)));
        colFecha.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); return; }
                Label lbl = new Label(item);
                lbl.getStyleClass().add("cell-fecha");
                setGraphic(lbl);
                setText(null);
            }
        });

        // ── Columna Asistencia ──────────────────────────────────
        colAsistencia.setCellValueFactory(data ->
            new SimpleObjectProperty<>(data.getValue()));
        colAsistencia.setCellFactory(col -> new AsistenciaCellFactory(this));
    }

    // ════════════════════════════════════════════════════════════
    //  Actualizar panel de resumen
    // ════════════════════════════════════════════════════════════
    void actualizarResumen() {
        long presentes = estudiantes.stream()
            .filter(e -> e.getEstado() == EstadoAsistencia.PRESENTE).count();
        long ausentes = estudiantes.stream()
            .filter(e -> e.getEstado() == EstadoAsistencia.AUSENTE).count();
        long excusas = estudiantes.stream()
            .filter(e -> e.getEstado() == EstadoAsistencia.EXCUSA).count();

        presentesCount.setText(String.valueOf(presentes));
        ausentesCount.setText(String.valueOf(ausentes));
        excusasCount.setText(String.valueOf(excusas));
    }

    // ════════════════════════════════════════════════════════════
    //  FXML Handlers
    // ════════════════════════════════════════════════════════════
    @FXML
    private void handleSaveAttendance() {
        long sinRegistro = estudiantes.stream()
            .filter(e -> e.getEstado() == EstadoAsistencia.SIN_MARCAR).count();
        if (sinRegistro > 0) {
            new Alert(Alert.AlertType.WARNING,
                sinRegistro + " estudiante(s) sin marcar asistencia.",
                ButtonType.OK).showAndWait();
            return;
        }
        new Alert(Alert.AlertType.INFORMATION,
            "Asistencia guardada correctamente para " + estudiantes.size() + " estudiantes.",
            ButtonType.OK).showAndWait();
    }

    @FXML
    private void handleLoadMore() {
        // TODO: paginar datos desde el servicio backend
    }

    // ════════════════════════════════════════════════════════════
    //  ╔═══════════════════════════════════╗
    //  ║  CELL FACTORY – Columna Estudiante ║
    //  ╚═══════════════════════════════════╝
    //  Renderiza avatar SVG diferenciado por género
    // ════════════════════════════════════════════════════════════
    private static class EstudianteCellFactory
            extends TableCell<EstudianteModel, EstudianteModel> {

        // ── SVG path: silueta masculina ─────────────────────────
        private static final String PATH_MALE =
            "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 " +
            "1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z";

        // ── SVG path: silueta femenina ──────────────────────────
        // Cuerpo con cabeza y silueta de falda/vestido
        private static final String PATH_FEMALE =
            "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 " +
            "1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v1h16v-1c0-2.66-5.33-4-8-4z" +
            "M9 18l1.5 3h3L15 18H9z";

        @Override
        protected void updateItem(EstudianteModel estudiante, boolean empty) {
            super.updateItem(estudiante, empty);
            if (empty || estudiante == null) {
                setGraphic(null);
                return;
            }

            // ── Avatar ──────────────────────────────────────────
            boolean esMasculino = estudiante.getGenero() == Genero.MASCULINO;
            String  svgPath     = esMasculino ? PATH_MALE : PATH_FEMALE;
            String  avatarClass = esMasculino ? "avatar-male" : "avatar-female";
            String  svgClass    = esMasculino ? "svg-avatar-male" : "svg-avatar-female";

            SVGPath svgNode = new SVGPath();
            svgNode.setContent(svgPath);
            svgNode.getStyleClass().add(svgClass);

            StackPane avatarPane = new StackPane(svgNode);
            avatarPane.getStyleClass().add(avatarClass);
            avatarPane.setMinSize(36, 36);
            avatarPane.setMaxSize(36, 36);
            StackPane.setAlignment(svgNode, Pos.CENTER);

            // ── Name + email ────────────────────────────────────
            Label nameLabel  = new Label(estudiante.getNombre());
            nameLabel.getStyleClass().add("cell-student-name");

            Label emailLabel = new Label(estudiante.getEmail());
            emailLabel.getStyleClass().add("cell-student-email");

            VBox textBox = new VBox(2, nameLabel, emailLabel);
            textBox.setAlignment(Pos.CENTER_LEFT);

            // ── Assemble ────────────────────────────────────────
            HBox cell = new HBox(12, avatarPane, textBox);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.setPadding(new Insets(0));

            setGraphic(cell);
            setText(null);
        }
    }

    // ════════════════════════════════════════════════════════════
    //  ╔══════════════════════════════════════╗
    //  ║  CELL FACTORY – Columna Asistencia   ║
    //  ╚══════════════════════════════════════╝
    //  Botones mutuamente excluyentes: Presente | Ausente | Excusa
    // ════════════════════════════════════════════════════════════
    private static class AsistenciaCellFactory
            extends TableCell<EstudianteModel, EstudianteModel> {

        private final Button btnPresente;
        private final Button btnAusente;
        private final Button btnExcusa;
        private final AsistenciaController controller;

        AsistenciaCellFactory(AsistenciaController controller) {
            this.controller = controller;

            btnPresente = new Button("Presente");
            btnAusente  = new Button("Ausente");
            btnExcusa   = new Button("Excusa");

            // Base styles
            btnPresente.getStyleClass().addAll("btn-attendance", "btn-present");
            btnAusente.getStyleClass().addAll("btn-attendance",  "btn-absent");
            btnExcusa.getStyleClass().addAll("btn-attendance",   "btn-excuse");

            // Handlers — read current row via getTableRow()
            btnPresente.setOnAction(e -> marcarEstado(EstadoAsistencia.PRESENTE));
            btnAusente.setOnAction(e  -> marcarEstado(EstadoAsistencia.AUSENTE));
            btnExcusa.setOnAction(e   -> marcarEstado(EstadoAsistencia.EXCUSA));
        }

        private void marcarEstado(EstadoAsistencia nuevoEstado) {
            EstudianteModel est = getItem();
            if (est == null) return;
            est.setEstado(nuevoEstado);
            refrescarEstilos(nuevoEstado);
            controller.actualizarResumen();
        }

        /** Aplica estilos "active" al botón seleccionado y restaura los demás */
        private void refrescarEstilos(EstadoAsistencia estado) {
            // Reset all to base style
            setButtonStyle(btnPresente, "btn-present",  "btn-present-active",
                           estado == EstadoAsistencia.PRESENTE);
            setButtonStyle(btnAusente,  "btn-absent",   "btn-absent-active",
                           estado == EstadoAsistencia.AUSENTE);
            setButtonStyle(btnExcusa,   "btn-excuse",   "btn-excuse-active",
                           estado == EstadoAsistencia.EXCUSA);
        }

        private void setButtonStyle(Button btn, String base, String active, boolean isActive) {
            btn.getStyleClass().removeAll(base, active);
            btn.getStyleClass().add(isActive ? active : base);
        }

        @Override
        protected void updateItem(EstudianteModel estudiante, boolean empty) {
            super.updateItem(estudiante, empty);
            if (empty || estudiante == null) {
                setGraphic(null);
                return;
            }
            // Sync button visual state with current model state
            refrescarEstilos(estudiante.getEstado());

            HBox box = new HBox(8, btnPresente, btnAusente, btnExcusa);
            box.setAlignment(Pos.CENTER_LEFT);
            box.setPadding(new Insets(0));
            setGraphic(box);
            setText(null);
        }
    }

    // ════════════════════════════════════════════════════════════
    //  MODEL CLASSES
    // ════════════════════════════════════════════════════════════

    /** Género del estudiante */
    public enum Genero {
        MASCULINO, FEMENINO
    }

    /** Estado de asistencia */
    public enum EstadoAsistencia {
        SIN_MARCAR, PRESENTE, AUSENTE, EXCUSA
    }

    /** Modelo observable de un estudiante */
    public static class EstudianteModel {

        private final StringProperty  nombre    = new SimpleStringProperty();
        private final StringProperty  email     = new SimpleStringProperty();
        private final StringProperty  matricula = new SimpleStringProperty();
        private final ObjectProperty<Genero>            genero = new SimpleObjectProperty<>();
        private final ObjectProperty<EstadoAsistencia>  estado = new SimpleObjectProperty<>(EstadoAsistencia.SIN_MARCAR);

        public EstudianteModel(String nombre, String email, String matricula, Genero genero) {
            this.nombre.set(nombre);
            this.email.set(email);
            this.matricula.set(matricula);
            this.genero.set(genero);
        }

        // Getters / setters
        public String getNombre()              { return nombre.get(); }
        public String getEmail()               { return email.get(); }
        public String getMatricula()           { return matricula.get(); }
        public Genero getGenero()              { return genero.get(); }
        public EstadoAsistencia getEstado()    { return estado.get(); }
        public void   setEstado(EstadoAsistencia s) { estado.set(s); }

        // Property accessors (for bindings if needed)
        public StringProperty  nombreProperty()    { return nombre; }
        public StringProperty  emailProperty()     { return email; }
        public StringProperty  matriculaProperty() { return matricula; }
        public ObjectProperty<Genero>           generoProperty() { return genero; }
        public ObjectProperty<EstadoAsistencia> estadoProperty() { return estado; }
    }
}
