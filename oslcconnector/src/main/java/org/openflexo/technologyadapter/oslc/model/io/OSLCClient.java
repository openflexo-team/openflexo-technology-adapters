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

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.apache.wink.client.ClientResponse;
import org.eclipse.lyo.oslc4j.client.OslcRestClient;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

public abstract class OSLCClient<T extends AbstractResource> {
	private static final Set<Class<?>> PROVIDERS = new HashSet<Class<?>>();

	private static URI CREATED_RESOURCE_URI;

	private final String baseURI;

	static {
		PROVIDERS.addAll(JenaProvidersRegistry.getProviders());
		PROVIDERS.addAll(Json4JProvidersRegistry.getProviders());
		PROVIDERS.add(ServiceProviderCatalog.class);
		PROVIDERS.add(ServiceProvider.class);
	}

	private final Class<T> resourceType;
	private final Class<T[]> resourceArrayType;

	private final ServiceProviderCatalog spc;
	private final ServiceProvider[] sps;

	@SuppressWarnings("unchecked")
	protected OSLCClient(Class<T> resourceType, String baseURI) {
		super();
		this.resourceType = resourceType;
		this.resourceArrayType = (Class<T[]>) Array.newInstance(resourceType, 0).getClass();
		this.baseURI = baseURI;
		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, baseURI + "/catalog", OslcMediaType.APPLICATION_RDF_XML);
		spc = oslcRestClient.getOslcResource(ServiceProviderCatalog.class);
		sps = oslcRestClient.getOslcResources(ServiceProvider[].class);
	}

	protected abstract T getResource();

	protected abstract String getResourceType();

	private static String getCreation(final String mediaType, final String type, ServiceProvider provider) {

		final Service[] services = provider.getServices();

		for (final Service service : services) {

			final CreationFactory[] creationFactories = service.getCreationFactories();

			for (final CreationFactory creationFactory : creationFactories) {
				final URI[] resourceTypes = creationFactory.getResourceTypes();

				for (final URI resourceType : resourceTypes) {
					if (resourceType.toString().equals(type)) {
						return creationFactory.getCreation().toString();
					}
				}
			}

		}

		return null;
	}

	private static String getQueryBase(final String mediaType, final String type, ServiceProvider provider) {

		final Service[] services = provider.getServices();

		for (final Service service : services) {

			final QueryCapability[] queryCapabilities = service.getQueryCapabilities();

			for (final QueryCapability queryCapability : queryCapabilities) {
				final URI[] resourceTypes = queryCapability.getResourceTypes();

				for (final URI resourceType : resourceTypes) {
					if (resourceType.toString().equals(type)) {
						return queryCapability.getQueryBase().toString();
					}
				}
			}
		}

		return null;
	}

	private static ResourceShape getResourceShape(final String mediaType, final String type, ServiceProvider provider) {
		final Service[] services = provider.getServices();

		for (final Service service : services) {

			final QueryCapability[] queryCapabilities = service.getQueryCapabilities();

			for (final QueryCapability queryCapability : queryCapabilities) {
				final URI[] resourceTypes = queryCapability.getResourceTypes();

				for (final URI resourceType : resourceTypes) {
					if (resourceType.toString().equals(type)) {
						final URI resourceShape = queryCapability.getResourceShape();

						if (resourceShape != null) {
							final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, resourceShape, mediaType);

							return oslcRestClient.getOslcResource(ResourceShape.class);
						}
					}
				}
			}
		}

		return null;
	}

	protected URI create(final String mediaType, ServiceProvider serviceProvider) throws URISyntaxException {

		final T resource = getResource();

		final String creation = getCreation(mediaType, getResourceType(), serviceProvider);

		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, creation, mediaType);

		final T addedResource = oslcRestClient.addOslcResource(resource);

		return addedResource.getAbout();
	}

	protected void delete(final String mediaType, ServiceProvider serviceProvider) {

		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, CREATED_RESOURCE_URI, mediaType);

		final ClientResponse clientResponse = oslcRestClient.removeOslcResourceReturnClientResponse();

		CREATED_RESOURCE_URI = null;
	}

	protected void retrieve(final String mediaType, ServiceProvider serviceProvider) throws URISyntaxException {

		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, CREATED_RESOURCE_URI, mediaType);

		final T resource = oslcRestClient.getOslcResource(resourceType);

	}

	protected void retrieves(final String mediaType, ServiceProvider serviceProvider) throws URISyntaxException {
		final String queryBase = getQueryBase(mediaType, getResourceType(), serviceProvider);
		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, queryBase, mediaType);

		final T[] resources = oslcRestClient.getOslcResources(resourceArrayType);

	}

	protected void update(final String mediaType, ServiceProvider serviceProvider) throws URISyntaxException {
		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, CREATED_RESOURCE_URI, mediaType);

		final T resource = oslcRestClient.getOslcResource(resourceType);

		final ClientResponse clientResponse = oslcRestClient.updateOslcResourceReturnClientResponse(resource);

		final T updatedResource = oslcRestClient.getOslcResource(resourceType);

	}

	private String getCatalogPath() {
		return baseURI + "/catalog";
	}

	private String getServicePath() {
		return baseURI + "/service";
	}

	public ServiceProvider[] getSps() {
		return sps;
	}

	public ServiceProviderCatalog getSpc() {
		return spc;
	}
}
