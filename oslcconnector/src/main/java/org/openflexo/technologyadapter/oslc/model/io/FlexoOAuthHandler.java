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

import org.apache.http.auth.InvalidCredentialsException;
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
 * If this handler is set on a client, each http request will call the handle method which add Authorization automatically to the request
 * 
 * @author Vincent
 * 
 */
public abstract class FlexoOAuthHandler implements ClientHandler {

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
		if (login == null) {
			return configuration.getLogin();
		}
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
		if (password == null) {
			return configuration.getPassword();
		}
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
	public void performoAuthAuthentification(String uri) throws IOException, OAuthException, URISyntaxException,
			InvalidCredentialsException {
		// Create an oAuth client
		OAuthClient client = new OAuthClient(new HttpClient4());
		// Get the temporary Request Token from the provider given the consumer key and shared secret
		askForARequestToken(client);
		// Using the request token and the oauth-autorize, redirect the user to identification form
		userAuthentification(client);
		// Exchange a request Token for an Access Token
		exchangeRequestForAccessToken(client);
	}

	private void askForARequestToken(OAuthClient client) throws IOException, OAuthException, URISyntaxException {
		client.getRequestToken(getAccessor());
	}

	/**
	 * The user of the application might authentificates himself.
	 * 
	 * @param httpClient
	 */
	public abstract void userAuthentification(OAuthClient client);

	private void exchangeRequestForAccessToken(OAuthClient client) throws IOException, URISyntaxException, InvalidCredentialsException,
			OAuthException {
		// Exchange request token for access token.
		if (accessor.accessToken == null) {
			try {
				client.getAccessToken(accessor, OAuthMessage.POST, null);
			} catch (OAuthException e) {
				accessor.accessToken = null;
				accessor.requestToken = null;
			}
		}
	}

	// private String sessionID;

	/**
	 * This is called in the chain, here we add the authorization header to access the data using oAuth.
	 */
	@Override
	public ClientResponse handle(ClientRequest request, HandlerContext context) throws Exception {
		OAuthMessage message = getAccessor().newRequestMessage(request.getMethod(), request.getURI().toString(), null);
		request.getHeaders().add("Authorization", message.getAuthorizationHeader(""));
		// request.getHeaders().add("X-Jazz-CSRF-Prevent", sessionID);
		return context.doChain(request);
	}

}
