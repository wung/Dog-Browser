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
		home.setToolTipText("Startseite");
		home.setBorderPainted(false);
		home.setContentAreaFilled(false);
		home.setFocusable(false);
		home.setMargin(new Insets(0, 0, 0, 0));
		home.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				browser.load("http://google.de");
			}
		});
		
		historyBack = new JButton(IconLoader.loadIcon("ic_history_backwards.png"));
		historyBack.setToolTipText("Eine Seite zurück");
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
		historyForward.setToolTipText("Eine Seite vor");
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
		menu = new JButton("Menü");
		menu.setContentAreaFilled(false);
		menu.setBorderPainted(false);
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
	
	public void updateUI(Tab selected) {
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
		
		private JMenuItem newTab = new JMenuItem("Neuer Tab", IconLoader.loadIcon("ic_new_tab.png"));
		private JMenuItem closeTab = new JMenuItem("Tab schließen", IconLoader.loadIcon("ic_close_tab.png"));
		private JMenuItem reload = new JMenuItem("Neu laden", IconLoader.loadIcon("ic_reload.png"));
		
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
			
			setPreferredSize(new Dimension(200, 25 * 3)); // 3 = Anzahl Componenten
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
			}
		}
		
	}
}