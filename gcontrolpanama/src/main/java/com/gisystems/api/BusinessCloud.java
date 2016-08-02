package com.gisystems.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.gisystems.gcontrolpanama.R;

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
	 * M�todo que env�a una petici�n a la capa WSL de la arquitectura de Gisystems.
	 * 
	 * @return verdadero/falso si hay o no conexi�n a internet.
	 * @throws Exception.
	 * @throws UnknownHostException.
	 */
	public static RespuestaWSL sendRequestWSL(Context context, PeticionWSL peticionWSL)
			throws Exception {
		RespuestaWSL respuestaWSL = null;
		if (isConnectionAvailable(context)) {
				String respuesta;
				respuesta = postData(context, prepareJSON(peticionWSL.toJSON()));
				respuestaWSL = new RespuestaWSL(respuesta); 
		}
		return respuestaWSL;
	}
			
	/**
	 * M�todo que verifica si hay o no conexi�n a internet.
	 * 
	 * @return verdadero/falso si hay o no conexi�n a internet.
	 * @throws IOException.
	 * @throws UnknownHostException.
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
	
	
	/**
	 * Genera una petici�n POST al API REST
	 * 
	 * @param content
	 *            Contenido del post.
	 * 
	 * @return String String que generalmente contiene un objeto JSON que ser�
	 *         parseado por el m�todo que llame al m�todo actual.
	 */
	private static String postData(Context context, String content)
			throws Exception {
		boolean isServerAvailable = false;
		String response = "";

		businessKey = new BusinessKey(context.getString(R.string.PROTOCOL),
				context.getString(R.string.HOST),
				context.getString(R.string.PORT),
				context.getString(R.string.VIRTUAL_DIRECTORY),
				context.getString(R.string.URI));

		try {
			isServerAvailable = isConnectionAvailable(context);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("SERVER AVAILABLE: "
				+ String.valueOf(isServerAvailable));
		if (isServerAvailable) {
			System.out.println("About to post\nURL: "
					+ businessKey.getSERVICE_URL() + "content: " + content);
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(businessKey.getSERVICE_URL());

			ByteArrayEntity baEntity = new ByteArrayEntity(
					content.getBytes("UTF8"));
			baEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json; charset=utf-8"));
			post.setEntity(baEntity);

			try {
				HttpResponse httpResponse = client.execute(post);
				InputStream respuesta = httpResponse.getEntity().getContent();
				Scanner scanner = new Scanner(new InputStreamReader(respuesta)); 
				response = scanner.useDelimiter("\\A").next();
				scanner.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} finally {
				
			}
		}

		return response;
	}
		

}
