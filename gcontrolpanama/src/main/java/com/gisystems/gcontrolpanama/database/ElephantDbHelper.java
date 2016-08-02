package com.gisystems.gcontrolpanama.database;

import com.gisystems.gcontrolpanama.models.Actividad;
import com.gisystems.gcontrolpanama.models.ActividadAvance;
import com.gisystems.gcontrolpanama.models.ActividadDescripcion;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.gcontrolpanama.models.Cliente;
import com.gisystems.gcontrolpanama.models.Construccion;
import com.gisystems.gcontrolpanama.models.EstadoProyecto;
import com.gisystems.gcontrolpanama.models.FotoActividad;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.gcontrolpanama.models.TipoProyecto;
import com.gisystems.gcontrolpanama.models.TipoUnidad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ElephantDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "GControlPanamaDb.db";
	private static final int DATABASE_VERSION = 1;
	private static ElephantDbHelper sInstance;
	
	public ElephantDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub
		AppValues.onCreate(database);
		Cliente.onCreate(database);
		TipoProyecto.onCreate(database);
		EstadoProyecto.onCreate(database);
		Proyecto.onCreate(database);
		TipoUnidad.onCreate(database);
		Actividad.onCreate(database);
		ActividadAvance.onCreate(database);
		FotoActividad.onCreate(database);
		Construccion.onCreate(database);
		ActividadDescripcion.onCreate(database);


	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		AppValues.onUpgrade(database, oldVersion, newVersion);
		Cliente.onUpgrade(database,oldVersion,newVersion);
		TipoProyecto.onUpgrade(database,oldVersion,newVersion);
		EstadoProyecto.onUpgrade(database,oldVersion,newVersion);
		Proyecto.onUpgrade(database,oldVersion,newVersion);
		TipoUnidad.onUpgrade(database, oldVersion, newVersion);
		Actividad.onUpgrade(database,oldVersion,newVersion);
		ActividadAvance.onUpgrade(database,oldVersion,newVersion);
		FotoActividad.onUpgrade(database,oldVersion,newVersion);
		Construccion.onUpgrade(database,oldVersion,newVersion);
		ActividadDescripcion.onUpgrade(database,oldVersion,newVersion);

	}
	
	public static ElephantDbHelper getInstance(Context context) {

	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (sInstance == null) {
	      sInstance = new ElephantDbHelper(context.getApplicationContext());
	    }
	    return sInstance;
	  }

	

	
	
}
