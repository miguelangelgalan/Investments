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
import com.gistlabs.mechanize.document.documentElements.AbstractDocumentElement;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.document.html.HtmlElements;
import com.gistlabs.mechanize.document.html.HtmlNode;
import com.gistlabs.mechanize.document.html.form.Form;
import com.gistlabs.mechanize.document.html.form.FormElement;
import com.gistlabs.mechanize.document.html.form.Forms;
import com.gistlabs.mechanize.document.html.form.RadioButton;
import com.gistlabs.mechanize.document.html.form.SubmitButton;
import com.gistlabs.mechanize.document.json.node.impl.AttributeNode;
import com.gistlabs.mechanize.document.link.Link;
import com.gistlabs.mechanize.document.link.Links;
import com.gistlabs.mechanize.document.node.Node;
import com.gistlabs.mechanize.impl.MechanizeAgent;
import com.gistlabs.mechanize.interfaces.document.Document;
import com.gistlabs.mechanize.parameters.Parameters;
import com.miguel.HYIP.helper.HTTPCLIENT;


public class Dayeer implements HYIPInterface {

	private static final String NAME="Dayeer";
	private String baseUrl = "https://dayeer.com";
	private String loginUrl = "/?a=cust&page=login";
	private String accountUrl = "/?a=account";
	private String logoutUrl = "/?a=logout";
	private String withdrawUrl = "/?a=withdraw";
	private String depositUrl = "/?a=deposit";
	private MechanizeAgent agent = null;
	private Logger log = Logger.getLogger("DAYEER");
	private HtmlDocument lastResponse; 

	public Dayeer() {
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
			log.info(this.NAME + " LOGIN " + user);
			if (isLogged()) {  // Si ya estamos logados, no seguimos
				return true;
			}
			
			HtmlDocument pg = this.getAgent().get(baseUrl + loginUrl);	
			Link l = pg.link("a[class=logologin]");
			HtmlElement he = pg.find("a[class=logologin]");
			
			
			Form loginForm = pg.form("mainform");
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
		if (isLogged()) {
			HtmlDocument pg = getAgent().get(baseUrl + logoutUrl);
			if (check_OK(pg)) {
				this.getAgent().cookies().removeAll();
				return true;
			}
		}
		return false;
	}
	
	public double getAmount() {
		String sAmount="0.0";
		if (isLogged()) {
			HtmlDocument pg = getAgent().get(baseUrl + accountUrl);
			if (check_OK(pg)) {
				HtmlElements hes = pg.htmlElements();
				List<HtmlElement> spans = hes.findAll("span[class]");
				for (HtmlElement e : spans) {
					String attr = e.getAttribute("class");
					if (attr.equalsIgnoreCase("accbtxt")) {
						String padre = e.getParent().getValue();
						if (padre.startsWith("Total Account Balance:")) {
							sAmount = e.getInnerHtml().replace('$', ' ');
							System.out.println(sAmount);							
						}
					}
				}
			} else {
				return 0.0;
			}
			
		} else {
			log.severe(this.NAME + " CANNOT GET AMOUNT. NOT LOGGED");
			return 0.0;	
		}
		return Double.parseDouble(sAmount);			
	}
	
	private boolean check_OK(HtmlDocument pagina) {
		if (pagina.getResponse().getStatusLine().getStatusCode() == 200) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean withdraw(double amount) {
		String sAmount="0.0";
		if (isLogged()) {
			HtmlDocument pg = getAgent().get(baseUrl + withdrawUrl);
			if (check_OK(pg)) {
				//Form withdrawForm = pg.form("withdraw");   // Noexiste
				Form withdrawForm = null;
				Forms forms = pg.forms();
				//Form wf = pg.form("form input[value=withdraw]");
				//Form wf = pg.form("form");
				if (forms.size() != 0) {
					System.out.println(forms.size());
					for (Form f : forms) { //Nos quedamos con el de withdraw, que es el que no tiene nombre, y campo oculto a=withdraw
						if (f.get("a") != null) {
							if (f.get("a").getValue().equalsIgnoreCase("withdraw")) {
							  withdrawForm = f;
							  break;
							}
						}						
					}
					if (withdrawForm != null)  {  // Lo tenemos

						
						// Establecemos el amount
						FormElement fAmount = withdrawForm.get("amount");
						String oldAmount = fAmount.getValue();
						fAmount.set(Double.toString(amount));		
						String newAmount = withdrawForm.get("amount").getValue();
						
						// NO FUNCIONA. VER ALTERNATIVA.
						//HtmlDocument res = withdrawForm.submit();

						// ALTERNATIVA:
						
						String uri = baseUrl + withdrawUrl;
						Parameters params = new Parameters();
						params.add("a","withdraw");
						params.add("action","preview");
						params.add("ec","43");
						params.add("amount", Double.toString(amount));
						params.add("comment", "");
						params.add("submit", "Request");
						HtmlDocument res = getAgent().post(uri, params);


						int returnCode = 0;
						if ((returnCode = res.getResponse().getStatusLine().getStatusCode()) == 200) {				
							Form confirmWithdrawForm = null;
							forms = res.forms();
							if (forms.size() != 0) {								
								for (Form f : forms) { //Nos quedamos con el de confirmación de withdraw, que es el que no tiene nombre, y campo oculto a=withdraw
									if (f.get("a") != null) {
										if (f.get("a").getValue().equalsIgnoreCase("withdraw")) {
										  confirmWithdrawForm = f;
										  break;
										}
									}						
								}
								if (confirmWithdrawForm != null)  {  // Lo tenemos
									log.info(this.NAME + " Realizando withdraw de: " + confirmWithdrawForm.get("amount").getValue() + " en: " + confirmWithdrawForm.get("ec").getValue());
									res = confirmWithdrawForm.submit();
									if (((returnCode = res.getResponse().getStatusLine().getStatusCode()) == 200) ||
											(returnCode = res.getResponse().getStatusLine().getStatusCode()) == 302){
										log.info(this.NAME + " WITHDRAW REALIZADO CORRECTAMENTE. Uri: " + res.getUri());
										return true;
									} else {
										log.severe(this.NAME + " WITHDRAW NO REALIZADO CORRECTAMENTE." + returnCode + " Uri: " + res.getUri());
										return false;
									}						
									
								}
							
							}
						} else {
							log.severe(this.NAME + " WITHDRAW NO REALIZADO CORRECTAMENTE." + returnCode + " Uri: " + res.getUri());
							return false;
						}				
						
						
						
						
						//log.info(this.NAME + " Realizando withdraw de: " + confirmDepositForm.get("amount").getValue() + " en: " + confirmDepositForm.get("type").getValue());
					} else { // No hemos encontrado el formulario de WITHDRAW
						log.severe(this.NAME + " WITHDRAW NO REALIZADO. NO SE ENCUENTRA EL FORMULARIO");
						return false;						
					}
						
						
						
//					FormElement username = loginForm.get("username");
//					username.setValue(user);
//					FormElement password = loginForm.get("password");
//					password.setValue(pass);
//
//					Resource res = loginForm.submit();
//					int a=0;
//					int returnCode = 0;
//					if ((returnCode = res.getResponse().getStatusLine().getStatusCode()) == 200) {				
//						log.info(this.NAME + " LOGIN OK. RC=" + returnCode);
//						setLastResponse((HtmlDocument)res);
//						return true;
//					} else {
//						log.severe(this.NAME + " LOGIN NOT OK. RC=" + returnCode);
//						return false;
//					}				
				} else {  // No se encuentra formulario de login
					log.severe(this.NAME + " WITHDRAW FORM NOT FOUND");
					return false;				
				}
				
				
				
				
			
			} else {
				return false;
			}
			
		} else {
			log.severe(this.NAME + " CANNOT WITHDRAW FUNDS. NOT LOGGED");
			return false;	
		}
		return true;	
	}

	public boolean makeInternalDeposit(double amount) {
		String sAmount="0.0";
		if (isLogged()) {
			HtmlDocument pg = getAgent().get(baseUrl + depositUrl);
			if (check_OK(pg)) {
				Form depositForm = pg.form("spendform");
				if (depositForm != null) {
					FormElement fAmount = depositForm.get("amount");
					String actualAmount = fAmount.getValue();
					fAmount.setValue(Double.toString(amount));
					actualAmount = fAmount.getValue();
					
					List<RadioButton> types = depositForm.getRadioButtons("type");
					for (RadioButton rb : types) {
						if (rb.getValue().equalsIgnoreCase("account_43")) {   // Checkeamos el correspondiente a "Spend funds from the Account Balance Payeer" => "account_43"
							rb.check();   																		// "Spend funds from PerfectMoney" => "process_18"
						}																						// "Spend funds from Payeer" => "process_43"
					}																							// "Spend funds from Bitcoin" => "process_48"
					
					
//					RadioButton type = depositForm.getRadioButton("type");
//					boolean b = type.isChecked();
//					String actualType = type.getValue();
//					type.check();
//					b = type.isChecked();
					
					HtmlDocument res = depositForm.submit();

					int returnCode = 0;
					if ((returnCode = res.getResponse().getStatusLine().getStatusCode()) == 200) {				
						Form confirmDepositForm = res.form("spend");
						if (confirmDepositForm != null) {
							// Chequeamos valores a enviar						
							log.info(this.NAME + " Realizando depósito de: " + confirmDepositForm.get("amount").getValue() + " en: " + confirmDepositForm.get("type").getValue());					
							res = confirmDepositForm.submit();
							if (((returnCode = res.getResponse().getStatusLine().getStatusCode()) == 200) ||
									(returnCode = res.getResponse().getStatusLine().getStatusCode()) == 302){
								log.info(this.NAME + " DEPOSITO REALIZADO CORRECTAMENTE.");
								return true;
							} else {
								log.severe(this.NAME + " DEPOSITO NO REALIZADO CORRECTAMENTE." + returnCode);
								return false;
							}						
						}
					} else {
						log.severe(this.NAME + " DEPOSITO NO REALIZADO CORRECTAMENTE." + returnCode);
						return false;
					}				
				} else {  // No se encuentra formulario de depósito
					log.severe(this.NAME + " DEPOSIT FORM NOT FOUND");
					return false;				
				}
				
				
			}
			
		} else {
			log.severe(this.NAME + " CANNOT WITHDRAW FUNDS. NOT LOGGED");
			return false;	
		}
		return true;	
	}	
}
