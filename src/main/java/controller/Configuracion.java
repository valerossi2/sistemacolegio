package controller;

import theme.ThemeManager;
import util.LanguageManager;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Configuracion {

    private ThemeManager theme;
    private VBox root;
    private Stage ownerStage;
    private Runnable updateTheme;

    private double compactScale = 1.0;
    private VBox content;
    private Text title, subtitle;
    private HBox profileSection;
    private VBox headerBox;
    private Text userName, userEmail;
    private Button editBtn;
    private Circle avatarBg;
    private static final double COMPACT_THRESHOLD = 700;

    private Text prefsTitle, langLabel, langDesc, themeLabel, themeDesc;
    private Text secTitle;
    private Button changePwdBtn, logoutBtn;
    private Button editModalTitle, editSaveBtn, editCancelBtn, editCloseBtn, editPhotoText;
    private VBox editNameField, editEmailField, editRoleField;
    private Text changePwdModalTitle, changePwdCloseBtn;
    private Button changePwdSaveBtn, changePwdCancelBtn;
    private VBox changePwdOldField, changePwdNewField, changePwdConfirmField;
    private ComboBox<String> langCombo;
    private Runnable languageUpdater;

    public Configuracion(ThemeManager theme) {
        this.theme = theme;
        root = new VBox();
        root.setPadding(new Insets(16, 24, 16, 24));
        root.setMaxWidth(900);

        title = new Text("Configuracion del Sistema");
        title.setFont(Font.font("Inter", FontWeight.BOLD, 22));

        subtitle = new Text("Bienvenido de nuevo. Aqui tienes un resumen del estado institucional hoy.");
        subtitle.setFont(Font.font("Inter", 12));

        headerBox = new VBox(2);
        headerBox.getChildren().addAll(title, subtitle);
        VBox.setMargin(headerBox, new Insets(0, 0, 10, 0));

        content = new VBox(10);
        content.setMaxWidth(820);

        profileSection = createProfileSection();
        VBox preferencesSection = createPreferencesSection();
        VBox securitySection = createSecuritySection();
        VBox logoutSection = createLogoutSection();

        content.getChildren().addAll(profileSection, preferencesSection, securitySection, logoutSection);

        updateTheme = () -> {
            root.setStyle("-fx-background-color: " + theme.bg() + ";");
            title.setFill(Color.web(theme.text()));
            subtitle.setFill(Color.web(theme.muted()));
        };

        setupResponsive();
        root.getChildren().addAll(headerBox, content);
        theme.addListener(updateTheme);
        updateTheme.run();

        languageUpdater = this::updateTexts;
        LanguageManager.getInstance().addListener(languageUpdater);
        updateTexts();
    }

    public VBox getView() {
        return root;
    }

    public void setOwnerStage(Stage stage) {
        this.ownerStage = stage;
    }

    private String cardStyle() {
        return "-fx-background-color: " + theme.card() + "; -fx-background-radius: 16; " +
            "-fx-border-color: " + theme.border() + "; -fx-border-radius: 16; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.05), 20, 0, 0, 4);";
    }

    private HBox createProfileSection() {
        HBox section = new HBox();
        section.setPadding(new Insets(14, 20, 14, 20));
        section.setStyle(cardStyle());

        HBox leftBox = new HBox(12);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        StackPane avatarStack = new StackPane();
        avatarBg = new Circle(22, Color.web(theme.isDark() ? "#475569" : "#dbe1ff"));
        SVGPath avatarIcon = new SVGPath();
        avatarIcon.setContent("M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z");
        avatarIcon.setScaleX(0.9);
        avatarIcon.setScaleY(0.9);
        avatarIcon.setFill(Color.web(theme.textSec()));
        avatarStack.getChildren().addAll(avatarBg, avatarIcon);

        Circle onlineDot = new Circle(4, Color.web(ThemeManager.COLOR_GREEN));
        onlineDot.setTranslateX(15);
        onlineDot.setTranslateY(15);
        onlineDot.setStrokeWidth(1.5);
        StackPane avatarWrapper = new StackPane(avatarStack, onlineDot);

        VBox nameBox = new VBox(0);
        userName = new Text("Admin User");
        userName.setFont(Font.font("Inter", FontWeight.BOLD, 16));
        userName.setFill(Color.web(theme.text()));
        userEmail = new Text("admin@lumina.edu");
        userEmail.setFont(Font.font("Inter", 12));
        userEmail.setFill(Color.web(theme.muted()));
        nameBox.getChildren().addAll(userName, userEmail);

        leftBox.getChildren().addAll(avatarWrapper, nameBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        editBtn = new Button("Editar Perfil");
        editBtn.setFont(Font.font("Inter", FontWeight.BOLD, 12));
        String btnPad = (int)(8 * compactScale) + " " + (int)(24 * compactScale);
        editBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: " + btnPad + ";");

        editBtn.setOnMouseEntered(e -> editBtn.setStyle(
            "-fx-background-color: #1D4ED8; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: " + (int)(8 * compactScale) + " " + (int)(24 * compactScale) + ";"));
        editBtn.setOnMouseExited(e -> editBtn.setStyle(
            "-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: " + (int)(8 * compactScale) + " " + (int)(24 * compactScale) + ";"));
        editBtn.setOnMouseClicked(e -> showEditProfileModal());

        section.getChildren().addAll(leftBox, spacer, editBtn);

        theme.addListener(() -> {
            section.setStyle(cardStyle());
            avatarBg.setFill(Color.web(theme.isDark() ? "#475569" : "#dbe1ff"));
            avatarIcon.setFill(Color.web(theme.textSec()));
            onlineDot.setStroke(Color.WHITE);
            userName.setFill(Color.web(theme.text()));
            userEmail.setFill(Color.web(theme.muted()));
            editBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: " + (int)(8 * compactScale) + " " + (int)(24 * compactScale) + ";");
        });

        return section;
    }

    private VBox createPreferencesSection() {
        VBox section = new VBox();
        section.setPadding(new Insets(14, 20, 14, 20));
        section.setStyle(cardStyle());

        prefsTitle = new Text("Preferencias de la Cuenta");
        prefsTitle.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        prefsTitle.setFill(Color.web(theme.text()));
        VBox.setMargin(prefsTitle, new Insets(0, 0, 8, 0));

        HBox idiomaRow = createIdiomaRow();
        HBox temaRow = createTemaRow();

        section.getChildren().addAll(prefsTitle, idiomaRow, temaRow);

        theme.addListener(() -> {
            section.setStyle(cardStyle());
            prefsTitle.setFill(Color.web(theme.text()));
        });

        return section;
    }

    private HBox createIdiomaRow() {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 0, 8, 0));

        Circle iconCircle = new Circle(16, Color.web(theme.isDark() ? "#1E3A5F" : "#dbe1ff"));
        SVGPath globeIcon = new SVGPath();
        globeIcon.setContent("M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.53c-.26-.81-1-1.4-1.9-1.4h-1v-3c0-.55-.45-1-1-1h-6v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z");
        globeIcon.setFill(Color.web(ThemeManager.COLOR_PRIMARY));
        globeIcon.setScaleX(0.7);
        globeIcon.setScaleY(0.7);
        StackPane iconStack = new StackPane(iconCircle, globeIcon);

        VBox textBox = new VBox(0);
        langLabel = new Text("Idioma");
        langLabel.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        langLabel.setFill(Color.web(theme.text()));
        langDesc = new Text("Cambia la configuracion del idioma de la cuenta");
        langDesc.setFont(Font.font("Inter", 11));
        langDesc.setFill(Color.web(theme.muted()));
        textBox.getChildren().addAll(langLabel, langDesc);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        langCombo = new ComboBox<>();
        langCombo.getItems().addAll("Español", "Inglés");
        String currentLang = LanguageManager.getInstance().getCurrentLanguageCode();
        langCombo.setValue(currentLang.equals("en") ? "Inglés" : "Español");
        langCombo.setPrefWidth(160);
        langCombo.setOnAction(e -> {
            String selected = langCombo.getValue();
            LanguageManager lm = LanguageManager.getInstance();
            if (lm.get("config.prefs.lang.en").equals(selected)) {
                lm.setLanguage("en");
            } else {
                lm.setLanguage("es");
            }
        });

        row.getChildren().addAll(iconStack, textBox, spacer, langCombo);

        Runnable updateRowTheme = () -> {
            row.setStyle("-fx-border-color: transparent transparent " + theme.divider() + " transparent;");
            iconCircle.setFill(Color.web(theme.isDark() ? "#1E3A5F" : "#dbe1ff"));
            langLabel.setFill(Color.web(theme.text()));
            langDesc.setFill(Color.web(theme.muted()));
            langCombo.setStyle("-fx-background-color: " + theme.inputBg() + "; " +
                "-fx-border-color: " + theme.inputBorder() + "; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-font-family: 'Inter'; -fx-font-size: 13; " +
                "-fx-padding: 4 8;");
        };

        theme.addListener(updateRowTheme);
        updateRowTheme.run();

        return row;
    }

    private HBox createTemaRow() {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 0, 0, 0));

        Circle iconCircle = new Circle(16);
        iconCircle.setFill(Color.web("#dbe1ff"));
        SVGPath sunIcon = new SVGPath();
        sunIcon.setContent("M12 3a9 9 0 0 0 0 18c.83 0 1.5-.67 1.5-1.5 0-.39-.15-.74-.39-1.01-.23-.26-.38-.61-.38-.99 0-.83.67-1.5 1.5-1.5H16c2.76 0 5-2.24 5-5 0-4.42-4.03-8-9-8zm-5.5 9c-.83 0-1.5-.67-1.5-1.5S5.67 9 6.5 9 8 9.67 8 10.5 7.33 12 6.5 12zm3-4C8.67 8 8 7.33 8 6.5S8.67 5 9.5 5s1.5.67 1.5 1.5S10.33 8 9.5 8zm5 0c-.83 0-1.5-.67-1.5-1.5S13.67 5 14.5 5s1.5.67 1.5 1.5S15.33 8 14.5 8zm3 4c-.83 0-1.5-.67-1.5-1.5s.67-1.5 1.5-1.5 1.5.67 1.5 1.5-.67 1.5-1.5 1.5z");
        sunIcon.setScaleX(0.6);
        sunIcon.setScaleY(0.6);
        sunIcon.setFill(Color.web(theme.textSec()));
        StackPane iconStack = new StackPane(iconCircle, sunIcon);

        VBox textBox = new VBox(0);
        themeLabel = new Text("Tema");
        themeLabel.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        themeLabel.setFill(Color.web(theme.text()));
        themeDesc = new Text("Personaliza la apariencia de tu interfaz");
        themeDesc.setFont(Font.font("Inter", 11));
        themeDesc.setFill(Color.web(theme.muted()));
        textBox.getChildren().addAll(themeLabel, themeDesc);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox toggleGroup = new HBox(4);
        toggleGroup.setPadding(new Insets(3));
        toggleGroup.setStyle("-fx-background-radius: 32;");
        toggleGroup.setCursor(javafx.scene.Cursor.HAND);

        StackPane sunBtn = new StackPane();
        sunBtn.setPrefSize(28, 28);
        sunBtn.setStyle("-fx-background-radius: 32; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);");
        SVGPath sunSmall = new SVGPath();
        sunSmall.setContent("M12 7c-2.76 0-5 2.24-5 5s2.24 5 5 5 5-2.24 5-5-2.24-5-5-5zM11 2h2v3h-2zM11 19h2v3h-2zM2 11h3v2H2zM19 11h3v2h-3zM5.64 5.64l1.41-1.41 2.12 2.12-1.41 1.41zM14.83 16.24l1.41-1.41 2.12 2.12-1.41 1.41zM5.64 18.36l2.12-2.12 1.41 1.41-2.12 2.12zM14.83 7.76l2.12-2.12 1.41 1.41-2.12 2.12z");
        sunSmall.setFill(Color.web(ThemeManager.COLOR_PRIMARY));
        sunSmall.setScaleX(0.7);
        sunSmall.setScaleY(0.7);
        sunBtn.getChildren().add(sunSmall);

        StackPane moonBtn = new StackPane();
        moonBtn.setPrefSize(28, 28);
        SVGPath moonIcon = new SVGPath();
        moonIcon.setContent("M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z");
        moonIcon.setScaleX(0.7);
        moonIcon.setScaleY(0.7);
        moonIcon.setFill(Color.web(theme.muted()));
        moonBtn.getChildren().add(moonIcon);

        toggleGroup.getChildren().addAll(sunBtn, moonBtn);

        sunBtn.setOnMouseClicked(e -> theme.setDark(false));
        moonBtn.setOnMouseClicked(e -> theme.setDark(true));

        row.getChildren().addAll(iconStack, textBox, spacer, toggleGroup);

        Runnable updateTemaRow = () -> {
            row.setStyle("-fx-border-color: transparent transparent " + theme.divider() + " transparent;");
            iconCircle.setFill(Color.web(theme.isDark() ? "#475569" : "#dbe1ff"));
            sunIcon.setFill(Color.web(theme.textSec()));
            themeLabel.setFill(Color.web(theme.text()));
            themeDesc.setFill(Color.web(theme.muted()));
            toggleGroup.setStyle("-fx-background-color: " + theme.toggleGroupBg() + "; -fx-background-radius: 32;");
            sunBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "transparent" : "white") + "; " +
                "-fx-background-radius: 32; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);");
            moonBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "white" : "transparent") + "; " +
                "-fx-background-radius: 32; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);");
            moonIcon.setFill(Color.web(theme.isDark() ? ThemeManager.COLOR_PRIMARY : theme.muted()));
            sunSmall.setFill(Color.web(theme.isDark() ? theme.muted() : ThemeManager.COLOR_PRIMARY));
        };

        theme.addListener(updateTemaRow);
        updateTemaRow.run();

        return row;
    }

    private VBox createSecuritySection() {
        VBox section = new VBox();
        section.setPadding(new Insets(14, 20, 14, 20));
        section.setStyle(cardStyle());

        secTitle = new Text("Seguridad");
        secTitle.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        secTitle.setFill(Color.web(theme.text()));
        VBox.setMargin(secTitle, new Insets(0, 0, 8, 0));

        changePwdBtn = new Button("Cambiar Contrasena");
        changePwdBtn.setMaxWidth(Double.MAX_VALUE);
        changePwdBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        changePwdBtn.setStyle("-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 0; " +
            "-fx-border-radius: 10; -fx-border-width: 1.5; " +
            "-fx-background-color: transparent; -fx-text-fill: " + ThemeManager.COLOR_PRIMARY + "; " +
            "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + ";");

        changePwdBtn.setOnMouseEntered(e -> changePwdBtn.setStyle(
            "-fx-background-color: " + (theme.isDark() ? "#1E3A5F" : "#EFF6FF") + "; " +
            "-fx-text-fill: " + ThemeManager.COLOR_PRIMARY + "; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
            "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 0; " +
            "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-border-radius: 10; -fx-border-width: 1.5;"));
        changePwdBtn.setOnMouseExited(e -> changePwdBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: " + ThemeManager.COLOR_PRIMARY + "; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
            "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 0; " +
            "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-border-radius: 10; -fx-border-width: 1.5;"));
        changePwdBtn.setOnMouseClicked(e -> showChangePasswordModal());

        section.getChildren().addAll(secTitle, changePwdBtn);

        theme.addListener(() -> {
            section.setStyle(cardStyle());
            secTitle.setFill(Color.web(theme.text()));
            changePwdBtn.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + ThemeManager.COLOR_PRIMARY + "; " +
                "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
                "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 0; " +
                "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-border-radius: 10; -fx-border-width: 1.5;");
        });

        return section;
    }

    private void updateTexts() {
        LanguageManager lang = LanguageManager.getInstance();
        title.setText(lang.get("config.title"));
        subtitle.setText(lang.get("config.subtitle"));
        userName.setText(lang.get("config.profile.name"));
        userEmail.setText(lang.get("config.profile.email"));
        editBtn.setText(lang.get("config.profile.editBtn"));
        prefsTitle.setText(lang.get("config.prefs.title"));
        langLabel.setText(lang.get("config.prefs.language"));
        langDesc.setText(lang.get("config.prefs.languageDesc"));
        themeLabel.setText(lang.get("config.prefs.theme"));
        themeDesc.setText(lang.get("config.prefs.themeDesc"));
        secTitle.setText(lang.get("config.security.title"));
        changePwdBtn.setText(lang.get("config.security.changePwd"));
        logoutBtn.setText(lang.get("config.security.logout"));

        String selected = lang.getCurrentLanguageCode().equals("en")
            ? lang.get("config.prefs.lang.en")
            : lang.get("config.prefs.lang.es");
        langCombo.getItems().setAll(lang.get("config.prefs.lang.es"), lang.get("config.prefs.lang.en"));
        langCombo.setValue(selected);
    }

    private void setupResponsive() {
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            double w = newVal.doubleValue();
            applyCompactScale(w < COMPACT_THRESHOLD);
        });
    }

    private void applyCompactScale(boolean compact) {
        double s = compact ? 0.7 : 1.0;
        if (Math.abs(s - compactScale) < 0.01) return;
        compactScale = s;

        title.setFont(Font.font("Inter", FontWeight.BOLD, 22 * s));
        subtitle.setFont(Font.font("Inter", 12 * s));
        VBox.setMargin(headerBox, new Insets(0, 0, 10 * s, 0));
        content.setSpacing(10 * s);
        root.setPadding(new Insets(16 * s, 24 * s, 16 * s, 24 * s));

        profileSection.setPadding(new Insets(14 * s, 20 * s, 14 * s, 20 * s));
        userName.setFont(Font.font("Inter", FontWeight.BOLD, 16 * s));
        userEmail.setFont(Font.font("Inter", 12 * s));
        avatarBg.setRadius(22 * s);
        String btnPad = (int)(8 * s) + " " + (int)(24 * s);
        editBtn.setFont(Font.font("Inter", FontWeight.BOLD, 12 * s));
        editBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: " + btnPad + ";");
    }

    private void showEditProfileModal() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.setWidth(460);
        modal.setHeight(560);
        if (ownerStage != null) {
            modal.initOwner(ownerStage);
            modal.setX(ownerStage.getX() + (ownerStage.getWidth() - 460) / 2);
            modal.setY(ownerStage.getY() + (ownerStage.getHeight() - 560) / 2);
        }

        VBox dialog = new VBox();
        dialog.setPadding(new Insets(28));
        dialog.setSpacing(20);
        dialog.setMaxWidth(460);
        String dialogBg = theme.isDark() ? "#1E293B" : "#FFFFFF";
        dialog.setStyle("-fx-background-color: " + dialogBg + "; -fx-background-radius: 20; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 30, 0, 0, 8);");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        LanguageManager lang = LanguageManager.getInstance();
        Text modalTitle = new Text(lang.get("config.editProfile.title"));
        modalTitle.setFont(Font.font("Inter", FontWeight.BOLD, 20));
        modalTitle.setFill(Color.web(theme.text()));
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Button closeBtn = new Button("X");
        closeBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + theme.muted() + "; " +
            "-fx-cursor: hand; -fx-padding: 4 8; -fx-background-radius: 8;");
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "#334155" : "#F1F5F9") + "; " +
            "-fx-text-fill: " + theme.text() + "; -fx-cursor: hand; -fx-padding: 4 8; -fx-background-radius: 8;"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + theme.muted() + "; " +
            "-fx-cursor: hand; -fx-padding: 4 8; -fx-background-radius: 8;"));
        closeBtn.setOnMouseClicked(e -> modal.close());
        topBar.getChildren().addAll(modalTitle, sp, closeBtn);

        VBox avatarSection = new VBox(8);
        avatarSection.setAlignment(Pos.CENTER);
        
        StackPane avatarOuter = new StackPane();
        Circle avatarBig = new Circle(36, Color.web(theme.isDark() ? "#475569" : "#dbe1ff"));
        SVGPath avatarBigIcon = new SVGPath();
        avatarBigIcon.setContent("M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z");
        avatarBigIcon.setScaleX(1.3);
        avatarBigIcon.setScaleY(1.3);
        avatarBigIcon.setFill(Color.web(theme.textSec()));
        
        StackPane cameraOverlay = new StackPane();
        cameraOverlay.setPrefSize(18, 18);
        cameraOverlay.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-background-radius: 9; " +
            "-fx-cursor: hand;");
        SVGPath cameraIcon = new SVGPath();
        cameraIcon.setContent("M12 15.2a3.2 3.2 0 1 0 0-6.4 3.2 3.2 0 0 0 0 6.4zM9 2L7.17 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2h-3.17L15 2H9zm3 15c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5z");
        cameraIcon.setScaleX(0.4);
        cameraIcon.setScaleY(0.4);
        cameraIcon.setFill(Color.WHITE);
        cameraOverlay.getChildren().add(cameraIcon);
        cameraOverlay.setTranslateX(24);
        cameraOverlay.setTranslateY(24);
        
        avatarOuter.getChildren().addAll(avatarBig, avatarBigIcon, cameraOverlay);
        Text changePhotoText = new Text(lang.get("config.editProfile.photo"));
        changePhotoText.setFont(Font.font("Inter", FontWeight.MEDIUM, 12));
        changePhotoText.setFill(Color.web(theme.muted()));
        avatarSection.getChildren().addAll(avatarOuter, changePhotoText);
        VBox.setMargin(avatarSection, new Insets(0, 0, 4, 0));

        VBox fields = new VBox(12);
        VBox nameField = createInputField(lang.get("config.editProfile.name"), "Admin User");
        TextField nameTf = (TextField) nameField.getChildren().get(1);
        nameTf.setStyle(nameTf.getStyle() + "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-border-width: 2;");
        
        fields.getChildren().addAll(
            nameField,
            createInputField(lang.get("config.editProfile.email"), "admin@lumina.edu"),
            createInputField(lang.get("config.editProfile.role"), "Admin")
        );

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button saveBtn = new Button(lang.get("config.editProfile.save"));
        saveBtn.setPrefWidth(160);
        saveBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        saveBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.3), 15, 0, 0, 8);");
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-background-color: #1D4ED8; -fx-text-fill: white; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; -fx-background-radius: 10; " +
            "-fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.4), 20, 0, 0, 10);"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; -fx-background-radius: 10; " +
            "-fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.3), 15, 0, 0, 8);"));
        saveBtn.setOnMouseClicked(e -> modal.close());

        Button cancelBtn = new Button(lang.get("config.editProfile.cancel"));
        cancelBtn.setPrefWidth(160);
        cancelBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        cancelBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "#334155" : "white") + "; " +
            "-fx-text-fill: " + theme.text() + "; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-border-color: " + (theme.isDark() ? "#475569" : "#E2E8F0") + "; -fx-border-radius: 10; -fx-border-width: 1;");
        cancelBtn.setOnMouseClicked(e -> modal.close());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);

        dialog.getChildren().addAll(topBar, avatarSection, fields, buttonBox);

        StackPane backdrop = new StackPane(dialog);
        backdrop.setPadding(new Insets(80));
        String backdropBg = "transparent";
        backdrop.setStyle("-fx-background-color: " + backdropBg + ";");

        Scene scene = new Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        modal.setScene(scene);
        modal.showAndWait();
    }

    private void showChangePasswordModal() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.setWidth(460);
        modal.setHeight(560);
        if (ownerStage != null) {
            modal.initOwner(ownerStage);
            modal.setX(ownerStage.getX() + (ownerStage.getWidth() - 460) / 2);
            modal.setY(ownerStage.getY() + (ownerStage.getHeight() - 560) / 2);
        }

        VBox dialog = new VBox();
        dialog.setPadding(new Insets(28));
        dialog.setSpacing(20);
        dialog.setMaxWidth(460);
        String dialogBg = theme.isDark() ? "#1E293B" : "#FFFFFF";
        dialog.setStyle("-fx-background-color: " + dialogBg + "; -fx-background-radius: 20; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 30, 0, 0, 8);");

        LanguageManager lang = LanguageManager.getInstance();

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        Text modalTitle = new Text(lang.get("config.changePassword.title"));
        modalTitle.setFont(Font.font("Inter", FontWeight.BOLD, 20));
        modalTitle.setFill(Color.web(theme.text()));
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Button closeBtn = new Button("X");
        closeBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + theme.muted() + "; " +
            "-fx-cursor: hand; -fx-padding: 4 8; -fx-background-radius: 8;");
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "#334155" : "#F1F5F9") + "; " +
            "-fx-text-fill: " + theme.text() + "; -fx-cursor: hand; -fx-padding: 4 8; -fx-background-radius: 8;"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + theme.muted() + "; " +
            "-fx-cursor: hand; -fx-padding: 4 8; -fx-background-radius: 8;"));
        closeBtn.setOnMouseClicked(e -> modal.close());
        topBar.getChildren().addAll(modalTitle, sp, closeBtn);

        VBox fields = new VBox(16);
        VBox oldPwdField = createPasswordField(lang.get("config.changePassword.actual"), lang.get("config.changePassword.actual"));
        VBox newPwdField = createPasswordField(lang.get("config.changePassword.new"), lang.get("config.changePassword.new"));
        VBox confirmPwdField = createPasswordField(lang.get("config.changePassword.confirm"), lang.get("config.changePassword.confirm"));

        fields.getChildren().addAll(oldPwdField, newPwdField, confirmPwdField);

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button saveBtn = new Button(lang.get("config.changePassword.update"));
        saveBtn.setPrefWidth(180);
        saveBtn.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        saveBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.3), 15, 0, 0, 8);");
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-background-color: #1D4ED8; -fx-text-fill: white; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 13; -fx-background-radius: 10; " +
            "-fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.4), 20, 0, 0, 10);"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 13; -fx-background-radius: 10; " +
            "-fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.3), 15, 0, 0, 8);"));
        saveBtn.setOnMouseClicked(e -> modal.close());

        Button cancelBtn = new Button(lang.get("config.changePassword.cancel"));
        cancelBtn.setPrefWidth(140);
        cancelBtn.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        cancelBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "#334155" : "white") + "; " +
            "-fx-text-fill: " + theme.text() + "; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-border-color: " + (theme.isDark() ? "#475569" : "#E2E8F0") + "; -fx-border-radius: 10; -fx-border-width: 1;");
        cancelBtn.setOnMouseClicked(e -> modal.close());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);

        dialog.getChildren().addAll(topBar, fields, buttonBox);

        StackPane backdrop = new StackPane(dialog);
        backdrop.setPadding(new Insets(60));
        backdrop.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(backdrop);
        scene.setFill(Color.TRANSPARENT);
        modal.setScene(scene);
        modal.showAndWait();
    }

    private VBox createPasswordField(String label, String placeholder) {
        VBox field = new VBox(6);
        Text lbl = new Text(label);
        lbl.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 13));
        lbl.setFill(Color.web(theme.muted()));
        PasswordField pf = new PasswordField();
        pf.setPromptText(placeholder);
        pf.setFont(Font.font("Inter", 14));
        pf.setStyle("-fx-background-color: " + (theme.isDark() ? "#0F172A" : "#F8FAFC") + "; " +
            "-fx-border-color: " + (theme.isDark() ? "#334155" : "#E2E8F0") + "; " +
            "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 12 16; " +
            "-fx-text-fill: " + theme.text() + ";");
        field.getChildren().addAll(lbl, pf);
        return field;
    }
 
    private VBox createInputField(String label, String value) {
        VBox field = new VBox(6);
        Text lbl = new Text(label);
        lbl.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 13));
        lbl.setFill(Color.web(theme.muted()));
        TextField tf = new TextField(value);
        tf.setFont(Font.font("Inter", 14));
        tf.setStyle("-fx-background-color: " + (theme.isDark() ? "#0F172A" : "#F8FAFC") + "; " +
            "-fx-border-color: " + (theme.isDark() ? "#334155" : "#E2E8F0") + "; " +
            "-fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 12 16; " +
            "-fx-text-fill: " + theme.text() + ";");
        field.getChildren().addAll(lbl, tf);
        return field;
    }

    private VBox createLogoutSection() {
        VBox section = new VBox();
        VBox.setMargin(section, new Insets(8, 0, 0, 0));

        logoutBtn = new Button("Cerrar Sesion");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        logoutBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_RED + "; -fx-text-fill: white; " +
            "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(220,38,38,0.3), 15, 0, 0, 8);");

        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(
            "-fx-background-color: " + ThemeManager.COLOR_RED_HOVER + "; -fx-text-fill: white; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
            "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(220,38,38,0.4), 20, 0, 0, 10);"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle(
            "-fx-background-color: " + ThemeManager.COLOR_RED + "; -fx-text-fill: white; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
            "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(220,38,38,0.3), 15, 0, 0, 8);"));

        section.getChildren().add(logoutBtn);

        theme.addListener(() -> {
            logoutBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_RED + "; -fx-text-fill: white; " +
                "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
                "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 0; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(220,38,38,0.3), 15, 0, 0, 8);");
        });

        return section;
    }
}
