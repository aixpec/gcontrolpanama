package com.gisystems.gcontrolpanama.reglas.cc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;

import java.util.ArrayList;

public class PreguntaIndividualUI extends CardView {

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
        this.preguntaRespondida = preguntaRespondida;
        this.tipoCampo = TipoCampoUI.getTipoCampoCorrespondiente(pregunta.getIdTipoDato());
        crearLayout();
    }

    private TextView agregarDescripcionPregunta() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        int Dips10 = obtenerMagnitudEnDIPs(10);
        int Dips5 = obtenerMagnitudEnDIPs(5);
        layoutParams.setMargins(Dips10, Dips5, Dips10, Dips10);
        TextView txt = new TextView(this.context);
        String preg = pregunta.getPregunta() + ":";
        txt.setText(preg);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        txt.setLayoutParams(layoutParams);
        return txt;
    }

    private void crearLayout() {
        //*** Layout del CardView
        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, obtenerMagnitudEnDIPs(10));
        this.setLayoutParams(layoutParams);
        this.setContentPadding(15, 15, 15, 15);
        //*** Agregar un LinearLayout que abarque el CardView
        LinearLayout layoutFondo = new LinearLayout(context);
        layoutFondo.setOrientation(LinearLayout.VERTICAL);
        layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutFondo.setLayoutParams(layoutParams);
        layoutFondo.addView(agregarDescripcionPregunta());
        //*** Layout de datos
        LinearLayout layoutDatos = new LinearLayout(context);
        layoutDatos.setOrientation(LinearLayout.VERTICAL);
        layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
                layoutPregunta = new PreguntaTextoUI(this.actividadCreo, this.pregunta, this.preguntaRespondida);
                break;
            case Fecha:
                layoutPregunta = new PreguntaFechaUI(this.actividadCreo, this.pregunta, this.preguntaRespondida);
                break;
            case Hora:
                layoutPregunta = new PreguntaHoraUI(this.actividadCreo, this.pregunta, this.preguntaRespondida);
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
        layoutFondo.addView(layoutDatos);
        this.addView(layoutFondo);
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

    public void mostrarRespuestas() {
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

