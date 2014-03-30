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


package org.openflexo.technologyadapter.csv.virtualmodel;

import org.openflexo.foundation.viewpoint.IndividualRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.csv.model.CSVObjectIndividual;

@ModelEntity
@ImplementationClass(CSVObjectIndividualRole.CSVObjectIndividualRoleImpl.class)
@XMLElement
public interface CSVObjectIndividualRole extends IndividualRole<CSVObjectIndividual> {

	public static abstract class CSVObjectIndividualRoleImpl extends IndividualRoleImpl<CSVObjectIndividual> implements
			CSVObjectIndividualRole {

		public CSVObjectIndividualRoleImpl() {
			super();
		}

	}
}
