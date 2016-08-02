package com.gisystems.utils;

public class TelephoneID {
	private String imsi;
	private String imei;
	
	public String getImsi() {
		if (imsi == null) { 
			return ""; 
		} else {
			return this.imsi;
		}
	}
	
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	
	public String getImei() {
		if (imei == null) { 
			return ""; 
		} else {
			return this.imei;
		}
	}
	
	public void setImei(String imei) {
		this.imei = imei;
	}
}
