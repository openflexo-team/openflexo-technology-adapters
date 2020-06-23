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

import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.pamela.annotations.Getter.Cardinality;

@ModelEntity
@ImplementationClass(OSLCServiceProviderCatalog.OSLCServiceProviderCatalogImpl.class)
@XMLElement(xmlTag = "OSLCServiceProviderCatalog")
public interface OSLCServiceProviderCatalog extends OSLCResource, ResourceData<OSLCServiceProviderCatalog> {

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

		@Override
		public String getUri() {
			return getOSLCServiceProviderCatalog().getAbout().toString();
		}

	}

}
