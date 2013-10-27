package com.bpmco.tramitefacil.DTO;

/**
 * Created by Cristian Cantillo on 26/09/13.
 */
public class botonesPqr
{
    private String nombre;
    private String id;

    public botonesPqr(String nombre, String id){
        this.setNombre(nombre);
        this.setId(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
