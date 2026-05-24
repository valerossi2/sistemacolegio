package ui;

import controller.MainController;
import theme.ThemeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import java.io.IOException;

public class LuminaAcademyFX extends Application {

    private ThemeManager theme = new ThemeManager();
    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_view.fxml"));
            Parent rootNode = loader.load();
            MainController controller = loader.getController();
            controller.setThemeManager(theme);
            controller.setStage(primaryStage);
            controller.setupEverything();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double width = Math.min(1050, screenBounds.getWidth() * 0.7);
            double height = Math.min(700, screenBounds.getHeight() * 0.7);
            Scene scene = new Scene(rootNode, width, height);

            primaryStage.setMinWidth(700);
            primaryStage.setMinHeight(450);

            theme.addListener(() -> {
                scene.getStylesheets().clear();
                scene.getStylesheets().add(theme.isDark()
                    ? getClass().getResource("/css/dark.css").toExternalForm()
                    : getClass().getResource("/css/light.css").toExternalForm());
            });

            scene.getStylesheets().add(theme.isDark()
                ? getClass().getResource("/css/dark.css").toExternalForm()
                : getClass().getResource("/css/light.css").toExternalForm());

            primaryStage.setTitle("Lumina Academy - Admin Portal");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
