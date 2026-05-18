package model;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "periodos_academicos")

public class PeriodoAcademico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false, length = 60)
    public String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPeriodo tipo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate FechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate FechaFin;

    @Column(nullable = false)
    private Boolean activo = true;

    public enum TipoPeriodo{
        anual, semestral, trimestral
    }

    public Boolean EstaAbierto(){
        return !LocalDate.now().isAfter(FechaFin);
    }


    public Integer getId ()    {return id;}
    public String getNombre()               { return nombre; }
    public void setNombre(String n)         { this.nombre = n; }
    public TipoPeriodo getTipo()            { return tipo; }
    public void setTipo(TipoPeriodo t)      { this.tipo = t; }
    public LocalDate getFechaInicio()       { return FechaInicio; }
    public void setFechaInicio(LocalDate f) { this.FechaInicio = f; }
    public LocalDate getFechaFin()          { return FechaFin; }
    public void setFechaFin(LocalDate f)    { this.FechaFin = f; }
    public Boolean getActivo()              { return activo; }
    public void setActivo(Boolean a)        { this.activo = a; }
}
