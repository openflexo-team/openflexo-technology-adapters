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

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * This abstract class is an action that allows to create or transform a {@link FlexoConcept} from a {@link DiagramShape}<br>
 * 
 * @author Sylvain, Vincent
 */
public class DeclareShapeInFlexoConcept extends DeclareDiagramElementInFlexoConcept<DeclareShapeInFlexoConcept, DiagramShape> {

	private static final Logger logger = Logger.getLogger(DeclareShapeInFlexoConcept.class.getPackage().getName());

	/**
	 * Create a new Flexo Action Type
	 */
	public static FlexoActionFactory<DeclareShapeInFlexoConcept, DiagramShape, DiagramElement<?>> actionType = new FlexoActionFactory<DeclareShapeInFlexoConcept, DiagramShape, DiagramElement<?>>(
			"declare_in_flexo_concept", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		@Override
		public DeclareShapeInFlexoConcept makeNewAction(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection,
				FlexoEditor editor) {
			return new DeclareShapeInFlexoConcept(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramShape shape, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramShape shape, Vector<DiagramElement<?>> globalSelection) {
			// TODO : implement the rights for modifying the viewpoint
			// ex: if(shape.getDiagramSpec.isEditable) ...

			return shape != null /*&& shape.getDiagramSpecification() != null*/;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeclareShapeInFlexoConcept.actionType, DiagramShape.class);
	}

	DeclareShapeInFlexoConcept(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
		getAvailableFlexoConceptCreationStrategies().add(new MapShapeToIndividualStrategy(this));
		getAvailableFlexoConceptCreationStrategies().add(new MapShapeToFlexoConceptlnstanceStrategy(this));
		getAvailableFlexoConceptCreationStrategies().add(new BlankFlexoConceptFromShapeCreationStrategy(this));
		getAvailableFlexoRoleCreationStrategies().add(new ShapeRoleCreationStrategy(this));
		getAvailableFlexoRoleSettingStrategies().add(new ShapeRoleSettingStrategy(this));
	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}

		return getSelectedEntriesCount() > 0;
	}

	@Override
	public FlexoConcept getFlexoConcept() {
		if (getPrimaryChoice() == DeclareInFlexoConceptChoices.CREATES_FLEXO_CONCEPT) {
			return getFlexoConceptCreationStrategy().getNewFlexoConcept();
		}
		return super.getFlexoConcept();
	}

}
