package de.jojo.browser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

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
	
	static class HTMLDialog {

		public static void showDialog(Component parent, String title, String text, Icon icon) {
			JEditorPane editorPane = new JEditorPane();
			editorPane.setEditable(false);
			editorPane.setBorder(new LineBorder(Color.WHITE, 0));
			editorPane.setEditorKit(new HTMLEditorKit());
			editorPane.addHyperlinkListener(new HyperlinkListener() {
				
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						try {
							Desktop desktop = Desktop.getDesktop();
							desktop.browse(e.getURL().toURI());
						}catch(Exception exc) {
							exc.printStackTrace();
						}
					}
				}
			});
			
			editorPane.setText("<!DOCTYPE html><html><head><style>a {text-decoration:none;}</style></head><body bgcolor=\"F0F0F0\">" + text + "</body></html>");
			
			JOptionPane.showMessageDialog(parent, editorPane, title, JOptionPane.INFORMATION_MESSAGE, icon);
		}
	}
}
