/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint.controller;

import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.technologyadapter.powerpoint.PowerpointTechnologyAdapter;
import org.openflexo.technologyadapter.powerpoint.fml.PowerpointShapeRole;
import org.openflexo.technologyadapter.powerpoint.fml.PowerpointSlideRole;
import org.openflexo.technologyadapter.powerpoint.gui.PowerpointIconLibrary;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointShape;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlide;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;
import org.openflexo.technologyadapter.powerpoint.view.PowerpointSlideshowView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class PowerpointAdapterController extends TechnologyAdapterController<PowerpointTechnologyAdapter> {

	@Override
	public Class<PowerpointTechnologyAdapter> getTechnologyAdapterClass() {
		return PowerpointTechnologyAdapter.class;
	}

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		pptInspectorGroup = controller.loadInspectorGroup("PowerPoint", getTechnologyAdapter().getLocales(),
				getFMLTechnologyAdapterInspectorGroup());
	}

	private InspectorGroup pptInspectorGroup;

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return pptInspectorGroup;
	}

	@Override
	protected void initializeActions(ControllerActionInitializer actionInitializer) {
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return PowerpointIconLibrary.POWERPOINT_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return PowerpointIconLibrary.POWERPOINT_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return PowerpointIconLibrary.POWERPOINT_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return PowerpointIconLibrary.POWERPOINT_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		return PowerpointIconLibrary.iconForObject(objectClass);
	}

	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> patternRoleClass) {
		if (PowerpointSlideRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(PowerpointSlide.class);
		}
		if (PowerpointShapeRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(PowerpointShape.class);
		}
		return null;
	}

	@Override
	public boolean isRepresentableInModuleView(TechnologyObject<PowerpointTechnologyAdapter> object) {
		return object instanceof PowerpointSlideshow;
	}
	
	@Override
	public FlexoObject getRepresentableMasterObject(TechnologyObject<PowerpointTechnologyAdapter> object) {
		if (object instanceof PowerpointSlideshow) {
			return object;
		}
		return object;
	}
	
	@Override
	public String getWindowTitleforObject(TechnologyObject<PowerpointTechnologyAdapter> object, FlexoController controller) {
		if (object instanceof PowerpointSlide) {
			return ((PowerpointSlide) object).getName();
		}
		return object.toString();
	}

	@Override
	public ModuleView<?> createModuleViewForMasterObject(TechnologyObject<PowerpointTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {
		if (object instanceof PowerpointSlideshow) {
			return new PowerpointSlideshowView((PowerpointSlideshow) object, controller, perspective);
		}
		return new EmptyPanel<>(controller, perspective, object);
	}

}
