package com.Wsdl2Code.WebServices.pqrdService;

import android.content.Context;
import android.os.AsyncTask;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Elemento;
import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.Database.DatabaseHandler;
import com.bpmco.tramitefacil.R;
import com.emil.android.util.Connectivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jgonzalez on 18/10/13.
 */
public class Syncronize extends AsyncTask<Object, Void, Void> {
    DatabaseHandler manejador = null;
    OnAsyncResult onAsyncResult;

    @Override
    protected Void doInBackground(Object... params) {
        start((Context)params[0]);
        return null;
    }

    public interface OnAsyncResult {
        public abstract void onResultSuccess(int resultCode, String message);
        public abstract void onResultFail(int resultCode, String message);
    }

    public void setOnResultListener(OnAsyncResult onAsyncResult) {
        if (onAsyncResult != null) {
            this.onAsyncResult = onAsyncResult;
        }
    }

    protected void onProgressUpdate() {
    }

    protected void onPostExecute() {
    }

    private void start(Context context){
        manejador = DatabaseHandler.getInstance();
        Contexto contexto = manejador.getHandlerContexto().getContexto();

        List<Registro> registros = new ArrayList<Registro>();
        try{
            if(contexto.getLastSync() != null && contexto.getLastSync().after(new Date())){
                onAsyncResult.onResultFail(1
                        , context.getString(R.string.message_sync_error_mintime, contexto.getSyncMinutes()));
                return;
            }

            if(contexto.getConexionType().equals(Contexto.CONN_TYPE.WIFI.toString())
                    && !Connectivity.isConnectedWifi(context)){
                onAsyncResult.onResultFail(1
                        , context.getString(R.string.message_sync_error_nowifi));
                return;
            }

            if(contexto.getConexionType().equals(Contexto.CONN_TYPE.WIFI_MOVIL.toString())
                    && !Connectivity.isConnected(context)){
                onAsyncResult.onResultFail(1
                        , context.getString(R.string.message_sync_error_nomovil));
                return;
            }

            registros = manejador.getHandlerRegistro().getRegistros("1,2");

            if(registros == null || registros.size() == 0)
                return;

            int pqrRadicadas = 0;
            int pqrStatus = 0;

            Iterator<Registro> iterator = registros.iterator();
            while(iterator.hasNext()){
                Registro registro = iterator.next();
                String status = registro.getStatus();
                if(status == null)
                    status ="0";

                if(status.equals(Registro.Status.REGISTRADO.toString())){
                    registrarPqrAsync regPqrAsync = new registrarPqrAsync();
                    regPqrAsync.setRegId(registro.getId()+"");

                    regPqrAsync.setOnResultListener(registrarPqrAsyncResult);
                    regPqrAsync.execute();
                    pqrRadicadas++;
                }
                else if(status.equals(Registro.Status.RADICADO.toString())){
                    getRespuestasPQRDAsync respPQR = new getRespuestasPQRDAsync();
                    respPQR.setRegId(registro.getId()+"");
                    respPQR.setOnResultListener(getRespuestasPQRDAsyncResult);
                    respPQR.execute();
                    pqrStatus++;
                }
            }

            onAsyncResult.onResultSuccess(0, context.getString(R.string.message_sync, pqrRadicadas, pqrStatus));
            contexto.setLastSync(new Date());
            manejador.getHandlerContexto().guardarContexto(contexto);

        }catch (Exception e){
            //Toast.makeText(this, "No se Puede Abrir la Base de Datos", 2000).show();
            onAsyncResult.onResultFail(2, context.getString(R.string.message_sync_error, e.getMessage()));
            e.printStackTrace();
            return;
        }
    }

    registrarPqrAsync.OnAsyncResult registrarPqrAsyncResult
            = new registrarPqrAsync.OnAsyncResult() {
        @Override
        public void onResultSuccess(final int resultCode, final String message, final respuestaRegistroVO pqrResult) {
        }

        @Override
        public void onResultFail(final int resultCode, final String errorMessage) {
        }
    };

    getRespuestasPQRDAsync.OnAsyncResult getRespuestasPQRDAsyncResult
            = new getRespuestasPQRDAsync.OnAsyncResult() {
        @Override
        public void onResultSuccess(final int resultCode, final String message, final VectorrespuestaProcesoVO pqrResult) {
        }

        @Override
        public void onResultFail(final int resultCode, final String errorMessage) {
        }
    };
}
