package model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "calificaciones")

public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_eval_id", nullable = false)
    private TipoEvaluacion tipoEvaluacion;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal nota;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate FechaRegistro;

    @Column(length = 300)
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrado_por", nullable = false)
    private Usuario registradoPor;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime CreadoEn;

    @PrePersist
    protected void OnCreate(){
        CreadoEn=LocalDateTime.now();
        if(FechaRegistro == null) FechaRegistro=LocalDate.now();
    }

    public Long getId()                             { return id; }
    public Matricula getMatricula()                 { return matricula; }
    public void setMatricula(Matricula m)           { this.matricula = m; }
    public Curso getCurso()                         { return curso; }
    public void setCurso(Curso c)                   { this.curso = c; }
    public TipoEvaluacion getTipoEvaluacion()       { return tipoEvaluacion; }
    public void setTipoEvaluacion(TipoEvaluacion t) { this.tipoEvaluacion = t; }
    public BigDecimal getNota()                     { return nota; }
    public void setNota(BigDecimal n)               { this.nota = n; }
    public String getObservacion()                  { return observacion; }
    public void setObservacion(String o)            { this.observacion = o; }
    public Usuario getRegistradoPor()               { return registradoPor; }
    public void setRegistradoPor(Usuario u)         { this.registradoPor = u; }
    public LocalDate getFechaRegistro()             { return FechaRegistro; }
    public void setFechaRegistro(LocalDate fecha)   { this.FechaRegistro = fecha; }
    public LocalDateTime getCreadoEn()              { return CreadoEn; }
}
