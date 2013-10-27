package com.bpmco.tramitefacil.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bpmco.tramitefacil.DTO.Ciudadano;
import com.bpmco.tramitefacil.DTO.Tramite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarin on 3/10/13.
 */
public class DBHandlerTramite {

    private SQLiteDatabase db;
    private String _tabla;

    private static DBHandlerTramite instance = null;

    private static final String createTabla = "CREATE TABLE frm_trf_tramite (traId INTEGER PRIMARY KEY AUTOINCREMENT, entId INTEGER, traNombre TEXT, traDescripcion TEXT, traIdentidad TEXT, traGeolocalizado TEXT)";

    private synchronized static void createInstance(SQLiteDatabase db, String tabla)
    {
        instance = new DBHandlerTramite(db,tabla);
    }

    public static DBHandlerTramite getInstance(SQLiteDatabase db, String tabla)
    {
        if(instance == null)
        {
            createInstance(db, tabla);
        }
        return instance;
    }

    private DBHandlerTramite(SQLiteDatabase db, String tabla)
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
        Tramite tramiteSalud = new Tramite();
        tramiteSalud.setTraNombre("SALUD");
        tramiteSalud.setEntId("0");
        tramiteSalud.setTraDescripcion("Peticiones Quejas Y reclamos en el area de la salud");
        tramiteSalud.setTraIdentidad("1");
        tramiteSalud.setTraGeolocalizado("0");
        createTramiteBD(tramiteSalud);

        Tramite tramiteGeneral = new Tramite();
        tramiteGeneral.setTraNombre("GENERAL");
        tramiteGeneral.setEntId("0");
        tramiteGeneral.setTraDescripcion("Peticiones Quejas Y reclamos en general");
        tramiteGeneral.setTraIdentidad("1");
        tramiteGeneral.setTraGeolocalizado("0");


        createTramiteBD(tramiteGeneral);
    }

    public boolean createTramiteBD(Tramite tramite)
    {
        long id = 0;
        try
        {
            this.db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("entId", tramite.getEntId());
            values.put("traNombre", tramite.getTraNombre());
            values.put("traNombre", tramite.getTraNombre());
            values.put("traDescripcion", tramite.getTraDescripcion());
            values.put("traIdentidad", tramite.getTraIdentidad());
            values.put("traGeolocalizado", tramite.getTraGeolocalizado());

            id = this.db.insert(this._tabla, null, values);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        }
        catch(Exception ex)
        {
            return false;
        }
        return true;
    }

    public List<Tramite> getTramites()
    {
        List<Tramite> listatramite = new ArrayList<Tramite>();
        String selectQuery = "SELECT traId, entId, traNombre, traDescripcion, traIdentidad, traGeolocalizado FROM " + this._tabla;
        Cursor cursor = db.rawQuery(selectQuery, null);
        Tramite objTramites = null;
        if (cursor.moveToFirst()) {
            do {
                objTramites = new Tramite();
                objTramites.setTraId(cursor.getString(0));
                objTramites.setEntId(cursor.getString(1));
                objTramites.setTraNombre(cursor.getString(2));
                objTramites.setTraDescripcion(cursor.getString(3));
                objTramites.setTraIdentidad(cursor.getString(4));
                objTramites.setTraGeolocalizado(cursor.getString(5));
                listatramite.add(objTramites);
            } while (cursor.moveToNext());
        }
        return listatramite;
    }

}
