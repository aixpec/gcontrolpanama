package com.gisystems.gcontrolpanama.adaptadores;

import java.util.ArrayList;
import java.util.Locale;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.ActividadAvance;
import com.gisystems.gcontrolpanama.models.Actividad;
import com.gisystems.gcontrolpanama.models.AppValues.EstadosEnvio;
import com.gisystems.gcontrolpanama.models.BitacoraItem;

import com.gisystems.gcontrolpanama.models.BitacoraItem.TipoBitacora;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.gcontrolpanama.models.Proyecto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;



public class AdaptadorBitacora extends ArrayAdapter<BitacoraItem> {

	private Activity context;
	private Actividad tActividad;
	
	private final ArrayList<BitacoraItem> arrayOriginal;
	private ArrayList<BitacoraItem> arrayModificado;

	static class ViewHolder {
	    public TextView tvProyecto;
		public TextView tvConstruccion;
	    public TextView tvActividad;
	    public TextView tvFechaCaptura;
	    public TextView tvFechaEnvio;
	    public TextView tvEstado;
	    public TextView tvIdAntiguedad;
	    public ImageView ivFoto;
	 }
	
	 @SuppressLint("InflateParams") @Override
	  public View getView(final int position, final View convertView, ViewGroup parent) {
	      
		View rowView = convertView;
	    // reuse views
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.list_item_bitacora, null);
	      // configure view holder 
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.tvProyecto = (TextView) rowView.findViewById(R.id.list_item_bitacora_LblProyecto);
		  viewHolder.tvConstruccion = (TextView) rowView.findViewById(R.id.list_item_bitacora_LblConstruccion);
	      viewHolder.tvActividad = (TextView) rowView.findViewById(R.id.list_item_bitacora_LblRenglon);
	      viewHolder.tvFechaCaptura = (TextView) rowView.findViewById(R.id.list_item_bitacora_LblFechaCaptura);
	      viewHolder.tvFechaEnvio = (TextView) rowView.findViewById(R.id.list_item_bitacora_LblFechaEnvio);
	      viewHolder.tvEstado = (TextView) rowView.findViewById(R.id.list_item_bitacora_LblEstado);
	      viewHolder.tvIdAntiguedad=(TextView) rowView.findViewById(R.id.list_item_bitacora_LblIDAntiguedad);
	      viewHolder.ivFoto = (ImageView) rowView.findViewById(R.id.list_item_bitacora_ivestado);
	      
	      rowView.setTag(viewHolder);
	      
	    }
	     
	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    BitacoraItem item = arrayModificado.get(position);
	    ActividadAvance avance=item.getActividadAvance();

	    if (avance!=null){//Hacer el select del tipo de bitacora

			holder.tvProyecto.setText(Proyecto.obtenerProyecto(context, avance.getIdCliente(), avance.getIdProyecto()).getAliasProyecto());
			holder.tvConstruccion.setText(Construccion.obtenerConstruccion(context, avance.getIdCliente(), avance.getIdProyecto(), avance.getIdConstruccion()).getDescripcion());
			holder.tvActividad.setText(Actividad.obtenerActividad(context, avance.getIdCliente(), avance.getIdProyecto(), avance.getIdConstruccion(), avance.getIdActividad()).getCodigoInstitucional());
			//holder.tvActividad.setText(String.valueOf(avance.getIdActividad()));

		    holder.tvFechaCaptura.setText("PreguntaFechaUI de captura: " + avance.getFechaCaptura());
		    holder.tvFechaEnvio.setText( "PreguntaFechaUI de envío: " + avance.getFechaEnvio());
		    holder.tvEstado.setText("Estado: " + avance.getEdoEnvio().name()); 
		    holder.tvIdAntiguedad.setText("Id. Recibido: " + String.valueOf(avance.getIdAntiguedad()));
		    holder.ivFoto.setImageBitmap(item.getImagenEstado());
	    }


	    return rowView;

	 }
	
	public AdaptadorBitacora(Activity Context, ArrayList<BitacoraItem> Items, TipoBitacora TBitacora){
		super(Context, R.layout.list_item_bitacora, Items);
		this.context=Context;
		this.arrayModificado=Items;
		this.arrayOriginal = new ArrayList<BitacoraItem>();
		this.arrayOriginal.addAll(Items);
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
         ArrayList<BitacoraItem> tempList=new ArrayList<BitacoraItem>();
      
         if(constraint != null && arrayModificado!=null && constraint.toString().length() > 0) {
             int length=arrayOriginal.size();
             int i=0;
             while(i<length){
            	 
            	String nombre =arrayOriginal.get(i).ObtenerTextoDescripcion().toLowerCase(Locale.getDefault());
            
            	if(nombre.contains(constraint.toString().toLowerCase(Locale.getDefault())))
            		tempList.add(arrayOriginal.get(i));
            	
                i++;
             }

             filterResults.values = tempList;
             filterResults.count = tempList.size();   
                
          }
         else{
        	 synchronized(this)
             {
        		 filterResults.values = arrayOriginal;
        		 filterResults.count = arrayOriginal.size();
             }
         }

         return filterResults;
      }
        
    @SuppressWarnings("unchecked")
	@Override
      protected void publishResults(CharSequence contraint, FilterResults results) {
    	 arrayModificado = (ArrayList<BitacoraItem>) results.values;
	     notifyDataSetChanged();
	     clear();
	     int l=arrayModificado.size();
	     for(int i = 0; i < l; i++)
	      add(arrayModificado.get(i));
	      notifyDataSetInvalidated();
      }
     };
     
     
     public ArrayList<BitacoraItem> obtenerItemsPorEstado(ArrayList<EstadosEnvio> Estados){
    	 ArrayList<BitacoraItem> items=new ArrayList<BitacoraItem>();
    	 for(int index=0;index<arrayOriginal.size();index++){
    		
    		 if(Estados.contains(arrayOriginal.get(index).getActividadAvance().getEdoEnvio())){
    			 items.add(arrayOriginal.get(index));
    		 }
    	 }
    	 
    	 return items;
     }
     
	
}
