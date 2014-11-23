package codes.mmc.va;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Replacer {
	
	
	private static final Map<String, String> to_replace = new HashMap<>();
	private static final List<String>        rows       = new ArrayList<String>();

	
	private static void loadConfig(File configFile) throws Exception {
		FileReader reader = new FileReader(configFile);
		BufferedReader br = new BufferedReader(reader);
		
		String line = null;
		String replaceChar = null;
		
		int lineIndex = 0;
		while ((line = br.readLine()) != null) {
			
			if (lineIndex < 2) {
				
				if (lineIndex == 1) { 
					replaceChar = line; 
				}
				lineIndex ++;
				
			} else {
				String newValue = line.split(replaceChar)[1];
				String oldValue = line.split(replaceChar)[0];
				to_replace.put(oldValue, newValue);
			}
		}
		br.close();
	}
	
	private static void readFile(File file) throws Exception {
		FileReader reader = new FileReader(file);
		BufferedReader br = new BufferedReader(reader);
		
		String line = null;
		
		boolean first = true;
		while ((line = br.readLine()) != null) {
			if (!first) {
				rows.add(line);
			}
			first = false;
		}
		br.close();
	}
	
	public static void saveFile(File fileName) throws Exception {
		PrintWriter pw = new PrintWriter(fileName);
		for (String s : rows) {
			pw.write(s + "\r\n");
		}
		pw.close();
	}
	
	private static void replaceValues() {
		for (int i = 0; i < rows.size(); i++) {
			String text = rows.get(i);
			
			for (String key : to_replace.keySet()) {
				text = text.replace(key, to_replace.get(key));
			}
			rows.set(i, text);
		}
	}
	
	
	public static void main(String[] args) {
		final boolean showStacktrace = true;
		final File file = new File("demo.txt");
		final File conf = new File("replace.txt");
		
		try {
			loadConfig(conf);
			
			readFile(file);
        	replaceValues();
        	saveFile(file);
	        	
		} catch (Exception e) {
			
			if (showStacktrace) { e.printStackTrace();
			
			} else {
				System.err.println("Error occurred. Activate Stacktrace for more information");
			}
		}
    }

}
