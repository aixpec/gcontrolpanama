package com.gisystems.gcontrolpanama.reglas;

import java.util.ArrayList;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.fragments.FragmentAgregarAvancesActividad;
import com.gisystems.gcontrolpanama.fragments.FragmentFotosActividadActivity;
import com.gisystems.gcontrolpanama.fragments.FragmentFotosActividadActivity.FotografiaListener;
import com.gisystems.gcontrolpanama.models.Actividad;
import com.gisystems.gcontrolpanama.models.ActividadAvance;
import com.gisystems.gcontrolpanama.models.FotoActividad;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.CustomPagerAdapter;
import com.gisystems.utils.Fragmento;
import com.gisystems.utils.Utilitarios.TipoFoto;
import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.api.RecepcionDatosAPI;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class ActividadAgregarAvancesActivity extends ActionBarActivity implements FotografiaListener  {
	
	CustomPagerAdapter mCustomPagerAdapter;
	ViewPager mViewPager;
	ArrayList<Fragmento> fragmentos=new ArrayList<Fragmento>();
	Context context;
	Actividad actividad;
	Construccion construccion;
	ActividadAvance NuevoAvance;
	FragmentAgregarAvancesActividad fAvances;
	FragmentFotosActividadActivity fFotos;
	boolean valido=false;
	double montoAvance=0;
	private ProgressDialog pDialog;
	private EnvioDatosAPI envioDatos;
	
	@Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_agregar_avances_actividad);
        context=this;
        try{
        	EstablecerValores();
        	
        	mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), context);	  	       
  	        mViewPager = (ViewPager) findViewById(R.id.pager); 	        
  	      
  	        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() 
	  	    {
	  	        @Override
	  	        public void onPageSelected(int position) 
	  	        {
	  	        	if (fFotos==null){
	  	        			fFotos=(FragmentFotosActividadActivity)
	  	    				((CustomPagerAdapter)mViewPager.getAdapter()).getFragment(1);
	  	        			fFotos.setFotografiaListener(ActividadAgregarAvancesActivity.this);
	  	        	}
	  	        }

	  	        @Override
	  	        public void onPageScrollStateChanged (int arg0)
	  	        {
	  	            
	  	        }
	  	    });
  	        
  	        mCustomPagerAdapter.setFragmentos(fragmentos);
  	        mViewPager.setAdapter(mCustomPagerAdapter);
        }
	    catch(Exception ex){
	    	ManejoErrores.registrarError_MostrarDialogo(context, ex, 
					ActividadAgregarAvancesActivity.class.getSimpleName(), "onCreate", 
	                null, null);
	    }
	}

	private boolean EstablecerValores()
	 {
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
         return false;
		}
	              
       try{
	        actividad = (Actividad) extras.getSerializable("Actividad");
		    construccion = (Construccion) extras.getSerializable("Construccion");

		    getSupportActionBar().setTitle(actividad.getCodigoInstitucional());

	        //Agregar los fragments
	        Bundle fragArgs= new Bundle();
	        fragArgs.putSerializable("Actividad", actividad);
	     
	        fragmentos.add(new Fragmento(FragmentAgregarAvancesActividad.class.getName(),this.getResources().getString(R.string.class_AgregarAvance_0),fragArgs));
	        fragmentos.add(new Fragmento(FragmentFotosActividadActivity.class.getName(),this.getResources().getString(R.string.class_AgregarAvance_1),fragArgs));
       }
       catch(Exception ex){
       	ManejoErrores.registrarError_MostrarDialogo(context, ex, 
   				ActividadAgregarAvancesActivity.class.getSimpleName(), "EstablecerValores", 
                   null, null);
       	return false;
       }
       return true;
	 }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actividad_avance, menu);
        menu.findItem(R.id.mnuaction_seleccionar_todas).setVisible(false);
		menu.findItem(R.id.mnuaction_eliminar).setVisible(false);
		menu.findItem(R.id.mnuaction_cancelar).setVisible(true);
		menu.findItem(R.id.mnuaction_aceptar).setVisible(true);
		menu.findItem(R.id.mnuaction_avanceActiv_agregarFoto).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }
	
   @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
	   
	   if (fFotos!=null){
		   int noSel = fFotos.adaptadorFotosActividad.ObtenerTotalFotosSeleccionadas();
		   if (noSel>0){
			   menu.findItem(R.id.mnuaction_seleccionar_todas).setVisible(true);
			   menu.findItem(R.id.mnuaction_eliminar).setVisible(true);
			   menu.findItem(R.id.mnuaction_cancelar).setVisible(false);
			   menu.findItem(R.id.mnuaction_aceptar).setVisible(false);
			   menu.findItem(R.id.mnuaction_avanceActiv_agregarFoto).setVisible(false);
			   
		   }
		   else
		   {
			   menu.findItem(R.id.mnuaction_seleccionar_todas).setVisible(false);
			   menu.findItem(R.id.mnuaction_eliminar).setVisible(false);
			   menu.findItem(R.id.mnuaction_cancelar).setVisible(true);
			   menu.findItem(R.id.mnuaction_aceptar).setVisible(true);
			   menu.findItem(R.id.mnuaction_avanceActiv_agregarFoto).setVisible(true);
		   }
		   
	   }
        return super.onPrepareOptionsMenu(menu);
    }
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	        case R.id.mnuaction_avanceActiv_agregarFoto:
	        	IniciarNuevaFoto();
	            return true;
	        case R.id.mnuaction_aceptar:
	         	fAvances= (FragmentAgregarAvancesActividad) 
	    		((CustomPagerAdapter)mViewPager.getAdapter()).getFragment(0);
	         	fFotos=(FragmentFotosActividadActivity)
   				((CustomPagerAdapter)mViewPager.getAdapter()).getFragment(1);
	         	AlertDialog.Builder alertDialogBuilder;
	         	AlertDialog alertDialog;
        		if (fAvances!=null){
        			if (!fAvances.isValido())
        			{
        				if (fAvances.getMensajeError()!=null )
        			     Log.w("", "fAvances!=null; !fAvances.isValido(); fAvances.getMensajeError()!=null;");
	  	    			 Toast.makeText(context, fAvances.getMensajeError(), Toast.LENGTH_LONG).show();
        				 return true;
        			}
        		}else return true;
        	
        		if (fFotos!=null){
        			if (!fFotos.isValido())
        				if (fFotos.getMensajeError()!=null){
        					 Log.w("", "fFotos!=null; !fFotos.isValido();fFotos.getMensajeError()!=null;");
        					Toast.makeText(context,  fFotos.getMensajeError(), Toast.LENGTH_LONG).show();
        					return true;	
        				}
        		}else return true;
        		
        		//Insertar actividad
        		alertDialogBuilder = new AlertDialog.Builder(this);   	    	
    			alertDialogBuilder.setTitle(this.getResources().getString(R.string.aviso));
    			alertDialogBuilder
    			.setMessage(this.getResources().getString(R.string.class_AgregarAvance_2))
    			.setCancelable(false)
    			.setPositiveButton(this.getResources().getString(R.string.si),new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog,int id) {
    					NuevoAvance=new ActividadAvance();
    					NuevoAvance.setIdCliente(actividad.getIdCliente());
						NuevoAvance.setIdProyecto(actividad.getIdProyecto());
						NuevoAvance.setIdConstruccion(actividad.getIdConstruccion());
    					NuevoAvance.setIdActividad(actividad.getIdActividad());
    					NuevoAvance.setComentario("");
    					NuevoAvance.setFotos(fFotos.getFotos());
    					NuevoAvance.setMontoNuevoAvance(fAvances.getMontoAvance());
    					TareaInsertarAvance t= new TareaInsertarAvance();
    					t.execute();
    				}
    			  })
    			.setNegativeButton(this.getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog,int id) {
    						dialog.cancel();
    					}
    				});
    			alertDialog = alertDialogBuilder.create();   			
    			alertDialog.show();
        		
				return true;
	        case R.id.mnuaction_cancelar:
	        	cancelarAvance();
	        	return true;
	        case R.id.mnuaction_seleccionar_todas:
	        	if(fFotos!=null){
	        		fFotos.establecerSeleccionTodasLasFotografias();
	        	}
	        	return true;
	        case R.id.mnuaction_eliminar:
	        	alertDialogBuilder = new AlertDialog.Builder(this);
	    		alertDialogBuilder.setTitle(this.getResources().getString(R.string.aviso));
	    		alertDialogBuilder
	    		.setMessage(this.getResources().getString(R.string.class_AgregarAvance_9))
	    		.setCancelable(false)
	    		.setPositiveButton(this.getResources().getString(R.string.si),new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog,int id) {
	    				if(fFotos!=null){
	    	        		fFotos.EliminarFotosSeleccionadas();
	    	        	}
	    			}
	    		  })
	    		.setNegativeButton(this.getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog,int id) {
	    					dialog.cancel();
	    				}
	    			});
	    		alertDialog = alertDialogBuilder.create();
	    		alertDialog.show();
	        	
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
    	}
    }
    
   private void IniciarNuevaFoto(){
		Intent mainIntent = new Intent(ActividadAgregarAvancesActivity.this,FotoNuevaActivity.class);
		mainIntent.putExtra("TipoFoto", TipoFoto.ActividadConAvance);
		mainIntent.putExtra("Actividad", actividad);
		ActividadAgregarAvancesActivity.this.startActivity(mainIntent);
	}
   
   private class TareaInsertarAvance extends AsyncTask<Void, Void, Long>{	   
	   @Override
		protected void onPreExecute(){
			super.onPreExecute();
			
			pDialog = new ProgressDialog(context);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(context.getApplicationContext().getString(R.string.almacenando_en_dispositivo));
            pDialog.setCancelable(false);
			pDialog.show();
			
		}

		@Override
		protected Long doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Long l=-Long.valueOf(-1);
			
			try {				
				l=ActividadAvance.insertarAvanceConFotografias(context,NuevoAvance);
				
			}
			catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						ActividadAgregarAvancesActivity.class.getSimpleName(), "TareaInsertarAvance_doInBackground", 
	                    null, null);  
			}
			
			return l;
		}
		
		@Override
		protected void onPostExecute(Long resultado){
			try {
				
				
	            
				dismissProgressDialog();
				
				if( resultado!=-1){	
					int idAntiguedad=Integer.valueOf(String.valueOf(resultado));
					NuevoAvance.setIdTemporal(idAntiguedad);
					for(int i=0;i<NuevoAvance.getFotos().size();i++){
				 		NuevoAvance.getFotos().get(i).setIdAntiguedad(idAntiguedad);
				 		
					}
					Toast.makeText(context, context.getResources().getString(R.string.class_AgregarAvance_3), Toast.LENGTH_LONG).show();
					new TareaEnviarAvance().execute();
					
				}
				else{
					Toast.makeText(context, context.getResources().getString(R.string.class_AgregarAvance_4), Toast.LENGTH_LONG).show();
				}
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						ActividadAgregarAvancesActivity.class.getSimpleName(), "TareaInsertarAvance_onPostExecute", 
	                    null, null);   
			}
		}
		   
   }
  
   private class TareaEnviarAvance extends AsyncTask<Void, Void, Boolean>{	   
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
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			boolean resultado=false;
			
			try {			
				envioDatos=new EnvioDatosAPI(context);
				resultado=envioDatos.EnviarAvanceHistorico(NuevoAvance);	
			}
			catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						ActividadAgregarAvancesActivity.class.getSimpleName(), "TareaEnviarAvance_doInBackground", 
	                    null, null);  
			}
			
			return resultado;
		}
		
		@Override
		protected void onPostExecute(Boolean resultado){
			try {
				
				
				dismissProgressDialog();
				
				if(resultado){
					//Toast.makeText(context, context.getResources().getString(R.string.class_AgregarAvance_5), Toast.LENGTH_LONG).show();
					new Enviar_FotografiaAvance().execute();
				}
				else{
					Log.w("resultado avance",String.valueOf(resultado));
					Toast.makeText(context, context.getResources().getString(R.string.class_AgregarAvance_6), Toast.LENGTH_LONG).show();
					finish();
				}
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						ActividadAgregarAvancesActivity.class.getSimpleName(), "TareaEnviarAvance_onPostExecute", 
	                    null, null);   
			}
		}
		   
   }
  
   private class Enviar_FotografiaAvance extends AsyncTask<Void, Integer, Boolean>{	
	   
	   private int CantFotos=0;
	  // private int EnviosSatisfactorios=0;
	   @Override
		protected void onPreExecute(){
			super.onPreExecute();
			
			pDialog = new ProgressDialog(context);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(context.getApplicationContext().getString(R.string.enviando_al_servidor));
            pDialog.setCancelable(false);
			pDialog.show();
			
			CantFotos=NuevoAvance.getFotos().size();
				
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			boolean resultado=false;
		
			try {			
				
				envioDatos=new EnvioDatosAPI(context);
				
				for(int index=0;index<CantFotos;index++){
					
					publishProgress(index+1);
					resultado=envioDatos.Enviar_FotografiaAvance(NuevoAvance.getFotos().get(index));
					
					//Sumar resultado
					//if(resultado)EnviosSatisfactorios+=1;
					
				}
			}
			catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						ActividadAgregarAvancesActivity.class.getSimpleName(), "Enviar_FotografiaAvance_doInBackground", 
	                    null, null);  
			}
			
			return resultado;
		}
		
		
		@Override
	    protected void onProgressUpdate(Integer... values) {
	        int progreso = values[0].intValue();
	        String msj = String.format(String.format(context.getApplicationContext().getString(R.string.class_AgregarAvance_10), 
	        		new Object[]{ null }) , progreso,CantFotos );
	        pDialog.setMessage(msj);
	    }
		
		@Override
		protected void onPostExecute(Boolean resultado){
			try {
				
	            
				dismissProgressDialog();
				//String msj = String.format(getResources().getString(R.string.class_AgregarAvance_7, new Object[]{ null }) , EnviosSatisfactorios,CantFotos );
				//Toast.makeText(context, msj, Toast.LENGTH_LONG).show();
				new InicializarBDTask().execute();
				
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						ActividadAgregarAvancesActivity.class.getSimpleName(), "Enviar_FotografiaAvance_onPostExecute", 
	                    null, null);   
			}
		}
		   
   }	
    
   private class InicializarBDTask extends AsyncTask<Object, Void, Boolean>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(context.getApplicationContext().getString(R.string.actualizandoDb));
            pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Object... param) {
			boolean resultado = false;
			try {
				
	            RecepcionDatosAPI recepcionDatosAPI = new RecepcionDatosAPI(context);
 			    resultado = recepcionDatosAPI.InicializarBD();
 			   
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
	        			ActividadAgregarAvancesActivity.class.getSimpleName(), "InicializarBDTask", 
	                    null, null);        
			}
			return resultado;
		}

		@Override
		protected void onPostExecute(Boolean resultado){
			try {
				
	            
				dismissProgressDialog();
				
				
				if(resultado){
					Toast.makeText(context, "La base de datos ha sido actualizada...", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(context, "No ha sido posible actualizar la base de datos. ", Toast.LENGTH_SHORT).show();
				}
				finish();
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
	        			ActividadAgregarAvancesActivity.class.getSimpleName(), "InicializarBDTask_onPostExecute", 
	                    null, null);   
			}
		}
		
		
		
	}
   

   
    private void cancelarAvance(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    	
		alertDialogBuilder.setTitle(this.getResources().getString(R.string.aviso));
		
		alertDialogBuilder
		.setMessage(this.getResources().getString(R.string.class_AgregarAvance_8))
		.setCancelable(false)
		.setPositiveButton(this.getResources().getString(R.string.si),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				finish();
			}
		  })
		.setNegativeButton(this.getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});

		AlertDialog alertDialog = alertDialogBuilder.create();
		
		alertDialog.show();
    }

	@Override
	public void onFotografiaSeleccionada(FotoActividad foto) {
		// TODO Auto-generated method stub
		
	}
	
	FotografiaListener fListener;

	@Override
	public void onFotografiaSeleccionada(Boolean haySeleccion) {
		// TODO Auto-generated method stub
		
		if (fFotos!=null){
			
			supportInvalidateOptionsMenu();
		}
	}
	
	@Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
	
	private void dismissProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

}
