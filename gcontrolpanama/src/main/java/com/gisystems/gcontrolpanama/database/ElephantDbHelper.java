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
import com.gisystems.gcontrolpanama.models.cc.AccionRespuesta;
import com.gisystems.gcontrolpanama.models.cc.ClaseIndicador;
import com.gisystems.gcontrolpanama.models.cc.Configuracion;
import com.gisystems.gcontrolpanama.models.cc.Indicador;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.Respuesta;
import com.gisystems.gcontrolpanama.models.cc.RespuestaAccionDetalle;
import com.gisystems.gcontrolpanama.models.cc.TipoDato;
import com.gisystems.gcontrolpanama.models.cc.TipoIndicador;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion_Respuesta;
import com.gisystems.gcontrolpanama.models.chk.TipoListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.TipoListaVerificacion_Seccion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ElephantDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "GControlPanamaDb.db";
	private static final int DATABASE_VERSION = 2;
	private static ElephantDbHelper sInstance;
	
	public ElephantDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
		// Tablas de configuraciones de preguntas / respuestas
		ClaseIndicador.onCreate(database);
		TipoIndicador.onCreate(database);
		TipoDato.onCreate(database);
		AccionRespuesta.onCreate(database);
		Configuracion.onCreate(database);
		Indicador.onCreate(database);
		Pregunta.onCreate(database);
		Respuesta.onCreate(database);
		RespuestaAccionDetalle.onCreate(database);
		// Tablas para checklists
		TipoListaVerificacion.onCreate(database);
		TipoListaVerificacion_Seccion.onCreate(database);
		ListaVerificacion.onCreate(database);
		ListaVerificacion_Respuesta.onCreate(database);
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
		// Tablas de configuraciones de preguntas / respuestas
		RespuestaAccionDetalle.onUpgrade(database,oldVersion,newVersion);
		Respuesta.onUpgrade(database,oldVersion,newVersion);
		Pregunta.onUpgrade(database,oldVersion,newVersion);
		Indicador.onUpgrade(database,oldVersion,newVersion);
		Configuracion.onUpgrade(database,oldVersion,newVersion);
		AccionRespuesta.onUpgrade(database,oldVersion,newVersion);
		TipoDato.onUpgrade(database,oldVersion,newVersion);
		TipoIndicador.onUpgrade(database,oldVersion,newVersion);
		ClaseIndicador.onUpgrade(database,oldVersion,newVersion);
		// Tablas para checklists
		ListaVerificacion_Respuesta.onUpgrade(database,oldVersion,newVersion);
		ListaVerificacion.onUpgrade(database,oldVersion,newVersion);
		TipoListaVerificacion_Seccion.onUpgrade(database,oldVersion,newVersion);
		TipoListaVerificacion.onUpgrade(database,oldVersion,newVersion);
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
