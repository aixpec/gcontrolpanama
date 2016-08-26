package com.gisystems.gcontrolpanama.adaptadores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.chk.EstadoListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;
import com.gisystems.utils.Utilitarios;

import java.util.ArrayList;
import java.util.Locale;


public class AdaptadorChecklists extends ArrayAdapter<ListaVerificacion> implements View.OnClickListener {

    private Activity context;
    private final ArrayList<ListaVerificacion> arrayOriginalListas;
    private ArrayList<ListaVerificacion> arrayModificadoListas;
    static class ViewHolder {
        public ImageView imgEstado;
        public TextView primeraLinea;
        public TextView segundaLinea;
        public TextView terceraLinea;
    }

    public AdaptadorChecklists(Activity Context, ArrayList<ListaVerificacion> Listas){
        super(Context, R.layout.list_item_checklists, Listas);
        this.context=Context;

        this.arrayModificadoListas=Listas;
        this.arrayOriginalListas = new ArrayList<ListaVerificacion>();
        this.arrayOriginalListas.addAll(Listas);
    }

    @SuppressLint("InflateParams") @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item_generico, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imgEstado = (ImageView) rowView.findViewById(R.id.list_item_checklists_imgEstado);;
            viewHolder.primeraLinea = (TextView) rowView.findViewById(R.id.list_item_checklists_lblPrimeraLinea);
            viewHolder.segundaLinea = (TextView) rowView.findViewById(R.id.list_item_checklists_lblSegundaLinea);
            viewHolder.terceraLinea = (TextView) rowView.findViewById(R.id.list_item_checklists_lblTerceraLinea);

            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        final ListaVerificacion lista = arrayModificadoListas.get(position);
        holder.primeraLinea.setText(lista.getCreoFecha().toString());
        holder.segundaLinea.setText(lista.getTipoListaVerificacion());
        holder.terceraLinea.setText(lista.getCreoUsuario());
        holder.imgEstado.setImageResource(EstadoListaVerificacion.getIdImageResource(lista.getIdEstadoListaVerificacion()));

        return rowView;
    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }




   /* private void IniciarDetalleProyecto(Proyecto Proyecto){
        Intent mainIntent = new Intent(context, ProyectoDetalleActivity.class);
        mainIntent.putExtra("Proyecto", Proyecto);
        context.startActivity(mainIntent);
    }*/

}
