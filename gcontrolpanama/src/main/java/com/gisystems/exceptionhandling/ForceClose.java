package com.gisystems.exceptionhandling;

import com.gisystems.gcontrolpanama.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ForceClose extends Activity {
	
	private String error;
	private String mensajeCompleto;
	private String stackTrace;
	private String nombreClase;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ManejoErrores(this));
        setContentView(R.layout.activity_force_close);
        
        error = getIntent().getStringExtra("error");
        mensajeCompleto = getIntent().getStringExtra("mensaje_completo");
        stackTrace = getIntent().getStringExtra("stack_trace");
        nombreClase = getIntent().getStringExtra("nombre_clase");
        		
        ((TextView) findViewById(R.id.error)).setText(mensajeCompleto);
        
        ManejoErrores.registrarError(this, error, stackTrace, nombreClase, "uncaughtException", "", "");
    }

}
