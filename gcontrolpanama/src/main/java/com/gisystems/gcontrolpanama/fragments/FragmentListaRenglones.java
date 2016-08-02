package com.gisystems.gcontrolpanama.fragments;

import java.util.ArrayList;


import com.gisystems.gcontrolpanama.reglas.ActividadAgregarAvancesActivity;
import com.gisystems.gcontrolpanama.reglas.ConstruccionDetalleActivity;
//import ProyectoDetalleActivity;
//import ProyectoDetalleActivity.FiltrarAdaptador;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorRenglones;
import com.gisystems.gcontrolpanama.models.Actividad;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.Utilitarios;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentListaRenglones extends Fragment implements ConstruccionDetalleActivity.FiltrarAdaptador {

	public AdaptadorRenglones adaptadorActivs;
	private ListView mListView;
	private Context context;
	ProgressDialog pDialog;

	Construccion construccion;
	ConstruccionDetalleActivity pDetalle;
	Actividad actividad;

	@Override
	public View onCreateView(LayoutInflater inflater,
	        ViewGroup container, Bundle savedInstanceState) {				
	   return inflater.inflate(R.layout.fragment_listado_actividades, container, false);	  
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		  new TareaObtenerListadoActividades().execute(); 
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		context=getActivity();
		
		mListView= (ListView) getActivity().findViewById(R.id.fragment_listado_renglones_lista);
		
		
		//Invocar al metodo click de los items de la lista de renglones
		//para que permita ingresar el avance de cada renglón
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			     
				//Validar que el proyecto ya se haya instanciado
				if(construccion != null){
			    	 
			    	 actividad = adaptadorActivs.getItem(position);
			    	
			    	 //La cantidad pendiente de ejecutar es igual a la diferencia entre la cantidad contratada y
			    	 //la cantidad ejecutada


			    	  Double CantPendienteDeEjecutar = Utilitarios.round(
			 				(actividad.getCantidadContratada()-actividad.getCantidadEjecutada()),2);
			    	 
			    	 //Para poder ingresar avances se aplica la siguiente condición:
			    	 //La candidad pendiente de ejecutar debe ser mayor a 0 ó
			    	 //La cantidad contratada debe ser mayor a 0 ya que si la cantidad contratada es igual a 0
			    	 //se asume que al renglón solo se ingresarán fotografías
/*			    	 if((CantPendienteDeEjecutar>0)
			    			 || (actividad.getCantidadContratada()==0))
			    	 {*/
				    	  iniciarActividadIngresoAvances();
			    	/* }
			    	  else{
			    		 Toast.makeText(getActivity(), 
			    				 getActivity().getString(R.string.fragment_agregar_avances_renglon_5), Toast.LENGTH_LONG).show();
					}*/
			    	 
			     }
			}
		});
		
		Bundle args = getArguments();
		
		if (args != null) {
			construccion  = (Construccion) args.getSerializable("Construccion");
			adaptadorActivs=new AdaptadorRenglones(getActivity(),new ArrayList<Actividad>());
		}
	}

	private class TareaObtenerListadoActividades extends AsyncTask<Actividad, Void, ArrayList<Actividad>>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			
			pDialog = new ProgressDialog(context);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(getActivity().getApplicationContext().getString(R.string.cargando));
            pDialog.setCancelable(false);
			pDialog.show();
			
		}

		@Override
		protected ArrayList<Actividad> doInBackground(Actividad... param) {
			 ArrayList<Actividad> resultado = new ArrayList<Actividad>();
			try {

				resultado = Actividad.obtenerActivsPorClienteProyectoConstruccion(context,
						construccion.getIdCliente(), construccion.getIdProyecto(), construccion.getIdConstruccion());

			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
	        			FragmentListaRenglones.class.getSimpleName(), "tareaObtenerListadoActividades", 
	                    null, null);        
			}
			return resultado;
		}

		@Override
		protected void onPostExecute(ArrayList<Actividad> resultado){
			
			try {
				
				pDialog.dismiss();
				
				adaptadorActivs=new AdaptadorRenglones(getActivity(),resultado);
				mListView.setAdapter(adaptadorActivs);
	        	
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						FragmentListaRenglones.class.getSimpleName(), "onPostExecute", 
	                    null, null);   
			}
		}

	}

	@Override
	public void onFiltrarAdaptador(String txt) {
		// TODO Auto-generated method stub
		this.adaptadorActivs.getFilter().filter(txt);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		pDetalle=(ConstruccionDetalleActivity)activity;
		pDetalle.setFiltrarAdaptador(this);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		pDetalle=null;
		super.onDetach();
	}

    public void iniciarActividadIngresoAvances(){
    	Intent mainIntent = new Intent(getActivity(),ActividadAgregarAvancesActivity.class);

		mainIntent.putExtra("Actividad", actividad);
		Log.w("datos de actividades",String.valueOf(actividad));
		mainIntent.putExtra("Construccion", construccion);
		Log.w("datos de construcciones", String.valueOf(construccion));
    	getActivity().startActivity(mainIntent);
    	
    }

}
