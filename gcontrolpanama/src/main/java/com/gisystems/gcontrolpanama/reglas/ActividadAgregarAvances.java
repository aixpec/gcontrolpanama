package com.gisystems.gcontrolpanama.reglas;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.gcontrolpanama.models.Proyecto;

import java.util.concurrent.ExecutionException;

/**
 * Clase
 * Created by aixpec on 26/07/16.
 */
public class ActividadAgregarAvances extends AppCompatActivity {

    AutoCompleteTextView aCTVAConstruccion, aCTVACtividad =null;
    private SimpleCursorAdapter sCAConstruccion, sCAActividad = null;
    Cursor cConst, cActiv;
    private ProgressDialog pDialog;
    Context ctx;
    int idCliente, idProyecto;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        //Prueba de comentario en github2

        setContentView(R.layout.activity_agregar_avances);

        ctx=this;

        obtenerIntents();

        establecerComportamientoControles();

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
                    ActividadAgregarAvances.class.getSimpleName(), "obtenerIntents",
                    null, null);

        }

        return false;

    }


    private void obtenerControles(){

        aCTVAConstruccion=(AutoCompleteTextView)
                findViewById(R.id.agregar_avances_aCConstruccion);

        aCTVACtividad=(AutoCompleteTextView)
                findViewById(R.id.agregar_avances_aCActividad);

    }

    private void establecerComportamientoControles(){

        obtenerControles();

        construirAdaptadorConstruccion();

        aCTVAConstruccion.setAdapter(sCAConstruccion);

        aCTVAConstruccion.setThreshold(1);

    }

    /***
     * Obtiene las construcciones almacenadas
     *
     */
    private class TareaObtenerListadoConstruccion extends AsyncTask<Object, Void, Cursor> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(ctx.getApplicationContext().getString(R.string.cargando_construcciones));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Cursor doInBackground(Object... param) {
            try {

                int cliente = (int) param[0];
                int proyecto = (int) param[1];
                CharSequence texto = (CharSequence) param[2];
                cConst=Construccion.obtenerCursorConstrucciones(getApplicationContext(), cliente, proyecto, texto);

            } catch (Exception e) {

                pDialog.dismiss();

               /* ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadAgregarAvances.class.getSimpleName(), "onPreExecute",
                        null, null);*/
            }

            return cConst;

        }

        @Override
        protected void onPostExecute(Cursor resultado){
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                pDialog.dismiss();
                /*ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadAgregarAvances.class.getSimpleName(), "onPostExecute",
                        null, null);*/

            }
        }

    }



    @Override
    protected void onPause() {

        if (pDialog != null) pDialog.dismiss();
        super.onPause();
    }


    /**
     * Construye el adaptador para la construccion
     */
    private void construirAdaptadorConstruccion(){

        String[] colsAd=new String[]{Construccion.COLUMN_DESCRIPCION};
        int[] adapterRowViews=new int[]{android.R.id.text1};

        //Inicializar el adaptador
        sCAConstruccion= new SimpleCursorAdapter(getApplicationContext(),
                R.layout.gc_list_item, cConst, colsAd, adapterRowViews,0);

        //Establecer el filtro para el adaptador
        sCAConstruccion.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                Cursor c = null;
                try {
                    //Obtener las construcciones coincidentes
                    c = new TareaObtenerListadoConstruccion().execute(new Object []{idCliente, idProyecto, str}).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return c;
            } });

        //Establecer que el nombre del proyecto seleccionado
        sCAConstruccion.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndexOrThrow(Construccion.COLUMN_DESCRIPCION);
                return cur.getString(index);
            }});

        //Establecer el layout gc_list_item como el estilo del dropdown del Adaptador
        sCAConstruccion.setDropDownViewResource(R.layout.gc_list_item);

    }

}
