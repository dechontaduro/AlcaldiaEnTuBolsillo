package com.bpmco.tramitefacil.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bpmco.tramitefacil.DTO.Registro;
import com.bpmco.tramitefacil.Utilidades;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarin on 9/10/13.
 */
public class DBHandlerRegistro {

    private SQLiteDatabase db;
    private String _tabla;

    private static DBHandlerRegistro instance = null;

    private static final String createTablaRegistro =
            "CREATE TABLE frm_trf_registro " +
            "(regId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, traId TEXT, ciuId TEXT" +
            ", regFecha DATE, regRadicacion TEXT, regInfo TEXT, regStatus TEXT, regTipo TEXT" +
            ", regGeo TEXT, regWSResultado TEXT, regVigencia INTEGER" +
            ", ultimoPaso TEXT, ultimoEstado TEXT, ultimaRespuesta TEXT, ultimaFecha TEXT)";

    private synchronized static void createInstance(SQLiteDatabase db, String tabla)
    {
        instance = new DBHandlerRegistro(db,tabla);
    }

    public static DBHandlerRegistro getInstance(SQLiteDatabase db, String tabla)
    {
        if(instance == null)
        {
            createInstance(db, tabla);
        }
        return instance;
    }

    private DBHandlerRegistro(SQLiteDatabase db, String tabla)
    {
        this.db = db;
        this._tabla = tabla;
    }

    public void creartabla()
    {
        db.execSQL(createTablaRegistro);
    }

    public String createRegistro(Registro registro)
    {
        long id = 0;
        try
        {
            this.db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("traId", registro.getTraId());
            values.put("ciuId", registro.getCiuId());
            values.put("regFecha", Utilidades.date2sqldate(registro.getFecha()));
            values.put("regRadicacion", registro.getRadicacion());
            values.put("regInfo", registro.getInfo());
            values.put("regStatus", registro.getStatus());
            values.put("regTipo", registro.getTipo());
            values.put("regGeo", registro.getGeo());
            values.put("regWSResultado", registro.getRespuestaWS());
            values.put("regVigencia", registro.getVigencia());
            values.put("ultimoPaso", registro.getUltimoPaso());
            values.put("ultimoEstado", registro.getUltimoEstado());
            values.put("ultimaRespuesta", registro.getUltimaRespuesta());
            values.put("ultimaFecha", registro.getUltimaFecha());

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

    public Registro getById(String id) {
        String selectQuery =
                "SELECT regId,traId,ciuId,regTipo,regFecha,regRadicacion,regInfo,regStatus,regGeo,regWSResultado,regVigencia " +
                    ",ultimoPaso,ultimoEstado,ultimaRespuesta,ultimaFecha " +
                "FROM frm_trf_registro " +
                "WHERE regId = '" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Registro registro = new Registro();
        if (cursor.moveToFirst()) {
            registro.setId(cursor.getInt(0));
            registro.setTraId(cursor.getInt(1));
            registro.setCiuId(cursor.getInt(2));
            registro.setTipo(cursor.getString(3));
            registro.setFecha(Utilidades.sqldate2date(cursor.getString(4)));
            registro.setRadicacion(cursor.getString(5));
            registro.setInfo(cursor.getString(6));
            registro.setStatus(cursor.getString(7));
            registro.setGeo(cursor.getString(8));
            registro.setRespuestaWS(cursor.getString(9));
            registro.setVigencia(cursor.getInt(10));
            registro.setUltimoPaso(cursor.getString(11));
            registro.setUltimoEstado(cursor.getString(12));
            registro.setUltimaRespuesta(cursor.getString(13));
            registro.setUltimaFecha(cursor.getString(14));
        }
        return registro;
    }

	public List<Registro> getRegistros(){
        List<Registro> listaRegistros = new ArrayList<Registro>();
        String selectQuery =
                "SELECT regId,traId,ciuId,regTipo,regFecha,regRadicacion,regInfo,regStatus,regGeo,regWSResultado,regVigencia " +
                    ",ultimoPaso,ultimoEstado,ultimaRespuesta,ultimaFecha " +
                "FROM frm_trf_registro";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Registro registro = null;
        if (cursor.moveToFirst()) {
            do {
                registro = new Registro();
                registro.setId(cursor.getInt(0));
                registro.setTraId(cursor.getInt(1));
                registro.setCiuId(cursor.getInt(2));
                registro.setTipo(cursor.getString(3));
                registro.setFecha(Utilidades.sqldate2date(cursor.getString(4)));
                registro.setRadicacion(cursor.getString(5));
                registro.setInfo(cursor.getString(6));
                registro.setStatus(cursor.getString(7));
                registro.setGeo(cursor.getString(8));
                registro.setRespuestaWS(cursor.getString(9));
                registro.setVigencia(cursor.getInt(10));
                registro.setUltimoPaso(cursor.getString(11));
                registro.setUltimoEstado(cursor.getString(12));
                registro.setUltimaRespuesta(cursor.getString(13));
                registro.setUltimaFecha(cursor.getString(14));

                listaRegistros.add(registro);
            } while (cursor.moveToNext());
        }
        return listaRegistros;
    }

	public boolean save(Registro registro)
    {
        long id = 0;
        try
        {
            this.db.beginTransaction();
            ContentValues values = new ContentValues();

            if(registro.getCiuId() != null) values.put("ciuId", registro.getCiuId());
            if(registro.getTipo() != null) values.put("regTipo", registro.getTipo());
            if(registro.getRadicacion() != null) values.put("regRadicacion", registro.getRadicacion());
            if(registro.getInfo() != null)values.put("regInfo", registro.getInfo());
            if(registro.getFecha() != null)values.put("regFecha", Utilidades.date2sqldate(registro.getFecha()));
            if(registro.getStatus() != null)values.put("regStatus", registro.getStatus());
            if(registro.getGeo() != null)values.put("regGeo", registro.getGeo());
            if(registro.getRespuestaWS() != null)values.put("regWSResultado", registro.getRespuestaWS());
            if(registro.getVigencia() != null)values.put("regVigencia", registro.getVigencia());
            if(registro.getUltimoPaso() != null)values.put("ultimoPaso", registro.getUltimoPaso());
            if(registro.getUltimoEstado() != null)values.put("ultimoEstado", registro.getUltimoEstado());
            if(registro.getUltimaRespuesta() != null)values.put("ultimaRespuesta", registro.getUltimaRespuesta());
            if(registro.getUltimaFecha() != null)values.put("ultimaFecha", registro.getUltimaFecha());

            id = this.db.update(this._tabla, values, "regId = '" + registro.getId() +"'", null);

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
