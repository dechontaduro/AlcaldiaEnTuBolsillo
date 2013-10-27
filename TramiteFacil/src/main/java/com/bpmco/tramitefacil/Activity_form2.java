package com.bpmco.tramitefacil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Respuesta;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_form2 extends Activity implements View.OnClickListener{


    LinearLayout lytppal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_form2);

        DatabaseHandler handler = DatabaseHandler.getInstance();
        Contexto contexto = handler.getHandlerContexto().getContexto();

        lytppal = (LinearLayout)findViewById(R.id.lytppal);
        lytppal.setOnClickListener(this);

        String tramite = contexto.getTraId();

        if(tramite.equals("")){
            Intent i = new Intent(Activity_form2.this, Activity_listadoPqr.class);
            startActivityForResult(i, 1);
        }


        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.title_activity_activity_form2));

        Button botonSiguiente = (Button)findViewById(R.id.btnSiguiente);

        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
                Intent i = new Intent(Activity_form2.this, Activity_Adjuntar.class);
                startActivityForResult(i, 1);
            }
        });

        ImageButton btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton btnHome = (ImageButton)findViewById(R.id.btnHomeIco);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_form2.this, Activity_listadoPqr.class);
                startActivity(i);
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.repsips);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Spinner spinner = (Spinner)findViewById(R.id.spEPSIPS);
                ArrayList<String> spinnerArray = new ArrayList<String>();
                switch(checkedId)
                {
                    case R.id.eps:
                        spinnerArray = new ArrayList( Arrays.asList(getResources().getStringArray(R.array.EPS)));
                        ArrayAdapter<String> adapterBarrio = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);
                        spinner.setAdapter(adapterBarrio);
                        break;
                    case R.id.ips:
                        spinnerArray = new ArrayList( Arrays.asList(getResources().getStringArray(R.array.IPS)));
                        ArrayAdapter<String> adapterVereda = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);
                        spinner.setAdapter(adapterVereda);
                        break;
                }
            }
        });
    }


        @Override
        public void onClick(View view) {
            if(view == lytppal) {
                hideSoftKeyboard();
            }
        }


        private void hideSoftKeyboard() {
            InputMethodManager inputManager = (InputMethodManager) getBaseContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null)
                inputManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }

    public boolean guardarDatos()
    {

        RadioGroup rg = (RadioGroup)findViewById(R.id.repsips);
        int selected = rg.getCheckedRadioButtonId();
        RadioButton radioSelected = (RadioButton)findViewById(selected);


        Spinner spEntidad = (Spinner)findViewById(R.id.spEPSIPS);
        EditText txtNombreEntidad = (EditText)findViewById(R.id.txtNombreEntidadQueja);
        Spinner spTipoQueja = (Spinner)findViewById(R.id.spTipoQuejaSalud);
        EditText txtMotivo = (EditText)findViewById(R.id.txtMotivo);

        String motivo = txtMotivo.getText().toString();
        String tipoQueja = spTipoQueja.getSelectedItem().toString();
        String nombreEntidad = txtNombreEntidad.getText().toString();
        String stTipoEntidad = radioSelected.getText().toString();
        String entidad = spEntidad.getSelectedItem().toString();

        DatabaseHandler handler = DatabaseHandler.getInstance();
        Contexto contexto = handler.getHandlerContexto().getContexto();
        String idRegistro = contexto.getTraId();


        handler.getHandlerElementoRespuesta().checkPageData("10,11,12,13,18");

        Respuesta respuesta = new Respuesta();

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("10"));
        respuesta.setResValor(stTipoEntidad);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("11"));
        respuesta.setResValor(entidad);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("12"));
        respuesta.setResValor(nombreEntidad);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("13"));
        respuesta.setResValor(tipoQueja);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("18"));
        respuesta.setResValor(motivo);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_form2, menu);
        return true;


    }


}
