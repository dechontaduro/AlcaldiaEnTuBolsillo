package com.bpmco.tramitefacil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Elemento;
import com.bpmco.tramitefacil.DTO.ElementoOpcion;
import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.DTO.Respuesta;
import com.bpmco.tramitefacil.Database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;


public class Activity_render extends Activity {
    private DatabaseHandler hand;
    Contexto contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hand = DatabaseHandler.getInstance();
        contexto = hand.getHandlerContexto().getContexto();

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_render);
        TextView titulo = (TextView)findViewById(R.id.txtcabecera);
        titulo.setText(this.getString(R.string.titulo_formulariopqr));

        renderForm();
    }

    public void renderForm()
    {
        List<Elemento> elementos = hand.getHandlerElemento().getElementos(contexto.getElePagina(), contexto.getTraId(), contexto.getRegId());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        LinearLayout principal = (LinearLayout)findViewById(R.id.principal);

        for(int i = 0; i < elementos.size(); i++) {

            layout = new LinearLayout(this);
            String id = elementos.get(i).getId();
            String etiqueta = elementos.get(i).getEtiqueta();
            char tipo  = elementos.get(i).getTipo().charAt(0);


            switch (tipo)
            {
                case 't':

                    TextView labelText = new TextView(getApplicationContext());
                    labelText.setText(etiqueta + " :");
                    labelText.setTextColor(Color.BLACK);
                    labelText.setTextSize(18);

                    EditText text = new EditText(getApplicationContext());
                    text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    text.setTextColor(Color.BLACK);
                    text.setTextSize(20);
                    text.setId(Integer.parseInt(elementos.get(i).getId()));

                    text.setText(elementos.get(i).getRepuesta());

                    principal.addView(labelText);
                    principal.addView(text);

                    break;
                case  'l':
                    TextView label = new TextView(getApplicationContext());
                    label.setText(etiqueta);
                    label.setTextSize(18);
                    label.setTextColor(Color.BLACK);
                    label.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    layout.addView(label);
                    break;
                case 'r':
                    TextView labelRadio = new TextView(getApplicationContext());
                    labelRadio.setTextColor(Color.BLACK);
                    labelRadio.setText(etiqueta + ":");
                    labelRadio.setTextSize(18);

                    principal.addView(labelRadio);

                    RadioGroup grupoRadio = new RadioGroup(getApplicationContext());

                    List<ElementoOpcion> rdOpc = hand.getHandlerElementoOpcion().getOpciones(elementos.get(i).getGrupo());
                    for(int r = 0; r < rdOpc.size(); r++) {
                        RadioButton radio = new RadioButton(getApplicationContext());
                        radio.setText(rdOpc.get(r).getTexto());
                        radio.setTag(rdOpc.get(r));
                        radio.setTextColor(Color.BLACK);
                        grupoRadio.addView(radio);
                        grupoRadio.setOrientation(LinearLayout.HORIZONTAL);
                    }

                    principal.addView(grupoRadio);
                    break;
                case 's':
                    LinearLayout layoutSelect = new LinearLayout(this);
                    layoutSelect.setOrientation(LinearLayout.HORIZONTAL);
                    layoutSelect.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                    TextView labelSelect = new TextView(getApplicationContext());
                    labelSelect.setText(etiqueta + " :");
                    labelSelect.setTextColor(Color.BLACK);
                    labelSelect.setTextSize(18);
                    layout.addView(labelSelect);

                    Spinner select = new Spinner(getApplicationContext());

                    List<ElementoOpcion> selOpc = hand.getHandlerElementoOpcion().getOpciones(elementos.get(i).getGrupo());
                    String[] arrOpc = new String[selOpc.size()];
                    for(int os = 0; os < selOpc.size(); os++) {
                        arrOpc[os] = selOpc.get(os).getTexto();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrOpc);
                    select.setAdapter(adapter);

                    select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });

                    layout.addView(select);
                    break;
            }

            principal.addView(layout);
        }

        //Tramite tramite = new Tramite();
        //tramite.setTraId(contexto.getTraId());
        //contexto.setMaxPage(hand.getHandlerElemento().getMaxPages(contexto.getTraId()));
        //hand.getHandlerContexto().guardarContexto(contexto);

        String maxPage = contexto.getMaxPage();
        String actPage = contexto.getElePagina();

        Button btnNext = new Button(getApplicationContext());
        btnNext.setText("Pagina " + actPage + "de " + maxPage);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout principal = (LinearLayout)findViewById(R.id.principal);
                ArrayList<EditText> myEditTextList = new ArrayList<EditText>();

                DatabaseHandler handler = DatabaseHandler.getInstance();
                Contexto contexto = handler.getHandlerContexto().getContexto();

                String idRegistro = contexto.getRegId();
                if(idRegistro == ""){
                    Registro registro = new Registro();
                    registro.setTraId(Integer.parseInt(contexto.getTraId()));
                    registro.setCiuId(Integer.parseInt(contexto.getCiuId()));
                    registro.setStatus(Registro.Status.PENDIENTE.toString());

                    idRegistro = handler.getHandlerRegistro().createRegistro(registro);
                    contexto.setRegId(idRegistro);
                    handler.getHandlerContexto().guardarContexto(contexto);
                }
                else
                {
                    Registro registro = handler.getHandlerRegistro().getById(idRegistro);
                    handler.getHandlerRegistro().save(registro);
                    handler.getHandlerElementoRespuesta().delete(idRegistro, contexto.getElePagina());
                }

                for( int i = 0; i < principal.getChildCount(); i++ )
                {
                   if(principal.getChildAt(i) instanceof EditText){

                       EditText elemento = (EditText)principal.getChildAt(i);
                       Respuesta respuesta = new Respuesta();
                       respuesta.setRegId(idRegistro);
                       respuesta.setEleId(String.valueOf(elemento.getId()));
                       respuesta.setResValor(elemento.getText().toString());

                       handler.getHandlerElementoRespuesta().createRespuesta(respuesta);
                   }
                }

                if(contexto.getMaxPage().compareTo(contexto.getElePagina()) == 0)
                {
                    Intent i = new Intent(Activity_render.this, Activity_Resumen.class);
                    startActivity(i);
                }
                else
                {
                    contexto.setElePagina((Integer.parseInt(contexto.getElePagina()) + 1) + "");
                    handler.getHandlerContexto().guardarContexto(contexto);
                    Intent i = new Intent(Activity_render.this, Activity_render.class);
                    startActivity(i);
                }

            }
        });

        principal.addView(btnNext);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_render, menu);
        return true;
    }
    
}
