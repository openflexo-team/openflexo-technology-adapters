/**
 * 
 * Copyright (c) 2015, Openflexo
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

package org.openflexo.technologyadapter.oslc.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.oslc.OSLCTechnologyAdapter;
import org.openflexo.technologyadapter.oslc.gui.OSLCIconLibrary;
import org.openflexo.technologyadapter.oslc.gui.view.FIBOSLCCatalog;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCService;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirement;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirementCollection;
import org.openflexo.technologyadapter.oslc.rm.OSLCResourceResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.AddOSLCRequirement;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.AddOSLCResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCRequirement;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCService;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.virtualmodel.core.OSLCResourceRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.core.OSLCServiceProviderRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.core.OSLCServiceRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.rm.OSLCRequirementCollectionRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.rm.OSLCRequirementRole;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class OSLCAdapterController extends TechnologyAdapterController<OSLCTechnologyAdapter> {
	static final Logger logger = Logger.getLogger(OSLCAdapterController.class.getPackage().getName());

	@Override
	public Class<OSLCTechnologyAdapter> getTechnologyAdapterClass() {
		return OSLCTechnologyAdapter.class;
	}

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		oslcInspectorGroup = controller.loadInspectorGroup("oslc", getTechnologyAdapter().getLocales(),
				getFMLTechnologyAdapterInspectorGroup());
	}

	private InspectorGroup oslcInspectorGroup;

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return oslcInspectorGroup;
	}

	@Override
	protected void initializeActions(ControllerActionInitializer actionInitializer) {
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return OSLCIconLibrary.OSLC_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return OSLCIconLibrary.OSLC_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return OSLCIconLibrary.OSLC_FILE_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return OSLCIconLibrary.OSLC_FILE_ICON;
	}

	/**
	 * Return icon representing supplied edition action
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForEditionAction(Class<? extends EditionAction> editionActionClass) {
		if (AddOSLCResource.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCResource.class), IconLibrary.DUPLICATE);
		}
		else if (AddOSLCRequirement.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCRequirement.class), IconLibrary.DUPLICATE);
		}
		else if (SelectOSLCResource.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCResource.class), IconLibrary.IMPORT);
		}
		else if (SelectOSLCRequirement.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCRequirement.class), IconLibrary.IMPORT);
		}
		else if (SelectOSLCServiceProvider.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCServiceProvider.class), IconLibrary.IMPORT);
		}
		else if (SelectOSLCService.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OSLCService.class), IconLibrary.IMPORT);
		}
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<OSLCTechnologyAdapter> arg0, FlexoController arg1,
			FlexoPerspective arg2) {
		if (arg0 instanceof OSLCServiceProviderCatalog) {
			return new FIBOSLCCatalog((OSLCResource) arg0, arg1);
		}
		return new EmptyPanel<TechnologyObject<OSLCTechnologyAdapter>>(arg1, arg2, arg0);
	}

	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> arg0) {
		if (OSLCResourceRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCResource.class);
		}
		if (OSLCRequirementRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCRequirement.class);
		}
		if (OSLCServiceProviderRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCServiceProvider.class);
		}
		if (OSLCServiceRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCService.class);
		}
		if (OSLCRequirementCollectionRole.class.isAssignableFrom(arg0)) {
			return getIconForTechnologyObject(OSLCRequirementCollection.class);
		}
		return null;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<OSLCTechnologyAdapter> arg0, FlexoController arg1) {
		if (arg0 instanceof OSLCResourceResource) {
			return ((OSLCResourceResource) arg0).getName();
		}
		return arg0.toString();
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<OSLCTechnologyAdapter> arg0, FlexoController arg1) {
		if (arg0 instanceof OSLCServiceProviderCatalog) {
			return true;
		}
		return false;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		return OSLCIconLibrary.iconForObject(objectClass);
	}

}
