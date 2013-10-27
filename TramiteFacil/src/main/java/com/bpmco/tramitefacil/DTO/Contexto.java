package com.bpmco.tramitefacil.DTO;

import java.util.Date;

/**
 * Created by smarin on 10/10/13.
 */
public class Contexto {
    String ciuId;
    String traId;
    String regId;
    String elePagina;
    String maxPage;
    Date lastSync;
    String conexionType;
    Integer syncMinutes;
    String geo;

    public Contexto() {
        ciuId = "";
        traId = "";
        regId = "";
        elePagina = "";
        maxPage = "";
        lastSync = null;
        conexionType = "WIFI";
        syncMinutes = 5;
        geo = "WIFI";
    }
    public String getCiuId() {return ciuId;}
    public void setCiuId(String ciuId) {this.ciuId = ciuId;}
    public String getTraId() {return traId;}
    public void setTraId(String traId) {this.traId = traId;}
    public String getRegId() {return regId;}
    public void setRegId(String regId) {this.regId = regId;}
    public String getElePagina() {return elePagina;}
    public void setElePagina(String elePagina) {this.elePagina = elePagina;}
    public String getMaxPage() {return maxPage;}
    public void setMaxPage(String maxPage) {this.maxPage = maxPage;}
    public Date getLastSync() {return lastSync;}
    public void setLastSync(Date lastSync) {this.lastSync = lastSync;}
    public String getConexionType() {return conexionType;}
    public void setConexionType(String conexionType) {this.conexionType = conexionType;}
    public Integer getSyncMinutes() {return syncMinutes;}
    public void setSyncMinutes(Integer syncMinutes) {this.syncMinutes = syncMinutes;}
    public String getGeo() {return geo;}
    public void setGeo(String geo) {this.geo = geo;}

    public enum CONN_TYPE {
        WIFI("WIFI", 0)
        , WIFI_MOVIL("WIFI/MOVIL", 1)
        ;

        private String stringValue;
        private int intValue;
        private CONN_TYPE(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }
    }
}
