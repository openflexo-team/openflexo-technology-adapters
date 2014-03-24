package org.openflexo.technologyadapter.owl.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.components.widget.OntologyBrowserModel;
import org.openflexo.components.widget.OntologyView;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.foundation.viewpoint.editionaction.EditionAction;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
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
import org.openflexo.technologyadapter.owl.model.PropertyStatement;
import org.openflexo.technologyadapter.owl.model.SubClassStatement;
import org.openflexo.technologyadapter.owl.viewpoint.DataPropertyStatementRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLClassRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLDataPropertyRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLIndividualRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLObjectPropertyRole;
import org.openflexo.technologyadapter.owl.viewpoint.OWLPropertyRole;
import org.openflexo.technologyadapter.owl.viewpoint.ObjectPropertyStatementRole;
import org.openflexo.technologyadapter.owl.viewpoint.RestrictionStatementRole;
import org.openflexo.technologyadapter.owl.viewpoint.SubClassStatementRole;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddDataPropertyStatement;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddOWLClass;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddOWLIndividual;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddObjectPropertyStatement;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddRestrictionStatement;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddSubClassStatement;
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

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {

		// TODO : Des choses à faire ici pour améliorer le support des répertoires dans le ClassPath

		actionInitializer.getController().getModuleInspectorController().loadDirectory(ResourceLocator.locateResource("Inspectors/OWL"));

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
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<OWLTechnologyAdapter>> objectClass) {
		if (OWLObject.class.isAssignableFrom(objectClass)) {
			return OWLIconLibrary.iconForObject((Class<? extends OWLObject>) objectClass);
		} else if (OWLStatement.class.isAssignableFrom(objectClass)) {
			return OWLIconLibrary.ONTOLOGY_STATEMENT_ICON;
		}
		return null;
	}

	/**
	 * Return icon representing supplied pattern role
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
		} else if (RestrictionStatementRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(PropertyStatement.class);
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
	public ImageIcon getIconForEditionAction(Class<? extends EditionAction<?, ?>> editionActionClass) {
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
