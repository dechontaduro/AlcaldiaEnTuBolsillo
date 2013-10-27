package com.Wsdl2Code.WebServices.pqrdService;

import android.os.AsyncTask;

import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jgonzalez on 17/10/13.
 */
public class getRespuestasPQRDAsync extends AsyncTask<Void, Void, Void> {
    private DatabaseHandler manejador;
    private long radicado;
    private int vigencia;
    private String regId;
    private Registro reg;

    public String getRegId() {return regId;}
    public void setRegId(String regId) {this.regId = regId;}
    //public long getRadicado() {return radicado;}
    //public void setRadicado(long radicado) {this.radicado = radicado;    }
    //public int getVigencia() {return vigencia;}
    //public void setVigencia(int vigencia) {this.vigencia = vigencia;}

    public interface OnAsyncResult {
        public abstract void onResultSuccess(int resultCode, String message, VectorrespuestaProcesoVO pqrResult);
        public abstract void onResultFail(int resultCode, String errorMessage);
    }

    OnAsyncResult onAsyncResult;
    public void setOnResultListener(OnAsyncResult onAsyncResult) {
        if (onAsyncResult != null) {
            this.onAsyncResult = onAsyncResult;
        }
    }

    @Override
    protected Void  doInBackground(Void... params) {
        if (onAsyncResult != null) {
            pqrdService client = new pqrdService();

            try
            {
                manejador = DatabaseHandler.getInstance();
                reg =  manejador.getHandlerRegistro().getById(regId);

                VectorrespuestaProcesoVO pqrStatus = client.getRespuestasPQRD(Long.parseLong(reg.getRadicacion()), true, reg.getVigencia(), true);
                if(pqrStatus != null){
                    String respuesta[] = getRespuesta(pqrStatus);
                    reg.setRespuestaWS(respuesta[0]);
                    reg.setUltimoPaso(respuesta[1]);
                    reg.setUltimoEstado(respuesta[2]);
                    reg.setUltimaRespuesta(respuesta[3]);
                    reg.setUltimaFecha(respuesta[4]);

                    //TODO:strings
                    if(reg.getUltimoPaso().equals("750") && reg.getUltimoEstado().equals("Finalizado"))
                        reg.setStatus(Registro.Status.FINALIZADO.toString());

                    manejador.getHandlerRegistro().save(reg);
                    onAsyncResult.onResultSuccess(0, "pasos:" + pqrStatus.size(), pqrStatus);
                }
                else
                    onAsyncResult.onResultFail(1, "error");

            }
            catch (Exception e)
            {
                e.printStackTrace();
                onAsyncResult.onResultFail(2, "Error no definido");
            }
        }
        return null;
    }

    protected void onProgressUpdate() {
    }

    protected void onPostExecute() {
    }

    private String[] getRespuesta(VectorrespuestaProcesoVO pqrStatus) {
        String[] respuesta = {"", "", "", "", ""};
        Iterator<respuestaProcesoVO> iterator = pqrStatus.iterator();
        while(iterator.hasNext()){
            respuestaProcesoVO elemento = iterator.next();
            respuesta[0] += elemento.fecRespuesta + "-[" + elemento.descripcion + "]:" + elemento.texRespuesta + "\n\r";
            respuesta[1] = elemento.numPaso;
            respuesta[2] = elemento.estadoTramite;
            respuesta[3] = "[" + elemento.descripcion + "]:" + elemento.texRespuesta + "";
            respuesta[4] = elemento.fecRespuesta;
        }
        return respuesta;
    }
}
