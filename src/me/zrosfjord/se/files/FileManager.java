package me.zrosfjord.se.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FileManager {
	
	private HashMap<String, ConfigFile> configMap;
	
	public FileManager() {
		configMap = new HashMap<String, ConfigFile>();
	}
	
	/**
	 * Adds a ConfigFile to the configMap after insuring it is valid.
	 * 
	 * @param name	the name of the config file.
	 */
	public void addConfigFile(String name) {
		ConfigFile cfg = new ConfigFile(name);
		if(cfg.loadContents())
			configMap.put(name, cfg);
	}
	
	/**
	 * Gets a config file from the configMap with the associated String
	 * 
	 * @param name	the String name of the file
	 * @return		the ConfigFile object or null if it doesn't exist in the map.
	 */
	public ConfigFile getConfigFile(String name) {
		if(configMap.containsKey(name)) {
			return configMap.get(name);
		} else {
			return null;
		}
	}
	
	public class ConfigFile {
		
		private String name;
		private HashMap<String, String> contentsMap;
		
		private ConfigFile(String name) {
			this.name = name;
			this.contentsMap = new HashMap<String, String>();
		}
		
		public boolean loadContents() {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name + ".txt");
			if(is == null)
				return false;
			
			BufferedReader rscReader = null;
			
			try {
				rscReader = new BufferedReader(new InputStreamReader(is));
				String nextLine;
				
				while((nextLine = rscReader.readLine()) != null) {
					if(nextLine.startsWith("%"))	// '%' is the indicator for a note line
						continue;
					if(!nextLine.contains("#"))		// '#' defines the sides of the set
						continue;
					
					String[] set = nextLine.split("#");
					
					if(set.length >= 2)
						contentsMap.put(set[0].trim(), set[1].trim());
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(rscReader != null)
					try {
						rscReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				else
					return false;
			}
			
			return true;
		}
		
		public String getStrValue(String key) {
			if(contentsMap.containsKey(key))
				return contentsMap.get(key);
			else
				return null;
		}
		
		public int getIntValue(String key) {
			if(contentsMap.containsKey(key))
				return Integer.parseInt(contentsMap.get(key));
			else
				return 0;
		}
		
		public float getFloatValue(String key) {
			if(contentsMap.containsKey(key))
				return Float.parseFloat(contentsMap.get(key));
			else
				return 0.0f;
		}
		
		public boolean getBoolValue(String key) {
			if(contentsMap.containsKey(key))
				return Boolean.parseBoolean(contentsMap.get(key));
			else
				return false;
		}
	}
	
}
