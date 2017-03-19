package back;

/**
 * Created by Adrian on 16/03/2017.
 */

public class Accesibilidad {


    private boolean estacionamientoDiscap;
    private boolean rampaAcceso;
    private boolean bandaAntidesliz;
    private boolean barraApoyo;

    public Accesibilidad(boolean estacionamientoDiscap, boolean rampaAcceso, boolean bandaAntidesliz, boolean barraApoyo) {
        this.estacionamientoDiscap = estacionamientoDiscap;
        this.rampaAcceso = rampaAcceso;
        this.bandaAntidesliz = bandaAntidesliz;
        this.barraApoyo = barraApoyo;
    }

    public boolean isEstacionamientoDiscap() {
        return estacionamientoDiscap;
    }

    public void setEstacionamientoDiscap(boolean estacionamientoDiscap) {
        this.estacionamientoDiscap = estacionamientoDiscap;
    }

    public boolean isRampaAcceso() {
        return rampaAcceso;
    }

    public void setRampaAcceso(boolean rampaAcceso) {
        this.rampaAcceso = rampaAcceso;
    }

    public boolean isBandaAntidesliz() {
        return bandaAntidesliz;
    }

    public void setBandaAntidesliz(boolean bandaAntidesliz) {
        this.bandaAntidesliz = bandaAntidesliz;
    }

    public boolean isBarraApoyo() {
        return barraApoyo;
    }

    public void setBarraApoyo(boolean barraApoyo) {
        this.barraApoyo = barraApoyo;
    }
}
