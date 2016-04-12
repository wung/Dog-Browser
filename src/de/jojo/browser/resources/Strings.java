package de.jojo.browser.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;

/**
 * loads strings for a given locale for the use in the UI
 * @author Johannes
 *
 */
public class Strings {

	public static final Locale[] VALID_LOCALES = new Locale[] {Locale.US, Locale.GERMANY};
	public static String CURRENT_LOCALE = Locale.getDefault().toString();
	
	private static HashMap<String, String> strings = new HashMap<String, String>();
	
	public static void loadStrings(String locale) {
		try {			
			boolean validLocale = false;
			for(Locale valid : VALID_LOCALES) {
				if(valid.toString().equals(locale)) {
					validLocale = true;
					break;
				}
			}
			if(!validLocale) locale = VALID_LOCALES[0].toString();
			CURRENT_LOCALE = locale;
						
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(Strings.class.getClassLoader().getResourceAsStream(
							"de/jojo/browser/resources/" + locale + ".lang"), "UTF-8"));
			
			
			String line;
			while((line = reader.readLine()) != null) {
				String[] temp = line.split("=");
				strings.put(temp[0], temp[1]);
			}
			
			reader.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String get(String key) {
		String result = strings.get(key);
		
		return result == null ? key : result;
	}
}
