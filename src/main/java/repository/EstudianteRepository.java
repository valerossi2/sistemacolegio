package repository;

import config.Hibernate_config;
import model.Estudiante;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class EstudianteRepository {

    public List<Estudiante> findAll() {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Estudiante e WHERE e.activo = true ORDER BY e.apellido",
                    Estudiante.class).list();
        }
    }

    public List<Estudiante> findBySeccionYPeriodo(Integer seccionId, Integer periodoId) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "SELECT m.estudiante FROM Matricula m " +
                                    "WHERE m.seccion.id = :seccionId " +
                                    "AND m.periodo.id = :periodoId " +
                                    "AND m.estado = 'ACTIVO' " +
                                    "ORDER BY m.estudiante.apellido",
                            Estudiante.class)
                    .setParameter("seccionId", seccionId)
                    .setParameter("periodoId", periodoId)
                    .list();
        }
    }

    public Optional<Estudiante> findById(Integer id) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.get(Estudiante.class, id));
        }
    }

    public Optional<Estudiante> findByCodigo(String codigo) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "FROM Estudiante e WHERE e.codigo = :codigo",
                            Estudiante.class)
                    .setParameter("codigo", codigo)
                    .uniqueResultOptional();
        }
    }

    public void save(Estudiante estudiante) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.merge(estudiante);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar estudiante", e);
        }
    }

    public void delete(Integer id) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Estudiante e = s.get(Estudiante.class, id);
            if (e != null) {
                e.setActivo(false); // soft delete
                s.merge(e);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar estudiante", e);
        }
    }
}