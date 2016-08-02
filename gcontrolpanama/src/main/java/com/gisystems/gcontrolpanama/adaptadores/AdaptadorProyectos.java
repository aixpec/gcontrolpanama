package com.gisystems.gcontrolpanama.adaptadores;


import java.util.ArrayList;
import java.util.Locale;

import com.gisystems.gcontrolpanama.reglas.FotoNuevaActivity;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.reglas.ProyectoDetalleActivity;
import com.gisystems.utils.Utilitarios;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View.OnClickListener;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

public class AdaptadorProyectos  extends ArrayAdapter<Proyecto>  implements OnClickListener{

	private Activity context;
	private final ArrayList<Proyecto> arrayOriginalProyectos;
	private ArrayList<Proyecto> arrayModificadoProyectos;
	static class ViewHolder {
	    public TextView nombre;
	    public TextView alias;
	    public ImageView foto;
		public ImageView detalle;
		public ImageView avance;
	    public PieChart mChart;
	 
	 }
	
	public AdaptadorProyectos(Activity Context, ArrayList<Proyecto> Proyectos){
		super(Context, R.layout.list_item_generico, Proyectos);
		this.context=Context;
		
		this.arrayModificadoProyectos=Proyectos;
		this.arrayOriginalProyectos = new ArrayList<Proyecto>();
		this.arrayOriginalProyectos.addAll(Proyectos);
	}
	
	  @SuppressLint("InflateParams") @Override
	  public View getView(final int position, final View convertView, ViewGroup parent) {
	      
		View rowView = convertView;
	    // reuse views
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.list_item_generico, null);
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.nombre = (TextView) rowView.findViewById(R.id.list_item_generico_LblTitulo);
	      viewHolder.alias = (TextView) rowView.findViewById(R.id.list_item_generico_LblSubTitulo);
	      viewHolder.foto = (ImageView) rowView.findViewById(R.id.list_item_generico_ivfoto);
	      //viewHolder.foto.setVisibility(View.VISIBLE);
	      viewHolder.foto.setVisibility(View.INVISIBLE);
		  viewHolder.detalle = (ImageView) rowView.findViewById(R.id.list_item_generico_detalle);
		  viewHolder.detalle.setVisibility(View.VISIBLE);
		  viewHolder.mChart=(PieChart) rowView.findViewById(R.id.pieChart1);
	      viewHolder.mChart.setDescription("");
	      viewHolder.mChart.setHoleRadius(45f); 
	      viewHolder.mChart.setTransparentCircleRadius(50f);
	      viewHolder.mChart.getLegend().setEnabled(false);
	      viewHolder.mChart.setPivotX((float)0);
	      viewHolder.mChart.setPivotY((float)0);
	      
	      rowView.setTag(viewHolder);
	      
	    }
	     
	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    final Proyecto proy = arrayModificadoProyectos.get(position);
	    holder.nombre.setText(proy.getAliasProyecto());
	    holder.alias.setText(proy.getNombreProyecto());
	    holder.foto.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_camera));
	    holder.foto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IniciarNuevaFoto(proy);
			}
		});
		holder.detalle.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.folder));
		holder.detalle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IniciarDetalleProyecto(arrayModificadoProyectos.get(position));
			}
		});

	    //Fisico
	    final Double aFisico=((proy.getAvanceFisico())!=null
    			? proy.getAvanceFisico()
				:0) * 100;
	    holder.mChart.setData(generatePieData(Float.valueOf(String.valueOf(aFisico))));
	    holder.mChart.setTag(position);
	    holder.mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener(){
	    String msj="[Ejecutado: " + String.valueOf(Utilitarios.round(aFisico, 2)) + "%]  [Por ejecutar: "
	    		+ String.valueOf(Utilitarios.round((100-aFisico), 2)) + "%]" ;
	    		
			@Override
			public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
				// TODO Auto-generated method stub
				Toast.makeText(context,msj, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected() {
				// TODO Auto-generated method stub
				//Toast.makeText(context, "jjaja", Toast.LENGTH_SHORT).show();
			}
	    });
	  
	    
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
         ArrayList<Proyecto> tempList=new ArrayList<Proyecto>();
      
         if(constraint != null && arrayModificadoProyectos!=null && constraint.toString().length() > 0) {
             int length=arrayOriginalProyectos.size();
             int i=0;
             while(i<length){
            	String nombre =arrayOriginalProyectos.get(i).getAliasProyecto().toLowerCase(Locale.getDefault());
            
            	if(nombre.contains(constraint.toString().toLowerCase(Locale.getDefault())))
            		tempList.add(arrayOriginalProyectos.get(i));
            	
                i++;
             }

             filterResults.values = tempList;
             filterResults.count = tempList.size();   
                
          }
         else{
        	 synchronized(this)
             {
        		 filterResults.values = arrayOriginalProyectos;
        		 filterResults.count = arrayOriginalProyectos.size();
             }
         }

         return filterResults;
      }
        
    @SuppressWarnings("unchecked")
	@Override
      protected void publishResults(CharSequence contraint, FilterResults results) {
    	 arrayModificadoProyectos = (ArrayList<Proyecto>) results.values;
	     notifyDataSetChanged();
	     clear();
	     int l=arrayModificadoProyectos.size();
	     for(int i = 0; i < l; i++)
	      add(arrayModificadoProyectos.get(i));
	      notifyDataSetInvalidated();
      }
     };
     
     
     /**
      * generates less data (1 DataSet, 4 values)
      * @return
      */
     private PieData generatePieData(float mEjecutado) {
         
         ArrayList<Entry> entries1 = new ArrayList<Entry>();
         ArrayList<String> xVals = new ArrayList<String>();
         
         double pendiente=( 100-mEjecutado ) ;
         xVals.add("");        
         entries1.add(new Entry( mEjecutado, 0));
         xVals.add("");  
         entries1.add(new Entry((float) pendiente, 1));
         
         PieDataSet ds1 = new PieDataSet(entries1, "");
         int[] colors = {context.getResources().getColor(R.color.graphLightBlue),
        		 context.getResources().getColor(R.color.graphRed)};
         ds1.setColors(colors);
         ds1.setSliceSpace(50f);
         ds1.setValueTextColor(Color.BLACK);
         ds1.setValueTextSize(8f);
         
         PieData d = new PieData(xVals, ds1);
         //d.setValueTypeface(tf);
         
         return d;
         
     }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
     
	

	private void IniciarNuevaFoto(Proyecto proyecto){
		Intent mainIntent = new Intent(context,FotoNuevaActivity.class);
		//mainIntent.putExtra("TipoFoto", TipoFoto.Proyecto);
		//mainIntent.putExtra("Proyecto", proyecto);
		context.startActivity(mainIntent);
	}

	private void IniciarDetalleProyecto(Proyecto Proyecto){
		Intent mainIntent = new Intent(context, ProyectoDetalleActivity.class);
		mainIntent.putExtra("Proyecto", Proyecto);
		context.startActivity(mainIntent);
	}


	
}
