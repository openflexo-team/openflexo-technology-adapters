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

package org.openflexo.technologyadapter.diagram.fml.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.DeletionScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.DeletionSchemeAction;
import org.openflexo.foundation.fml.rt.action.DeletionSchemeActionFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * Delete action beeing applied on an heterogeneous selection in a FML-controlled diagram.<br>
 * Selected elements might be either DiagramElements and/or FMLControlledDiagramElements (in this case, this is the
 * {@link FlexoConceptInstance} which should be deleted, using a {@link DeletionScheme})
 * 
 * @author vincent, sylvain
 *
 */
public class DeleteDiagramElementsAndFlexoConceptInstances
		extends FlexoAction<DeleteDiagramElementsAndFlexoConceptInstances, FlexoObject, FlexoObject> {

	private static final Logger logger = Logger.getLogger(DeleteDiagramElementsAndFlexoConceptInstances.class.getPackage().getName());

	public static FlexoActionFactory<DeleteDiagramElementsAndFlexoConceptInstances, FlexoObject, FlexoObject> actionType = new FlexoActionFactory<DeleteDiagramElementsAndFlexoConceptInstances, FlexoObject, FlexoObject>(
			"delete", FlexoActionFactory.editGroup, FlexoActionFactory.DELETE_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DeleteDiagramElementsAndFlexoConceptInstances makeNewAction(FlexoObject focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new DeleteDiagramElementsAndFlexoConceptInstances(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoObject focusedObject, Vector<FlexoObject> globalSelection) {
			if (isHomogeneousFlexoConceptInstanceSelection(focusedObject, globalSelection)) {
				return false;
			}
			return focusedObject instanceof DiagramElement || focusedObject instanceof FlexoConceptInstance;
		}

		@Override
		public boolean isEnabledForSelection(FlexoObject focusedObject, Vector<FlexoObject> globalSelection) {
			if (isHomogeneousFlexoConceptInstanceSelection(focusedObject, globalSelection)) {
				return false;
			}
			return deleteableSelection(focusedObject, globalSelection);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeleteDiagramElementsAndFlexoConceptInstances.actionType, DiagramElement.class);
		FlexoObjectImpl.addActionForClass(DeleteDiagramElementsAndFlexoConceptInstances.actionType, FlexoConceptInstance.class);
	}

	// Evaluate if the selection contains something deleteable
	protected static boolean deleteableSelection(FlexoObject focusedObject, Vector<FlexoObject> globalSelection) {
		// A diagram element is deleteable
		if (deleteableSelected(focusedObject)) {
			return true;
		}
		else {
			for (FlexoObject object : globalSelection) {
				if (deleteableSelected(object)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean deleteableSelected(FlexoObject focusedObject) {
		if (focusedObject instanceof DiagramElement) {
			return true;
		}
		else if (focusedObject instanceof FlexoConceptInstance) {
			// A flexo concept instance is deleatable if only it contains a deletion scheme
			FlexoConceptInstance fci = (FlexoConceptInstance) focusedObject;
			if (!fci.getFlexoConcept().getDeletionSchemes().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	// private HashMap<FlexoConceptInstance, DeletionScheme> selectedFlexoConceptInstanceDeletionSchemes;
	public boolean removePendingConnectors = true;
	private List<FlexoConceptInstanceElementEntry> selectedFlexoConceptInstanceEntries;
	private List<DiagramElement<?>> selectedDiagramElements;
	private List<FlexoObject> allObjectsToBeDeleted;

	protected DeleteDiagramElementsAndFlexoConceptInstances(FlexoObject focusedObject, Vector<FlexoObject> globalSelection,
			FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) {

		logger.info("Delete Flexo Concept Instances");

		// First delete Flexo Concept Instance via their selected deletion scheme
		for (FlexoConceptInstanceElementEntry fciEntry : getFlexoConceptInstancesToBeDeleted()) {
			if (fciEntry.getCurrentDeletionScheme() != null) {
				VirtualModelInstance<?, ?> vmi = fciEntry.getFlexoConceptInstance().getVirtualModelInstance();
				DeletionSchemeActionFactory deletionSchemeActionFactory = new DeletionSchemeActionFactory(fciEntry.currentDeletionScheme,
						fciEntry.getFlexoConceptInstance());
				DeletionSchemeAction deletionSchemeAction = deletionSchemeActionFactory
						.makeNewEmbeddedAction(fciEntry.getFlexoConceptInstance(), null, this);
				// deletionSchemeAction.setVirtualModelInstance(vmi);
				// deletionSchemeAction.setDeletionScheme(fciEntry.currentDeletionScheme);
				deletionSchemeAction.doAction();
				vmi.removeFromFlexoConceptInstances(fciEntry.getFlexoConceptInstance());
				fciEntry.getFlexoConceptInstance().delete(context);
			}
		}

		logger.info("Delete remaining diagram connectors");

		// A list of connectors that may be deleted if a shape is connected to it
		/*List<DiagramConnector> impliedConnectors = new ArrayList<DiagramConnector>();
		for (DiagramElement<?> o : getDiagramElementsToBeDeleted()) {
			if (o instanceof DiagramShape) {
				impliedConnectors.addAll(((DiagramShape) o).getStartConnectors());
				impliedConnectors.addAll(((DiagramShape) o).getEndConnectors());
			}
		}
		// Delete these connectors
		for (Iterator<DiagramConnector> connectors = impliedConnectors.iterator(); connectors.hasNext();) {
			DiagramConnector connector = connectors.next();
			if (!connector.isDeleted()) {
				logger.info("Try to delete undeleted DiagramConnector " + connector);
				connector.delete();
				logger.info("DiagramConnector " + connector + " has been successfully deleted");
			} else {
				logger.info("DiagramConnector " + connector + " has already been successfully deleted");
			}
		}*/

		logger.info("Delete remaining diagram shapes");

		for (DiagramElement<?> diagramElt : getDiagramElementsToBeDeleted()) {
			if (!diagramElt.isDeleted()) {
				logger.info("Delete undeleted DiagramElement " + diagramElt);
				diagramElt.delete();
			}
			else {
				logger.info("DiagramElement " + diagramElt + " has been successfully deleted");
			}
		}
	}

	// TODO
	public List<FlexoConceptInstanceElementEntry> getFlexoConceptInstancesToBeDeleted() {
		return getSelectedFlexoConceptInstanceEntries();
	}

	private List<FlexoConceptInstanceElementEntry> getSelectedFlexoConceptInstanceEntries() {
		if (selectedFlexoConceptInstanceEntries == null) {
			initSelectedFlexoConceptInstanceEntries();
		}
		return selectedFlexoConceptInstanceEntries;
	}

	private void initSelectedFlexoConceptInstanceEntries() {
		selectedFlexoConceptInstanceEntries = new ArrayList<>();
		for (FlexoObject object : getGlobalSelectionAndFocusedObject()) {
			if (object instanceof FlexoConceptInstance) {
				selectedFlexoConceptInstanceEntries.add(new FlexoConceptInstanceElementEntry((FlexoConceptInstance) object));
			}
		}
	}

	// TODO
	public List<DiagramElement<?>> getDiagramElementsToBeDeleted() {
		// A list of connectors that may be deleted if a shape is connected to it
		List<DiagramConnector> impliedConnectors = new ArrayList<>();
		for (DiagramElement<?> o : getSelectedDiagramElements()) {
			if (o instanceof DiagramShape) {
				impliedConnectors.addAll(((DiagramShape) o).getStartConnectors());
				impliedConnectors.addAll(((DiagramShape) o).getEndConnectors());
			}
		}
		getSelectedDiagramElements().addAll(impliedConnectors);
		return getSelectedDiagramElements();
	}

	private List<DiagramElement<?>> getSelectedDiagramElements() {
		if (selectedDiagramElements == null) {
			initSelectedDiagramElements();
		}
		return selectedDiagramElements;
	}

	private void initSelectedDiagramElements() {
		selectedDiagramElements = new ArrayList<>();
		for (FlexoObject object : getGlobalSelectionAndFocusedObject()) {
			if (object instanceof DiagramElement) {
				selectedDiagramElements.add((DiagramElement<?>) object);
			}
		}
	}

	public List<FlexoObject> getAllObjectsToBeDeleted() {
		if (allObjectsToBeDeleted == null) {
			allObjectsToBeDeleted = new ArrayList<>();
			computeAllObjectsToBeDeleted();
		}
		return allObjectsToBeDeleted;
	}

	public void setAllObjectsToBeDeleted(List<FlexoObject> allObjectsToBeDeleted) {
		this.allObjectsToBeDeleted = allObjectsToBeDeleted;
	}

	private void computeAllObjectsToBeDeleted() {
		allObjectsToBeDeleted.clear();
		for (FlexoConceptInstanceElementEntry fci : getFlexoConceptInstancesToBeDeleted()) {
			allObjectsToBeDeleted.addAll(fci.getDeletedElements());
			allObjectsToBeDeleted.add(fci.getFlexoConceptInstance());
		}
		for (DiagramElement<?> de : getDiagramElementsToBeDeleted()) {
			if (!allObjectsToBeDeleted.contains(de)) {
				allObjectsToBeDeleted.add(de);
			}
		}
		getPropertyChangeSupport().firePropertyChange("allObjectsToBeDeleted", null, null);
	}

	// TODO
	public void selectAll() {
	}

	// TODO
	public void selectNone() {
	}

	public class FlexoConceptInstanceElementEntry {
		private DeletionScheme currentDeletionScheme;
		private final FlexoConceptInstance flexoConceptInstance;
		private final List<FlexoObject> deletedElements;

		public FlexoConceptInstanceElementEntry(FlexoConceptInstance flexoConceptInstance) {
			this.flexoConceptInstance = flexoConceptInstance;
			deletedElements = new ArrayList<>();
			if (!flexoConceptInstance.getFlexoConcept().getDeletionSchemes().isEmpty()) {
				this.currentDeletionScheme = flexoConceptInstance.getFlexoConcept().getDeletionSchemes().get(0);
				computeDeletedElements();
			}
		}

		public DeletionScheme getCurrentDeletionScheme() {
			return currentDeletionScheme;
		}

		public void setCurrentDeletionScheme(DeletionScheme currentDeletionScheme) {
			this.currentDeletionScheme = currentDeletionScheme;
			computeDeletedElements();
			update();
		}

		public FlexoConceptInstance getFlexoConceptInstance() {
			return flexoConceptInstance;
		}

		public List<DeletionScheme> getAvailableDeletionSchemes() {
			return flexoConceptInstance.getFlexoConcept().getDeletionSchemes();
		}

		public FlexoConcept getFlexoConcept() {
			return getFlexoConceptInstance().getFlexoConcept();
		}

		public String getName() {
			return getFlexoConceptInstance().getStringRepresentation();
		}

		public List<FlexoObject> getDeletedElements() {
			return deletedElements;
		}

		private void computeDeletedElements() {
			/*try {
				deletedElements.clear();
				if (currentDeletionScheme != null && currentDeletionScheme.getActions() != null) {
					for (EditionAction ea : currentDeletionScheme.getActions()) {
						if (ea instanceof DeleteAction) {
							DeleteAction deleteAction = (DeleteAction) ea;
							FlexoObject object = (FlexoObject) deleteAction.getObject().getBindingValue(flexoConceptInstance);
							deletedElements.add(object);
						}
					}
				}
			} catch (TypeMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullReferenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}

		private void update() {
			DeleteDiagramElementsAndFlexoConceptInstances.this.computeAllObjectsToBeDeleted();
		}
	}
}
