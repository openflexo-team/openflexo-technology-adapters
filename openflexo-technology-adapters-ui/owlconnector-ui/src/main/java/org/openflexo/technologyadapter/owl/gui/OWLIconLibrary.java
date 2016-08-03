/**
 * 
 * Copyright (c) 2013-2014, Openflexo
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

package org.openflexo.technologyadapter.owl.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.icon.ImageIconResource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;
import org.openflexo.technologyadapter.owl.model.OWLObject;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLStatement;

public class OWLIconLibrary {

	private static final Logger logger = Logger.getLogger(OWLIconLibrary.class.getPackage().getName());

	public static final ImageIconResource ONTOLOGY_LIBRARY_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/32x32/OntologyLibrary.png"));

	public static final ImageIconResource ONTOLOGY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Ontology.png"));
	public static final ImageIconResource ONTOLOGY_LIBRARY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OntologyLibrary.png"));
	public static final ImageIconResource ONTOLOGY_CLASS_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OntologyClass.png"));
	public static final ImageIconResource ONTOLOGY_INDIVIDUAL_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OntologyIndividual.png"));
	public static final ImageIconResource ONTOLOGY_OBJECT_PROPERTY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OntologyObjectProperty.png"));
	public static final ImageIconResource ONTOLOGY_DATA_PROPERTY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OntologyDataProperty.png"));
	public static final ImageIconResource ONTOLOGY_PROPERTY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OntologyProperty.png"));
	public static final ImageIconResource ONTOLOGY_ANNOTATION_PROPERTY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OntologyAnnotationProperty.png"));
	public static final ImageIconResource ONTOLOGY_STATEMENT_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OntologyStatement.png"));

	public static ImageIcon iconForObject(Class<? extends OWLObject> objectClass) {
		if (OWLOntology.class.isAssignableFrom(objectClass)) {
			return ONTOLOGY_ICON;
		}
		else if (OWLClass.class.isAssignableFrom(objectClass)) {
			return ONTOLOGY_CLASS_ICON;
		}
		else if (OWLIndividual.class.isAssignableFrom(objectClass)) {
			return ONTOLOGY_INDIVIDUAL_ICON;
		}
		/* else if (object instanceof IFlexoOntologyStructuralProperty && ((IFlexoOntologyStructuralProperty) object).isAnnotationProperty()) {
		return ONTOLOGY_ANNOTATION_PROPERTY_ICON;
		}*/else if (OWLObjectProperty.class.isAssignableFrom(objectClass)) {
			return ONTOLOGY_OBJECT_PROPERTY_ICON;
		}
		else if (OWLDataProperty.class.isAssignableFrom(objectClass)) {
			return ONTOLOGY_DATA_PROPERTY_ICON;
		}
		else if (OWLStatement.class.isAssignableFrom(objectClass)) {
			return ONTOLOGY_STATEMENT_ICON;
		}
		logger.warning("No icon for " + objectClass);
		return null;
	}
}
