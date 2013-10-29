package com.bpmco.tramitefacil.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bpmco.tramitefacil.DTO.ElementoOpcion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarin on 11/10/13.
 */
public class DBHandlerElementoOpcion {


    private SQLiteDatabase db;
    private String _tabla;

    private static DBHandlerElementoOpcion instance = null;

    private static final String createTabla =
            "CREATE TABLE frm_trf_elementoOpcion " +
            "(opcId INTEGER PRIMARY KEY AUTOINCREMENT, opcGrupo TEXT, opcValor TEXT, opcTexto TEXT, opcOrden INTEGER)";

    private synchronized static void createInstance(SQLiteDatabase db, String tabla)
    {
        instance = new DBHandlerElementoOpcion(db,tabla);
    }

    public static DBHandlerElementoOpcion getInstance(SQLiteDatabase db, String tabla)
    {
        if(instance == null)
        {
            createInstance(db, tabla);
        }
        return instance;
    }

    private DBHandlerElementoOpcion(SQLiteDatabase db, String tabla)
    {
        this.db = db;
        this._tabla = tabla;
    }

    public void creartabla()
    {
        db.execSQL(createTabla);
        initData();
    }

    public void initData()
    {
        createOptions("TD","10","Cedula de Ciudadania","1");
        createOptions("TD","20","NIT","2");
        createOptions("TD","30","Pasaporte","3");
        createOptions("TD","40","Tarjeta Identidad","4");
        createOptions("TD","50","Cedula de Extranjeria","5");
        createOptions("TD","60","Registro Civil","6");
        createOptions("TPQR","0","DENUNCIA","1");
        createOptions("TPQR","1","QUEJA","2");
        createOptions("TPQR","2","RECLAMO","3");
        createOptions("TPQR","3","MANIFESTACION","4");
        createOptions("TPQR","4","CONSULTA","5");
        createOptions("TPQR","5","SUGERENCIA","6");
        createOptions("TPQR","6","FELICITACION","7");
        createOptions("TPQR","7","PETICION DE INFORMACION","8");
        createOptions("TPQR","8","OTRO TIPO","9");
        createOptions("SN","SI","Si","1");
        createOptions("SN","NO","No","1");
        createOptions("VER","Agua Bonita","Agua Bonita","1");
        createOptions("VER","Altamar","Altamar","2");
        createOptions("BAR","Albania","Albania","1");
        createOptions("BAR","Alhambra","Alhambra","2");
        createOptions("EPS","SaludCoop","SaludCoop","1");
        createOptions("EPS","SOS","SOS","2");
        createOptions("IPS","Pasbisalud","Pasbisalud","1");
        createOptions("IPS","Unisalud","Unisalud","2");
        createOptions("TQSAL","Asignación cita medica","Asignación cita medica","1");
        createOptions("TQSAL","Consulta especializada","Consulta especializada","2");
        createOptions("TQSAL","Medicamentos","Medicamentos","3");
        createOptions("TLUG","Barrio","Barrio","1");
        createOptions("TLUG","Vereda","Vereda","2");
        createOptions("AMES","1","1","1");
        createOptions("AMES","2","2","2");
        createOptions("AMES","3","3","3");
        createOptions("AMES","4","4","4");
        createOptions("AMES","5","5","5");
        createOptions("AMES","6","6","6");
        createOptions("AMES","7","7","7");
        createOptions("AMES","8","8","8");
        createOptions("AMES","9","9","9");
        createOptions("AMES","10","10","10");
        createOptions("AMES","11","11","11");
        createOptions("AMES","0","0","0");
        createOptions("TGQ","EPS","EPS","1");
        createOptions("TGQ","IPS","IPS","2");
        createOptions("TGQ","OTRA","OTRA","3");
    }

    public String createOptions(String opcGrupo, String opcValor, String opcTexto, String opcOrden)
    {

        long id = 0;
        try
        {
            this.db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("opcGrupo", opcGrupo);
            values.put("opcValor", opcValor);
            values.put("opcTexto", opcTexto);
            values.put("opcOrden", opcOrden);

            id = this.db.insert(this._tabla, null, values);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        }
        catch(Exception ex)
        {
            return "-1";
        }

        return String.valueOf(id);
    }

    public List<ElementoOpcion> getOpciones(String grupo)
    {
        List<ElementoOpcion> lst  = new ArrayList<ElementoOpcion>();
        String selectQuery =
                "SELECT opcId, opcGrupo, opcValor, opcTexto, opcOrden " +
                "FROM " + this._tabla + " " +
                "WHERE opcGrupo = '" + grupo + "' " +
                "ORDER BY opcOrden";

        Cursor cursor = db.rawQuery(selectQuery, null);
        ElementoOpcion obj = null;
        if (cursor.moveToFirst()) {
            do {
                obj = new ElementoOpcion();
                obj.setId(cursor.getInt(0));
                obj.setGrupo(cursor.getString(1));
                obj.setValor(cursor.getString(2));
                obj.setTexto(cursor.getString(3));
                obj.setOrden(cursor.getInt(4));

                lst.add(obj);
            } while (cursor.moveToNext());
        }
        return lst;
    }

}
