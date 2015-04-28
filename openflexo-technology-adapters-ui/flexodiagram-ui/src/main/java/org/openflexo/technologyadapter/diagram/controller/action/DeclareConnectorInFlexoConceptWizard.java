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

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.fib.annotation.FIBPanel;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareConnectorInFlexoConcept;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareConnectorInFlexoConcept.NewFlexoConceptChoices;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class DeclareConnectorInFlexoConceptWizard extends AbstractDeclareInFlexoConceptWizard<DeclareConnectorInFlexoConcept> {

	private static final String FLEXO_ROLE_IS_NULL = FlexoLocalization.localizedForKey("please_choose_flexo_role");
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

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DeclareConnectorInFlexoConceptWizard.class.getPackage().getName());

	public DeclareConnectorInFlexoConceptWizard(DeclareConnectorInFlexoConcept action, FlexoController controller) {
		super(action, controller);
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("declare_connector_in_flexo_concept");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(FMLIconLibrary.FLEXO_CONCEPT_MEDIUM_ICON, DiagramIconLibrary.CONNECTOR_MARKER).getImage();
	}

	@Override
	public ReplaceConnectorInExistingFlexoConcept replaceElementInExistingFlexoConcept() {
		return new ReplaceConnectorInExistingFlexoConcept();
	}

	@Override
	public WizardStep createsElementInExistingFlexoConcept() {
		return new CreateConnectorInExistingFlexoConcept();
	}

	@Override
	public WizardStep chooseNewFlexoConcept() {
		return new CreateNewFlexoConceptWithConnector();
	}

	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/ReplaceConnectorInExistingFlexoConcept.fib")
	public class ReplaceConnectorInExistingFlexoConcept extends WizardStep {

		public ReplaceConnectorInExistingFlexoConcept() {
			if (getAction().getFlexoConcept() == null && getAction().getVirtualModel() != null
					&& getAction().getVirtualModel().getFlexoConcepts().size() > 0) {
				getAction().setFlexoConcept(getAction().getVirtualModel().getFlexoConcepts().get(0));
			}
			if (getAction().getFlexoRole() == null && getAction().getFlexoConcept() != null && getAvailableFlexoRoles().size() > 0) {
				getAction().setFlexoRole(getAvailableFlexoRoles().get(0));
			}
		}

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public DeclareConnectorInFlexoConcept getAction() {
			return DeclareConnectorInFlexoConceptWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return FlexoLocalization.localizedForKey("choose_which_connector_you_want_to_set_or_replace");
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
			return getAction().getAvailableFlexoRoles();
		}

		public ConnectorRole getFlexoRole() {
			return getAction().getFlexoRole();
		}

		public void setFlexoRole(ConnectorRole ConnectorRole) {
			if (ConnectorRole != getFlexoRole()) {
				ConnectorRole oldValue = getFlexoRole();
				getAction().setFlexoRole(ConnectorRole);
				getPropertyChangeSupport().firePropertyChange("ConnectorRole", oldValue, ConnectorRole);
				checkValidity();
			}
		}

		@Override
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
		}

	}

	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/CreateConnectorInExistingFlexoConcept.fib")
	public class CreateConnectorInExistingFlexoConcept extends WizardStep {

		public CreateConnectorInExistingFlexoConcept() {
			if (getAction().getFlexoConcept() == null && getAction().getVirtualModel() != null
					&& getAction().getVirtualModel().getFlexoConcepts().size() > 0) {
				getAction().setFlexoConcept(getAction().getVirtualModel().getFlexoConcepts().get(0));
			}

			if (StringUtils.isEmpty(getAction().getConnectorRoleName()) && getFlexoConcept() != null) {
				getAction().setConnectorRoleName(
						getFlexoConcept().getAvailablePropertyName(DeclareConnectorInFlexoConcept.DEFAULT_ROLE_NAME));
			}

		}

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public DeclareConnectorInFlexoConcept getAction() {
			return DeclareConnectorInFlexoConceptWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return FlexoLocalization.localizedForKey("create_a_new_connector_role");
		}

		public FlexoConcept getFlexoConcept() {
			return getAction().getFlexoConcept();
		}

		public void setFlexoConcept(FlexoConcept flexoConcept) {
			if (flexoConcept != getFlexoConcept()) {
				FlexoConcept oldValue = getFlexoConcept();
				getAction().setFlexoConcept(flexoConcept);
				getPropertyChangeSupport().firePropertyChange("flexoConcept", oldValue, flexoConcept);
				getPropertyChangeSupport().firePropertyChange("connectorRoleName", null, getConnectorRoleName());
				checkValidity();
			}
		}

		public String getConnectorRoleName() {
			return getAction().getConnectorRoleName();
		}

		public void setConnectorRoleName(String newConnectorRoleName) {
			if (newConnectorRoleName != getConnectorRoleName()) {
				String oldValue = getConnectorRoleName();
				getAction().setConnectorRoleName(newConnectorRoleName);
				getPropertyChangeSupport().firePropertyChange("connectorRoleName", oldValue, newConnectorRoleName);
				checkValidity();
			}
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getConnectorRoleName())) {
				setIssueMessage(FlexoLocalization.localizedForKey(NEW_CONNECTOR_ROLE_NAME_IS_NULL), IssueMessageType.ERROR);
				return false;
			}
			if (getFlexoConcept() == null) {
				setIssueMessage(FlexoLocalization.localizedForKey(FLEXO_CONCEPT_IS_NULL), IssueMessageType.ERROR);
				return false;
			}

			if (getFlexoConcept().getAccessibleProperty(getConnectorRoleName()) != null) {
				setIssueMessage(FlexoLocalization.localizedForKey(NEW_CONNECTOR_ROLE_NAME_ALREADY_EXISTS), IssueMessageType.ERROR);
				return false;
			}
			return true;
		}

	}

	@FIBPanel("Fib/Wizard/DeclareInFlexoConcept/CreateNewFlexoConceptWithConnector.fib")
	public class CreateNewFlexoConceptWithConnector extends WizardStep {

		public DeclareConnectorInFlexoConcept getAction() {
			return DeclareConnectorInFlexoConceptWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return FlexoLocalization.localizedForKey("create_new_flexo_concept");
		}

		public NewFlexoConceptChoices getPatternChoice() {
			return getAction().getPatternChoice();
		}

		public void setPatternChoice(NewFlexoConceptChoices patternChoice) {
			if (patternChoice != getPatternChoice()) {
				NewFlexoConceptChoices oldValue = getPatternChoice();
				getAction().setPatternChoice(patternChoice);
				getPropertyChangeSupport().firePropertyChange("patternChoice", oldValue, patternChoice);
				getPropertyChangeSupport().firePropertyChange("modelSlot", null, getModelSlot());
				getPropertyChangeSupport().firePropertyChange("flexoConceptName", null, getFlexoConceptName());
				getPropertyChangeSupport().firePropertyChange("individualFlexoRoleName", null, getIndividualFlexoRoleName());
				getPropertyChangeSupport().firePropertyChange("concept", null, getConcept());
				getPropertyChangeSupport().firePropertyChange("virtualModelFlexoRoleName", null, getVirtualModelFlexoRoleName());
				getPropertyChangeSupport().firePropertyChange("virtualModelConcept", null, getVirtualModelConcept());
				getPropertyChangeSupport().firePropertyChange("adressedFlexoMetaModel", null, getAdressedFlexoMetaModel());
				getPropertyChangeSupport().firePropertyChange("adressedVirtualModel", null, getAdressedVirtualModel());
			}
		}

		public String getFlexoConceptName() {
			return getAction().getFlexoConceptName();
		}

		public void setFlexoConceptName(String flexoConceptName) {
			if (!flexoConceptName.equals(getFlexoConceptName())) {
				String oldValue = getFlexoConceptName();
				getAction().setFlexoConceptName(flexoConceptName);
				getPropertyChangeSupport().firePropertyChange("flexoConceptName", oldValue, flexoConceptName);
				checkValidity();
			}
		}

		public String getIndividualFlexoRoleName() {
			return getAction().getIndividualFlexoRoleName();
		}

		public void setIndividualFlexoRoleName(String individualFlexoRoleName) {
			if (!individualFlexoRoleName.equals(getIndividualFlexoRoleName())) {
				String oldValue = getIndividualFlexoRoleName();
				getAction().setIndividualFlexoRoleName(individualFlexoRoleName);
				getPropertyChangeSupport().firePropertyChange("individualFlexoRoleName", oldValue, individualFlexoRoleName);
				checkValidity();
			}
		}

		public IFlexoOntologyClass<?> getConcept() {
			return getAction().getConcept();
		}

		public void setConcept(IFlexoOntologyClass<?> concept) {
			if (concept != getConcept()) {
				IFlexoOntologyClass<?> oldValue = getConcept();
				getAction().setConcept(concept);
				getPropertyChangeSupport().firePropertyChange("concept", oldValue, concept);
				getPropertyChangeSupport().firePropertyChange("individualFlexoRoleName", null, getIndividualFlexoRoleName());
				getPropertyChangeSupport().firePropertyChange("flexoConceptName", null, getFlexoConceptName());
				checkValidity();
			}
		}

		public IFlexoOntologyObjectProperty<?> getObjectProperty() {
			return getAction().getObjectProperty();
		}

		public void setObjectProperty(IFlexoOntologyObjectProperty<?> objectProperty) {
			if (objectProperty != getObjectProperty()) {
				IFlexoOntologyObjectProperty<?> oldValue = getObjectProperty();
				getAction().setObjectProperty(objectProperty);
				getPropertyChangeSupport().firePropertyChange("objectProperty", oldValue, objectProperty);
				getPropertyChangeSupport().firePropertyChange("individualFlexoRoleName", null, getIndividualFlexoRoleName());
				getPropertyChangeSupport().firePropertyChange("flexoConceptName", null, getFlexoConceptName());
				checkValidity();
			}
		}

		public String getVirtualModelFlexoRoleName() {
			return getAction().getVirtualModelFlexoRoleName();
		}

		public void setVirtualModelFlexoRoleName(String virtualModelFlexoRoleName) {
			if (!virtualModelFlexoRoleName.equals(getVirtualModelFlexoRoleName())) {
				String oldValue = getVirtualModelFlexoRoleName();
				getAction().setVirtualModelFlexoRoleName(virtualModelFlexoRoleName);
				getPropertyChangeSupport().firePropertyChange("virtualModelFlexoRoleName", oldValue, virtualModelFlexoRoleName);
				checkValidity();
			}
		}

		public String getObjectPropertyStatementFlexoRoleName() {
			return getAction().getObjectPropertyStatementFlexoRoleName();
		}

		public void setObjectPropertyStatementFlexoRoleName(String objectPropertyStatementFlexoRoleName) {
			if (!objectPropertyStatementFlexoRoleName.equals(getObjectPropertyStatementFlexoRoleName())) {
				String oldValue = getObjectPropertyStatementFlexoRoleName();
				getAction().setObjectPropertyStatementFlexoRoleName(objectPropertyStatementFlexoRoleName);
				getPropertyChangeSupport().firePropertyChange("objectPropertyStatementFlexoRoleName", oldValue,
						objectPropertyStatementFlexoRoleName);
				checkValidity();
			}
		}

		public FlexoConcept getVirtualModelConcept() {
			return getAction().getVirtualModelConcept();
		}

		public void setVirtualModelConcept(FlexoConcept virtualModelConcept) {
			if (virtualModelConcept != getVirtualModelConcept()) {
				FlexoConcept oldValue = getVirtualModelConcept();
				getAction().setVirtualModelConcept(virtualModelConcept);
				getPropertyChangeSupport().firePropertyChange("virtualModelConcept", oldValue, virtualModelConcept);
				getPropertyChangeSupport().firePropertyChange("flexoConceptName", null, getFlexoConceptName());
				checkValidity();
			}
		}

		public ModelSlot<?> getModelSlot() {
			return getAction().getModelSlot();
		}

		public void setModelSlot(ModelSlot<?> modelSlot) {
			if (modelSlot != getModelSlot()) {
				ModelSlot<?> oldValue = getModelSlot();
				getAction().setModelSlot(modelSlot);
				getPropertyChangeSupport().firePropertyChange("modelSlot", oldValue, modelSlot);
				getPropertyChangeSupport().firePropertyChange("concept", null, getConcept());
				getPropertyChangeSupport().firePropertyChange("adressedFlexoMetaModel", null, getAdressedFlexoMetaModel());
				getPropertyChangeSupport().firePropertyChange("adressedVirtualModel", null, getAdressedVirtualModel());
				checkValidity();
			}
		}

		public FlexoMetaModel<?> getAdressedFlexoMetaModel() {
			return getAction().getAdressedFlexoMetaModel();
		}

		public VirtualModel getAdressedVirtualModel() {
			return getAction().getAdressedVirtualModel();
		}

		public String getLinkSchemeName() {
			return getAction().getLinkSchemeName();
		}

		public void setLinkSchemeName(String linkSchemeName) {
			if (!linkSchemeName.equals(getLinkSchemeName())) {
				String oldValue = getLinkSchemeName();
				getAction().setLinkSchemeName(linkSchemeName);
				getPropertyChangeSupport().firePropertyChange("linkSchemeName", oldValue, linkSchemeName);
				checkValidity();
			}
		}

		public FlexoConcept getFromFlexoConcept() {
			return getAction().getFromFlexoConcept();
		}

		public void setFromFlexoConcept(FlexoConcept fromFlexoConcept) {
			if (fromFlexoConcept != getFromFlexoConcept()) {
				FlexoConcept oldValue = getFromFlexoConcept();
				getAction().setFromFlexoConcept(fromFlexoConcept);
				getPropertyChangeSupport().firePropertyChange("fromFlexoConcept", oldValue, fromFlexoConcept);
				checkValidity();
			}
		}

		public FlexoConcept getToFlexoConcept() {
			return getAction().getToFlexoConcept();
		}

		public void setToFlexoConcept(FlexoConcept toFlexoConcept) {
			if (toFlexoConcept != getToFlexoConcept()) {
				FlexoConcept oldValue = getToFlexoConcept();
				getAction().setToFlexoConcept(toFlexoConcept);
				getPropertyChangeSupport().firePropertyChange("toFlexoConcept", oldValue, toFlexoConcept);
				checkValidity();
			}
		}

		@Override
		public boolean isValid() {
			switch (getPatternChoice()) {
			case MAP_SINGLE_INDIVIDUAL:
				if (StringUtils.isEmpty(getFlexoConceptName())) {
					setIssueMessage(FlexoLocalization.localizedForKey(FLEXO_CONCEPT_NAME_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				if (StringUtils.isEmpty(getIndividualFlexoRoleName())) {
					setIssueMessage(FlexoLocalization.localizedForKey(INDIVIDUAL_FLEXO_ROLE_NAME_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				if (getConcept() == null) {
					setIssueMessage(FlexoLocalization.localizedForKey(CONCEPT_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				if (getAction().getSelectedEntriesCount() == 0) {
					setIssueMessage(FlexoLocalization.localizedForKey(NO_SELECTED_ENTRY), IssueMessageType.ERROR);
					return false;
				}
				return true;

			case MAP_SINGLE_FLEXO_CONCEPT:
				if (StringUtils.isEmpty(getFlexoConceptName())) {
					setIssueMessage(FlexoLocalization.localizedForKey(FLEXO_CONCEPT_NAME_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				if (StringUtils.isEmpty(getVirtualModelFlexoRoleName())) {
					setIssueMessage(FlexoLocalization.localizedForKey(VIRTUAL_MODEL_FLEXO_ROLE_NAME_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				if (getVirtualModelConcept() == null) {
					setIssueMessage(FlexoLocalization.localizedForKey(VIRTUAL_MODEL_CONCEPT_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				if (getAction().getSelectedEntriesCount() == 0) {
					setIssueMessage(FlexoLocalization.localizedForKey(NO_SELECTED_ENTRY), IssueMessageType.ERROR);
					return false;
				}
				return true;

			case BLANK_FLEXO_CONCEPT:
				if (StringUtils.isEmpty(getFlexoConceptName())) {
					setIssueMessage(FlexoLocalization.localizedForKey(FLEXO_CONCEPT_NAME_IS_NULL), IssueMessageType.ERROR);
					return false;
				}
				if (getAction().getSelectedEntriesCount() == 0) {
					setIssueMessage(FlexoLocalization.localizedForKey(NO_SELECTED_ENTRY), IssueMessageType.ERROR);
					return false;
				}
			default:
				break;
			}
			return false;
		}
	}

}
