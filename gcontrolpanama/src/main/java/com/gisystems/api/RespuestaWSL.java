package com.gisystems.api;

import org.json.JSONObject;

import android.util.Log;

public class RespuestaWSL {

	private String json = "";
	private boolean ack = false;
	private String motivo = "";
	private String parametros = "";

	public String getRespuestaFormatoJSON() {
		return this.json;
	}

	public void setRespuestaFormatoJSON(String json) {
		this.json = json;
		ObtenerDatosDelJSON();
	}

	public boolean getEjecutadoSinError() {
		return ack;
	}

	public String getMotivoFallo() {
		return motivo;
	}

	public String getParametros() {
		return parametros;
	}
	
	public RespuestaWSL (String json) {
		if (json.endsWith("\"")) {
			json = json.substring(0, json.length() -1);
		}
		if (json.startsWith("\"")) {
			json = json.substring(1);
		}
		//json = json.replace("\\\\\\\"", "###");
		//json = json.replace("\"", "\'");
		//json = json.replace("###", "\"");
		//json = json.replace("\\", "");
		setRespuestaFormatoJSON(json);
	}

	private void InicializarPropiedades() {
		ack = false;
		motivo = "";
		parametros = "";
	}

	private boolean JsonCorrecto() {
		boolean correcto = false;
		try {
			correcto = (json.trim().length() > 0);
			if (correcto) {
				JSONObject x = new JSONObject(this.json);
				correcto = x.getBoolean("ack");
			}
		} catch (Exception e) {
			Log.e("JSON error: ", Log.getStackTraceString(e));
		}
		return correcto;
	}

	private void ObtenerDatosDelJSON() {
		try {
			InicializarPropiedades();
			// 1. Verificar que el JSON sea de respuesta de WSL
			if (JsonCorrecto()) {
				JSONObject x = new JSONObject(this.json);
				// 2. Extraer propiedad ACK
				this.ack = x.getBoolean("ack");
				// 3. Extraer propiedad Motivo
				if (x.has("motivo")) {
					this.motivo = x.getString("motivo");
				}
				// 4. Extraer propiedad Parametros
				if (this.ack) {
					if (x.has("parametros")) {
						this.parametros = x.get("parametros").toString();
					}
				}
			} else {
				this.motivo = "Formato JSON incorrecto.";
			}
		} catch (Exception e) {
			this.motivo = e.getMessage();
			Log.e("Err_ObtenerDatosDelJSON", Log.getStackTraceString(e));
		}
	}

}
