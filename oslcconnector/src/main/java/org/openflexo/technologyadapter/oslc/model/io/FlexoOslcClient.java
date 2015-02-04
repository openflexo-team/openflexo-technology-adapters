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
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.wink.client.ClientWebException;
import org.eclipse.lyo.oslc4j.client.OslcRestClient;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

public class FlexoOslcClient {

	private static Set<Class<?>> PROVIDERS = new HashSet<Class<?>>();
	private final String mediaType;
	private final FlexoOslcAdaptorConfiguration adaptorConfiguration;

	static {
		PROVIDERS.addAll(JenaProvidersRegistry.getProviders());
		PROVIDERS.addAll(Json4JProvidersRegistry.getProviders());
		PROVIDERS.add(ServiceProviderCatalog.class);
		PROVIDERS.add(ServiceProvider.class);
	}

	public FlexoOslcClient(FlexoOslcAdaptorConfiguration adaptorConfiguration) {
		this.mediaType = OslcMediaType.RDF_XML;
		this.adaptorConfiguration = adaptorConfiguration;
	}

	public FlexoOslcClient(FlexoOslcAdaptorConfiguration adaptorConfiguration, String mediaType) {
		this.mediaType = mediaType;
		this.adaptorConfiguration = adaptorConfiguration;
	}

	public FlexoOslcClient(FlexoOslcAdaptorConfiguration adaptorConfiguration, Set<Class<?>> providers, String mediaType) {
		PROVIDERS.addAll(providers);
		this.mediaType = mediaType;
		this.adaptorConfiguration = adaptorConfiguration;
	}

	/**
	 * Get an OSLC resource of a certain class from a given uri, in preconfigured media type
	 * 
	 * @param resourceClass
	 * @param uri
	 * @return the OSLC resource
	 */
	public <T> T getResource(String uri, final Class<T> resourceClass) {
		return getResource(resourceClass, uri, mediaType);
	}

	/**
	 * Get an OSLC resource of a certain class from a given uri and a media type
	 * 
	 * @param resourceClass
	 * @param uri
	 * @param mediaType
	 * @return the OSLC resource
	 */
	public <T> T getResource(final Class<T> resourceClass, String uri, String mediaType) {
		// Create the client
		T result = null;
		trustIt();
		OslcRestClient client = new OslcRestClient(PROVIDERS, uri);
		try {
			// Try to get the resource
			result = client.getOslcResource(resourceClass);
		} catch (ClientWebException e) {
			if (e.getResponse().getStatusCode() == 401) {
				System.out.println("Autorization required");
				result = getoAuthResource(resourceClass, uri, mediaType);
			}
		} catch (Exception e) {
			System.out.println("Connection Error");
		}
		return result;
	}

	/**
	 * Get an OSLC resource requiring oAuth of a certain class from a given uri and a media type, and a set of required parameters for oAuth
	 * potential authentification
	 * 
	 * @param resourceClass
	 * @param uri
	 * @param mediaType
	 * @return
	 */
	public <T> T getoAuthResource(final Class<T> resourceClass, String uri, String mediaType) {
		FlexoOAuthClient oAuth = new FlexoOAuthClient(adaptorConfiguration, null, null);
		return getoAuthResource(uri, oAuth, resourceClass);
	}

	/**
	 * Get an OSLC resource using oAuth authentification
	 * 
	 * @param resourceUri
	 * @param oAuth
	 * @param resourceClass
	 * @return
	 */
	public <T> T getoAuthResource(String resourceUri, FlexoOAuthClient oAuth, final Class<T> resourceClass) {
		try {
			// Create an oAuth client
			OAuthClient client = new OAuthClient(new HttpClient4());

			// get the Request Token
			client.getRequestToken(oAuth.getAccessor());

			// Get the redirection url
			String redirect = oAuth.getConsumer().serviceProvider.userAuthorizationURL + "?oauth_token=" + oAuth.getAccessor().requestToken;
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

			// POST user login and password to the authorization url

			HttpPost formPost = new HttpPost(oAuth.getAuthorizationUrl());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("j_username", oAuth.getLogin()));
			nvps.add(new BasicNameValuePair("j_password", oAuth.getPassword()));
			formPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse formResponse = httpClient.execute(formPost);
			location = formResponse.getFirstHeader("Location");

			// Get the refirection
			if (location != null) {
				HttpGet request4 = new HttpGet(location.getValue());
				HttpClientParams.setRedirecting(request4.getParams(), false);
				response = httpClient.execute(request4);

				Map<String, String> oAuthMap = getQueryMap(location.getValue());
				String oauthToken = oAuthMap.get("oauth_token");
				String oauthverifier = oAuthMap.get("oauth_verifier");
				// The server requires an authentication: Create the login form
				HttpPost formPost2 = new HttpPost(oAuth.getAuthURL());
				formPost2.getParams().setParameter("oauth_token", oauthToken);
				formPost2.getParams().setParameter("oauth_verifier", oauthverifier);
				formPost2.getParams().setParameter("authorize", "true");
				formPost2.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				formResponse = httpClient.execute(formPost2);
			}

			Header header = formResponse.getFirstHeader("Content-Length");
			if ((header != null) && (!("0".equals(header.getValue())))) {
				// The login failed
				throw new InvalidCredentialsException("Authentication failed");
			}
			else {
				// The login succeed, Request again the protected resource
				return getResource(resourceUri, resourceClass);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> getQueryMap(String query) {
		Map<String, String> map = new HashMap<String, String>();
		String[] params = query.split("&");

		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}

		return map;
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

	/*protected URI create(final String mediaType, ServiceProvider serviceProvider) throws URISyntaxException {

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

	}*/

	/**
	 * This should be removed
	 */
	private void trustIt() {

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

}
