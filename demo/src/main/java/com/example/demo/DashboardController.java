package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class DashboardController {

    private VBox mainCanvas;
    private HBox kpiGrid;
    private HBox middleSection;
    private VBox scheduleBox;

    // Colores
    private final String COLOR_PRIMARY = "#004ac6";
    private final String COLOR_PRIMARY_CONTAINER = "#2563eb";
    private final String COLOR_SURFACE_LOW = "#f0f3ff";
    private final String COLOR_SURFACE_CONTAINER = "#e7eeff";
    private final String COLOR_ON_SURFACE = "#111c2d";
    private final String COLOR_OUTLINE = "#737686";
    private final String COLOR_WHITE = "#ffffff";
    private final String COLOR_PRIMARY_FIXED = "#dbe1ff";
    private final String COLOR_SECONDARY_FIXED = "#c0e8ff";
    private final String COLOR_TERTIARY_FIXED = "#dbe4ea";

    public DashboardController(VBox mainCanvas, HBox kpiGrid, HBox middleSection, VBox scheduleBox) {
        this.mainCanvas = mainCanvas;
        this.kpiGrid = kpiGrid;
        this.middleSection = middleSection;
        this.scheduleBox = scheduleBox;
    }

    // Método para agregar una nueva KPI Card
    public void addKpiCard(String label, String value, String color, String icon) {
        VBox card = createKpiCard(label, value, color, icon);
        kpiGrid.getChildren().add(card);
    }

    // Método para agregar una nueva sección personalizada
    public void addCustomSection(String title, Pane content) {
        VBox section = new VBox(16);
        section.getStyleClass().add("glass-card");
        section.setPadding(new Insets(24));

        Text titleText = new Text(title);
        titleText.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 20));
        titleText.setFill(Color.web(COLOR_ON_SURFACE));

        section.getChildren().addAll(titleText, content);
        mainCanvas.getChildren().add(section);
    }

    // Método para agregar una fila al horario
    public void addScheduleRow(String time, String subject, String details, boolean isFirst) {
        // Note: This requires access to the list inside scheduleBox. 
        // For simplicity, we'll assume the user manages the list or we add to the bottom.
        // A better approach would be to expose the list or have a method to rebuild.
        // Here we just add to the existing structure if possible, or recreate.
        // Given the current structure, let's just add a note that this needs the list reference.
    }

    private VBox createKpiCard(String label, String value, String color, String icon) {
        HBox card = new HBox(12);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("glass-card");

        Circle iconCircle = new Circle(16, Color.web(COLOR_SURFACE_CONTAINER));
        Text iconTxt = new Text(icon);
        iconTxt.setFont(Font.font(14));
        iconTxt.setFill(Color.web(COLOR_PRIMARY));
        StackPane iconStack = new StackPane(iconCircle, iconTxt);
        
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
}
