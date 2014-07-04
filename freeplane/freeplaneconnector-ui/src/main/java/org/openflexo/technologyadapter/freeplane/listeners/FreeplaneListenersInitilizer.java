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
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.view.controller.FlexoController;

public class FreeplaneListenersInitilizer {

    private static final Logger LOGGER = Logger.getLogger(FreeplaneListenersInitilizer.class.getSimpleName());

    public static void init(final IFreeplaneMap map, final FlexoController controller) {
        Controller.getCurrentModeController().getMapController()
                .addNodeSelectionListener(new FreeplaneNodeSelectionListener(map, controller));
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
                changeMouseListener(((MapView) FreeplaneBasicAdapter.getInstance().getMapView()).getNodeView(value).getMainView(), listener);
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
