package com.miguel.HYIP.Motor;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Demonio {

	public static void main(String[] args) {
		TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				printHour();
				HYIPManager manager = new HYIPManager();
				manager.doDayeer();
				//manager.doHourlyBank();
				manager.doFeers();
				manager.doHourlyCool();
			}
		};
		
		
		Timer timer = new Timer();
		//timer.scheduleAtFixedRate(timerTask, 0, 3600000);  // Cada Hora
		timer.scheduleAtFixedRate(timerTask, 0, 1200000);  // Cada 20 mins

	}

	public static void printHour() {
		Calendar calendario = Calendar.getInstance();
		int hora = calendario.get(Calendar.HOUR_OF_DAY);
		int minutos = calendario.get(Calendar.MINUTE);
		int  segundos = calendario.get(Calendar.SECOND);
		
		System.out.println("---------------------------------------------------------------------------------------");
		System.out.println("PROCESANDO AHORA: " + hora + ":" + minutos + ":" + segundos);		
	}
}
