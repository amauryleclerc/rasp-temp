package fr.aleclerc.rasp.temp.infra.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class CapteurService {

	public CapteurService() {
		super();

		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(new String[] { "modprobe", "w1-gpio" });
			runtime.exec(new String[] { "modprobe", "w1-therm" });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Path path = getDeviceFile();
		readFile( path);
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
				resultat = path;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultat;
	}
	private String readFile(Path path){
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
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return sb.toString();
		
	}
	// base_dir = '/sys/bus/w1/devices/'
	// device_folder = glob.glob(base_dir + '28*')[0]
	// device_file = device_folder + '/w1_slave'
	//
	// def read_temp_raw():
	// f = open(device_file, 'r')
	// lines = f.readlines()
	// f.close()
	// return lines
	//
	// def read_temp():
	// lines = read_temp_raw()
	// while lines[0].strip()[-3:] != 'YES':
	// time.sleep(0.2)
	// lines = read_temp_raw()
	// equals_pos = lines[1].find('t=')
	// if equals_pos != -1:
	// temp_string = lines[1][equals_pos+2:]
	// temp_c = float(temp_string) / 1000.0
	// temp_f = temp_c * 9.0 / 5.0 + 32.0
	// return temp_c, temp_f
	//
	// while True:
	// print(read_temp())
	// time.sleep(1)
}
