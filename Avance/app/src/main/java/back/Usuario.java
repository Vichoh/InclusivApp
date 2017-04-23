package back;

import android.net.Uri;

/**
 * Created by Adrian on 22/04/2017.
 */

public class Usuario {

    private String nombre;
    private String correo;
    private Uri imagenPerfil;

    public Usuario() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Uri getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(Uri imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }
}
