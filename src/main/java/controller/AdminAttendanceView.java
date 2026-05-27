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
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import theme.ThemeManager;
import util.LanguageManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AdminAttendanceView {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String ICON_SAVE = "M17 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V7l-4-4zM12 19c-1.66 0-3-1.34-3-3s1.34-3 3-3 3 1.34 3 3-1.34 3-3 3zM6 8V5h9v3H6z";
    private static final String[] AVATAR_COLORS = {
            "#3B82F6", "#8B5CF6", "#EC4899", "#F59E0B",
            "#10B981", "#EF4444", "#6366F1", "#14B8A6"
    };

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
        label.setFont(Font.font("Plus Jakarta Sans", 10));
        languageUpdaters.add(() -> label.setText(lang.get(labelKey)));

        valueLabel.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 11));
        valueLabel.setPadding(new Insets(0, 2, 0, 0));

        HBox box = new HBox(3, label, valueLabel);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(1, 6, 1, 6));
        box.setCursor(javafx.scene.Cursor.HAND);
        box.setOnMouseClicked(e -> {
            if (valueLabel == gradeLabel)
                showSelectorMenu(e, List.of("5to", "4to", "3ro", "2do", "1ro"), gradeLabel);
            else
                showSelectorMenu(e, List.of("A", "B", "C", "D", "E"), sectionLabel);
        });
        themeUpdaters.add(() -> {
            label.setTextFill(Color.web(textMuted()));
            valueLabel.setTextFill(Color.web(text()));
            box.setStyle(cardStyle(6, borderSoft()));
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

    private final Label tableDateLabel = new Label();
    private void buildTableCard() {
        VBox tableCard = new VBox();
        tableCard.setMinWidth(520);
        tableDateLabel.setFont(Font.font("Plus Jakarta Sans", 12));
        tableDateLabel.setText(LocalDate.now().format(DATE_FORMAT));
        VBox tableHeader = new VBox(4, listTitle, listSubtitle, tableDateLabel);
        tableHeader.setPadding(new Insets(22, 24, 14, 24));
        listTitle.setFont(Font.font("Plus Jakarta Sans", FontWeight.SEMI_BOLD, 18));
        listSubtitle.setFont(Font.font("Plus Jakarta Sans", 13));

        HBox header = new HBox();
        header.setPadding(new Insets(14, 24, 14, 24));
        HBox studentHeader = headerCell("attendance.student", 200);
        studentHeader.setAlignment(Pos.CENTER_LEFT);
        studentHeader.setPadding(new Insets(0, 0, 0, 40));
        HBox.setHgrow(studentHeader, Priority.ALWAYS);
        Label attendanceLabel = new Label();
        attendanceLabel.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 11));
        languageUpdaters.add(() -> attendanceLabel.setText(lang.get("attendance.attendance").toUpperCase(Locale.ROOT)));
        themeUpdaters.add(() -> attendanceLabel.setTextFill(Color.web(textMuted())));
        HBox attendanceHeader = new HBox(attendanceLabel);
        attendanceHeader.setAlignment(Pos.CENTER);
        attendanceHeader.setMinWidth(260);
        attendanceHeader.setPrefWidth(260);
        header.getChildren().addAll(
            studentHeader,
            attendanceHeader
        );

        rowsBox.setFillWidth(true);
        ScrollPane rowScroll = new ScrollPane(rowsBox);
        rowScroll.setFitToWidth(true);
        rowScroll.setMinHeight(220);
        rowScroll.setPrefHeight(390);
        rowScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rowScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        tableCard.getChildren().addAll(tableHeader, header, rowScroll);

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
            listTitle.setTextFill(Color.web(text()));
            listSubtitle.setTextFill(Color.web(textMuted()));
            tableDateLabel.setTextFill(Color.web(textMuted()));
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
        int idx = 1;
        for (StudentAttendance student : students) {
            if (!q.isEmpty() && !student.name().toLowerCase(Locale.ROOT).contains(q)) {
                continue;
            }
            rowsBox.getChildren().add(createStudentRow(student, idx++));
        }
        updateSummary();
        applyTheme();
    }

    private HBox createStudentRow(StudentAttendance student, int number) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 16, 8, 16));
        row.setMinHeight(48);

        StackPane avatarContainer = new StackPane();
        avatarContainer.setMinSize(28, 28);
        avatarContainer.setPrefSize(28, 28);
        Circle avatarCircle = new Circle(14);
        Text numberText = new Text(String.valueOf(number));
        numberText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        numberText.setFill(Color.WHITE);
        avatarContainer.getChildren().addAll(avatarCircle, numberText);

        Label nameLabel = new Label(student.name());
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 14));
        nameLabel.setTextFill(Color.web(text()));
        HBox studentCell = new HBox(12, avatarContainer, nameLabel);
        studentCell.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(studentCell, Priority.ALWAYS);

        HBox actions = new HBox(8,
            statusButton(student, AttendanceStatus.PRESENT, "attendance.presentButton"),
            statusButton(student, AttendanceStatus.ABSENT, "attendance.absentButton"),
            statusButton(student, AttendanceStatus.EXCUSE, "attendance.excuseButton")
        );
        actions.setMinWidth(210);
        row.getChildren().addAll(studentCell, actions);

        themeUpdaters.add(() -> {
            row.setStyle("-fx-background-color: " + card() + "; -fx-border-color: transparent transparent " + borderSoft() + " transparent;");
            avatarCircle.setFill(Color.web(AVATAR_COLORS[student.colorIndex % AVATAR_COLORS.length]));
            nameLabel.setTextFill(Color.web(text()));
        });
        return row;
    }

    private Button statusButton(StudentAttendance student, AttendanceStatus status, String key) {
        Button button = new Button();
        button.setFont(Font.font("Plus Jakarta Sans", FontWeight.SEMI_BOLD, 12));
        button.setPadding(new Insets(6, 14, 6, 14));
        button.setText(lang.get(key));
        updateButtonState(button, student, status);
        button.setOnAction(e -> {
            if (student.status == status) {
                student.status = AttendanceStatus.UNMARKED;
            } else {
                student.status = status;
            }
            refreshRows();
        });
        languageUpdaters.add(() -> button.setText(lang.get(key)));
        themeUpdaters.add(() -> button.setStyle(statusButtonStyle(student, status)));
        return button;
    }

    private void updateButtonState(Button button, StudentAttendance student, AttendanceStatus btnStatus) {
        button.setStyle(statusButtonStyle(student, btnStatus));
    }

    private String statusButtonStyle(StudentAttendance student, AttendanceStatus buttonStatus) {
        boolean active = student.status == buttonStatus;
        boolean anyChosen = student.status != AttendanceStatus.UNMARKED;

        if (!anyChosen) {
            if (buttonStatus == AttendanceStatus.PRESENT)
                return buttonStyle("#16a34a", "#ffffff", "transparent");
            if (buttonStatus == AttendanceStatus.ABSENT)
                return buttonStyle("#ef4444", "#ffffff", "transparent");
            return buttonStyle("#f59e0b", "#ffffff", "transparent");
        }

        if (active) {
            if (buttonStatus == AttendanceStatus.PRESENT)
                return buttonStyle("#16a34a", "#ffffff", "#15803d");
            if (buttonStatus == AttendanceStatus.ABSENT)
                return buttonStyle("#ef4444", "#ffffff", "#dc2626");
            return buttonStyle("#f59e0b", "#ffffff", "#d97706");
        }

        String grayBg = theme.isDark() ? "#374151" : "#d1d5db";
        String grayText = theme.isDark() ? "#6b7280" : "#9ca3af";
        return buttonStyle(grayBg, grayText, "transparent");
    }

    private String buttonStyle(String bg, String text, String border) {
        return "-fx-background-color: " + bg + "; -fx-text-fill: " + text + "; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: " + border + "; -fx-border-width: " + ("transparent".equals(border) ? "0" : "2") + "; -fx-cursor: hand;";
    }

    private void showSelectorMenu(MouseEvent event, List<String> options, Label target) {
        ContextMenu menu = new ContextMenu();
        String textC = theme.isDark() ? "#F8FAFC" : "#0f172a";
        menu.setStyle("-fx-background-color: " + (theme.isDark() ? "#1E293B" : "#ffffff") + "; -fx-border-color: " + (theme.isDark() ? "#334155" : "#E5E7EB") + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 6; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0.15, 0, 4);");
        for (String opt : options) {
            MenuItem item = new MenuItem(opt);
            boolean active = opt.equals(target.getText());
            item.setStyle((active ? "-fx-text-fill: " + (theme.isDark() ? "#475569" : "#94A3B8") : "-fx-text-fill: " + textC) + "; -fx-font-weight: 700; -fx-font-size: 12; -fx-padding: 8 16;");
            item.setDisable(active);
            item.setOnAction(e -> {
                target.setText(opt);
                updateLanguage();
                reloadStudents();
            });
            menu.getItems().add(item);
        }
        menu.show((Node) event.getSource(), Side.BOTTOM, 0, 0);
    }

    private void wireEvents() {
        searchField.textProperty().addListener((obs, oldValue, newValue) -> refreshRows());
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
        reloadStudents();
    }

    private void reloadStudents() {
        students.clear();
        String g = gradeLabel.getText();

        int count;
        if (g.startsWith("4")) count = 30 + RNG.nextInt(10); // 30-39
        else if (g.startsWith("5")) count = 30 + RNG.nextInt(6); // 30-35
        else if (g.startsWith("6")) count = 25 + RNG.nextInt(6); // 25-30
        else count = 20 + RNG.nextInt(11);

        for (int i = 0; i < count; i++) {
            String name = LAST_NAMES[RNG.nextInt(LAST_NAMES.length)] + ", " + FIRST_NAMES[RNG.nextInt(FIRST_NAMES.length)];
            students.add(new StudentAttendance(name, RNG.nextInt(8)));
        }
        refreshRows();
    }

    private static final Random RNG = new Random(42);
    private static final String[] FIRST_NAMES = {
        "Alejandro", "Lucía", "Carlos", "Sofía", "Diego", "Valentina", "Camila", "Daniel",
        "Mariana", "Sebastián", "Isabella", "Mateo", "Ximena", "Santiago", "Fernanda", "Nicolás",
        "Valeria", "Emiliano", "Renata", "Mauricio", "Paulina", "Alan", "Fátima", "Raúl",
        "Abril", "Emilio", "Daniela", "Gustavo", "Jimena", "Fernando", "Benjamín", "Natalia"
    };
    private static final String[] LAST_NAMES = {
        "García López", "Martínez Ríos", "Pérez Soto", "López Vargas", "Ramírez Cruz",
        "Torres Medina", "Núñez Vera", "Santos Rojas", "Díaz Mendoza", "Ortiz Guzmán",
        "Reyes Aguilar", "Medina Cárdenas", "Cruz Peña", "Vega Roldán", "Campos Navarro",
        "Paredes Vega", "Figueroa Lara", "Acosta Molina", "Navarrete Ríos", "Salinas Cordero",
        "Guerrero Luna", "Maldonado Pacheco", "Sosa Del Valle", "Castillo Peña", "Rivas Contreras",
        "Peralta Sandoval", "Herrera Campos", "Morales Vega", "Castro Rivas", "Flores Delgado",
        "Ríos Paredes", "Delgado Rojas"
    };

    private enum AttendanceStatus { UNMARKED, PRESENT, ABSENT, EXCUSE }

    private static class StudentAttendance {
        private final String name;
        private final int colorIndex;
        private AttendanceStatus status = AttendanceStatus.UNMARKED;

        StudentAttendance(String name, int colorIndex) {
            this.name = name;
            this.colorIndex = colorIndex;
        }

        String name() { return name; }
    }
}
