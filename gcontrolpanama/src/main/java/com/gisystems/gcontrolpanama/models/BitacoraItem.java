package com.gisystems.gcontrolpanama.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.AppValues.EstadosEnvio;

public class BitacoraItem {


	private ActividadAvance actividadAvance;

	private TipoBitacora tipoBitacora;
	private EstadosEnvio edoEnvio;

	private int idAntiguedad;
	private Context context;
	
	public TipoBitacora getTipoBitacora() {
		return tipoBitacora;
	}

	public ActividadAvance getActividadAvance() {
		return actividadAvance;
	}

	public int getIdAntiguedad() {
		return idAntiguedad;
	}
	
	public Object ObtenerItem(){
		Object o;
		switch(this.tipoBitacora){
			case Registro_De_Avances_Con_Fotografia:
				o = this.actividadAvance;
				break;
			default:
				o=null;
				break;
			}
		return o;
	}
	
	public enum TipoBitacora { Registro_De_Avances_Con_Fotografia, Fotografia};
	
	public BitacoraItem(ActividadAvance ActAvance, Context Ctx){
		this.tipoBitacora=TipoBitacora.Registro_De_Avances_Con_Fotografia;
		this.context=Ctx;
		this.actividadAvance=ActAvance;
		this.edoEnvio=this.actividadAvance.getEdoEnvio();
	}
	
	public Bitmap getImagenEstado()
	{
		
		Bitmap bMap =null;
			switch(this.edoEnvio){
				case Enviado:
					bMap = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),
							R.drawable.green_button_75);
				break;
				case Pendiente_De_Confirmacion:
					bMap = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),
							R.drawable.yellow_dark_button_75);
				break;
				case No_Enviado:
					bMap = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),
							R.drawable.gray_button_75);
				break;
				default:
					bMap = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),
							R.drawable.red_button);
				break;
				}
			
		return bMap;
	}
	
	public String ObtenerTextoDescripcion(){
		String tBusqueda="";
		switch(this.tipoBitacora){
			case Registro_De_Avances_Con_Fotografia:
			//	tBusqueda=(this.actividadAvance.getDescripcion().length()>125)
			//	? this.actividadAvance.getDescripcion().substring(0, 125):this.actividadAvance.getDescripcion();

				tBusqueda=(this.actividadAvance.getDescripcion());

				break;
			default:
				tBusqueda="";
				break;
		}
		return 	tBusqueda;
	}
	
	
	
	
	
}
