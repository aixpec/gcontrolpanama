package com.gisystems.gcontrolpanama.models.chk;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.gcontrolpanama.models.cc.Indicador;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.Respuesta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rlemus on 15/08/2016.
 */
public class TipoListaVerificacion_Seccion {

    private int idCliente;
    private int idTipoListaVerificacion;
    private int idSeccion;
    private int idConfiguracion;
    private int idIndicador;
    private String nombre;
    private int deDatosGenerales;
    private int deObservaciones;
    private int cantidadPreguntas;
    private int cantidadPreguntasRespondidas;
    private int cantidadPreguntasRequeridas;
    private int cantidadPreguntasRequeridasSinResponder;
    private int cantidadRespuestasInconforme;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdTipoListaVerificacion() {
        return idTipoListaVerificacion;
    }

    public void setIdTipoListaVerificacion(int idTipoListaVerificacion) {
        this.idTipoListaVerificacion = idTipoListaVerificacion;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(int idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public int getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(int idIndicador) {
        this.idIndicador = idIndicador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDeDatosGenerales() {
        return deDatosGenerales;
    }

    public void setDeDatosGenerales(int deDatosGenerales) {
        this.deDatosGenerales = deDatosGenerales;
    }

    public int getDeObservaciones() {
        return deObservaciones;
    }

    public void setDeObservaciones(int deObservaciones) {
        this.deObservaciones = deObservaciones;
    }

    public int getCantidadPreguntas() {
        return cantidadPreguntas;
    }

    public void setCantidadPreguntas(int cantidadPreguntas) {
        this.cantidadPreguntas = cantidadPreguntas;
    }

    public int getCantidadPreguntasRespondidas() {
        return cantidadPreguntasRespondidas;
    }

    public void setCantidadPreguntasRespondidas(int cantidadPreguntasRespondidas) {
        this.cantidadPreguntasRespondidas = cantidadPreguntasRespondidas;
    }

    public int getCantidadPreguntasRequeridas() {
        return cantidadPreguntasRequeridas;
    }

    public void setCantidadPreguntasRequeridas(int cantidadPreguntasRequeridas) {
        this.cantidadPreguntasRequeridas = cantidadPreguntasRequeridas;
    }

    public int getCantidadPreguntasRequeridasSinResponder() {
        return cantidadPreguntasRequeridasSinResponder;
    }

    public void setCantidadPreguntasRequeridasSinResponder(int cantidadPreguntasRequeridasSinResponder) {
        this.cantidadPreguntasRequeridasSinResponder = cantidadPreguntasRequeridasSinResponder;
    }

    public int getCantidadRespuestasInconforme() {
        return cantidadRespuestasInconforme;
    }

    public void setCantidadRespuestasInconforme(int cantidadRespuestasInconforme) {
        this.cantidadRespuestasInconforme = cantidadRespuestasInconforme;
    }

    public static final String NOMBRE_TABLA 				        ="tblChkTipoListaVerificacion_Seccion";
    public static final String COLUMN_ID_CLIENTE			        ="IdCliente";
    public static final String COLUMN_ID_TIPO_LISTA_VERIFICACION    ="IdTipoListaVerificacion";
    public static final String COLUMN_ID_SECCION			        ="IdSeccion";
    public static final String COLUMN_ID_CONFIGURACION	            ="IdConfiguracion";
    public static final String COLUMN_ID_INDICADOR	                ="IdIndicador";
    public static final String COLUMN_NOMBRE			            ="Nombre";
    public static final String COLUMN_DE_DATOS_GENERALES	        ="DeDatosGenerales";
    public static final String COLUMN_DE_OBSERVACIONES		        ="DeObservaciones";

    public static final String COLUMN_CANTIDAD_PREGUNTAS		        ="cantidadPreguntas";
    public static final String COLUMN_CANTIDAD_PREGUNTAS_RESPONDIDAS	="cantidadPreguntasRespondidas";
    public static final String COLUMN_CANTIDAD_PREGUNTAS_REQUERIDAS		="cantidadPreguntasRequeridas";
    public static final String COLUMN_CANTIDAD_PREGUNTAS_REQUERIDAS_SIN_RESPONDER	="cantidadPreguntasRequeridasSinResponder";
    public static final String COLUMN_CANTIDAD_RESPUESTAS_INCONFORME    ="cantidadRespuestasInconforme";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_TIPO_LISTA_VERIFICACION 	+ " integer not null, "
            + COLUMN_ID_SECCION 				    + " integer not null, "
            + COLUMN_ID_CONFIGURACION 				+ " integer not null, "
            + COLUMN_ID_INDICADOR 				    + " integer not null, "
            + COLUMN_NOMBRE				            + " text not null, "
            + COLUMN_DE_DATOS_GENERALES 			+ " integer not null, "
            + COLUMN_DE_OBSERVACIONES 				+ " integer not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_TIPO_LISTA_VERIFICACION + ", "  + COLUMN_ID_SECCION + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + " ) REFERENCES " + Indicador.NOMBRE_TABLA + "("   + Indicador.COLUMN_ID_CLIENTE + "," + Indicador.COLUMN_ID_CONFIGURACION + "," + Indicador.COLUMN_ID_INDICADOR + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_TIPO_LISTA_VERIFICACION + " ) REFERENCES " + TipoListaVerificacion.NOMBRE_TABLA + "("   + TipoListaVerificacion.COLUMN_ID_CLIENTE + "," + TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TipoListaVerificacion_Seccion.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }

    public String getDescripcionEstado() {
        String resultado = "No Finalizada";
        if (cantidadPreguntasRequeridasSinResponder == 0) {
            if (cantidadRespuestasInconforme > 0) {
                resultado = "Finalizada con Inconformidades";
            } else {
                resultado = "Tarea Finalizada";
            }
        }
        return resultado;
    }

    public int getIdImageResource() {
        int img_resource;
        if (cantidadPreguntasRequeridasSinResponder == 0) {
            if (cantidadRespuestasInconforme > 0) {
                img_resource = EstadoListaVerificacion.getIdImageResource(EstadoListaVerificacion.ID_FINALIZADO_CON_INCONFORMIDADES);
            } else {
                img_resource = EstadoListaVerificacion.getIdImageResource(EstadoListaVerificacion.ID_FINALIZADO);
            }
        } else {
            img_resource = EstadoListaVerificacion.getIdImageResource(EstadoListaVerificacion.ID_SIN_FINALIZAR);
        }
        return img_resource;
    }

    //Devuelve las secciones (tareas) por lista de verificación
    public static ArrayList<TipoListaVerificacion_Seccion> obtenerSecciones_X_ListaVerificacion(Context ctx, int idCliente,
                                                                                    int idListaVerificacion,
                                                                                    int idTipoListaVerificacion){
        DAL w = new DAL(ctx);
        ArrayList<TipoListaVerificacion_Seccion> secciones = new ArrayList<TipoListaVerificacion_Seccion>();
        TipoListaVerificacion_Seccion seccion;

        Cursor c = null;

        try{
            String query = "Select  "
                    + " S." + TipoListaVerificacion_Seccion.COLUMN_ID_SECCION + ", "
                    + " S." + TipoListaVerificacion_Seccion.COLUMN_ID_CONFIGURACION + ", "
                    + " S." + TipoListaVerificacion_Seccion.COLUMN_ID_INDICADOR + ", "
                    + " S." + TipoListaVerificacion_Seccion.COLUMN_NOMBRE + ", "
                    + " (SELECT COUNT(*)"
                    + "  FROM " + Pregunta.NOMBRE_TABLA + " P"
                    + "  WHERE P." + Pregunta.COLUMN_ID_CLIENTE + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE
                    + "   AND P." + Pregunta.COLUMN_ID_CONFIGURACION + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CONFIGURACION
                    + "   AND P." + Pregunta.COLUMN_ID_INDICADOR + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_INDICADOR
                    + "   AND P." + Pregunta.COLUMN_REQUERIDO + " = 1) as " + TipoListaVerificacion_Seccion.COLUMN_CANTIDAD_PREGUNTAS_REQUERIDAS + ", "
                    + " (SELECT COUNT(*) "
                    + "  FROM " + Pregunta.NOMBRE_TABLA + " P"
                    + "  LEFT OUTER JOIN " + ListaVerificacion_Respuesta.NOMBRE_TABLA + " R"
                    + "    ON R." + ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + " = P." + Pregunta.COLUMN_ID_CLIENTE
                    + "   AND R." + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + " = " + idListaVerificacion
                    + "   AND R." + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + " = P." + Pregunta.COLUMN_ID_CONFIGURACION
                    + "   AND R." + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + " = P." + Pregunta.COLUMN_ID_INDICADOR
                    + "   AND R." + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + " = P." + Pregunta.COLUMN_ID_PREGUNTA
                    + "   AND R." + ListaVerificacion_Respuesta.COLUMN_ELIMINADO + " = 0"
                    + "  WHERE P." + Pregunta.COLUMN_ID_CLIENTE + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE
                    + "   AND P." + Pregunta.COLUMN_ID_CONFIGURACION + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CONFIGURACION
                    + "   AND P." + Pregunta.COLUMN_ID_INDICADOR + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_INDICADOR
                    + "   AND P." + Pregunta.COLUMN_REQUERIDO + " = 1"
                    + "   AND R." + ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA + " is null  ) as " + TipoListaVerificacion_Seccion.COLUMN_CANTIDAD_PREGUNTAS_REQUERIDAS_SIN_RESPONDER + ", "
                    + "  (SELECT COUNT(*)"
                    + "   FROM " + ListaVerificacion_Respuesta.NOMBRE_TABLA + " R "
                    + "   WHERE R." + ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE
                    + "     AND R." + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + " = " + idListaVerificacion
                    + "     AND R." + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CONFIGURACION
                    + "     AND R." + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_INDICADOR
                    + "     AND R." + ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA + " = 1"
                    + "     AND (R." + ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA + " like 'NC%') "
                    + "     AND S." + TipoListaVerificacion_Seccion.COLUMN_DE_DATOS_GENERALES + " = 0"
                    + "     AND S." + TipoListaVerificacion_Seccion.COLUMN_DE_OBSERVACIONES + " = 0"
                    + "     AND R." + ListaVerificacion_Respuesta.COLUMN_ELIMINADO + " = 0"
                    + "   ) as " + TipoListaVerificacion_Seccion.COLUMN_CANTIDAD_RESPUESTAS_INCONFORME
                    + " FROM " + TipoListaVerificacion_Seccion.NOMBRE_TABLA + " S "
                    + " WHERE S." + TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE + " = " + String.valueOf(idCliente)
                    + "   and S." + TipoListaVerificacion_Seccion.COLUMN_ID_TIPO_LISTA_VERIFICACION + " = " + String.valueOf(idTipoListaVerificacion);
            
            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    seccion=new TipoListaVerificacion_Seccion();
                    seccion.setIdCliente(idCliente);
                    seccion.setIdTipoListaVerificacion(idTipoListaVerificacion);
                    seccion.setIdSeccion(c.getInt(c.getColumnIndexOrThrow(TipoListaVerificacion_Seccion.COLUMN_ID_SECCION)));
                    seccion.setIdConfiguracion(c.getInt(c.getColumnIndexOrThrow(TipoListaVerificacion_Seccion.COLUMN_ID_CONFIGURACION)));
                    seccion.setIdIndicador(c.getInt(c.getColumnIndexOrThrow(TipoListaVerificacion_Seccion.COLUMN_ID_INDICADOR)));
                    seccion.setNombre(c.getString(c.getColumnIndexOrThrow(TipoListaVerificacion_Seccion.COLUMN_NOMBRE)));

                    seccion.setCantidadPreguntasRequeridas(c.getInt(c.getColumnIndexOrThrow(TipoListaVerificacion_Seccion.COLUMN_CANTIDAD_PREGUNTAS_REQUERIDAS)));
                    seccion.setCantidadPreguntasRequeridasSinResponder(c.getInt(c.getColumnIndexOrThrow(TipoListaVerificacion_Seccion.COLUMN_CANTIDAD_PREGUNTAS_REQUERIDAS_SIN_RESPONDER)));
                    seccion.setCantidadRespuestasInconforme(c.getInt(c.getColumnIndexOrThrow(TipoListaVerificacion_Seccion.COLUMN_CANTIDAD_RESPUESTAS_INCONFORME)));

                    secciones.add(seccion);
                }
                while(c.moveToNext());
                c.close();
            }
        }
        catch (Exception e){
            ManejoErrores.registrarError(ctx, e,
                    TipoListaVerificacion_Seccion.class.getSimpleName(), "obtenerSecciones_X_ListaVerificacion",
                    null, null);
        }

        return secciones;
    }


    //Devuelve las preguntas por sección de la lista de verificación
    public static ArrayList<Pregunta> obtenerPreguntas_X_Seccion(Context ctx, int idCliente,
                                                                int idTipoListaVerificacion,
                                                                int idSeccion){
        DAL w = new DAL(ctx);
        ArrayList<Pregunta> preguntas = new ArrayList<>();
        Pregunta pregunta;

        Cursor c;

        try {
            String query = "Select  "
                    + " P." + Pregunta.COLUMN_ID_CONFIGURACION + ", "
                    + " P." + Pregunta.COLUMN_ID_INDICADOR + ", "
                    + " P." + Pregunta.COLUMN_ID_PREGUNTA + ", "
                    + " P." + Pregunta.COLUMN_ID_TIPO_DATO + ", "
                    + " P." + Pregunta.COLUMN_PREGUNTA + ", "
                    + " P." + Pregunta.COLUMN_REQUERIDO + ", "
                    + " I." + Indicador.COLUMN_DESCRIPCION
                    + " FROM " + TipoListaVerificacion_Seccion.NOMBRE_TABLA + " S "
                    + " JOIN " + Pregunta.NOMBRE_TABLA + " P "
                    + "   ON P." + Pregunta.COLUMN_ID_CLIENTE + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE
                    + "  AND P." + Pregunta.COLUMN_ID_CONFIGURACION + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CONFIGURACION
                    + "  AND P." + Pregunta.COLUMN_ID_INDICADOR + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_INDICADOR
                    + " JOIN " + Indicador.NOMBRE_TABLA + " I "
                    + "   ON I." + Indicador.COLUMN_ID_CLIENTE + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE
                    + "  AND I." + Indicador.COLUMN_ID_CONFIGURACION + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_CONFIGURACION
                    + "  AND I." + Indicador.COLUMN_ID_INDICADOR + " = S." + TipoListaVerificacion_Seccion.COLUMN_ID_INDICADOR
                    + " WHERE S." + TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE + " = " + String.valueOf(idCliente)
                    + "   and S." + TipoListaVerificacion_Seccion.COLUMN_ID_TIPO_LISTA_VERIFICACION + " = " + String.valueOf(idTipoListaVerificacion)
                    + "   and S." + TipoListaVerificacion_Seccion.COLUMN_ID_SECCION + " = " + String.valueOf(idSeccion);

            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    pregunta=new Pregunta();
                    pregunta.setIdCliente(idCliente);
                    pregunta.setIdConfiguracion(c.getInt(c.getColumnIndexOrThrow(Pregunta.COLUMN_ID_CONFIGURACION)));
                    pregunta.setIdIndicador(c.getInt(c.getColumnIndexOrThrow(Pregunta.COLUMN_ID_INDICADOR)));
                    pregunta.setIdPregunta(c.getInt(c.getColumnIndexOrThrow(Pregunta.COLUMN_ID_PREGUNTA)));
                    pregunta.setIdTipoDato(c.getInt(c.getColumnIndexOrThrow(Pregunta.COLUMN_ID_TIPO_DATO)));
                    pregunta.setPregunta(c.getString(c.getColumnIndexOrThrow(Pregunta.COLUMN_PREGUNTA)));
                    pregunta.setRequerido(c.getInt(c.getColumnIndexOrThrow(Pregunta.COLUMN_REQUERIDO)) == 1 );
                    pregunta.setIndicador(c.getString(c.getColumnIndexOrThrow(Indicador.COLUMN_DESCRIPCION)));
                    pregunta.setRespuestas(Respuesta.obtenerRespuestas_X_Pregunta(ctx,pregunta));
                    preguntas.add(pregunta);
                }
                while(c.moveToNext());
                c.close();
            }
        }
        catch (Exception e){
            ManejoErrores.registrarError(ctx, e,
                    TipoListaVerificacion_Seccion.class.getSimpleName(), "obtenerPreguntas_X_Seccion",
                    null, null);
        }
        return preguntas;
    }



}
