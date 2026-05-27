package com.edugrade.model;

import javafx.beans.property.*;

import java.util.HashMap;
import java.util.Map;

public class Student {

    public enum Gender { MALE, FEMALE }

    private final StringProperty  name          = new SimpleStringProperty();
    private final StringProperty  email         = new SimpleStringProperty();
    private final StringProperty  studentId     = new SimpleStringProperty();
    private final DoubleProperty  currentGrade  = new SimpleDoubleProperty();
    private final StringProperty  newGrade      = new SimpleStringProperty("");
    private final ObjectProperty<Gender> gender = new SimpleObjectProperty<>();

    private final Map<String, Double> evalGrades = new HashMap<>();

    public Student(String name, String email, String studentId,
                   double currentGrade, Gender gender) {
        this.name.set(name);
        this.email.set(email);
        this.studentId.set(studentId);
        this.currentGrade.set(currentGrade);
        this.gender.set(gender);
    }

    public void addEvalGrade(String evalType, double grade) {
        evalGrades.put(evalType, grade);
        double sum = 0;
        for (double g : evalGrades.values()) sum += g;
        currentGrade.set(sum);
    }

    public double getEvalGrade(String evalType) {
        return evalGrades.getOrDefault(evalType, 0.0);
    }

    public void clearEvalGrades() {
        evalGrades.clear();
        currentGrade.set(0);
    }

    public StringProperty  nameProperty()         { return name; }
    public StringProperty  emailProperty()        { return email; }
    public StringProperty  studentIdProperty()    { return studentId; }
    public DoubleProperty  currentGradeProperty() { return currentGrade; }
    public StringProperty  newGradeProperty()     { return newGrade; }
    public ObjectProperty<Gender> genderProperty(){ return gender; }

    public String  getName()         { return name.get(); }
    public String  getEmail()        { return email.get(); }
    public String  getStudentId()    { return studentId.get(); }
    public double  getCurrentGrade() { return currentGrade.get(); }
    public String  getNewGrade()     { return newGrade.get(); }
    public Gender  getGender()       { return gender.get(); }
}
