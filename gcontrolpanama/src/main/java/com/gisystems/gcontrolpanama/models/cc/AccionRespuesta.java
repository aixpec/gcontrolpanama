package com.gisystems.gcontrolpanama.models.cc;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by rlemus on 15/08/2016.
 */
public class AccionRespuesta {

    private int idAccionRespuesta;
    private String nombreAccionRespuesta;
    private String descAccionRespuesta;

    public int getIdAccionRespuesta() {return idAccionRespuesta;}

    public void setIdAccionRespuesta(int idAccionRespuesta) {this.idAccionRespuesta = idAccionRespuesta;}

    public String getNombreAccionRespuesta() {return nombreAccionRespuesta;}

    public void setNombreAccionRespuesta(String nombreAccionRespuesta) {this.nombreAccionRespuesta = nombreAccionRespuesta;}

    public String getDescAccionRespuesta() {return descAccionRespuesta;}

    public void setDescAccionRespuesta(String descAccionRespuesta) {this.descAccionRespuesta = descAccionRespuesta;}

    public static final String NOMBRE_TABLA 				="tblCcAccionRespuesta";
    public static final String COLUMN_ID_ACCION_RESPUESTA	="IdAccionRespuesta";
    public static final String COLUMN_NOMBRE_ACCION_RESPUESTA	="NombreAccionRespuesta";
    public static final String COLUMN_DESC_ACCION_RESPUESTA		="DescAccionRespuesta";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_ACCION_RESPUESTA 					+ " integer not null, "
            + COLUMN_NOMBRE_ACCION_RESPUESTA				+ " text not null, "
            + COLUMN_DESC_ACCION_RESPUESTA				    + " text not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_ACCION_RESPUESTA + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(AccionRespuesta.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }

}
