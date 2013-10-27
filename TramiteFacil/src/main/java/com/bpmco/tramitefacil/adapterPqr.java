package com.bpmco.tramitefacil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cristian Cantillo on 26/09/13.
 */
public class adapterPqr extends BaseAdapter implements Filterable
{
    private ArrayList<botonesPqr> botones;
    private ArrayList<botonesPqr> botonesAll;
    private LayoutInflater inflater = null;


    public adapterPqr(Context c, ArrayList<botonesPqr> botones){
        this.botones = botones;
        this.botonesAll = botones;
        this.inflater = LayoutInflater.from(c);
    }


    @Override
    public int getCount() {
        return botones.size();
    }

    @Override
    public Object getItem(int i) {
        return botones.get(i);
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

        botonesPqr pqr = botones.get(i);
        Button btn = (Button)view.findViewById(R.id.btnPqr);
        btn.setText(pqr.getNombre());
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults Result = new FilterResults();

                if(charSequence.length() == 0){
                    Result.values = botonesAll;
                    Result.count = botonesAll.size();
                    return Result;
                }

                ArrayList<botonesPqr> pqrFiltro = new ArrayList<botonesPqr>();
                String filtro = charSequence.toString().toLowerCase();
                String filtrable;

                for(int i = 0; i < botonesAll.size(); i++){
                    filtrable = botonesAll.get(i).getNombre();
                    if(filtrable.toLowerCase().contains(filtro)){
                        pqrFiltro.add(new botonesPqr(filtrable));
                    }
                }
                Result.values = pqrFiltro;
                Result.count = pqrFiltro.size();

                return Result;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                botones = (ArrayList<botonesPqr>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
