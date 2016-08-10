package com.miguel.HYIP.Motor;

import com.gistlabs.mechanize.document.html.form.Email;
import com.miguel.HYIP.core.Dayeer;
import com.miguel.HYIP.core.HourlyBank;
import com.miguel.HYIP.helper.Configuracion;

public class HYIPManager {

	
	public void doDayeer() {
		Dayeer dayeer = new Dayeer();
		if (dayeer.isAlive()) {
			String user, pwd;
			int total = Integer.parseInt(Configuracion.getProperty("dayeer_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("dayeer" + i + "_user");
				pwd = Configuracion.getProperty("dayeer" + i + "_pwd");
				if (dayeer.login(user,pwd)) {

					// 1.- Comprobamos SALDO
					double accountAmount = dayeer.getAmount();
					System.out.println("DAYEER " + user + ": Amount: " + accountAmount);

					// 2.- Sacamos SALDO
					// Sólo sacamos cantidades enteras, para evitar comisiones de más
					double withdrawAmount = new Double(accountAmount).intValue();
					// Sólo si es >1 nos interesa
					if (withdrawAmount >= 1) {
						dayeer.withdraw(withdrawAmount);	
					}					

					// 3.- Si es posible hacer otro depósito, AVISAR

					if (dayeer.isPossibleDeposit()) {
						System.out.println("SI es posible");
						// AVISAR
						//Email - SMS
					} else {
						System.out.println("NO es posible");
					}
						
//					//dayeer.makeInternalDeposit(0.0);
					dayeer.logout(); 				
				}
			}
		}
	}
	
	public void doHourlyBank() {
		HourlyBank hourlyBank = new HourlyBank();
		double amount = 0;
		if (hourlyBank.isAlive()) {
			String user, pwd;
			int total = Integer.parseInt(Configuracion.getProperty("hourly_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("hourly" + i + "_user");
				pwd = Configuracion.getProperty("hourly" + i + "_pwd");
				if (hourlyBank.login(user,pwd)) {
					//1.- Comprobamos SALDO
					amount = hourlyBank.getAmount();
					System.out.println("HOURLYBANK - " + user + " Amount: " + amount);
					
					//2.- Chequeamos si hay depósito activo.
					// Si lo hay, no hacemos nada.
					// Si no lo hay, hacemos depósito de 5$, y sacamos el resto.
					if (!hourlyBank.existActiveDeposit()) {
						boolean estado = hourlyBank.makeInternalDeposit(5.0);
						amount = hourlyBank.getAmount();
						hourlyBank.withdraw(amount);	
					}
					hourlyBank.logout(); 				
				}
			}
		}
	}
}
