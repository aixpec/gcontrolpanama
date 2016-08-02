package com.gisystems.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.gisystems.gcontrolpanama.reglas.ProyectoDetalleActivity;
import com.gisystems.exceptionhandling.ManejoErrores;

public class CustomPagerAdapter  extends android.support.v4.app.FragmentPagerAdapter {
	
	 Context mContext;
     private List<Fragmento> fragmentos = new ArrayList<Fragmento>();

     private SparseArray<Fragment> mFragmentTags;
     
     public List<Fragmento> getFragmentos() {
		return fragmentos;
	}

	public void setFragmentos(List<Fragmento> fragmentos) {
		this.fragmentos = fragmentos;
	}

	public CustomPagerAdapter(FragmentManager fragmentManager, Context context) {
         super(fragmentManager);
         mContext = context;

         mFragmentTags = new SparseArray<Fragment>();
     }

     @Override
     public Fragment getItem(int position) {
     	try
     	{
     	
     		return Fragment.instantiate(mContext, fragmentos.get(position).getNombreClaseFragment(),fragmentos.get(position).getArgs());		   
     		
     	}
     	catch  (Exception ex){
     		ManejoErrores.registrarError_MostrarDialogo(mContext, ex, 
     				ProyectoDetalleActivity.class.getSimpleName(), "CustomPagerAdapter_getItem", 
                     null, null);
     	}    	
     	return null;
     }

	@Override
     public int getCount() {
     	 return fragmentos.size();
     }

     @Override
     public CharSequence getPageTitle(int position) {
         return fragmentos.get(position).getTituloFragment() ;
     }

     @Override
     public Object instantiateItem(ViewGroup container, int position) {
         Object obj = super.instantiateItem(container, position);
      
         if (obj instanceof Fragment) {
             // record the fragment tag here.
             Fragment f = (Fragment) obj;
             //Log.w("instanceof ID FRAGMENT",String.valueOf(f.getId()));
             //String tag = String.valueOf(f.getId());
             mFragmentTags.put(position, f);  
             //Log.w("SIZE TAG on instansciate", String.valueOf( mFragmentTags.size()));
             //Log.w("POSITION",String.valueOf(position));
             
             
            
         }
         return obj;
     }

     public Fragment getFragment(int position) {
    	
         Fragment f =  mFragmentTags.get(position);
       
         if (f == null)
             return null;
      
         return f;
     }
}
