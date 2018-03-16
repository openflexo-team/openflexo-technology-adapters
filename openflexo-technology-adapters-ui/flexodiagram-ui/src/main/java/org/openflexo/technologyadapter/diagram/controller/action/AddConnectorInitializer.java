/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.diana.Drawing.ConnectorNode;
import org.openflexo.diana.swing.view.JConnectorView;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramModuleView;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramModuleView;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.AddConnector;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class AddConnectorInitializer extends ActionInitializer<AddConnector, DiagramShape, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public AddConnectorInitializer(ControllerActionInitializer actionInitializer) {
		super(AddConnector.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionRunnable<AddConnector, DiagramShape, DiagramElement<?>> getDefaultInitializer() {
		return (e, action) -> {
			if (action.getAutomaticallyCreateConnector()) {
				/*String newName = FlexoController.askForString(FlexoLocalization.localizedForKey("name_for_new_connector"));
				if (newName == null || StringUtils.isEmpty(newName)) {
					return false;
				}*/
				action.setNewConnectorName("");
				return true;
			}

			/*	DynamicDropDownParameter<Role> roles = new DynamicDropDownParameter<Role>("parentRole", "role_that_will_be_specialized", availableRoles, availableRoles.firstElement());
			roles.setShowReset(false);
			roles.setFormatter("name");
			TextFieldParameter annotation = new TextFieldParameter("Annotation","annotation","");
			
			AskParametersDialog dialog = AskParametersDialog.createAskParametersDialog(getProject(), null, FlexoLocalization.localizedForKey("specialize_a_new_role"), FlexoLocalization.localizedForKey("please_select_a_role"), roles, annotation);
			if (dialog.getStatus() == AskParametersDialog.VALIDATE) {
				Role parentRole =  roles.getValue();
				if (parentRole == null)
					return false;
				action.setNewParentRole(parentRole);
				action.setAnnotation(annotation.getValue());
				return true;
			} else {
				return false;
			}*/

			return false;
		};
	}

	@Override
	protected FlexoActionRunnable<AddConnector, DiagramShape, DiagramElement<?>> getDefaultFinalizer() {
		return (e, action) -> {
			if (action.getConnector() != null) {
				getController().getSelectionManager().setSelectedObject(action.getConnector());
				ConnectorNode<DiagramConnector> connectorNode = null;
				JConnectorView<DiagramConnector> connectorView = null;
				if (getController().getCurrentModuleView() instanceof FreeDiagramModuleView) {
					connectorNode = ((FreeDiagramModuleView) getController().getCurrentModuleView()).getEditor().getDrawing()
							.getConnectorNode(action.getConnector());
					connectorView = ((FreeDiagramModuleView) getController().getCurrentModuleView()).getEditor().getDrawingView()
							.connectorViewForNode(connectorNode);
				}
				else if (getController().getCurrentModuleView() instanceof FMLControlledDiagramModuleView) {
					connectorNode = ((FMLControlledDiagramModuleView) getController().getCurrentModuleView()).getEditor().getDrawing()
							.getConnectorNode(action.getConnector());
					connectorView = ((FMLControlledDiagramModuleView) getController().getCurrentModuleView()).getEditor().getDrawingView()
							.connectorViewForNode(connectorNode);
				}
				if (connectorView != null && action.getConnector() != null) {
					if (connectorView.getLabelView() != null) {
						connectorNode.setContinuousTextEditing(true);
						connectorView.getLabelView().startEdition();
					}
				}

			}
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory<AddConnector, DiagramShape, DiagramElement<?>> actionType) {
		return DiagramIconLibrary.CONNECTOR_ICON;
	}

}
