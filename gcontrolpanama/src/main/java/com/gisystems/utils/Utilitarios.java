package com.gisystems.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.gisystems.api.BusinessCloud;
import com.gisystems.api.PeticionWSL;
import com.gisystems.api.RespuestaWSL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utilitarios {

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
    //Lista de los posibles orígenes de la solicitud a la capa WSL.
	public static enum TipoOrigen {
	        Escritorio, Web, Android;
	}

    //Lista de las posibles funciones solicitadas a la capa WSL.
	public static enum TipoFuncion {
        validarUsuario, ejecutarMetodo;
	}

	//Lista de los posibles orígenes de la solicitud a la capa WSL.
	public static enum TipoFoto {
		    Proyecto, ActividadConAvance, ActividadSinAvance;
		}
	
	public static final String UPDATER_BROADCAST_INTENT_FILTER = "Updater_Broadcast_Intent_Filter";
	
	public static String ObtenerAndroid_ID(Context context) {
		String resultado =  Secure.getString(context.getContentResolver(), Secure.ANDROID_ID); 
		return resultado;
	}
	
	public static TelephoneID ObtenerIdTelefono(Context context) {
		TelephoneID respuesta = new TelephoneID();
		android.telephony.TelephonyManager manager = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		respuesta.setImei(manager.getDeviceId());
		respuesta.setImsi(manager.getSubscriberId());
		return respuesta;
	}
	
    public static boolean ValidarUsuarioPassword(Context context,
    						String userName, String password, 
                            StringBuilder mensajeError) {
        boolean resultado = false;
        PeticionWSL peticionWSL;
        RespuestaWSL respuesta = null;
         
        ArrayList<Object> parametros = new ArrayList<Object>(); 
		parametros.add(userName);
		parametros.add(password);
        String nombreArchivoAssembly = ""; 
        String namespaceClase = "";
		String nombreClase = ""; 
		String metodoEjecutara = "";
		String pathLog = ""; 
        try {
            //1. Convierte los parámetros recibidos en una petición a la capa WSL en formato JSON
        	peticionWSL = Utilitarios.ObtenerPeticionWSL(context,
        			TipoFuncion.validarUsuario, userName, password,
        			parametros, nombreArchivoAssembly, 
        			namespaceClase, nombreClase, metodoEjecutara, 
        			pathLog);
            //2. Conexión al API REST de la capa WSL
            respuesta = BusinessCloud.sendRequestWSL(context, peticionWSL);
            //3. Obtener resultado
            if (respuesta != null) {
            if (respuesta.getEjecutadoSinError()) {
                if (respuesta.getParametros() == "true") {
                    resultado = true;
                } else {
                    mensajeError.append(respuesta.getMotivoFallo());
                }
            } else {
            	mensajeError.append(respuesta.getMotivoFallo());
            }
            }
        } catch (Exception e) {
            mensajeError.append(e.getMessage());
        }
        return resultado;
    }

//    ''' <summary>
//    ''' Esta función recibe los datos que se deben incluirse en una petición a la capa WSL y los serializa al formato JSON.
//    ''' </summary>
//    ''' <param name="origen">Donde se origina la petición a la capa WSL.</param>
//    ''' <param name="funcion">Función que se está pidiendo a la capa WSL y que se ejecuta en la capa MML.</param>
//    ''' <param name="userName">Username del usuario de la aplicación en la capa de UI.</param>
//    ''' <param name="password">Password del usuario de la aplicación en la capa de UI.</param>
//    ''' <param name="imei">IMEI del teléfono de donde se hace la petición a la capa WSL.</param>
//    ''' <param name="imsi">IMSI del teléfono de donde se hace la petición a la capa WSL.</param>
//    ''' <param name="nombreArchivoAssembly">Nombre del archivo físico del assembly donde se encuentra el método que se pide ejecutar.</param>
//    ''' <param name="namespaceClase">Namespace de la clase que contiene el método que se pide ejecutar.</param>
//    ''' <param name="nombreClase">Nombre de la clase que contiene el método que se pide ejecutar.</param>
//    ''' <param name="metodoEjecutara">Nombre del método que se pide ejecutar.</param>
//    ''' <param name="parametros">Parámetros requeridos por el método que se pide ejecutar.</param>
//    ''' <param name="pathLog">Path del archivo de log para la aplicación de la capa de UI que está haciendo la petición.</param>
//    ''' <returns>Devuelve los datos enviados como parámetros ya serializados para ser enviados a la capa WSL.</returns>
	public static PeticionWSL ObtenerPeticionWSL(Context context, 
			TipoFuncion funcion, String userName, String password,
			List<Object> parametros,
			String nombreArchivoAssembly, String namespaceClase,
			String nombreClase, String metodoEjecutara, 
			String pathLog) {
		PeticionWSL peticion = null;
		TelephoneID telephoneID; 
		try {
			//1. Obtener datos del dispositivo
			telephoneID =  ObtenerIdTelefono(context);
			//2. Instanciar PeticionWSL
			peticion = new PeticionWSL();
			peticion.setOrigen(TipoOrigen.Android.toString());
			peticion.setFuncion(funcion.toString());
			peticion.setUserName(userName);
			peticion.setPassword(password);
			peticion.setImei(telephoneID.getImei());
			peticion.setImsi(telephoneID.getImsi());
			peticion.setNombreArchivoAssembly(nombreArchivoAssembly);
			peticion.setNamespaceClase(namespaceClase);
			peticion.setNombreClase(nombreClase);
			peticion.setMetodoEjecutara(metodoEjecutara);
			peticion.setParametros(parametros);
			peticion.setPathLog(pathLog);
		} catch (Exception e) {
		}
		return peticion;
	}
	
	
	
public static Bitmap redimensionarImagen(String path){
		
		File f = new File(path);
		
	    Bitmap b = null;
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;

	        FileInputStream fis = new FileInputStream(f);
	        BitmapFactory.decodeStream(fis, null, o);
	        fis.close();
	        
	    
	        //De Momento se dejara a 1024
			final int IMAGE_MAX_SIZE = 1024;

	        int scale = 1;
	        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
	            scale = (int)Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        fis = new FileInputStream(f);
	        b = BitmapFactory.decodeStream(fis, null, o2);
	        fis.close();
	    } catch (IOException e) {
	    }
	    return b;
	}
	
	 
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	/**
	 *
	 * Método que verifica si hay o no conexión a internet.
	 *
	 * @return verdadero/falso si hay o no conexión a internet.
	 * @throws IOException.
	 * @throws UnknownHostException.
	 */
	public static boolean isConnectionAvailable(Context context) throws IOException {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info==null || !info.isConnected()) {
                return false;
        }
        if (info.isRoaming()) {
                // here is the roaming option you can change it if you want to disable internet while roaming, just return false
                return true;
        }
        return true;
	}
	public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);       
        return bd;
    }
	
	
	@SuppressLint("SimpleDateFormat") public static String formatDateTime(Context context, String timeToFormat) {

	    String finalDateTime = "";          

	    SimpleDateFormat iso8601Format = new SimpleDateFormat(
	            "yyyy-MM-dd HH:mm:ss");

	    Date date = null;
	    if (timeToFormat != null) {
	        try {
	            date = iso8601Format.parse(timeToFormat);
	        } catch (ParseException e) {
	            date = null;
	        }

	        if (date != null) {
	            long when = date.getTime();
	            int flags = 0;
	            flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
	            flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
	            flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
	            flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

	            finalDateTime = android.text.format.DateUtils.formatDateTime(context,
	            when + TimeZone.getDefault().getOffset(when), flags);               
	        }
	    }
	    return finalDateTime;
	}
	
	
	/**
     * Da formato a un par de coordenadas geográficas para mostrarlas al usuario.
     * 
     * @param lat Latitud que se formateará
     * @param lon Longitud que se formateará
     * @return Ubicación geográfica formateada para mostrarla al usuario.
     */
    @SuppressLint("DefaultLocale")
	public static String getLatLonFormated(double lat, double lon)
    {
		char latdir            = lat < 0 ? 'S' : 'N';
		if (latdir == 'S') lat = -lat;
		double latsecs = (lat * 3600) % 60;
		int latmins            = (int) (lat * 60) % 60;
		int latdegs            =  (int)lat;
		char londir            = lon < 0 ? 'W' : 'E';
		if (londir == 'W') lon = -lon;
		double lonsecs         = (lon * 3600) % 60;
		int lonmins            = (int) (lon * 60) % 60;
		int londegs            = (int)lon;
		
		return String.format ("Posición: %c %d\" %d' %.3f\" %c %d\" %d' %.3f\"", latdir, latdegs, latmins, latsecs, londir, londegs, lonmins, lonsecs);
    }
    
    /**
     * Obtiene la posición geográfica con un formato específico, partiendo de un objeto Location.
     * 
     * @param location Objeto location de donde se obtendrán las coordenadas geograficas.
     * @return Ubicación geográfica formateada para mostrarla al usuario.
     */
    public static String getLocationFormated(Location location)
    {
    	return getLatLonFormated(location.getLatitude(),location.getLongitude());
    }
	
    /**
     * Obtiene la posición geográfica con un formato específico, partiendo de un objeto GeoPoint.
     * 
     * @param location Objeto geopoint de donde se obtendrán las coordenadas geográficas.
     * @return Ubicación geográfica formateada para mostrarla al usuario.
     */
    public static String getLocationFormated(GeoPoint location)
    {
		return getLatLonFormated(location.getLatitud(),location.getLongitud());
    }
    
    
	/**
	 * Obtiene la ruta de un archivo para guardar una imagen o video.
	 * 
	 * @param type Tipo de archivo que se estará generando (Fotos y videos) .
	 * @return a File for saving an image or video.
	 */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

		if (verificarSdcard())
		{
		    File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Inventario");
		    
		    
		    // This location works best if you want the created images to be shared
		    // between applications and persist after your app has been uninstalled.

		    // Create the storage directory if it does not exist
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            Log.d("Elephant", "failed to create directory");
		            return null;
		        }
		    }

		    // Create a media file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		    File mediaFile;
		    if (type == MEDIA_TYPE_IMAGE){
		        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		        "IMG_"+ timeStamp + String.valueOf(System.currentTimeMillis()) + ".jpg");
		    } else if(type == MEDIA_TYPE_VIDEO) {
		        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		        "VID_"+ timeStamp + String.valueOf(System.currentTimeMillis()) + ".mp4");
		    } else {
		        return null;
		    }
		    
		    return mediaFile;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Obtiene la URI de un archivo para guardar una imagen o video.
	 * 
	 * @param type Tipo de archivo que se estará generando (Fotos y videos).
	 * @return a file Uri for saving an image or video.
	 */
	public static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	
	/**
	 * Método que verifica si la SDCARD está presente en el dispositivo.
	 * 
	 * @return si la memoria externa está presente
	 */
	public static boolean verificarSdcard() {
	    return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * Escanea una fotografía para agregarla a la galería fotográfica.
	 * 
	 * @param ctx Contexto de la aplicación.
	 * @param path Ruta de la fotografía que se escaneará.
	 */
	public static void scanPicture(Context ctx, String path)
	{
		 MediaScannerConnection scanner = new MediaScannerConnection(ctx, null);
		 scanner.connect();

		 if (scanner.isConnected())
		     scanner.scanFile(path, null);
		 
		 scanner.disconnect();
	}

	public static boolean esNumero(Object str){
		try  
		  {  
			Double.parseDouble(String.valueOf(str));  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true; 
	}

    /**
     * Fuerza a Android a cerrar el teclado
     * Tomado de: http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
     * @param ctx Contexto de la aplicación
     * @param v Vista a la que se le pasará el foco, después de ocultar el teclado
     */
	public static void ocultarTeclado(Context ctx, View v){

		InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

	}
	
}
