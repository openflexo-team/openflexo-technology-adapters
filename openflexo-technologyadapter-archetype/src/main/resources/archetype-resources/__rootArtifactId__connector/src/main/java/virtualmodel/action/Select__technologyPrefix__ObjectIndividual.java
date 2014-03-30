#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.virtualmodel.action;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.editionaction.FetchRequest;
import org.openflexo.foundation.viewpoint.editionaction.SelectIndividual;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import ${package}.${technologyPrefix}TypeAwareModelSlot;
import ${package}.metamodel.${technologyPrefix}MetaModel;
import ${package}.model.${technologyPrefix}Model;
import ${package}.model.${technologyPrefix}ObjectIndividual;

/**
 * ${technologyPrefix} technology - specific {@link FetchRequest} allowing to retrieve a selection of some {@link ${technologyPrefix}ObjectIndividual} matching some
 * conditions and a given type.<br>
 * 
 * @author ${author}
 */

@ModelEntity
@ImplementationClass(Select${technologyPrefix}ObjectIndividual.Select${technologyPrefix}ObjectIndividualImpl.class)
@XMLElement
public interface Select${technologyPrefix}ObjectIndividual extends SelectIndividual<${technologyPrefix}TypeAwareModelSlot, ${technologyPrefix}ObjectIndividual> {

	public static abstract class Select${technologyPrefix}ObjectIndividualImpl extends SelectIndividualImpl<${technologyPrefix}TypeAwareModelSlot, ${technologyPrefix}ObjectIndividual> implements
			Select${technologyPrefix}ObjectIndividual {

		private static final Logger logger = Logger.getLogger(Select${technologyPrefix}ObjectIndividual.class.getPackage().getName());

		public Select${technologyPrefix}ObjectIndividualImpl() {
			super();
		}

		@Override
		public List<${technologyPrefix}ObjectIndividual> performAction(FlexoBehaviourAction action) {
			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			List<${technologyPrefix}ObjectIndividual> returned = null;

			// TODO : Implement Action

			return returned;
		}

		@Override
		public TypeAwareModelSlotInstance<${technologyPrefix}Model, ${technologyPrefix}MetaModel, ${technologyPrefix}TypeAwareModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (TypeAwareModelSlotInstance<${technologyPrefix}Model, ${technologyPrefix}MetaModel, ${technologyPrefix}TypeAwareModelSlot>) super.getModelSlotInstance(action);
		}

	}
}
