package com.bpmco.tramitefacil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Preview_Foto extends Activity implements View.OnClickListener{

    ImageButton btnAtras = null;
    ImageButton btnHome = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_foto);

        Bundle bundle = getIntent().getExtras();

        String ruta = bundle.getString("ruta");
        String[] arrRuta = ruta.split("/");

        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(arrRuta[arrRuta.length - 1]);

        Bitmap foto = BitmapFactory.decodeFile(ruta);

        ImageView prev = (ImageView)findViewById(R.id.imgPrev);
        prev.setImageBitmap(foto);

        btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnHome = (ImageButton)findViewById(R.id.btnHomeIco);

        btnAtras.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__preview__foto, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnAtras){
            finish();
        }

        if(view == btnHome){
            Intent i = new Intent(Activity_Preview_Foto.this, Activity_listadoPqr.class);
            startActivity(i);
        }
    }
}
