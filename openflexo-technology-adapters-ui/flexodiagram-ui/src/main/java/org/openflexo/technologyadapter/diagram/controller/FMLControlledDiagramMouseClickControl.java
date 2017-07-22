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

package org.openflexo.technologyadapter.diagram.controller;

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.control.MouseClickControl;
import org.openflexo.fge.control.MouseControlContext;
import org.openflexo.fge.control.actions.MouseClickControlActionImpl;
import org.openflexo.fge.control.actions.MouseClickControlImpl;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.NavigationScheme;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionType;
import org.openflexo.foundation.fml.rt.action.NavigationSchemeAction;
import org.openflexo.foundation.fml.rt.action.NavigationSchemeActionType;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DiagramEditor;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementAction;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementAction.ActionMask;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

/**
 * A {@link MouseClickControl} which handle special controls required for diagram edition in FML-controlled context
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramMouseClickControl extends MouseClickControlImpl<DiagramEditor> {

	private GraphicalElementRole<?, ?> patternRole = null;
	private GraphicalElementAction.ActionMask mask;
	private FMLRTVirtualModelInstance vmInstance;

	public FMLControlledDiagramMouseClickControl(GraphicalElementAction.ActionMask mask, GraphicalElementRole<?, ?> patternRole,
			FMLRTVirtualModelInstance vmInstance, EditingContext editingContext) {
		super(mask.name(), MouseButton.LEFT, mask == ActionMask.DoubleClick ? 2 : 1, null, mask == ActionMask.ShiftClick,
				mask == ActionMask.CtrlClick, mask == ActionMask.MetaClick, mask == ActionMask.AltClick, editingContext);
		this.vmInstance = vmInstance;
		this.patternRole = patternRole;
		this.mask = mask;
		setControlAction(new MouseClickControlActionImpl<DiagramEditor>() {
			@Override
			public boolean handleClick(DrawingTreeNode<?, ?> dtn, DiagramEditor controller, MouseControlContext context) {
				return performHandleClick(dtn, controller, context);
			}
		});
	}

	private boolean performHandleClick(DrawingTreeNode<?, ?> dtn, DiagramEditor controller, MouseControlContext context) {
		FlexoEditor editor = controller.getFlexoController().getEditor();
		DiagramElement<?> diagramElement = null;
		if (dtn.getDrawable() instanceof DiagramElement) {
			diagramElement = (DiagramElement<?>) dtn.getDrawable();
		}
		if (diagramElement != null) {
			FlexoConceptInstance epi = diagramElement.getFlexoConceptInstance(vmInstance);
			if (epi != null) {
				for (GraphicalElementAction action : patternRole.getActions(mask)) {
					if (action.evaluateCondition(epi)) {
						if (action.getAbstractActionScheme() instanceof NavigationScheme) {
							NavigationSchemeAction navigationAction = new NavigationSchemeActionType(
									(NavigationScheme) action.getAbstractActionScheme(), epi).makeNewAction(epi, null, editor);
							navigationAction.doAction();
							return true;
						} else if (action.getAbstractActionScheme() instanceof ActionScheme) {
							ActionSchemeAction actionAction = new ActionSchemeActionType((ActionScheme) action.getAbstractActionScheme(),
									epi).makeNewAction(epi, null, editor);
							actionAction.doAction();
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
