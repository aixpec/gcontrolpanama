package com.gisystems.gcontrolpanama.reglas;

public class MiExcepcion extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String msgCustom;

	
	

	public String getMsgCustom() {
		return msgCustom;
	}




	public void setMsgCustom(String msgCustom) {
		this.msgCustom = msgCustom;
	}




	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return  this.msgCustom + " -- " + super.getMessage();
	}

	
}
