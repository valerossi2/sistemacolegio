package service;

import model.Calificacion;
import model.Curso;
import repository.CalificacionRepository;

import java.time.LocalDate;
import java.util.List;

public class CalificationService {
    private final CalificacionRepository repo = new CalificacionRepository();

    public boolean periodoEstaAbierto (Curso curso){
        return curso.estaAbierto();
    }

    public void registrarNota(Calificacion calificacion){
        if (!periodoEstaAbierto(calificacion.getCurso())) {
            throw new PeriodoCerradoException(
                    "El periodo a finalizado. Las notas son solo de lectura"
            );
        }
        calificacion.setFechaRegistro(LocalDate.now());
        repo.save(calificacion);
    }

    public void registrarNotas(List<Calificacion> calificaciones){
        if (calificaciones.isEmpty()) return;

        Curso curso = calificaciones.get(0).getCurso();
        if(!periodoEstaAbierto(curso)){
            throw new PeriodoCerradoException(
                    "El periodo a finalizado. Las notas son solo de lectura"
            );
        }
        calificaciones.forEach(c -> c.setFechaRegistro(LocalDate.now()));
        repo.saveAll(calificaciones);
    }
    public double CalcularPromedioPonderado (Integer matriculaId, Integer cursoId){
        List<Calificacion> notas = repo.findByMatriculaYCurso(matriculaId,cursoId);
        if (notas.isEmpty()) return 0;

        double sumaNota=0;
        double sumaPesos=0;

        for (Calificacion c : notas){
            double peso = c.getTipoEvaluacion().getPeso().doubleValue();
            sumaNota += c.getNota().doubleValue() * peso;
            sumaPesos += peso;
        }
        return sumaPesos == 0 ? 0.0 : Math.round((sumaNota / sumaPesos) * 100.0) / 100.0;
    }
    public List<Calificacion> getCalificacionesPorCurso(Integer cursoId){
        return repo.findByCurso(cursoId);
    }
    public List<Calificacion> getCalificacionesPorMatricula(Integer matriculaId, Integer cursoId){
        return repo.findByMatriculaYCurso(matriculaId, cursoId);
    }

    public static class PeriodoCerradoException extends RuntimeException {
        public PeriodoCerradoException(String msg) { super(msg); }
    }


}

