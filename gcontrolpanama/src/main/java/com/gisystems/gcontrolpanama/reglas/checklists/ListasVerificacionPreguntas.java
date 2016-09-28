package com.gisystems.gcontrolpanama.reglas.checklists;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion_PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion_Respuesta;
import com.gisystems.gcontrolpanama.models.chk.TipoListaVerificacion_Seccion;
import com.gisystems.gcontrolpanama.reglas.cc.PreguntaIndividualUI;
import com.gisystems.gcontrolpanama.reglas.cc.PreguntasPorIndicadorActivity;

import java.util.ArrayList;

public class ListasVerificacionPreguntas extends PreguntasPorIndicadorActivity {

    private Context ctx;
    private int idCliente, idProyecto, idListaVerificacion, idTipoListaVerificacion, idSeccion;
    private String seccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx=this;
        obtenerIntents();

        this.setTitle(seccion);
        this.mostrarPreguntas(obtenerListaPreguntas());
    }


    /**
     * Obtiene los Intents esperado por la clase: IdCliente y IdProyecto, idListaVerificacion, idTipoListaVerificacion, tipoListaVerificacion
     * @return true si los intents son válidos
     */
    private boolean obtenerIntents(){
        try{
            idCliente=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_CLIENTE,-1);
            idProyecto=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_PROYECTO,-1);
            idListaVerificacion=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION,-1);
            idTipoListaVerificacion=this.getIntent().getIntExtra(TipoListaVerificacion_Seccion.COLUMN_ID_TIPO_LISTA_VERIFICACION,-1);
            idSeccion=this.getIntent().getIntExtra(TipoListaVerificacion_Seccion.COLUMN_ID_SECCION,-1);
            seccion=this.getIntent().getStringExtra(TipoListaVerificacion_Seccion.COLUMN_NOMBRE);

            if (idCliente > 0 &  idProyecto >0 &  idListaVerificacion >0 &  idTipoListaVerificacion >0 &  idSeccion >0) return true;
        }
        catch (Exception e) {
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListasVerificacionSecciones.class.getSimpleName(), "obtenerIntents",
                    null, null);
        }
        return false;
    }

    private ArrayList<PreguntaIndividualUI> obtenerListaPreguntas(){
        ArrayList<PreguntaIndividualUI> listaPreguntasResp = new ArrayList<>();
        ArrayList<Pregunta> listaPreguntas;
        ListaVerificacion_PreguntaRespondida pregResp;
        PreguntaIndividualUI pregInd;
        ArrayList<ListaVerificacion_Respuesta> listaRespuestasPorPregunta;
        listaPreguntas = TipoListaVerificacion_Seccion.obtenerPreguntas_X_Seccion(ctx, idCliente,
                                                                                  idTipoListaVerificacion, idSeccion);
        for (Pregunta preg:listaPreguntas) {
            pregResp = new ListaVerificacion_PreguntaRespondida(preg);
            pregResp.setRespuestasSeleccionadas(new ArrayList<RespuestaIngresada>());
            listaRespuestasPorPregunta = ListaVerificacion_Respuesta.obtenerRespuestasIngresadas_X_Pregunta(ctx,idCliente,idListaVerificacion,preg);
            for (ListaVerificacion_Respuesta r: listaRespuestasPorPregunta) {
                pregResp.getRespuestasSeleccionadas().add(r);            }
            pregInd = new PreguntaIndividualUI(ctx,preg,pregResp);
            listaPreguntasResp.add(pregInd);
        }
        return listaPreguntasResp;
    }

    @Override
    protected long grabarRespuestas() {
        long resultado = 0;
        StringBuilder mensaje = new StringBuilder();
        boolean validas = true;
        ListaVerificacion_Respuesta lr;
        //Verificar que todas las respuestas ingresadas sean válidas
        for (PreguntaIndividualUI preg:this.listaPreguntas) {
            if (preg.esRespuestaIngresada()) {
                if (!preg.esRespuestaValida(mensaje)) {
                    validas = false;
                    break;
                }
            }
        }
        //Si las respuestas son válidas, guardar en la BD cada una
        if (!validas) {
            if (mensaje.length() > 0) {
                Toast.makeText(this, mensaje.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            for (PreguntaIndividualUI preg:this.listaPreguntas) {
                for (RespuestaIngresada r:preg.obtenerRespuestas()) {
                    lr = new ListaVerificacion_Respuesta(r,this.idListaVerificacion);
                    if (lr.GrabarRespuestaEnBD(this) > 0) {
                        resultado += 1;
                        lr.EnviarRespuestaAlServidor(this);
                    }
                }
            }
            if (resultado > 0) {
                ListaVerificacion.ActualizarEstadoListaVerificacion(ListasVerificacionPreguntas.this,this.idCliente,this.idListaVerificacion);
            }
        }
        return resultado;
    }

}
