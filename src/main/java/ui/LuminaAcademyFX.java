package ui;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class LuminaAcademyFX extends Application {

    // --- PALETA DE COLORES EXACTA DEL DESIGN.MD ---
    private final String COLOR_PRIMARY = "#004ac6";
    private final String COLOR_PRIMARY_CONTAINER = "#2563eb";
    private final String COLOR_BACKGROUND = "#f9f9ff";
    private final String COLOR_SURFACE = "#f9f9ff";
    private final String COLOR_SURFACE_LOW = "#f0f3ff";
    private final String COLOR_SURFACE_CONTAINER = "#e7eeff";
    private final String COLOR_ON_SURFACE = "#111c2d";
    private final String COLOR_ON_SURFACE_VARIANT = "#434655";
    private final String COLOR_OUTLINE = "#737686";
    private final String COLOR_WHITE = "#ffffff";
    private final String COLOR_PRIMARY_FIXED = "#dbe1ff";
    private final String COLOR_SECONDARY_FIXED = "#c0e8ff";
    private final String COLOR_TERTIARY_FIXED = "#dbe4ea";

    private BorderPane root;
    private StackPane contentArea;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + COLOR_BACKGROUND + ";");

        // 1. Sidebar
        root.setLeft(createSidebar());

        // 2. TopAppBar
        root.setTop(createHeader());

        // 3. Main Canvas
        VBox mainCanvas = new VBox(32);
        mainCanvas.setPadding(new Insets(40, 40, 40, 40));
        mainCanvas.setAlignment(Pos.TOP_CENTER);
        mainCanvas.setMaxWidth(1200);

        // --- SECCIÓN 1: HEADER ---
        VBox headerSec = new VBox(8);
        Text h1 = new Text("Panel de Administración Central");
        h1.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 32));
        h1.setFill(Color.web(COLOR_ON_SURFACE));
        
        Text sub = new Text("Bienvenido de nuevo. Aquí tienes un resumen del estado institucional hoy.");
        sub.setFont(Font.font("Plus Jakarta Sans", 16));
        sub.setFill(Color.web(COLOR_OUTLINE));
        headerSec.getChildren().addAll(h1, sub);

        // --- SECCIÓN 2: KPI BENTO GRID ---
        HBox kpiGrid = new HBox(24);
        kpiGrid.setAlignment(Pos.CENTER);
        kpiGrid.getChildren().addAll(
            createKpiCard("Total Estudiantes", "1,250", COLOR_SECONDARY_FIXED, "👤"),
            createKpiCard("Total Cursos", "35", COLOR_PRIMARY_FIXED, "📈"),
            createKpiCard("Profesores", "42", COLOR_TERTIARY_FIXED, "🎓"),
            createKpiCard("Asistencia Estudiantes", "92%", COLOR_SECONDARY_FIXED, "✔️")
        );

        // --- SECCIÓN 3: DUAL COLUMN ---
        HBox middleSection = new HBox(24);
        
        // Gestión de Cursos (8/12)
        VBox coursesBox = createCourseManagement();
        HBox.setHgrow(coursesBox, Priority.ALWAYS);
        
        // Desempeño (4/12)
        VBox performanceBox = createPerformancePanel();
        performanceBox.setPrefWidth(380);

        middleSection.getChildren().addAll(coursesBox, performanceBox);

        // --- SECCIÓN 4: HORARIO DE HOY ---
        VBox scheduleBox = createSchedulePanel();

        mainCanvas.getChildren().addAll(headerSec, kpiGrid, middleSection, scheduleBox);
        
        StackPane centerWrapper = new StackPane(mainCanvas);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        root.setCenter(centerWrapper);

        // 4. Floating Action Button (FAB)
        root.setCenter(stackWithFab(centerWrapper));

        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add("data:text/css," + getInternalCSS());
        
        primaryStage.setTitle("Lumina Academy - Admin Portal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane stackWithFab(Region center) {
        StackPane stack = new StackPane(center);
        stack.setAlignment(Pos.TOP_CENTER);

        // FAB Bolt
        StackPane fab = new StackPane();
        fab.setPrefSize(64, 64);
        fab.getStyleClass().add("fab-button");
        Text bolt = new Text("⚡");
        bolt.setFont(Font.font(24));
        bolt.setFill(Color.WHITE);
        fab.getChildren().add(bolt);

        StackPane.setMargin(fab, new Insets(0, 0, 40, 0));
        StackPane.setAlignment(fab, Pos.BOTTOM_RIGHT);
        
        // El FAB debe estar en una capa superior
        Pane fabLayer = new Pane(fab);
        fabLayer.setPrefSize(1300, 850);
        
        // Envolver todo
        StackPane rootStack = new StackPane(center, fabLayer);
        return rootStack;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(12);
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(32, 24, 32, 24));
        sidebar.setStyle("-fx-background-color: " + COLOR_WHITE + ";");

        // Logo
        HBox logo = new HBox(12);
        logo.setPadding(new Insets(0, 0, 48, 0));
        logo.setAlignment(Pos.CENTER_LEFT);
        
        Circle logoIcon = new Circle(20, Color.web(COLOR_PRIMARY));
        // Simulation of the bulb icon inside the circle
        Text bulb = new Text("💡");
        bulb.setFont(Font.font(14));
        bulb.setFill(Color.WHITE);
        StackPane logoStack = new StackPane(logoIcon, bulb);
        
        VBox logoText = new VBox(2);
        Text lTitle = new Text("Lumina Academy");
        lTitle.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        lTitle.setFill(Color.web(COLOR_PRIMARY));
        Text lSub = new Text("ADMIN PORTAL");
        lSub.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        lSub.setFill(Color.web(COLOR_OUTLINE));
        logoText.getChildren().addAll(lTitle, lSub);
        
        logo.getChildren().addAll(logoStack, logoText);
        sidebar.getChildren().add(logo);

        // Navegación
        String[] items = {"Inicio", "Estudiantes", "Cursos", "Horario", "Configuración"};
        for (int i = 0; i < items.length; i++) {
            Button btn = new Button(items[i]);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setPadding(new Insets(12, 16, 12, 16));
            if (i == 0) {
                btn.getStyleClass().add("sidebar-active");
            } else {
                btn.getStyleClass().add("nav-button");
            }
            sidebar.getChildren().add(btn);
        }

        return sidebar;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(0, 32, 0, 32));
        header.setPrefHeight(72);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + COLOR_WHITE + "; -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER + " transparent;");

        // Search
        HBox searchBox = new HBox(12);
        searchBox.setPadding(new Insets(0, 20, 0, 20));
        searchBox.getStyleClass().add("search-container");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        Text searchIcon = new Text("🔍");
        searchIcon.setFont(Font.font(14));
        searchIcon.setFill(Color.web(COLOR_OUTLINE));

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar expedientes, cursos o reportes...");
        searchField.getStyleClass().add("search-input");
        
        searchBox.getChildren().addAll(searchIcon, searchField);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Icons
        HBox iconBox = new HBox(24);
        iconBox.setAlignment(Pos.CENTER);
        Text notifIcon = new Text("🔔");
        notifIcon.setFont(Font.font(20));
        Text helpIcon = new Text("❓");
        helpIcon.setFont(Font.font(20));
        iconBox.getChildren().addAll(notifIcon, helpIcon);

        Region spacer2 = new Region();
        spacer2.setPrefWidth(24);

        // User
        HBox userBox = new HBox(12);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        VBox uText = new VBox(2);
        uText.setAlignment(Pos.CENTER_RIGHT);
        Text uName = new Text("Admin User");
        uName.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 14));
        Text uEmail = new Text("ADMIN@LUMINA.EDU");
        uEmail.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        uEmail.setFill(Color.web(COLOR_OUTLINE));
        uText.getChildren().addAll(uName, uEmail);
        
        Circle avatar = new Circle(20, Color.web(COLOR_PRIMARY_FIXED));
        // Placeholder for avatar image
        Text avatarText = new Text("👤");
        avatarText.setFont(Font.font(14));
        StackPane avatarStack = new StackPane(avatar, avatarText);
        
        userBox.getChildren().addAll(uText, avatarStack);

        header.getChildren().addAll(searchBox, spacer, iconBox, spacer2, userBox);
        return header;
    }

    private VBox createKpiCard(String label, String value, String color, String icon) {
        HBox card = new HBox(20);
        card.setPadding(new Insets(24));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("glass-card");

        // Icon in a light blue circle
        Circle iconCircle = new Circle(24, Color.web(COLOR_SURFACE_CONTAINER));
        Text iconTxt = new Text(icon);
        iconTxt.setFont(Font.font(16));
        iconTxt.setFill(Color.web(COLOR_PRIMARY));
        StackPane iconStack = new StackPane(iconCircle, iconTxt);
        
        VBox text = new VBox(4);
        Text lbl = new Text(label.toUpperCase());
        lbl.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 11));
        lbl.setFill(Color.web(COLOR_OUTLINE));
        Text val = new Text(value);
        val.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 24));
        val.setFill(Color.web(COLOR_ON_SURFACE));
        text.getChildren().addAll(lbl, val);

        card.getChildren().addAll(iconStack, text);
        VBox wrapper = new VBox(card);
        HBox.setHgrow(wrapper, Priority.ALWAYS);
        return wrapper;
    }

    private VBox createCourseManagement() {
        VBox panel = new VBox(0);
        panel.getStyleClass().add("glass-card");
        panel.setPadding(new Insets(0));

        // Header
        HBox head = new HBox();
        head.setPadding(new Insets(24));
        head.setAlignment(Pos.CENTER_LEFT);
        Text title = new Text("Gestión de Cursos");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 20));
        title.setFill(Color.web(COLOR_ON_SURFACE));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnAll = new Button("Ver todos");
        btnAll.getStyleClass().add("text-button");
        head.getChildren().addAll(title, spacer, btnAll);

        // Tabla
        VBox table = new VBox();
        table.setPadding(new Insets(0, 24, 24, 24));
        
        HBox cols = new HBox();
        cols.setPadding(new Insets(12, 0, 12, 0));
        cols.getChildren().addAll(
            createColH("CURSO", 200), 
            createColH("PROFESOR", 200), 
            createColH("ALUMNOS", 150), 
            createColH("RENDIMIENTO", 150)
        );

        table.getChildren().addAll(cols);
        table.getChildren().addAll(
            createCourseRow("Introducción a la IA", "Dr. Roberto Sánchez", "32 Estudiantes", 0.92, "9.2"),
            createCourseRow("Cálculo Avanzado", "Dra. Elena Méndez", "28 Estudiantes", 0.78, "7.8"),
            createCourseRow("Literatura Moderna", "Prof. Juan Carlos Rico", "40 Estudiantes", 0.85, "8.5"),
            createCourseRow("Diseño UX/UI", "Mtra. Sofía Valdéz", "24 Estudiantes", 0.88, "8.8")
        );

        panel.getChildren().addAll(head, table);
        return panel;
    }

    private HBox createColH(String t, double w) {
        Text txt = new Text(t);
        txt.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 11));
        txt.setFill(Color.web(COLOR_OUTLINE));
        HBox box = new HBox(txt);
        box.setPrefWidth(w);
        return box;
    }

    private HBox createCourseRow(String name, String prof, String st, double prog, String score) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(15, 0, 15, 0));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("course-row");

        Text tName = new Text(name);
        tName.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 14));
        tName.setFill(Color.web(COLOR_ON_SURFACE));
        
        HBox progBox = new HBox(10);
        progBox.setAlignment(Pos.CENTER_RIGHT);
        
        Rectangle bg = new Rectangle(60, 6, Color.web(COLOR_SURFACE_CONTAINER));
        bg.setArcWidth(6); bg.setArcHeight(6);
        Rectangle fill = new Rectangle(60 * prog, 6, Color.web(COLOR_PRIMARY));
        fill.setArcWidth(6); fill.setArcHeight(6);
        
        Text tScore = new Text(score);
        tScore.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 13));
        tScore.setFill(Color.web(COLOR_PRIMARY));

        progBox.getChildren().addAll(new Pane(bg, fill), tScore);

        row.getChildren().addAll(
            new HBox(tName), new HBox(new Text(prof)), new HBox(new Text(st)), progBox
        );
        row.getChildren().get(0).setPrefWidth(200);
        row.getChildren().get(1).setPrefWidth(200);
        row.getChildren().get(2).setPrefWidth(150);
        row.getChildren().get(3).setPrefWidth(150);

        return row;
    }

    private VBox createPerformancePanel() {
        VBox panel = new VBox(20);
        panel.getStyleClass().add("glass-card");
        panel.setPadding(new Insets(24));

        HBox head = new HBox();
        Text title = new Text("Desempeño");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 20));
        title.setFill(Color.web(COLOR_ON_SURFACE));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Text more = new Text("⋯");
        more.setFont(Font.font(20));
        head.getChildren().addAll(title, spacer, more);

        Text sub = new Text("Promedio general mensual (6 meses)");
        sub.setFont(Font.font("Plus Jakarta Sans", 14));
        sub.setFill(Color.web(COLOR_OUTLINE));

        // Gráfico
        HBox chart = new HBox(16);
        chart.setAlignment(Pos.BOTTOM_CENTER);
        chart.setPrefHeight(200);
        
        String[] labels = {"5to E", "6to A", "4to B", "4to C", "5to A", "6to B"};
        int[] heights = {100, 130, 120, 110, 180, 150};

        for (int i = 0; i < labels.length; i++) {
            VBox barBox = new VBox(12);
            barBox.setAlignment(Pos.BOTTOM_CENTER);
            // Use primary for the highest bar, primary_fixed for others
            Color barColor = (i == 4) ? Color.web(COLOR_PRIMARY) : Color.web(COLOR_PRIMARY_FIXED);
            Rectangle bar = new Rectangle(32, heights[i], barColor);
            bar.setArcWidth(12); bar.setArcHeight(12);
            Text label = new Text(labels[i]);
            label.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 11));
            label.setFill(i == 4 ? Color.web(COLOR_PRIMARY) : Color.web(COLOR_OUTLINE));
            barBox.getChildren().addAll(bar, label);
            chart.getChildren().add(barBox);
        }

        HBox footer = new HBox();
        Text fT = new Text("Crecimiento Semestral");
        fT.setFont(Font.font("Plus Jakarta Sans", 14));
        fT.setFill(Color.web(COLOR_OUTLINE));
        Region s2 = new Region();
        HBox.setHgrow(s2, Priority.ALWAYS);
        Label fV = new Label("+12.4%");
        fV.getStyleClass().add("growth-badge");
        footer.getChildren().addAll(fT, s2, fV);

        panel.getChildren().addAll(head, sub, chart, footer);
        return panel;
    }

    private VBox createSchedulePanel() {
        VBox panel = new VBox(20);
        panel.getStyleClass().add("glass-card");
        panel.setPadding(new Insets(24));

        Text title = new Text("Horario de Hoy");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 20));
        title.setFill(Color.web(COLOR_ON_SURFACE));

        // LISTA
        VBox list = new VBox(12);
        list.getChildren().addAll(
            createScheduleRow("08:00", "Matemáticas Avanzadas", "Salón 402 • Prof. Sánchez"),
            createScheduleRow("10:00", "Historia Universal", "Biblioteca • Dra. Méndez"),
            createScheduleRow("12:00", "Química Orgánica", "Laboratorio B • Prof. Rico"),
            createScheduleRow("14:00", "Física Cuántica", "Laboratorio A • Prof. Einstein"),
            createScheduleRow("16:00", "Arte Moderno", "Galería • Prof. Picasso"),
            createScheduleRow("18:00", "Inglés Técnico", "Aula 10 • Prof. Smith")
        );

        ScrollPane sp = new ScrollPane(list);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        sp.setPrefHeight(300);

        panel.getChildren().addAll(title, sp);
        return panel;
    }

    private HBox createScheduleRow(String time, String subj, String det) {
        HBox row = new HBox(20);
        row.setPadding(new Insets(16, 0, 16, 0));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("schedule-row");

        // Clock Icon
        Circle iconCircle = new Circle(24, Color.web(COLOR_SURFACE_LOW));
        Text clockIcon = new Text("🕒");
        clockIcon.setFont(Font.font(14));
        StackPane iconStack = new StackPane(iconCircle, clockIcon);
        
        VBox text = new VBox(4);
        Text t1 = new Text(time + " - " + subj);
        t1.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 15));
        t1.setFill(Color.web(COLOR_ON_SURFACE));
        Text t2 = new Text(det);
        t2.setFont(Font.font("Plus Jakarta Sans", 13));
        t2.setFill(Color.web(COLOR_OUTLINE));
        text.getChildren().addAll(t1, t2);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text arrow = new Text("❯");
        arrow.setFont(Font.font(14));
        arrow.setFill(Color.web(COLOR_OUTLINE));

        row.getChildren().addAll(iconStack, text, spacer, arrow);
        return row;
    }

    private String getInternalCSS() {
        return ".nav-button { -fx-background-color: transparent; -fx-text-fill: " + COLOR_ON_SURFACE_VARIANT + "; -fx-cursor: hand; -fx-font-family: 'Plus Jakarta Sans'; -fx-font-weight: bold; } " +
               ".nav-button:hover { -fx-background-color: " + COLOR_SURFACE_LOW + "; -fx-text-fill: " + COLOR_PRIMARY + "; } " +
               ".sidebar-active { -fx-background-color: " + COLOR_PRIMARY_CONTAINER + "; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 12; -fx-font-family: 'Plus Jakarta Sans'; -fx-font-weight: bold; } " +
               ".search-container { -fx-background-color: " + COLOR_SURFACE_LOW + "; -fx-background-radius: 32; -fx-border-color: " + COLOR_SURFACE_CONTAINER + "; -fx-border-radius: 32; } " +
               ".search-input { -fx-background-color: transparent; -fx-border-color: transparent; -fx-font-family: 'Plus Jakarta Sans'; } " +
               ".glass-card { -fx-background-color: " + COLOR_WHITE + "; -fx-background-radius: 24; -fx-border-color: " + COLOR_SURFACE_CONTAINER + "; -fx-border-radius: 24; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5); } " +
               ".course-row { -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER + " transparent; } " +
               ".schedule-row { -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER + " transparent; } " +
               ".text-button { -fx-text-fill: " + COLOR_PRIMARY + "; -fx-background-color: transparent; -fx-underline: false; -fx-cursor: hand; -fx-font-weight: bold; } " +
               ".growth-badge { -fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-weight: bold; -fx-padding: 4 12 4 12; -fx-background-radius: 16; } " +
               ".fab-button { -fx-background-color: " + COLOR_PRIMARY + "; -fx-background-radius: 32; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.4), 15, 0, 0, 8); }";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
