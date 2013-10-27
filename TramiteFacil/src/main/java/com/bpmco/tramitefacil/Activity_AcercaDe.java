package com.bpmco.tramitefacil;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Activity_AcercaDe extends Activity implements View.OnClickListener{

    ImageButton btnAtras = null;
    ImageButton btnHome = null;
    Button btnInicio = null;
    Button btnMisPqr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acercade);

        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.textofooter2));

        btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnHome = (ImageButton)findViewById(R.id.btnHomeIco);
        btnInicio = (Button)findViewById(R.id.btnAcercaDe);
        btnMisPqr = (Button)findViewById(R.id.btnMisPqr);

        btnAtras.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnInicio.setOnClickListener(this);
        btnMisPqr.setOnClickListener(this);
        btnInicio.setText(this.getString(R.string.txt_inicio));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__acerca_de, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnAtras){
            finish();
        }

        if(view == btnHome){
            Intent i = new Intent(Activity_AcercaDe.this, Activity_listadoPqr.class);
            startActivity(i);
        }

        if(view == btnMisPqr){
            Intent i = new Intent(Activity_AcercaDe.this, Activity_MisPqr.class);
            startActivityForResult(i, 1);
        }

        if(view == btnInicio){
            Intent i = new Intent(Activity_AcercaDe.this, Activity_listadoPqr.class);
            startActivityForResult(i, 1);
        }
    }
}
