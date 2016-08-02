package com.gisystems.exceptionhandling;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogoError  {

	private Context context;
	
	public DialogoError(Context context) {
		this.context = context;
	}
	
	public void show(String clase, String nombreMetodo, Exception e) {
		String msj = "ERROR en " + clase + ": " + nombreMetodo + ". " + e.getMessage(); 
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Aviso")
        .setMessage(msj)
        .setCancelable(false)
        .setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
	}

}
