package com.edugrade.cell;

import com.edugrade.model.Student;
import javafx.geometry.Insets;
import javafx.scene.control.*;

public class GradeInputCell extends TableCell<Student, String> {

    private final TextField input = new TextField();

    public GradeInputCell() {
        super();
        input.getStyleClass().add("grade-input");
        input.setPromptText("0.0 – 10.0");

        input.setOnAction(e -> commitEdit(input.getText()));

        input.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (wasFocused && !isFocused) {
                commitEdit(input.getText());
            }
        });

        input.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                input.setText(oldVal);
            }
        });
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

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        Student student = getTableView().getItems().get(getIndex());
        if (student != null) {
            student.newGradeProperty().set(newValue);
        }
    }
}
