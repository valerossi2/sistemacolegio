package com.edugrade.cell;

import com.edugrade.model.Student;
import com.edugrade.model.Student.Gender;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;

public class StudentCell extends TableCell<Student, Student> {

    private static final String SVG_MALE =
        "M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12z" +
        "M12 14.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z";

    private static final String SVG_FEMALE =
        "M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12z" +
        "M12 14.4c-3.2 0-9.6 1.6-9.6 4.8v1.2c0 .7.5 1.2 1.2 1.2h16.8c.7 0 1.2-.5 " +
        "1.2-1.2v-1.2c0-3.2-6.4-4.8-9.6-4.8z";

    private final HBox      root       = new HBox(12);
    private final StackPane avatarPane = new StackPane();
    private final SVGPath   avatarIcon = new SVGPath();
    private final VBox      textBox    = new VBox(2);
    private final Label     nameLabel  = new Label();
    private final Label     emailLabel = new Label();

    public StudentCell() {
        super();
        setPadding(new Insets(0));
        setGraphic(null);

        avatarPane.setMinSize(40, 40);
        avatarPane.setPrefSize(40, 40);
        avatarPane.getChildren().add(avatarIcon);

        nameLabel.getStyleClass().add("student-name-label");
        emailLabel.getStyleClass().add("student-email-label");
        textBox.setAlignment(Pos.CENTER_LEFT);
        textBox.getChildren().addAll(nameLabel, emailLabel);

        root.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(avatarPane, textBox);
    }

    @Override
    protected void updateItem(Student student, boolean empty) {
        super.updateItem(student, empty);

        if (empty || student == null) {
            setGraphic(null);
            return;
        }

        nameLabel.setText(student.getName());
        emailLabel.setText(student.getEmail());

        boolean isMale = student.getGender() == Gender.MALE;

        avatarPane.getStyleClass().removeAll(
            "student-avatar-container",
            "student-avatar-male",
            "student-avatar-female"
        );
        avatarIcon.getStyleClass().removeAll(
            "student-avatar-icon-male",
            "student-avatar-icon-female"
        );

        avatarPane.getStyleClass().addAll(
            "student-avatar-container",
            isMale ? "student-avatar-male" : "student-avatar-female"
        );
        avatarIcon.getStyleClass().add(
            isMale ? "student-avatar-icon-male" : "student-avatar-icon-female"
        );

        avatarIcon.setContent(isMale ? SVG_MALE : SVG_FEMALE);

        double scale = 40.0 / 24.0;
        avatarIcon.setScaleX(scale * 0.75);
        avatarIcon.setScaleY(scale * 0.75);

        setGraphic(root);
    }
}
