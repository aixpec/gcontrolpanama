package com.gisystems.gcontrolpanama.reglas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;

public class ActividadSincronizacion extends AppCompatActivity {

    private Context ctx;
    private int idCliente, idProyecto;
    private ProgressDialog pDialog;

    private ImageButton btnEnviarDatos;
    private ImageButton btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizacion);

        this.setTitle(R.string.ActividadSincronizacion_0);

        this.ctx = this;

        obtenerIntents();

        configurarBotones();
    }

    private void configurarBotones() {
        btnEnviarDatos = (ImageButton) findViewById(R.id.sincronizacion_btnEnviarDatos);
        btnActualizar = (ImageButton) findViewById(R.id.sincronizacion_btnActualizar);

        btnEnviarDatos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TareaEnviarDatos().execute(idCliente);
            }
        });
    }

    /**
     * Obtiene los Intents esperado por la clase: IdCliente y IdProyecto
     * @return true si los intents son válidos
     */
    private boolean obtenerIntents(){
        try{
            idCliente=this.getIntent().getIntExtra(Proyecto.COLUMN_ID_CLIENTE,-1);
            idProyecto=this.getIntent().getIntExtra(Proyecto.COLUMN_ID,-1);

            Log.i("INTENT PARAMS: ",String.valueOf(idCliente) + "|" + String.valueOf(idProyecto));

            if (idCliente > 0 &  idProyecto >0) return true;
        }
        catch (Exception e) {
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ActividadSincronizacion.class.getSimpleName(), "obtenerIntents",
                    null, null);
        }
        return false;
    }

    private class TareaEnviarDatos extends AsyncTask<Integer, Void, Boolean> {
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
        protected Boolean doInBackground(Integer... idCliente) {
            Boolean resultado = false;
            try {
                    EnvioDatosAPI envioDatosAPI = new EnvioDatosAPI(ctx);
                    resultado = envioDatosAPI.EnviarTodosLosDatosNoEnviados(idCliente[0]);
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadSincronizacion.class.getSimpleName(), "TareaEnviarDatos_doInBackground",
                        null, null);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado){
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadSincronizacion.class.getSimpleName(), "TareaEnviarDatos_onPostExecute",
                        null, null);
            }
        }
    }

}
