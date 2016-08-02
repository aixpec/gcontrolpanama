package com.gisystems.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;


public class Fragmento extends Fragment {
	
	 private String nombreClaseFragment;
	 private String tituloFragment;
     private Bundle args = new Bundle();



     
     public String getNombreClaseFragment() {
		return nombreClaseFragment;
     }
     
     public void setNombreClaseFragment(String nombreClaseFragment) {
		this.nombreClaseFragment = nombreClaseFragment;
     }

     public String getTituloFragment() {
		return tituloFragment;
     }
	
     public void setTituloFragment(String tituloFragment) {
		this.tituloFragment = tituloFragment;
     }

     public Bundle getArgs() {
		return args;
	}

	public void setArgs(Bundle args) {
		this.args = args;
	}


	public Fragmento(String NombreClaseFragment, String TituloFragment, Bundle Args){
    	 super();
    	 this.nombreClaseFragment=NombreClaseFragment;
    	 this.tituloFragment=TituloFragment;
    	 this.args=Args;
	}

}
