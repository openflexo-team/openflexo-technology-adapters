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

package org.openflexo.technologyadapter.diagram.fml.action;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.transformation.AbstractDeclareInFlexoConcept;
import org.openflexo.foundation.action.transformation.FlexoConceptCreationStrategy;
import org.openflexo.foundation.action.transformation.FlexoRoleCreationStrategy;
import org.openflexo.foundation.action.transformation.FlexoRoleSettingStrategy;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * This abstract class is a base action that allows to create or transform a {@link FlexoConcept} from a {@link DiagramElement}<br>
 * 3 kind of transformations are proposed from the selection of a {@link TechnologyObject}:
 * <ul>
 * <li>a {@link FlexoConceptCreationStrategy} which allows to create a new {@link FlexoConcept}</li>
 * <li>a {@link FlexoRoleCreationStrategy} which allows to create a new {@link FlexoRole} in existing {@link FlexoConcept}</li>
 * <li>a {@link FlexoRoleSettingStrategy} which allows to set an existing {@link FlexoRole} in existing {@link FlexoConcept}</li>
 * </ul>
 * Some strategies should be implemented for each of that choices, defined as primary choices<br>
 * 
 * Note that to be valid, this class must be externally set with {@link VirtualModelResource}
 * 
 * @author Sylvain, Vincent
 * 
 * @param <A>
 * @param <T1>
 */
public abstract class DeclareDiagramElementInFlexoConcept<A extends DeclareDiagramElementInFlexoConcept<A, T>, T extends DiagramElement<?>>
		extends AbstractDeclareInFlexoConcept<A, T, DiagramElement<?>> {
	/**
	 * Stores the model slot which encodes the access to a {@link Diagram} conform to a {@link DiagramSpecification}, in the context of a
	 * {@link VirtualModel} (which is a context where a diagram is federated with other sources of informations)
	 */
	private TypedDiagramModelSlot diagramModelSlot;

	/**
	 * Constructor for this class
	 * 
	 * @param actionType
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	DeclareDiagramElementInFlexoConcept(FlexoActionFactory actionType, T focusedObject, Vector<DiagramElement<?>> globalSelection,
			FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);

		// Get the set of internal elements inside the current focused object
		drawingObjectEntries = new Vector<>();
		int shapeIndex = 1;
		int connectorIndex = 1;

		List<? extends DiagramElement<?>> elements = (getFocusedObject() instanceof DiagramContainerElement
				? ((DiagramContainerElement<?>) getFocusedObject()).getDescendants()
				: Collections.singletonList(getFocusedObject()));

		for (DiagramElement<?> o : elements) {
			if (o instanceof DiagramShape) {
				DiagramShape shape = (DiagramShape) o;
				String shapeRoleName = "shape" + (shapeIndex > 1 ? shapeIndex : "");
				drawingObjectEntries.add(new DrawingObjectEntry(shape, shapeRoleName));
				shapeIndex++;
			}
			if (o instanceof DiagramConnector) {
				DiagramConnector connector = (DiagramConnector) o;
				String connectorRoleName = "connector" + (connectorIndex > 1 ? connectorIndex : "");
				drawingObjectEntries.add(new DrawingObjectEntry(connector, connectorRoleName));
				connectorIndex++;
			}
		}
	}

	/**
	 * Return the model slot which encodes the access to a {@link Diagram} conform to a {@link DiagramSpecification}, in the context of a
	 * {@link VirtualModel} (which is a context where a diagram is federated with other sources of informations)
	 * 
	 * @return
	 */
	public TypedDiagramModelSlot getDiagramModelSlot() {
		if (diagramModelSlot == null && getVirtualModel() != null
				&& getVirtualModel().getModelSlots(TypedDiagramModelSlot.class).size() > 0) {
			diagramModelSlot = getVirtualModel().getModelSlots(TypedDiagramModelSlot.class).get(0);
		}
		return diagramModelSlot;
	}

	/**
	 * Sets the model slot which encodes the access to a {@link Diagram} conform to a {@link DiagramSpecification}, in the context of a
	 * {@link VirtualModel} (which is a context where a diagram is federated with other sources of informations)
	 * 
	 * @return
	 */
	public void setDiagramModelSlot(TypedDiagramModelSlot diagramModelSlot) {
		this.diagramModelSlot = diagramModelSlot;
	}

	// public abstract GraphicalElementRole<T, ?> getFlexoRole();

	// public abstract List<? extends GraphicalElementRole<T, ?>> getAvailableFlexoRoles();

	private final List<DrawingObjectEntry> drawingObjectEntries;

	public List<DrawingObjectEntry> getDrawingObjectEntries() {
		return drawingObjectEntries;
	}

	public int getSelectedEntriesCount() {
		int returned = 0;
		for (DrawingObjectEntry e : drawingObjectEntries) {
			if (e.selectThis) {
				returned++;
			}
		}
		return returned;
	}

	public DrawingObjectEntry getEntry(DiagramElement<?> o) {
		for (DrawingObjectEntry e : drawingObjectEntries) {
			if (e.graphicalObject == o) {
				return e;
			}
		}
		return null;
	}

	public DiagramSpecification getDiagramSpecification() {
		return getFocusedObject().getDiagram().getDiagramSpecification();
	}

	public TypedDiagramModelSlot getTypedDiagramModelSlot() {
		if (getVirtualModel().getModelSlots(TypedDiagramModelSlot.class) != null
				&& getVirtualModel().getModelSlots(TypedDiagramModelSlot.class).size() > 0) {
			return getVirtualModel().getModelSlots(TypedDiagramModelSlot.class).get(0);
		}
		DiagramTechnologyAdapter diagramTechnologyAdapter = getEditor().getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);
		TypedDiagramModelSlot typedDiagramModelSlot = diagramTechnologyAdapter.makeModelSlot(TypedDiagramModelSlot.class,
				getVirtualModel());
		typedDiagramModelSlot.setName("typedDiagramModelSlot");
		// ((TypeAwareModelSlot) newModelSlot).setMetaModelResource(mmRes);
		getVirtualModel().addToModelSlots(typedDiagramModelSlot);
		return typedDiagramModelSlot;
	}

	public class DrawingObjectEntry {
		private boolean selectThis;
		public DiagramElement<?> graphicalObject;
		public String flexoRoleName;

		public DrawingObjectEntry(DiagramElement<?> graphicalObject, String flexoRoleName) {
			super();
			this.graphicalObject = graphicalObject;
			this.flexoRoleName = flexoRoleName;
			this.selectThis = isMainEntry();
		}

		public boolean isMainEntry() {
			return graphicalObject == getFocusedObject();
		}

		public boolean getSelectThis() {
			if (isMainEntry()) {
				return true;
			}
			return selectThis;
		}

		public void setSelectThis(boolean aFlag) {
			if (!isMainEntry()) {
				selectThis = aFlag;
			}
		}

		public DrawingObjectEntry getParentEntry() {
			return getEntry(graphicalObject.getParent());
		}
	}

}
