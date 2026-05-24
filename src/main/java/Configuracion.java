package com.example.demo;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public class Configuracion {

    private ThemeManager theme;
    private VBox root;

    private Runnable updateTheme;

    public Configuracion(ThemeManager theme) {
        this.theme = theme;
        root = new VBox();
        root.setPadding(new Insets(32));
        root.setMaxWidth(900);

        Text title = new Text("Configuración del Sistema");
        title.setFont(Font.font("Inter", FontWeight.BOLD, 28));

        Text subtitle = new Text("Bienvenido de nuevo. Aquí tienes un resumen del estado institucional hoy.");
        subtitle.setFont(Font.font("Inter", 14));

        VBox headerBox = new VBox(4);
        headerBox.getChildren().addAll(title, subtitle);
        VBox.setMargin(headerBox, new Insets(0, 0, 24, 0));

        VBox content = new VBox(24);
        content.setMaxWidth(820);

        HBox profileSection = createProfileSection();
        VBox preferencesSection = createPreferencesSection();
        VBox securitySection = createSecuritySection();
        VBox logoutSection = createLogoutSection();

        content.getChildren().addAll(profileSection, preferencesSection, securitySection, logoutSection);

        updateTheme = () -> {
            root.setStyle("-fx-background-color: " + theme.bg() + ";");
            title.setFill(Color.web(theme.text()));
            subtitle.setFill(Color.web(theme.muted()));
        };

        root.getChildren().addAll(headerBox, content);
        theme.addListener(updateTheme);
        updateTheme.run();
    }

    public VBox getView() {
        return root;
    }

    private String cardStyle() {
        return "-fx-background-color: " + theme.card() + "; -fx-background-radius: 16; " +
            "-fx-border-color: " + theme.border() + "; -fx-border-radius: 16; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 20, 0, 0, 4);";
    }

    // ──────────────────────────────────────────────────────────────
    //  PROFILE SECTION
    // ──────────────────────────────────────────────────────────────
    private HBox createProfileSection() {
        HBox section = new HBox();
        section.setPadding(new Insets(24));

        HBox leftBox = new HBox(16);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        StackPane avatarStack = new StackPane();
        Circle avatarBg = new Circle(40);
        SVGPath avatarIcon = new SVGPath();
        avatarIcon.setContent("M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z");
        avatarIcon.setScaleX(1.5);
        avatarIcon.setScaleY(1.5);
        avatarStack.getChildren().addAll(avatarBg, avatarIcon);

        Circle onlineDot = new Circle(6, Color.web(ThemeManager.COLOR_GREEN));
        onlineDot.setTranslateX(28);
        onlineDot.setTranslateY(28);
        onlineDot.setStrokeWidth(3);
        StackPane avatarWrapper = new StackPane(avatarStack, onlineDot);

        VBox nameBox = new VBox(2);
        Text userName = new Text("Admin User");
        userName.setFont(Font.font("Inter", FontWeight.BOLD, 18));
        Text userEmail = new Text("admin@lumina.edu");
        userEmail.setFont(Font.font("Inter", 14));
        nameBox.getChildren().addAll(userName, userEmail);

        leftBox.getChildren().addAll(avatarWrapper, nameBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("Editar Perfil");
        editBtn.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        editBtn.setStyle("-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 28;");

        editBtn.setOnMouseEntered(e -> editBtn.setStyle(
            "-fx-background-color: #1D4ED8; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 28;"));
        editBtn.setOnMouseExited(e -> editBtn.setStyle(
            "-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 28;"));

        section.getChildren().addAll(leftBox, spacer, editBtn);

        theme.addListener(() -> {
            section.setStyle(cardStyle());
            avatarBg.setFill(Color.web(theme.isDark() ? "#475569" : "#E2E8F0"));
            avatarIcon.setFill(Color.web(theme.textSec()));
            onlineDot.setStroke(Color.WHITE);
            userName.setFill(Color.web(theme.text()));
            userEmail.setFill(Color.web(theme.muted()));
            editBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 28;");
        });

        return section;
    }

    // ──────────────────────────────────────────────────────────────
    //  PREFERENCES SECTION
    // ──────────────────────────────────────────────────────────────
    private VBox createPreferencesSection() {
        VBox section = new VBox();
        section.setPadding(new Insets(24));

        Text sectionTitle = new Text("Preferencias de la Cuenta");
        sectionTitle.setFont(Font.font("Inter", FontWeight.BOLD, 16));
        VBox.setMargin(sectionTitle, new Insets(0, 0, 16, 0));

        HBox idiomaRow = createIdiomaRow();
        HBox temaRow = createTemaRow();

        section.getChildren().addAll(sectionTitle, idiomaRow, temaRow);

        theme.addListener(() -> {
            section.setStyle(cardStyle());
            sectionTitle.setFill(Color.web(theme.text()));
        });

        return section;
    }

    private HBox createIdiomaRow() {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16, 0, 16, 0));

        Circle iconCircle = new Circle(20);
        SVGPath globeIcon = new SVGPath();
        globeIcon.setContent("M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9h18");
        globeIcon.setFill(Color.web(ThemeManager.COLOR_PRIMARY));
        globeIcon.setScaleX(0.8);
        globeIcon.setScaleY(0.8);
        StackPane iconStack = new StackPane(iconCircle, globeIcon);

        VBox textBox = new VBox(2);
        Text label = new Text("Idioma");
        label.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        Text desc = new Text("Cambia la configuración del idioma de la cuenta");
        desc.setFont(Font.font("Inter", 12));
        textBox.getChildren().addAll(label, desc);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> langCombo = new ComboBox<>();
        langCombo.getItems().addAll("Español", "English", "Fran\u00e7ais");
        langCombo.setValue("Español");
        langCombo.setPrefWidth(160);

        row.getChildren().addAll(iconStack, textBox, spacer, langCombo);

        theme.addListener(() -> {
            row.setStyle("-fx-border-color: transparent transparent " + theme.divider() + " transparent;");
            iconCircle.setFill(Color.web(theme.isDark() ? "#1E3A5F" : "#E8F0FE"));
            label.setFill(Color.web(theme.text()));
            desc.setFill(Color.web(theme.muted()));
            langCombo.setStyle("-fx-background-color: " + theme.inputBg() + "; " +
                "-fx-border-color: " + theme.inputBorder() + "; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-font-family: 'Inter'; -fx-font-size: 13; " +
                "-fx-padding: 4 8;");
        });

        return row;
    }

    private HBox createTemaRow() {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(16, 0, 0, 0));

        Circle iconCircle = new Circle(20);
        SVGPath sunIcon = new SVGPath();
        sunIcon.setContent("M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z");
        sunIcon.setScaleX(0.8);
        sunIcon.setScaleY(0.8);
        StackPane iconStack = new StackPane(iconCircle, sunIcon);

        VBox textBox = new VBox(2);
        Text label = new Text("Tema");
        label.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        Text desc = new Text("Personaliza la apariencia de tu interfaz");
        desc.setFont(Font.font("Inter", 12));
        textBox.getChildren().addAll(label, desc);

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
        sunSmall.setContent("M12 18a6 6 0 1 1 0-12 6 6 0 0 1 0 12z");
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
        moonBtn.getChildren().add(moonIcon);

        toggleGroup.getChildren().addAll(sunBtn, moonBtn);

        sunBtn.setOnMouseClicked(e -> theme.setDark(false));
        moonBtn.setOnMouseClicked(e -> theme.setDark(true));

        row.getChildren().addAll(iconStack, textBox, spacer, toggleGroup);

        theme.addListener(() -> {
            row.setStyle("-fx-border-color: transparent transparent " + theme.divider() + " transparent;");
            iconCircle.setFill(Color.web(theme.isDark() ? "#475569" : "#F4F4F5"));
            sunIcon.setFill(Color.web(theme.textSec()));
            label.setFill(Color.web(theme.text()));
            desc.setFill(Color.web(theme.muted()));
            toggleGroup.setStyle("-fx-background-color: " + theme.toggleGroupBg() + "; -fx-background-radius: 32;");
            sunBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "transparent" : "white") + "; " +
                "-fx-background-radius: 32; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);");
            moonBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "white" : "transparent") + "; " +
                "-fx-background-radius: 32; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);");
            moonIcon.setFill(Color.web(theme.isDark() ? theme.text() : theme.muted()));
            sunSmall.setFill(Color.web(theme.isDark() ? theme.muted() : ThemeManager.COLOR_PRIMARY));
        });

        return row;
    }

    // ──────────────────────────────────────────────────────────────
    //  SECURITY SECTION
    // ──────────────────────────────────────────────────────────────
    private VBox createSecuritySection() {
        VBox section = new VBox();
        section.setPadding(new Insets(24));

        Text sectionTitle = new Text("Seguridad");
        sectionTitle.setFont(Font.font("Inter", FontWeight.BOLD, 16));
        VBox.setMargin(sectionTitle, new Insets(0, 0, 16, 0));

        Button changePwdBtn = new Button("Cambiar Contraseña");
        changePwdBtn.setMaxWidth(Double.MAX_VALUE);
        changePwdBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        changePwdBtn.setStyle("-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-border-radius: 12; -fx-border-width: 1.5; " +
            "-fx-background-color: transparent; -fx-text-fill: " + ThemeManager.COLOR_PRIMARY + "; " +
            "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + ";");

        changePwdBtn.setOnMouseEntered(e -> changePwdBtn.setStyle(
            "-fx-background-color: " + (theme.isDark() ? "#1E3A5F" : "#EFF6FF") + "; " +
            "-fx-text-fill: " + ThemeManager.COLOR_PRIMARY + "; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
            "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-border-radius: 12; -fx-border-width: 1.5;"));
        changePwdBtn.setOnMouseExited(e -> changePwdBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: " + ThemeManager.COLOR_PRIMARY + "; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
            "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 12 0; " +
            "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-border-radius: 12; -fx-border-width: 1.5;"));

        section.getChildren().addAll(sectionTitle, changePwdBtn);

        theme.addListener(() -> {
            section.setStyle(cardStyle());
            sectionTitle.setFill(Color.web(theme.text()));
            changePwdBtn.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + ThemeManager.COLOR_PRIMARY + "; " +
                "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
                "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 12 0; " +
                "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-border-radius: 12; -fx-border-width: 1.5;");
        });

        return section;
    }

    // ──────────────────────────────────────────────────────────────
    //  LOGOUT SECTION
    // ──────────────────────────────────────────────────────────────
    private VBox createLogoutSection() {
        VBox section = new VBox();
        VBox.setMargin(section, new Insets(8, 0, 0, 0));

        Button logoutBtn = new Button("Cerrar Sesión");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setFont(Font.font("Inter", FontWeight.BOLD, 16));
        logoutBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_RED + "; -fx-text-fill: white; " +
            "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 14 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(220,38,38,0.3), 15, 0, 0, 8);");

        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(
            "-fx-background-color: " + ThemeManager.COLOR_RED_HOVER + "; -fx-text-fill: white; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 16; " +
            "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 14 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(220,38,38,0.4), 20, 0, 0, 10);"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle(
            "-fx-background-color: " + ThemeManager.COLOR_RED + "; -fx-text-fill: white; " +
            "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 16; " +
            "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 14 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(220,38,38,0.3), 15, 0, 0, 8);"));

        section.getChildren().add(logoutBtn);

        theme.addListener(() -> {
            logoutBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_RED + "; -fx-text-fill: white; " +
                "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 16; " +
                "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 14 0; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(220,38,38,0.3), 15, 0, 0, 8);");
        });

        return section;
    }
}
