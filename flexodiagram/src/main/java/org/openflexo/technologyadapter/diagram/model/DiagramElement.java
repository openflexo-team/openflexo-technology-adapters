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

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.diana.GraphicalRepresentation;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InnerResourceData;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.DiagramModelSlot;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;

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

	public static final String INVALIDATE = "invalidate";

	public static final String GRAPHICAL_REPRESENTATION = "graphicalRepresentation";
	public static final String NAME = "name";
	public static final String TEXT = "text";
	public static final String PARENT = "parent";

	// public String getIdentifier();

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
	 * Return text of this diagram element
	 * 
	 * @return
	 */
	@Getter(value = TEXT)
	@XMLAttribute
	public String getText();

	/**
	 * Sets text of this diagram element
	 * 
	 * @param aName
	 */
	@Setter(value = TEXT)
	public void setText(String aText);

	/**
	 * Return parent of this diagram element
	 * 
	 * @return
	 */
	@Getter(value = PARENT)
	// We set here the CloningStrategy to IGNORE, otherwise clone will be added again to parent when cloning
	@CloningStrategy(StrategyType.IGNORE)
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

	public boolean hasChanged();

	public List<DiagramContainerElement<?>> getAncestors();

	/**
	 * Return {@link FlexoConceptInstance} where this {@link DiagramElement} is involved, asserting that this {@link DiagramElement} is
	 * contained in a {@link Diagram} which is the bound diagram of a {@link DiagramModelSlot} declared in {@link VirtualModel} of supplied
	 * {@link FMLRTVirtualModelInstance}
	 * 
	 * @param vmInstance
	 *            : instance of {@link VirtualModel} where is declared a {@link DiagramModelSlot}
	 * @return
	 */
	public FlexoConceptInstance getFlexoConceptInstance(FMLRTVirtualModelInstance vmInstance);

	/**
	 * Return {@link GraphicalElementRole} played by this {@link DiagramElement} in related {@link FlexoConceptInstance}, asserting that
	 * this {@link DiagramElement} is contained in a {@link Diagram} which is the bound diagram of a {@link DiagramModelSlot} declared in
	 * {@link VirtualModel} of supplied {@link FMLRTVirtualModelInstance}
	 * 
	 * @param vmInstance
	 *            : instance of {@link VirtualModel} where is declared a {@link DiagramModelSlot}
	 * @return
	 */
	public GraphicalElementRole<?, ?> getPatternRole(FMLRTVirtualModelInstance vmInstance);

}
