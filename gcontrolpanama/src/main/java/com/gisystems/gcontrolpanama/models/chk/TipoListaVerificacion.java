package com.gisystems.gcontrolpanama.models.chk;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.gcontrolpanama.models.cc.Configuracion;

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
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION + " ) REFERENCES " + Configuracion.NOMBRE_TABLA + "("   + Configuracion.COLUMN_ID_CLIENTE + "," + Configuracion.COLUMN_ID_CONFIGURACION + "), "
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

}
