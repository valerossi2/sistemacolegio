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

    @Column(name = "ip_adress", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 512)
    private String userAgent;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_auth", nullable = false)
    private MetodoAuth metodoAuth;

    @Column(name = "creado_en", updatable = false)
    private LocalDate CreadoEn;


    public enum MetodoAuth { GOOGLE, LOCAL }

    @PrePersist
    protected void onCreate() { CreadoEn = LocalDate.now(); }

    public Long getId()                    { return id; }
    public Usuario getUsuario()            { return usuario; }
    public void setUsuario(Usuario u)      { this.usuario = u; }
    public String getIpAddress()           { return ipAddress; }
    public void setIpAddress(String ip)    { this.ipAddress = ip; }
    public String getUserAgent()           { return userAgent; }
    public void setUserAgent(String ua)    { this.userAgent = ua; }
    public MetodoAuth getMetodoAuth()      { return metodoAuth; }
    public void setMetodoAuth(MetodoAuth m){ this.metodoAuth = m; }
    public LocalDate getCreadoEn()     { return CreadoEn; }





}
