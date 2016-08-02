package com.gisystems.api;

/**
 * 
 * Clase que implementa variables globles de la aplicacion,
 * y define donde estan los servidores en los que se postears y consumira
 * la data. (La informacion de los servidores encuentra en la carpeta 
 * res en el archivo strings.xml
 * 
 * @author Milton Carranza - Modificado por Rita Lorena Lemus Maga�a
 * 
 * @version 2
 *  
 */
public class BusinessKey {
		
	private String PROTOCOL;
	private String HOST;
	private String PORT;
	private String VIRTUAL_DIRECTORY;
	private String URI;
	private	String SERVICE_URL;

	/**
	 * M�todo que inicializa la clase que gestiona informaci�n importante del negocio (URLs).
	 * 
	 * @param protocol
	 * @param host
	 * @param port
	 * @param virtualDirectory
	 * @param webService
	 */
	public BusinessKey(String protocol, String host, String port, String virtualDirectory, String uri) {
		super();
		// TODO Auto-generated constructor stub
		
		 PROTOCOL 			= protocol;
		 HOST 				= host;
		 PORT 				= port;
		 VIRTUAL_DIRECTORY 	= virtualDirectory;
		 URI 				= uri;
		 SERVICE_URL 		= PROTOCOL + HOST + PORT + VIRTUAL_DIRECTORY + URI;
	}
	
	/**
	 * Obtiene el host al que se conectar�.
	 * 
	 * @return el nombre del host.
	 */
	public String getHOST() {
		return HOST;
	}
	
	/**
	 * Obtiene la ruta del servicio web.
	 * 
	 * @return la ruta del servicio.
	 */
	public String getSERVICE_URL() {
		return SERVICE_URL;
	}
	
	
}
