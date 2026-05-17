package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asignaciones_maestro")
public class AsignacionMaestro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maestro_id", nullable = false)
    private Usuario maestro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "asignado_en", updatable = false)
    private LocalDateTime asignadoEn;

    @PrePersist
    protected void onCreate() { asignadoEn = LocalDateTime.now(); }

    public Integer getId()             { return id; }
    public Usuario getMaestro()        { return maestro; }
    public void setMaestro(Usuario m)  { this.maestro = m; }
    public Curso getCurso()            { return curso; }
    public void setCurso(Curso c)      { this.curso = c; }
    public LocalDateTime getAsignadoEn(){ return asignadoEn; }
}