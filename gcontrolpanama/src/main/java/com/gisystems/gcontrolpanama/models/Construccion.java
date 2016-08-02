package com.gisystems.gcontrolpanama.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import android.content.ContentValues;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;

import java.io.Serializable;
import java.util.ArrayList;

public class Construccion extends Proyecto  implements Serializable{

	private static final long serialVersionUID = 1L;
	private int idCliente;
	private int idProyecto;
	private int idConstruccion;
	private String descripcion;
	private Double latitud;
	private Double longitud;
	private Double altitud;

	private Double monto;
	private Double porcAvance;

	public int getIdCliente() {return idCliente;}

	public void setIdCliente(int idCliente) {this.idCliente = idCliente;}

	public int getIdProyecto() {return idProyecto;}

	public void setIdProyecto(int idProyecto) {this.idProyecto = idProyecto;}

	public int getIdConstruccion() {return idConstruccion;}

	public void setIdConstruccion(int idConstruccion) {this.idConstruccion = idConstruccion;}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String Descripcion) {
		descripcion = Descripcion;
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

	public void setLongitud(Double longitud) {this.longitud = longitud;}

	public Double getAltitud() {return altitud;}

	public void setAltitud(Double altitud) {this.altitud = altitud;}

	public Double getMonto() {return monto;}

	public void setMonto(Double Monto) {monto = Monto;}

	public Double getPorcAvance() {return porcAvance;}

	public void setPorcAvance(Double porcAvance) {this.porcAvance = porcAvance;}


	//****** Nombre de tabla y columnas


	/*private float cantidadContratada;
	private float precioUnitario;
	private float montoContratado;
	private float montoAvance;
	private float cantidadEjecutada;
	private float porcAvance;*/
	public static final String NOMBRE_TABLA 				="tblConstrucciones";
	public static final String COLUMN_ID_CLIENTE			="IdCliente";
	public static final String COLUMN_ID_PROYECTO			="IdProyecto";
	public static final String COLUMN_ID_CONSTRUCCION		="IdConstruccion";
	public static final String COLUMN_DESCRIPCION			="Descripcion";

	public static final String COLUMN_LATITUD			   ="Latitud";
	public static final String COLUMN_LONGITUD			   ="Longitud";
	public static final String COLUMN_ALTITUD			   ="Altitud";

	public static final String COLUMN_MONTO			        ="Monto";
	public static final String COLUMN_PORC_AVANCE			="PorcentajeAvance";

	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_ID_CLIENTE 					+ " integer not null, "
			+ COLUMN_ID_PROYECTO 					+ " integer not null, "
			+ COLUMN_ID_CONSTRUCCION				+ " integer not null, "
			+ COLUMN_DESCRIPCION 					+ " text not null, "
			+ COLUMN_LATITUD 				        + " real not null, "
			+ COLUMN_LONGITUD 				        + " real not null ,"
			+ COLUMN_ALTITUD 				        + " real not null ,"
			+ COLUMN_MONTO 					        + " real null, "
			+ COLUMN_PORC_AVANCE 					+ " real null, "

			+ "PRIMARY KEY ( " + COLUMN_ID_CLIENTE + ", "  + COLUMN_ID_PROYECTO + ", "  + COLUMN_ID_CONSTRUCCION +  "), "
			+ "FOREIGN KEY ( " + COLUMN_ID_CLIENTE +  ", "  + COLUMN_ID_PROYECTO   + " ) REFERENCES " + Proyecto.NOMBRE_TABLA + "("   + Proyecto.COLUMN_ID_CLIENTE + "," + Proyecto.COLUMN_ID + ")"
			+ ")";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    Log.w(Construccion.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	  }
	
	
	public static void insertarConstruccion(Context ctx,
			int IdCliente,
			int IdProyecto,
			int IdConstruccion,
			String Descripcion,
			Double Latitud,
			Double Longitud,
			Double Altitud,
			Double Monto,
			Double PorcentajeAvance){

		ContentValues values = new ContentValues();
		values.put(COLUMN_ID_CLIENTE, 			IdCliente);
		values.put(COLUMN_ID_PROYECTO, 			IdProyecto);
		values.put(COLUMN_ID_CONSTRUCCION, 	    IdConstruccion);
		values.put(COLUMN_DESCRIPCION, 			Descripcion);
		values.put(COLUMN_LATITUD, 			    Latitud);
		values.put(COLUMN_LONGITUD, 		    Longitud);
		values.put(COLUMN_ALTITUD, 			    Altitud);
		values.put(COLUMN_MONTO, 		        Monto);
		values.put(COLUMN_PORC_AVANCE, 			PorcentajeAvance);

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
					Construccion.class.getSimpleName(), "insertarConstruccion",
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
	}

	
/*
		 * Devuelve una actividad registrada en la base de datos
		 * @param ctx
		 * @param idCliente
		 * @param idProyecto
		 * @param idConstruccion
		 * @return */

		public static Construccion obtenerConstruccion(Context ctx,
				int idCliente, int idProyecto, int idConstruccion){
			
			DAL w = new DAL(ctx);
			Construccion constr = null;

			String[] columns={Construccion.COLUMN_ID_CONSTRUCCION,
					Construccion.COLUMN_ID_CLIENTE, Construccion.COLUMN_ID_PROYECTO,
					Construccion.COLUMN_DESCRIPCION,
					Construccion.COLUMN_LATITUD,
					Construccion.COLUMN_LONGITUD,
					Construccion.COLUMN_ALTITUD,
					Construccion.COLUMN_MONTO,
					Construccion.COLUMN_PORC_AVANCE};
			
			String where= Construccion.COLUMN_ID_CLIENTE + "=" + String.valueOf(idCliente)
					   + " and " +  Construccion.COLUMN_ID_PROYECTO + "=" + String.valueOf(idProyecto)
					    + " and " +  Construccion.COLUMN_ID_CONSTRUCCION + "=" + String.valueOf(idConstruccion);
			
			
			Cursor c = null;
			
			try{
				Proyecto p=null;
				 c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);

				 if(c.moveToFirst()){
					
					do {
						
						constr=new Construccion();
						constr.setIdCliente(c.getInt(c.getColumnIndexOrThrow(Construccion.COLUMN_ID_CLIENTE)));
						constr.setIdProyecto(c.getInt(c.getColumnIndexOrThrow(Construccion.COLUMN_ID_PROYECTO)));
						constr.setIdConstruccion(c.getInt(c.getColumnIndexOrThrow(Construccion.COLUMN_ID_CONSTRUCCION)));
						constr.setDescripcion(c.getString(c.getColumnIndexOrThrow(Construccion.COLUMN_DESCRIPCION)));
						constr.setLatitud(c.getDouble(c.getColumnIndexOrThrow(Construccion.COLUMN_LATITUD)));
						constr.setLongitud(c.getDouble(c.getColumnIndexOrThrow(Construccion.COLUMN_LONGITUD)));
						constr.setAltitud(c.getDouble(c.getColumnIndexOrThrow(Construccion.COLUMN_ALTITUD)));
						constr.setMonto(c.getDouble(c.getColumnIndexOrThrow(Construccion.COLUMN_MONTO)));
						constr.setPorcAvance(c.getDouble(c.getColumnIndexOrThrow(Construccion.COLUMN_PORC_AVANCE)));
						//Obtener Datos del Proyecto
						p=Proyecto.obtenerProyecto(ctx, constr.getIdCliente(), constr.getIdProyecto());
						if(p!=null){
							constr.setNombreProyecto(p.getNombreProyecto());
							constr.setAliasProyecto(p.getAliasProyecto());
						}

					}
					while(c.moveToNext());
					c.close();
				 }
				
			}
			catch (Exception e){
				ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
						Construccion.class.getSimpleName(), "obtenerConstrPorClienteProyecto",
	                    null, null);
			}
			finally{
				//w.cerrarDb();
			}
			
			return constr;
		}
	

	
	//Devuelve las construcciones de un proyecto especifíco
	public static ArrayList<Construccion>  obtenerConstrPorClienteProyecto(Context ctx, int idCliente, int idProyecto){
		
		DAL w = new DAL(ctx);
		ArrayList<Construccion> construccion = new ArrayList<Construccion>();
		Construccion constr;
		
		
		
		String[] columns={Construccion.COLUMN_ID_CONSTRUCCION,
				Construccion.COLUMN_ID_CLIENTE,
				Construccion.COLUMN_ID_PROYECTO,
				Construccion.COLUMN_DESCRIPCION,
				Construccion.COLUMN_LATITUD,
				Construccion.COLUMN_LONGITUD,
				Construccion.COLUMN_ALTITUD,
				Construccion.COLUMN_MONTO,
				Construccion.COLUMN_PORC_AVANCE};
		
		String where= Construccion.COLUMN_ID_CLIENTE + "=" + String.valueOf(idCliente)
				   + " and " +  Construccion.COLUMN_ID_PROYECTO + "=" + String.valueOf(idProyecto);
		
		
		Cursor c = null;
		
		try{
			
			 c = w.getRowsByFilter(NOMBRE_TABLA,  columns,where ,null,null);

			 if(c.moveToFirst()){
				
				do {
					
					constr=new Construccion();
					constr.setIdConstruccion(c.getInt(c.getColumnIndexOrThrow(Construccion.COLUMN_ID_CONSTRUCCION)));
					constr.setIdCliente(c.getInt(c.getColumnIndexOrThrow(Construccion.COLUMN_ID_CLIENTE)));
					constr.setIdProyecto(c.getInt(c.getColumnIndexOrThrow(Construccion.COLUMN_ID_PROYECTO)));
					constr.setDescripcion(c.getString(c.getColumnIndexOrThrow(Construccion.COLUMN_DESCRIPCION)));
					constr.setLatitud(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_LATITUD)));
					constr.setLongitud(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_LONGITUD)));
					constr.setAltitud(c.getDouble(c.getColumnIndexOrThrow(Proyecto.COLUMN_ALTITUD)));
					constr.setMonto(c.getDouble(c.getColumnIndexOrThrow(Construccion.COLUMN_MONTO)));
					constr.setPorcAvance(c.getDouble(c.getColumnIndexOrThrow(Construccion.COLUMN_PORC_AVANCE)));
					construccion.add(constr);
					constr=null;

				}
				while(c.moveToNext());
				c.close();
			 }
			
		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
					Construccion.class.getSimpleName(), "obtenerConstrPorClienteProyecto",
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		return construccion;
	}


	public static Cursor obtenerCursorConstrucciones(Context ctx, int IdCliente, int IdProyecto, CharSequence Cmp){

		DAL w = new DAL(ctx);
		Cursor c = null;

		try{
			String query = "Select "
					+ Construccion.COLUMN_ID_CLIENTE + ", "
					+ Construccion.COLUMN_ID_PROYECTO + ", "
					+ Construccion.COLUMN_ID_CONSTRUCCION +  " AS  _id, "
					+ Construccion.COLUMN_DESCRIPCION
					+ " FROM " + NOMBRE_TABLA
					+ " WHERE "
                    + Construccion.COLUMN_ID_CLIENTE + " = " + String.valueOf(IdCliente)
                    + " and " + Construccion.COLUMN_ID_PROYECTO + " = " + String.valueOf(IdProyecto)
                    + " and " + Construccion.COLUMN_DESCRIPCION + " LIKE '%"  + Cmp  + "%'";

			c =  w.getRow(query);

		}
		catch (Exception e){
			ManejoErrores.registrarError_MostrarDialogo(ctx, e,
					Construccion.class.getSimpleName(), "obtenerCursorConstruccion",
					null, null);
		}
		finally{
			//w.cerrarDb();
		}

		return c;

	}

		
	@Override
    public String toString() {
        return "Construccion ["
        		+ ", " + Construccion.COLUMN_ID_CLIENTE  			+ "=" + idCliente
        		+ ", " + Construccion.COLUMN_ID_PROYECTO   		    + "=" + idProyecto
        		+ ", " + Construccion.COLUMN_ID_CONSTRUCCION   		+ "=" + idConstruccion
        		+ ", " + Construccion.COLUMN_DESCRIPCION  			+ "=" + descripcion
				+ ", " + Construccion.COLUMN_LATITUD   			    + "=" + latitud
				+ ", " + Construccion.COLUMN_LONGITUD   			+ "=" + longitud
				+ ", " + Construccion.COLUMN_ALTITUD   			    + "=" + altitud
        		+ ", " + Construccion.COLUMN_MONTO		  	        + "=" + monto
        		+ ", " + Construccion.COLUMN_PORC_AVANCE   		    + "=" + porcAvance
        		+ "]";
    } 
	
}
