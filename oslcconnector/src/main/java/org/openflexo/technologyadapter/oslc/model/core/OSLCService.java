/*
 * (c) Copyright 2013 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of either : 
 * - GNU General Public License as published by
 * the Free Software Foundation version 3 of the License.
 * - EUPL v1.1 : European Union Public Licence
 * 
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License or EUPL for more details.
 *
 * You should have received a copy of the GNU General Public License or 
 * European Union Public Licence along with OpenFlexo. 
 * If not, see <http://www.gnu.org/licenses/>, or http://ec.europa.eu/idabc/eupl.html
 *
 */

package org.openflexo.technologyadapter.oslc.model.core;

import java.util.List;

import org.eclipse.lyo.oslc4j.core.model.Service;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;

@ModelEntity
@ImplementationClass(OSLCService.OSLCServiceImpl.class)
@XMLElement(xmlTag = "OSLCService")
public interface OSLCService extends OSLCResource {

	public static final String OSLC_SERVICE_KEY = "OSLCService";

	@PropertyIdentifier(type = List.class)
	public static final String CREATION_FACTORIES_KEY = "factories";

	@PropertyIdentifier(type = List.class)
	public static final String QUERY_CAPABILITIES_KEY = "queries";

	@Getter(value = OSLC_SERVICE_KEY, ignoreType = true)
	public Service getOSLCService();

	@Setter(OSLC_SERVICE_KEY)
	public void setOSLCService(Service oslcService);

	@Getter(value = CREATION_FACTORIES_KEY, cardinality = Cardinality.LIST, inverse = OSLCCreationFactory.OSLC_SERVICE_KEY)
	public List<OSLCCreationFactory> getOSLCCreationFactories();

	@Setter(CREATION_FACTORIES_KEY)
	public void setOSLCCreationFactories(List<OSLCCreationFactory> oslcCreationFactories);

	@Adder(CREATION_FACTORIES_KEY)
	public void addToOSLCCreationFactories(OSLCCreationFactory oslcCreationFactory);

	@Remover(CREATION_FACTORIES_KEY)
	public void removeFromOSLCCreationFactories(OSLCCreationFactory oslcCreationFactory);

	@Getter(value = QUERY_CAPABILITIES_KEY, cardinality = Cardinality.LIST, inverse = OSLCCreationFactory.OSLC_SERVICE_KEY)
	public List<OSLCQueryCapability> getOSLCQueryCapabilities();

	@Setter(QUERY_CAPABILITIES_KEY)
	public void setOSLCQueryCapabilities(List<OSLCQueryCapability> oslcQueryCapabilities);

	@Adder(QUERY_CAPABILITIES_KEY)
	public void addToOSLCQueryCapabilities(OSLCQueryCapability oslcQueryCapability);

	@Remover(QUERY_CAPABILITIES_KEY)
	public void removeFromOSLCQueryCapabilities(OSLCQueryCapability oslcQueryCapability);

	public static abstract class OSLCServiceImpl extends OSLCResourceImpl implements OSLCService {

		public OSLCServiceImpl() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getUri() {
			if (getOSLCService().getAbout() != null) {
				return getOSLCService().getAbout().toString();
			}
			else {
				return "";
			}

		}

	}

}
