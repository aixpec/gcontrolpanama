package com.gisystems.exceptionhandling;



import android.os.AsyncTask;
import android.os.Looper;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.gcontrolpanama.models.RegistroError;

public class EnviarLogErroresTask extends
		AsyncTask<RegistroError, Void, Boolean> {

		@Override
		protected Boolean doInBackground(RegistroError... regs) {
			boolean resultado = false;
			try {
				Looper.prepare();
				RegistroError reg = regs[0];
				EnvioDatosAPI envioDatosAPI = new EnvioDatosAPI(reg.getContext());
				envioDatosAPI.Enviar_Registro_Log_Errores(reg.getMensaje_error(), reg.getStack_trace(),
						   								  reg.getNombre_clase(), reg.getNombre_metodo(),
						   								  reg.getJson_peticion(), 
						   								  reg.getJson_respuesta());
				Looper.myLooper().quit();
				resultado = true;
			} catch (Exception e) {    
				if (Looper.myLooper()!=null) Looper.myLooper().quit();
			}
			return resultado;
		}

		@Override
		protected void onPostExecute(Boolean resultado){
			System.gc();
		}
	
}
