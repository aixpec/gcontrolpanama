package com.gisystems.gcontrolpanama.reglas.checklists;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class ListasVerificacionSecciones extends AppCompatActivity implements  SearchView.OnQueryTextListener,
        DialogoCerrarListaVerificacion.CerrarListaVerificacionDialogListener {

    private Context ctx;
    private ProgressDialog pDialog;
    private SearchView mSearchView;
    private ListView mListView;
    private AdaptadorSeccionesDeChecklist adaptadorSeccionesDeChecklist;
    private int idCliente, idProyecto, idListaVerificacion, idTipoListaVerificacion;
    private String tipoListaVerificacion;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_listas_verificacion);

        ctx=this;
        mListView = (ListView) findViewById(R.id.activity_checklist_list);
        obtenerIntents();

        this.setTitle(tipoListaVerificacion);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TipoListaVerificacion_Seccion p = adaptadorSeccionesDeChecklist.getItem(position);
                MostrarPantallaPreguntas(p);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        new TareaObtenerListado().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnu_listas_verificacion_secciones, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuaction_cerrarChecklist:
                abrirDialogoCerrarLista();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        mainIntent.putExtra(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION,idListaVerificacion);
        mainIntent.putExtra(TipoListaVerificacion_Seccion.COLUMN_ID_TIPO_LISTA_VERIFICACION,idTipoListaVerificacion);
        mainIntent.putExtra(TipoListaVerificacion_Seccion.COLUMN_ID_SECCION,seccion.getIdSeccion());
        mainIntent.putExtra(TipoListaVerificacion_Seccion.COLUMN_NOMBRE,seccion.getNombre());
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
     * Obtiene los Intents esperado por la clase: IdCliente y IdProyecto, idListaVerificacion, idTipoListaVerificacion, tipoListaVerificacion
     * @return true si los intents son válidos
     */
    private boolean obtenerIntents(){
        try{
            idCliente=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_CLIENTE,-1);
            idProyecto=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_PROYECTO,-1);
            idListaVerificacion=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION,-1);
            idTipoListaVerificacion=this.getIntent().getIntExtra(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION,-1);
            tipoListaVerificacion=this.getIntent().getStringExtra(ListaVerificacion.COLUMN_TIPO_LISTA_VERIFICACION);

            if (idCliente > 0 &  idProyecto >0 &  idListaVerificacion >0 &  idTipoListaVerificacion >0) return true;
        }
        catch (Exception e) {
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListasVerificacionSecciones.class.getSimpleName(), "obtenerIntents",
                    null, null);
        }
        return false;
    }

    private void abrirDialogoCerrarLista(){
        try {
            // Create an instance of the dialog fragment and show it
            AppCompatDialogFragment dialog = DialogoCerrarListaVerificacion.newInstance(this.idCliente, this.idListaVerificacion);
            dialog.show(getSupportFragmentManager(), "DialogoCerrarChecklist");
        }
        catch (Exception e)
        {
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListasVerificacionActivity.class.getSimpleName(), "abrirDialogoCerrarLista",
                    null, null);
        }
    }

    public void onCerrarListaDialogPositiveClick(boolean exitoso) {
        try {
            if (exitoso) {
                this.finish();
            }
        }
        catch (Exception e)
        {
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListasVerificacionActivity.class.getSimpleName(), "onCerrarListaDialogPositiveClick",
                    null, null);
        }
    }


}
