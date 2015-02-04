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

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;

public class FlexoOAuthClient {

	private String login;
	private String password;
	private final FlexoOslcAdaptorConfiguration configuration;

	private final OAuthServiceProvider provider;
	private final OAuthConsumer consumer;
	private final OAuthAccessor accessor;

	public FlexoOAuthClient(FlexoOslcAdaptorConfiguration adaptorConfiguration, String login, String password) {
		super();
		this.configuration = adaptorConfiguration;
		this.login = login;
		this.password = password;
		this.provider = new OAuthServiceProvider(adaptorConfiguration.getRequestTokenUrl(), adaptorConfiguration.getAuthorizationUrl(),
				adaptorConfiguration.getAccessToken());
		this.consumer = new OAuthConsumer("", adaptorConfiguration.getConsumerKey(), adaptorConfiguration.getConsumerSecret(), provider);
		this.accessor = new OAuthAccessor(consumer);
	}

	public OAuthConsumer getConsumer() {
		return consumer;
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

	public String getConsumerKey() {
		return configuration.getConsumerKey();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthorizationUrl() {
		return configuration.getAuthorizationUrl();
	}

	public String getConsumerSecret() {
		return configuration.getConsumerSecret();
	}

	public String getAuthURL() {
		return configuration.getAuthURL();
	}
}
