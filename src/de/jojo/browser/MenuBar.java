package de.jojo.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import de.jojo.browser.resources.IconLoader;
import de.jojo.browser.resources.Strings;

public class MenuBar extends JPanel {

	private static final long serialVersionUID = 464032375755709500L;

	private Browser browser;
	
	private JTextField addressBar;
	
	private JButton home;
	private JButton historyBack;
	private JButton historyForward;
	
	private JButton menu;
	private Menu overflowMenu;
	
	public MenuBar(Browser browser) {
		this.browser = browser;
		setLayout(new BorderLayout());
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		home = new JButton(IconLoader.loadIcon("ic_home.png"));
		home.setToolTipText(Strings.get("menu.homepage"));
		home.setBorderPainted(false);
		home.setContentAreaFilled(false);
		home.setFocusable(false);
		home.setMargin(new Insets(0, 0, 0, 0));
		home.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				browser.load(Util.getHomepage());
			}
		});
		
		historyBack = new JButton(IconLoader.loadIcon("ic_history_backwards.png"));
		historyBack.setToolTipText(Strings.get("menu.backward"));
		historyBack.setEnabled(false);
		historyBack.setBorderPainted(false);
		historyBack.setContentAreaFilled(false);
		historyBack.setFocusable(false);
		historyBack.setMargin(new Insets(0, 0, 0, 0));
		historyBack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				browser.goInHistory(-1);
			}
		});
		
		historyForward = new JButton(IconLoader.loadIcon("ic_history_forwards.png"));
		historyForward.setToolTipText(Strings.get("menu.forward"));
		historyForward.setEnabled(false);
		historyForward.setBorderPainted(false);
		historyForward.setContentAreaFilled(false);
		historyForward.setFocusable(false);
		historyForward.setMargin(new Insets(0, 0, 0, 0));
		historyForward.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				browser.goInHistory(1);
			}
		});
		
		buttonPanel.add(home);
		buttonPanel.add(historyBack);
		buttonPanel.add(historyForward);
		
		addressBar = new JTextField();
		addressBar.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				SwingUtilities.invokeLater(() -> {
					if(Browser.DOMAIN_VALIDATOR.isValid(addressBar.getText()) || Browser.URL_VALIDATOR.isValid(addressBar.getText()) || Browser.URL_VALIDATOR.isValid("http://" + addressBar.getText())) {
						addressBar.setForeground(Color.BLACK);
					}else {
						addressBar.setForeground(Color.RED);
					}
				});
				
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					browser.load(addressBar.getText());
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) { }
			
			@Override
			public void keyPressed(KeyEvent e) { }
		});
		
		addressBar.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				addressBar.selectAll();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) { }
			@Override
			public void mousePressed(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) { }
			@Override
			public void mouseEntered(MouseEvent e) { }
		});
		
		overflowMenu = new Menu();
		menu = new JButton(Strings.get("menu.title"));
		menu.setContentAreaFilled(false);
		menu.setBorderPainted(true);
		menu.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					overflowMenu.show(e.getComponent(), menu.getWidth() - (int) overflowMenu.getPreferredSize().getWidth(), menu.getHeight() + 2);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		
		add(buttonPanel, BorderLayout.WEST);
		add(addressBar, BorderLayout.CENTER);
		add(menu, BorderLayout.EAST);
	}
	
	public void updateUI(BrowserTab selected) {
		SwingUtilities.invokeLater(() -> {
			addressBar.setText(selected.getUrl());
			historyBack.setEnabled(selected.canGoBack());
			historyForward.setEnabled(selected.canGoForward());
			
			if(selected.getUrl().startsWith("https")) addressBar.setForeground(new Color(0, 128, 0));
			else addressBar.setForeground(Color.BLACK);
		});
	}
	
	private class Menu extends JPopupMenu implements ActionListener {

		private static final long serialVersionUID = 1837885908758406482L;
		
		private JMenuItem newTab = new JMenuItem(Strings.get("menu.new_tab"), IconLoader.loadIcon("ic_new_tab.png"));
		private JMenuItem closeTab = new JMenuItem(Strings.get("menu.close_tab"), IconLoader.loadIcon("ic_close_tab.png"));
		private JMenuItem reload = new JMenuItem(Strings.get("menu.refresh"), IconLoader.loadIcon("ic_reload.png"));
		private JMenuItem about = new JMenuItem(Strings.get("menu.about"), IconLoader.loadIcon("ic_info.png"));
		
		public Menu(){	
			add(newTab);
			newTab.addActionListener(this);
			newTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
			
			add(closeTab);
			closeTab.addActionListener(this);
			closeTab.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK));
			
			add(reload);
			reload.addActionListener(this);
			reload.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
			
			add(about);
			about.addActionListener(this);
			about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
			
			setPreferredSize(new Dimension(200, 25 * 4)); // 3 = count of components
		}
		
		@Override
		public void show(Component invoker, int x, int y) {			
			super.show(invoker, x, y);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(newTab)) {
				browser.newTab(null);
			}else if(e.getSource().equals(closeTab)) {
				browser.closeTab();
			}else if(e.getSource().equals(reload)) {
				browser.reload();
			}else if(e.getSource().equals(about)) {
				String infoText = Browser.WINDOW_TITLE + " v" + Browser.VERSION 
						+ "<br>&copy; <a href=\"https://github.com/SlendermanDE/Dog-Browser\">Johannes M.</a> 2016"
						+ "<br><br><b>" + Strings.get("about.libaries") + ":</b><ul>"
						+ "<li><a href=\"http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html\">Oracle JavaFX</a></li>"
						+ "<li><a href=\"http://p.yusukekamiyamane.com/\">Fugue Icons</a></li></ul>";
				
				Util.HTMLDialog.showDialog(getParent(), Strings.get("menu.about"), infoText, IconLoader.loadIcon("icon.png"));
			}
		}
		
	}
}