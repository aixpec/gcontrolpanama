package com.gisystems.gcontrolpanama.models.chk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.gcontrolpanama.models.cc.Configuracion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rlemus on 15/08/2016.
 */
public class TipoListaVerificacion {

    private int idCliente;
    private int idTipoListaVerificacion;
    private int idConfiguracion;
    private String descripcion;

    public int getIdCliente() {return idCliente;}

    public void setIdCliente(int idCliente) {this.idCliente = idCliente;}

    public int getIdTipoListaVerificacion() {return idTipoListaVerificacion;}

    public void setIdTipoListaVerificacion(int idTipoListaVerificacion) {this.idTipoListaVerificacion = idTipoListaVerificacion;}

    public int getIdConfiguracion() {return idConfiguracion;}

    public void setIdConfiguracion(int idConfiguracion) {this.idConfiguracion = idConfiguracion;}

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    @Override
    public String toString() {
        return this.descripcion;
    }


    public static final String NOMBRE_TABLA 				        ="tblChkTipoListaVerificacion";
    public static final String COLUMN_ID_CLIENTE			        ="IdCliente";
    public static final String COLUMN_ID_TIPO_LISTA_VERIFICACION    ="IdTipoListaVerificacion";
    public static final String COLUMN_ID_CONFIGURACION	            ="IdConfiguracion";
    public static final String COLUMN_DESCRIPCION			        ="Descripcion";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_TIPO_LISTA_VERIFICACION 	+ " integer not null, "
            + COLUMN_ID_CONFIGURACION 				+ " integer not null, "
            + COLUMN_DESCRIPCION				    + " text not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_TIPO_LISTA_VERIFICACION + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION + " ) REFERENCES " + Configuracion.NOMBRE_TABLA + "("   + Configuracion.COLUMN_ID_CLIENTE + "," + Configuracion.COLUMN_ID_CONFIGURACION + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TipoListaVerificacion.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }


    public ArrayList<TipoListaVerificacion> obtenerListadoTipos_X_Proyecto(Context ctx,
                                                                           int idCliente,
                                                                           int idProyecto) {
        DAL w = new DAL(ctx);
        ArrayList<TipoListaVerificacion> tipos = new ArrayList<>();
        TipoListaVerificacion tipo;

        Cursor c = null;

        try{
            String query = "Select "
                    + " T." + TipoListaVerificacion.COLUMN_ID_CLIENTE + ", "
                    + " T." + TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + ", "
                    + " T." + TipoListaVerificacion.COLUMN_DESCRIPCION + ", "
                    + " T." + TipoListaVerificacion.COLUMN_ID_CONFIGURACION
                    + " FROM " + Asign_Proyecto_TipoListaVerificacion.NOMBRE_TABLA + " A "
                    + " JOIN " + TipoListaVerificacion.NOMBRE_TABLA + " T "
                    + "   ON A." + Asign_Proyecto_TipoListaVerificacion.COLUMN_ID_CLIENTE + " = T." + TipoListaVerificacion.COLUMN_ID_CLIENTE
                    + "  AND A." + Asign_Proyecto_TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + " = T." + TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION
                    + " WHERE A." + Asign_Proyecto_TipoListaVerificacion.COLUMN_ID_CLIENTE + " = " + String.valueOf(idCliente)
                    + "   AND A." + Asign_Proyecto_TipoListaVerificacion.COLUMN_ID_PROYECTO + " = " + String.valueOf(idProyecto)
                    + " ORDER BY T." + TipoListaVerificacion.COLUMN_DESCRIPCION ;

            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    tipo=new TipoListaVerificacion();
                    tipo.setIdCliente(c.getInt(c.getColumnIndexOrThrow(TipoListaVerificacion.COLUMN_ID_CLIENTE)));
                    tipo.setIdTipoListaVerificacion(c.getInt(c.getColumnIndexOrThrow(TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION)));
                    tipo.setIdConfiguracion(c.getInt(c.getColumnIndexOrThrow(TipoListaVerificacion.COLUMN_ID_CONFIGURACION)));
                    tipo.setDescripcion(c.getString(c.getColumnIndexOrThrow(TipoListaVerificacion.COLUMN_DESCRIPCION)));
                    tipos.add(tipo);
                }
                while(c.moveToNext());
                c.close();
            }
        }
        catch (Exception e){
            ManejoErrores.registrarError(ctx, e,
                    TipoListaVerificacion.class.getSimpleName(), "obtenerListadoTipos",
                    null, null);
        }
        return tipos;
    }

}
