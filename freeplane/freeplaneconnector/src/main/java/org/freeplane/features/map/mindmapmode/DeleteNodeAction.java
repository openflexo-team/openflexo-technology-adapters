package org.freeplane.features.map.mindmapmode;

import java.awt.event.ActionEvent;
import java.util.Iterator;

import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;

public class DeleteNodeAction extends DeleteAction {

    /**
     * 
     */
    private static final long serialVersionUID = 8428363245663275415L;

    public DeleteNodeAction() {
        super();
    }

    /**
     * Copy-paste from freeplane code to remove confirm dialog
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        final ModeController modeController = Controller.getCurrentModeController();
        for (final NodeModel node : modeController.getMapController().getSelectedNodes()) {
            if (node.isRoot()) {
                return;
            }
        }
        final Controller controller = Controller.getCurrentController();

        final MMapController mapController = (MMapController) modeController.getMapController();
        final Iterator<NodeModel> iterator = controller.getSelection().getSortedSelection(true).iterator();
        while (iterator.hasNext()) {
            mapController.deleteNode(iterator.next());
        }
    }
}
