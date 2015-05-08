package fr.aleclerc.rasp.temp.webservice;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.research.ws.wadl.Response;

import fr.aleclerc.rasp.temp.infra.service.CapteurService;
import fr.aleclerc.rasp.temp.pojo.TempResponse;

@Path("temp")
public class TempRessource {

	private CapteurService capteur;
	
	@PostConstruct
	public void init(){
		this.capteur = CapteurService.getInstance();
	}

	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getTemperture(){
		return ""+capteur.getTemperature();
		
	}
	@GET
	@Path("json")
	@Produces(MediaType.APPLICATION_JSON)
	public TempResponse getTemperatureJSON(){
		
		TempResponse reponse = new TempResponse();
		reponse.setTemperature(capteur.getTemperature());
		reponse.setDate(new Date());
		return reponse;
		
	}
}
