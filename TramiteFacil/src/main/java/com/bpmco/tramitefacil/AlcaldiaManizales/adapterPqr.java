package com.bpmco.tramitefacil.AlcaldiaManizales;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import com.bpmco.tramitefacil.Activity_DatosPersonales;
import com.bpmco.tramitefacil.DTO.Tramite;
import com.bpmco.tramitefacil.DTO.botonesPqr;
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
        if (view == null){
            view = inflater.inflate(R.layout.botonespqr, null);
        }

        Tramite tramite = tramiteAll.get(i);

        Button btn = (Button)view.findViewById(R.id.btnPqr);
        btn.setText(tramite.getTraNombre());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn = (Button)view;
                if(btn.getText().equals("PQR Salud")){
                    Intent i = new Intent(c, Activity_DatosPersonales.class);
                    c.startActivity(i);
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

                ArrayList<botonesPqr> pqrFiltro = new ArrayList<botonesPqr>();
                String filtro = charSequence.toString().toLowerCase();
                String filtrable;

                for(int i = 0; i < tramiteAll.size(); i++){
                    filtrable = tramiteAll.get(i).getTraNombre();
                    if(filtrable.toLowerCase().contains(filtro)){
                        pqrFiltro.add(new botonesPqr(filtrable, "0"));
                    }
                }
                Result.values = pqrFiltro;
                Result.count = pqrFiltro.size();

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
