#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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


package ${package}.virtualmodel;

import org.openflexo.foundation.viewpoint.IndividualRole;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import ${package}.model.${technologyPrefix}ObjectIndividual;

@ModelEntity
@ImplementationClass(${technologyPrefix}ObjectIndividualRole.${technologyPrefix}ObjectIndividualRoleImpl.class)
@XMLElement
public interface ${technologyPrefix}ObjectIndividualRole extends IndividualRole<${technologyPrefix}ObjectIndividual> {

	public static abstract class ${technologyPrefix}ObjectIndividualRoleImpl extends IndividualRoleImpl<${technologyPrefix}ObjectIndividual> implements
			${technologyPrefix}ObjectIndividualRole {

		public ${technologyPrefix}ObjectIndividualRoleImpl() {
			super();
		}

	}
}
