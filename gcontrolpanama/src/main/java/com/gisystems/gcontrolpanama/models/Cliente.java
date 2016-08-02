package com.gisystems.gcontrolpanama.models;

import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Cliente {
	
	//****** Nombre de tabla y columnas
	public static final String NOMBRE_TABLA = "tblClientes";
	public static final String COLUMN_ID="IdCliente";
	public static final String COLUMN_CLIENTE="Cliente";
	public static final String COLUMN_DIRECCION="Direccion";
	public static final String COLUMN_PAIS="Pais";
	public static final String COLUMN_TELEFONO="Telefono";
	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID 		+ " integer not null, "
			+ COLUMN_CLIENTE 	+ " text not null, "
			+ COLUMN_DIRECCION 	+ " text not null, "
			+ COLUMN_TELEFONO 	+ " text not null ,"
			+ "PRIMARY KEY ( " 	+ COLUMN_ID +  ")"
			+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    	
		Log.w(Cliente.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
		
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	    
	  }
	
	public static void insertarCliente(Context ctx, 
			int idCliente,
			String Cliente,
			String Direccion,
			String Telefono){
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, idCliente);
		values.put(COLUMN_CLIENTE, Cliente);
		values.put(COLUMN_DIRECCION, Direccion);
		values.put(COLUMN_TELEFONO, Telefono);
		DAL w = new DAL(ctx);
		try
		{
			w.iniciarTransaccion();
			w.insertRow(NOMBRE_TABLA, values);
			w.finalizarTransaccion(true);
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					Cliente.class.getSimpleName(), "insertarCliente", 
                    null, null);
			w.finalizarTransaccion(false);
		}
		finally{
		
			//w.cerrarDb();
		}
		
	}
	

	
}
