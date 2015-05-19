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

import java.util.logging.Logger;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.connectors.ConnectorSpecification.ConnectorType;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.PamelaResourceModelFactory;
import org.openflexo.foundation.action.FlexoUndoManager;
import org.openflexo.foundation.resource.PamelaResourceImpl.IgnoreLoadingEdits;
import org.openflexo.model.converter.RelativePathResourceConverter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;

/**
 * Diagram factory<br>
 * One instance of this class should be used for each DiagramResource
 * 
 * @author sylvain
 * 
 */
public class DiagramFactory extends FGEModelFactoryImpl implements PamelaResourceModelFactory<DiagramResource> {

	private static final Logger logger = Logger.getLogger(DiagramFactory.class.getPackage().getName());

	private final DiagramResource resource;
	private IgnoreLoadingEdits ignoreHandler = null;
	private FlexoUndoManager undoManager = null;

	public DiagramFactory(DiagramResource resource, EditingContext editingContext) throws ModelDefinitionException {
		super(Diagram.class, DiagramShape.class, DiagramConnector.class);
		this.resource = resource;
		setEditingContext(editingContext);
		if(resource!=null){
			addConverter(new RelativePathResourceConverter(resource.getFlexoIODelegate().getParentPath()));
		}
	}

	@Override
	public DiagramResource getResource() {
		return resource;
	}

	public Diagram makeNewDiagram() {
		return newInstance(Diagram.class);
	}

	public Diagram makeNewDiagram(DiagramSpecification diagramSpecification) {
		Diagram returned = newInstance(Diagram.class);
		returned.setDiagramSpecification(diagramSpecification);
		return returned;
	}

	public DiagramShape makeNewShape(String name, ShapeGraphicalRepresentation gr, DiagramContainerElement<?> container) {
		if (gr == null) {
			gr = makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
		}
		DiagramShape returned = newInstance(DiagramShape.class);
		returned.setGraphicalRepresentation(gr);
		returned.setName(name);
		container.addToShapes(returned);
		return returned;
	}

	public DiagramShape makeNewShape(String name, ShapeType shapeType, FGEPoint fgePoint, DiagramContainerElement<?> container) {
		ShapeGraphicalRepresentation gr = makeShapeGraphicalRepresentation(shapeType);
		if (fgePoint != null) {
			gr.setX(fgePoint.getX());
			gr.setY(fgePoint.getY());
		}
		DiagramShape returned = newInstance(DiagramShape.class);
		returned.setGraphicalRepresentation(gr);
		returned.setName(name);
		container.addToShapes(returned);
		return returned;
	}

	public DiagramShape makeNewShape(String name, DiagramContainerElement<?> container) {
		return makeNewShape(name, ShapeType.RECTANGLE, null, container);
	}

	public DiagramConnector makeNewConnector(String name, DiagramShape startShape, DiagramShape endShape,
			DiagramContainerElement<?> container) {
		ConnectorGraphicalRepresentation gr = makeConnectorGraphicalRepresentation(ConnectorType.LINE);
		DiagramConnector returned = newInstance(DiagramConnector.class);
		returned.setGraphicalRepresentation(gr);
		returned.setName(name);
		returned.setStartShape(startShape);
		returned.setEndShape(endShape);
		container.addToConnectors(returned);
		return returned;
	}

	@Override
	public synchronized void startDeserializing() {
		EditingContext editingContext = getResource().getServiceManager().getEditingContext();

		if (editingContext != null && editingContext.getUndoManager() instanceof FlexoUndoManager) {
			undoManager = (FlexoUndoManager) editingContext.getUndoManager();
			undoManager.addToIgnoreHandlers(ignoreHandler = new IgnoreLoadingEdits(resource));
			// System.out.println("@@@@@@@@@@@@@@@@ START LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public synchronized void stopDeserializing() {
		if (ignoreHandler != null) {
			undoManager.removeFromIgnoreHandlers(ignoreHandler);
			// System.out.println("@@@@@@@@@@@@@@@@ END LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public <I> void objectHasBeenDeserialized(I newlyCreatedObject, Class<I> implementedInterface) {
		super.objectHasBeenDeserialized(newlyCreatedObject, implementedInterface);
		if (newlyCreatedObject instanceof FlexoObject) {
			if (getResource() != null) {
				getResource().setLastID(((FlexoObject) newlyCreatedObject).getFlexoID());
			} else {
				logger.warning("Could not access resource beeing deserialized");
			}
		}
	}

}
