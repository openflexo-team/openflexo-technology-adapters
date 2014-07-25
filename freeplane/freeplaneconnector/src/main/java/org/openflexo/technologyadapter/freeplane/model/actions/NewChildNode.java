package org.openflexo.technologyadapter.freeplane.model.actions;

import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.mindmapmode.MMapController;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.ActionMenu;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

import java.util.Vector;

public class NewChildNode extends FlexoAction<NewChildNode, IFreeplaneNode, IFreeplaneMap> {

	private static final class AddChildNodeActionType extends FlexoActionType<NewChildNode, IFreeplaneNode, IFreeplaneMap> {

		protected AddChildNodeActionType() {
			super("add_child_node", NewChildNode.FREEPLANE_MENU, FlexoActionType.editGroup, FlexoActionType.ADD_ACTION_TYPE);
		}

		@Override
		public NewChildNode makeNewAction(final IFreeplaneNode node, final Vector<IFreeplaneMap> maps, final FlexoEditor flexoEditor) {
			return new NewChildNode(node, maps, flexoEditor);
		}

		@Override
		public boolean isVisibleForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> map) {
			return node != null && node.getNodeModel().isVisible();
		}

		@Override
		public boolean isEnabledForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> map) {
			return node != null && !node.getNodeModel().getMap().isReadOnly();
		}
	}

	public static final ActionMenu FREEPLANE_MENU = new ActionMenu("freeplane_actions", FlexoActionType.defaultGroup);

	public static final FlexoActionType<NewChildNode, IFreeplaneNode, IFreeplaneMap> ACTION_TYPE = new AddChildNodeActionType();

	private NewChildNode(final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
	}

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, IFreeplaneNode.class);
	}

	@Override
	protected void doAction(final Object objet) throws FlexoException {
		// Some Copy-paste from freeplane To allow us to update our model.
		final ModeController modeController = Controller.getCurrentModeController();
		final MMapController mapController = (MMapController) modeController.getMapController();
		final NodeModel child = mapController.addNewNode(MMapController.NEW_CHILD);
		this.getFocusedObject().addChild(child);
	}

}
