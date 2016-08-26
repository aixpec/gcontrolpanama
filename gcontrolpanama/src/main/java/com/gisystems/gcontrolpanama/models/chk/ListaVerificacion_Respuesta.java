package com.gisystems.gcontrolpanama.models.chk;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.Respuesta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rlemus on 15/08/2016.
 */
public class ListaVerificacion_Respuesta {

    private int idCliente;
    private int idListaVerificacion;
    private int idConfiguracion;
    private int idIndicador;
    private int idPregunta;
    private int idListaVerificacionRespuesta;
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

    public int getIdListaVerificacionRespuesta() {
        return this.idListaVerificacionRespuesta;
    }

    public void setIdListaVerificacionRespuesta(int IdListaVerificacionRespuesta) {
        this.idListaVerificacionRespuesta = IdListaVerificacionRespuesta;
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
    public static final String COLUMN_ID_CONFIGURACION	            ="idConfiguracion";
    public static final String COLUMN_ID_INDICADOR	                ="IdIndicador";
    public static final String COLUMN_ID_PREGUNTA	                ="IdPregunta";
    public static final String COLUMN_ID_LISTA_VERIFICACION_RESP    ="IdListaVerificacionRespuesta";
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
            + COLUMN_ID_CONFIGURACION 	            + " integer not null, "
            + COLUMN_ID_INDICADOR 	                + " integer not null, "
            + COLUMN_ID_PREGUNTA		            + " integer not null, "
            + COLUMN_ID_LISTA_VERIFICACION_RESP		+ " integer not null, "
            + COLUMN_DESCRIPCION_INDICADOR			+ " text not null, "
            + COLUMN_DESCRIPCION_PREGUNTA			+ " text not null, "
            + COLUMN_ID_RESPUESTA		            + " integer null, "
            + COLUMN_DESCRIPCION_RESPUESTA			+ " text null, "
            + COLUMN_VALOR_RESPUESTA				+ " text not null, "
            + COLUMN_ESTADO_REGISTRO				+ " text not null, "
            + COLUMN_CREO_USUARIO				    + " text not null, "
            + COLUMN_CREO_FECHA				        + " text not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_LISTA_VERIFICACION + ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + ", "  + COLUMN_ID_PREGUNTA + ", " + COLUMN_ID_LISTA_VERIFICACION_RESP + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_LISTA_VERIFICACION + " ) REFERENCES " + ListaVerificacion.NOMBRE_TABLA + "("   + ListaVerificacion.COLUMN_ID_CLIENTE + "," + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + ")  ON UPDATE CASCADE, "
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

    public boolean ActualizarIdListaVerificacionRespuesta(Context ctx, int IdListaVerificacionRespuestaNuevo){
        boolean resultado=false;
        if (this.getIdListaVerificacionRespuesta() >= 0) {
            return false;
        }
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            //Actualizar el Id del avance
            ContentValues values = new ContentValues();
            values.put(ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION_RESP , IdListaVerificacionRespuestaNuevo);
            values.put(ListaVerificacion_Respuesta.COLUMN_ESTADO_REGISTRO, AppValues.EstadosEnvio.Enviado.name());
            String where=ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + "=" + String.valueOf(this.getIdCliente())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(this.getIdListaVerificacion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + "=" + String.valueOf(this.getIdConfiguracion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + "=" + String.valueOf(this.getIdIndicador())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + "=" + String.valueOf(this.getIdPregunta())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION_RESP + "=" + String.valueOf(this.getIdListaVerificacionRespuesta());
            resultado= (w.updateRow(ListaVerificacion.NOMBRE_TABLA, values, where)>0);

            if(resultado) {
                this.setIdListaVerificacionRespuesta(IdListaVerificacionRespuestaNuevo);
                this.setEstadoRegistro(AppValues.EstadosEnvio.Enviado.name());
            }
            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=false;
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "ActualizarIdListaVerificacionRespuesta",
                    null, null);
        }
        return resultado;
    }

    public boolean ActualizarEstadoRegistro(Context ctx, AppValues.EstadosEnvio nuevoEstado){
        boolean resultado=false;
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            //Actualizar el Id del avance
            ContentValues values = new ContentValues();
            values.put(ListaVerificacion_Respuesta.COLUMN_ESTADO_REGISTRO, nuevoEstado.name());
            String where=ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + "=" + String.valueOf(this.getIdCliente())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(this.getIdListaVerificacion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + "=" + String.valueOf(this.getIdConfiguracion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + "=" + String.valueOf(this.getIdIndicador())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + "=" + String.valueOf(this.getIdPregunta())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION_RESP + "=" + String.valueOf(this.getIdListaVerificacionRespuesta());
            resultado= (w.updateRow(ListaVerificacion_Respuesta.NOMBRE_TABLA, values, where)>0);

            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=false;
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "ActualizarEstadoRegistro",
                    null, null);
        }

        return resultado;
    }

}
