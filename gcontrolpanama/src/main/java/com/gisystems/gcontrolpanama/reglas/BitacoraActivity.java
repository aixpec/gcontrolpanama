package com.gisystems.gcontrolpanama.reglas;

import java.util.ArrayList;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorBitacora;
import com.gisystems.gcontrolpanama.models.ActividadAvance;
import com.gisystems.gcontrolpanama.models.AppValues.EstadosEnvio;
import com.gisystems.gcontrolpanama.models.BitacoraItem;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.Utilitarios;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;

public class BitacoraActivity extends AppCompatActivity implements  SearchView.OnQueryTextListener,
SearchView.OnCloseListener{ 

	private Context context;
	private ProgressDialog pDialog;
	private ListView mListView;
	private AdaptadorBitacora adaptador;
	private ActividadAvance avance=null;
	private EnvioDatosAPI envioDatos=null;
	private SearchView mSearchView;
	ArrayList<BitacoraItem> itemsAEnviar;
	@Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_bitacora);
        context=this;
        mListView = (ListView) findViewById(R.id.activity_bitacora_list);
        
        new TareaObtenerListadoAvances().execute();
        
        mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // Dependiendo del tipo de bitacora así va a ser el objeto a obtener
				OnItemClick(position);

			}
		});
    }

	private void OnItemClick(int posicion){
		
		
	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        //Crear un nuevo search view
    	
        mSearchView = new SearchView(getSupportActionBar().getThemedContext());
        mSearchView.setQueryHint(getResources().getString(R.string.filtrar));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnu_bitacora, menu);
        menu.findItem(R.id.mnu_bitacora_search).setActionView(mSearchView);

        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.\
    	
    	switch (item.getItemId()) {
       
        case R.id.mnu_bitacora_enviar_pendientes:
        				try{
        					
        					ArrayList<EstadosEnvio> estados=new ArrayList<EstadosEnvio>();
        					estados.add(EstadosEnvio.No_Enviado);
        					estados.add(EstadosEnvio.Pendiente_De_Confirmacion);
        					
        					itemsAEnviar= adaptador.obtenerItemsPorEstado( estados );

        					if (itemsAEnviar.size()>0)
				            	if(Utilitarios.isConnectionAvailable(context)) {
				            		new TareaEnviarAvance().execute();
				            	}
				            	else
				            	{
				            		Toast.makeText(context, R.string.verificarConexionDeInternet, Toast.LENGTH_SHORT).show();
				            	}
        					else
        					{
        						Toast.makeText(context, "Sin registros por sincronizar...", Toast.LENGTH_SHORT).show();
        					}
		            	}
			            catch (Exception e){
			            	ManejoErrores.registrarError_MostrarDialogo(context, e, 
				        			BitacoraActivity.class.getSimpleName(), "mnu_action_actualizar", 
				                    null, null);  
			            }

            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
    }

	private class TareaObtenerListadoAvances extends AsyncTask<Void, Void, ArrayList<BitacoraItem>>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			
			pDialog = new ProgressDialog(context);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(context.getApplicationContext().getString(R.string.cargando));
            pDialog.setCancelable(false);
			pDialog.show();
			
		}

		@Override
		protected ArrayList<BitacoraItem> doInBackground(Void... param) {
			 ArrayList<BitacoraItem> resultado = new ArrayList<BitacoraItem>();
			 ArrayList<ActividadAvance> avances = new ArrayList<ActividadAvance>();
			 BitacoraItem item=null;
			try {
				avances = ActividadAvance.ObtenerTodosLosAvances(context);

				for (int index=0; index<avances.size();index++){
					item=new BitacoraItem(avances.get(index),context);
					resultado.add(item);
					item=null;
				}
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						BitacoraActivity.class.getSimpleName(), "onPreExecute", 
	                    null, null);        
			}
			return resultado;
		}

		@Override
		protected void onPostExecute(ArrayList<BitacoraItem> resultado){
			try {
				pDialog.dismiss();
				adaptador =  new AdaptadorBitacora(BitacoraActivity.this,resultado, BitacoraItem.TipoBitacora.Registro_De_Avances_Con_Fotografia);
				mListView.setAdapter(adaptador);
	        	
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						BitacoraActivity.class.getSimpleName(), "onPostExecute", 
	                    null, null);   
			}
		}

	}

	private class TareaEnviarAvance extends AsyncTask<Void, Integer, Void>{
		
			int cantRegistrosPendientes=0;
			int enviosSatisfactorios=0;
			
		
		   @Override
			protected void onPreExecute(){
				super.onPreExecute();
				
				pDialog = new ProgressDialog(context);
	            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            pDialog.setMessage(context.getApplicationContext().getString(R.string.enviando_al_servidor));
	            pDialog.setCancelable(false);
				pDialog.show();
				
			}

		   
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				boolean resultado=false;
				try {

		 			envioDatos=new EnvioDatosAPI(context);
		 			cantRegistrosPendientes=itemsAEnviar.size();
					for(int index=0; index<cantRegistrosPendientes; index++){
						enviosSatisfactorios+=1;
						publishProgress(index+1);
				  		avance=(ActividadAvance) itemsAEnviar.get(index).ObtenerItem();						
				  		resultado=envioDatos.EnviarAvanceHistorico(avance);

						if (resultado){
							if (avance.getFotos()!=null)

								for(int indexFotos=0;indexFotos<avance.getFotos().size();indexFotos++)
									envioDatos.Enviar_FotografiaAvance(avance.getFotos().get(indexFotos));
						}
						else
						{
							enviosSatisfactorios-=1;
						}
					}





				}
				catch (Exception e) {
					ManejoErrores.registrarError_MostrarDialogo(context, e, 
							ActividadAgregarAvancesActivity.class.getSimpleName(), "TareaEnviarAvance_doInBackground", 
		                    null, null);  
				}
				return null;				
			}
			
			@Override
		    protected void onProgressUpdate(Integer... values) {
		        int progreso = values[0].intValue();
		        pDialog.setMessage("Enviando registro " 
		        + String.valueOf(progreso) + " de " + String.valueOf(cantRegistrosPendientes));
		        
		    }
			
			@Override
			protected void onPostExecute(Void result){
				try {
					
					pDialog.dismiss();
					/*String msj = String.format(getResources().getString(
							R.string.class_AgregarAvance_7, new Object[]{ null }) , EnviosSatisfactorios,CantFotos );*/
					Toast.makeText(context, "Han sido enviados " + String.valueOf(enviosSatisfactorios)  
							+ " de " + String.valueOf(cantRegistrosPendientes)   +   " registros pendientes.", Toast.LENGTH_SHORT).show();
					
					//Toast.makeText(context, context.getResources().getString(R.string.class_AgregarAvance_5), Toast.LENGTH_LONG).show();
					new TareaObtenerListadoAvances().execute();
						
						//new Enviar_FotografiaAvance().execute();
				
					
				} catch (Exception e) {
					ManejoErrores.registrarError_MostrarDialogo(context, e, 
							ActividadAgregarAvancesActivity.class.getSimpleName(), "TareaEnviarAvance_onPostExecute", 
		                    null, null);   
				}
			}
			   
	   }






	@Override
	public boolean onClose() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
