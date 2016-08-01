package com.miguel.HYIP.core;

public class InvestJob implements HYIPInterface {
	
	public boolean isAlive() {
		return true;
		// A ver si se ve
	}
	
	public boolean login(String user, String pass) {
		return true;
	}

	public boolean logout() {
		return true;
	}
	
	public double getAmount() {
		return 0.0;
	}
}
