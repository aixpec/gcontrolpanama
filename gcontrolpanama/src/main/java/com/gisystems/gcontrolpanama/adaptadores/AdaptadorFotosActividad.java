package com.gisystems.gcontrolpanama.adaptadores;


import java.util.ArrayList;

import com.gisystems.gcontrolpanama.R;

import com.gisystems.gcontrolpanama.models.FotoActividad;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class AdaptadorFotosActividad extends ArrayAdapter<FotoActividad> {

	private Activity context; 
	private ArrayList<FotoActividad> fotos;

	static class ViewHolder {
	    public ImageView foto;
	    public LinearLayout layout;
	 }
	
	public AdaptadorFotosActividad(Activity Context, ArrayList<FotoActividad> Fotos){
		super(Context, R.layout.grid_item_actividades, Fotos);
		this.context=Context;
		this.fotos=Fotos;
	}
	
	
	  @SuppressLint("InflateParams") @Override
	  public View getView(final int position,  View convertView, ViewGroup parent) {
	  
	    // reuse views
		ViewHolder holder;
	    if (convertView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      convertView = inflater.inflate(R.layout.grid_item_actividades, null);
	      // configure view holder
	      holder = new ViewHolder();
	      holder.foto = (ImageView) convertView
		          .findViewById(R.id.grid_item_actividades_foto);
	      holder.layout=(LinearLayout) convertView.findViewById(R.id.grid_item_actividades_layout);
	      convertView.setTag(holder);
	    }
	    else
	    {
	    	 holder = (ViewHolder) convertView.getTag();
	    }
	    
	    FotoActividad foto= fotos.get(position);
	  
	    Bitmap img;
	    img=  foto.getImagen(); //obtenerFoto(foto.getRutaFoto());
	    if (img==null){
	    	img=BitmapFactory.decodeResource(context.getResources(), R.drawable.no);
	    }
	    
	    holder.foto.setImageBitmap(img);
	
	    if (foto.isEstadoSeleccion()) {
	    	holder.layout.setBackgroundColor(context.getResources().getColor(R.color.graphLightBlue));
        } else {
        	holder.layout.setBackgroundColor(context.getResources().getColor(R.color.Grey50));
        }
	    
	    return convertView;
	 }

	 public void ActualizarListado(ArrayList<FotoActividad> Fotos){
		 
		 int m= this.fotos.size();
		 int l=Fotos.size();
		 boolean igual=false;
		 int j=0;
		 
		 if(m>0)
		 {
			 for(int i = 0; i < l; i++)
			 {
				 for( j = 0; j < m; j++)
				 {
					 if (Fotos.get(i).equals(this.fotos.get(j)))
					 {
						 igual=true;
						 break;
					 }
				 }
				 Log.w("FotosEqual",String.valueOf(igual));
				 if (!igual)  add(Fotos.get(i));
				 igual=false;
			 }
		 }
		 else
		 {
			 for( j = 0; j < l; j++)
			 {
				 add(Fotos.get(j));
			 }
		 }
		 notifyDataSetChanged();
	 }
	 
	 public ArrayList<FotoActividad> ObtenerFotosSeleccionadas(){
		 ArrayList<FotoActividad> fotosSeleccionadas=new ArrayList<FotoActividad>();
		 for(int index=0; index<fotos.size(); index++){
			 if(fotos.get(index).isEstadoSeleccion())
				 fotosSeleccionadas.add(fotos.get(index));
		 }
		 return fotosSeleccionadas;
	 }
	 
	 public int ObtenerTotalFotosSeleccionadas(){
		 ArrayList<FotoActividad> fotosSeleccionadas=ObtenerFotosSeleccionadas();
		 return (fotosSeleccionadas!=null)? fotosSeleccionadas.size():0;
	 }
	 	  
}
