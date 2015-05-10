package fr.aleclerc.rasp.temp.webservice;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import redis.clients.jedis.Jedis;
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
	public String getTemperature(){
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
	@GET
	@Path("mesures")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TempResponse> getMesures(){

		List<TempResponse> resultat = new ArrayList<TempResponse>();
		Jedis jedis = new Jedis("localhost");
		Set<String> keys = jedis.keys("*");
		for (String key : keys) {
			TempResponse response = new TempResponse();
			String value = jedis.get(key);
			response.setTime(Long.valueOf(key));
			response.setTemperature(Float.valueOf(value));
			Date date = new Date();
			date.setTime(response.getTime());
			response.setDate(date);
			resultat.add(response);
		}
		jedis.close();
		return resultat;
	}
}
