package com.gisystems.gcontrolpanama.models.cc;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.gcontrolpanama.models.Proyecto;

/**
 * Created by rlemus on 15/08/2016.
 */
public class Indicador {

    private int idCliente;
    private int idConfiguracion;
    private int idIndicador;
    private int idClaseIndicador;
    private int idTipoIndicador;
    private String descripcion;
    private boolean requerido;

    public int getIdCliente() {return idCliente;}

    public void setIdCliente(int idCliente) {this.idCliente = idCliente;}

    public int getIdConfiguracion() {return idConfiguracion;}

    public void setIdConfiguracion(int idConfiguracion) {this.idConfiguracion = idConfiguracion;}

    public int getIdIndicador() {return idIndicador;}

    public void setIdIndicador(int idIndicador) {this.idIndicador = idIndicador;}

    public int getIdClaseIndicador() {return idClaseIndicador;}

    public void setIdClaseIndicador(int idClaseIndicador) {this.idClaseIndicador = idClaseIndicador;}

    public int getIdTipoIndicador() {return idTipoIndicador;}

    public void setIdTipoIndicador(int idTipoIndicador) {this.idTipoIndicador = idTipoIndicador;}

    public String getDescripcion() {return descripcion;}

    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public boolean getRequerido() {return requerido;}

    public void setRequerido(boolean requerido) {this.requerido = requerido;}

    public static final String NOMBRE_TABLA 				="tblCcIndicador";
    public static final String COLUMN_ID_CLIENTE			="IdCliente";
    public static final String COLUMN_ID_CONFIGURACION		="IdConfiguracion";
    public static final String COLUMN_ID_INDICADOR		    ="IdIndicador";
    public static final String COLUMN_ID_CLASE_INDICADOR	="IdClaseIndicador";
    public static final String COLUMN_ID_TIPO_INDICADOR		="IdTipoIndicador";
    public static final String COLUMN_DESCRIPCION			="Descripcion";
    public static final String COLUMN_REQUERIDO			    ="Requerido";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_CONFIGURACION 				+ " integer not null, "
            + COLUMN_ID_INDICADOR 				    + " integer not null, "
            + COLUMN_ID_CLASE_INDICADOR 			+ " integer not null, "
            + COLUMN_ID_TIPO_INDICADOR 				+ " integer not null, "
            + COLUMN_DESCRIPCION				    + " text not null, "
            + COLUMN_REQUERIDO				        + " integer not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR +  "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION   + " ) REFERENCES " + Configuracion.NOMBRE_TABLA + "("   + Configuracion.COLUMN_ID_CLIENTE + "," + Configuracion.COLUMN_ID_CONFIGURACION + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLASE_INDICADOR   + " ) REFERENCES " + ClaseIndicador.NOMBRE_TABLA + "("   + ClaseIndicador.COLUMN_ID_CLASE_INDICADOR + "), "
            + "FOREIGN KEY ( " + COLUMN_ID_TIPO_INDICADOR   + " ) REFERENCES " + TipoIndicador.NOMBRE_TABLA + "("   + TipoIndicador.COLUMN_ID_TIPO_INDICADOR + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Indicador.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }

}
