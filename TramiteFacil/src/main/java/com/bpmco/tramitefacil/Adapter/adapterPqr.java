package com.bpmco.tramitefacil.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import com.bpmco.tramitefacil.Activity_DatosPersonales;
import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Tramite;
import com.bpmco.tramitefacil.DTO.botonesPqr;
import com.bpmco.tramitefacil.Database.DatabaseHandler;
import com.bpmco.tramitefacil.R;

import java.util.ArrayList;

/**
 * Created by Cristian Cantillo on 26/09/13.
 */
public class adapterPqr extends BaseAdapter implements Filterable
{
    private ArrayList<Tramite> tramite;
    private ArrayList<Tramite> tramiteAll;
    private LayoutInflater inflater = null;
    private Context c = null;


    public adapterPqr(Context c, ArrayList<Tramite> tramite){
        this.tramite = tramite;
        this.tramiteAll = tramite;
        this.inflater = LayoutInflater.from(c);
        this.c = c;
    }


    @Override
    public int getCount() {
        return tramite.size();
    }

    @Override
    public Object getItem(int i) {
        return tramite.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
        {
            view = inflater.inflate(R.layout.botonespqr, null);
        }

        Tramite tramite = tramiteAll.get(i);
        Button btn = (Button)view.findViewById(R.id.btnPqr);
        btn.setTag(tramite);
        btn.setText(tramite.getTraNombre());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button)view;
                Tramite tramite = (Tramite)btn.getTag();
                String id = tramite.getTraId();

                DatabaseHandler handler = DatabaseHandler.getInstance();
                String maxPages = handler.getHandlerElemento().getMaxPages(id);

                Contexto contexto = handler.getHandlerContexto().getContexto();
                contexto.setTraId(id);
                contexto.setElePagina("1");
                contexto.setMaxPage(maxPages);
                contexto.setRegId(null);

                handler.getHandlerContexto().guardarContexto(contexto);
                Intent i = new Intent(c, Activity_DatosPersonales.class);
                try
                {
                    c.startActivity(i);
                }
                catch (Exception e){
                    Log.i("hola", e.toString());
                }
            }
        });
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults Result = new FilterResults();

                if(charSequence.length() == 0){
                    Result.values = tramiteAll;
                    Result.count = tramiteAll.size();
                    return Result;
                }

                ArrayList<Tramite> objTramites = new ArrayList<Tramite>();
                String filtro = charSequence.toString().toLowerCase();
                String filtrable;

                for(int i = 0; i < tramiteAll.size(); i++){
                    Tramite t = new Tramite();
                    t = tramiteAll.get(i);
                    filtrable = t.getTraNombre();
                    if(filtrable.toLowerCase().contains(filtro)){
                        objTramites.add(t);
                    }
                }
                Result.values = objTramites;
                Result.count = objTramites.size();

                return Result;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tramite = (ArrayList<Tramite>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
