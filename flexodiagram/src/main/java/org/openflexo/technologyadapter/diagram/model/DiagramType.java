/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.model;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.openflexo.connie.type.CustomTypeFactory;
import org.openflexo.foundation.fml.TechnologySpecificType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;

public class DiagramType implements TechnologySpecificType<TechnologyAdapter> {

	protected DiagramSpecification diagramSpecification;

	private DiagramType(DiagramSpecification diagramSpecification) {
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
	public boolean isOfType(Object object, boolean permissive) {
		if (!(object instanceof Diagram)) {
			return false;
		}
		// TODO please implement me
		return true;
	}

	@Override
	public String simpleRepresentation() {
		return "DiagramType" + ":" + (diagramSpecification != null ? diagramSpecification.toString() : null);
	}

	@Override
	public String fullQualifiedRepresentation() {
		return "DiagramType" + ":" + (diagramSpecification != null ? diagramSpecification.toString() : null);
	}

	@Override
	public String toString() {
		return simpleRepresentation();
	}

	@Override
	public TechnologyAdapter getSpecificTechnologyAdapter() {
		return getDiagramSpecification().getTechnologyAdapter();
	}

	private static Map<DiagramSpecification, DiagramType> dsMap = new HashMap<DiagramSpecification, DiagramType>();

	public static DiagramType getDiagramType(DiagramSpecification diagramSpecification) {

		DiagramType returned = dsMap.get(diagramSpecification);
		if (returned == null) {
			returned = new DiagramType(diagramSpecification);
			dsMap.put(diagramSpecification, returned);
		}
		return returned;
	}

	@Override
	public String getSerializationRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isResolved() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void resolve(CustomTypeFactory<?> factory) {
		// TODO Auto-generated method stub

	}

}
