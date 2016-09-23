package com.gisystems.gcontrolpanama.models.chk;

import com.gisystems.gcontrolpanama.models.cc.Pregunta;
import com.gisystems.gcontrolpanama.models.cc.PreguntaRespondida;

/**
 * Created by rlemus on 01/09/2016.
 */
public class ListaVerificacion_PreguntaRespondida extends PreguntaRespondida {

    public ListaVerificacion_PreguntaRespondida(Pregunta pregunta) {
        super(pregunta);
    }

    public int GrabarRespuestasEnBD() {
        return 0;
    };

}
