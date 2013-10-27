package com.Wsdl2Code.WebServices.pqrdService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.bpmco.tramitefacil.DTO.Ciudadano;
import com.bpmco.tramitefacil.DTO.Elemento;
import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.Database.DatabaseHandler;
import com.bpmco.tramitefacil.Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jgonzalez on 16/10/13.
 * http://smartphonebysachin.blogspot.com/2012/11/how-to-return-value-from-async-task-in.html
 */
public class registrarPqrAsync extends AsyncTask<Void, Void, Void> {
    private DatabaseHandler manejador;

    private String regId;
    private Registro reg;
    private Ciudadano ciu;
    private List<Elemento> listaElementos;

    private registroPQRDVO pqr;
    private respuestaRegistroVO pqrResp;
    private VectorByte foto;
    private VectorByte video;
    private archivoAdjuntoVO fotoA;
    private archivoAdjuntoVO videoA;

    public String getRegId() {return regId;}
    public void setRegId(String regId) {this.regId = regId;}

    @Override
    protected Void  doInBackground(Void... params) {
        if (onAsyncResult != null) {
            pqrdService client = new pqrdService();
            try
            {
                manejador = DatabaseHandler.getInstance();
                setPQR();

                pqrResp = client.registrarPqr(pqr, videoA, video, fotoA, foto);
                if(pqrResp != null && pqrResp.mensajeError.equals("OK"))
                {
                    reg.setRadicacion(pqrResp.numRadicado+"");
                    reg.setVigencia(pqrResp.anno);
                    reg.setStatus(Registro.Status.RADICADO.toString());
                    manejador.getHandlerRegistro().save(reg);

                    onAsyncResult.onResultSuccess(0, pqrResp.numRadicado + "-" + pqrResp.anno, pqrResp);
                }
                else
                    onAsyncResult.onResultFail(1, pqrResp.mensajeError);

            }
            catch (Exception e)
            {
                e.printStackTrace();
                onAsyncResult.onResultFail(2, "Error no definido");
            }
        }
        return null;
    }

    OnAsyncResult onAsyncResult;
    public void setOnResultListener(OnAsyncResult onAsyncResult) {
        if (onAsyncResult != null) {
            this.onAsyncResult = onAsyncResult;
        }
    }

    public interface OnAsyncResult {
        public abstract void onResultSuccess(int resultCode, String message, respuestaRegistroVO pqrResult);
        public abstract void onResultFail(int resultCode, String errorMessage);
    }

    protected void onProgressUpdate() {
    }

    protected void onPostExecute() {
    }

    private int setPQR(){
        pqr = new registroPQRDVO();
        reg =  manejador.getHandlerRegistro().getById(regId);
        ciu =  manejador.getHandlerCiudadano().getCiudadano(reg.getCiuId() + "");
        listaElementos =
                manejador.getHandlerElemento().getElementos("0", reg.getTraId()+"", reg.getId()+"");

        //TODO: Tomar el tipo de documento como código. Debe ajustarse la DB y el combo a Long
        pqr.tipoIdentificacion = 10;//ciu.getTipoDoc();
        //pqr.tipoIdentificacionSpecified = true;
        pqr.idCiudadano = ciu.getDocumento();
        //TODO: tomar la fecha del sistema y formatear
        pqr.fechaSolicitud = Utilidades.date2string(reg.getFecha(), "yyyy-MM-dd'T'HH:MM:ssZZZZZ");
        //pqr.fechaSolicitudSpecified = true;
        //TODO: En ninguna parte se pregunta el tipo de PQR
        pqr.tipoPQRD = "8";

        pqr.nombres = ciu.getNombres();
        pqr.apellidos = ciu.getApellidos();
        pqr.email = ciu.getEmail();
        pqr.telefonos = ciu.getTelefono() + " - " + ciu.getCelular();

        pqr.direccion = ciu.getDireccion();
        //TODO: En ninguna parte se pregunta la ciudad. Falta agregar a la DB y al DTO la ciudad
        pqr.ciudad = "Manizales";
        pqr.coordenadasGPS = reg.getGeo();// "5.055460801404837,-75.4915052"; //reg.getGeo();

        //TODO: tomar el lugar de los elementos
        pqr.lugarDireccion = getRespuestas("1,2,3", "", "", true);
        pqr.motivoSolicitud = getRespuestas("", "1,2,3", "", true);

        setFoto();
        setVideo();

        return 0;
    }

    private String getRespuestas(String incId, String excId, String type, boolean excNull) {
        if(!excId.equals(""))
            excId = "," + excId + ",";

        String motivo = "";

        Iterator<Elemento> iterator = listaElementos.iterator();
        while(iterator.hasNext()){
            Elemento elemento = iterator.next();

            if(!excId.equals("") && excId.contains("," + elemento.getId()+","))
                continue;

            if(!incId.equals("") && !incId.contains("," + elemento.getId()+","))
                continue;

            if(excNull && elemento.getRepuesta() == null)
                continue;

            if(!type.equals("") && !type.equals(elemento.getTipo()))
                continue;

            motivo += elemento.getEtiqueta() + ":" + elemento.getRepuesta();
        }
        return motivo;
    }

    private int setFoto(){
        foto = null;
        fotoA = null;

        String path = getRespuestas("", "", "f", true);
        if(path.equals(""))
            return 1;

        path = path.split(":")[1];
        foto = bytesToVectorBytes(path);
        return 0;
    }

    private int setVideo(){
        video = null;
        videoA = null;

        String path = getRespuestas("", "", "v", true);
        if(path.equals(""))
            return 1;

        path = path.split(":")[1];
        video = bytesToVectorBytes(path);
        return 0;
    }

    public VectorByte bytesToVectorBytes(String path){
        VectorByte result = null;
        try{
            InputStream is = new FileInputStream(path);
            if (path != null) {
                try {
                    byte[] fotoB = streamToBytes(is);
                    result = new VectorByte(fotoB);
                } finally {
                    is.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (java.io.IOException e) {
        }
        return os.toByteArray();
    }
}
