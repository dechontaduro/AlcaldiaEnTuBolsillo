package com.bpmco.tramitefacil;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bpmco.tramitefacil.DTO.Ciudadano;
import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.Database.DatabaseHandler;
import com.bpmco.tramitefacil.Database.Validation;

import java.util.List;

public class Activity_DatosPersonales extends Activity implements View.OnClickListener{

    ImageButton btnAtras = null;
    ImageButton btnHome = null;
    Ciudadano ciudadano = null;
    Contexto c = null;

    Button btnSiguiente = null;
    Spinner spTipoDoc = null;
    DatabaseHandler manejador = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try{
        setContentView(R.layout.activity_datospersonales);
        }catch(Exception e){
            e.printStackTrace();
        }

        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.title_activity_activity__datos_personales));
        btnSiguiente = (Button)findViewById(R.id.btnIngresar);
        btnSiguiente.setText(this.getString(R.string.btnDatosPersonales));
        btnSiguiente.setOnClickListener(this);

        btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnAtras.setOnClickListener(this);

        btnHome = (ImageButton)findViewById(R.id.btnHomeIco);
        btnHome.setOnClickListener(this);

        spTipoDoc = (Spinner)findViewById(R.id.spTipoDoc);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tipoDoc,android.R.layout.simple_spinner_item);
        spTipoDoc.setAdapter(adapter);

        try{
            manejador = DatabaseHandler.getInstance();
            cargarDatosCiudadano();
            c = manejador.getHandlerContexto().getContexto();
            String lblNext =
                    this.getString(R.string.label_boton_siguiente
                    , "1"
                    , Integer.parseInt(c.getMaxPage())+1);
            btnSiguiente.setText(lblNext);
        }catch (Exception e){
            Toast.makeText(this,"No se Puede Crear o Abrir la Base de Datos",2000).show();
            e.printStackTrace();
        }
    }


    public void cargarDatosCiudadano(){
        ciudadano = manejador.getHandlerCiudadano().getCiudadano();
        setCampoTexto(R.id.txtNombre, ciudadano.getNombres());
        setCampoTexto(R.id.txtApellido, ciudadano.getApellidos());
        setCampoTexto(R.id.txtNumId, ciudadano.getDocumento());
        setCampoTexto(R.id.txtDir, ciudadano.getDireccion());
        setCampoTexto(R.id.txtTel, ciudadano.getTelefono());
        setCampoTexto(R.id.txtEmail, ciudadano.getEmail());
        setDatoCombo(ciudadano.getTipoDoc());
    }

    public boolean guardarDatosCiudadano(){
        String nombre = getDatoCampoTexto(R.id.txtNombre);
        String apellido = getDatoCampoTexto(R.id.txtApellido);
        String numid = getDatoCampoTexto(R.id.txtNumId);
        String direccion = getDatoCampoTexto(R.id.txtDir);
        String telefono = getDatoCampoTexto(R.id.txtTel);
        String email = getDatoCampoTexto(R.id.txtEmail);

        Spinner spTipoDoc = (Spinner)findViewById(R.id.spTipoDoc);
        String tipoDoc = String.valueOf((spTipoDoc.getSelectedItemPosition() * 10));


        if(nombre.equals("") || apellido.equals("") || numid.equals("") ||
           direccion.equals("") || telefono.equals("") || email.equals(""))
        {
            Toast.makeText(this, "Llene todos los datos faltantes", Toast.LENGTH_LONG).show();
            return false;
        }

        Validation validation = new Validation();

        if(!validation.checkemail(email))
        {
            Toast.makeText(this, "Formato incorrecto en correo electronico", Toast.LENGTH_LONG).show();
            return false;
        }


        ciudadano.setTipoDoc(tipoDoc);
        ciudadano.setNombres(nombre);
        ciudadano.setApellidos(apellido);
        ciudadano.setDocumento(numid);
        ciudadano.setDireccion(direccion);
        ciudadano.setTelefono(telefono);
        ciudadano.setEmail(email);
        ciudadano.setTipoDoc(getDatoCombo());

        return true;
    }

    public EditText getCampoTexto(int id){
        EditText campo = (EditText)findViewById(id);
        return campo;
    }

    public void setCampoTexto(int id, String dato){
        getCampoTexto(id).setText(dato);
    }

    public String getDatoCampoTexto(int id){
        return getCampoTexto(id).getText().toString();
    }

    public void setDatoCombo(String dato){
        int n = spTipoDoc.getCount();

        for(int i = 0; i < n; i++){
            String valor = spTipoDoc.getItemAtPosition(i).toString();
            if(valor.equals(dato)){
                spTipoDoc.setSelection(i);
                i = n;
            }
        }
    }

    public String getDatoCombo(){
        return spTipoDoc.getSelectedItem().toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__datos_personales, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == btnAtras || view == btnHome){
            finish();
        }

        if(view == btnSiguiente){

            if(!guardarDatosCiudadano()){
                return;
            }

            String numeroCiudadano = manejador.getHandlerCiudadano().guardarCiudadano(ciudadano);

            if(numeroCiudadano != "-1"){
                Contexto contexto = manejador.getHandlerContexto().getContexto();
                contexto.setCiuId(numeroCiudadano);
                //contexto.setElePagina("1");
                //contexto.setMaxPage("1");
                manejador.getHandlerContexto().guardarContexto(contexto);

                contexto = manejador.getHandlerContexto().getContexto();
                if(contexto.getTraId().equals("1"))
                {
                    Intent i = new Intent(this, Activity_form1.class);
                    startActivityForResult(i, 1);
                }

                if(contexto.getTraId().equals("2"))
                {
                    Intent i = new Intent(this, Activity_form21.class);
                    startActivityForResult(i, 1);
                }
            }
            else{
                Toast.makeText(this, "Error al crear el perfil en la base de datos", 2000).show();
            }
        }
    }
}
