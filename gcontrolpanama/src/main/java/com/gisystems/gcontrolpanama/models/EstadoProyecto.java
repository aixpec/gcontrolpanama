package com.gisystems.gcontrolpanama.models;


import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EstadoProyecto {
	
	
	private int idCatEstadoProyecto;
	private String descripcionEstado;

	
	
	public int getIdCatEstadoProyecto() {
		return idCatEstadoProyecto;
	}

	public void setIdCatEstadoProyecto(int idCatEstadoProyecto) {
		this.idCatEstadoProyecto = idCatEstadoProyecto;
	}

	public String getDescripcionEstado() {
		return descripcionEstado;
	}

	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}

	public static final String NOMBRE_TABLA 		= "tblCatEstadoProyecto";
	public static final String COLUMN_ID			= "IdCatEstadoProyecto";
	public static final String COLUMN_EDO_PROYECTO	= "DescripcionEstado";

	
	
	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID 			+ " integer not null, "
			+ COLUMN_EDO_PROYECTO 	+ " text not null, "
			+ "PRIMARY KEY ( " 	+ COLUMN_ID +  ")"
			+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    	
		Log.w(EstadoProyecto.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
		
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	    
	  }
	
	public static void insertarEdoProyecto(Context ctx, 
			int EstadoProyecto,
			String DescripcionEstado){
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, EstadoProyecto);
		values.put(COLUMN_EDO_PROYECTO, DescripcionEstado);
		DAL w = new DAL(ctx);
		try
		{
			w.iniciarTransaccion();
			w.insertRow(NOMBRE_TABLA, values);
			w.finalizarTransaccion(true);
		}
		catch(Exception e)
		{
			w.finalizarTransaccion(false);
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					EstadoProyecto.class.getSimpleName(), "insertarEdoProyecto", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
	}
	
	public EstadoProyecto ObtenerEstadoProyecto(Context context, int IdEstadoProyecto){
		DAL w = new DAL(context);
		Cursor c = null;
		EstadoProyecto eProyecto=null;
		try{
			String[] columns={EstadoProyecto.COLUMN_ID,
					EstadoProyecto.COLUMN_EDO_PROYECTO};
			
			String where=EstadoProyecto.COLUMN_ID + "=" + String.valueOf(IdEstadoProyecto);
			c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);
			 
			 if(c.moveToFirst()){
				 eProyecto=new EstadoProyecto();
				do {
					eProyecto.setIdCatEstadoProyecto(c.getInt(c.getColumnIndexOrThrow(EstadoProyecto.COLUMN_ID)));
					eProyecto.setDescripcionEstado(c.getString(c.getColumnIndexOrThrow(EstadoProyecto.COLUMN_EDO_PROYECTO)));
				}
				while(c.moveToNext());
				c.close();
			 }
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					EstadoProyecto.class.getSimpleName(), "ObtenerEstadoProyecto", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
			
		}
		return eProyecto;
		
	}
	
}
