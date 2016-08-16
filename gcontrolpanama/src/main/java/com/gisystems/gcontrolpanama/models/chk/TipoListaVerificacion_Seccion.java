package com.gisystems.gcontrolpanama.models.chk;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.gcontrolpanama.models.cc.Indicador;

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

    public static final String NOMBRE_TABLA 				        ="tblChkTipoListaVerificacion_Seccion";
    public static final String COLUMN_ID_CLIENTE			        ="IdCliente";
    public static final String COLUMN_ID_TIPO_LISTA_VERIFICACION    ="IdTipoListaVerificacion";
    public static final String COLUMN_ID_SECCION			        ="IdSeccion";
    public static final String COLUMN_ID_CONFIGURACION	            ="IdConfiguracion";
    public static final String COLUMN_ID_INDICADOR	                ="IdIndicador";
    public static final String COLUMN_NOMBRE			            ="Nombre";
    public static final String COLUMN_DE_DATOS_GENERALES	        ="DeDatosGenerales";
    public static final String COLUMN_DE_OBSERVACIONES		        ="DeObservaciones";

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
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION + COLUMN_ID_INDICADOR + " ) REFERENCES " + Indicador.NOMBRE_TABLA + "("   + Indicador.COLUMN_ID_CLIENTE + "," + Indicador.COLUMN_ID_CONFIGURACION + "," + Indicador.COLUMN_ID_INDICADOR + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_TIPO_LISTA_VERIFICACION + " ) REFERENCES " + TipoListaVerificacion.NOMBRE_TABLA + "("   + TipoListaVerificacion.COLUMN_ID_CLIENTE + "," + TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION + "), "
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

}
