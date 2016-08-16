package com.gisystems.gcontrolpanama.models.cc;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by rlemus on 15/08/2016.
 */
public class ClaseIndicador {

    private int idClaseIndicador;
    private String descripcion;

    public int getIdClaseIndicador() {return idClaseIndicador;}

    public void setIdClaseIndicador(int IdClaseIndicador) {this.idClaseIndicador = idClaseIndicador;}

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public static final String NOMBRE_TABLA 				="tblCcClaseIndicador";
    public static final String COLUMN_ID_CLASE_INDICADOR	="IdClaseIndicador";
    public static final String COLUMN_DESCRIPCION			="Descripcion";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLASE_INDICADOR 					+ " integer not null, "
            + COLUMN_DESCRIPCION				    + " text not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLASE_INDICADOR + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ClaseIndicador.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }

}
