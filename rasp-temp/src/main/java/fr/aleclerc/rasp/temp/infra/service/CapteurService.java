package fr.aleclerc.rasp.temp.infra.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.aleclerc.rasp.temp.pojo.TempResponse;

public class CapteurService {

	private Path filePath;
	private static CapteurService instance;

	public CapteurService() {
		super();
		this.runW1();
		this.filePath = getDeviceFile();
	}

	public static CapteurService getInstance() {
		if (instance == null) {
			instance = new CapteurService();
		}
		return instance;
	}

	private void runW1() {
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(new String[] { "modprobe", "w1-gpio" });
			runtime.exec(new String[] { "modprobe", "w1-therm" });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Path getDeviceFile() {
		Path resultat = null;
		String baseDir = "/sys/bus/w1/devices/";
		DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
			@Override
			public boolean accept(Path file) throws IOException {
				return ((Files.isDirectory(file)) && file.getFileName()
						.toString().startsWith("28"));
			}
		};
		Path dir = FileSystems.getDefault().getPath(baseDir);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir,
				filter)) {
			for (Path path : stream) {
				resultat = path.resolve("w1_slave");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultat;
	}

	private String readFile(Path path) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(path.toFile()));
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				sb.append(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();

	}

	public float getTemperature() {
		float resultat = -1;
		String content = readFile(this.filePath);
		while (!content.contains("YES")) {
			continue;
		}
		int index = content.indexOf("t=");
		if (index != -1) {
			String stTemp = content.substring(index + 2);
			resultat = Float.valueOf(stTemp) / 1000;
		}
		return resultat;

	}

	public static TempResponse getMesure() {
		TempResponse reponse = new TempResponse();
		reponse.setTemperature(getInstance().getTemperature());
		reponse.setDate(new Date());
		reponse.setTime(new Date().getTime());
		return reponse;
	}

}
