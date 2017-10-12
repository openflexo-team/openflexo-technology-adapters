/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.fml.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.binding.FunctionPathElement;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.foundation.fml.TechnologySpecificType;
import org.openflexo.foundation.ontology.IFlexoOntologyFeatureAssociation;
import org.openflexo.foundation.ontology.IndividualOfClass;
import org.openflexo.foundation.ontology.SubClassOfClass;
import org.openflexo.foundation.ontology.SubPropertyOfProperty;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeAssociation;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeDataProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeObjectProperty;
import org.openflexo.technologyadapter.emf.metamodel.EMFClassClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFReferenceAssociation;
import org.openflexo.technologyadapter.emf.metamodel.EMFReferenceObjectProperty;

/**
 * This class represent the {@link BindingFactory} dedicated to handle EMF technology-specific binding elements
 * 
 * @author sylvain
 * 
 */
public final class EMFBindingFactory extends TechnologyAdapterBindingFactory {
	static final Logger logger = Logger.getLogger(EMFBindingFactory.class.getPackage().getName());

	public EMFBindingFactory() {
		super();
	}

	@Override
	protected SimplePathElement makeSimplePathElement(Object object, IBindingPathElement parent) {
		if (object instanceof EMFAttributeAssociation) {
			if (((EMFAttributeAssociation) object).getFeature() instanceof EMFAttributeDataProperty) {
				return new AttributeDataPropertyFeatureAssociationPathElement(parent, (EMFAttributeAssociation) object,
						(EMFAttributeDataProperty) ((EMFAttributeAssociation) object).getFeature());
			}
			else if (((EMFAttributeAssociation) object).getFeature() instanceof EMFAttributeObjectProperty) {
				return new AttributeObjectPropertyFeatureAssociationPathElement(parent, (EMFAttributeAssociation) object,
						(EMFAttributeObjectProperty) ((EMFAttributeAssociation) object).getFeature());
			}
		}
		else if (object instanceof EMFReferenceAssociation
				&& ((EMFReferenceAssociation) object).getFeature() instanceof EMFReferenceObjectProperty) {
			return new ObjectReferenceFeatureAssociationPathElement(parent, (EMFReferenceAssociation) object,
					(EMFReferenceObjectProperty) ((EMFReferenceAssociation) object).getFeature());
		}
		logger.warning("Unexpected " + object);
		return null;
	}

	@Override
	public boolean handleType(TechnologySpecificType technologySpecificType) {
		if ((technologySpecificType instanceof IndividualOfClass)
				&& ((IndividualOfClass) technologySpecificType).getOntologyClass() instanceof EMFClassClass) {
			return true;
		}
		if ((technologySpecificType instanceof SubClassOfClass)
				&& ((SubClassOfClass) technologySpecificType).getOntologyClass() instanceof EMFClassClass) {
			return true;
		}
		if ((technologySpecificType instanceof SubPropertyOfProperty)
		/*&& ((SubPropertyOfProperty) technologySpecificType).getOntologyProperty() instanceof*/) {
			return true;
		}
		return false;
	}

	@Override
	public List<? extends SimplePathElement> getAccessibleSimplePathElements(IBindingPathElement parent) {
		if (parent.getType() instanceof IndividualOfClass) {
			IndividualOfClass parentType = (IndividualOfClass) parent.getType();
			List<SimplePathElement> returned = new ArrayList<SimplePathElement>();
			if (parentType.getOntologyClass() instanceof EMFClassClass) {
				for (IFlexoOntologyFeatureAssociation fa : ((EMFClassClass) parentType.getOntologyClass())
						.getStructuralFeatureAssociations()) {
					returned.add(getSimplePathElement(fa, parent));
				}
			}

			// returned.addAll(super.getAccessibleSimplePathElements(parent));

			return returned;
		}

		return super.getAccessibleSimplePathElements(parent);
	}

	@Override
	public List<? extends FunctionPathElement> getAccessibleFunctionPathElements(IBindingPathElement parent) {
		// TODO: implements same as above, with behavioural features
		return super.getAccessibleFunctionPathElements(parent);
	}

}
