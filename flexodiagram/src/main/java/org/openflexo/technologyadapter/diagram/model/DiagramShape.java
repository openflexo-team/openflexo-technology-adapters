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

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.resource.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;

/**
 * Represents a Shape in Openflexo build-in diagram technology<br>
 * A shape may be a container of other shapes and connectors
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(DiagramShapeImpl.class)
@XMLElement(xmlTag = "Shape")
public interface DiagramShape extends DiagramContainerElement<ShapeGraphicalRepresentation> {

	public static final String START_CONNECTORS = "start_connectors";
	public static final String END_CONNECTORS = "end_connectors";

	@Override
	@Getter(value = PARENT, inverse = DiagramContainerElement.SHAPES)
	public DiagramContainerElement<?> getParent();

	@Override
	public ShapeRole getPatternRole(VirtualModelInstance vmInstance);

	@Getter(value = START_CONNECTORS, cardinality = Cardinality.LIST, inverse = DiagramConnector.START_SHAPE)
	@CloningStrategy(StrategyType.IGNORE)
	public List<DiagramConnector> getStartConnectors();

	@Setter(START_CONNECTORS)
	public void setStartConnectors(List<DiagramConnector> someConnectors);

	@Adder(START_CONNECTORS)
	public void addToStartConnectors(DiagramConnector aConnector);

	@Remover(START_CONNECTORS)
	public void removeFromStartConnectors(DiagramConnector aConnector);

	@Getter(value = END_CONNECTORS, cardinality = Cardinality.LIST, inverse = DiagramConnector.END_SHAPE)
	@CloningStrategy(StrategyType.IGNORE)
	public List<DiagramConnector> getEndConnectors();

	@Setter(END_CONNECTORS)
	public void setEndConnectors(List<DiagramConnector> someConnectors);

	@Adder(END_CONNECTORS)
	public void addToEndConnectors(DiagramConnector aConnector);

	@Remover(END_CONNECTORS)
	public void removeFromEndConnectors(DiagramConnector aConnector);
	
	public ScreenshotImage<DiagramShape> getScreenshotImage();

}