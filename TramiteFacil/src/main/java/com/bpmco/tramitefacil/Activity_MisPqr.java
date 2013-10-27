package com.bpmco.tramitefacil;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class Activity_MisPqr extends Activity implements View.OnClickListener{

    ImageButton btnAtras = null;
    ImageButton btnHome = null;
    Button btnAcercaDe = null;
    Button btnHome2 = null;
    DatabaseHandler manejador = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mispqr);

        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.tituloMisPqr));

        btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnHome = (ImageButton)findViewById(R.id.btnHomeIco);
        btnHome2 = (Button)findViewById(R.id.btnMisPqr);
        btnAcercaDe = (Button)findViewById(R.id.btnAcercaDe);
        btnAtras.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnHome2.setOnClickListener(this);
        btnAcercaDe.setOnClickListener(this);
        btnHome2.setText(this.getString(R.string.txt_inicio));
        verRegistros();
    }

    public void verRegistros(){
        List<Registro> listaRegistros = getRegistros();
        if(listaRegistros != null || listaRegistros.size() > 0){
            int n = listaRegistros.size();
            LinearLayout lyRegistro = (LinearLayout)findViewById(R.id.lyMisPqr);

            for(int i = 0; i < n; i++){
                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout lyDatos = (LinearLayout)inflater.inflate(R.layout.pqr_registradas,null, false);
                lyRegistro.addView(lyDatos);

                if((i % 2) != 0){
                    lyDatos.setBackgroundResource(R.drawable.fondomispqr2);
                }

                TextView radicado = (TextView)lyDatos.findViewById(R.id.lblRadicado);
                TextView descripcion = (TextView)lyDatos.findViewById(R.id.lblDesc);
                TextView fecha = (TextView)lyDatos.findViewById(R.id.lblFecha);
                ImageView imagen = (ImageView)lyDatos.findViewById(R.id.iconoEstado);

                Registro registro = listaRegistros.get(i);
                radicado.setText(registro.getRadicacion());
                descripcion.setText(registro.getInfo());
                String fechaReg = Utilidades.date2string(registro.getFecha(), "dd-MM-yy");
                fecha.setText(fechaReg);

                imagen.setTag(registro);

                if(listaRegistros.get(i).getStatus().equals("0")){
                    imagen.setBackgroundResource(R.drawable.enregistro);
                }
                else if(listaRegistros.get(i).getStatus().equals("1")){
                    imagen.setBackgroundResource(R.drawable.registrado);
                }
                else if(listaRegistros.get(i).getStatus().equals("2")){
                    imagen.setBackgroundResource(R.drawable.enviado);
                }else{
                    imagen.setBackgroundResource(R.drawable.enregistro);
                }

                imagen.setOnClickListener(this);
            }
        }else{
            Toast.makeText(this, "No Se Encontraron Datos registrados", 2000).show();
        }
    }

    public List<Registro> getRegistros(){
        List<Registro> listaRegistro = new ArrayList<Registro>();
        try{
            manejador = DatabaseHandler.getInstance();
            listaRegistro = manejador.getHandlerRegistro().getRegistros();
        }catch (Exception e){
            Toast.makeText(this, "No se Puede Abrir la Base de Datos", 2000).show();
            e.printStackTrace();
            return null;
        }
        return listaRegistro;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__mis_pqr, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getTag() == null){
            if(view == btnAtras){
                finish();
            }

            if(view == btnHome){
                Intent i = new Intent(Activity_MisPqr.this, Activity_listadoPqr.class);
                startActivity(i);
            }

            if(view == btnHome2){
                Intent i = new Intent(Activity_MisPqr.this, Activity_listadoPqr.class);
                startActivity(i);
            }

            if(view == btnAcercaDe){
                Intent i = new Intent(Activity_MisPqr.this, Activity_AcercaDe.class);
                startActivity(i);
            }
        }else{
            Registro r = (Registro)view.getTag();
            Contexto c = manejador.getHandlerContexto().getContexto();
            c.setTraId(null);
            c.setRegId(r.getId().toString());
            manejador.getHandlerContexto().guardarContexto(c);

            Intent i = new Intent(Activity_MisPqr.this, Activity_Resumen.class);
            startActivity(i);
        }
    }
}
