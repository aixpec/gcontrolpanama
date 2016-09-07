package com.gisystems.gcontrolpanama.models.cc;

import java.util.Date;

/**
 * Created by rlemus on 01/09/2016.
 */
public class RespuestaIngresada extends Pregunta {

    private int idRespuesta;
    private String descripcionRespuesta;
    private String valorRespuesta;
    private Date fechaCaptura;

    public RespuestaIngresada() {
    }

    public RespuestaIngresada(Pregunta pregunta) {
        this.setIdCliente(pregunta.getIdCliente());
        this.setIdConfiguracion(pregunta.getIdConfiguracion());
        this.setIdIndicador(pregunta.getIdIndicador());
        this.setIdPregunta(pregunta.getIdPregunta());
        this.setIdTipoDato(pregunta.getIdTipoDato());
        this.setIndicador(pregunta.getIndicador());
        this.setPregunta(pregunta.getPregunta());
        this.setRequerido(pregunta.getRequerido());
    }

    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    public void setDescripcionRespuesta(String descripcionRespuesta) {
        this.descripcionRespuesta = descripcionRespuesta;
    }

    public String getValorRespuesta() {
        return valorRespuesta;
    }

    public void setValorRespuesta(String valorRespuesta) {
        this.valorRespuesta = valorRespuesta;
    }

    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
