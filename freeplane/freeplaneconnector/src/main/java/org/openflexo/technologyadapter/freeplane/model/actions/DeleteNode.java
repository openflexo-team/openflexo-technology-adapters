package org.openflexo.technologyadapter.freeplane.model.actions;

import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.mindmapmode.MMapController;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

import java.util.Vector;

public class DeleteNode extends FlexoAction<DeleteNode, IFreeplaneNode, IFreeplaneMap> {

	private static final class DeleteNodeActionType extends FlexoActionType<DeleteNode, IFreeplaneNode, IFreeplaneMap> {

		public DeleteNodeActionType() {
			super("delete_node", NewChildNode.FREEPLANE_MENU, FlexoActionType.editGroup, FlexoActionType.DELETE_ACTION_TYPE);
		}

		@Override
		public DeleteNode makeNewAction(final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection,
				final FlexoEditor editor) {
			return new DeleteNode(focusedObject, globalSelection, editor);
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

	public static final FlexoActionType<DeleteNode, IFreeplaneNode, IFreeplaneMap> ACTION_TYPE = new DeleteNodeActionType();

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, IFreeplaneNode.class);
	}

	public DeleteNode(final IFreeplaneNode focusedObject,
			final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
		super(DeleteNode.ACTION_TYPE, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(final Object context) throws FlexoException {
		// Some Copy-paste from freeplane To allow us to update our model.
		final ModeController modeController = Controller.getCurrentModeController();
		for (final NodeModel node : modeController.getMapController().getSelectedNodes()) {
			if (node.isRoot()) {
				return;
			}
		}
		final Controller controller = Controller.getCurrentController();

		final MMapController mapController = (MMapController) modeController.getMapController();
		for (NodeModel nodeModel : controller.getSelection().getSortedSelection(true)) {
			mapController.deleteNode(nodeModel);
		}
		// Model not up-up-to-date, implement a deleter(NodeModel).
	}

}
