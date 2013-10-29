package com.bpmco.tramitefacil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Elemento;
import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.DTO.Respuesta;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Activity_Resumen extends Activity implements View.OnClickListener{

    ImageButton btnAtras = null;
    ImageButton btnHome = null;
    Button btnGrabar = null;
    Button btnCancelar = null;
    DatabaseHandler manejador = null;
    Contexto contexto = null;
    Registro registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.title_activity_activity__resumen));

        btnAtras = (ImageButton)findViewById(R.id.btnAtrasIco);
        btnHome = (ImageButton)findViewById(R.id.btnHomeIco);
        btnGrabar = (Button)findViewById(R.id.btnRegistrar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);

        btnAtras.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnGrabar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        verResumen();
    }

    public void verResumen(){
        manejador = DatabaseHandler.getInstance();
        contexto = manejador.getHandlerContexto().getContexto();

        if(contexto.getRegId() == null || contexto.getRegId().equals(""))
        {
            Toast.makeText(this, "No Se Encontraron Datos registrados", 2000).show();
            return;
        }

        if(contexto.getTraId() == null || contexto.getTraId().equals("")){
            btnGrabar.setVisibility(View.GONE);
            btnCancelar.setVisibility(View.GONE);
        }

        registro = manejador.getHandlerRegistro().getById(contexto.getRegId());

        verAvances(registro);
        verRespuestas(registro);
    }

    private void verAvances(Registro reg) {
        String ultimaRespuesta = reg.getUltimaRespuesta();
        if(ultimaRespuesta != null && !ultimaRespuesta.equals(""))
        {
            LinearLayout lytR = (LinearLayout)findViewById(R.id.lyResumen);
            TextView resumen = (TextView)lytR.findViewById(R.id.txtResumen);
            resumen.setVisibility(View.VISIBLE);

            LinearLayout lytAvance = (LinearLayout)findViewById(R.id.lytAvances);
            lytAvance.setVisibility(View.VISIBLE);

            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout lytAvanceElem = (LinearLayout)inflater.inflate(R.layout.ele_pqravance,null, false);

            lytAvance.addView(lytAvanceElem);

            TextView fecha = (TextView)lytAvanceElem.findViewById(R.id.lblFecha);
            fecha.setText(reg.getUltimaFecha().substring(0, 10));

            TextView avance = (TextView)lytAvanceElem.findViewById(R.id.lblAvance);
            avance.setText(reg.getUltimaRespuesta());
        }
    }


    public List<Elemento> getElementos(Registro reg){
        List<Elemento> lst = new ArrayList<Elemento>();
        try{
            lst = manejador.getHandlerElemento().getElementos("0", reg.getTraId().toString(), reg.getId().toString());
        }catch (Exception e){
            Toast.makeText(this, "No se Puede Abrir la Base de Datos", 2000).show();
            e.printStackTrace();
            return null;
        }
        return lst;
    }

    private void verRespuestas(Registro reg) {

        List<Elemento> lst = getElementos(reg);
        if(lst == null || lst.size() == 0){
            Toast.makeText(this, "No Se Encontraron Datos registrados", 2000).show();
            return;
        }

        int n = lst.size();
        LinearLayout lytR = (LinearLayout)findViewById(R.id.lyResumen);

        for(int i = 0; i < n; i++){
            Elemento ele = lst.get(i);

            if(!ele.getTipo().equals("l") && ele.getRepuesta() == null )
                continue;

            boolean esAdjunto = false;
            int typeLyt = R.layout.labelinfo;

            if(ele.getTipo().equals("f") || ele.getTipo().equals("v")){
                typeLyt = R.layout.ver_adjunto;
                esAdjunto = true;
            }

            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout lytEle = (LinearLayout)inflater.inflate(typeLyt,null, false);
            lytR.addView(lytEle);

            TextView label = (TextView)lytEle.findViewById(R.id.lblTitulo);
            if(ele.getTipo().equals("l")){
                label.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
                label.setBackgroundResource(R.drawable.header);
            }

            String labelText = ele.getEtiqueta();
            label.setText(labelText + ":");

            if(!esAdjunto){
                TextView dato = (TextView)lytEle.findViewById(R.id.lblDatos);
                dato.setText(lst.get(i).getRepuesta());
            }else{
                Button btnPreview = (Button)lytEle.findViewById(R.id.btnPreview);
                String[] nombreArchivo = lst.get(i).getRepuesta().split("/");
                btnPreview.setText(nombreArchivo[nombreArchivo.length - 1]);
                btnPreview.setTag(lst.get(i));
                btnPreview.setOnClickListener(this);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__resumen, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getTag() == null){
            if(view == btnAtras){
                Intent i = new Intent(Activity_Resumen.this, Activity_listadoPqr.class);
                startActivity(i);
            }

            if(view == btnHome || view == btnCancelar){
                Intent i = new Intent(Activity_Resumen.this, Activity_listadoPqr.class);
                startActivity(i);
            }

            if(view == btnGrabar){
                registro.setStatus(Registro.Status.REGISTRADO.toString());
                manejador.getHandlerRegistro().save(registro);

                contexto.setTraId("");
                contexto.setCiuId("");

                manejador.getHandlerContexto().guardarContexto(contexto);

                Intent i = new Intent(Activity_Resumen.this, Activity_PQRRegister.class);
                startActivity(i);
            }
        }else{
            Elemento elem = (Elemento)view.getTag();
            if(elem.getTipo().equals("f")){
                Intent i = new Intent(Activity_Resumen.this, Activity_Preview_Foto.class);
                i.putExtra("ruta", elem.getRepuesta());
                startActivity(i);
            }else{
                Intent i = new Intent(Activity_Resumen.this, Activity_Preview_Video.class);
                i.putExtra("ruta", elem.getRepuesta());
                startActivity(i);
            }
        }
    }
}
