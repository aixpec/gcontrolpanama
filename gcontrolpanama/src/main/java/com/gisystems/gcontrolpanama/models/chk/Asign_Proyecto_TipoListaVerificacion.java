package com.gisystems.gcontrolpanama.models.chk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.gcontrolpanama.models.Proyecto;

import java.util.ArrayList;

/**
 * Created by rlemus on 11/10/2016.
 */
public class Asign_Proyecto_TipoListaVerificacion {

    public static final String NOMBRE_TABLA 				        ="tblChkAsignTiposListaVerificacionProyectos";
    public static final String COLUMN_ID_CLIENTE			        ="IdCliente";
    public static final String COLUMN_ID_TIPO_LISTA_VERIFICACION    ="IdTipoListaVerificacion";
    public static final String COLUMN_ID_PROYECTO	                ="IdProyecto";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_TIPO_LISTA_VERIFICACION 	+ " integer not null, "
            + COLUMN_ID_PROYECTO 				+ " integer not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", " + COLUMN_ID_TIPO_LISTA_VERIFICACION + ", "  + COLUMN_ID_PROYECTO + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_TIPO_LISTA_VERIFICACION + " ) REFERENCES " + TipoListaVerificacion.NOMBRE_TABLA + "("   + TipoListaVerificacion.COLUMN_ID_CLIENTE + "," + TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_PROYECTO + " ) REFERENCES " + Proyecto.NOMBRE_TABLA + "("   + Proyecto.COLUMN_ID_CLIENTE + "," + Proyecto.COLUMN_ID + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Asign_Proyecto_TipoListaVerificacion.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }



}
