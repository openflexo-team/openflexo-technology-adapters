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
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fml.controller.action.AbstractCreateVirtualModelWizard;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModel;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModel.DiagramSpecificationChoice;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateFMLControlledDiagramVirtualModelWizard extends AbstractCreateVirtualModelWizard<CreateFMLControlledDiagramVirtualModel> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFMLControlledDiagramVirtualModelWizard.class.getPackage().getName());

	private final DescribeFMLControlledDiagramVirtualModel describeVirtualModel;
	private CreateNewDiagramSpecification createNewDiagramSpecification;
	private UseExistingDiagramSpecification useExistingDiagramSpecification;

	public CreateFMLControlledDiagramVirtualModelWizard(CreateFMLControlledDiagramVirtualModel action, FlexoController controller) {
		super(action, controller);
		addStep(describeVirtualModel = new DescribeFMLControlledDiagramVirtualModel());
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("create_diagram_virtual_model");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory
				.getImageIcon(IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_BIG_ICON, FMLIconLibrary.VIRTUAL_MODEL_BIG_MARKER),
						IconLibrary.NEW_32_32)
				.getImage();
	}

	public DescribeFMLControlledDiagramVirtualModel getDescribeVirtualModel() {
		return describeVirtualModel;
	}

	@Override
	public VirtualModel getContainerVirtualModel() {
		return getAction().getFocusedObject();
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/CreateDiagramVirtualModel/DescribeFMLControlledDiagramVirtualModel.fib")
	public class DescribeFMLControlledDiagramVirtualModel extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateFMLControlledDiagramVirtualModel getAction() {
			return CreateFMLControlledDiagramVirtualModelWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("describe_fml_controlled_diagram_virtual_model");
		}

		@Override
		public boolean isTransitionalStep() {
			return true;
		}

		@Override
		public void performTransition() {
			super.performTransition();
			if (getChoice() == DiagramSpecificationChoice.CreateNewDiagramSpecification) {
				addStep(createNewDiagramSpecification = new CreateNewDiagramSpecification());
			}
			else if (getChoice() == DiagramSpecificationChoice.UseExistingDiagramSpecification) {
				addStep(useExistingDiagramSpecification = new UseExistingDiagramSpecification());
			}
		}

		@Override
		public void discardTransition() {
			super.discardTransition();

			if (getChoice() == DiagramSpecificationChoice.CreateNewDiagramSpecification) {
				removeStep(createNewDiagramSpecification);
			}
			else if (getChoice() == DiagramSpecificationChoice.UseExistingDiagramSpecification) {
				removeStep(useExistingDiagramSpecification);
			}
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getNewVirtualModelName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_supply_valid_virtual_model_name"), IssueMessageType.ERROR);
				return false;
			}
			else if (getAction().getFocusedObject().getVirtualModelNamed(getNewVirtualModelName()) != null) {
				setIssueMessage(getAction().getLocales().localizedForKey("duplicated_virtual_model_name"), IssueMessageType.ERROR);
				return false;
			}
			else if (StringUtils.isEmpty(getNewVirtualModelDescription())) {
				setIssueMessage(getAction().getLocales().localizedForKey("it_is_recommanded_to_describe_virtual_model"),
						IssueMessageType.WARNING);
			}

			return true;
		}

		public String getNewVirtualModelName() {
			return getAction().getNewVirtualModelName();
		}

		public void setNewVirtualModelName(String newVirtualModelName) {
			if (!newVirtualModelName.equals(getNewVirtualModelName())) {
				String oldValue = getNewVirtualModelName();
				getAction().setNewVirtualModelName(newVirtualModelName);
				getPropertyChangeSupport().firePropertyChange("newVirtualModelName", oldValue, newVirtualModelName);
				checkValidity();
			}
		}

		public DiagramSpecificationChoice getChoice() {
			return getAction().getChoice();
		}

		public void setChoice(DiagramSpecificationChoice choice) {
			if (choice != getChoice()) {
				DiagramSpecificationChoice oldValue = getChoice();
				getAction().setChoice(choice);
				getPropertyChangeSupport().firePropertyChange("choice", oldValue, choice);
				checkValidity();
			}
		}

		public String getNewVirtualModelDescription() {
			return getAction().getNewVirtualModelDescription();
		}

		public void setNewVirtualModelDescription(String newViewPointDescription) {
			if (!newViewPointDescription.equals(getNewVirtualModelDescription())) {
				String oldValue = getNewVirtualModelDescription();
				getAction().setNewVirtualModelDescription(newViewPointDescription);
				getPropertyChangeSupport().firePropertyChange("newVirtualModelDescription", oldValue, newViewPointDescription);
				checkValidity();
			}
		}

	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/CreateDiagramVirtualModel/UseExistingDiagramSpecification.fib")
	public class UseExistingDiagramSpecification extends WizardStep {

		public UseExistingDiagramSpecification() {
		}

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateFMLControlledDiagramVirtualModel getAction() {
			return CreateFMLControlledDiagramVirtualModelWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("choose_existing_gina_fib_component");
		}

		public String getDiagramModelSlotName() {
			return getAction().getDiagramModelSlotName();
		}

		public void setDiagramModelSlotName(String diagramModelSlotName) {
			if (!diagramModelSlotName.equals(getDiagramModelSlotName())) {
				String oldValue = getDiagramModelSlotName();
				getAction().setDiagramModelSlotName(diagramModelSlotName);
				getPropertyChangeSupport().firePropertyChange("diagramModelSlotName", oldValue, diagramModelSlotName);
				checkValidity();
			}
		}

		public DiagramSpecificationResource getDiagramSpecificationResource() {
			return getAction().getDiagramSpecificationResource();
		}

		public void setDiagramSpecificationResource(DiagramSpecificationResource diagramSpecificationResource) {
			if (diagramSpecificationResource != getDiagramSpecificationResource()) {
				DiagramSpecificationResource oldValue = getDiagramSpecificationResource();
				getAction().setDiagramSpecificationResource(diagramSpecificationResource);
				getPropertyChangeSupport().firePropertyChange("diagramSpecificationResource", oldValue, diagramSpecificationResource);
				checkValidity();
			}
		}

		public Type getDiagramSpecificationType() {
			return DiagramSpecification.class;
		}

		public DiagramTechnologyAdapter getDiagramTechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getDiagramModelSlotName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_supply_a_valid_model_slot_name"), IssueMessageType.ERROR);
				return false;
			}

			if (getDiagramSpecificationResource() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_choose_an_existing_diagram_specification"),
						IssueMessageType.ERROR);
				return false;
			}

			return true;
		}
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/CreateDiagramVirtualModel/CreateNewDiagramSpecification.fib")
	public class CreateNewDiagramSpecification extends WizardStep {

		public CreateNewDiagramSpecification() {
		}

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateFMLControlledDiagramVirtualModel getAction() {
			return CreateFMLControlledDiagramVirtualModelWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("creates_new_diagram_specification");
		}

		public String getDiagramModelSlotName() {
			return getAction().getDiagramModelSlotName();
		}

		public void setDiagramModelSlotName(String diagramModelSlotName) {
			if (!diagramModelSlotName.equals(getDiagramModelSlotName())) {
				String oldValue = getDiagramModelSlotName();
				getAction().setDiagramModelSlotName(diagramModelSlotName);
				getPropertyChangeSupport().firePropertyChange("diagramModelSlotName", oldValue, diagramModelSlotName);
				checkValidity();
			}
		}

		public String getNewDiagramSpecificationName() {
			return getAction().getNewDiagramSpecificationName();
		}

		public void setNewDiagramSpecificationName(String newDiagramSpecificationName) {
			if ((newDiagramSpecificationName == null && getNewDiagramSpecificationName() != null)
					|| (newDiagramSpecificationName != null && !newDiagramSpecificationName.equals(getNewDiagramSpecificationName()))) {
				String oldValue = getNewDiagramSpecificationName();
				getAction().setNewDiagramSpecificationName(newDiagramSpecificationName);
				getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationName", oldValue, newDiagramSpecificationName);
				checkValidity();
			}
		}

		public String getNewDiagramSpecificationURI() {
			return getAction().getNewDiagramSpecificationURI();
		}

		public void setNewDiagramSpecificationURI(String newDiagramSpecificationURI) {
			if ((newDiagramSpecificationURI == null && getNewDiagramSpecificationName() != null)
					|| (newDiagramSpecificationURI != null && !newDiagramSpecificationURI.equals(getNewDiagramSpecificationURI()))) {
				String oldValue = getNewDiagramSpecificationURI();
				getAction().setNewDiagramSpecificationURI(newDiagramSpecificationURI);
				getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationURI", oldValue, newDiagramSpecificationURI);
				checkValidity();
			}
		}

		public RepositoryFolder<DiagramSpecificationResource, ?> getRepositoryFolder() {
			return getAction().getRepositoryFolder();
		}

		public void setRepositoryFolder(RepositoryFolder<DiagramSpecificationResource, ?> repositoryFolder) {
			if (repositoryFolder != getRepositoryFolder()) {
				RepositoryFolder<DiagramSpecificationResource, ?> oldValue = getRepositoryFolder();
				getAction().setRepositoryFolder(repositoryFolder);
				getPropertyChangeSupport().firePropertyChange("repositoryFolder", oldValue, repositoryFolder);
				checkValidity();
			}
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getDiagramModelSlotName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_provide_a_valid_diagram_model_slot_name"),
						IssueMessageType.ERROR);
				return false;
			}

			if (getRepositoryFolder() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_provide_a_folder_for_the_new_component"),
						IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getNewDiagramSpecificationName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_provide_a_name_for_the_new_diagram_specification"),
						IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getNewDiagramSpecificationURI())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_provide_a_uri_for_the_new_diagram_specification"),
						IssueMessageType.ERROR);
				return false;
			}

			try {
				new URL(getNewDiagramSpecificationURI());
			} catch (MalformedURLException e) {
				setIssueMessage(getAction().getLocales().localizedForKey("invalid_uri_for_the_new_diagram_specification"),
						IssueMessageType.ERROR);
				return false;
			}

			return true;
		}

		public Type getDiagramSpecificationType() {
			return DiagramSpecification.class;
		}

		public DiagramTechnologyAdapter getDiagramTechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}

	}

}
