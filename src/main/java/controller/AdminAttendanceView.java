package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import theme.ThemeManager;
import util.LanguageManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminAttendanceView {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String ICON_SAVE = "M17 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V7l-4-4zM12 19c-1.66 0-3-1.34-3-3s1.34-3 3-3 3 1.34 3 3-1.34 3-3 3zM6 8V5h9v3H6z";

    private final ThemeManager theme;
    private final LanguageManager lang = LanguageManager.getInstance();
    private final ObservableList<StudentAttendance> students = FXCollections.observableArrayList();
    private final List<Runnable> themeUpdaters = new ArrayList<>();
    private final List<Runnable> languageUpdaters = new ArrayList<>();
    private final VBox root = new VBox(24);
    private final GridPane contentGrid = new GridPane();
    private final VBox summaryCard = new VBox(24);
    private final VBox tableColumn = new VBox(18);
    private final VBox rowsBox = new VBox();
    private final Label breadcrumb = new Label();
    private final Label title = new Label();
    private final Label summaryTitle = new Label();
    private final Label dateText = new Label();
    private final Label presentCount = new Label();
    private final Label absentCount = new Label();
    private final Label excuseCount = new Label();
    private final Label tutorTag = new Label();
    private final Label tutorName = new Label();
    private final Label listTitle = new Label();
    private final Label listSubtitle = new Label();
    private final Button loadMoreButton = new Button();
    private final Button saveButton = new Button();
    private final Label gradeLabel = new Label("5to");
    private final Label sectionLabel = new Label("E");
    private final TextField searchField = new TextField();
    private final VBox titleBox = new VBox(8);
    private final HBox selectorBox = new HBox(8);
    private final HBox pageHeader = new HBox(24);
    private boolean compact;

    public AdminAttendanceView(ThemeManager theme) {
        this.theme = theme;
        System.out.println("[AdminAttendanceView] CREADO. isDark=" + theme.isDark() + " text()=" + text() + " textMuted()=" + textMuted());
        loadInitialData();
        buildView();
        wireEvents();
        updateLanguage();
        themeUpdaters.forEach(Runnable::run);
        applyTheme();
        theme.addListener(this::applyTheme);
        lang.addListener(this::updateLanguage);
        root.sceneProperty().addListener((obs, old, scene) -> {
            if (scene != null) {
                themeUpdaters.forEach(Runnable::run);
                applyTheme();
                Platform.runLater(() -> applyTheme());
            }
        });
    }

    public Node getView() {
        return root;
    }

    public void attachSearchField(TextField externalSearchField) {
        externalSearchField.clear();
        externalSearchField.setPromptText(lang.get("attendance.search"));
        externalSearchField.textProperty().addListener((obs, oldValue, newValue) -> {
            searchField.setText(newValue);
        });
    }

    private void buildView() {
        root.setPadding(new Insets(20, 32, 32, 32));
        root.setMaxWidth(1200);
        root.setFillWidth(true);

        pageHeader.setAlignment(Pos.BOTTOM_LEFT);
        pageHeader.setSpacing(16);
        titleBox.getChildren().setAll(breadcrumb, title);
        selectorBox.setSpacing(8);
        selectorBox.getChildren().setAll(createLabelSelector("attendance.grade", gradeLabel), createLabelSelector("attendance.section", sectionLabel));
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        pageHeader.getChildren().setAll(titleBox, headerSpacer, selectorBox);

        gradeLabel.setOnMouseClicked(e -> showSelectorMenu(e, List.of("5to", "4to", "3ro", "2do", "1ro"), gradeLabel));
        sectionLabel.setOnMouseClicked(e -> showSelectorMenu(e, List.of("A", "B", "C", "D", "E"), sectionLabel));

        buildSummaryCard();
        buildTableCard();

        contentGrid.setHgap(24);
        contentGrid.setVgap(24);
        contentGrid.add(summaryCard, 0, 0);
        contentGrid.add(tableColumn, 1, 0);
        GridPane.setHgrow(tableColumn, Priority.ALWAYS);

        root.getChildren().addAll(pageHeader, contentGrid);
        root.widthProperty().addListener((obs, oldValue, newValue) -> setCompact(newValue.doubleValue() < 820));
        setCompact(false);
    }

    private HBox createLabelSelector(String labelKey, Label valueLabel) {
        Label label = new Label();
        label.setFont(Font.font("Plus Jakarta Sans", 11));
        languageUpdaters.add(() -> label.setText(lang.get(labelKey)));

        valueLabel.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 13));
        valueLabel.setCursor(javafx.scene.Cursor.HAND);
        valueLabel.setPadding(new Insets(0, 2, 0, 0));

        HBox box = new HBox(4, label, valueLabel);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(2, 8, 2, 8));
        themeUpdaters.add(() -> {
            label.setTextFill(Color.web(textMuted()));
            valueLabel.setTextFill(Color.web(text()));
            box.setStyle(cardStyle(8, borderSoft()));
        });
        return box;
    }

    private void buildSummaryCard() {
        summaryCard.setPadding(new Insets(24));
        summaryCard.setMinWidth(260);
        summaryCard.setPrefWidth(300);
        summaryTitle.setFont(Font.font("Plus Jakarta Sans", FontWeight.SEMI_BOLD, 18));

        HBox dateBadge = new HBox(4);
        dateBadge.setPadding(new Insets(12, 14, 12, 14));
        Label dateLabel = new Label();
        dateLabel.setFont(Font.font("Plus Jakarta Sans", 13));
        dateText.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 13));
        dateLabel.setTextFill(Color.web(theme.isDark() ? "#fcd34d" : "#92400e"));
        dateText.setTextFill(Color.web(theme.isDark() ? "#fde68a" : "#92400e"));
        languageUpdaters.add(() -> dateLabel.setText(lang.get("attendance.currentDate")));
        themeUpdaters.add(() -> {
            dateBadge.setStyle("-fx-background-color: " + (theme.isDark() ? "#451a03" : "#fffbeb") + "; -fx-border-color: " + (theme.isDark() ? "#92400e" : "#fde68a") + "; -fx-border-radius: 12; -fx-background-radius: 12;");
            dateLabel.setTextFill(Color.web(theme.isDark() ? "#fcd34d" : "#92400e"));
            dateText.setTextFill(Color.web(theme.isDark() ? "#fde68a" : "#92400e"));
        });

        VBox stats = new VBox(16,
            createStatRow("attendance.present", presentCount),
            createStatRow("attendance.absent", absentCount),
            createStatRow("attendance.excuse", excuseCount)
        );

        HBox tutorCard = new HBox(14);
        tutorCard.setAlignment(Pos.CENTER_LEFT);
        tutorCard.setPadding(new Insets(14));
        StackPane avatar = new StackPane(new Label("LM"));
        avatar.setMinSize(48, 48);
        avatar.setMaxSize(48, 48);
        VBox tutorTexts = new VBox(2, tutorTag, tutorName);
        tutorCard.getChildren().addAll(avatar, tutorTexts);
        tutorTag.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        tutorName.setFont(Font.font("Plus Jakarta Sans", FontWeight.SEMI_BOLD, 13));
        themeUpdaters.add(() -> {
            summaryCard.setStyle(cardStyle(16, borderSoft()));
            summaryTitle.setTextFill(Color.web(text()));
            tutorCard.setStyle("-fx-background-color: " + surfaceLow() + "; -fx-border-color: " + borderSoft() + "; -fx-border-radius: 12; -fx-background-radius: 12;");
            avatar.setStyle("-fx-background-color: " + (theme.isDark() ? "#1e3a8a" : "#dbeafe") + "; -fx-background-radius: 100;");
            ((Label) avatar.getChildren().get(0)).setTextFill(Color.web(theme.isDark() ? "#bfdbfe" : "#1d4ed8"));
            tutorTag.setTextFill(Color.web(textMuted()));
            tutorName.setTextFill(Color.web(text()));
        });

        summaryCard.getChildren().addAll(summaryTitle, dateBadge, stats, tutorCard);
    }

    private HBox createStatRow(String labelKey, Label value) {
        Label label = new Label();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(label, spacer, value);
        row.setAlignment(Pos.CENTER_LEFT);
        label.setFont(Font.font("Plus Jakarta Sans", 14));
        value.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 14));
        languageUpdaters.add(() -> label.setText(lang.get(labelKey)));
        themeUpdaters.add(() -> {
            label.setTextFill(Color.web(textSecondary()));
            value.setTextFill(Color.web(text()));
        });
        return row;
    }

    private void buildTableCard() {
        VBox tableCard = new VBox();
        tableCard.setMinWidth(520);
        VBox tableHeader = new VBox(4, listTitle, listSubtitle);
        tableHeader.setPadding(new Insets(22, 24, 18, 24));
        listTitle.setFont(Font.font("Plus Jakarta Sans", FontWeight.SEMI_BOLD, 18));
        listSubtitle.setFont(Font.font("Plus Jakarta Sans", 13));

        HBox header = new HBox();
        header.setPadding(new Insets(14, 24, 14, 24));
        header.getChildren().addAll(
            headerCell("attendance.student", 290),
            headerCell("attendance.enrollment", 150),
            headerCell("attendance.date", 120),
            headerCell("attendance.attendance", 260)
        );

        rowsBox.setFillWidth(true);
        ScrollPane rowScroll = new ScrollPane(rowsBox);
        rowScroll.setFitToWidth(true);
        rowScroll.setMinHeight(220);
        rowScroll.setPrefHeight(390);
        rowScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rowScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        HBox tableFooter = new HBox(loadMoreButton);
        tableFooter.setAlignment(Pos.CENTER);
        tableFooter.setPadding(new Insets(14));

        tableCard.getChildren().addAll(tableHeader, header, rowScroll, tableFooter);

        HBox saveContent = new HBox(8, icon(ICON_SAVE, 17, "#ffffff"), new Label());
        saveContent.setAlignment(Pos.CENTER);
        saveButton.setGraphic(saveContent);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setMinHeight(56);
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        tableColumn.getChildren().addAll(tableCard, saveButton);

        languageUpdaters.add(() -> ((Label) saveContent.getChildren().get(1)).setText(lang.get("attendance.save")));
        themeUpdaters.add(() -> {
            tableCard.setStyle(cardStyle(16, borderSoft()));
            tableHeader.setStyle("-fx-border-color: transparent transparent " + borderSoft() + " transparent;");
            header.setStyle("-fx-background-color: " + surfaceLow() + "; -fx-border-color: transparent transparent " + borderSoft() + " transparent;");
            rowScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
            tableFooter.setStyle("-fx-background-color: " + card() + "; -fx-border-color: " + borderSoft() + " transparent transparent transparent;");
            listTitle.setTextFill(Color.web(text()));
            listSubtitle.setTextFill(Color.web(textMuted()));
            loadMoreButton.setStyle("-fx-background-color: transparent; -fx-text-fill: " + textSecondary() + "; -fx-font-weight: 600; -fx-cursor: hand;");
            saveButton.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 16; -fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: 700; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(37,99,235,0.25), 8, 0.2, 0, 2);");
            ((Label) saveContent.getChildren().get(1)).setTextFill(Color.WHITE);
        });
        refreshRows();
    }

    private HBox headerCell(String key, double width) {
        Label label = new Label();
        label.setMinWidth(width);
        label.setPrefWidth(width);
        label.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 11));
        languageUpdaters.add(() -> label.setText(lang.get(key).toUpperCase(Locale.ROOT)));
        themeUpdaters.add(() -> label.setTextFill(Color.web(textMuted())));
        return new HBox(label);
    }

    private void refreshRows() {
        rowsBox.getChildren().clear();
        String q = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase(Locale.ROOT);
        for (StudentAttendance student : students) {
            if (!q.isEmpty() && !student.name().toLowerCase(Locale.ROOT).contains(q) && !student.email().toLowerCase(Locale.ROOT).contains(q)) {
                continue;
            }
            rowsBox.getChildren().add(createStudentRow(student));
        }
        updateSummary();
        applyTheme();
    }

    private HBox createStudentRow(StudentAttendance student) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 24, 12, 24));
        row.setMinHeight(68);

        StackPane initials = new StackPane(new Label(student.initials()));
        initials.setMinSize(36, 36);
        initials.setMaxSize(36, 36);
        VBox studentTexts = new VBox(2, new Label(student.name()), new Label(student.email()));
        HBox studentCell = new HBox(12, initials, studentTexts);
        studentCell.setAlignment(Pos.CENTER_LEFT);
        studentCell.setMinWidth(290);
        studentCell.setPrefWidth(290);

        Label enrollment = new Label(student.enrollment());
        enrollment.setMinWidth(150);
        enrollment.setPrefWidth(150);
        Label date = new Label(LocalDate.now().format(DATE_FORMAT));
        date.setMinWidth(120);
        date.setPrefWidth(120);

        HBox actions = new HBox(8,
            statusButton(student, AttendanceStatus.PRESENT, "attendance.presentButton"),
            statusButton(student, AttendanceStatus.ABSENT, "attendance.absentButton"),
            statusButton(student, AttendanceStatus.EXCUSE, "attendance.excuseButton")
        );
        actions.setMinWidth(260);
        row.getChildren().addAll(studentCell, enrollment, date, actions);

        themeUpdaters.add(() -> {
            row.setStyle("-fx-background-color: " + card() + "; -fx-border-color: transparent transparent " + borderSoft() + " transparent;");
            initials.setStyle("-fx-background-color: " + student.avatarBg(theme.isDark()) + "; -fx-background-radius: 100;");
            ((Label) initials.getChildren().get(0)).setTextFill(Color.web(student.avatarText(theme.isDark())));
            ((Label) studentTexts.getChildren().get(0)).setTextFill(Color.web(text()));
            ((Label) studentTexts.getChildren().get(1)).setTextFill(Color.web(textMuted()));
            enrollment.setTextFill(Color.web(textSecondary()));
            date.setTextFill(Color.web(textSecondary()));
        });
        return row;
    }

    private Button statusButton(StudentAttendance student, AttendanceStatus status, String key) {
        Button button = new Button();
        button.setFont(Font.font("Plus Jakarta Sans", FontWeight.SEMI_BOLD, 12));
        button.setPadding(new Insets(6, 14, 6, 14));
        button.setOnAction(e -> {
            student.status = status;
            refreshRows();
        });
        languageUpdaters.add(() -> button.setText(lang.get(key)));
        themeUpdaters.add(() -> button.setStyle(statusButtonStyle(student.status == status, status)));
        return button;
    }

    private String statusButtonStyle(boolean active, AttendanceStatus status) {
        if (status == AttendanceStatus.PRESENT) {
            return active ? buttonStyle("#16a34a", "#ffffff", "#15803d") : buttonStyle("#16a34a", "#ffffff", "transparent");
        }
        if (status == AttendanceStatus.ABSENT) {
            return active ? buttonStyle("#ef4444", "#ffffff", "#dc2626") : buttonStyle(theme.isDark() ? "#451a1a" : "#fee2e2", theme.isDark() ? "#fecaca" : "#b91c1c", "transparent");
        }
        return active ? buttonStyle("#f59e0b", "#ffffff", "#d97706") : buttonStyle(theme.isDark() ? "#422006" : "#fef9c3", theme.isDark() ? "#fde68a" : "#92400e", "transparent");
    }

    private String buttonStyle(String bg, String text, String border) {
        return "-fx-background-color: " + bg + "; -fx-text-fill: " + text + "; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: " + border + "; -fx-border-width: " + ("transparent".equals(border) ? "0" : "2") + "; -fx-cursor: hand;";
    }

    private void showSelectorMenu(MouseEvent event, List<String> options, Label target) {
        ContextMenu menu = new ContextMenu();
        for (String opt : options) {
            MenuItem item = new MenuItem(opt);
            if (opt.equals(target.getText())) item.setDisable(true);
            item.setOnAction(e -> {
                target.setText(opt);
                updateLanguage();
            });
            menu.getItems().add(item);
        }
        menu.show((Node) event.getSource(), Side.BOTTOM, 0, 0);
    }

    private void wireEvents() {
        searchField.textProperty().addListener((obs, oldValue, newValue) -> refreshRows());
        loadMoreButton.setOnAction(e -> {
            int start = students.size() + 1;
            students.addAll(
                new StudentAttendance("Herrera, Sebastian " + start, "sebastian.h@student.edu", "STU-2023-20" + start, "SH", 0),
                new StudentAttendance("Morales, Isabella " + (start + 1), "isabella.m@student.edu", "STU-2023-22" + start, "IM", 1)
            );
            refreshRows();
        });
        saveButton.setOnAction(e -> {
            long pending = students.stream().filter(s -> s.status == AttendanceStatus.UNMARKED).count();
            if (pending > 0) {
                new Alert(Alert.AlertType.WARNING, lang.get("attendance.pendingMessage").replace("{0}", String.valueOf(pending)), ButtonType.OK).showAndWait();
                return;
            }
            new Alert(Alert.AlertType.INFORMATION, lang.get("attendance.savedMessage").replace("{0}", String.valueOf(students.size())), ButtonType.OK).showAndWait();
        });
    }

    private void setCompact(boolean compact) {
        if (this.compact == compact) return;
        this.compact = compact;
        pageHeader.getChildren().clear();
        if (compact) {
            pageHeader.setAlignment(Pos.TOP_LEFT);
            VBox headerGroup = new VBox(12, titleBox, selectorBox);
            pageHeader.getChildren().add(headerGroup);
        } else {
            pageHeader.setAlignment(Pos.BOTTOM_LEFT);
            Region headerSpacer = new Region();
            HBox.setHgrow(headerSpacer, Priority.ALWAYS);
            pageHeader.getChildren().addAll(titleBox, headerSpacer, selectorBox);
        }
        contentGrid.getChildren().clear();
        if (compact) {
            contentGrid.add(summaryCard, 0, 0);
            contentGrid.add(tableColumn, 0, 1);
            summaryCard.setPrefWidth(Double.MAX_VALUE);
            tableColumn.setMinWidth(0);
        } else {
            contentGrid.add(summaryCard, 0, 0);
            contentGrid.add(tableColumn, 1, 0);
            summaryCard.setPrefWidth(300);
            tableColumn.setMinWidth(520);
        }
    }

    private void updateLanguage() {
        String grade = gradeLabel.getText();
        String section = sectionLabel.getText();
        breadcrumb.setText(lang.get("attendance.breadcrumb").replace("{0}", grade).replace("{1}", section));
        title.setText(lang.get("attendance.title"));
        summaryTitle.setText(lang.get("attendance.summary"));
        dateText.setText(LocalDate.now().format(DATE_FORMAT));
        tutorTag.setText(lang.get("attendance.tutorTag").toUpperCase(Locale.ROOT));
        tutorName.setText(lang.get("attendance.tutorName"));
        listTitle.setText(lang.get("attendance.studentList"));
        listSubtitle.setText(lang.get("attendance.enrolledCount").replace("{0}", String.valueOf(students.size())));
        loadMoreButton.setText(lang.get("attendance.loadMore"));
        searchField.setPromptText(lang.get("attendance.search"));
        languageUpdaters.forEach(Runnable::run);
    }

    private void updateSummary() {
        presentCount.setText(String.valueOf(students.stream().filter(s -> s.status == AttendanceStatus.PRESENT).count()));
        absentCount.setText(String.valueOf(students.stream().filter(s -> s.status == AttendanceStatus.ABSENT).count()));
        excuseCount.setText(String.valueOf(students.stream().filter(s -> s.status == AttendanceStatus.EXCUSE).count()));
        listSubtitle.setText(lang.get("attendance.enrolledCount").replace("{0}", String.valueOf(students.size())));
    }

    private void applyTheme() {
        root.setStyle("-fx-background-color: " + bg() + ";");
        breadcrumb.setTextFill(Color.web(textMuted()));
        title.setTextFill(Color.web(text()));
        breadcrumb.setFont(Font.font("Plus Jakarta Sans", 14));
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 30));
        themeUpdaters.forEach(Runnable::run);
    }

    private SVGPath icon(String path, double size, String fill) {
        SVGPath icon = new SVGPath();
        icon.setContent(path);
        icon.setFill(Color.web(fill));
        double scale = size / 24.0;
        icon.setScaleX(scale);
        icon.setScaleY(scale);
        return icon;
    }

    private String cardStyle(int radius, String border) {
        return "-fx-background-color: " + card() + "; -fx-border-color: " + border + "; -fx-border-width: 1; -fx-border-radius: " + radius + "; -fx-background-radius: " + radius + "; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0.12, 0, 2);";
    }

    private String bg() { return theme.isDark() ? "#0F172A" : "#f1f5f9"; }
    private String card() { return theme.isDark() ? "#1E293B" : "#ffffff"; }
    private String surfaceLow() { return theme.isDark() ? "#334155" : "#e2e8f0"; }
    private String borderSoft() { return theme.isDark() ? "#475569" : "#94a3b8"; }
    private String text() { return theme.isDark() ? "#F8FAFC" : "#0f172a"; }
    private String textSecondary() { return theme.isDark() ? "#CBD5E1" : "#1e293b"; }
    private String textMuted() { return theme.isDark() ? "#94A3B8" : "#334155"; }

    private void loadInitialData() {
        students.addAll(
            new StudentAttendance("Garcia, Alejandro", "alejandro.g@student.edu", "STU-2023-045", "AG", 0),
            new StudentAttendance("Martinez, Lucia", "lucia.m@student.edu", "STU-2023-089", "LM", 1),
            new StudentAttendance("Perez, Carlos", "carlos.p@student.edu", "STU-2023-112", "PC", 2),
            new StudentAttendance("Lopez, Sofia", "sofia.l@student.edu", "STU-2023-134", "LS", 3),
            new StudentAttendance("Ramirez, Diego", "diego.r@student.edu", "STU-2023-158", "RD", 0),
            new StudentAttendance("Torres, Valentina", "valentina.t@student.edu", "STU-2023-177", "VT", 1),
            new StudentAttendance("Nunez, Camila", "camila.n@student.edu", "STU-2023-188", "CN", 2),
            new StudentAttendance("Santos, Daniel", "daniel.s@student.edu", "STU-2023-196", "DS", 3)
        );
    }

    private enum AttendanceStatus { UNMARKED, PRESENT, ABSENT, EXCUSE }

    private static class StudentAttendance {
        private final String name;
        private final String email;
        private final String enrollment;
        private final String initials;
        private final int colorIndex;
        private AttendanceStatus status = AttendanceStatus.PRESENT;

        StudentAttendance(String name, String email, String enrollment, String initials, int colorIndex) {
            this.name = name;
            this.email = email;
            this.enrollment = enrollment;
            this.initials = initials;
            this.colorIndex = colorIndex;
        }

        String name() { return name; }
        String email() { return email; }
        String enrollment() { return enrollment; }
        String initials() { return initials; }

        String avatarBg(boolean dark) {
            String[] light = {"#e0e7ff", "#dbeafe", "#fce7f3", "#dcfce7"};
            String[] darkColors = {"#312e81", "#1e3a8a", "#831843", "#14532d"};
            return dark ? darkColors[colorIndex % darkColors.length] : light[colorIndex % light.length];
        }

        String avatarText(boolean dark) {
            String[] light = {"#4338ca", "#1d4ed8", "#be185d", "#15803d"};
            String[] darkColors = {"#c7d2fe", "#bfdbfe", "#f9a8d4", "#bbf7d0"};
            return dark ? darkColors[colorIndex % darkColors.length] : light[colorIndex % light.length];
        }
    }
}
