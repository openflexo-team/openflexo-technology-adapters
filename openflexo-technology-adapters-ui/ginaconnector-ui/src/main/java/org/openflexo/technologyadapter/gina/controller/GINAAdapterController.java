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

package org.openflexo.technologyadapter.gina.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.fml.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.view.GINAModuleView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class GINAAdapterController extends TechnologyAdapterController<GINATechnologyAdapter> {

	static final Logger LOGGER = Logger.getLogger(GINAAdapterController.class.getPackage().getName());

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		// ginaInspectorGroup = controller.loadInspectorGroup("Gina", getFMLTechnologyAdapterInspectorGroup());
		// actionInitializer.getController().getModuleInspectorController().loadDirectory(ResourceLocator.locateResource("Inspectors/Excel"));
	}

	private InspectorGroup ginaInspectorGroup;

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return ginaInspectorGroup;
	}

	@Override
	public Class<GINATechnologyAdapter> getTechnologyAdapterClass() {
		return GINATechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
		// actionInitializer.getController().getModuleInspectorController().loadDirectory(ResourceLocator.locateResource("Inspectors/GIN"));
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return GINAIconLibrary.GINA_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return GINAIconLibrary.GINA_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return GINAIconLibrary.FIB_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return GINAIconLibrary.FIB_FILE_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(final Class<? extends TechnologyObject<?>> objectClass) {
		return GINAIconLibrary.GINA_TECHNOLOGY_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(final TechnologyObject<GINATechnologyAdapter> object, final FlexoController controller,
			final FlexoPerspective perspective) {
		// TODO Auto-generated method stub : update your moduleView code to have somethig represented
		if (object instanceof GINAFIBComponent) {
			return new GINAModuleView((GINAFIBComponent) object, controller, perspective);
		}
		return new EmptyPanel<TechnologyObject<GINATechnologyAdapter>>(controller, perspective, object);
	}

	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> flexoRoleClass) {
		return GINAIconLibrary.GINA_TECHNOLOGY_ICON;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<GINATechnologyAdapter> obj, FlexoController controller) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<GINATechnologyAdapter> obj, FlexoController controller) {
		return obj instanceof GINAFIBComponent;
	}
}
