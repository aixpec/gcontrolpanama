package com.gisystems.gcontrolpanama.models.cc;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by rlemus on 15/08/2016.
 */
public class AccionRespuesta {

    private int idAccionRespuesta;
    private String nombreAccionRespuesta;
    private String descAccionRespuesta;


    public int getIdAccionRespuesta() {return idAccionRespuesta;}

    public void setIdAccionRespuesta(int idAccionRespuesta) {this.idAccionRespuesta = idAccionRespuesta;}

    public String getNombreAccionRespuesta() {return nombreAccionRespuesta;}

    public void setNombreAccionRespuesta(String nombreAccionRespuesta) {this.nombreAccionRespuesta = nombreAccionRespuesta;}

    public String getDescAccionRespuesta() {return descAccionRespuesta;}

    public void setDescAccionRespuesta(String descAccionRespuesta) {this.descAccionRespuesta = descAccionRespuesta;}


    public static final String NOMBRE_TABLA 				="tblCcAccionRespuesta";
    public static final String COLUMN_ID_ACCION_RESPUESTA	="IdAccionRespuesta";
    public static final String COLUMN_NOMBRE_ACCION_RESPUESTA	="NombreAccionRespuesta";
    public static final String COLUMN_DESC_ACCION_RESPUESTA		="DescAccionRespuesta";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_ACCION_RESPUESTA 					+ " integer not null, "
            + COLUMN_NOMBRE_ACCION_RESPUESTA				+ " text not null, "
            + COLUMN_DESC_ACCION_RESPUESTA				    + " text not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_ACCION_RESPUESTA + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(AccionRespuesta.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }





    public enum TipoDeAccionDeRespuesta {
        IrAOtraPregunta, IngresarRespuestaNoDefinida, FinalizarEncuesta, DeshabilitarPregunta,
        NoDefinida, ModificarLimitePreguntaTipoNumero, SeleccionarRespuestaAuxiliar, IrAOtraPreguntaSiLaDifEntreFechasEsMayorAUno, HabilitarPregunta,
        AgregarAccionDeSalidaAOtraRespuesta;
    }


    public static int getCodAccionRespuesta(TipoDeAccionDeRespuesta TipoDeAccion) {

        int  Id_accion_respuesta;

        switch (TipoDeAccion) {
            case  IrAOtraPregunta:
                Id_accion_respuesta= 1;
                break;

            case IngresarRespuestaNoDefinida:
                Id_accion_respuesta= 2;
                break;

            case FinalizarEncuesta:
                Id_accion_respuesta=3;
                break;

            case DeshabilitarPregunta:
                Id_accion_respuesta=4;
                break;

            case ModificarLimitePreguntaTipoNumero:
                Id_accion_respuesta=5;
                break;

            case SeleccionarRespuestaAuxiliar:
                Id_accion_respuesta=6;
                break;

            case IrAOtraPreguntaSiLaDifEntreFechasEsMayorAUno:
                Id_accion_respuesta=7;
                break;

            case HabilitarPregunta:
                Id_accion_respuesta=8;
                break;

            case AgregarAccionDeSalidaAOtraRespuesta:
                Id_accion_respuesta=9;
                break;

            case NoDefinida:
                Id_accion_respuesta=-1;
                break;

            default:
                return -1;
        }

        return Id_accion_respuesta;
    }


    public  TipoDeAccionDeRespuesta getId_accion_respuesta() {
        TipoDeAccionDeRespuesta TipoDeAccion;
        switch (this.idAccionRespuesta) {
            case  1:
                TipoDeAccion= TipoDeAccionDeRespuesta.IrAOtraPregunta;
                break;

            case 2:
                TipoDeAccion= TipoDeAccionDeRespuesta.IngresarRespuestaNoDefinida;
                break;

            case 3:
                TipoDeAccion= TipoDeAccionDeRespuesta.FinalizarEncuesta;
                break;

            case 4:
                TipoDeAccion=TipoDeAccionDeRespuesta.DeshabilitarPregunta;
                break;

            case 5:
                TipoDeAccion=TipoDeAccionDeRespuesta.ModificarLimitePreguntaTipoNumero;
                break;

            case 6:
                TipoDeAccion=TipoDeAccionDeRespuesta.SeleccionarRespuestaAuxiliar;
                break;

            case 7:
                TipoDeAccion=TipoDeAccionDeRespuesta.IrAOtraPreguntaSiLaDifEntreFechasEsMayorAUno;
                break;

            case 8:
                TipoDeAccion=TipoDeAccionDeRespuesta.HabilitarPregunta;
                break;

            case 9:
                TipoDeAccion=TipoDeAccionDeRespuesta.AgregarAccionDeSalidaAOtraRespuesta;
                break;

            default:
                TipoDeAccion= TipoDeAccionDeRespuesta.NoDefinida;
                break;
        }

        return TipoDeAccion;

    }




}
