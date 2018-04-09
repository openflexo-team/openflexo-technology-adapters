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

import javax.swing.Icon;

import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.action.LinkSchemeAction;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.ParametersRetriever;

public class LinkSchemeActionInitializer
		extends ActionInitializer<LinkSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> {
	public LinkSchemeActionInitializer(ControllerActionInitializer actionInitializer) {
		super(LinkSchemeAction.class, actionInitializer);
	}

	@Override
	protected FlexoActionRunnable<LinkSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> getDefaultInitializer() {
		return (e, action) -> {
			getController().willExecute(action);
			ParametersRetriever<LinkScheme> parameterRetriever = new ParametersRetriever<>(action,
					getController() != null ? getController().getApplicationContext() : null);
			if (action.escapeParameterRetrievingWhenValid && parameterRetriever.isSkipable()) {
				return true;
			}
			return parameterRetriever.retrieveParameters();
		};

	}

	@Override
	protected FlexoActionRunnable<LinkSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> getDefaultFinalizer() {
		return (e, action) -> {
			// Well, not easy to understand here
			// The new connector has well be added to the diagram, and the drawing (which listen to the diagram) has well received the
			// event
			// The drawing is now up-to-date... but there is something wrong if we are in FML-controlled mode.
			// Since the connector has been added BEFORE the FlexoConceptInstance has been set, the drawing only knows about the
			// DiagamConnector,
			// and not about an FMLControlledDiagramShape. That's why we need to notify again the new diagram element's parent, to be
			// sure that the Drawing can discover that the new connector is FML-controlled
			action.getNewConnector().getParent().getPropertyChangeSupport().firePropertyChange("invalidate", null,
					action.getNewConnector().getParent());

			getController().getSelectionManager().setSelectedObject(action.getNewConnector());
			return true;
		};
	}

	@Override
	protected FlexoExceptionHandler<LinkSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> getDefaultExceptionHandler() {
		return (exception, action) -> {
			if (exception instanceof NotImplementedException) {
				FlexoController.notify(action.getLocales().localizedForKey("not_implemented_yet"));
				return true;
			}
			return false;
		};
	}

	@Override
	protected Icon getEnabledIcon(
			FlexoActionFactory<LinkSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> actionFactory) {
		return DiagramIconLibrary.SHAPE_ICON;
	}

}
