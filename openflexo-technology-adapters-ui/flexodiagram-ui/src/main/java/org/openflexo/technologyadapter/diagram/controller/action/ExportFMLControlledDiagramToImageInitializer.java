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

import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.icon.IconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class ExportFMLControlledDiagramToImageInitializer
		extends ActionInitializer<ExportFMLControlledDiagramToImageAction, FlexoConceptInstance, FlexoConceptInstance> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public ExportFMLControlledDiagramToImageInitializer(ControllerActionInitializer actionInitializer) {
		super(ExportFMLControlledDiagramToImageAction.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<ExportFMLControlledDiagramToImageAction, FlexoConceptInstance, FlexoConceptInstance> getDefaultInitializer() {
		return (e, action) -> action.saveAsImage();
	}

	@Override
	protected Icon getEnabledIcon(
			FlexoActionFactory<ExportFMLControlledDiagramToImageAction, FlexoConceptInstance, FlexoConceptInstance> actionType) {
		return IconLibrary.EXPORT_ICON;
	}

}
