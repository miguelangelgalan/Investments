package com.miguel.HYIP.Motor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Demonio {

	public static void main(String[] args) {
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				printHour();
				HYIPManager manager = new HYIPManager();
				//// SCAM ----- manager.doHourlyBank(); ---- SCAM
				//// SCAM ----- manager.doBitIncome(); ---- SCAM
				//// SCAM ----- manager.doFeers();
				//// SCAM ----- manager.doDayeer();
				//// SCAM ----- manager.doPayHour();
				//manager.doDamas();
				
			//manager.doROIHour();
				
				manager.doHourlyOil();
				manager.doROIHour();
				manager.doHourlyCool();				
				manager.doHourlyGo();  

			}
		};
		
		
		Timer timer = new Timer();
		//timer.scheduleAtFixedRate(timerTask, 0, 3600000);  // Cada Hora
		//timer.scheduleAtFixedRate(timerTask, 0, 1200000);  // Cada 20 mins
		timer.scheduleAtFixedRate(timerTask, 0, 2100000);  // Cada 35 mins

	}

	public static void printHour() {
		Calendar calendario = Calendar.getInstance();
		//calendario.add(Calendar.HOUR_OF_DAY, -6);
		//calendario.add(arg0, arg1);
		int hora = calendario.get(Calendar.HOUR_OF_DAY);
		int minutos = calendario.get(Calendar.MINUTE);
		int  segundos = calendario.get(Calendar.SECOND);
		int mes = calendario.get(Calendar.MONTH);
		int dia = calendario.get(Calendar.DAY_OF_MONTH);

		Date d = calendario.getTime();
		String month = new SimpleDateFormat("MMM").format(d);
		String fecha = month + "-" + dia + "-" + calendario.get(Calendar.YEAR);
		System.out.println(fecha);
		
		
		//String buscar = d.toGMTString(); 
		
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.println("PROCESANDO AHORA: " + hora + ":" + minutos + ":" + segundos);		
	}
}
