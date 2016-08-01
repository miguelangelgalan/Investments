package com.miguel.HYIP.core;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.cookie.Cookie;
import com.gistlabs.mechanize.cookie.Cookies;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.form.Form;
import com.gistlabs.mechanize.document.html.form.FormElement;
import com.gistlabs.mechanize.document.html.form.Forms;
import com.gistlabs.mechanize.document.link.Link;
import com.gistlabs.mechanize.document.link.Links;
import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.interfaces.document.Document;
import com.miguel.HYIP.helper.HTTPCLIENT;


public class HourlyBank implements HYIPInterface {

	private static final String NAME="Hourlybank";
	private String baseUrl = "https://hourlybank.biz";
	private MechanizeAgent agent = null;
	private Logger log = Logger.getLogger("HOURLYBANK");
	private HtmlDocument lastResponse; 

	public HourlyBank() {
		super();
		agent = new MechanizeAgent(HTTPCLIENT.getClient());
	}
	
	private MechanizeAgent getAgent() {
		return agent;
	}
	
	public HtmlDocument getLastResponse() {
		return lastResponse;
	}

	public void setLastResponse(HtmlDocument lastResponse) {
		this.lastResponse = lastResponse;
	}
	public boolean isAlive() {
		
		try {
			HtmlDocument pg = this.getAgent().get(baseUrl);
			setLastResponse(pg);
			int returnCode = 0;
			if ((returnCode = pg.getResponse().getStatusLine().getStatusCode()) == 200) {
				log.info(baseUrl + " is Alive. RC=" + returnCode);
				return true;
			} else {
				log.severe(baseUrl + " is NOT Alive. RC=" + returnCode);
				return false;
			}
			
		} catch (Exception e) {
			log.severe(baseUrl + " isAlive() - EXCEPCIÓN: " + e.getMessage() + " - " + e.getCause());
			return false;
		}
		
		//HttpResponse hr = pg.getResponse();
		//hr.get
		//List l = page.links();
		//Forms f = pg.forms();
		//return true;
	}
	
	public boolean isLogged() {
		Cookies cs = this.getAgent().cookies();
		List<Cookie> listaCookies = cs.getAll();
		for (Cookie ck : listaCookies) {
			if (ck.getName().equalsIgnoreCase("password")) {
				return true;
			}
		}
		return false;
	}


	public boolean login(String user, String pass) {
		try {
			
			if (isLogged()) {  // Si ya estamos logados, no seguimos
				return true;
			}
			
			HtmlDocument pg = this.getAgent().get(baseUrl);	
			Form loginForm = pg.form("loginform");
			if (loginForm != null) {
				FormElement username = loginForm.get("username");
				username.setValue(user);
				FormElement password = loginForm.get("password");
				password.setValue(pass);

				Resource res = loginForm.submit();
				int a=0;
				int returnCode = 0;
				if ((returnCode = res.getResponse().getStatusLine().getStatusCode()) == 200) {				
					log.info(this.NAME + " LOGIN OK. RC=" + returnCode);
					setLastResponse((HtmlDocument)res);
					return true;
				} else {
					log.severe(this.NAME + " LOGIN NOT OK. RC=" + returnCode);
					return false;
				}				
			} else {  // No se encuentra formulario de login
				log.severe(this.NAME + " LOGIN FORM NOT FOUND");
				return false;				
			}
		} catch (Exception e) {
			log.severe(baseUrl + " login() - EXCEPCIÓN: " + e.getMessage() + " - " + e.getCause());
			return false;			
		}
		
	}

	public boolean logout() {
		return true;
	}
	
	public double getAmount() {
		if (isLogged()) {
			HtmlDocument pg = getLastResponse();
			Links ls = pg.links();
			for (Link l : ls) {
				String hr = l.href();
				String ts = l.toString();
			}
			Link l = pg.link("Account");
			
			Form loginForm = pg.form("loginform");
			if (loginForm != null) {
				return 0.0;
			}
			
		} else {
			log.severe(this.NAME + " CANNOT GET AMOUNT. NOT LOGGED");
			return 0.0;	
		}
		return 0.0;	
		
	}
}
