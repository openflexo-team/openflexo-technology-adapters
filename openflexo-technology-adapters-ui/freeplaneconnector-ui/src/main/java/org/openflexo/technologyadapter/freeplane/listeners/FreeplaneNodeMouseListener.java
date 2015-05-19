/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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
		if (!isInside(e)) {
			return;
		}
        this.nodeSelector.stopTimerForDelayedSelection();

        final NodeView nodeV = this.nodeSelector.getRelatedNodeView(e);
		Controller.getCurrentController().getSelection().selectAsTheOnlyOneSelected(nodeV.getModel());
        Controller.getCurrentModeController().getMapController().onSelect(nodeV.getModel());
    }

    /**
	 * Wasn't public on node selector ==> CopyPaste
	 * 
	 * @param e
	 * @return
	 */
    private static boolean isInside(final MouseEvent e) {
        return new Rectangle(0, 0, e.getComponent().getWidth(), e.getComponent().getHeight()).contains(e.getPoint());
    }
}
