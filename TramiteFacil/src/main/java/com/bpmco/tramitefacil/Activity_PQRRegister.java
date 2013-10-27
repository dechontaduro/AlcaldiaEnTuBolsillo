package com.bpmco.tramitefacil;

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
    public TextView txtRadicado;
    public WebView webTwitter;

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
            Registro reg = manejador.getHandlerRegistro().getById(contexto.getRegId());
            reg.setStatus(Registro.Status.REGISTRADO.toString());
            manejador.getHandlerRegistro().save(reg);
        }catch (Exception e){
            //TODO: strings
            Toast.makeText(this, "No se Puede Crear o Abrir la Base de Datos", 2000).show();
            e.printStackTrace();
            return;
        }

        prepareNavigationButtons();

        imgPQR = (ImageView)findViewById(R.id.imgPQR);
        txtResult = (TextView)findViewById(R.id.txtResult);
        txtRadicado = (TextView)findViewById(R.id.txtRadicado);
        webTwitter = (WebView)findViewById(R.id.webTwitter);

        imgPQR.setImageResource(R.drawable.pqr_wait);

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
            txtRadicado.setText(pqrResult.numRadicado + "-" + pqrResult.anno);
            //TODO: strings
            txtResult.setText("Se radico con exito la solicitud");
            viewTwitter();
        }
        else{
            txtResult.setText(message);
        }
    }

    private void sendPQR(){
        if(contexto.getConexionType().equals("WIFI") && !Connectivity.isConnectedWifi(this.getApplicationContext())){
            //TODO:strings
            viewPQRResponse("Conectese a una red WIFI para radicar la PQR", R.drawable.pqr_error, null);
            return;
        }

        if(contexto.getConexionType().equals("WIFI/MOVIL") && !Connectivity.isConnected(this.getApplicationContext())){
            //TODO:strings
            viewPQRResponse("Conectese a una red WIFI o active su conectividad movil para radicar la PQR", R.drawable.pqr_error, null);
            return;
        }

        txtResult.setText("Por favor espere mientras se radica la PQRD");

        regPqrAsync = new registrarPqrAsync();
        regPqrAsync.setRegId(contexto.getRegId());

        regPqrAsync.setOnResultListener(asynResult);
        regPqrAsync.execute();
    }

    private void viewTwitter(){
        webTwitter.setVisibility(View.VISIBLE);

        webTwitter.getSettings().setDomStorageEnabled(true);
        webTwitter.getSettings().setJavaScriptEnabled(true);

        webTwitter.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Toast.makeText(this, "Oh no! " + description, Toast.LENGTH_LONG).show();
            }
        });

        String summary =
                "<a href= \\\"https://twitter.com/share \\\" class= \\\"twitter-share-button \\\" data-text= \\\"Matrimonio Igualitario #senadoco #pl47_2012 http://www.imprenta.gov.co/gacetap/gaceta.mostrar_documento?p_tipo=05&amp;p_numero=47&amp;p_consec=33584 #sí Libertades \\\" data-via= \\\"votovisible \\\" data-lang= \\\"es \\\" data-size= \\\"large \\\" data-count= \\\"none \\\" data-dnt= \\\"true \\\">Twittear</a>\n" +
                "<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>\n";

        webTwitter.loadDataWithBaseURL("https://twitter.com", summary, "text/html", null, null);

    }
}
