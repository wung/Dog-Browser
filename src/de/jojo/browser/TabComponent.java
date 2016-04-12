package de.jojo.browser;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.jojo.browser.resources.IconLoader;
import de.jojo.browser.resources.Strings;

public class TabComponent extends JComponent implements MouseListener {

	private static final long serialVersionUID = -6919825579133704788L;
	
	private JLabel title;
	private JButton close;
	
	private Browser browser;
	private int index = -1;
	
	public TabComponent(Browser browser, String titleString, int index) {
		this.browser = browser;
		this.index = index;
		
		title = new JLabel(titleString);
		title.setToolTipText(titleString);
		
		close = new JButton(IconLoader.loadIcon("ic_close_tab_small.png"));
		close.setContentAreaFilled(false);
		close.setBorderPainted(false);
		close.setFocusPainted(false);
		close.setOpaque(true);
		close.setMargin(new Insets(0, 0, 0, 0));
		close.setBackground(new Color(240, 240, 240));
		close.setToolTipText(Strings.get("menu.close_tab"));
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				browser.closeTab(index);
			}
		});
		
		addMouseListener(this);
		title.addMouseListener(this);
		/*addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					browser.selectTab(index);
					System.out.println("select");
				}else if(e.getButton() == MouseEvent.BUTTON2) {
					browser.closeTab(index);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		});*/
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(title);
		add(close);
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTitle() {
		return title.getText();
	}

	public void setTitle(String titleString) {
		SwingUtilities.invokeLater(() -> {
			title.setText(titleString);
			title.setToolTipText(titleString);
		});
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			browser.selectTab(index);
		}else if(e.getButton() == MouseEvent.BUTTON2) {
			browser.closeTab(index);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
}
