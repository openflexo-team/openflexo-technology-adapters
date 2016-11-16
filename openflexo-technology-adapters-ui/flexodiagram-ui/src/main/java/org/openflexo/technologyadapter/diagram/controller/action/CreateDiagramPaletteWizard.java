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
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateDiagramPaletteWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateDiagramPaletteWizard.class.getPackage().getName());

	private final CreateDiagramPalette action;

	private final DescribePalette describePalette;

	public CreateDiagramPaletteWizard(CreateDiagramPalette action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(describePalette = new DescribePalette());
	}

	@Override
	public String getWizardTitle() {
		return action.getLocales().localizedForKey("create_new_diagram_palette");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_PALETTE_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public DescribePalette getConfigureNewConcept() {
		return describePalette;
	}

	/**
	 * This step is used to configure new {@link Diagram}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/CreateElement/DescribeDiagramPalette.fib")
	public class DescribePalette extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateDiagramPalette getAction() {
			return action;
		}

		@Override
		public String getTitle() {
			return action.getLocales().localizedForKey("configure_new_palette");
		}

		@Override
		public boolean isValid() {
			if (StringUtils.isEmpty(getNewPaletteName())) {
				setIssueMessage(noNameMessage(), IssueMessageType.ERROR);
				return false;
			}

			if (action.getFocusedObject().getPalette(getNewPaletteName()) != null
					|| action.getFocusedObject().getPalette(getNewPaletteName() + ".palette") != null) {
				setIssueMessage(duplicatedNameMessage(), IssueMessageType.ERROR);
				return false;
			}

			if (!getNewPaletteName().equals(JavaUtils.getClassName(getNewPaletteName()))
					&& !getNewPaletteName().equals(JavaUtils.getVariableName(getNewPaletteName()))) {
				setIssueMessage(invalidNameMessage(), IssueMessageType.ERROR);
				return false;
			}

			return true;
		}

		public String getNewPaletteName() {
			return action.getNewPaletteName();
		}

		public void setNewPaletteName(String newPaletteName) {
			if ((newPaletteName == null && getNewPaletteName() != null)
					|| (newPaletteName != null && !newPaletteName.equals(getNewPaletteName()))) {
				String oldValue = getNewPaletteName();
				action.setNewPaletteName(newPaletteName);
				getPropertyChangeSupport().firePropertyChange("newPaletteName", oldValue, newPaletteName);
				checkValidity();
			}
		}

		public String getDescription() {
			return action.getDescription();
		}

		public void setDescription(String description) {
			if ((description == null && getDescription() != null) || (description != null && !description.equals(getDescription()))) {
				String oldValue = getDescription();
				action.setDescription(description);
				getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
			}
		}

		private String noNameMessage() {
			return action.getLocales().localizedForKey("no_palette_name_defined");
		}

		private String invalidNameMessage() {
			return action.getLocales().localizedForKey("invalid_name_for_new_palette");
		}

		private String duplicatedNameMessage() {
			return action.getLocales().localizedForKey("a_palette_with_that_name_already_exists");
		}

	}

}
