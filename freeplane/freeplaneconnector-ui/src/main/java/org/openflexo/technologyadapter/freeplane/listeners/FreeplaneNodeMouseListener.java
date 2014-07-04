package org.openflexo.technologyadapter.freeplane.listeners;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import org.freeplane.features.mode.Controller;
import org.freeplane.view.swing.map.NodeView;
import org.freeplane.view.swing.ui.mindmapmode.MNodeMotionListener;
import org.openflexo.view.controller.FlexoController;

public class FreeplaneNodeMouseListener extends MNodeMotionListener {

    private final FlexoController controller;

    public FreeplaneNodeMouseListener(final FlexoController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public void showPopupMenu(final MouseEvent e) {
        this.triggerSelection(e);
        if (!e.isPopupTrigger()) {
            return;
        }
        if (this.controller.getSelectionManager().getSelection().size() == 1) {
            this.controller.objectWasRightClicked(this.controller.getSelectionManager().getSelection().get(0), e);
        }
    }

    /**
     * See inheritance of freeplane, but just a copy paste with a control
     * statement removed, to force selection.
     * 
     * @param e
     *            MouseEvent
     */
    private void triggerSelection(final MouseEvent e) {
        if (!isInside(e))
            return;
        this.nodeSelector.stopTimerForDelayedSelection();

        final NodeView nodeV = this.nodeSelector.getRelatedNodeView(e);
        final Controller controller = Controller.getCurrentController();
        controller.getSelection().selectAsTheOnlyOneSelected(nodeV.getModel());
        Controller.getCurrentModeController().getMapController().onSelect(nodeV.getModel());
    }

    /**
     * Wasn't public on node soloctor ==> CopyPaste
     * 
     * @param e
     * @return
     */
    private static boolean isInside(final MouseEvent e) {
        return new Rectangle(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight()).contains(e.getPoint());
    }
}
