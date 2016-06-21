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
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.action.BlankFlexoConceptFromConnectorCreationStrategy;
import org.openflexo.technologyadapter.diagram.fml.action.ConnectorRoleCreationStrategy;
import org.openflexo.technologyadapter.diagram.fml.action.ConnectorRoleSettingStrategy;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareConnectorInFlexoConcept;
import org.openflexo.technologyadapter.diagram.fml.action.FlexoConceptFromConnectorCreationStrategy;
import org.openflexo.technologyadapter.diagram.fml.action.MapConnectorToFlexoConceptlnstanceStrategy;
import org.openflexo.technologyadapter.diagram.fml.action.MapConnectorToIndividualStrategy;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class DeclareConnectorInFlexoConceptWizard
		extends AbstractDeclareDiagramElementInFlexoConceptWizard<DeclareConnectorInFlexoConcept> {

	/*private static final String FLEXO_ROLE_IS_NULL = FlexoLocalization.localizedForKey("please_choose_flexo_role");
	private static final String NEW_CONNECTOR_ROLE_NAME_IS_NULL = FlexoLocalization.localizedForKey("please_supply_a_valid_role_name");
	private static final String NEW_CONNECTOR_ROLE_NAME_ALREADY_EXISTS = FlexoLocalization.localizedForKey("this_role_name_already_exists");
	private static final String FLEXO_CONCEPT_IS_NULL = FlexoLocalization.localizedForKey("please_choose_flexo_concept");
	
	private static final String FLEXO_CONCEPT_NAME_IS_NULL = FlexoLocalization.localizedForKey("flexo_concept_name_is_null");
	// private static final String FOCUSED_OBJECT_IS_NULL = FlexoLocalization.localizedForKey("focused_object_is_null");
	private static final String INDIVIDUAL_FLEXO_ROLE_NAME_IS_NULL = FlexoLocalization
			.localizedForKey("individual_flexo_role_name_is_null");
	private static final String CONCEPT_IS_NULL = FlexoLocalization.localizedForKey("concept_is_null");
	private static final String NO_SELECTED_ENTRY = FlexoLocalization.localizedForKey("no_selected_entry");
	private static final String A_SCHEME_NAME_IS_NOT_VALID = FlexoLocalization.localizedForKey("a_scheme_name_is_not_valid");
	private static final String VIRTUAL_MODEL_FLEXO_ROLE_NAME_IS_NULL = FlexoLocalization
			.localizedForKey("virtual_model_flexo_role_name_is_null");
	private static final String VIRTUAL_MODEL_CONCEPT_IS_NULL = FlexoLocalization.localizedForKey("virtual_model_concept_is_null");
	*/

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DeclareConnectorInFlexoConceptWizard.class.getPackage().getName());

	public DeclareConnectorInFlexoConceptWizard(DeclareConnectorInFlexoConcept action, FlexoController controller) {
		super(action, controller);
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("declare_connector_in_flexo_concept");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMLIconLibrary.FLEXO_CONCEPT_MEDIUM_ICON, DiagramIconLibrary.CONNECTOR_MARKER).getImage();
	}

	@Override
	public ReplaceConnectorInExistingFlexoConcept replaceElementInExistingFlexoConcept() {
		if (getAction().getFlexoRoleSettingStrategy() instanceof ConnectorRoleSettingStrategy) {
			return new ReplaceConnectorInExistingFlexoConcept((ConnectorRoleSettingStrategy) getAction().getFlexoRoleSettingStrategy());
		}
		return null;
	}

	@Override
	public CreateConnectorInExistingFlexoConcept createsElementInExistingFlexoConcept() {
		if (getAction().getFlexoRoleCreationStrategy() instanceof ConnectorRoleCreationStrategy) {
			return new CreateConnectorInExistingFlexoConcept((ConnectorRoleCreationStrategy) getAction().getFlexoRoleCreationStrategy());
		}
		return null;
	}

	@Override
	public ConfigureCreateNewFlexoConceptFromConnectorStep<?> chooseNewFlexoConcept() {
		if (getAction().getFlexoConceptCreationStrategy() instanceof MapConnectorToFlexoConceptlnstanceStrategy) {
			return new ConfigureMapConnectorToFlexoConceptlnstanceStep(
					(MapConnectorToFlexoConceptlnstanceStrategy) getAction().getFlexoConceptCreationStrategy());
		}
		else if (getAction().getFlexoConceptCreationStrategy() instanceof MapConnectorToIndividualStrategy) {
			return new ConfigureMapConnectorToIndividualStep(
					(MapConnectorToIndividualStrategy) getAction().getFlexoConceptCreationStrategy());
		}
		else if (getAction().getFlexoConceptCreationStrategy() instanceof BlankFlexoConceptFromConnectorCreationStrategy) {
			return new ConfigureBlankFlexoConceptFromConnectorCreationStrategyStep(
					(BlankFlexoConceptFromConnectorCreationStrategy) getAction().getFlexoConceptCreationStrategy());
		}
		return null;
	}

	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/ReplaceConnectorInExistingFlexoConcept.fib")
	public class ReplaceConnectorInExistingFlexoConcept
			extends ConfigureGraphicalElementRoleSettingStrategyStep<ConnectorRoleSettingStrategy> {

		public ReplaceConnectorInExistingFlexoConcept(ConnectorRoleSettingStrategy strategy) {
			super(strategy);
			if (getAction().getFlexoConcept() == null && getAction().getVirtualModel() != null
					&& getAction().getVirtualModel().getFlexoConcepts().size() > 0) {
				getAction().setFlexoConcept(getAction().getVirtualModel().getFlexoConcepts().get(0));
			}
			if (getStrategy().getFlexoRole() == null && getAction().getFlexoConcept() != null && getAvailableFlexoRoles().size() > 0) {
				getStrategy().setFlexoRole(getAvailableFlexoRoles().get(0));
			}
		}

		@Override
		public DeclareConnectorInFlexoConcept getAction() {
			return super.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("choose_which_connector_you_want_to_set_or_replace");
		}

		public FlexoConcept getFlexoConcept() {
			return getAction().getFlexoConcept();
		}

		public void setFlexoConcept(FlexoConcept flexoConcept) {
			if (flexoConcept != getFlexoConcept()) {
				FlexoConcept oldValue = getFlexoConcept();
				getAction().setFlexoConcept(flexoConcept);
				getPropertyChangeSupport().firePropertyChange("flexoConcept", oldValue, flexoConcept);
				getPropertyChangeSupport().firePropertyChange("availableFlexoRoles", null, getAvailableFlexoRoles());
				checkValidity();
			}
		}

		public List<ConnectorRole> getAvailableFlexoRoles() {
			return getStrategy().getAvailableFlexoRoles();
		}

		public ConnectorRole getFlexoRole() {
			return getStrategy().getFlexoRole();
		}

		public void setFlexoRole(ConnectorRole connectorRole) {
			if (connectorRole != getFlexoRole()) {
				ConnectorRole oldValue = getFlexoRole();
				getStrategy().setFlexoRole(connectorRole);
				getPropertyChangeSupport().firePropertyChange("connectorRole", oldValue, connectorRole);
				checkValidity();
			}
		}

		/*	@Override
			public boolean isValid() {
		
				if (getFlexoRole() == null) {
					setIssueMessage(FlexoLocalization.localizedForKey(FLEXO_ROLE_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				if (getFlexoConcept() == null) {
					setIssueMessage(FlexoLocalization.localizedForKey(FLEXO_CONCEPT_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				return true;
			}*/

	}

	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/CreateConnectorInExistingFlexoConcept.fib")
	public class CreateConnectorInExistingFlexoConcept
			extends ConfigureGraphicalElementRoleCreationStrategyStep<ConnectorRoleCreationStrategy> {

		public CreateConnectorInExistingFlexoConcept(ConnectorRoleCreationStrategy strategy) {
			super(strategy);
			if (getAction().getFlexoConcept() == null && getAction().getVirtualModel() != null
					&& getAction().getVirtualModel().getFlexoConcepts().size() > 0) {
				getAction().setFlexoConcept(getAction().getVirtualModel().getFlexoConcepts().get(0));
			}

			if (StringUtils.isEmpty(getStrategy().getNewRoleName()) && getFlexoConcept() != null) {
				getStrategy().setNewRoleName(getFlexoConcept().getAvailablePropertyName(getStrategy().getDefaultRoleName()));
			}

		}

		@Override
		public DeclareConnectorInFlexoConcept getAction() {
			return super.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("create_a_new_connector_role");
		}

		public FlexoConcept getFlexoConcept() {
			return getAction().getFlexoConcept();
		}

		public void setFlexoConcept(FlexoConcept flexoConcept) {
			if (flexoConcept != getFlexoConcept()) {
				FlexoConcept oldValue = getFlexoConcept();
				getAction().setFlexoConcept(flexoConcept);
				getPropertyChangeSupport().firePropertyChange("flexoConcept", oldValue, flexoConcept);
				getPropertyChangeSupport().firePropertyChange("newConnectorRoleName", null, getNewConnectorRoleName());
				checkValidity();
			}
		}

		public String getNewConnectorRoleName() {
			return getStrategy().getNewRoleName();
		}

		public void setNewConnectorRoleName(String newConnectorRoleName) {
			if (newConnectorRoleName != getNewConnectorRoleName()) {
				String oldValue = getNewConnectorRoleName();
				getStrategy().setNewRoleName(newConnectorRoleName);
				getPropertyChangeSupport().firePropertyChange("newConnectorRoleName", oldValue, newConnectorRoleName);
				checkValidity();
			}
		}

		/*	@Override
			public boolean isValid() {
		
				if (StringUtils.isEmpty(getNewConnectorRoleName())) {
					setIssueMessage(FlexoLocalization.localizedForKey(NEW_SHAPE_ROLE_NAME_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				if (getFlexoConcept() == null) {
					setIssueMessage(FlexoLocalization.localizedForKey(FLEXO_CONCEPT_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
		
				if (getFlexoConcept().getAccessibleProperty(getNewConnectorRoleName()) != null) {
					setIssueMessage(FlexoLocalization.localizedForKey(NEW_SHAPE_ROLE_NAME_ALREADY_EXISTS), IssueMessageType.ERROR);
					return false;
				}
				return true;
			}*/

	}

	public abstract class ConfigureCreateNewFlexoConceptFromConnectorStep<S extends FlexoConceptFromConnectorCreationStrategy>
			extends ConfigureCreateNewFlexoConceptFromDiagramElementStep<S> {

		public ConfigureCreateNewFlexoConceptFromConnectorStep(S strategy) {
			super(strategy);
		}
	}

	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/ConfigureMapConnectorToFlexoConceptlnstanceStep.fib")
	public class ConfigureMapConnectorToFlexoConceptlnstanceStep
			extends ConfigureCreateNewFlexoConceptFromConnectorStep<MapConnectorToFlexoConceptlnstanceStrategy> {

		public ConfigureMapConnectorToFlexoConceptlnstanceStep(MapConnectorToFlexoConceptlnstanceStrategy strategy) {
			super(strategy);
		}

		@Override
		public DeclareConnectorInFlexoConcept getAction() {
			return super.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales()
					.localizedForKey("create_new_flexo_concept_where_connector_is_mapped_to_a_flexo_concept_instance");
		}

		public String getFlexoConceptInstanceRoleName() {
			return getStrategy().getFlexoConceptInstanceRoleName();
		}

		public void setFlexoConceptInstanceRoleName(String flexoConceptInstanceRoleName) {
			if (!flexoConceptInstanceRoleName.equals(getFlexoConceptInstanceRoleName())) {
				String oldValue = getFlexoConceptInstanceRoleName();
				getStrategy().setFlexoConceptInstanceRoleName(flexoConceptInstanceRoleName);
				getPropertyChangeSupport().firePropertyChange("flexoConceptInstanceRoleName", oldValue, flexoConceptInstanceRoleName);
				checkValidity();
			}
		}

		public FlexoConcept getTypeConcept() {
			return getStrategy().getTypeConcept();
		}

		public void setTypeConcept(FlexoConcept typeConcept) {
			if (typeConcept != getTypeConcept()) {
				FlexoConcept oldValue = getTypeConcept();
				getStrategy().setTypeConcept(typeConcept);
				getPropertyChangeSupport().firePropertyChange("typeConcept", oldValue, typeConcept);
				getPropertyChangeSupport().firePropertyChange("flexoConceptInstanceRoleName", null, getFlexoConceptName());
				checkValidity();
			}
		}

		@Override
		public void setModelSlot(ModelSlot<?> modelSlot) {
			super.setModelSlot(modelSlot);
			getPropertyChangeSupport().firePropertyChange("typeConcept", null, getTypeConcept());
		}

	}

	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/ConfigureMapConnectorToIndividualStep.fib")
	public class ConfigureMapConnectorToIndividualStep
			extends ConfigureCreateNewFlexoConceptFromConnectorStep<MapConnectorToIndividualStrategy> {

		public ConfigureMapConnectorToIndividualStep(MapConnectorToIndividualStrategy strategy) {
			super(strategy);
		}

		@Override
		public DeclareConnectorInFlexoConcept getAction() {
			return super.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("create_new_flexo_concept_where_connector_is_mapped_to_a_single_individual");
		}

		public String getIndividualFlexoRoleName() {
			return getStrategy().getIndividualFlexoRoleName();
		}

		public void setIndividualFlexoRoleName(String individualFlexoRoleName) {
			if (!individualFlexoRoleName.equals(getIndividualFlexoRoleName())) {
				String oldValue = getIndividualFlexoRoleName();
				getStrategy().setIndividualFlexoRoleName(individualFlexoRoleName);
				getPropertyChangeSupport().firePropertyChange("individualFlexoRoleName", oldValue, individualFlexoRoleName);
				checkValidity();
			}
		}

		public IFlexoOntologyClass<?> getConcept() {
			return getStrategy().getConcept();
		}

		public void setConcept(IFlexoOntologyClass<?> concept) {
			if (concept != getConcept()) {
				IFlexoOntologyClass<?> oldValue = getConcept();
				getStrategy().setConcept(concept);
				getPropertyChangeSupport().firePropertyChange("concept", oldValue, concept);
				getPropertyChangeSupport().firePropertyChange("individualFlexoRoleName", null, getIndividualFlexoRoleName());
				checkValidity();
			}
		}

		@Override
		public void setModelSlot(ModelSlot<?> modelSlot) {
			super.setModelSlot(modelSlot);
			getPropertyChangeSupport().firePropertyChange("concept", null, getConcept());
		}

	}

	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/ConfigureBlankFlexoConceptFromConnectorCreationStrategyStep.fib")
	public class ConfigureBlankFlexoConceptFromConnectorCreationStrategyStep
			extends ConfigureCreateNewFlexoConceptFromConnectorStep<BlankFlexoConceptFromConnectorCreationStrategy> {

		public ConfigureBlankFlexoConceptFromConnectorCreationStrategyStep(BlankFlexoConceptFromConnectorCreationStrategy strategy) {
			super(strategy);
		}

		@Override
		public DeclareConnectorInFlexoConcept getAction() {
			return super.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("create_new_flexo_concept_without_any_mapping_to_model");
		}

	}

}
