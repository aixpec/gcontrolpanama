package com.gisystems.gcontrolpanama.models.chk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.FotoActividad;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.cc.Indicador;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rlemus on 15/08/2016.
 */
public class ListaVerificacion {

    private int idCliente;
    private int idListaVerificacion;
    private int idProyecto;
    private int idTipoListaVerificacion;
    private String tipoListaVerificacion;
    private int idEstadoListaVerificacion;
    private String estadoListaVerificacion;
    private int listaCerrada;
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

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public int getIdTipoListaVerificacion() {
        return idTipoListaVerificacion;
    }

    public void setIdTipoListaVerificacion(int idTipoListaVerificacion) {
        this.idTipoListaVerificacion = idTipoListaVerificacion;
    }

    public String getTipoListaVerificacion() {
        return tipoListaVerificacion;
    }

    public void setTipoListaVerificacion(String tipoListaVerificacion) {
        this.tipoListaVerificacion = tipoListaVerificacion;
    }

    public int getIdEstadoListaVerificacion() {
        return idEstadoListaVerificacion;
    }

    public void setIdEstadoListaVerificacion(int idEstadoListaVerificacion) {
        this.idEstadoListaVerificacion = idEstadoListaVerificacion;
    }

    public String getEstadoListaVerificacion() {
        return estadoListaVerificacion;
    }

    public void setEstadoListaVerificacion(String estadoListaVerificacion) {
        this.estadoListaVerificacion = estadoListaVerificacion;
    }

    public int getListaCerrada() {
        return this.listaCerrada;
    }

    public void setListaCerrada(int listaCerrada) {
        this.listaCerrada = listaCerrada;
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

    public static final String NOMBRE_TABLA 				        ="tblChkListaVerificacion";
    public static final String COLUMN_ID_CLIENTE			        ="IdCliente";
    public static final String COLUMN_ID_LISTA_VERIFICACION         ="IdListaVerificacion";
    public static final String COLUMN_ID_PROYECTO	                ="IdProyecto";
    public static final String COLUMN_ID_TIPO_LISTA_VERIFICACION    ="IdTipoListaVerificacion";
    public static final String COLUMN_TIPO_LISTA_VERIFICACION       ="TipoListaVerificacion";
    public static final String COLUMN_ID_ESTADO_LISTA_VERIFICACION  ="IdEstadoListaVerificacion";
    public static final String COLUMN_ESTADO_LISTA_VERIFICACION     ="EstadoListaVerificacion";
    public static final String COLUMN_LISTA_CERRADA		            ="ListaCerrada";
    public static final String COLUMN_ESTADO_ENVIO		            ="EstadoEnvio";
    public static final String COLUMN_CREO_USUARIO			        ="CreoUsuario";
    public static final String COLUMN_CREO_FECHA			        ="CreoFecha";
    public static final String COLUMN_CONFIRMADO_AL_ACTUALIZAR      ="ConfirmadaAlActualizar";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_LISTA_VERIFICACION 	        + " integer not null, "
            + COLUMN_ID_PROYECTO 	                + " integer not null, "
            + COLUMN_ID_TIPO_LISTA_VERIFICACION		+ " integer not null, "
            + COLUMN_ID_ESTADO_LISTA_VERIFICACION	+ " integer not null, "
            + COLUMN_LISTA_CERRADA	                + " integer not null, "
            + COLUMN_ESTADO_ENVIO				    + " text not null, "
            + COLUMN_CREO_USUARIO				    + " text not null, "
            + COLUMN_CREO_FECHA				        + " text not null, "
            + COLUMN_CONFIRMADO_AL_ACTUALIZAR       + " integer DEFAULT 0, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_LISTA_VERIFICACION + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_TIPO_LISTA_VERIFICACION + " ) REFERENCES " + TipoListaVerificacion.NOMBRE_TABLA + "("   + TipoListaVerificacion.COLUMN_ID_CLIENTE + "," + TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_PROYECTO + " ) REFERENCES " + Proyecto.NOMBRE_TABLA + "("   + Proyecto.COLUMN_ID_CLIENTE + "," + Proyecto.COLUMN_ID + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ListaVerificacion.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }


    public boolean ActualizarIdListaVerificacion(Context ctx, int idListaVerificacionNuevo){
        boolean resultado=false;
        if (this.getIdListaVerificacion() >= 0) {
            return false;
        }
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            //Actualizar el Id del avance
            ContentValues values = new ContentValues();
            values.put(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION , idListaVerificacionNuevo);
            values.put(ListaVerificacion.COLUMN_ESTADO_ENVIO, AppValues.EstadosEnvio.Enviado.name());
            String where=ListaVerificacion.COLUMN_ID_CLIENTE + "=" + String.valueOf(this.getIdCliente())
                    + " and " + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(this.getIdListaVerificacion());
            resultado= (w.updateRow(ListaVerificacion.NOMBRE_TABLA, values, where)>0);

            if(resultado) {
                this.setIdListaVerificacion(idListaVerificacionNuevo);
                this.setEstadoRegistro(AppValues.EstadosEnvio.Enviado.name());
            }
            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=false;
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "ActualizarIdListaVerificacion",
                    null, null);
        }

        return resultado;
    }


    //Devuelve las listas de verificación abiertas por cliente y proyecto
    public static ArrayList<ListaVerificacion> obtenerListasAbiertasPorClienteProyecto(Context ctx, int idCliente, int idProyecto){
        DAL w = new DAL(ctx);
        ArrayList<ListaVerificacion> listas = new ArrayList<ListaVerificacion>();
        ListaVerificacion lista;

        Calendar cal = Calendar.getInstance();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH );
        Date dateRepresentation;

        Cursor c;

        try{
            String query = "Select "
                    + " L." + ListaVerificacion.COLUMN_ID_CLIENTE + ", "
                    + " L." + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + ", "
                    + " L." + ListaVerificacion.COLUMN_CREO_FECHA + ", "
                    + " L." + ListaVerificacion.COLUMN_CREO_USUARIO + ", "
                    + " L." + ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION + ", "
                    + " L." + ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + ", "
                    + " T." + TipoListaVerificacion.COLUMN_DESCRIPCION + " as " + ListaVerificacion.COLUMN_TIPO_LISTA_VERIFICACION + ", "
                    + " E." + EstadoListaVerificacion.COLUMN_DESCRIPCION + " as " + ListaVerificacion.COLUMN_ESTADO_LISTA_VERIFICACION
                    + " FROM " + ListaVerificacion.NOMBRE_TABLA + " L "
                    + " JOIN " + TipoListaVerificacion.NOMBRE_TABLA + " T "
                    + "   ON T." + TipoListaVerificacion.COLUMN_ID_CLIENTE + " = L." + ListaVerificacion.COLUMN_ID_CLIENTE
                    + "   AND T." + TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + " = L." + ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION
                    + " JOIN " + EstadoListaVerificacion.NOMBRE_TABLA + " E "
                    + "   ON E." + EstadoListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION + " = L." + EstadoListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION
                    + " WHERE L." + ListaVerificacion.COLUMN_ID_CLIENTE + " = " + String.valueOf(idCliente)
                    + "   and L." + ListaVerificacion.COLUMN_ID_PROYECTO + " = " + String.valueOf(idProyecto)
                    + "   and L." + ListaVerificacion.COLUMN_LISTA_CERRADA + " = 0"
                    + " ORDER BY L." + ListaVerificacion.COLUMN_CREO_FECHA + ", T." + TipoListaVerificacion.COLUMN_DESCRIPCION;

            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    lista=new ListaVerificacion();
                    lista.setIdCliente(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_CLIENTE)));
                    lista.setIdListaVerificacion(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION)));
                    lista.setIdTipoListaVerificacion(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION)));
                    cal.setTime(sdf.parse(c.getString(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_CREO_FECHA))));
                    dateRepresentation = cal.getTime();
                    lista.setCreoFecha(dateRepresentation);
                    lista.setCreoUsuario(c.getString(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_CREO_USUARIO)));
                    lista.setTipoListaVerificacion(c.getString(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_TIPO_LISTA_VERIFICACION)));
                    lista.setIdEstadoListaVerificacion(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION)));
                    lista.setEstadoListaVerificacion(c.getString(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ESTADO_LISTA_VERIFICACION)));
                    listas.add(lista);
                }
                while(c.moveToNext());
                c.close();
            }
        }
        catch (Exception e){
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "obtenerListasAbiertasPorClienteProyecto",
                    null, null);
        }

        return listas;
    }


    public static long insertarNuevaListaVerificacion(Context ctx,
                                                      int idCliente,
                                                      int idProyecto,
                                                      int idTipoListaVerificacion){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        long resultado=-1;

        ContentValues values = new ContentValues();
        values.put(ListaVerificacion.COLUMN_ID_CLIENTE, 		            idCliente);
        values.put(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION, 		    obtenerSiguienteIdListaVerificacion(ctx, idCliente));
        values.put(ListaVerificacion.COLUMN_ID_PROYECTO, 		            idProyecto);
        values.put(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION, 	idTipoListaVerificacion);
        values.put(ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION,   EstadoListaVerificacion.ID_SIN_FINALIZAR);
        values.put(ListaVerificacion.COLUMN_LISTA_CERRADA,  0);
        values.put(ListaVerificacion.COLUMN_ESTADO_ENVIO, 	AppValues.EstadosEnvio.No_Enviado.name());
        values.put(ListaVerificacion.COLUMN_CREO_USUARIO, 	AppValues.SharedPref_obtenerUsuarioNombre(ctx));
        values.put(ListaVerificacion.COLUMN_CREO_FECHA, 	sdf.format(date));

        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            resultado=w.insertRow(NOMBRE_TABLA, values);

            if (resultado!=-1)
            {
                values.clear();
            }
            else{
                w.finalizarTransaccion(false);
                return -1;
            }

            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=-1;
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "insertarNuevaListaVerificacion",
                    null, null);
        }
        return resultado;
    }

    public static int obtenerSiguienteIdListaVerificacion(Context ctx, int idCliente) {
        int ultimoId = 0;
        String query = " SELECT Min(" + COLUMN_ID_LISTA_VERIFICACION + ") as minID ";
        query += " FROM " + NOMBRE_TABLA;
        query += " WHERE " + COLUMN_ID_CLIENTE + " = " + idCliente;
        DAL w = new DAL(ctx);
        Cursor currentCursor = w.getRow(query);
        if (currentCursor.moveToFirst()) {
            while (!currentCursor.isAfterLast()) {
                ultimoId = currentCursor.getInt(0);
                if (ultimoId > 0) {
                    ultimoId = 0;
                }
                currentCursor.moveToNext();
            }
        }
        return (ultimoId - 1);
    }

    private static int obtenerNuevoEstadoListaVerificacion(Context ctx,
                                                          int idCliente,
                                                          int idListaVerificacion) {
        int idNuevoEstado = 0;
        int idEstadoActual = 0;
        int cantidadPreguntasRequeridasSinResponder = 0;
        int cantidadRespuestasInconforme = 0;
        String query = "Select  L."  + ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION + ", "
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
                + " FROM " + ListaVerificacion.NOMBRE_TABLA + " L "
                + " JOIN " + TipoListaVerificacion_Seccion.NOMBRE_TABLA + " S "
                + "   ON S." + TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE + " = L." + ListaVerificacion.COLUMN_ID_CLIENTE
                + "   AND S." + TipoListaVerificacion_Seccion.COLUMN_ID_TIPO_LISTA_VERIFICACION + " = L." + ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION
                + " WHERE L." + ListaVerificacion.COLUMN_ID_CLIENTE + " = " + String.valueOf(idCliente)
                + "   and L." + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + " = " + String.valueOf(idListaVerificacion);
        DAL w = new DAL(ctx);
        Cursor currentCursor = w.getRow(query);
        if (currentCursor.moveToFirst()) {
            while (!currentCursor.isAfterLast()) {
                idEstadoActual = currentCursor.getInt(currentCursor.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION));
                cantidadPreguntasRequeridasSinResponder += currentCursor.getInt(currentCursor.getColumnIndexOrThrow(TipoListaVerificacion_Seccion.COLUMN_CANTIDAD_PREGUNTAS_REQUERIDAS_SIN_RESPONDER));
                cantidadRespuestasInconforme += currentCursor.getInt(currentCursor.getColumnIndexOrThrow(TipoListaVerificacion_Seccion.COLUMN_CANTIDAD_RESPUESTAS_INCONFORME));
                currentCursor.moveToNext();
            }
        }
        if (cantidadPreguntasRequeridasSinResponder > 0) {
                idNuevoEstado = EstadoListaVerificacion.ID_SIN_FINALIZAR;
        } else {
            if (cantidadRespuestasInconforme > 0) {
                idNuevoEstado = EstadoListaVerificacion.ID_FINALIZADO_CON_INCONFORMIDADES;
            } else {
                idNuevoEstado = EstadoListaVerificacion.ID_FINALIZADO;
            }
        }
        if (idEstadoActual == idNuevoEstado) {
            idNuevoEstado = -1;
        }
        return idNuevoEstado;
    }

    public static boolean ActualizarEstadoListaVerificacion(Context ctx,
                                                            int idCliente,
                                                            int idListaVerificacion){
        boolean resultado;
        int idNuevoEstado = obtenerNuevoEstadoListaVerificacion(ctx,idCliente,idListaVerificacion);
        if (idNuevoEstado < 0) {
            return false;
        }
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            //Actualizar el Id del estado de la lista de verificación
            ContentValues values = new ContentValues();
            values.put(ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION , idNuevoEstado);
            values.put(ListaVerificacion.COLUMN_ESTADO_ENVIO, AppValues.EstadosEnvio.No_Enviado.name());
            String where=ListaVerificacion.COLUMN_ID_CLIENTE + "=" + String.valueOf(idCliente)
                    + " and " + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(idListaVerificacion);
            resultado= (w.updateRow(ListaVerificacion.NOMBRE_TABLA, values, where)>0);
            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=false;
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "ActualizarEstadoListaVerificacion",
                    null, null);
        }
        return resultado;
    }

    public static boolean CerrarListaVerificacion(Context ctx,
                                                  int idCliente,
                                                  int idListaVerificacion){
        boolean resultado;
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            //Actualizar el Id del estado de la lista de verificación
            ContentValues values = new ContentValues();
            values.put(ListaVerificacion.COLUMN_LISTA_CERRADA , 1);
            values.put(ListaVerificacion.COLUMN_ESTADO_ENVIO, AppValues.EstadosEnvio.No_Enviado.name());
            String where=ListaVerificacion.COLUMN_ID_CLIENTE + "=" + String.valueOf(idCliente)
                    + " and " + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(idListaVerificacion)
                    + " and " + ListaVerificacion.COLUMN_LISTA_CERRADA + "=0";
            resultado= (w.updateRow(ListaVerificacion.NOMBRE_TABLA, values, where)>0);
            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=false;
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "CerrarListaVerificacion",
                    null, null);
        }
        return resultado;
    }

    //Devuelve las listas de verificación que no se han actualizado en el servidor
    public static ArrayList<ListaVerificacion> obtenerListasNoEnviadasAlServidor(Context ctx){
        DAL w = new DAL(ctx);
        ArrayList<ListaVerificacion> listas = new ArrayList<ListaVerificacion>();
        ListaVerificacion lista;

        Calendar cal = Calendar.getInstance();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH );
        Date dateRepresentation;

        Cursor c;

        try{
            String query = "Select "
                    + " L." + ListaVerificacion.COLUMN_ID_CLIENTE + ", "
                    + " L." + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + ", "
                    + " L." + ListaVerificacion.COLUMN_ID_PROYECTO + ", "
                    + " L." + ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + ", "
                    + " L." + ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION + ", "
                    + " L." + ListaVerificacion.COLUMN_LISTA_CERRADA + ", "
                    + " L." + ListaVerificacion.COLUMN_CREO_FECHA + ", "
                    + " L." + ListaVerificacion.COLUMN_CREO_USUARIO
                    + " FROM " + ListaVerificacion.NOMBRE_TABLA + " L "
                    + " WHERE L." + ListaVerificacion.COLUMN_ESTADO_ENVIO + " <> '" + AppValues.EstadosEnvio.Enviado + "'";

            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    lista=new ListaVerificacion();
                    lista.setIdCliente(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_CLIENTE)));
                    lista.setIdListaVerificacion(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION)));
                    lista.setIdProyecto(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_PROYECTO)));
                    lista.setIdTipoListaVerificacion(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION)));
                    lista.setIdEstadoListaVerificacion(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION)));
                    lista.setListaCerrada(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_LISTA_CERRADA)));
                    cal.setTime(sdf.parse(c.getString(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_CREO_FECHA))));
                    dateRepresentation = cal.getTime();
                    lista.setCreoFecha(dateRepresentation);
                    lista.setCreoUsuario(c.getString(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_CREO_USUARIO)));
                    listas.add(lista);
                }
                while(c.moveToNext());
                c.close();
            }
        }
        catch (Exception e){
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "obtenerListasNoEnviadasAlServidor",
                    null, null);
        }

        return listas;
    }

    public boolean RegistrarComoEnviadaAlServidor(Context ctx){
        boolean resultado;
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            ContentValues values = new ContentValues();
            values.put(ListaVerificacion.COLUMN_ESTADO_ENVIO, AppValues.EstadosEnvio.Enviado.name());
            String where=ListaVerificacion.COLUMN_ID_CLIENTE + "=" + String.valueOf(idCliente)
                    + " and " + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(idListaVerificacion);
            resultado= (w.updateRow(ListaVerificacion.NOMBRE_TABLA, values, where)>0);
            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=false;
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "RegistrarComoEnviadaAlServidor",
                    null, null);
        }
        return resultado;
    }

    public static boolean MarcarTodoComoNoConfirmado(Context ctx, DAL w) {
        boolean resultado = false;

        //1. Preparar el campo que se actualizará
        ContentValues values = new ContentValues();
        values.put(ListaVerificacion.COLUMN_CONFIRMADO_AL_ACTUALIZAR, 	    0);

        //3. Ejecutar el UPDATE
        try {
            String where=ListaVerificacion.COLUMN_ESTADO_ENVIO + "= '" + AppValues.EstadosEnvio.Enviado.name() + "'";
            resultado= (w.updateRow(ListaVerificacion.NOMBRE_TABLA, values, where)>0);
        }
        catch (Exception e)
        {
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "MarcarTodoComoNoConfirmado",
                    null, null);
        }
        return resultado;
    }

    public boolean RegistrarListaRecibidaDelServidor(Context ctx, DAL w) {
        boolean resultado = false;
        boolean existe;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());

        //1. Consultar si la lista ya existe en la BD local
        existe = EstaListaExisteEnBdLocal(ctx, w);

        //2. Preparar los datos a actualizar/insertar
        ContentValues values = new ContentValues();
        if (!existe) {
            values.put(ListaVerificacion.COLUMN_ID_CLIENTE, idCliente);
            values.put(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION, idListaVerificacion);
        }
        values.put(ListaVerificacion.COLUMN_ID_PROYECTO, 		            idProyecto);
        values.put(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION, 	idTipoListaVerificacion);
        values.put(ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION,   EstadoListaVerificacion.ID_SIN_FINALIZAR);
        values.put(ListaVerificacion.COLUMN_LISTA_CERRADA,                  0);
        values.put(ListaVerificacion.COLUMN_ESTADO_ENVIO, 	                AppValues.EstadosEnvio.Enviado.name());
        values.put(ListaVerificacion.COLUMN_CREO_USUARIO, 	                this.creoUsuario);
        values.put(ListaVerificacion.COLUMN_CREO_FECHA, 	                sdf.format(this.creoFecha));
        values.put(ListaVerificacion.COLUMN_CONFIRMADO_AL_ACTUALIZAR, 	    1);

        //3. Ejecutar el INSERT o el UPDATE
        try{
            if (!existe) {
                resultado = (w.insertRow(NOMBRE_TABLA, values) > 0);
            } else {
                String where=ListaVerificacion.COLUMN_ID_CLIENTE + "=" + String.valueOf(idCliente)
                        + " and " + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(idListaVerificacion);
                resultado= (w.updateRow(ListaVerificacion.NOMBRE_TABLA, values, where)>0);
            }
        }
        catch (Exception e)
        {
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "RegistrarListaRecibidaDelServidor",
                    null, null);
        }
        return resultado;
    }

    private boolean EstaListaExisteEnBdLocal(Context ctx, DAL w) {
        boolean existe = false;
        int cantidad = 0;

        Cursor c;

        try{
            String query = "SELECT count(*) as cantidad "
                         + " FROM " + ListaVerificacion.NOMBRE_TABLA
                         + " WHERE " + ListaVerificacion.COLUMN_ID_CLIENTE + " = " + String.valueOf(idCliente)
                         + "   and " + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + " = " + String.valueOf(idListaVerificacion);

            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    cantidad = c.getInt(c.getColumnIndexOrThrow("cantidad"));
                }
                while(c.moveToNext());
                c.close();
            }

            existe = (cantidad > 0);
        }
        catch (Exception e){
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "EstaListaExisteEnBdLocal",
                    null, null);
        }
        return existe;
    }

    public static boolean EliminarTodoLoNoConfirmado(Context ctx, DAL w) {
        boolean resultado = false;

        //1. Preparar el campo que se actualizará
        ContentValues values = new ContentValues();
        values.put(ListaVerificacion.COLUMN_CONFIRMADO_AL_ACTUALIZAR, 	    0);

        //3. Ejecutar el UPDATE
        try {
            String where=ListaVerificacion.COLUMN_ESTADO_ENVIO + "=" + AppValues.EstadosEnvio.Enviado.name() +
                    " and " + ListaVerificacion.COLUMN_CONFIRMADO_AL_ACTUALIZAR + "= 0";
            resultado= w.deleteRow(ListaVerificacion.NOMBRE_TABLA, where);
        }
        catch (Exception e)
        {
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "EliminarTodoLoNoConfirmado",
                    null, null);
        }
        return resultado;
    }

}
