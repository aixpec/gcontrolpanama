package com.gisystems.gcontrolpanama.reglas.cc;

import java.util.ArrayList;

import android.content.Context;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;

public class PreguntaNumeroUI extends PreguntaUI {

    private EditText txtRespuesta;

    public PreguntaNumeroUI(Context actividad, Pregunta pregunta, PreguntaRespondida preguntaRespondida) {
        super(actividad, pregunta, preguntaRespondida);
        crearView();
    }

    private void crearView() {
        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txtRespuesta = new EditText(context);
        txtRespuesta.setLayoutParams(layoutParams);
        switch (TipoCampoUI.getTipoCampoCorrespondiente(pregunta.getIdTipoDato())) {
            case NumeroReal:
                txtRespuesta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case NumeroEntero:
                txtRespuesta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
        }
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
                respuestaSeleccionada =  new RespuestaIngresada();
                respuestaSeleccionada.setValorRespuesta(respuesta);

                this.preguntaRespondida.getRespuestasSeleccionadas().add(respuestaSeleccionada);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.preguntaRespondida.getRespuestasSeleccionadas();
    }

    //public void verificarAccionesRealizar(QuestionSet currentQuestionSet) {}

}
