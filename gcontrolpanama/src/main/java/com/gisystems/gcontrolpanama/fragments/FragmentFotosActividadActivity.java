package com.gisystems.gcontrolpanama.fragments;

import java.util.ArrayList;


import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.adaptadores.AdaptadorFotosActividad;
import com.gisystems.gcontrolpanama.models.Actividad;
import com.gisystems.gcontrolpanama.models.FotoActividad;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
 
public class FragmentFotosActividadActivity extends Fragment {
 
	Context context; 
	private ProgressDialog pDialog;
	public AdaptadorFotosActividad adaptadorFotosActividad;
	GridView gvFotos;
	FotoActividad foto;
	Actividad actividad;
	private boolean valido;
	private StringBuilder mensajeError;
	private TareaObtenerFotosActividad tObtenerFotos;
	private ArrayList<FotoActividad> fotos;
	private boolean todasSeleccionadas=false;
	public ArrayList<FotoActividad> getFotos() {
		return fotos;
	}
   
	@Override
	public View onCreateView(LayoutInflater inflater,
	        ViewGroup container, Bundle savedInstanceState) {				
	   return inflater.inflate(R.layout.fragment_grid_fotos_actividad, container, false);	  
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		context=getActivity();
		establecerValores();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tObtenerFotos=new TareaObtenerFotosActividad();
		tObtenerFotos.execute();
	}

	private void establecerValores(){
		
		gvFotos= (GridView) getActivity().findViewById(R.id.gridFotosActividad);
		//gvFotos.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
		
		gvFotos.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View v,
	                int position, long id) {
	        	
	        	adaptadorFotosActividad.getItem(position).setEstadoSeleccion(
	        			!adaptadorFotosActividad.getItem(position).isEstadoSeleccion());
	        	
	        	adaptadorFotosActividad.notifyDataSetChanged();	
	        	
	        	if (listener!=null) {
                    listener.onFotografiaSeleccionada(adaptadorFotosActividad.ObtenerTotalFotosSeleccionadas()>0);
                }
	        	
	        }
	    });
		 
		Bundle args = getArguments();
		
        if (args != null) {
        	actividad=(Actividad)args.get("Actividad");
			adaptadorFotosActividad =  new AdaptadorFotosActividad(getActivity(),new ArrayList<FotoActividad>());			
        }
		
	}
	
	private class TareaObtenerFotosActividad extends AsyncTask<Void, Void, ArrayList<FotoActividad>>{
		
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
		protected ArrayList<FotoActividad> doInBackground(Void... param) {
			fotos = new ArrayList<FotoActividad>();
			try {
				
				fotos = FotoActividad.obtenerFotosSinGuardarActividadConAvance(context,
						actividad.getIdCliente(), actividad.getIdProyecto(), actividad.getIdConstruccion(), actividad.getIdActividad(), true);
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
	        			FragmentFotosActividadActivity.class.getSimpleName(), "onPreExecute", 
	                    null, null);        
			}
			return fotos;
		}

		@Override
		protected void onPostExecute(ArrayList<FotoActividad> resultado){
			try {
				
				pDialog.dismiss();
				
				if( fotos !=null){
					fotos=resultado;
					adaptadorFotosActividad.ActualizarListado(resultado);
						
					gvFotos.setAdapter(adaptadorFotosActividad);
					valido=validarFotografias();
					Log.w("validarFotografia", String.valueOf(valido));
				}
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						FragmentFotosActividadActivity.class.getSimpleName(), "onPostExecute", 
	                    null, null);   
			}
		}
	}
	
	private boolean validarFotografias(){
		
		boolean v=false;
		mensajeError=new StringBuilder();
		
		v=(adaptadorFotosActividad.getCount()==0);
		if (v) {
			mensajeError.append("Debe agregar al menos una fotografía.");
			return false;
		}
		
	    return true;
	}
	
	public boolean isValido() {
		return valido;
	}

	public StringBuilder getMensajeError() {
		return mensajeError;
	}

	public void ejecutarTareaObtenerFotos() {
		tObtenerFotos.execute();
	}
	
	private FotografiaListener listener;
	
	public interface FotografiaListener {
		void onFotografiaSeleccionada(FotoActividad foto);
		void onFotografiaSeleccionada(Boolean haySeleccion);
	}
	
	public void setFotografiaListener(FotografiaListener listener) {
        this.listener=listener;
    }
	
	public void EliminarFotosSeleccionadas(){
		if(adaptadorFotosActividad.ObtenerTotalFotosSeleccionadas()>0)
			new TareaEliminarFotosSeleccionadas().execute();
		else
			Toast.makeText(context, "No existen fotografías seleccionadas por eliminar.", 
					Toast.LENGTH_LONG).show();		
	} 
	
	private class TareaEliminarFotosSeleccionadas extends AsyncTask<Void, FotoActividad, Integer>{

		private int totalFotosSel;
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			
			totalFotosSel=adaptadorFotosActividad.ObtenerTotalFotosSeleccionadas();
			Log.w("totalFotosSel",String.valueOf(totalFotosSel));
			pDialog = new ProgressDialog(context);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
          // pDialog.setMessage(context.getApplicationContext().getString(R.string.cargando));
            pDialog.setMessage("Eliminando registros");
            pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... param) {
			
			int cantFotosEliminadas=0;
			
			try {
				FotoActividad foto=null;
				Log.w("adaptadorFotosActividad.getCount()",String.valueOf(adaptadorFotosActividad.getCount()));
				
				if(todasSeleccionadas){
					foto=adaptadorFotosActividad.getItem(0);
					FotoActividad.eliminarTodasFotoAvanceSinGuardar(context, 
							foto.getIdCliente(), foto.getIdProyecto(), 
							foto.getIdConstruccion(), foto.getIdActividad());
				}
				else
				{
					for(int index=0; index<adaptadorFotosActividad.getCount(); index++){
						foto=adaptadorFotosActividad.getItem(index);
						Log.w("index",String.valueOf(index));
						if(foto.isEstadoSeleccion())
						{
							Log.w("(foto.isEstadoSeleccion()",String.valueOf((foto.isEstadoSeleccion())));
							cantFotosEliminadas+=FotoActividad.eliminarFotoAvanceSinGuardar(context, 
									foto.getIdCliente(), foto.getIdProyecto(), foto.getIdConstruccion(),
									foto.getIdActividad(), foto.getIdFoto())
									?1:0;
							
							publishProgress(foto);
						}
					}
				}
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
	        			FragmentFotosActividadActivity.class.getSimpleName(), "TareaEliminarFotosSeleccionadas_onPreExecute", 
	                    null, null);        
			}
			
			return cantFotosEliminadas;
		}

		@Override 
		protected void onProgressUpdate(FotoActividad... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			try {
				FotoActividad f = values[0];
				adaptadorFotosActividad.remove(f);
				Log.w("adaptadorFotosActividad.remove(f)","adaptadorFotosActividad.remove(f)");
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
	        			FragmentFotosActividadActivity.class.getSimpleName(), "TareaEliminarFotosSeleccionadas_onProgressUpdate", 
	                    null, null);        
			}
			
		}
		
		//HABÍAN 8 REGISTROS
		@Override
		protected void onPostExecute(Integer resultado){
			try {

				pDialog.dismiss();
				
				if(todasSeleccionadas){
					adaptadorFotosActividad.clear();
				}
				else
				{
					Toast.makeText(context, "Han sido eliminados " 
							+ String.valueOf(resultado) + " registros", Toast.LENGTH_SHORT).show();
					
				}
				
				todasSeleccionadas=false;
				adaptadorFotosActividad.notifyDataSetChanged();
				
				if (listener!=null) {
                    listener.onFotografiaSeleccionada(adaptadorFotosActividad.ObtenerTotalFotosSeleccionadas()>0);
                }
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(context, e, 
						FragmentFotosActividadActivity.class.getSimpleName(), "TareaEliminarFotosSeleccionadas_onPostExecute", 
	                    null, null);   
			}
		}
	}

	
	
	
	public void establecerSeleccionTodasLasFotografias( ){
		todasSeleccionadas=!todasSeleccionadas;
		for(int index=0; index<adaptadorFotosActividad.getCount();index++){
			adaptadorFotosActividad.getItem(index).setEstadoSeleccion(todasSeleccionadas);
		}
    	adaptadorFotosActividad.notifyDataSetChanged();	
    	if (listener!=null) {
            listener.onFotografiaSeleccionada(adaptadorFotosActividad.ObtenerTotalFotosSeleccionadas()>0);
        }
	}
	

}
