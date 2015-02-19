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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.ClientWebException;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.eclipse.lyo.oslc4j.core.model.Service;

/**
 * FlexoOslcClient provides HighLevel services to manipulate OSLC resource. It creates and uses an OslcRestClient to access OSLC resource.
 * FlexoOslcClient provides basic services : <li>{@link #create(CreationFactory factory, T resource) create(CreationFactory factory, T
 * resource)}</li> <li>{@link #delete(T resource) delete(T resource)}</li> <li>{@link #retrieve(String resourceUri) retrieve(String
 * resourceUri)}</li> <li>
 * {@link #update(T resource) update(T resource)}</li>
 * 
 * @author Vincent
 * 
 */
public class FlexoOslcClient {

	private static final Logger logger = Logger.getLogger(FlexoOslcClient.class.getPackage().getName());

	// Extra Providers to convert server data into a specific format requested by the client.
	private static Set<Class<?>> PROVIDERS;

	private String mediaType;
	private final FlexoOslcAdaptorConfiguration adaptorConfiguration;

	public FlexoOslcClient(FlexoOslcAdaptorConfiguration adaptorConfiguration) {
		this.adaptorConfiguration = adaptorConfiguration;
	}

	public FlexoOslcClient(FlexoOslcAdaptorConfiguration adaptorConfiguration, String mediaType) {
		this.mediaType = mediaType;
		this.adaptorConfiguration = adaptorConfiguration;
	}

	/**
	 * Use this client to set other providers.
	 * 
	 * @param adaptorConfiguration
	 * @param providers
	 * @param mediaType
	 */
	public FlexoOslcClient(FlexoOslcAdaptorConfiguration adaptorConfiguration, Set<Class<?>> providers, String mediaType) {
		PROVIDERS.addAll(providers);
		this.mediaType = mediaType;
		this.adaptorConfiguration = adaptorConfiguration;
	}

	/**
	 * Create a resource using a creation factory
	 * 
	 * @see <a href="http://open-services.net/bin/view/Main/OslcCoreSpecification#Creation_Factories">Creation Factories</a>
	 * 
	 * @param factory
	 * @param resource
	 * @return The created OSLC resource.
	 * @throws URISyntaxException
	 */
	public <T extends AbstractResource> Object create(CreationFactory factory, T resource, String mediaType) throws URISyntaxException {
		final FlexoOslcRestClient oslcRestClient = createNewFlexoOslcClient(factory.getCreation().toString(), mediaType);
		return oslcRestClient.addOslcResource(resource);
	}

	/**
	 * Delete a resource
	 * 
	 * @param resource
	 */
	public <T extends AbstractResource> void delete(T resource) {
		final FlexoOslcRestClient oslcRestClient = createNewFlexoOslcClient(resource.getAbout().toString(), mediaType);
		oslcRestClient.removeOslcResourceReturnClientResponse();
	}

	/**
	 * Delete a resource using its uri
	 * 
	 * @param mediaType
	 * @param uri
	 */
	public void delete(String resourceUri) {
		final FlexoOslcRestClient oslcRestClient = createNewFlexoOslcClient(resourceUri, null);
		oslcRestClient.removeOslcResourceReturnClientResponse();
	}

	/**
	 * Retrieve an OSLC resource given an URI
	 * 
	 * @param mediaType
	 * @param resourceUri
	 * @return
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractResource> T retrieve(String resourceUri) throws URISyntaxException {
		final FlexoOslcRestClient oslcRestClient = createNewFlexoOslcClient(resourceUri, null);
		return (T) getClientResource(oslcRestClient, null);
	}

	/**
	 * Retrieve an OSLC resource given an URI
	 * 
	 * @param mediaType
	 * @param resourceUri
	 * @return
	 * @throws URISyntaxException
	 */
	/*@SuppressWarnings("unchecked")
	public <T extends AbstractResource> T retrieve(QueryCapability queryCapability, String resourceUri) throws URISyntaxException {
		return (T) getResourceFromClient(resourceUri, null, null);
	}*/

	/**
	 * Retrieve an OSLC resource given an URI and a media type
	 * 
	 * @param mediaType
	 * @param resourceUri
	 * @return
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractResource> T retrieve(String resourceUri, final String mediaType) throws URISyntaxException {
		final FlexoOslcRestClient oslcRestClient = createNewFlexoOslcClient(resourceUri, mediaType);
		return (T) getClientResource(oslcRestClient, null);
	}

	/**
	 * Retrieve an OSLC resource given an URI and a resource type
	 * 
	 * @param mediaType
	 * @param resourceUri
	 * @return
	 * @throws URISyntaxException
	 */
	public <T extends AbstractResource> T retrieve(String resourceUri, final Class<T> resourceClass) throws URISyntaxException {
		final FlexoOslcRestClient oslcRestClient = createNewFlexoOslcClient(resourceUri, null);
		return getClientResource(oslcRestClient, resourceClass);
	}

	/**
	 * Retrieve an OSLC resource given an URI and a type of resource
	 * 
	 * @param mediaType
	 * @param resourceUri
	 * @return
	 * @throws URISyntaxException
	 */
	public <T extends AbstractResource> T retrieve(String resourceUri, final Class<T> resourceClass, final String mediaType)
			throws URISyntaxException {
		final FlexoOslcRestClient oslcRestClient = createNewFlexoOslcClient(resourceUri, mediaType);
		return getClientResource(oslcRestClient, resourceClass);
	}

	/**
	 * Retrieve a set of OSLC resource of a given type
	 * 
	 * @param mediaType
	 * @param serviceProvider
	 * @param resourceClass
	 * @throws URISyntaxException
	 */
	public <T extends AbstractResource> T[] retrieves(String resourceUri, final Class<T[]> oslcResourceArrayClass)
			throws URISyntaxException {
		final FlexoOslcRestClient oslcRestClient = createNewFlexoOslcClient(resourceUri, null);
		return getResources(oslcRestClient, oslcResourceArrayClass);
	}

	/*public <T> T[] retrieves(final String mediaType, Service service, String type, final Class<T[]> oslcResourceArrayClass)
			throws URISyntaxException {
		final String queryBase = getFirstQueryCapability(type, service).getAbout().toString();
		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, queryBase, mediaType);
		final T[] resources = oslcRestClient.getOslcResources(oslcResourceArrayClass);
		return resources;
	}*/

	public <T extends AbstractResource> T update(T resource) throws URISyntaxException {
		final FlexoOslcRestClient oslcRestClient = createNewFlexoOslcClient(resource.getAbout().toString(), null);
		oslcRestClient.updateOslcResourceReturnClientResponse(resource);
		return retrieve(resource.getAbout().toString());
	}

	public CreationFactory getFirstCreationFactory(final String type, Service service) {
		final CreationFactory[] creationFactory = getCreationFactories(type, service);
		if (creationFactory.length > 0) {
			return creationFactory[0];
		}
		else {
			logger.warning("No creation factory was found for type " + type + " and Service " + service.getAbout());
			return null;
		}
	}

	/**
	 * Get the creation factories from a service that corresponds to a particular type
	 * 
	 * @param type
	 * @param service
	 * @return a CreationFactory
	 */
	public CreationFactory[] getCreationFactories(final String type, Service service) {
		List<CreationFactory> correctCreationFactories = new ArrayList<CreationFactory>();
		final CreationFactory[] creationFactories = service.getCreationFactories();
		for (final CreationFactory creationFactory : creationFactories) {
			final URI[] resourceTypes = creationFactory.getResourceTypes();
			for (final URI resourceType : resourceTypes) {
				if (resourceType.toString().equals(type)) {
					correctCreationFactories.add(creationFactory);
				}
			}
		}
		return correctCreationFactories.toArray(new CreationFactory[correctCreationFactories.size()]);
	}

	public QueryCapability getFirstQueryCapability(final String type, Service service) {
		final QueryCapability[] queryCapabilities = getQueryCapabilities(type, service);
		if (queryCapabilities.length > 0) {
			return queryCapabilities[0];
		}
		else {
			logger.warning("No query capability was found for type " + type + " and Service " + service.getAbout());
			return null;
		}
	}

	/**
	 * Get the QueryCapabilities from a service that corresponds to a particular type
	 * 
	 * @param type
	 * @param service
	 * @return a QueryCapability
	 */
	public QueryCapability[] getQueryCapabilities(final String type, Service service) {
		List<QueryCapability> correctQueryCapabilities = new ArrayList<QueryCapability>();
		final QueryCapability[] queryCapabilities = service.getQueryCapabilities();
		for (final QueryCapability queryCapability : queryCapabilities) {
			final URI[] resourceTypes = queryCapability.getResourceTypes();
			for (final URI resourceType : resourceTypes) {
				if (resourceType.toString().equals(type)) {
					correctQueryCapabilities.add(queryCapability);
				}
			}
		}
		return correctQueryCapabilities.toArray(new QueryCapability[correctQueryCapabilities.size()]);
	}

	/**
	 * Detect the resource shape from Service provider
	 * 
	 * @param mediaType
	 * @param type
	 * @param provider
	 * @return ResourceShape
	 * @throws URISyntaxException
	 */
	public ResourceShape getResourceShape(QueryCapability queryCapability, final String mediaType, final String type)
			throws URISyntaxException {

		final URI[] resourceTypes = queryCapability.getResourceTypes();
		for (final URI resourceType : resourceTypes) {
			if (resourceType.toString().equals(type)) {
				final URI resourceShape = queryCapability.getResourceShape();
				if (resourceShape != null) {
					return retrieve(resourceShape.toString(), ResourceShape.class, null);
				}
			}
		}
		return null;
	}

	/**
	 * Detect the resource shape from Service provider
	 * 
	 * @param mediaType
	 * @param type
	 * @param provider
	 * @return ResourceShape
	 * @throws URISyntaxException
	 */
	public ResourceShape getResourceShape(CreationFactory creationFactory, final String mediaType, final String type)
			throws URISyntaxException {
		final URI[] resourceTypes = creationFactory.getResourceTypes();
		for (final URI resourceType : resourceTypes) {
			if (resourceType.toString().equals(type)) {
				for (URI resourceShape : creationFactory.getResourceShapes()) {
					if (resourceShape != null) {
						return retrieve(resourceShape.toString(), ResourceShape.class, null);
					}
				}
			}
		}
		return null;
	}

	private <T extends AbstractResource> T[] retrievesSingleKindOfElements(String uri, Class<T> resourceClasses) {
		try {
			return retrieves(uri, (Class<T[]>) Array.newInstance(resourceClasses, 0).getClass());
		} catch (URISyntaxException e) {
			logger.warning("URI " + uri + " is not correct");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the resource using an OslcRestClient. If the resource class is not given, then it will be determinated using the providers.
	 * 
	 * @param client
	 * @param resourceClass
	 * @return an OSLC resource
	 */
	@SuppressWarnings("unchecked")
	private <T extends AbstractResource> T getClientResource(FlexoOslcRestClient client, final Class<T> resourceClass) {
		if (resourceClass == null) {
			return (T) getResource(client);
		}
		else {
			return getResource(client, resourceClass);
		}
	}

	/**
	 * Get the OSLC resource, retrieve the kind of resource given the set of providers.
	 * 
	 * @param client
	 * @return an OSLC resource
	 */
	@SuppressWarnings("unchecked")
	private <T extends AbstractResource> T getResource(FlexoOslcRestClient client) {
		T result = null;
		try {
			// Try to get the resource
			ClientResponse reponse = client.getOslcResource();

			// Find the type of this resource given the providers
			for (Class<?> p : PROVIDERS) {
				try {
					if (reponse.getEntity(p) != null) {
						return (T) reponse.getEntity(p);
					}
				} catch (RuntimeException e) {
					// Skip this provider
				}
			}
			// Choose the String one
			return convertStringToAbstractResource(reponse.getEntity(String.class));
		} catch (ClientWebException e) {

		} catch (Exception e) {
			System.out.println("Connection Error " + e.getMessage());
		}
		return result;
	}

	private <T extends AbstractResource> T convertStringToAbstractResource(String source) {
		// TODO
		return null;
	}

	/**
	 * Get an OSLC Resource given a resource class
	 * 
	 * @param client
	 * @param resourceClass
	 * @return an OSLC resource
	 */
	private <T extends AbstractResource> T getResource(FlexoOslcRestClient client, final Class<T> resourceClass) {
		T result = null;
		try {
			result = client.getOslcResource(resourceClass);
		} catch (Exception e) {
			System.out.println("Connection Error " + e.getMessage());
		}
		return result;
	}

	/**
	 * Get an OSLC Resource given a resource class
	 * 
	 * @param client
	 * @param resourceClass
	 * @return an OSLC resource
	 */
	private <T extends AbstractResource> T[] getResources(FlexoOslcRestClient client, final Class<T[]> resourceClass) {
		T[] result = null;
		try {
			result = client.getOslcResources(resourceClass);
		} catch (Exception e) {
			System.out.println("Connection Error " + e.getMessage());
		}
		return result;
	}

	private FlexoOslcRestClient createNewFlexoOslcClient(String resourceUri, String mediaType) {
		// TODO If certificate is not authetificated, then automatically trust it. This should be remove later!!
		trustIt();
		return new FlexoOslcRestClientImpl(resourceUri, mediaType, adaptorConfiguration, PROVIDERS);
	}

	/**
	 * This should be removed
	 */
	public static void trustIt() {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				/** Ignore Method Call */
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				/** Ignore Method Call */
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };

		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*private SecureRandom secureRandom;
	private KeyStore serverKeyStore;
	private KeyStore clientKeyStore;
	private SSLContext sslContext;
	private String passphrase;*/

	/*
	 *  initiate a secure socket connection to a remote server
	 */
	/*private void SSLConnextion(String host, int port) {
		//Create a SecureRandom, a source of secure random numbers. Secure random numbers are numbers 
		//that are random enough that they will not make the encryption vulnerable to attack
		secureRandom = new SecureRandom();
	    secureRandom.nextInt();

	    setupServerKeystore();
	    setupClientKeyStore();
	    
	    //Create a TrustManagerFactory from the remote server's KeyStore. This is used to authenticate the remote server.
	    TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
	    tmf.init( serverKeyStore );
	    //Create a KeyManagerFactory from the server's KeyStore. This is used for encrypting and decrypting data.
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
	    kmf.init( clientKeyStore, passphrase.toCharArray() );
	    
	    //Create an SSLContext object, using the KeyManagerFactory, the TrustManagerFactory, and the SecureRandom.
	    sslContext = SSLContext.getInstance( "TLS" );
	    sslContext.init( kmf.getKeyManagers(),
	                       tmf.getTrustManagers(),
	                       secureRandom );
	    
	    // Create a secured connection
	    SSLSocketFactory sf = sslContext.getSocketFactory();
	    SSLSocket socket = (SSLSocket)sf.createSocket( host, port );
	}
	
	//Create a KeyStore object containing the client's public/private key pair, including its public key certificate. This is read from client.private.
	private void setupServerKeystore()
	        throws GeneralSecurityException, IOException {
	      serverKeyStore = KeyStore.getInstance( "JKS" );
	      serverKeyStore.load( new FileInputStream( "server.public" ),
	                          passphrase.toCharArray() );
	}
	
	//Create a KeyStore object containing the remote server's public key. This is read from server.public.
	private void setupClientKeyStore()
	        throws GeneralSecurityException, IOException {
	      clientKeyStore = KeyStore.getInstance( "JKS" );
	      clientKeyStore.load( new FileInputStream( "client.private" ),
	                         "public".toCharArray() );
	}*/

}
