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
import javafx.scene.Cursor;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class LuminaAcademyFX extends Application {

    // --- PALETA DE COLORES EXACTA DEL DESIGN.MD ---
    private final String COLOR_PRIMARY = "#004ac6";
    private final String COLOR_PRIMARY_CONTAINER = "#2563eb";
    private final String COLOR_BACKGROUND = "#f9f9ff";
    private final String COLOR_SURFACE = "#f9f9ff";
    private final String COLOR_SURFACE_LOW = "#f0f3ff";
    private final String COLOR_SURFACE_CONTAINER = "#e7eeff";
    private final String COLOR_SURFACE_CONTAINER_HIGH = "#dee8ff";
    private final String COLOR_ON_SURFACE = "#111c2d";
    private final String COLOR_ON_SURFACE_VARIANT = "#434655";
    private final String COLOR_OUTLINE = "#737686";
    private final String COLOR_OUTLINE_VARIANT = "#c3c6d7";
    private final String COLOR_WHITE = "#ffffff";
    private final String COLOR_PRIMARY_FIXED = "#dbe1ff";
    private final String COLOR_PRIMARY_FIXED_DIM = "#b4c5ff";
    private final String COLOR_SECONDARY = "#006686";
    private final String COLOR_SECONDARY_FIXED = "#c0e8ff";
    private final String COLOR_TERTIARY = "#4e565b";
    private final String COLOR_TERTIARY_CONTAINER = "#666f74";
    private final String COLOR_TERTIARY_FIXED = "#dbe4ea";
    private final String COLOR_ERROR = "#ba1a1a";

    // --- MATERIAL SYMBOLS SVG PATHS ---
    private final String ICON_LIGHTBULB = "M9 21c0 .55.45 1 1 1h4c.55 0 1-.45 1-1v-1H9v1zm3-19C8.14 2 5 5.14 5 9c0 2.38 1.19 4.47 3 5.74V17c0 .55.45 1 1 1h6c.55 0 1-.45 1-1v-2.26c1.81-1.27 3-3.36 3-5.74 0-3.86-3.14-7-7-7z";
    private final String ICON_HOME = "M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z";
    private final String ICON_SCHOOL = "M12 3L1 9l4 2.18v6L12 21l7-3.82v-6l2-1.09V17h2V9L12 3zm6.82 6L12 12.72 5.18 9 12 5.28 18.82 9zM17 15.99l-5 2.73-5-2.73v-3.72L12 15l5-2.73v3.72z";
    private final String ICON_BOOK = "M19 2l-5 4.5v11l5-4.5V2zM6.5 5C4.55 5 2.45 5.4 1 6.5v11.5c1.45-1.1 3.55-1.5 5.5-1.5s4.05.4 5.5 1.5V6.5C14.95 5.4 12.85 5 10.5 5S8.05 5.4 6.5 5z";
    private final String ICON_GROUP = "M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z";
    private final String ICON_CALENDAR = "M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11z";
    private final String ICON_SETTINGS = "M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.07.62-.07.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z";
    private final String ICON_SEARCH = "M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z";
    private final String ICON_NOTIFICATIONS = "M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z";
    private final String ICON_HELP = "M11 18h2v-2h-2v2zm1-16C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm0-14c-2.21 0-4 1.79-4 4h2c0-1.1.9-2 2-2s2 .9 2 2c0 2-3 1.75-3 5h2c0-2.25 3-2.5 3-5 0-2.21-1.79-4-4-4z";
    private final String ICON_PERSON_PIN = "M19 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h4l3 3 3-3h4c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-7 3.3c1.49 0 2.7 1.21 2.7 2.7s-1.21 2.7-2.7 2.7S9.3 9.49 9.3 8s1.21-2.7 2.7-2.7zM18 16H6v-.9c0-2 4-3.1 6-3.1s6 1.1 6 3.1v.9z";
    private final String ICON_TRENDING_UP = "M16 6l2.29 2.29-4.88 4.88-4-4L2 16.59 3.41 18l6-6 4 4 6.3-6.29L22 12V6z";
    private final String ICON_CHECK_CIRCLE = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z";
    private final String ICON_MORE_HORIZ = "M6 10c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm12 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm-6 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z";
    private final String ICON_SCHEDULE = "M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z";
    private final String ICON_CHEVRON_RIGHT = "M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z";
    private final String ICON_BOLT = "M14.69 2.21c-.29-.27-.71-.27-1.01 0l-10 9.52c-.29.28-.35.73-.14 1.07.2.34.58.51.98.44l5.93-.85-2.46 8.38c-.12.41.08.85.46 1.03.23.11.49.12.71.03.09-.04.17-.09.24-.16l10-9.52c.29-.28.35-.73.14-1.07-.2-.34-.58-.51-.98-.44l-5.93.85 2.46-8.38c.12-.41-.08-.85-.46-1.03-.11-.05-.23-.07-.34-.06z";
    private final String ICON_AVATAR = "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z";

    private SVGPath createIcon(String pathData, double size, String fill) {
        SVGPath icon = new SVGPath();
        icon.setContent(pathData);
        icon.setFill(Color.web(fill));
        double scale = size / 24.0;
        icon.setScaleX(scale);
        icon.setScaleY(scale);
        return icon;
    }

    private void addHoverScale(Region node, double scaleTo, double seconds) {
        ScaleTransition stIn = new ScaleTransition(Duration.seconds(seconds), node);
        stIn.setToX(scaleTo);
        stIn.setToY(scaleTo);
        ScaleTransition stOut = new ScaleTransition(Duration.seconds(seconds), node);
        stOut.setToX(1.0);
        stOut.setToY(1.0);
        node.setOnMouseEntered(e -> stIn.playFromStart());
        node.setOnMouseExited(e -> stOut.playFromStart());
    }

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
        VBox mainCanvas = new VBox(16);
        // Reduced top padding to bring everything up
        mainCanvas.setPadding(new Insets(10, 40, 40, 40));
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
        HBox kpiGrid = new HBox(16);
        kpiGrid.setAlignment(Pos.CENTER);
        kpiGrid.getChildren().addAll(
            createKpiCard("Total Estudiantes", "1,250", COLOR_SECONDARY_FIXED, COLOR_SECONDARY, ICON_PERSON_PIN),
            createKpiCard("Total Cursos", "35", COLOR_PRIMARY_FIXED, COLOR_PRIMARY, ICON_TRENDING_UP),
            createKpiCard("Profesores", "42", COLOR_TERTIARY_FIXED, COLOR_TERTIARY, ICON_SCHOOL),
            createKpiCard("Asistencia Estudiantes", "92%", COLOR_SECONDARY_FIXED, COLOR_SECONDARY, ICON_CHECK_CIRCLE)
        );

        // --- SECCIÓN 3: DUAL COLUMN ---
        HBox middleSection = new HBox(24);
        
        // Gestión de Cursos (8/12)
        VBox coursesBox = createCourseManagement();
        // Removed Hgrow to prevent vertical expansion and white space
        
        // Desempeño (4/12)
        VBox performanceBox = createPerformancePanel();
        performanceBox.setPrefWidth(320);

        middleSection.getChildren().addAll(coursesBox, performanceBox);

        // --- SECCIÓN 4: HORARIO DE HOY ---
        VBox scheduleBox = createSchedulePanel();

        mainCanvas.getChildren().addAll(headerSec, kpiGrid, middleSection, scheduleBox);
        
        // Inicializar el controlador para agregar secciones dinámicamente
        DashboardController controller = new DashboardController(mainCanvas, kpiGrid, middleSection, scheduleBox);
        
        // Ejemplo: Agregar una nueva KPI Card dinámicamente
        // controller.addKpiCard("Nueva Métrica", "100", COLOR_PRIMARY_FIXED, "⭐");
        
        StackPane centerWrapper = new StackPane(mainCanvas);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        contentArea.getChildren().add(centerWrapper);

        root.getChildren().add(contentArea);

        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add("data:text/css," + getInternalCSS());
        
        primaryStage.setTitle("Lumina Academy - Admin Portal");
        primaryStage.setScene(scene);
        
        root.setFocusTraversable(true);
        root.requestFocus();

        primaryStage.show();
    }

    private StackPane stackWithFab(Region center) {
        StackPane stack = new StackPane(center);
        stack.setAlignment(Pos.TOP_CENTER);

        // FAB Bolt
        StackPane fab = new StackPane();
        fab.setPrefSize(64, 64);
        fab.getStyleClass().add("fab-button");
        SVGPath bolt = createIcon(ICON_BOLT, 28, COLOR_WHITE);
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
        VBox sidebar = new VBox(24);
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(32, 20, 32, 20));
        sidebar.setStyle("-fx-background-color: " + COLOR_WHITE + "; -fx-border-color: transparent " + COLOR_SURFACE_CONTAINER_HIGH + " transparent transparent;");

        // Logo
        HBox logo = new HBox(12);
        logo.setPadding(new Insets(0, 0, 48, 0));
        logo.setAlignment(Pos.CENTER_LEFT);
        
        Circle logoCircle = new Circle(20, Color.web(COLOR_PRIMARY));
        SVGPath logoSymbol = createIcon(ICON_LIGHTBULB, 20, COLOR_WHITE);
        StackPane logoStack = new StackPane(logoCircle, logoSymbol);
        
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
            {"Inicio", ICON_HOME},
            {"Estudiantes", ICON_SCHOOL},
            {"Profesores", ICON_GROUP},
            {"Cursos", ICON_BOOK},
            {"Horario", ICON_CALENDAR},
            {"Configuración", ICON_SETTINGS}
        };

        for (int i = 0; i < items.length; i++) {
            HBox btnContainer = new HBox(12);
            btnContainer.setAlignment(Pos.CENTER_LEFT);
            btnContainer.setPadding(new Insets(12, 16, 12, 16));
            btnContainer.setPrefWidth(Double.MAX_VALUE);
            
            SVGPath icon = createIcon(items[i][1], 20, COLOR_WHITE);
            
            Text label = new Text(items[i][0]);
            label.setFont(Font.font("Plus Jakarta Sans", FontWeight.MEDIUM, 15));
            
            if (i == 0) {
                btnContainer.getStyleClass().add("sidebar-active");
                label.setFill(Color.WHITE);
                icon.setFill(Color.WHITE);
            } else {
                btnContainer.getStyleClass().add("nav-item");
                icon.setFill(Color.web(COLOR_ON_SURFACE_VARIANT));
                label.setFill(Color.web(COLOR_ON_SURFACE_VARIANT));
                
                int idx = i;
                btnContainer.setOnMouseEntered(e -> {
                    label.setFill(Color.web(COLOR_PRIMARY));
                    icon.setFill(Color.web(COLOR_PRIMARY));
                });
                btnContainer.setOnMouseExited(e -> {
                    label.setFill(Color.web(COLOR_ON_SURFACE_VARIANT));
                    icon.setFill(Color.web(COLOR_ON_SURFACE_VARIANT));
                });
            }
            
            btnContainer.getChildren().addAll(icon, label);
            sidebar.getChildren().add(btnContainer);
        }

        return sidebar;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(0, 32, 0, 32));
        header.setPrefHeight(70);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + COLOR_WHITE + "; -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER_HIGH + " transparent;");

        // Search Bar
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10, 20, 10, 20));
        searchBox.getStyleClass().add("search-container");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefWidth(400);
        
        SVGPath searchIcon = createIcon(ICON_SEARCH, 20, COLOR_OUTLINE);

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar expedientes, cursos o reportes...");
        searchField.getStyleClass().add("search-input");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        searchBox.getChildren().addAll(searchIcon, searchField);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right Side Container
        HBox rightContainer = new HBox(24);
        rightContainer.setAlignment(Pos.CENTER_RIGHT);

        // Notification Bell
        StackPane notifBtn = new StackPane();
        notifBtn.setPrefSize(32, 32);
        notifBtn.getStyleClass().add("icon-button");
        SVGPath bellIcon = createIcon(ICON_NOTIFICATIONS, 20, COLOR_ON_SURFACE_VARIANT);
        notifBtn.getChildren().add(bellIcon);
        addHoverScale(notifBtn, 1.1, 0.2);

        // Help Button
        StackPane helpBtn = new StackPane();
        helpBtn.setPrefSize(32, 32);
        helpBtn.getStyleClass().add("icon-button");
        SVGPath helpIcon = createIcon(ICON_HELP, 20, COLOR_ON_SURFACE_VARIANT);
        helpBtn.getChildren().add(helpIcon);
        addHoverScale(helpBtn, 1.1, 0.2);

        // Vertical Separator
        Rectangle separator = new Rectangle(1, 32, Color.web(COLOR_SURFACE_CONTAINER_HIGH));
        
        // User Profile
        HBox userBox = new HBox(12);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        userBox.setCursor(Cursor.HAND);
        VBox uText = new VBox(2);
        uText.setAlignment(Pos.CENTER_RIGHT);
        Text uName = new Text("Admin User");
        uName.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 14));
        Text uEmail = new Text("ADMIN@LUMINA.EDU");
        uEmail.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        uEmail.setFill(Color.web(COLOR_OUTLINE));
        uText.getChildren().addAll(uName, uEmail);
        
        Circle avatar = new Circle(20, Color.web(COLOR_PRIMARY_FIXED));
        avatar.setStroke(Color.web(COLOR_PRIMARY_FIXED));
        avatar.setStrokeWidth(2);
        SVGPath avatarSvg = createIcon(ICON_AVATAR, 20, COLOR_PRIMARY);
        StackPane avatarStack = new StackPane(avatar, avatarSvg);
        
        userBox.setOnMouseEntered(e -> {
            uName.setFill(Color.web(COLOR_PRIMARY));
            avatar.setStroke(Color.web(COLOR_PRIMARY));
        });
        userBox.setOnMouseExited(e -> {
            uName.setFill(Color.web(COLOR_ON_SURFACE));
            avatar.setStroke(Color.web(COLOR_PRIMARY_FIXED));
        });
        
        userBox.getChildren().addAll(uText, avatarStack);

        rightContainer.getChildren().addAll(notifBtn, helpBtn, separator, userBox);

        header.getChildren().addAll(searchBox, spacer, rightContainer);
        return header;
    }

    private VBox createKpiCard(String label, String value, String bgColor, String iconColor, String iconPath) {
        HBox card = new HBox(12);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("glass-card");

        // Icon in a colored circle
        Circle iconCircle = new Circle(16, Color.web(bgColor));
        SVGPath iconSvg = createIcon(iconPath, 16, iconColor);
        StackPane iconStack = new StackPane(iconCircle, iconSvg);
        
        // Hover: scale icon with transition
        ScaleTransition stIn = new ScaleTransition(Duration.seconds(0.2), iconStack);
        stIn.setToX(1.1);
        stIn.setToY(1.1);
        ScaleTransition stOut = new ScaleTransition(Duration.seconds(0.2), iconStack);
        stOut.setToX(1.0);
        stOut.setToY(1.0);
        card.setOnMouseEntered(e -> stIn.playFromStart());
        card.setOnMouseExited(e -> stOut.playFromStart());
        
        VBox text = new VBox(2);
        Text lbl = new Text(label.toUpperCase());
        lbl.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        lbl.setFill(Color.web(COLOR_OUTLINE));
        Text val = new Text(value);
        val.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
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
        head.setPadding(new Insets(12));
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
        table.setPadding(new Insets(0, 12, 0, 12));
        
        HBox cols = new HBox();
        cols.setPadding(new Insets(8, 0, 8, 0));
        cols.setStyle("-fx-background-color: " + COLOR_SURFACE_LOW + "; -fx-background-radius: 8;");
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
        row.setPadding(new Insets(10, 0, 0, 0));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("course-row");

        Text tName = new Text(name);
        tName.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 13));
        tName.setFill(Color.web(COLOR_ON_SURFACE));
        
        HBox progBox = new HBox(10);
        progBox.setAlignment(Pos.CENTER);
        
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
        VBox panel = new VBox(8);
        panel.getStyleClass().add("glass-card");
        panel.setPadding(new Insets(12));

        HBox head = new HBox();
        Text title = new Text("Desempeño");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        title.setFill(Color.web(COLOR_ON_SURFACE));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        StackPane moreBtn = new StackPane();
        moreBtn.setPrefSize(32, 32);
        moreBtn.getStyleClass().add("icon-button");
        SVGPath moreDots = createIcon(ICON_MORE_HORIZ, 20, COLOR_OUTLINE);
        moreBtn.getChildren().add(moreDots);
        head.getChildren().addAll(title, spacer, moreBtn);

        Text sub = new Text("Promedio general mensual (6 meses)");
        sub.setFont(Font.font("Plus Jakarta Sans", 12));
        sub.setFill(Color.web(COLOR_OUTLINE));

        // Gráfico
        HBox chart = new HBox(12);
        chart.setAlignment(Pos.BOTTOM_CENTER);
        chart.setPrefHeight(120);
        
        String[] labels = {"5to E", "6to A", "4to B", "4to C", "5to A", "6to B"};
        int[] heights = {60, 70, 80, 60, 110, 90};

        for (int i = 0; i < labels.length; i++) {
            VBox barBox = new VBox(8);
            barBox.setAlignment(Pos.BOTTOM_CENTER);
            Color barColor = (i == 4) ? Color.web(COLOR_PRIMARY) : Color.web(COLOR_PRIMARY_FIXED);
            Rectangle bar = new Rectangle(20, heights[i], barColor);
            bar.setArcWidth(10); bar.setArcHeight(10);
            // Make top rounded, bottom flat
            bar.setArcWidth(10);
            bar.setArcHeight(10);
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
        VBox panel = new VBox(8);
        panel.getStyleClass().add("glass-card");
        panel.setPadding(new Insets(12));

        Text title = new Text("Horario de Hoy");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        title.setFill(Color.web(COLOR_ON_SURFACE));

        // LISTA
        VBox list = new VBox(6);
        list.getChildren().addAll(
            createScheduleRow("08:00", "Matemáticas Avanzadas", "Salón 402 • Prof. Sánchez", false),
            createScheduleRow("10:00", "Historia Universal", "Biblioteca • Dra. Méndez", false),
            createScheduleRow("12:00", "Química Orgánica", "Laboratorio B • Prof. Rico", false),
            createScheduleRow("14:00", "Física Cuántica", "Laboratorio A • Prof. Einstein", false),
            createScheduleRow("16:00", "Arte Moderno", "Galería • Prof. Picasso", false),
            createScheduleRow("18:00", "Inglés Técnico", "Aula 10 • Prof. Smith", false)
        );

        ScrollPane sp = new ScrollPane(list);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        sp.setPrefHeight(220);

        panel.getChildren().addAll(title, sp);
        return panel;
    }

    private HBox createScheduleRow(String time, String subj, String det, boolean isFirst) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(8, 0, 8, 0));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("schedule-row");

        // Clock Icon
        Circle iconCircle = new Circle(24, Color.web(COLOR_SURFACE_LOW));
        SVGPath clockIcon = createIcon(ICON_SCHEDULE, 20, COLOR_ON_SURFACE);
        StackPane iconStack = new StackPane(iconCircle, clockIcon);
        
        // Hover effect for schedule icon
        Color hoverBg, hoverText;
        if (subj.contains("Matemáticas") || subj.contains("Física")) {
            hoverBg = Color.web(COLOR_PRIMARY);
            hoverText = Color.WHITE;
        } else if (subj.contains("Historia") || subj.contains("Inglés")) {
            hoverBg = Color.web(COLOR_SECONDARY);
            hoverText = Color.WHITE;
        } else {
            hoverBg = Color.web(COLOR_TERTIARY);
            hoverText = Color.WHITE;
        }
        row.setOnMouseEntered(e -> {
            iconCircle.setFill(hoverBg);
            clockIcon.setFill(hoverText);
        });
        row.setOnMouseExited(e -> {
            iconCircle.setFill(Color.web(COLOR_SURFACE_LOW));
            clockIcon.setFill(Color.web(COLOR_ON_SURFACE));
        });
        
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
            SVGPath bolt = createIcon(ICON_BOLT, 18, COLOR_WHITE);
            fab.getChildren().add(bolt);
            row.getChildren().addAll(iconStack, text, spacer, fab);
        } else {
            StackPane arrowBtn = new StackPane();
            arrowBtn.setPrefSize(40, 40);
            arrowBtn.getStyleClass().add("icon-button");
            SVGPath arrow = createIcon(ICON_CHEVRON_RIGHT, 20, COLOR_OUTLINE);
            arrowBtn.getChildren().add(arrow);
            row.getChildren().addAll(iconStack, text, spacer, arrowBtn);
        }
        return row;
    }

    private String getInternalCSS() {
        return ".nav-item { -fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: " + COLOR_ON_SURFACE_VARIANT + "; } " +
                ".nav-item:hover { -fx-background-color: " + COLOR_SURFACE_LOW + "; -fx-cursor: hand; -fx-background-radius: 12; -fx-text-fill: " + COLOR_PRIMARY + "; } " +
               ".sidebar-active { -fx-background-color: " + COLOR_PRIMARY_CONTAINER + "; -fx-cursor: hand; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.3), 16, 0, 0, 8); } " +
               ".search-container { -fx-background-color: " + COLOR_SURFACE_LOW + "; -fx-background-radius: 32; -fx-border-color: " + COLOR_SURFACE_CONTAINER + "; -fx-border-radius: 32; } " +
               ".search-input { -fx-background-color: transparent; -fx-border-color: transparent; -fx-font-family: 'Plus Jakarta Sans'; -fx-text-fill: " + COLOR_ON_SURFACE + "; -fx-prompt-text-fill: " + COLOR_OUTLINE + "; } " +
               ".glass-card { -fx-background-color: " + COLOR_WHITE + "; -fx-background-radius: 16; -fx-border-color: " + COLOR_SURFACE_CONTAINER + "; -fx-border-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.05), 20, 0, 0, 4); } " +
               ".course-row { -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER + " transparent; } " +
               ".course-row:hover { -fx-background-color: " + COLOR_SURFACE_LOW + "; } " +
               ".schedule-row { -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER + " transparent; } " +
               ".schedule-row:hover { -fx-background-color: " + COLOR_SURFACE_LOW + "; } " +
               ".text-button { -fx-text-fill: " + COLOR_PRIMARY + "; -fx-background-color: transparent; -fx-underline: false; -fx-cursor: hand; -fx-font-weight: bold; } " +
               ".text-button:hover { -fx-underline: true; } " +
               ".icon-button { -fx-background-radius: 32; -fx-cursor: hand; } " +
                ".growth-badge { -fx-background-color: " + COLOR_PRIMARY_FIXED + "; -fx-text-fill: " + COLOR_PRIMARY + "; -fx-font-weight: bold; -fx-padding: 4 12 4 12; -fx-background-radius: 16; } " +
               ".fab-button { -fx-background-color: " + COLOR_PRIMARY + "; -fx-background-radius: 32; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.4), 15, 0, 0, 8); } " +
               ".fab-button:hover { -fx-scale-x: 1.1; -fx-scale-y: 1.1; }";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
