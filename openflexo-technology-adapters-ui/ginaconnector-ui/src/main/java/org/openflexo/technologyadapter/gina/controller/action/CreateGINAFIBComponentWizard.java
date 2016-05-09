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

package org.openflexo.technologyadapter.gina.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.gina.controller.GINAIconLibrary;
import org.openflexo.technologyadapter.gina.model.action.CreateGINAFIBComponent;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateGINAFIBComponentWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateGINAFIBComponentWizard.class.getPackage().getName());

	private final CreateGINAFIBComponent action;

	private final DescribeNewComponent configureNewConcept;

	public CreateGINAFIBComponentWizard(CreateGINAFIBComponent action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(configureNewConcept = new DescribeNewComponent());
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("create_new_component");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(GINAIconLibrary.GINA_TECHNOLOGY_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public DescribeNewComponent getConfigureNewConcept() {
		return configureNewConcept;
	}

	/**
	 * This step is used to configure new {@link DiagramSpecification}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/DescribeNewComponent.fib")
	public class DescribeNewComponent extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateGINAFIBComponent getAction() {
			return action;
		}

		@Override
		public String getTitle() {
			return FlexoLocalization.localizedForKey("configure_new_component");
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getComponentName())) {
				setIssueMessage(FlexoLocalization.localizedForKey("no_component_name_defined"), IssueMessageType.ERROR);
				return false;
			}
			return true;

		}

		public String getComponentName() {
			return action.getComponentName();
		}

		public void setComponentName(String newComponentName) {
			if (!newComponentName.equals(getComponentName())) {
				String oldValue = getComponentName();
				action.setComponentName(newComponentName);
				getPropertyChangeSupport().firePropertyChange("componentName", oldValue, newComponentName);
				checkValidity();
			}
		}

		public String getDescription() {
			return action.getDescription();
		}

		public void setDescription(String newComponentDescription) {
			if (!newComponentDescription.equals(getDescription())) {
				String oldValue = getDescription();
				action.setDescription(newComponentDescription);
				getPropertyChangeSupport().firePropertyChange("description", oldValue, newComponentDescription);
				checkValidity();
			}
		}

	}

}
