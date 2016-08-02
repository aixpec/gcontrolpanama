package com.gisystems.gcontrolpanama.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.gcontrolpanama.models.AppValues.EstadosEnvio;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ActividadAvance extends Actividad {

	private static final long serialVersionUID = 1L;

	public static final String NOMBRE_TABLA 			="tblAvancesHistoricos";
	public static final String COLUMN_ID_CLIENTE		="IdCliente";
	public static final String COLUMN_ID_PROYECTO		="IdProyecto";
	public static final String COLUMN_ID_CONSTRUCCION   ="IdConstruccion";
	public static final String COLUMN_ID_ACTIVIDAD		="IdActividad";
	public static final String COLUMN_ID_TEMPORAL		="IdAntiguedadTemporal";
	public static final String COLUMN_ID_ANTIGUEDAD		="IdAntiguedad";
	public static final String COLUMN_MONTO_AVANCE		="MontoAvance";
	public static final String COLUMN_COMENTARIO		="Comentario";
	public static final String COLUMN_LATITUD			="Latitud";
	public static final String COLUMN_LONGITUD			="Longitud";
	public static final String COLUMN_ALTITUD			="Altitud";
	public static final String COLUMN_USUARIO_CREO		="UsuarioCreo";
	public static final String COLUMN_FECHA_CAPTURA		="FechaCaptura";
	public static final String COLUMN_FECHA_ENVIO		="FechaEnvio";
	public static final String COLUMN_ESTADO_ENVIO		="EdoEnvio";
	
	private int idCliente;
	private int idConstruccion;
	private int idProyecto;
	private int idActividad;
	private int idTemporal;
	private int idAntiguedad;
	private double montoNuevoAvance;
	private String comentario;
	private double actLatitud;
	private double actLongitud;
	private double actAltitud;
	private String fechaCaptura;
	private String fechaEnvio;
	private EstadosEnvio edoEnvio;
	private ArrayList<FotoActividad> fotos;
	private String usuarioCreo;
	
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
		this.idConstruccion = idConstruccion;
	}

	public int getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}

	public int getIdTemporal() {
		return idTemporal;
	}

	public void setIdTemporal(int idTemporal) {
		this.idTemporal = idTemporal;
	}

	public int getIdAntiguedad() {
		return idAntiguedad;
	}

	public void setIdAntiguedad(int idAntiguedad) {
		this.idAntiguedad = idAntiguedad;
	}

	public double getMontoNuevoAvance() {
		return montoNuevoAvance;
	}

	public void setMontoNuevoAvance(double montoAvance) {
		this.montoNuevoAvance = montoAvance;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public double getActLatitud() {
		return actLatitud;
	}

	public void setActLatitud(double latitud) {
		this.actLatitud = latitud;
	}

	public double getActLongitud() {
		return actLongitud;
	}

	public void setActLongitud(double longitud) {
		this.actLongitud = longitud;
	}

	public double getActAltitud() {
		return actAltitud;
	}

	public void setActAltitud(double altitud) {
		this.actAltitud = altitud;
	}

	public String getFechaCaptura() {
		return fechaCaptura;
	}

	public void setFechaCaptura(String fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}

	public String getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(String fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public EstadosEnvio getEdoEnvio() {
		return edoEnvio;
	}

	public void setEdoEnvio(EstadosEnvio edoEnvio) {
		this.edoEnvio = edoEnvio;
	}

	public ArrayList<FotoActividad> getFotos() {
		return fotos;
	}

	public void setFotos(ArrayList<FotoActividad> fotos) {
		this.fotos = fotos;
	}



	public String getUsuarioCreo() {
		return usuarioCreo;
	}

	public void setUsuarioCreo(String usuarioCreo) {
		this.usuarioCreo = usuarioCreo;
	}



	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID_CLIENTE 					+ " integer not null, "
			+ COLUMN_ID_PROYECTO 					+ " integer not null, "
			+ COLUMN_ID_CONSTRUCCION				+ " integer not null, "
			+ COLUMN_ID_ACTIVIDAD 					+ " integer not null, "
			+ COLUMN_ID_TEMPORAL 					+ " integer primary key AUTOINCREMENT, "
			+ COLUMN_ID_ANTIGUEDAD					+ " integer null, "	
			+ COLUMN_COMENTARIO 					+ " text null, "
			+ COLUMN_MONTO_AVANCE 					+ " real null, "
			+ COLUMN_LATITUD 						+ " real null ,"
			+ COLUMN_LONGITUD 						+ " real null, "
			+ COLUMN_ALTITUD 						+ " real null, "
			+ COLUMN_USUARIO_CREO 					+ " text not null, "
			+ COLUMN_FECHA_CAPTURA 					+ " text not null, "
			+ COLUMN_FECHA_ENVIO 					+ " text null, "
			+ COLUMN_ESTADO_ENVIO 					+ " integer not null "
			//+ "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_PROYECTO + ", "  + COLUMN_ID_CONSTRUCCION	+ ", "  + COLUMN_ID_ACTIVIDAD + ", "  + COLUMN_ID_TEMPORAL +  ") "
			//+ "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_PROYECTO +  ","  +  COLUMN_ID_ACTIVIDAD  +  " ) REFERENCES " + Actividad.NOMBRE_TABLA + "("   + Actividad.COLUMN_ID_CLIENTE + "," + Actividad.COLUMN_ID_PROYECTO + "," + Actividad.COLUMN_ID + ") "  
			+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    Log.w(ActividadAvance.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	  }
	
	
	@Override
    public String toString() {
        return "ActividadAvance ["  
        		+ ", " + ActividadAvance.COLUMN_ID_CLIENTE  		+ "=" + idCliente
				+ ", " + ActividadAvance.COLUMN_ID_PROYECTO   		+ "=" + idProyecto
				+ ", " + ActividadAvance.COLUMN_ID_CONSTRUCCION     + "=" + idConstruccion
        		+ ", " + ActividadAvance.COLUMN_ID_ACTIVIDAD   		+ "=" + idActividad
        		+ ", " + ActividadAvance.COLUMN_ID_TEMPORAL			+ "=" + this.idTemporal
        		+ ", " + ActividadAvance.COLUMN_ID_ANTIGUEDAD 		+ "=" + this.idAntiguedad
        		+ ", " + ActividadAvance.COLUMN_COMENTARIO  		+ "=" + this.comentario
        		+ ", " + ActividadAvance.COLUMN_MONTO_AVANCE  		+ "=" + this.montoNuevoAvance
        		+ ", " + ActividadAvance.COLUMN_LATITUD				+ "=" + this.actLatitud 
        		+ ", " + ActividadAvance.COLUMN_ALTITUD  			+ "=" + this.actAltitud 
        		+ ", " + ActividadAvance.COLUMN_LONGITUD	   		+ "=" + this.actLongitud 
        		+ ", " + ActividadAvance.COLUMN_FECHA_CAPTURA   	+ "=" + this.fechaCaptura 
        		+ ", " + ActividadAvance.COLUMN_FECHA_ENVIO   		+ "=" + this.fechaEnvio
        		+ ", " + ActividadAvance.COLUMN_ESTADO_ENVIO   		+ "=" + this.edoEnvio
        		+ "]";
    } 

	public static long insertarAvanceConFotografias(Context ctx, ActividadAvance NuevoAvance){
		
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.getDefault());
		long resultado=-1;

		ContentValues values = new ContentValues();
		values.put(ActividadAvance.COLUMN_ID_CLIENTE, 		NuevoAvance.getIdCliente());
		values.put(ActividadAvance.COLUMN_ID_PROYECTO, 		NuevoAvance.getIdProyecto());
		values.put(ActividadAvance.COLUMN_ID_CONSTRUCCION,  NuevoAvance.getIdConstruccion());
		values.put(ActividadAvance.COLUMN_ID_ACTIVIDAD, 	NuevoAvance.getIdActividad());
		values.put(ActividadAvance.COLUMN_COMENTARIO, 		NuevoAvance.getComentario());
		values.put(ActividadAvance.COLUMN_MONTO_AVANCE, 	NuevoAvance.getMontoNuevoAvance());
		values.put(ActividadAvance.COLUMN_LATITUD, 			NuevoAvance.getLatitud());
		values.put(ActividadAvance.COLUMN_ALTITUD, 			NuevoAvance.getAltitud());
		values.put(ActividadAvance.COLUMN_LONGITUD, 		NuevoAvance.getLongitud());
		values.put(ActividadAvance.COLUMN_FECHA_CAPTURA, 	sdf.format(date));
		values.put(ActividadAvance.COLUMN_ESTADO_ENVIO, 	AppValues.EstadosEnvio.No_Enviado.name());
		values.put(ActividadAvance.COLUMN_USUARIO_CREO, 	AppValues.LLAVE_SPUSUARIONOMBRE);

		DAL w = new DAL(ctx);
		try{
			
			w.iniciarTransaccion();
			resultado=w.insertRow(NOMBRE_TABLA, values);
		
		
			if (resultado!=-1)
			{
				
				values.clear();
				values.put(FotoActividad.COLUMN_ID_ANTIGUEDAD_DISP,resultado);
		
				String where=FotoActividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(NuevoAvance.getIdCliente())
						+ " and " + FotoActividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(NuevoAvance.getIdProyecto())
					   + " and " + FotoActividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(NuevoAvance.getIdConstruccion())
					   + " and " + FotoActividad.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(NuevoAvance.getIdActividad())
			  		   + " and " + FotoActividad.COLUMN_ID_ANTIGUEDAD_DISP + " is null";
				w.updateRow(FotoActividad.NOMBRE_TABLA, values, where);
			}
			else{
				w.finalizarTransaccion(false);
				//w.cerrarDb();
				return -1;
				}
		
			w.finalizarTransaccion(true);
		}
		catch (Exception e)
		{
			w.finalizarTransaccion(false);
			resultado=-1;
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					ActividadAvance.class.getSimpleName(), "insertarAvanceConFotografias", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		return resultado;
	}
	
	
	public boolean ActualizarIdAntiguedadAvance(Context ctx){
		boolean resultado=false;
		DAL w = new DAL(ctx);
		try{
			w.iniciarTransaccion();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.getDefault());
			//Actualizar el Id del avance
			ContentValues values = new ContentValues();
			values.put(ActividadAvance.COLUMN_ID_ANTIGUEDAD, this.getIdAntiguedad());
			values.put(ActividadAvance.COLUMN_FECHA_ENVIO, sdf.format(date) );
			values.put(ActividadAvance.COLUMN_ESTADO_ENVIO, AppValues.EstadosEnvio.Enviado.name());
			String where=ActividadAvance.COLUMN_ID_CLIENTE + "=" + String.valueOf(this.getIdCliente())
				    + " and " + ActividadAvance.COLUMN_ID_PROYECTO + "=" + String.valueOf(this.getIdProyecto())
					+ " and " + ActividadAvance.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(this.getIdConstruccion())
					+ " and " + ActividadAvance.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(this.getIdActividad())
		  		    + " and " + ActividadAvance.COLUMN_ID_TEMPORAL  + "=" +  String.valueOf(this.getIdTemporal());
			resultado= (w.updateRow(ActividadAvance.NOMBRE_TABLA, values, where)>0)?true:false;
			
			if(resultado) {
				//Actualizar el Id del avance en las fotografías
				values.clear();
				values.put(FotoActividad.COLUMN_ID_ANTIGUEDAD,this.getIdAntiguedad());
				where=FotoActividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(this.getIdCliente())
					   + " and " + FotoActividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(this.getIdProyecto())
					   + " and " + FotoActividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(this.getIdConstruccion())
					   + " and " + FotoActividad.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(this.getIdActividad())
			  		   + " and " + FotoActividad.COLUMN_ID_ANTIGUEDAD_DISP  +  "=" + String.valueOf(this.getIdTemporal());
				w.updateRow(FotoActividad.NOMBRE_TABLA, values, where);
				
				this.edoEnvio=AppValues.EstadosEnvio.Enviado;
	
			}
			w.finalizarTransaccion(true);
		}
		catch (Exception e)
		{
			w.finalizarTransaccion(false);
			resultado=false;
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					ActividadAvance.class.getSimpleName(), "ActualizarIdAntiguedadAvance", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		return resultado;
	}
	
	
	public boolean ActualizarEdoAvanceAPendienteConfirmar(Context ctx){
		boolean resultado=false;
		try{
			resultado=ActualizarEdoAvance(ctx,AppValues.EstadosEnvio.Pendiente_De_Confirmacion);
		
		}
		catch (Exception e)
		{
			
			resultado=false;
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					ActividadAvance.class.getSimpleName(), "ActualizarEdoAvanceAPendienteConfirmar", 
                    null, null);
		}
		return resultado;
	}
	
	private boolean ActualizarEdoAvance(Context ctx, AppValues.EstadosEnvio Edo){
		boolean resultado=false;
		DAL w = new DAL(ctx);
		try{
			w.iniciarTransaccion();
			
			//Actualizar el Id del avance
			ContentValues values = new ContentValues();
			values.put(ActividadAvance.COLUMN_ESTADO_ENVIO, Edo.name());
			String where=ActividadAvance.COLUMN_ID_CLIENTE + "=" + String.valueOf(this.getIdCliente())
				   + " and " +  ActividadAvance.COLUMN_ID_PROYECTO + "=" + String.valueOf(this.getIdProyecto())
				   + " and " +  ActividadAvance.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(this.getIdConstruccion())
				   + " and " + ActividadAvance.COLUMN_ID_ACTIVIDAD + "=" + String.valueOf(this.getIdActividad())
		  		   + " and " + ActividadAvance.COLUMN_ID_TEMPORAL  + "=" +  String.valueOf(this.getIdTemporal());
			resultado= (w.updateRow(ActividadAvance.NOMBRE_TABLA, values, where)>0)?true:false;
			if(resultado) this.edoEnvio=Edo;
			w.finalizarTransaccion(true);
		}
		catch (Exception e)
		{
			w.finalizarTransaccion(false);
			resultado=false;
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					ActividadAvance.class.getSimpleName(), "ActualizarEdoAvance", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		return resultado;
	}
	
	public static ArrayList<ActividadAvance> ObtenerTodosLosAvances(Context context){
		
		DAL w = new DAL(context);
		ArrayList<ActividadAvance> avances = new ArrayList<ActividadAvance>();
		ActividadAvance avance;
		Actividad actividad;
		Cursor c = null;
		
		try{
			 
			 c = w.getRow("Select * from " + NOMBRE_TABLA);
			 
			 if(c.moveToFirst()){
				do {					 
					avance=new ActividadAvance();
					avance.idCliente=c.getInt(c.getColumnIndexOrThrow(COLUMN_ID_CLIENTE));
					avance.idProyecto=c.getInt(c.getColumnIndexOrThrow(COLUMN_ID_PROYECTO));
					avance.idConstruccion=c.getInt(c.getColumnIndexOrThrow(COLUMN_ID_CONSTRUCCION));
					avance.idActividad=c.getInt(c.getColumnIndexOrThrow(COLUMN_ID_ACTIVIDAD));
					avance.idTemporal=c.getInt(c.getColumnIndexOrThrow(COLUMN_ID_TEMPORAL));
					avance.idAntiguedad=c.getInt(c.getColumnIndexOrThrow(COLUMN_ID_ANTIGUEDAD));
					avance.montoNuevoAvance=c.getDouble(c.getColumnIndexOrThrow(COLUMN_MONTO_AVANCE));
					avance.comentario=c.getString(c.getColumnIndexOrThrow(COLUMN_COMENTARIO));
					avance.usuarioCreo=c.getString(c.getColumnIndexOrThrow(COLUMN_USUARIO_CREO));
					avance.fechaCaptura=c.getString(c.getColumnIndexOrThrow(COLUMN_FECHA_CAPTURA));
					avance.fechaEnvio=c.getString(c.getColumnIndexOrThrow(COLUMN_FECHA_ENVIO));
					avance.edoEnvio=EstadosEnvio.valueOf(c.getString(c.getColumnIndexOrThrow(COLUMN_ESTADO_ENVIO)));
					actividad= obtenerActividad(context, avance.idCliente,
							avance.idProyecto, avance.idConstruccion,  avance.idActividad);
					if(actividad!=null){
							avance.setDescripcion(actividad.getDescripcion());
							avance.setNombreProyecto(actividad.getNombreProyecto());
							avance.setAliasProyecto(actividad.getAliasProyecto());
							avance.setFotos(FotoActividad.obtenerFotosSinEnviarDeActividadConAvance(context, 
									avance.getIdCliente(),
									avance.getIdProyecto(),
									avance.getIdConstruccion(),
									avance.getIdActividad(), 
									avance.getIdTemporal()));
						}
					
					avances.add(avance);
					avance=null;
				}
				while(c.moveToNext());
				c.close();
			 }
			
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					ActividadAvance.class.getSimpleName(), "ObtenerTodosLosAvances", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		return avances;
	}
	
	
	
}
