/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.technologyadapter.diagram;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoBehaviours;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DiagramNavigationScheme;
import org.openflexo.technologyadapter.diagram.fml.DiagramRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.fml.editionaction.CreateDiagram;
import org.openflexo.technologyadapter.diagram.fml.editionaction.GraphicalAction;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * Implementation of the ModelSlot class for the Openflexo built-in diagram technology adapter<br>
 * 
 * We modelize here the access to a free {@link Diagram} (no conformance to any {@link DiagramSpecification}).
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ ShapeRole.class, ConnectorRole.class, DiagramRole.class })
@DeclareFlexoBehaviours({ DropScheme.class, LinkScheme.class, DiagramNavigationScheme.class })
@DeclareEditionActions({ CreateDiagram.class, AddShape.class, AddConnector.class, GraphicalAction.class })
@DeclareFetchRequests({})
@ModelEntity
@ImplementationClass(FreeDiagramModelSlot.FreeDiagramModelSlotImpl.class)
@XMLElement
@FML("FreeDiagramModelSlot")
public interface FreeDiagramModelSlot extends FreeModelSlot<Diagram>, DiagramModelSlot {

	public abstract class FreeDiagramModelSlotImpl extends FreeModelSlotImpl<Diagram> implements FreeDiagramModelSlot {

		private static final Logger logger = Logger.getLogger(FreeDiagramModelSlot.class.getPackage().getName());

		@Override
		public String getStringRepresentation() {
			return "FreeDiagramModelSlot";
		}

		@Override
		public Class<DiagramTechnologyAdapter> getTechnologyAdapterClass() {
			return DiagramTechnologyAdapter.class;
		}

		@Override
		public DiagramTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (DiagramTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public boolean getIsRequired() {
			return true;
		}

		@Override
		public String getURIForObject(FreeModelSlotInstance<Diagram, ? extends FreeModelSlot<Diagram>> msInstance, Object o) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(FreeModelSlotInstance<Diagram, ? extends FreeModelSlot<Diagram>> msInstance, String objectURI) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Type getType() {
			// TODO Auto-generated method stub
			return null;
		}

		/**
		 * Overrides super implementation by providing default graphical representations
		 */
		/*
		@Override
		public <PR extends FlexoRole<?>> PR makeFlexoRole(Class<PR> flexoRoleClass) {
			PR returned = super.makeFlexoRole(flexoRoleClass);
			if (ShapeRole.class.isAssignableFrom(flexoRoleClass)) {
				ShapeRole shapeRole = (ShapeRole) returned;
				ShapeGraphicalRepresentation gr = getFMLModelFactory().makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
				gr.setWidth(80);
				gr.setHeight(50);
				gr.setX(20);
				gr.setY(20);
				gr.setIsFloatingLabel(false);
				shapeRole.setGraphicalRepresentation(gr);
			}
			if (ConnectorRole.class.isAssignableFrom(flexoRoleClass)) {
				ConnectorRole connectorRole = (ConnectorRole) returned;
				connectorRole.setGraphicalRepresentation(getFMLModelFactory().makeConnectorGraphicalRepresentation(ConnectorType.LINE));
			}
			return returned;
		}
		*/
	}

}
