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
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;

import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.gui.${technologyPrefix}IconLibrary;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class ${technologyPrefix}AdapterController extends TechnologyAdapterController<${technologyPrefix}TechnologyAdapter> {
	static final Logger logger = Logger.getLogger(${technologyPrefix}AdapterController.class.getPackage().getName());

	@Override
	public Class<${technologyPrefix}TechnologyAdapter> getTechnologyAdapterClass() {
		return ${technologyPrefix}TechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
		actionInitializer.getController().getModuleInspectorController()
				.loadDirectory(ResourceLocator.locateResource("src/main/resources/Inspectors/${technologyPrefix}"));
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

	public ImageIcon getIconForOntologyObject(Class<? extends IFlexoOntologyObject<${technologyPrefix}TechnologyAdapter>> objectClass) {
		return ${technologyPrefix}IconLibrary.iconForObject(objectClass);
	}

	@Overwride
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<${technologyPrefix}TechnologyAdapter>> objectClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(
			TechnologyObject<${technologyPrefix}TechnologyAdapter> arg0, FlexoController arg1,
			FlexoPerspective arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageIcon getIconForPatternRole(Class<? extends FlexoRole<?>> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWindowTitleforObject(
			TechnologyObject<${technologyPrefix}TechnologyAdapter> arg0, FlexoController arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasModuleViewForObject(
			TechnologyObject<${technologyPrefix}TechnologyAdapter> arg0, FlexoController arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}

