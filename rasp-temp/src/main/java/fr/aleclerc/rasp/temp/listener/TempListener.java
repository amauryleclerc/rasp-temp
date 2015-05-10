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
		try {
			while (true) {
				
				TempResponse obj = CapteurService.getMesure();
				TempService.broadcast(obj);
				Thread.sleep(5000);
//				Jedis jedis = new Jedis("localhost");
				jedis.set(obj.getTime().toString(), String.valueOf(obj.getTemperature()));
				jedis.expire(obj.getTime().toString(), 600);
			}
		} catch (InterruptedException e) {
		}

	}
}
