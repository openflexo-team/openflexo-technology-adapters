package org.freeplane.main.application;

import javax.swing.JFrame;

import org.freeplane.features.mode.Controller;
import org.freeplane.features.ui.IMapViewManager;

/**
 * Avoid NPE on setTitle : we don't have any docking windows, remove the call by
 * overriding setTitle.
 * 
 * @author eloubout
 * 
 */
public class NonNPEApplicationViewController extends ApplicationViewController {

    public NonNPEApplicationViewController(final Controller controller, final IMapViewManager mapViewController, final JFrame frame) {
        super(controller, mapViewController, frame);
    }

    @Override
    public void setTitle(final String frameTitle) {
        this.getJFrame().setTitle(frameTitle);
    }

}
