package org.openflexo.technologyadapter.diagram.controller;

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.control.MouseClickControl;
import org.openflexo.fge.control.MouseControlContext;
import org.openflexo.fge.control.actions.MouseClickControlActionImpl;
import org.openflexo.fge.control.actions.MouseClickControlImpl;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.NavigationScheme;
import org.openflexo.foundation.fmlrt.FlexoConceptInstance;
import org.openflexo.foundation.fmlrt.VirtualModelInstance;
import org.openflexo.foundation.fmlrt.action.ActionSchemeAction;
import org.openflexo.foundation.fmlrt.action.ActionSchemeActionType;
import org.openflexo.foundation.fmlrt.action.NavigationSchemeAction;
import org.openflexo.foundation.fmlrt.action.NavigationSchemeActionType;
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
	private VirtualModelInstance vmInstance;

	public FMLControlledDiagramMouseClickControl(GraphicalElementAction.ActionMask mask, GraphicalElementRole<?, ?> patternRole,
			VirtualModelInstance vmInstance, EditingContext editingContext) {
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
