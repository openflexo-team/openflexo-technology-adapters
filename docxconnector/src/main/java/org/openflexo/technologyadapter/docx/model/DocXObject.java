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

package org.openflexo.technologyadapter.docx.model;

import org.openflexo.foundation.InnerResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;

@ModelEntity
@ImplementationClass(DocXObject.DocXObjectImpl.class)
@XMLElement
public interface DocXObject extends TechnologyObject<DocXTechnologyAdapter>, InnerResourceData<DocXDocument> {

	public DocXDocument getDocXDocument();

	public static abstract class DocXObjectImpl extends FlexoObjectImpl implements DocXObject {

		public DocXObjectImpl() {
			super();
		}

		@Override
		public DocXDocument getResourceData() {
			return getDocXDocument();
		}
	}

}
