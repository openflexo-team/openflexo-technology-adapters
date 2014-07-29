package org.openflexo.technologyadapter.freeplane.listeners;

import org.freeplane.features.map.INodeChangeListener;
import org.freeplane.features.map.NodeChangeEvent;
import org.freeplane.features.mode.Controller;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

/**
 * Created by eloubout on 29/07/14.
 */
public class FreeplaneNodeChangeListener implements INodeChangeListener {

	private final IFreeplaneMap resourceData;

	/**
	 * Listener add himself to current MapController.
	 *
	 * @param map IFreeplaneMap accessed
	 */
	public FreeplaneNodeChangeListener(IFreeplaneMap map) {
		this.resourceData = map;
		Controller.getCurrentModeController().getMapController().addNodeChangeListener(this);
	}

	@Override public void nodeChanged(NodeChangeEvent event) {
		this.resourceData.setModified(true);
	}
}
