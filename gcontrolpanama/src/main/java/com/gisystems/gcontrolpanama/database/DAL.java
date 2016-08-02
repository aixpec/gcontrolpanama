package com.gisystems.gcontrolpanama.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.EventListenerProxy;

public class DAL  {
	
	private  SQLiteDatabase db;
	private  Context context;
	private static ElephantDbHelper helper;

	public DAL(Context context) {
	
		this.context = context;
		if (helper == null) {
			helper = ElephantDbHelper.getInstance(context);
		}

		if (db == null) {
				db =helper.getWritableDatabase();
				db.execSQL("PRAGMA foreign_keys=ON;");
			}
		else {

			while(db.isDbLockedByCurrentThread() ) {
				//db is locked, keep looping
			}

			if (!db.isOpen()){
				db =helper.getWritableDatabase();
				db.execSQL("PRAGMA foreign_keys=ON;");
			}
		}


	}
	
	public void execSQL(String sql) {
		db.execSQL(sql);
	}
	
	public boolean deleteRow(String table, String pkWhere) {
		return db.delete(table, pkWhere, null) > 0;
	}
	
	public Cursor getRow(String query)
	{
	   return db.rawQuery(query, new String [] {});
	}
	
	public Cursor getAllRows(String nombreTabla, String[] columns, String orderBy){

		return db.query(nombreTabla, columns, null, null, null, null, orderBy);
	}
	
	public Cursor getRowsByFilter(String nombreTabla, String[] columns, String selection,
			                      String[] selectionArgs, String orderBy)
	{
	   return db.query(nombreTabla, columns, selection, selectionArgs, null, null, orderBy);
	}
	
	public long insertRow(String table, ContentValues contentValues)
	{
		return db.insert(table, null, contentValues);
	}
	
	public int updateRow(String table, ContentValues contentValues, String pkWhere)
	{
		return db.update(table, contentValues, pkWhere, null);
	}
	
	public void iniciarTransaccion(){
		db.beginTransaction();
	}
	
	public void finalizarTransaccion(boolean transaccionExitosa){
		
		if(transaccionExitosa) 
			db.setTransactionSuccessful();
		
		db.endTransaction();
			 
	}
	
	public void cerrarDb() throws Throwable {

			if(null != helper)
				helper.close();
			if(null != db)
				db.close();

			super.finalize();

	}

	public boolean dbIsOpen(){

		if (db != null){
			return db.isOpen();
		}

		return false;
	}
	
}