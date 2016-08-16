package com.gisystems.gcontrolpanama.models.cc;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by rlemus on 15/08/2016.
 */
public class Respuesta {

    private int idCliente;
    private int idConfiguracion;
    private int idIndicador;
    private int idPregunta;
    private int idRespuesta;
    private String respuesta;

    public int getIdCliente() {return idCliente;}

    public void setIdCliente(int idCliente) {this.idCliente = idCliente;}

    public int getIdConfiguracion() {return idConfiguracion;}

    public void setIdConfiguracion(int idConfiguracion) {this.idConfiguracion = idConfiguracion;}

    public int getIdIndicador() {return idIndicador;}

    public void setIdIndicador(int idIndicador) {this.idIndicador = idIndicador;}

    public int getIdPregunta() {return idPregunta;}

    public void setIdPregunta(int idPregunta) {this.idPregunta = idPregunta;}

    public int getIdRespuesta() {return idRespuesta;}

    public void setIdRespuesta(int idRespuesta) {this.idRespuesta = idRespuesta;}

    public String getRespuesta() {return respuesta;}

    public void setRespuesta(String respuesta) {this.respuesta = respuesta;}

    public static final String NOMBRE_TABLA 				="tblCcRespuesta";
    public static final String COLUMN_ID_CLIENTE			="IdCliente";
    public static final String COLUMN_ID_CONFIGURACION		="IdConfiguracion";
    public static final String COLUMN_ID_INDICADOR		    ="IdIndicador";
    public static final String COLUMN_ID_PREGUNTA	    	="IdPregunta";
    public static final String COLUMN_ID_RESPUESTA  		="IdRespuesta";
    public static final String COLUMN_RESPUESTA  			="Respuesta";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_CONFIGURACION 				+ " integer not null, "
            + COLUMN_ID_INDICADOR 				    + " integer not null, "
            + COLUMN_ID_PREGUNTA 			        + " integer not null, "
            + COLUMN_ID_RESPUESTA 				    + " integer not null, "
            + COLUMN_RESPUESTA				        + " text not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + ", "  + COLUMN_ID_PREGUNTA + ", "  + COLUMN_ID_RESPUESTA +  "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + ", "  + COLUMN_ID_PREGUNTA +  " ) REFERENCES " + Pregunta.NOMBRE_TABLA + "("   + Pregunta.COLUMN_ID_CLIENTE + "," + Pregunta.COLUMN_ID_CONFIGURACION + "," + Pregunta.COLUMN_ID_INDICADOR + "," + Pregunta.COLUMN_ID_PREGUNTA  + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Respuesta.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }

}
