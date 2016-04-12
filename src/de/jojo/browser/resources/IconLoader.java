
package de.jojo.browser.resources;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * L&auml;dt Icons aus dem aktuellen package
 * @author Johannes Meesters
 */

public class IconLoader {

	/**
	 * 
	 * @param path - name/path of the requested icon
	 * @return the requested icon
	 */
	public static Icon loadIcon(String path) {
		return new ImageIcon(IconLoader.class.getResource(path));
	}
	
	public static Image iconToImage(Icon icon) {
		   if (icon instanceof ImageIcon) {
		      return ((ImageIcon)icon).getImage();
		   }else {
		      int w = icon.getIconWidth();
		      int h = icon.getIconHeight();
		      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		      GraphicsDevice gd = ge.getDefaultScreenDevice();
		      GraphicsConfiguration gc = gd.getDefaultConfiguration();
		      BufferedImage image = gc.createCompatibleImage(w, h);
		      Graphics2D g = image.createGraphics();
		      icon.paintIcon(null, g, 0, 0);
		      g.dispose();
		      return image;
		   }
	}
}