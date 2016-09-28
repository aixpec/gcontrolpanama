package com.gisystems.gcontrolpanama.reglas;


import com.gisystems.api.RecepcionDatosAPI;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.AppValues;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.utils.Utilitarios;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity implements OnClickListener {

	private EditText etUsuario;
	private EditText etPassword;
	private Context ctx;
	private ProgressDialog progressDialog;
	private Boolean usuario_valido=false;
	private Button btnValidarCredenciales;
	private Button btnModoDesconectado;
	private ProgressDialog pDialog;

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE  = 544;

    @Override
	protected void onCreate(Bundle savedState) {
		// TODO Auto-generated method stub
		super.onCreate(savedState);
		setContentView(R.layout.activity_login);
		
	    ctx=this;
	    obtenerViews();
	    
	    establecerInterfaz();
	    
	    pDialog = new ProgressDialog(ctx);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage(getApplicationContext().getString(R.string.class_generic_login_80));
        pDialog.setCancelable(false);
        
	    if(  (AppValues.SharedPref_obtenerValorEsUsuarioValido(ctx) && AppValues.AppEstaSincronizada(this))
				&& (!AppValues.obtenerAppValueModoDesconectado(ctx) ) )
		{	
	    	//Preguntar al usuario si realmente desea salir
	    	new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(getResources().getString(R.string.class_generic_login_20))
	        .setMessage(getResources().getString(R.string.class_generic_login_30))
	        .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
					//Establecer el inicio de seision a falso
	            	AppValues.SharedPref_guardarUsuarioValido(ctx, false);
	            	
	    			//borrar la contrasena
	    			AppValues.SharedPref_guardarUsuarioPassword(ctx, null);
	    			
	    			iniciarActividad();
	    			
	            }
	        })
	        .setNegativeButton("No", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {

					Intent retornar = new Intent(getBaseContext(), ProyectosListadoActivity.class);
	    			startActivity(retornar);
					finish();

				}
	        })
	        .show();
		}	    
	}
	
	
	public void establecerInterfaz(){
		if (AppValues.AppEstaSincronizada(ctx)){
	    	this.btnModoDesconectado.setVisibility(View.VISIBLE);
	    }
	    else
	    {
	    	this.btnModoDesconectado.setVisibility(View.INVISIBLE);
	    }
		
		etUsuario.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                        	if (etUsuario.length() > 0)
                        	{
                        		etPassword.requestFocus();
                        	}
                        	return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
		
	    etPassword.setOnKeyListener(new OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                        	if (etUsuario.length() > 0 && etPassword.length() > 0)
                        	{
                        		verificarUsuario();
                        	}
                        	return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
	}
	
	public void onClick(View v) {
		try {
			int id = v.getId();
			
			if (id==R.id.activity_login_btnIngresar)
			{
                if (tengoPermisoParaAccederAlTelefono()) {
                    verificarUsuario();
                } else {
                    solicitarPermisosAlUsuario();
                }
			}
			else if (id==R.id.activity_login_btnIngresarModoDesconectado)
			{								
				new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle(getResources().getString(R.string.class_generic_login_60))
		        .setMessage(getResources().getString(R.string.class_generic_login_70))
		        .setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
		        	
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	
						//Setear el inicio de seision a falso
		            	AppValues.SharedPref_guardarUsuarioValido(ctx, false);
		            	
		            	//Establecer el valor en la db como modo local
		            	//Establecer el app value sync como actualizado
						AppValues.actualizarAppValueModoDesconectado(ctx, true);
						
		    			//borrar la contrasena	    			
		    			AppValues.SharedPref_guardarUsuarioPassword(ctx,null);

		    			//Ingresar en modo desconectado
						Intent retornar = new Intent(getBaseContext(), ProyectosListadoActivity.class);
		    			startActivity(retornar);
		    			finish();

					}
		        })
		        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            	//Establecer el app value sync como actualizado
						AppValues.actualizarAppValueModoDesconectado(ctx, false);
						
		            }
		        })
		        .show();
			}
		} catch (Exception e) {
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
        			LoginActivity.class.getSimpleName(), "onClick", 
                    null, null);
		}
	}
	
	private void obtenerViews(){
		
	    btnValidarCredenciales = (Button)findViewById(R.id.activity_login_btnIngresar);
	    btnValidarCredenciales.setOnClickListener(this);
	    btnModoDesconectado	 = (Button)findViewById(R.id.activity_login_btnIngresarModoDesconectado);
	    btnModoDesconectado.setOnClickListener(this);
	    
	    etPassword=(EditText)findViewById(R.id.activity_login_etPassword);
	    etUsuario=(EditText)findViewById(R.id.activity_login_etEmail);
	    
	    TextView tvLinkRegistro =(TextView)findViewById(R.id.activity_login_tvLabelRegistro);
		tvLinkRegistro.setMovementMethod(LinkMovementMethod.getInstance());
		
	}
	
	@SuppressLint("HandlerLeak") private final Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			
			progressDialog.dismiss();
			
			AppValues.SharedPref_guardarUsuarioValido(ctx, usuario_valido);
			
			String msj;
			
			if (usuario_valido==true)
			{
				//Establecer el app value sync como actualizado
				AppValues.actualizarAppValueModoDesconectado(ctx, false);
				AppValues.SharedPref_guardarUsuarioNombre(ctx, etUsuario.getText().toString());
				AppValues.SharedPref_guardarUsuarioPassword(ctx, etPassword.getText().toString());
				msj = getResources().getString(R.string.class_generic_login_0);
				
			}
			else
			{
				msj = getResources().getString(R.string.class_generic_login_10);
				Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();
			}
	    				
			

	    	//Si el usuario es valido  se inicializa la base de datos.
			if ( (usuario_valido==true))
			{
				new InicializarBDTask().execute();
				
			}
        }
    }; 
    
	private void verificarUsuario()
	{
		try {
			if(etUsuario.getText().toString().length() > 0 && etPassword.getText().toString().length()>0)
			{
				progressDialog = ProgressDialog.show(
						this,
						"", 
						getResources().getString(R.string.class_generic_login_40), 
						true);
				new Thread(new Runnable(){
					@Override
					public void run() {
						try {
							StringBuilder mensajeDeError =new StringBuilder(getResources().getString(R.string.class_generic_login_10));
							usuario_valido=Utilitarios.ValidarUsuarioPassword(ctx, 
									etUsuario.getText().toString(), 
									etPassword.getText().toString(), mensajeDeError);
							progressHandler.sendEmptyMessage(0);						
						} catch (Exception e) {
							
							progressHandler.sendEmptyMessage(0);
						    AppValues.SharedPref_guardarUsuarioValido(ctx,false);
							ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
									LoginActivity.class.getSimpleName(), "verificarUsuario", 
				                    null, null);
						}
					}}).start();					
			}
			else
			{
				Toast.makeText(LoginActivity.this, getResources().getString(R.string.class_generic_login_50), Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
        			LoginActivity.class.getSimpleName(), "verificarUsuario", 
                    null, null);
		}
	}

    private void iniciarActividad()
	{		
		try {	

			String usuario = AppValues.SharedPref_obtenerUsuarioNombre(ctx);		
			String password = AppValues.SharedPref_obtenerUsuarioPassword(ctx);
			
			etUsuario.setText(usuario);
			etPassword.setText(password);	
			btnValidarCredenciales.setOnClickListener(this);	
			
			if (AppValues.obtenerAppValueSync(this))
			{
				iniciarActividadModoLocal();
			}
			else
			{
				//btnModoDesconectado.setVisibility(8);
				btnModoDesconectado.setVisibility(View.GONE);
			}
			
		} catch (Exception e) {
			ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
        			LoginActivity.class.getSimpleName(), "iniciarActividad", 
                    null, null);
		}
	}
    
    private void iniciarActividadModoLocal()
	{
		btnModoDesconectado=(Button)findViewById(R.id.activity_login_btnIngresarModoDesconectado);
		btnModoDesconectado.setOnClickListener(this);
		//btnModoDesconectado.setVisibility(0);
		btnModoDesconectado.setVisibility(View.INVISIBLE);
	}
	
    
    private class InicializarBDTask extends AsyncTask<Object, Void, Boolean>{
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Object... param) {
			boolean resultado = false;
			try {
				
	            RecepcionDatosAPI recepcionDatosAPI = new RecepcionDatosAPI(LoginActivity.this);
  			    resultado = recepcionDatosAPI.InicializarBD();
  			   
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
	        			LoginActivity.class.getSimpleName(), "InicializarBDTask", 
	                    null, null);        
			}
			return resultado;
		}

		@Override
		protected void onPostExecute(Boolean resultado){
			try {
				pDialog.dismiss();
				
				if (resultado) {
					 //Establecer el app value sync como actualizado
					AppValues.actualizarAppValueSync(ctx, true);
					
					Toast.makeText(ctx, "Bienvenido ",
							Toast.LENGTH_SHORT).show();		
					
					Intent mainIntent = new Intent(LoginActivity.this,ProyectosListadoActivity.class);
					LoginActivity.this.startActivity(mainIntent);
					LoginActivity.this.finish();

				} else {
					 AppValues.actualizarAppValueSync(ctx,false);
					 Toast.makeText(ctx, "Imposible inicializar la base datos.",
							Toast.LENGTH_SHORT).show();
					 Toast.makeText(ctx, "Verifique los proyectos asignados a su cuenta por favor.",
								Toast.LENGTH_LONG).show();
				}
				
				
			} catch (Exception e) {
				ManejoErrores.registrarError_MostrarDialogo(ctx, e, 
	        			LoginActivity.class.getSimpleName(), "onPostExecute", 
	                    null, null);   
			}
		}

	}



	@Override
	protected void onPause() {

		if (pDialog != null) pDialog.dismiss();
		super.onPause();
	}


    private boolean tengoPermisoParaAccederAlTelefono() {
        return (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED);
    }

    //https://developer.android.com/training/permissions/requesting.html
    private void solicitarPermisosAlUsuario() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission was granted, yay!
                    verificarUsuario();
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}
		}
	}

}
