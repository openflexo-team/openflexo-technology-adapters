/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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

package org.openflexo.technologyadapter.emf.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.ontology.components.widget.OntologyBrowserModel;
import org.openflexo.ontology.components.widget.OntologyView;
import org.openflexo.ontology.controller.FlexoOntologyTechnologyAdapterController;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.fml.EMFClassClassRole;
import org.openflexo.technologyadapter.emf.fml.EMFEnumClassRole;
import org.openflexo.technologyadapter.emf.fml.EMFObjectIndividualRole;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.fml.editionaction.SelectEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.gui.EMFIconLibrary;
import org.openflexo.technologyadapter.emf.gui.EMFMetaModelBrowserModel;
import org.openflexo.technologyadapter.emf.gui.EMFMetaModelView;
import org.openflexo.technologyadapter.emf.gui.EMFModelBrowserModel;
import org.openflexo.technologyadapter.emf.gui.EMFModelView;
import org.openflexo.technologyadapter.emf.metamodel.EMFClassClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFEnumClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class EMFAdapterController extends FlexoOntologyTechnologyAdapterController<EMFTechnologyAdapter> {

	static final Logger logger = Logger.getLogger(EMFAdapterController.class.getPackage().getName());

	/**
	 * 
	 * Follow the link.
	 * 
	 * @see org.openflexo.view.controller.TechnologyAdapterController#getTechnologyAdapterClass()
	 */
	@Override
	public Class<EMFTechnologyAdapter> getTechnologyAdapterClass() {
		return EMFTechnologyAdapter.class;
	}

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		emfInspectorGroup = controller.loadInspectorGroup("EMF", getFMLTechnologyAdapterInspectorGroup());
	}

	private InspectorGroup emfInspectorGroup;

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return emfInspectorGroup;
	}

	/**
	 * 
	 * Follow the link.
	 * 
	 * @see org.openflexo.view.controller.TechnologyAdapterController#initializeActions(org.openflexo.view.controller.ControllerActionInitializer)
	 */
	@Override
	protected void initializeActions(ControllerActionInitializer actionInitializer) {

	}

	/**
	 * Return icon representing underlying technology, required size is 32x32
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyBigIcon() {
		return EMFIconLibrary.EMF_TECHNOLOGY_BIG_ICON;
	}

	/**
	 * Return icon representing underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyIcon() {
		return EMFIconLibrary.EMF_TECHNOLOGY_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getModelIcon() {
		return EMFIconLibrary.EMF_FILE_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getMetaModelIcon() {
		return EMFIconLibrary.ECORE_FILE_ICON;
	}

	/**
	 * Return icon representing supplied ontology object
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		return EMFIconLibrary.iconForObject(objectClass);
	}

	/**
	 * /** Return icon representing supplied pattern property
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> patternRoleClass) {
		if (EMFObjectIndividualRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(EMFObjectIndividual.class);
		}
		else if (EMFClassClassRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(EMFClassClass.class);
		}
		else if (EMFEnumClassRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(EMFEnumClass.class);
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
		if (AddEMFObjectIndividual.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(EMFObjectIndividual.class), IconLibrary.DUPLICATE);
		}
		if (SelectEMFObjectIndividual.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(EMFObjectIndividual.class), IconLibrary.IMPORT);
		} /*else if (AddEMFClassClass.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForOntologyObject(EMFClassClass.class), IconLibrary.DUPLICATE);
			}*/
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public OntologyBrowserModel makeOntologyBrowserModel(IFlexoOntology context) {
		if (context instanceof EMFMetaModel) {
			return new EMFMetaModelBrowserModel((EMFMetaModel) context);
		}
		else if (context instanceof EMFModel) {
			return new EMFModelBrowserModel((EMFModel) context);
		}
		else {
			logger.warning("Unexpected " + context);
			return null;
		}
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject object, FlexoController controller) {
		return object instanceof EMFModel || object instanceof EMFMetaModel;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject object, FlexoController controller) {
		if (object instanceof EMFModel) {
			return ((EMFModel) object).getName();
		}
		if (object instanceof EMFMetaModel) {
			return ((EMFMetaModel) object).getName();
		}
		return object.toString();
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<EMFTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {
		if (object instanceof EMFModel) {
			OntologyView<EMFModel> returned = new EMFModelView((EMFModel) object, controller, perspective);
			returned.setShowClasses(true);
			returned.setShowDataProperties(false);
			returned.setShowObjectProperties(false);
			returned.setShowAnnotationProperties(false);
			returned.update();
			return returned;
		}
		else if (object instanceof EMFMetaModel) {
			OntologyView<EMFMetaModel> returned = new EMFMetaModelView((EMFMetaModel) object, controller, perspective);
			returned.setShowClasses(true);
			returned.setShowDataProperties(true);
			returned.setShowObjectProperties(true);
			returned.setShowAnnotationProperties(true);
			returned.update();
			return returned;
		}
		return new EmptyPanel<TechnologyObject<EMFTechnologyAdapter>>(controller, perspective, object);
	}
}
