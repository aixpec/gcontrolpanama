package com.gisystems.utils;


import java.util.Date;

/**
 * 
 * Clase que modela un punto geogr�fico con sus propiedades particulares.
 * 
 * @author Milton Carranza
 * 
 * @version 1
 *  
 */

public class GeoPoint {
	
	private Double altitud;
	private Date fecha_captura;
	private Double latitud;
	private Double longitud;
	
	public GeoPoint(Double latitud,
			Double longitud, Double altitud, Date fechaCaptura) {
		super();
		this.latitud = latitud;
		this.longitud = longitud;
		this.altitud = altitud;
		this.fecha_captura = fechaCaptura;
	}

	/**
	 * @return the altitud
	 */
	public Double getAltitud() {
		return altitud;
	}
	/**
	 * @return the fecha_captura
	 */
	public Date getFecha_captura() {
		return fecha_captura;
	}
	/**
	 * @return the latitud
	 */
	public Double getLatitud() {
		return latitud;
	}
	/**
	 * @return the longitud
	 */
	public Double getLongitud() {
		return longitud;
	}
	/**
	 * @param altitud the altitud to set
	 */
	public void setAltitud(Double altitud) {
		this.altitud = altitud;
	}
	/**
	 * @param fecha_captura the fecha_captura to set
	 */
	public void setFecha_captura(Date fecha_captura) {
		this.fecha_captura = fecha_captura;
	}
	/**
	 * @param latitud the latitud to set
	 */
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}
	/**
	 * @param longitud the longitud to set
	 */
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

}
