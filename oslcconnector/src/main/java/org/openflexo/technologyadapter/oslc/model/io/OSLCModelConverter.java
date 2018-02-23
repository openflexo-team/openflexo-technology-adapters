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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.technologyadapter.oslc.OSLCTechnologyAdapter;
import org.openflexo.technologyadapter.oslc.model.core.OSLCObject;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;

public class OSLCModelConverter {

	private static final Logger logger = Logger.getLogger(OSLCModelConverter.class.getPackage().getName());

	protected final Map<Object, OSLCObject> OSLCObjects = new HashMap<>();

	private OSLCTechnologyAdapter technologyAdapter;
	private final FlexoOslcClient oslcClient;
	private List<OSLCModelDedicatedConverter> converters;
	private final FlexoOslcAdaptorConfiguration adaptorConfiguration;

	/**
	 * Constructor.
	 */
	public OSLCModelConverter(FlexoOslcAdaptorConfiguration adaptorConfiguration) {
		this.adaptorConfiguration = adaptorConfiguration;
		oslcClient = new FlexoOslcClient(adaptorConfiguration);
		converters = new ArrayList<>();
		converters.add(new OSLCCoreModelConverter(this));
		converters.add(new OSLCRMModelConverter(this));
	}

	public OSLCTechnologyAdapter getTechnologyAdapter() {
		return technologyAdapter;
	}

	public void setTechnologyAdapter(OSLCTechnologyAdapter technologyAdapter) {
		this.technologyAdapter = technologyAdapter;
	}

	public OSLCServiceProviderCatalog convertAllCoreResourcesFromCatalog() {
		OSLCCoreModelConverter coreConverter = getConverter(OSLCCoreModelConverter.class);
		if (coreConverter != null) {
			return coreConverter.convertAllCoreResourcesFromCatalog();
		}
		logger.warning("No core converter found ");
		return null;
	}

	public Map<Object, OSLCObject> getOSLCObjects() {
		return OSLCObjects;
	}

	/* Unused
	private <T extends AbstractResource> T[] retrieveResources(String uri, Class<T> resourceClasses) {
		try {
			return oslcClient.retrieves(uri, (Class<T[]>) Array.newInstance(resourceClasses, 0).getClass());
		} catch (URISyntaxException e) {
			logger.warning("URI " + uri + " is not correct");
			e.printStackTrace();
			return null;
		}
	}
	*/
	/* Unused
	private <T extends AbstractResource> T retrieveResource(String uri, Class<T> resourceClass) {
		try {
			return oslcClient.retrieve(uri, resourceClass);
		} catch (URISyntaxException e) {
			logger.warning("URI " + uri + " is not correct");
			e.printStackTrace();
			return null;
		}
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
	*/

	public FlexoOslcClient getOslcClient() {
		return oslcClient;
	}

	public List<OSLCModelDedicatedConverter> getConverters() {
		return converters;
	}

	public void setConverters(List<OSLCModelDedicatedConverter> converters) {
		this.converters = converters;
	}

	@SuppressWarnings("unchecked")
	public <T> T getConverter(Class<T> converterClass) {
		for (OSLCModelDedicatedConverter converter : converters) {
			if (converter.getClass().equals(converterClass)) {
				return (T) converter;
			}
		}
		return null;
	}

	public FlexoOslcAdaptorConfiguration getAdaptorConfiguration() {
		return adaptorConfiguration;
	}
}
