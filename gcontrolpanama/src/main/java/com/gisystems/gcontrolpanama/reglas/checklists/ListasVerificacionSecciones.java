package com.gisystems.gcontrolpanama.reglas.checklists;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorChecklists;
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorSeccionesDeChecklist;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.TipoListaVerificacion_Seccion;

import java.util.ArrayList;

public class ListasVerificacionSecciones extends ActionBarActivity implements  SearchView.OnQueryTextListener {

    private Context ctx;
    private ProgressDialog pDialog;
    private SearchView mSearchView;
    private ListView mListView;
    private AdaptadorSeccionesDeChecklist adaptadorSeccionesDeChecklist;
    private int idCliente, idProyecto, idListaVerificacion, idTipoListaVerificacion;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_listas_verificacion);

        ctx=this;
        mListView = (ListView) findViewById(R.id.activity_checklist_list);
        obtenerIntents();

        new TareaObtenerListado().execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TipoListaVerificacion_Seccion p = adaptadorSeccionesDeChecklist.getItem(position);
                MostrarPantallaPreguntas(p);
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adaptadorSeccionesDeChecklist.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adaptadorSeccionesDeChecklist.getFilter().filter(query);
        return false;
    }

    private void MostrarPantallaPreguntas(TipoListaVerificacion_Seccion seccion){
        Intent mainIntent = new Intent(ListasVerificacionSecciones.this,ListasVerificacionPreguntas.class);
        mainIntent.putExtra(ListaVerificacion.COLUMN_ID_CLIENTE,idCliente);
        mainIntent.putExtra(ListaVerificacion.COLUMN_ID_PROYECTO,idProyecto);
        mainIntent.putExtra(TipoListaVerificacion_Seccion.COLUMN_ID_TIPO_LISTA_VERIFICACION,seccion.getIdTipoListaVerificacion());
        ListasVerificacionSecciones.this.startActivity(mainIntent);
    }



    private class TareaObtenerListado extends AsyncTask<Void, Void, ArrayList<TipoListaVerificacion_Seccion>> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(ctx.getApplicationContext().getString(R.string.cargando_secciones));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<TipoListaVerificacion_Seccion> doInBackground(Void... param) {
            ArrayList<TipoListaVerificacion_Seccion> resultado = new ArrayList<TipoListaVerificacion_Seccion>();
            try {
                resultado = TipoListaVerificacion_Seccion.obtenerSecciones_X_ListaVerificacion(ctx,idCliente,idListaVerificacion, idTipoListaVerificacion);
            } catch (Exception e) {
                ManejoErrores.registrarError(ctx, e,
                        ListasVerificacionSecciones.class.getSimpleName(), "onPreExecute",
                        null, null);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(ArrayList<TipoListaVerificacion_Seccion> resultado){
            try {
                pDialog.dismiss();
                adaptadorSeccionesDeChecklist=  new AdaptadorSeccionesDeChecklist(ListasVerificacionSecciones.this,resultado);
                mListView.setAdapter(adaptadorSeccionesDeChecklist);
            } catch (Exception e) {
                ManejoErrores.registrarError(ctx, e,
                        ListasVerificacionSecciones.class.getSimpleName(), "onPostExecute",
                        null, null);
            }
        }
    }


    /**
     * Obtiene los Intents esperado por la clase: IdCliente y IdProyecto
     * @return true si los intents son válidos
     */
    private boolean obtenerIntents(){
        try{
            idCliente=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_CLIENTE,-1);
            idProyecto=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_PROYECTO,-1);
            idListaVerificacion=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION,-1);
            idTipoListaVerificacion=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION,-1);

            Log.i("INTENT PARAMS: ",String.valueOf(idCliente) + "|" + String.valueOf(idProyecto));

            if (idCliente > 0 &  idProyecto >0 &  idListaVerificacion >0 &  idTipoListaVerificacion >0) return true;
        }
        catch (Exception e) {
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListasVerificacionSecciones.class.getSimpleName(), "obtenerIntents",
                    null, null);
        }
        return false;
    }


}
