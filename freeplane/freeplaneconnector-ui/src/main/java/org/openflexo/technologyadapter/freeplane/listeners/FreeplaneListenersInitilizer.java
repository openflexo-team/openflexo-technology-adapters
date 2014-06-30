package org.openflexo.technologyadapter.freeplane.listeners;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.core.ui.IUserInputListenerFactory;
import org.freeplane.features.mode.Controller;
import org.freeplane.view.swing.ui.UserInputListenerFactory;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.view.controller.FlexoController;

public class FreeplaneListenersInitilizer {

    private static final Logger LOGGER = Logger.getLogger(FreeplaneListenersInitilizer.class.getSimpleName());

    public static void init(final IFreeplaneMap map, final FlexoController controller) {
        Controller.getCurrentModeController().getMapController()
                .addNodeSelectionListener(new FreeplaneNodeSelectionListener(map, controller));
        popMenuChangeOnNode(controller);
    }

    @SuppressWarnings("unused")
    private static void popMenuChangeOnNode(final FlexoController controller) {
        final String msg = "Error while setting mouse listener on freeplane node";
        final IUserInputListenerFactory factoryToModify = Controller.getCurrentModeController().getUserInputListenerFactory();
        try {
            final Field f = UserInputListenerFactory.class.getDeclaredField("nodeMouseMotionListener");
            f.setAccessible(true);
            f.set(factoryToModify, new FreeplaneNodeMouseListener(controller));
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

}
