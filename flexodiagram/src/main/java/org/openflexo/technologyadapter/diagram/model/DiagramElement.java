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

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.openflexo.antar.binding.BindingEvaluationContext;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InnerResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.DiagramModelSlot;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementPatternRole;

/**
 * Root class for any object involved in Openflexo Diagram built-in technology
 * 
 * @author sylvain
 * 
 * @param <G>
 *            type of underlying graphical representation (sub-class of {@link GraphicalRepresentation})
 */
@ModelEntity
@ImplementationClass(DiagramElementImpl.class)
public interface DiagramElement<G extends GraphicalRepresentation> extends FlexoObject, InnerResourceData<Diagram>,
		TechnologyObject<DiagramTechnologyAdapter>, BindingEvaluationContext, Cloneable, Observer {

	public static final String GRAPHICAL_REPRESENTATION = "graphicalRepresentation";
	public static final String NAME = "name";
	public static final String PARENT = "parent";

	/**
	 * Return name of this diagram element
	 * 
	 * @return
	 */
	@Getter(value = NAME)
	@XMLAttribute
	public String getName();

	/**
	 * Sets name of this diagram element
	 * 
	 * @param aName
	 */
	@Setter(value = NAME)
	public void setName(String aName);

	/**
	 * Return parent of this diagram element
	 * 
	 * @return
	 */
	@Getter(value = PARENT)
	public DiagramContainerElement<?> getParent();

	/**
	 * Sets parent of this diagram element
	 * 
	 * @param aName
	 */
	@Setter(value = PARENT)
	public void setParent(DiagramContainerElement<?> aParent);

	/**
	 * Return the diagram where this diagram element exists
	 * 
	 * @return
	 */
	public Diagram getDiagram();

	/**
	 * Return the {@link GraphicalRepresentation} of this diagram element
	 * 
	 * @return
	 */
	@Getter(value = GRAPHICAL_REPRESENTATION)
	@CloningStrategy(StrategyType.CLONE)
	@Embedded
	@XMLElement
	public G getGraphicalRepresentation();

	/**
	 * Sets the {@link GraphicalRepresentation} of this diagram element
	 * 
	 * @param graphicalRepresentation
	 */
	@Setter(value = GRAPHICAL_REPRESENTATION)
	public void setGraphicalRepresentation(G graphicalRepresentation);

	/**
	 * Indicates if this diagram element is contained in supplied diagram element
	 * 
	 * @param o
	 * @return
	 */
	public boolean isContainedIn(DiagramElement<?> element);

	/**
	 * Clone this diagram element
	 * 
	 * @return
	 */
	public DiagramElement<G> clone();

	@Override
	public void update(Observable o, Object arg);

	@Override
	public void setChanged();

	public boolean hasChanged();

	public List<DiagramContainerElement<?>> getAncestors();

	/**
	 * Return {@link FlexoConceptInstance} where this {@link DiagramElement} is involved, asserting that this {@link DiagramElement} is
	 * contained in a {@link Diagram} which is the bound diagram of a {@link DiagramModelSlot} declared in {@link VirtualModel} of supplied
	 * {@link VirtualModelInstance}
	 * 
	 * @param vmInstance
	 *            : instance of {@link VirtualModel} where is declared a {@link DiagramModelSlot}
	 * @return
	 */
	public FlexoConceptInstance getFlexoConceptInstance(VirtualModelInstance vmInstance);

	/**
	 * Return {@link GraphicalElementPatternRole} played by this {@link DiagramElement} in related {@link FlexoConceptInstance}, asserting
	 * that this {@link DiagramElement} is contained in a {@link Diagram} which is the bound diagram of a {@link DiagramModelSlot} declared
	 * in {@link VirtualModel} of supplied {@link VirtualModelInstance}
	 * 
	 * @param vmInstance
	 *            : instance of {@link VirtualModel} where is declared a {@link DiagramModelSlot}
	 * @return
	 */
	public GraphicalElementPatternRole<?, ?> getPatternRole(VirtualModelInstance vmInstance);

}
