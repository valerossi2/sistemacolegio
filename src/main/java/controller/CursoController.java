package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

public class CursoController {

    @FXML private BorderPane rootPane;
    @FXML private VBox sideNavBar;
    @FXML private VBox navItemsContainer;
    @FXML private HBox navinicio;
    @FXML private HBox navCursos;
    @FXML private HBox navEstudiantes;
    @FXML private HBox navHorario;
    @FXML private HBox navConfig;
    @FXML private Button btnLogout;
    @FXML private HBox topAppBar;
    @FXML private HBox searchBar;
    @FXML private TextField searchField;
    @FXML private Button btnNotifications;
    @FXML private Circle notifBadge;
    @FXML private StackPane contentStack;
    @FXML private ScrollPane vistaListaCursos;
    @FXML private ScrollPane vistaDetalleCurso;
    @FXML private Label lblTotalCursos;
    @FXML private Button FilterTodos;
    @FXML private Button filterActivos;
    @FXML private TableView<?> cursosTable;
    @FXML private TableColumn<?,?> colGrado;
    @FXML private TableColumn<?,?> colEncargado;
    @FXML private TableColumn<?,?> colSeccion;
    @FXML private TableColumn<?,?> colAlumnos;
    @FXML private TableColumn<?,?> colRendimiento;
    @FXML private TableColumn<?,?> colEstado;
    @FXML private Label lblPaginacion;
    @FXML private Button btnPage1;
    @FXML private Button btnPage2;
    @FXML private Button btnPage3;
    @FXML private Button btnPageNext;
    @FXML private Label breadcrumbCursos;
    @FXML private Label breadcrumbCursoActual;
    @FXML private Label pageTitle;
    @FXML private Button btnPrint;
    @FXML private Button btnEditCourse;
    @FXML private GridPane bentoGrid;
    @FXML private VBox courseOverviewCard;
    @FXML private Label badgeNivel;
    @FXML private Label lblTutorPrincipal;
    @FXML private Label totalStudents;
    @FXML private Label totalTeachers;
    @FXML private Label assignedRoom;
    @FXML private Button btnVerTodosDocentes;
    @FXML private Label studentsSubtitle;
    @FXML private ToggleButton btnViewTable;
    @FXML private ToggleButton btnViewGrid;
    @FXML private TableView<?> studentsTable;
    @FXML private TableColumn<?,?> colStudent;
    @FXML private TableColumn<?,?> colMatricula;
    @FXML private TableColumn<?,?> colAsistencia;
    @FXML private TableColumn<?,?> colAcciones;
    @FXML private Button btnLoadMore;

    @FXML
    public void initialize() {
    }

    @FXML private void onNavinicio() {}
    @FXML private void onNavCursos() {}
    @FXML private void onNavEstudiantes() {}
    @FXML private void onNavHorario() {}
    @FXML private void onNavConfig() {}
    @FXML private void onLogout() {}
    @FXML private void onSearch() {}
    @FXML private void onFilterTodos() {}
    @FXML private void onFilterActivos() {}
    @FXML private void onPage1() {}
    @FXML private void onPage2() {}
    @FXML private void onPage3() {}
    @FXML private void onPageNext() {}
    @FXML private void onBreadcrumbCursos() {}
    @FXML private void onImprimirReporte() {}
    @FXML private void onEditarCurso() {}
    @FXML private void onVerTodosDocentes() {}
    @FXML private void onCargarMas() {}
}
