package com.gisystems.gcontrolpanama.reglas.cc;

import java.util.ArrayList;

import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.LinearLayout;

abstract class PreguntaUI extends LinearLayout {

    protected Context context;
    protected Pregunta pregunta;
    protected PreguntaRespondida preguntaRespondida;
    public int irApregunta = -1;

    PreguntaUI(Context context, Pregunta pregunta, PreguntaRespondida preguntaRespondida){
        super(context);
        this.context = context;
        this.pregunta = pregunta;
        this.preguntaRespondida = preguntaRespondida;
    }

    protected float dipToPx(float valueInDP) {
        Resources res = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDP, res.getDisplayMetrics());
    }

    public abstract boolean esRespuestaIngresada();

    public abstract boolean esRespuestaValida(StringBuilder mensaje);

    public abstract void mostrarRespuestas();

    public abstract ArrayList<RespuestaIngresada> obtenerRespuestas();

    //public abstract void verificarAccionesRealizar(QuestionSet currentQuestionSet);

}
