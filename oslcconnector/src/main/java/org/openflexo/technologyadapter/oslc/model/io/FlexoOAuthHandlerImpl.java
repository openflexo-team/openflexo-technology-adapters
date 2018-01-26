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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.oauth.client.OAuthClient;

/**
 * This Handler manage user authentifaction for some tools, ie IBM JAZZ
 * 
 * @author Vincent
 * 
 */
public class FlexoOAuthHandlerImpl extends FlexoOAuthHandler {

	public FlexoOAuthHandlerImpl(FlexoOslcAdaptorConfiguration adaptorConfiguration) {
		super(adaptorConfiguration);
	}

	/**
	 * The user of the application might authentificates himself.
	 * 
	 * @param httpClient
	 */
	@Override
	public void userAuthentification(OAuthClient client) {
		// TODO Check authentification is OK and also how to give the username/password?
		if (getPassword() == null || getLogin() == null) {
			// TODO Ask for user authentification...
		}

		try {
			// use the oauth-autorize url using the request token
			String redirect = getConsumer().serviceProvider.userAuthorizationURL + "?oauth_token=" + getAccessor().requestToken;
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request1 = new HttpGet(redirect);
			System.out.println("Request1: " + redirect);
			HttpClientParams.setRedirecting(request1.getParams(), false);
			HttpResponse response = httpClient.execute(request1);
			System.out.println("Response1: " + response);
			EntityUtils.consume(response.getEntity());
			// Get the location of the redirection
			Header location = response.getFirstHeader("Location");
			if (location != null) {
				HttpGet request2 = new HttpGet(location.getValue());
				System.out.println("Request2: " + location.getValue());
				HttpClientParams.setRedirecting(request2.getParams(), false);
				response = httpClient.execute(request2);
				System.out.println("Response2: " + response);
				EntityUtils.consume(response.getEntity());

				HttpPost formPost = new HttpPost(getAuthURL());
				System.out.println("Request3: " + getAuthURL());
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("j_username", getLogin()));
				nvps.add(new BasicNameValuePair("j_password", getPassword()));
				formPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));

				HttpResponse formResponse = httpClient.execute(formPost);
				System.out.println("Response3: " + formResponse);
				EntityUtils.consume(formResponse.getEntity());
				System.out.println(formResponse.getStatusLine().getStatusCode());

				location = formResponse.getFirstHeader("Location");
				// Third GET
				HttpGet request4 = new HttpGet(location.getValue());
				System.out.println("Request4: " + location.getValue());
				HttpClientParams.setRedirecting(request4.getParams(), false);
				response = httpClient.execute(request4);
				System.out.println("Response4: " + response.getAllHeaders());
				EntityUtils.consume(response.getEntity());

				/*for (Header header : response.getAllHeaders()) {
					for (HeaderElement element : header.getElements()) {
						if (element.getName().equals("JSESSIONID")) {
							sessionID = element.getValue();
						}
					}
				}*/

				location = response.getFirstHeader("Location");
				HttpGet request5 = new HttpGet(location.getValue());
				System.out.println("Request5: " + location.getValue());
				HttpClientParams.setRedirecting(request5.getParams(), false);
				response = httpClient.execute(request5);
				System.out.println("Response5: " + response);
				EntityUtils.consume(response.getEntity());
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
