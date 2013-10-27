package com.bpmco.tramitefacil;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bpmco.tramitefacil.Adapter.adapterPqr;
import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Tramite;
import com.bpmco.tramitefacil.DTO.botonesPqr;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Activity_listadoPqr extends Activity implements View.OnClickListener{

    Button btnQdS = null;
    Button btnAcercaDe = null;
    Button btnMisPqr = null;
    ImageButton btnConfig = null;
    EditText buscador = null;
    ListView listaPqr = null;
    DatabaseHandler manejador = null;
    Contexto contexto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_prq);

        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.title_activity_activity_listado_pqr));

        try{
            manejador = DatabaseHandler.getInstance();
        }catch (Exception e){
            Toast.makeText(this, "No se Puede Crear o Abrir la Base de Datos", 2000).show();
            e.printStackTrace();
        }

        contexto = manejador.getHandlerContexto().getContexto();

        TextView lblFecha = (TextView)findViewById(R.id.lblFechaSinc);
        String ultimaSync = Utilidades.date2string(contexto.getLastSync(),"dd-MM-yy HH:mm" );
        lblFecha.setText(ultimaSync);

        ponerConexion();

        List<Tramite> listaTramites = manejador.getHandlerTramite().getTramites();


        listaPqr = (ListView)findViewById(R.id.lista);
        final adapterPqr adapter = new adapterPqr(Activity_listadoPqr.this, (ArrayList)listaTramites);

        listaPqr.setAdapter(adapter);
        buscador = (EditText)findViewById(R.id.txtFiltro);

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                adapter.getFilter().filter(s.toString());
                ViewGroup.LayoutParams params = listaPqr.getLayoutParams();
                if (start > 0) {
                    params.height = adapter.getCount() * 70;
                } else {
                    params.height = 350;
                }
                listaPqr.setLayoutParams(params);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnQdS = (Button)findViewById(R.id.btnSaber);
        btnAcercaDe = (Button)findViewById(R.id.btnAcercaDe);
        btnMisPqr = (Button)findViewById(R.id.btnMisPqr);
        btnConfig = (ImageButton)findViewById(R.id.btnConfig);

        btnQdS.setOnClickListener(this);
        btnAcercaDe.setOnClickListener(this);
        btnMisPqr.setOnClickListener(this);
        btnConfig.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_listado_pqr, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnQdS){
            Intent i = new Intent(Activity_listadoPqr.this, Activity_qdSaber.class);
            //Intent i = new Intent(Activity_listadoPqr.this, Activity_Adjuntar.class);
            startActivity(i);
        }

        if(view == btnAcercaDe){
            Intent i = new Intent(Activity_listadoPqr.this, Activity_AcercaDe.class);
            startActivity(i);
        }

        if(view == btnMisPqr){
            Intent i = new Intent(Activity_listadoPqr.this, Activity_MisPqr.class);
            startActivity(i);
        }

        if(view == btnConfig){
            Intent i = new Intent(Activity_listadoPqr.this, Activity_Config.class);
            startActivity(i);
        }
    }

    public void ponerConexion(){
        TestConexion conexion = new TestConexion();
        if(conexion.tieneConectividad(this)){
            Toast.makeText(this, "Tiene Conexion: " + conexion.getTipoConexion(), Toast.LENGTH_LONG).show();
            cambiarIconoConexion(conexion);
        }else{
            Toast.makeText(this, "No Tiene Conexion", Toast.LENGTH_LONG).show();
        }
    }

    public void cambiarIconoConexion(TestConexion conexion){
        ImageView wifi = (ImageView)findViewById(R.id.icoWifi);
        ImageView datos = (ImageView)findViewById(R.id.icoDatos);
        ImageView config = (ImageView)findViewById(R.id.btnConfig);

        if(conexion.esWifi()){
            if(!contexto.getConexionType().equals("")){
                wifi.setImageResource(R.drawable.wifipred);
            }else{
                wifi.setImageResource(R.drawable.wifiok);
                config.setImageResource(R.drawable.configuracion);
            }
        }

        if(conexion.esMobil()){
            if(!contexto.getConexionType().equals("")){
                datos.setImageResource(R.drawable.datospredpng);
            }else{
                datos.setImageResource(R.drawable.datosok);
                config.setImageResource(R.drawable.configuracion);
            }
        }
    }
}
