package com.gisystems.gcontrolpanama.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;

public class ActividadDescripcion {

	private String Descripcion;


	public String getDescripcion() {
		return Descripcion;
	}

	public void setDescripcion(String descripcion) {Descripcion = descripcion;
	}



	public static final String NOMBRE_TABLA 			= "tblAsignActDescripcion";
	public static final String COLUMN_ID_CLIENTE		= "IdCliente";
	public static final String COLUMN_ID_PROYECTO	    = "IdProyecto";
	public static final String COLUMN_ID   			 	= "IdActividad";
	public static final String COLUMN_DESCRIPCION		= "Descripcion";
	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID_CLIENTE 		+ " integer not null, "
			+ COLUMN_ID_PROYECTO    	+ " integer not null, "
			+ COLUMN_ID	    			+ " integer not null, "
			+ COLUMN_DESCRIPCION        + " text not null, "
			+ "PRIMARY KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_PROYECTO   + ", " + COLUMN_ID +  "), "
			+ "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_PROYECTO   + " ) REFERENCES " + Proyecto.NOMBRE_TABLA + "("   + Proyecto.COLUMN_ID_CLIENTE + "," + Proyecto.COLUMN_ID + ") "
         	+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);  
	  }

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    	
		Log.w(ActividadDescripcion.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	    
	  }
	
	public static void insertarDescripcionActividad(Context ctx,
			int IdCliente,
			int IdProyecto,
			int IdActividad,
			String Descripcion){
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID_CLIENTE, 		IdCliente);
		values.put(COLUMN_ID_PROYECTO, 		IdProyecto);
		values.put(COLUMN_ID, 				IdActividad);
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
					ActividadDescripcion.class.getSimpleName(), "insertarDescripcionActividad",
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		
		
	}


	public ActividadDescripcion ObtenerDescripcionActividad(Context context, int IdCliente, int IdProyecto, int IdActividad) {
		DAL w = new DAL(context);
		Cursor c = null;
		ActividadDescripcion tActividad = null;
		try {
			String[] columns = {ActividadDescripcion.COLUMN_ID_CLIENTE,
					ActividadDescripcion.COLUMN_ID_PROYECTO,
					ActividadDescripcion.COLUMN_ID,
					ActividadDescripcion.COLUMN_DESCRIPCION};

			String where = 		ActividadDescripcion.COLUMN_ID_CLIENTE + "=" + String.valueOf(IdCliente)
					+ " AND " + ActividadDescripcion.COLUMN_ID_PROYECTO + "=" + String.valueOf(IdProyecto)
					+ " AND " + ActividadDescripcion.COLUMN_ID + "=" + String.valueOf(IdActividad);

			c = w.getRowsByFilter(NOMBRE_TABLA, columns, where, null, null);

			if (c.moveToFirst()) {
				tActividad = new ActividadDescripcion();
				do {

					tActividad.setDescripcion(c.getString(c.getColumnIndexOrThrow(ActividadDescripcion.COLUMN_DESCRIPCION)));
				}
				while (c.moveToNext());
				c.close();
			}
		} catch (Exception e) {
			ManejoErrores.registrarError_MostrarDialogo(context, e,
					ActividadDescripcion.class.getSimpleName(), "ObtenerDescripcionActividad",
					null, null);
		} finally {
			//w.cerrarDb();

		}
		return tActividad;


	}

	/*@Override
	public String toString() {
		return "ActividadDescripcion ["
				+ ", " + ActividadDescripcion.COLUMN_ID_CLIENTE  			+ "=" + idCliente
				+ ", " + ActividadDescripcion.COLUMN_ID_PROYECTO   		    + "=" + idProyecto
				+ ", " + ActividadDescripcion.COLUMN_ID_ACTIVIDAD 		    + "=" + idActividad
				+ ", " + ActividadDescripcion.COLUMN_DESCRIPCION  			+ "=" + descripcion
				+ "]";
	}
	*/


}
