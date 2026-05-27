package com.edugrade.cell;

import com.edugrade.model.Student;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GradeInputCell extends TableCell<Student, String> {

    private final TextField input = new TextField();
    private final Label totalLabel = new Label();
    private final HBox container = new HBox(8);
    private final DoubleProperty maxGrade = new SimpleDoubleProperty(100.0);

    public GradeInputCell() {
        super();
        container.setAlignment(Pos.CENTER_LEFT);

        input.getStyleClass().add("grade-input");
        input.setPromptText("0 – 100");
        input.setPrefWidth(80);

        totalLabel.getStyleClass().add("grade-total-label");
        totalLabel.setVisible(false);

        container.getChildren().addAll(input, totalLabel);

        input.setOnAction(e -> commitEdit(input.getText()));

        input.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (wasFocused && !isFocused) {
                commitEdit(input.getText());
            }
        });

        input.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                input.setText(oldVal);
                return;
            }
            if (!newVal.isEmpty()) {
                try {
                    double val = Double.parseDouble(newVal);
                    if (val > maxGrade.get()) {
                        input.setText(oldVal);
                    }
                } catch (NumberFormatException e) {
                    input.setText(oldVal);
                }
            }
            updateTotal();
        });
    }

    public double getMaxGrade() { return maxGrade.get(); }
    public void setMaxGrade(double max) {
        maxGrade.set(max);
        input.setPromptText("0 \u2013 " + (int) max);
    }
    public DoubleProperty maxGradeProperty() { return maxGrade; }

    private void updateTotal() {
        Student student = getTableRow() != null ? getTableRow().getItem() : null;
        if (student == null) return;

        String text = input.getText();
        if (text == null || text.isEmpty()) {
            totalLabel.setVisible(false);
            return;
        }
        try {
            double increment = Double.parseDouble(text);
            double total = student.getCurrentGrade() + increment;
            totalLabel.setText("\u2192 " + String.format("%.1f", total));
            totalLabel.setVisible(true);
        } catch (NumberFormatException e) {
            totalLabel.setVisible(false);
        }
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
            return;
        }

        input.setText(item == null ? "" : item);
        updateTotal();
        setGraphic(container);
        setPadding(new Insets(0, 16, 0, 16));
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        Student student = getTableView().getItems().get(getIndex());
        if (student != null) {
            student.newGradeProperty().set(newValue);
            if (newValue != null && !newValue.isBlank()) {
                try {
                    double increment = Double.parseDouble(newValue);
                    double finalGrade = student.getCurrentGrade() + increment;
                    student.currentGradeProperty().set(finalGrade);
                } catch (NumberFormatException ignored) {}
            }
        }
        input.clear();
        totalLabel.setVisible(false);
    }
}
