package com.gisystems.gcontrolpanama.models;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LogApp {

	//****** Nombre de tabla y columnas
	public static final String NOMBRE_TABLA = "tblLog";
	public static final String COLUMN_ID="IdLog";
	public static final String COLUMN_MSG="Msg";
	public static final String COLUMN_ORIGEN="Origen";
	public static final String COLUMN_FECHA="PreguntaFechaUI";
	public static final String COLUMN_USUARIO="Usuario";
	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID 		+ " integer not null, "
			+ COLUMN_MSG 		+ " text not null, "
			+ COLUMN_ORIGEN 	+ " text not null, "
			+ COLUMN_FECHA 		+ " text not null ,"
			+ COLUMN_USUARIO 	+ " text not null ,"
			+ "PRIMARY KEY ( " 	+ COLUMN_ID +  ")"
			+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    	
		Log.w(LogApp.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
		
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	    
	  }
	
	
}
