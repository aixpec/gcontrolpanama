package com.gisystems.gcontrolpanama.reglas;

import java.util.ArrayList;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.fragments.FragmentAvancesProyecto;
import com.gisystems.gcontrolpanama.fragments.FragmentDetalleProyecto;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.CustomPagerAdapter;
import com.gisystems.utils.Fragmento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



public class ProyectoDetalleActivity extends ActionBarActivity  implements  SearchView.OnQueryTextListener {

	Context context;
	Proyecto proyecto;
	Fragment fragment;
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
				ProyectoDetalleActivity.class.getSimpleName(), "OnCreate", 
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
	        Proyecto proyecto = (Proyecto) extras.getSerializable("Proyecto");
	        
	        //Meterlo en un AsyncTask()
	       // proyecto=Proyecto.obtenerProyecto(context,p.getIdProyecto());
	        getSupportActionBar().setTitle(proyecto.getAliasProyecto());
	 
	        //Agregar los fragments
	        Bundle fragArgs= new Bundle();
	        fragArgs.putSerializable("Proyecto", proyecto);

	        //fragmentos.add(new Fragmento(FragmentListaRenglones.class.getName(), "Renglones", fragArgs));

		    fragmentos.add(new Fragmento(FragmentAvancesProyecto.class.getName(),"Avances",fragArgs));
			fragmentos.add(new Fragmento(FragmentDetalleProyecto.class.getName(),"Información General",fragArgs));
        }
        catch(Exception ex){
        	ManejoErrores.registrarError_MostrarDialogo(context, ex, 
    				ProyectoDetalleActivity.class.getSimpleName(), "EstablecerValores", 
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
		 menu.findItem(R.id.mnu_action_bitacora_fotos).setVisible(false);
         menu.findItem(R.id.mnu_action_activs).setVisible(false);
         menu.findItem(R.id.mnu_action_search).setVisible(false);
         menu.findItem(R.id.mnu_action_bitacora).setVisible(false);
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
         		ProyectoDetalleActivity.this.startActivity(mainIntent);
         		ProyectoDetalleActivity.this.finish();
            return true;
			case R.id.mnu_action_construcciones:
				mainIntent= new Intent(context,ConstruccionesListadoActivity.class);
				ProyectoDetalleActivity.this.startActivity(mainIntent);
				ProyectoDetalleActivity.this.finish();
				return true;
			default:
             return super.onOptionsItemSelected(item);
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
