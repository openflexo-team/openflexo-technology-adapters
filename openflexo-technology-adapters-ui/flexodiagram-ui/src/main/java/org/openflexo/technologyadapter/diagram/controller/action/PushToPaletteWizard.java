/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Fml-technologyadapter-ui, a component of the software infrastructure 
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
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.action.PushToPalette;
import org.openflexo.technologyadapter.diagram.fml.action.PushToPalette.PushToPaletteChoices;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class PushToPaletteWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PushToPaletteWizard.class.getPackage().getName());

	private final PushToPalette action;

	private final PushToPaletteOptions configureNewConcept;

	public PushToPaletteWizard(PushToPalette action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(configureNewConcept = new PushToPaletteOptions());
	}

	@Override
	public String getWizardTitle() {
		return action.getLocales().localizedForKey("push_to_palette");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_PALETTE_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public PushToPaletteOptions getConfigureNewConcept() {
		return configureNewConcept;
	}

	/**
	 * This step is used to configure new {@link DiagramSpecification}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/PushToPalette/PushToPaletteOptions.fib")
	public class PushToPaletteOptions extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public PushToPalette getAction() {
			return action;
		}

		@Override
		public String getTitle() {
			return action.getLocales().localizedForKey("configure_push_to_palette");
		}

		@Override
		public boolean isValid() {
			if (StringUtils.isEmpty(getNewElementName())) {
				setIssueMessage(noNameMessage(), IssueMessageType.ERROR);
				return false;
			}

			if (getPalette() == null) {
				setIssueMessage(noPaletteSelectedMessage(), IssueMessageType.ERROR);
				return false;
			}

			if (getPrimaryChoice().equals(PushToPaletteChoices.CONFIGURE_FML_CONTROL)) {
				if (getVirtualModel() == null) {
					setIssueMessage(noVirtualModelSelectedMessage(), IssueMessageType.ERROR);
					return false;
				}
				if (getFlexoConcept() == null) {
					setIssueMessage(noFlexoConceptSelectedMessage(), IssueMessageType.ERROR);
					return false;
				}

				if (getDropScheme() == null) {
					setIssueMessage(noDropSchemeSelectedMessage(), IssueMessageType.ERROR);
					return false;
				}

			}

			return true;
		}

		public String getNewElementName() {
			return action.getNewElementName();
		}

		public void setNewElementName(String newElementName) {
			if (!newElementName.equals(getNewElementName())) {
				String oldValue = getNewElementName();
				action.setNewElementName(newElementName);
				getPropertyChangeSupport().firePropertyChange("newElementName", oldValue, newElementName);
				checkValidity();
			}
		}

		public DiagramPalette getPalette() {
			return action.getPalette();
		}

		public void setPalette(DiagramPalette palette) {
			if (palette != getPalette()) {
				DiagramPalette oldValue = getPalette();
				action.setPalette(palette);
				getPropertyChangeSupport().firePropertyChange("palette", oldValue, palette);
				checkValidity();
			}
		}

		public PushToPaletteChoices getPrimaryChoice() {
			return action.getPrimaryChoice();
		}

		public void setPrimaryChoice(PushToPaletteChoices primaryChoice) {
			if (primaryChoice != getPrimaryChoice()) {
				PushToPaletteChoices oldValue = getPrimaryChoice();
				action.setPrimaryChoice(primaryChoice);
				getPropertyChangeSupport().firePropertyChange("primaryChoice", oldValue, primaryChoice);
				checkValidity();
			}
		}

		public FlexoConcept getFlexoConcept() {
			return action.getFlexoConcept();
		}

		public void setFlexoConcept(FlexoConcept flexoConcept) {
			if (flexoConcept != getFlexoConcept()) {
				FlexoConcept oldValue = getFlexoConcept();
				action.setFlexoConcept(flexoConcept);
				getPropertyChangeSupport().firePropertyChange("flexoConcept", oldValue, flexoConcept);
				getPropertyChangeSupport().firePropertyChange("availableDropSchemes", null, getAvailableDropSchemes());
				checkValidity();
			}
		}

		public DropScheme getDropScheme() {
			return action.getDropScheme();
		}

		public void setDropScheme(DropScheme dropScheme) {
			if (dropScheme != getDropScheme()) {
				DropScheme oldValue = getDropScheme();
				action.setDropScheme(dropScheme);
				getPropertyChangeSupport().firePropertyChange("dropScheme", oldValue, dropScheme);
				checkValidity();
			}
		}

		public VirtualModel getVirtualModel() {
			return action.getVirtualModel();
		}

		public void setVirtualModel(VirtualModel virtualModel) {
			if (virtualModel != getVirtualModel()) {
				VirtualModel oldValue = getVirtualModel();
				action.setVirtualModel(virtualModel);
				getPropertyChangeSupport().firePropertyChange("virtualModel", oldValue, virtualModel);
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

		public boolean takeScreenshotForTopLevelElement() {
			return action.takeScreenshotForTopLevelElement();
		}

		public void setTakeScreenshotForTopLevelElement(boolean takeScreenshotForTopLevelElement) {
			if (takeScreenshotForTopLevelElement != takeScreenshotForTopLevelElement()) {
				action.setTakeScreenshotForTopLevelElement(takeScreenshotForTopLevelElement);
				getPropertyChangeSupport().firePropertyChange("takeScreenshotForTopLevelElement", !takeScreenshotForTopLevelElement,
						takeScreenshotForTopLevelElement);
				checkValidity();
			}
		}

		public boolean overrideDefaultGraphicalRepresentations() {
			return action.overrideDefaultGraphicalRepresentations();
		}

		public void setOverrideDefaultGraphicalRepresentations(boolean overrideDefaultGraphicalRepresentations) {
			if (overrideDefaultGraphicalRepresentations != overrideDefaultGraphicalRepresentations()) {
				action.setOverrideDefaultGraphicalRepresentations(overrideDefaultGraphicalRepresentations);
				getPropertyChangeSupport().firePropertyChange("overrideDefaultGraphicalRepresentations",
						!overrideDefaultGraphicalRepresentations, overrideDefaultGraphicalRepresentations);
				checkValidity();
			}
		}

		public DiagramSpecification getDiagramSpecification() {
			return getAction().getDiagramSpecification();
		}

		public List<DropScheme> getAvailableDropSchemes() {
			return getAction().getAvailableDropSchemes();
		}

		public String noNameMessage() {
			return action.getLocales().localizedForKey("no_palette_element_name_defined");
		}

		public String noPaletteSelectedMessage() {
			return action.getLocales().localizedForKey("no_palette_selected");
		}

		public String noFlexoConceptSelectedMessage() {
			return action.getLocales().localizedForKey("no_flexo_concept_selected");
		}

		public String noDropSchemeSelectedMessage() {
			return action.getLocales().localizedForKey("no_drop_scheme_selected");
		}

		public String noVirtualModelSelectedMessage() {
			return action.getLocales().localizedForKey("no_virtual_model_selected");
		}

	}

}
