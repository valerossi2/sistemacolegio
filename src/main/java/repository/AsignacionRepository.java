package repository;

import config.Hibernate_config;
import model.AsignacionMaestro;
import model.Curso;
import model.Matricula;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AsignacionRepository {

    /** Cursos asignados a un maestro específico */
    public List<Curso> findCursosByMaestro(Integer maestroId) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "SELECT a.curso FROM AsignacionMaestro a " +
                                    "WHERE a.maestro.id = :maestroId " +
                                    "AND a.curso.activo = true " +
                                    "ORDER BY a.curso.periodo.fechaInicio DESC",
                            Curso.class)
                    .setParameter("maestroId", maestroId)
                    .list();
        }
    }

    /** Estudiantes matriculados en un curso */
    public List<Matricula> findMatriculasByCurso(Integer cursoId) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "FROM Matricula m " +
                                    "WHERE m.seccion.id = (" +
                                    "   SELECT c.seccion.id FROM Curso c WHERE c.id = :cursoId" +
                                    ") AND m.periodo.id = (" +
                                    "   SELECT c.periodo.id FROM Curso c WHERE c.id = :cursoId" +
                                    ") AND m.estado = 'ACTIVO' " +
                                    "ORDER BY m.estudiante.apellido",
                            Matricula.class)
                    .setParameter("cursoId", cursoId)
                    .list();
        }
    }

    public void save(AsignacionMaestro asignacion) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.merge(asignacion);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar asignación", e);
        }
    }

    public void delete(Integer maestroId, Integer cursoId) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.createMutationQuery(
                            "DELETE FROM AsignacionMaestro a " +
                                    "WHERE a.maestro.id = :maestroId AND a.curso.id = :cursoId")
                    .setParameter("maestroId", maestroId)
                    .setParameter("cursoId", cursoId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar asignación", e);
        }
    }
}
