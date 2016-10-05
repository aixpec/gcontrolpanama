package com.gisystems.gcontrolpanama.reglas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.api.RecepcionDatosAPI;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;

public class ActividadSincronizacion extends AppCompatActivity {

    private Context ctx;

    private ProgressDialog pDialog;

    private ImageButton btnEnviarDatos;
    private ImageButton btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ManejoErrores(this));

        setContentView(R.layout.activity_sincronizacion);

        this.setTitle(R.string.ActividadSincronizacion_0);

        this.ctx = this;

        configurarBotones();
    }

    private void configurarBotones() {
        btnEnviarDatos = (ImageButton) findViewById(R.id.sincronizacion_btnEnviarDatos);
        btnActualizar = (ImageButton) findViewById(R.id.sincronizacion_btnActualizar);

        btnEnviarDatos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TareaEnviarDatos().execute();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TareaActualizarDatos().execute();
            }
        });
    }


    private class TareaEnviarDatos extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(ctx.getApplicationContext().getString(R.string.ActividadSincronizacion_3));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... idCliente) {
            Integer resultado = 0;
            try {
                    EnvioDatosAPI envioDatosAPI = new EnvioDatosAPI(ctx);
                    if (envioDatosAPI.EnviarTodosLosDatosNoEnviados()) {
                        resultado = 1;
                    } else {
                        resultado = 0;
                    };

            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadSincronizacion.class.getSimpleName(), "TareaEnviarDatos_doInBackground",
                        null, null);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Integer resultado){
            try {
                pDialog.dismiss();
                if (resultado > 0) {
                    Toast.makeText(ctx, R.string.ActividadSincronizacion_5, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx, R.string.ActividadSincronizacion_6, Toast.LENGTH_SHORT).show();
                }
                new TareaActualizarDatos().execute();
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadSincronizacion.class.getSimpleName(), "TareaEnviarDatos_onPostExecute",
                        null, null);
            }
        }
    }


    private class TareaActualizarDatos extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(ctx.getApplicationContext().getString(R.string.ActividadSincronizacion_4));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... idCliente) {
            Integer resultado = 0;
            try {
                RecepcionDatosAPI recepcionDatosAPI = new RecepcionDatosAPI(ctx);
                if (recepcionDatosAPI.ActualizarDatosEnBdLocal()) {
                    resultado = 1;
                } else {
                    resultado = 0;
                };

            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadSincronizacion.class.getSimpleName(), "TareaActualizarDatos_doInBackground",
                        null, null);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Integer resultado){
            try {
                pDialog.dismiss();
                if (resultado > 0) {
                    Toast.makeText(ctx, R.string.ActividadSincronizacion_7, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx, R.string.ActividadSincronizacion_8, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadSincronizacion.class.getSimpleName(), "TareaActualizarDatos_onPostExecute",
                        null, null);
            }
        }
    }

}
