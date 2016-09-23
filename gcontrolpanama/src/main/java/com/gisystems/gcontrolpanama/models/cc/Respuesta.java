package com.gisystems.gcontrolpanama.models.cc;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.database.DAL;

import java.util.ArrayList;

/**
 * Created by rlemus on 15/08/2016.
 */
public class Respuesta {

    private int idCliente;
    private int idConfiguracion;
    private int idIndicador;
    private int idPregunta;
    private int idRespuesta;
    private String respuesta;
    private ArrayList<RespuestaAccionDetalle> acciones = new ArrayList<>();

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

    public String getRespuesta() {return respuesta;}

    public void setRespuesta(String respuesta) {this.respuesta = respuesta;}

    public ArrayList<RespuestaAccionDetalle> getAcciones() {
        return this.acciones;
    }
    public void setAcciones(ArrayList<RespuestaAccionDetalle> Acciones) {
        acciones = Acciones;
    }

    public static final String NOMBRE_TABLA 				="tblCcRespuesta";
    public static final String COLUMN_ID_CLIENTE			="IdCliente";
    public static final String COLUMN_ID_CONFIGURACION		="IdConfiguracion";
    public static final String COLUMN_ID_INDICADOR		    ="IdIndicador";
    public static final String COLUMN_ID_PREGUNTA	    	="IdPregunta";
    public static final String COLUMN_ID_RESPUESTA  		="IdRespuesta";
    public static final String COLUMN_RESPUESTA  			="Respuesta";

    private static final String DATABASE_CREATE="create table "
            + NOMBRE_TABLA
            + "("
            + COLUMN_ID_CLIENTE 					+ " integer not null, "
            + COLUMN_ID_CONFIGURACION 				+ " integer not null, "
            + COLUMN_ID_INDICADOR 				    + " integer not null, "
            + COLUMN_ID_PREGUNTA 			        + " integer not null, "
            + COLUMN_ID_RESPUESTA 				    + " integer not null, "
            + COLUMN_RESPUESTA				        + " text not null, "
            + "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + ", "  + COLUMN_ID_PREGUNTA + ", "  + COLUMN_ID_RESPUESTA +  "), "
            + "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_CONFIGURACION + ", "  + COLUMN_ID_INDICADOR + ", "  + COLUMN_ID_PREGUNTA +  " ) REFERENCES " + Pregunta.NOMBRE_TABLA + "("   + Pregunta.COLUMN_ID_CLIENTE + "," + Pregunta.COLUMN_ID_CONFIGURACION + "," + Pregunta.COLUMN_ID_INDICADOR + "," + Pregunta.COLUMN_ID_PREGUNTA  + ") "
            + ")";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(Respuesta.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(database);
    }


    /**
     * Verifica si una acción especifíca existe para esta respuesta
     * @param TipoDeAccion tipo de acción
     */
    public boolean SeEncuentraEstaAccion(AccionRespuesta.TipoDeAccionDeRespuesta TipoDeAccion)
    {
        for(int i=0; i<this.acciones.size();i++)
        {
            Log.w("","Accion:  " + TipoDeAccion);
            if(this.acciones.get(i).getIdAccionRespuesta()==AccionRespuesta.getCodAccionRespuesta(TipoDeAccion))
            {
                return true;
            }
        }
        return false;
    }


    public static ArrayList<Respuesta> obtenerRespuestas_X_Pregunta(Context ctx, Pregunta pregunta) {
        DAL w = new DAL(ctx);
        ArrayList<Respuesta> respuestas = new ArrayList<>();
        Respuesta respuesta = null;
        RespuestaAccionDetalle accion;
        int idUltimaRespuesta = -1;

        Cursor c = null;

        try{
            String query = "SELECT "
                    + " R." + Respuesta.COLUMN_ID_RESPUESTA + ", "
                    + " R." + Respuesta.COLUMN_RESPUESTA + ", "
                    + " ifnull(A." + RespuestaAccionDetalle.COLUMN_ID_RESPUESTA_ACCION_DETALLE_CORR + ",-1) as "  + RespuestaAccionDetalle.COLUMN_ID_RESPUESTA_ACCION_DETALLE_CORR + ", "
                    + " ifnull(A." + RespuestaAccionDetalle.COLUMN_ID_ACCION_RESPUESTA + ",-1) as "  + RespuestaAccionDetalle.COLUMN_ID_ACCION_RESPUESTA + ", "
                    + " ifnull(A." + RespuestaAccionDetalle.COLUMN_IR_A_PREGUNTA + ",'') as "  + RespuestaAccionDetalle.COLUMN_IR_A_PREGUNTA + ", "
                    + " ifnull(A." + RespuestaAccionDetalle.COLUMN_DESHABILITAR_PREGUNTA_COD + ",-1) as "  + RespuestaAccionDetalle.COLUMN_DESHABILITAR_PREGUNTA_COD + ", "
                    + " ifnull(A." + RespuestaAccionDetalle.COLUMN_HABILITAR_PREGUNTA_COD + ",-1) as "  + RespuestaAccionDetalle.COLUMN_HABILITAR_PREGUNTA_COD + ", "
                    + " ifnull(A." + RespuestaAccionDetalle.COLUMN_CAMBIAR_LIMITE_PREGUNTA_COD + ",-1) as "  + RespuestaAccionDetalle.COLUMN_CAMBIAR_LIMITE_PREGUNTA_COD + ", "
                    + " ifnull(A." + RespuestaAccionDetalle.COLUMN_COD_PREG_APLICAR_ACCION + ",-1) as "  + RespuestaAccionDetalle.COLUMN_COD_PREG_APLICAR_ACCION + ", "
                    + " ifnull(A." + RespuestaAccionDetalle.COLUMN_COD_RESP_APLICAR_ACCION + ",-1) as "  + RespuestaAccionDetalle.COLUMN_COD_RESP_APLICAR_ACCION
                    + " FROM " + Respuesta.NOMBRE_TABLA + " R "
                    + " LEFT OUTER JOIN " + RespuestaAccionDetalle.NOMBRE_TABLA + " A "
                    + "   ON A." + RespuestaAccionDetalle.COLUMN_ID_CLIENTE + " = R." + Respuesta.COLUMN_ID_CLIENTE
                    + "  AND A." + RespuestaAccionDetalle.COLUMN_ID_CONFIGURACION + " = R." + Respuesta.COLUMN_ID_CONFIGURACION
                    + "  AND A." + RespuestaAccionDetalle.COLUMN_ID_INDICADOR + " = R." + Respuesta.COLUMN_ID_INDICADOR
                    + "  AND A." + RespuestaAccionDetalle.COLUMN_ID_PREGUNTA + " = R." + Respuesta.COLUMN_ID_PREGUNTA
                    + "  AND A." + RespuestaAccionDetalle.COLUMN_ID_RESPUESTA + " = R." + Respuesta.COLUMN_ID_RESPUESTA
                    + " WHERE R." + Respuesta.COLUMN_ID_CLIENTE + " = " + String.valueOf(pregunta.getIdCliente())
                    + "   AND R." + Respuesta.COLUMN_ID_CONFIGURACION + " = " + String.valueOf(pregunta.getIdConfiguracion())
                    + "   AND R." + Respuesta.COLUMN_ID_INDICADOR + " = " + String.valueOf(pregunta.getIdIndicador())
                    + "   AND R." + Respuesta.COLUMN_ID_PREGUNTA + " = " + String.valueOf(pregunta.getIdPregunta())
                    + " ORDER BY R." + Respuesta.COLUMN_ID_RESPUESTA + ", " + RespuestaAccionDetalle.COLUMN_ID_RESPUESTA_ACCION_DETALLE_CORR ;

            c =  w.getRow(query);

            if(c.moveToFirst()){
                do {
                    if (idUltimaRespuesta != c.getInt(c.getColumnIndexOrThrow(Respuesta.COLUMN_ID_RESPUESTA)))
                    {
                        respuesta=new Respuesta();
                        respuesta.setIdCliente(pregunta.getIdCliente());
                        respuesta.setIdConfiguracion(pregunta.getIdConfiguracion());
                        respuesta.setIdIndicador(pregunta.getIdIndicador());
                        respuesta.setIdPregunta(pregunta.getIdPregunta());
                        respuesta.setIdRespuesta(c.getInt(c.getColumnIndexOrThrow(Respuesta.COLUMN_ID_RESPUESTA)));
                        respuesta.setRespuesta(c.getString(c.getColumnIndexOrThrow(Respuesta.COLUMN_RESPUESTA)));
                        respuesta.setAcciones(new ArrayList<RespuestaAccionDetalle>());
                        respuestas.add(respuesta);

                        idUltimaRespuesta = c.getInt(c.getColumnIndexOrThrow(Respuesta.COLUMN_ID_RESPUESTA));
                    }
                    if (respuesta != null) {
                        if (c.getInt(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_ID_ACCION_RESPUESTA)) != -1) {
                            accion = new RespuestaAccionDetalle();
                            accion.setIdCliente(pregunta.getIdCliente());
                            accion.setIdConfiguracion(pregunta.getIdConfiguracion());
                            accion.setIdIndicador(pregunta.getIdIndicador());
                            accion.setIdPregunta(pregunta.getIdPregunta());
                            accion.setIdRespuesta(c.getInt(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_ID_RESPUESTA)));
                            accion.setIdAccionRespuesta(c.getInt(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_ID_ACCION_RESPUESTA)));
                            accion.setIdRespuestaAccionDetalleCorr(c.getInt(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_ID_RESPUESTA_ACCION_DETALLE_CORR)));
                            accion.setIrAPregunta(c.getString(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_IR_A_PREGUNTA)));
                            accion.setDeshabilitarPreguntaCod(c.getInt(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_DESHABILITAR_PREGUNTA_COD)));
                            accion.setHabilitarPreguntaCod(c.getInt(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_HABILITAR_PREGUNTA_COD)));
                            accion.setCambiarLimitePreguntaCod(c.getInt(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_CAMBIAR_LIMITE_PREGUNTA_COD)));
                            accion.setCodPregAplicarAccion(c.getInt(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_COD_PREG_APLICAR_ACCION)));
                            accion.setCodRespAplicarAccion(c.getInt(c.getColumnIndexOrThrow(RespuestaAccionDetalle.COLUMN_COD_RESP_APLICAR_ACCION)));

                            respuesta.acciones.add(accion);
                        }
                    }

                }
                while(c.moveToNext());
                c.close();
            }
        }
        catch (Exception e){
            ManejoErrores.registrarError(ctx, e,
                    Respuesta.class.getSimpleName(), "obtenerRespuestas_X_Pregunta",
                    null, null);
        }
        return respuestas;
    }


}
