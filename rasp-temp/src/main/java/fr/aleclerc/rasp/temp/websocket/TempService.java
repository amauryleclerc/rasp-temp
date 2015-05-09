package fr.aleclerc.rasp.temp.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.aleclerc.rasp.temp.pojo.TempResponse;

@ServerEndpoint(value = "/websocket/temp")
public class TempService {

	private static final Set<TempService> connections = new CopyOnWriteArraySet<>();
	private Session session;

	@OnOpen
	public void start(Session session) {
		this.session = session;
		connections.add(this);
		System.out.println("start");
	}

	public TempService() {
		System.out.println("websocket");
	}

	@OnClose
	public void end() {
		connections.remove(this);
		System.out.println("end");
	}

	@OnMessage
	public void incoming(String message) {
		System.out.println(message);
	}

	@OnError
	public void onError(Throwable t) throws Throwable {
		System.out.println("error");
	}

	public static void broadcast(TempResponse obj) {
		System.out.println("send");
//	
//		 Gson gson = new GsonBuilder()
//	     .registerTypeAdapter(Id.class, new IdTypeAdapter())
//	     .enableComplexMapKeySerialization()
//	     .serializeNulls()
//	     .setDateFormat(DateFormat.LONG)
//	     .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
//	     .setPrettyPrinting()
//	     .setVersion(1.0)
//	     .create();
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		for (TempService client : connections) {
			try {
				synchronized (client) {		
					client.session.getBasicRemote().sendText(json);
				}
			} catch (IOException e) {
				try {
					client.session.close();
				} catch (IOException e1) {
					// Ignore
				}

			}
		}
	}
}
