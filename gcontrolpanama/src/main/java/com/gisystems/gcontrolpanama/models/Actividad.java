package com.gisystems.gcontrolpanama.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Actividad extends Construccion  implements Serializable{

	private static final long serialVersionUID = 1L;
	private int idCliente;
	private int idProyecto;
	private int idConstruccion;
	private int idActividad;
	private int idAsignacionUnidad;
	private String codigoInstitucional;
	private String descripcion;
	
	private Double cantidadContratada;
	//private Double precioUnitario;
	//private Double montoContratado;
	//private Double montoAvance;
	private Double cantidadEjecutada;
	private Double porcAvance;
	
	private String fechaActualizacion;
	//private int ordenActividad;



	public int getIdCliente() {return idCliente;}

	public void setIdCliente(int idCliente) {this.idCliente = idCliente;}

	public int getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(int idProyecto) {
		this.idProyecto = idProyecto;
	}

	public int getIdConstruccion() {
		return idConstruccion;
	}

	public void setIdConstruccion(int idConstruccion) {this.idConstruccion = idConstruccion;}

	public int getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(int idActividad) {this.idActividad = idActividad;}

	public int getIdAsignacionUnidad() {return idAsignacionUnidad;}

	public void setIdAsignacionUnidad(int idAsignacionUnidad) {this.idAsignacionUnidad = idAsignacionUnidad;}

	public String getCodigoInstitucional() {return codigoInstitucional;}

	public void setCodigoInstitucional(String codigoInstitucional) {this.codigoInstitucional = codigoInstitucional;}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String Descripcion) {descripcion = Descripcion;}

	/*public Double getMontoAvance() {
		return montoAvance;
	}

	public void setMontoAvance(Double MontoAvance) {
		montoAvance = MontoAvance;
	}

	public Double getMontoContratado() {
		return montoContratado;
	}

	public void setMontoContratado(Double MontoContratado) {
		montoContratado = MontoContratado;
	}*/


	public Double getPorcAvance() {return porcAvance;}

	public void setPorcAvance(Double porcAvance) {this.porcAvance = porcAvance;}

		/*public int getOrdenActividad() {
		return ordenActividad;
	}

	public void setOrdenActividad(int ordenActividad) {
		this.ordenActividad = ordenActividad;
	}
*/
	public Double getCantidadContratada() {return cantidadContratada;}

	public void setCantidadContratada(Double cantidadContratada) {this.cantidadContratada = cantidadContratada;}

	public Double getCantidadEjecutada() {return cantidadEjecutada;}

	public void setCantidadEjecutada(Double cantidadEjecutada) {this.cantidadEjecutada = cantidadEjecutada;}

	public String getFechaActualizacion() {return fechaActualizacion;}

	public void setFechaActualizacion(String fechaActualizacion) {this.fechaActualizacion = fechaActualizacion;}


	//****** Nombre de tabla y columnas
	

	/*public Double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
*/



	public static final String NOMBRE_TABLA 				="tblAsignActConstrucciones";
	public static final String COLUMN_ID_CLIENTE			="IdCliente";
	public static final String COLUMN_ID_PROYECTO			="IdProyecto";
	public static final String COLUMN_ID_CONSTRUCCION		="IdConstruccion";
	public static final String COLUMN_ID					="IdActividad";
	public static final String COLUMN_ID_ASIGNACION_UNIDAD  ="IdAsignacionUnidad";
	public static final String COLUMN_COD_INSTITUCIONAL 	="CodigoInstitucional";
	//public static final String COLUMN_DESCRIPCION			="Descripcion";
	
	public static final String COLUMN_CANTIDAD_CONTRATADA	="CantidadContratada";
	public static final String COLUMN_CANTIDAD_EJECUTADA	="CantidadEjecutada";
	public static final String COLUMN_PORC_AVANCE	        ="PorcentajeAvance";

	public static final String COLUMN_FECHA_ACTUALIZACION	="FechaActualizacion";

	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID_CLIENTE 					+ " integer not null, "
			+ COLUMN_ID_PROYECTO 					+ " integer not null, "
			+ COLUMN_ID_CONSTRUCCION				+ " integer not null, "
			+ COLUMN_ID				                + " integer not null, "
			+ COLUMN_ID_ASIGNACION_UNIDAD 			+ " integer not null, "
			+ COLUMN_COD_INSTITUCIONAL				+ " text not null, "
			+ COLUMN_CANTIDAD_CONTRATADA 			+ " real null, "
			+ COLUMN_CANTIDAD_EJECUTADA 			+ " real not null ,"
			+ COLUMN_PORC_AVANCE			        + " real not null ,"
			+ COLUMN_FECHA_ACTUALIZACION 			+ " text null, "
			+ "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_PROYECTO + ", "  + COLUMN_ID_CONSTRUCCION + ", "  + COLUMN_ID +  "), "
			+ "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_PROYECTO   + " ) REFERENCES " + Proyecto.NOMBRE_TABLA + "("   + Proyecto.COLUMN_ID_CLIENTE + "," + Proyecto.COLUMN_ID + "), "
			//+ "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_PROYECTO   +  ", "  + COLUMN_ID  + " ) REFERENCES " + ActividadDescripcion.NOMBRE_TABLA + "(" + ActividadDescripcion.COLUMN_ID_CLIENTE + "," + ActividadDescripcion.COLUMN_ID_PROYECTO +  "," + ActividadDescripcion.COLUMN_ID +  " ), "
			+ "FOREIGN KEY ( " + COLUMN_ID_ASIGNACION_UNIDAD +   " ) REFERENCES " + TipoUnidad.NOMBRE_TABLA + "("   + TipoUnidad.COLUMN_ID + ") "
			+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    Log.w(Actividad.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	  }
	
	
	/*public static void insertarActividad(Context ctx, 
			int IdCliente,
			int IdProyecto,
			int IdActiviad,
			int IdAsignacionUnidad,
			String Descripcion,
			Double MontoAvance,
			Double MontoContratado,
			String FechaActualizacion,
			Double PorcentajeAvance,
			int OrdenActividad){
		
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID_CLIENTE, 			IdCliente);
		values.put(COLUMN_ID_PROYECTO, 			IdProyecto);
		values.put(COLUMN_ID, 					IdActiviad);
		values.put(COLUMN_ID_ASIGNACION_UNIDAD, IdAsignacionUnidad);
		values.put(COLUMN_DESCRIPCION, 			Descripcion);
		values.put(COLUMN_MONTO_AVANCE, 		MontoAvance);
		values.put(COLUMN_MONTO_CONTRATADO, 	MontoContratado);
		values.put(COLUMN_FECHA_ACTUALIZACION, 	FechaActualizacion);
		values.put(COLUMN_PORC_AVANCE, 			PorcentajeAvance);
		values.put(COLUMN_ORDEN_ACTIVIDAD, 		OrdenActividad);
		
		DAL w = new DAL(ctx);
		try{
			w.iniciarTransaccion();
			w.insertRow(NOMBRE_TABLA, values);
			w.finalizarTransaccion(true);
		}
			catch (Exception e)
		{
			w.finalizarTransaccion(false);
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					Actividad.class.getSimpleName(), "insertarActividad", 
                    null, null);
		}
		finally{
			w.cerrarDb();
		}
	}*/
	
	
		/**
		 * Devuelve una actividad registrada en la base de datos
		 * @param ctx
		 * @param idCliente
		 * @param idProyecto
		 * @param idConstruccion
		 * @param idActividad
		 * @return
		 */
		public static Actividad  obtenerActividad(Context ctx, 
				int idCliente,  int idProyecto, int idConstruccion,   int idActividad){
			
			DAL w = new DAL(ctx);
			Actividad activ = null;
			ActividadDescripcion Desc;

			String[] columns={Actividad.COLUMN_ID, 
					Actividad.COLUMN_ID_CLIENTE,
					Actividad.COLUMN_ID_PROYECTO,
					Actividad.COLUMN_ID_CONSTRUCCION,
					Actividad.COLUMN_COD_INSTITUCIONAL,
					Actividad.COLUMN_CANTIDAD_CONTRATADA, 
					Actividad.COLUMN_CANTIDAD_EJECUTADA,
					Actividad.COLUMN_PORC_AVANCE,
					Actividad.COLUMN_FECHA_ACTUALIZACION};
			
			String where=Actividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(idCliente)
					    + " and " +  Actividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(idProyecto)
					    + " and " +  Actividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(idConstruccion)
					    + " and " +  Actividad.COLUMN_ID + "=" + String.valueOf(idActividad);

			Cursor c = null;
			
			try{
				Proyecto p=null;
				 c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);

				 if(c.moveToFirst()){
					
					do {
						
						activ=new Actividad();
						activ.setIdActividad(c.getInt(c.getColumnIndexOrThrow(Actividad.COLUMN_ID)));
						activ.setIdCliente(c.getInt(c.getColumnIndexOrThrow(Actividad.COLUMN_ID_CLIENTE)));
						activ.setIdProyecto(c.getInt(c.getColumnIndexOrThrow(Actividad.COLUMN_ID_PROYECTO)));
						activ.setIdConstruccion(c.getInt(c.getColumnIndexOrThrow(Actividad.COLUMN_ID_CONSTRUCCION)));
						activ.setCodigoInstitucional(c.getString(c.getColumnIndexOrThrow(Actividad.COLUMN_COD_INSTITUCIONAL)));

						//Obtener descripción de Actividad
						Desc=new ActividadDescripcion();
						activ.setDescripcion(Desc.ObtenerDescripcionActividad(ctx, activ.getIdCliente(), activ.getIdProyecto(), activ.getIdActividad()).getDescripcion());

						//activ.setDescripcion(c.getString(c.getColumnIndexOrThrow(Actividad.COLUMN_DESCRIPCION)));
						activ.setFechaActualizacion(c.getString(c.getColumnIndexOrThrow(Actividad.COLUMN_FECHA_ACTUALIZACION)));
						if (activ.getFechaActualizacion().compareToIgnoreCase("null")==0){
								activ.setFechaActualizacion("");
						}
						activ.setCantidadContratada(c.getDouble(c.getColumnIndexOrThrow(Actividad.COLUMN_CANTIDAD_CONTRATADA)));
						activ.setCantidadEjecutada(c.getDouble(c.getColumnIndexOrThrow(Actividad.COLUMN_CANTIDAD_EJECUTADA)));
						activ.setPorcAvance(c.getDouble(c.getColumnIndexOrThrow(Actividad.COLUMN_PORC_AVANCE)));

						//Obtener Datos del Proyecto
						p= obtenerProyecto(ctx, activ.getIdCliente(), activ.getIdProyecto());
						if(p!=null){
							activ.setNombreProyecto(p.getNombreProyecto());
							activ.setAliasProyecto(p.getAliasProyecto());
						}

					}
					while(c.moveToNext());
					c.close();
				 }
				
			}
			catch (Exception e){
				ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
						Actividad.class.getSimpleName(), "obtenerActivsPorClienteProyecto", 
	                    null, null);
			}
			finally{
				//w.cerrarDb();
			}
			
			return activ;
		}
	
	
	
	//Devuelve las actividades de un proyecto especifíco
	public static ArrayList<Actividad>  obtenerActivsPorClienteProyectoConstruccion(Context ctx, int idCliente, int idProyecto, int idConstruccion){
		
		DAL w = new DAL(ctx);
		ArrayList<Actividad> activs = new ArrayList<Actividad>();
		Actividad activ;
		ActividadDescripcion Desc;

		
		String[] columns={Actividad.COLUMN_ID, 
				Actividad.COLUMN_ID_CLIENTE,
				Actividad.COLUMN_ID_PROYECTO,
				Actividad.COLUMN_ID_CONSTRUCCION,
				Actividad.COLUMN_COD_INSTITUCIONAL,
				Actividad.COLUMN_CANTIDAD_CONTRATADA,
				Actividad.COLUMN_CANTIDAD_EJECUTADA,
				Actividad.COLUMN_PORC_AVANCE,
				Actividad.COLUMN_FECHA_ACTUALIZACION};
		
		String where=Actividad.COLUMN_ID_CLIENTE + "=" + String.valueOf(idCliente)
				   + " and " +  Actividad.COLUMN_ID_PROYECTO + "=" + String.valueOf(idProyecto)
				   + " and " +  Actividad.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(idConstruccion);
		
		
		Cursor c = null;
		
		try{
			
			 c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);
			 Log.w("Total de actividades de la construccion, "
					 + "cliente:" + String.valueOf(idCliente)
					 + " proyecto:" + String.valueOf(idProyecto)  + " construccion:" +  String.valueOf(idConstruccion),String.valueOf(c.getCount()));
			 if(c.moveToFirst()){
				
				do {
					
					activ=new Actividad();
					activ.setIdActividad(c.getInt(c.getColumnIndexOrThrow(Actividad.COLUMN_ID)));
					activ.setIdCliente(c.getInt(c.getColumnIndexOrThrow(Actividad.COLUMN_ID_CLIENTE)));
					activ.setIdProyecto(c.getInt(c.getColumnIndexOrThrow(Actividad.COLUMN_ID_PROYECTO)));
					activ.setIdConstruccion(c.getInt(c.getColumnIndexOrThrow(Actividad.COLUMN_ID_CONSTRUCCION)));
					activ.setCodigoInstitucional(c.getString(c.getColumnIndexOrThrow(Actividad.COLUMN_COD_INSTITUCIONAL)));

					//Obtener descripción de Actividad
					Desc=new ActividadDescripcion();
					activ.setDescripcion(Desc.ObtenerDescripcionActividad(ctx, activ.getIdCliente(), activ.getIdProyecto(), activ.getIdActividad()).getDescripcion());

					//activ.setDescripcion(c.getString(c.getColumnIndexOrThrow(Actividad.COLUMN_DESCRIPCION)));
					activ.setCantidadContratada(c.getDouble(c.getColumnIndexOrThrow(Actividad.COLUMN_CANTIDAD_CONTRATADA)));
					activ.setCantidadEjecutada(c.getDouble(c.getColumnIndexOrThrow(Actividad.COLUMN_CANTIDAD_EJECUTADA)));
					activ.setPorcAvance(c.getDouble(c.getColumnIndexOrThrow(Actividad.COLUMN_PORC_AVANCE)));
					activ.setFechaActualizacion(c.getString(c.getColumnIndexOrThrow(Actividad.COLUMN_FECHA_ACTUALIZACION)));
					if (activ.getFechaActualizacion().compareToIgnoreCase("null")==0){
							activ.setFechaActualizacion("");
					};
					activs.add(activ);
					activ=null;

				}
				while(c.moveToNext());
				c.close();
			 }
			
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					Actividad.class.getSimpleName(), "obtenerActivsPorClienteProyecto", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		return activs;
	}

		
	@Override
    public String toString() {
        return "Actividad ["  
        		+ ", " + Actividad.COLUMN_ID_CLIENTE  			+ "=" + idCliente
				+ ", " + Actividad.COLUMN_ID_PROYECTO   		+ "=" + idProyecto
				+ ", " + Actividad.COLUMN_ID_CONSTRUCCION 		+ "=" + idConstruccion
				+ ", " + Actividad.COLUMN_ID   					+ "=" + idActividad
				+ ", " + Actividad.COLUMN_COD_INSTITUCIONAL		+ "=" + codigoInstitucional
        		+ ", " + Actividad.COLUMN_CANTIDAD_CONTRATADA  	+ "=" + cantidadContratada
        		+ ", " + Actividad.COLUMN_CANTIDAD_EJECUTADA   	+ "=" + cantidadEjecutada 
        		+ ", " + Actividad.COLUMN_PORC_AVANCE   		+ "=" + porcAvance
				+ ", " + Actividad.COLUMN_FECHA_ACTUALIZACION  	+ "=" + fechaActualizacion

        		+ "]";
    } 
	
}
