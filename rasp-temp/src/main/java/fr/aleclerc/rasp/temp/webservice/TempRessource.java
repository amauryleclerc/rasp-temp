package fr.aleclerc.rasp.temp.webservice;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.aleclerc.rasp.temp.infra.service.CapteurService;

@Singleton
@Path("temp")
public class TempRessource {

	private CapteurService capteur;
	
	@PostConstruct
	public void init(){
		this.capteur = new CapteurService();
	}

	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getTemperture(){
		return ""+capteur.getTemperature();
		
	}
}
