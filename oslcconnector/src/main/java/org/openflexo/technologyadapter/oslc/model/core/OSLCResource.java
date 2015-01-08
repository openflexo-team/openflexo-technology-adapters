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

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.model.io.OSLCCoreModelConverter;

@ModelEntity
@ImplementationClass(OSLCResource.OSLCResourceImpl.class)
@XMLElement(xmlTag = "OSLCResource")
public interface OSLCResource extends OSLCObject, ResourceData<OSLCResource> {

	public static final String OSLCResource_KEY = "OSLCResource";

	public AbstractResource getOSLCResource();

	public void setOSLCResource(AbstractResource oslcResource);

	public OSLCCoreModelConverter getConverter();

	public void setConverter(OSLCCoreModelConverter converter);

	public static abstract class OSLCResourceImpl extends OSLCObjectImpl implements OSLCResource {

		public OSLCResourceImpl() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public String getUri() {
			return getName();
		}

	}

}
