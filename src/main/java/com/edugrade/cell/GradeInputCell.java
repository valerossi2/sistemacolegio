package com.edugrade.cell;

import com.edugrade.model.Student;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

public class GradeInputCell extends TableCell<Student, String> {

    private final TextField input = new TextField();

    public GradeInputCell() {
        super();
        input.getStyleClass().add("grade-input");
        input.setPromptText("0 – 100");

        input.setOnAction(e -> submitGrade());

        input.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (wasFocused && !isFocused) {
                submitGrade();
            }
        });

        input.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                input.setText(oldVal);
            }
        });
    }

    private void submitGrade() {
        String raw = input.getText();
        if (raw == null || raw.isBlank()) return;
        Student student = getTableView().getItems().get(getIndex());
        if (student != null) {
            student.newGradeProperty().set(raw);
        }
        input.clear();
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            return;
        }
        input.setText(item == null ? "" : item);
        setGraphic(input);
        setPadding(new Insets(0, 16, 0, 16));
    }
}
