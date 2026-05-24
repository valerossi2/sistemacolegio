package model;

import jakarta.persistence.*;

@Entity
@Table(name = "materias")

public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(nullable = false, unique = true, length = 15)
    private String codigo;

    public Integer getId()          { return id; }
    public String getNombre()       { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public String getCodigo()       { return codigo; }
    public void setCodigo(String c) { this.codigo = c; }
}
