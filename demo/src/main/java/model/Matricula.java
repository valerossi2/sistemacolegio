package model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "matriculas")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private PeriodoAcademico periodo;

    @Column(name = "fecha_matricula", nullable = false)
    private LocalDate fechaMatricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMatricula estado = EstadoMatricula.ACTIVO;

    public enum EstadoMatricula { ACTIVO, RETIRADO, TRASLADADO }

    public Integer getId()                     { return id; }
    public Estudiante getEstudiante()          { return estudiante; }
    public void setEstudiante(Estudiante e)    { this.estudiante = e; }
    public Seccion getSeccion()                { return seccion; }
    public void setSeccion(Seccion s)          { this.seccion = s; }
    public PeriodoAcademico getPeriodo()       { return periodo; }
    public void setPeriodo(PeriodoAcademico p) { this.periodo = p; }
    public LocalDate getFechaMatricula()       { return fechaMatricula; }
    public void setFechaMatricula(LocalDate f) { this.fechaMatricula = f; }
    public EstadoMatricula getEstado()         { return estado; }
    public void setEstado(EstadoMatricula e)   { this.estado = e; }
}