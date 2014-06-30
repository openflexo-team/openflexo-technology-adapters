package org.openflexo.technologyadapter.freeplane.listeners;

import java.awt.event.MouseEvent;

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
        if (!e.isPopupTrigger())
            return;
        if (this.controller.getSelectionManager().getSelection().size() == 1) {
            this.controller.objectWasRightClicked(this.controller.getSelectionManager().getSelection().get(0), e);
        }
    }
}
