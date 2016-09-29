package com.gisystems.gcontrolpanama.reglas.checklists;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gisystems.api.EnvioDatosAPI;
import com.gisystems.exceptionhandling.ManejoErrores;
import com.gisystems.gcontrolpanama.R;
import com.gisystems.gcontrolpanama.models.chk.ListaVerificacion;
import com.gisystems.gcontrolpanama.models.chk.TipoListaVerificacion;

import java.util.ArrayList;

public class ListasVerificacionNueva extends AppCompatDialogFragment {

    private Context ctx;
    private int idCliente;
    private int idProyecto;
    private Spinner spinnerTipos;
    private ProgressDialog pDialog;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NuevaListaVerificacionDialogListener {
        void onNuevaListaDialogPositiveClick(ArrayList<ListaVerificacion> listas);
    }

    // Use this instance of the interface to deliver action events
    NuevaListaVerificacionDialogListener mListener;

    public static ListasVerificacionNueva newInstance(int idCliente, int idProyecto) {
        ListasVerificacionNueva f = new ListasVerificacionNueva();

        Bundle args = new Bundle();
        args.putInt("idCliente", idCliente);
        args.putInt("idProyecto", idProyecto);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getActivity();
        idCliente = getArguments().getInt("idCliente");
        idProyecto = getArguments().getInt("idProyecto");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        try {
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.fragment_dialog_nueva_checklist, null);

            spinnerTipos = (Spinner)view.findViewById(R.id.spinnerTipoListaVerificacion);

            TipoListaVerificacion tipoListaVerificacion = new TipoListaVerificacion();
            ArrayList<TipoListaVerificacion> tipos = tipoListaVerificacion.obtenerListadoTipos(getActivity(),this.idCliente);

            ArrayAdapter<TipoListaVerificacion> dataAdapter = new ArrayAdapter<TipoListaVerificacion>(getActivity(),
                    android.R.layout.simple_spinner_item, tipos);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerTipos.setAdapter(dataAdapter);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                    // Título
                    .setTitle(R.string.ListasVerificacionActivity_1)
                    // Add action buttons
                    .setPositiveButton(R.string.ListasVerificacionActivity_2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            TipoListaVerificacion oTipoListaVerificacion = (TipoListaVerificacion) spinnerTipos.getSelectedItem();
                            if (oTipoListaVerificacion != null) {
                                GrabarNuevaLista(oTipoListaVerificacion);
                            } else {
                                dialog.cancel();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ListasVerificacionNueva.this.getDialog().cancel();
                        }
                    });
        } catch (Exception e) {
            ManejoErrores.registrarError_MostrarDialogo(getActivity(), e,
                    ListasVerificacionNueva.class.getSimpleName(), "onCreateDialog",
                    null, null);
        }
        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NuevaListaVerificacionDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /// Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NuevaListaVerificacionDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NuevaListaVerificacionDialogListener");
        }
    }

    private void GrabarNuevaLista(TipoListaVerificacion oTipoListaVerificacion) {
        ListaVerificacion nuevaLista = new ListaVerificacion();
        nuevaLista.setIdCliente(idCliente);
        nuevaLista.setIdProyecto(idProyecto);
        nuevaLista.setIdTipoListaVerificacion(oTipoListaVerificacion.getIdTipoListaVerificacion());
        new TareaInsertarNuevaLista().execute(nuevaLista);
    }

    private class TareaInsertarNuevaLista extends AsyncTask<ListaVerificacion, Void, ArrayList<ListaVerificacion>> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(ctx.getApplicationContext().getString(R.string.ListasVerificacionActivity_4));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected ArrayList<ListaVerificacion> doInBackground(ListaVerificacion... lista) {
            ArrayList<ListaVerificacion> resultado = new ArrayList<>();
            try {
                ListaVerificacion.insertarNuevaListaVerificacion(ctx,
                        lista[0].getIdCliente(),
                        lista[0].getIdProyecto(),
                        lista[0].getIdTipoListaVerificacion());
//                EnvioDatosAPI envioDatosAPI = new EnvioDatosAPI(ctx);
//                envioDatosAPI.EnviarListaVerificacion(lista[0]);
                resultado = ListaVerificacion.obtenerListasAbiertasPorClienteProyecto(ctx,idCliente,idProyecto);
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ListasVerificacionNueva.class.getSimpleName(), "TareaInsertarNuevaLista_onPreExecute",
                        null, null);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(ArrayList<ListaVerificacion> resultado){
            try {
                mListener.onNuevaListaDialogPositiveClick(resultado);
                pDialog.dismiss();
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        ListasVerificacionNueva.class.getSimpleName(), "TareaInsertarNuevaLista_onPostExecute",
                        null, null);
            }
        }
    }


}