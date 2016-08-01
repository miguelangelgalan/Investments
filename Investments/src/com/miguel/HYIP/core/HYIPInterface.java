package com.miguel.HYIP.core;

public interface HYIPInterface {

	boolean isAlive();
	boolean login(String user, String pass);
	boolean logout();
	double getAmount();

}
