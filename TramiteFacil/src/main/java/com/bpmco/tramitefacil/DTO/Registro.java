package com.bpmco.tramitefacil.DTO;

import java.util.Date;

/**
 * Created by smarin on 10/10/13.
 */
public class Registro {

    Integer id;
    Integer traId;
    Integer ciuId;
    Date fecha;
    String radicacion;
    Integer vigencia;
    String info;
    String status;
    String tipo;
    String geo;
    String respuestaWS;

    String ultimoPaso;
    String ultimoEstado;
    String ultimaRespuesta;
    String ultimaFecha;


    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public Integer getTraId() {return traId;}
    public void setTraId(Integer traId) {this.traId = traId;}
    public Integer getCiuId() {return ciuId;}
    public void setCiuId(Integer ciuId) {this.ciuId = ciuId;}
    public Date getFecha() {return fecha;}
    public void setFecha(Date fecha) {this.fecha = fecha;}
    public String getRadicacion() {return radicacion;}
    public void setRadicacion(String radicacion) {this.radicacion = radicacion;}
    public Integer getVigencia() {return vigencia;}
    public void setVigencia(Integer vigencia) {this.vigencia = vigencia;}
    public String getInfo() {return info;}
    public void setInfo(String info) {this.info = info;}
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public String getGeo() {return geo;}
    public void setGeo(String geo) {this.geo = geo;}
    public String getRespuestaWS() {return respuestaWS;}
    public void setRespuestaWS(String respuestaWS) {this.respuestaWS = respuestaWS;}

    public String getUltimoPaso() {return ultimoPaso;}
    public void setUltimoPaso(String ultimoPaso) {this.ultimoPaso = ultimoPaso;}
    public String getUltimoEstado() {return ultimoEstado;}
    public void setUltimoEstado(String ultimoEstado) {this.ultimoEstado = ultimoEstado;}
    public String getUltimaRespuesta() {return ultimaRespuesta;}
    public void setUltimaRespuesta(String ultimaRespuesta) {this.ultimaRespuesta = ultimaRespuesta;}
    public String getUltimaFecha() {return ultimaFecha;}
    public void setUltimaFecha(String ultimaFecha) {this.ultimaFecha = ultimaFecha;}


    //0: En registro, 1: Registrado, 2:Radicado, 3: Finalizado
    public enum Status {
        PENDIENTE("0", 0)
        , REGISTRADO("1", 1)
        , RADICADO("2", 2)
        , FINALIZADO("3", 3)
        ;

        private String stringValue;
        private int intValue;
        private Status(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }
}
