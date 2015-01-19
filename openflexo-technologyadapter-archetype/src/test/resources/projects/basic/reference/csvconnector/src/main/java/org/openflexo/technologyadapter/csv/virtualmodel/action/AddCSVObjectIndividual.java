/*
 * (c) Copyright 2013- Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.csv.virtualmodel.action;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.editionaction.AddIndividual;
import org.openflexo.foundation.fml.editionaction.DataPropertyAssertion;
import org.openflexo.foundation.fml.editionaction.ObjectPropertyAssertion;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.csv.CSVTypeAwareModelSlot;
import org.openflexo.technologyadapter.csv.metamodel.CSVMetaModel;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.technologyadapter.csv.model.CSVObjectIndividual;

/**
 * Create CSV Object.
 * 
 * @author gbesancon
 * 
 */

@FIBPanel("Fib/AddCSVObjectIndividual.fib")
@ModelEntity
@ImplementationClass(AddCSVObjectIndividual.AddCSVObjectIndividualImpl.class)
@XMLElement
public interface AddCSVObjectIndividual extends AddIndividual<CSVTypeAwareModelSlot, CSVObjectIndividual> {

	public static abstract class AddCSVObjectIndividualImpl extends AddIndividualImpl<CSVTypeAwareModelSlot, CSVObjectIndividual> implements
			AddCSVObjectIndividual {

		private static final Logger logger = Logger.getLogger(AddCSVObjectIndividual.class.getPackage().getName());

		public AddCSVObjectIndividualImpl() {
			super();
		}

		@Override
		public Class<CSVObjectIndividual> getOntologyIndividualClass() {
			return CSVObjectIndividual.class;
		}

		@Override
		public CSVObjectIndividual performAction(FlexoBehaviourAction action) {
			CSVObjectIndividual result = null;
			// TODO : Implement Action
			return result;
		}

	}
}
