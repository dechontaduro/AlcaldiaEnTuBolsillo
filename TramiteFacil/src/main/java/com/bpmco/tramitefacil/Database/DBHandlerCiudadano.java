package com.bpmco.tramitefacil.Database;

/**
 * Created by smarin on 30/09/13.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bpmco.tramitefacil.DTO.Ciudadano;

import java.util.ArrayList;
import java.util.List;

public class DBHandlerCiudadano {

    private SQLiteDatabase db;
    private String _tabla;

    private static DBHandlerCiudadano instance = null;

    private static final String createTablaCiudadano = "CREATE TABLE" +
            " frm_trf_ciudadano (ciuId INTEGER PRIMARY KEY AUTOINCREMENT, ciuTipoDocumento TEXT" +
            ", ciuDocumento TEXT, ciuNombre TEXT" +
            ",ciuApellidos TEXT, ciuDireccion TEXT, ciuTelefono TEXT, ciuCelular TEXT" +
            ",ciuEmail TEXT)";


    private synchronized static void createInstance(SQLiteDatabase db, String tabla)
    {
        instance = new DBHandlerCiudadano(db,tabla);
    }

    public static DBHandlerCiudadano getInstance(SQLiteDatabase db, String tabla)
    {
        if(instance == null)
        {
            createInstance(db, tabla);
        }
        return instance;
    }

    private DBHandlerCiudadano(SQLiteDatabase db, String tabla)
    {
        this.db = db;
        this._tabla = tabla;
    }

    public void creartabla()
    {
        db.execSQL(createTablaCiudadano);
    }

    public Ciudadano getCiudadano() {
        return getCiudadano(null);
    }


    public Ciudadano getCiudadano(String id) {
        String selectQuery =
                "SELECT ciuId,ciuTipoDocumento,ciuDocumento,ciuNombre,ciuApellidos,ciuDireccion,ciuTelefono,ciuCelular,ciuEmail " +
                "FROM frm_trf_ciudadano ";

        if(id != null)
            selectQuery += "WHERE ciuId = '"+ id +"' ";

        selectQuery += "ORDER BY ciuId DESC LIMIT 1 ";
        Cursor cursor = db.rawQuery(selectQuery, null);

        Ciudadano persona = new Ciudadano();
        if (cursor.moveToFirst()) {
            persona.setId(cursor.getString(0));
            persona.setTipoDoc(cursor.getString(1));
            persona.setDocumento(cursor.getString(2));
            persona.setNombres(cursor.getString(3));
            persona.setApellidos(cursor.getString(4));
            persona.setDireccion(cursor.getString(5));
            persona.setTelefono(cursor.getString(6));
            persona.setCelular(cursor.getString(7));
            persona.setEmail(cursor.getString(8));
        }
        return persona;
    }


    public String guardarCiudadano(Ciudadano ciudadano)
    {
        long id = 0;
        try
        {
            this.db.beginTransaction();
            ContentValues values = new ContentValues();

            values.put("ciuTipoDocumento", ciudadano.getTipoDoc());
            values.put("ciuDocumento", ciudadano.getDocumento());
            values.put("ciuNombre", ciudadano.getNombres());
            values.put("ciuApellidos", ciudadano.getApellidos());
            values.put("ciuDireccion", ciudadano.getDireccion());
            values.put("ciuTelefono", ciudadano.getTelefono());
            values.put("ciuCelular", ciudadano.getCelular());
            values.put("ciuEmail", ciudadano.getEmail());

            id = this.db.insert(this._tabla, null, values);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        }
        catch(Exception ex)
        {
            return "-1";
        }

        if(id == -1){
            return "-1";
        }

        return String.valueOf(id);
    }
}
