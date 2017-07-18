/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Fml-rt-technologyadapter-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.controller.action;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareDiagramElementInFlexoConcept;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareDiagramElementInFlexoConcept.DrawingObjectEntry;
import org.openflexo.technologyadapter.diagram.fml.action.FlexoConceptFromDiagramElementCreationStrategy;
import org.openflexo.technologyadapter.diagram.fml.action.GraphicalElementRoleCreationStrategy;
import org.openflexo.technologyadapter.diagram.fml.action.GraphicalElementRoleSettingStrategy;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.action.AbstractDeclareInFlexoConceptWizard;

public abstract class AbstractDeclareDiagramElementInFlexoConceptWizard<A extends DeclareDiagramElementInFlexoConcept<A, ?>>
		extends AbstractDeclareInFlexoConceptWizard<A> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AbstractDeclareDiagramElementInFlexoConceptWizard.class.getPackage().getName());

	public AbstractDeclareDiagramElementInFlexoConceptWizard(A action, FlexoController controller) {
		super(action, controller);
	}

	public abstract class ConfigureCreateNewFlexoConceptFromDiagramElementStep<S extends FlexoConceptFromDiagramElementCreationStrategy<A>>
			extends TransformationConfigurationStep<S> {

		public ConfigureCreateNewFlexoConceptFromDiagramElementStep(S strategy) {
			super(strategy);
		}

		public FlexoMetaModel<?> getAdressedFlexoMetaModel() {
			return getAction().getAdressedFlexoMetaModel();
		}

		public VirtualModel getAdressedVirtualModel() {
			return getAction().getAdressedVirtualModel();
		}

		public ModelSlot<?> getModelSlot() {
			return getAction().getInformationSourceModelSlot();
		}

		public void setModelSlot(ModelSlot<?> modelSlot) {
			if (modelSlot != getModelSlot()) {
				ModelSlot<?> oldValue = getModelSlot();
				getAction().setInformationSourceModelSlot(modelSlot);
				getPropertyChangeSupport().firePropertyChange("modelSlot", oldValue, modelSlot);
				getPropertyChangeSupport().firePropertyChange("adressedFlexoMetaModel", null, getAdressedFlexoMetaModel());
				getPropertyChangeSupport().firePropertyChange("adressedVirtualModel", null, getAdressedVirtualModel());
				checkValidity();
			}
		}

	}

	public abstract class ConfigureGraphicalElementRoleCreationStrategyStep<S extends GraphicalElementRoleCreationStrategy<A, ?, ?, ?>>
			extends TransformationConfigurationStep<S> {

		public ConfigureGraphicalElementRoleCreationStrategyStep(S strategy) {
			super(strategy);
		}

		public VirtualModel getVirtualModel() {
			return getAction().getVirtualModel();
		}

	}

	public abstract class ConfigureGraphicalElementRoleSettingStrategyStep<S extends GraphicalElementRoleSettingStrategy<A, ?, ?, ?>>
			extends TransformationConfigurationStep<S> {

		public ConfigureGraphicalElementRoleSettingStrategyStep(S strategy) {
			super(strategy);
		}

		public VirtualModel getVirtualModel() {
			return getAction().getVirtualModel();
		}

	}

	@Override
	public ChooseNewConceptCreationStrategy chooseNewConceptCreationStrategy() {
		return new ChooseNewConceptCreationStrategy();
	}

	/**
	 * This step is used to select new concept creation strategy
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/ChooseCreationStrategy.fib")
	public class ChooseNewConceptCreationStrategy extends AbstractChooseNewConceptCreationStrategy {

		public TypedDiagramModelSlot getDiagramModelSlot() {
			return getAction().getDiagramModelSlot();
		}

		public void setDiagramModelSlot(TypedDiagramModelSlot modelSlot) {
			if (modelSlot != getDiagramModelSlot()) {
				TypedDiagramModelSlot oldValue = getDiagramModelSlot();
				getAction().setDiagramModelSlot(modelSlot);
				getPropertyChangeSupport().firePropertyChange("diagramModelSlot", oldValue, modelSlot);
				checkValidity();
			}
		}

		public List<TypedDiagramModelSlot> getAvailableDiagramModelSlots() {
			return getAction().getVirtualModel().getModelSlots(TypedDiagramModelSlot.class);
		}

		public List<DrawingObjectEntry> getDrawingObjectEntries() {
			return (List) getAction().getDrawingObjectEntries();
		}

		@Override
		public boolean isValid() {
			if (getDiagramModelSlot() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_choose_a_diagram_model_slot"), IssueMessageType.ERROR);
				return false;
			}
			return super.isValid();
		}

	}

}
