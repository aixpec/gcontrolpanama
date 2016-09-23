package com.gisystems.gcontrolpanama.reglas.cc;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;
import com.gisystems.gcontrolpanama.models.cc.Respuesta;
import com.gisystems.gcontrolpanama.models.cc.AccionRespuesta;
import com.gisystems.gcontrolpanama.models.cc.RespuestaIngresada;

public class PreguntaListaTextoUI extends PreguntaUI {

    private Spinner spnRespuesta;
    private EditText txtFiltro;
    private ArrayAdapter<CharSequence> adapterLista;
    private boolean modoFiltroParaSpinner;
    private TextWatcher listenerFiltroParaSpinner;

    PreguntaListaTextoUI(Context actividad, Pregunta pregunta, PreguntaRespondida preguntaRespondida) {
        super(actividad, pregunta, preguntaRespondida);
        this.setOrientation(LinearLayout.VERTICAL);
        crearViews_ListaTexto();
    }

    private void crearViews_ListaTexto() {
        int padding = Math.round(dipToPx(10));
        LinearLayout.LayoutParams layoutParams;
        layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txtFiltro = new EditText(context);
        txtFiltro.setLayoutParams(layoutParams);
        txtFiltro.setPadding(padding, padding, padding, padding);
        txtFiltro.setInputType(InputType.TYPE_CLASS_TEXT);
        txtFiltro.setImeOptions(0x000000b1);
        this.addView(txtFiltro);

        spnRespuesta = new Spinner(context);
        layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Math.round(dipToPx(60)));
        spnRespuesta.setPadding(padding, padding, padding, padding);
        spnRespuesta.setLayoutParams(layoutParams);
        habilitarListenerSpinnerItemSeleccionado();
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

    private void deshabilitarListenerFiltroParaSpinner() {
        modoFiltroParaSpinner = false;
        txtFiltro.setHint("Especifique") ;
        txtFiltro.setFocusable(true);
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
            if (modoFiltroParaSpinner) {
                respuesta = spnRespuesta.getSelectedItem().toString();
            } else {
                respuesta = txtFiltro.getText().toString();
            }
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

    private void habilitarListenerSpinnerItemSeleccionado() {
        spnRespuesta.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> paramAdapterView,
                                       View paramView, int paramInt, long paramLong) {
                //Verificar si esta respuesta permitira una respuesta no definida
                if( pregunta.getRespuestas().get(paramInt).SeEncuentraEstaAccion(AccionRespuesta.TipoDeAccionDeRespuesta.IngresarRespuestaNoDefinida))
                {
                    deshabilitarListenerFiltroParaSpinner();
                    if(preguntaRespondida.getRespuestasSeleccionadas().size() > 0)
                    {
                        txtFiltro.setText(preguntaRespondida.getRespuestasSeleccionadas().get(0).getValorRespuesta());
                    }
                }
                else
                {
                    habilitarListenerFiltroParaSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> paramAdapterView) { }
        });
    }

    @Override
    public void mostrarRespuestas() {
        if (preguntaRespondida.getRespuestasSeleccionadas().size() > 0) {
            //1. Verificar si la respuesta a la pregunta es "otro"
            boolean respuestaEsOtro = false;
            int contador = 0;
            String respuesta = preguntaRespondida.getRespuestasSeleccionadas().get(0).getValorRespuesta();
            while (!respuestaEsOtro && contador < pregunta.getRespuestas().size()) {
                if (pregunta.getRespuestas().get(contador).getIdRespuesta() ==
                        preguntaRespondida.getRespuestasSeleccionadas().get(0).getIdRespuesta()) {
                    if (pregunta.getRespuestas().get(contador).SeEncuentraEstaAccion(AccionRespuesta.TipoDeAccionDeRespuesta.IngresarRespuestaNoDefinida)) {
                        respuestaEsOtro = true;
                        respuesta = pregunta.getRespuestas().get(contador).getRespuesta();
                    }
                }
                contador++;
            }
            //2. Seleccionar la opción correspondiente en el Spinner
            for (int i = 0; i < adapterLista.getCount(); i++) {
                if (String.valueOf(spnRespuesta.getItemAtPosition(i)).equals(respuesta)) {
                    spnRespuesta.setSelection(i);
                    if (respuestaEsOtro) {
                        deshabilitarListenerFiltroParaSpinner();
                        txtFiltro.setText(preguntaRespondida.getRespuestasSeleccionadas().get(0).getValorRespuesta());
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<RespuestaIngresada> obtenerRespuestas() {
        RespuestaIngresada respuestaSeleccionada;
        String respuesta = "";
        Boolean respuestaValida = false;
        //**1. Respuesta de lugar
        //Tipo de Dato PreguntaListaUI
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
                //**Verificar que tenga un objeto RespuestaIngresada
                if (this.preguntaRespondida.getRespuestasSeleccionadas().size() > 0) {
                    respuestaSeleccionada =  this.preguntaRespondida.getRespuestasSeleccionadas().get(0);
                } else {
                    respuestaSeleccionada =  new RespuestaIngresada(this.pregunta);
                    this.preguntaRespondida.getRespuestasSeleccionadas().add(respuestaSeleccionada);
                }
                //**************************************
                //**** Respuesta de la pregunta 1 ******
                respuestaSeleccionada.setValorRespuesta(respuesta);
                respuestaSeleccionada.setFechaCaptura(new Date());
                //la posicion del spinner sera la misma que la del arreglo
                for(int index = 0; index < pregunta.getRespuestas().size(); index++)
                {
                    if ( pregunta.getRespuestas().get(index).getRespuesta().equals(spnRespuesta.getSelectedItem().toString()))
                    {
                        respuestaSeleccionada.setIdRespuesta(pregunta.getRespuestas().get(index).getIdRespuesta());
                        respuestaSeleccionada.setDescripcionRespuesta(respuesta);
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
