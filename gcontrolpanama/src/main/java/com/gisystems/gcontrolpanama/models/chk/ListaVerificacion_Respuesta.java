package com.gisystems.gcontrolpanama.models.chk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.FotoActividad;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.Respuesta;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rlemus on 15/08/2016.
 */
public class ListaVerificacion_Respuesta extends RespuestaIngresada {

    private int idListaVerificacion;
    private String estadoRegistro;
    private String creoUsuario;
    private Date creoFecha;

    public int getIdListaVerificacion() {
        return idListaVerificacion;
    }

    public void setIdListaVerificacion(int idListaVerificacion) {
        this.idListaVerificacion = idListaVerificacion;
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
    public static final String COLUMN_ID_CONFIGURACION	            ="IdConfiguracion";
    public static final String COLUMN_ID_INDICADOR	                ="IdIndicador";
    public static final String COLUMN_ID_PREGUNTA	                ="IdPregunta";
    public static final String COLUMN_DESCRIPCION_INDICADOR		    ="DescripcionIndicador";
    public static final String COLUMN_DESCRIPCION_PREGUNTA		    ="DescripcionPregunta";
    public static final String COLUMN_ID_RESPUESTA	                ="IdRespuesta";
    public static final String COLUMN_DESCRIPCION_RESPUESTA		    ="DescripcionRespuesta";
    public static final String COLUMN_VALOR_RESPUESTA		        ="ValorRespuesta";
    public static final String COLUMN_ESTADO_ENVIO		            ="EstadoEnvio";
    public static final String COLUMN_ELIMINADO		                ="Eliminado";
    public static final String COLUMN_CREO_USUARIO			        ="CreoUsuario";
    public static final String COLUMN_CREO_FECHA			        ="CreoFecha";
    public static final String COLUMN_CONFIRMADO_AL_ACTUALIZAR      ="ConfirmadaAlActualizar";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_LISTA_VERIFICACION 	        + " integer not null, "
            + COLUMN_ID_CONFIGURACION 	            + " integer not null, "
            + COLUMN_ID_INDICADOR 	                + " integer not null, "
            + COLUMN_ID_PREGUNTA		            + " integer not null, "
            + COLUMN_DESCRIPCION_INDICADOR			+ " text not null, "
            + COLUMN_DESCRIPCION_PREGUNTA			+ " text not null, "
            + COLUMN_ID_RESPUESTA		            + " integer null, "
            + COLUMN_DESCRIPCION_RESPUESTA			+ " text null, "
            + COLUMN_VALOR_RESPUESTA				+ " text not null, "
            + COLUMN_ESTADO_ENVIO  				    + " text not null, "
            + COLUMN_ELIMINADO				        + " integer not null DEFAULT 0, "
            + COLUMN_CREO_USUARIO				    + " text not null, "
            + COLUMN_CREO_FECHA				        + " text not null, "
            + COLUMN_CONFIRMADO_AL_ACTUALIZAR       + " integer DEFAULT 0, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_LISTA_VERIFICACION + ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + ", "  + COLUMN_ID_PREGUNTA + "), "
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

    public ListaVerificacion_Respuesta() {
    }

    public ListaVerificacion_Respuesta(RespuestaIngresada respuestaIngresada,
                                       int idListaVerificacion) {
        this.setIdCliente(respuestaIngresada.getIdCliente());
        this.setIdConfiguracion(respuestaIngresada.getIdConfiguracion());
        this.setIdIndicador(respuestaIngresada.getIdIndicador());
        this.setIdPregunta(respuestaIngresada.getIdPregunta());
        this.setIdTipoDato(respuestaIngresada.getIdTipoDato());
        this.setIndicador(respuestaIngresada.getIndicador());
        this.setPregunta(respuestaIngresada.getPregunta());
        this.setRequerido(respuestaIngresada.getRequerido());

        this.setIdRespuesta(respuestaIngresada.getIdRespuesta());
        this.setDescripcionRespuesta(respuestaIngresada.getDescripcionRespuesta());
        this.setValorRespuesta(respuestaIngresada.getValorRespuesta());
        this.setFechaCaptura(respuestaIngresada.getFechaCaptura());
        this.setCreoFecha(respuestaIngresada.getFechaCaptura());

        this.setIdListaVerificacion(idListaVerificacion);
    }


    @Override
    public long GrabarRespuestaEnBD(Context ctx) {
        long resultado = 0;
        this.setValorRespuesta(this.getValorRespuesta().trim());
        this.setDescripcionRespuesta(this.getDescripcionRespuesta().trim());
        boolean yaExisteUnRegistro = existeYaUnaRespuestaEnLaBD(ctx);
        if (!yaExisteUnRegistro) {
            if (this.getValorRespuesta().length() > 0) {
                resultado = insertarRespuestaBD(ctx);
            }
        } else {
            resultado = actualizarRespuestaBD(ctx);
        };
        return resultado;
    }

    @Override
    public long EnviarRespuestaAlServidor(Context ctx) {
        EnvioDatosAPI envioDatosAPI = new EnvioDatosAPI(ctx);
        long resultado = 0;
        if (envioDatosAPI.EnviarListaVerificacionRespuesta(this)) {
            resultado = 1;
        };
        return resultado;
    }

    public boolean ActualizarEstadoListaVerificacionRespuesta(Context ctx){
        boolean resultado;
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            //Actualizar el Id del avance
            ContentValues values = new ContentValues();
            values.put(ListaVerificacion_Respuesta.COLUMN_ESTADO_ENVIO, AppValues.EstadosEnvio.Enviado.name());
            String where=ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + "=" + String.valueOf(this.getIdCliente())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(this.getIdListaVerificacion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + "=" + String.valueOf(this.getIdConfiguracion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + "=" + String.valueOf(this.getIdIndicador())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + "=" + String.valueOf(this.getIdPregunta());
            resultado= (w.updateRow(ListaVerificacion_Respuesta.NOMBRE_TABLA, values, where)>0);

            if(resultado) {
                this.setEstadoRegistro(AppValues.EstadosEnvio.Enviado.name());
            }
            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=false;
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "ActualizarIdListaVerificacionRespuesta",
                    null, null);
        }
        return resultado;
    }

    public boolean ActualizarEstadoRegistro(Context ctx, AppValues.EstadosEnvio nuevoEstado){
        boolean resultado;
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            ContentValues values = new ContentValues();
            values.put(ListaVerificacion_Respuesta.COLUMN_ESTADO_ENVIO, nuevoEstado.name());
            String where=ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + "=" + String.valueOf(this.getIdCliente())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(this.getIdListaVerificacion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + "=" + String.valueOf(this.getIdConfiguracion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + "=" + String.valueOf(this.getIdIndicador())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + "=" + String.valueOf(this.getIdPregunta());
            resultado= (w.updateRow(ListaVerificacion_Respuesta.NOMBRE_TABLA, values, where)>0);

            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=false;
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "ActualizarEstadoRegistro",
                    null, null);
        }

        return resultado;
    }

    //Devuelve las respuestas ingresadas por pregunta
    public static ArrayList<ListaVerificacion_Respuesta> obtenerRespuestasIngresadas_X_Pregunta(Context ctx,
                                                                                                int idCliente,
                                                                                                int idListaVerificacion,
                                                                                                Pregunta pregunta){
        DAL w = new DAL(ctx);
        ArrayList<ListaVerificacion_Respuesta> respuestas = new ArrayList<>();
        ListaVerificacion_Respuesta respuesta;

        Cursor c;

        try {
            String query = "Select  "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_CREO_FECHA
                    + " FROM " + ListaVerificacion_Respuesta.NOMBRE_TABLA + " R "
                    + " WHERE R." + ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + " = " + String.valueOf(idCliente)
                    + "   and R." + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + " = " + String.valueOf(idListaVerificacion)
                    + "   and R." + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + " = " + String.valueOf(pregunta.getIdConfiguracion())
                    + "   and R." + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + " = " + String.valueOf(pregunta.getIdIndicador())
                    + "   and R." + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + " = " + String.valueOf(pregunta.getIdPregunta())
                    + "   and R." + ListaVerificacion_Respuesta.COLUMN_ELIMINADO + " = 0";
            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    respuesta=new ListaVerificacion_Respuesta();
                    respuesta.setIdCliente(idCliente);
                    respuesta.setIdListaVerificacion(idListaVerificacion);
                    respuesta.setIdConfiguracion(pregunta.getIdConfiguracion());
                    respuesta.setIdIndicador(pregunta.getIdIndicador());
                    respuesta.setIdPregunta(pregunta.getIdPregunta());
                    respuesta.setIdRespuesta(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA)));
                    respuesta.setDescripcionRespuesta(c.getString(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA)));
                    respuesta.setValorRespuesta(c.getString(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA)));

                    respuesta.setIndicador(pregunta.getIndicador());
                    respuesta.setPregunta(pregunta.getPregunta());

                    respuestas.add(respuesta);
                }
                while(c.moveToNext());
                c.close();
            }
        }
        catch (Exception e){
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "obtenerRespuestasIngresadas_X_Pregunta",
                    null, null);
        }
        return respuestas;
    }

    public long insertarRespuestaBD(Context ctx){

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.getDefault());
        long resultado;

        this.creoUsuario = AppValues.SharedPref_obtenerUsuarioNombre(ctx);
        this.creoFecha = date;

        ContentValues values = new ContentValues();

        values.put(ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE, 		        this.getIdCliente());
        values.put(ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION, 	this.getIdListaVerificacion());
        values.put(ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION, 		this.getIdConfiguracion());
        values.put(ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR, 		    this.getIdIndicador());
        values.put(ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA, 		        this.getIdPregunta());
        values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_INDICADOR, 	this.getIndicador());
        values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_PREGUNTA, 	this.getPregunta());
        if (this.getIdRespuesta() > 0) {
            values.put(ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA, this.getIdRespuesta());
        }
        values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA, 	this.getDescripcionRespuesta());
        values.put(ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA, 		    this.getValorRespuesta());
        values.put(ListaVerificacion_Respuesta.COLUMN_ESTADO_ENVIO, 	        AppValues.EstadosEnvio.No_Enviado.name());
        values.put(ListaVerificacion_Respuesta.COLUMN_ELIMINADO, 	            0);
        values.put(ListaVerificacion_Respuesta.COLUMN_CREO_USUARIO, 	        this.creoUsuario);
        values.put(ListaVerificacion_Respuesta.COLUMN_CREO_FECHA, 	            sdf.format(this.creoFecha));

        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            resultado=w.insertRow(NOMBRE_TABLA, values);
            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=-1;
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "insertarRespuestaBD",
                    null, null);
        }
        return resultado;
    }

    public boolean existeYaUnaRespuestaEnLaBD(Context ctx) {
        int cantidadExistente = 0;
        String query = " SELECT count(*) as cantidadExistente ";
        query += " FROM " + NOMBRE_TABLA;
        query += " WHERE " + COLUMN_ID_CLIENTE + " = " + this.getIdCliente();
        query += " AND " + COLUMN_ID_LISTA_VERIFICACION + " = " + this.getIdListaVerificacion();
        query += " AND " + COLUMN_ID_CONFIGURACION + " = " + this.getIdConfiguracion();
        query += " AND " + COLUMN_ID_INDICADOR + " = " + this.getIdIndicador();
        query += " AND " + COLUMN_ID_PREGUNTA + " = " + this.getIdPregunta();

        DAL w = new DAL(ctx);
        Cursor currentCursor = w.getRow(query);
        if (currentCursor.moveToFirst()) {
            while (!currentCursor.isAfterLast()) {
                cantidadExistente = currentCursor.getInt(0);
                currentCursor.moveToNext();
            }
        }
        return (cantidadExistente > 0);
    }

    public long actualizarRespuestaBD(Context ctx){
        long resultado;
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.getDefault());
            //Actualizar el Id del avance
            ContentValues values = new ContentValues();
            values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_INDICADOR, 	this.getIndicador());
            values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_PREGUNTA, 	this.getPregunta());
            if (this.getIdRespuesta() > 0) {
                values.put(ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA, this.getIdRespuesta());
            }
            values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA, 	this.getDescripcionRespuesta());
            values.put(ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA, 		    this.getValorRespuesta());
            values.put(ListaVerificacion_Respuesta.COLUMN_ESTADO_ENVIO, 	        AppValues.EstadosEnvio.No_Enviado.name());
            values.put(ListaVerificacion_Respuesta.COLUMN_CREO_USUARIO, 	        AppValues.SharedPref_obtenerUsuarioNombre(ctx));
            values.put(ListaVerificacion_Respuesta.COLUMN_CREO_FECHA, 	            sdf.format(date));
            if (this.getValorRespuesta().length() > 0) {
                values.put(ListaVerificacion_Respuesta.COLUMN_ELIMINADO, 	        0);
            } else {
                values.put(ListaVerificacion_Respuesta.COLUMN_ELIMINADO, 	        1);
            }
            String where=ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + "=" + String.valueOf(this.getIdCliente())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(this.getIdListaVerificacion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + "=" + String.valueOf(this.getIdConfiguracion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + "=" + String.valueOf(this.getIdIndicador())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + "=" + String.valueOf(this.getIdPregunta())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA  + " <> '" +  String.valueOf(this.getValorRespuesta()) + "'";
            resultado= w.updateRow(ListaVerificacion_Respuesta.NOMBRE_TABLA, values, where);

            w.finalizarTransaccion(true);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            resultado=-1;
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "actualizarRespuestaBD",
                    null, null);
        }
        return resultado;
    }

    //Devuelve las respuestas ingresadas que no han sido actualizadas en el servidor
    public static ArrayList<ListaVerificacion_Respuesta> obtenerRespuestasIngresadasNoEnviadasAlServidor(Context ctx){
        DAL w = new DAL(ctx);
        ArrayList<ListaVerificacion_Respuesta> respuestas = new ArrayList<>();
        ListaVerificacion_Respuesta respuesta;

        Cursor c;

        try {
            String query = "Select  "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_INDICADOR + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_PREGUNTA + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA + ", "
                    + " R." + ListaVerificacion_Respuesta.COLUMN_CREO_FECHA
                    + " FROM " + ListaVerificacion_Respuesta.NOMBRE_TABLA + " R "
                    + " WHERE R." + ListaVerificacion_Respuesta.COLUMN_ESTADO_ENVIO + " <> '" + AppValues.EstadosEnvio.Enviado + "'";
            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    respuesta=new ListaVerificacion_Respuesta();
                    respuesta.setIdCliente(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE)));
                    respuesta.setIdListaVerificacion(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION)));
                    respuesta.setIdConfiguracion(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION)));
                    respuesta.setIdIndicador(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR)));
                    respuesta.setIdPregunta(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA)));
                    respuesta.setIdRespuesta(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA)));
                    respuesta.setDescripcionRespuesta(c.getString(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA)));
                    respuesta.setValorRespuesta(c.getString(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA)));

                    respuesta.setIndicador(c.getString(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_INDICADOR)));
                    respuesta.setPregunta(c.getString(c.getColumnIndexOrThrow(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_PREGUNTA)));

                    respuestas.add(respuesta);
                }
                while(c.moveToNext());
                c.close();
            }
        }
        catch (Exception e){
            ManejoErrores.registrarError(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "obtenerRespuestasIngresadasNoEnviadasAlServidor",
                    null, null);
        }
        return respuestas;
    }

    public static boolean MarcarTodoComoNoConfirmado(Context ctx) {
        boolean resultado = false;

        //1. Preparar el campo que se actualizará
        ContentValues values = new ContentValues();
        values.put(ListaVerificacion_Respuesta.COLUMN_CONFIRMADO_AL_ACTUALIZAR, 	    0);

        //2. Ejecutar el UPDATE
        DAL w = new DAL(ctx);
        try {
            w.iniciarTransaccion();
            String where=ListaVerificacion_Respuesta.COLUMN_ESTADO_ENVIO + "=" + AppValues.EstadosEnvio.Enviado.name();
            resultado= (w.updateRow(ListaVerificacion_Respuesta.NOMBRE_TABLA, values, where)>0);
            w.finalizarTransaccion(resultado);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "MarcarTodoComoNoConfirmado",
                    null, null);
        }
        return resultado;
    }

    public boolean RegistrarRespuestaRecibidaDelServidor(Context ctx) {
        boolean resultado = false;
        boolean existe;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());

        //1. Consultar si la respuesta ya existe en la BD local
        existe = EstaRespuestaExisteEnBdLocal(ctx);

        //2. Preparar los datos a actualizar/insertar
        ContentValues values = new ContentValues();
        if (!existe) {
            values.put(ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE, getIdCliente() );
            values.put(ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION, idListaVerificacion);
            values.put(ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION, getIdConfiguracion() );
            values.put(ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR, getIdIndicador() );
            values.put(ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA, getIdPregunta() );
        }
        values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_INDICADOR, 		getIndicador() );
        values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_PREGUNTA, 	    getPregunta() );
        values.put(ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA,                 getIdRespuesta() );
        values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA,        getDescripcionRespuesta() );
        values.put(ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA,              getValorRespuesta());
        values.put(ListaVerificacion_Respuesta.COLUMN_ESTADO_ENVIO, 	            AppValues.EstadosEnvio.Enviado.name());
        values.put(ListaVerificacion_Respuesta.COLUMN_ELIMINADO, 	                0);
        values.put(ListaVerificacion_Respuesta.COLUMN_CREO_USUARIO, 	            this.creoUsuario);
        values.put(ListaVerificacion_Respuesta.COLUMN_CREO_FECHA, 	                sdf.format(this.creoFecha));
        values.put(ListaVerificacion_Respuesta.COLUMN_CONFIRMADO_AL_ACTUALIZAR, 	1);

        //3. Ejecutar el INSERT o el UPDATE
        DAL w = new DAL(ctx);
        try{
            w.iniciarTransaccion();

            if (!existe) {
                resultado = (w.insertRow(NOMBRE_TABLA, values) > 0);
            } else {
                String where=ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + "=" + String.valueOf(getIdCliente())
                        + " and " + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(idListaVerificacion)
                        + " and " + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + "=" + String.valueOf(getIdConfiguracion())
                        + " and " + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + "=" + String.valueOf(getIdIndicador())
                        + " and " + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + "=" +  String.valueOf(getIdPregunta());
                resultado= (w.updateRow(ListaVerificacion_Respuesta.NOMBRE_TABLA, values, where)>0);
            }

            w.finalizarTransaccion(resultado);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "RegistrarRespuestaRecibidaDelServidor",
                    null, null);
        }
        return resultado;
    }

    private boolean EstaRespuestaExisteEnBdLocal(Context ctx) {
        boolean existe = false;
        int cantidad = 0;
        DAL w = new DAL(ctx);

        Cursor c;

        try{
            String query = "SELECT count(*) as cantidad "
                    + " FROM " + ListaVerificacion_Respuesta.NOMBRE_TABLA
                    + " WHERE " + ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE + " = " + String.valueOf(getIdCliente())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION + "=" + String.valueOf(idListaVerificacion)
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION + "=" + String.valueOf(getIdConfiguracion())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR + "=" + String.valueOf(getIdIndicador())
                    + " and " + ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA + "=" +  String.valueOf(getIdPregunta());

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
                    ListaVerificacion_Respuesta.class.getSimpleName(), "EstaRespuestaExisteEnBdLocal",
                    null, null);
        }
        return existe;
    }

    public static boolean EliminarTodoLoNoConfirmado(Context ctx) {
        boolean resultado = false;

        //1. Preparar el campo que se actualizará
        ContentValues values = new ContentValues();
        values.put(ListaVerificacion_Respuesta.COLUMN_CONFIRMADO_AL_ACTUALIZAR, 	    0);

        //3. Ejecutar el UPDATE
        DAL w = new DAL(ctx);
        try {
            w.iniciarTransaccion();
            String where=ListaVerificacion_Respuesta.COLUMN_ESTADO_ENVIO + "=" + AppValues.EstadosEnvio.Enviado.name() +
                    " and " + ListaVerificacion_Respuesta.COLUMN_CONFIRMADO_AL_ACTUALIZAR + "= 0";
            resultado= w.deleteRow(ListaVerificacion_Respuesta.NOMBRE_TABLA, where);
            w.finalizarTransaccion(resultado);
        }
        catch (Exception e)
        {
            w.finalizarTransaccion(false);
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion_Respuesta.class.getSimpleName(), "EliminarTodoLoNoConfirmado",
                    null, null);
        }
        return resultado;
    }


}
