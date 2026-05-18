package model;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "horarios")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


}
