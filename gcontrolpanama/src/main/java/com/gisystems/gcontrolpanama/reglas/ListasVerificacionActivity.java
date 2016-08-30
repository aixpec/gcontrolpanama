package com.gisystems.gcontrolpanama.reglas;

import android.app.Activity;
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
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorConstrucciones;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;
import com.gisystems.utils.Utilitarios;

import java.util.ArrayList;

public class ListasVerificacionActivity extends ActionBarActivity implements  SearchView.OnQueryTextListener {

    private Context ctx;
    private ProgressDialog pDialog;
    private SearchView mSearchView;
    private ListView mListView;
    private AdaptadorChecklists adaptadorChecklists;
    private Proyecto proyecto;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_listas_verificacion);

        ctx=this;
        mListView = (ListView) findViewById(R.id.activity_checklist_list);


        new TareaObtenerListado().execute();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                ListaVerificacion p = (ListaVerificacion) adaptadorChecklists.getItem(position);
                //IniciarConstruccion(p);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //Crear un nuevo search view

        mSearchView = new SearchView(getSupportActionBar().getThemedContext());
        mSearchView.setQueryHint(getResources().getString(R.string.buscar));
        mSearchView.setOnQueryTextListener(this);
        //mSearchView.setOnCloseListener(this);

        //Barra de titulo de construcciones
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return false;
        }

        Proyecto proyecto = (Proyecto) extras.getSerializable("Proyecto");
        getSupportActionBar().setTitle(proyecto.getAliasProyecto());

        //Bundle fragArgs= new Bundle();
        //fragArgs.putSerializable("Proyecto", proyecto);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.mnu_action_search).setActionView(mSearchView);

        menu.findItem(R.id.mnu_action_actualizar).setVisible(false);
        menu.findItem(R.id.mnu_action_construcciones).setVisible(false);
        menu.findItem(R.id.mnu_action_activs).setVisible(false);
        menu.findItem(R.id.mnu_action_bitacora_fotos).setVisible(false);
        //menu.findItem(R.id.mnu_action_search).setVisible(false);
        //menu.findItem(R.id.mnu_action_bitacora).setVisible(false);
        menu.findItem(R.id.mnu_action_salir).setVisible(false);
        menu.findItem(R.id.mnu_action_acercaDe).setVisible(false);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.\
        Intent mainIntent=null;
        switch (item.getItemId()) {
            case R.id.mnu_action_proyectos:
                mainIntent= new Intent(ctx,ProyectosListadoActivity.class);
                ListasVerificacionActivity.this.startActivity(mainIntent);
                ListasVerificacionActivity.this.finish();
                return true;
            case R.id.mnu_action_salir:
                mainIntent= new Intent(ListasVerificacionActivity.this,ProyectosListadoActivity.class );
                ListasVerificacionActivity.this.startActivity(mainIntent);
                ListasVerificacionActivity.this.finish();
                return true;
            case R.id.mnu_action_actualizar:

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getResources().getString(R.string.ProyectosListadoActivity_0))
                        .setMessage(getResources().getString(R.string.ProyectosListadoActivity_10))
                        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    if(Utilitarios.isConnectionAvailable(ctx)) {
                                        //new InicializarBDTask().execute();
                                    }
                                    else
                                    {
                                        Toast.makeText(ctx, R.string.verificarConexionDeInternet, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception e){
                                    ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                                            ListasVerificacionActivity.class.getSimpleName(), "mnu_action_actualizar",
                                            null, null);
                                }
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

                return true;
            case R.id.mnu_action_bitacora:
                mainIntent = new Intent(ListasVerificacionActivity.this,BitacoraActivity.class);
                ListasVerificacionActivity.this.startActivity(mainIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
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

    private void IniciarConstruccion(Construccion Construccion){
        Intent mainIntent = new Intent(ListasVerificacionActivity.this,ConstruccionDetalleActivity.class);
        mainIntent.putExtra("Construccion", Construccion);
        ListasVerificacionActivity.this.startActivity(mainIntent);

        //ListasVerificacionActivity.this.finish();

        //ProjectsActivity.this.finish();
    }


    @Override
    public void onBackPressed() {
        Intent mainIntent=null;
        mainIntent= new Intent(ctx,ProyectosListadoActivity.class);
        ListasVerificacionActivity.this.startActivity(mainIntent);
        ListasVerificacionActivity.this.finish();
    }


    private class TareaObtenerListado extends AsyncTask<Void, Void, ArrayList<ListaVerificacion>> {


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
        protected ArrayList<ListaVerificacion> doInBackground(Void... param) {
            ArrayList<ListaVerificacion> resultado = new ArrayList<ListaVerificacion>();

            Bundle extras = getIntent().getExtras();
            if (extras != null) {

                try {
                    Proyecto proyecto = (Proyecto) extras.getSerializable("Proyecto");
                    resultado = ListaVerificacion.obtenerListasAbiertasPorClienteProyecto(ctx,proyecto.getIdCliente(),proyecto.getIdProyecto());
                } catch (Exception e) {
                    ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                            ListasVerificacionActivity.class.getSimpleName(), "onPreExecute",
                            null, null);
                }
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


    private boolean EstablecerValores()
    {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return false;
        }

        try{
            //Proyecto proyecto = (Proyecto) extras.getSerializable("Proyecto");

            new TareaObtenerListado().execute();
        }
        catch(Exception ex){
            ManejoErrores.registrarError_MostrarDialogo(ctx, ex,
                    ListasVerificacionActivity.class.getSimpleName(), "EstablecerValores",
                    null, null);
            return false;
        }
        return true;
    }

}