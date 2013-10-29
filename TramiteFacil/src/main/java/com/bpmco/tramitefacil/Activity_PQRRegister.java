package com.bpmco.tramitefacil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.Wsdl2Code.WebServices.pqrdService.archivoAdjuntoVO;
import com.Wsdl2Code.WebServices.pqrdService.registrarPqrAsync;
import com.Wsdl2Code.WebServices.pqrdService.registroPQRDVO;
import com.Wsdl2Code.WebServices.pqrdService.respuestaRegistroVO;
import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.Database.DatabaseHandler;
import com.emil.android.util.Connectivity;

public class Activity_PQRRegister extends Activity implements View.OnClickListener{
    private DatabaseHandler manejador;

    public TextView txtActividadTitulo;
    public ImageButton btnAtras;
    public ImageButton btnHome;
    public Button btnMisPQR;
    public Button btnAcerca;

    public ImageView imgPQR;
    public TextView txtResult;

    public ImageButton btnShare;

    ProgressDialog progreso = null;

    private Contexto contexto;

    private registrarPqrAsync regPqrAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pqrregister);

        try{
            manejador = DatabaseHandler.getInstance();
            contexto = manejador.getHandlerContexto().getContexto();
        }catch (Exception e){
            //TODO: strings
            Toast.makeText(this, "No se Puede Crear o Abrir la Base de Datos", 2000).show();
            e.printStackTrace();
            return;
        }


        imgPQR = (ImageView)findViewById(R.id.imgPQR);
        txtResult = (TextView)findViewById(R.id.txtResult);
        btnShare = (ImageButton)findViewById(R.id.btnShare);

        prepareNavigationButtons();
        sendPQR();
    }

    private void prepareNavigationButtons() {
        txtActividadTitulo = (TextView)findViewById(R.id.txtcabecera);
        btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnHome = (ImageButton)findViewById(R.id.btnHomeIco);
        btnMisPQR = (Button)findViewById(R.id.btnMisPqr);
        btnAcerca = (Button)findViewById(R.id.btnAcercaDe);

        //TODO: strings
        txtActividadTitulo.setText(this.getString(R.string.title_activity_activity__pqrregister));
        btnAtras.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnMisPQR.setOnClickListener(this);
        btnAcerca.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__pqrregister, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnAtras){
            finish();
        }
        else if(view == btnHome){
            Intent i = new Intent(Activity_PQRRegister.this, Activity_listadoPqr.class);
            startActivity(i);
        }
        else if(view == btnMisPQR){
            Intent i = new Intent(Activity_PQRRegister.this, Activity_MisPqr.class);
            startActivity(i);
        }
        else if(view == btnAcerca){
            Intent i = new Intent(Activity_PQRRegister.this, Activity_AcercaDe.class);
            startActivity(i);
        }
        else if(view == btnShare){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, btnShare.getTag().toString());
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        }
    }

    registrarPqrAsync.OnAsyncResult asynResult = new registrarPqrAsync.OnAsyncResult() {
        @Override
        public void onResultSuccess(final int resultCode, final String message, final respuestaRegistroVO pqrResult) {
            runOnUiThread(new Runnable() {
                public void run() {
                viewPQRResponse(message, R.drawable.pqr_sucess, pqrResult);
                }
            });
        }

        @Override
        public void onResultFail(final int resultCode, final String errorMessage) {
            runOnUiThread(new Runnable() {
                public void run() {
                viewPQRResponse(errorMessage, R.drawable.pqr_error, null);
                }
            });
        }
    };

    private void viewPQRResponse(String message, int imgStatus, respuestaRegistroVO pqrResult) {
        contexto.setTraId(null);
        contexto.setCiuId(null);
        contexto.setElePagina(null);
        contexto.setMaxPage(null);
        contexto.setRegId(null);
        manejador.getHandlerContexto().guardarContexto(contexto);
        imgPQR.setImageResource(imgStatus);
        if(pqrResult != null){
            String msg = this.getString(R.string.message_radicado, pqrResult.numRadicado, pqrResult.anno);
            txtResult.setText(msg);

            msg = this.getString(R.string.message_share, pqrResult.numRadicado, pqrResult.anno);
            btnShare.setTag(msg);
            btnShare.setVisibility(View.VISIBLE);
            progreso.cancel();
        }
        else{
            String msg = this.getString(R.string.message_errorradicado, message);
            txtResult.setText(msg);
            progreso.cancel();
        }
    }

    private void sendPQR(){
        if(contexto.getConexionType().equals(Contexto.CONN_TYPE.WIFI.toString())
                && !Connectivity.isConnectedWifi(this.getApplicationContext())){
            viewPQRResponse(this.getString(R.string.message_error_nowifi), R.drawable.pqr_error, null);
            return;
        }

        if(contexto.getConexionType().equals(Contexto.CONN_TYPE.WIFI_MOVIL.toString())
                && !Connectivity.isConnected(this.getApplicationContext())){
            viewPQRResponse(this.getString(R.string.message_error_nomovil), R.drawable.pqr_error, null);
            return;
        }

        verCargando();

        regPqrAsync = new registrarPqrAsync();
        regPqrAsync.setRegId(contexto.getRegId());

        regPqrAsync.setOnResultListener(asynResult);
        regPqrAsync.execute();
    }

    public void verCargando(){
        progreso = new ProgressDialog(Activity_PQRRegister.this);
        progreso.setTitle(this.getString(R.string.title_progress_radicando));
        progreso.setMessage(this.getString(R.string.message_progress_radicando));
        progreso.setIndeterminate(true);
        progreso.setCancelable(true);
        progreso.show();
    }

}
