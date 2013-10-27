package com.bpmco.tramitefacil.DTO;

/**
 * Created by jgonzalez on 20/10/13.
 */
public class ElementoOpcion {
    private Integer id;
    private String grupo;
    private String valor;
    private String texto;
    private Integer orden;

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getGrupo() {return grupo;}
    public void setGrupo(String grupo) {this.grupo = grupo;}
    public String getValor() {return valor;}
    public void setValor(String valor) {this.valor = valor;}
    public String getTexto() {return texto;}
    public void setTexto(String texto) {this.texto = texto;}
    public Integer getOrden() {return orden;}
    public void setOrden(Integer orden) {this.orden = orden;}

}
