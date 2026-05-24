package com.example.demo.controller;

import com.example.demo.theme.ThemeManager;
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

    private Runnable updateTheme;

    public Configuracion(ThemeManager theme) {
        this.theme = theme;
        root = new VBox();
        root.setPadding(new Insets(16, 24, 16, 24));
        root.setMaxWidth(900);

        Text title = new Text("Configuracion del Sistema");
        title.setFont(Font.font("Inter", FontWeight.BOLD, 22));

        Text subtitle = new Text("Bienvenido de nuevo. Aqui tienes un resumen del estado institucional hoy.");
        subtitle.setFont(Font.font("Inter", 12));

        VBox headerBox = new VBox(2);
        headerBox.getChildren().addAll(title, subtitle);
        VBox.setMargin(headerBox, new Insets(0, 0, 10, 0));

        VBox content = new VBox(10);
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
            "-fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.05), 20, 0, 0, 4);";
    }

    private HBox createProfileSection() {
        HBox section = new HBox();
        section.setPadding(new Insets(14, 20, 14, 20));
        section.setStyle(cardStyle());

        HBox leftBox = new HBox(12);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        StackPane avatarStack = new StackPane();
        Circle avatarBg = new Circle(32, Color.web(theme.isDark() ? "#475569" : "#dbe1ff"));
        SVGPath avatarIcon = new SVGPath();
        avatarIcon.setContent("M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z");
        avatarIcon.setScaleX(1.2);
        avatarIcon.setScaleY(1.2);
        avatarIcon.setFill(Color.web(theme.textSec()));
        avatarStack.getChildren().addAll(avatarBg, avatarIcon);

        Circle onlineDot = new Circle(5, Color.web(ThemeManager.COLOR_GREEN));
        onlineDot.setTranslateX(22);
        onlineDot.setTranslateY(22);
        onlineDot.setStrokeWidth(2);
        StackPane avatarWrapper = new StackPane(avatarStack, onlineDot);

        VBox nameBox = new VBox(0);
        Text userName = new Text("Admin User");
        userName.setFont(Font.font("Inter", FontWeight.BOLD, 16));
        userName.setFill(Color.web(theme.text()));
        Text userEmail = new Text("admin@lumina.edu");
        userEmail.setFont(Font.font("Inter", 12));
        userEmail.setFill(Color.web(theme.muted()));
        nameBox.getChildren().addAll(userName, userEmail);

        leftBox.getChildren().addAll(avatarWrapper, nameBox);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editBtn = new Button("Editar Perfil");
        editBtn.setFont(Font.font("Inter", FontWeight.BOLD, 12));
        editBtn.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 24;");

        editBtn.setOnMouseEntered(e -> editBtn.setStyle(
            "-fx-background-color: #1D4ED8; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 24;"));
        editBtn.setOnMouseExited(e -> editBtn.setStyle(
            "-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-text-fill: white; " +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 24;"));
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
                "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 24;");
        });

        return section;
    }

    private VBox createPreferencesSection() {
        VBox section = new VBox();
        section.setPadding(new Insets(14, 20, 14, 20));
        section.setStyle(cardStyle());

        Text sectionTitle = new Text("Preferencias de la Cuenta");
        sectionTitle.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        sectionTitle.setFill(Color.web(theme.text()));
        VBox.setMargin(sectionTitle, new Insets(0, 0, 8, 0));

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
        row.setPadding(new Insets(8, 0, 8, 0));

        Circle iconCircle = new Circle(16, Color.web(theme.isDark() ? "#1E3A5F" : "#dbe1ff"));
        SVGPath globeIcon = new SVGPath();
        globeIcon.setContent("M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9h18");
        globeIcon.setFill(Color.web(ThemeManager.COLOR_PRIMARY));
        globeIcon.setScaleX(0.7);
        globeIcon.setScaleY(0.7);
        StackPane iconStack = new StackPane(iconCircle, globeIcon);

        VBox textBox = new VBox(0);
        Text label = new Text("Idioma");
        label.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        label.setFill(Color.web(theme.text()));
        Text desc = new Text("Cambia la configuracion del idioma de la cuenta");
        desc.setFont(Font.font("Inter", 11));
        desc.setFill(Color.web(theme.muted()));
        textBox.getChildren().addAll(label, desc);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> langCombo = new ComboBox<>();
        langCombo.getItems().addAll("Español", "Ingles", "Frances");
        langCombo.setValue("Español");
        langCombo.setPrefWidth(160);

        row.getChildren().addAll(iconStack, textBox, spacer, langCombo);

        theme.addListener(() -> {
            row.setStyle("-fx-border-color: transparent transparent " + theme.divider() + " transparent;");
            iconCircle.setFill(Color.web(theme.isDark() ? "#1E3A5F" : "#dbe1ff"));
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
        row.setPadding(new Insets(12, 0, 0, 0));

        Circle iconCircle = new Circle(16);
        iconCircle.setFill(Color.web("#dbe1ff"));
        SVGPath sunIcon = new SVGPath();
        sunIcon.setContent("M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z");
        sunIcon.setScaleX(0.6);
        sunIcon.setScaleY(0.6);
        sunIcon.setFill(Color.web(theme.textSec()));
        StackPane iconStack = new StackPane(iconCircle, sunIcon);

        VBox textBox = new VBox(0);
        Text label = new Text("Tema");
        label.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        label.setFill(Color.web(theme.text()));
        Text desc = new Text("Personaliza la apariencia de tu interfaz");
        desc.setFont(Font.font("Inter", 11));
        desc.setFill(Color.web(theme.muted()));
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
        moonIcon.setFill(Color.web(theme.muted()));
        moonBtn.getChildren().add(moonIcon);

        toggleGroup.getChildren().addAll(sunBtn, moonBtn);

        sunBtn.setOnMouseClicked(e -> theme.setDark(false));
        moonBtn.setOnMouseClicked(e -> theme.setDark(true));

        row.getChildren().addAll(iconStack, textBox, spacer, toggleGroup);

        theme.addListener(() -> {
            row.setStyle("-fx-border-color: transparent transparent " + theme.divider() + " transparent;");
            iconCircle.setFill(Color.web(theme.isDark() ? "#475569" : "#dbe1ff"));
            sunIcon.setFill(Color.web(theme.textSec()));
            label.setFill(Color.web(theme.text()));
            desc.setFill(Color.web(theme.muted()));
            toggleGroup.setStyle("-fx-background-color: " + theme.toggleGroupBg() + "; -fx-background-radius: 32;");
            sunBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "transparent" : "white") + "; " +
                "-fx-background-radius: 32; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);");
            moonBtn.setStyle("-fx-background-color: " + (theme.isDark() ? "white" : "transparent") + "; " +
                "-fx-background-radius: 32; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 4, 0, 0, 2);");
            moonIcon.setFill(Color.web(theme.isDark() ? ThemeManager.COLOR_PRIMARY : theme.muted()));
            sunSmall.setFill(Color.web(theme.isDark() ? theme.muted() : ThemeManager.COLOR_PRIMARY));
        });

        return row;
    }

    private VBox createSecuritySection() {
        VBox section = new VBox();
        section.setPadding(new Insets(14, 20, 14, 20));
        section.setStyle(cardStyle());

        Text sectionTitle = new Text("Seguridad");
        sectionTitle.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        sectionTitle.setFill(Color.web(theme.text()));
        VBox.setMargin(sectionTitle, new Insets(0, 0, 8, 0));

        Button changePwdBtn = new Button("Cambiar Contrasena");
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

        section.getChildren().addAll(sectionTitle, changePwdBtn);

        theme.addListener(() -> {
            section.setStyle(cardStyle());
            sectionTitle.setFill(Color.web(theme.text()));
            changePwdBtn.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: " + ThemeManager.COLOR_PRIMARY + "; " +
                "-fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14; " +
                "-fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 0; " +
                "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-border-radius: 10; -fx-border-width: 1.5;");
        });

        return section;
    }

    private void showEditProfileModal() {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initStyle(StageStyle.UNDECORATED);
        modal.setWidth(460);
        modal.setHeight(560);

        VBox dialog = new VBox();
        dialog.setPadding(new Insets(28));
        dialog.setSpacing(20);
        dialog.setMaxWidth(460);
        String dialogBg = theme.isDark() ? "#1E293B" : "#FFFFFF";
        dialog.setStyle("-fx-background-color: " + dialogBg + "; -fx-background-radius: 20; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 30, 0, 0, 8);");

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        Text modalTitle = new Text("Editar Perfil");
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
        Circle avatarBig = new Circle(50, Color.web(theme.isDark() ? "#475569" : "#dbe1ff"));
        SVGPath avatarBigIcon = new SVGPath();
        avatarBigIcon.setContent("M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z");
        avatarBigIcon.setScaleX(2);
        avatarBigIcon.setScaleY(2);
        avatarBigIcon.setFill(Color.web(theme.textSec()));
        
        StackPane cameraOverlay = new StackPane();
        cameraOverlay.setPrefSize(30, 30);
        cameraOverlay.setStyle("-fx-background-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-background-radius: 15; " +
            "-fx-cursor: hand;");
        SVGPath cameraIcon = new SVGPath();
        cameraIcon.setContent("M12 15.2a3.2 3.2 0 1 0 0-6.4 3.2 3.2 0 0 0 0 6.4zM9 2L7.17 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2h-3.17L15 2H9zm3 15c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5z");
        cameraIcon.setScaleX(0.6);
        cameraIcon.setScaleY(0.6);
        cameraIcon.setFill(Color.WHITE);
        cameraOverlay.getChildren().add(cameraIcon);
        cameraOverlay.setTranslateX(35);
        cameraOverlay.setTranslateY(35);
        
        avatarOuter.getChildren().addAll(avatarBig, avatarBigIcon, cameraOverlay);
        Text changePhotoText = new Text("Cambiar Foto");
        changePhotoText.setFont(Font.font("Inter", FontWeight.MEDIUM, 12));
        changePhotoText.setFill(Color.web(theme.muted()));
        avatarSection.getChildren().addAll(avatarOuter, changePhotoText);
        VBox.setMargin(avatarSection, new Insets(0, 0, 4, 0));

        VBox fields = new VBox(12);
        VBox nameField = createInputField("Nombre Completo", "Admin User");
        TextField nameTf = (TextField) nameField.getChildren().get(1);
        nameTf.setStyle(nameTf.getStyle() + "-fx-border-color: " + ThemeManager.COLOR_PRIMARY + "; -fx-border-width: 2;");
        
        fields.getChildren().addAll(
            nameField,
            createInputField("Correo Electrónico", "admin@lumina.edu"),
            createInputField("Cargo/Rol", "Admin")
        );

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Button saveBtn = new Button("Guardar Cambios");
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

        Button cancelBtn = new Button("Cancelar");
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
        modal.setScene(scene);
        modal.showAndWait();
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

        Button logoutBtn = new Button("Cerrar Sesion");
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
