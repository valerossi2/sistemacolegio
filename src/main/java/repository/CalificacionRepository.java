package repository;


import config.Hibernate_config;
import model.Calificacion;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CalificacionRepository {

    public List<Calificacion> findByCurso(Integer cursoId) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "FROM Calificacion c WHERE c.curso.id = :cursoId " +
                                    "ORDER BY c.matricula.estudiante.apellido",
                            Calificacion.class)
                    .setParameter("cursoId", cursoId)
                    .list();
        }
    }

    public List<Calificacion> findByMatriculaYCurso(Integer matriculaId, Integer cursoId) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "FROM Calificacion c " +
                                    "WHERE c.matricula.id = :matriculaId " +
                                    "AND c.curso.id = :cursoId",
                            Calificacion.class)
                    .setParameter("matriculaId", matriculaId)
                    .setParameter("cursoId", cursoId)
                    .list();
        }
    }

    public void save(Calificacion calificacion) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.merge(calificacion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar calificación", e);
        }
    }

    public void saveAll(List<Calificacion> calificaciones) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            calificaciones.forEach(s::merge);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar calificaciones", e);
        }
    }

    public void delete(Long id) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Calificacion c = s.get(Calificacion.class, id);
            if (c != null) s.remove(c);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar calificación", e);
        }
    }
}