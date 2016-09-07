package com.gisystems.gcontrolpanama.reglas.checklists;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorChecklists;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;
import com.gisystems.gcontrolpanama.reglas.BitacoraActivity;
import com.gisystems.gcontrolpanama.reglas.ConstruccionDetalleActivity;
import com.gisystems.gcontrolpanama.reglas.ProyectosListadoActivity;
import com.gisystems.utils.Utilitarios;

import java.util.ArrayList;

public class ListasVerificacionActivity extends ActionBarActivity implements  SearchView.OnQueryTextListener {

    private Context ctx;
    private ProgressDialog pDialog;
    private SearchView mSearchView;
    private ListView mListView;
    private AdaptadorChecklists adaptadorChecklists;
    private int idCliente, idProyecto;

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
                ListaVerificacion p = adaptadorChecklists.getItem(position);
                MostrarPantallaSecciones(p);
            }
        });
    }



    @Override
    public boolean onQueryTextChange(String query) {
        adaptadorChecklists.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adaptadorChecklists.getFilter().filter(query);
        return false;
    }

    private void MostrarPantallaSecciones(ListaVerificacion lista){
        Intent mainIntent = new Intent(ListasVerificacionActivity.this,ListasVerificacionSecciones.class);
        mainIntent.putExtra(ListaVerificacion.COLUMN_ID_CLIENTE,idCliente);
        mainIntent.putExtra(ListaVerificacion.COLUMN_ID_PROYECTO,idProyecto);
        mainIntent.putExtra(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION,lista.getIdListaVerificacion());
        mainIntent.putExtra(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION,lista.getIdTipoListaVerificacion());
        ListasVerificacionActivity.this.startActivity(mainIntent);
    }

    private class TareaObtenerListado extends AsyncTask<Void, Void, ArrayList<ListaVerificacion>> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(ctx.getApplicationContext().getString(R.string.cargando_listas));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<ListaVerificacion> doInBackground(Void... param) {
            ArrayList<ListaVerificacion> resultado = new ArrayList<ListaVerificacion>();
            try {
                resultado = ListaVerificacion.obtenerListasAbiertasPorClienteProyecto(ctx,idCliente,idProyecto);
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ListasVerificacionActivity.class.getSimpleName(), "onPreExecute",
                        null, null);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(ArrayList<ListaVerificacion> resultado){
            try {
                pDialog.dismiss();
                adaptadorChecklists=  new AdaptadorChecklists(ListasVerificacionActivity.this,resultado);
                mListView.setAdapter(adaptadorChecklists);
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ListasVerificacionActivity.class.getSimpleName(), "onPostExecute",
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
            idCliente=this.getIntent().getIntExtra(Proyecto.COLUMN_ID_CLIENTE,-1);
            idProyecto=this.getIntent().getIntExtra(Proyecto.COLUMN_ID,-1);

            Log.i("INTENT PARAMS: ",String.valueOf(idCliente) + "|" + String.valueOf(idProyecto));

            if (idCliente > 0 &  idProyecto >0) return true;
        }
        catch (Exception e) {
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListasVerificacionActivity.class.getSimpleName(), "obtenerIntents",
                    null, null);
        }
        return false;
    }


}