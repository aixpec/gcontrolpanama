package com.gisystems.gcontrolpanama.reglas.cc;

import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rlemus on 13/09/2016.
 */
public class PreguntaHoraUI extends PreguntaUI {

    private TimePicker tpRespuesta;
    private EditText txtDias;

    public PreguntaHoraUI(Context actividad, Pregunta pregunta, PreguntaRespondida preguntaRespondida) {
        super(actividad, pregunta, preguntaRespondida);
        crearViews();
    }

    private void crearViews() {
        LinearLayout.LayoutParams layoutParams;
        this.setOrientation(LinearLayout.VERTICAL);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        // Agregar timepicker
        tpRespuesta = new TimePicker(context);
        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tpRespuesta.setLayoutParams(layoutParams);
        tpRespuesta.setIs24HourView(false);
        this.addView(tpRespuesta);
        ((LinearLayout.LayoutParams)tpRespuesta.getLayoutParams()).gravity = Gravity.CENTER_HORIZONTAL;
    }

    @Override
    public boolean esRespuestaIngresada() {
        return (obtenerRespuestas().size() > 0);
    }

    @Override
    public boolean esRespuestaValida(StringBuilder mensaje) {
        boolean valida = false;
        try {
            String respuesta;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                respuesta = tpRespuesta.getCurrentHour() + ":" + tpRespuesta.getCurrentMinute();
            } else {
                respuesta = tpRespuesta.getHour() + ":" + tpRespuesta.getMinute();
            }
            valida = FuncionesGlobales.validateInput(context.getSharedPreferences("validar_data", Context.MODE_PRIVATE).getBoolean("validar_data", false), pregunta, respuesta);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
            Toast.makeText(context, e1.getMessage(), Toast.LENGTH_LONG).show();
        }
        return valida;
    }

    @Override
    public void mostrarRespuestas() {
        if (this.preguntaRespondida.getRespuestasSeleccionadas().size() > 0) {
            String[] hora;
            hora = this.preguntaRespondida.getRespuestasSeleccionadas().get(0).getValorRespuesta().split(":");
            if (hora.length == 2) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    tpRespuesta.setCurrentHour(Integer.parseInt(hora[0]));
                    tpRespuesta.setCurrentMinute(Integer.parseInt(hora[1]));
                } else {
                    tpRespuesta.setHour(Integer.parseInt(hora[0]));
                    tpRespuesta.setMinute(Integer.parseInt(hora[1]));
                }
            } else {
                Toast.makeText(context, "No se ha podido obtener la fecha anterior", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public ArrayList<RespuestaIngresada> obtenerRespuestas() {
        RespuestaIngresada respuestaSeleccionada;
        Boolean respuestaValida = false;
        String respuesta;
        //**Verificar que tenga un objeto RespuestaIngresada
        if (this.preguntaRespondida.getRespuestasSeleccionadas().size() > 0) {
            respuestaSeleccionada =  this.preguntaRespondida.getRespuestasSeleccionadas().get(0);
        } else {
            respuestaSeleccionada =  new RespuestaIngresada(this.pregunta);
            this.preguntaRespondida.getRespuestasSeleccionadas().add(respuestaSeleccionada);
        }
        //**Obtener respuesta
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            respuesta = tpRespuesta.getCurrentHour() + ":" + tpRespuesta.getCurrentMinute();
        } else {
            respuesta = tpRespuesta.getHour() + ":" + tpRespuesta.getMinute();
        }
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


}
