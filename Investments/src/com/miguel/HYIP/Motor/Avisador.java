package com.miguel.HYIP.Motor;

import com.miguel.HYIP.core.Dayeer;
import com.miguel.HYIP.core.HourlyBank;
import com.miguel.HYIP.helper.Configuracion;

public class Avisador {

	public static void main(String[] args) {
				
		//processAllHourlyBank();
		//processThisHourlyBank("miki", "atitelovoyadecir");
		
		processAllDayeer();
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
				hourlyBank.withdraw(1);
				//hourlyBank.makeInternalDeposit(amount);
				hourlyBank.logout(); 				
			}
			
		}

	}
	
	private static void processAllDayeer() {
		Dayeer dayeer = new Dayeer();
		double accountAmount = 0.0;
		double withdrawAmount = 0.0;
		if (dayeer.isAlive()) {
			String user, pwd;
			int total = Integer.parseInt(Configuracion.getProperty("dayeer_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("dayeer" + i + "_user");
				pwd = Configuracion.getProperty("dayeer" + i + "_pwd");
				if (dayeer.login(user,pwd)) {
//					if (dayeer.isPossibleDeposit()) {
//						System.out.println("SI es posible");
//					} else {
//						System.out.println("NO es posible");
//					}
						
					accountAmount = dayeer.getAmount();
					System.out.println(user + ": " + accountAmount);
					// Sólo sacamos cantidades enteras, para evitar comisiones de más
					withdrawAmount = new Double(accountAmount).intValue();
					// Sólo si es >1 nos interesa
					if (withdrawAmount >= 1) {
						dayeer.withdraw(withdrawAmount);	
					}					
//					//dayeer.makeInternalDeposit(0.0);
					dayeer.logout(); 				
				}
			}
		}
	}
	
}
