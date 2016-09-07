package com.gisystems.gcontrolpanama.models.cc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rlemus on 30/08/2016.
 */
public abstract class PreguntaRespondida extends Pregunta  {

    private ArrayList<RespuestaIngresada> respuestasSeleccionadas = new ArrayList<RespuestaIngresada>();
    private int irAPregunta;
    private int preguntaAnterior;

    public ArrayList<RespuestaIngresada> getRespuestasSeleccionadas() {
        return respuestasSeleccionadas;
    }

    public void setRespuestasSeleccionadas(ArrayList<RespuestaIngresada> respuestasSeleccionadas) {
        this.respuestasSeleccionadas = respuestasSeleccionadas;
    }

    public int getIrAPregunta() {
        return irAPregunta;
    }

    public void setIrAPregunta(int irAPregunta) {
        this.irAPregunta = irAPregunta;
    }

    public int getPreguntaAnterior() {
        return preguntaAnterior;
    }

    public void setPreguntaAnterior(int preguntaAnterior) {
        this.preguntaAnterior = preguntaAnterior;
    }

    public PreguntaRespondida() {
    }

    public PreguntaRespondida(Pregunta pregunta) {
        this.setIdCliente(pregunta.getIdCliente());
        this.setIdConfiguracion(pregunta.getIdConfiguracion());
        this.setIdIndicador(pregunta.getIdIndicador());
        this.setIndicador(pregunta.getIndicador());
        this.setIdPregunta(pregunta.getIdPregunta());
        this.setPregunta(pregunta.getPregunta());
        this.setIdTipoDato(pregunta.getIdTipoDato());
        this.setRequerido(pregunta.getRequerido());
    }

    public void AgregarRespuesta(Respuesta respuesta, String valorRespuesta, Date fechaCaptura) {
        RespuestaIngresada r = new RespuestaIngresada();
        if (respuesta != null) {
            r.setIdRespuesta(respuesta.getIdRespuesta());
            r.setDescripcionRespuesta(respuesta.getRespuesta());
        }
        r.setValorRespuesta(valorRespuesta);
        r.setFechaCaptura(fechaCaptura);
        this.respuestasSeleccionadas.add(r);
    }

    public abstract int GrabarRespuestasEnBD();

}
