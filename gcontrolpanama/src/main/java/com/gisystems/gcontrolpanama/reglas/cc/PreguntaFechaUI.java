package com.gisystems.gcontrolpanama.reglas.cc;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;

public class PreguntaFechaUI extends PreguntaUI {

    private DatePicker dtpRespuesta;
    private EditText txtDias;

    public PreguntaFechaUI(Context actividad, Pregunta pregunta, PreguntaRespondida preguntaRespondida) {
        super(actividad, pregunta, preguntaRespondida);
        crearViews();
    }

    private void crearViews() {
        LinearLayout.LayoutParams layoutParams;
        this.setOrientation(LinearLayout.VERTICAL);
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

        // Agregar datepicker
        dtpRespuesta = new DatePicker(context);
        layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dtpRespuesta.setLayoutParams(layoutParams);
        this.addView(dtpRespuesta);
        ((LayoutParams)dtpRespuesta.getLayoutParams()).gravity = Gravity.CENTER_HORIZONTAL;

        //Agregar layout para botones de restar o sumar días
        LinearLayout layoutBotones = new LinearLayout(context);
        layoutBotones.setOrientation(LinearLayout.HORIZONTAL);
        layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutBotones.setLayoutParams(layoutParams);
        layoutBotones.setPadding(0, Math.round(dipToPx(15)), 0, 0);

        //Agregar views para restar o sumar días
        txtDias = new EditText(context);
        layoutParams = new LayoutParams(Math.round(dipToPx(60)), Math.round(dipToPx(50)));
        txtDias.setLayoutParams(layoutParams);
        txtDias.setInputType(InputType.TYPE_CLASS_NUMBER);
        txtDias.setMaxEms(3);
        txtDias.setHint("dias");
        int maxLength = 3;
        txtDias.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        layoutBotones.addView(txtDias);

        Button btnSumarDias = new Button(context);
        btnSumarDias.setLayoutParams(new LayoutParams(Math.round(dipToPx(85)), Math.round(dipToPx(50))));
        btnSumarDias.setText("Sumar");
        btnSumarDias.setOnClickListener(sumarDiasViewFecha);
        layoutBotones.addView(btnSumarDias);

        Button btnRestarDias = new Button(context);
        btnRestarDias.setLayoutParams(new LayoutParams(Math.round(dipToPx(85)), Math.round(dipToPx(50))));
        btnRestarDias.setText("Restar");
        btnRestarDias.setOnClickListener(restarDiasViewFecha);
        layoutBotones.addView(btnRestarDias);

        this.addView(layoutBotones);
        ((LayoutParams)layoutBotones.getLayoutParams()).gravity = Gravity.CENTER;
    }

    @Override
    public boolean esRespuestaIngresada() {
        return (obtenerRespuestas().size() > 0);
    }

    @Override
    public boolean esRespuestaValida(StringBuilder mensaje) {
        boolean valida = false;
        try {
            String respuesta = dtpRespuesta.getDayOfMonth() + "-" + String.valueOf(dtpRespuesta.getMonth() + 1) + "-" + dtpRespuesta.getYear();
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
            String[] Fecha;
            Fecha = this.preguntaRespondida.getRespuestasSeleccionadas().get(0).getValorRespuesta().split("-");
            if (Fecha.length == 3) {
                dtpRespuesta.updateDate(Integer.parseInt(Fecha[2]), (Integer.parseInt(Fecha[1])) - 1, Integer.parseInt(Fecha[0]));
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
        //**Vaciar lista
        this.preguntaRespondida.getRespuestasSeleccionadas().clear();
        //**Obtener respuesta
        respuesta = dtpRespuesta.getDayOfMonth() + "-" + String.valueOf( dtpRespuesta.getMonth()+1) + "-" + dtpRespuesta.getYear();
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
                respuestaSeleccionada =  new RespuestaIngresada(this.pregunta);
                respuestaSeleccionada.setValorRespuesta(respuesta);
                //Almacenando las respuestas seleccionadas en la pregunta
                this.preguntaRespondida.getRespuestasSeleccionadas().add(respuestaSeleccionada);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.preguntaRespondida.getRespuestasSeleccionadas();
    }

    View.OnClickListener restarDiasViewFecha = new View.OnClickListener() {
        public void onClick(View v) {
            String textoDias = txtDias.getText().toString();
            if (txtDias.length() > 0) {
                int cantidadDias = Integer.parseInt(textoDias);
                if (cantidadDias > 0) {
                    Calendar fecha = Calendar.getInstance();
                    fecha.set(dtpRespuesta.getYear(), dtpRespuesta.getMonth(), dtpRespuesta.getDayOfMonth());
                    fecha.add(Calendar.DATE, 0 - cantidadDias);
                    dtpRespuesta.updateDate(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH), fecha.get(Calendar.DATE));
                }
            }
        }
    };

    View.OnClickListener sumarDiasViewFecha = new View.OnClickListener() {
        public void onClick(View v) {
            String textoDias = txtDias.getText().toString();
            if (txtDias.length() > 0) {
                int cantidadDias = Integer.parseInt(textoDias);
                if (cantidadDias > 0) {
                    Calendar fecha = Calendar.getInstance();
                    fecha.set(dtpRespuesta.getYear(), dtpRespuesta.getMonth(), dtpRespuesta.getDayOfMonth());
                    fecha.add(Calendar.DATE, cantidadDias);
                    dtpRespuesta.updateDate(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH), fecha.get(Calendar.DATE));
                }
            }
        }
    };



}

