package com.gisystems.gcontrolpanama.models;

import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TipoProyecto {

	private int idTipoProyecto;
	private String NombreTipoProyecto;
	private String DescripcionTipoProyecto;
	
	
	
	public int getIdTipoProyecto() {
		return idTipoProyecto;
	}

	public void setIdTipoProyecto(int idTipoProyecto) {
		this.idTipoProyecto = idTipoProyecto;
	}

	public String getNombreTipoProyecto() {
		return NombreTipoProyecto;
	}

	public void setNombreTipoProyecto(String nombreTipoProyecto) {
		NombreTipoProyecto = nombreTipoProyecto;
	}

	public String getDescripcionTipoProyecto() {
		return DescripcionTipoProyecto;
	}

	public void setDescripcionTipoProyecto(String descripcionTipoProyecto) {
		DescripcionTipoProyecto = descripcionTipoProyecto;
	}


	public static final String NOMBRE_TABLA 		= "tblCatTipoProyecto";
	public static final String COLUMN_ID			= "IdCatTipoProyecto";
	public static final String COLUMN_TIPO_PROYECTO	= "TipoProyecto";
	public static final String COLUMN_DESCRIPCION	= "Descripcion";

	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID 			+ " integer not null, "
			+ COLUMN_TIPO_PROYECTO 	+ " text not null, "
			+ COLUMN_DESCRIPCION	+ " text not null, "
			+ "PRIMARY KEY ( " 	+ COLUMN_ID +  ")"
			+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    	
		Log.w(TipoProyecto.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
		
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	    
	  }
	
	public static void insertarTipoProyecto(Context ctx, 
			int IdTipoProyecto,
			String TipoProyecto,
			String Descripcion){
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, 				IdTipoProyecto);
		values.put(COLUMN_TIPO_PROYECTO, 	TipoProyecto);
		values.put(COLUMN_DESCRIPCION, 		Descripcion);
		
		DAL w = new DAL(ctx);
		try{
			w.iniciarTransaccion();
			w.insertRow(NOMBRE_TABLA, values);
			w.finalizarTransaccion(true);
		}
		catch(Exception e){
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					TipoProyecto.class.getSimpleName(), "insertarTipoProyecto", 
                    null, null);
			w.finalizarTransaccion(false);
		}
		finally{
			//w.cerrarDb();
		}
		
		
	}
	
	
	public TipoProyecto ObtenerTipoProyecto(Context context, int IdTipoProyecto){
		DAL w = new DAL(context);
		Cursor c = null;
		TipoProyecto tProyecto=null;
		try{
			String[] columns={TipoProyecto.COLUMN_ID,
					TipoProyecto.COLUMN_TIPO_PROYECTO,
					TipoProyecto.COLUMN_DESCRIPCION};
			
			String where=TipoProyecto.COLUMN_ID + "=" + String.valueOf(IdTipoProyecto);
			c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);
			 
			 if(c.moveToFirst()){
				 tProyecto =new TipoProyecto();
				do {
					tProyecto.setIdTipoProyecto(c.getInt(c.getColumnIndexOrThrow(TipoProyecto.COLUMN_ID)));
					tProyecto.setNombreTipoProyecto(c.getString(c.getColumnIndexOrThrow(TipoProyecto.COLUMN_TIPO_PROYECTO)));
					tProyecto.setDescripcionTipoProyecto(c.getString(c.getColumnIndexOrThrow(TipoProyecto.COLUMN_DESCRIPCION)));
				}
				while(c.moveToNext());
				c.close();
			 }
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					TipoProyecto.class.getSimpleName(), "ObtenerTipoProyecto", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
			
		}
		return tProyecto;
		
	}
	
	
}
