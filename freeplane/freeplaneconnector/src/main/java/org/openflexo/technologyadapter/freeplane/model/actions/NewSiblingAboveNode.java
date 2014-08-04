package org.openflexo.technologyadapter.freeplane.model.actions;

import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.mindmapmode.MMapController;
import org.freeplane.features.mode.Controller;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

import java.util.Vector;

public class NewSiblingAboveNode extends FlexoAction<NewSiblingAboveNode, IFreeplaneNode, IFreeplaneMap> {

	private static final class NewSiblingAboveNodeActionType extends FlexoActionType<NewSiblingAboveNode, IFreeplaneNode, IFreeplaneMap> {

		public NewSiblingAboveNodeActionType() {
			super("add_sibling_above_node", NewChildNode.FREEPLANE_MENU, FlexoActionType.editGroup, FlexoActionType.ADD_ACTION_TYPE);
		}

		@Override
		public NewSiblingAboveNode makeNewAction(final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection,
				final FlexoEditor editor) {
			return new NewSiblingAboveNode(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> globalSelection) {
			return node != null && node.getNodeModel().isVisible() && node.getParent() != null;
		}

		@Override
		public boolean isEnabledForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> globalSelection) {
			return node != null && !node.getNodeModel().getMap().isReadOnly();
		}

	}

	public static final FlexoActionType<NewSiblingAboveNode, IFreeplaneNode, IFreeplaneMap> ACTION_TYPE = new NewSiblingAboveNodeActionType();

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, IFreeplaneNode.class);
	}

	private NewSiblingAboveNode(final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
		super(NewSiblingAboveNode.ACTION_TYPE, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(final Object context) throws FlexoException {
		// Some Copy-paste from freeplane To allow us to update our model.
		final NodeModel brother = ((MMapController) Controller.getCurrentModeController().getMapController())
				.addNewNode(MMapController.NEW_SIBLING_BEFORE);
		this.getFocusedObject().getParent().addFreeplaneChild(brother);
	}

}
