/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.odt.fml;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.odt.model.ODTParagraph;

/**
 * Represents the specification of accessing to a ODTParagraph
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(ODTParagraphRole.ODTParagraphRoleImpl.class)
@XMLElement
public interface ODTParagraphRole extends FlexoRole<ODTParagraph> {

	public static abstract class ODTParagraphRoleImpl extends FlexoRoleImpl<ODTParagraph> implements ODTParagraphRole {

		public ODTParagraphRoleImpl() {
			super();
		}

	}
}
