package de.jojo.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.UrlValidator;

import de.jojo.browser.resources.IconLoader;

public class Browser extends JFrame implements WindowListener, ChangeListener {
	
	public static void main(String[] args) {
		new Browser();
	}
	
	public static final Icon IC_LOADING = IconLoader.loadIcon("ic_loading.gif");
	public static final Icon IC_READY = IconLoader.loadIcon("ic_ready.png");
	public static final Icon IC_ERROR = IconLoader.loadIcon("ic_error.png");

	public static final int STATUS_ERROR = 0;
	public static final int STATUS_LOADING = 1;
	public static final int STATUS_READY = 2;
	
	public static final UrlValidator URL_VALIDATOR = new UrlValidator(new String[] {"http", "https", "file:///"});
	public static final DomainValidator DOMAIN_VALIDATOR = DomainValidator.getInstance(true);

	private static final long serialVersionUID = -5576403718075078341L;
	public static final String WINDOW_TITLE = "Dog Browser";
	public static final String VERSION = "1.0b";

	private MenuBar menuBar;
	
	private JTabbedPane tabbedPane;
	private LinkedList<Tab> tabs;
	
	private JLabel status;
	
	public Browser() {
		setTitle(WINDOW_TITLE);
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(IconLoader.iconToImage(IconLoader.loadIcon("icon.png")));
		
		try { // set LAF to system default - otherwise it will look ugly af
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
		}
		
		addWindowListener(this);
		
		menuBar = new MenuBar(this);
		
		tabs = new LinkedList<Tab>();
		tabs.add(new Tab(this, null, 0));
		
		tabbedPane = new JTabbedPane();
		tabbedPane.add(tabs.getFirst().getTabComponent().getTitle(), tabs.getFirst());
		tabbedPane.setTabComponentAt(0, tabs.getFirst().getTabComponent());
		tabbedPane.addChangeListener(this);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setFont(new Font(tabbedPane.getFont().getName(), Font.BOLD, tabbedPane.getFont().getSize()));
				
		status = new JLabel("Willkommen bei " + WINDOW_TITLE + "!");
		
		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);
		add(status, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	public void selectTab(int index) {
		if(index > -2 && index < tabbedPane.getTabCount()) {
			SwingUtilities.invokeLater(() -> {
				tabbedPane.setSelectedIndex(index);
			});
		}
	}
	
	public void load(String url) {
		if(tabbedPane.getSelectedIndex() != -1) {
			tabs.get(tabbedPane.getSelectedIndex()).load(url);
		}
	}
	
	public void reload() {
		if(tabbedPane.getSelectedIndex() != -1) {
			tabs.get(tabbedPane.getSelectedIndex()).reload();
		}
	}
	
	public void updateUI() {
		if(tabbedPane.getSelectedIndex() != -1) {
			Tab selected = tabs.get(tabbedPane.getSelectedIndex());
			
			SwingUtilities.invokeLater(() -> {
				for(int i = 0; i < tabbedPane.getTabCount(); i++) {
					tabs.get(i).getTabComponent().setIndex(i);
				}
				
				menuBar.updateUI(selected);
				switch (selected.getStatus()) {
					case STATUS_LOADING:
						setStatus(selected.getStatusText(), Browser.IC_LOADING);
						break;
					case STATUS_READY:
						setStatus(selected.getHover().isEmpty() ? selected.getStatusText() : selected.getStatusText() + " - " + selected.getHover(), Browser.IC_READY);
						setTitle(selected.getTabComponent().getTitle() + " - " + Browser.WINDOW_TITLE);
						break;
					case STATUS_ERROR:
						setStatus(selected.getStatusText(), Browser.IC_ERROR);
						break;
					default:
						break;
				}
			});
		}
	}
	
	public void newTab(String url) {
		SwingUtilities.invokeLater(() -> {
			Tab newTab = new Tab(this, url, tabbedPane.getTabCount());
			tabs.add(newTab);
			tabbedPane.addTab(newTab.getTabComponent().getTitle(), newTab);
			tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, newTab.getTabComponent());
			tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
			
			updateUI();
		});
	}
	
	public void closeTab(int index) {
		if(index > -1 && index < tabbedPane.getTabCount()) {
			if(tabbedPane.getTabCount() > 1) {
				SwingUtilities.invokeLater(() -> {
					tabbedPane.removeTabAt(index);
					tabs.remove(index);
				});
			}else {
				tabs.get(index).load(null);
			}
			
			// updateUI(); // TODO updatet er sich durch den change listener nicht schon?
		}
	}
	
	public void closeTab() {
		closeTab(tabbedPane.getSelectedIndex());
	}
	
	public void goInHistory(int offset) {
		if(tabbedPane.getSelectedIndex() != -1) {
			tabs.get(tabbedPane.getSelectedIndex()).goInHistory(offset);
		}
	}
	
	public void hover() {
		if(tabbedPane.getSelectedIndex() != -1) {
			Tab selected = tabs.get(tabbedPane.getSelectedIndex());
			if(selected.getStatus() == STATUS_READY) {
				setStatus(selected.getHover().isEmpty() ? selected.getStatusText() : selected.getStatusText() + " - " + selected.getHover(), Browser.IC_READY);
			}
		}
	}
	
	private void setStatus(String newStatus, Icon icon) {
		SwingUtilities.invokeLater(() -> {
			status.setText(newStatus);
			status.setIcon(icon);
		});
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		updateUI();
	}
	
	public MenuBar getMenu() {
		return menuBar;
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		for(Tab tab : tabs) tab.cancel();
	}

	@Override
	public void windowActivated(WindowEvent e) { }
	@Override
	public void windowClosed(WindowEvent e) { }
	@Override
	public void windowDeactivated(WindowEvent e) { }
	@Override
	public void windowDeiconified(WindowEvent e) { }
	@Override
	public void windowIconified(WindowEvent e) { }
	@Override
	public void windowOpened(WindowEvent e) { }
}
