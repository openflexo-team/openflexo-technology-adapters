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

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.core.ui.IMouseListener;
import org.freeplane.core.ui.IUserInputListenerFactory;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.freeplane.view.swing.map.MainView;
import org.freeplane.view.swing.map.MapView;
import org.freeplane.view.swing.ui.UserInputListenerFactory;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.technologyadapter.freeplane.fml.FMLControlledFreeplaneVirtualModelInstanceNature;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.view.controller.FlexoController;

public class FreeplaneListenersInitilizer {

	private static final Logger LOGGER = Logger.getLogger(FreeplaneListenersInitilizer.class.getSimpleName());

	/**
	 * Prevent external instantiation : static class.
	 */
	private FreeplaneListenersInitilizer() {
	}

	public static void init(final IFreeplaneMap map, final FlexoController controller) {
		Controller.getCurrentModeController().getMapController()
				.addNodeSelectionListener(new FreeplaneNodeSelectionListener(map, controller));
		new FreeplaneNodeChangeListener(map);
		Controller.getCurrentModeController().getMapController().addMapChangeListener(new FreeplaneMapViewChangeListener(map));
		popMenuChangeOnNode(map, controller);
	}

	public static void init(final FMLRTVirtualModelInstance vmInstance, final FlexoController controller) {
		Controller.getCurrentModeController().getMapController()
				.addNodeSelectionListener(new FreeplaneNodeSelectionListener(vmInstance, controller));
		final IFreeplaneMap map = FMLControlledFreeplaneVirtualModelInstanceNature.getMap(vmInstance);
		new FreeplaneNodeChangeListener(map);
		Controller.getCurrentModeController().getMapController().addMapChangeListener(new FreeplaneMapViewChangeListener(map));
		popMenuChangeOnNode(map, controller);
	}

	private static void popMenuChangeOnNode(final IFreeplaneMap map, final FlexoController controller) {
		final String msg = "Error while setting mouse listener on freeplane node";
		final IUserInputListenerFactory factoryToModify = Controller.getCurrentModeController().getUserInputListenerFactory();
		try {
			final Field f = UserInputListenerFactory.class.getDeclaredField("nodeMouseMotionListener");
			final Field f2 = UserInputListenerFactory.class.getDeclaredField("nodeMotionListener");
			f.setAccessible(true);
			f2.setAccessible(true);
			final FreeplaneNodeMouseListener listener = new FreeplaneNodeMouseListener(controller);
			f.set(factoryToModify, listener);
			f2.set(factoryToModify, listener);
			changeRootNodeListener(listener);
		} catch (final NoSuchFieldException e) {
			LOGGER.log(Level.SEVERE, msg, e);
		} catch (final SecurityException e) {
			LOGGER.log(Level.SEVERE, msg, e);
		} catch (final IllegalArgumentException e) {
			LOGGER.log(Level.SEVERE, msg, e);
		} catch (final IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, msg, e);
		}
	}

	private static void changeRootNodeListener(final IMouseListener listener) {
		final MainView rootMainView = ((MapView) FreeplaneBasicAdapter.getInstance().getMapView()).getRoot().getMainView();
		changeMouseListener(rootMainView, listener);
		changeNodeListener(listener);
	}

	private static void changeNodeListener(final IMouseListener listener) {
		final String msg = "Error While changing freeplane listener";
		try {
			final Field fieldNodes = MapModel.class.getDeclaredField("nodes");
			fieldNodes.setAccessible(true);
			final Map<String, NodeModel> nodes = (Map<String, NodeModel>) fieldNodes.get(((MapView) FreeplaneBasicAdapter.getInstance()
					.getMapView()).getModel());
			for (final NodeModel value : nodes.values()) {
				if (((MapView) FreeplaneBasicAdapter.getInstance().getMapView()).getNodeView(value) == null) {
					continue;
				}
				changeMouseListener(((MapView) FreeplaneBasicAdapter.getInstance().getMapView()).getNodeView(value).getMainView(),
						listener);
			}
		} catch (final NoSuchFieldException e) {
			LOGGER.log(Level.SEVERE, msg, e);
		} catch (final SecurityException e) {
			LOGGER.log(Level.SEVERE, msg, e);
		} catch (final IllegalArgumentException e) {
			LOGGER.log(Level.SEVERE, msg, e);
		} catch (final IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, msg, e);
		}
	}

	private static void changeMouseListener(final MainView mainView, final IMouseListener listener) {
		for (final MouseListener tmp : mainView.getMouseListeners()) {
			mainView.removeMouseListener(tmp);
		}
		for (final MouseMotionListener tmp : mainView.getMouseMotionListeners()) {
			mainView.removeMouseMotionListener(tmp);
		}
		mainView.addMouseListener(listener);
		mainView.addMouseMotionListener(listener);
	}

}
