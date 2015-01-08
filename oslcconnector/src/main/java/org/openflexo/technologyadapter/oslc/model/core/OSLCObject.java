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

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.OSLCTechnologyAdapter;

@ModelEntity
@ImplementationClass(OSLCObject.OSLCObjectImpl.class)
@XMLElement(xmlTag = "OSLCObject")
public interface OSLCObject extends TechnologyObject<OSLCTechnologyAdapter> {

	@PropertyIdentifier(type = String.class)
	public static final String NAME_KEY = "name";

	@PropertyIdentifier(type = String.class)
	public static final String URI_KEY = "uri";

	@Getter(value = NAME_KEY)
	public String getName();

	@Setter(value = NAME_KEY)
	public void setName(String name);

	@Getter(value = URI_KEY)
	public String getUri();

	@Setter(value = URI_KEY)
	public void setUri(String uri);

	@Override
	public OSLCTechnologyAdapter getTechnologyAdapter();

	public void setTechnologyAdapter(OSLCTechnologyAdapter technologyAdapter);

	public static abstract class OSLCObjectImpl extends FlexoObjectImpl implements OSLCObject {

		private OSLCTechnologyAdapter technologyAdapter;

		@Override
		public void setTechnologyAdapter(OSLCTechnologyAdapter technologyAdapter) {
			this.technologyAdapter = technologyAdapter;
		}

		@Override
		public OSLCTechnologyAdapter getTechnologyAdapter() {
			return technologyAdapter;
		}

	}

}
