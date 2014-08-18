package org.openflexo.technologyadapter.xml.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.components.widget.OntologyBrowserModel;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.foundation.viewpoint.editionaction.EditionAction;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.xml.XSDTechnologyAdapter;
import org.openflexo.technologyadapter.xml.editionaction.AddXSClass;
import org.openflexo.technologyadapter.xml.editionaction.AddXSIndividual;
import org.openflexo.technologyadapter.xml.gui.XMLModelBrowserModel;
import org.openflexo.technologyadapter.xml.gui.XMLXSDModelView;
import org.openflexo.technologyadapter.xml.gui.XSDIconLibrary;
import org.openflexo.technologyadapter.xml.gui.XSDMetaModelBrowserModel;
import org.openflexo.technologyadapter.xml.gui.XSDMetaModelView;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XSOntClass;
import org.openflexo.technologyadapter.xml.model.AbstractXSOntObject;
import org.openflexo.technologyadapter.xml.model.XMLXSDModel;
import org.openflexo.technologyadapter.xml.model.XSOntIndividual;
import org.openflexo.technologyadapter.xml.virtualmodel.XSClassRole;
import org.openflexo.technologyadapter.xml.virtualmodel.XSIndividualRole;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.IFlexoOntologyTechnologyAdapterController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class XSDAdapterController extends TechnologyAdapterController<XSDTechnologyAdapter> implements
		IFlexoOntologyTechnologyAdapterController {

	static final Logger logger = Logger.getLogger(XSDAdapterController.class.getPackage().getName());

	@Override
	public Class<XSDTechnologyAdapter> getTechnologyAdapterClass() {
		return XSDTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {

		actionInitializer.getController().getModuleInspectorController().loadDirectory(ResourceLocator.locateResource("Inspectors/XSD"));
	}

	/**
	 * Return icon representing underlying technology, required size is 32x32
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyBigIcon() {
		return XSDIconLibrary.XSD_TECHNOLOGY_BIG_ICON;
	}

	/**
	 * Return icon representing underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyIcon() {
		return XSDIconLibrary.XSD_TECHNOLOGY_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getModelIcon() {
		return XSDIconLibrary.XML_FILE_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getMetaModelIcon() {
		return XSDIconLibrary.XSD_FILE_ICON;
	}

	/**
	 * Return icon representing supplied ontology object
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<XSDTechnologyAdapter>> objectClass) {
		if (AbstractXSOntObject.class.isAssignableFrom(objectClass)) {
			return XSDIconLibrary.iconForObject((Class<? extends AbstractXSOntObject>) objectClass);
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
		if (XSClassRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(XSOntClass.class);
		} else if (XSIndividualRole.class.isAssignableFrom(patternRoleClass)) {
			return getIconForTechnologyObject(XSOntIndividual.class);
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
		if (AddXSIndividual.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(XSOntIndividual.class), IconLibrary.DUPLICATE);
		} else if (AddXSClass.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(getIconForTechnologyObject(XSOntClass.class), IconLibrary.DUPLICATE);
		}
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public OntologyBrowserModel makeOntologyBrowserModel(IFlexoOntology context) {
		if (context instanceof XMLMetaModel) {
			return new XSDMetaModelBrowserModel((XMLMetaModel) context);
		} else if (context instanceof XMLXSDModel) {
			return new XMLModelBrowserModel((XMLXSDModel) context);
		} else {
			logger.warning("Unexpected " + context);
			return null;
		}
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject object, FlexoController controller) {
		return object instanceof XMLMetaModel || object instanceof XMLXSDModel;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject object, FlexoController controller) {
		if (object instanceof XMLXSDModel) {
			return ((XMLXSDModel) object).getName();
		}
		if (object instanceof XMLMetaModel) {
			return ((XMLMetaModel) object).getName();
		}
		return object.toString();
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<XSDTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {
		if (object instanceof XMLXSDModel) {
			XMLXSDModelView returned = new XMLXSDModelView((XMLXSDModel) object, controller, perspective);
			returned.setShowClasses(false);
			returned.setShowDataProperties(false);
			returned.setShowObjectProperties(false);
			returned.setShowAnnotationProperties(false);
			return returned;
		} else if (object instanceof XMLMetaModel) {
			XSDMetaModelView returned = new XSDMetaModelView((XMLMetaModel) object, controller, perspective);
			returned.setShowClasses(true);
			returned.setShowDataProperties(true);
			returned.setShowObjectProperties(true);
			returned.setShowAnnotationProperties(true);
			return returned;
		}
		return new EmptyPanel<TechnologyObject<XSDTechnologyAdapter>>(controller, perspective, object);
	}

}
