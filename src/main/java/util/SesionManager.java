package util;
import model.Usuario;

public class SesionManager {
    private static SesionManager instance;
    private Usuario usuarioActual;

    private SesionManager (){}

    public static SesionManager getInstance(){
        if (instance==null) instance = new SesionManager();
        return instance;
    }

    public void IniciarSesion (Usuario usuario){
        this.usuarioActual =usuario;
    }

    public void CerrarSesion (){
        this.usuarioActual=null;
    }

    public Usuario getUsuarioActual(){
        return usuarioActual;
    }

    public boolean estaAutenticado(){
        return usuarioActual !=null;
    }

    public boolean esAdmin(){
        return estaAutenticado() && usuarioActual.esAdmin();
    }

    public boolean esMaestro(){
        return estaAutenticado() && usuarioActual.esMaestro();
    }


}
