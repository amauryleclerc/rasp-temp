package fr.aleclerc.rasp.temp;

import fr.aleclerc.rasp.temp.infra.service.CapteurService;

public class Launcher {

	public static void main(String[] args) {
		CapteurService service = new CapteurService();
		float temp = service.getTemperature();
		System.out.println("température de "+temp);
	}

}
