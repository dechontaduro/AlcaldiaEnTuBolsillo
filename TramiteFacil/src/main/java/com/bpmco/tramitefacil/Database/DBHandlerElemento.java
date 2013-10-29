package com.bpmco.tramitefacil.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bpmco.tramitefacil.DTO.Ciudadano;
import com.bpmco.tramitefacil.DTO.Contexto;
import com.bpmco.tramitefacil.DTO.Elemento;
import com.bpmco.tramitefacil.DTO.Tramite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smarin on 2/10/13.
 */
public class DBHandlerElemento {


    private SQLiteDatabase db;
    private String _tabla;

    private static DBHandlerElemento instance = null;

    private static final String createTabla =
            "CREATE TABLE frm_trf_elemento " +
            "(eleId INTEGER PRIMARY KEY, traId TEXT, eleFormEleId TEXT, eleTipo TEXT, eleEtiqueta TEXT, eleOpciones TEXT" +
            ",eleRequerido TEXT, eleValidacion TEXT, elePagina INTEGER, eleOrden TEXT, opcGrupo TEXT" +
            ", eleParentId TEXT, eleParentType TEXT, eleParentValue TEXT)";

    private synchronized static void createInstance(SQLiteDatabase db, String tabla)
    {
        instance = new DBHandlerElemento(db,tabla);
    }

    public static DBHandlerElemento getInstance(SQLiteDatabase db, String tabla)
    {
        if(instance == null)
        {
            createInstance(db, tabla);
        }
        return instance;
    }

    private DBHandlerElemento(SQLiteDatabase db, String tabla)
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
        createElement(1, "1","t","Lugar o direccion en Manizales donde sucedio el evento","NULL"  ,"1","AN","1","1","NULL","NULL","NULL","NULL");
        createElement(2, "1","r","Barrio o Vereda","TLUG"                               ,"1","NULL","1","2","NULL","NULL","NULL","NULL");
        createElement(3, "1","s","Barrio","BAR"                                         ,"1","NULL","1","3","2","0","Barrio","NULL");
        createElement(23, "1","s","Vereda","VER"                                        ,"1","NULL","1","3","2","0","Vereda","NULL");
        createElement(6, "1","l","Informacion del Afectado","NULL"                      ,"0","NULL","1","4","NULL","NULL","NULL","NULL");
        createElement(7, "1","s","Tipo de Identificacion","TD"                          ,"1","NULL","1","5","NULL","NULL","NULL","NULL");
        createElement(8, "1","t","No. de Identificacion","NULL"                         ,"1","E","1","6","NULL","NULL","NULL","NULL");
        createElement(9, "1","t","Nombre del Afectado","NULL"                           ,"1","A","1","7","NULL","NULL","NULL","NULL");
        createElement(4, "1","t","Edad (anios)","NULL"                                  ,"1","E","1","8","NULL","NULL","NULL","NULL");
        createElement(5, "1","s","Edad (meses)","AMES"                                  ,"1","NULL","1","9","NULL","NULL","NULL","NULL");

        createElement(10, "1","r","Tipo Entidad Generadora de Queja","TGQ"              ,"1","NULL","2","1","NULL","NULL","NULL","NULL");
        createElement(11, "1","s","EPS","EPS"                                           ,"1","NULL","2","2","10","0","EPS","NULL");
        createElement(24, "1","s","IPS","EPS"                                           ,"1","NULL","2","2","10","0","EPS","NULL");
        createElement(12, "1","t","Nombre Entidad Generadora de Queja","NULL"           ,"1","AN","2","3","10","0","OTRA","NULL");
        createElement(13, "1","s","Clasifique su Queja de Salud","TQSAL"                ,"1","NULL","2","4","NULL","NULL","NULL","NULL");
        createElement(29, "1","l","Adjuntos","NULL"                                     ,"0","NULL","1","4","NULL","NULL","NULL","NULL");
        createElement(14, "1","r","Desea incluir una foto?","SN"                        ,"1","NULL","4","1","NULL","NULL","NULL","NULL");
        createElement(15, "1","f","Foto","NULL"                                         ,"1","NULL","3","1","NULL","0","SI","NULL");
        createElement(16, "1","r","Desea incluir un video?","SN"                        ,"1","NULL","4","2","NULL","NULL","NULL","NULL");
        createElement(17, "1","v","Video","NULL"                                        ,"1","NULL","3","2","NULL","0","SI","NULL");
        createElement(18, "1","t","Motivo de la Solicitud","NULL"                       ,"1","AN","2","5","NULL","NULL","NULL","NULL");

        //Tramite 2
        createElement(19, "2","t","Lugar o direccion en Manizales donde sucedio el evento","NULL" ,"1","AN","1","1","NULL","NULL","NULL","NULL");
        createElement(20, "2","r","Barrio o Vereda","TLUG"                              ,"1","NULL","1","2","NULL","NULL","NULL","NULL");
        createElement(21, "2","s","Barrio","BAR"                                        ,"1","NULL","1","3","20","0","Barrio","NULL");
        createElement(25, "2","s","Vereda","VER"                                        ,"1","NULL","1","3","20","0","Vereda","NULL");
        createElement(22, "2","t","Motivo de la Solicitud","NULL"                       ,"1","AN","1","4","NULL","NULL","NULL","NULL");
        createElement(28, "2","l","Adjuntos","NULL"                      ,"0","NULL","1","4","NULL","NULL","NULL","NULL");
        createElement(26, "2","f","Foto","NULL"                                         ,"1","NULL","2","1","NULL","0","SI","NULL");
        createElement(27, "2","v","Video","NULL"                                        ,"1","NULL","2","2","NULL","0","SI","NULL");

    }


    public String createElement(long id, String traId, String eleTipo, String eleEtiqueta
            , String opcGrupo, String eleRequerido, String eleValidacion, String elePagina
            , String eleOrden, String eleParentId, String eleParentType, String eleParentValue
            , String eleFormEleId)
    {
        Integer errors = 0;
        try
        {
            this.db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put("eleId", id);
            values.put("traId", traId);
            values.put("eleFormEleId", eleFormEleId);
            values.put("eleTipo", eleTipo);
            values.put("eleEtiqueta", eleEtiqueta);
            values.put("eleRequerido", eleRequerido);
            values.put("eleValidacion", eleValidacion);
            values.put("elePagina", (elePagina.equals("NULL")?null:elePagina));
            values.put("eleOrden", eleOrden);

            values.put("eleParentId", eleParentId);
            values.put("eleParentType", eleParentType);
            values.put("eleParentValue", eleParentValue);
            values.put("opcGrupo", opcGrupo);

            id = this.db.insert(this._tabla, null, values);
            this.db.setTransactionSuccessful();
            this.db.endTransaction();
        }
        catch(Exception ex)
        {
           return "-1";
        }

        if(errors > 0)
        {
            return "-1";
        }

        return String.valueOf(id);
    }

    public String createOpciones()
    {
        return "";
    }

    public List<Elemento> getElementos(String pagina, String idTramite)
    {
        return getElementos(pagina, idTramite, null);
    }

    public List<Elemento> getElementos(String pagina, String idTramite, String idRegistro)
    {
        List<Elemento> listaElementos  = new ArrayList<Elemento>();
        String selectQuery =
                "SELECT e.eleId,e.traId,e.eleFormEleId,e.eleTipo,e.eleEtiqueta,e.eleOpciones,e.eleRequerido,e.eleValidacion" +
                        ",e.elePagina,e.eleOrden,e.opcGrupo ";


        if(idRegistro != null)
            selectQuery += ",r.resValor ";

        selectQuery += "FROM " + this._tabla + " as e ";

        if(idRegistro != null)
            selectQuery += "LEFT JOIN frm_trf_elementoRespuesta r ON e.eleId = r.eleId AND r.regId = '" + idRegistro + "' ";

                selectQuery += "WHERE e.traId = '" + idTramite + "' ";

                if(pagina != "0")
                    selectQuery += " AND e.elePagina = '" + pagina + "' ";

                selectQuery += "ORDER BY e.elePagina, e.eleOrden ";

                Cursor cursor = db.rawQuery(selectQuery, null);
                Elemento objElemento = null;
                if (cursor.moveToFirst()) {
                    do {
                        objElemento = new Elemento();
                        objElemento.setId(cursor.getString(0));
                        objElemento.setTramiteId(cursor.getString(1));
                        objElemento.setFormId(cursor.getString(2));
                        objElemento.setTipo(cursor.getString(3));
                        objElemento.setEtiqueta(cursor.getString(4));
                        objElemento.setOpciones(cursor.getString(5));
                        objElemento.setRequerido(cursor.getString(6));
                        objElemento.setValidacion(cursor.getString(7));
                        objElemento.setPagina(cursor.getString(8));
                        objElemento.setOrden(cursor.getString(9));
                        objElemento.setGrupo(cursor.getString(10));
                if(idRegistro != null)
                    objElemento.setRepuesta(cursor.getString(11));
                else
                    objElemento.setRepuesta("");

                listaElementos.add(objElemento);
            } while (cursor.moveToNext());
        }
        return listaElementos;
    }

    public String getMaxPages(String traId)
    {
        String paginas = "0";
        String selectQuery = "SELECT max(elePagina) FROM frm_trf_elemento WHERE traId = " + traId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            paginas = cursor.getString(0);
        }

        return paginas;
    }


}
