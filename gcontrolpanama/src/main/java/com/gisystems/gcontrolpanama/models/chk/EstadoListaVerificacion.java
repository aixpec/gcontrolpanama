package com.gisystems.gcontrolpanama.models.chk;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by rlemus on 19/08/2016.
 */
public class EstadoListaVerificacion {

    private int IdEstadoListaVerificacion;
    private String descripcion;
    private boolean listarEnAppMovil;

    public int getIdEstadoListaVerificacion() {
        return IdEstadoListaVerificacion;
    }

    public void setIdEstadoListaVerificacion(int idEstadoListaVerificacion) {
        IdEstadoListaVerificacion = idEstadoListaVerificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isListarEnAppMovil() {
        return listarEnAppMovil;
    }

    public void setListarEnAppMovil(boolean listarEnAppMovil) {
        this.listarEnAppMovil = listarEnAppMovil;
    }



    public static final String NOMBRE_TABLA 				="tblChkEstadoListaVerificacion";
    public static final String COLUMN_ID_ESTADO_LISTA_VERIFICACION 		="IdEstadoListaVerificacion";
    public static final String COLUMN_DESCRIPCION			="Descripcion";
    public static final String COLUMN_LISTAR_EN_APP_MOVIL	="ListarEnAppMovil";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_ESTADO_LISTA_VERIFICACION 			+ " integer not null, "
            + COLUMN_DESCRIPCION				            + " text not null, "
            + COLUMN_LISTAR_EN_APP_MOVIL				    + " integer not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_ESTADO_LISTA_VERIFICACION + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(EstadoListaVerificacion.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }


}
