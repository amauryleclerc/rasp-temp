package fr.aleclerc.rasp.temp.pojo;

import java.util.Date;

public class TempResponse {
	private float temperature;
	private Date date;
	public TempResponse() {
		
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
