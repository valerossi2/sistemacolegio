package report;


import model.*;
import repository.*;
import service.AsistenciaService;
import service.CalificationService;

// iText 7
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ReportService — genera PDFs con datos reales de la BD.
 *
 * Reportes disponibles:
 *   Admin  → reporteGeneralCursos()        — todos los cursos con KPIs
 *   Admin  → reporteEstudiantesPorCurso()  — lista de alumnos de un curso
 *   Maestro → reporteCalificacionesMaestro() — notas de su curso
 *   Maestro → reporteBoletaEstudiante()    — boleta individual
 */
public class ReporteService {

    // ── Repositorios y servicios ──────────────────────────────
    private final CursoRepository      cursoRepo      = new CursoRepository();
    private final AsignacionRepository asignacionRepo = new AsignacionRepository();
    private final EstudianteRepository estudianteRepo = new EstudianteRepository();
    private final CalificationService calSvc         = new CalificationService();
    private final AsistenciaService    asistSvc       = new AsistenciaService();

    // ── Paleta de colores (igual a la de tus compañeros) ─────
    private static final DeviceRgb AZUL_OSCURO  = new DeviceRgb(0x1a, 0x3a, 0x5c);
    private static final DeviceRgb AZUL_MEDIO   = new DeviceRgb(0x2e, 0x6d, 0xa4);
    private static final DeviceRgb AZUL_HEADER  = new DeviceRgb(0x1e, 0x4d, 0x8c);
    private static final DeviceRgb AZUL_INFO    = new DeviceRgb(0x15, 0x65, 0xc0);
    private static final DeviceRgb LIGHT_BLUE   = new DeviceRgb(0xD9, 0xE8, 0xF5);
    private static final DeviceRgb BLANCO       = new DeviceRgb(255,  255,  255);
    private static final DeviceRgb GRIS_CLARO   = new DeviceRgb(0xf0, 0xf5, 0xfa);
    private static final DeviceRgb GRIS_BORDE   = new DeviceRgb(0xd0, 0xdc, 0xe8);
    private static final DeviceRgb GRIS_TEXTO   = new DeviceRgb(85,   85,   85);
    private static final DeviceRgb VERDE        = new DeviceRgb(0x2e, 0x7d, 0x32);
    private static final DeviceRgb NARANJA      = new DeviceRgb(0xf5, 0x7c, 0x00);
    private static final DeviceRgb ROJO         = new DeviceRgb(0xc6, 0x28, 0x28);
    private static final DeviceRgb GOLD         = new DeviceRgb(0xC8, 0x96, 0x0C);

    private static final String COLEGIO = "Politécnico Virgen de la Altagracia";
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ═══════════════════════════════════════════════════════════
    // 1. REPORTE GENERAL DE CURSOS (Admin)
    //    Equivalente a ReporteCursosPolitecnico.java de tus compañeros
    //    pero con datos reales de la BD
    // ═══════════════════════════════════════════════════════════
    public void reporteGeneralCursos(String rutaArchivo) throws IOException {

        PdfFont bold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        PdfDocument pdfDoc  = new PdfDocument(new PdfWriter(rutaArchivo));
        Document    document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(14, 14, 14, 14);

        List<Curso> cursos = cursoRepo.findAll();

        // Calcular KPIs reales
        int totalCursos     = cursos.size();
        int totalEstudiantes = 0;
        int totalAbiertos   = 0;
        double sumaPromedios = 0;

        for (Curso c : cursos) {
            List<Matricula> mats = asignacionRepo.findMatriculasByCurso(c.getId());
            totalEstudiantes += mats.size();
            if (c.estaAbierto()) totalAbiertos++;
            for (Matricula m : mats) {
                sumaPromedios += calSvc.CalcularPromedioPonderado(
                        m.getId(), c.getId());
            }
        }

        double promedioGeneral = totalEstudiantes > 0
                ? sumaPromedios / totalEstudiantes : 0.0;

        // ── Encabezado ────────────────────────────────────────
        document.add(buildEncabezado(bold, regular,
                "Reporte de Cursos",
                LocalDate.now().format(FMT)));
        document.add(spacer(4));

        // ── KPIs reales ───────────────────────────────────────
        document.add(sectionLabel("INDICADORES GENERALES", bold));
        document.add(spacer(2));
        document.add(buildKpiStrip(bold, regular,
                totalCursos, totalEstudiantes,
                totalAbiertos, promedioGeneral));
        document.add(spacer(4));

        // ── Tabla de cursos ───────────────────────────────────
        document.add(sectionLabel("DETALLE DE CURSOS", bold));
        document.add(spacer(2));
        document.add(buildTablaCursos(bold, regular, cursos));
        document.add(spacer(4));

        // ── Resumen por periodo ───────────────────────────────
        document.add(sectionLabel("RESUMEN POR PERIODO", bold));
        document.add(spacer(2));
        document.add(buildResumenPeriodo(bold, regular, cursos));

        // ── Pie ───────────────────────────────────────────────
        document.add(buildFooter(regular, "Reporte de Cursos"));

        document.close();
    }

    // ═══════════════════════════════════════════════════════════
    // 2. REPORTE DE CALIFICACIONES DEL MAESTRO
    //    Equivalente a boletinNotas.java de tus compañeros
    //    pero con datos reales de la BD
    // ═══════════════════════════════════════════════════════════
    public void reporteCalificacionesMaestro(Integer cursoId,
                                             String rutaArchivo) throws IOException {

        PdfFont bold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Curso no encontrado: " + cursoId));

        // Maestro asignado
        List<AsignacionMaestro> asigs =
                asignacionRepo.findAsignacionesByCurso(cursoId);
        String nombreMaestro = asigs.isEmpty() ? "Sin asignar" :
                asigs.get(0).getMaestro().getNombreCompleto();

        // Estudiantes y sus notas
        List<Matricula> matriculas =
                asignacionRepo.findMatriculasByCurso(cursoId);

        PdfDocument pdfDoc  = new PdfDocument(new PdfWriter(rutaArchivo));
        Document    document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(28, 42, 34, 42);

        // ── Encabezado ────────────────────────────────────────
        document.add(buildEncabezado(bold, regular,
                "Reporte de Notas — " + curso.getNombreCompleto(),
                LocalDate.now().format(FMT)));
        document.add(spacer(5));

        // ── Datos del maestro ─────────────────────────────────
        addSectionTitle(document, "DATOS DEL PROFESOR", bold);
        Table datosMaestro = new Table(
                UnitValue.createPercentArray(new float[]{25, 75}))
                .useAllAvailableWidth();

        addDatoCell(datosMaestro, "Profesor:",  bold,    10, AZUL_OSCURO, GRIS_CLARO);
        addDatoCell(datosMaestro, nombreMaestro, regular, 10, GRIS_TEXTO, BLANCO);
        addDatoCell(datosMaestro, "Materia:",   bold,    10, AZUL_OSCURO, GRIS_CLARO);
        addDatoCell(datosMaestro, curso.getMateria().getNombre(), regular, 10, GRIS_TEXTO, BLANCO);
        addDatoCell(datosMaestro, "Sección:",   bold,    10, AZUL_OSCURO, GRIS_CLARO);
        addDatoCell(datosMaestro, curso.getSeccion().getNombre() + " — " +
                curso.getSeccion().getGrado().getNombre(), regular, 10, GRIS_TEXTO, BLANCO);
        addDatoCell(datosMaestro, "Período:",   bold,    10, AZUL_OSCURO, GRIS_CLARO);
        addDatoCell(datosMaestro, curso.getPeriodo().getNombre(), regular, 10, GRIS_TEXTO, BLANCO);

        document.add(datosMaestro);
        document.add(spacer(5));

        // ── Tabla de calificaciones ───────────────────────────
        addSectionTitle(document, "CALIFICACIONES DE ESTUDIANTES", bold);

        Table calTable = new Table(
                UnitValue.createPercentArray(new float[]{5, 15, 30, 12, 12, 12, 14}))
                .useAllAvailableWidth();

        // Encabezados
        for (String h : new String[]{"#","Matrícula","Estudiante",
                "Parcial","Final","Tareas","Promedio"}) {
            calTable.addHeaderCell(tableHeaderCell(h, bold));
        }

        // Filas con datos reales de la BD
        int idx = 1;
        double sumaPromedios = 0;
        double notaAlta = 0;
        double notaBaja = 100;

        for (Matricula m : matriculas) {
            double promedio = calSvc.CalcularPromedioPonderado(
                    m.getId(), cursoId);
            sumaPromedios += promedio;
            if (promedio > notaAlta) notaAlta = promedio;
            if (promedio < notaBaja) notaBaja = promedio;

            List<Calificacion> notas =
                    calSvc.getCalificacionesPorMatricula(m.getId(), cursoId);

            String parcial = "—", exFinal = "—", tareas = "—";
            for (Calificacion c : notas) {
                String tipo = c.getTipoEvaluacion().getNombre();
                String nota = String.format("%.0f", c.getNota());
                switch (tipo) {
                    case "Examen Parcial" -> parcial  = nota;
                    case "Examen Final"   -> exFinal  = nota;
                    case "Tareas"         -> tareas   = nota;
                }
            }

            DeviceRgb bg = (idx % 2 == 0) ? GRIS_CLARO : BLANCO;
            DeviceRgb colorProm = colorNota(promedio);

            calTable.addCell(tableCell(String.valueOf(idx),             regular, 10, GRIS_TEXTO, bg, TextAlignment.CENTER));
            calTable.addCell(tableCell(m.getEstudiante().getCodigo(),   regular, 10, GRIS_TEXTO, bg, TextAlignment.CENTER));
            calTable.addCell(tableCell(m.getEstudiante().getNombreCompleto(), regular, 10, GRIS_TEXTO, bg, TextAlignment.LEFT));
            calTable.addCell(tableCell(parcial,  regular, 10, GRIS_TEXTO, bg, TextAlignment.CENTER));
            calTable.addCell(tableCell(exFinal,  regular, 10, GRIS_TEXTO, bg, TextAlignment.CENTER));
            calTable.addCell(tableCell(tareas,   regular, 10, GRIS_TEXTO, bg, TextAlignment.CENTER));
            calTable.addCell(tableCell(String.format("%.1f", promedio), bold, 10, colorProm, bg, TextAlignment.CENTER));
            idx++;
        }

        document.add(calTable);
        document.add(spacer(5));

        // ── Resumen del periodo ───────────────────────────────
        addSectionTitle(document, "RESUMEN DEL PERÍODO", bold);

        double promedioGeneral = matriculas.isEmpty() ? 0 :
                sumaPromedios / matriculas.size();

        Table resumen = new Table(
                UnitValue.createPercentArray(new float[]{25, 25, 25, 25}))
                .useAllAvailableWidth();

        addResumenCell(resumen, String.format("%.1f", promedioGeneral),
                "Promedio General", bold, regular, colorNota(promedioGeneral));
        addResumenCell(resumen, String.format("%.1f", notaAlta),
                "Nota Más Alta", bold, regular, colorNota(notaAlta));
        addResumenCell(resumen, String.format("%.1f", notaBaja),
                "Nota Más Baja", bold, regular, colorNota(notaBaja));
        addResumenCell(resumen, condicion(promedioGeneral),
                "Condición General", bold, regular, colorNota(promedioGeneral));

        document.add(resumen);
        document.add(spacer(5));

        // ── Firmas ────────────────────────────────────────────
        Table firmas = new Table(
                UnitValue.createPercentArray(new float[]{50, 50}))
                .useAllAvailableWidth();

        firmas.addCell(firmaCell("_____________________________",
                nombreMaestro, curso.getMateria().getNombre(), bold, regular));
        firmas.addCell(firmaCell("_____________________________",
                "Director Académico", COLEGIO, bold, regular));

        document.add(firmas);
        document.add(buildFooter(regular,
                "Calificaciones — " + curso.getNombreCompleto()));
        document.close();
    }

    // ═══════════════════════════════════════════════════════════
    // 3. BOLETA INDIVIDUAL DEL ESTUDIANTE (Maestro)
    //    Equivalente a boletindocente.java de tus compañeros
    // ═══════════════════════════════════════════════════════════
    public void reporteBoletaEstudiante(Integer matriculaId,
                                        Integer cursoId, String rutaArchivo) throws IOException {

        PdfFont bold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        // Buscar la matrícula específica
        List<Matricula> todas = asignacionRepo.findMatriculasByCurso(cursoId);
        Matricula matricula = todas.stream()
                .filter(m -> m.getId().equals(matriculaId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Matrícula no encontrada"));

        Estudiante estudiante = matricula.getEstudiante();

        // Maestro
        List<AsignacionMaestro> asigs =
                asignacionRepo.findAsignacionesByCurso(cursoId);
        String nombreMaestro = asigs.isEmpty() ? "Sin asignar" :
                asigs.get(0).getMaestro().getNombreCompleto();

        // Calificaciones reales
        List<Calificacion> notas =
                calSvc.getCalificacionesPorMatricula(matriculaId, cursoId);
        double promedio = calSvc.CalcularPromedioPonderado(matriculaId, cursoId);
        double pctAsist = asistSvc.getPorcentajeAsistencia(matriculaId, cursoId);

        PdfDocument pdfDoc  = new PdfDocument(new PdfWriter(rutaArchivo));
        Document    document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(40, 40, 40, 40);

        // ── Encabezado ────────────────────────────────────────
        document.add(buildEncabezado(bold, regular,
                "BOLETÍN DE NOTAS",
                curso.getPeriodo().getNombre()));
        document.add(spacer(5));

        // ── Datos del estudiante ──────────────────────────────
        Table datos = new Table(
                UnitValue.createPercentArray(new float[]{30, 70}))
                .useAllAvailableWidth();

        addDatoCell(datos, "Estudiante:", bold,    11, AZUL_OSCURO, GRIS_CLARO);
        addDatoCell(datos, estudiante.getNombreCompleto(), regular, 11, GRIS_TEXTO, BLANCO);
        addDatoCell(datos, "Matrícula:",  bold,    11, AZUL_OSCURO, GRIS_CLARO);
        addDatoCell(datos, estudiante.getCodigo(),  regular, 11, GRIS_TEXTO, BLANCO);
        addDatoCell(datos, "Grado:",      bold,    11, AZUL_OSCURO, GRIS_CLARO);
        addDatoCell(datos, curso.getSeccion().getNombre() + " — " +
                curso.getSeccion().getGrado().getNombre(), regular, 11, GRIS_TEXTO, BLANCO);
        addDatoCell(datos, "Tutor:",      bold,    11, AZUL_OSCURO, GRIS_CLARO);
        addDatoCell(datos, nombreMaestro, regular, 11, GRIS_TEXTO, BLANCO);
        addDatoCell(datos, "Emisión:",    bold,    11, AZUL_OSCURO, GRIS_CLARO);
        addDatoCell(datos, LocalDate.now().format(FMT), regular, 11, GRIS_TEXTO, BLANCO);

        document.add(datos);
        document.add(spacer(5));

        // ── Tabla de notas ────────────────────────────────────
        addSectionTitle(document, "CALIFICACIONES", bold);

        Table tabla = new Table(
                UnitValue.createPercentArray(new float[]{5, 40, 15, 15, 25}))
                .useAllAvailableWidth();

        for (String h : new String[]{"#","Asignatura / Tipo","Peso (%)","Nota","Condición"}) {
            tabla.addHeaderCell(tableHeaderCell(h, bold));
        }

        int contador = 1;
        for (Calificacion c : notas) {
            DeviceRgb bg = (contador % 2 == 0) ? GRIS_CLARO : BLANCO;
            double nota  = c.getNota().doubleValue();

            tabla.addCell(tableCell(String.valueOf(contador),
                    regular, 11, GRIS_TEXTO, bg, TextAlignment.CENTER));
            tabla.addCell(tableCell(c.getTipoEvaluacion().getNombre(),
                    regular, 11, GRIS_TEXTO, bg, TextAlignment.LEFT));
            tabla.addCell(tableCell(
                    String.format("%.0f%%", c.getTipoEvaluacion().getPeso()),
                    regular, 11, GRIS_TEXTO, bg, TextAlignment.CENTER));
            tabla.addCell(tableCell(String.format("%.0f", nota),
                    bold, 11, colorNota(nota), bg, TextAlignment.CENTER));
            tabla.addCell(tableCell(condicion(nota),
                    bold, 11, colorNota(nota), bg, TextAlignment.CENTER));
            contador++;
        }

        document.add(tabla);
        document.add(spacer(5));

        // ── Resumen ───────────────────────────────────────────
        addSectionTitle(document, "RESUMEN", bold);

        Table resumen = new Table(
                UnitValue.createPercentArray(new float[]{25, 25, 25, 25}))
                .useAllAvailableWidth();

        addResumenCell(resumen, String.format("%.1f", promedio),
                "Promedio General", bold, regular, colorNota(promedio));
        addResumenCell(resumen, condicion(promedio),
                "Condición", bold, regular, colorNota(promedio));
        addResumenCell(resumen, String.format("%.1f%%", pctAsist),
                "Asistencia", bold, regular,
                pctAsist >= 75 ? VERDE : ROJO);
        addResumenCell(resumen, promedio >= 60 ? "APROBADO" : "REPROBADO",
                "Estado Final", bold, regular,
                promedio >= 60 ? VERDE : ROJO);

        document.add(resumen);
        document.add(spacer(5));

        // ── Observación ───────────────────────────────────────
        addSectionTitle(document, "OBSERVACIÓN DEL TUTOR", bold);
        String obs = promedio >= 80
                ? estudiante.getNombre() + " ha demostrado excelente desempeño académico este periodo."
                : promedio >= 60
                  ? estudiante.getNombre() + " ha aprobado el periodo. Se recomienda reforzar las materias con menor calificación."
                  : estudiante.getNombre() + " requiere apoyo adicional. Se recomienda tutorías y comunicación con el tutor familiar.";

        document.add(new Paragraph(obs)
                .setFont(regular)
                .setFontSize(10)
                .setFontColor(AZUL_OSCURO)
                .setBackgroundColor(GRIS_CLARO)
                .setBorder(new SolidBorder(GRIS_BORDE, 0.5f))
                .setPadding(9)
                .setFixedLeading(14f));

        document.add(spacer(7));

        // ── Firmas ────────────────────────────────────────────
        Table firmas = new Table(
                UnitValue.createPercentArray(new float[]{50, 50}))
                .useAllAvailableWidth();

        firmas.addCell(firmaCell("_____________________________",
                nombreMaestro, curso.getMateria().getNombre(), bold, regular));
        firmas.addCell(firmaCell("_____________________________",
                "Director Académico", COLEGIO, bold, regular));

        document.add(firmas);
        document.add(buildFooter(regular,
                "Boletín — " + estudiante.getNombreCompleto()));
        document.close();
    }

    // ═══════════════════════════════════════════════════════════
    // MÉTODOS AUXILIARES — construcción de elementos
    // ═══════════════════════════════════════════════════════════

    private Table buildEncabezado(PdfFont bold, PdfFont regular,
                                  String titulo, String subtitulo) throws IOException {

        float contentW = PageSize.A4.getWidth() - 28f;

        // Logo (opcional)
        Image logo = null;
        try {
            logo = new Image(ImageDataFactory.create(
                    getClass().getResource("/images/logo.png").toString()))
                    .setWidth(50).setHeight(50);
        } catch (Exception ignored) {}

        Paragraph title = new Paragraph(COLEGIO)
                .setFont(bold).setFontSize(16).setFontColor(BLANCO).setMargin(0);
        Paragraph sub = new Paragraph(titulo)
                .setFont(regular).setFontSize(10).setFontColor(BLANCO).setMargin(0);
        Paragraph fecha = new Paragraph(subtitulo)
                .setFont(regular).setFontSize(9).setFontColor(BLANCO)
                .setTextAlignment(TextAlignment.RIGHT).setMargin(0);

        Cell logoCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setPadding(6);
        if (logo != null) logoCell.add(logo);

        Cell titleCell = new Cell().add(title).add(sub)
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPaddingLeft(8);

        Cell dateCell = new Cell().add(fecha)
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPaddingRight(8);

        float logoW  = 55f;
        float dateW  = 108f;
        float titleW = contentW - logoW - dateW;

        return new Table(new float[]{logoW, titleW, dateW})
                .setWidth(contentW)
                .addCell(logoCell)
                .addCell(titleCell)
                .addCell(dateCell)
                .setBackgroundColor(AZUL_HEADER)
                .setMargin(0);
    }

    private Table buildKpiStrip(PdfFont bold, PdfFont regular,
                                int totalCursos, int totalEstudiantes,
                                int totalAbiertos, double promedioGeneral) {

        float contentW = PageSize.A4.getWidth() - 28f;
        float colW = contentW / 4;

        String[][] kpis = {
            {String.valueOf(totalCursos),       "Total Cursos",      "BLUE"},
            {String.valueOf(totalEstudiantes),  "Total Estudiantes", "BLUE"},
            {String.valueOf(totalAbiertos),     "Cursos Abiertos",   "BLUE"},
            {String.format("%.1f", promedioGeneral), "Promedio Gral.", "GOLD"},
        };

        Table t = new Table(new float[]{colW, colW, colW, colW})
            .setWidth(contentW);

        for (String[] kpi : kpis) {
            DeviceRgb color = "GOLD".equals(kpi[2]) ? GOLD : AZUL_OSCURO;
            t.addCell(new Cell()
                .add(new Paragraph(kpi[0])
                    .setFont(bold).setFontSize(20).setFontColor(color)
                    .setTextAlignment(TextAlignment.CENTER).setMargin(0))
                .add(new Paragraph(kpi[1])
                    .setFont(regular).setFontSize(7).setFontColor(GRIS_TEXTO)
                    .setTextAlignment(TextAlignment.CENTER).setMargin(0))
                .setBackgroundColor(LIGHT_BLUE)
                .setBorder(new SolidBorder(BLANCO, 0.5f))
                .setPaddingTop(4).setPaddingBottom(4));
        }
        return t;
    }

    private Table buildTablaCursos(PdfFont bold, PdfFont regular,
                                   List<Curso> cursos) {

        Table t = new Table(UnitValue.createPercentArray(
            new float[]{5, 25, 15, 25, 15, 15}))
            .useAllAvailableWidth();

        for (String h : new String[]{"#","Materia","Sección",
                "Maestro","Periodo","Estado"}) {
            t.addHeaderCell(tableHeaderCell(h, bold));
        }

        int i = 1;
        for (Curso c : cursos) {
            DeviceRgb bg = (i % 2 == 0) ? LIGHT_BLUE : BLANCO;

            List<AsignacionMaestro> asigs =
                asignacionRepo.findAsignacionesByCurso(c.getId());
            String maestro = asigs.isEmpty() ? "Sin asignar" :
                asigs.get(0).getMaestro().getNombreCompleto();

            DeviceRgb estadoColor = c.estaAbierto() ? VERDE : ROJO;

            t.addCell(tableCell(String.valueOf(i),
                regular, 9, GRIS_TEXTO, bg, TextAlignment.CENTER));
            t.addCell(tableCell(c.getMateria().getNombre(),
                bold, 9, AZUL_OSCURO, bg, TextAlignment.LEFT));
            t.addCell(tableCell(c.getSeccion().getNombre() + " — " +
                c.getSeccion().getGrado().getNombre(),
                regular, 9, GRIS_TEXTO, bg, TextAlignment.CENTER));
            t.addCell(tableCell(maestro,
                regular, 9, GRIS_TEXTO, bg, TextAlignment.LEFT));
            t.addCell(tableCell(c.getPeriodo().getNombre(),
                regular, 9, GRIS_TEXTO, bg, TextAlignment.CENTER));
            t.addCell(tableCell(c.estaAbierto() ? "Abierto" : "Cerrado",
                bold, 9, estadoColor, bg, TextAlignment.CENTER));
            i++;
        }
        return t;
    }

    private Table buildResumenPeriodo(PdfFont bold, PdfFont regular,
                                      List<Curso> cursos) {

        float contentW = PageSize.A4.getWidth() - 28f;
        float colW = contentW / 3;

        Table t = new Table(new float[]{colW, colW, colW})
            .setWidth(contentW);

        for (String h : new String[]{"Periodo","Cursos","Estado"}) {
            t.addHeaderCell(tableHeaderCell(h, bold));
        }

        java.util.Map<String, Long> porPeriodo = cursos.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                c -> c.getPeriodo().getNombre(),
                java.util.stream.Collectors.counting()));

        int i = 0;
        for (var entry : porPeriodo.entrySet()) {
            DeviceRgb bg = (i % 2 == 0) ? BLANCO : LIGHT_BLUE;
            t.addCell(tableCell(entry.getKey(),
                bold, 9, AZUL_OSCURO, bg, TextAlignment.LEFT));
            t.addCell(tableCell(String.valueOf(entry.getValue()),
                regular, 9, GRIS_TEXTO, bg, TextAlignment.CENTER));
            t.addCell(tableCell("Activo",
                bold, 9, VERDE, bg, TextAlignment.CENTER));
            i++;
        }
        return t;
    }

    private Table buildFooter(PdfFont regular, String info) {
        float contentW = PageSize.A4.getWidth() - 28f;

        Cell left = new Cell()
            .add(new Paragraph(COLEGIO + "  |  " + info +
                "  |  " + LocalDate.now().format(FMT))
                .setFont(regular).setFontSize(7).setFontColor(GRIS_TEXTO))
            .setBorderTop(new SolidBorder(GRIS_TEXTO, 0.5f))
            .setBorderBottom(Border.NO_BORDER)
            .setBorderLeft(Border.NO_BORDER)
            .setBorderRight(Border.NO_BORDER)
            .setPaddingTop(3);

        return new Table(new float[]{contentW})
            .setWidth(contentW)
            .addCell(left);
    }

    private Table sectionLabel(String text, PdfFont bold) {
        float contentW = PageSize.A4.getWidth() - 28f;
        return new Table(new float[]{contentW})
            .setWidth(contentW)
            .addCell(new Cell()
                .add(new Paragraph(text)
                    .setFont(bold).setFontSize(9).setFontColor(BLANCO))
                .setBackgroundColor(AZUL_MEDIO)
                .setBorder(Border.NO_BORDER)
                .setPaddingTop(3).setPaddingBottom(3)
                .setPaddingLeft(6));
    }

    private void addSectionTitle(Document doc, String text, PdfFont bold) {
        Table bar = new Table(
            UnitValue.createPercentArray(new float[]{100}))
            .useAllAvailableWidth();
        bar.addCell(new Cell()
            .add(new Paragraph("  " + text)
                .setFont(bold).setFontSize(11).setFontColor(BLANCO))
            .setBackgroundColor(AZUL_MEDIO)
            .setBorder(Border.NO_BORDER)
            .setPaddingTop(6).setPaddingBottom(6).setPaddingLeft(6));
        doc.add(bar);
        doc.add(spacer(2));
    }

    private Cell tableHeaderCell(String text, PdfFont bold) {
        return new Cell()
            .add(new Paragraph(text).setFont(bold).setFontSize(9)
                .setFontColor(BLANCO))
            .setBackgroundColor(AZUL_OSCURO)
            .setBorder(new SolidBorder(GRIS_BORDE, 0.4f))
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingTop(4).setPaddingBottom(4);
    }

    private Cell tableCell(String text, PdfFont font, float size,
                           DeviceRgb fg, DeviceRgb bg, TextAlignment align) {
        return new Cell()
            .add(new Paragraph(text == null ? "—" : text)
                .setFont(font).setFontSize(size).setFontColor(fg))
            .setTextAlignment(align)
            .setBackgroundColor(bg)
            .setBorder(new SolidBorder(GRIS_BORDE, 0.4f))
            .setPaddingTop(4).setPaddingBottom(4)
            .setPaddingLeft(5).setPaddingRight(5)
            .setVerticalAlignment(VerticalAlignment.MIDDLE);
    }

    private void addDatoCell(Table t, String text, PdfFont font,
                             float size, DeviceRgb fg, DeviceRgb bg) {
        t.addCell(new Cell()
            .add(new Paragraph(text)
                .setFont(font).setFontSize(size).setFontColor(fg))
            .setBackgroundColor(bg)
            .setBorder(new SolidBorder(GRIS_BORDE, 0.3f))
            .setPaddingTop(5).setPaddingBottom(5)
            .setPaddingLeft(6).setPaddingRight(4)
            .setVerticalAlignment(VerticalAlignment.MIDDLE));
    }

    private void addResumenCell(Table t, String valor, String etiqueta,
                                PdfFont bold, PdfFont regular, DeviceRgb color) {
        t.addCell(new Cell()
            .add(new Paragraph(valor)
                .setFont(bold).setFontSize(18).setFontColor(color)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(2))
            .add(new Paragraph(etiqueta)
                .setFont(regular).setFontSize(9).setFontColor(GRIS_TEXTO)
                .setTextAlignment(TextAlignment.CENTER))
            .setBackgroundColor(GRIS_CLARO)
            .setBorder(new SolidBorder(GRIS_BORDE, 0.5f))
            .setPaddingTop(8).setPaddingBottom(8)
            .setVerticalAlignment(VerticalAlignment.MIDDLE));
    }

    private Cell firmaCell(String linea, String nombre, String cargo,
                           PdfFont bold, PdfFont regular) {
        return new Cell()
            .add(new Paragraph(linea)
                .setFont(regular).setFontSize(9).setFontColor(AZUL_OSCURO)
                .setTextAlignment(TextAlignment.CENTER))
            .add(new Paragraph(nombre)
                .setFont(bold).setFontSize(9).setFontColor(AZUL_OSCURO)
                .setTextAlignment(TextAlignment.CENTER))
            .add(new Paragraph(cargo)
                .setFont(regular).setFontSize(8).setFontColor(AZUL_OSCURO)
                .setTextAlignment(TextAlignment.CENTER))
            .setBorder(Border.NO_BORDER)
            .setPadding(4);
    }

    private Paragraph spacer(float pts) {
        return new Paragraph(" ").setFontSize(pts).setMargin(0).setPadding(0);
    }

    private DeviceRgb colorNota(double nota) {
        if (nota >= 90) return AZUL_INFO;
        if (nota >= 80) return VERDE;
        if (nota >= 70) return NARANJA;
        return ROJO;
    }

    private String condicion(double nota) {
        if (nota >= 90) return "Excelente";
        if (nota >= 80) return "Muy Bueno";
        if (nota >= 70) return "Bueno";
        if (nota >= 60) return "Regular";
        return "Deficiente";
    }
}
