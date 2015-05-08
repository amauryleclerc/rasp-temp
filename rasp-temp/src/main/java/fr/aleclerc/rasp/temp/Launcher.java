package fr.aleclerc.rasp.temp;

import fr.aleclerc.rasp.temp.infra.service.CapteurService;

public class Launcher {

	public static void main(String[] args) {
		System.out.println("lancement");
		CapteurService service = new CapteurService();
		service.getTemperature();
		System.out.println("arret");
	}

}
