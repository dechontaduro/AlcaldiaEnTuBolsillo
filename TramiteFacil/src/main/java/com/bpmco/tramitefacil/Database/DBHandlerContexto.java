package com.bpmco.tramitefacil.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.Utilidades;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by smarin on 10/10/13.
 */
public class DBHandlerContexto {


    private SQLiteDatabase db;
    private String _tabla;

    private static DBHandlerContexto instance = null;

    private static final String createTablaContexto =
            "CREATE TABLE frm_trf_contexto " +
            "(ciuId TEXT, traId TEXT, regId TEXT, elePagina TEXT, maxPage TEXT, lastSync DATE" +
            ", conexionType TEXT, syncMinutes INTEGER, geo TEXT)";

    private synchronized static void createInstance(SQLiteDatabase db, String tabla)
    {
        instance = new DBHandlerContexto(db,tabla);
    }

    public static DBHandlerContexto getInstance(SQLiteDatabase db, String tabla)
    {
        if(instance == null)
        {
            createInstance(db, tabla);
        }
        return instance;
    }

    private DBHandlerContexto(SQLiteDatabase db, String tabla)
    {
        this.db = db;
        this._tabla = tabla;
    }

    public void creartabla()
    {
        db.execSQL(createTablaContexto);
        initData();
    }


    public boolean initData()
    {
        long id = 0;
        try
        {
            this.db.beginTransaction();
            ContentValues values = new ContentValues();

            values.put("ciuId", "");
            values.put("traId", "");
            values.put("regId", "");
            values.put("elePagina", "1");
            values.put("maxPage", "");
            values.put("lastSync", "2000-01-01 00:00:00");
            values.put("conexionType", "WIFI");
            values.put("syncMinutes", 5);
            values.put("geo", "");

            id = this.db.insert(this._tabla, null, values);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        }
        catch(Exception ex)
        {
            return false;
        }

        if(id == -1){
            return false;
        }

        return true;
    }

    public boolean guardarContexto(Contexto contexto)
    {
        long id = 0;
        try
        {
            this.db.beginTransaction();
            ContentValues values = new ContentValues();

            if(contexto.getCiuId() != "") values.put("ciuId", contexto.getCiuId());
            if(contexto.getTraId() != "") values.put("traId", contexto.getTraId());
            if(contexto.getRegId() != "") values.put("regId", contexto.getRegId());
            if(contexto.getElePagina() != "") values.put("elePagina", contexto.getElePagina());
            if(contexto.getMaxPage() != "") values.put("maxPage", contexto.getMaxPage());
            if(contexto.getLastSync() != null) values.put("lastSync", Utilidades.date2sqldate(contexto.getLastSync()));
            if(contexto.getConexionType() != null) values.put("conexionType", contexto.getConexionType());
            if(contexto.getSyncMinutes() != null) values.put("syncMinutes", contexto.getSyncMinutes());
            if(contexto.getGeo() != null) values.put("geo", contexto.getGeo());

            id = this.db.update(this._tabla, values, null, null);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        }
        catch(Exception ex)
        {
            return false;
        }
        return true;
    }

    public Contexto getContexto()
    {
        String selectQuery =
                "SELECT ciuId,traId,regId,elePagina,maxPage,lastSync,conexionType,syncMinutes,geo " +
                "FROM frm_trf_contexto";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Contexto contexto = new Contexto();
        if (cursor.moveToFirst()) {

            contexto.setCiuId(cursor.getString(0));
            contexto.setTraId(cursor.getString(1));
            contexto.setRegId(cursor.getString(2));
            contexto.setElePagina(cursor.getString(3));
            contexto.setMaxPage(cursor.getString(4));
            contexto.setLastSync(Utilidades.sqldate2date(cursor.getString(5)));
            contexto.setConexionType(cursor.getString(6));
            contexto.setSyncMinutes(cursor.getInt(7));
            contexto.setGeo(cursor.getString(8));
        }
        return contexto;
    }


}
