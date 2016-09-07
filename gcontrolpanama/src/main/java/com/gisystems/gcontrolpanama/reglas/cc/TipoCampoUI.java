package com.gisystems.gcontrolpanama.reglas.cc;

public enum TipoCampoUI {
    Lista(1),NumeroReal(2),NumeroEntero(3),Texto(4),Fecha(5),Hora(6),
    Lista_Texto(7),Lista_Numero_Moneda(8),
    Opcion_Multiple(9);

    private final int intTipoCampo;

    TipoCampoUI(int valorTipoCampo) {
        this.intTipoCampo = valorTipoCampo;
    }

    public int getValorTipoCampo() {
        return this.intTipoCampo;
    }

    public static TipoCampoUI getTipoCampoCorrespondiente(int valorTipoCampo) {
        TipoCampoUI resultado = null;
        switch (valorTipoCampo) {
            case 1:
                resultado = Lista;
                break;
            case 2:
                resultado = NumeroReal;
                break;
            case 3:
                resultado = NumeroEntero;
                break;
            case 4:
                resultado = Texto;
                break;
            case 5:
                resultado = Fecha;
                break;
            case 6:
                resultado = Hora;
                break;
            case 7:
                resultado = Lista_Texto;
                break;
            case 8:
                resultado = Lista_Numero_Moneda;
                break;
            case 9:
                resultado = Opcion_Multiple;
                break;
        }
        return resultado;
    }

}
