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
            {"Denis Ramirez", "denis.ramirez@edu.com", "Ofimática", "4to E", "Activo"},
            {"Santa Hichez", "santa.hichez@edu.com", "Español", "4to E", "Activo"},
            {"Luz Leclerc", "luz.leclerc@edu.com", "Matemáticas", "4to E", "Activo"},
            {"Shirley Gómez", "shirley.gomez@edu.com", "Artística", "4to E", "Activo"},
            {"Claudilina Ferrera", "claudilina.ferrera@edu.com", "Biología", "4to E", "Activo"},
            {"Kelvis", "kelvis@edu.com", "Educación Física", "4to E", "Activo"},
            {"DASI Profesor", "dasi.prof@edu.com", "DASI", "4to E", "Activo"},
            {"Jose Sierra", "jose.sierra@edu.com", "DASI", "5to E", "Activo"},
            {"Anderson", "anderson@edu.com", "Base de datos", "5to E", "Activo"},
            {"Paola Dilone", "paola.dilone@edu.com", "Química", "5to E", "Activo"},
            {"Sofía", "sofia@edu.com", "Español", "5to E", "Activo"},
            {"Karina Pérez", "karina.perez@edu.com", "Matemáticas", "5to E", "Activo"},
            {"María Rodríguez", "maria.rodriguez@edu.com", "Base de datos", "6to E", "Activo"},
            {"Carlos Méndez", "carlos.mendez@edu.com", "Página web", "6to E", "Activo"},
            {"Laura Jiménez", "laura.jimenez@edu.com", "DASI", "6to E", "Activo"},
            {"Roberto Vargas", "roberto.vargas@edu.com", "Diseño de Reporte", "6to E", "Activo"},
        };

        for (int i = 0; i < rawTeachers.length; i++) {
            String[] r = rawTeachers[i];
            TEACHERS.add(new TeacherInfo(r[0], r[1], r[2], r[3], r[4], i));
        }

        String[][] rawCourses = {
            {"4to", "E", "Ofimática", "0", "32", "9.2", "En clase"},
            {"5to", "E", "DASI", "7", "28", "7.8", "En clase"},
            {"6to", "E", "Sistemas", "12", "24", "8.8", "En clase"},
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
