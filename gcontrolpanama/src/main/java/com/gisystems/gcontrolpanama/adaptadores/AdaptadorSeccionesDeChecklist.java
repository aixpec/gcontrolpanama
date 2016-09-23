package com.gisystems.gcontrolpanama.adaptadores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.chk.EstadoListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.TipoListaVerificacion_Seccion;

import java.util.ArrayList;


public class AdaptadorSeccionesDeChecklist extends ArrayAdapter<TipoListaVerificacion_Seccion> implements View.OnClickListener {

    private Activity context;
    private final ArrayList<TipoListaVerificacion_Seccion> arrayOriginalSecciones;
    private ArrayList<TipoListaVerificacion_Seccion> arrayModificadoSecciones;
    static class ViewHolder {
        public ImageView imgEstado;
        public TextView primeraLinea;
        public TextView segundaLinea;
        public TextView terceraLinea;
    }

    public AdaptadorSeccionesDeChecklist(Activity Context, ArrayList<TipoListaVerificacion_Seccion> secciones){
        super(Context, R.layout.list_item_checklists, secciones);
        this.context=Context;

        this.arrayModificadoSecciones=secciones;
        this.arrayOriginalSecciones = new ArrayList<>();
        this.arrayOriginalSecciones.addAll(secciones);
    }

    @SuppressLint("InflateParams") @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item_checklists, null);
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
        final TipoListaVerificacion_Seccion seccion = arrayModificadoSecciones.get(position);
        holder.primeraLinea.setText(seccion.getNombre());
        String segundaLinea = "Preguntas sin responder: " + seccion.getCantidadPreguntasRequeridasSinResponder() + " / " + seccion.getCantidadPreguntasRequeridas();
        holder.segundaLinea.setText(segundaLinea);
        String terceraLinea = seccion.getDescripcionEstado();
        holder.terceraLinea.setText(terceraLinea);
        holder.imgEstado.setImageResource(seccion.getIdImageResource());

        return rowView;
    }

    @Override
    public void onClick(View v) {
    }

}
