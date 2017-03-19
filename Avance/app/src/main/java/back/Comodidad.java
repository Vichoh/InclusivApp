package back;

/**
 * Created by Adrian on 16/03/2017.
 */

public class Comodidad {

    private String anchoPuerta;
    private boolean bañoDiscap;
    private String bandaAntideslizante;
    private String barraApoyo;
    private String espacioBaño;
    private String anchoPuertas;
    private boolean lavamanosAjustado;

    public Comodidad(String anchoPuerta, boolean bañoDiscap, String bandaAntideslizante, String barraApoyo, String espacioBaño, String anchoPuertas, boolean lavamanosAjustado) {
        this.anchoPuerta = anchoPuerta;
        this.bañoDiscap = bañoDiscap;
        this.bandaAntideslizante = bandaAntideslizante;
        this.barraApoyo = barraApoyo;
        this.espacioBaño = espacioBaño;
        this.anchoPuertas = anchoPuertas;
        this.lavamanosAjustado = lavamanosAjustado;
    }

    public String getAnchoPuerta() {
        return anchoPuerta;
    }

    public void setAnchoPuerta(String anchoPuerta) {
        this.anchoPuerta = anchoPuerta;
    }

    public boolean isBañoDiscap() {
        return bañoDiscap;
    }

    public void setBañoDiscap(boolean bañoDiscap) {
        this.bañoDiscap = bañoDiscap;
    }

    public String getBandaAntideslizante() {
        return bandaAntideslizante;
    }

    public void setBandaAntideslizante(String bandaAntideslizante) {
        this.bandaAntideslizante = bandaAntideslizante;
    }

    public String getBarraApoyo() {
        return barraApoyo;
    }

    public void setBarraApoyo(String barraApoyo) {
        this.barraApoyo = barraApoyo;
    }

    public String getEspacioBaño() {
        return espacioBaño;
    }

    public void setEspacioBaño(String espacioBaño) {
        this.espacioBaño = espacioBaño;
    }

    public String getAnchoPuertas() {
        return anchoPuertas;
    }

    public void setAnchoPuertas(String anchoPuertas) {
        this.anchoPuertas = anchoPuertas;
    }

    public boolean isLavamanosAjustado() {
        return lavamanosAjustado;
    }

    public void setLavamanosAjustado(boolean lavamanosAjustado) {
        this.lavamanosAjustado = lavamanosAjustado;
    }
}
