package com.alcaldia.pqrws;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.Wsdl2Code.WebServices.pqrdService.VectorByte;
import com.Wsdl2Code.WebServices.pqrdService.VectorrespuestaProcesoVO;
import com.Wsdl2Code.WebServices.pqrdService.archivoAdjuntoVO;
import com.Wsdl2Code.WebServices.pqrdService.pqrdService;
import com.Wsdl2Code.WebServices.pqrdService.registroPQRDVO;
import com.Wsdl2Code.WebServices.pqrdService.respuestaRegistroVO;
import com.Wsdl2Code.WebServices.pqrdService.respuestaProcesoVO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

/**
 * Created by jgonzalez on 3/10/13.
 */
public class alcaldiaWS  extends AsyncTask<URL, Integer, Long> {
    private long radicado = 0;
    private int vigencia = 0;

    private registroPQRDVO pqr = null;
    private byte[] foto = null;
    private byte[] video = null;

    public long getRadicado() {
        return radicado;
    }

    public void setRadicado(long radicado) {
        this.radicado = radicado;
    }

    public int getVigencia() {
        return vigencia;
    }

    public void setVigencia(int vigencia) {
        this.vigencia = vigencia;
    }

    public registroPQRDVO getPqr() {
        return pqr;
    }

    public void setPqr(registroPQRDVO pqr) {
        this.pqr = pqr;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    @Override
    protected Long doInBackground(URL... urls) {
        if(radicado != 0)
            getPQRStatus();
        else if(pqr != null)
            sendPQR();
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    }

    protected void sendPQR() {

        respuestaRegistroVO pqrResp = null;
        archivoAdjuntoVO fotoA = null;
        archivoAdjuntoVO videoA = null;

        VectorByte fotoByte = null;
        VectorByte videoByte = null;

        if(foto != null)
            fotoByte = new VectorByte(foto);
        if(video != null)
            videoByte = new VectorByte(video);

        pqrdService client = new pqrdService();

        try
        {
            pqrResp = client.registrarPqr(pqr, videoA, videoByte, fotoA, fotoByte);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void getPQRStatus() {
        VectorrespuestaProcesoVO pqrStatus = null;

        pqrdService client = new pqrdService();
        try
        {
            pqrStatus = client.getRespuestasPQRD(radicado, true, vigencia, true);
            if(pqrStatus != null)
            {
                long recs = pqrStatus.size();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}


