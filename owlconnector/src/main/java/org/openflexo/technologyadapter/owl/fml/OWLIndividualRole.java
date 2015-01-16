/*
 *
 * (c) Copyright 2014- Openflexo
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

package org.openflexo.technologyadapter.owl.fml;

import org.openflexo.foundation.fml.IndividualRole;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;

@ModelEntity
@ImplementationClass(OWLIndividualRole.OWLIndividualRoleImpl.class)
@XMLElement
@FML("OWLIndividualRole")
public interface OWLIndividualRole extends IndividualRole<OWLIndividual> {

	public static abstract class OWLIndividualRoleImpl extends IndividualRoleImpl<OWLIndividual> implements OWLIndividualRole {

		public OWLIndividualRoleImpl() {
			super();
		}

	}
}
