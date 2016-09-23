package com.gisystems.gcontrolpanama.models;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.database.DAL;
import com.gisystems.exceptionhandling.ManejoErrores;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class AppValues {

	private boolean sync ;
	private boolean modo_local;	
	private final static String NOMBRE_TABLA		=	"Almacen_APPVALS";
	public static final String COLUMN_SYNC			=	"Sync";
	public static final String COLUMN_MODO_LOCAL	=	"ModoLocal";
	private static Cursor cCursor;
	public static final String LLAVE_SPUSUARIONOMBRE	=	"username";
	public static final String LLAVE_SPUSUARIOPASS		=	"password";
	public static final String LLAVE_SPFIRSTRUN			=	"first_run";
	public static final String LLAVE_SPUSUARIOVALIDO	=	"usuario_valido";
	public static final String LLAVE_SPDEVICEID	        =	"device_id";
    public static final String LLAVE_SPSUSCRIBERID	    =	"suscriber_id";
	
	
	private static final String DATABASE_CREATE="create table "
			+ NOMBRE_TABLA
			+ "("
			+ COLUMN_SYNC 			+ " BOOLEAN null DEFAULT 0, "
			+ COLUMN_MODO_LOCAL 	+ " BOOLEAN null DEFAULT 0 "
			+ ");";

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
	    Log.w(AppValues.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
	    onCreate(database);
	  }

	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	public boolean isSync() {
		return this.sync;
	}
	
	public void setSync(boolean sync) {
		this.sync = sync;
	}
	
	public boolean isModo_local() {
		return this.modo_local;
	}
	
	public void setModo_local(boolean modo_local) {
		this.modo_local = modo_local;
	}	
	
	public static String NombreAppSharedPref(Context ctx)
	{
		return ctx.getString(R.string.sharedPreferencesKey);
	}

	public static void SharedPref_guardarUsuarioNombre(Context ctx, String Valor)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(LLAVE_SPUSUARIONOMBRE,Valor);
		editor.commit();		
	}
	
	public static void SharedPref_guardarUsuarioPassword(Context ctx, String Valor)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(LLAVE_SPUSUARIOPASS,Valor);
		editor.commit();		
	}
		
	public static void SharedPref_guardarValorPrimeraEjecucion(Context ctx, boolean Valor)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(LLAVE_SPFIRSTRUN, Valor);
		editor.commit();		
	}
	
	public static void SharedPref_guardarUsuarioValido(Context ctx, boolean Valor)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(LLAVE_SPUSUARIOVALIDO, Valor);
		editor.commit();		
	}

    public static void SharedPref_guardarDeviceID(Context ctx, String deviceID)
    {
        SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LLAVE_SPDEVICEID, deviceID);
        editor.commit();
    }

    public static void SharedPref_guardarSuscriberID(Context ctx, String suscriberID)
    {
        SharedPreferences prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LLAVE_SPSUSCRIBERID, suscriberID);
        editor.commit();
    }

	public static String SharedPref_obtenerUsuarioNombre(Context ctx){
		return ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE).getString(LLAVE_SPUSUARIONOMBRE, ""); }
	
	public static String SharedPref_obtenerUsuarioPassword(Context ctx){
		return ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE).getString(LLAVE_SPUSUARIOPASS, ""); }
	
	public static boolean SharedPref_obtenerValorEsPrimeraEjecucion(Context ctx){
		return ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE).getBoolean(LLAVE_SPFIRSTRUN, true); }

	public static boolean SharedPref_obtenerValorEsUsuarioValido(Context ctx){
		return ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE).getBoolean(LLAVE_SPUSUARIOVALIDO, false); }

    public static String SharedPref_obtenerDeviceID(Context ctx){
        return ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE).getString(LLAVE_SPDEVICEID, ""); }

    public static String SharedPref_obtenerSuscriberID(Context ctx){
        return ctx.getSharedPreferences(ctx.getString(R.string.sharedPreferencesKey),Context.MODE_PRIVATE).getString(LLAVE_SPSUSCRIBERID, ""); }

	/*Devuelve un valor booleano indicando si la aplicación se encuentra sincronizada o no
	 * @param context Contexto de la aplicación
	 * */
	public static boolean AppEstaSincronizada(Context context){
		AppValues appV=  obtenerAlmacenAPPVALUES(context);
		try{	
			//Si no existen se agrega un registro con valores iniciales
			if (appV!=null) {
				//solo existira un registro
				boolean valor=appV.isSync();
				return valor;						
			}
			else
			{
				Log.w("AppEstaSincronizada","No existen registros en AppValues");
				crearAppsValues(context);
			}	
			
		}
		catch (Exception e) {
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					AppValues.class.getSimpleName(), "AppEstaSincronizada", 
                    null, null);
		}
	
		return false;   	
	}
	
	public static AppValues obtenerAlmacenAPPVALUES(Context context){
		DAL w = new DAL(context);
		AppValues appV = null;
		try{
		
			cCursor= w.getRow("SELECT * FROM " + AppValues.NOMBRE_TABLA);
			
			if (cCursor.moveToFirst()) {
				appV=new AppValues();
				do{
					appV.sync=cCursor.getInt(0)>0;
					appV.modo_local=cCursor.getInt(1)>0;		
				}while(cCursor.moveToNext());
				cCursor.close();
				return appV;	
			}
			else
			{
				return null;
			}
		}
		catch (Exception e){
			return null;
		}
		finally{
			
			//w.cerrarDb();
		}
	
	}
	
	
	/*Devuelve un valor booleano indicando si la aplicación se encuentra sincronizada o no
	/*Crea los valores iniciales de la aplicación
	 * @param context Contexto de la aplicación
	 * */
	public static boolean crearAppsValues(Context context)
	{		
		AppValues appVals =  obtenerAlmacenAPPVALUES(context);	
		DAL w = new DAL(context);
		boolean res=false;
		
		try{
		
			//Si no existen se agrega un registro con valores iniciales
			if (appVals == null) {
				ContentValues values = new ContentValues();	
				values.put(COLUMN_SYNC, false);	
				values.put(COLUMN_MODO_LOCAL, false);
				w.iniciarTransaccion();
				w.insertRow(NOMBRE_TABLA, values);
				w.finalizarTransaccion(true);
			    Log.w("crearAppsValues:","Han sido creados los registros de app values");
			}
			else
			{
				Log.w("crearAppsValues:","Ya existen registros en appvalues");
			}
		
			res=true;
		}
		catch (Exception e) {
			w.finalizarTransaccion(false);
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					AppValues.class.getSimpleName(), "crearAppsValues", 
                    null, null);
			
			res=false;
		}
		finally{
			//w.cerrarDb();
		}
		
		return res;
	
	}
			
	/*Crea los valores iniciales de la aplicación
	 * @param context Contexto de la aplicación
	 * */
	public static void actualizarAppValueModoDesconectado(Context context, boolean esModoDesconectado)
	{		
		DAL w = new DAL(context);
		try{
			ContentValues values = new ContentValues();
			values.put(COLUMN_MODO_LOCAL, esModoDesconectado);		
			         
			//Solo existe una fila con las variables de aplicación
			
			w.updateRow(NOMBRE_TABLA, values, "rowid=" + String.valueOf(1));
		}
		catch (Exception e) {
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					AppValues.class.getSimpleName(), "actualizarAppValueModoDesconectado", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
	}
	
	/*Devuelve un valor booleano indicando si la aplicación se encuentra sincronizada o no
	 * @param context Contexto de la aplicación
	 * */
	public static boolean obtenerAppValueModoDesconectado(Context context)
	{				
		AppValues appV=  obtenerAlmacenAPPVALUES(context);
		//Si no existen se agrega un registro con valores iniciales
		if (appV!=null) {
			//solo existira un registro
			boolean valor= appV.modo_local;				
			return valor;						
		}
		else
		{
			Log.w("obtenerAppValueModoDesconectado","No existen registros en AppValues");
			return false;
		}		
	}
	
	/*Devuelve un valor booleano indicando si la aplicación se encuentra sincronizada o no
	 * @param context Contexto de la aplicación
	 * */
	public  static boolean obtenerAppValueSync(Context context)
	{		
		AppValues appV=  obtenerAlmacenAPPVALUES(context);
		//Si no existen se agrega un registro con valores iniciales
		if (appV!=null) {
			
				//solo existira un registro
				boolean valor= appV.isSync();
				return valor;						
		}
		else
		{
			Log.w("obtenerAppValueSync","No existen registros en AppValues");
			crearAppsValues(context);
			
		}	
		return false;
	}
	
	
	/*Crea los valores iniciales de la aplicación
	 * @param context Contexto de la aplicación
	 * */
	public static void actualizarAppValueSync(Context context, boolean _sync)
	{		
		DAL w = new DAL(context);
		try{
			Log.w("Actualizar sync ",String.valueOf(_sync));
			ContentValues values = new ContentValues();
			values.put(AppValues.COLUMN_SYNC, _sync);		
			//Solo existe una fila con las variables de aplicación
			w.updateRow(NOMBRE_TABLA, values, "rowid=" + String.valueOf(1));	
			Log.w("", String.valueOf(obtenerAppValueSync(context)));
		}
		catch (Exception e)
		{
			ManejoErrores.registrarError_MostrarDialogo(context, e, 
					AppValues.class.getSimpleName(), "actualizarAppValueSync", 
                    null, null);
		}
		finally{
			//w.cerrarDb();
		}
		
		
	}

	public enum EstadosEnvio { No_Enviado, Pendiente_De_Confirmacion, Enviado_Con_Error, Enviado };
	
	
	
}
