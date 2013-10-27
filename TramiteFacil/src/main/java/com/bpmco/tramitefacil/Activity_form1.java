package com.bpmco.tramitefacil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.DTO.Respuesta;
import com.bpmco.tramitefacil.Database.DatabaseHandler;
import com.bpmco.tramitefacil.Database.Validation;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_form1 extends Activity implements View.OnClickListener {


    LinearLayout lytppal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_form1);


        lytppal = (LinearLayout)findViewById(R.id.lytppal);
        lytppal.setOnClickListener(this);

        DatabaseHandler handler = DatabaseHandler.getInstance();
        Contexto contexto = handler.getHandlerContexto().getContexto();

        String tramite = contexto.getTraId();

        if(tramite == null || tramite.equals("")){
            Intent i = new Intent(Activity_form1.this, Activity_listadoPqr.class);
            startActivityForResult(i, 1);
        }

        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.title_activity_activity_form1));

        RadioGroup rg = (RadioGroup) findViewById(R.id.rbarriovereda);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Spinner spinner = (Spinner)findViewById(R.id.spbarriosvereda);
                ArrayList<String> spinnerArray = new ArrayList<String>();
                switch(checkedId)
                {
                    case R.id.barrio:
                        spinnerArray = new ArrayList( Arrays.asList(getResources().getStringArray(R.array.barrios)));
                        ArrayAdapter<String> adapterBarrio = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);
                        spinner.setAdapter(adapterBarrio);
                        break;
                    case R.id.vereda:
                        spinnerArray = new ArrayList( Arrays.asList(getResources().getStringArray(R.array.veredas)));
                        ArrayAdapter<String> adapterVereda = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);
                        spinner.setAdapter(adapterVereda);
                        break;
                }
            }
        });

        Button botonSiguiente = (Button)findViewById(R.id.btnSiguiente);
        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(guardarDatos())
                {
                    Intent i = new Intent(Activity_form1.this, Activity_form2.class);
                    startActivityForResult(i, 1);
                }
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
                Intent i = new Intent(Activity_form1.this, Activity_listadoPqr.class);
                startActivity(i);
            }
        });


        EditText txtEdadAnio = (EditText)findViewById(R.id.txtEdadAnio);
        txtEdadAnio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                EditText text = (EditText)findViewById(R.id.txtEdadAnio);

                if(text.getText().toString().equals("")){
                    return;
                }

                Integer anios = Integer.parseInt(text.getText().toString());

                EditText EditTextMes = (EditText)findViewById(R.id.txtEdadMeses);
                TextView textMes = (TextView)findViewById(R.id.textMes);

                if(!b)
                {
                    if(anios < 2)
                    {
                        EditTextMes.setVisibility(View.VISIBLE);
                        textMes.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        EditTextMes.setVisibility(View.GONE);
                        textMes.setVisibility(View.GONE);
                    }
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

        EditText txtLugar = (EditText)findViewById(R.id.txtlugar);

        RadioGroup rg = (RadioGroup)findViewById(R.id.rbarriovereda);
        int selected = rg.getCheckedRadioButtonId();
        RadioButton radioSelected = (RadioButton)findViewById(selected);

        Spinner spRadioVereda = (Spinner)findViewById(R.id.spbarriosvereda);
        Spinner spTipoDoc = (Spinner)findViewById(R.id.spTipoDoc);

        EditText txtIdenti = (EditText)findViewById(R.id.txtIdenti);

        EditText txtNombreAfectado = (EditText)findViewById(R.id.txtNombreAfectado);
        EditText txtEdadAnio = (EditText)findViewById(R.id.txtEdadAnio);

        String edadMeses = "0";

        if(Integer.parseInt(txtEdadAnio.getText().toString()) < 2)
        {
            EditText txtEdadMeses = (EditText)findViewById(R.id.txtEdadMeses);
            edadMeses = txtEdadMeses.getText().toString();
        }

        Spinner type = (Spinner)findViewById(R.id.tipo);
        String edadAnio = txtEdadAnio.getText().toString();
        String nombreAfectado = txtNombreAfectado.getText().toString();
        String identificacion = txtIdenti.getText().toString();
        String sTipoDoc = spTipoDoc.getSelectedItem().toString();
        String sbarrioVereda = spRadioVereda.getSelectedItem().toString();
        String rbarrioVereda = radioSelected.getText().toString();
        String lugarProblema = txtLugar.getText().toString();
        String tipo = type.getSelectedItem().toString();


        if(edadMeses.equals("") || edadAnio.equals("") || nombreAfectado.equals("") || identificacion.equals("")
      || sTipoDoc.equals("") || sbarrioVereda.equals("") || rbarrioVereda.equals("") || lugarProblema.equals("")
      || tipo.equals("")){

            Toast.makeText(this, "Llene todos los datos faltantes", Toast.LENGTH_LONG).show();
            return false;
        }


        if(Integer.parseInt(edadAnio) > 120){
            Toast.makeText(this, "La edad no puede ser Mayor a 120", Toast.LENGTH_LONG).show();
            return false;
        }


        Validation validacion = new Validation();

        DatabaseHandler handler = DatabaseHandler.getInstance();
        Contexto contexto = handler.getHandlerContexto().getContexto();

        handler.getHandlerElementoRespuesta().checkPageData("1,2,3,4,5,6,7,8,9,19");

        Registro registro = new Registro();
        registro.setTraId(Integer.parseInt(contexto.getTraId()));
        registro.setCiuId(Integer.parseInt(contexto.getCiuId()));

        String idRegistro = contexto.getRegId();

        if(idRegistro == null || idRegistro.equals("")){
            idRegistro = handler.getHandlerRegistro().createRegistro(registro);//Crear Registro
            contexto.setRegId(idRegistro);
            handler.getHandlerContexto().guardarContexto(contexto);
        }

        Respuesta respuesta = new Respuesta();

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("1"));
        respuesta.setResValor(lugarProblema);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("2"));
        respuesta.setResValor(rbarrioVereda);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("3"));
        respuesta.setResValor(sbarrioVereda);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("4"));
        respuesta.setResValor(edadAnio);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);


        if(!edadMeses.equals("0"))
        {
            respuesta.setRegId(idRegistro);
            respuesta.setEleId(String.valueOf("5"));
            respuesta.setResValor(edadMeses);

            handler.getHandlerElementoRespuesta().createRespuesta(respuesta);
        }

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("7"));
        respuesta.setResValor(sTipoDoc);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("8"));
        respuesta.setResValor(identificacion);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("9"));
        respuesta.setResValor(nombreAfectado);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);


        respuesta.setRegId(idRegistro);
        respuesta.setEleId(String.valueOf("20"));
        respuesta.setResValor(nombreAfectado);

        handler.getHandlerElementoRespuesta().createRespuesta(respuesta);

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_form1, menu);
        return true;
    }
    
}
