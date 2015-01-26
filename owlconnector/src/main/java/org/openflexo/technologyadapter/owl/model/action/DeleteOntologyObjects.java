/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl.model.action;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.ontology.FlexoOntologyObjectImpl;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;

public class DeleteOntologyObjects extends FlexoAction<DeleteOntologyObjects, OWLConcept, OWLConcept> {

	private static final Logger logger = Logger.getLogger(DeleteOntologyObjects.class.getPackage().getName());

	public static FlexoActionType<DeleteOntologyObjects, OWLConcept, OWLConcept> actionType = new FlexoActionType<DeleteOntologyObjects, OWLConcept, OWLConcept>(
			"delete", FlexoActionType.editGroup, FlexoActionType.DELETE_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DeleteOntologyObjects makeNewAction(OWLConcept focusedObject, Vector<OWLConcept> globalSelection, FlexoEditor editor) {
			return new DeleteOntologyObjects(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(OWLConcept object, Vector<OWLConcept> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(OWLConcept focusedObject, Vector<OWLConcept> globalSelection) {
			Vector<OWLConcept> objectsToDelete = objectsToDelete(focusedObject, globalSelection);
			if (objectsToDelete.size() == 0) {
				return false;
			}
			/*for (IFlexoOntologyConcept o : globalSelection) {
				if (o.getIsReadOnly()) {
					return false;
				}
			}*/
			return true;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeleteOntologyObjects.actionType, OWLConcept.class);
	}

	protected static Vector<OWLConcept> objectsToDelete(OWLConcept focusedObject, Vector<OWLConcept> globalSelection) {
		Vector<OWLConcept> returned = new Vector<OWLConcept>();
		if (globalSelection == null || !globalSelection.contains(focusedObject)) {
			returned.add(focusedObject);
		}
		if (globalSelection != null) {
			for (FlexoOntologyObjectImpl o : globalSelection) {
				if (o instanceof OWLClass || o instanceof OWLIndividual || o instanceof OWLObjectProperty
						|| o instanceof IFlexoOntologyDataProperty) {
					returned.add((OWLConcept) o);
				}
			}
		}
		return returned;
	}

	protected DeleteOntologyObjects(OWLConcept focusedObject, Vector<OWLConcept> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
		logger.info("Created DeleteOntologyObjects action focusedObject=" + focusedObject + "globalSelection=" + globalSelection);
	}

	@Override
	protected void doAction(Object context) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("DeleteOntologyObjects");
		}
		if (logger.isLoggable(Level.INFO)) {
			logger.info("selection is: " + getGlobalSelection());
		}
		if (logger.isLoggable(Level.INFO)) {
			logger.info("selection to delete is: " + getObjectsToDelete());
		}
		for (OWLConcept o : getObjectsToDelete()) {
			o.delete();
		}
	}

	private Vector<OWLConcept> _objectsToDelete;

	/**
	 * This method returns all the objects on which the delete method needs to be called. This should not be done by some other code than
	 * the one located in the doAction method. This method can be used in either initialiazer or finalizer of the action.
	 * 
	 * @return All the objects to be deleted.
	 */
	public Vector<OWLConcept> getObjectsToDelete() {
		if (_objectsToDelete == null) {
			_objectsToDelete = objectsToDelete(getFocusedObject(), getGlobalSelection());
		}
		return _objectsToDelete;
	}

}
