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
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.controller.GINAIconLibrary;
import org.openflexo.technologyadapter.gina.fml.action.GivesFMLControlledFIBVirtualModelNature;
import org.openflexo.technologyadapter.gina.fml.action.GivesFMLControlledFIBVirtualModelNature.FIBComponentChoice;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class GivesFMLControlledFIBVirtualModelNatureWizard extends FlexoWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(GivesFMLControlledFIBVirtualModelNatureWizard.class.getPackage().getName());

	private final GivesFMLControlledFIBVirtualModelNature action;

	private final ChooseStrategy chooseStrategy;
	private UseExistingGINAFIBComponent useExistingGINAFIBComponent;
	private CreateNewGINAFIBComponent createNewGINAFIBComponent;

	public GivesFMLControlledFIBVirtualModelNatureWizard(GivesFMLControlledFIBVirtualModelNature action, FlexoController controller) {
		super(controller);
		this.action = action;
		addStep(chooseStrategy = new ChooseStrategy());
	}

	public GivesFMLControlledFIBVirtualModelNature getAction() {
		return action;
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("gives_screen_nature");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory
				.getImageIcon(IconFactory.getImageIcon(GINAIconLibrary.FIB_COMPONENT_BIG_ICON, FMLIconLibrary.VIRTUAL_MODEL_BIG_MARKER),
						IconLibrary.NEW_32_32)
				.getImage();
	}

	public ChooseStrategy getChooseStrategy() {
		return chooseStrategy;
	}

	public FlexoConcept getFlexoConcept() {
		return getAction().getFocusedObject();
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/GivesFMLControlledFIBVirtualModelNature/ChooseStrategy.fib")
	public class ChooseStrategy extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public GivesFMLControlledFIBVirtualModelNature getAction() {
			return GivesFMLControlledFIBVirtualModelNatureWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("choose_a_strategy");
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

			if (getAction().getChoice() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("please_choose_a_strategy"), IssueMessageType.ERROR);
				return false;
			}
			return true;
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

	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link FMLRTVirtualModelInstance}
	 * 
	 * @author sylvain
	 *
	 */
	@FIBPanel("Fib/Wizard/GivesFMLControlledFIBVirtualModelNature/UseExistingGINAFIBComponent.fib")
	public class UseExistingGINAFIBComponent extends WizardStep {

		public UseExistingGINAFIBComponent() {
		}

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public GivesFMLControlledFIBVirtualModelNature getAction() {
			return GivesFMLControlledFIBVirtualModelNatureWizard.this.getAction();
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
	@FIBPanel("Fib/Wizard/GivesFMLControlledFIBVirtualModelNature/CreateNewGINAFIBComponent.fib")
	public class CreateNewGINAFIBComponent extends WizardStep {

		public CreateNewGINAFIBComponent() {
		}

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public GivesFMLControlledFIBVirtualModelNature getAction() {
			return GivesFMLControlledFIBVirtualModelNatureWizard.this.getAction();
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

			return true;

		}

		public Type getGINAFIBComponentType() {
			return GINAFIBComponent.class;
		}

		public GINATechnologyAdapter getGINATechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);
		}

	}

}
