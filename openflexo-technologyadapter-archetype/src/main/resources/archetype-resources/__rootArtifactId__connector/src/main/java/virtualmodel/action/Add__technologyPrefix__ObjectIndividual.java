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
import ${package}.${technologyPrefix}TypeAwareModelSlot;
import ${package}.metamodel.${technologyPrefix}MetaModel;
import ${package}.model.${technologyPrefix}Model;
import ${package}.model.${technologyPrefix}ObjectIndividual;

/**
 * Create ${technologyPrefix} Object.
 * 
 * @author gbesancon
 * 
 */

@FIBPanel("Fib/Add${technologyPrefix}ObjectIndividual.fib")
@ModelEntity
@ImplementationClass(Add${technologyPrefix}ObjectIndividual.Add${technologyPrefix}ObjectIndividualImpl.class)
@XMLElement
public interface Add${technologyPrefix}ObjectIndividual extends AddIndividual<${technologyPrefix}TypeAwareModelSlot, ${technologyPrefix}ObjectIndividual> {

	public static abstract class Add${technologyPrefix}ObjectIndividualImpl extends AddIndividualImpl<${technologyPrefix}TypeAwareModelSlot, ${technologyPrefix}ObjectIndividual> implements
			Add${technologyPrefix}ObjectIndividual {

		private static final Logger logger = Logger.getLogger(Add${technologyPrefix}ObjectIndividual.class.getPackage().getName());

		public Add${technologyPrefix}ObjectIndividualImpl() {
			super();
		}

		@Override
		public Class<${technologyPrefix}ObjectIndividual> getOntologyIndividualClass() {
			return ${technologyPrefix}ObjectIndividual.class;
		}

		@Override
		public ${technologyPrefix}ObjectIndividual performAction(FlexoBehaviourAction action) {
			${technologyPrefix}ObjectIndividual result = null;
			// TODO : Implement Action
			return result;
		}

	}
}
