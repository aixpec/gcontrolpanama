package com.gisystems.gcontrolpanama.adaptadores;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.reglas.FotoNuevaActivity;
import com.gisystems.utils.Utilitarios;
import com.gisystems.utils.Utilitarios.TipoFoto;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;
import java.util.Locale;

public class AdaptadorConstrucciones extends ArrayAdapter<Construccion>  implements OnClickListener{

	private Activity context;
	private final ArrayList<Construccion> arrayOriginalConstrucciones;
	private ArrayList<Construccion> arrayModificadoConstrucciones;
	static class ViewHolder {
	    public TextView nombre;
	    public TextView alias;
	    public ImageView foto;
	    public PieChart mChart;

	 }

	public AdaptadorConstrucciones(Activity Context, ArrayList<Construccion> Construcciones){
		super(Context, R.layout.list_item_construcciones, Construcciones);
		this.context=Context;
		
		this.arrayModificadoConstrucciones=Construcciones;
		this.arrayOriginalConstrucciones = new ArrayList<Construccion>();
		this.arrayOriginalConstrucciones.addAll(Construcciones);
	}
	
	  @SuppressLint("InflateParams") @Override
	  public View getView(final int position, final View convertView, ViewGroup parent) {
	      
		View rowView = convertView;
	    // reuse views
	    if (rowView == null) {



	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.list_item_construcciones, null);
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.nombre = (TextView) rowView.findViewById(R.id.list_item_construccion_LblTitulo);
	      viewHolder.alias = (TextView) rowView.findViewById(R.id.list_item_construccion_LblSubTitulo);
	      viewHolder.foto = (ImageView) rowView.findViewById(R.id.list_item_construccion_ivfoto);
	      //viewHolder.foto.setVisibility(0);
	      viewHolder.foto.setVisibility(View.INVISIBLE);
	      viewHolder.mChart=(PieChart) rowView.findViewById(R.id.pieChart2);
	      viewHolder.mChart.setDescription("");
	      viewHolder.mChart.setHoleRadius(45f);
			viewHolder.mChart.setTransparentCircleRadius(50f);
			viewHolder.mChart.getLegend().setEnabled(false);
			viewHolder.mChart.setPivotX((float) 0);
			viewHolder.mChart.setPivotY((float) 0);
			viewHolder.mChart.setVisibility(View.INVISIBLE);

			rowView.setTag(viewHolder);
	      
	    }
	     
	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    final Construccion constr = arrayModificadoConstrucciones.get(position);
	    //**holder.nombre.setText(proy.getAliasProyecto());
	    //**holder.alias.setText(proy.getNombreProyecto());

		  //if (constr.getDescripcion().length() > 35){
		  //  holder.nombre.setText(constr.getDescripcion().substring(0,30)+ "...");
		  // }
		  //else
		 //  {
			  holder.nombre.setText(constr.getDescripcion());
		  //}

		//holder.nombre.setText(constr.getDescripcion());
		holder.alias.setText("Avance  " + String.valueOf(constr.getPorcAvance()) + "%");
	    holder.foto.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_camera));
	    holder.foto.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	IniciarNuevaFoto(constr);
	         }
	     });
	    
	    //Fisico
	    final Double aFisico=((constr.getAvanceFisico())!=null
    			? constr.getAvanceFisico()
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
         ArrayList<Construccion> tempList=new ArrayList<Construccion>();
      
         if(constraint != null && arrayModificadoConstrucciones!=null && constraint.toString().length() > 0) {
             int length=arrayOriginalConstrucciones.size();
             int i=0;
             while(i<length){
            	String nombre =arrayOriginalConstrucciones.get(i).getDescripcion().toLowerCase(Locale.getDefault());
            
            	if(nombre.contains(constraint.toString().toLowerCase(Locale.getDefault())))
            		tempList.add(arrayOriginalConstrucciones.get(i));
            	
                i++;
             }

             filterResults.values = tempList;
             filterResults.count = tempList.size();   
                
          }
         else{
        	 synchronized(this)
             {
        		 filterResults.values = arrayOriginalConstrucciones;
        		 filterResults.count = arrayOriginalConstrucciones.size();
             }
         }

         return filterResults;
      }
        
    @SuppressWarnings("unchecked")
	@Override
      protected void publishResults(CharSequence contraint, FilterResults results) {
    	 arrayModificadoConstrucciones = (ArrayList<Construccion>) results.values;
	     notifyDataSetChanged();
	     clear();
	     int l=arrayModificadoConstrucciones.size();
	     for(int i = 0; i < l; i++)
	      add(arrayModificadoConstrucciones.get(i));
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
		mainIntent.putExtra("TipoFoto", TipoFoto.Proyecto);
		mainIntent.putExtra("Proyecto", proyecto);
		context.startActivity(mainIntent);
	}
   
    
	
}
