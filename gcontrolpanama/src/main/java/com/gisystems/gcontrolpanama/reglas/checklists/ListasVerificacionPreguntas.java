package com.gisystems.gcontrolpanama.reglas.checklists;

import android.app.Activity;
import android.os.Bundle;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.reglas.cc.PreguntasPorIndicadorActivity;

public class ListasVerificacionPreguntas extends PreguntasPorIndicadorActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas_verificacion_preguntas);
    }
}
