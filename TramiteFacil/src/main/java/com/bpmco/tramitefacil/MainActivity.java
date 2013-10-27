package com.bpmco.tramitefacil;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.View;
import android.widget.Button;

import com.Wsdl2Code.WebServices.pqrdService.Syncronize;
import com.alcaldia.pqrws.alcaldiaWS;
import com.bpmco.tramitefacil.Database.DBHandlerCiudadano;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

public class MainActivity extends Activity implements View.OnClickListener{
    Button btnIngresar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnIngresar = (Button)findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(this);

        TRLocationListener locationListener = new TRLocationListener(this.getApplicationContext());

        Syncronize sync = new Syncronize();
        sync.start(this.getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnIngresar){
            Intent i = new Intent(MainActivity.this, Activity_listadoPqr.class);
            startActivity(i);
        }
    }
}
