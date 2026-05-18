package model;
import jakarta.persistence.*;

@Entity
@Table (name = "secciones")

public class Seccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false, length = 10)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grado_id", nullable = false)
    private Grado grado;

    @Column(nullable = false)
    private Boolean activa = true;

    public Integer getId()  { return id;}
    public String getNombre()  {return nombre; }
    public Grado getGrado()  {return grado; }
    public Boolean getActiva() {return activa;}

}
