package com.bpmco.tramitefacil.Database;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;


import java.io.File;

/**
 * Created by BPMCO02 on 12/09/13.
 */
public class DatabaseHandler implements Cloneable{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tramite.sqlite";
    private static final String DB_PATH = android.os.Environment.getExternalStorageDirectory().toString();

    private static final String TABLE_CIUD = "frm_trf_ciudadano";
    private static final String TABLE_ELEM = "frm_trf_elemento";
    private static final String TABLE_TRAM = "frm_trf_tramite";
    private static final String TABLE_ENTI = "frm_trf_entidad";
    private static final String TABLE_REGI = "frm_trf_registro";
    private static final String TABLE_CONT = "frm_trf_contexto";
    private static final String TABLE_RESP = "frm_trf_elementoRespuesta";
    private static final String TABLE_OPCI = "frm_trf_elementoOpcion";


    private static SQLiteDatabase db;

    private static DatabaseHandler instance = null;
    private static DBHandlerCiudadano handlerCiudadano  = null;
    private static DBHandlerElemento handlerElemento = null;
    private static DBHandlerTramite handlerTramite = null;
    private static DBHandlerRegistro handlerRegistro = null;
    private static DBHandlerContexto handlerContexto = null;
    private static DBHandlerElementoRespuesta handlerRespuesta = null;
    private static DBHandlerElementoOpcion handlerElementoOpcion = null;

    private DatabaseHandler() {}

    private synchronized static void createInstance() {
        instance = new DatabaseHandler();
    }

    public static DatabaseHandler getInstance() {
        if(instance == null)
        {
            createInstance();
            String ruta = DB_PATH + File.separator + DATABASE_NAME;
            if(!checkDataBase(ruta))
            {

                db = SQLiteDatabase.openOrCreateDatabase(ruta, null);
                crearHandlers();
                CreateTables(db);
                db.setVersion(DATABASE_VERSION);
            }
            else
            {
                db = SQLiteDatabase.openOrCreateDatabase(ruta, null);
                crearHandlers();
                if(db.getVersion() != DATABASE_VERSION)
                {
                    Upgrade(db,db.getVersion(),DATABASE_VERSION);
                }
            }
        }
        return instance;
    }

    private static void crearHandlers()
    {
        handlerCiudadano = DBHandlerCiudadano.getInstance(db, TABLE_CIUD);
        handlerElemento = DBHandlerElemento.getInstance(db, TABLE_ELEM);
        handlerTramite = DBHandlerTramite.getInstance(db, TABLE_TRAM);
        handlerRegistro = DBHandlerRegistro.getInstance(db, TABLE_REGI);
        handlerContexto = DBHandlerContexto.getInstance(db, TABLE_CONT);
        handlerRespuesta = DBHandlerElementoRespuesta.getInstance(db, TABLE_RESP);
        handlerElementoOpcion = DBHandlerElementoOpcion.getInstance(db, TABLE_OPCI);
    }

    public DBHandlerCiudadano getHandlerCiudadano()
    {
        return handlerCiudadano;
    }

    public DBHandlerRegistro getHandlerRegistro()
    {
        return handlerRegistro;
    }

    public DBHandlerTramite getHandlerTramite()
    {
        return handlerTramite;
    }

    public DBHandlerElemento getHandlerElemento()
    {
        return handlerElemento;
    }

    public DBHandlerContexto getHandlerContexto()
    {
        return handlerContexto;
    }

    public DBHandlerElementoRespuesta getHandlerElementoRespuesta()
    {
        return handlerRespuesta;
    }

    public DBHandlerElementoOpcion getHandlerElementoOpcion()
    {
        return handlerElementoOpcion;
    }

    private static boolean checkDataBase(String path)
    {
        try
        {
            SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
            return true;
        }
        catch (SQLiteException e)
        {
            return false;
        }
    }

    public void executeSQL(String sql)
    {
        db.beginTransaction();
        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public static boolean CreateTables(SQLiteDatabase db)
    {
        try
        {
            handlerCiudadano.creartabla();
            handlerContexto.creartabla();
            handlerElemento.creartabla();
            handlerRegistro.creartabla();
            handlerTramite.creartabla();
            handlerRespuesta.creartabla();
            handlerElementoOpcion.creartabla();
        }
        catch(Exception ex)
        {
            return false;
        }
        return true;
    }

    public static boolean Upgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        boolean retorno;
        try
        {
            retorno = true;
        }
        catch(Exception ex)
        {
            retorno = false;
        }
        finally
        {
            db.setVersion(newVersion);
        }
        return retorno;
    }

    public boolean PrepararTabla(SQLiteDatabase db, String tabla, String cadenaCreacion){
        try
        {
            db.execSQL("DROP TABLE IF EXISTS " + tabla);
            db.execSQL(cadenaCreacion);
        }
        catch(Exception ex)
        {
            return false;
        }
        return true;
    }

}

