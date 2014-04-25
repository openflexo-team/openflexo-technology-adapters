/*
 * (c) Copyright 2013-2014 Openflexo
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
package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

/**
 * Represents a {@link DiagramElement} seen in federated context<br>
 * Instead of just referencing the {@link DiagramElement}, we address it from a {@link FlexoConceptInstance} and a {@link ShapeRole}.
 * 
 * 
 * @author sylvain
 * 
 */
@ModelEntity(isAbstract = true)
public interface FMLControlledDiagramElement<E extends DiagramElement<GR>, GR extends GraphicalRepresentation> extends FlexoObject {

	public static final String FLEXO_CONCEPT_INSTANCE_KEY = "flexoConceptInstance";
	public static final String DIAGRAM_ELEMENT_KEY = "diagramElement";
	public static final String ROLE_KEY = "role";

	/**
	 * Return the {@link FlexoConceptInstance} where {@link DiagramElement} is referenced
	 * 
	 * @return
	 */
	@Getter(FLEXO_CONCEPT_INSTANCE_KEY)
	public FlexoConceptInstance getFlexoConceptInstance();

	/**
	 * Sets the {@link FlexoConceptInstance} where {@link DiagramElement} is referenced
	 * 
	 * @param aFlexoConceptInstance
	 */
	@Setter(FLEXO_CONCEPT_INSTANCE_KEY)
	public void setFlexoConceptInstance(FlexoConceptInstance aFlexoConceptInstance);

	/**
	 * Return the addressed {@link DiagramElement}
	 * 
	 * @return
	 */
	@Getter(DIAGRAM_ELEMENT_KEY)
	public E getDiagramElement();

	/**
	 * 
	 * @param diagramElement
	 */
	@Setter(DIAGRAM_ELEMENT_KEY)
	public void setDiagramElement(E diagramElement);

	/**
	 * Return the role from which related {@link FlexoConceptInstance} addresses the {@link DiagramElement}
	 * 
	 * @return
	 */
	@Getter(ROLE_KEY)
	public GraphicalElementRole<E, GR> getRole();

	/**
	 * Sets the role from which related {@link FlexoConceptInstance} addresses the {@link DiagramElement}
	 * 
	 * @return
	 */
	@Setter(ROLE_KEY)
	public void setRole(GraphicalElementRole<E, GR> aRole);

}