package com.gisystems.gcontrolpanama.models.chk;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.Respuesta;

import java.util.Date;

/**
 * Created by rlemus on 15/08/2016.
 */
public class ListaVerificacion_Respuesta {

    private int idCliente;
    private int idListaVerificacion;
    private int idListaVerificacion_Temp;
    private int idConfiguracion;
    private int idIndicador;
    private int idPregunta;
    private String descripcionIndicador;
    private String descripcionPregunta;
    private int idRespuesta;
    private String descripcionRespuesta;
    private String valorRespuesta;
    private String estadoRegistro;
    private String creoUsuario;
    private Date creoFecha;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdListaVerificacion() {
        return idListaVerificacion;
    }

    public void setIdListaVerificacion(int idListaVerificacion) {
        this.idListaVerificacion = idListaVerificacion;
    }

    public int getIdListaVerificacion_Temp() {
        return idListaVerificacion_Temp;
    }

    public void setIdListaVerificacion_Temp(int idListaVerificacion_Temp) {
        this.idListaVerificacion_Temp = idListaVerificacion_Temp;
    }

    public int getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(int idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public int getIdIndicador() {
        return this.idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public int getIdPregunta() {
        return this.idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getDescripcionIndicador() {
        return this.descripcionIndicador;
    }

    public void setDescripcionIndicador(String descripcionIndicador) {
        this.descripcionIndicador = descripcionIndicador;
    }

    public String getDescripcionPregunta() {
        return this.descripcionPregunta;
    }

    public void setDescripcionPregunta(String descripcionPregunta) {
        this.descripcionPregunta = descripcionPregunta;
    }

    public int getIdRespuesta() {
        return this.idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getDescripcionRespuesta() {
        return this.descripcionRespuesta;
    }

    public void setDescripcionRespuesta(String descripcionRespuesta) {
        this.descripcionRespuesta = descripcionRespuesta;
    }

    public String getValorRespuesta() {
        return this.valorRespuesta;
    }

    public void setValorRespuesta(String valorRespuesta) {
        this.valorRespuesta = valorRespuesta;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    public String getCreoUsuario() {
        return creoUsuario;
    }

    public void setCreoUsuario(String creoUsuario) {
        this.creoUsuario = creoUsuario;
    }

    public Date getCreoFecha() {
        return creoFecha;
    }

    public void setCreoFecha(Date creoFecha) {
        this.creoFecha = creoFecha;
    }

    public static final String NOMBRE_TABLA 				        ="tblChkListaVerificacion_Respuesta";
    public static final String COLUMN_ID_CLIENTE			        ="IdCliente";
    public static final String COLUMN_ID_LISTA_VERIFICACION         ="IdListaVerificacion";
    public static final String COLUMN_ID_LISTA_VERIFICACION_TEMP    ="IdListaVerificacion_Temp";
    public static final String COLUMN_ID_CONFIGURACION	            ="idConfiguracion";
    public static final String COLUMN_ID_INDICADOR	                ="IdIndicador";
    public static final String COLUMN_ID_PREGUNTA	                ="IdPregunta";
    public static final String COLUMN_DESCRIPCION_INDICADOR		    ="DescripcionIndicador";
    public static final String COLUMN_DESCRIPCION_PREGUNTA		    ="DescripcionPregunta";
    public static final String COLUMN_ID_RESPUESTA	                ="IdRespuesta";
    public static final String COLUMN_DESCRIPCION_RESPUESTA		    ="DescripcionRespuesta";
    public static final String COLUMN_VALOR_RESPUESTA		        ="ValorRespuesta";
    public static final String COLUMN_ESTADO_REGISTRO		        ="EstadoRegistro";
    public static final String COLUMN_CREO_USUARIO			        ="CreoUsuario";
    public static final String COLUMN_CREO_FECHA			        ="CreoFecha";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_LISTA_VERIFICACION 	        + " integer not null, "
            + COLUMN_ID_LISTA_VERIFICACION_TEMP 	+ " integer not null, "
            + COLUMN_ID_CONFIGURACION 	            + " integer not null, "
            + COLUMN_ID_INDICADOR 	                + " integer not null, "
            + COLUMN_ID_PREGUNTA		            + " integer not null, "
            + COLUMN_DESCRIPCION_INDICADOR			+ " text not null, "
            + COLUMN_DESCRIPCION_PREGUNTA			+ " text not null, "
            + COLUMN_ID_RESPUESTA		            + " integer null, "
            + COLUMN_DESCRIPCION_RESPUESTA			+ " text null, "
            + COLUMN_VALOR_RESPUESTA				+ " text null, "
            + COLUMN_ESTADO_REGISTRO				+ " text not null, "
            + COLUMN_CREO_USUARIO				    + " text not null, "
            + COLUMN_CREO_FECHA				        + " text not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_LISTA_VERIFICACION + ", "  + COLUMN_ID_LISTA_VERIFICACION_TEMP + ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + ", "  + COLUMN_ID_PREGUNTA + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_LISTA_VERIFICACION + ", "  + COLUMN_ID_LISTA_VERIFICACION_TEMP + " ) REFERENCES " + ListaVerificacion.NOMBRE_TABLA + "("   + ListaVerificacion.COLUMN_ID_CLIENTE + "," + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + "," + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION_TEMP + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION +  ", "  + COLUMN_ID_INDICADOR +  ", "  + COLUMN_ID_PREGUNTA + " ) REFERENCES " + Pregunta.NOMBRE_TABLA + "("   + Pregunta.COLUMN_ID_CLIENTE + "," + Pregunta.COLUMN_ID_CONFIGURACION + "," + Pregunta.COLUMN_ID_INDICADOR + "," + Pregunta.COLUMN_ID_PREGUNTA + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION +  ", "  + COLUMN_ID_INDICADOR +  ", "  + COLUMN_ID_PREGUNTA +  ", "  + COLUMN_ID_RESPUESTA + " ) REFERENCES " + Respuesta.NOMBRE_TABLA + "("   + Respuesta.COLUMN_ID_CLIENTE + "," + Respuesta.COLUMN_ID_CONFIGURACION + "," + Respuesta.COLUMN_ID_INDICADOR + "," + Respuesta.COLUMN_ID_PREGUNTA + "," + Respuesta.COLUMN_ID_RESPUESTA + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ListaVerificacion_Respuesta.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }

}
