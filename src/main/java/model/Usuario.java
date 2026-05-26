package model;
import jakarta.persistence.*;
import java.time.LocalDateTime;


    @Entity
    @Table (name= "usuarios")

    public class Usuario {
        @Id
        @GeneratedValue (strategy = GenerationType.IDENTITY)
        public Integer id;

        @Column(name = "google_sub", unique = true)
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

        @PrePersist
        protected void onCreate(){
           creado_en= actualizado_en =LocalDateTime.now();
        }

        public Integer getId()                    { return id; }
        public String getGoogleSub()              { return googlesub; }
        public void   setGoogleSub(String s)      { this.googlesub = s; }
        public String getNombre()                 { return nombre; }
        public void   setNombre(String n)         { this.nombre = n; }
        public String getApellido()               { return apellido; }
        public void   setApellido(String a)       { this.apellido = a; }
        public String getNombreCompleto()         { return nombre + " " + apellido; }
        public String getEmail()                  { return email; }
        public void   setEmail(String e)          { this.email = e; }
        public String getPasswordHash()           { return password_hash; }
        public void   setPasswordHash(String ph)  { this.password_hash= ph; }
        public Rol    getRol()                    { return rol; }
        public void   setRol(Rol r)               { this.rol = r; }
        public String getFotoUrl()                { return FotoUrl; }
        public void   setFotoUrl(String u)        { this.FotoUrl = u; }
        public Boolean getActivo()                { return activo; }
        public void    setActivo(String a)       { this.activo = false; }
        public boolean esAdmin()   { return "ADMIN".equals(rol.getNombre()); }
        public boolean esMaestro() { return "MAESTRO".equals(rol.getNombre()); }
    }

