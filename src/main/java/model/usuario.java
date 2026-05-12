package model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

public class usuario {
    @Entity
    @Table (name= "usuarios")

    public class Usuario {
        @Id
        @GeneratedValue (strategy = GenerationType.IDENTITY)
        public Integer id;

        @Column(name = "google-sub", unique = true)
        private String googlesub;


        @Column(nullable = false, length = 100)
        private String nombre;


        @Column(nullable = false, length = 100)
        private String apellido;


        @Column(nullable = false, length = 100)
        private String email;


        @Column(name = "password_hash")
        private String password_hash;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "rol_id", nullable = false)
        private Rol rol;


        @Column(name = "foto_url")
        private String FotoUrl;

        @Column(nullable = false)
        private Boolean activo = true;

        @Column(name = "creado_en", updatable = false )
        private LocalDateTime creado_en;

        @Column(name = "Actualizado_en")
        private LocalDateTime actualizado_en;
    }
}
