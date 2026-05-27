package util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DataStore {

    // ── Records ──
    public record TeacherInfo(String nombre, String email, String materia, String seccion, String estado, int avatarIdx) {}
    public record CourseInfo(String grado, String seccion, String profesorNombre, int profesorIdx, int alumnos, double rendimiento, String estado) {}
    public record ScheduleInfo(String time, String subject, String detail, String gradoSeccion) {}
    public record StudentInfo(String nombre, String matricula) {}

    private static final List<TeacherInfo> TEACHERS = new ArrayList<>();
    private static final List<CourseInfo> COURSES = new ArrayList<>();
    private static final List<ScheduleInfo> SCHEDULE = new ArrayList<>();
    private static final Map<String, List<StudentInfo>> COURSE_STUDENTS = new LinkedHashMap<>();
    private static final Map<String, List<String>> TEACHER_COURSES = new LinkedHashMap<>();

    private static boolean seeded = false;

    public static synchronized void seedIfEmpty() {
        if (seeded) return;
        seeded = true;

        String[][] rawTeachers = {
            {"Prof. Laura Méndez", "laura.mendez@edu.com", "Matemáticas", "5to E", "Activo"},
            {"Prof. Carlos Ruiz", "carlos.ruiz@edu.com", "Historia", "4to A", "Activo"},
            {"Prof. Elena Torres", "elena.torres@edu.com", "Lenguaje", "3ro B", "Activo"},
            {"Prof. Ana Silva", "ana.silva@edu.com", "Ciencias", "2do C", "Activo"},
            {"Prof. Miguel Soto", "miguel.soto@edu.com", "Inglés", "1ro A", "Inactivo"},
            {"Prof. Diana Ríos", "diana.rios@edu.com", "Arte", "5to B", "Activo"},
            {"Prof. Pedro Lima", "pedro.lima@edu.com", "Educación Física", "4to B", "Activo"},
            {"Prof. Sofía Vega", "sofia.vega@edu.com", "Música", "3ro A", "Activo"},
            {"Prof. Luis Paz", "luis.paz@edu.com", "Filosofía", "6to A", "Inactivo"},
            {"Prof. Carmen Rojas", "carmen.rojas@edu.com", "Biología", "5to C", "Activo"},
            {"Prof. Andrés Cruz", "andres.cruz@edu.com", "Química", "4to C", "Activo"},
            {"Prof. Valeria Solís", "valeria.solis@edu.com", "Historia del Arte", "6to B", "Activo"},
        };

        for (int i = 0; i < rawTeachers.length; i++) {
            String[] r = rawTeachers[i];
            TEACHERS.add(new TeacherInfo(r[0], r[1], r[2], r[3], r[4], i));
        }

        String[][] rawCourses = {
            {"4to", "E", "Matemáticas", "0", "32", "9.2", "En clase"},
            {"5to", "E", "Historia", "1", "28", "7.8", "En clase"},
            {"6to", "E", "Lenguaje", "2", "24", "8.8", "En clase"},
        };

        String[] firstNames = {"Liam","Emma","Noah","Olivia","Mateo","Isabella","Santiago","Sophia","Lucas","Mía","Benjamín","Valentina"};
        String[] lastNames = {"Castillo","Rodríguez","García","Martínez","Hernández","López","Pérez","González","Fernández","Torres"};

        for (String[] rc : rawCourses) {
            String grado = rc[0];
            String seccion = rc[1];
            int profIdx = Integer.parseInt(rc[3]);
            int alumCount = Integer.parseInt(rc[4]);
            double rend = Double.parseDouble(rc[5]);
            String estado = rc[6];
            String key = grado + " " + seccion;
            String profName = rawTeachers[profIdx][0];

            COURSES.add(new CourseInfo(grado, seccion, profName, profIdx, alumCount, rend, estado));

            // Students for this course
            List<StudentInfo> students = new ArrayList<>();
            for (int s = 0; s < alumCount; s++) {
                String name = firstNames[ThreadLocalRandom.current().nextInt(firstNames.length)] + " " + lastNames[ThreadLocalRandom.current().nextInt(lastNames.length)];
                String mat = String.format("MAT-%03d", ThreadLocalRandom.current().nextInt(1, 999));
                students.add(new StudentInfo(name, mat));
            }
            COURSE_STUDENTS.put(key, students);

            // Teacher-course mapping
            TEACHER_COURSES.computeIfAbsent(profName, k -> new ArrayList<>()).add(key);
        }

        // Schedule (real courses mapped to time slots)
        String[][] rawSchedule = {
            {"08:00", "4to E", "Salon 101"},
            {"09:00", "5to E", "Salon 102"},
            {"10:00", "6to E", "Salon 103"},
        };

        for (String[] rs : rawSchedule) {
            String time = rs[0];
            String gs = rs[1];
            String location = rs[2];
            CourseInfo match = COURSES.stream().filter(c -> (c.grado() + " " + c.seccion()).equals(gs)).findFirst().orElse(null);
            if (match != null) {
                String tutor = match.profesorNombre();
                String subject = match.grado() + " " + match.seccion() + " - " + tutor;
                String detail = location + " - " + tutor;
                SCHEDULE.add(new ScheduleInfo(time, subject, detail, gs));
            }
        }
    }

    public static List<TeacherInfo> getTeachers() { return Collections.unmodifiableList(TEACHERS); }
    public static List<CourseInfo> getCourses() { return Collections.unmodifiableList(COURSES); }
    public static List<ScheduleInfo> getSchedule() { return Collections.unmodifiableList(SCHEDULE); }
    public static List<StudentInfo> getStudentsForCourse(String gradoSeccion) { return COURSE_STUDENTS.getOrDefault(gradoSeccion, List.of()); }
    public static List<String> getCoursesForTeacher(String teacherName) { return TEACHER_COURSES.getOrDefault(teacherName, List.of()); }
    public static int getTotalStudents() { return COURSE_STUDENTS.values().stream().mapToInt(List::size).sum(); }
    public static int getTotalCourses() { return COURSES.size(); }
    public static int getTotalTeachers() { return TEACHERS.size(); }

    // Keep old API for backward compat
    public static String getTeacherName(int idx) {
        if (idx >= 0 && idx < TEACHERS.size()) return TEACHERS.get(idx).nombre();
        return "Docente";
    }
}
