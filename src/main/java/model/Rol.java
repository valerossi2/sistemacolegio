package model;

import jakarta .persistence.*;

@Entity
@Table (name = "roles")

public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false, unique = true, length = 30)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String descripcion;

    public Integer getId()          { return id; }
    public String getNombre()       { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public String getDescripcion()  { return descripcion; }
}
