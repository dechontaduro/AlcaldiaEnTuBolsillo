package com.bpmco.tramitefacil;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_qdSaber extends Activity implements View.OnClickListener{

    ImageButton btnAtras = null;
    ImageButton btnHome = null;
    Button btnRegistrar = null;
    Button btnMisPqr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qdsaber);
        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.titulo_qdSaber));

        btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnHome = (ImageButton)findViewById(R.id.btnHomeIco);
        btnAtras.setOnClickListener(this);
        btnHome.setOnClickListener(this);

        btnRegistrar = (Button)findViewById(R.id.btnAcercaDe);
        btnMisPqr = (Button)findViewById(R.id.btnMisPqr);

        btnRegistrar.setOnClickListener(this);
        btnRegistrar.setText(this.getString(R.string.txt_boton_registrar));
        btnMisPqr.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_qd_saber, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnAtras || view == btnHome){
            finish();
        }

        if(view == btnRegistrar){
            Intent i = new Intent(Activity_qdSaber.this, Activity_DatosPersonales.class);
            startActivityForResult(i, 1);
        }

        if(view == btnMisPqr){
            Intent i = new Intent(Activity_qdSaber.this, Activity_MisPqr.class);
            startActivityForResult(i, 1);
        }
    }
}
