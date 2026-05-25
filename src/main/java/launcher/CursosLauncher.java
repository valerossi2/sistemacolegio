package launcher;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import theme.ThemeManager;
import util.LanguageManager;

public class CursosLauncher extends Application {

    private static final String ICON_LIGHTBULB = "M9 21c0 .55.45 1 1 1h4c.55 0 1-.45 1-1v-1H9v1zm3-19C8.14 2 5 5.14 5 9c0 2.38 1.19 4.47 3 5.74V17c0 .55.45 1 1 1h6c.55 0 1-.45 1-1v-2.26c1.81-1.27 3-3.36 3-5.74 0-3.86-3.14-7-7-7z";
    private static final String ICON_HOME = "M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z";
    private static final String ICON_SCHOOL = "M12 3L1 9l4 2.18v6L12 21l7-3.82v-6l2-1.09V17h2V9L12 3zm6.82 6L12 12.72 5.18 9 12 5.28 18.82 9zM17 15.99l-5 2.73-5-2.73v-3.72L12 15l5-2.73v3.72z";
    private static final String ICON_GROUP = "M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z";
    private static final String ICON_BOOK = "M19 2l-5 4.5v11l5-4.5V2zM6.5 5C4.55 5 2.45 5.4 1 6.5v11.5c1.45-1.1 3.55-1.5 5.5-1.5s4.05.4 5.5 1.5V6.5C14.95 5.4 12.85 5 10.5 5S8.05 5.4 6.5 5z";
    private static final String ICON_CALENDAR = "M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11z";
    private static final String ICON_SETTINGS = "M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.07.62-.07.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z";
    private static final String ICON_SEARCH = "M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z";
    private static final String ICON_MOON = "M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z";
    private static final String ICON_SUN = "M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 1 1-8 0 4 4 0 0 1 8 0z";
    private static final String ICON_LANG = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z";
    private static final String ICON_AVATAR = "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z";

    private ThemeManager theme = new ThemeManager();
    private LanguageManager lang = LanguageManager.getInstance();
    private Stage stage;
    private boolean maximized = false;
    private double dragOffsetX, dragOffsetY;
    private VBox root;
    private StackPane centerWrapper;
    private HBox navItemsContainer;
    private Text lTitle, lSub;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        buildUI();
        Scene scene = new Scene(root, 1300, 850);
        applyTheme();
        stage.setScene(scene);
        stage.show();
    }

    private void buildUI() {
        root = new VBox();
        root.getStyleClass().add("root-pane");
        root.setPrefWidth(1300);
        root.setPrefHeight(850);

        // Title bar
        HBox titleBar = buildTitleBar();
        root.getChildren().add(titleBar);

        // Main horizontal split: sidebar + content
        HBox mainSplit = new HBox();
        VBox.setVgrow(mainSplit, Priority.ALWAYS);

        // Sidebar
        VBox sidebar = buildSidebar();
        mainSplit.getChildren().add(sidebar);

        // Content area (topbar + center)
        VBox contentArea = new VBox();
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        HBox topBar = buildTopBar();
        contentArea.getChildren().add(topBar);

        centerWrapper = new StackPane();
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(centerWrapper, Priority.ALWAYS);
        contentArea.getChildren().add(centerWrapper);

        mainSplit.getChildren().add(contentArea);
        root.getChildren().add(mainSplit);
        root.widthProperty().addListener((o, ov, nv) -> handleResize(nv.doubleValue()));

        loadCursosView();
    }

    private HBox buildTitleBar() {
        HBox bar = new HBox();
        bar.getStyleClass().add("launcher-titlebar");
        bar.setAlignment(Pos.CENTER_LEFT);

        Text label = new Text("Lumina Academy — Cursos");
        label.getStyleClass().add("launcher-titlebar__label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox controls = new HBox(0);
        controls.setAlignment(Pos.CENTER_RIGHT);

        Button minBtn = new Button();
        minBtn.getStyleClass().add("launcher-window-btn");
        SVGPath minIcon = new SVGPath();
        minIcon.setContent("M4 14h16v2H4z");
        minIcon.setScaleX(0.5);
        minIcon.setScaleY(0.5);
        minBtn.setGraphic(minIcon);

        Button maxBtn = new Button();
        maxBtn.getStyleClass().add("launcher-window-btn");
        SVGPath maxIcon = new SVGPath();
        maxIcon.setContent("M5 5h14v14H5z");
        maxIcon.setScaleX(0.5);
        maxIcon.setScaleY(0.5);
        maxIcon.setFill(null);
        maxIcon.setStrokeWidth(1.8);
        maxBtn.setGraphic(maxIcon);

        Button closeBtn = new Button();
        closeBtn.getStyleClass().addAll("launcher-window-btn", "launcher-window-btn--close");
        SVGPath closeIcon = new SVGPath();
        closeIcon.setContent("M6 6l12 12M18 6L6 18");
        closeIcon.setScaleX(0.5);
        closeIcon.setScaleY(0.5);
        closeIcon.setStrokeWidth(1.8);
        closeBtn.setGraphic(closeIcon);

        controls.getChildren().addAll(minBtn, maxBtn, closeBtn);
        bar.getChildren().addAll(label, spacer, controls);

        minBtn.setOnAction(e -> stage.setIconified(true));
        maxBtn.setOnAction(e -> toggleMaximize());
        closeBtn.setOnAction(e -> Platform.exit());

        bar.setOnMousePressed(e -> {
            dragOffsetX = e.getScreenX() - stage.getX();
            dragOffsetY = e.getScreenY() - stage.getY();
        });
        bar.setOnMouseDragged(e -> {
            if (maximized) toggleMaximize();
            stage.setX(e.getScreenX() - dragOffsetX);
            stage.setY(e.getScreenY() - dragOffsetY);
        });
        bar.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) toggleMaximize();
        });
        return bar;
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(24);
        sidebar.getStyleClass().add("side-navbar");
        sidebar.setPrefWidth(256);
        sidebar.setMinWidth(256);

        HBox brand = new HBox(12);
        brand.getStyleClass().add("side-navbar__brand");
        brand.setAlignment(Pos.CENTER_LEFT);

        StackPane logoStack = new StackPane();
        Circle logoCircle = new Circle(20, Color.web("#3FC2E8"));
        SVGPath logoSvg = new SVGPath();
        logoSvg.setContent(ICON_LIGHTBULB);
        logoSvg.setFill(Color.WHITE);
        logoSvg.setScaleX(20.0/24);
        logoSvg.setScaleY(20.0/24);
        logoStack.getChildren().addAll(logoCircle, logoSvg);

        VBox brandText = new VBox(2);
        lTitle = new Text("Lumina Academy");
        lTitle.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 16));
        lTitle.setFill(Color.web("#234E5D"));
        lSub = new Text("ADMIN PORTAL");
        lSub.setFont(Font.font("Plus Jakarta Sans", FontWeight.MEDIUM, 11));
        lSub.setFill(Color.web("#64748B"));
        brandText.getChildren().addAll(lTitle, lSub);

        brand.getChildren().addAll(logoStack, brandText);
        sidebar.getChildren().add(brand);

        navItemsContainer = new HBox();
        // Using VBox via a trick
        VBox navContainer = new VBox(12);
        navContainer.getStyleClass().add("nav-items-container");
        navContainer.setVgrow(Priority.ALWAYS);

        buildNavItems(navContainer);
        sidebar.getChildren().add(navContainer);

        HBox profile = new HBox(12);
        profile.getStyleClass().add("side-navbar__profile");
        profile.setAlignment(Pos.CENTER_LEFT);

        Circle avatarCircle = new Circle(20, Color.web("#E2E8F0"));
        SVGPath avatarSvg = new SVGPath();
        avatarSvg.setContent(ICON_AVATAR);
        avatarSvg.setFill(Color.web("#64748B"));
        avatarSvg.setScaleX(20.0/24);
        avatarSvg.setScaleY(20.0/24);
        StackPane avatarStack = new StackPane(avatarCircle, avatarSvg);

        VBox profileText = new VBox(2);
        Text pName = new Text("Admin User");
        pName.getStyleClass().add("profile-name");
        Text pEmail = new Text("admin@lumina.edu");
        pEmail.getStyleClass().add("profile-email");
        profileText.getChildren().addAll(pName, pEmail);

        profile.getChildren().addAll(avatarStack, profileText);
        sidebar.getChildren().add(profile);
        return sidebar;
    }

    private void buildNavItems(VBox container) {
        String[][] items = {
                {lang.get("sidebar.home"), ICON_HOME},
                {lang.get("sidebar.students"), ICON_SCHOOL},
                {lang.get("sidebar.teachers"), ICON_GROUP},
                {lang.get("sidebar.courses"), ICON_BOOK},
                {lang.get("sidebar.schedule"), ICON_CALENDAR},
                {lang.get("sidebar.settings"), ICON_SETTINGS}
        };

        for (int i = 0; i < items.length; i++) {
            HBox item = new HBox(12);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setPadding(new Insets(10, 16, 10, 16));
            item.setPrefWidth(Double.MAX_VALUE);

            Text icon = new Text(i == 3 ? "📚" : i == 0 ? "🏠" : i == 1 ? "🎓" : i == 2 ? "👥" : i == 4 ? "📅" : "⚙");
            icon.getStyleClass().add("nav-item__icon");

            Text label = new Text(items[i][0]);
            label.getStyleClass().add("nav-item__label");

            if (i == 3) {
                item.getStyleClass().add("nav-item--active");
                icon.setFill(Color.WHITE);
                label.setFill(Color.WHITE);
            } else {
                item.getStyleClass().add("nav-item");
            }

            item.getChildren().addAll(icon, label);
            container.getChildren().add(item);
        }
    }

    private HBox buildTopBar() {
        HBox topBar = new HBox();
        topBar.getStyleClass().add("top-app-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.getStyleClass().add("search-bar");

        Text searchIcon = new Text("🔍");
        searchIcon.getStyleClass().add("search-bar__icon");

        TextField searchField = new TextField();
        searchField.setPromptText(lang.get("search.prompt", "Search courses..."));
        searchField.getStyleClass().add("search-bar__input");
        searchField.setPrefWidth(300);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        searchBox.getChildren().addAll(searchIcon, searchField);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox actions = new HBox(8);
        actions.getStyleClass().add("launcher-header-actions");
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button langBtn = new Button("EN");
        langBtn.getStyleClass().add("launcher-toggle-btn");
        langBtn.setOnAction(e -> toggleLanguage(langBtn));

        Button themeBtn = new Button();
        themeBtn.getStyleClass().add("launcher-toggle-btn");
        themeBtn.setOnAction(e -> toggleTheme(themeBtn));
        updateThemeBtnText(themeBtn);

        actions.getChildren().addAll(langBtn, themeBtn);

        topBar.getChildren().addAll(searchBox, spacer, actions);
        return topBar;
    }

    private void toggleLanguage(Button btn) {
        String current = lang.getCurrentLanguageCode();
        if ("es".equals(current)) {
            lang.setLanguage("en");
            btn.setText("ES");
        } else {
            lang.setLanguage("es");
            btn.setText("EN");
        }
    }

    private void toggleTheme(Button btn) {
        theme.toggle();
        applyTheme();
        updateThemeBtnText(btn);
    }

    private void updateThemeBtnText(Button btn) {
        btn.setText(theme.isDark() ? "☀" : "☾");
    }

    private void applyTheme() {
        if (root == null) return;
        String css = getClass().getResource("/css/Cursos.css").toExternalForm();
        root.getStylesheets().clear();
        root.getStylesheets().add(css);

        if (theme.isDark()) {
            root.setStyle("-fx-cursos-bg: #0F172A; -fx-cursos-surface: #1E293B; -fx-cursos-surface-hover: #334155; -fx-cursos-surface-active: #1E3A5F; -fx-cursos-border: #334155; -fx-cursos-border-light: #1E293B; -fx-cursos-text-primary: #F8FAFC; -fx-cursos-text-secondary: #CBD5E1; -fx-cursos-text-muted: #64748B; -fx-cursos-text-extra-muted: #475569; -fx-cursos-table-header-bg: #1E293B; -fx-cursos-input-bg: #334155; -fx-cursos-input-border: #475569; -fx-cursos-pagination-bg: #334155; -fx-cursos-pagination-hover: #475569; -fx-cursos-sidebar-bg: #1E293B; -fx-cursos-sidebar-border: #334155; -fx-cursos-topbar-bg: #1E293B;".replace("; ", ";\n");
        } else {
            root.setStyle("");
        }
    }

    private void toggleMaximize() {
        maximized = !maximized;
        stage.setFullScreen(maximized);
    }

    private void handleResize(double width) {
        boolean compact = width < 900;
        if (centerWrapper.getChildren().isEmpty()) return;
        Node view = centerWrapper.getChildren().get(0);
        if (view instanceof ScrollPane sp) {
            Node content = sp.getContent();
            if (content instanceof Pane pane) {
                pane.setPrefWidth(compact ? width - 60 : Math.min(width - 80, 1200));
            }
        }
    }

    private void loadCursosView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/AdminCursos.fxml"));
            Parent view = loader.load();
            centerWrapper.getChildren().setAll(view);
            StackPane.setAlignment(view, Pos.TOP_LEFT);
        } catch (Exception e) {
            e.printStackTrace();
            Label err = new Label("Error loading courses view: " + e.getMessage());
            centerWrapper.getChildren().setAll(err);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
