package com.gisystems.gcontrolpanama.models.cc;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by rlemus on 15/08/2016.
 */
public class TipoDato {

    private int idTipoDato;
    private String descripcion;

    public int getIdTipoDato() {return idTipoDato;}

    public void setIdTipoDato(int idTipoDato) {this.idTipoDato = idTipoDato;}

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public static final String NOMBRE_TABLA 				="tblCcTipoDato";
    public static final String COLUMN_ID_TIPO_DATO  		="IdTipoDato";
    public static final String COLUMN_DESCRIPCION			="Descripcion";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_TIPO_DATO 					+ " integer not null, "
            + COLUMN_DESCRIPCION				    + " text not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_TIPO_DATO + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TipoDato.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }

}
