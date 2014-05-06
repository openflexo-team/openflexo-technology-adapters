/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.technologyadapter.diagram.model;

import java.lang.reflect.Type;

import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.viewpoint.TechnologySpecificCustomType;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;

public class DiagramType implements TechnologySpecificCustomType<TechnologyAdapter> {

	protected DiagramSpecification diagramSpecification;

	public DiagramType(DiagramSpecification diagramSpecification) {
		this.diagramSpecification = diagramSpecification;
	}

	public DiagramSpecification getDiagramSpecification() {
		return diagramSpecification;
	}
	
	@Override
	public Class<Diagram> getBaseClass() {
		return Diagram.class;
	}

	@Override
	public boolean isTypeAssignableFrom(Type aType, boolean permissive) {
		// System.out.println("isTypeAssignableFrom " + aType + " (i am a " + this + ")");
		if (aType instanceof DiagramType) {
			return diagramSpecification.equals(((DiagramType) aType).getDiagramSpecification());
		}
		return false;
	}

	@Override
	public String simpleRepresentation() {
		return "DiagramType" + ":" + diagramSpecification.toString();
	}

	@Override
	public String fullQualifiedRepresentation() {
		return "DiagramType" + ":" + diagramSpecification.toString();
	}

	@Override
	public String toString() {
		return simpleRepresentation();
	}


	@Override
	public TechnologyAdapter getTechnologyAdapter() {
		return getDiagramSpecification().getTechnologyAdapter();
	}
}