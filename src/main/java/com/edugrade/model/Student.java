package com.edugrade.model;

import javafx.beans.property.*;

public class Student {

    public enum Gender { MALE, FEMALE }

    private final StringProperty  name          = new SimpleStringProperty();
    private final StringProperty  email         = new SimpleStringProperty();
    private final StringProperty  studentId     = new SimpleStringProperty();
    private final DoubleProperty  currentGrade  = new SimpleDoubleProperty();
    private final StringProperty  newGrade      = new SimpleStringProperty("");
    private final ObjectProperty<Gender> gender = new SimpleObjectProperty<>();

    public Student(String name, String email, String studentId,
                   double currentGrade, Gender gender) {
        this.name.set(name);
        this.email.set(email);
        this.studentId.set(studentId);
        this.currentGrade.set(currentGrade);
        this.gender.set(gender);
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
