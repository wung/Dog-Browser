package de.jojo.browser;

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
}
