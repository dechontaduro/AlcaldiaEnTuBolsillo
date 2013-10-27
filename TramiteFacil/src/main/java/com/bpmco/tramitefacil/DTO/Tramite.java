package com.bpmco.tramitefacil.DTO;

/**
 * Created by smarin on 3/10/13.
 */
public class Tramite {

    String traId;
    String entId;
    String traNombre;
    String traDescripcion;
    String traIdentidad;
    String traGeolocalizado;
    private int traStatus;

    public String getTraId() {
        return traId;
    }

    public void setTraId(String traId) {
        this.traId = traId;
    }

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getTraNombre() {
        return traNombre;
    }

    public void setTraNombre(String traNombre) {
        this.traNombre = traNombre;
    }

    public String getTraDescripcion() {
        return traDescripcion;
    }

    public void setTraDescripcion(String traDescripcion) {
        this.traDescripcion = traDescripcion;
    }

    public String getTraIdentidad() {
        return traIdentidad;
    }

    public void setTraIdentidad(String traIdentidad) {
        this.traIdentidad = traIdentidad;
    }

    public String getTraGeolocalizado() {
        return traGeolocalizado;
    }

    public void setTraGeolocalizado(String traGeolocalizado) {
        this.traGeolocalizado = traGeolocalizado;
    }


    public int getTraStatus() {
        return traStatus;
    }

    public void setTraStatus(int traStatus) {
        this.traStatus = traStatus;
    }
}
