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

package org.openflexo.technologyadapter.owl.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.components.widget.OntologyBrowserModel;
import org.openflexo.components.widget.OntologyView;
import org.openflexo.fib.utils.InspectorGroup;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.fml.DataPropertyStatementRole;
import org.openflexo.technologyadapter.owl.fml.OWLClassRole;
import org.openflexo.technologyadapter.owl.fml.OWLDataPropertyRole;
import org.openflexo.technologyadapter.owl.fml.OWLIndividualRole;
import org.openflexo.technologyadapter.owl.fml.OWLObjectPropertyRole;
import org.openflexo.technologyadapter.owl.fml.OWLPropertyRole;
import org.openflexo.technologyadapter.owl.fml.ObjectPropertyStatementRole;
import org.openflexo.technologyadapter.owl.fml.SubClassStatementRole;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddDataPropertyStatement;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddOWLClass;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddOWLIndividual;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddObjectPropertyStatement;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddRestrictionStatement;
import org.openflexo.technologyadapter.owl.fml.editionaction.AddSubClassStatement;
import org.openflexo.technologyadapter.owl.gui.OWLIconLibrary;
import org.openflexo.technologyadapter.owl.gui.OWLOntologyBrowserModel;
import org.openflexo.technologyadapter.owl.gui.OWLOntologyView;
import org.openflexo.technologyadapter.owl.model.DataPropertyStatement;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;
import org.openflexo.technologyadapter.owl.model.OWLObject;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLProperty;
import org.openflexo.technologyadapter.owl.model.OWLStatement;
import org.openflexo.technologyadapter.owl.model.ObjectPropertyStatement;
import org.openflexo.technologyadapter.owl.model.SubClassStatement;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.IFlexoOntologyTechnologyAdapterController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class OWLAdapterController extends TechnologyAdapterController<OWLTechnologyAdapter> implements
		IFlexoOntologyTechnologyAdapterController {

	static final Logger logger = Logger.getLogger(OWLAdapterController.class.getPackage().getName());

	// Ontology edition
	public static String CREATE_ONTOLOGY_CLASS_DIALOG_FIB_NAME = "Fib/Dialog/CreateOntologyClassDialog.fib";
	public static String CREATE_ONTOLOGY_INDIVIDUAL_FIB_NAME = "Fib/Dialog/CreateOntologyIndividualDialog.fib";
	public static String DELETE_ONTOLOGY_OBJECTS_DIALOG_FIB_NAME = "Fib/Dialog/DeleteOntologyObjectsDialog.fib";
	public static String CREATE_DATA_PROPERTY_DIALOG_FIB_NAME = "Fib/Dialog/CreateDataPropertyDialog.fib";
	public static String CREATE_OBJECT_PROPERTY_DIALOG_FIB_NAME = "Fib/Dialog/CreateObjectPropertyDialog.fib";

	@Override
	public Class<OWLTechnologyAdapter> getTechnologyAdapterClass() {
		return OWLTechnologyAdapter.class;
	}

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		owlInspectorGroup = controller.loadInspectorGroup("OWL", getFMLTechnologyAdapterInspectorGroup());
	}

	private InspectorGroup owlInspectorGroup;

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return owlInspectorGroup;
	}

	@Override
	protected void initializeActions(ControllerActionInitializer actionInitializer) {

		// TODO : Des choses à faire ici pour améliorer le support des répertoires dans le ClassPath

		new CreateOntologyClassInitializer(actionInitializer);
		new CreateOntologyIndividualInitializer(actionInitializer);
		new CreateObjectPropertyInitializer(actionInitializer);
		new CreateDataPropertyInitializer(actionInitializer);
		new DeleteOntologyObjectsInitializer(actionInitializer);
		new AddAnnotationStatementInitializer(actionInitializer);
	}

	/**
	 * Return icon representing underlying technology, required size is 32x32
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyBigIcon() {
		return OWLIconLibrary.ONTOLOGY_LIBRARY_BIG_ICON;
	}

	/**
	 * Return icon representing underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyIcon() {
		return OWLIconLibrary.ONTOLOGY_LIBRARY_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getModelIcon() {
		return OWLIconLibrary.ONTOLOGY_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getMetaModelIcon() {
		return OWLIconLibrary.ONTOLOGY_ICON;
	}

	/**
	 * Return icon representing supplied ontology object
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		if (OWLObject.class.isAssignableFrom(objectClass)) {
			return OWLIconLibrary.iconForObject((Class<? extends OWLObject>) objectClass);
		} else if (OWLStatement.class.isAssignableFrom(objectClass)) {
			return OWLIconLibrary.ONTOLOGY_STATEMENT_ICON;
		}
		return null;
	}

	/**
	 * Return icon representing supplied pattern property
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForPatternRole(Class<? extends FlexoRole<?>> patternRoleClass) {
		if (OWLClassRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(OWLClass.class);
		} else if (OWLIndividualRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(OWLIndividual.class);
		} else if (OWLDataPropertyRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(OWLDataProperty.class);
		} else if (OWLObjectPropertyRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(OWLObjectProperty.class);
		} else if (OWLPropertyRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(OWLProperty.class);
		} else if (DataPropertyStatementRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(DataPropertyStatement.class);
		} else if (ObjectPropertyStatementRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(ObjectPropertyStatement.class);
		} else if (SubClassStatementRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(SubClassStatement.class);
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
		if (AddOWLIndividual.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OWLIndividual.class), IconLibrary.DUPLICATE);
		}
		if (AddDataPropertyStatement.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(DataPropertyStatement.class), IconLibrary.DUPLICATE);
		}
		if (AddObjectPropertyStatement.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(ObjectPropertyStatement.class), IconLibrary.DUPLICATE);
		}
		if (AddRestrictionStatement.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OWLStatement.class), IconLibrary.DUPLICATE);
		}
		if (AddSubClassStatement.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(SubClassStatement.class), IconLibrary.DUPLICATE);
		} else if (AddOWLClass.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(OWLClass.class), IconLibrary.DUPLICATE);
		}
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject object, FlexoController controller) {
		return object instanceof OWLOntology;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject object, FlexoController controller) {
		if (object instanceof OWLOntology) {
			return ((OWLOntology) object).getName();
		}
		return object.toString();
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<OWLTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {
		if (object instanceof OWLOntology) {
			OntologyView<OWLOntology> returned = new OWLOntologyView((OWLOntology) object, controller, perspective);
			returned.setShowClasses(true);
			returned.setShowDataProperties(true);
			returned.setShowObjectProperties(true);
			returned.setShowAnnotationProperties(true);
			return returned;
		}
		return new EmptyPanel<TechnologyObject<OWLTechnologyAdapter>>(controller, perspective, object);
	}

	@Override
	public OntologyBrowserModel makeOntologyBrowserModel(IFlexoOntology context) {
		if (context instanceof OWLOntology) {
			return new OWLOntologyBrowserModel((OWLOntology) context);
		} else {
			logger.warning("Unexpected " + context);
			return null;
		}
	}

}
