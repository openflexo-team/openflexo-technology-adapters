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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Type;
import java.util.List;
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
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.controller.GINAIconLibrary;
import org.openflexo.technologyadapter.gina.fml.action.CreateFMLControlledFIBVirtualModel;
import org.openflexo.technologyadapter.gina.fml.action.CreateFMLControlledFIBVirtualModel.FIBComponentChoice;
import org.openflexo.technologyadapter.gina.fml.action.CreateFMLControlledFIBVirtualModel.GINAFIBComponentAPIEntry;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateFMLControlledFIBVirtualModelWizard extends AbstractCreateVirtualModelWizard<CreateFMLControlledFIBVirtualModel> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFMLControlledFIBVirtualModelWizard.class.getPackage().getName());

	private final DescribeFMLControlledFIBVirtualModel describeVirtualModel;
	private UseExistingGINAFIBComponent useExistingGINAFIBComponent;
	private CreateNewGINAFIBComponent createNewGINAFIBComponent;

	public CreateFMLControlledFIBVirtualModelWizard(CreateFMLControlledFIBVirtualModel action, FlexoController controller) {
		super(action, controller);
		addStep(describeVirtualModel = new DescribeFMLControlledFIBVirtualModel());
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("create_screen_virtual_model");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory
				.getImageIcon(IconFactory.getImageIcon(GINAIconLibrary.FIB_COMPONENT_BIG_ICON, FMLIconLibrary.VIRTUAL_MODEL_BIG_MARKER),
						IconLibrary.BIG_NEW_MARKER)
				.getImage();
	}

	public DescribeFMLControlledFIBVirtualModel getDescribeVirtualModel() {
		return describeVirtualModel;
	}

	@Override
	public VirtualModel getContainerVirtualModel() {
		if (getAction().getFocusedObject() instanceof VirtualModel) {
			return (VirtualModel) getAction().getFocusedObject();
		}
		return null;
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/DescribeFMLControlledFIBVirtualModel.fib")
	public class DescribeFMLControlledFIBVirtualModel extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateFMLControlledFIBVirtualModel getAction() {
			return CreateFMLControlledFIBVirtualModelWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("describe_screen_virtual_model");
		}

		@Override
		public boolean isTransitionalStep() {
			return true;
		}

		@Override
		public void performTransition() {
			super.performTransition();
			if (getChoice() == FIBComponentChoice.UseExistingComponent) {
				addStep(useExistingGINAFIBComponent = new UseExistingGINAFIBComponent());
			}
			else if (getChoice() == FIBComponentChoice.CreateNewComponent) {
				addStep(createNewGINAFIBComponent = new CreateNewGINAFIBComponent());
			}
		}

		@Override
		public void discardTransition() {
			super.discardTransition();
			if (getChoice() == FIBComponentChoice.UseExistingComponent) {
				removeStep(useExistingGINAFIBComponent);
			}
			else if (getChoice() == FIBComponentChoice.CreateNewComponent) {
				removeStep(createNewGINAFIBComponent);
			}
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getNewVirtualModelName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_supply_valid_virtual_model_name"), IssueMessageType.ERROR);
				return false;
			}
			else if (getAction().getFocusedObject() instanceof VirtualModel
					&& ((VirtualModel) getAction().getFocusedObject()).getVirtualModelNamed(getNewVirtualModelName()) != null) {
				setIssueMessage(getAction().getLocales().localizedForKey("duplicated_virtual_model_name"), IssueMessageType.ERROR);
				return false;
			}
			else if (getAction().getFocusedObject() instanceof RepositoryFolder
					&& ((RepositoryFolder) getAction().getFocusedObject()).getResourceWithName(getNewVirtualModelName()) != null) {
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

		public FIBComponentChoice getChoice() {
			return getAction().getChoice();
		}

		public void setChoice(FIBComponentChoice choice) {
			if (choice != getChoice()) {
				FIBComponentChoice oldValue = getChoice();
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
	@FIBPanel("Fib/Wizard/UseExistingGINAFIBComponent.fib")
	public class UseExistingGINAFIBComponent extends WizardStep {

		public UseExistingGINAFIBComponent() {
		}

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateFMLControlledFIBVirtualModel getAction() {
			return CreateFMLControlledFIBVirtualModelWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("choose_existing_gina_fib_component");
		}

		public String getFIBModelSlotName() {
			return getAction().getFIBModelSlotName();
		}

		public void setFIBModelSlotName(String fibModelSlotName) {
			if (!fibModelSlotName.equals(getFIBModelSlotName())) {
				String oldValue = getFIBModelSlotName();
				getAction().setFIBModelSlotName(fibModelSlotName);
				getPropertyChangeSupport().firePropertyChange("fIBModelSlotName", oldValue, fibModelSlotName);
				checkValidity();
			}
		}

		public GINAFIBComponentResource getTemplateResource() {
			return getAction().getTemplateResource();
		}

		public void setTemplateResource(GINAFIBComponentResource templateResource) {
			if (templateResource != getTemplateResource()) {
				GINAFIBComponentResource oldValue = getTemplateResource();
				getAction().setTemplateResource(templateResource);
				getPropertyChangeSupport().firePropertyChange("templateResource", oldValue, templateResource);
				checkValidity();
			}
		}

		public Type getGINAFIBComponentType() {
			return GINAFIBComponent.class;
		}

		public GINATechnologyAdapter getGINATechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getFIBModelSlotName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_supply_a_valid_model_slot_name"), IssueMessageType.ERROR);
				return false;
			}

			if (getTemplateResource() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_choose_an_existing_gina_fib_component"),
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
	@FIBPanel("Fib/Wizard/CreateNewGINAFIBComponent.fib")
	public class CreateNewGINAFIBComponent extends WizardStep implements PropertyChangeListener {

		public CreateNewGINAFIBComponent() {
		}

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateFMLControlledFIBVirtualModel getAction() {
			return CreateFMLControlledFIBVirtualModelWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("creates_new_gina_fib_component");
		}

		public String getFIBModelSlotName() {
			return getAction().getFIBModelSlotName();
		}

		public void setFIBModelSlotName(String fibModelSlotName) {
			if (!fibModelSlotName.equals(getFIBModelSlotName())) {
				String oldValue = getFIBModelSlotName();
				getAction().setFIBModelSlotName(fibModelSlotName);
				getPropertyChangeSupport().firePropertyChange("fIBModelSlotName", oldValue, fibModelSlotName);
				checkValidity();
			}
		}

		public String getNewComponentName() {
			return getAction().getNewComponentName();
		}

		public void setNewComponentName(String newComponentName) {
			if ((newComponentName == null && getNewComponentName() != null)
					|| (newComponentName != null && !newComponentName.equals(getNewComponentName()))) {
				String oldValue = getNewComponentName();
				getAction().setNewComponentName(newComponentName);
				getPropertyChangeSupport().firePropertyChange("newComponentName", oldValue, newComponentName);
				checkValidity();
			}
		}

		public RepositoryFolder<GINAFIBComponentResource, ?> getRepositoryFolder() {
			return getAction().getRepositoryFolder();
		}

		public void setRepositoryFolder(RepositoryFolder<GINAFIBComponentResource, ?> repositoryFolder) {
			if (repositoryFolder != getRepositoryFolder()) {
				RepositoryFolder<GINAFIBComponentResource, ?> oldValue = getRepositoryFolder();
				getAction().setRepositoryFolder(repositoryFolder);
				getPropertyChangeSupport().firePropertyChange("repositoryFolder", oldValue, repositoryFolder);
				checkValidity();
			}
		}

		public List<GINAFIBComponentAPIEntry> getAPIEntries() {
			return getAction().getAPIEntries();
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getAPIEntries().contains(evt.getSource())) {
				checkValidity();
			}
		}

		public GINAFIBComponentAPIEntry newAPIEntry() {
			GINAFIBComponentAPIEntry newEntry = getAction().newAPIEntry();
			newEntry.getPropertyChangeSupport().addPropertyChangeListener(this);
			checkValidity();
			return newEntry;
		}

		public void deleteAPIEntry(GINAFIBComponentAPIEntry apiEntryToDelete) {
			apiEntryToDelete.getPropertyChangeSupport().removePropertyChangeListener(this);
			getAction().deleteAPIEntry(apiEntryToDelete);
			checkValidity();
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getFIBModelSlotName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_provide_a_valid_model_slot_name"), IssueMessageType.ERROR);
				return false;
			}

			if (getRepositoryFolder() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_provide_a_folder_for_the_new_component"),
						IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getNewComponentName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_provide_a_name_for_the_new_component"),
						IssueMessageType.ERROR);
				return false;
			}
			else {
				if (!getNewComponentName().endsWith(".fib")) {
					setIssueMessage(getAction().getLocales().localizedForKey("name_for_the_new_component_must_ends_with_.fib"),
							IssueMessageType.ERROR);
					return false;
				}
			}

			if (getAction().getAPIEntries().size() == 0) {
				setIssueMessage(getAction().getLocales().localizedForKey("no_API_entries_defined_for_new_component"),
						IssueMessageType.WARNING);
				return true;
			}
			else {

				// We try to detect duplicated names
				for (GINAFIBComponentAPIEntry entry : getAPIEntries()) {
					String entryName = entry.getName();
					for (GINAFIBComponentAPIEntry entry2 : getAPIEntries()) {
						if ((entry != entry2) && (entry.getName().equals(entry2.getName()))) {
							setIssueMessage(getAction().getLocales().localizedForKey("duplicated_entry_name") + " : " + entryName,
									IssueMessageType.ERROR);
							return false;
						}
					}
				}

				// Then, we valid each GINAFIBComponentAPIEntry
				boolean hasWarnings = false;
				for (GINAFIBComponentAPIEntry entry : getAPIEntries()) {
					String errorMessage = entry.getConfigurationErrorMessage();
					String warningMessage = entry.getConfigurationWarningMessage();
					if (StringUtils.isNotEmpty(errorMessage)) {
						setIssueMessage(errorMessage, IssueMessageType.ERROR);
						return false;
					}
					if (StringUtils.isNotEmpty(warningMessage)) {
						setIssueMessage(warningMessage, IssueMessageType.WARNING);
						hasWarnings = true;
					}
				}
				if (!hasWarnings) {
					setIssueMessage(getAction().getLocales().localizedForKey("all_API_entries_are_valid"), IssueMessageType.INFO);
				}
				return true;
			}

		}

		public Type getGINAFIBComponentType() {
			return GINAFIBComponent.class;
		}

		public GINATechnologyAdapter getGINATechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);
		}

	}

}
