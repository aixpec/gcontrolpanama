package com.gisystems.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gisystems.gcontrolpanama.models.ActividadDescripcion;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.gcontrolpanama.models.cc.AccionRespuesta;
import com.gisystems.gcontrolpanama.models.cc.ClaseIndicador;
import com.gisystems.gcontrolpanama.models.cc.Configuracion;
import com.gisystems.gcontrolpanama.models.cc.Indicador;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.Respuesta;
import com.gisystems.gcontrolpanama.models.cc.RespuestaAccionDetalle;
import com.gisystems.gcontrolpanama.models.cc.TipoDato;
import com.gisystems.gcontrolpanama.models.cc.TipoIndicador;
import com.gisystems.gcontrolpanama.models.chk.EstadoListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion_Respuesta;
import com.gisystems.gcontrolpanama.models.chk.TipoListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.TipoListaVerificacion_Seccion;
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
		boolean resultado;
		PeticionWSL peticion = null;
		RespuestaWSL respuesta = null;
		miExcepcion=new MiExcepcion();
        BusinessCloud businessCloud = new BusinessCloud();
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
            respuesta = businessCloud.sendRequestWSL(ctx, peticion);
            try{
	            //Iniciar la transacción
	            w=new DAL(ctx);
	            w.iniciarTransaccion();
	            ContentValues values;
	            
	            if (respuesta.getEjecutadoSinError()) {
                    EliminarDatosDeLaBD(w);

	            	String datosObtenidos = "";
					datosObtenidos = respuesta.getParametros();

                    datosJSON =  new JSONObject(datosObtenidos);

					//Validar que existan en el JSON las entidades a almacenar
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

                    resultado = InicializarBD_Parte2(w);

					w.finalizarTransaccion(resultado);

                    if (resultado) {
                        //Establecer el estado de la sincronización a true
                        AppValues.actualizarAppValueSync(ctx, true);
                    }
	            }
	            else
	            {
	            	w.finalizarTransaccion(false);
	            	resultado = false;
	            }

			} catch (Exception e) {
				resultado=false;
				w.finalizarTransaccion(false);
				ManejoErrores.registrarError(this.ctx, e,
										   RecepcionDatosAPI.class.getSimpleName(), "InicializarBD",
										   peticion, respuesta);
			}
		} catch (Exception e) {
			resultado=false;
			ManejoErrores.registrarError(this.ctx, e,
									   RecepcionDatosAPI.class.getSimpleName(), "InicializarBD",
									   peticion, respuesta);
		}
		
		return resultado;
	}

    private void EliminarDatosDeLaBD(DAL w)  {
        // Tablas para checklists
        w.deleteRow(ListaVerificacion_Respuesta.NOMBRE_TABLA, null);
        w.deleteRow(ListaVerificacion.NOMBRE_TABLA, null);
        w.deleteRow(TipoListaVerificacion_Seccion.NOMBRE_TABLA, null);
        w.deleteRow(TipoListaVerificacion.NOMBRE_TABLA, null);
        w.deleteRow(EstadoListaVerificacion.NOMBRE_TABLA, null);
        // Tablas de configuraciones de preguntas / respuestas
        w.deleteRow(RespuestaAccionDetalle.NOMBRE_TABLA, null);
        w.deleteRow(Respuesta.NOMBRE_TABLA, null);
        w.deleteRow(Pregunta.NOMBRE_TABLA, null);
        w.deleteRow(Indicador.NOMBRE_TABLA, null);
        w.deleteRow(Configuracion.NOMBRE_TABLA, null);
        w.deleteRow(AccionRespuesta.NOMBRE_TABLA, null);
        w.deleteRow(TipoDato.NOMBRE_TABLA, null);
        w.deleteRow(TipoIndicador.NOMBRE_TABLA, null);
        w.deleteRow(ClaseIndicador.NOMBRE_TABLA, null);
        // Tablas de proyectos y actividades
        w.deleteRow(ActividadDescripcion.NOMBRE_TABLA, null);
        w.deleteRow(Construccion.NOMBRE_TABLA, null);
        w.deleteRow(Actividad.NOMBRE_TABLA, null);
        w.deleteRow(TipoUnidad.NOMBRE_TABLA, null);
        w.deleteRow(Proyecto.NOMBRE_TABLA, null);
        w.deleteRow(EstadoProyecto.NOMBRE_TABLA, null);
        w.deleteRow(TipoProyecto.NOMBRE_TABLA, null);
        w.deleteRow(Cliente.NOMBRE_TABLA, null);
    }

    private boolean InicializarBD_Parte2(DAL w)  {
        boolean resultado;
        PeticionWSL peticion = null;
        RespuestaWSL respuesta = null;
        miExcepcion=new MiExcepcion();
        BusinessCloud businessCloud = new BusinessCloud();
        try {

            //1. Obtener la petición para la capa WSL de la arquitectura
            ArrayList<Object> parametros = new ArrayList<>();
            parametros.add(this.userName);
            String nombreArchivoAssembly = "Gisystems.Elephant.BLL.dll";
            String namespaceClase = "Gisystems.Elephant.BLL";
            String nombreClase = "ApiAppMovil";
            String metodoEjecutara = "ObtenerDatosInicializacionBD_Parte2";
            String pathLog = "";
            peticion = Utilitarios.ObtenerPeticionWSL(ctx,
                    Utilitarios.TipoFuncion.ejecutarMetodo,
                    this.userName, this.password, parametros, nombreArchivoAssembly, namespaceClase,
                    nombreClase, metodoEjecutara, pathLog);

            //2. Enviar petición. Conexión al API REST de la capa WSL
            JSONObject datosJSON;
            JSONArray array;
            JSONObject registro;
            respuesta = businessCloud.sendRequestWSL(ctx, peticion);
            try{
                ContentValues values;

                if (respuesta.getEjecutadoSinError()) {

                    String datosObtenidos = "";
                    datosObtenidos = respuesta.getParametros();
                    datosJSON = new JSONObject(datosObtenidos);

                    //Validar que existan en el JSON las entidades a almacenar
                    if( !(// Tablas de configuraciones de preguntas / respuestas
                          datosJSON.has(ClaseIndicador.NOMBRE_TABLA)
                            && datosJSON.has(TipoIndicador.NOMBRE_TABLA)
                            && datosJSON.has(TipoDato.NOMBRE_TABLA)
                            && datosJSON.has(AccionRespuesta.NOMBRE_TABLA)
                            && datosJSON.has(Configuracion.NOMBRE_TABLA)
                            && datosJSON.has(Indicador.NOMBRE_TABLA)
                            && datosJSON.has(Pregunta.NOMBRE_TABLA)
                            && datosJSON.has(Respuesta.NOMBRE_TABLA)
                            && datosJSON.has(RespuestaAccionDetalle.NOMBRE_TABLA)
                            // Tablas para checklists
                            && datosJSON.has(TipoListaVerificacion.NOMBRE_TABLA)
                            && datosJSON.has(TipoListaVerificacion_Seccion.NOMBRE_TABLA)
                            && datosJSON.has(ListaVerificacion.NOMBRE_TABLA)
                            && datosJSON.has(ListaVerificacion_Respuesta.NOMBRE_TABLA))) {
                        w.finalizarTransaccion(false);
                        return false;}

                    // ********************************************************************
                    // ********* Tablas de configuraciones de preguntas / respuestas ******
                    // ********************************************************************

                    //3. Obtener datos para la tabla Clases de Indicador
                    array = datosJSON.getJSONArray(ClaseIndicador.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(ClaseIndicador.COLUMN_ID_CLASE_INDICADOR, 	registro.getInt(ClaseIndicador.COLUMN_ID_CLASE_INDICADOR));
                        values.put(ClaseIndicador.COLUMN_NOMBRE, 			registro.getString(ClaseIndicador.COLUMN_NOMBRE));

                        if (w.insertRow(ClaseIndicador.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción CLASES DE INDICADOR");

                    //4. Obtener datos para la tabla Tipos de Indicador
                    array = datosJSON.getJSONArray(TipoIndicador.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(TipoIndicador.COLUMN_ID_TIPO_INDICADOR, 	registro.getInt(TipoIndicador.COLUMN_ID_TIPO_INDICADOR));
                        values.put(TipoIndicador.COLUMN_DESCRIPCION, 		registro.getString(TipoIndicador.COLUMN_DESCRIPCION));

                        if (w.insertRow(TipoIndicador.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción TIPOS DE INDICADOR");

                    //5. Obtener datos para la tabla Tipos de Dato
                    array = datosJSON.getJSONArray(TipoDato.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(TipoDato.COLUMN_ID_TIPO_DATO, 	registro.getInt(TipoDato.COLUMN_ID_TIPO_DATO));
                        values.put(TipoDato.COLUMN_NOMBRE, 	registro.getString(TipoDato.COLUMN_NOMBRE));

                        if (w.insertRow(TipoDato.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción TIPOS DE DATO");

                    //6. Obtener datos para la tabla Acciones por Respuesta
                    array = datosJSON.getJSONArray(AccionRespuesta.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(AccionRespuesta.COLUMN_ID_ACCION_RESPUESTA, 	registro.getInt(AccionRespuesta.COLUMN_ID_ACCION_RESPUESTA));
                        values.put(AccionRespuesta.COLUMN_NOMBRE_ACCION_RESPUESTA, 	registro.getString(AccionRespuesta.COLUMN_NOMBRE_ACCION_RESPUESTA));
                        values.put(AccionRespuesta.COLUMN_DESC_ACCION_RESPUESTA, 	registro.getString(AccionRespuesta.COLUMN_DESC_ACCION_RESPUESTA));

                        if (w.insertRow(AccionRespuesta.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción ACCIONES POR RESPUESTA");

                    //7. Obtener datos para la tabla de Configuraciones
                    array = datosJSON.getJSONArray(Configuracion.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(Configuracion.COLUMN_ID_CLIENTE, 	registro.getInt(Configuracion.COLUMN_ID_CLIENTE));
                        values.put(Configuracion.COLUMN_ID_CONFIGURACION, 	registro.getInt(Configuracion.COLUMN_ID_CONFIGURACION));
                        values.put(Configuracion.COLUMN_DESCRIPCION, 	registro.getString(Configuracion.COLUMN_DESCRIPCION));

                        if (w.insertRow(Configuracion.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción CONFIGURACIONES");

                    //8. Obtener datos para la tabla de Indicadores
                    array = datosJSON.getJSONArray(Indicador.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(Indicador.COLUMN_ID_CLIENTE, 	registro.getInt(Indicador.COLUMN_ID_CLIENTE));
                        values.put(Indicador.COLUMN_ID_CONFIGURACION, 	registro.getInt(Indicador.COLUMN_ID_CONFIGURACION));
                        values.put(Indicador.COLUMN_ID_INDICADOR, 	registro.getInt(Indicador.COLUMN_ID_INDICADOR));
                        values.put(Indicador.COLUMN_ID_CLASE_INDICADOR, 	registro.getInt(Indicador.COLUMN_ID_CLASE_INDICADOR));
                        values.put(Indicador.COLUMN_ID_TIPO_INDICADOR, 	registro.getInt(Indicador.COLUMN_ID_TIPO_INDICADOR));
                        values.put(Indicador.COLUMN_DESCRIPCION, 	registro.getString(Indicador.COLUMN_DESCRIPCION));
                        values.put(Indicador.COLUMN_REQUERIDO, 	registro.getInt(Indicador.COLUMN_REQUERIDO));

                        if (w.insertRow(Indicador.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción INDICADORES");

                    //9. Obtener datos para la tabla de Preguntas
                    array = datosJSON.getJSONArray(Pregunta.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(Pregunta.COLUMN_ID_CLIENTE, 	registro.getInt(Pregunta.COLUMN_ID_CLIENTE));
                        values.put(Pregunta.COLUMN_ID_CONFIGURACION, 	registro.getInt(Pregunta.COLUMN_ID_CONFIGURACION));
                        values.put(Pregunta.COLUMN_ID_INDICADOR, 	registro.getInt(Pregunta.COLUMN_ID_INDICADOR));
                        values.put(Pregunta.COLUMN_ID_PREGUNTA, 	registro.getInt(Pregunta.COLUMN_ID_PREGUNTA));
                        values.put(Pregunta.COLUMN_ID_TIPO_DATO, 	registro.getInt(Pregunta.COLUMN_ID_TIPO_DATO));
                        values.put(Pregunta.COLUMN_PREGUNTA, 	registro.getString(Pregunta.COLUMN_PREGUNTA));
                        values.put(Pregunta.COLUMN_REQUERIDO, 	registro.getInt(Pregunta.COLUMN_REQUERIDO));

                        if (w.insertRow(Pregunta.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción PREGUNTAS");

                    //10. Obtener datos para la tabla de Respuestas
                    array = datosJSON.getJSONArray(Respuesta.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(Respuesta.COLUMN_ID_CLIENTE, 	registro.getInt(Respuesta.COLUMN_ID_CLIENTE));
                        values.put(Respuesta.COLUMN_ID_CONFIGURACION, 	registro.getInt(Respuesta.COLUMN_ID_CONFIGURACION));
                        values.put(Respuesta.COLUMN_ID_INDICADOR, 	registro.getInt(Respuesta.COLUMN_ID_INDICADOR));
                        values.put(Respuesta.COLUMN_ID_PREGUNTA, 	registro.getInt(Respuesta.COLUMN_ID_PREGUNTA));
                        values.put(Respuesta.COLUMN_ID_RESPUESTA, 	registro.getInt(Respuesta.COLUMN_ID_RESPUESTA));
                        values.put(Respuesta.COLUMN_RESPUESTA, 	registro.getString(Respuesta.COLUMN_RESPUESTA));

                        if (w.insertRow(Respuesta.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción RESPUESTAS");

                    //11. Obtener datos para la tabla de Detalle de Acciones por Respuesta
                    array = datosJSON.getJSONArray(RespuestaAccionDetalle.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(RespuestaAccionDetalle.COLUMN_ID_CLIENTE, 	registro.getInt(RespuestaAccionDetalle.COLUMN_ID_CLIENTE));
                        values.put(RespuestaAccionDetalle.COLUMN_ID_CONFIGURACION, 	registro.getInt(RespuestaAccionDetalle.COLUMN_ID_CONFIGURACION));
                        values.put(RespuestaAccionDetalle.COLUMN_ID_INDICADOR, 	registro.getInt(RespuestaAccionDetalle.COLUMN_ID_INDICADOR));
                        values.put(RespuestaAccionDetalle.COLUMN_ID_PREGUNTA, 	registro.getInt(RespuestaAccionDetalle.COLUMN_ID_PREGUNTA));
                        values.put(RespuestaAccionDetalle.COLUMN_ID_RESPUESTA, 	registro.getInt(RespuestaAccionDetalle.COLUMN_ID_RESPUESTA));
                        values.put(RespuestaAccionDetalle.COLUMN_ID_RESPUESTA_ACCION_DETALLE_CORR, 	registro.getInt(RespuestaAccionDetalle.COLUMN_ID_RESPUESTA_ACCION_DETALLE_CORR));
                        values.put(RespuestaAccionDetalle.COLUMN_ID_ACCION_RESPUESTA, 	registro.getInt(RespuestaAccionDetalle.COLUMN_ID_ACCION_RESPUESTA));
                        values.put(RespuestaAccionDetalle.COLUMN_IR_A_PREGUNTA, 	registro.getString(RespuestaAccionDetalle.COLUMN_IR_A_PREGUNTA));
                        values.put(RespuestaAccionDetalle.COLUMN_DESHABILITAR_PREGUNTA_COD, 	registro.getInt(RespuestaAccionDetalle.COLUMN_DESHABILITAR_PREGUNTA_COD));
                        values.put(RespuestaAccionDetalle.COLUMN_HABILITAR_PREGUNTA_COD, 	registro.getInt(RespuestaAccionDetalle.COLUMN_HABILITAR_PREGUNTA_COD));
                        values.put(RespuestaAccionDetalle.COLUMN_CAMBIAR_LIMITE_PREGUNTA_COD, 	registro.getInt(RespuestaAccionDetalle.COLUMN_CAMBIAR_LIMITE_PREGUNTA_COD));
                        values.put(RespuestaAccionDetalle.COLUMN_COD_PREG_APLICAR_ACCION, 	registro.getInt(RespuestaAccionDetalle.COLUMN_COD_PREG_APLICAR_ACCION));
                        values.put(RespuestaAccionDetalle.COLUMN_COD_RESP_APLICAR_ACCION, 	registro.getInt(RespuestaAccionDetalle.COLUMN_COD_RESP_APLICAR_ACCION));

                        if (w.insertRow(RespuestaAccionDetalle.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción DETALLE DE ACCIONES POR RESPUESTA");

                    // ********************************************************************
                    // ********* Tablas para checklists ***********************************
                    // ********************************************************************
                    //12. Obtener datos para la tabla Estados de Listas de Verificación
                    array = datosJSON.getJSONArray(EstadoListaVerificacion.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(EstadoListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION, 	registro.getInt(EstadoListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION));
                        values.put(EstadoListaVerificacion.COLUMN_DESCRIPCION, 	registro.getString(EstadoListaVerificacion.COLUMN_DESCRIPCION));
                        values.put(EstadoListaVerificacion.COLUMN_LISTAR_EN_APP_MOVIL, 	registro.getInt(EstadoListaVerificacion.COLUMN_LISTAR_EN_APP_MOVIL));

                        if (w.insertRow(EstadoListaVerificacion.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción ESTADOS DE LISTAS DE VERIFICACION");

                    //13. Obtener datos para la tabla de Tipos de PreguntaListaUI de Verificación
                    array = datosJSON.getJSONArray(TipoListaVerificacion.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(TipoListaVerificacion.COLUMN_ID_CLIENTE, 	registro.getInt(TipoListaVerificacion.COLUMN_ID_CLIENTE));
                        values.put(TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION, 	registro.getInt(TipoListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION));
                        values.put(TipoListaVerificacion.COLUMN_ID_CONFIGURACION, 	registro.getInt(TipoListaVerificacion.COLUMN_ID_CONFIGURACION));
                        values.put(TipoListaVerificacion.COLUMN_DESCRIPCION, 	registro.getString(TipoListaVerificacion.COLUMN_DESCRIPCION));

                        if (w.insertRow(TipoListaVerificacion.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción TIPOS DE LISTA DE VERIFICACION");

                    //14. Obtener datos para la tabla de Secciones por Tipo de PreguntaListaUI de Verificación
                    array = datosJSON.getJSONArray(TipoListaVerificacion_Seccion.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE, 	registro.getInt(TipoListaVerificacion_Seccion.COLUMN_ID_CLIENTE));
                        values.put(TipoListaVerificacion_Seccion.COLUMN_ID_TIPO_LISTA_VERIFICACION, 	registro.getInt(TipoListaVerificacion_Seccion.COLUMN_ID_TIPO_LISTA_VERIFICACION));
                        values.put(TipoListaVerificacion_Seccion.COLUMN_ID_SECCION, 	registro.getInt(TipoListaVerificacion_Seccion.COLUMN_ID_SECCION));
                        values.put(TipoListaVerificacion_Seccion.COLUMN_ID_CONFIGURACION, 	registro.getInt(TipoListaVerificacion_Seccion.COLUMN_ID_CONFIGURACION));
                        values.put(TipoListaVerificacion_Seccion.COLUMN_ID_INDICADOR, 	registro.getInt(TipoListaVerificacion_Seccion.COLUMN_ID_INDICADOR));
                        values.put(TipoListaVerificacion_Seccion.COLUMN_NOMBRE, 	registro.getString(TipoListaVerificacion_Seccion.COLUMN_NOMBRE));
                        values.put(TipoListaVerificacion_Seccion.COLUMN_DE_DATOS_GENERALES, 	registro.getInt(TipoListaVerificacion_Seccion.COLUMN_DE_DATOS_GENERALES));
                        values.put(TipoListaVerificacion_Seccion.COLUMN_DE_OBSERVACIONES, 	registro.getInt(TipoListaVerificacion_Seccion.COLUMN_DE_OBSERVACIONES));

                        if (w.insertRow(TipoListaVerificacion_Seccion.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción SECCIONES POR TIPO DE LISTA DE VERIFICACION");

                    //15. Obtener datos para la tabla de Listas de Verificación
                    array = datosJSON.getJSONArray(ListaVerificacion.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(ListaVerificacion.COLUMN_ID_CLIENTE, 	registro.getInt(ListaVerificacion.COLUMN_ID_CLIENTE));
                        values.put(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION, 	registro.getInt(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION));
                        values.put(ListaVerificacion.COLUMN_ID_PROYECTO, 	registro.getInt(ListaVerificacion.COLUMN_ID_PROYECTO));
                        values.put(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION, 	registro.getInt(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION));
                        values.put(ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION, 	registro.getInt(ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION));
                        values.put(ListaVerificacion.COLUMN_LISTA_CERRADA, 	0);
                        values.put(ListaVerificacion.COLUMN_ESTADO_ENVIO, 	AppValues.EstadosEnvio.Enviado.name());
                        values.put(ListaVerificacion.COLUMN_CREO_USUARIO, 	registro.getString(ListaVerificacion.COLUMN_CREO_USUARIO));
                        values.put(ListaVerificacion.COLUMN_CREO_FECHA, 	registro.getString(ListaVerificacion.COLUMN_CREO_FECHA));

                        if (w.insertRow(ListaVerificacion.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción LISTAS DE VERIFICACION");

                    //16. Obtener datos para la tabla de Respuestas de las Listas de Verificación
                    array = datosJSON.getJSONArray(ListaVerificacion_Respuesta.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        values = new ContentValues();
                        values.put(ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE, 	registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE));
                        values.put(ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION, 	registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION));
                        values.put(ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION, 	registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION));
                        values.put(ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR, 	registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR));
                        values.put(ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA, 	registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA));
                        values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_INDICADOR, 	registro.getString(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_INDICADOR));
                        values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_PREGUNTA, 	registro.getString(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_PREGUNTA));
                        if (registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA) > 0)
                        {
                            values.put(ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA, 	registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA));
                        }
                        values.put(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA, 	registro.getString(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA));
                        values.put(ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA, 	registro.getString(ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA));
                        values.put(ListaVerificacion_Respuesta.COLUMN_ESTADO_ENVIO, 	AppValues.EstadosEnvio.Enviado.name());
                        values.put(ListaVerificacion_Respuesta.COLUMN_ELIMINADO, 0);
                        values.put(ListaVerificacion_Respuesta.COLUMN_CREO_USUARIO, 	registro.getString(ListaVerificacion_Respuesta.COLUMN_CREO_USUARIO));
                        values.put(ListaVerificacion_Respuesta.COLUMN_CREO_FECHA, 	registro.getString(ListaVerificacion_Respuesta.COLUMN_CREO_FECHA));

                        if (w.insertRow(ListaVerificacion_Respuesta.NOMBRE_TABLA, values)<0)
                            throw new EmptyStackException();

                        values.clear();
                    }

                    Log.w("RecepcionDatosApi", "Fin inserción RESPUESTAS DE LAS LISTAS DE VERIFICACION");

                    resultado=true;
                }
                else
                {
                    resultado= false;
                }

            } catch (Exception e) {
                resultado=false;
                ManejoErrores.registrarError(this.ctx, e,
                        RecepcionDatosAPI.class.getSimpleName(), "InicializarBD_Parte2",
                        peticion, respuesta);
            }
        } catch (Exception e) {
            resultado=false;
            ManejoErrores.registrarError(this.ctx, e,
                    RecepcionDatosAPI.class.getSimpleName(), "InicializarBD_Parte2",
                    peticion, respuesta);
        }
        return resultado;
    }

    public boolean ActualizarDatosEnBdLocal()  {
        boolean resultado;
        PeticionWSL peticion = null;
        RespuestaWSL respuesta = null;
        miExcepcion=new MiExcepcion();
        BusinessCloud businessCloud = new BusinessCloud();
        try {

            //1. Obtener la petición para la capa WSL de la arquitectura
            ArrayList<Object> parametros = new ArrayList<>();
            parametros.add(this.userName);
            String nombreArchivoAssembly = "Gisystems.Elephant.BLL.dll";
            String namespaceClase = "Gisystems.Elephant.BLL";
            String nombreClase = "ApiAppMovil";
            String metodoEjecutara = "prSincronizarAppPorUsuario_Actualizacion";
            String pathLog = "";
            peticion = Utilitarios.ObtenerPeticionWSL(ctx,
                    Utilitarios.TipoFuncion.ejecutarMetodo,
                    this.userName, this.password, parametros, nombreArchivoAssembly, namespaceClase,
                    nombreClase, metodoEjecutara, pathLog);

            //2. Enviar petición. Conexión al API REST de la capa WSL
            JSONObject datosJSON;
            JSONArray array;
            JSONObject registro;
            respuesta = businessCloud.sendRequestWSL(ctx, peticion);
            try{
                //3. Iniciar la transacción
                w=new DAL(ctx);
                w.iniciarTransaccion();

                if (respuesta.getEjecutadoSinError()) {
                    String datosObtenidos = "";
                    datosObtenidos = respuesta.getParametros();

                    datosJSON =  new JSONObject(datosObtenidos);

                    //4. Validar que existan en el JSON las entidades a almacenar
                    if( !(datosJSON.has(ListaVerificacion.NOMBRE_TABLA)
                       && datosJSON.has(ListaVerificacion_Respuesta.NOMBRE_TABLA))) {
                        w.finalizarTransaccion(false);
                        return false;}

                    //5. Obtener datos para la tabla de Listas de Verificación
                    ListaVerificacion.MarcarTodoComoNoConfirmado(ctx, w);

                    ListaVerificacion lista;
                    Calendar cal = Calendar.getInstance();
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH );
                    Date dateRepresentation;

                    array = datosJSON.getJSONArray(ListaVerificacion.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        lista = new ListaVerificacion();
                        lista.setIdCliente(registro.getInt(ListaVerificacion.COLUMN_ID_CLIENTE));
                        lista.setIdListaVerificacion(registro.getInt(ListaVerificacion.COLUMN_ID_LISTA_VERIFICACION));
                        lista.setIdProyecto(registro.getInt(ListaVerificacion.COLUMN_ID_PROYECTO));
                        lista.setIdTipoListaVerificacion(registro.getInt(ListaVerificacion.COLUMN_ID_TIPO_LISTA_VERIFICACION));
                        lista.setIdEstadoListaVerificacion(registro.getInt(ListaVerificacion.COLUMN_ID_ESTADO_LISTA_VERIFICACION));
                        lista.setListaCerrada(0);
                        lista.setCreoUsuario(registro.getString(ListaVerificacion.COLUMN_CREO_USUARIO));
                        cal.setTime(sdf.parse(registro.getString(ListaVerificacion.COLUMN_CREO_FECHA)));
                        dateRepresentation = cal.getTime();
                        lista.setCreoFecha(dateRepresentation);

                        lista.RegistrarListaRecibidaDelServidor(ctx, w);
                    }

                    ListaVerificacion.EliminarTodoLoNoConfirmado(ctx, w);

                    Log.w("RecepcionDatosApi", "Fin actualización LISTAS DE VERIFICACION");

                    //6. Obtener datos para la tabla de Respuestas de las Listas de Verificación
                    ListaVerificacion_Respuesta.MarcarTodoComoNoConfirmado(ctx);

                    ListaVerificacion_Respuesta resp;

                    array = datosJSON.getJSONArray(ListaVerificacion_Respuesta.NOMBRE_TABLA);
                    for(int i = 0 ; i < array.length(); i++){
                        registro = array.getJSONObject(i);
                        resp = new ListaVerificacion_Respuesta();

                        resp.setIdCliente(registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_CLIENTE));
                        resp.setIdListaVerificacion(registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_LISTA_VERIFICACION));
                        resp.setIdConfiguracion(registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_CONFIGURACION));
                        resp.setIdIndicador(registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_INDICADOR));
                        resp.setIdPregunta(registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_PREGUNTA));
                        resp.setIndicador(registro.getString(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_INDICADOR));
                        resp.setPregunta(registro.getString(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_PREGUNTA));
                        resp.setIdRespuesta(registro.getInt(ListaVerificacion_Respuesta.COLUMN_ID_RESPUESTA));
                        resp.setDescripcionRespuesta(registro.getString(ListaVerificacion_Respuesta.COLUMN_DESCRIPCION_RESPUESTA));
                        resp.setValorRespuesta(registro.getString(ListaVerificacion_Respuesta.COLUMN_VALOR_RESPUESTA));
                        resp.setCreoUsuario(registro.getString(ListaVerificacion_Respuesta.COLUMN_CREO_USUARIO));
                        cal.setTime(sdf.parse(registro.getString(ListaVerificacion_Respuesta.COLUMN_CREO_FECHA)));
                        dateRepresentation = cal.getTime();
                        resp.setCreoFecha(dateRepresentation);

                        resp.RegistrarRespuestaRecibidaDelServidor(ctx);
                    }

                    ListaVerificacion_Respuesta.EliminarTodoLoNoConfirmado(ctx);

                    Log.w("RecepcionDatosApi", "Fin actualización RESPUESTAS DE LAS LISTAS DE VERIFICACION");

                    resultado=true;

                    w.finalizarTransaccion(resultado);
                }
                else
                {
                    w.finalizarTransaccion(false);
                    resultado = false;
                }

            } catch (Exception e) {
                resultado=false;
                w.finalizarTransaccion(false);
                ManejoErrores.registrarError(this.ctx, e,
                        RecepcionDatosAPI.class.getSimpleName(), "ActualizarDatosEnBdLocal",
                        peticion, respuesta);
            }
        } catch (Exception e) {
            resultado=false;
            ManejoErrores.registrarError(this.ctx, e,
                    RecepcionDatosAPI.class.getSimpleName(), "ActualizarDatosEnBdLocal",
                    peticion, respuesta);
        }

        return resultado;
    }



}
