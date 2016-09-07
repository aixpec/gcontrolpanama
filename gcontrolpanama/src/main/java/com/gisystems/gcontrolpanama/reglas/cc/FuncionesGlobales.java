package com.gisystems.gcontrolpanama.reglas.cc;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;


import com.gisystems.gcontrolpanama.models.cc.Pregunta;

/**
 * Created by rlemus on 01/09/2016.
 */
public class FuncionesGlobales {

    /**
     * Valida una entrada de datos de acuerdo a parámetros definidos.
     *
     * @param validarParametro define si se validará o no el parametro en cuestión.
     * @param pregunta objeto pregunta con sus propiedades para el respectivo proceso.
     * @param respuesta texto que define la respuesta ingresada.
     * @return si la respuesta es válida de acuerdo al tipo de dato de la pregunta.
     * @throws IllegalArgumentException Se dispara cuando no coincide con las validaciones respectivas.
     */
    public static boolean validateInput(boolean validarParametro, Pregunta pregunta, Object respuesta) throws IllegalArgumentException
    {
        boolean isValid = false;
        if(!validarParametro)
        {
            isValid = true;
        }
        else
        {
            TipoCampoUI tipo = TipoCampoUI.getTipoCampoCorrespondiente(pregunta.getIdTipoDato());
            switch (tipo)
            {
                case Lista: // entero; debe el usuario seleccionar de una lista
                    //La respuesta proviene de una lista,  por lo que lo único que se valida es que no este en blanco
                    if (String.valueOf(respuesta).length()>0 )
                    {
                        isValid=true;
                    }
                    else
                    {
                        isValid=false;
                        throw new IllegalArgumentException("Seleccione una respuesta válida");
                    }

                    break;
                case NumeroReal: // decimal siempre existira un registro
                    //cuando se quiera validar, si la respuesta es libre,
                    //no existira registro en respuestas y no se validara
                    //el rango pero si se validara que sea un numero
                    String operador = null;
                    double numero = 0;
                    String operador2 = null;
                    double numero2 = 0;

                    String[] ElementosDeCondicion;

                    try {
                        Double.parseDouble(respuesta.toString());
                        //extraer las validaciones
                        if (pregunta.getRespuestas().size() > 0)
                        {
                            //Ejecutar siempre este bloque
                            ElementosDeCondicion=pregunta.getRespuestas().get(0).getRespuesta().split(" ");
                            Log.w("","Largo " + String.valueOf(ElementosDeCondicion.length));
                            if(ElementosDeCondicion.length==2)//Solo una condición
                            {
                                operador  = ElementosDeCondicion[0];
                                numero = Double.parseDouble(ElementosDeCondicion[1]);
                            }
                            else if (ElementosDeCondicion.length==4)
                            {
                                operador  = ElementosDeCondicion[0];
                                numero = Double.parseDouble(ElementosDeCondicion[1]);
                                operador2  = ElementosDeCondicion[2];
                                numero2 = Double.parseDouble(ElementosDeCondicion[3]);
                            }
                            if((ElementosDeCondicion.length==2)  ||   (ElementosDeCondicion.length==4))
                            {
                                // si se ha ingresado mal se aceptara cualquier valor
                                // para no entorpecer la recoleccion de datos
                                if(operador.equals(">") || operador.equals("<") || operador.equals("<="))
                                {
                                    if(operador.equals(">"))
                                    {
                                        if(Double.parseDouble(respuesta.toString()) > numero)
                                        {
                                            isValid = true;
                                        }
                                        else
                                        {
                                            throw new IllegalArgumentException("El numero ingresado no esta en un rango valido");
                                        }
                                    }
                                    else if(operador.equals("<"))
                                    {
                                        if(Double.parseDouble(respuesta.toString()) < numero)
                                        {
                                            isValid = true;
                                        }
                                        else
                                        {
                                            throw new IllegalArgumentException("El numero ingresado no esta en un rango valido");
                                        }
                                    }
                                    else if(operador.equals("<="))
                                    {

                                        if(Double.parseDouble(respuesta.toString()) <= numero)
                                        {
                                            isValid = true;
                                        }
                                        else
                                        {
                                            throw new IllegalArgumentException("El numero ingresado no esta en un rango valido");
                                        }

                                    }
                                    if (operador2!=null)
                                    {
                                        if(operador2.equals("<"))
                                        {
                                            if ((Double.parseDouble(respuesta.toString()) < numero2) && (isValid))
                                            {
                                                isValid = true;
                                            }
                                            else
                                            {
                                                throw new IllegalArgumentException("El numero ingresado no esta en un rango valido");
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    // si no se guarda el formato especificado
                                    // el valor se dara como valido
                                    isValid = true;
                                }

                            }
                            else
                            {
                                isValid = true;
                            }
                        }
                        else
                        {
                            isValid = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException("El numero ingresado no es valido o la condicion es invalida; formato de la condicion: {[OPERADOR ESPACIO NUMERO ej: X < 100, X > 25]}");
                    }
                    break;
                case NumeroEntero: // decimal siempre existira un registro
                    //cuando se quiera validar, si la respuesta es libre,
                    //no existira registro en respuestas y no se validara
                    //el rango pero si se validara que sea un numero
                    break;
                case Texto: //Texto
                    try {
                        if (respuesta.toString().length() > 0)
                        {
                            isValid = true;
                        }
                        else
                        {
                            throw new IllegalArgumentException("Ingrese un texto valido");
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Ingrese un texto valido");
                    }
                    break;
                case Fecha: //Fecha

                    try {
                        if(isThisDateValid(respuesta.toString(), "dd-MM-yyyy"))
                        {
                            isValid = true;
                        }
                        else
                        {
                            throw new IllegalArgumentException("Ingrese una fecha válida (dia-mes-año)");
                        }

                    } catch (Exception e) {
                        throw new IllegalArgumentException("Ingrese una fecha válida (dia-mes-año)");
                    }

                    break;
                case Lista_Texto:
                    try {
                        if (respuesta.toString().length() > 0)
                        {
                            isValid = true;
                        }
                        else
                        {
                            throw new IllegalArgumentException("Ingrese un texto que describa la opción 'OTRO(S)'");
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Ingrese un texto que describa la opción 'OTRO'");
                    }
                    break;
                case Lista_Numero_Moneda:
                    try {
                        Double.parseDouble(respuesta.toString());
                        isValid = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException("El numero ingresado no es valido");
                    }
                    break;
                case Opcion_Multiple:
                    try {
                        if (respuesta.toString().length() > 0)
                        {
                            isValid = true;
                        }
                        else
                        {
                            throw new IllegalArgumentException("Seleccione al menos una opción, o ingrese un valor para la opción otros");
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Seleccione al menos una opción, o ingrese un valor para la opción otros");
                    }
                    break;
            }
        }
        return isValid;
    }



    public static boolean isThisDateValid(String dateToValidate, String dateFormat){
        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }


}
