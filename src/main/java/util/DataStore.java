package util;

import java.util.ArrayList;
import java.util.List;

import com.edugrade.controllers.CursoController;
import com.edugrade.controllers.MaestrosController;

public class DataStore {

    private static List<CursoController.CourseRow> courses = new ArrayList<>();
    private static List<MaestrosController.TeacherRow> teachers = new ArrayList<>();
    private static String cachedAttendanceRate = "0%";

    private static final String[] NAMES = {
        "Dr. Roberto", "Dra. Elena", "Prof. Juan Carlos", "Dra. Elena",
        "Dr. Roberto", "Prof. Juan Carlos", "Mtra. Sofia", "Dr. Roberto"
    };
    private static final String[] SURNAMES = {
        "S\u00e1nchez", "M\u00e9ndez", "Rico", "M\u00e9ndez",
        "S\u00e1nchez", "Rico", "Vald\u00e9z", "S\u00e1nchez"
    };

    public static void setCourses(List<CursoController.CourseRow> c) {
        courses = new ArrayList<>(c);
    }

    public static List<CursoController.CourseRow> getCourses() {
        return courses;
    }

    public static void setTeachers(List<MaestrosController.TeacherRow> t) {
        teachers = new ArrayList<>(t);
    }

    public static List<MaestrosController.TeacherRow> getTeachers() {
        return teachers;
    }

    public static String getTeacherName(int idx) {
        int i = idx % NAMES.length;
        return NAMES[i] + " " + SURNAMES[i];
    }

    public static int getTotalStudents() {
        return courses.stream().mapToInt(CursoController.CourseRow::alumnos).sum();
    }

    public static int getTotalCourses() {
        return courses.size();
    }

    public static int getTotalTeachers() {
        return Math.max(teachers.size(), 1);
    }

    public static String getAttendanceRate() {
        return cachedAttendanceRate;
    }

    public static void setAttendanceRate(String rate) {
        cachedAttendanceRate = rate;
    }
}
