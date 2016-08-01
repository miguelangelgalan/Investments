package com.miguel.HYIP.helper;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

public class HTTPCLIENT {

	private static DefaultHttpClient httpClient = null;
	
	public static DefaultHttpClient getClient() {
		if (httpClient == null) {
			
			String proxy_host = Configuracion.getProperty("proxy_host");
			if ((proxy_host.equalsIgnoreCase("")) || (proxy_host == null)) {  // No hay proxy
				httpClient = new DefaultHttpClient();
			} else { // Hay proxy
				HttpHost proxy = new HttpHost(proxy_host,8080);
				String proxy_user = Configuracion.getProperty("proxy_user");
				String proxy_pwd = Configuracion.getProperty("proxy_pwd");
				
				httpClient = new DefaultHttpClient();
				httpClient.getCredentialsProvider().setCredentials(
					    new AuthScope(proxy_host, 8080),
						new UsernamePasswordCredentials(proxy_user,proxy_pwd));
				httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);				
			}
			
		}
		
		return httpClient;
		
	}
}
