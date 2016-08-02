package com.gisystems.gcontrolpanama.fragments;


import java.util.ArrayList;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

public class FragmentAvancesProyecto extends Fragment{

	private String TAG=FragmentAvancesProyecto.class.getSimpleName();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
	        ViewGroup container, Bundle savedInstanceState) {
		
	   return inflater.inflate(R.layout.fragment_avances_proyecto, container, false);
	  
	}

	@Override
	public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        
        Bundle args = getArguments();
		
        if (args != null) {
        	try{
	        	Proyecto proyecto = (Proyecto) args.getSerializable("Proyecto");
	         
	        	//Fisico
	        	Double aFisico=(proyecto.getAvanceFisico())!=null
		    			? proyecto.getAvanceFisico()
						:0;
		        PieChart mChart=new PieChart( getActivity().getApplicationContext());
		    	mChart=(PieChart) getActivity().findViewById(R.id.frgmnt_avancesProyecto_pieChartFisico);
		    	mChart.setHoleRadius(30f); 
		    	mChart.setTransparentCircleRadius(35f);
		    	//mChart.getLegend().setEnabled(false);
		    	mChart.setDescription("Físico");
		    	mChart.setPivotX((float)0);
		    	mChart.setPivotY((float)0);
		    	mChart.setData(generatePieData(Float.valueOf(String.valueOf(aFisico*100)))) ;
		    	mChart.setDescriptionTextSize(12f);
		    	
		    	//Financiero
	        	Double aFinanciero=(proyecto.getAvanceFinanciero()!=null)
		    			?proyecto.getAvanceFinanciero()
						:0;
		    	mChart=(PieChart) getActivity().findViewById(R.id.frgmnt_avancesProyecto_pieChartFinanciero);
		    	mChart.setHoleRadius(30f); 
		    	mChart.setTransparentCircleRadius(35f);
		    	//mChart.getLegend().setEnabled(false);
		    	mChart.setDescription("Financiero");
		    	mChart.setPivotX((float)0);
		    	mChart.setPivotY((float)0);
		    	mChart.setData(generatePieData(Float.valueOf(String.valueOf((aFinanciero*100)))));
		    	mChart.setDescriptionTextSize(12f);
		    	
		    	//Tiempo
		    	float porcTiempoEjecutado=0;
		    	float totalDias = proyecto.getTotalDiasDuraciónProyecto();
		    	float diasEjecutados =proyecto.getTotalDiasEjecutadosALaFecha();
		    	
		    	if(diasEjecutados<0) diasEjecutados=0;
		    	
		    	porcTiempoEjecutado = (diasEjecutados/totalDias) * 100;
		    	
		    	//Log.w(TAG, "Dias de diferencia"+ String.valueOf(format.parse(proyecto.getFechaFinaliza())));
		    	//Log.w(TAG, "Dias de diferencia"+ String.valueOf(TimeUnit.MILLISECONDS.toDays(diff)));
		        
		    	mChart=(PieChart) getActivity().findViewById(R.id.frgmnt_avancesProyecto_pieChartTiempo);
		    	mChart.setHoleRadius(30f); 
		    	mChart.setTransparentCircleRadius(35f);
		    	//mChart.getLegend().setEnabled(false);
		    	mChart.setDescription("En tiempo");
		    	mChart.setPivotX((float)0);
		    	mChart.setPivotY((float)0);
		    	mChart.setData(generatePieData(porcTiempoEjecutado));
		    	mChart.setDescriptionTextSize(12f);
		    	
		        }
		        catch(Exception e){
		        	ManejoErrores.registrarError(getActivity(), e,
		        			TAG, "onActivityCreated",
							   null, null);
		        }
        }
	        
        
    }
	 	 
	/**
     * 
     * @return
     */ 
    private PieData generatePieData(float mEjecutado) {
        
        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        
        xVals.add("Ejecutado"); 
        xVals.add("Por ejecutar");
        //double ejecutado=(Math.random() * 100) ;
        float pendiente=( 100-mEjecutado ) ;
        //xVals.add("" + 1);        
        entries1.add(new Entry( mEjecutado, 1));
        //xVals.add("" + 2);  
        entries1.add(new Entry((float) pendiente, 2));
        
        PieDataSet ds1 = new PieDataSet(entries1, "");
        int[] colors = {getActivity().getApplicationContext().getResources().getColor(R.color.graphLightBlue),
        		getActivity().getApplicationContext().getResources().getColor(R.color.graphRed)};
        
        ds1.setColors(colors);
        ds1.setSliceSpace(0f);
        ds1.setValueTextColor(Color.BLACK);
        ds1.setValueTextSize(10f);
        
        PieData d = new PieData(xVals, ds1);
        
        return d;
        
    }
	
}
