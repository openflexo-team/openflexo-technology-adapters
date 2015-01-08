/*
 * (c) Copyright 2013 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.oslc.model.core;

import java.util.List;

import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
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
@ImplementationClass(OSLCServiceProviderCatalog.OSLCServiceProviderCatalogImpl.class)
@XMLElement(xmlTag = "OSLCServiceProviderCatalog")
public interface OSLCServiceProviderCatalog extends OSLCResource {

	public static final String OSLC_SERVICE_PROVIDER_CATALOG_KEY = "OSLCServiceProviderCatalog";

	@Getter(value = OSLC_SERVICE_PROVIDER_CATALOG_KEY, ignoreType = true)
	public ServiceProviderCatalog getOSLCServiceProviderCatalog();

	@Setter(OSLC_SERVICE_PROVIDER_CATALOG_KEY)
	public void setOSLCServiceProviderCatalog(ServiceProviderCatalog oslcServiceProviderCatalog);

	@PropertyIdentifier(type = List.class)
	public static final String SERVICE_PROVIDERS_KEY = "serviceProviders";

	@Getter(value = SERVICE_PROVIDERS_KEY, cardinality = Cardinality.LIST)
	public List<OSLCServiceProvider> getOSLCServiceProviders();

	@Setter(SERVICE_PROVIDERS_KEY)
	public void setOSLCServiceProviders(List<OSLCServiceProvider> oslcServiceProviders);

	@Adder(SERVICE_PROVIDERS_KEY)
	public void addToOSLCServiceProviders(OSLCServiceProvider oslcServiceProvider);

	@Remover(SERVICE_PROVIDERS_KEY)
	public void removeFromOSLCServiceProviders(OSLCServiceProvider oslcServiceProvider);

	public static abstract class OSLCServiceProviderCatalogImpl extends OSLCResourceImpl implements OSLCServiceProviderCatalog {

		public OSLCServiceProviderCatalogImpl() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getUri() {
			return getOSLCServiceProviderCatalog().getAbout().toString();
		}

	}

}
