package com.gisystems.gcontrolpanama.models.cc;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by rlemus on 15/08/2016.
 */
public class Pregunta {

    private int idCliente;
    private int idConfiguracion;
    private int idIndicador;
    private int idPregunta;
    private int idTipoDato;
    private String pregunta;
    private boolean requerido;

    public int getIdCliente() {return idCliente;}

    public void setIdCliente(int idCliente) {this.idCliente = idCliente;}

    public int getIdConfiguracion() {return idConfiguracion;}

    public void setIdConfiguracion(int idConfiguracion) {this.idConfiguracion = idConfiguracion;}

    public int getIdIndicador() {return idIndicador;}

    public void setIdIndicador(int idIndicador) {this.idIndicador = idIndicador;}

    public int getIdPregunta() {return idPregunta;}

    public void setIdPregunta(int idPregunta) {this.idPregunta = idPregunta;}

    public int getIdTipoDato() {return idTipoDato;}

    public void setIdTipoDato(int idTipoDato) {this.idTipoDato = idTipoDato;}

    public String getPregunta() {return pregunta;}

    public void setPregunta(String descripcion) {this.pregunta = pregunta;}

    public boolean getRequerido() {return requerido;}

    public void setRequerido(boolean requerido) {this.requerido = requerido;}

    public static final String NOMBRE_TABLA 				="tblCcPregunta";
    public static final String COLUMN_ID_CLIENTE			="IdCliente";
    public static final String COLUMN_ID_CONFIGURACION		="IdConfiguracion";
    public static final String COLUMN_ID_INDICADOR		    ="IdIndicador";
    public static final String COLUMN_ID_PREGUNTA	    	="IdPregunta";
    public static final String COLUMN_ID_TIPO_DATO  		="IdTipoDato";
    public static final String COLUMN_PREGUNTA  			="Pregunta";
    public static final String COLUMN_REQUERIDO			    ="Requerido";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_CONFIGURACION 				+ " integer not null, "
            + COLUMN_ID_INDICADOR 				    + " integer not null, "
            + COLUMN_ID_PREGUNTA 			        + " integer not null, "
            + COLUMN_ID_TIPO_DATO 				    + " integer not null, "
            + COLUMN_PREGUNTA				        + " text not null, "
            + COLUMN_REQUERIDO				        + " integer not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + ", "  + COLUMN_ID_PREGUNTA +  "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR +  " ) REFERENCES " + Indicador.NOMBRE_TABLA + "("   + Indicador.COLUMN_ID_CLIENTE + "," + Indicador.COLUMN_ID_CONFIGURACION + "," + Indicador.COLUMN_ID_INDICADOR + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_TIPO_DATO   + " ) REFERENCES " + TipoDato.NOMBRE_TABLA + "("   + TipoDato.COLUMN_ID_TIPO_DATO + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Pregunta.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }

}
