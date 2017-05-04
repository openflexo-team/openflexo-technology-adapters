/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.technologyadapter.owl.fml.editionaction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.FetchRequest;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.fml.editionaction.SelectIndividual;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.OWLModelSlot;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;
import org.openflexo.technologyadapter.owl.model.OWLOntology;

/**
 * OWL technology - specific {@link FetchRequest} allowing to retrieve a selection of some {@link OWLIndividual} of a given {@link OWLClass}
 * matching some conditions and a given type.<br>
 * 
 * @author sylvain
 */
@ModelEntity
@ImplementationClass(SelectOWLIndividual.SelectOWLIndividualImpl.class)
@XMLElement
@FML("SelectOWLIndividual")
public interface SelectOWLIndividual extends SelectIndividual<OWLModelSlot, OWLOntology, OWLIndividual> {

	public static abstract class SelectOWLIndividualImpl extends SelectIndividualImpl<OWLModelSlot, OWLOntology, OWLIndividual>
			implements SelectOWLIndividual {

		private static final Logger logger = Logger.getLogger(SelectOWLIndividual.class.getPackage().getName());

		@Override
		public Type getFetchedType() {
			if (getType() != null) {
				return super.getFetchedType();
			}
			return OWLClass.class;
		}

		@Override
		public List<OWLIndividual> execute(RunTimeEvaluationContext evaluationContext) {

			// TODO: improve perfs !

			OWLOntology ontology = getReceiver(evaluationContext);

			// System.out.println("On cherche tous les individuals de " + ontology);
			// System.out.println("type=" + getType());

			List<OWLIndividual> selectedIndividuals = new ArrayList<>();
			IFlexoOntologyClass flexoOntologyClass = getType();
			for (OWLIndividual i : ontology.getAccessibleIndividuals()) {
				boolean takeIt = false;
				if (flexoOntologyClass == null) {
					takeIt = true;
				}
				else {
					// System.out.println("les types de i: " + i.getTypes());
					for (OWLClass t : i.getTypes()) {
						if (flexoOntologyClass.isSuperClassOf(t)) {
							takeIt = true;
							break;
						}
					}
				}
				if (takeIt) {
					selectedIndividuals.add(i);
				}
			}

			List<OWLIndividual> returned = filterWithConditions(selectedIndividuals, evaluationContext);

			// System.out.println("SelectOWLIndividual, without filtering =" + selectedIndividuals + " after filtering=" + returned);

			return returned;

		}

	}
}
