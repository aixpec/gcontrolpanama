package com.gisystems.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;*/

import com.gisystems.gcontrolpanama.R;
import com.gisystems.exceptionhandling.ManejoErrores;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Implementa los metodos para conectarse a un servidor y consumir y postear data.
 * 
 * @author Rita Lorena Lemus Maga�a
 * 
 * @version 1
 *  
 */

@SuppressWarnings("deprecation")
public class BusinessCloud {

	private static BusinessKey businessKey;

	/**
	 * Método que envía una petición a la capa WSL de la arquitectura de Gisystems.
	 *
	 * @return verdadero/falso si hay o no conexión a internet.
	 */
	public RespuestaWSL sendRequestWSL(Context context, PeticionWSL peticionWSL)
			throws Exception {
		RespuestaWSL respuestaWSL = null;
		try {
			if (isConnectionAvailable(context)) {
				BusinessKey businessKey = new BusinessKey(context.getString(R.string.PROTOCOL),
						context.getString(R.string.HOST),
						context.getString(R.string.PORT),
						context.getString(R.string.VIRTUAL_DIRECTORY),
						"");

				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(businessKey.getSERVICE_URL())
						.addConverterFactory(GsonConverterFactory.create())
						.build();

				RetrofitAPI service = retrofit.create(RetrofitAPI.class);

				Call<String> r = service.enviarPeticionABCDEF(peticionWSL.toJSON());

				String respuesta = r.execute().body();
				respuestaWSL = new RespuestaWSL(respuesta);
			}
		} catch (IOException|IllegalStateException|NullPointerException e) {
			ManejoErrores.registrarError(context, e,
					BusinessCloud.class.getSimpleName(), "sendRequestWSL",
					peticionWSL, null);
		} catch (Exception e) {
			ManejoErrores.registrarError(context, e,
					EnvioDatosAPI.class.getSimpleName(), "sendRequestWSL",
					peticionWSL, null);
		}
		return respuestaWSL;
	}
			
	/**
	 * M�todo que verifica si hay o no conexi�n a internet.
	 * 
	 * @return verdadero/falso si hay o no conexi�n a internet.
	 */
	public static boolean isConnectionAvailable(Context context)
			throws IOException {
		NetworkInfo info = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			return true;
		}
		return true;
	}
	
	private static String prepareJSON(String json) throws Exception {
		String respuesta = "\"" + json.toString().replace("\"", "'") + "\"";
		return respuesta;
	}
	

		

}
