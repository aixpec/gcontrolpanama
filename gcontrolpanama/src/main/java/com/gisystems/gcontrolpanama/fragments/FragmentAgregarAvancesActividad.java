package com.gisystems.gcontrolpanama.fragments;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.Actividad;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.Proyecto.TipoGestion;

import com.gisystems.utils.Utilitarios;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentAgregarAvancesActividad extends Fragment{

	private Actividad activ;
	private TextView tvEtiquetaAgregarAvances;
	private EditText etNuevoAvance;
	private double CantPendienteDeEjecutar;
	private double PorcPendienteDeEjecutar;
	private double CantEjecutada;
	private double PorcEjecutado;
	private double cantContratada;
	private double CantExcendete;
	private double PorcExcedente;
	private TextView desActividad;
	private boolean valido;
	private StringBuilder mensajeError;
	private double montoAvance;
	private TipoGestion tGestion;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
	        ViewGroup container, Bundle savedInstanceState) {		
	   return inflater.inflate(R.layout.fragment_agregar_avances_actividad, container, false);	  
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Bundle args = getArguments();
		
        if (args != null) {
        	
        	activ=(Actividad)args.get("Actividad");
        	
        	ObtenerVistas();
        	
        	//Verificar el tipo de gestion del proyecto: Por Cantidades o Por Porcentajes
        	tGestion = Proyecto.ObtenerTipoGestionProyecto(getActivity(),
        			activ.getIdCliente(),activ.getIdProyecto());

        	EstablecerVistas(tGestion);
        	       	
        }
	}

	private void ObtenerVistas(){
    	
    	etNuevoAvance = (EditText)getActivity().findViewById(R.id.et_fragAgregarAvancesRenglon_NuevoAvance);
    	
    	tvEtiquetaAgregarAvances = ((TextView) getActivity()
    			.findViewById(R.id.tvEtiquetaAgregarAvances));

		desActividad = ((TextView) getActivity()
				.findViewById(R.id.tv_fragAgregarAvancesRenglon_Actividad));
    	
	}
	
	private void EstablecerVistas(TipoGestion tGestion){

		//Obtener Monto Ejecutado y pendiente de ejecutar
		CantEjecutada=Utilitarios.round(activ.getCantidadEjecutada(),4);
	
		cantContratada=Utilitarios.round(activ.getCantidadContratada(),4);
		
		//CantPendienteDeEjecutar = Utilitarios.round((activ.getCantidadContratada()-activ.getCantidadEjecutada()),4);


		if ( activ.getCantidadEjecutada() > activ.getCantidadContratada()){
			CantPendienteDeEjecutar = 0;
			CantExcendete = Utilitarios.round((activ.getCantidadEjecutada()-activ.getCantidadContratada()),4);
			PorcExcedente = Utilitarios.round((CantExcendete / cantContratada * 100),2);
			((TextView) getActivity().findViewById(R.id.tv_fragAgregarAvancesRenglon_PorcCantidadExcedente)).setTextColor(getResources().getColorStateList(R.color.graphRed));
		}
		else{
			CantPendienteDeEjecutar = Utilitarios.round((activ.getCantidadContratada()-activ.getCantidadEjecutada()),4);
			((TextView) getActivity().findViewById(R.id.tv_fragAgregarAvancesRenglon_PorcCantidadExcedente)).setTextColor(getResources().getColorStateList(R.color.graphGreen));
		}

		
		if ( activ.getCantidadContratada() > 0){
				PorcEjecutado=Utilitarios.round((activ.getCantidadEjecutada()/cantContratada) * 100,2);
				PorcPendienteDeEjecutar = Utilitarios.round((CantPendienteDeEjecutar / cantContratada * 100),2);
			}
		else{
			PorcPendienteDeEjecutar	= 0.00;
			PorcPendienteDeEjecutar = 0.00;
		}

		((TextView) getActivity()
				.findViewById(R.id.tv_fragAgregarAvancesRenglon_Actividad))
				.setText(String.valueOf(cantContratada));

		((TextView) getActivity()
    			.findViewById(R.id.tv_fragAgregarAvancesRenglon_CantidadContratada))
    			.setText(String.valueOf(cantContratada));

    	((TextView) getActivity()
    			.findViewById(R.id.tv_fragAgregarAvancesRenglon_CantidadEjecutada))
    			.setText(String.valueOf(CantEjecutada));

    	((TextView) getActivity()
    			.findViewById(R.id.tv_fragAgregarAvancesRenglon_PorcCantidadEjecutada))
    			.setText( String.valueOf(PorcEjecutado) + "%");


    	tvEtiquetaAgregarAvances.setText(getActivity().getString(R.string.fragment_agregar_avances_renglon_4));

		desActividad.setText("[" + activ.getCodigoInstitucional() + "] - " + activ.getDescripcion());
    	
		if(tGestion == TipoGestion.Por_Cantidades){
			((TextView) getActivity()
        			.findViewById(R.id.et_fragAgregarAvancesEtiquetaPorc))
        			.setVisibility(0);
    	}
    	else{
    		
    		((TextView) getActivity()
        			.findViewById(R.id.et_fragAgregarAvancesEtiquetaPorc))
        			.setText("%");

    	}

		
		//Se deshabilitara el widget de ingreso del avance para cuando la cantidad
		//pendiente de ejecutar sea igual o menor a 0
		/*if (CantPendienteDeEjecutar>0){
        	((TextView) getActivity()
        			.findViewById(R.id.tv_fragAgregarAvancesRenglon_FechaActualizo))
        			.setText(getActivity().getResources().getString(R.string.fragment_agregar_avances_renglon_2)
        					+ " " + String.valueOf(activ.getFechaActualizacion()));
    	}
    	else
    	{
    		Toast.makeText(getActivity(), getActivity().getString(R.string.fragment_agregar_avances_renglon_5), Toast.LENGTH_LONG).show();
    		etNuevoAvance.setText("0.00");
    		etNuevoAvance.setEnabled(false);
    	}*/
    	
    	((TextView) getActivity()
    			.findViewById(R.id.tv_fragAgregarAvancesRenglon_CantidadPorEjecutar))
    			.setText(String.valueOf(Utilitarios.round(CantPendienteDeEjecutar, 4)));
    	((TextView) getActivity()
    			.findViewById(R.id.tv_fragAgregarAvancesRenglon_PorcCantidadPorEjecutar))
    			.setText(String.valueOf(Utilitarios.round(PorcPendienteDeEjecutar, 2)) + "%");

		((TextView) getActivity()
				.findViewById(R.id.tv_fragAgregarAvancesRenglon_CantidadExcedente))
				.setText(String.valueOf(Utilitarios.round(CantExcendete, 4)));

		((TextView) getActivity()
				.findViewById(R.id.tv_fragAgregarAvancesRenglon_PorcCantidadExcedente))
				.setText(String.valueOf(Utilitarios.round(PorcExcedente, 2)) + "%");

		validarAvance(String.valueOf(etNuevoAvance.getText()));
    	
    	etNuevoAvance.addTextChangedListener(new TextWatcher() {
    		
			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				validarAvance(s.toString());
									
			}
    	});
		
	}
	
	/***
	 * Proceso que valida el avance ingresado bajo las sigs. 
	 * Condiciones:
	 * 1. Si la cantidad contratada es igual a 0 se asumira que el avance ingresado es igual 0
	 *    y por lo tanto valido.
	 * 2. Validar que la entrada sea un número.
	 * 3. Validar que el avance ingresado no sea mayor que la cantidad pendiente de ejecutar.
	 * 4. Validar que el avance ingreasado sea mayor que 0.
	 * 5. Validar que la cantidad pendiente de ejecutar sea mayor a 0.
	 *    
	 * @param NuevoAvance = El avance ingresado
	 * 
	 */
	
	private void validarAvance(Object NuevoAvance){
		
		mensajeError = new StringBuilder();
		
		valido = false;
		
		//1. Si la cantidad contratada es igual a 0
		//   se asumira que el avance ingresado es igual 0 y por lo tanto valido
		valido=cantContratada==0;
		if(valido){
			montoAvance=0.00;
			valido=true;
			return;
		}
		
		//2. Validar que la entrada sea un número
		valido=Utilitarios.esNumero(NuevoAvance);
		
		if(!valido){
			mensajeError.append(getActivity().getString(R.string.fragment_agregar_avances_renglon_6));
			valido=false;
			return;
		}
		
		montoAvance=Double.parseDouble(String.valueOf(NuevoAvance));
		
		//3. Validar que el avance ingresado no sea mayor que la cantidad pendiente de ejecutar
		/*valido=(montoAvance>CantPendienteDeEjecutar);
		
		if (valido) {
			mensajeError.append(getActivity().getString(R.string.fragment_agregar_avances_renglon_7));
			valido=false;
			return;
		}*/
		
		
		//4. Validar que el avance ingresado sea menor que 0
		//valido = ( (montoAvance == 0) || (montoAvance < 0) ) ;
		valido = ( montoAvance < 0 ) ;
		if (valido){
			mensajeError.append(getActivity().getString(R.string.fragment_agregar_avances_renglon_8));
			valido = false;
			return;
		}

		
		//5. Validar que la cantidad pendiente de ejecutar sea mayor a 0 
		/*valido = ( (CantPendienteDeEjecutar==0)) ;

		if (valido){
			mensajeError.append(getActivity().getString(R.string.fragment_agregar_avances_renglon_5));
			valido = false;
			return;
		}*/
		
		valido = true;
	}
	
	public boolean isValido() {
		return valido;
	}

	public StringBuilder getMensajeError() {
		return mensajeError;
	}
	
	public Double getMontoAvance() {
		return montoAvance;
	}

	public void limpiar(){
		
		this.montoAvance = 0;
		
		this.etNuevoAvance.setText("");
		
	}

}

