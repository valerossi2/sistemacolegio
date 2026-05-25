package repository;


import config.Hibernate_config;
import model.Asistencia;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class AsistenciaRepository {

    public List<Asistencia> findByCursoYFecha(Integer cursoId, LocalDate fecha) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "FROM Asistencia a " +
                                    "WHERE a.curso.id = :cursoId " +
                                    "AND a.fecha = :fecha " +
                                    "ORDER BY a.matricula.estudiante.apellido",
                            Asistencia.class)
                    .setParameter("cursoId", cursoId)
                    .setParameter("fecha", fecha)
                    .list();
        }
    }

    public List<Asistencia> findByCursoYMes(Integer cursoId, int year, int month) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            LocalDate inicio = LocalDate.of(year, month, 1);
            LocalDate fin    = inicio.withDayOfMonth(inicio.lengthOfMonth());
            return s.createQuery(
                            "FROM Asistencia a " +
                                    "WHERE a.curso.id = :cursoId " +
                                    "AND a.fecha BETWEEN :inicio AND :fin " +
                                    "ORDER BY a.fecha, a.matricula.estudiante.apellido",
                            Asistencia.class)
                    .setParameter("cursoId", cursoId)
                    .setParameter("inicio", inicio)
                    .setParameter("fin", fin)
                    .list();
        }
    }

    public long countByMatriculaYCurso(Integer matriculaId, Integer cursoId) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "SELECT COUNT(a) FROM Asistencia a " +
                                    "WHERE a.matricula.id = :matriculaId " +
                                    "AND a.curso.id = :cursoId",
                            Long.class)
                    .setParameter("matriculaId", matriculaId)
                    .setParameter("cursoId", cursoId)
                    .uniqueResult();
        }
    }

    public long countPresentes(Integer matriculaId, Integer cursoId) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "SELECT COUNT(a) FROM Asistencia a " +
                                    "WHERE a.matricula.id = :matriculaId " +
                                    "AND a.curso.id = :cursoId " +
                                    "AND a.estado = 'PRESENTE'",
                            Long.class)
                    .setParameter("matriculaId", matriculaId)
                    .setParameter("cursoId", cursoId)
                    .uniqueResult();
        }
    }

    public void saveAll(List<Asistencia> registros) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            registros.forEach(s::merge);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar asistencia", e);
        }
    }
}
