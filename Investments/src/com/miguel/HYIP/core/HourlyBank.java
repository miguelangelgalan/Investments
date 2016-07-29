package com.miguel.HYIP.core;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.interfaces.document.Document;


public class HourlyBank implements HYIP {

	private String baseUrl = "https://hourlybank.biz";
	
	public boolean isAlive() {
		//mechanize  Buscar esta libreria
		//HttpsURLConnection hCon = new
		HttpHost proxy = new HttpHost("proxy.sir.renfe.es",8080);
		Credentials credentials = new UsernamePasswordCredentials("6836076","Junio006");
		AuthScope authScope = new AuthScope("proxy.sir.renfe.es", 8080);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(authScope, credentials);
		DefaultHttpClient dClient = new DefaultHttpClient();
		dClient.setProxyAuthenticationHandler(handler);
				
				
				(DefaultHttpClient) HttpClientBuilder.create().setProxy(proxy).setDefaultCredentialsProvider(credsProvider).build();
		HttpClient client = HttpClientBuilder.create().setProxy(proxy).setDefaultCredentialsProvider(credsProvider).build();
		try {
			HttpResponse response=client.execute(new HttpGet("http://stackoverflow.com/questions/6962047/apache-httpclient-4-1-proxy-authentication"));
			HttpResponse response2=dClient.execute(new HttpGet("http://stackoverflow.com/questions/6962047/apache-httpclient-4-1-proxy-authentication"));
			response.getLocale();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MechanizeAgent agent = new MechanizeAgent(dClient);
		Document page = agent.get(baseUrl);
		List l = page.links();
		return true;
	}
	
	public boolean login() {
		return true;
	}

	public boolean logout() {
		return true;
	}
	
	public double getAmount() {
		return 0.0;
	}
}
