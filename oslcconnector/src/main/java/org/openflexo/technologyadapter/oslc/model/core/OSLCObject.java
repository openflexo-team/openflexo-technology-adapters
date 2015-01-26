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
