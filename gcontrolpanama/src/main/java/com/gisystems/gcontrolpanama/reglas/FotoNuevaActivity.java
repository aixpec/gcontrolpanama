package com.gisystems.gcontrolpanama.reglas;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Locale;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.Actividad;
import com.gisystems.gcontrolpanama.models.AppValues.EstadosEnvio;
import com.gisystems.gcontrolpanama.models.FotoActividad;
import com.gisystems.gcontrolpanama.models.Proyecto;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.AlbumStorageDirFactory;
import com.gisystems.utils.Utilitarios;
import com.gisystems.utils.Utilitarios.TipoFoto;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class FotoNuevaActivity extends ActionBarActivity implements  GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	
	private Proyecto proyecto;
	private Actividad actividad;
	private static final String ALBUM_NAME = "GControlPanama_Gisystems";
	private Bitmap bm;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;	
	private Location location = null;
	private Location locationCaptura = null;
	private String currentPhotoPath = null;
	private Uri currentPhotoUri = null;
	private final String TAG = "GetLocationElephantApp";
	private Double latitud,longitud,altitud;
	private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;
	private ImageView imageFoto;
	private String fechaFoto;
	private TextView tvComentario, tvGPS;
	private TipoFoto tipoFoto;
	private ProgressDialog pDialog;
	private EnvioDatosAPI envioDatos;
	
	
	@Override
	public void onResume() {
		super.onResume();
		try {
			
			 if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
				 iniciarLocalizacion();
			    }
		} catch (Exception e) {
			ManejoErrores.registrarError(this, e, 
				FotoNuevaActivity.class.getSimpleName(), "onResume", 
                null, null);
		}
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ManejoErrores(this));
		setContentView(R.layout.activity_foto_actividad);
		try {
			
			System.gc();
			
			ObtenerViews();

			mGoogleApiClient = new GoogleApiClient.Builder(this)
	        .addApi(LocationServices.API)
	        .addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this)
	        .build();
	
			imageFoto.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	takePictureFromCamera();
	            }
	        });
			
			
			
			if (savedInstanceState == null)
			 {
				Bundle extras = getIntent().getExtras();
			    if(extras == null) {
			        actividad= null;
			    } else {
			    	
			    	 tipoFoto= (TipoFoto)extras.get("TipoFoto");
			    	 
			    	 switch(tipoFoto){
			    	 /*case Proyecto:
			    		 proyecto=(Proyecto)extras.getSerializable("Proyecto");
			    		 break;*/
			    	 case ActividadConAvance:
			    	 case ActividadSinAvance:
			    		 actividad= (Actividad)extras.getSerializable("Actividad");
			    		 break;
					default:
						break;
			    	 }
			       
			    } 
			    
			 }
			 else
			 {
				currentPhotoUri= savedInstanceState.getParcelable("currentPhotoUri");
				currentPhotoPath = savedInstanceState.getCharSequence("currentPhotoPath").toString();
				tvComentario.setText(savedInstanceState.getCharSequence("comentario").toString());
				actividad = (Actividad) savedInstanceState.getSerializable("Actividad");
			 }
	
		}
		catch (Exception e) {
			ManejoErrores.registrarError_MostrarDialogo(this, e, 
					FotoNuevaActivity.class.getSimpleName(), "onCreate", 
                    null, null);
		}
    }
    
    


    public void setTextLocation(String location)
	{
    	tvGPS.setText(location);
	}
	
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	    
	    		
	private void addPictureToGallery() {
		Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(currentPhotoUri.getPath());
		Uri contentUri = Uri.fromFile(f);
		intent.setData(contentUri);
		FotoNuevaActivity.this.sendBroadcast(intent);
	}
	
	public void CancelarCaptura(){
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	    	
			alertDialogBuilder.setTitle(this.getResources().getString(R.string.aviso));
			
			alertDialogBuilder
			.setMessage(this.getResources().getString(R.string.class_fotografia_8))
			.setCancelable(false)
			.setPositiveButton(this.getResources().getString(R.string.si),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					finish();
				}
			  })
			.setNegativeButton(this.getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});

			AlertDialog alertDialog = alertDialogBuilder.create();
			
			alertDialog.show();
		}	
	
    public void cancelarGrabacionFotografia() {
    	imageFoto.setImageResource(R.drawable.aperture);
	   	 bm = null;
	   	 currentPhotoPath=null;
	   	 currentPhotoUri=null;
	}
    
    public boolean datosCompletados(StringBuilder mensajeError){
    	boolean resultado;
    	resultado = (currentPhotoPath != null && currentPhotoPath.length() > 0);
		if (!resultado) {
			mensajeError.append(this.getResources().getString(R.string.class_fotografia_4));
			return resultado;
		}
		return resultado;
    }
    
    protected void detenerLocalizacion() {
    	
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        
        mRequestingLocationUpdates=false;
        
    }
	    
    protected void iniciarLocalizacion() {
    	try{

    		 mLocationRequest = LocationRequest.create();
	         mLocationRequest.setInterval(10000); // Update location every second
	         mLocationRequest.setFastestInterval(5000);
	         mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	         
	         LocationServices.FusedLocationApi.requestLocationUpdates(
	                 mGoogleApiClient, mLocationRequest, this);
	         
	         mRequestingLocationUpdates=true;
    	}
		catch (Exception e) {
			ManejoErrores.registrarError_MostrarDialogo(this, e, 
					FotoNuevaActivity.class.getSimpleName(), "onCreate", 
                    null, null);
		}
         
    }
    
    protected void grabarFotografia(
            String comentarioFoto) {
    	
    		StringBuilder mensajeError = new StringBuilder();
    		if (datosCompletados(mensajeError))
            {
    			try{			
    				Date date = new Date();
    				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.getDefault());
    			
    				if (locationCaptura ==null)
    				{
    					fechaFoto = sdf.format(date);			
    					latitud  = (double) -1; 
    					longitud = (double) -1; 
    					altitud  = (double) -1;
    				}
    				else
    				{
    					latitud=locationCaptura.getLatitude();
    					longitud=locationCaptura.getLongitude();
    					altitud=locationCaptura.getAltitude();
    					fechaFoto=sdf.format(locationCaptura.getTime());
    				}
    				
    				String nombre_fotografia_dispositivo = currentPhotoUri.getPath();
    				long result=0;
    				
    				switch(tipoFoto){
    				case ActividadConAvance:
    					if(actividad!=null)
    						result=FotoActividad.insertarFotografia(this, 
        						actividad.getIdCliente(), actividad.getIdProyecto(), actividad.getIdConstruccion(), actividad.getIdActividad(),
        						comentarioFoto, nombre_fotografia_dispositivo, 
        						latitud, longitud, altitud, fechaFoto, TipoFoto.ActividadConAvance);
    					else
    						result=-1;
    					break;
    				case ActividadSinAvance:
    					if(actividad!=null)
							result= FotoActividad.insertarFotografia(this,
									actividad.getIdCliente(), actividad.getIdProyecto(), actividad.getIdConstruccion(), actividad.getIdActividad(),
									comentarioFoto, nombre_fotografia_dispositivo,
									latitud, longitud, altitud, fechaFoto,TipoFoto.ActividadSinAvance);
    						/*result=FotoActividad.insertarFotografia(this,
        						actividad.getIdCliente(), actividad.getIdProyecto(), actividad.getIdConstruccion(), actividad.getIdActividad(),
        						comentarioFoto, nombre_fotografia_dispositivo, 
        						latitud, longitud, altitud, fechaFoto, TipoFoto.ActividadSinAvance);
    			            */
						else
    						result=-1;
    					break;
					default:
						break;
    				}

    			 	if (result>-1){
    					addPictureToGallery();
    					currentPhotoUri = null;
    					locationCaptura=null;
    					imageFoto.setImageResource(R.drawable.aperture);	
    					tvComentario.setText("");
    					tvGPS.setText("");			
    					Toast.makeText(FotoNuevaActivity.this, this.getResources().getString(R.string.class_fotografia_6), Toast.LENGTH_SHORT).show();														
    					
    					//Si la fotografía es de tipo proyecto o de Actividad sin avance intentar enviarla al servidor
    					if(Utilitarios.isConnectionAvailable(FotoNuevaActivity.this)){{
	    					if ((tipoFoto==TipoFoto.Proyecto) || (tipoFoto==TipoFoto.ActividadSinAvance))
	    					    new enviar_Fotografias().execute();
    					}}
    				}
    				else{
    					throw new EmptyStackException();
    				}
    			}
    			catch( Exception ex){
    				ManejoErrores.registrarError(this, ex, FotoNuevaActivity.class.getSimpleName(), this.getResources().getString(R.string.class_fotografia_7), null, null);
    				Toast.makeText(FotoNuevaActivity.this,this.getResources().getString(R.string.class_fotografia_5), Toast.LENGTH_SHORT).show();
    			}
    			
            }
    		else
    		{	
    			Toast.makeText(FotoNuevaActivity.this , mensajeError, Toast.LENGTH_LONG).show();
            }
			
	}
    
	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
			packageManager.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
    public void muestraPosicion(Location location) {
    	if (location!=null)
    	{
			this.setLocation(location);
			long time = location.getTime();
			Date date = new Date(time);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
			String fecha_foto = sdf.format(date);
			//if (timer!=null)timer.cancel();		
			System.out.println(fecha_foto);
			setTextLocation(Utilitarios.getLocationFormated(this.getLocation()));	
    	}    	
	}
	
    private String obtenerComentario(){
    	return (String) tvComentario.getText().toString();
    }
    
    /** 
     * Obtiene las vistas con las que interactuara la clase
     * */
	private void ObtenerViews(){
		
		imageFoto=(ImageView) this.findViewById(R.id.activity_foto_actividad_ivFoto);
		tvComentario=(TextView) this.findViewById(R.id.activity_foto_actividad_Comentario);
		tvGPS=((TextView) FotoNuevaActivity.this.findViewById(R.id.activity_foto_actividad_tvFotografia_GPS));
		
	}
	
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		try{
	        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	            if (resultCode == android.app.Activity.RESULT_OK ) {       	            	

	            	if (location!=null) {locationCaptura=location;}
	            	
	                //crear el objeto bitmap para asociarlo con el imageview
				    BitmapFactory.Options bmo = new BitmapFactory.Options ();
				    bmo.inSampleSize = 4;
				    if (bm != null) bm.recycle();
				    bm = null;
				    bm = BitmapFactory.decodeFile (currentPhotoPath, bmo);
				    //verificar si existe la imagen
					File f = new File(currentPhotoPath);
				    if (f.exists())
					{
				    	if (bm != null)
				    		imageFoto.setImageBitmap(bm);					
						    Utilitarios.scanPicture(FotoNuevaActivity.this, currentPhotoPath);
					}
					else
					{
						Toast.makeText(FotoNuevaActivity.this, getResources().getString(R.string.class_fotografia_0), Toast.LENGTH_LONG).show();
						imageFoto.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.aperture));
					}
	            } else if (resultCode == android.app.Activity.RESULT_CANCELED) {
	                // User cancelled the image capture
	            	Toast.makeText(FotoNuevaActivity.this, getResources().getString(R.string.class_fotografia_1), Toast.LENGTH_LONG).show();
	            	cancelarGrabacionFotografia();
	            } else {
	                // Image capture failed, advise user
	            	Toast.makeText(FotoNuevaActivity.this, getResources().getString(R.string.class_fotografia_2), Toast.LENGTH_LONG).show();
	            	cancelarGrabacionFotografia();
	            }
	        }
		}
		catch( Exception ex){
			ManejoErrores.registrarError(this, ex, FotoNuevaActivity.class.getSimpleName(),"onActivityResult", null, null);
			Toast.makeText(FotoNuevaActivity.this,this.getResources().getString(R.string.class_fotografia_5), Toast.LENGTH_SHORT).show();
		}
	}

	
	@Override
    public void onConnected(Bundle bundle) {
    	this.setLocation(LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)); 
		
		if (this.getLocation()!=null) muestraPosicion(this.getLocation());
		
    	iniciarLocalizacion();
       
    }
	
	@Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }
    
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.accept_cancel, menu);
        return super.onCreateOptionsMenu(menu);
    }
   
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
		case R.id.mnuaction_aceptarCancelar_aceptar:
			
			grabarFotografia(obtenerComentario());
	        return true;
	        
		case R.id.mnuaction_aceptarCancelar_cancelar:
			this.CancelarCaptura();
			return true;
			
	    default:
	        return super.onOptionsItemSelected(item);
	 	}
	
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
	
	protected void onSaveInstanceState(Bundle outState){
		outState.putParcelable("currentPhotoUri", currentPhotoUri);
	    outState.putCharSequence("currentPhotoPath", currentPhotoPath );
	    outState.putCharSequence("comentario", tvComentario.getText());
	    
	    switch(tipoFoto){
		   /*	 case Proyecto:
		   		outState.putSerializable("Proyecto", proyecto);
		   		 break;*/
		   	 case ActividadConAvance:
				 outState.putSerializable("Actividad", actividad);
				 break;
		   	 case ActividadSinAvance:
		   		outState.putSerializable("Actividad", actividad);
		   		 break;
				default:
			break;
   	 	}
	     
	}
    
	@Override
	public void onStart() {
        super.onStart();
        // Connect the client.
       mGoogleApiClient.connect();
       
    }
	
	@Override
	public void onStop() {
    	 super.onStop();
        // Disconnecting the client invalidates it.
    	detenerLocalizacion();
        mGoogleApiClient.disconnect();
    }
	
	//Llama al Intent que abre la camara del dispositivo
	private void takePictureFromCamera() {
			if (Utilitarios.verificarSdcard())
			{
				if (isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = null;
					try {
						f = AlbumStorageDirFactory.setUpPhotoFile(ALBUM_NAME);
						currentPhotoPath = f.getPath();
						currentPhotoUri = Uri.fromFile(f);				
						intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
					} catch (IOException e) {
						e.printStackTrace();
						f = null;
						currentPhotoUri = null;
					}
					startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				}
			}
			else
			{
				Toast.makeText(FotoNuevaActivity.this, "Verifique la SDCARD del dispositivo por favor.", Toast.LENGTH_LONG).show();
			}
		}
		
	
    @Override
    public void onLocationChanged(Location location) {
      //  mLocationView.setText("Location received: " + location.toString());
        muestraPosicion(location);
    }
	    

   private class enviar_Fotografias extends AsyncTask<Void, Integer, Boolean>{	
 	   
 	   private int CantFotos=0;

 	   private ArrayList<FotoActividad> FotografiasDeActividad;
 	   private String msj;
 	  // private int EnviosSatisfactorios=0;
 	   @Override
 		protected void onPreExecute(){
 			super.onPreExecute();
 			
 			 pDialog = new ProgressDialog(FotoNuevaActivity.this);
             pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
             pDialog.setMessage(FotoNuevaActivity.this.getApplicationContext().getString(R.string.enviando_al_servidor));
             pDialog.setCancelable(false);
 			 pDialog.show();
 			 
 			   switch(tipoFoto){

 			   	 case ActividadSinAvance:
					 FotografiasDeActividad= FotoActividad.obtenerFotosSinEnviarDeActividadSinAvance(FotoNuevaActivity.this.getApplicationContext(),
							 actividad.getIdCliente(), actividad.getIdProyecto(), actividad.getIdConstruccion(), actividad.getIdActividad());
					 CantFotos=FotografiasDeActividad.size();
					/* FotografiasDeActividad=FotoActividad.obtenerFotosSinEnviarDeActividadSinAvance(
 			   				FotoNuevaActivity.this.getApplicationContext(), 
 			   				actividad.getIdCliente(), actividad.getIdProyecto(), actividad.getIdConstruccion(), actividad.getIdActividad());
 			   	     CantFotos=FotografiasDeActividad.size();*/
 			   		 break;
 					default:
 				break;
 	   	 	}
 			 
 		}

 		@Override
 		protected Boolean doInBackground(Void... params) {
 			// TODO Auto-generated method stub
 			boolean resultado=false;
 		
 			try {			
 				
 				
 				envioDatos=new EnvioDatosAPI(FotoNuevaActivity.this);
 				
 				
 				switch(tipoFoto){
	 			   /*	 case Proyecto:
	 			   		for(int index=0;index<CantFotos;index++){
	 	 					publishProgress(index+1);
	 	 					resultado=envioDatos.Enviar_FotografiaProyecto(FotografiasDeProyecto.get(index));
	 			   		}
	 			   		 break;
	 			   	*/
	 			   	 case ActividadSinAvance:
						 /*for(int index=0;index<CantFotos;index++){
							 publishProgress(index+1);
							 resultado=envioDatos.Enviar_FotografiaSinAvance(FotografiasDeActividad.get(index));*/
	 			   		for(int index=0;index<CantFotos;index++){
	 	 					publishProgress(index+1);

	 	 			 		resultado=envioDatos.Enviar_FotografiaSinAvance(FotografiasDeActividad.get(index));
	 	 			 		if (resultado)
	 	 			 			FotoActividad.actualizarEdoFotografia(FotoNuevaActivity.this, 
	 	 			 					FotografiasDeActividad.get(index), EstadosEnvio.Enviado);
	 	 			 		
	 			   		}
	 			   		 break;
	 					default:
	 				break;
				
 					}
 				
 			
 			}
 			catch (Exception e) {
 				ManejoErrores.registrarError_MostrarDialogo(FotoNuevaActivity.this, e, 
 						FotoNuevaActivity.class.getSimpleName(), "enviar_Fotografias_doInBackground", 
 	                    null, null);  
 			}
 			
 			return resultado;
 		}
 		
 		@Override
 	    protected void onProgressUpdate(Integer... values) {
 	        int progreso = values[0].intValue();
 	        
 	       msj=String.format(String.format(getResources().getString(R.string.class_AgregarAvance_10),
				   new Object[]{ null }) , progreso,CantFotos);
 	       pDialog.setMessage(msj);
 	    
 	        
 	    }
 		
 		@Override
 		protected void onPostExecute(Boolean resultado){
 			try {
 				
 				pDialog.dismiss();
 				
 				
 				
 			} catch (Exception e) {
 				ManejoErrores.registrarError_MostrarDialogo(FotoNuevaActivity.this, e, 
 						FotoNuevaActivity.class.getSimpleName(), "enviar_Fotografias_onPostExecute", 
 	                    null, null);   
 			}
 		}
 		   
    }	


	
}