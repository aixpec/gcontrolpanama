package com.gisystems.api;

import java.util.ArrayList;
import java.util.EmptyStackException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gisystems.gcontrolpanama.models.ActividadDescripcion;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.gcontrolpanama.reglas.MiExcepcion;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.gcontrolpanama.models.Actividad;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.Cliente;
import com.gisystems.gcontrolpanama.models.EstadoProyecto;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.TipoProyecto;
import com.gisystems.gcontrolpanama.models.TipoUnidad;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.Utilitarios;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;


public class RecepcionDatosAPI {
	
	private Context ctx;
	private String userName = ""; 
	private String password = "";
	private DAL w;
	private MiExcepcion miExcepcion; 
	public RecepcionDatosAPI(Context context) {
		//Obtener datos globales
		ctx=context;
		this.userName=AppValues.SharedPref_obtenerUsuarioNombre(ctx);
		this.password=AppValues.SharedPref_obtenerUsuarioPassword(ctx);
	}
	
	public boolean InicializarBD()  {
		
		boolean resultado = false;
		PeticionWSL peticion = null;
		RespuestaWSL respuesta = null;
		miExcepcion=new MiExcepcion();
		try {	
			
			//1. Obtener la petición para la capa WSL de la arquitectura
			ArrayList<Object> parametros = new ArrayList<Object>(); 
			parametros.add(this.userName);
			String nombreArchivoAssembly = "Gisystems.Elephant.BLL.dll"; 
	        String namespaceClase = "Gisystems.Elephant.BLL";
			String nombreClase = "ApiAppMovil"; 
			String metodoEjecutara = "ObtenerDatosInicializacionBD";
			String pathLog = ""; 
			peticion = Utilitarios.ObtenerPeticionWSL(ctx, 
					Utilitarios.TipoFuncion.ejecutarMetodo, 
					this.userName, this.password, parametros, nombreArchivoAssembly, namespaceClase, 
					nombreClase, metodoEjecutara, pathLog);
			
			//2. Enviar petición. Conexión al API REST de la capa WSL
			JSONObject datosJSON;
			JSONArray array;
			JSONObject registro;
            respuesta = BusinessCloud.sendRequestWSL(ctx, peticion);
            try{
	            //Iniciar la transacción
	            w=new DAL(ctx);
	            w.iniciarTransaccion();
	            ContentValues values;
	            
	            if (respuesta.getEjecutadoSinError()) {

					w.deleteRow(ActividadDescripcion.NOMBRE_TABLA, null);
					w.deleteRow(Construccion.NOMBRE_TABLA, null);
	            	w.deleteRow(Actividad.NOMBRE_TABLA, null);
	            	w.deleteRow(TipoUnidad.NOMBRE_TABLA, null);
	            	w.deleteRow(Proyecto.NOMBRE_TABLA, null);
	            	w.deleteRow(EstadoProyecto.NOMBRE_TABLA, null);
	             	w.deleteRow(TipoProyecto.NOMBRE_TABLA, null);
	            	w.deleteRow(Cliente.NOMBRE_TABLA, null);
	           
	            	String datosObtenidos = "";
					datosObtenidos = respuesta.getParametros();
					datosJSON = new JSONObject(datosObtenidos);
	            	
					//Vaidar que existan en el JSON las entidades a almacenar
					if( !(datosJSON.has(Cliente.NOMBRE_TABLA)
							&& datosJSON.has(TipoProyecto.NOMBRE_TABLA)
							&& datosJSON.has(EstadoProyecto.NOMBRE_TABLA)
							&& datosJSON.has(Proyecto.NOMBRE_TABLA)
							&& datosJSON.has(TipoUnidad.NOMBRE_TABLA)
							&& datosJSON.has(Actividad.NOMBRE_TABLA)
							&& datosJSON.has(Construccion.NOMBRE_TABLA)
							&& datosJSON.has(ActividadDescripcion.NOMBRE_TABLA))) {
							w.finalizarTransaccion(false);
							return false;}
					
					//3. Obtener datos para la tabla Clientes				
						array = datosJSON.getJSONArray(Cliente.NOMBRE_TABLA);
						if (array.length()==0) {
							miExcepcion.setMsgCustom("No posee registros en: " + Cliente.NOMBRE_TABLA);
							throw miExcepcion;
						}
						for(int i = 0 ; i < array.length(); i++){
							
							registro = array.getJSONObject(i);
							values=new ContentValues();
							values.put(Cliente.COLUMN_ID, 			registro.getInt(Cliente.COLUMN_ID));
							values.put(Cliente.COLUMN_CLIENTE, 		registro.getString(Cliente.COLUMN_CLIENTE));
							values.put(Cliente.COLUMN_DIRECCION, 	registro.getString(Cliente.COLUMN_DIRECCION));
							values.put(Cliente.COLUMN_TELEFONO, 	registro.getString(Cliente.COLUMN_TELEFONO));
							
							if (w.insertRow(Cliente.NOMBRE_TABLA, values)<0) 
								throw new EmptyStackException();
							
							values.clear();
					    }
					
					
					Log.w("RecepcionDatosApi", "Fin incersion CLIENTES");
					
					//4. Obtener datos para la tabla Tipo Proyecto			
						array = datosJSON.getJSONArray(TipoProyecto.NOMBRE_TABLA);
						
						for(int i = 0 ; i < array.length(); i++){
							registro = array.getJSONObject(i);
							values = new ContentValues();
							values.put(TipoProyecto.COLUMN_ID, 				registro.getInt(TipoProyecto.COLUMN_ID));
							values.put(TipoProyecto.COLUMN_TIPO_PROYECTO, 	registro.getString(TipoProyecto.COLUMN_TIPO_PROYECTO));
							values.put(TipoProyecto.COLUMN_DESCRIPCION, 	registro.getString(TipoProyecto.COLUMN_DESCRIPCION));
							
							if (w.insertRow(TipoProyecto.NOMBRE_TABLA, values)<0) 
								throw new EmptyStackException();
							values.clear();
											
					    }
					
					Log.w("RecepcionDatosApi", "Fin incersion TIPOS PROYECTO");
					
					//5. Obtener datos para la tabla Estado Proyecto				
						array = datosJSON.getJSONArray(EstadoProyecto.NOMBRE_TABLA);				
						for(int i = 0 ; i < array.length(); i++){
							registro = array.getJSONObject(i);
							values = new ContentValues();
							values.put(EstadoProyecto.COLUMN_ID, 			registro.getInt(EstadoProyecto.COLUMN_ID));
							values.put(EstadoProyecto.COLUMN_EDO_PROYECTO, 	registro.getString(EstadoProyecto.COLUMN_EDO_PROYECTO));
							
							if (w.insertRow(EstadoProyecto.NOMBRE_TABLA, values)<0) 
								throw new EmptyStackException();		
							values.clear();
					    }
					
					Log.w("RecepcionDatosApi", "Fin incersion ESTADOS PROYECTO");
					
					//6. Obtener datos para la tabla Proyecto				
						array = datosJSON.getJSONArray(Proyecto.NOMBRE_TABLA);
						if (array.length()==0) {
							miExcepcion.setMsgCustom("No posee registros en: " + Proyecto.NOMBRE_TABLA);
							throw miExcepcion;
						}
						for(int i = 0 ; i < array.length(); i++){
							registro = array.getJSONObject(i);
							values = new ContentValues();
							values.put(Proyecto.COLUMN_ID, 					registro.getInt(Proyecto.COLUMN_ID));
							values.put(Proyecto.COLUMN_ID_CLIENTE, 			registro.getInt(Proyecto.COLUMN_ID_CLIENTE));
							values.put(Proyecto.COLUMN_ID_TIPO_PROYECTO, 	registro.getInt(Proyecto.COLUMN_ID_TIPO_PROYECTO));
							values.put(Proyecto.COLUMN_ID_EDO_PROYECTO, 	registro.getInt(Proyecto.COLUMN_ID_EDO_PROYECTO));
							values.put(Proyecto.COLUMN_NOMBRE_PROYECTO, 	registro.getString(Proyecto.COLUMN_NOMBRE_PROYECTO));
							values.put(Proyecto.COLUMN_ALIAS_PROYECTO, 		registro.getString(Proyecto.COLUMN_ALIAS_PROYECTO));
							values.put(Proyecto.COLUMN_FECHA_INICIA, 		registro.getString(Proyecto.COLUMN_FECHA_INICIA));
							values.put(Proyecto.COLUMN_FECHA_FINALIZA, 		registro.getString(Proyecto.COLUMN_FECHA_FINALIZA));
							values.put(Proyecto.COLUMN_MONEDA, 				registro.getString(Proyecto.COLUMN_MONEDA));
							values.put(Proyecto.COLUMN_MONTO_ORIGINAL, 		Utilitarios.round(registro.getDouble(Proyecto.COLUMN_MONTO_ORIGINAL),2));
							values.put(Proyecto.COLUMN_MONTO_MODIFICADO, 	Utilitarios.round(registro.getDouble(Proyecto.COLUMN_MONTO_MODIFICADO),2));
							values.put(Proyecto.COLUMN_MONTO_PAGADO, 		Utilitarios.round(registro.getDouble(Proyecto.COLUMN_MONTO_PAGADO),2));
							values.put(Proyecto.COLUMN_MONTO_EJECUTADO, 	Utilitarios.round(registro.getDouble(Proyecto.COLUMN_MONTO_EJECUTADO),2));
							values.put(Proyecto.COLUMN_AVANCE_FISICO, 		Utilitarios.round(registro.getDouble(Proyecto.COLUMN_AVANCE_FISICO),2));
							values.put(Proyecto.COLUMN_AVANCE_FINANCIERO, 	Utilitarios.round(registro.getDouble(Proyecto.COLUMN_AVANCE_FINANCIERO),2));
							values.put(Proyecto.COLUMN_LATITUD, 			registro.getDouble(Proyecto.COLUMN_LATITUD));
							values.put(Proyecto.COLUMN_LONGITUD, 			registro.getDouble(Proyecto.COLUMN_LONGITUD));
							values.put(Proyecto.COLUMN_ALTITUD, 			registro.getDouble(Proyecto.COLUMN_ALTITUD));
							values.put(Proyecto.COLUMN_TIPO_GESTION, 		registro.getInt(Proyecto.COLUMN_TIPO_GESTION));
							values.put(Proyecto.COLUMN_DESCRIPCION, 		registro.getString(Proyecto.COLUMN_DESCRIPCION));
							
							if (w.insertRow(Proyecto.NOMBRE_TABLA, values)<0) 
								throw new EmptyStackException();
							values.clear();
					    }
					
					Log.w("RecepcionDatosApi", "Fin incersion PROYECTOS");
					
					
					//7. Obtener datos para la tabla Tipo Unidad			
						array = datosJSON.getJSONArray(TipoUnidad.NOMBRE_TABLA);
						for(int i = 0 ; i < array.length(); i++){
							registro = array.getJSONObject(i);
							values = new ContentValues();
							values.put(TipoUnidad.COLUMN_ID, 				registro.getInt(TipoUnidad.COLUMN_ID));
							values.put(TipoUnidad.COLUMN_ID_CLIENTE, 		registro.getInt(TipoUnidad.COLUMN_ID_CLIENTE));
							values.put(TipoUnidad.COLUMN_ID_TIPO_UNIDAD, 	registro.getInt(TipoUnidad.COLUMN_ID_TIPO_UNIDAD));
							values.put(TipoUnidad.COLUMN_DESCRIPCION, 		registro.getString(TipoUnidad.COLUMN_DESCRIPCION));
							
							if (w.insertRow(TipoUnidad.NOMBRE_TABLA, values)<0) 
								throw new EmptyStackException();
							values.clear();
					    }
					
					Log.w("RecepcionDatosApi", "Fin incersion TIPO UNIDAD");
					
					
					//8. Obtener datos para la tabla Actividades					
						array = datosJSON.getJSONArray(Actividad.NOMBRE_TABLA);
						for(int i = 0 ; i < array.length(); i++){
							registro = array.getJSONObject(i);
							values = new ContentValues();
							values.put(Actividad.COLUMN_ID_CLIENTE, 			registro.getInt(Actividad.COLUMN_ID_CLIENTE));
							values.put(Actividad.COLUMN_ID_PROYECTO, 			registro.getInt(Actividad.COLUMN_ID_PROYECTO));
							values.put(Actividad.COLUMN_ID_CONSTRUCCION, 		registro.getInt(Actividad.COLUMN_ID_CONSTRUCCION));
							values.put(Actividad.COLUMN_ID, 					registro.getInt(Actividad.COLUMN_ID));
							values.put(Actividad.COLUMN_ID_ASIGNACION_UNIDAD, 	registro.getInt(Actividad.COLUMN_ID_ASIGNACION_UNIDAD));
							values.put(Actividad.COLUMN_COD_INSTITUCIONAL,		registro.getString(Actividad.COLUMN_COD_INSTITUCIONAL));
							//values.put(Actividad.COLUMN_DESCRIPCION, 			registro.getString(Actividad.COLUMN_DESCRIPCION));
							values.put(Actividad.COLUMN_CANTIDAD_CONTRATADA, 	registro.getDouble(Actividad.COLUMN_CANTIDAD_CONTRATADA));
							values.put(Actividad.COLUMN_CANTIDAD_EJECUTADA, 	registro.getDouble(Actividad.COLUMN_CANTIDAD_EJECUTADA));
							values.put(Actividad.COLUMN_PORC_AVANCE, 			registro.getDouble(Actividad.COLUMN_PORC_AVANCE));
							values.put(Actividad.COLUMN_FECHA_ACTUALIZACION, 	registro.getString(Actividad.COLUMN_FECHA_ACTUALIZACION));


							
							if (w.insertRow(Actividad.NOMBRE_TABLA, values)<0) 
								throw new EmptyStackException();
							
							values.clear();
					    }
					
					Log.w("RecepcionDatosApi", "Fin incersion ACTIVIDADES");



					//9. Obtener datos para la tabla Construcciones
					array = datosJSON.getJSONArray(Construccion.NOMBRE_TABLA);
					for(int i = 0 ; i < array.length(); i++){
						registro = array.getJSONObject(i);
						values = new ContentValues();
						values.put(Construccion.COLUMN_ID_CLIENTE, 			    registro.getInt(Construccion.COLUMN_ID_CLIENTE));
						values.put(Construccion.COLUMN_ID_PROYECTO, 			registro.getInt(Construccion.COLUMN_ID_PROYECTO));
						values.put(Construccion.COLUMN_ID_CONSTRUCCION, 	    registro.getInt(Construccion.COLUMN_ID_CONSTRUCCION));
						values.put(Construccion.COLUMN_DESCRIPCION,		        registro.getString(Construccion.COLUMN_DESCRIPCION));
						values.put(Construccion.COLUMN_LATITUD, 	            registro.getDouble(Construccion.COLUMN_LATITUD));
						values.put(Construccion.COLUMN_LONGITUD, 			    registro.getDouble(Construccion.COLUMN_LONGITUD));
						values.put(Construccion.COLUMN_ALTITUD, 	            registro.getDouble(Construccion.COLUMN_ALTITUD));
						values.put(Construccion.COLUMN_MONTO, 			        registro.getDouble(Construccion.COLUMN_MONTO));
						values.put(Construccion.COLUMN_PORC_AVANCE, 			registro.getDouble(Construccion.COLUMN_PORC_AVANCE));

						if (w.insertRow(Construccion.NOMBRE_TABLA, values)<0)
							throw new EmptyStackException();

						values.clear();
					}

					Log.w("RecepcionDatosApi", "Fin incersion CONSTRUCCIONES");

					//10. Obtener datos para la tabla Actividad Descripcion
					array = datosJSON.getJSONArray(ActividadDescripcion.NOMBRE_TABLA);
					for(int i = 0 ; i < array.length(); i++){
						registro = array.getJSONObject(i);
						values = new ContentValues();
						values.put(ActividadDescripcion.COLUMN_ID_CLIENTE, 			    registro.getInt(ActividadDescripcion.COLUMN_ID_CLIENTE));
						values.put(ActividadDescripcion.COLUMN_ID_PROYECTO, 			registro.getInt(ActividadDescripcion.COLUMN_ID_PROYECTO));
						values.put(ActividadDescripcion.COLUMN_ID, 						registro.getInt(ActividadDescripcion.COLUMN_ID));
						values.put(ActividadDescripcion.COLUMN_DESCRIPCION, 			registro.getString(ActividadDescripcion.COLUMN_DESCRIPCION));

						if (w.insertRow(ActividadDescripcion.NOMBRE_TABLA, values)<0)
							throw new EmptyStackException();

						values.clear();
					}

					Log.w("RecepcionDatosApi", "Fin incersion DESCRIPCION ACTIVIDADES");

					resultado=true;
					w.finalizarTransaccion(true);
					//Establecer el estado de la sincronización a true
					AppValues.actualizarAppValueSync(ctx, true);	
					
	            }
	            else
	            {
	            	w.finalizarTransaccion(false);
	            	//w.cerrarDb();
	            	return false;
	            }
				
			} catch (Exception e) {
				resultado=false;
				w.finalizarTransaccion(false);
				ManejoErrores.registrarError(this.ctx, e,
										   RecepcionDatosAPI.class.getSimpleName(), "InicializarBD",
										   peticion, respuesta);
			}
			finally{
				w.finalizarTransaccion(false);
				//w.cerrarDb();
			}
		} catch (Exception e) {
			resultado=false;
			ManejoErrores.registrarError(this.ctx, e,
									   RecepcionDatosAPI.class.getSimpleName(), "InicializarBD",
									   peticion, respuesta);
		}
		
		return resultado;
		
	}
	
	

	
}
