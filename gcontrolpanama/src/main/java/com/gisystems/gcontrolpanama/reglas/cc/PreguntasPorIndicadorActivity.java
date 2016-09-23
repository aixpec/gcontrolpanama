package com.gisystems.gcontrolpanama.reglas.cc;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorChecklists;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;

import java.util.ArrayList;

public class PreguntasPorIndicadorActivity extends AppCompatActivity {

    protected LinearLayout layoutPreguntas;
    protected Button btnGuardar;
    protected ArrayList<PreguntaIndividualUI> listaPreguntas;
    protected ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_por_indicador);
        layoutPreguntas=(LinearLayout)findViewById(R.id.generic_question_layout_views_pregunta);
        btnGuardar=(Button)findViewById(R.id.preguntas_por_indicador_btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TareaGrabarRespuestas().execute();
            }
        });
    }

    protected void mostrarPreguntas(ArrayList<PreguntaIndividualUI> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
        for (PreguntaIndividualUI preg:listaPreguntas) {
            layoutPreguntas.addView(preg);
            preg.mostrarRespuestas();
        }
    }

    protected long grabarRespuestas() {
        return 0;
    }


    protected class TareaGrabarRespuestas extends AsyncTask<Integer, Void, Long> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(PreguntasPorIndicadorActivity.this);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(PreguntasPorIndicadorActivity.this.getApplicationContext().getString(R.string.almacenando_en_dispositivo));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Long doInBackground(Integer... params) {
            long resultado = 0;
            try {
                resultado = grabarRespuestas();
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(PreguntasPorIndicadorActivity.this, e,
                        PreguntasPorIndicadorActivity.class.getSimpleName(), "TareaGrabarRespuestas_onPreExecute",
                        null, null);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Long resultado){
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(PreguntasPorIndicadorActivity.this, e,
                        PreguntasPorIndicadorActivity.class.getSimpleName(), "TareaGrabarRespuestas_onPostExecute",
                        null, null);
            }
        }
    }


}
