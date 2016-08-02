package com.gisystems.gcontrolpanama.reglas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.fragments.FragmentListaRenglones;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.models.FotoActividad;
import com.gisystems.utils.CustomPagerAdapter;
import com.gisystems.utils.Fragmento;
import com.gisystems.utils.Utilitarios;

import java.util.ArrayList;


public class ConstruccionDetalleActivity extends ActionBarActivity  implements  SearchView.OnQueryTextListener {

	Context context;
	Construccion construccion;
	Fragment fragment;
	private ProgressDialog pDialog;
	private EnvioDatosAPI envioDatos;
	CustomPagerAdapter mCustomPagerAdapter;
	ViewPager mViewPager;
    ArrayList<Fragmento> fragmentos=new ArrayList<Fragmento>();
    private SearchView mSearchView;
    
	public interface FiltrarAdaptador {
		public void onFiltrarAdaptador(String txt);
	}
	
	private FiltrarAdaptador filtrarAdaptador = null;

	@Override
	    protected void onCreate(Bundle savedState) {
	        super.onCreate(savedState);
	        setContentView(R.layout.activity_info_proyecto);
	        
	        context=this;
	    
	        try{
	        	
	        	EstablecerValores();
	        	 
	        	mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), context);	  	       
	  	        mViewPager = (ViewPager) findViewById(R.id.pager);
	  	        
	  	        mCustomPagerAdapter.setFragmentos(fragmentos);
	  	        mViewPager.setAdapter(mCustomPagerAdapter);
	  	        	  	     
	  	        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() 
		  	    {
		  	        @Override
		  	        public void onPageSelected(int position) 
		  	        {
		  	            //Clear action bar menu items. 
		  	            //This prompts the onPrepareOptionsMenu which recreates the menu with
		  	            //with the appropriate options for a given fragment as defined by you 
		  	            supportInvalidateOptionsMenu();
		  	           Log.w("onPageSelected",String.valueOf(position));
		  	        }
	
		  	        @Override
		  	        public void onPageScrollStateChanged (int arg0)
		  	        {
		  	            //Clear action bar menu items and display correct ones
		  	            //This invalidates the menu options as soon as the swiping of pages begins
		  	            //supportInvalidateOptionsMenu();
		  	            Log.w("onPageScrollStateChanged",String.valueOf(arg0));
		  	        }
		  	    });
	  	      
	        }
	        catch (Exception ex){
	        	ManejoErrores.registrarError_MostrarDialogo(context, ex, 
				ConstruccionDetalleActivity.class.getSimpleName(), "OnCreate",
                null, null);
	        }   
	 }
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		int pageNum = mViewPager.getCurrentItem();
		//Log.w("onPrepareOptionsMenu",String.valueOf(pageNum));
	    if(pageNum==0 ){      
	    
	    	 menu.findItem(R.id.mnu_action_search).setVisible(true);

	    }else{       
	    	
	    	menu.findItem(R.id.mnu_action_search).setVisible(false);
	    }
	    
	    return super.onPrepareOptionsMenu(menu);
	}




	
	 private boolean EstablecerValores()
	 {
		Bundle extras = getIntent().getExtras();
        if (extras == null) {
          return false;
        }
	        
        
        try{
	        Construccion construccion = (Construccion) extras.getSerializable("Construccion");

	        
	        //Meterlo en un AsyncTask()
	       // proyecto=Proyecto.obtenerProyecto(context,p.getIdProyecto());
	        getSupportActionBar().setTitle(construccion.getDescripcion());
	 
	        //Agregar los fragments
	        Bundle fragArgs= new Bundle();
	        fragArgs.putSerializable("Construccion", construccion);


	        fragmentos.add(new Fragmento(FragmentListaRenglones.class.getName(), "Sub Actividades", fragArgs));
	        //fragmentos.add(new Fragmento(FragmentDetalleProyecto.class.getName(),"Información General",fragArgs));
		    //fragmentos.add(new Fragmento(FragmentAvancesProyecto.class.getName(),"Avances",fragArgs));
		   
        }
        catch(Exception ex){
        	ManejoErrores.registrarError_MostrarDialogo(context, ex, 
    				ConstruccionDetalleActivity.class.getSimpleName(), "EstablecerValores",
                    null, null);
        	return false;
        }
        return true;
	 }
	

     
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
    	 
         // Inflate the menu; this adds items to the action bar if it is present.    	
         //Crear un nuevo search view	
         mSearchView = new SearchView(getSupportActionBar().getThemedContext());
         mSearchView.setQueryHint(getResources().getString(R.string.buscar));
         mSearchView.setOnQueryTextListener(this);
            
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.main, menu);
         menu.findItem(R.id.mnu_action_search).setActionView(mSearchView); 
         
         menu.findItem(R.id.mnu_action_actualizar).setVisible(false);
         menu.findItem(R.id.mnu_action_activs).setVisible(false);
         menu.findItem(R.id.mnu_action_search).setVisible(false);
         menu.findItem(R.id.mnu_action_salir).setVisible(false);
         menu.findItem(R.id.mnu_action_acercaDe).setVisible(false);
		 menu.findItem(R.id.mnu_action_construcciones).setVisible(false);
  
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
			 	mainIntent= new Intent(context,ProyectosListadoActivity.class);
         		ConstruccionDetalleActivity.this.startActivity(mainIntent);
         		ConstruccionDetalleActivity.this.finish();
            return true;
			case R.id.mnu_action_construcciones:
				mainIntent= new Intent(context,ConstruccionesListadoActivity.class);
				ConstruccionDetalleActivity.this.startActivity(mainIntent);
				ConstruccionDetalleActivity.this.finish();
				return true;
            case R.id.mnu_action_bitacora:
                mainIntent = new Intent(context,BitacoraActivity.class);
                ConstruccionDetalleActivity.this.startActivity(mainIntent);
                return true;
            case R.id.mnu_action_bitacora_fotos:

				new AlertDialog.Builder(this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(getResources().getString(R.string.ProyectosListadoActivity_40))
						.setMessage(getResources().getString(R.string.ProyectosListadoActivity_50))
						.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								try{
									if(Utilitarios.isConnectionAvailable(context)) {
										new enviar_Fotografias().execute();
									}
									else
									{
										Toast.makeText(context, R.string.verificarConexionDeInternet, Toast.LENGTH_SHORT).show();
									}
								}
								catch (Exception e){
									ManejoErrores.registrarError_MostrarDialogo(context, e,
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

				return true;


			default:
             return super.onOptionsItemSelected(item);
     }
     }


	private class enviar_Fotografias extends AsyncTask<Void, Integer, Boolean> {

		private int CantFotos=0;

		private ArrayList<FotoActividad> FotografiasDeActividad;
		private String msj;
		private int EnviosSatisfactorios=0;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog = new ProgressDialog(context);

			pDialog = new ProgressDialog(context);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage(context.getApplicationContext().getString(R.string.enviando_al_servidor));
			pDialog.setCancelable(false);
			pDialog.show();

			FotografiasDeActividad= FotoActividad.obtenerFotosSinEnviarSinAvance(context);
			CantFotos=FotografiasDeActividad.size();


		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			boolean resultado=false;

			try {


				envioDatos=new EnvioDatosAPI(context);
				for(int index=0;index<CantFotos;index++){
					EnviosSatisfactorios+=1;
					publishProgress(index+1);

					resultado=envioDatos.Enviar_FotografiaSinAvance(FotografiasDeActividad.get(index));
					if (resultado)
						FotoActividad.actualizarEdoFotografia(context,
								FotografiasDeActividad.get(index), AppValues.EstadosEnvio.Enviado);


				}


			}
			catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e,
						FotoNuevaActivity.class.getSimpleName(), "enviar_Fotografias_doInBackground",
						null, null);
			}

			return resultado;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int progreso = values[0].intValue();

			msj=String.format(String.format(getResources().getString(R.string.class_AgregarAvance_10),
					new Object[]{ null }) , progreso,CantFotos);
			pDialog.setMessage(msj);


		}

		@Override
		protected void onPostExecute(Boolean resultado){
			try {

				pDialog.dismiss();
				Toast.makeText(context, "Han sido enviadas " + String.valueOf(EnviosSatisfactorios)
						+ " de " + String.valueOf(CantFotos) + " fotos pendientes.", Toast.LENGTH_SHORT).show();



			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e,
						FotoNuevaActivity.class.getSimpleName(), "enviar_Fotografias_onPostExecute",
						null, null);
			}
		}

	}


	@Override
	public boolean onQueryTextChange(String txt) {
		// TODO Auto-generated method stub
		if (filtrarAdaptador!=null){  filtrarAdaptador.onFiltrarAdaptador(txt); }
		return false;
	}


	@Override
	public boolean onQueryTextSubmit(String txt) {
		// TODO Auto-generated method stub
		if (filtrarAdaptador!=null){  filtrarAdaptador.onFiltrarAdaptador(txt); }
		return false;
	}
	
	
	public void setFiltrarAdaptador(FiltrarAdaptador listener) {
        this.filtrarAdaptador=listener;
    }
 
}
