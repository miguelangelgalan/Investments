package com.miguel.HYIP.Motor;

import com.gistlabs.mechanize.document.html.form.Email;
import com.miguel.HYIP.core.BitIncome;
import com.miguel.HYIP.core.Dayeer;
import com.miguel.HYIP.core.Feers;
import com.miguel.HYIP.core.HourlyBank;
import com.miguel.HYIP.core.HourlyCool;
import com.miguel.HYIP.core.HourlyOil;
import com.miguel.HYIP.core.PayHour;
import com.miguel.HYIP.core.ROIHour;
import com.miguel.HYIP.helper.Configuracion;

public class HYIPManager {

	
	public void doDayeer() {
		Dayeer dayeer = new Dayeer();
		if (dayeer.isAlive()) {
			String user, pwd, modo;
			int total = Integer.parseInt(Configuracion.getProperty("dayeer_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("dayeer" + i + "_user");
				pwd = Configuracion.getProperty("dayeer" + i + "_pwd");
				modo = Configuracion.getProperty("dayeer" + i + "_modo");
				if (dayeer.login(user,pwd)) {

					// 1.- Comprobamos SALDO
					double accountAmount = dayeer.getAmount();
					System.out.println("DAYEER " + user + ": Amount: " + accountAmount);

					// 2.- Sacamos SALDO según modo
					if (modo.equalsIgnoreCase("mantenimiento")) {
						if (dayeer.getActiveDeposit() == 0.0) {  // Se acabó el depósito anterior
							// Reinvertimos y sacamos ganancias
							if (dayeer.makeInternalDeposit(6)) {
								accountAmount = dayeer.getAmount();
								dayeer.withdraw(accountAmount);								
							} 
						}
					} else if (modo.equalsIgnoreCase("mantenimiento2")) {
						if (dayeer.getActiveDeposit() == 0.0) {  // Se acabó el depósito anterior
							// Reinvertimos todo
							accountAmount = dayeer.getAmount();
							dayeer.makeInternalDeposit(accountAmount);
						}
					} else { // Modo normal. Sacamos dinero.
						// Sólo sacamos cantidades enteras, para evitar comisiones de más
						double withdrawAmount = new Double(accountAmount).intValue();
						// Sólo si es >1 nos interesa
						if (withdrawAmount >= 1) {
							dayeer.withdraw(withdrawAmount);	
						}											
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
	
	public void doFeers() {
		Feers feers = new Feers();
		if (feers.isAlive()) {
			String user, pwd, modo;
			int total = Integer.parseInt(Configuracion.getProperty("feers_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("feers" + i + "_user");
				pwd = Configuracion.getProperty("feers" + i + "_pwd");
				modo = Configuracion.getProperty("feers" + i + "_modo");
				if (feers.login(user,pwd)) {
					
					// 1.- Comprobamos SALDO
					double accountAmount = feers.getAmount();
					System.out.println("FEERS " + user + ": Amount: " + accountAmount);

					// 2.- Sacamos SALDO según modo
					if (modo.equalsIgnoreCase("mantenimiento")) {
						if (feers.getActiveDeposit() == 0.0) {  // Se acabó el depósito anterior
							// Reinvertimos y sacamos ganancias. 7 - 0,50
							if (feers.makeInternalDeposit(7)) {
								accountAmount = feers.getAmount();
								feers.withdraw(accountAmount);								
							} 
						}
					} else { // Modo normal. Sacamos dinero.
						// Sólo sacamos cantidades enteras, para evitar comisiones de más
						double withdrawAmount = new Double(accountAmount).intValue();
						// Sólo si es >1 nos interesa
						if (withdrawAmount >= 1) {
							feers.withdraw(withdrawAmount);	
						}											
					}

					feers.logout(); 				
				}
			}
		}
	}
	
	public void doHourlyOil() {
		HourlyOil hourlyOil = new HourlyOil();
		if (hourlyOil.isAlive()) {
			String user, pwd, modo;
			int total = Integer.parseInt(Configuracion.getProperty("hourlyoil_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("hourlyoil" + i + "_user");
				pwd = Configuracion.getProperty("hourlyoil" + i + "_pwd");
				modo = Configuracion.getProperty("hourlyoil" + i + "_modo");
				if (hourlyOil.login(user,pwd)) {					
					// 1.- Comprobamos SALDO
					double accountAmount = hourlyOil.getAmount();
					System.out.println("HOURLYOIL " + user + ": Amount: " + accountAmount);

					// 2.- Sacamos SALDO según modo
					if (modo.equalsIgnoreCase("mantenimiento")) {
						if (hourlyOil.getActiveDeposit() == 0.0) {  // Se acabó el depósito anterior
							// Reinvertimos y sacamos ganancias. 7 - 0,50
							if (hourlyOil.makeInternalDeposit(2.10)) {
								accountAmount = hourlyOil.getAmount();
								hourlyOil.withdraw(accountAmount);								
							} 
						}
					} else { // Modo normal. Sacamos dinero.
						// Sólo sacamos cantidades enteras, para evitar comisiones de más
						double withdrawAmount = new Double(accountAmount).intValue();
						// Sólo si es >1 nos interesa
						if (withdrawAmount >= 1) {
							hourlyOil.withdraw(withdrawAmount);	
						}											
					}

					hourlyOil.logout(); 				
				}
			}
		}
	}	
	public void doROIHour() {
		ROIHour roiHour = new ROIHour();
		if (roiHour.isAlive()) {
			String user, pwd, modo;
			int total = Integer.parseInt(Configuracion.getProperty("roihour_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("roihour" + i + "_user");
				pwd = Configuracion.getProperty("roihour" + i + "_pwd");
				modo = Configuracion.getProperty("roihour" + i + "_modo");
				if (roiHour.login(user,pwd)) {							
					// 1.- Comprobamos SALDO
					double accountAmount = roiHour.getAmount();
					System.out.println("ROIHOUR " + user + ": Amount: " + accountAmount);

					// 2.- Sacamos SALDO según modo
					if (modo.equalsIgnoreCase("mantenimiento")) {
						// Nuevo método, por no ser fiable el tema de si hay un depósito activo.
						accountAmount = roiHour.getAmount();
						if (accountAmount > 2.0) {  
							// Reinvertimos y sacamos el resto. // OJO , fallará cuando hay dos depósitos de 5...
							// Si falla, no sacamos.
							if (roiHour.makeInternalDeposit(2)) {
								accountAmount = roiHour.getAmount();
								roiHour.withdraw(accountAmount);								
							} 
						}
					}
					else if (modo.equalsIgnoreCase("mantenimiento_compuesto")) {   // OJO, NO HACERLO CON ESTA, DEPOSITOS ALTOS LOS INVIERTE a 1 AÑO.
							if (roiHour.getActiveDeposit() == 0.0) {  // Se acabó el depósito anterior
								// Reinvertimos y sacamos ganancias. Sacamos 5% de las ganancias, y reinvertimos el resto
								//Calculos:
								accountAmount = roiHour.getAmount();
								Double withdrawAmount = accountAmount * 0.05;
								Double reinversionAmount = accountAmount - withdrawAmount;
								if (roiHour.makeInternalDeposit(reinversionAmount)) {									
									roiHour.withdraw(withdrawAmount);								
								} 
							}
						}					
					else { // Modo normal. Sacamos dinero.
						// Sólo sacamos cantidades enteras, para evitar comisiones de más, excepto si ya han vencido los depósitos, que sacamos lo que quede...
						if (roiHour.getActiveDeposit() == 0.0) { // Sacamos todo
							roiHour.withdraw(accountAmount);
						} else {
							double withdrawAmount = new Double(accountAmount).intValue();
							// Sólo si es >1 nos interesa
							if (withdrawAmount >= 1) {
								roiHour.withdraw(withdrawAmount);	
							}
						}					}

					roiHour.logout(); 				
				}
			}
		}
	}		
	public void doHourlyCool() {
		HourlyCool hourlyCool = new HourlyCool();
		if (hourlyCool.isAlive()) {
			String user, pwd, modo;
			int total = Integer.parseInt(Configuracion.getProperty("hourlycool_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("hourlycool" + i + "_user");
				pwd = Configuracion.getProperty("hourlycool" + i + "_pwd");
				modo = Configuracion.getProperty("hourlycool" + i + "_modo");
				if (hourlyCool.login(user,pwd)) {

					// 1.- Comprobamos SALDO
					double accountAmount = hourlyCool.getAmount();
					System.out.println("HOURLYCOOL " + user + ": Amount: " + accountAmount);

					// 2.- Sacamos SALDO según modo
					if (modo.equalsIgnoreCase("mantenimiento")) {
						System.out.println("Modo Mantenimiento");
						if (hourlyCool.getActiveDeposit() == 0.0) {  // Se acabó el depósito anterior
							System.out.println("Se acabó deposito anterior");
							// Sacamos las ganancias si son > 0,50
							// Sino, reinvertimos todo.
							if (accountAmount - 3.0 >= 0.50) {
								System.out.println("Tenemos 0,50 o más de ganancia");
								if (hourlyCool.makeInternalDeposit(3)) {
									accountAmount = hourlyCool.getAmount();
									hourlyCool.withdraw(accountAmount);								
								} 								
							} else {
								System.out.println("Tenemos menos de 0,50 de ganancia");
								hourlyCool.makeInternalDeposit(accountAmount);
							}
						}
					} else { // Modo normal. Sacamos dinero.
						// Sólo sacamos cantidades enteras, para evitar comisiones de más, excepto si ya han vencido los depósitos, que sacamos lo que quede...
						if (hourlyCool.getActiveDeposit() == 0.0) { // Sacamos todo
							if (accountAmount > 0.0) {
									hourlyCool.withdraw(accountAmount);
							}
						} else {
							double withdrawAmount = new Double(accountAmount).intValue();
							// Sólo si es >1 nos interesa
							if (withdrawAmount >= 1) {
								hourlyCool.withdraw(withdrawAmount);	
							}
						}
					}

					// 3.- Si es posible hacer otro depósito, AVISAR

					if (hourlyCool.isPossibleDeposit()) {
						System.out.println("SI es posible");
						// AVISAR
						//Email - SMS
					} else {
						System.out.println("NO es posible");
					}
						

					hourlyCool.logout(); 				
				}
			}
		}
	}
	
	public void doPayHour() {
		PayHour payHour = new PayHour();
		if (payHour.isAlive()) {
			String user, pwd, modo;
			int total = Integer.parseInt(Configuracion.getProperty("payhour_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("payhour" + i + "_user");
				pwd = Configuracion.getProperty("payhour" + i + "_pwd");
				modo = Configuracion.getProperty("payhour" + i + "_modo");
				payHour.logout();  // ¿? 		
				if (payHour.login(user,pwd)) {

					// 1.- Comprobamos SALDO
					double accountAmount = payHour.getAmount();
					System.out.println("PAYHOUR " + user + ": Amount: " + accountAmount);

					// 2.- Sacamos SALDO según modo
					if (modo.equalsIgnoreCase("mantenimiento")) {
						System.out.println("Modo Mantenimiento");
						if (payHour.getActiveDeposit() == 0.0) {  // Se acabó el depósito anterior
							System.out.println("Se acabó deposito anterior");
							// Sacamos las ganancias si son > 0,50
							// Sino, reinvertimos todo.
							if (accountAmount - 3.0 >= 0.50) {
								System.out.println("Tenemos 0,50 o más de ganancia");
								if (payHour.makeInternalDeposit(3)) {
									accountAmount = payHour.getAmount();
									payHour.withdraw(accountAmount);								
								} 								
							} else {
								System.out.println("Tenemos menos de 0,50 de ganancia");
								payHour.makeInternalDeposit(accountAmount);
							}
						}
					} else { // Modo normal. Sacamos dinero.
						// Sólo sacamos cantidades enteras, para evitar comisiones de más, excepto si ya han vencido los depósitos, que sacamos lo que quede...
						if (payHour.getActiveDeposit() == 0.0) { // Sacamos todo
							if (accountAmount > 0.0) {
								payHour.withdraw(accountAmount);
							}
						} else {
							double withdrawAmount = new Double(accountAmount).intValue();
							// Sólo si es >1 nos interesa
							if (withdrawAmount >= 1) {
								payHour.withdraw(withdrawAmount);	
							}
						}
					}

					// 3.- Si es posible hacer otro depósito, AVISAR

					if (payHour.isPossibleDeposit()) {
						System.out.println("SI es posible");
						// AVISAR
						//Email - SMS
					} else {
						System.out.println("NO es posible");
					}
						

					payHour.logout(); 				
				}
			}
		}
	}

	public void doBitIncome() {
		BitIncome bitIncome = new BitIncome();
		if (bitIncome.isAlive()) {
			String user, pwd, modo;
			int total = Integer.parseInt(Configuracion.getProperty("bitincome_total"));
			for (int i=1; i <= total; i++) {
				user = Configuracion.getProperty("bitincome" + i + "_user");
				pwd = Configuracion.getProperty("bitincome" + i + "_pwd");
				modo = Configuracion.getProperty("bitincome" + i + "_modo");
				if (bitIncome.login(user,pwd)) {
				
					// 1.- Comprobamos SALDO
					double accountAmount = bitIncome.getAmount();
					System.out.println("BITINCOME " + user + ": Amount: " + accountAmount);

					// 2.- Sacamos SALDO según modo
					if (modo.equalsIgnoreCase("mantenimiento")) {
						if (bitIncome.getActiveDeposit() == 0.0) {  // Se acabó el depósito anterior
							// Reinvertimos y sacamos ganancias
							if (bitIncome.makeInternalDeposit(6)) {
								accountAmount = bitIncome.getAmount();
								bitIncome.withdraw(accountAmount);								
							} 
						}
					} else { // Modo normal. Sacamos dinero.
						// Sólo sacamos cantidades enteras, para evitar comisiones de más
						double withdrawAmount = new Double(accountAmount).intValue();
						// Sólo si es >1 nos interesa
						if (withdrawAmount >= 1) {
							bitIncome.withdraw(withdrawAmount);	
						}											
					}

					// 3.- Si es posible hacer otro depósito, AVISAR

					if (bitIncome.isPossibleDeposit()) {
						System.out.println("SI es posible");
						// AVISAR
						//Email - SMS
					} else {
						System.out.println("NO es posible");
					}
						
//					//dayeer.makeInternalDeposit(0.0);
					bitIncome.logout(); 				
				}
			}
		}
	}
	
}
