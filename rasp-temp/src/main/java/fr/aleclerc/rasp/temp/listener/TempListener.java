package fr.aleclerc.rasp.temp.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import fr.aleclerc.rasp.temp.infra.service.CapteurService;
import fr.aleclerc.rasp.temp.pojo.TempResponse;
import fr.aleclerc.rasp.temp.websocket.TempService;

public class TempListener implements ServletContextListener, Runnable {

	private Thread t;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		t.interrupt();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
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
			}
		} catch (InterruptedException e) {
		}

	}
}
