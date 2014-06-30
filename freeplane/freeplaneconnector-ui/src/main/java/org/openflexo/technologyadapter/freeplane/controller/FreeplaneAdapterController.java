/*
 * (c) Copyright 2013- Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.freeplane.controller;

import javax.swing.ImageIcon;

import org.freeplane.features.mode.Controller;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.gui.FreeplaneIconLibrary;
import org.openflexo.technologyadapter.freeplane.listeners.FreeplaneNodeSelectionListener;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.view.FreeplaneModuleView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class FreeplaneAdapterController extends TechnologyAdapterController<FreeplaneTechnologyAdapter> {

    @Override
    public Class<FreeplaneTechnologyAdapter> getTechnologyAdapterClass() {
        return FreeplaneTechnologyAdapter.class;
    }

    @Override
    public void initializeActions(final ControllerActionInitializer actionInitializer) {
        actionInitializer.getController().getModuleInspectorController()
                .loadDirectory(ResourceLocator.locateResource("Inspectors/Freeplane"));

        new AddChildNodeInitializer(actionInitializer);
    }

    @Override
    public ImageIcon getTechnologyBigIcon() {
        return FreeplaneIconLibrary.FREEPLANE_TECHNOLOGY_BIG_ICON;
    }

    @Override
    public ImageIcon getTechnologyIcon() {
        return FreeplaneIconLibrary.FREEPLANE_TECHNOLOGY_ICON;
    }

    @Override
    public ImageIcon getModelIcon() {
        return FreeplaneIconLibrary.FREEPLANE_FILE_ICON;
    }

    @Override
    public ImageIcon getMetaModelIcon() {
        return FreeplaneIconLibrary.FREEPLANE_FILE_ICON;
    }

    @Override
    public ImageIcon getIconForTechnologyObject(final Class<? extends TechnologyObject<FreeplaneTechnologyAdapter>> objectClass) {
        return FreeplaneIconLibrary.FREEPLANE_TECHNOLOGY_ICON;
    }

    @Override
    public ModuleView<?> createModuleViewForObject(final TechnologyObject<FreeplaneTechnologyAdapter> object,
            final FlexoController controller, final FlexoPerspective perspective) {
        if (object instanceof IFreeplaneMap) {
            Controller.getCurrentModeController().getMapController()
                    .addNodeSelectionListener(new FreeplaneNodeSelectionListener((IFreeplaneMap) object, controller));
            return new FreeplaneModuleView((IFreeplaneMap) object, controller, perspective);
        }

        return new EmptyPanel<TechnologyObject<FreeplaneTechnologyAdapter>>(controller, perspective, object);
    }

    @Override
    public ImageIcon getIconForPatternRole(final Class<? extends FlexoRole<?>> arg0) {
        return FreeplaneIconLibrary.FREEPLANE_TECHNOLOGY_ICON;
    }

    @Override
    public String getWindowTitleforObject(final TechnologyObject<FreeplaneTechnologyAdapter> object, final FlexoController arg1) {
        if (object instanceof IFreeplaneMap) {
            return FreeplaneBasicAdapter.getInstance().getMapName();
        }
        return object.toString();
    }

    /**
     * @return true if <code>object</code> is instance of {@link IFreeplaneMap}
     *         or {@link IFreeplaneNode}
     */
    @Override
    public boolean hasModuleViewForObject(final TechnologyObject<FreeplaneTechnologyAdapter> object, final FlexoController controller) {
        return object instanceof IFreeplaneMap;
    }
}
