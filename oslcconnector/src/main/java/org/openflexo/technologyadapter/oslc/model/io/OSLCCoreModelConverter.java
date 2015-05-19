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
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.oslc.model.core.OSLCCreationFactory;
import org.openflexo.technologyadapter.oslc.model.core.OSLCQueryCapability;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCService;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;

public class OSLCCoreModelConverter implements OSLCModelDedicatedConverter {

	private static final Logger logger = Logger.getLogger(OSLCCoreModelConverter.class.getPackage().getName());

	private ModelFactory factory;
	private final OSLCModelConverter mainConverter;

	/**
	 * Constructor.
	 */
	public OSLCCoreModelConverter(OSLCModelConverter mainConverter) {
		this.mainConverter = mainConverter;
		try {
			factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(OSLCResource.class, OSLCServiceProviderCatalog.class,
					OSLCServiceProvider.class, OSLCQueryCapability.class, OSLCCreationFactory.class));
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OSLCServiceProviderCatalog convertAllCoreResourcesFromCatalog() {
		OSLCServiceProviderCatalog oslcResource = null;
		ServiceProviderCatalog catalog;
		catalog = retrieveResource(getBaseUri(), ServiceProviderCatalog.class);
		// Could be as well
		// catalog = oslcClient.retrieve(baseUri);

		if (catalog != null) {
			oslcResource = convertOSLCServiceProviderCatalog(catalog);
			for (ServiceProvider sp : catalog.getServiceProviders()) {
				oslcResource.addToOSLCServiceProviders(convertOSLCServiceProvider(sp));
			}
		}
		return oslcResource;
	}

	public OSLCServiceProviderCatalog convertOSLCServiceProviderCatalog(ServiceProviderCatalog resource) {
		OSLCServiceProviderCatalog oslcResource = factory.newInstance(OSLCServiceProviderCatalog.class);
		oslcResource.setOSLCServiceProviderCatalog(resource);
		oslcResource.setTechnologyAdapter(mainConverter.getTechnologyAdapter());
		mainConverter.getOSLCObjects().put(resource, oslcResource);
		return oslcResource;
	}

	public OSLCServiceProvider convertOSLCServiceProvider(ServiceProvider serviceProvider) {
		OSLCServiceProvider oslcResource = factory.newInstance(OSLCServiceProvider.class);
		Service[] services = serviceProvider.getServices();
		if (services == null || services.length == 0) {
			services = retrieveResources(serviceProvider.getAbout().toString(), Service.class);
		}
		for (Service service : services) {
			oslcResource.addToOSLCServices(convertOSLCService(service));
		}
		oslcResource.setOSLCServiceProvider(serviceProvider);
		oslcResource.setTechnologyAdapter(mainConverter.getTechnologyAdapter());
		mainConverter.getOSLCObjects().put(serviceProvider, oslcResource);
		return oslcResource;
	}

	public OSLCService convertOSLCService(Service resource) {
		OSLCService oslcResource = factory.newInstance(OSLCService.class);
		oslcResource.setOSLCService(resource);
		for (CreationFactory factory : resource.getCreationFactories()) {
			oslcResource.addToOSLCCreationFactories(convertOSLCCreationFactory(factory));
		}
		for (QueryCapability query : resource.getQueryCapabilities()) {
			oslcResource.addToOSLCQueryCapabilities(convertOSLCQueryCapability(query));
		}
		oslcResource.setTechnologyAdapter(mainConverter.getTechnologyAdapter());
		mainConverter.getOSLCObjects().put(resource, oslcResource);
		return oslcResource;
	}

	public OSLCQueryCapability convertOSLCQueryCapability(QueryCapability resource) {
		OSLCQueryCapability oslcResource = factory.newInstance(OSLCQueryCapability.class);
		oslcResource.setOSLCQueryCapability(resource);
		oslcResource.setTechnologyAdapter(mainConverter.getTechnologyAdapter());
		// catalogOslcClient.getResourcesFromQueryCapability(OslcMediaType.APPLICATION_RDF_XML, resource);
		mainConverter.getOSLCObjects().put(resource, oslcResource);
		return oslcResource;
	}

	public OSLCCreationFactory convertOSLCCreationFactory(CreationFactory resource) {
		OSLCCreationFactory oslcResource = factory.newInstance(OSLCCreationFactory.class);
		oslcResource.setOSLCCreationFactory(resource);
		oslcResource.setTechnologyAdapter(mainConverter.getTechnologyAdapter());
		mainConverter.getOSLCObjects().put(resource, oslcResource);
		return oslcResource;
	}

	public <R extends OSLCResource> R createOSLCResource(Class<R> klass, AbstractResource resource, CreationFactory creationFactory) {
		R oslcResource = factory.newInstance(klass);
		oslcResource.setTechnologyAdapter(mainConverter.getTechnologyAdapter());
		mainConverter.getOSLCObjects().put(resource, oslcResource);
		return oslcResource;
	}

	private String getBaseUri() {
		return mainConverter.getAdaptorConfiguration().getBaseUri();
	}

	private FlexoOslcClient getClient() {
		return mainConverter.getOslcClient();
	}

	private <T extends AbstractResource> T[] retrieveResources(String uri, Class<T> resourceClasses) {
		try {
			return getClient().retrieves(uri, (Class<T[]>) Array.newInstance(resourceClasses, 0).getClass());
		} catch (URISyntaxException e) {
			logger.warning("URI " + uri + " is not correct");
			e.printStackTrace();
			return null;
		}
	}

	private <T extends AbstractResource> T retrieveResource(String uri, Class<T> resourceClass) {
		try {
			return getClient().retrieve(uri, resourceClass);
		} catch (URISyntaxException e) {
			logger.warning("URI " + uri + " is not correct");
			e.printStackTrace();
			return null;
		}
	}
}
