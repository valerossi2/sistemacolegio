package model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(name = "email_tutor", length = 150)
    private String emailTutor;

    @Column(name = "telefono_tutor", length = 20)
    private String telefonoTutor;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() { creadoEn = LocalDateTime.now(); }

    public enum Genero { M, F, OTRO }

    public String getNombreCompleto() { return nombre + " " + apellido; }

    public Integer getId()                      { return id; }
    public String getCodigo()                   { return codigo; }
    public void setCodigo(String c)             { this.codigo = c; }
    public String getNombre()                   { return nombre; }
    public void setNombre(String n)             { this.nombre = n; }
    public String getApellido()                 { return apellido; }
    public void setApellido(String a)           { this.apellido = a; }
    public LocalDate getFechaNacimiento()       { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate f) { this.fechaNacimiento = f; }
    public Genero getGenero()                   { return genero; }
    public void setGenero(Genero g)             { this.genero = g; }
    public String getEmailTutor()               { return emailTutor; }
    public void setEmailTutor(String e)         { this.emailTutor = e; }
    public String getTelefonoTutor()            { return telefonoTutor; }
    public void setTelefonoTutor(String t)      { this.telefonoTutor = t; }
    public Boolean getActivo()                  { return activo; }
    public void setActivo(Boolean a)            { this.activo = a; }
}