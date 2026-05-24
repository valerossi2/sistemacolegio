package com.example.demo.controller;

import com.example.demo.theme.ThemeManager;
import javafx.animation.ScaleTransition;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainController {

    @FXML private VBox root;
    @FXML private VBox sidebar;
    @FXML private VBox navItemsContainer;
    @FXML private HBox logo;
    @FXML private StackPane logoStack;
    @FXML private Text lTitle;
    @FXML private Text lSub;
    @FXML private VBox contentArea;
    @FXML private HBox header;
    @FXML private HBox searchBox;
    @FXML private StackPane searchIconContainer;
    @FXML private TextField searchField;
    @FXML private HBox rightContainer;
    @FXML private StackPane notifBtn;
    @FXML private StackPane helpBtn;
    @FXML private Rectangle headerSeparator;
    @FXML private HBox userBox;
    @FXML private Text uName;
    @FXML private Text uEmail;
    @FXML private StackPane avatarStack;
    @FXML private StackPane centerWrapper;
    @FXML private VBox mainCanvas;
    @FXML private Text h1;
    @FXML private Text sub;
    @FXML private HBox kpiGrid;
    @FXML private VBox middleSection;
    private HBox middleSectionHBox;
    @FXML private VBox coursesBox;
    @FXML private VBox performanceBox;
    @FXML private VBox scheduleBox;
    @FXML private HBox titleBar;
    @FXML private StackPane minimizeBtn;
    @FXML private StackPane maximizeBtn;
    @FXML private StackPane closeBtn;

    private ThemeManager theme;
    private Stage stage;
    private boolean maximized = false;
    private double dragOffsetX, dragOffsetY;
    private List<Runnable> themeUpdaters = new ArrayList<>();
    private double compactScale = 1.0;
    private final List<HBox> kpiCardList = new ArrayList<>();
    private final List<Text> kpiLabelList = new ArrayList<>();
    private final List<Text> kpiValueList = new ArrayList<>();
    private final List<Circle> kpiCircleList = new ArrayList<>();
    private Text courseTitleText;
    private Text perfTitleText;
    private Text perfSubText;
    private Text scheduleTitleText;
    private final List<HBox> colHeaderList = new ArrayList<>();
    private final List<List<HBox>> courseRowCells = new ArrayList<>();
    private final List<Circle> scheduleCircleList = new ArrayList<>();
    private final List<Text> scheduleSubjList = new ArrayList<>();
    private final List<Text> scheduleDetList = new ArrayList<>();
    private final List<Rectangle> perfBarsList = new ArrayList<>();

    private final String L_PRIMARY = "#004ac6";
    private final String L_PRIMARY_CONTAINER = "#2563eb";
    private final String L_BG = "#f9f9ff";
    private final String L_SURFACE_LOW = "#f0f3ff";
    private final String L_SURFACE_CONTAINER = "#e7eeff";
    private final String L_SURFACE_CONTAINER_HIGH = "#dee8ff";
    private final String L_ON_SURFACE = "#111c2d";
    private final String L_ON_SURFACE_VARIANT = "#434655";
    private final String L_OUTLINE = "#737686";
    private final String L_WHITE = "#ffffff";
    private final String L_PRIMARY_FIXED = "#dbe1ff";
    private final String L_SECONDARY = "#006686";
    private final String L_SECONDARY_FIXED = "#c0e8ff";
    private final String L_TERTIARY = "#4e565b";
    private final String L_TERTIARY_CONTAINER = "#666f74";
    private final String L_TERTIARY_FIXED = "#dbe4ea";

    private final String D_PRIMARY = "#2D6AEE";
    private final String D_PRIMARY_CONTAINER = "#1D4ED8";
    private final String D_BG = "#0F172A";
    private final String D_SURFACE_LOW = "#1E293B";
    private final String D_SURFACE_CONTAINER = "#334155";
    private final String D_SURFACE_CONTAINER_HIGH = "#475569";
    private final String D_ON_SURFACE = "#F8FAFC";
    private final String D_ON_SURFACE_VARIANT = "#CBD5E1";
    private final String D_OUTLINE = "#64748B";
    private final String D_WHITE = "#1E293B";
    private final String D_PRIMARY_FIXED = "#1E3A5F";
    private final String D_SECONDARY = "#38BDF8";
    private final String D_SECONDARY_FIXED = "#0C4A6E";
    private final String D_TERTIARY = "#94A3B8";
    private final String D_TERTIARY_CONTAINER = "#475569";
    private final String D_TERTIARY_FIXED = "#334155";

    private String c(String l, String d) { return theme.isDark() ? d : l; }

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

    public void setThemeManager(ThemeManager theme) {
        this.theme = theme;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        // Dejamos vacío o solo configuraciones que no dependan del theme
    }

    public void setupEverything() {
        setupUI();
        setupTheme();
    }

    private void setupUI() {
        setupLogo();
        setupNavigation();
        setupTitleBar();
        setupHeader();
        setupKpis();
        setupCourseManagement();
        setupPerformancePanel();
        setupSchedulePanel();
        setupResponsiveLayout();
    }

    private void setupLogo() {
        Circle logoCircle = new Circle(20, Color.web(L_PRIMARY));
        SVGPath logoSymbol = createIcon(ICON_LIGHTBULB, 20, L_WHITE);
        logoStack.getChildren().addAll(logoCircle, logoSymbol);
        
        lTitle.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        lTitle.setFill(Color.web(L_PRIMARY));
        lSub.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        
        themeUpdaters.add(() -> lSub.setFill(Color.web(c(L_OUTLINE, D_OUTLINE))));
    }

    private void setupNavigation() {
        String[][] items = {
            {"Inicio", ICON_HOME},
            {"Estudiantes", ICON_SCHOOL},
            {"Profesores", ICON_GROUP},
            {"Cursos", ICON_BOOK},
            {"Horario", ICON_CALENDAR},
            {"Configuracion", ICON_SETTINGS}
        };

        for (int i = 0; i < items.length; i++) {
            HBox btnContainer = new HBox(12);
            btnContainer.setAlignment(Pos.CENTER_LEFT);
            btnContainer.setPadding(new Insets(12, 16, 12, 16));
            btnContainer.setPrefWidth(Double.MAX_VALUE);

            SVGPath icon = createIcon(items[i][1], 20, L_WHITE);
            Text label = new Text(items[i][0]);
            label.setFont(Font.font("Plus Jakarta Sans", FontWeight.MEDIUM, 15));

            int idx = i;
            if (i == 0) {
                btnContainer.getStyleClass().add("sidebar-active");
            } else {
                btnContainer.getStyleClass().add("nav-item");
                icon.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));
                label.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));

                btnContainer.setOnMouseEntered(e -> {
                    if (!btnContainer.getStyleClass().contains("sidebar-active")) {
                        label.setFill(Color.web(L_PRIMARY));
                        icon.setFill(Color.web(L_PRIMARY));
                    }
                });
                btnContainer.setOnMouseExited(e -> {
                    if (!btnContainer.getStyleClass().contains("sidebar-active")) {
                        label.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));
                        icon.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));
                    }
                });
            }

            themeUpdaters.add(() -> {
                if (!btnContainer.getStyleClass().contains("sidebar-active")) {
                    icon.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));
                    label.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));
                }
            });

            btnContainer.setOnMouseClicked(e -> {
                for (Node child : navItemsContainer.getChildren()) {
                    if (child instanceof HBox) {
                        HBox h = (HBox) child;
                        h.getStyleClass().remove("sidebar-active");
                        h.getStyleClass().add("nav-item");
                        if (h.getChildren().size() >= 2) {
                            Node first = h.getChildren().get(0);
                            Node second = h.getChildren().get(1);
                            if (first instanceof SVGPath) ((SVGPath) first).setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));
                            if (second instanceof Text) ((Text) second).setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));
                        }
                    }
                }
                btnContainer.getStyleClass().remove("nav-item");
                btnContainer.getStyleClass().add("sidebar-active");
                label.setFill(Color.WHITE);
                icon.setFill(Color.WHITE);
                handleNavigation(idx);
            });

            btnContainer.getChildren().addAll(icon, label);
            navItemsContainer.getChildren().add(btnContainer);
        }
    }

    private void handleNavigation(int index) {
        if (index == 0) {
            setCenterView(mainCanvas);
        } else if (index == 5) {
            com.example.demo.controller.Configuracion config = new com.example.demo.controller.Configuracion(theme);
            setCenterView(config.getView());
        } else {
            System.out.println("Navegando a sección: " + index + " (No implementada)");
        }
    }

    private void setupResponsiveLayout() {
        middleSectionHBox = new HBox(24);
        HBox.setHgrow(coursesBox, Priority.ALWAYS);
        
        mainCanvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            double contentWidth = width - 80;
            boolean compact = contentWidth < 700;
            
            applyCompactScale(compact);
            
            if (contentWidth < 900) {
                if (middleSection.getChildren().contains(middleSectionHBox)) {
                    middleSection.getChildren().clear();
                }
                if (!middleSection.getChildren().contains(coursesBox)) {
                    middleSection.getChildren().addAll(coursesBox, performanceBox);
                }
                coursesBox.setPrefWidth(contentWidth);
                performanceBox.setPrefWidth(contentWidth);
            } else {
                if (middleSection.getChildren().contains(coursesBox)) {
                    middleSection.getChildren().clear();
                }
                if (!middleSection.getChildren().contains(middleSectionHBox)) {
                    middleSectionHBox.getChildren().setAll(coursesBox, performanceBox);
                    middleSection.getChildren().add(middleSectionHBox);
                }
                coursesBox.setPrefWidth(contentWidth - 320 - 24);
                performanceBox.setPrefWidth(320);
            }
        });
        
        // Trigger initial layout
        setCenterView(mainCanvas);
    }

    private void setCenterView(Node node) {
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent; -fx-padding: 0;");
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        // Center content
        StackPane container = new StackPane(node);
        container.setAlignment(Pos.TOP_LEFT);
        container.setStyle("-fx-background-color: transparent;");
        sp.setContent(container);
        
        centerWrapper.getChildren().setAll(sp);
    }

    private void setupTitleBar() {
        // Minimize icon
        SVGPath minIcon = new SVGPath();
        minIcon.setContent("M4 14h16v2H4z");
        minIcon.setScaleX(0.5);
        minIcon.setScaleY(0.5);
        minimizeBtn.getChildren().add(minIcon);

        // Maximize icon
        SVGPath maxIcon = new SVGPath();
        maxIcon.setContent("M5 5h14v14H5z");
        maxIcon.setScaleX(0.5);
        maxIcon.setScaleY(0.5);
        maxIcon.setFill(null);
        maxIcon.setStrokeWidth(1.8);
        maximizeBtn.getChildren().add(maxIcon);

        // Close icon
        SVGPath closeIcon = new SVGPath();
        closeIcon.setContent("M6 6l12 12M18 6L6 18");
        closeIcon.setScaleX(0.5);
        closeIcon.setScaleY(0.5);
        closeIcon.setStrokeWidth(1.8);
        closeBtn.getChildren().add(closeIcon);

        // Window controls hover effects
        minimizeBtn.setOnMouseEntered(e -> minIcon.setFill(Color.web(c(L_PRIMARY, D_PRIMARY))));
        minimizeBtn.setOnMouseExited(e -> minIcon.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT))));

        maximizeBtn.setOnMouseEntered(e -> maxIcon.setStroke(Color.web(c(L_PRIMARY, D_PRIMARY))));
        maximizeBtn.setOnMouseExited(e -> maxIcon.setStroke(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT))));

        closeBtn.setOnMouseEntered(e -> closeIcon.setStroke(Color.WHITE));
        closeBtn.setOnMouseExited(e -> closeIcon.setStroke(Color.web(c(L_OUTLINE, D_OUTLINE))));

        // Window controls - minimize
        minimizeBtn.setOnMouseClicked(e -> {
            if (stage != null) stage.setIconified(true);
        });

        // Window controls - maximize/restore
        maximizeBtn.setOnMouseClicked(e -> toggleMaximize());

        // Window controls - close
        closeBtn.setOnMouseClicked(e -> {
            if (stage != null) stage.close();
        });

        // Double-click title bar to toggle fullscreen
        titleBar.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) toggleMaximize();
        });

        // Window dragging (only when not fullscreen)
        titleBar.setOnMousePressed(e -> {
            if (stage != null) {
                dragOffsetX = e.getScreenX() - stage.getX();
                dragOffsetY = e.getScreenY() - stage.getY();
            }
        });
        titleBar.setOnMouseDragged(e -> {
            if (stage != null) {
                if (maximized) {
                    toggleMaximize();
                    dragOffsetX = e.getScreenX() - stage.getX();
                    dragOffsetY = e.getScreenY() - stage.getY();
                }
                stage.setX(e.getScreenX() - dragOffsetX);
                stage.setY(e.getScreenY() - dragOffsetY);
            }
        });

        // Hide/show title bar on fullscreen change
        stage.fullScreenProperty().addListener((obs, wasFull, isFull) -> {
            if (isFull) {
                titleBar.setVisible(false);
                titleBar.setManaged(false);
                maximized = true;
                SVGPath icon = (SVGPath) maximizeBtn.getChildren().get(0);
                icon.setContent("M8 3v5h13V3H8zm-5 8v10h10V11H3z");
            } else {
                titleBar.setVisible(true);
                titleBar.setManaged(true);
                maximized = false;
                SVGPath icon = (SVGPath) maximizeBtn.getChildren().get(0);
                icon.setContent("M5 5h14v14H5z");
            }
        });

        themeUpdaters.add(() -> {
            String tbBg = c(L_WHITE, D_WHITE);
            String tbText = c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT);

            titleBar.setStyle("-fx-background-color: " + tbBg + ";");

            minIcon.setFill(Color.web(tbText));
            maxIcon.setStroke(Color.web(tbText));
            maxIcon.setFill(null);
            closeIcon.setStroke(Color.web(c(L_OUTLINE, D_OUTLINE)));
        });
    }

    private void toggleMaximize() {
        if (stage == null) return;
        stage.setFullScreen(!stage.isFullScreen());
    }

    private void setupHeader() {
        SVGPath searchIcon = createIcon(ICON_SEARCH, 20, c(L_OUTLINE, D_OUTLINE));
        searchIconContainer.getChildren().add(searchIcon);

        SVGPath bellIcon = createIcon(ICON_NOTIFICATIONS, 20, c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT));
        notifBtn.getChildren().add(bellIcon);
        notifBtn.setOnMouseEntered(e -> bellIcon.setFill(Color.web(L_PRIMARY)));
        notifBtn.setOnMouseExited(e -> bellIcon.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT))));

        SVGPath helpIcon = createIcon(ICON_HELP, 20, c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT));
        helpBtn.getChildren().add(helpIcon);
        helpBtn.setOnMouseEntered(e -> helpIcon.setFill(Color.web(L_PRIMARY)));
        helpBtn.setOnMouseExited(e -> helpIcon.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT))));

        headerSeparator.setFill(Color.web(c(L_SURFACE_CONTAINER_HIGH, D_SURFACE_CONTAINER_HIGH)));

        uName.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 14));
        uName.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
        uEmail.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        uEmail.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));

        Circle avatar = new Circle(20, Color.web(c(L_PRIMARY_FIXED, D_PRIMARY_FIXED)));
        avatar.setStroke(Color.web(c(L_PRIMARY_FIXED, D_PRIMARY_FIXED)));
        avatar.setStrokeWidth(2);
        SVGPath avatarSvg = createIcon(ICON_AVATAR, 20, c(L_PRIMARY, D_PRIMARY));
        avatarStack.getChildren().addAll(avatar, avatarSvg);

        userBox.setOnMouseEntered(e -> {
            uName.setFill(Color.web(L_PRIMARY));
            avatar.setStroke(Color.web(L_PRIMARY));
        });
        userBox.setOnMouseExited(e -> {
            uName.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
            avatar.setStroke(Color.web(c(L_PRIMARY_FIXED, D_PRIMARY_FIXED)));
        });

        themeUpdaters.add(() -> {
            String hbg = c(L_WHITE, D_WHITE);
            String hborder = c(L_SURFACE_CONTAINER_HIGH, D_SURFACE_CONTAINER_HIGH);
            header.setStyle("-fx-background-color: " + hbg + "; -fx-border-color: transparent transparent " + hborder + " transparent;");
            searchIcon.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
            bellIcon.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));
            helpIcon.setFill(Color.web(c(L_ON_SURFACE_VARIANT, D_ON_SURFACE_VARIANT)));
            headerSeparator.setFill(Color.web(c(L_SURFACE_CONTAINER_HIGH, D_SURFACE_CONTAINER_HIGH)));
            uName.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
            uEmail.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
            avatar.setFill(Color.web(c(L_PRIMARY_FIXED, D_PRIMARY_FIXED)));
            avatar.setStroke(Color.web(c(L_PRIMARY_FIXED, D_PRIMARY_FIXED)));
            avatarSvg.setFill(Color.web(c(L_PRIMARY, D_PRIMARY)));
        });
    }

    private void setupKpis() {
        kpiGrid.getChildren().addAll(
            createKpiCard("Total Estudiantes", "1,250", L_SECONDARY_FIXED, L_SECONDARY, ICON_PERSON_PIN),
            createKpiCard("Total Cursos", "35", L_PRIMARY_FIXED, L_PRIMARY, ICON_TRENDING_UP),
            createKpiCard("Profesores", "42", L_TERTIARY_FIXED, L_TERTIARY, ICON_SCHOOL),
            createKpiCard("Asistencia Estudiantes", "92%", L_SECONDARY_FIXED, L_SECONDARY, ICON_CHECK_CIRCLE)
        );
    }

    private VBox createKpiCard(String label, String value, String lightBg, String iconColor, String iconPath) {
        HBox card = new HBox(12);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("glass-card");

        Circle iconCircle = new Circle(16);
        SVGPath iconSvg = createIcon(iconPath, 16, "#FFFFFF");
        StackPane iconStack = new StackPane(iconCircle, iconSvg);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), iconStack);
        card.setOnMouseEntered(e -> {
            st.setFromX(1.0); st.setFromY(1.0);
            st.setToX(1.1); st.setToY(1.1);
            st.playFromStart();
        });
        card.setOnMouseExited(e -> {
            st.setFromX(1.1); st.setFromY(1.1);
            st.setToX(1.0); st.setToY(1.0);
            st.playFromStart();
        });

        VBox text = new VBox(2);
        Text lbl = new Text(label.toUpperCase());
        lbl.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        Text val = new Text(value);
        val.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        text.getChildren().addAll(lbl, val);

        card.getChildren().addAll(iconStack, text);
        VBox wrapper = new VBox(card);
        HBox.setHgrow(wrapper, Priority.ALWAYS);

        kpiCardList.add(card);
        kpiLabelList.add(lbl);
        kpiValueList.add(val);
        kpiCircleList.add(iconCircle);

        themeUpdaters.add(() -> {
            iconCircle.setFill(Color.web(c(lightBg, D_SURFACE_CONTAINER)));
            iconSvg.setFill(Color.web(c("#FFFFFF", iconColor)));
            lbl.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
            val.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
        });

        return wrapper;
    }

    private void setupCourseManagement() {
        HBox head = new HBox();
        head.setPadding(new Insets(12));
        head.setAlignment(Pos.CENTER_LEFT);
        courseTitleText = new Text("Gestion de Cursos");
        courseTitleText.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        courseTitleText.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnAll = new Button("Ver todos");
        btnAll.getStyleClass().add("text-button");
        head.getChildren().addAll(courseTitleText, spacer, btnAll);

        VBox table = new VBox();
        table.setPadding(new Insets(0, 12, 0, 12));

        HBox cols = new HBox();
        cols.setPadding(new Insets(8, 0, 8, 0));
        cols.setStyle("-fx-background-color: " + c(L_SURFACE_LOW, D_SURFACE_LOW) + "; -fx-background-radius: 8;");
        colHeaderList.addAll(List.of(
            createColH("CURSO", 180),
            createColH("PROFESOR", 180),
            createColH("ALUMNOS", 120),
            createColH("RENDIMIENTO", 120)
        ));
        cols.getChildren().addAll(colHeaderList);

        table.getChildren().addAll(cols);
        table.getChildren().addAll(
            createCourseRow("Introduccion a la IA", "Dr. Roberto Sanchez", "32 Estudiantes", 0.92, "9.2"),
            createCourseRow("Calculo Avanzado", "Dra. Elena Mendez", "28 Estudiantes", 0.78, "7.8"),
            createCourseRow("Literatura Moderna", "Prof. Juan Carlos Rico", "40 Estudiantes", 0.85, "8.5"),
            createCourseRow("Diseno UX/UI", "Mtra. Sofia Valdez", "24 Estudiantes", 0.88, "8.8")
        );

        coursesBox.getChildren().addAll(head, table);
        themeUpdaters.add(() -> {
            courseTitleText.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
            cols.setStyle("-fx-background-color: " + c(L_SURFACE_LOW, D_SURFACE_LOW) + "; -fx-background-radius: 8;");
        });
    }

    private HBox createColH(String t, double w) {
        Text txt = new Text(t);
        txt.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        HBox box = new HBox(txt);
        box.setPadding(new Insets(0, 0, 0, 10));
        box.setPrefWidth(w);
        themeUpdaters.add(() -> txt.setFill(Color.web(c(L_OUTLINE, D_OUTLINE))));
        return box;
    }

    private HBox createCourseRow(String name, String prof, String st, double prog, String score) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(10, 0, 0, 0));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("course-row");

        Text tName = new Text(name);
        tName.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 13));
        tName.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));

        HBox progBox = new HBox(10);
        progBox.setAlignment(Pos.CENTER);

        Rectangle bg = new Rectangle(50, 5);
        bg.setArcWidth(5); bg.setArcHeight(5);
        bg.setFill(Color.web(c(L_SURFACE_CONTAINER, D_SURFACE_CONTAINER)));
        Rectangle fill = new Rectangle(50 * prog, 5, Color.web(c(L_PRIMARY, D_PRIMARY)));
        fill.setArcWidth(5); fill.setArcHeight(5);

        Text tScore = new Text(score);
        tScore.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 12));
        tScore.setFill(Color.web(c(L_PRIMARY, D_PRIMARY)));

        progBox.getChildren().addAll(new Pane(bg, fill), tScore);

        String[] stParts = st.split(" ");
        VBox stBox = new VBox(2);
        stBox.setAlignment(Pos.CENTER_LEFT);
        Text stNum = new Text(stParts[0]);
        stNum.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 12));
        stNum.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
        Text stLabel = new Text(stParts[1]);
        stLabel.setFont(Font.font("Plus Jakarta Sans", 10));
        stLabel.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
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
        courseRowCells.add(List.of(hName, hProf, hStud, hPerf));

        themeUpdaters.add(() -> {
            tName.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
            bg.setFill(Color.web(c(L_SURFACE_CONTAINER, D_SURFACE_CONTAINER)));
            fill.setFill(Color.web(c(L_PRIMARY, D_PRIMARY)));
            tScore.setFill(Color.web(c(L_PRIMARY, D_PRIMARY)));
            stNum.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
            stLabel.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
        });

        return row;
    }

    private void setupPerformancePanel() {
        HBox head = new HBox();
        perfTitleText = new Text("Desempeno");
        perfTitleText.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        perfTitleText.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        StackPane moreBtn = new StackPane();
        moreBtn.setPrefSize(32, 32);
        moreBtn.getStyleClass().add("icon-button");
        SVGPath moreDots = createIcon(ICON_MORE_HORIZ, 20, c(L_OUTLINE, D_OUTLINE));
        moreBtn.getChildren().add(moreDots);
        head.getChildren().addAll(perfTitleText, spacer, moreBtn);

        perfSubText = new Text("Promedio general mensual (6 meses)");
        perfSubText.setFont(Font.font("Plus Jakarta Sans", 12));
        perfSubText.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));

        HBox chart = new HBox(12);
        chart.setAlignment(Pos.BOTTOM_CENTER);
        chart.setPrefHeight(120);

        String[] labels = {"5to E", "6to A", "4to B", "4to C", "5to A", "6to B"};
        int[] heights = {60, 70, 80, 60, 110, 90};

        List<Rectangle> bars = new ArrayList<>();
        List<Text> barLabels = new ArrayList<>();

        for (int i = 0; i < labels.length; i++) {
            VBox barBox = new VBox(8);
            barBox.setAlignment(Pos.BOTTOM_CENTER);
            Rectangle bar = new Rectangle(20, heights[i], Color.web(c(L_PRIMARY, D_PRIMARY)));
            bar.setArcWidth(10); bar.setArcHeight(10);
            Text lbl = new Text(labels[i]);
            lbl.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
            lbl.setFill(Color.web(c(i == 4 ? L_PRIMARY : L_OUTLINE, D_ON_SURFACE)));
            barBox.getChildren().addAll(bar, lbl);
            chart.getChildren().add(barBox);
            bars.add(bar);
            barLabels.add(lbl);
        }

        HBox footer = new HBox();
        Text fT = new Text("Crecimiento Semestral");
        fT.setFont(Font.font("Plus Jakarta Sans", 12));
        fT.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
        Region s2 = new Region();
        HBox.setHgrow(s2, Priority.ALWAYS);
        Label fV = new Label("+12.4%");
        fV.getStyleClass().add("growth-badge");
        footer.getChildren().addAll(fT, s2, fV);

        performanceBox.getChildren().addAll(head, perfSubText, chart, footer);
        performanceBox.setPadding(new Insets(12));
        performanceBox.setSpacing(8);
        perfBarsList.addAll(bars);

        themeUpdaters.add(() -> {
            perfTitleText.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
            perfSubText.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
            moreDots.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
            fT.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
            for (int i = 0; i < bars.size(); i++) {
                bars.get(i).setFill(Color.web(c(i == 4 ? L_PRIMARY : L_PRIMARY_FIXED, D_PRIMARY)));
                barLabels.get(i).setFill(Color.web(c(i == 4 ? L_PRIMARY : L_OUTLINE, D_ON_SURFACE)));
            }
        });
    }

    private void setupSchedulePanel() {
        scheduleTitleText = new Text("Horario de Hoy");
        scheduleTitleText.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        scheduleTitleText.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));

        VBox list = new VBox(6);
        list.getChildren().addAll(
            createScheduleRow("08:00", "Matematicas Avanzadas", "Salon 402 - Prof. Sanchez", false),
            createScheduleRow("10:00", "Historia Universal", "Biblioteca - Dra. Mendez", false),
            createScheduleRow("12:00", "Quimica Organica", "Laboratorio B - Prof. Rico", false),
            createScheduleRow("14:00", "Fisica Cuantica", "Laboratorio A - Prof. Einstein", false),
            createScheduleRow("16:00", "Arte Moderno", "Galeria - Prof. Picasso", false),
            createScheduleRow("18:00", "Ingles Tecnico", "Aula 10 - Prof. Smith", false)
        );

        ScrollPane sp = new ScrollPane(list);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        sp.setPrefHeight(220);

        scheduleBox.getChildren().addAll(scheduleTitleText, sp);
        scheduleBox.setPadding(new Insets(12));
        scheduleBox.setSpacing(8);

        themeUpdaters.add(() -> scheduleTitleText.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE))));
    }

    private HBox createScheduleRow(String time, String subj, String det, boolean isFirst) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(8, 0, 8, 0));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("schedule-row");

        Circle iconCircle = new Circle(24, Color.web(c(L_SURFACE_LOW, D_SURFACE_LOW)));
        SVGPath clockIcon = createIcon(ICON_SCHEDULE, 20, c(L_ON_SURFACE, D_ON_SURFACE));
        StackPane iconStack = new StackPane(iconCircle, clockIcon);

        Color hoverBg, hoverText;
        if (subj.contains("Matematicas") || subj.contains("Fisica")) {
            hoverBg = Color.web(L_PRIMARY);
            hoverText = Color.WHITE;
        } else if (subj.contains("Historia") || subj.contains("Ingles")) {
            hoverBg = Color.web(L_SECONDARY);
            hoverText = Color.WHITE;
        } else {
            hoverBg = Color.web(L_TERTIARY);
            hoverText = Color.WHITE;
        }
        row.setOnMouseEntered(e -> {
            iconCircle.setFill(hoverBg);
            clockIcon.setFill(hoverText);
        });
        row.setOnMouseExited(e -> {
            iconCircle.setFill(Color.web(c(L_SURFACE_LOW, D_SURFACE_LOW)));
            clockIcon.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
        });

        VBox text = new VBox(4);
        Text t1 = new Text(time + " - " + subj);
        t1.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 15));
        t1.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
        Text t2 = new Text(det);
        t2.setFont(Font.font("Plus Jakarta Sans", 13));
        t2.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
        text.getChildren().addAll(t1, t2);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        StackPane actionBtn = new StackPane();
        actionBtn.setPrefSize(40, 40);
        actionBtn.getStyleClass().add("icon-button");
        String arrowPath = isFirst ? ICON_BOLT : ICON_CHEVRON_RIGHT;
        SVGPath arrow = createIcon(arrowPath, isFirst ? 18 : 20, c(isFirst ? L_WHITE : L_OUTLINE, isFirst ? L_WHITE : D_OUTLINE));
        actionBtn.getChildren().add(arrow);
        row.getChildren().addAll(iconStack, text, spacer, actionBtn);

        scheduleCircleList.add(iconCircle);
        scheduleSubjList.add(t1);
        scheduleDetList.add(t2);

        themeUpdaters.add(() -> {
            iconCircle.setFill(Color.web(c(L_SURFACE_LOW, D_SURFACE_LOW)));
            clockIcon.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
            t1.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
            t2.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
            arrow.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));
        });

        return row;
    }

    private void applyCompactScale(boolean compact) {
        double s = compact ? 0.65 : 1.0;
        if (Math.abs(s - compactScale) < 0.01) return;
        compactScale = s;

        // KPI cards
        for (int i = 0; i < kpiCardList.size(); i++) {
            kpiCardList.get(i).setPadding(new Insets(12 * s));
            kpiCardList.get(i).setSpacing(12 * s);
            kpiCircleList.get(i).setRadius(16 * s);
            kpiLabelList.get(i).setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10 * s));
            kpiValueList.get(i).setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18 * s));
        }
        kpiGrid.setSpacing(16 * s);

        // Course table
        courseTitleText.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18 * s));
        double[] cw = {180 * s, 180 * s, 120 * s, 120 * s};
        for (int i = 0; i < colHeaderList.size(); i++) {
            colHeaderList.get(i).setPrefWidth(cw[i]);
        }
        for (List<HBox> cells : courseRowCells) {
            for (int i = 0; i < Math.min(cells.size(), cw.length); i++) {
                cells.get(i).setPrefWidth(cw[i]);
            }
        }

        // Performance
        perfTitleText.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18 * s));
        perfSubText.setFont(Font.font("Plus Jakarta Sans", 12 * s));
        for (Rectangle bar : perfBarsList) {
            bar.setWidth(20 * s);
            bar.setArcWidth(10 * s);
            bar.setArcHeight(10 * s);
        }

        // Container boxes
        scheduleBox.setPadding(new Insets(12 * s));
        scheduleBox.setSpacing(8 * s);
        coursesBox.setPadding(new Insets(compact ? 8 : 0));
        coursesBox.setSpacing(compact ? 8 : 0);
        performanceBox.setPadding(new Insets(12 * s));
        performanceBox.setSpacing(8 * s);

        // Schedule
        scheduleTitleText.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18 * s));
        for (int i = 0; i < scheduleCircleList.size(); i++) {
            scheduleCircleList.get(i).setRadius(24 * s);
            scheduleSubjList.get(i).setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 15 * s));
            scheduleDetList.get(i).setFont(Font.font("Plus Jakarta Sans", 13 * s));
        }
    }

    private SVGPath createIcon(String pathData, double size, String fill) {
        SVGPath icon = new SVGPath();
        icon.setContent(pathData);
        icon.setFill(Color.web(fill));
        double scale = size / 24.0;
        icon.setScaleX(scale);
        icon.setScaleY(scale);
        return icon;
    }

    private void setupTheme() {
        h1.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 32));
        h1.setFill(Color.web(c(L_ON_SURFACE, D_ON_SURFACE)));
        sub.setFont(Font.font("Plus Jakarta Sans", 16));
        sub.setFill(Color.web(c(L_OUTLINE, D_OUTLINE)));

        theme.addListener(() -> {
            for (Runnable r : themeUpdaters) r.run();
        });
        
        // Trigger initial update
        for (Runnable r : themeUpdaters) r.run();
    }
}
