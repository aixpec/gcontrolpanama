package com.gisystems.gcontrolpanama.models.cc;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by rlemus on 15/08/2016.
 */
public class RespuestaAccionDetalle {

    private int idCliente;
    private int idConfiguracion;
    private int idIndicador;
    private int idPregunta;
    private int idRespuesta;
    private int IdAccionRespuesta;
    private int idRespuestaAccionDetalleCorr;
    private String irAPregunta;
    private int DeshabilitarPreguntaCod;
    private int HabilitarPreguntaCod;
    private int CambiarLimitePreguntaCod;
    private int CodPregAplicarAccion;
    private int CodRespAplicarAccion;

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

    public int getIdAccionRespuesta() { return IdAccionRespuesta;  }

    public void setIdAccionRespuesta(int idAccionRespuesta) { IdAccionRespuesta = idAccionRespuesta; }

    public int getIdRespuestaAccionDetalleCorr() { return idRespuestaAccionDetalleCorr; }

    public void setIdRespuestaAccionDetalleCorr(int idRespuestaAccionDetalleCorr) { this.idRespuestaAccionDetalleCorr = idRespuestaAccionDetalleCorr; }

    public String getIrAPregunta() { return irAPregunta; }

    public void setIrAPregunta(String irAPregunta) { this.irAPregunta = irAPregunta;  }

    public int getDeshabilitarPreguntaCod() { return DeshabilitarPreguntaCod; }

    public void setDeshabilitarPreguntaCod(int deshabilitarPreguntaCod) { DeshabilitarPreguntaCod = deshabilitarPreguntaCod; }

    public int getHabilitarPreguntaCod() { return HabilitarPreguntaCod; }

    public void setHabilitarPreguntaCod(int habilitarPreguntaCod) { HabilitarPreguntaCod = habilitarPreguntaCod; }

    public int getCambiarLimitePreguntaCod() { return CambiarLimitePreguntaCod; }

    public void setCambiarLimitePreguntaCod(int cambiarLimitePreguntaCod) { CambiarLimitePreguntaCod = cambiarLimitePreguntaCod; }

    public int getCodPregAplicarAccion() { return CodPregAplicarAccion; }

    public void setCodPregAplicarAccion(int codPregAplicarAccion) { CodPregAplicarAccion = codPregAplicarAccion;  }

    public int getCodRespAplicarAccion() {  return CodRespAplicarAccion;   }

    public void setCodRespAplicarAccion(int codRespAplicarAccion) {  CodRespAplicarAccion = codRespAplicarAccion;   }

    public static final String NOMBRE_TABLA 				="tblCcRespuestaAccionDetalle";
    public static final String COLUMN_ID_CLIENTE			="IdCliente";
    public static final String COLUMN_ID_CONFIGURACION		="IdConfiguracion";
    public static final String COLUMN_ID_INDICADOR		    ="IdIndicador";
    public static final String COLUMN_ID_PREGUNTA	    	="IdPregunta";
    public static final String COLUMN_ID_RESPUESTA  		="IdRespuesta";
    public static final String COLUMN_ID_ACCION_RESPUESTA  			="IdAccionRespuesta";
    public static final String COLUMN_ID_RESPUESTA_ACCION_DETALLE_CORR  ="idRespuestaAccionDetalleCorr";
    public static final String COLUMN_IR_A_PREGUNTA  			    ="irAPregunta";
    public static final String COLUMN_DESHABILITAR_PREGUNTA_COD		="DeshabilitarPreguntaCod";
    public static final String COLUMN_HABILITAR_PREGUNTA_COD  		="HabilitarPreguntaCod";
    public static final String COLUMN_CAMBIAR_LIMITE_PREGUNTA_COD  	="CambiarLimitePreguntaCod";
    public static final String COLUMN_COD_PREG_APLICAR_ACCION  		="CodPregAplicarAccion";
    public static final String COLUMN_COD_RESP_APLICAR_ACCION  		="CodRespAplicarAccion";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_CONFIGURACION 				+ " integer not null, "
            + COLUMN_ID_INDICADOR 				    + " integer not null, "
            + COLUMN_ID_PREGUNTA 			        + " integer not null, "
            + COLUMN_ID_RESPUESTA 				    + " integer not null, "
            + COLUMN_ID_RESPUESTA_ACCION_DETALLE_CORR 	+ " integer not null, "
            + COLUMN_ID_ACCION_RESPUESTA 				+ " integer not null, "
            + COLUMN_IR_A_PREGUNTA				        + " text not null, "
            + COLUMN_DESHABILITAR_PREGUNTA_COD 			+ " integer not null, "
            + COLUMN_HABILITAR_PREGUNTA_COD 			+ " integer not null, "
            + COLUMN_CAMBIAR_LIMITE_PREGUNTA_COD 		+ " integer not null, "
            + COLUMN_COD_PREG_APLICAR_ACCION 			+ " integer not null, "
            + COLUMN_COD_RESP_APLICAR_ACCION 			+ " integer not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + ", "  + COLUMN_ID_PREGUNTA + ", " + COLUMN_ID_RESPUESTA + ", " + COLUMN_ID_RESPUESTA_ACCION_DETALLE_CORR +  "), "
            + "FOREIGN KEY ( " + COLUMN_ID_ACCION_RESPUESTA + " ) REFERENCES " + AccionRespuesta.NOMBRE_TABLA + "("   + AccionRespuesta.COLUMN_ID_ACCION_RESPUESTA + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(RespuestaAccionDetalle.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }

}
