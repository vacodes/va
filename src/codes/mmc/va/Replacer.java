package codes.mmc.va;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
				lineIndex++;
				
			} else {
				String newValue = line.split(replaceChar)[1];
				String oldValue = line.split(replaceChar)[0];
				to_replace.put(oldValue, newValue);
			}
		}
		br.close();
	}
	
	private static void replaceValues(final File file) throws Exception {
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		
		final File tempOutFile = File.createTempFile("tmp-replacer", "txt");
		final PrintWriter writer = new PrintWriter(tempOutFile);
		
		boolean firstLine = true;
		
		String text = null;
		while((text = reader.readLine()) != null) {
			if(firstLine) { 
				firstLine = false; 
				continue;
			}
			
			for (final Entry<String, String> entry : to_replace.entrySet()) {
				text = text.replace(entry.getKey(), entry.getValue());
			}
			
			writer.println(text);
		}
		
		reader.close();
		writer.close();
		
		Files.move(tempOutFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	public static void main(final String[] args) {
		final boolean showStacktrace = true;
		final File file = new File("demo.txt");
		final File conf = new File("replace.txt");
		
		try {
			loadConfig(conf);
        	replaceValues(file);
		} catch (Exception e) {
			if (showStacktrace) { 
				e.printStackTrace();
			} else {
				System.err.println("Error occurred. Activate Stacktrace for more information");
			}
		}
    }

}
