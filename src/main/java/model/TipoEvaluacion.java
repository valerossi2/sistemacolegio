package model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tipos_evaluacion")
public class TipoEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 40)
    private String nombre;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal peso;

    public Integer getId()            { return id; }
    public String getNombre()         { return nombre; }
    public void setNombre(String n)   { this.nombre = n; }
    public BigDecimal getPeso()       { return peso; }
    public void setPeso(BigDecimal p) { this.peso = p; }
}


