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

import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
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
@ImplementationClass(OSLCServiceProvider.OSLCServiceProviderImpl.class)
@XMLElement(xmlTag = "OSLCServiceProvider")
public interface OSLCServiceProvider extends OSLCResource {

	public static final String OSLC_SERVICE_PROVIDER_KEY = "OSLCServiceProvider";

	@Getter(value = OSLC_SERVICE_PROVIDER_KEY, ignoreType = true)
	public ServiceProvider getOSLCServiceProvider();

	@Setter(OSLC_SERVICE_PROVIDER_KEY)
	public void setOSLCServiceProvider(ServiceProvider oslcServiceProvider);

	@PropertyIdentifier(type = List.class)
	public static final String SERVICES_KEY = "services";

	@Getter(value = SERVICES_KEY, cardinality = Cardinality.LIST)
	public List<OSLCService> getOSLCServices();

	@Setter(SERVICES_KEY)
	public void setOSLCServices(List<OSLCService> oslcServices);

	@Adder(SERVICES_KEY)
	public void addToOSLCServices(OSLCService oslcService);

	@Remover(SERVICES_KEY)
	public void removeFromOSLCServices(OSLCService oslcService);

	public static abstract class OSLCServiceProviderImpl extends OSLCResourceImpl implements OSLCServiceProvider {

		public OSLCServiceProviderImpl() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getUri() {
			return getOSLCServiceProvider().getAbout().toString();
		}

	}

}
