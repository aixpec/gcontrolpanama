package com.gisystems.exceptionhandling;

import java.io.PrintWriter;
import java.io.StringWriter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.api.PeticionWSL;
import com.gisystems.api.RespuestaWSL;
import com.gisystems.gcontrolpanama.models.RegistroError;
import com.gisystems.utils.Utilitarios;


public class ManejoErrores implements java.lang.Thread.UncaughtExceptionHandler {

	public static final String LOG_TAG = "AppElephant";
	
	private final Activity ctx;
    private final String LINE_SEPARATOR = "\n";

    public ManejoErrores(Activity context) {
        ctx = context;
    }
	
	public static void registrarError(Context ctx, Exception e,
								      String nombre_clase, String nombre_metodo,
								      PeticionWSL peticion, RespuestaWSL respuesta) {
		String peticionJSON = "";
		String datosObtenidos = "";
		if (peticion != null) {
			peticionJSON = peticion.toJSON();
		}
		if (respuesta != null) {
			datosObtenidos = respuesta.getParametros();
		}
		
		StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        
        registrarError(ctx, e.getMessage(), stackTrace.toString(),
				       nombre_clase, nombre_metodo, peticionJSON, datosObtenidos);
	}
	
	public static void registrarError(Context ctx, String mensajeError, String stackTrace,
		      						  String nombre_clase, String nombre_metodo,
		      						  String peticionJSON, String datosObtenidos) {		
		//if this returns true, then you're on the UI thread!
		try{
			if (Utilitarios.isConnectionAvailable(ctx)) {
				if (Looper.myLooper() == Looper.getMainLooper()) {
					RegistroError registroError = new RegistroError();
					registroError.setContext(ctx);
					registroError.setMensaje_error(mensajeError); 
					registroError.setStack_trace(stackTrace.toString());
					registroError.setNombre_clase(nombre_clase); 
					registroError.setNombre_metodo(nombre_metodo);
					registroError.setJson_peticion(peticionJSON);
					registroError.setJson_respuesta(datosObtenidos);
				
						new EnviarLogErroresTask().execute(registroError);
					
				} else {
					EnvioDatosAPI envioDatosAPI = new EnvioDatosAPI(ctx);
					envioDatosAPI.Enviar_Registro_Log_Errores(mensajeError, stackTrace,
								    			  nombre_clase, nombre_metodo,
								    			  peticionJSON, datosObtenidos);
				}
			}
		}
		catch (Exception ex)
		{
			
		}
	}
	  
	public static void registrarError_MostrarDialogo(Context ctx, Exception e,
											         String nombre_clase, String nombre_metodo,
											         PeticionWSL peticion, RespuestaWSL respuesta) {
		registrarError(ctx, e, nombre_clase, nombre_metodo, peticion, respuesta);
		DialogoError dialogo = new DialogoError(ctx);
		dialogo.show(nombre_clase, nombre_metodo, e);
	}
	
	public void uncaughtException(Thread thread, Throwable exception) {
		StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        
        int versionCode = 0;
		String versionName = "No se pudo obtener la versi�n de la app.";
        PackageManager manager = ctx.getPackageManager();
        PackageInfo info;
		try {
			info = manager.getPackageInfo( ctx.getPackageName(), 0);
			versionCode = info.versionCode;
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
		}
        
        errorReport.append("************ SE HA ENCONTRADO UN ERROR EN LA APLICACION ************\n\n");
        errorReport.append("************************** CAUSA DEL ERROR *************************\n\n");
        errorReport.append(stackTrace.toString());
        errorReport.append("\n");
        errorReport.append("**************************** VERSION APP *****************************\n");
        errorReport.append("Versi�n actual: [" + versionCode + "] " + versionName + " \n");
        errorReport.append("***************************** FIRMWARE *****************************\n");
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);
        
        Intent intent = new Intent(ctx, ForceClose.class);
        intent.putExtra("error", exception.getMessage());
        intent.putExtra("mensaje_completo", errorReport.toString());
        intent.putExtra("stack_trace", stackTrace.toString());
        intent.putExtra("nombre_clase", ctx.getLocalClassName());
        ctx.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
	}
	
}
