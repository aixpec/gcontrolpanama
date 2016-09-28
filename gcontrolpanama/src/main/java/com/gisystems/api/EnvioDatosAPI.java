package com.gisystems.api;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.gisystems.gcontrolpanama.models.ActividadAvance;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.FotoActividad;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion_Respuesta;
import com.gisystems.utils.Base64;
import com.gisystems.utils.Utilitarios;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class EnvioDatosAPI {

	private Context context;

	private String userName = ""; 
	private String password = "";
	private String nombreArchivoAssembly = "Gisystems.Elephant.BLL.dll"; 
	private String namespaceClase = "Gisystems.Elephant.BLL";
	private String nombreClase = "ApiAppMovil"; 
	private String metodoEjecutara = "";
	private String pathLog = ""; 
	//private String TAG=EnvioDatosAPI.class.getSimpleName();

    BusinessCloud businessCloud = new BusinessCloud();

	public EnvioDatosAPI(Context context) {
		this.context = context;	
		//Obtener datos globales
		ObtenerDatosGlobales();
	}

    public boolean Enviar_Registro_Log_Errores(String mensaje_error, String stack_trace,
                                               String nombre_clase, String nombre_metodo,
                                               String json_peticion, String json_respuesta) {
        boolean resultado = false;
        try {
            String accion = "I";
            String id_usuario = this.userName;
            String id_disp_movil =  Utilitarios.ObtenerAndroid_ID(this.context);
            long corr_error = 0;
            //1. Obtener la petición para la capa WSL de la arquitectura
            ArrayList<Object> parametros = new ArrayList<Object>();
            parametros.add(accion);
            parametros.add(id_usuario );
            parametros.add(id_disp_movil );
            parametros.add(corr_error);
            parametros.add(mensaje_error );
            parametros.add(stack_trace);
            parametros.add(nombre_clase );
            parametros.add(nombre_metodo);
            parametros.add(json_peticion );
            parametros.add(json_respuesta);
            metodoEjecutara = "Actualizar_Log_Errores";
            //2. Enviar datos de secciones de trabajo actualizadas y obtener respuesta
            PeticionWSL peticion = Utilitarios.ObtenerPeticionWSL(context,
                    Utilitarios.TipoFuncion.ejecutarMetodo,
                    this.userName, this.password, parametros, this.nombreArchivoAssembly, this.namespaceClase,
                    this.nombreClase, this.metodoEjecutara, this.pathLog);
            RespuestaWSL respuesta = null;
            respuesta = businessCloud.sendRequestWSL(context, peticion);
            resultado = (respuesta.getEjecutadoSinError());
        } catch (Exception e) {
            Log.w(ManejoErrores.LOG_TAG, "Error en Enviar_Registro_Log_Errores. " + e.getStackTrace());
        }
        return resultado;
    }
		
	public boolean EnviarAvanceHistorico(ActividadAvance Avance) {
				int idAntiguedad = -1;
				boolean valor=false;
				try {
					if(Utilitarios.isConnectionAvailable(context)){
						String id_usuario = this.userName; 
						String id_disp_movil =  Utilitarios.ObtenerAndroid_ID(this.context); 
						 //1. Obtener la petici�n para la capa WSL de la arquitectura
			 			ArrayList<Object> parametros = new ArrayList<Object>();
						parametros.add(Avance.getIdCliente());
						parametros.add(Avance.getIdProyecto() );
						parametros.add(Avance.getIdConstruccion() );
						parametros.add(Avance.getIdActividad());
						parametros.add(Avance.getMontoNuevoAvance() );
						parametros.add(Avance.getComentario());
						parametros.add(id_usuario );
						parametros.add(id_disp_movil );
						parametros.add(Avance.getIdTemporal());
						metodoEjecutara = "fCrearAvanceHistorico";
						//2. Enviar datos del avance y obtener el ID como respuesta
						PeticionWSL peticion = Utilitarios.ObtenerPeticionWSL(context, 
						Utilitarios.TipoFuncion.ejecutarMetodo, 
						this.userName, this.password, parametros, this.nombreArchivoAssembly, this.namespaceClase, 
						this.nombreClase, this.metodoEjecutara, this.pathLog);
						RespuestaWSL respuesta = null;
						respuesta = businessCloud.sendRequestWSL(context, peticion);
						
						//Validar el ID obtenido
						idAntiguedad=(respuesta.getEjecutadoSinError())
								?Integer.valueOf(respuesta.getParametros()):-1;
						
						if (idAntiguedad>0){		
							Avance.setIdAntiguedad(idAntiguedad);
							valor=Avance.ActualizarIdAntiguedadAvance(context);
						}
						else{
							Avance.ActualizarEdoAvanceAPendienteConfirmar(context);
							valor=false;
						}
					}
				} catch (Exception e) {
					valor=false;
					Log.w(ManejoErrores.LOG_TAG, "Error en EnviarAvanceHistorico. " + e.getMessage());
				}
				
				return valor;
				
		}
	
	private void ObtenerDatosGlobales() {
		this.userName=AppValues.SharedPref_obtenerUsuarioNombre(this.context);
		this.password=AppValues.SharedPref_obtenerUsuarioPassword(this.context);
	}
	
	public boolean Enviar_FotografiaAvance(FotoActividad foto) {
		RespuestaWSL respuesta = null;
		boolean resultado=false;
		String androidID;
		try {		
			//1. Obtener los valores de identificación del teléfono
			androidID =  Utilitarios.ObtenerAndroid_ID(context);
			
			//2. Obtener la petición para la capa WSL de la arquitectura
			ArrayList<Object> parametros = new ArrayList<Object>(); 	

			File f = new File(foto.getRutaFoto());
			//si la imagen no es nula
			if (foto.getRutaFoto().compareTo ("null") != 0 && f.exists())
			{
				Bitmap bmp = Utilitarios.redimensionarImagen(foto.getRutaFoto());
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
				byte[] byteArray = stream.toByteArray();
				bmp.recycle();
				bmp = null;
				stream = null;
				f = null;				
				foto.setStringImagen(Base64.encodeBytes(byteArray));
				byteArray = null;
			}
			
			parametros.add(foto.getIdCliente());
			parametros.add(foto.getIdProyecto());
			parametros.add(foto.getIdConstruccion());
			parametros.add(foto.getIdActividad());
			parametros.add(foto.getIdAntiguedad());
			parametros.add(foto.getComentario());
			parametros.add(foto.getFechaCaptura());
			parametros.add(androidID);
			parametros.add(foto.getLatitud());
			parametros.add(foto.getLongitud());
			parametros.add(foto.getAltitud());
			parametros.add(userName);
			parametros.add(foto.getRutaFoto());
			parametros.add(foto.getStringImagen());
			
			Log.w("Parametros",parametros.toString());
			
			String metodoEjecutara = "GuardarFotografiaActividadAvance";
			String pathLog = ""; 
			PeticionWSL peticion = Utilitarios.ObtenerPeticionWSL(context, 
					Utilitarios.TipoFuncion.ejecutarMetodo, 
					this.userName, this.password, parametros, nombreArchivoAssembly, namespaceClase, 
					nombreClase, metodoEjecutara, pathLog);
			
			//3. Enviar petición. Conexión al API REST de la capa WSL
            respuesta = businessCloud.sendRequestWSL(context, peticion);
          
			//4. Actualizar datos en BD local SQLite
            
            
            
			if (respuesta.getEjecutadoSinError()) {
				Log.w(ManejoErrores.LOG_TAG, "Se envió la fotografía " + 
			          foto.getRutaFoto() + ". " + String.valueOf(respuesta.getParametros()));
				 resultado=true;
			}
			else
			{
				Log.w(ManejoErrores.LOG_TAG, "No se pudo enviar la fotografía " + 
				          foto.getRutaFoto() + ". " + String.valueOf(respuesta.getMotivoFallo()));
				 resultado=false;
			}
			
		} catch (Exception e) {
			resultado=false;
			ManejoErrores.registrarError(this.context, e,
					   EnvioDatosAPI.class.getSimpleName(), 
					   "Enviar_FotografiaArchivo",
					   null, null);
		}
		return resultado;
	}
	
	
	public boolean Enviar_FotografiaSinAvance(FotoActividad foto) {
		RespuestaWSL respuesta = null;
		boolean resultado=false;
		String androidID;
		try {
			
			//1. Obtener los valores de identificación del teléfono
			androidID =  Utilitarios.ObtenerAndroid_ID(context);
			
			//2. Obtener la petición para la capa WSL de la arquitectura
			ArrayList<Object> parametros = new ArrayList<Object>(); 	

			File f = new File(foto.getRutaFoto());
			//si la imagen no es nula
			if (foto.getRutaFoto().compareTo ("null") != 0 && f.exists())
			{
				Bitmap bmp = Utilitarios.redimensionarImagen(foto.getRutaFoto());
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
				byte[] byteArray = stream.toByteArray();
				foto.setStringImagen(Base64.encodeBytes(byteArray));
				bmp.recycle();
				bmp = null;
				stream = null;
				f = null;
				byteArray = null;
			}
			
			parametros.add(foto.getIdCliente());
			parametros.add(foto.getIdProyecto());
			parametros.add(foto.getIdConstruccion());
			parametros.add(foto.getIdActividad());
			parametros.add(foto.getComentario());
			parametros.add(foto.getFechaCaptura());
			parametros.add(androidID);
			parametros.add(foto.getLatitud());
			parametros.add(foto.getLongitud());
			parametros.add(foto.getAltitud());
			parametros.add(userName);
			parametros.add(foto.getRutaFoto());
			parametros.add(foto.getStringImagen());
			
			Log.w("Parametros",parametros.toString());
			
			String metodoEjecutara = "GuardarFotografiaActividadSinAvance";
			String pathLog = ""; 
			PeticionWSL peticion = Utilitarios.ObtenerPeticionWSL(context, 
					Utilitarios.TipoFuncion.ejecutarMetodo, 
					this.userName, this.password, parametros, nombreArchivoAssembly, namespaceClase, 
					nombreClase, metodoEjecutara, pathLog);
			
			//3. Enviar petición. Conexión al API REST de la capa WSL
            respuesta = businessCloud.sendRequestWSL(context, peticion);
          
			//4. Actualizar datos en BD local SQLite
			if (respuesta.getEjecutadoSinError()) {
				Log.w(ManejoErrores.LOG_TAG, "Se envió la fotografía " + 
			          foto.getRutaFoto() + ". " + String.valueOf(respuesta.getParametros()));
				 resultado=true;
			}
			else
			{
				Log.w(ManejoErrores.LOG_TAG, "No se pudo enviar la fotografía " + 
				          foto.getRutaFoto() + ". " + String.valueOf(respuesta.getMotivoFallo()));
				 resultado=false;
			}
			
		} catch (Exception e) {
			resultado=false;
			ManejoErrores.registrarError(this.context, e,
					   EnvioDatosAPI.class.getSimpleName(), 
					   "Enviar_FotografiaDeActividadSinAvance",
					   null, null);
		}
		return resultado;
	}
	
	/*
	public boolean Enviar_FotografiaDeActividadSinAvance(FotoActividad foto) {
		RespuestaWSL respuesta = null;
		boolean resultado=false;
		String androidID;
		try {
			
			//1. Obtener los valores de identificación del teléfono
			androidID =  Utilitarios.ObtenerAndroid_ID(context);
			
			//2. Obtener la petición para la capa WSL de la arquitectura
			ArrayList<Object> parametros = new ArrayList<Object>(); 	

			File f = new File(foto.getRutaFoto());
			//si la imagen no es nula
			if (foto.getRutaFoto().compareTo ("null") != 0 && f.exists())
			{
				Bitmap bmp = Utilitarios.redimensionarImagen(foto.getRutaFoto());
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
				byte[] byteArray = stream.toByteArray();
				bmp.recycle();
				bmp = null;
				stream = null;
				f = null;				
				foto.setStringImagen(Base64.encodeBytes(byteArray));
				byteArray = null;
			}
		
			
			parametros.add(foto.getIdCliente());
			parametros.add(foto.getIdProyecto());
			parametros.add(foto.getIdConstruccion());
			parametros.add(foto.getIdActividad());
			parametros.add(foto.getComentario());
			parametros.add(foto.getFechaCaptura());
			parametros.add(androidID);
			parametros.add(foto.getLatitud());
			parametros.add(foto.getLongitud());
			parametros.add(foto.getAltitud());
			parametros.add(userName);
			parametros.add(foto.getRutaFoto());
			parametros.add(foto.getStringImagen());
			
			Log.w("Parametros",parametros.toString());
			
			String metodoEjecutara = "GuardarFotografiaActividadSinAvance";
			String pathLog = ""; 
			PeticionWSL peticion = Utilitarios.ObtenerPeticionWSL(context, 
					Utilitarios.TipoFuncion.ejecutarMetodo, 
					this.userName, this.password, parametros, nombreArchivoAssembly, namespaceClase, 
					nombreClase, metodoEjecutara, pathLog);
			
			//3. Enviar petición. Conexión al API REST de la capa WSL
            respuesta = BusinessCloud.sendRequestWSL(context, peticion);
          
			//4. Actualizar datos en BD local SQLite
            

			if (respuesta.getEjecutadoSinError()) {
				Log.w(ManejoErrores.LOG_TAG, "Se envió la fotografía " + 
			          foto.getRutaFoto() + ". " + String.valueOf(respuesta.getParametros()));
				 resultado=true;
			}
			else
			{
				Log.w(ManejoErrores.LOG_TAG, "No se pudo enviar la fotografía " + 
				          foto.getRutaFoto() + ". " + String.valueOf(respuesta.getMotivoFallo()));
				 resultado=false;
			}
			
		} catch (Exception e) {
			resultado=false;
			ManejoErrores.registrarError(this.context, e,
					   EnvioDatosAPI.class.getSimpleName(), 
					   "Enviar_FotografiaDeActividadSinAvance",
					   null, null);
		}
		return resultado;
	}
	*/

	public boolean EnviarListaVerificacion(ListaVerificacion lista) {
		int IdListaVerificacion;
		boolean valor=false;
		try {
			if(Utilitarios.isConnectionAvailable(context)){
				String id_usuario = this.userName;
				String id_disp_movil =  Utilitarios.ObtenerAndroid_ID(this.context);
				//1. Obtener la petici�n para la capa WSL de la arquitectura
				ArrayList<Object> parametros = new ArrayList<Object>();
				parametros.add(lista.getIdCliente());
				parametros.add(lista.getIdProyecto() );
				parametros.add(lista.getIdTipoListaVerificacion() );
				parametros.add(id_usuario );
				parametros.add(id_disp_movil );
				metodoEjecutara = "fCrearListaVerificacion";
				//2. Enviar datos del avance y obtener el ID como respuesta
				PeticionWSL peticion = Utilitarios.ObtenerPeticionWSL(context,
						Utilitarios.TipoFuncion.ejecutarMetodo,
						this.userName, this.password, parametros, this.nombreArchivoAssembly, this.namespaceClase,
						this.nombreClase, this.metodoEjecutara, this.pathLog);
				RespuestaWSL respuesta = null;
				respuesta = businessCloud.sendRequestWSL(context, peticion);

				//Validar el ID obtenido
                IdListaVerificacion=(respuesta.getEjecutadoSinError())
						?Integer.valueOf(respuesta.getParametros()):-1;

				if (lista.getIdListaVerificacion()<0){
                    valor=lista.ActualizarIdListaVerificacion(context, IdListaVerificacion);
				}
				else{
					valor=false;
				}
			}
		} catch (Exception e) {
			valor=false;
			Log.w(ManejoErrores.LOG_TAG, "Error en EnviarListaVerificacion. " + e.getMessage());
		}
		return valor;
	}


    public boolean EnviarListaVerificacionRespuesta(ListaVerificacion_Respuesta resp) {
        boolean valor=false;
        try {
            if(Utilitarios.isConnectionAvailable(context)){
                String id_usuario = this.userName;
                String id_disp_movil =  Utilitarios.ObtenerAndroid_ID(this.context);
                //1. Obtener la petici�n para la capa WSL de la arquitectura
                ArrayList<Object> parametros = new ArrayList<>();
                parametros.add(resp.getIdCliente());
                parametros.add(resp.getIdListaVerificacion() );
                parametros.add(resp.getIdConfiguracion() );
                parametros.add(resp.getIdIndicador());
                parametros.add(resp.getIdPregunta() );
                parametros.add(resp.getIndicador());
                parametros.add(resp.getPregunta());
                parametros.add(resp.getIdRespuesta());
                parametros.add(resp.getDescripcionRespuesta());
                parametros.add(resp.getValorRespuesta());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                parametros.add(sdf.format(resp.getFechaCaptura()));
                parametros.add(id_usuario );
                parametros.add(id_disp_movil );
                metodoEjecutara = "fCrearListaVerificacionRespuesta";
                //2. Enviar datos del avance y obtener el ID como respuesta
                PeticionWSL peticion = Utilitarios.ObtenerPeticionWSL(context,
                        Utilitarios.TipoFuncion.ejecutarMetodo,
                        this.userName, this.password, parametros, this.nombreArchivoAssembly, this.namespaceClase,
                        this.nombreClase, this.metodoEjecutara, this.pathLog);
                RespuestaWSL respuesta = null;
                respuesta = businessCloud.sendRequestWSL(context, peticion);

                //Validar el ID obtenido
                int filasAfectadas=(respuesta.getEjecutadoSinError())
                        ?Integer.valueOf(respuesta.getParametros()):-1;

                if (filasAfectadas > 0){
                    valor=resp.ActualizarEstadoListaVerificacionRespuesta(context);
                }
                else{
                    resp.ActualizarEstadoRegistro(context, AppValues.EstadosEnvio.No_Enviado);
                    valor=false;
                }
            }
        } catch (Exception e) {
            valor=false;
            Log.w(ManejoErrores.LOG_TAG, "Error en EnviarListaVerificacionRespuesta. " + e.getMessage());
        }
        return valor;
    }


}
