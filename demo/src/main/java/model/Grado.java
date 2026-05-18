package model;
import jakarta.persistence.*;

@Entity
@Table(name = "grados")

public class Grado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (nullable = false, length = 30)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nivel_id", nullable = false)
    private NivelEducativo nivel;

    @Column(nullable = false)
    private Integer orden;

    public Integer getId() { return id;}
    public String getNombre(){return nombre;}
    public NivelEducativo getNivel(){return nivel;}
    public Integer getOrden(){return orden;}
}
