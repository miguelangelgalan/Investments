package com.miguel.HYIP.Motor;

import com.miguel.HYIP.core.HourlyBank;
import com.miguel.HYIP.helper.Configuracion;

public class Avisador {

	public static void main(String[] args) {
				
		//processAllHourlyBank();
		processThisHourlyBank("neonat", "atitelovoyadecir");
	}
	
	private static void processAllHourlyBank() {
		HourlyBank hourlyBank = new HourlyBank();
		double amount = 0;
		if (hourlyBank.isAlive()) {
			String user, pwd;
			int total = Integer.parseInt(Configuracion.getProperty("hourly_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("hourly" + i + "_user");
				pwd = Configuracion.getProperty("hourly" + i + "_pwd");
				if (hourlyBank.login(user,pwd)) {
					amount = hourlyBank.getAmount();
					System.out.println(user + ": " + amount);
					//hourlyBank.withdraw(0.0);
					hourlyBank.makeInternalDeposit(0.0);
					hourlyBank.logout(); 				
				}
			}
		}

	}
	private static void processThisHourlyBank(String user, String pass) {
		HourlyBank hourlyBank = new HourlyBank();
		double amount = 0;
		if (hourlyBank.isAlive()) {
			if (hourlyBank.login(user,pass)) {
				amount = hourlyBank.getAmount();
				System.out.println(user + ": " + amount);
				//hourlyBank.withdraw(0.0);
				hourlyBank.makeInternalDeposit(amount);
				hourlyBank.logout(); 				
			}
			
		}

	}
	
}
