package fr.aleclerc.rasp.temp.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import redis.clients.jedis.Jedis;
import fr.aleclerc.rasp.temp.infra.service.CapteurService;
import fr.aleclerc.rasp.temp.pojo.TempResponse;
import fr.aleclerc.rasp.temp.websocket.TempService;

public class TempListener implements ServletContextListener, Runnable {

	private Thread t;
	private Jedis jedis;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		t.interrupt();
		jedis.close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		jedis = new Jedis("localhost");
		jedis.flushAll();
		t = new Thread(this);
		t.start();

	}

	@Override
	public void run() {
		int i = 0;
		float moyMinute = 0;
		int nbValMinute =0;
		float moy15Minute =0;
		int nbVal15Minute =0;
		float moyHeure = 0;
		int nbValHeure =0;
		try {
			while (true) {
				i++;
				nbValMinute++;
				TempResponse obj = CapteurService.getMesure();
				TempService.broadcast(obj);
				moyMinute = (moyMinute*(nbValMinute-1) + obj.getTemperature())/nbValMinute;
		
				
				if(0 ==(i % 360) ){
					nbValHeure++;
					nbVal15Minute++;
					moy15Minute = (moyMinute*(nbVal15Minute-1) + moyMinute)/nbVal15Minute;
					moyMinute = 0 ;
					nbValMinute = 0;
					moyHeure = (moyHeure*(nbValHeure-1) + moy15Minute)/nbValHeure;
					moy15Minute = 0 ;
					nbVal15Minute = 0;
					jedis.set(obj.getTime().toString(), String.valueOf(moyHeure));
					jedis.expire(obj.getTime().toString(), 604800);
					i=0;
					moyHeure = 0 ;
					nbValHeure = 0;
				}
				else if(0 ==(i % 90) ){
					nbValHeure++;
					jedis.set(obj.getTime().toString(), String.valueOf(moy15Minute));
					jedis.expire(obj.getTime().toString(), 86400);
					moyHeure = (moyHeure*(nbValHeure-1) + moy15Minute)/nbValHeure;
					moy15Minute = 0 ;
					nbVal15Minute = 0;
				}else	if(0 ==(i % 6) ){
					nbVal15Minute++;
					jedis.set(obj.getTime().toString(), String.valueOf(moyMinute));
					jedis.expire(obj.getTime().toString(), 3600);
					moy15Minute = (moyMinute*(nbVal15Minute-1) + moyMinute)/nbVal15Minute;
					moyMinute = 0 ;
					nbValMinute = 0;
				}
			
				Thread.sleep(10000);
			
			}
		} catch (InterruptedException e) {
		}

	}
}
