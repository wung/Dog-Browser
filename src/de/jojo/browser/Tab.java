package de.jojo.browser;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import de.jojo.browser.resources.Strings;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

public class Tab extends JPanel implements ChangeListener<State>, EventHandler<WebErrorEvent> {

	private static final long serialVersionUID = 70457514861141162L;
	private Browser browser;
	
	private JFXPanel jfxPanel;
	private WebView webView;
	
	private TabComponent tabComponent;
	private String statusText = Strings.get("ui.loading");
	private String hover = "";
	private String url = "";
	private int status = Browser.STATUS_LOADING;
	
	public Tab(Browser browser, String url, int index) {
		this.browser = browser;
		setLayout(new BorderLayout());
		
		jfxPanel = new JFXPanel();
		
		Platform.runLater(() -> { // Creation of scene and future interactions with JFXPanel should take place on the JavaFX Application Thread
			webView = new WebView();
			webView.getEngine().setJavaScriptEnabled(true);
			webView.setContextMenuEnabled(false);
			webView.getEngine().setUserAgent(Util.generateUserAgent());
			webView.getEngine().getLoadWorker().progressProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					statusText = webView.getEngine().getLoadWorker().getMessage() + " " + Math.round((double) observable.getValue() * 100F) + "%";
					
					browser.updateUI();
				}
			});
			webView.getEngine().setOnStatusChanged(new EventHandler<WebEvent<String>>() {

				@Override
				public void handle(WebEvent<String> event) {
					if(event.getData() != null && !event.getData().isEmpty()) hover = event.getData();
					else hover = "";
					
					browser.hover();
				}
			});
			/*webView.getEngine().getLoadWorker().messageProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					System.out.println("[MESSAGES] " + observable.getValue());
				}
			});*/
			webView.getEngine().getLoadWorker().exceptionProperty().addListener(new ChangeListener<Throwable>() {

				@Override
				public void changed(ObservableValue<? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
					System.err.println(observable.toString());
					status = Browser.STATUS_ERROR;
					statusText = observable.getValue().getMessage();
					
					browser.updateUI();
				}

			});
		    webView.getEngine().getLoadWorker().stateProperty().addListener(this);
		    webView.getEngine().setOnError(this);
		    
		    jfxPanel.setScene(new Scene(webView));
		});
		
		tabComponent = new TabComponent(browser, Strings.get("ui.loading"), index);
		
		add(jfxPanel, BorderLayout.CENTER);
		load(url);
	}

	public void load(String url) {
		Platform.runLater(() -> { // Creation of scene and future interactions with JFXPanel should take place on the JavaFX Application Thread
			if(url != null) {				
				if(Browser.URL_VALIDATOR.isValid(url)) {
					webView.getEngine().load(url);
				}else if(Browser.URL_VALIDATOR.isValid("http://" + url)) {
					webView.getEngine().load("http://" + url);
				}else if(Browser.DOMAIN_VALIDATOR.isValid(url)) {
					webView.getEngine().load("http://" + url);
				}
			}else {
				webView.getEngine().load(Util.getHomepage());
			}
		});
	}
	
	public void reload() {
		Platform.runLater(() -> { // Creation of scene and future interactions with JFXPanel should take place on the JavaFX Application Thread
			webView.getEngine().reload();
		});
	}
	
	public void cancel() {
		Platform.runLater(() -> {
			webView.getEngine().getLoadWorker().cancel();
		});
	}

	@Override
	public void handle(WebErrorEvent event) {
		status = Browser.STATUS_ERROR;
		statusText = event.getMessage();
		browser.updateUI();
	}
	
	public boolean canGoBack() {
		// System.out.println(webView.getEngine().getHistory().getCurrentIndex() + "/" + webView.getEngine().getHistory().getEntries().size());
		
		return webView.getEngine().getHistory().getCurrentIndex() > 0;
	}
	
	public boolean canGoForward() {
		// System.out.println(webView.getEngine().getHistory().getCurrentIndex() + "/" + webView.getEngine().getHistory().getEntries().size());
		
		return webView.getEngine().getHistory().getEntries().size() > webView.getEngine().getHistory().getCurrentIndex() + 1;
	}
	
	public void goInHistory(int offset) {
		Platform.runLater(() -> {
			if(offset == -1 && canGoBack()) {
				webView.getEngine().getHistory().go(offset);
			}else if(offset == 1 && canGoForward()) {
				webView.getEngine().getHistory().go(offset);
			}else {
				System.out.println("hmm");
			}
		});
	}

	@Override
	public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
		switch (newValue) {
		case RUNNING:
			tabComponent.setTitle("laden...");
			status = Browser.STATUS_LOADING;
			url = webView.getEngine().getLocation();
			
			browser.updateUI();
			break;
		case SUCCEEDED:
			status = Browser.STATUS_READY;
			tabComponent.setTitle(webView.getEngine().getTitle());
			
			browser.updateUI();
			break;
		case FAILED:
			status = Browser.STATUS_ERROR;
			
			browser.updateUI();
			break;
		default:
			break;
		}
	}
	
	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	
	public String getHover() {
		return hover;
	}

	public void setHover(String hover) {
		this.hover = hover;
	}

	public TabComponent getTabComponent() {
		return tabComponent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
