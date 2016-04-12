package de.jojo.browser;

import java.util.Locale;

import org.apache.commons.validator.routines.DomainValidator;

public class Util {

	public static String generateUserAgent() {
		String userAgent = "Mozilla/5.0 ("
					+ System.getProperty("os.name") + "; "
					+ System.getProperty("os.arch") 
					+ ") AppleWebKit/538.19 (KHTML, like Gecko) " 
					+ Browser.WINDOW_TITLE + "/"
					+ Browser.VERSION + " Safari/538.19";
		
		// System.out.println("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/538.19 (KHTML, like Gecko) JavaFX/8.0 Safari/538.19"); // original
		// System.out.println(userAgent); // Dog Browser
		return userAgent;
	}
	
	public static String getHomepage() {
		String domain = "google.";
		String localDomain = domain += Locale.getDefault().getCountry().toLowerCase();
		
		if(DomainValidator.getInstance(false).isValid(localDomain)) return "https://www." + localDomain + "/";
		else return "https://www." + domain + "/";
	}
}
