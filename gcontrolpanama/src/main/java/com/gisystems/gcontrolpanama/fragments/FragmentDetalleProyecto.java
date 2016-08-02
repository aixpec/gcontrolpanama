package com.gisystems.gcontrolpanama.fragments;



import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.Proyecto;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentDetalleProyecto extends Fragment{

	Proyecto proyecto=new Proyecto();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
	        ViewGroup container, Bundle savedInstanceState) {		
	   return inflater.inflate(R.layout.fragment_detalle_proyecto, container, false);	  
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Bundle args = getArguments();
		
        if (args != null) {
        	proyecto = (Proyecto) args.getSerializable("Proyecto");
            ((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_Estado)).setText(proyecto.getEdoProyectoDesc());
            ((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_Descripcion)).setText("Componente: " + proyecto.getNombreProyecto() );
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_Tipo)).setText(proyecto.getTipoProyectoDesc());
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_TipoGestion)).setText(proyecto.getTipoGestion().name());
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_FechaInicio)).setText(proyecto.getFechaInicia());
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_FechaFinaliza)).setText(proyecto.getFechaFinaliza());
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_MontoOriginal)).setText(proyecto.getMoneda() + " " + proyecto.getMontoOriginal().toString());
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_MontoModificado)).setText(proyecto.getMoneda() + " " + proyecto.getMontoModificado().toString());
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_MontoPagado)).setText(proyecto.getMoneda() + " " + proyecto.getMontoPagado().toString());
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_Altitud)).setText(proyecto.getAltitud().toString());
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_Latitud)).setText(proyecto.getLatitud().toString());
        	((TextView) getActivity().findViewById(R.id.tv_fragmentDetProyecto_Longitud)).setText(proyecto.getLongitud().toString());
        }
        
	}
	
	

	
	
	
}
