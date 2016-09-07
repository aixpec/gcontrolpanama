package com.gisystems.gcontrolpanama.reglas.cc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gisystems.gcontrolpanama.R;

public class PreguntasPorIndicadorActivity extends AppCompatActivity {

    protected LinearLayout layoutPreguntas;
    protected Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_por_indicador);
    }
}
