package model;
import jakarta.persistence.*;

@Entity
@Table(name = "niveles_educativos")

public class NivelEducativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    public Integer getId() { return id;}
    public String getNombre(){return nombre;}
}
