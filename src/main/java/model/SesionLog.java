package model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sesiones_log")
public class SesionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;







}
