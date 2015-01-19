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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.fml.editionaction.FetchRequest;
import org.openflexo.foundation.fml.editionaction.SelectIndividual;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.csv.CSVTypeAwareModelSlot;
import org.openflexo.technologyadapter.csv.metamodel.CSVMetaModel;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.technologyadapter.csv.model.CSVObjectIndividual;

/**
 * CSV technology - specific {@link FetchRequest} allowing to retrieve a selection of some {@link CSVObjectIndividual} matching some
 * conditions and a given type.<br>
 * 
 * @author Jean Le Paon
 */

@ModelEntity
@ImplementationClass(SelectCSVObjectIndividual.SelectCSVObjectIndividualImpl.class)
@XMLElement
public interface SelectCSVObjectIndividual extends SelectIndividual<CSVTypeAwareModelSlot, CSVObjectIndividual> {

	public static abstract class SelectCSVObjectIndividualImpl extends SelectIndividualImpl<CSVTypeAwareModelSlot, CSVObjectIndividual> implements
			SelectCSVObjectIndividual {

		private static final Logger logger = Logger.getLogger(SelectCSVObjectIndividual.class.getPackage().getName());

		public SelectCSVObjectIndividualImpl() {
			super();
		}

		@Override
		public List<CSVObjectIndividual> performAction(FlexoBehaviourAction action) {
			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			List<CSVObjectIndividual> returned = null;

			// TODO : Implement Action

			return returned;
		}

		@Override
		public TypeAwareModelSlotInstance<CSVModel, CSVMetaModel, CSVTypeAwareModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (TypeAwareModelSlotInstance<CSVModel, CSVMetaModel, CSVTypeAwareModelSlot>) super.getModelSlotInstance(action);
		}

	}
}
