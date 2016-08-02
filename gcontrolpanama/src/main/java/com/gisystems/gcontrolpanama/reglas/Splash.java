package com.gisystems.gcontrolpanama.reglas;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
public class Splash  extends Activity{

	private Context ctx;
	ProgressDialog pDialog;
	
	@Override
	public void onCreate(Bundle icicle)
	{
		
		super.onCreate(icicle);
		setContentView(R.layout.activity_splash);
   
        Thread.setDefaultUncaughtExceptionHandler(new ManejoErrores(this));
        ctx=this;
        
        try {
        	
	        
	        if(AppValues.SharedPref_obtenerValorEsPrimeraEjecucion(this))
	        {
	        	
	        	Log.w(Splash.class.getSimpleName() + "_OnCreate","IsFirstRun");
	        	
	        	pDialog = new ProgressDialog(ctx);
	            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            pDialog.setMessage("Cargando...");
	            pDialog.setCancelable(false);
	            
	            //Al iniciar la aplicacion, se estableceran algunos valores por defecto
	            TareaIniciarValoresApps tIniciarValoresApps = new TareaIniciarValoresApps();
	            tIniciarValoresApps.execute();
	            
	        }
	        else 
	        {
	        	IrAActividad();
	        }      
	      
	         
	        
    	} catch (Exception e) {
    		ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
    				Splash.class.getSimpleName(), "onCreate", 
                    null, null);
		}   	       
		
	}	
	
	private boolean InicializarValoresPorDefecto()
    {
		boolean inicioExitoso=false;
    	try {
	    	AppValues.SharedPref_guardarValorPrimeraEjecucion(ctx, false);
			inicioExitoso=AppValues.crearAppsValues(this);
    	} catch (Exception e) {
    		ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
    				Splash.class.getSimpleName(), "InicializarValoresPorDefecto", 
                    null, null);
    		inicioExitoso=false;
		} 
    	return inicioExitoso;
    }
	
	
	private class TareaIniciarValoresApps extends AsyncTask<Void, Integer, Boolean> {
		 
        @Override
        protected Boolean doInBackground(Void... params) {        
        	boolean res= InicializarValoresPorDefecto();
        	return res;
        }
 
        @Override
        protected void onPreExecute() {
            pDialog.show();
        }
 
        @Override
        protected void onPostExecute(Boolean result) {
        	
        	pDialog.dismiss();
            if(result)
            {
            	IrAActividad();
            }
            else
            {
     			Toast.makeText(ctx, "La aplicación no ha podido ser iniciada.",
                        Toast.LENGTH_SHORT).show();
     			Splash.this.finish();
            }
            
        } 
    }
	
	public void IrAActividad()
	{
		 if( (AppValues.SharedPref_obtenerValorEsUsuarioValido(ctx) && (AppValues.AppEstaSincronizada(this)))
					|| (AppValues.obtenerAppValueModoDesconectado(ctx) ) ) 
		 {
			Intent mainIntent = new Intent(Splash.this,ActividadMainMenu.class);
  			Splash.this.startActivity(mainIntent);
  			Splash.this.finish();
		 }
		 else
		 {
			 Intent mainIntent = new Intent(Splash.this,LoginActivity.class);
  			 Splash.this.startActivity(mainIntent);
  			 Splash.this.finish();
		 }

	}
	



	
}
