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

import org.openflexo.diana.ConnectorGraphicalRepresentation;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;

@ModelEntity
@ImplementationClass(DiagramConnectorImpl.class)
@XMLElement(xmlTag = "Connector")
public interface DiagramConnector extends DiagramElement<ConnectorGraphicalRepresentation> {

	public static final String START_SHAPE = "startShape";
	public static final String END_SHAPE = "endShape";

	/**
	 * Returns the start shape of this connector
	 * 
	 * @return
	 */
	@Getter(value = START_SHAPE, inverse = DiagramShape.START_CONNECTORS)
	@XMLElement(context = "Start")
	@CloningStrategy(StrategyType.CLONE)
	public DiagramShape getStartShape();

	/**
	 * Sets the start shape of this connector
	 * 
	 * @return
	 */
	@Setter(START_SHAPE)
	public void setStartShape(DiagramShape startShape);

	/**
	 * Returns the end shape of this connector
	 * 
	 * @return
	 */
	@Getter(value = END_SHAPE, inverse = DiagramShape.END_CONNECTORS)
	@XMLElement(context = "End")
	@CloningStrategy(StrategyType.CLONE)
	public DiagramShape getEndShape();

	/**
	 * Sets the end shape of this connector
	 * 
	 * @return
	 */
	@Setter(END_SHAPE)
	public void setEndShape(DiagramShape endShape);

	@Override
	@Getter(value = PARENT, inverse = DiagramContainerElement.CONNECTORS)
	public DiagramContainerElement<?> getParent();

	@Override
	public ConnectorRole getPatternRole(FMLRTVirtualModelInstance vmInstance);

}
