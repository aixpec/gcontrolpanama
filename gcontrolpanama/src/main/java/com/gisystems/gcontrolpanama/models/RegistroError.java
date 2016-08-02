package com.gisystems.gcontrolpanama.models;

import android.content.Context;

public class RegistroError {
	private Context context;
	private String mensaje_error; 
	private String stack_trace;
	private String nombre_clase; 
	private String nombre_metodo;
	private String json_peticion;
	private String json_respuesta;
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public String getMensaje_error() {
		return mensaje_error;
	}
	public void setMensaje_error(String mensaje_error) {
		this.mensaje_error = mensaje_error;
	}
	public String getStack_trace() {
		return stack_trace;
	}
	public void setStack_trace(String stack_trace) {
		this.stack_trace = stack_trace;
	}
	public String getNombre_clase() {
		return nombre_clase;
	}
	public void setNombre_clase(String nombre_clase) {
		this.nombre_clase = nombre_clase;
	}
	public String getNombre_metodo() {
		return nombre_metodo;
	}
	public void setNombre_metodo(String nombre_metodo) {
		this.nombre_metodo = nombre_metodo;
	}
	public String getJson_peticion() {
		return json_peticion;
	}
	public void setJson_peticion(String json_peticion) {
		this.json_peticion = json_peticion;
	}
	public String getJson_respuesta() {
		return json_respuesta;
	}
	public void setJson_respuesta(String json_respuesta) {
		this.json_respuesta = json_respuesta;
	}
}
