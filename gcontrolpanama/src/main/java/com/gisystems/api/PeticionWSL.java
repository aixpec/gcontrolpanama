package com.gisystems.api;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class PeticionWSL {
	private String origen;
    private String funcion;
    private String userName;
    private String password;
    private String imei;
    private String imsi;
    private String nombreArchivoAssembly;
    private String namespaceClase;
    private String nombreClase;
    private String metodoEjecutara;
    private List<Object> parametros;
    private String pathLog;
    
	public String getOrigen() {
		return origen;
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getFuncion() {
		return funcion;
	}
	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getNombreArchivoAssembly() {
		return nombreArchivoAssembly;
	}
	public void setNombreArchivoAssembly(String nombreArchivoAssembly) {
		this.nombreArchivoAssembly = nombreArchivoAssembly;
	}
	public String getNamespaceClase() {
		return namespaceClase;
	}
	public void setNamespaceClase(String namespaceClase) {
		this.namespaceClase = namespaceClase;
	}
	public String getNombreClase() {
		return nombreClase;
	}
	public void setNombreClase(String nombreClase) {
		this.nombreClase = nombreClase;
	}
	public String getMetodoEjecutara() {
		return metodoEjecutara;
	}
	public void setMetodoEjecutara(String metodoEjecutara) {
		this.metodoEjecutara = metodoEjecutara;
	}
	public Object getParametros() {
		return parametros;
	}
	public void setParametros(List<Object> parametros) {
		this.parametros = parametros;
	}
	public String getPathLog() {
		return pathLog;
	}
	public void setPathLog(String pathLog) {
		this.pathLog = pathLog;
	}
	
	public String toJSON(){
	    JSONObject jsonObject= new JSONObject();
	    JSONArray array;
	    try {
	    	jsonObject.put("origen", this.origen);
	    	jsonObject.put("funcion", this.funcion);
	    	jsonObject.put("userName", this.userName);
	    	jsonObject.put("password", this.password);
	    	jsonObject.put("imei", this.imei);
	    	jsonObject.put("imsi", this.imsi);
	    	jsonObject.put("nombreArchivoAssembly", this.nombreArchivoAssembly);
	    	jsonObject.put("namespaceClase", this.namespaceClase);
	    	jsonObject.put("nombreClase", this.nombreClase);
	    	jsonObject.put("metodoEjecutara", this.metodoEjecutara);
	    	array = new JSONArray(this.parametros);
	    	jsonObject.put("parametros", array);
	    	jsonObject.put("pathLog", this.pathLog);
	        return jsonObject.toString();
	    } catch (Exception e) {
			Log.e("JSON error: ", Log.getStackTraceString(e));
			return "";
		}
	}
	
	
}
