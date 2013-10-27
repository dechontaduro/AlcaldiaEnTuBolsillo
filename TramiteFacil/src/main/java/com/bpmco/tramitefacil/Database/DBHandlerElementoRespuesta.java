package com.bpmco.tramitefacil.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Respuesta;

import java.sql.SQLException;


/**
 * Created by smarin on 11/10/13.
 */
public class DBHandlerElementoRespuesta {


    private SQLiteDatabase db;
    private String _tabla;

    private static DBHandlerElementoRespuesta instance = null;

    private static final String createTabla = "CREATE TABLE frm_trf_elementoRespuesta (resId INTEGER PRIMARY KEY, regId INTEGER, eleId INTEGER, resValor TEXT)";

    private synchronized static void createInstance(SQLiteDatabase db, String tabla)
    {
        instance = new DBHandlerElementoRespuesta(db,tabla);
    }

    public static DBHandlerElementoRespuesta getInstance(SQLiteDatabase db, String tabla)
    {
        if(instance == null)
        {
            createInstance(db, tabla);
        }
        return instance;
    }

    private DBHandlerElementoRespuesta(SQLiteDatabase db, String tabla)
    {
        this.db = db;
        this._tabla = tabla;
    }

    public void creartabla()
    {
        db.execSQL(createTabla);
    }


    public String createRespuesta(Respuesta respuesta)
    {
        long id = 0;
        try
        {
            this.db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("regId", respuesta.getRegId());
            values.put("eleId", respuesta.getEleId());
            values.put("resValor", respuesta.getResValor());

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

    public String delete(String registroId, String pagina)
    {
        String condition =
                "resId IN " +
                "(SELECT r.resId " +
                "FROM frm_trf_elementoRespuesta r " +
                "   INNER JOIN frm_trf_elemento e ON e.eleId = r.eleId " +
                "WHERE r.regId = ? AND e.elePagina = ?)";

        long id = 0;
        try
        {
            this.db.beginTransaction();


            id = this.db.delete(this._tabla, condition, new String[] {registroId, pagina});
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


    public boolean checkPageData(String ids)
    {
        DatabaseHandler hand = DatabaseHandler.getInstance();
        Contexto contexto = hand.getHandlerContexto().getContexto();
        String registro = contexto.getRegId();
        if(registro == null || registro.equals(""))
            return false;

        try
        {
            this.db.beginTransaction();
            String query = "DELETE FROM frm_trf_elementoRespuesta WHERE regid = " + registro + " AND eleid IN (" + ids + ")";
            db.execSQL(query);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        }
        catch(Exception ex)
        {
            return false;
        }

        return true;
    }
}