package com.gisystems.gcontrolpanama.models;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

public class Proyecto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int idCliente;
	private int idProyecto;
	private int idCatTipoProyecto;
	private int idCatEstadoProyecto;
	private String nombreProyecto;
	private String aliasProyecto;
	private String fechaInicia;
	private String fechaFinaliza;
	private String moneda;
	
	private Double montoOriginal;
	private Double montoModificado;
	private Double montoPagado;
	private Double montoEjecutado;
	private Double avanceFisico;
	private Double avanceFinanciero;
	
	private Double latitud;
	private Double longitud;
	private Double altitud;
	private String TipoProyectoDesc;
	private String EdoProyectoDesc;
	private String descripcion;
	private static TipoGestion tipoGestion;
	public enum TipoGestion { Por_Cantidades, Por_Porcentajes};
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIdCatTipoProyecto() {
		return idCatTipoProyecto;
	}

	public void setIdCatTipoProyecto(int idCatTipoProyecto) {
		this.idCatTipoProyecto = idCatTipoProyecto;
	}

	public int getIdCatEstadoProyecto() {
		return idCatEstadoProyecto;
	}

	public void setIdCatEstadoProyecto(int idCatEstadoProyecto) {
		this.idCatEstadoProyecto = idCatEstadoProyecto;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Double getMontoOriginal() {
		return montoOriginal;
	}

	public void setMontoOriginal(Double montoOriginal) {
		this.montoOriginal = montoOriginal;
	}

	public Double getMontoModificado() {
		return montoModificado;
	}

	public void setMontoModificado(Double montoModificado) {
		this.montoModificado = montoModificado;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Double getAltitud() {
		return altitud;
	}

	public void setAltitud(Double altitud) {
		this.altitud = altitud;
	}

	public String getTipoProyectoDesc() {
		return TipoProyectoDesc;
	}

	public void setTipoProyectoDesc(String tipoProyectoDesc) {
		TipoProyectoDesc = tipoProyectoDesc;
	}

	public String getEdoProyectoDesc() {
		return EdoProyectoDesc;
	}

	public void setEdoProyectoDesc(String edoProyectoDesc) {
		EdoProyectoDesc = edoProyectoDesc;
	}

	public String getFechaInicia() {
		return fechaInicia;
	}

	public void setFechaInicia(String fechaInicia) {
		this.fechaInicia = fechaInicia;
	}

	public String getFechaFinaliza() {
		return fechaFinaliza;
	}

	public void setFechaFinaliza(String fechaFinaliza) {
		this.fechaFinaliza = fechaFinaliza;
	}

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

	public TipoGestion getTipoGestion() {
		return tipoGestion;
	}

	public Double getMontoPagado() {
		return montoPagado;
	}

	public void setMontoPagado(Double montoPagado) {
		this.montoPagado = montoPagado;
	}

	public Double getMontoEjecutado() {
		return montoEjecutado;
	}

	public void setMontoEjecutado(Double montoEjecutado) {
		this.montoEjecutado = montoEjecutado;
	}

	public Double getAvanceFisico() {
		return avanceFisico;
	}

	public void setAvanceFisico(Double avanceFisico) {
		this.avanceFisico = avanceFisico;
	}

	public Double getAvanceFinanciero() {
		return avanceFinanciero;
	}

	public void setAvanceFinanciero(Double avanceFinanciero) {
		this.avanceFinanciero = avanceFinanciero;
	}

	public Proyecto(String NombreProyecto, String AliasProyecto ){
		super();
		this.nombreProyecto=NombreProyecto;
		this.aliasProyecto=AliasProyecto;
	}
	
	/**
	 * Devuelve la cantidad de días ejecutados a la fecha de hoy
	 * Ver también: ObtenerTotalDiasEjecutados()
	 * @return
	 * @throws Exception
	 */
	public Long getTotalDiasEjecutadosALaFecha() throws Exception  {
		
		long totalDiasEjecutados=0;
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		try{
			
			totalDiasEjecutados=TimeUnit.MILLISECONDS.toDays((new Date().getTime()) )
	    			- TimeUnit.MILLISECONDS.toDays(format.parse(this.getFechaInicia()).getTime());
			
		}
		catch (Exception e){
			throw e;
		}
		
		return totalDiasEjecutados;
	}	
	
	
	/**
	 * Devuelve el total de días ejecutados de un proyecto. 
	 * Si el total de dias ejecutados a la fecha excede al total de días de duración del proyecto
	 * se devuelve el valor en negativo de los días sobrepasados.
	 * Ver también: ObtenerTotalDiasEjecutadosALaFecha() 
	 * @return Días ejecutados
	 * @throws Exception
	 */
	public Long getTotalDiasEjecutados() throws Exception  {
		
		long totalDiasEjecutados=0;
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		try{

			long totalDias=getTotalDiasDuraciónProyecto();
			
			
			
			totalDiasEjecutados=TimeUnit.MILLISECONDS.toDays((new Date().getTime()) )
	    			- TimeUnit.MILLISECONDS.toDays(format.parse(this.getFechaInicia()).getTime());
			
			//Si el proyecto no ha empezado
			if(totalDiasEjecutados<=0) 
				return Long.valueOf(0);
			
			//Si el total de dias ejecutados a la fecha excede al total de días de duración del proyecto
			//se devuelve el valor en negativo de los días sobrepasados
			if(totalDiasEjecutados>=totalDias) 
				return totalDias;
			
			
			
		}
		catch (Exception e){
			throw e;
		}
		
		return totalDiasEjecutados;
	}
	
	
	/**
	 * Devuelve el total de días de duración de un proyecto
	 * @return
	 * @throws Exception
	 */
	public Long getTotalDiasDuraciónProyecto() throws Exception  {
		
		long total=0;
		
		try{
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			total=TimeUnit.MILLISECONDS.toDays(format.parse(this.getFechaFinaliza()).getTime()) 
	    			- TimeUnit.MILLISECONDS.toDays(format.parse(this.getFechaInicia()).getTime());
		}
		catch (Exception e){
			throw e;
		}
		
		return total;
	}
	
	public Proyecto( ){
		super();
	}
	
	public static final String NOMBRE_TABLA 			="tblProyectos";
	public static final String COLUMN_ID				="IdProyecto";
	public static final String COLUMN_ID_CLIENTE		="IdCliente";
	public static final String COLUMN_ID_TIPO_PROYECTO	="IdCatTipoProyecto";
	public static final String COLUMN_ID_EDO_PROYECTO	="IdCatEstadoProyecto";
	public static final String COLUMN_NOMBRE_PROYECTO	="NombreProyecto";
	public static final String COLUMN_ALIAS_PROYECTO	="AliasProyecto";	
	public static final String COLUMN_FECHA_INICIA		="FechaInicia";
	public static final String COLUMN_FECHA_FINALIZA	="FechaFinaliza";
	public static final String COLUMN_MONEDA			="Moneda";
	public static final String COLUMN_MONTO_ORIGINAL	="MontoOriginal";
	public static final String COLUMN_MONTO_MODIFICADO	="MontoModificado";
	public static final String COLUMN_MONTO_PAGADO		="MontoPagado";
	public static final String COLUMN_MONTO_EJECUTADO	="MontoEjecutado";
	public static final String COLUMN_AVANCE_FISICO		="AvanceFisico";
	public static final String COLUMN_AVANCE_FINANCIERO	="AvanceFinanciero";
	public static final String COLUMN_LATITUD			="Latitud";
	public static final String COLUMN_LONGITUD			="Longitud";
	public static final String COLUMN_ALTITUD			="Altitud";
	public static final String COLUMN_TIPO_GESTION		="TipoGestion";
	public static final String COLUMN_DESCRIPCION		="Descripcion";
	
	public static String[] Columns= {COLUMN_ID,COLUMN_ID_CLIENTE,COLUMN_ID_TIPO_PROYECTO,
		COLUMN_ID_EDO_PROYECTO,COLUMN_ID_EDO_PROYECTO,COLUMN_NOMBRE_PROYECTO,COLUMN_ALIAS_PROYECTO,
		COLUMN_FECHA_INICIA,COLUMN_FECHA_FINALIZA,COLUMN_MONEDA,COLUMN_MONTO_ORIGINAL,
		COLUMN_MONTO_MODIFICADO,COLUMN_LATITUD,COLUMN_LONGITUD,COLUMN_ALTITUD,COLUMN_TIPO_GESTION};
	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID 					+ " integer not null, "
			+ COLUMN_ID_CLIENTE 			+ " integer not null, "
			+ COLUMN_ID_TIPO_PROYECTO 		+ " integer not null, "
			+ COLUMN_ID_EDO_PROYECTO 		+ " integer not null ,"
			+ COLUMN_NOMBRE_PROYECTO 		+ " text not null, "
			+ COLUMN_ALIAS_PROYECTO 		+ " text null, "
			+ COLUMN_FECHA_INICIA 			+ " text null ,"
			+ COLUMN_FECHA_FINALIZA 		+ " text null, "
			+ COLUMN_MONEDA 				+ " text not null, "
			+ COLUMN_MONTO_ORIGINAL 		+ " real null ,"
			+ COLUMN_MONTO_MODIFICADO 		+ " real null, "
			+ COLUMN_MONTO_PAGADO 			+ " real null, "
			+ COLUMN_MONTO_EJECUTADO 		+ " real null, "
			+ COLUMN_AVANCE_FISICO 			+ " real null, "
			+ COLUMN_AVANCE_FINANCIERO 		+ " real null, "
			+ COLUMN_LATITUD 				+ " real not null, "
			+ COLUMN_LONGITUD 				+ " real not null ,"
			+ COLUMN_ALTITUD 				+ " real not null ,"
			+ COLUMN_TIPO_GESTION 			+ " integer not null ,"
			+ COLUMN_DESCRIPCION	 		+ " text null, "
			+ "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID +  "), "
			+ "FOREIGN KEY ( " + COLUMN_ID_CLIENTE + " ) REFERENCES " + Cliente.NOMBRE_TABLA + "(" + Cliente.COLUMN_ID + "), "  
			+ "FOREIGN KEY ( " + COLUMN_ID_EDO_PROYECTO + " ) REFERENCES " + EstadoProyecto.NOMBRE_TABLA + "(" + EstadoProyecto.COLUMN_ID + "), "  
			+ "FOREIGN KEY ( " + COLUMN_ID_TIPO_PROYECTO + " ) REFERENCES " + TipoProyecto.NOMBRE_TABLA + "(" + TipoProyecto.COLUMN_ID + ")"  
			+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    Log.w(Proyecto.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	  }
	
	public static void insertarProyecto(Context ctx, 
			int IdProyecto,
			int IdCliente,
			int IdCatTipoProyecto,
			int IdCatEstadoProyecto,
			String NombreProyecto,
			String AliasProyecto,
			String FechaInicia,
			String FechaFinaliza,
			String Moneda,
			Double MontoOriginal,
			Double MontoModificado,
			Double Latitud,
			Double Longitud,
			Double Altitud,
			int TipoGestion,
			String Descripcion){
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, 				IdProyecto);
		values.put(COLUMN_ID_CLIENTE, 		IdCliente);
		values.put(COLUMN_ID_TIPO_PROYECTO, IdCatTipoProyecto);
		values.put(COLUMN_ID_EDO_PROYECTO, 	IdCatEstadoProyecto);
		values.put(COLUMN_NOMBRE_PROYECTO, 	NombreProyecto);
		values.put(COLUMN_ALIAS_PROYECTO, 	AliasProyecto);
		values.put(COLUMN_FECHA_INICIA, 	FechaInicia);
		values.put(COLUMN_FECHA_FINALIZA, 	FechaFinaliza);
		values.put(COLUMN_MONEDA, 			Moneda);
		values.put(COLUMN_MONTO_ORIGINAL, 	MontoOriginal);
		values.put(COLUMN_MONTO_MODIFICADO, MontoModificado);
		values.put(COLUMN_LATITUD, 			Latitud);
		values.put(COLUMN_LONGITUD, 		Longitud);
		values.put(COLUMN_ALTITUD, 			Altitud);
		values.put(COLUMN_TIPO_GESTION, 	TipoGestion);
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
					Proyecto.class.getSimpleName(), "insertarProyecto", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
	
	}
	
	
		//Devuelve el registro de un proyecto
		public static Proyecto  obtenerProyecto(Context ctx, int IdCliente, int IdProyecto ){
			
			DAL w = new DAL(ctx);
			Proyecto p = null;
			Cursor c = null;
			String[] columns={Proyecto.COLUMN_ID_CLIENTE, Proyecto.COLUMN_ID, 
					Proyecto.COLUMN_NOMBRE_PROYECTO, Proyecto.COLUMN_FECHA_INICIA,
					Proyecto.COLUMN_ALIAS_PROYECTO};
			
			String where=Proyecto.COLUMN_ID_CLIENTE + "=" + String.valueOf(IdCliente)
					+ " AND "  + Proyecto.COLUMN_ID + "=" + String.valueOf(IdProyecto);
			try{
				
				 c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);
			
				 if(c.moveToFirst()){
					do {
						p=new Proyecto();
						p.idCliente=c.getInt(c.getColumnIndexOrThrow(Proyecto.COLUMN_ID_CLIENTE));
						p.idProyecto=c.getInt(c.getColumnIndexOrThrow(Proyecto.COLUMN_ID));
						p.nombreProyecto=c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_NOMBRE_PROYECTO));
						p.aliasProyecto=c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_ALIAS_PROYECTO));
						//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						//Date d = sdf.parse(c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_FECHA_INICIA)));
						//p.fechaInicia=d;
					}
					while(c.moveToNext());
					c.close();
				 }
			}
			catch (Exception e){
				ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
						Proyecto.class.getSimpleName(), "obtenerTodosProyectos", 
	                    null, null);
			}
			finally{
				//w.cerrarDb();
			}
			return p;
		}
	
		
	public static TipoGestion ObtenerTipoGestionProyecto(Context ctx, int IdCliente, int IdProyecto ){
		
		DAL w = new DAL(ctx);
		TipoGestion tGestion = null;
		Cursor c = null;
		String[] columns={Proyecto.COLUMN_TIPO_GESTION};
		
		String where=Proyecto.COLUMN_ID_CLIENTE + "=" + String.valueOf(IdCliente)
				+ " AND "  + Proyecto.COLUMN_ID + "=" + String.valueOf(IdProyecto);
		
		try{
			
			 c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);
		
			 if(c.moveToFirst()){
				do {
					tGestion=(c.getInt(c.getColumnIndexOrThrow(Proyecto.COLUMN_TIPO_GESTION))==1)?
							TipoGestion.Por_Cantidades:TipoGestion.Por_Porcentajes;
				}
				while(c.moveToNext());
				c.close();
			 }
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					Proyecto.class.getSimpleName(), "obtenerTodosProyectos", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		return tGestion;		

	}
	
	//Devuelve todos los proyectos registrados en la base de datos
	public static ArrayList<Proyecto>  obtenerTodosProyectos(Context ctx){
		
		DAL w = new DAL(ctx);
		ArrayList<Proyecto> proyectos = new ArrayList<Proyecto>();
		Proyecto p;
		EstadoProyecto eProyecto;
		TipoProyecto tProyecto;
		Cursor c = null;
		
		try{
			
			 c = w.getRow("Select * from " + NOMBRE_TABLA);

			 if(c.moveToFirst()){
				
				do {
					 
					p=new Proyecto();
					p.setIdCliente(c.getInt(c.getColumnIndexOrThrow(Proyecto.COLUMN_ID_CLIENTE)));
					p.setIdProyecto(c.getInt(c.getColumnIndexOrThrow(Proyecto.COLUMN_ID)));
					p.setNombreProyecto(c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_NOMBRE_PROYECTO)));
					p.setAliasProyecto(c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_ALIAS_PROYECTO)));
					p.setMontoOriginal(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_MONTO_ORIGINAL)));
					p.setMontoModificado(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_MONTO_MODIFICADO)));
					p.setMontoPagado(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_MONTO_PAGADO)));
					p.setMontoEjecutado(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_MONTO_EJECUTADO)));
					p.setAvanceFisico(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_AVANCE_FISICO)));
					p.setAvanceFinanciero(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_AVANCE_FINANCIERO)));					
					p.setAltitud(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_ALTITUD)));
					p.setLatitud(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_LATITUD)));
					p.setLongitud(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_LONGITUD)));
					p.setFechaInicia(c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_FECHA_INICIA)));
					p.setFechaFinaliza(c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_FECHA_FINALIZA)));
					p.setMoneda(c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_MONEDA)));
					p.setDescripcion(c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_DESCRIPCION)));
					tipoGestion=((c.getInt(c.getColumnIndexOrThrow(Proyecto.COLUMN_TIPO_GESTION)))==1)?TipoGestion.Por_Cantidades: TipoGestion.Por_Porcentajes;
					
					//Obtener descripción estado del proyecto
					eProyecto=new EstadoProyecto();
					p.setIdCatEstadoProyecto(c.getInt
							(c.getColumnIndexOrThrow(Proyecto.COLUMN_ID_EDO_PROYECTO)));
					p.setEdoProyectoDesc(eProyecto.ObtenerEstadoProyecto(ctx, p.getIdCatEstadoProyecto())
							.getDescripcionEstado());
					
					//Obtener descripción estado del proyecto
					tProyecto=new TipoProyecto();
					p.setIdCatTipoProyecto(c.getInt(c.getColumnIndexOrThrow(Proyecto.COLUMN_ID_TIPO_PROYECTO)));
					p.setTipoProyectoDesc(tProyecto.ObtenerTipoProyecto(ctx, p.getIdCatTipoProyecto()).getNombreTipoProyecto());
					
					
					proyectos.add(p);
					p=null;
					
					
					/*try {
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
						Date result  = df.parse(c.getString(c.getColumnIndexOrThrow(Proyecto.COLUMN_FECHA_INICIA)));
						df = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
						p.setFechaInicia(df.format(result));
						
					}
					catch(Exception ex){
						Log.w("exception",ex.getMessage());
					}*/

					
					
				}
				while(c.moveToNext());
				c.close();
			 }
			
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					Proyecto.class.getSimpleName(), "obtenerTodosProyectos", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		return proyectos;
	}

	//Devuelve todos los proyectos registrados en la base de datos
	public static Cursor  obtenerCursorProyectos(Context ctx, CharSequence Cmp){


		DAL w = new DAL(ctx);
		Cursor c = null;

		try{

			String query = "Select "
					+ Proyecto.COLUMN_ID_CLIENTE + ", "
					+ Proyecto.COLUMN_ID + " AS  _id, "
					+ Proyecto.COLUMN_NOMBRE_PROYECTO + ", "
                    + Proyecto.COLUMN_ALIAS_PROYECTO
					+ " FROM " + NOMBRE_TABLA
                    + " WHERE " + Proyecto.COLUMN_NOMBRE_PROYECTO
                    + " LIKE '%"  + Cmp  + "%'";

			c =  w.getRow(query);


        }
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(ctx, e,
					Proyecto.class.getSimpleName(), "obtenerCursorProyectos",
					null, null);
		}
		finally{
            /*if (w.dbIsOpen())
                w.cerrarDb();*/
		}

		return c;

	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public void setNombreProyecto(String NombreProyecto) {
		nombreProyecto = NombreProyecto;
	}

	public String getAliasProyecto() {
		return aliasProyecto;
	}

	public void setAliasProyecto(String AliasProyecto) {
		aliasProyecto = AliasProyecto;
	}
	
	 @Override
	    public String toString() {
	        return "Proyecto ["  
	        		+ ", " + COLUMN_ID  				+ "=" + idProyecto
	        		+ ", " + COLUMN_ID_TIPO_PROYECTO   	+ "=" + idCatTipoProyecto 
	        		+ ", " + COLUMN_ID_EDO_PROYECTO   	+ "=" + idCatEstadoProyecto 
	        		+ ", " + COLUMN_NOMBRE_PROYECTO   	+ "=" + nombreProyecto 
	        		+ ", " + COLUMN_ALIAS_PROYECTO   	+ "=" + aliasProyecto 
	        		+ ", " + COLUMN_FECHA_INICIA   		+ "=" + fechaInicia 
	        		+ ", " + COLUMN_FECHA_FINALIZA   	+ "=" + fechaFinaliza 
	        		+ ", " + COLUMN_MONEDA   			+ "=" + moneda 
	        		+ ", " + COLUMN_MONTO_ORIGINAL   	+ "=" + montoOriginal
	        		+ ", " + COLUMN_MONTO_MODIFICADO   	+ "=" + montoModificado
					+ ", " + COLUMN_MONTO_PAGADO 		+ "=" + montoPagado
					+ ", " + COLUMN_MONTO_EJECUTADO 	+ "=" + montoEjecutado
					+ ", " + COLUMN_AVANCE_FISICO 		+ "=" + avanceFisico
					+ ", " + COLUMN_AVANCE_FINANCIERO 	+ "=" + avanceFinanciero
	        		+ ", " + COLUMN_LATITUD   			+ "=" + latitud
	        		+ ", " + COLUMN_LONGITUD   			+ "=" + longitud
	        		+ ", " + COLUMN_ALTITUD   			+ "=" + altitud
	        		+ ", " + COLUMN_TIPO_GESTION		+ "=" + tipoGestion
	        		+ "]";
	    }





}
