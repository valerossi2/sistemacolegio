package model;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "horarios")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignacion_id", nullable = false)
    private AsignacionMaestro asignacion;

    @Column(name = "dia_semana", nullable = false)
    private Integer DiaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime HoraInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime HoraFin;

    @Column(length = 20)
    private String aula;

    public String getDiaNombre(){
        String[] dias ={"", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
        return dias[DiaSemana];
    }
    public Integer getId()                      { return id; }
    public AsignacionMaestro getAsignacion()    { return asignacion; }
    public void setAsignacion(AsignacionMaestro a){ this.asignacion = a; }
    public Integer getDiaSemana()               { return DiaSemana; }
    public void setDiaSemana(Integer d)         { this.DiaSemana = d; }
    public LocalTime getHoraInicio()            { return HoraInicio; }
    public void setHoraInicio(LocalTime h)      { this.HoraInicio = h; }
    public LocalTime getHoraFin()               { return HoraFin; }
    public void setHoraFin(LocalTime h)         { this.HoraFin = h; }
    public String getAula()                     { return aula; }
    public void setAula(String a)               { this.aula = a; }
}







