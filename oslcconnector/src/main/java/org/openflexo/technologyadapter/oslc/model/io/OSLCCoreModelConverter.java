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

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.openflexo.model.ModelContext;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.oslc.OSLCTechnologyAdapter;
import org.openflexo.technologyadapter.oslc.model.core.OSLCCreationFactory;
import org.openflexo.technologyadapter.oslc.model.core.OSLCObject;
import org.openflexo.technologyadapter.oslc.model.core.OSLCQueryCapability;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCService;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;

public class OSLCCoreModelConverter {

	private static final Logger logger = Logger.getLogger(OSLCCoreModelConverter.class.getPackage().getName());

	protected final Map<Object, OSLCObject> OSLCObjects = new HashMap<Object, OSLCObject>();

	private ModelFactory factory;
	private ModelContext modelContext;
	private String baseUri;
	private OSLCTechnologyAdapter technologyAdapter;
	private FlexoOslcClient oslcClient;

	/**
	 * Constructor.
	 */
	public OSLCCoreModelConverter(FlexoOslcAdaptorConfiguration adaptorConfiguration) {
		try {
			this.baseUri = adaptorConfiguration.getBaseUri();
			factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(OSLCResource.class, OSLCServiceProviderCatalog.class,
					OSLCServiceProvider.class, OSLCQueryCapability.class, OSLCCreationFactory.class));
			modelContext = new ModelContext(OSLCResource.class);
			oslcClient = new FlexoOslcClient(adaptorConfiguration);
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OSLCTechnologyAdapter getTechnologyAdapter() {
		return technologyAdapter;
	}

	public void setTechnologyAdapter(OSLCTechnologyAdapter technologyAdapter) {
		this.technologyAdapter = technologyAdapter;
	}

	public OSLCServiceProviderCatalog convertAllCoreResourcesFromCatalog() {
		OSLCServiceProviderCatalog oslcResource = null;
		ServiceProviderCatalog catalog;
		try {
			catalog = (ServiceProviderCatalog) oslcClient.retrieve(baseUri);

			if (catalog != null) {
				oslcResource = convertOSLCServiceProviderCatalog(catalog);
				for (ServiceProvider sp : catalog.getServiceProviders()) {
					oslcResource.addToOSLCServiceProviders(convertOSLCServiceProvider(sp));
				}
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return oslcResource;
	}

	public OSLCServiceProviderCatalog convertOSLCServiceProviderCatalog(ServiceProviderCatalog resource) {
		OSLCServiceProviderCatalog oslcResource = factory.newInstance(OSLCServiceProviderCatalog.class);
		oslcResource.setOSLCServiceProviderCatalog(resource);
		oslcResource.setTechnologyAdapter(technologyAdapter);
		OSLCObjects.put(resource, oslcResource);
		return oslcResource;
	}

	public OSLCServiceProvider convertOSLCServiceProvider(ServiceProvider serviceProvider) {
		OSLCServiceProvider oslcResource = factory.newInstance(OSLCServiceProvider.class);
		for (Service service : serviceProvider.getServices()) {
			oslcResource.addToOSLCServices(convertOSLCService(service));
		}
		oslcResource.setOSLCServiceProvider(serviceProvider);
		oslcResource.setTechnologyAdapter(technologyAdapter);
		OSLCObjects.put(serviceProvider, oslcResource);
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
		oslcResource.setTechnologyAdapter(technologyAdapter);
		OSLCObjects.put(resource, oslcResource);
		return oslcResource;
	}

	public OSLCQueryCapability convertOSLCQueryCapability(QueryCapability resource) {
		OSLCQueryCapability oslcResource = factory.newInstance(OSLCQueryCapability.class);
		oslcResource.setOSLCQueryCapability(resource);
		oslcResource.setTechnologyAdapter(technologyAdapter);
		// catalogOslcClient.getResourcesFromQueryCapability(OslcMediaType.APPLICATION_RDF_XML, resource);
		OSLCObjects.put(resource, oslcResource);
		return oslcResource;
	}

	public OSLCCreationFactory convertOSLCCreationFactory(CreationFactory resource) {
		OSLCCreationFactory oslcResource = factory.newInstance(OSLCCreationFactory.class);
		oslcResource.setOSLCCreationFactory(resource);
		oslcResource.setTechnologyAdapter(technologyAdapter);
		OSLCObjects.put(resource, oslcResource);
		return oslcResource;
	}

	public Map<Object, OSLCObject> getOSLCObjects() {
		return OSLCObjects;
	}

	private Object getOSLCObjectFromFlexoOSLCObject(OSLCObject object) {
		for (Entry entry : OSLCObjects.entrySet()) {
			Object key = entry.getKey();
			if (object.equals(OSLCObjects.get(key))) {
				return key;
			}
		}
		return null;
	}

}
