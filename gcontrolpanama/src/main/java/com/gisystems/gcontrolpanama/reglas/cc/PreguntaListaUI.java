package com.gisystems.gcontrolpanama.reglas.cc;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gisystems.gcontrolpanama.models.cc.AccionRespuesta;
import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.Respuesta;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;


public class PreguntaListaUI extends PreguntaUI {

    private Spinner spnRespuesta;
    private EditText txtFiltro;
    private ArrayAdapter<CharSequence> adapterLista;
    private boolean modoFiltroParaSpinner;
    private TextWatcher listenerFiltroParaSpinner;

    PreguntaListaUI(Context actividad, Pregunta pregunta, PreguntaRespondida preguntaRespondida) {
        super(actividad, pregunta, preguntaRespondida);
        this.setOrientation(LinearLayout.VERTICAL);
        crearViews_Lista();
    }

    private void crearViews_Lista() {
        int padding = Math.round(dipToPx(10));
        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txtFiltro = new EditText(context);
        txtFiltro.setLayoutParams(layoutParams);
        txtFiltro.setPadding(padding, padding, padding, padding);
        txtFiltro.setInputType(InputType.TYPE_CLASS_TEXT);
        txtFiltro.setImeOptions(0x000000b1);
        habilitarListenerFiltroParaSpinner();
        this.addView(txtFiltro);

        spnRespuesta = new Spinner(context);
        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, Math.round(dipToPx(60)));
        spnRespuesta.setPadding(padding, padding, padding, padding);
        spnRespuesta.setLayoutParams(layoutParams);
        this.addView(spnRespuesta);

        // ***poblar Spinners
        adapterLista = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
        adapterLista.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayList<Respuesta> respuestasPosibles = this.pregunta.getRespuestas();
        // Para cada posible respuesta se agregara un elemento al adaptador
        for (int i = 0; i <= respuestasPosibles.size() - 1; i++) {
            adapterLista.add(respuestasPosibles.get(i).getRespuesta());
        }
        // Asociar el adaptador al respectivo spinner
        spnRespuesta.setAdapter(adapterLista);
    }

    @Override
    public boolean esRespuestaIngresada() {
        return (spnRespuesta.getSelectedItem() != null);
    }

    @Override
    public boolean esRespuestaValida(StringBuilder mensaje) {
        boolean valida = false;
        String respuesta = "";
        if (spnRespuesta.getCount() > 0)
        {
            respuesta = spnRespuesta.getSelectedItem().toString();
        }
        valida = respuesta.length() > 0;
        return valida;
    }

    private void habilitarListenerFiltroParaSpinner() {
        if (listenerFiltroParaSpinner == null) {
            listenerFiltroParaSpinner = new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (modoFiltroParaSpinner) {
                        adapterLista.getFilter().filter(s);
                    }
                }
                @Override
                public void afterTextChanged(Editable arg0) {
                }
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                }
            };
            txtFiltro.addTextChangedListener(listenerFiltroParaSpinner);
        }
        modoFiltroParaSpinner = true;
        txtFiltro.setHint("Filtrar");
    }

    @Override
    public void mostrarRespuestas() {
        //1. Verificar si la respuesta a la pregunta de lugar visitado es "otro"
        boolean respuestaEsOtro = false;
        int contador = 0;
        String respuesta = preguntaRespondida.getRespuestasSeleccionadas().get(0).getValorRespuesta();
        while (!respuestaEsOtro && contador < pregunta.getRespuestas().size()) {
            if (pregunta.getRespuestas().get(contador).getIdRespuesta() ==
                preguntaRespondida.getRespuestasSeleccionadas().get(0).getIdRespuesta())
            {
                if (pregunta.getRespuestas().get(contador).SeEncuentraEstaAccion(AccionRespuesta.TipoDeAccionDeRespuesta.IngresarRespuestaNoDefinida))
                {
                    respuestaEsOtro = true;
                    respuesta = pregunta.getRespuestas().get(contador).getRespuesta();
                }
            }
            contador++;
        }
        //2. Seleccionar la opción correspondiente en el Spinner
        for(int i=0; i< adapterLista.getCount(); i++)
        {
            if(String.valueOf(spnRespuesta.getItemAtPosition(i)).equals(respuesta))
            {
                spnRespuesta.setSelection(i);
				if (respuestaEsOtro) {
					txtFiltro.setText(preguntaRespondida.getRespuestasSeleccionadas().get(0).getValorRespuesta());
				}
            }
        }
    }

    @Override
    public ArrayList<RespuestaIngresada> obtenerRespuestas() {
        RespuestaIngresada respuestaSeleccionada;
        String respuesta = "";
        Boolean respuestaValida = false;
        //Obtenemos la respuesta del spinner
        if (spnRespuesta.getCount() > 0)
        {
            //Si IdAccionResputas = 2 entonces seleccionaron la opción OTROS
            if (pregunta.getRespuestas().get(spnRespuesta.getSelectedItemPosition()).SeEncuentraEstaAccion(AccionRespuesta.TipoDeAccionDeRespuesta.IngresarRespuestaNoDefinida))
            {
                //Para validar que la opción OTROS del inputtext contiene datos
                respuesta = txtFiltro.getText().toString();
            }
            else
            {
                respuesta = spnRespuesta.getSelectedItem().toString();
            }
        }
        //**Validar respuesta
        if (respuesta.length() > 0) {
            try {
                respuestaValida = FuncionesGlobales.validateInput(context.getSharedPreferences("validar_data",Context.MODE_PRIVATE).getBoolean("validar_data", false), pregunta, respuesta);
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
                Toast.makeText(this.context, e1.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if (respuestaValida)
        {
            try {
                //**************************************
                //**** Respuesta de la pregunta 1 ******
                respuestaSeleccionada =  new RespuestaIngresada();
                respuestaSeleccionada.setValorRespuesta(respuesta);
                //la posicion del spinner sera la misma que la del arreglo
                for(int index = 0; index < pregunta.getRespuestas().size(); index++)
                {
                    if ( pregunta.getRespuestas().get(index).getRespuesta().equals(spnRespuesta.getSelectedItem().toString()))
                    {
                        respuestaSeleccionada.setIdRespuesta(pregunta.getRespuestas().get(index).getIdRespuesta());
                        break;
                    }
                }

                this.preguntaRespondida.getRespuestasSeleccionadas().add(respuestaSeleccionada);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.preguntaRespondida.getRespuestasSeleccionadas();
    }

    //public void verificarAccionesRealizar(QuestionSet currentQuestionSet) {}
}