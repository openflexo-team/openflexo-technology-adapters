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
