package com.gisystems.gcontrolpanama.reglas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;

import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.reglas.checklists.ListasVerificacionActivity;
import com.gisystems.utils.Utilitarios;

import java.util.concurrent.ExecutionException;


/**
 * Created by aixpec on 19/07/16.
 */
public class ActividadMainMenu extends AppCompatActivity implements View.OnClickListener {

    private Context ctx;
    private ProgressDialog pDialog;
    private SimpleCursorAdapter sCAProyecto = null;
    private AutoCompleteTextView aCTVProyecto;
    private Cursor c=null;
    private int idClienteSeleccionado;
    private int idProyectoSeleccionado;
    private ImageButton ibAvances, ibFotografia, ibBitacora, ibChekList;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main_menu);

        ctx=this;

        establecerComportamientoControles();
    }

    /**
     * Obtener los controles de la vista
     */
    private void obtenerControles(){
        //Obtener vistas
        aCTVProyecto = (AutoCompleteTextView)
                findViewById(R.id.main_menu_aCProyecto);

        ibAvances = (ImageButton)
                findViewById(R.id.main_menu_ibAvances);

        ibFotografia = (ImageButton)
                findViewById(R.id.main_menu_ibFotografia);

        ibBitacora = (ImageButton)
                findViewById(R.id.main_menu_ibBitacora);

        ibChekList = (ImageButton)
                findViewById(R.id.main_menu_ibChekList);
    }



    /**
     * Establecer el comportamiento inicial que tendrán los controles
     */
    private void establecerComportamientoControles(){

        //1. Obtener los controles de la vista
        obtenerControles();

        //2. Habilitar foco en modo touch para los ImageButton, para poder cambiar el foco desde ACTV
        //después de seleccionado el proyecto
        establecerFocoEnModoTouchImageButton(true);

        //3. Construir el Adaptador que utilizará el ACTV del proyecto
        construirAdaptadorProyecto();

        //4. Establecer listener de cambio de foco, para validar el proyecto seleccionado
        aCTVProyecto.setOnFocusChangeListener(new FocusListener());

        //5. Establecer la acción al momento que el usuario seleccione un proyecto
        aCTVProyecto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                // Get the Item Number from this row in the database.
                idClienteSeleccionado = cursor.getInt(cursor.getColumnIndexOrThrow(Proyecto.COLUMN_ID_CLIENTE));
                idProyectoSeleccionado = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

                //Pasar el foco al botón ingresar avances, para validar el texto ingresado
                ibAvances.requestFocus();

                //Ocultar el teclado del dispositivo
                Utilitarios.ocultarTeclado(getApplicationContext(), ibAvances);

            }

        });

        /*
         * Establecer un listener para que este atento cuando el usuario modifique el texto en el ACTV
         * y provoque que éste falle. Y así forzamos al usuario a que elija el proyecto  desde
         * el listado de sugerencias
         */
        aCTVProyecto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Establecemos la variable del proyecto seleccionado a -1, para indicar que
                //el usuario a modificado o no ha seleccionado el proyecto desde la lista de sugerencias
                idClienteSeleccionado = -1;
                idProyectoSeleccionado = -1;

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        //Establecer que el ACTV muestre las sugerencias al ingresar el primer carácter
        aCTVProyecto.setThreshold(1);

        //Establecer el adaptador del ACTV
        aCTVProyecto.setAdapter(sCAProyecto);

        ibAvances.setOnClickListener(this);
        ibChekList.setOnClickListener(this);
    }


    /**
     * Habilitar foco en modo touch para los ImageButton, para poder cambiar el foto desde ACTV
     * @param encendido
     */
    private void establecerFocoEnModoTouchImageButton(boolean encendido){

        ibAvances.setFocusableInTouchMode(encendido);
        ibFotografia.setFocusableInTouchMode(encendido);
        ibBitacora.setFocusableInTouchMode(encendido);
        ibChekList.setFocusableInTouchMode(encendido);



    }

    /**
     * Construye el adaptador para el proyecto
     */
    private void construirAdaptadorProyecto(){

        String[] colsAd=new String[]{Proyecto.COLUMN_NOMBRE_PROYECTO};
        int[] adapterRowViews=new int[]{android.R.id.text1};

        //Inicializar el adaptador
        sCAProyecto= new SimpleCursorAdapter(getApplicationContext(),
                R.layout.gc_list_item, c, colsAd, adapterRowViews,0);

        //Establecer el filtro para el adaptador
        sCAProyecto.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                Cursor c = null;
                try {
                    //Obtener los proyectos que contienen en su nombre el texto ingresado
                    c = new TareaObtenerListadoProyectos().execute(str).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return c;
            } });



        //Establecer que el nombre del proyecto seleccionado
        sCAProyecto.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndexOrThrow(Proyecto.COLUMN_NOMBRE_PROYECTO);
                return cur.getString(index);
            }});

        //Establecer el layout gc_list_item como el estilo del dropdown del Adaptador
        sCAProyecto.setDropDownViewResource(R.layout.gc_list_item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        //menu.findItem(R.id.mnu_action_proyectos).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.\
        Intent mainIntent=null;
        switch (item.getItemId()) {
            case R.id.mnu_action_salir:
                mainIntent= new Intent(ActividadMainMenu.this,LoginActivity.class);
                ActividadMainMenu.this.startActivity(mainIntent);
                ActividadMainMenu.this.finish();
                return true;
            case R.id.mnu_action_actualizar:
                IrAActividad(R.id.mnu_action_actualizar);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Obtiene los proyectos almacenados en la base de datos, que coinciden con el texto enviado
     */
    private class TareaObtenerListadoProyectos extends AsyncTask<CharSequence, Void, Cursor> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(ctx.getApplicationContext().getString(R.string.cargando_proyectos));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Cursor doInBackground(CharSequence... param) {
            try {

                c=Proyecto.obtenerCursorProyectos(getApplicationContext(), param[0]);
                return c;

            } catch (Exception e) {

                pDialog.dismiss();

                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadMainMenu.class.getSimpleName(), "onPreExecute",
                        null, null);
            }

            return null;

        }

        @Override
        protected void onPostExecute(Cursor resultado){
            try {
                pDialog.dismiss();
            } catch (Exception e) {
                pDialog.dismiss();
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ActividadMainMenu.class.getSimpleName(), "onPostExecute",
                        null, null);

            }
        }

    }


    class FocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (v.getId() == R.id.main_menu_aCProyecto && !hasFocus) {

                if ( !validarIds() ){
                    mostrarMensajeErrorProyecto();
                    limpiarTextoACTVProycecto();
                }
                else{
                    Log.i("IdProyectoSeleccionado", String.valueOf(idProyectoSeleccionado));
                }

                Utilitarios.ocultarTeclado(getApplicationContext(),ibAvances);

            }

        }
    }


    public void IrAActividad(int Id)
    {
        Intent mainIntent=null;

        switch (Id){

            case R.id.main_menu_ibAvances:
                mainIntent = new Intent(ActividadMainMenu.this,ActividadAgregarAvances.class);
                mainIntent.putExtra(Proyecto.COLUMN_ID_CLIENTE,idClienteSeleccionado);
                mainIntent.putExtra(Proyecto.COLUMN_ID,idProyectoSeleccionado);
                ActividadMainMenu.this.startActivity(mainIntent);
            break;

            case R.id.main_menu_ibChekList:
                mainIntent = new Intent(ActividadMainMenu.this,ListasVerificacionActivity.class);
                mainIntent.putExtra(Proyecto.COLUMN_ID_CLIENTE,idClienteSeleccionado);
                mainIntent.putExtra(Proyecto.COLUMN_ID,idProyectoSeleccionado);
                ActividadMainMenu.this.startActivity(mainIntent);
                break;

            case R.id.mnu_action_actualizar:
                mainIntent= new Intent(ActividadMainMenu.this,ActividadSincronizacion.class);
                mainIntent.putExtra(Proyecto.COLUMN_ID_CLIENTE,idClienteSeleccionado);
                mainIntent.putExtra(Proyecto.COLUMN_ID,idProyectoSeleccionado);
                ActividadMainMenu.this.startActivity(mainIntent);
                break;
        }
            //ActividadMainMenu.this.finish();
    }

    public void mostrarMensajeErrorProyecto(){
        Toast.makeText(getApplicationContext(), "Por favor seleccione un componente válido", Toast.LENGTH_LONG).show();
    }

    public void limpiarTextoACTVProycecto(){
        aCTVProyecto.setText("");
    }

    //Valida que el id del proyecto sea válido
    public boolean validarIds() {
        if ( idClienteSeleccionado <=0 || idProyectoSeleccionado<=0 ) {
            return false;
        }else{
            return true;
        }
    }


    public void onClick(View v) {
        // do something when the button is clicked
        if (validarIds()) {
            switch (v.getId()) {

                case R.id.main_menu_ibAvances:
                    IrAActividad(v.getId());
                    break;

                case R.id.main_menu_ibChekList:
                    IrAActividad(v.getId());
                    break;
            }
        }
        else {
            mostrarMensajeErrorProyecto();
            limpiarTextoACTVProycecto();
        }

    }

}


