package repository;

import config.Hibernate_config;
import model.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class UsuarioRepositry {

    public Optional<Usuario> findByEmail(String email){
        try (Session s= Hibernate_config.getSessionFactory().openSession()){
            return s.createQuery(
                    "FROM usuario u WHERE u.email = :email AND u.activo = 'activo'",
                    Usuario.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        }
    }

    public Optional<Usuario> findByGoogleSub(String googleSub) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                            "FROM Usuario u WHERE u.googleSub = :sub AND u.activo = 'activo'",
                     Usuario.class)
                    .setParameter("sub", googleSub)
                    .uniqueResultOptional();
        }
    }


    public List<Usuario> findAllMaestros() {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Usuario u WHERE u.rol.nombre = 'MAESTRO' AND u.activo = 'activo' ORDER BY u.apellido",
                    Usuario.class).list();
        }
    }

    public List<Usuario> findAll() {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return s.createQuery(
                    "FROM Usuario u ORDER BY u.apellido",
                    Usuario.class).list();
        }
    }

    public Optional<Usuario> findById(Integer id) {
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.get(Usuario.class, id));
        }
    }

    public void save(Usuario usuario) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            s.merge(usuario);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }

    public void delete(Integer id) {
        Transaction tx = null;
        try (Session s = Hibernate_config.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Usuario u = s.get(Usuario.class, id);
            if (u != null) {
                u.setActivo("suspendido"); // soft delete
                s.merge(u);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }
}
