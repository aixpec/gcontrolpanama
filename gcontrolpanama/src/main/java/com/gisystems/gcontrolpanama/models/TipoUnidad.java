package com.gisystems.gcontrolpanama.models;

import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TipoUnidad {
	
	public static final String NOMBRE_TABLA 			= "tblAsignTipoUnidad";
	public static final String COLUMN_ID				= "IdAsignacionUnidad";
	public static final String COLUMN_ID_CLIENTE		= "IdCliente";
	public static final String COLUMN_ID_TIPO_UNIDAD	= "IdTipoUnidad";
	public static final String COLUMN_DESCRIPCION		= "Descripcion";
	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID 			+ " integer not null, "
			+ COLUMN_ID_CLIENTE 	+ " integer not null, "
			+ COLUMN_ID_TIPO_UNIDAD + " integer not null, "
			+ COLUMN_DESCRIPCION	+ " text not null, "
			+ "PRIMARY KEY ( " 	+ COLUMN_ID +  "), "
			+ "FOREIGN KEY ( " + COLUMN_ID_CLIENTE + " ) REFERENCES " + Cliente.NOMBRE_TABLA + "(" + Cliente.COLUMN_ID + ")"  
			+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);  
	  }

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    	
		Log.w(TipoUnidad.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
		
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	    
	  }
	
	public static void insertarTipoUnidad(Context ctx, 
			int IdAsignacionUnidad,
			int IdCliente,
			int IdTipoUnidad,
			String Descripcion){
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, 				IdAsignacionUnidad);
		values.put(COLUMN_ID_CLIENTE, 		IdCliente);
		values.put(COLUMN_ID_TIPO_UNIDAD, 	IdTipoUnidad);
		values.put(COLUMN_DESCRIPCION, 		Descripcion);
		
		
		DAL w = new DAL(ctx);
		try{
			w.iniciarTransaccion();
			w.insertRow(NOMBRE_TABLA, values);
			w.finalizarTransaccion(true);
		}
		catch (Exception e){
			w.finalizarTransaccion(false);
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					TipoUnidad.class.getSimpleName(), "insertarTipoUnidad", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		
		
	}

}
