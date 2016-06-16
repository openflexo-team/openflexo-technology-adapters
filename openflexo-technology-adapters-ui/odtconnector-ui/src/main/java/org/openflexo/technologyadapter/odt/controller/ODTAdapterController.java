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

package org.openflexo.technologyadapter.odt.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.technologyadapter.odt.ODTTechnologyAdapter;
import org.openflexo.technologyadapter.odt.gui.ODTIconLibrary;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class ODTAdapterController extends TechnologyAdapterController<ODTTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(ODTAdapterController.class.getPackage().getName());

	@Override
	public Class<ODTTechnologyAdapter> getTechnologyAdapterClass() {
		return ODTTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return ODTIconLibrary.ODT_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return ODTIconLibrary.ODT_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return ODTIconLibrary.ODT_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return ODTIconLibrary.ODT_FILE_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<ODTTechnologyAdapter> arg0, FlexoController arg1,
			FlexoPerspective arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<ODTTechnologyAdapter> arg0, FlexoController arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<ODTTechnologyAdapter> arg0, FlexoController arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void initializeInspectors(FlexoController controller) {
		ODTInspectorGroup = controller.loadInspectorGroup("ODT", getTechnologyAdapter().getLocales(),
				getFMLTechnologyAdapterInspectorGroup());
	}

	private InspectorGroup ODTInspectorGroup;

	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return ODTInspectorGroup;
	}
}
