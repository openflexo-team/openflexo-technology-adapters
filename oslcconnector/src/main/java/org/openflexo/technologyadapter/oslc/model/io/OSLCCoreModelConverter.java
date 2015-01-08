package org.openflexo.technologyadapter.oslc.model.io;

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
	private OSLCClient client;
	private OSLCTechnologyAdapter technologyAdapter;

	/**
	 * Constructor.
	 */
	public OSLCCoreModelConverter(OSLCClient client) {
		try {
			this.client = client;
			factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(OSLCResource.class, OSLCServiceProviderCatalog.class,
					OSLCServiceProvider.class, OSLCQueryCapability.class, OSLCCreationFactory.class));
			modelContext = new ModelContext(OSLCResource.class);
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

	public OSLCServiceProviderCatalog convertAllCoreResources() {
		OSLCServiceProviderCatalog catalog = convertOSLCServiceProviderCatalog(client.getSpc());
		for (ServiceProvider sp : client.getSps()) {
			catalog.addToOSLCServiceProviders(convertOSLCServiceProvider(sp));
		}
		return catalog;
	}

	public OSLCServiceProviderCatalog convertOSLCServiceProviderCatalog(ServiceProviderCatalog resource) {
		OSLCServiceProviderCatalog oslcResource = factory.newInstance(OSLCServiceProviderCatalog.class);
		oslcResource.setOSLCServiceProviderCatalog(resource);
		oslcResource.setTechnologyAdapter(technologyAdapter);
		OSLCObjects.put(resource, oslcResource);
		return oslcResource;
	}

	public OSLCServiceProvider convertOSLCServiceProvider(ServiceProvider resource) {
		OSLCServiceProvider oslcResource = factory.newInstance(OSLCServiceProvider.class);
		for (Service service : resource.getServices()) {
			oslcResource.addToOSLCServices(convertOSLCService(service));
		}
		oslcResource.setOSLCServiceProvider(resource);
		oslcResource.setTechnologyAdapter(technologyAdapter);
		OSLCObjects.put(resource, oslcResource);
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
