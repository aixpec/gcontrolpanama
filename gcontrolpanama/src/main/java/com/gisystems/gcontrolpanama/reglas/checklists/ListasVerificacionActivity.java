package com.gisystems.gcontrolpanama.reglas.checklists;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.gisystems.api.EnvioDatosAPI;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorChecklists;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;

import java.util.ArrayList;

public class ListasVerificacionActivity extends AppCompatActivity implements  SearchView.OnQueryTextListener,
    ListasVerificacionNueva.NuevaListaVerificacionDialogListener
{

    private Context ctx;
    private ProgressDialog pDialog;
    private SearchView mSearchView;
    private ListView mListView;
    private AdaptadorChecklists adaptadorChecklists;
    private int idCliente, idProyecto;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Thread.setDefaultUncaughtExceptionHandler(new ManejoErrores(this));

        setContentView(R.layout.activity_listas_verificacion);

        ctx=this;
        mListView = (ListView) findViewById(R.id.activity_checklist_list);

        // Create an icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_playlist_add_black);

        FloatingActionButton.LayoutParams layoutParams = new FloatingActionButton.LayoutParams(180,180,60);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setTheme(FloatingActionButton.THEME_LIGHT)
                .setLayoutParams(layoutParams)
                .build();

        actionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                abrirDialogoCrearNuevaLista();
            }
        });

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
        mainIntent.putExtra(ListaVerificacion.COLUMN_TIPO_LISTA_VERIFICACION,lista.getTipoListaVerificacion());
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
                llenarListView(resultado);
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

    private void llenarListView(ArrayList<ListaVerificacion> listas) {
        adaptadorChecklists=  new AdaptadorChecklists(ListasVerificacionActivity.this,listas);
        mListView.setAdapter(adaptadorChecklists);
    }

    private void abrirDialogoCrearNuevaLista(){
        try {
            // Create an instance of the dialog fragment and show it
            AppCompatDialogFragment dialog = ListasVerificacionNueva.newInstance(this.idCliente, this.idProyecto);
            dialog.show(getSupportFragmentManager(), "DialogoNuevaChecklist");
        }
        catch (Exception e)
        {
            ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                    ListasVerificacionActivity.class.getSimpleName(), "abrirDialogoCrearNuevaLista",
                    null, null);
        }
    }

    public void onNuevaListaDialogPositiveClick(ArrayList<ListaVerificacion> listas) {
        llenarListView(listas);
    }



}