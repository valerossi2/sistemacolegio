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
    private static final Map<String, List<Integer>> COURSE_TEACHER_INDICES = new LinkedHashMap<>();
    private static final Map<String, Map<String, String>> ATTENDANCE = new HashMap<>();
    // key: "grado seccion" → (student matricula → "presente"|"ausente"|"excusa")

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
            {"4to", "E", "Ofimática", "0", "9.2", "En clase"},
            {"5to", "E", "DASI", "6", "7.8", "En clase"},
            {"6to", "E", "Sistemas", "11", "8.8", "En clase"},
        };

        String[] firstNames = {"Liam","Emma","Noah","Olivia","Mateo","Isabella","Santiago","Sophia","Lucas","Mía","Benjamín","Valentina","Sebastián","Camila","Daniel","Gabriela","Carlos","Valeria","Diego","Sofía"};
        String[] lastNames = {"Castillo","Rodríguez","García","Martínez","Hernández","López","Pérez","González","Fernández","Torres","Ramírez","Morales","Ortiz","Cruz","Reyes","Vargas"};

        for (String[] rc : rawCourses) {
            String grado = rc[0];
            String seccion = rc[1];
            int profIdx = Integer.parseInt(rc[3]);
            int alumCount = ThreadLocalRandom.current().nextInt(20, 36);
            double rend = Double.parseDouble(rc[4]);
            String estado = rc[5];
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

            // Initialize attendance for this course
            Map<String, String> att = new HashMap<>();
            for (var st : students) {
                att.put(st.matricula(), "presente");
            }
            ATTENDANCE.put(key, att);

            // Teacher-course mapping
            TEACHER_COURSES.computeIfAbsent(profName, k -> new ArrayList<>()).add(key);
        }

        // Schedule
        String[][] rawSchedule = {
            {"08:00", "4to E"},
            {"09:00", "5to E"},
            {"10:00", "6to E"},
        };

        for (String[] rs : rawSchedule) {
            String time = rs[0];
            String gs = rs[1];
            CourseInfo match = COURSES.stream().filter(c -> (c.grado() + " " + c.seccion()).equals(gs)).findFirst().orElse(null);
            if (match != null) {
                String tutor = match.profesorNombre();
                String subject = match.grado() + " " + match.seccion() + " - " + tutor;
                SCHEDULE.add(new ScheduleInfo(time, subject, gs, gs));
            }
        }

        // ── Course → Teacher mapping (8–12 per course) ──
        Map<String, List<Integer>> ct = new LinkedHashMap<>();
        ct.put("4to E", List.of(0,1,2,3,4,5,6,7,8));
        ct.put("5to E", List.of(5,6,7,8,9,10,0,1,3));
        ct.put("6to E", List.of(11,12,13,14,0,6,7,5,8));
        ct.forEach((key, indices) -> COURSE_TEACHER_INDICES.put(key, List.copyOf(indices)));
    }

    public static List<TeacherInfo> getTeachers() { return Collections.unmodifiableList(TEACHERS); }
    public static List<CourseInfo> getCourses() { return Collections.unmodifiableList(COURSES); }
    public static List<ScheduleInfo> getSchedule() { return Collections.unmodifiableList(SCHEDULE); }
    public static List<StudentInfo> getStudentsForCourse(String gradoSeccion) { return COURSE_STUDENTS.getOrDefault(gradoSeccion, List.of()); }
    public static List<String> getCoursesForTeacher(String teacherName) { return TEACHER_COURSES.getOrDefault(teacherName, List.of()); }
    public static List<Integer> getTeacherIndicesForCourse(String gradoSeccion) { return COURSE_TEACHER_INDICES.getOrDefault(gradoSeccion, List.of()); }
    public static int getTotalStudents() { return COURSE_STUDENTS.values().stream().mapToInt(List::size).sum(); }
    public static int getTotalCourses() { return COURSES.size(); }
    public static int getTotalTeachers() { return TEACHERS.size(); }

    // Keep old API for backward compat
    public static String getTeacherName(int idx) {
        if (idx >= 0 && idx < TEACHERS.size()) return TEACHERS.get(idx).nombre();
        return "Docente";
    }

    public static String getTeacherMateria(int idx) {
        if (idx >= 0 && idx < TEACHERS.size()) return TEACHERS.get(idx).materia();
        return "—";
    }

    // ── Shared Attendance ──
    public static String getAttendance(String courseKey, String matricula) {
        return ATTENDANCE.getOrDefault(courseKey, Map.of()).getOrDefault(matricula, "presente");
    }

    public static void setAttendance(String courseKey, String matricula, String status) {
        ATTENDANCE.computeIfAbsent(courseKey, k -> new HashMap<>()).put(matricula, status);
    }

    public static java.util.Map<String, String> getAttendanceForCourse(String courseKey) {
        return Collections.unmodifiableMap(ATTENDANCE.getOrDefault(courseKey, Collections.emptyMap()));
    }

    // ── Helpers ──
    public static String getEncargadoForCourse(String gradoSeccion) {
        return COURSES.stream()
            .filter(c -> (c.grado() + " " + c.seccion()).equals(gradoSeccion))
            .map(CourseInfo::profesorNombre)
            .findFirst().orElse("—");
    }
}
