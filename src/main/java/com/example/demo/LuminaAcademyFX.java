package com.example.demo;

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
        HBox root = new HBox();
        root.setStyle("-fx-background-color: " + COLOR_BACKGROUND + ";");

        // 1. Sidebar
        VBox sidebar = createSidebar();
        root.getChildren().add(sidebar);

        // 2. Main Content Area (Header + Canvas)
        VBox contentArea = new VBox();
        contentArea.setPadding(new Insets(20, 0, 0, 0)); // Added top padding to lower the header slightly
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // TopAppBar
        HBox header = createHeader();
        contentArea.getChildren().add(header);

        // Main Canvas
        VBox mainCanvas = new VBox(32);
        // Reduced top padding to 40 to bring the schedule back into view
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
        performanceBox.setPrefWidth(320);

        middleSection.getChildren().addAll(coursesBox, performanceBox);

        // --- SECCIÓN 4: HORARIO DE HOY ---
        VBox scheduleBox = createSchedulePanel();

        mainCanvas.getChildren().addAll(headerSec, kpiGrid, middleSection, scheduleBox);
        
        StackPane centerWrapper = new StackPane(mainCanvas);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        contentArea.getChildren().add(centerWrapper);

        root.getChildren().add(contentArea);

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
        VBox sidebar = new VBox(16);
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(32, 20, 32, 20));
        sidebar.setStyle("-fx-background-color: " + COLOR_WHITE + ";");

        // Logo
        HBox logo = new HBox(12);
        logo.setPadding(new Insets(0, 0, 48, 0));
        logo.setAlignment(Pos.CENTER_LEFT);
        
        Circle logoIcon = new Circle(20, Color.web(COLOR_PRIMARY));
        Text logoSymbol = new Text("💡");
        logoSymbol.setFont(Font.font(14));
        logoSymbol.setFill(Color.WHITE);
        StackPane logoStack = new StackPane(logoIcon, logoSymbol);
        
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

        // Navegación con Iconos
        String[][] items = {
            {"Inicio", "🏠"},
            {"Estudiantes", "🎓"},
            {"Cursos", "📖"},
            {"Horario", "📅"},
            {"Configuración", "⚙️"}
        };

        for (int i = 0; i < items.length; i++) {
            HBox btnContainer = new HBox(12);
            btnContainer.setAlignment(Pos.CENTER_LEFT);
            btnContainer.setPadding(new Insets(12, 16, 12, 16));
            btnContainer.setPrefWidth(Double.MAX_VALUE);
            
            Text icon = new Text(items[i][1]);
            icon.setFont(Font.font(18));
            
            Text label = new Text(items[i][0]);
            label.setFont(Font.font("Plus Jakarta Sans", FontWeight.MEDIUM, 15));
            
            if (i == 0) {
                btnContainer.getStyleClass().add("sidebar-active");
                label.setFill(Color.WHITE);
                icon.setFill(Color.WHITE);
            } else {
                btnContainer.getStyleClass().add("nav-item");
                label.setFill(Color.web(COLOR_ON_SURFACE));
                icon.setFill(Color.web(COLOR_ON_SURFACE));
            }
            
            btnContainer.getChildren().addAll(icon, label);
            sidebar.getChildren().add(btnContainer);
        }

        return sidebar;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(0, 32, 0, 32));
        header.setPrefHeight(64);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + COLOR_WHITE + "; -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER + " transparent;");

        // Left Spacer to push search bar to the right
        Region leftSpacer = new Region();
        leftSpacer.setPrefWidth(40);

        // Search Bar
        HBox searchBox = new HBox(12);
        searchBox.setPadding(new Insets(0, 20, 0, 20));
        searchBox.getStyleClass().add("search-container");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefWidth(500);
        searchBox.setPrefHeight(44);
        
        Text searchIcon = new Text("🔍");
        searchIcon.setFont(Font.font(14));
        searchIcon.setFill(Color.web(COLOR_OUTLINE));

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar expedientes, cursos o reportes...");
        searchField.getStyleClass().add("search-input");
        
        searchBox.getChildren().addAll(searchIcon, searchField);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right Side Container
        HBox rightContainer = new HBox(24);
        rightContainer.setAlignment(Pos.CENTER_RIGHT);

        // Icons
        HBox iconBox = new HBox(20);
        iconBox.setAlignment(Pos.CENTER);
        
        StackPane notifStack = new StackPane();
        Text notifBell = new Text("🔔");
        notifBell.setFont(Font.font(20));
        Circle redDot = new Circle(4, Color.RED);
        StackPane.setAlignment(redDot, Pos.TOP_RIGHT);
        notifStack.getChildren().addAll(notifBell, redDot);
        
        StackPane helpStack = new StackPane();
        Circle helpCircle = new Circle(10, Color.TRANSPARENT);
        helpCircle.setStroke(Color.web(COLOR_OUTLINE));
        helpCircle.setStrokeWidth(1.5);
        Text helpQ = new Text("?");
        helpQ.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        helpQ.setFill(Color.web(COLOR_OUTLINE));
        helpStack.getChildren().addAll(helpCircle, helpQ);
        
        iconBox.getChildren().addAll(notifStack, helpStack);

        // Vertical Separator
        Rectangle separator = new Rectangle(1, 24, Color.web(COLOR_SURFACE_CONTAINER));
        
        // User Profile
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
        avatar.setStroke(Color.web(COLOR_PRIMARY));
        avatar.setStrokeWidth(2);
        Text avatarText = new Text("👤");
        avatarText.setFont(Font.font(14));
        StackPane avatarStack = new StackPane(avatar, avatarText);
        
        userBox.getChildren().addAll(uText, avatarStack);

        rightContainer.getChildren().addAll(iconBox, separator, userBox);

        header.getChildren().addAll(leftSpacer, searchBox, spacer, rightContainer);
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
        head.setPadding(new Insets(16));
        head.setAlignment(Pos.CENTER_LEFT);
        Text title = new Text("Gestión de Cursos");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        title.setFill(Color.web(COLOR_ON_SURFACE));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnAll = new Button("Ver todos");
        btnAll.getStyleClass().add("text-button");
        head.getChildren().addAll(title, spacer, btnAll);

        // Tabla
        VBox table = new VBox();
        table.setPadding(new Insets(0, 16, 16, 16));
        
        HBox cols = new HBox();
        cols.setPadding(new Insets(8, 0, 8, 0));
        cols.getChildren().addAll(
            createColH("CURSO", 180), 
            createColH("PROFESOR", 180), 
            createColH("ALUMNOS", 120), 
            createColH("RENDIMIENTO", 120)
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
        txt.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        txt.setFill(Color.web(COLOR_OUTLINE));
        HBox box = new HBox(txt);
        box.setPrefWidth(w);
        return box;
    }

    private HBox createCourseRow(String name, String prof, String st, double prog, String score) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(10, 0, 10, 0));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("course-row");

        Text tName = new Text(name);
        tName.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 13));
        tName.setFill(Color.web(COLOR_ON_SURFACE));
        
        HBox progBox = new HBox(10);
        progBox.setAlignment(Pos.CENTER_RIGHT);
        
        Rectangle bg = new Rectangle(50, 5, Color.web(COLOR_SURFACE_CONTAINER));
        bg.setArcWidth(5); bg.setArcHeight(5);
        Rectangle fill = new Rectangle(50 * prog, 5, Color.web(COLOR_PRIMARY));
        fill.setArcWidth(5); fill.setArcHeight(5);
        
        Text tScore = new Text(score);
        tScore.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 12));
        tScore.setFill(Color.web(COLOR_PRIMARY));

        progBox.getChildren().addAll(new Pane(bg, fill), tScore);

        // Stacked text for Students: Number (Bold) + "Estudiantes" (Smaller)
        String[] stParts = st.split(" ");
        VBox stBox = new VBox(2);
        stBox.setAlignment(Pos.CENTER_LEFT);
        Text stNum = new Text(stParts[0]);
        stNum.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 12));
        Text stLabel = new Text(stParts[1]);
        stLabel.setFont(Font.font("Plus Jakarta Sans", 10));
        stLabel.setFill(Color.web(COLOR_OUTLINE));
        stBox.getChildren().addAll(stNum, stLabel);

        HBox hName = new HBox(tName);
        hName.setPrefWidth(180);
        HBox hProf = new HBox(new Text(prof));
        hProf.setPrefWidth(180);
        HBox hStud = new HBox(stBox);
        hStud.setPrefWidth(120);
        HBox hPerf = progBox;
        hPerf.setPrefWidth(120);

        row.getChildren().addAll(hName, hProf, hStud, hPerf);

        return row;
    }

    private VBox createPerformancePanel() {
        VBox panel = new VBox(16);
        panel.getStyleClass().add("glass-card");
        panel.setPadding(new Insets(16));

        HBox head = new HBox();
        Text title = new Text("Desempeño");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        title.setFill(Color.web(COLOR_ON_SURFACE));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Text more = new Text("⋯");
        more.setFont(Font.font(18));
        head.getChildren().addAll(title, spacer, more);

        Text sub = new Text("Promedio general mensual (6 meses)");
        sub.setFont(Font.font("Plus Jakarta Sans", 12));
        sub.setFill(Color.web(COLOR_OUTLINE));

        // Gráfico
        HBox chart = new HBox(12);
        chart.setAlignment(Pos.BOTTOM_CENTER);
        chart.setPrefHeight(150);
        
        String[] labels = {"5to E", "6to A", "4to B", "4to C", "5to A", "6to B"};
        int[] heights = {80, 100, 90, 80, 140, 120};

        for (int i = 0; i < labels.length; i++) {
            VBox barBox = new VBox(8);
            barBox.setAlignment(Pos.BOTTOM_CENTER);
            Color barColor = (i == 4) ? Color.web(COLOR_PRIMARY) : Color.web(COLOR_PRIMARY_FIXED);
            Rectangle bar = new Rectangle(24, heights[i], barColor);
            bar.setArcWidth(10); bar.setArcHeight(10);
            Text label = new Text(labels[i]);
            label.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
            label.setFill(i == 4 ? Color.web(COLOR_PRIMARY) : Color.web(COLOR_OUTLINE));
            barBox.getChildren().addAll(bar, label);
            chart.getChildren().add(barBox);
        }

        HBox footer = new HBox();
        Text fT = new Text("Crecimiento Semestral");
        fT.setFont(Font.font("Plus Jakarta Sans", 12));
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
            createScheduleRow("08:00", "Matemáticas Avanzadas", "Salón 402 • Prof. Sánchez", true),
            createScheduleRow("10:00", "Historia Universal", "Biblioteca • Dra. Méndez", false),
            createScheduleRow("12:00", "Química Orgánica", "Laboratorio B • Prof. Rico", false),
            createScheduleRow("14:00", "Física Cuántica", "Laboratorio A • Prof. Einstein", false),
            createScheduleRow("16:00", "Arte Moderno", "Galería • Prof. Picasso", false),
            createScheduleRow("18:00", "Inglés Técnico", "Aula 10 • Prof. Smith", false)
        );

        ScrollPane sp = new ScrollPane(list);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        sp.setPrefHeight(300);

        panel.getChildren().addAll(title, sp);
        return panel;
    }

    private HBox createScheduleRow(String time, String subj, String det, boolean isFirst) {
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

        if (isFirst) {
            // FAB Bolt for the first row
            StackPane fab = new StackPane();
            fab.setPrefSize(40, 40);
            fab.getStyleClass().add("fab-button");
            Text bolt = new Text("⚡");
            bolt.setFont(Font.font(16));
            bolt.setFill(Color.WHITE);
            fab.getChildren().add(bolt);
            row.getChildren().addAll(iconStack, text, spacer, fab);
        } else {
            Text arrow = new Text("❯");
            arrow.setFont(Font.font(14));
            arrow.setFill(Color.web(COLOR_OUTLINE));
            row.getChildren().addAll(iconStack, text, spacer, arrow);
        }
        return row;
    }

    private String getInternalCSS() {
        return ".nav-item { -fx-background-color: transparent; -fx-cursor: hand; } " +
               ".nav-item:hover { -fx-background-color: " + COLOR_SURFACE_LOW + "; } " +
               ".sidebar-active { -fx-background-color: " + COLOR_PRIMARY_CONTAINER + "; -fx-cursor: hand; -fx-background-radius: 12; } " +
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
