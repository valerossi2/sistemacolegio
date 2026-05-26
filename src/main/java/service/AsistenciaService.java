package service;


import model.Asistencia;
import repository.AsistenciaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AsistenciaService {

    private final AsistenciaRepository repo = new AsistenciaRepository();

    /**
     * Registra la asistencia de todos los estudiantes de un curso en un día.
     */
    public void registrarAsistencia(List<Asistencia> registros) {
        if (registros.isEmpty()) return;
        repo.saveAll(registros);
    }

    /**
     * Retorna la asistencia de un curso en una fecha específica.
     * Se usa para cargar el panel del día en el calendario.
     */
    public List<Asistencia> getAsistenciaDia(Integer cursoId, LocalDate fecha) {
        return repo.findByCursoYFecha(cursoId, fecha);
    }

    /**
     * Retorna el resumen mensual de un curso.
     * Clave = fecha, Valor = lista de asistencias de ese día.
     * Se usa para pintar los puntos de colores en el calendario.
     */
    public Map<LocalDate, List<Asistencia>> getResumenMensual(
            Integer cursoId, int year, int month) {
        List<Asistencia> registros = repo.findByCursoYMes(cursoId, year, month);
        return registros.stream()
                .collect(Collectors.groupingBy(Asistencia::getFecha));
    }

    /**
     * Calcula el porcentaje de asistencia de un estudiante en un curso.
     * Fórmula: (presentes / total) × 100
     */
    public double getPorcentajeAsistencia(Integer matriculaId, Integer cursoId) {
        long total    = repo.countByMatriculaYCurso(matriculaId, cursoId);
        long presentes = repo.countPresentes(matriculaId, cursoId);
        if (total == 0) return 0.0;
        return Math.round((presentes * 100.0 / total) * 100.0) / 100.0;
    }

    /**
     * Retorna el color del punto según el estado de asistencia.
     * Se usa para pintar el calendario visualmente.
     */
    public String getColorEstado(Asistencia.EstadoAsistencia estado) {
        return switch (estado) {
            case PRESENTE    -> "#2e7d32"; // verde
            case AUSENTE     -> "#c62828"; // rojo
            case TARDANZA    -> "#f57f17"; // naranja
            case JUSTIFICADO -> "#1565c0"; // azul
        };
    }
}