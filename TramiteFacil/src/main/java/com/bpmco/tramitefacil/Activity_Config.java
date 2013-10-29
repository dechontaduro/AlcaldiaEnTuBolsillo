package com.bpmco.tramitefacil;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

public class Activity_Config extends Activity implements View.OnClickListener {
    private DatabaseHandler manejador;
    private Contexto contexto;

    public TextView txtActividadTitulo;
    public ImageButton btnAtras;
    public ImageButton btnHome;
    public Button btnMisPQR;
    public Button btnAcerca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        prepareNavigationButtons();

        try{
            manejador = DatabaseHandler.getInstance();
            contexto = manejador.getHandlerContexto().getContexto();

            RadioButton rbWIFI = (RadioButton) findViewById(R.id.conn_wifi);
            RadioButton rbMovil = (RadioButton) findViewById(R.id.conn_movil);

            if(contexto.getConexionType().equals(Contexto.CONN_TYPE.WIFI.toString()))
                rbWIFI.setChecked(true);
            else
                rbMovil.setChecked(true);

            RadioGroup rgConn = (RadioGroup) findViewById(R.id.conectividad);
            rgConn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    switch(checkedId)
                    {
                        case R.id.conn_wifi:
                            contexto.setConexionType(Contexto.CONN_TYPE.WIFI.toString());
                            break;
                        case R.id.conn_movil:
                            contexto.setConexionType(Contexto.CONN_TYPE.WIFI_MOVIL.toString());
                            break;
                    }
                    manejador.getHandlerContexto().guardarContexto(contexto);
                    Toast.makeText(getApplicationContext(),getString(R.string.msj_conf_conexion), 2000).show();
                }
            });

        }catch (Exception e){
            //TODO: strings
            Toast.makeText(this, "No se Puede Crear o Abrir la Base de Datos", 2000).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity__config, menu);
        return true;
    }

    private void prepareNavigationButtons() {
        txtActividadTitulo = (TextView)findViewById(R.id.txtcabecera);
        btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnHome = (ImageButton)findViewById(R.id.btnHomeIco);
        btnMisPQR = (Button)findViewById(R.id.btnMisPqr);
        btnAcerca = (Button)findViewById(R.id.btnAcercaDe);

        //TODO: strings
        txtActividadTitulo.setText(getString(R.string.title_activity_activity__config));
        btnAtras.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnMisPQR.setOnClickListener(this);
        btnAcerca.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnAtras || view == btnHome){
            finish();
        }

        if(view == btnMisPQR){
            Intent i = new Intent(Activity_Config.this, Activity_MisPqr.class);
            startActivity(i);
        }

        if(view == btnAcerca){
            Intent i = new Intent(Activity_Config.this, Activity_AcercaDe.class);
            startActivity(i);
        }
    }
    
}
