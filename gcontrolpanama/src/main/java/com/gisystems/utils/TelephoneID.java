package com.gisystems.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.gisystems.gcontrolpanama.models.AppValues;

public class TelephoneID {
	private String imsi;
	private String imei;

	public TelephoneID(Context context) {
		imei = AppValues.SharedPref_obtenerDeviceID(context);
		imsi = AppValues.SharedPref_obtenerSuscriberID(context);
	}

	public String getImsi() {
		if (imsi == null) { 
			return ""; 
		} else {
			return this.imsi;
		}
	}

	public String getImei() {
		if (imei == null) { 
			return ""; 
		} else {
			return this.imei;
		}
	}

    public boolean seTienenLosDatos() {
        return (this.imei.length() > 0 && this.imsi.length() > 0);
    }

    public void obtenerDatosDelTelefono(Context context) {
        if (tengoPermisoParaAccederAlTelefono(context)) {
            android.telephony.TelephonyManager manager = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            this.imei = manager.getDeviceId();
            this.imsi = manager.getSubscriberId();
            AppValues.SharedPref_guardarDeviceID(context, this.imei);
            AppValues.SharedPref_guardarSuscriberID(context, this.imsi);
        }
    }

    private boolean tengoPermisoParaAccederAlTelefono(Context context) {
        return (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED);
    }

}
