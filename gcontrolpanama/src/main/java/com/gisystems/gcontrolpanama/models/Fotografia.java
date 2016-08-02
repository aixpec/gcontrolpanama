package com.gisystems.gcontrolpanama.models;

import java.io.File;

import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.Utilitarios.TipoFoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Fotografia {
	
	private int idAntiguedadDisp;
	private int idAntiguedad;
	private int idFoto;
	private String comentario;
	private String rutaFoto;
	private double latitud;
	private double longitud;
	private double altitud;
	private String FechaCaptura;
	private String FechaEnvio;
	private AppValues.EstadosEnvio EdoEnvio;
	private Bitmap imagen;
	private String stringImagen;
	private boolean estadoSeleccion;
	private TipoFoto tipoFoto;

	public int getIdAntiguedadDisp() {
		return idAntiguedadDisp;
	}
	public void setIdAntiguedadDisp(int idAntiguedadDisp) {
		this.idAntiguedadDisp = idAntiguedadDisp;
	}
	public int getIdAntiguedad() {
		return idAntiguedad;
	}
	public void setIdAntiguedad(int idAntiguedad) {
		this.idAntiguedad = idAntiguedad;
	}
	public int getIdFoto() {
		return idFoto;
	}
	public void setIdFoto(int idFoto) {
		this.idFoto = idFoto;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public String getRutaFoto() {
		return rutaFoto;
	}
	public void setRutaFoto(String rutaFoto) {
		this.rutaFoto = rutaFoto;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	public double getAltitud() {
		return altitud;
	}
	public void setAltitud(double altitud) {
		this.altitud = altitud;
	}
	public String getFechaCaptura() {
		return FechaCaptura;
	}
	public void setFechaCaptura(String fechaCaptura) {
		FechaCaptura = fechaCaptura;
	}
	public String getFechaEnvio() {
		return FechaEnvio;
	}
	public void setFechaEnvio(String fechaEnvio) {
		FechaEnvio = fechaEnvio;
	}
	public AppValues.EstadosEnvio getEdoEnvio() {
		return EdoEnvio;
	}
	public void setEdoEnvio(AppValues.EstadosEnvio edoEnvio) {
		EdoEnvio = edoEnvio;
	}
	public Bitmap getImagen() {
		return imagen;
	}
	public void setImagen(Bitmap imagen) {
		this.imagen = imagen;
	}
	public String getStringImagen() {
		return stringImagen;
	}
	public void setStringImagen(String stringImagen) {
		this.stringImagen = stringImagen;
	}
	public boolean isEstadoSeleccion() {
		return estadoSeleccion;
	}
	public void setEstadoSeleccion(boolean estadoSeleccion) {
		this.estadoSeleccion = estadoSeleccion;
	}
	public TipoFoto getTipoFoto() {
		return tipoFoto;
	}
	public void setTipoFoto(TipoFoto tipoFoto) {
		this.tipoFoto = tipoFoto;
	}
	public Bitmap obtenerBMapDeFoto(String PATH, Context context){
		 
	    try{
	    	  BitmapFactory.Options bmo = new BitmapFactory.Options ();
			    bmo.inSampleSize = 16;
			    //if (bm != null) bm.recycle();
			    Bitmap bm = null;
			    bm = BitmapFactory.decodeFile (PATH, bmo);
			    //verificar si existe la imagen
				File f = new File(PATH);
			    if (f.exists())
				{
			    	if (bm != null)
			    		return bm;
				}
	
	  } catch (Exception e) {
		ManejoErrores.registrarError_MostrarDialogo(context, e, 
				Fotografia.class.getSimpleName(), "obtenerFoto", 
            null, null);   
	  }
	  return null;
	}
	

	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof FotoActividad)) {
	        return false;
	    }
	
	    FotoActividad that = (FotoActividad) other;
	
	    // Custom equality check here.
	    return this.rutaFoto.equals(that.getRutaFoto());
	    
	}
	

}
