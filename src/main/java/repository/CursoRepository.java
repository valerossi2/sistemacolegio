package repository;

import config.Hibernate_config;
import model.Curso;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class CursoRepository {

    public List<Curso> findAll() {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Curso c WHERE c.activo = true ORDER BY c.periodo.fechaInicio DESC",
                    Curso.class).list();
        }
    }

    public List<Curso> findByPeriodo(Integer periodoId) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "FROM Curso c WHERE c.periodo.id = :periodoId AND c.activo = true",
                            Curso.class)
                    .setParameter("periodoId", periodoId)
                    .list();
        }
    }

    public Optional<Curso> findById(Integer id) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.get(Curso.class, id));
        }
    }

    public void save(Curso curso) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.merge(curso);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar curso", e);
        }
    }

    public void delete(Integer id) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Curso c = s.get(Curso.class, id);
            if (c != null) {
                c.setActivo(false); // soft delete
                s.merge(c);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar curso", e);
        }
    }
}