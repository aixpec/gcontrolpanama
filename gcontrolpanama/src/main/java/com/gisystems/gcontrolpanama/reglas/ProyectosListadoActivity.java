package com.gisystems.gcontrolpanama.reglas;

import java.util.ArrayList;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.api.RecepcionDatosAPI;


import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorProyectos;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.FotoActividad;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.Utilitarios;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ProyectosListadoActivity extends AppCompatActivity {

	private Context ctx;
	private ProgressDialog pDialog;
	private SearchView mSearchView;
	private ListView mListView;
	private AdaptadorProyectos adaptadorProyectos;
	private EnvioDatosAPI envioDatos;


	@Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main_menu);

        ctx=this;
        

        
        new TareaObtenerListadoProyectos().execute();


        /*mListView = (ListView) findViewById(R.id.activity_main_list);
        mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
				Proyecto p = (Proyecto) adaptadorProyectos.getItem(position);
				IniciarDetalleProyecto(p);

				
			}
		});*/
        
    }
 /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    } */
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        //Crear un nuevo search view
    	
       /* mSearchView = new SearchView(getSupportActionBar().getThemedContext());
        mSearchView.setQueryHint(getResources().getString(R.string.buscar));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);*/
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        //menu.findItem(R.id.mnu_action_search).setActionView(mSearchView);
         
       // menu.findItem(R.id.mnu_action_proyectos).setVisible(false);
		//menu.findItem(R.id.mnu_action_construcciones).setVisible(false);
       // menu.findItem(R.id.mnu_action_activs).setVisible(false);
     
        
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
        	mainIntent= new Intent(ProyectosListadoActivity.this,LoginActivity.class);
        	ProyectosListadoActivity.this.startActivity(mainIntent);
        	ProyectosListadoActivity.this.finish();

            return true;
        case R.id.mnu_action_actualizar:

        /*	new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(getResources().getString(R.string.ProyectosListadoActivity_0))
	        .setMessage(getResources().getString(R.string.ProyectosListadoActivity_10))
	        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {	            
	        	@Override
	            public void onClick(DialogInterface dialog, int which) {
		            	try{
			            	if(Utilitarios.isConnectionAvailable(ctx)) {
			            		new InicializarBDTask().execute();
			            	}
			            	else
			            	{
			            		Toast.makeText(ctx, R.string.verificarConexionDeInternet, Toast.LENGTH_SHORT).show();
			            	}
		            	}
			            catch (Exception e){
			            	ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
				        			ProyectosListadoActivity.class.getSimpleName(), "mnu_action_actualizar", 
				                    null, null);  
			            }
	            	}
	            })
	        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {  
	    			
	            }
	        })
	        .show();*/
        	
            return true;
        case R.id.mnu_action_bitacora:
        	mainIntent = new Intent(ProyectosListadoActivity.this,BitacoraActivity.class);
        	ProyectosListadoActivity.this.startActivity(mainIntent);
        	return true;
		case R.id.mnu_action_bitacora_fotos:

				/*new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(getResources().getString(R.string.ProyectosListadoActivity_40))
						.setMessage(getResources().getString(R.string.ProyectosListadoActivity_50))
						.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								try{
									if(Utilitarios.isConnectionAvailable(ctx)) {
										new enviar_Fotografias().execute();
									}
									else
									{
										Toast.makeText(ctx, R.string.verificarConexionDeInternet, Toast.LENGTH_SHORT).show();
									}
								}
								catch (Exception e){
									ManejoErrores.registrarError_MostrarDialogo(ctx, e,
											ProyectosListadoActivity.class.getSimpleName(), "mnu_action_actualizar",
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
*/
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
/*
	@Override
	public boolean onQueryTextChange(String query) {
		// TODO Auto-generated method stub
		adaptadorProyectos.getFilter().filter(query);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		adaptadorProyectos.getFilter().filter(query);
	      return false;
	}

	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub
		
		return false;
	}

	private void IniciarDetalleProyecto(Proyecto Proyecto){
		//Intent mainIntent = new Intent(ProyectosListadoActivity.this,ProyectoDetalleActivity.class);

		Intent mainIntent = new Intent(ProyectosListadoActivity.this, ConstruccionesListadoActivity.class);
		mainIntent.putExtra("Proyecto", Proyecto);
		ProyectosListadoActivity.this.startActivity(mainIntent);
		ProyectosListadoActivity.this.finish();

	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getResources().getString(R.string.abandonar))
        .setMessage(getResources().getString(R.string.ProyectosListadoActivity_30))
        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	finish();
            }
        })
        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {  
    			
            }
        })
        .show();
	}
	*/

	private class TareaObtenerListadoProyectos extends AsyncTask<Void, Void, ArrayList<Proyecto>>{
		
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
		protected ArrayList<Proyecto> doInBackground(Void... param) {
			 ArrayList<Proyecto> resultado = new ArrayList<Proyecto>();
			try {
				resultado = Proyecto.obtenerTodosProyectos(ctx);
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
						ProyectosListadoActivity.class.getSimpleName(), "onPreExecute", 
	                    null, null);        
			}
			return resultado;
		}

		@Override
		protected void onPostExecute(ArrayList<Proyecto> resultado){
			try {
				pDialog.dismiss();
				adaptadorProyectos =  new AdaptadorProyectos(ProyectosListadoActivity.this,resultado);
				mListView.setAdapter(adaptadorProyectos);
	        	
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
						ProyectosListadoActivity.class.getSimpleName(), "onPostExecute", 
	                    null, null);   
			}
		}

	}
	/*
private class InicializarBDTask extends AsyncTask<Object, Void, Boolean>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(ctx.getApplicationContext().getString(R.string.actualizandoDb));
            pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Object... param) {
			boolean resultado = false;
			try {
				
	            RecepcionDatosAPI recepcionDatosAPI = new RecepcionDatosAPI(ctx);
 			    resultado = recepcionDatosAPI.InicializarBD();
 			   
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
						ProyectosListadoActivity.class.getSimpleName(), "InicializarBDTask", 
	                    null, null);        
			}
			return resultado;
		}

		@Override
		protected void onPostExecute(Boolean resultado){
			try {
				pDialog.dismiss();
				if(resultado){
					Toast.makeText(ctx, "La base de datos ha sido actualizada...", Toast.LENGTH_SHORT).show();
					new TareaObtenerListadoProyectos().execute();
				}
				else
				{
					Toast.makeText(ctx, "No ha sido posible actualizar la base de datos. ", Toast.LENGTH_SHORT).show();
				}
				
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
						ProyectosListadoActivity.class.getSimpleName(), "InicializarBDTask_onPostExecute", 
	                    null, null);   
			}
		}
	}



	private class enviar_Fotografias extends AsyncTask<Void, Integer, Boolean>{

		private int CantFotos=0;

		private ArrayList<FotoActividad> FotografiasDeActividad;
		private String msj;
		private int EnviosSatisfactorios=0;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(ctx);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage(ctx.getApplicationContext().getString(R.string.enviando_al_servidor));
			pDialog.setCancelable(false);
			pDialog.show();

					FotografiasDeActividad= FotoActividad.obtenerFotosSinEnviarSinAvance(ctx);
					CantFotos=FotografiasDeActividad.size();


		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			boolean resultado=false;

			try {

				envioDatos=new EnvioDatosAPI(ctx);
						for(int index=0;index<CantFotos;index++){
							EnviosSatisfactorios+=1;
							publishProgress(index+1);

							resultado=envioDatos.Enviar_FotografiaSinAvance(FotografiasDeActividad.get(index));
							if (resultado)
								FotoActividad.actualizarEdoFotografia(ctx,
										FotografiasDeActividad.get(index), AppValues.EstadosEnvio.Enviado);


				}

			}
			catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(ctx, e,
						FotoNuevaActivity.class.getSimpleName(), "enviar_Fotografias_doInBackground",
						null, null);
			}

			return resultado;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int progreso = values[0].intValue();

			msj=String.format(String.format(getResources().getString(R.string.class_AgregarAvance_10),
					new Object[]{null}), progreso, CantFotos);
			pDialog.setMessage(msj);


		}

		@Override
		protected void onPostExecute(Boolean resultado){
			try {

				pDialog.dismiss();
				     Toast.makeText(ctx, "Han sido enviadas " + String.valueOf(EnviosSatisfactorios)
							+ " de " + String.valueOf(CantFotos)   +   " fotos pendientes.", Toast.LENGTH_SHORT).show();



			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(ctx, e,
						FotoNuevaActivity.class.getSimpleName(), "enviar_Fotografias_onPostExecute",
						null, null);
			}
		}

	}

*/
}
