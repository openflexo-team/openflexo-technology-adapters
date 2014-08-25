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

package org.openflexo.technologyadapter.csv.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.library.CSVIconLibrary;
import org.openflexo.technologyadapter.csv.view.CSVModuleView;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class CSVAdapterController extends TechnologyAdapterController<CSVTechnologyAdapter> {
    
	static final Logger LOGGER = Logger.getLogger(CSVAdapterController.class.getPackage().getName());

	@Override
	public Class<CSVTechnologyAdapter> getTechnologyAdapterClass() {
		return CSVTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
		actionInitializer.getController().getModuleInspectorController()
				.loadDirectory(ResourceLocator.locateResource("Inspectors/CSV"));
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return CSVIconLibrary.CSV_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return CSVIconLibrary.CSV_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return CSVIconLibrary.CSV_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return CSVIconLibrary.CSV_FILE_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(final Class<? extends TechnologyObject<CSVTechnologyAdapter>> objectClass) {
		// TODO Auto-generated method stub
		return CSVIconLibrary.CSV_TECHNOLOGY_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(final TechnologyObject<CSVTechnologyAdapter> object, final FlexoController controller, final FlexoPerspective perspective) {
		// TODO Auto-generated method stub : update your moduleView code to have somethig represented
		if (object instanceof CSVModel){
			return new CSVModuleView((CSVModel) object, controller, perspective);
		}
		return new EmptyPanel<TechnologyObject<CSVTechnologyAdapter>>(controller, perspective, object);
	}

	@Override
	public ImageIcon getIconForPatternRole(Class<? extends FlexoRole<?>> role) {
		// TODO Auto-generated method stub
		return CSVIconLibrary.CSV_TECHNOLOGY_ICON;
	}

	@Override
	public String getWindowTitleforObject(
			TechnologyObject<CSVTechnologyAdapter> obj, FlexoController controller) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public boolean hasModuleViewForObject(
			TechnologyObject<CSVTechnologyAdapter> obj, FlexoController controller) {
		// TODO Auto-generated method stub
		return obj instanceof CSVModel;
	}
}

