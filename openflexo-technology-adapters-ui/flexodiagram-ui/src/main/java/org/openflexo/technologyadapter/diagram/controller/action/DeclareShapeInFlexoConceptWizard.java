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

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fib.annotation.FIBPanel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareInFlexoConcept.DeclareInFlexoConceptChoices;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareShapeInFlexoConcept;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.view.controller.FlexoController;

public class DeclareShapeInFlexoConceptWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DeclareShapeInFlexoConceptWizard.class.getPackage().getName());

	private final DeclareShapeInFlexoConcept action;

	private final ChooseOption chooseOption;

	public DeclareShapeInFlexoConceptWizard(DeclareShapeInFlexoConcept action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(chooseOption = new ChooseOption());
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("declare_shape_in_flexo_concept");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMLIconLibrary.FLEXO_CONCEPT_MEDIUM_ICON, DiagramIconLibrary.SHAPE_MARKER).getImage();
	}

	/**
	 * This step is used to select option
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/ChooseOption.fib")
	public class ChooseOption extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public DeclareShapeInFlexoConcept getAction() {
			return action;
		}

		@Override
		public String getTitle() {
			return FlexoLocalization.localizedForKey("choose_an_option");
		}

		@Override
		public boolean isTransitionalStep() {
			return true;
		}

		@Override
		public boolean isValid() {

			if (getPrimaryChoice() == null) {
				setIssueMessage(FlexoLocalization.localizedForKey("please_choose_an_option"), IssueMessageType.ERROR);
				return false;
			} else if (getVirtualModelResource() == null) {
				setIssueMessage(FlexoLocalization.localizedForKey("please_select_a_virtual_model"), IssueMessageType.ERROR);
				return false;
			}
			return true;
		}

		public DeclareInFlexoConceptChoices getPrimaryChoice() {
			return action.getPrimaryChoice();
		}

		public void setPrimaryChoice(DeclareInFlexoConceptChoices choice) {
			if (choice != getPrimaryChoice()) {
				DeclareInFlexoConceptChoices oldValue = getPrimaryChoice();
				action.setPrimaryChoice(choice);
				getPropertyChangeSupport().firePropertyChange("primaryChoice", oldValue, choice);
				checkValidity();
			}
		}

		public VirtualModelResource getVirtualModelResource() {
			return action.getVirtualModelResource();
		}

		public void setVirtualModelResource(VirtualModelResource virtualModelResource) {
			if (virtualModelResource != getVirtualModelResource()) {
				VirtualModelResource oldValue = getVirtualModelResource();
				action.setVirtualModelResource(virtualModelResource);
				getPropertyChangeSupport().firePropertyChange("virtualModelResource", oldValue, virtualModelResource);
				checkValidity();
			}
		}

	}

}
