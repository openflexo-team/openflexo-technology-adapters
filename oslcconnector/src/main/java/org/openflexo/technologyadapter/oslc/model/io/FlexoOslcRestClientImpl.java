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
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthException;

import org.apache.http.auth.InvalidCredentialsException;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.ClientWebException;
import org.apache.wink.client.Resource;
import org.eclipse.lyo.client.oslc.resources.Requirement;
import org.eclipse.lyo.client.oslc.resources.RequirementCollection;
import org.eclipse.lyo.oslc4j.client.OslcRestClient;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

/**
 * 
 * This is the REST client to Access OSLC resources. It uses Apache Wink OSLC Rest Client. It manage potential oAuth through
 * FlexoOAuthHandler
 * 
 * @author Vincent
 * 
 */
public class FlexoOslcRestClientImpl implements FlexoOslcRestClient {

	// Providers converts server data into a specific format requested by the client
	private static Set<Class<?>> PROVIDERS = new HashSet<Class<?>>();

	/**
	 * Common providers for OSLC
	 */
	static {
		PROVIDERS.addAll(JenaProvidersRegistry.getProviders());
		PROVIDERS.addAll(Json4JProvidersRegistry.getProviders());
		PROVIDERS.add(ServiceProviderCatalog.class);
		PROVIDERS.add(ServiceProvider.class);
		PROVIDERS.add(Service.class);
		PROVIDERS.add(CreationFactory.class);
		PROVIDERS.add(QueryCapability.class);
		PROVIDERS.add(Requirement.class);
		PROVIDERS.add(RequirementCollection.class);
	}

	private static final Logger logger = Logger.getLogger(FlexoOslcRestClientImpl.class.getPackage().getName());

	private final static String DEFAULT_MEDIA_TYPE = "application/" + OslcMediaType.RDF_XML;

	private OslcRestClient oslcRestClient;

	public FlexoOslcRestClientImpl(String uri, String mediaType, FlexoOslcAdaptorConfiguration adaptorConfiguration, Set<Class<?>> PROVIDERS) {
		// Create the client
		if (mediaType == null) {
			oslcRestClient = new OslcRestClient(PROVIDERS, uri, DEFAULT_MEDIA_TYPE);
		}
		else {
			oslcRestClient = new OslcRestClient(PROVIDERS, uri, mediaType);
		}
		try {
			ClientResponse response = oslcRestClient.getClientResource().accept(oslcRestClient.getMediaType()).get();
			if (HttpServletResponse.SC_UNAUTHORIZED == response.getStatusCode()) {
				try {
					FlexoOAuthHandler oAuth = new FlexoOAuthHandlerImpl(adaptorConfiguration);
					oAuth.performoAuthAuthentification(oslcRestClient.getUri());
					oslcRestClient = new OslcRestClient(PROVIDERS, oslcRestClient.getUri(), oslcRestClient.getMediaType(),
							oslcRestClient.getReadTimeout(), oAuth);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidCredentialsException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (OAuthException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (ClientWebException e) {

		} catch (Exception e) {
			System.out.println("Connection Error " + e.getMessage());
		}
	}

	public Resource getClientResource() {
		return oslcRestClient.getClientResource();
	}

	public OslcRestClient getOslcRestClient() {
		return oslcRestClient;
	}

	@Override
	public <T> String addOslcResource(T oslcResource) {
		try {
			return oslcRestClient.addOslcResourceReturnClientResponse(oslcResource).getEntity(String.class);
		} catch (ClientWebException e) {
			logger.warning(e.getResponse().getEntity(String.class));
			return null;
		}
	}

	@Override
	public ClientResponse removeOslcResourceReturnClientResponse() {
		return oslcRestClient.removeOslcResourceReturnClientResponse();
	}

	@Override
	public <T> T getOslcResource(final Class<T> oslcResourceClass) {
		return oslcRestClient.getOslcResource(oslcResourceClass);
	}

	@Override
	public <T> T[] getOslcResources(final Class<T[]> oslcResourceArrayClass) {
		return oslcRestClient.getOslcResources(oslcResourceArrayClass);
	}

	@Override
	public <T> ClientResponse removeOslcResourceReturnClientResponse(T resource) {
		// TODO Auto-generated method stub
		return oslcRestClient.removeOslcResourceReturnClientResponse();
	}

	@Override
	public ClientResponse updateOslcResourceReturnClientResponse(final Object oslcResource) {
		return oslcRestClient.updateOslcResourceReturnClientResponse(oslcResource);
	}

	@Override
	public ClientResponse getOslcResource() {
		return getClientResource().get();
	}

}
