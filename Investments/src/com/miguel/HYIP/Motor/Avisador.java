package com.miguel.HYIP.Motor;

import com.miguel.HYIP.core.HourlyBank;

public class Avisador {

	public static void main(String[] args) {
		HourlyBank hourlyBank = new HourlyBank();
		double amount = 0;
		
		if (hourlyBank.isAlive()) {
			hourlyBank.login();
			amount = hourlyBank.getAmount();
			//if (amount > hourlyBank.getMinDeposit())
			System.out.println(amount);
			hourlyBank.logout(); 
			
		}

	}

}
