#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.rm.ResourceLocator;
import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.library.${technologyPrefix}IconLibrary;
import ${package}.view.${technologyPrefix}ModuleView;
import ${package}.model.${technologyPrefix}Model;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class ${technologyPrefix}AdapterController extends TechnologyAdapterController<${technologyPrefix}TechnologyAdapter> {
    
	static final Logger LOGGER = Logger.getLogger(${technologyPrefix}AdapterController.class.getPackage().getName());

	@Override
	public Class<${technologyPrefix}TechnologyAdapter> getTechnologyAdapterClass() {
		return ${technologyPrefix}TechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
		actionInitializer.getController().getModuleInspectorController()
				.loadDirectory(ResourceLocator.locateResource("Inspectors/${technologyPrefix}"));
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return ${technologyPrefix}IconLibrary.${technologyPrefix}_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return ${technologyPrefix}IconLibrary.${technologyPrefix}_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return ${technologyPrefix}IconLibrary.${technologyPrefix}_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return ${technologyPrefix}IconLibrary.${technologyPrefix}_FILE_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(final Class<? extends TechnologyObject<${technologyPrefix}TechnologyAdapter>> objectClass) {
		// TODO Auto-generated method stub
		return ${technologyPrefix}IconLibrary.${technologyPrefix}_TECHNOLOGY_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(final TechnologyObject<${technologyPrefix}TechnologyAdapter> object, final FlexoController controller, final FlexoPerspective perspective) {
		// TODO Auto-generated method stub : update your moduleView code to have somethig represented
		if (object instanceof ${technologyPrefix}Model){
			return new ${technologyPrefix}ModuleView((${technologyPrefix}Model) object, controller, perspective);
		}
		return new EmptyPanel<TechnologyObject<${technologyPrefix}TechnologyAdapter>>(controller, perspective, object);
	}

	@Override
	public ImageIcon getIconForPatternRole(Class<? extends FlexoRole<?>> role) {
		// TODO Auto-generated method stub
		return ${technologyPrefix}IconLibrary.${technologyPrefix}_TECHNOLOGY_ICON;
	}

	@Override
	public String getWindowTitleforObject(
			TechnologyObject<${technologyPrefix}TechnologyAdapter> obj, FlexoController controller) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public boolean hasModuleViewForObject(
			TechnologyObject<${technologyPrefix}TechnologyAdapter> obj, FlexoController controller) {
		// TODO Auto-generated method stub
		return obj instanceof ${technologyPrefix}Model;
	}
}

