package com.gisystems.gcontrolpanama.models;

import java.io.File;
import java.util.ArrayList;

import com.gisystems.gcontrolpanama.adaptadores.AdaptadorFotosActividad;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.Utilitarios.TipoFoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FotoActividad extends Fotografia {
	
	private int idCliente;
	private int idConstruccion;
	private int idProyecto;
	private int idActividad;


	public static final String NOMBRE_TABLA 				="tblAsignFotoActividades";
	public static final String COLUMN_ID_CLIENTE			="IdCliente";
	public static final String COLUMN_ID_PROYECTO			="IdProyecto";
	public static final String COLUMN_ID_CONSTRUCCION		="IdConstruccion";
	public static final String COLUMN_ID_ACTIVIDAD			="IdActividad";
	public static final String COLUMN_ID					="IdFoto";
	public static final String COLUMN_ID_ANTIGUEDAD_DISP	="IdAntiguedadDisp";
	public static final String COLUMN_ID_ANTIGUEDAD			="IdAntiguedad";
	public static final String COLUMN_COMENTARIO			="Comentario";
	public static final String COLUMN_RUTA_FOTO				="RutaDispositivo";
	public static final String COLUMN_LATITUD				="Latitud";
	public static final String COLUMN_LONGITUD				="Longitud";
	public static final String COLUMN_ALTITUD				="Altitud";
	public static final String COLUMN_USUARIO_CREO			="UsuarioCreo";
	public static final String COLUMN_FECHA_CAPTURA			="FechaCaptura";
	public static final String COLUMN_FECHA_ENVIO			="FechaEnvio";
	public static final String COLUMN_ESTADO_ENVIO			="EdoEnvio";
	public static final String COLUMN_TIPO_FOTO				="TipoFoto";
	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID_CLIENTE 			+ " integer not null, "
			+ COLUMN_ID_PROYECTO 			+ " integer not null, "
			+ COLUMN_ID_CONSTRUCCION 		+ " integer not null, "
			+ COLUMN_ID_ACTIVIDAD 			+ " integer not null, "
			+ COLUMN_ID 					+ " integer primary key AUTOINCREMENT, "
			+ COLUMN_ID_ANTIGUEDAD_DISP		+ " integer null,"
			+ COLUMN_ID_ANTIGUEDAD 			+ " integer null, "
			+ COLUMN_COMENTARIO 			+ " text not null, "
			+ COLUMN_RUTA_FOTO 				+ " text not null, "
			+ COLUMN_LATITUD 				+ " real not null ,"
			+ COLUMN_LONGITUD 				+ " real not null, "
			+ COLUMN_ALTITUD 				+ " real not null, "
			+ COLUMN_USUARIO_CREO 			+ " text not null, "
			+ COLUMN_FECHA_CAPTURA 			+ " text not null, "
			+ COLUMN_FECHA_ENVIO 			+ " text null, "
			+ COLUMN_ESTADO_ENVIO 			+ " integer not null, "
			+ COLUMN_TIPO_FOTO	 			+ " integer not null, "
			+ "UNIQUE ( " + COLUMN_ID_CLIENTE + ", " + COLUMN_ID_PROYECTO + ", " + COLUMN_ID_CONSTRUCCION + ", "  + COLUMN_ID_ACTIVIDAD + ", "  + COLUMN_ID +  ") "
			//+ "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_PROYECTO +  ","  +  COLUMN_ID_ACTIVIDAD  +  " ) REFERENCES " + Actividad.NOMBRE_TABLA + "("   + Actividad.COLUMN_ID_CLIENTE + "," + Actividad.COLUMN_ID_PROYECTO + "," + Actividad.COLUMN_ID + ") "  
			+ ")";

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(int idProyecto) {
		this.idProyecto = idProyecto;
	}

	public int getIdConstruccion() {
		return idConstruccion;
	}

	public void setIdConstruccion(int idConstruccion) {
		this.idConstruccion= idConstruccion;
	}

	public int getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}

	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    Log.w(FotoActividad.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	  }
	
	public static long insertarFotografia(Context ctx, 
			int IdCliente,
			int IdProyecto,
			int IdConstruccion,
			int IdActividad,
			String Comentario,
			String RutaFoto,
			Double Latitud,
			Double Longitud,
			Double Altitud,
			String FechaCaptura,
			TipoFoto TipoFotografia){
		
		long result=-1;
		
		ContentValues values = new ContentValues();
		values.put(FotoActividad.COLUMN_ID_CLIENTE, 	IdCliente);
		values.put(FotoActividad.COLUMN_ID_PROYECTO, 	IdProyecto);
		values.put(FotoActividad.COLUMN_ID_CONSTRUCCION,IdConstruccion);
		values.put(FotoActividad.COLUMN_ID_ACTIVIDAD, 	IdActividad);
		values.put(FotoActividad.COLUMN_COMENTARIO, 	Comentario);
		values.put(FotoActividad.COLUMN_RUTA_FOTO, 		RutaFoto);
		values.put(FotoActividad.COLUMN_LATITUD, 		Latitud);
		values.put(FotoActividad.COLUMN_LONGITUD, 		Longitud);
		values.put(FotoActividad.COLUMN_ALTITUD, 		Altitud);
		values.put(FotoActividad.COLUMN_USUARIO_CREO, 	AppValues.SharedPref_obtenerUsuarioNombre(ctx));
		values.put(FotoActividad.COLUMN_ESTADO_ENVIO, 	AppValues.EstadosEnvio.No_Enviado.name());
		values.put(FotoActividad.COLUMN_FECHA_CAPTURA,  FechaCaptura);
		values.put(FotoActividad.COLUMN_TIPO_FOTO,  	TipoFotografia.name());
	
		DAL w = new DAL(ctx);
		try{
			w.iniciarTransaccion();

			result=w.insertRow(NOMBRE_TABLA, values);
	
			w.finalizarTransaccion(true);
		}
			catch (Exception e)
		{
			w.finalizarTransaccion(false);
			result=-1;
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					Actividad.class.getSimpleName(), "insertarActividad", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		return result;
	}
	
	private static ArrayList<FotoActividad> obtenerFotosSinEnviar(Context context,
			int IdCliente,  int IdProyecto, int IdConstruccion, int IdActividad, int IdDispositivo, TipoFoto TipoFotografia){
		
		System.gc();
		
		ArrayList<FotoActividad> fotos = new ArrayList<FotoActividad>();
		DAL w = new DAL(context);
		FotoActividad foto;
		String[] columns={FotoActividad.COLUMN_ID_CLIENTE,
				FotoActividad.COLUMN_ID_PROYECTO,
				FotoActividad.COLUMN_ID_CONSTRUCCION,
				FotoActividad.COLUMN_ID_ACTIVIDAD,
				FotoActividad.COLUMN_ID,
				FotoActividad.COLUMN_COMENTARIO,
				FotoActividad.COLUMN_RUTA_FOTO,
				FotoActividad.COLUMN_ALTITUD,
				FotoActividad.COLUMN_LATITUD,
				FotoActividad.COLUMN_LONGITUD,
				FotoActividad.COLUMN_FECHA_CAPTURA,
				FotoActividad.COLUMN_ID_ANTIGUEDAD_DISP,
				FotoActividad.COLUMN_ID_ANTIGUEDAD,
				FotoActividad.COLUMN_TIPO_FOTO};
		
		
		String where="";
		
		switch(TipoFotografia){
			case ActividadConAvance:
				where=FotoActividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(IdCliente)
				   + " and " +  FotoActividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(IdProyecto)
				   + " and " +  FotoActividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(IdConstruccion)
				   + " and " +  FotoActividad.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(IdActividad)
		  		   + " and " +  FotoActividad.COLUMN_ID_ANTIGUEDAD_DISP + "=" + String.valueOf(IdDispositivo)
		  		   + " and " +  FotoActividad.COLUMN_TIPO_FOTO			+ "='" + TipoFotografia.name() + "'"
		  		   + " and " +  FotoActividad.COLUMN_ID_ANTIGUEDAD + " is null";
				break;
			case ActividadSinAvance:
				where=FotoActividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(IdCliente)
				   + " and " +  FotoActividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(IdProyecto)
			       + " and " +  FotoActividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(IdConstruccion)
				   + " and " +  FotoActividad.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(IdActividad)
				   + " and " +  FotoActividad.COLUMN_ESTADO_ENVIO + "='" + AppValues.EstadosEnvio.No_Enviado + "'"
		  		   + " and " +  FotoActividad.COLUMN_TIPO_FOTO			+ "='" + TipoFotografia.name() + "'";
				break;
			default:
				break;
		}

		Cursor c = null;
		try{
			
			 c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);
			
			 if(c.moveToFirst()){
				Log.w("Fotos sin avance actividad",String.valueOf(c.getCount()));
				do {
					foto=new FotoActividad();
					foto.setIdCliente(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_CLIENTE)));
					foto.setIdProyecto(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_PROYECTO)));
					foto.setIdConstruccion(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_CONSTRUCCION)));
					foto.setIdActividad(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_ACTIVIDAD)));
					foto.setIdFoto(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID)));
					foto.setIdAntiguedadDisp(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_ANTIGUEDAD_DISP));
					foto.setIdAntiguedad(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_ANTIGUEDAD));
					foto.setLatitud(c.getDouble(c.getColumnIndexOrThrow(FotoActividad.COLUMN_LATITUD)));
					foto.setLongitud(c.getDouble(c.getColumnIndexOrThrow(FotoActividad.COLUMN_LONGITUD)));
					foto.setAltitud(c.getDouble(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ALTITUD)));
					foto.setRutaFoto(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_RUTA_FOTO)));
					foto.setComentario(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_COMENTARIO)));
					foto.setFechaCaptura(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_FECHA_CAPTURA)));
					foto.setTipoFoto(TipoFoto.valueOf(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_TIPO_FOTO))));
					fotos.add(foto);					
				}
				while(c.moveToNext());
				foto=null;
				c.close();
			 }
			 
			
			 
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					FotoActividad.class.getSimpleName(), "ObtenerFotosSinGuardarActividad", 
                   null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		
		return fotos;
		
	}
	
	public static ArrayList<FotoActividad> obtenerFotosSinEnviarDeActividadConAvance(Context context,
			int IdCliente, int IdProyecto, int IdConstruccion,  int IdActividad, int IdDispositivo){
		
		ArrayList<FotoActividad> fotos = new ArrayList<FotoActividad>();
		
		try{
			
			fotos=obtenerFotosSinEnviar(context, IdCliente, IdProyecto, IdConstruccion,  IdActividad,
					IdDispositivo,TipoFoto.ActividadConAvance);

		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					FotoActividad.class.getSimpleName(), "ObtenerFotosSinGuardarActividad", 
                   null, null);
		}

		return fotos;
	}
	
	public static ArrayList<FotoActividad> obtenerFotosSinEnviarDeActividadSinAvance(Context context,
			int IdCliente,  int IdProyecto, int IdConstruccion,  int IdActividad){

		ArrayList<FotoActividad> fotos = new ArrayList<FotoActividad>();

		try{

			fotos=obtenerFotosSinEnviar(context, IdCliente, IdProyecto, IdConstruccion, IdActividad,
					-1,TipoFoto.ActividadSinAvance);

		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e,
					FotoActividad.class.getSimpleName(), "ObtenerFotosSinGuardarActividad",
                   null, null);
		}

		return fotos;

	}

	public static ArrayList<FotoActividad> obtenerFotosSinEnviarSinAvance(Context context){

		ArrayList<FotoActividad> fotos = new ArrayList<FotoActividad>();
		DAL w = new DAL(context);
		FotoActividad foto;
		String[] columns={FotoActividad.COLUMN_ID_CLIENTE,
				FotoActividad.COLUMN_ID_PROYECTO,
				FotoActividad.COLUMN_ID_CONSTRUCCION,
				FotoActividad.COLUMN_ID_ACTIVIDAD,
				FotoActividad.COLUMN_ID,
				FotoActividad.COLUMN_COMENTARIO,
				FotoActividad.COLUMN_RUTA_FOTO,
				FotoActividad.COLUMN_ALTITUD,
				FotoActividad.COLUMN_LATITUD,
				FotoActividad.COLUMN_LONGITUD,
				FotoActividad.COLUMN_FECHA_CAPTURA,
				FotoActividad.COLUMN_ID_ANTIGUEDAD_DISP,
				FotoActividad.COLUMN_ID_ANTIGUEDAD,
				FotoActividad.COLUMN_TIPO_FOTO};


		String where="";


				where= FotoActividad.COLUMN_ESTADO_ENVIO + "='" + AppValues.EstadosEnvio.No_Enviado + "'"
						+ " and " +  FotoActividad.COLUMN_TIPO_FOTO			+ "='" + TipoFoto.ActividadSinAvance + "'";


		Cursor c = null;
		try{

			c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);

			if(c.moveToFirst()){
				Log.w("Fotos sin avance sin actividad",String.valueOf(c.getCount()));
				do {
					foto=new FotoActividad();
					foto.setIdCliente(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_CLIENTE)));
					foto.setIdProyecto(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_PROYECTO)));
					foto.setIdConstruccion(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_CONSTRUCCION)));
					foto.setIdActividad(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_ACTIVIDAD)));
					foto.setIdFoto(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID)));
					foto.setIdAntiguedadDisp(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_ANTIGUEDAD_DISP));
					foto.setIdAntiguedad(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_ANTIGUEDAD));
					foto.setLatitud(c.getDouble(c.getColumnIndexOrThrow(FotoActividad.COLUMN_LATITUD)));
					foto.setLongitud(c.getDouble(c.getColumnIndexOrThrow(FotoActividad.COLUMN_LONGITUD)));
					foto.setAltitud(c.getDouble(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ALTITUD)));
					foto.setRutaFoto(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_RUTA_FOTO)));
					foto.setComentario(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_COMENTARIO)));
					foto.setFechaCaptura(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_FECHA_CAPTURA)));
					foto.setTipoFoto(TipoFoto.valueOf(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_TIPO_FOTO))));
					fotos.add(foto);
				}
				while(c.moveToNext());
				foto=null;
				c.close();
			}



		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e,
					FotoActividad.class.getSimpleName(), "ObtenerFotosSinGuardarActividad",
					null, null);
		}
		finally{
			//w.cerrarDb();
		}


		return fotos;

	}


	private static ArrayList<FotoActividad> obtenerFotosSinGuardarActividad(Context context,
			int IdCliente, int IdProyecto, int IdConstruccion, int IdActividad, boolean ObtenerBitmap, TipoFoto TipoFotografia){
		
		
		System.gc();
		
		ArrayList<FotoActividad> fotos = new ArrayList<FotoActividad>();
		DAL w = new DAL(context);
		FotoActividad foto;
		String[] columns={FotoActividad.COLUMN_ID_CLIENTE,
				FotoActividad.COLUMN_ID_PROYECTO,
				FotoActividad.COLUMN_ID_CONSTRUCCION,
				FotoActividad.COLUMN_ID_ACTIVIDAD, FotoActividad.COLUMN_ID, FotoActividad.COLUMN_COMENTARIO,
				FotoActividad.COLUMN_RUTA_FOTO, FotoActividad.COLUMN_ALTITUD, FotoActividad.COLUMN_LATITUD,
				FotoActividad.COLUMN_LONGITUD, FotoActividad.COLUMN_FECHA_CAPTURA, FotoActividad.COLUMN_TIPO_FOTO};
		
		String where=FotoActividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(IdCliente)
				+ " and " +  FotoActividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(IdProyecto)
				+ " and " +  FotoActividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(IdConstruccion)
				+ " and " +  FotoActividad.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(IdActividad)
				+ " and " +  FotoActividad.COLUMN_TIPO_FOTO			+ "='" + TipoFotografia.name() + "'"
		  		+ " and " +  FotoActividad.COLUMN_ID_ANTIGUEDAD_DISP + " is null";
		
		Cursor c = null;
		try{
			
			 c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);
			
			 if(c.moveToFirst()){
				Log.w("Fotos actividad",String.valueOf(c.getCount()));
				do {
					foto=new FotoActividad();
					foto.setIdCliente(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_CLIENTE)));
					foto.setIdProyecto(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_PROYECTO)));
					foto.setIdConstruccion(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_CONSTRUCCION)));
					foto.setIdActividad(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID_ACTIVIDAD)));
					foto.setIdFoto(c.getInt(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ID)));
					foto.setLatitud(c.getDouble(c.getColumnIndexOrThrow(FotoActividad.COLUMN_LATITUD)));
					foto.setLongitud(c.getDouble(c.getColumnIndexOrThrow(FotoActividad.COLUMN_LONGITUD)));
					foto.setAltitud(c.getDouble(c.getColumnIndexOrThrow(FotoActividad.COLUMN_ALTITUD)));
					foto.setRutaFoto(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_RUTA_FOTO)));
					foto.setComentario(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_COMENTARIO)));
					foto.setFechaCaptura(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_FECHA_CAPTURA)));
					foto.setTipoFoto(TipoFoto.valueOf(c.getString(c.getColumnIndexOrThrow(FotoActividad.COLUMN_TIPO_FOTO))));
					fotos.add(foto);					
				}
				while(c.moveToNext());
				foto=null;
				c.close();
			 }
			 
			 if(ObtenerBitmap){
				 Bitmap img =null;
				 for(int i=0; i<fotos.size(); i++){
					img =obtenerFoto(fotos.get(i).getRutaFoto(),context);
					if (img!=null)
						fotos.get(i).setImagen(img);
					img=null;
				 } 
			 }
			 
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					FotoActividad.class.getSimpleName(), "ObtenerFotosSinGuardarActividad", 
                   null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		
		return fotos;
		
	}
	
	public static ArrayList<FotoActividad> obtenerFotosSinGuardarActividadConAvance(Context context,
			int IdCliente, int IdProyecto, int IdConstruccion,  int IdActividad, boolean ObtenerBitmap){
		
		ArrayList<FotoActividad> fotos = new ArrayList<FotoActividad>();
		
		try{
			fotos=obtenerFotosSinGuardarActividad(context, IdCliente, IdProyecto, IdConstruccion, IdActividad,
					ObtenerBitmap, TipoFoto.ActividadConAvance);
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					FotoActividad.class.getSimpleName(), "ObtenerFotosSinGuardarActividad", 
                   null, null);
		}
		return fotos;
	}
	
	public static ArrayList<FotoActividad> obtenerFotosSinGuardarActividadSinAvance(Context context,
			int IdCliente, int IdProyecto,  int IdConstruccion, int IdActividad, boolean ObtenerBitmap){
		
		ArrayList<FotoActividad> fotos = new ArrayList<FotoActividad>();
		
		try{
			fotos=obtenerFotosSinGuardarActividad(context, IdCliente, IdProyecto, IdConstruccion, IdActividad,
					ObtenerBitmap, TipoFoto.ActividadSinAvance);
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					FotoActividad.class.getSimpleName(), "ObtenerFotosSinGuardarActividad", 
                   null, null);
		}
		return fotos;
	}
	private static Bitmap obtenerFoto(String PATH, Context context){
		 
		    try{
		    	  BitmapFactory.Options bmo = new BitmapFactory.Options ();
				    bmo.inSampleSize = 16;
				    //if (bm != null) bm.recycle();
				    Bitmap bm = null;
				    bm = BitmapFactory.decodeFile (PATH, bmo);
				    //verificar si existe la imagen
					File f = new File(PATH);
				    if (f.exists())
					{
				    	if (bm != null)
				    		return bm;
					}
		
		  } catch (Exception e) {
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					AdaptadorFotosActividad.class.getSimpleName(), "obtenerFoto",
                null, null);   
		  }
		  return null;
	}

	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof FotoActividad)) {
	        return false;
	    }

	    FotoActividad that = (FotoActividad) other;

	    // Custom equality check here.
	    return this.getRutaFoto().equals(that.getRutaFoto());
	    
	}
	
	public static boolean eliminarFotoAvanceSinGuardar(Context context,
			int IdCliente, int IdProyecto, int IdConstruccion, int IdActividad, int IdFoto){
		
		DAL w = new DAL(context);
		boolean resultado=false;
		String where=FotoActividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(IdCliente)
				   + " and " +  FotoActividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(IdProyecto)
				   + " and " +  FotoActividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(IdConstruccion)
				   + " and " +  FotoActividad.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(IdActividad)
				   + " and " +  FotoActividad.COLUMN_ID + "=" + String.valueOf(IdFoto);
		
		resultado = w.deleteRow(NOMBRE_TABLA, where);
		
		return resultado;
	}
	
	public static boolean eliminarTodasFotoAvanceSinGuardar(Context context,
			int IdCliente, int IdProyecto, int IdConstruccion, int IdActividad){
		
		DAL w = new DAL(context);
		boolean resultado=false;
		String where=FotoActividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(IdCliente)
				   + " and " +  FotoActividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(IdProyecto)
				   + " and " +  FotoActividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(IdConstruccion)
				   + " and " +  FotoActividad.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(IdActividad);
		
		resultado = w.deleteRow(NOMBRE_TABLA, where);
		
		return resultado;
	}
	
	public static boolean actualizarEdoFotografia(Context context, FotoActividad foto, AppValues.EstadosEnvio Edo){
		boolean resultado=false;
		DAL w = new DAL(context);
		try{
			w.iniciarTransaccion();
			
			//Actualizar el Id del avance
			ContentValues values = new ContentValues();
			values.put(FotoActividad.COLUMN_ESTADO_ENVIO, Edo.name());
			String where=FotoActividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(foto.getIdCliente())
					+ " and " +  FotoActividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(foto.getIdProyecto())
					+ " and " +  FotoActividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(foto.getIdConstruccion())
					+ " and " +  FotoActividad.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(foto.getIdActividad())
		  		    + " and " +  FotoActividad.COLUMN_ID + "=" +  String.valueOf(foto.getIdFoto());
			
			resultado= (w.updateRow(FotoActividad.NOMBRE_TABLA, values, where)>0)?true:false;
			
			w.finalizarTransaccion(true);
		}
		catch (Exception e)
		{
			w.finalizarTransaccion(false);
			resultado=false;
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					FotoActividad.class.getSimpleName(), "actualizarEdoFotografia", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		return resultado;
	}
	
	
}
