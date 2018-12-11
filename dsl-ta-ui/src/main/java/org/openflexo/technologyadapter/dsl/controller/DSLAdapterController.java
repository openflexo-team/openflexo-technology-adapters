/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.dsl.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.dsl.DSLTechnologyAdapter;
import org.openflexo.technologyadapter.dsl.fml.DSLComponentRole;
import org.openflexo.technologyadapter.dsl.fml.DSLLinkRole;
import org.openflexo.technologyadapter.dsl.fml.DSLSlotRole;
import org.openflexo.technologyadapter.dsl.fml.editionaction.AbstractSelectDSLComponent;
import org.openflexo.technologyadapter.dsl.fml.editionaction.AbstractSelectDSLLink;
import org.openflexo.technologyadapter.dsl.fml.editionaction.AddDSLComponent;
import org.openflexo.technologyadapter.dsl.fml.editionaction.AddDSLLink;
import org.openflexo.technologyadapter.dsl.gui.DSLIconLibrary;
import org.openflexo.technologyadapter.dsl.model.DSLComponent;
import org.openflexo.technologyadapter.dsl.model.DSLLink;
import org.openflexo.technologyadapter.dsl.model.DSLObject;
import org.openflexo.technologyadapter.dsl.model.DSLSlot;
import org.openflexo.technologyadapter.dsl.model.DSLSystem;
import org.openflexo.technologyadapter.dsl.view.DSLSystemView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class DSLAdapterController extends TechnologyAdapterController<DSLTechnologyAdapter> {

	static final Logger logger = Logger.getLogger(DSLAdapterController.class.getPackage().getName());

	private InspectorGroup dslInspectorGroup;

	@Override
	public Class<DSLTechnologyAdapter> getTechnologyAdapterClass() {
		return DSLTechnologyAdapter.class;
	}

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		dslInspectorGroup = controller.loadInspectorGroup("DSL", getTechnologyAdapter().getLocales(),
				getFMLTechnologyAdapterInspectorGroup());
	}

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return dslInspectorGroup;
	}

	@Override
	protected void initializeActions(ControllerActionInitializer actionInitializer) {

		// You can initialize here actions specific to that technology
	}

	/**
	 * Return icon representing underlying technology, required size is 32x32
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyBigIcon() {
		return DSLIconLibrary.DSL_TA_BIG_ICON;
	}

	/**
	 * Return icon representing underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyIcon() {
		return DSLIconLibrary.DSL_TA_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getModelIcon() {
		return DSLIconLibrary.DSL_SYSTEM_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getMetaModelIcon() {
		return DSLIconLibrary.DSL_TA_ICON;
	}

	/**
	 * Return icon representing supplied ontology object
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		if (DSLObject.class.isAssignableFrom(objectClass)) {
			return DSLIconLibrary.iconForObject((Class<? extends DSLObject>) objectClass);
		}
		return null;
	}

	/**
	 * Return icon representing supplied role
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> roleClass) {
		if (DSLComponentRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(DSLComponent.class);
		}
		if (DSLSlotRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(DSLSlot.class);
		}
		if (DSLLinkRole.class.isAssignableFrom(roleClass)) {
			return getIconForTechnologyObject(DSLLink.class);
		}
		return null;
	}

	/**
	 * Return icon representing supplied edition action
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForEditionAction(Class<? extends EditionAction> editionActionClass) {
		if (AddDSLComponent.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(DSLComponent.class), IconLibrary.DUPLICATE);
		}
		if (AddDSLLink.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(DSLLink.class), IconLibrary.DUPLICATE);
		}
		else if (AbstractSelectDSLComponent.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(DSLComponent.class), IconLibrary.IMPORT);
		}
		else if (AbstractSelectDSLLink.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(DSLLink.class), IconLibrary.IMPORT);
		}
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<DSLTechnologyAdapter> object, FlexoController controller) {
		return object instanceof DSLSystem;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<DSLTechnologyAdapter> object, FlexoController controller) {
		if (object instanceof DSLSystem) {
			return ((DSLSystem) object).getResource().getName();
		}
		return object.toString();
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<DSLTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {
		if (object instanceof DSLSystem) {
			DSLSystemView returned = new DSLSystemView((DSLSystem) object, controller, perspective);
			return returned;
		}
		return new EmptyPanel<>(controller, perspective, object);
	}

}
