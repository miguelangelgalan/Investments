package com.miguel.HYIP.core;

import javax.net.ssl.HttpsURLConnection;

public class HourlyBank implements HYIP {

	private String baseUrl = "https://hourlybank.biz";
	
	public boolean isAlive() {
		//mechanize  Buscar esta libreria
		//HttpsURLConnection hCon = new
		return true;
	}
	
	public boolean login() {
		return true;
	}

	public boolean logout() {
		return true;
	}
	
	public double getAmount() {
		return 0.0;
	}
}
