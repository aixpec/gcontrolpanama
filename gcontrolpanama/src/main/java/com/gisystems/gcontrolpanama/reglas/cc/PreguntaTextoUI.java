package com.gisystems.gcontrolpanama.reglas.cc;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rlemus on 12/09/2016.
 */
public class PreguntaTextoUI extends PreguntaUI {

    private EditText txtRespuesta;

    public PreguntaTextoUI(Context actividad, Pregunta pregunta, PreguntaRespondida preguntaRespondida) {
        super(actividad, pregunta, preguntaRespondida);
        crearView();
    }

    private void crearView() {
        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txtRespuesta = new EditText(context);
        txtRespuesta.setLayoutParams(layoutParams);
        txtRespuesta.setInputType(InputType.TYPE_CLASS_TEXT);
        this.addView(txtRespuesta);
    }

    @Override
    public boolean esRespuestaIngresada() {
        return (txtRespuesta.getText().toString().length() > 0);
    }

    @Override
    public boolean esRespuestaValida(StringBuilder mensaje) {
        boolean valida = false;
        if (txtRespuesta.getText().toString().length() > 0) {
            valida = true;
        } else {
            mensaje.append("No ha respondido: " + pregunta.getPregunta());
        }
        return valida;
    }

    @Override
    public void mostrarRespuestas() {
        if (this.preguntaRespondida.getRespuestasSeleccionadas().size() > 0) {
            txtRespuesta.setText(preguntaRespondida.getRespuestasSeleccionadas().get(0).getValorRespuesta());
        }
    }

    @Override
    public ArrayList<RespuestaIngresada> obtenerRespuestas() {
        RespuestaIngresada respuestaSeleccionada;
        String respuesta = "";
        Boolean respuestaValida = false;
        //**Verificar que tenga un objeto RespuestaIngresada
        if (this.preguntaRespondida.getRespuestasSeleccionadas().size() > 0) {
            respuestaSeleccionada =  this.preguntaRespondida.getRespuestasSeleccionadas().get(0);
        } else {
            respuestaSeleccionada =  new RespuestaIngresada(this.pregunta);
            this.preguntaRespondida.getRespuestasSeleccionadas().add(respuestaSeleccionada);
        }
        //**Obtener respuesta
        respuesta = txtRespuesta.getText().toString();
        //**Validar respuesta
        if (respuesta.length() > 0) {
            try {
                respuestaValida = FuncionesGlobales.validateInput(context.getSharedPreferences("validar_data",Context.MODE_PRIVATE).getBoolean("validar_data", false), pregunta, respuesta);
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
                Toast.makeText(this.context, e1.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (respuestaValida)
        {
            try {
                respuestaSeleccionada.setValorRespuesta(respuesta);
                respuestaSeleccionada.setDescripcionRespuesta(respuesta);
                respuestaSeleccionada.setFechaCaptura(new Date());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.preguntaRespondida.getRespuestasSeleccionadas();
    }

    //public void verificarAccionesRealizar(QuestionSet currentQuestionSet) {}

}
