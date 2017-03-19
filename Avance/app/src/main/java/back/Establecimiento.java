package back;

/**
 * Created by Adrian on 14/03/2017.
 */

public class Establecimiento {


    private String nombre;
    private String calle;
    private int numero;
    private String ciudad;
    private String categoria;
    private String telefono;
    private String sitioWeb;
    private String email;
    private double longitud;
    private double latitud;
    private String nota;
    private String valoracion;

    public Establecimiento(String valoracion, String calle, int numero, String ciudad, String categoria, String telefono, String sitioWeb, String email, double longitud, double latitud, String nota, String nombre) {
        this.valoracion = valoracion;
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.categoria = categoria;
        this.telefono = telefono;
        this.sitioWeb = sitioWeb;
        this.email = email;
        this.longitud = longitud;
        this.latitud = latitud;
        this.nota = nota;
        this.nombre = nombre;
    }

    public Establecimiento() {
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
}
