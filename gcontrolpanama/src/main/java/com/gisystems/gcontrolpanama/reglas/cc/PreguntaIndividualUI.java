package com.gisystems.gcontrolpanama.reglas.cc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;

import java.util.ArrayList;

public class PreguntaIndividualUI extends LinearLayout {

    private Context actividadCreo;
    private Context context;
    private PreguntaRespondida preguntaRespondida;
    private PreguntaUI layoutPregunta = null;
    private Pregunta pregunta;
    private TipoCampoUI tipoCampo;

    public PreguntaIndividualUI(Context actividad, Pregunta pregunta, PreguntaRespondida preguntaRespondida) {
        super(actividad);
        this.context = actividad;
        this.actividadCreo = actividad;
        this.pregunta = pregunta;
        this.tipoCampo = TipoCampoUI.getTipoCampoCorrespondiente(pregunta.getIdTipoDato());
        crearLayout();
    }

    private void agregarDescripcionPregunta() {
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        int Dips10 = obtenerMagnitudEnDIPs(10);
        int Dips5 = obtenerMagnitudEnDIPs(5);
        layoutParams.setMargins(Dips10, Dips5, Dips10, Dips10);
        TextView txt = new TextView(this.context);
        String preg = pregunta.getPregunta() + ":";
        txt.setText(preg);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        txt.setLayoutParams(layoutParams);
        this.addView(txt);
    }

    private void crearLayout() {
        this.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, obtenerMagnitudEnDIPs(10));
        this.setLayoutParams(layoutParams);
        agregarDescripcionPregunta();
        //*** Layout de datos
        LinearLayout layoutDatos = new LinearLayout(context);
        layoutDatos.setOrientation(LinearLayout.VERTICAL);
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
        layoutDatos.setLayoutParams(layoutParams);
        //*** Crear views correspondientes al tipo de campo
        switch (tipoCampo) {
            case Lista:
                layoutPregunta = new PreguntaListaUI(this.actividadCreo, this.pregunta, this.preguntaRespondida);
                break;
            case NumeroReal:
                layoutPregunta = new PreguntaNumeroUI(this.actividadCreo, this.pregunta, this.preguntaRespondida);
                break;
            case NumeroEntero:
                layoutPregunta = new PreguntaNumeroUI(this.actividadCreo, this.pregunta, this.preguntaRespondida);
                break;
            case Texto:
                break;
            case Fecha:
                layoutPregunta = new PreguntaFechaUI(this.actividadCreo, this.pregunta, this.preguntaRespondida);
                break;
            case Hora:
                break;
            case Lista_Texto:
                layoutPregunta = new PreguntaListaTextoUI(this.actividadCreo, this.pregunta, this.preguntaRespondida);
                break;
            case Lista_Numero_Moneda:
                break;
            case Opcion_Multiple:
                break;
        }
        if (layoutPregunta != null) {
            layoutDatos.addView(layoutPregunta);
        }
        addView(layoutDatos);
    }

    public boolean esRespuestaIngresada() {
        boolean esRespuesta = false;
        if (layoutPregunta != null) {
            esRespuesta = layoutPregunta.esRespuestaIngresada();
        }
        return esRespuesta;
    }

    public boolean esRespuestaValida(StringBuilder mensaje) {
        boolean valida = false;
        if (layoutPregunta != null) {
            valida = layoutPregunta.esRespuestaValida(mensaje);
        }
        return valida;
    }

    public int getIrAPregunta() {
        int respuesta = -1;
        if (layoutPregunta != null) {
            respuesta = layoutPregunta.irApregunta;
        }
        return respuesta;
    }

    public void mostrarRespuestas(PreguntaRespondida preguntaRespondida) {
        if (layoutPregunta != null) {
            layoutPregunta.mostrarRespuestas();
        }
    }

    public int obtenerMagnitudEnDIPs(int magnitud) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (magnitud * scale + 0.5f);
    }

    public ArrayList<RespuestaIngresada> obtenerRespuestas() {
        ArrayList<RespuestaIngresada> listaPreguntasRespondidas = new ArrayList<RespuestaIngresada>();
        if (layoutPregunta != null) {
            listaPreguntasRespondidas = layoutPregunta.obtenerRespuestas();
        }
        return listaPreguntasRespondidas;
    }

//    public void verificarAccionesRealizar(QuestionSet currentQuestionSet) {
//        if (layoutPregunta != null) {
//            layoutPregunta.verificarAccionesRealizar(currentQuestionSet);
//        }
//    }

}
