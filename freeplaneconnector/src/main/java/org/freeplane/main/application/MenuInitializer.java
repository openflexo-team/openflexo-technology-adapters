package org.freeplane.main.application;

import java.util.Set;

import org.freeplane.core.ui.components.UITools;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.freeplane.features.mode.browsemode.BModeController;
import org.freeplane.features.mode.filemode.FModeController;
import org.freeplane.features.mode.mindmapmode.LoadAcceleratorPresetsAction;
import org.freeplane.features.mode.mindmapmode.MModeController;

public class MenuInitializer {

    private MenuInitializer() {
    }

    public static void buildMenus(final Controller controller, final Set<String> plugins) {
        buildMenus(controller, plugins, MModeController.MODENAME, "/xml/mindmapmodemenu.xml");
        LoadAcceleratorPresetsAction.install();
        if (!UITools.useRibbonsMenu()) {
            buildMenus(controller, plugins, BModeController.MODENAME, "/xml/browsemodemenu.xml");
            buildMenus(controller, plugins, FModeController.MODENAME, "/xml/filemodemenu.xml");
        }
    }

    private static void buildMenus(final Controller controller, final Set<String> plugins, final String mode, final String xml) {
        final ModeController modeController = controller.getModeController(mode);
        controller.selectModeForBuild(modeController);
        modeController.updateMenus(xml, plugins);
        controller.selectModeForBuild(null);
    }
}
