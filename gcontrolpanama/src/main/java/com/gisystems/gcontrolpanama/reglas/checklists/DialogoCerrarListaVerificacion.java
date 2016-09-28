package com.gisystems.gcontrolpanama.reglas.checklists;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
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

/**
 * Created by rlemus on 26/09/2016.
 */
public class DialogoCerrarListaVerificacion extends AppCompatDialogFragment {
    private Context ctx;
    private int idCliente;
    private int idListaVerificacion;
    private ProgressDialog pDialog;

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface CerrarListaVerificacionDialogListener {
        void onCerrarListaDialogPositiveClick(boolean exitoso);
    }

    // Use this instance of the interface to deliver action events
    CerrarListaVerificacionDialogListener mListener;

    public static DialogoCerrarListaVerificacion newInstance(int idCliente, int idListaVerificacion) {
        DialogoCerrarListaVerificacion f = new DialogoCerrarListaVerificacion();

        Bundle args = new Bundle();
        args.putInt("idCliente", idCliente);
        args.putInt("idListaVerificacion", idListaVerificacion);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getActivity();
        idCliente = getArguments().getInt("idCliente");
        idListaVerificacion = getArguments().getInt("idListaVerificacion");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        try {
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.fragment_dialog_cerrar_checklist, null);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                    // Título
                    .setTitle(R.string.ListasVerificacionActivity_9)
                    // Add action buttons
                    .setPositiveButton(R.string.ListasVerificacionActivity_8, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            CerrarLista();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DialogoCerrarListaVerificacion.this.getDialog().cancel();
                        }
                    });
        } catch (Exception e) {
            ManejoErrores.registrarError_MostrarDialogo(getActivity(), e,
                    DialogoCerrarListaVerificacion.class.getSimpleName(), "onCreateDialog",
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
            mListener = (CerrarListaVerificacionDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement CerrarListaVerificacionDialogListener");
        }
    }

    private void CerrarLista() {
        ListaVerificacion nuevaLista = new ListaVerificacion();
        nuevaLista.setIdCliente(idCliente);
        nuevaLista.setIdListaVerificacion(idListaVerificacion);
        new TareaCerrarLista().execute(nuevaLista);
    }

    private class TareaCerrarLista extends AsyncTask<ListaVerificacion, Void, Boolean> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog = new ProgressDialog(ctx);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setMessage(ctx.getApplicationContext().getString(R.string.ListasVerificacionActivity_6));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(ListaVerificacion... lista) {
            Boolean resultado = false;
            try {
                resultado = ListaVerificacion.CerrarListaVerificacion(ctx,
                                                                lista[0].getIdCliente(),
                                                                lista[0].getIdListaVerificacion());
                if (resultado) {
                    EnvioDatosAPI envioDatosAPI = new EnvioDatosAPI(ctx);
                    envioDatosAPI.EnviarListaVerificacion(lista[0]);
                }
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        DialogoCerrarListaVerificacion.class.getSimpleName(), "TareaCerrarLista_onPreExecute",
                        null, null);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado){
            try {
                mListener.onCerrarListaDialogPositiveClick(resultado);
                pDialog.dismiss();
            } catch (Exception e) {
                ManejoErrores.registrarError_MostrarDialogo(ctx, e,
                        DialogoCerrarListaVerificacion.class.getSimpleName(), "TareaCerrarLista_onPostExecute",
                        null, null);
            }
        }
    }


}
