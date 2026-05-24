package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccion_id", nullable = false)
    private Seccion seccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private PeriodoAcademico periodo;

    @Column(length = 200)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    protected void onCreate() { creadoEn = LocalDateTime.now(); }

    public String getNombreCompleto() {
        return materia.getNombre() + " – " +
                seccion.getNombre() + " " +
                seccion.getGrado().getNombre();
    }

    public boolean estaAbierto() { return periodo.EstaAbierto(); }

    public Integer getId()               { return id; }
    public Materia getMateria()          { return materia; }
    public void setMateria(Materia m)    { this.materia = m; }
    public Seccion getSeccion()          { return seccion; }
    public void setSeccion(Seccion s)    { this.seccion = s; }
    public PeriodoAcademico getPeriodo() { return periodo; }
    public void setPeriodo(PeriodoAcademico p){ this.periodo = p; }
    public String getDescripcion()       { return descripcion; }
    public void setDescripcion(String d) { this.descripcion = d; }
    public Boolean getActivo()           { return activo; }
    public void setActivo(Boolean a)     { this.activo = a; }
}
