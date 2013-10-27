package com.Wsdl2Code.WebServices.pqrdService;

import android.content.Context;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Elemento;
import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.Database.DatabaseHandler;
import com.emil.android.util.Connectivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jgonzalez on 18/10/13.
 */
public class Syncronize {
    DatabaseHandler manejador = null;

    public void start(Context context){
        manejador = DatabaseHandler.getInstance();
        Contexto contexto = manejador.getHandlerContexto().getContexto();

        List<Registro> registros = new ArrayList<Registro>();
        try{
            if(contexto.getLastSync() != null && contexto.getLastSync().after(new Date()))
                return;

            if(contexto.getConexionType() == Contexto.CONN_TYPE.WIFI.toString() && !Connectivity.isConnectedWifi(context))
                return;

            if(contexto.getConexionType() == Contexto.CONN_TYPE.WIFI_MOVIL.toString() && !Connectivity.isConnected(context))
                return;

            registros = manejador.getHandlerRegistro().getRegistros();

            if(registros == null || registros.size() == 0)
                return;

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
                }
                else if(status.equals(Registro.Status.RADICADO.toString())){
                    getRespuestasPQRDAsync respPQR = new getRespuestasPQRDAsync();
                    respPQR.setRegId(registro.getId()+"");
                    respPQR.setOnResultListener(getRespuestasPQRDAsyncResult);
                    respPQR.execute();
                }
            }

        }catch (Exception e){
            //Toast.makeText(this, "No se Puede Abrir la Base de Datos", 2000).show();
            e.printStackTrace();
            return;
        }

        contexto.setLastSync(new Date());
        manejador.getHandlerContexto().guardarContexto(contexto);
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
