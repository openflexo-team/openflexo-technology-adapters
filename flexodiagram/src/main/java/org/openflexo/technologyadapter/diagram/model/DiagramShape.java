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
import org.openflexo.fge.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
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
	public ShapeRole getPatternRole(FMLRTVirtualModelInstance vmInstance);

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
