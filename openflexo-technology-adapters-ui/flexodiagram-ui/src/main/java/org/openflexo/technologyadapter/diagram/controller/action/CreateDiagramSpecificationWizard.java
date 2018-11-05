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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramSpecification;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateDiagramSpecificationWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateDiagramSpecificationWizard.class.getPackage().getName());

	private final CreateDiagramSpecification action;

	private final DescribeDiagramSpecification configureNewConcept;

	public CreateDiagramSpecificationWizard(CreateDiagramSpecification action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(configureNewConcept = new DescribeDiagramSpecification());
	}

	@Override
	public String getWizardTitle() {
		return action.getLocales().localizedForKey("create_new_diagram_specification");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_BIG_ICON, IconLibrary.NEW_32_32).getImage();
	}

	public DescribeDiagramSpecification getConfigureNewConcept() {
		return configureNewConcept;
	}

	/**
	 * This step is used to configure new {@link DiagramSpecification}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/CreateElement/DescribeDiagramSpecification.fib")
	public class DescribeDiagramSpecification extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateDiagramSpecification getAction() {
			return action;
		}

		@Override
		public String getTitle() {
			return action.getLocales().localizedForKey("configure_new_diagram_specification");
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getNewDiagramSpecificationName())) {
				setIssueMessage(action.getLocales().localizedForKey("no_diagram_specification_name_defined"), IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getNewDiagramSpecificationURI())) {
				setIssueMessage(action.getLocales().localizedForKey("please_supply_valid_diagram_specification_uri"),
						IssueMessageType.ERROR);
				return false;
			}
			try {
				new URL(getNewDiagramSpecificationURI());
			} catch (MalformedURLException e) {
				setIssueMessage(action.getLocales().localizedForKey("malformed_uri"), IssueMessageType.ERROR);
				return false;
			}
			if (getAction().getFocusedObject().getResourceRepository() == null) {
				setIssueMessage(action.getLocales().localizedForKey("could_not_access_registered_resources"), IssueMessageType.ERROR);
				return false;
			}
			if (getAction().getFocusedObject().getResourceRepository().getResource(getNewDiagramSpecificationURI()) != null) {
				setIssueMessage(action.getLocales().localizedForKey("already_existing_diagram_specification_uri"), IssueMessageType.ERROR);
				return false;
			}
			return true;

		}

		public String getNewDiagramSpecificationName() {
			return action.getNewDiagramSpecificationName();
		}

		public void setNewDiagramSpecificationName(String newDiagramSpecificationName) {
			if (!newDiagramSpecificationName.equals(getNewDiagramSpecificationName())) {
				String oldValue = getNewDiagramSpecificationName();
				action.setNewDiagramSpecificationName(newDiagramSpecificationName);
				getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationName", oldValue, newDiagramSpecificationName);
				checkValidity();
			}
		}

		public String getNewDiagramSpecificationURI() {
			return action.getNewDiagramSpecificationURI();
		}

		public void setNewDiagramSpecificationURI(String newDiagramSpecificationURI) {
			if (!newDiagramSpecificationURI.equals(getNewDiagramSpecificationURI())) {
				String oldValue = getNewDiagramSpecificationURI();
				action.setNewDiagramSpecificationURI(newDiagramSpecificationURI);
				getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationURI", oldValue, newDiagramSpecificationURI);
				checkValidity();
			}
		}

		public String getNewDiagramSpecificationDescription() {
			return action.getNewDiagramSpecificationDescription();
		}

		public void setNewDiagramSpecificationDescription(String newDiagramSpecificationDescription) {
			if (!newDiagramSpecificationDescription.equals(getNewDiagramSpecificationDescription())) {
				String oldValue = getNewDiagramSpecificationDescription();
				action.setNewDiagramSpecificationDescription(newDiagramSpecificationDescription);
				getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationDescription", oldValue,
						newDiagramSpecificationDescription);
				checkValidity();
			}
		}

	}

}
