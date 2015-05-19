/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Openflexo-technologyadapter-archetype, a component of the software infrastructure 
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
		public List<CSVObjectIndividual> execute(FlexoBehaviourAction action) {
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
