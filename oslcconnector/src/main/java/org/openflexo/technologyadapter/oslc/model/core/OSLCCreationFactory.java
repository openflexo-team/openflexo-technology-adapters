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

import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;

@ModelEntity
@ImplementationClass(OSLCCreationFactory.OSLCCreationFactoryImpl.class)
@XMLElement(xmlTag = "OSLCCreationFactory")
public interface OSLCCreationFactory extends OSLCResource {

	public static final String OSLC_QUERY_CAPABILITY_KEY = "OSLCCreationFactory";

	public static final String OSLC_SERVICE_KEY = "OSLCService";

	@Getter(value = OSLC_QUERY_CAPABILITY_KEY, ignoreType = true)
	public CreationFactory getOSLCCreationFactory();

	@Setter(OSLC_QUERY_CAPABILITY_KEY)
	public void setOSLCCreationFactory(CreationFactory CreationFactory);

	@Getter(value = OSLC_SERVICE_KEY, inverse = OSLCService.CREATION_FACTORIES_KEY)
	public OSLCService getOSLCService();

	@Setter(OSLC_SERVICE_KEY)
	public void setOSLCService(OSLCService oslcService);

	public static abstract class OSLCCreationFactoryImpl extends OSLCResourceImpl implements OSLCCreationFactory {

		public OSLCCreationFactoryImpl() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getUri() {
			if (getOSLCCreationFactory().getAbout() != null) {
				return getOSLCCreationFactory().getAbout().toString();
			}
			else {
				return getOSLCCreationFactory().getCreation().toString();
			}

		}

	}

}
