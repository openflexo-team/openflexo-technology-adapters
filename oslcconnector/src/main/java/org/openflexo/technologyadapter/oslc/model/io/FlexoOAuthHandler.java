/**
 * 
 * Copyright (c) 2015, Openflexo
 * 
 * This file is part of Oslcconnector, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.oslc.model.io;

import java.io.IOException;
import java.net.URISyntaxException;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wink.client.ClientRequest;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.handlers.ClientHandler;
import org.apache.wink.client.handlers.HandlerContext;

/**
 * The oAuth handler consists to get a consumer key for Openflexo application from the OSLC provider First a temporary request token is
 * given by the server, given a consumer key and a shared secret. The the user of the application identifies himslef and authorizes an acces
 * to the protected resources thanks to the request token The request token is exchanged to an access token Now it is possible to access
 * protected resources thanks to this acces token
 * 
 * @author Vincent
 * 
 */
public class FlexoOAuthHandler implements ClientHandler {

	private String login;
	private String password;
	private final FlexoOslcAdaptorConfiguration configuration;

	private final OAuthServiceProvider provider;
	private final OAuthConsumer consumer;
	private final OAuthAccessor accessor;

	public FlexoOAuthHandler(FlexoOslcAdaptorConfiguration adaptorConfiguration) {
		super();
		this.configuration = adaptorConfiguration;
		this.provider = new OAuthServiceProvider(adaptorConfiguration.getRequestTokenUrl(), adaptorConfiguration.getAuthorizationUrl(),
				adaptorConfiguration.getAccessToken());
		this.consumer = new OAuthConsumer("", adaptorConfiguration.getConsumerKey(), adaptorConfiguration.getConsumerSecret(), provider);
		this.accessor = new OAuthAccessor(consumer);
	}

	/**
	 * This is the application that wants to connect to the server(Openflexo in our case) Notes the application might be known by the
	 * server, and key/secret might be defined between them
	 * 
	 * @return
	 */
	public OAuthConsumer getConsumer() {
		return consumer;
	}

	public String getConsumerKey() {
		return configuration.getConsumerKey();
	}

	public String getConsumerSecret() {
		return configuration.getConsumerSecret();
	}

	public OAuthAccessor getAccessor() {
		return accessor;
	}

	public String getAccessToken() {
		return configuration.getAccessToken();
	}

	public String getRequestTokenUrl() {
		return configuration.getRequestTokenUrl();
	}

	public String getAuthorizationUrl() {
		return configuration.getAuthorizationUrl();
	}

	public String getAuthURL() {
		return configuration.getAuthURL();
	}

	/**
	 * User login
	 * 
	 * @return
	 */
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * User password
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Perform the oAuth authentification given a set of parameters. <li>get a request token</li> <li>redirect exception to authorization
	 * URL</li> <li>exchange request token for access token</li> <li>access protected URL and return OAuthMessage</li>
	 * 
	 * @param oAuth
	 * @throws IOException
	 * @throws OAuthException
	 * @throws URISyntaxException
	 * @throws InvalidCredentialsException
	 */
	public void performoAuthAuthentification() throws IOException, OAuthException, URISyntaxException, InvalidCredentialsException {
		// Create an oAuth client
		OAuthClient client = new OAuthClient(new HttpClient4());
		// get the temporary Request Token from the provider given the consumer key and shared secret
		client.getRequestToken(getAccessor());

		// Get the redirection url
		// TODO do it automatically
		String redirect = getConsumer().serviceProvider.userAuthorizationURL + "?oauth_token=" + getAccessor().requestToken;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request1 = new HttpGet(redirect);
		HttpClientParams.setRedirecting(request1.getParams(), false);
		HttpResponse response = httpClient.execute(request1);
		// Get the location of the redirection
		Header location = response.getFirstHeader("Location");
		if (location != null) {
			HttpGet request2 = new HttpGet(location.getValue());
			HttpClientParams.setRedirecting(request2.getParams(), false);
			response = httpClient.execute(request2);
		}

		// The user might identificate himself
		userAuthentification(httpClient);

		// Exchange request token for access token.
		if (accessor.accessToken == null) {
			try {
				client.getAccessToken(accessor, OAuthMessage.POST, null);
			} catch (OAuthException e) {
				// restart
				accessor.accessToken = null;
				accessor.requestToken = null;
				performoAuthAuthentification();
			}
		}
	}

	/**
	 * The user of the application might authentificates himself.
	 * 
	 * @param httpClient
	 */
	private void userAuthentification(HttpClient httpClient) {
		// TODO Check authentification is OK and also how to give the username/password?
		if (getPassword() == null || getLogin() == null) {
			// TODO Ask for user authentification...
		}

		try {
			HttpPost formPost = new HttpPost(getAuthURL() + "?j_username=" + getLogin() + "&" + "j_password=" + getPassword());
			HttpResponse formResponse = httpClient.execute(formPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This is called in the chain, here we add the authorization header to access the data using oAuth.
	 */
	@Override
	public ClientResponse handle(ClientRequest request, HandlerContext context) throws Exception {
		OAuthMessage message = getAccessor().newRequestMessage(request.getMethod(), request.getURI().toString(), null);
		request.getHeaders().add("Authorization", message.getAuthorizationHeader(""));
		return context.doChain(request);
	}
}
