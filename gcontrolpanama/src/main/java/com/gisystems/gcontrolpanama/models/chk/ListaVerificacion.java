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
    public static final String COLUMN_ID_ESTADO_LISTA_VERIFICACION  ="IdEstadoListaVerificacion";
    public static final String COLUMN_ESTADO_REGISTRO		        ="EstadoRegistro";
    public static final String COLUMN_CREO_USUARIO			        ="CreoUsuario";
    public static final String COLUMN_CREO_FECHA			        ="CreoFecha";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_LISTA_VERIFICACION 	        + " integer not null, "
            + COLUMN_ID_PROYECTO 	                + " integer not null, "
            + COLUMN_ID_TIPO_LISTA_VERIFICACION		+ " integer not null, "
            + COLUMN_ID_ESTADO_LISTA_VERIFICACION	+ " integer not null, "
            + COLUMN_ESTADO_REGISTRO				+ " text not null, "
            + COLUMN_CREO_USUARIO				    + " text not null, "
            + COLUMN_CREO_FECHA				        + " text not null, "
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
            values.put(ListaVerificacion.COLUMN_ESTADO_REGISTRO, AppValues.EstadosEnvio.Enviado.name());
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
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
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

        Cursor c = null;

        try{
            String query = "Select "
                    + " L." + ListaVerificacion.COLUMN_ID_CLIENTE + ", "
                    + " L." + ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION + ", "
                    + " L." + ListaVerificacion.COLUMN_CREO_FECHA + ", "
                    + " L." + ListaVerificacion.COLUMN_CREO_USUARIO + ", "
                    + " T." + TipoListaVerificacion.COLUMN_DESCRIPCION + ", "
                    + " FROM " + ListaVerificacion.NOMBRE_TABLA + " L "
                    + " JOIN " + TipoListaVerificacion.NOMBRE_TABLA + " T "
                    + "   ON T." + TipoListaVerificacion.COLUMN_ID_CLIENTE + " = L." + ListaVerificacion.COLUMN_ID_CLIENTE
                    + "   AND T." + TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + " = L." + ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION
                    + " WHERE L." + ListaVerificacion.COLUMN_ID_CLIENTE + " = " + String.valueOf(idCliente)
                    + "   and L." + ListaVerificacion.COLUMN_ID_PROYECTO + " = " + String.valueOf(idProyecto);

            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    lista=new ListaVerificacion();
                    lista.setIdCliente(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_CLIENTE)));
                    lista.setIdListaVerificacion(c.getInt(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION)));
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, 1988);
                    cal.set(Calendar.MONTH, Calendar.JANUARY);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    Date dateRepresentation = cal.getTime();
                    //lista.setCreoFecha(new Date(c.getString(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_CREO_FECHA))));
                    lista.setCreoFecha(dateRepresentation);
                    lista.setCreoUsuario(c.getString(c.getColumnIndexOrThrow(ListaVerificacion.COLUMN_CREO_USUARIO)));
                    lista.setTipoListaVerificacion(c.getString(c.getColumnIndexOrThrow(TipoListaVerificacion.COLUMN_DESCRIPCION)));
                    listas.add(lista);
                    lista=null;
                }
                while(c.moveToNext());
                c.close();
            }

        }
        catch (Exception e){
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListaVerificacion.class.getSimpleName(), "obtenerListasAbiertasPorClienteProyecto",
                    null, null);
        }

        return listas;
    }

}
