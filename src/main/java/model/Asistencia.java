package model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "asistencia")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsistencia estado;

    @Column(length = 200)
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por", nullable = false)
    private Usuario registradoPor;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime CreadoEn;

    @PrePersist
    protected void onCreate() {
        CreadoEn = LocalDateTime.now();
        if (fecha == null) fecha = LocalDate.now();
    }

    public enum EstadoAsistencia{
        PRESENTE, AUSENTE, TARDANZA, JUSTIFICADO
    }

    public Long getId()                       { return id; }
    public Matricula getMatricula()           { return matricula; }
    public void setMatricula(Matricula m)     { this.matricula = m; }
    public Curso getCurso()                   { return curso; }
    public void setCurso(Curso c)             { this.curso = c; }
    public LocalDate getFecha()               { return fecha; }
    public void setFecha(LocalDate f)         { this.fecha = f; }
    public EstadoAsistencia getEstado()       { return estado; }
    public void setEstado(EstadoAsistencia e) { this.estado = e; }
    public String getObservacion()            { return observacion; }
    public void setObservacion(String o)      { this.observacion = o; }
    public Usuario getRegistradoPor()         { return registradoPor; }
    public void setRegistradoPor(Usuario u)   { this.registradoPor = u; }
    public LocalDateTime getCreadoEn()        { return CreadoEn; }

}
