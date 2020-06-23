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

package org.openflexo.technologyadapter.odt.model;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;

/**
 * Represents a {@link ODTDocument}<br>
 * This class wraps <here name of JOpenDocument concept>
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(ODTDocument.ODTDocumentImpl.class)
@XMLElement
public interface ODTDocument extends ODTObject, ResourceData<ODTDocument> {

	public static abstract class ODTDocumentImpl extends ODTObjectImpl implements ODTDocument {

		public ODTDocumentImpl() {
			super();
		}

	}

}
