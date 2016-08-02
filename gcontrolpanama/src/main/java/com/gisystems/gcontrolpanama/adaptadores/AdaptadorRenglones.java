package com.gisystems.gcontrolpanama.adaptadores;

import java.util.ArrayList;
import java.util.Locale;


import com.gisystems.gcontrolpanama.reglas.FotoNuevaActivity;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.Actividad;

import com.gisystems.utils.Utilitarios;
import com.gisystems.utils.Utilitarios.TipoFoto;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorRenglones extends ArrayAdapter<Actividad> {

	private Activity context;
	private final ArrayList<Actividad> arrayOriginalActivs;
	private ArrayList<Actividad> arrayModificadoActivs;
	private double PorcentajeEjecutado;
	
	static class ViewHolder {
	    public TextView nombre;
	    public TextView alias;
		public TextView estado;
	    public HorizontalBarChart mChart;	 
	    public ImageView foto;
		public ImageView ivfoto;
	 }
	
	
	public AdaptadorRenglones(Activity Context, ArrayList<Actividad> Activs){
		super(Context, R.layout.list_item_actividades, Activs);
		this.context=Context;
		this.arrayModificadoActivs=Activs;
		this.arrayOriginalActivs = new ArrayList<Actividad>();
		this.arrayOriginalActivs.addAll(Activs);
	}
	
	@SuppressLint("InflateParams") @Override
	  public View getView(final int position, final View convertView, ViewGroup parent) {
	      
		View rowView = convertView;
	    // reuse views
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.list_item_actividades, null);
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      
	      viewHolder.nombre = (TextView) rowView.findViewById(R.id.list_item_activs_LblTitulo);
	      
	      viewHolder.alias = (TextView) rowView.findViewById(R.id.list_item_activs_LblSubTitulo);

		  viewHolder.estado = (TextView) rowView.findViewById(R.id.list_item_activs_estado);

	      viewHolder.foto = (ImageView) rowView.findViewById(R.id.list_item_activs_ivfoto);
	      viewHolder.foto.setVisibility(View.VISIBLE);
		  viewHolder.ivfoto = (ImageView) rowView.findViewById(R.id.list_item_activs_estadofoto);
		  viewHolder.ivfoto.setVisibility(View.INVISIBLE);
	      
	      viewHolder.mChart=(HorizontalBarChart) rowView.findViewById(R.id.list_item_activs_hBarChartAvances);
	      viewHolder.mChart.setDrawBarShadow(false);
	      viewHolder.mChart.setDrawValueAboveBar(true);
	      viewHolder.mChart.setDescription("");
	      viewHolder.mChart.setDrawGridBackground(false);
	      viewHolder.mChart.setPinchZoom(false);

	        XAxis xl = viewHolder.mChart.getXAxis();
	        xl.setPosition(XAxisPosition.BOTTOM);
	        xl.setDrawAxisLine(true);
	        xl.setDrawGridLines(true);
	        xl.setGridLineWidth(0.1f);

	        YAxis yl = viewHolder.mChart.getAxisLeft();
	        yl.setDrawAxisLine(true);
	        yl.setDrawGridLines(true);
	        yl.setGridLineWidth(0.1f);
	        //yl.mAxisRange();

	        YAxis yr = viewHolder.mChart.getAxisRight();
	        yr.setDrawAxisLine(true);
	        yr.setDrawGridLines(false);      
	        rowView.setTag(viewHolder);
	        
	        viewHolder.mChart.setDoubleTapToZoomEnabled(false);
	        viewHolder.mChart.setScaleEnabled(false);

	        
	    }   

	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    final Actividad activ = arrayModificadoActivs.get(position);

	    holder.nombre.setText(activ.getCodigoInstitucional());

		if (activ.getDescripcion().length() > 100){
			holder.alias.setText(activ.getDescripcion().substring(0,100)+ "...");
		}
		else
		{
			holder.alias.setText(activ.getDescripcion());
		}

		//holder.alias.setText("[" + activ.getCodigoInstitucional() +  "] " +   activ.getDescripcion());


	 /*	if(activ.getCantidadContratada() - activ.getCantidadEjecutada()>0){
			holder.estado.setText(" En Proceso ");
			holder.ivfoto.setVisibility(View.VISIBLE);
			holder.ivfoto.setImageBitmap(BitmapFactory.decodeResource(context.getApplicationContext().getResources(), R.drawable.gray_button_75));

		}
		else
		{
			holder.estado.setText(" Terminada ");
			holder.ivfoto.setVisibility(View.INVISIBLE);
		}*/


		if(activ.getCantidadContratada()>0){
	    	PorcentajeEjecutado=Utilitarios.round(
	    			((activ.getCantidadEjecutada()/activ.getCantidadContratada())*100),2);
			holder.estado.setText(String.valueOf("Avance  " + PorcentajeEjecutado  + "%"));
	    }
	    else
	    {
	    	PorcentajeEjecutado=0.0;
			holder.estado.setText(String.valueOf("Avance  " + PorcentajeEjecutado  + "%"));
	    }

	    
	    holder.foto.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_camera));
	    holder.foto.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	IniciarNuevaFoto(activ);
	         }
	     });
	    
	    holder.mChart.setData(setData( (float) PorcentajeEjecutado));
	    //holder.mChart.animateY(2500);
	    return rowView;

	 }
	
	@Override
  	public Filter getFilter() {
		return myFilter;
	}
	
	private Filter myFilter = new Filter() {
		  
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
         Locale.setDefault(new Locale("es_GT"));
         FilterResults filterResults = new FilterResults();   
         ArrayList<Actividad> tempList=new ArrayList<Actividad>();
      
         if(constraint != null && arrayModificadoActivs!=null && constraint.toString().length() > 0) {
             int length=arrayOriginalActivs.size();
             int i=0;
             while(i<length){
            	String nombre =arrayOriginalActivs.get(i).getDescripcion().toLowerCase(Locale.getDefault());
            
            	if(nombre.contains(constraint.toString().toLowerCase(Locale.getDefault())))
            		tempList.add(arrayOriginalActivs.get(i));
            	
                i++;
             }

             filterResults.values = tempList;
             filterResults.count = tempList.size();   
                
          }
         else{
        	 synchronized(this)
             {
        		 filterResults.values = arrayOriginalActivs;
        		 filterResults.count = arrayOriginalActivs.size();
             }
         }

         return filterResults;
      }
        
    @SuppressWarnings("unchecked")
	@Override
      protected void publishResults(CharSequence contraint, FilterResults results) {
       
    	 arrayModificadoActivs = (ArrayList<Actividad>) results.values;
	     notifyDataSetChanged();
	     clear();
	     int l=arrayModificadoActivs.size();
	     for(int i = 0; i < l; i++)
	      add(arrayModificadoActivs.get(i));
	      notifyDataSetInvalidated();
         
      }
     };
     
     
     private BarData setData(float  Ejecutado) {

         ArrayList<String> xVals = new ArrayList<String>();
                
         xVals.add("Ejecutado %");
         xVals.add("Por ejecutar %");
         //xVals.add("");
         ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
         yVals1.add(new BarEntry(Ejecutado, 0));
         yVals1.add(new BarEntry(100-Ejecutado, 1));
         //yVals1.add(new BarEntry(100,2));
        
         BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
         set1.setBarSpacePercent(40f);
         
         ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
         dataSets.add(set1);
                  
         int[] colors = {context.getResources().getColor(R.color.graphCyan200),
        		 context.getResources().getColor(R.color.graphGrey400)};
         set1.setColors(colors);
         
         BarData data = new BarData(xVals, dataSets);
         
         data.setValueTextSize(10f);
        // data.setValueTypeface(tf);

         return data;
     }
     
     private void IniciarNuevaFoto(Actividad actividad){
 		Intent mainIntent = new Intent(context,FotoNuevaActivity.class);
 		mainIntent.putExtra("TipoFoto", TipoFoto.ActividadSinAvance);
 		mainIntent.putExtra("Actividad", actividad);
 		context.startActivity(mainIntent);
 	}
	
}
