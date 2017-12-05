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

package org.openflexo.technologyadapter.odt.fml.action;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.editionaction.TechnologySpecificAction;
import org.openflexo.foundation.fml.editionaction.TechnologySpecificActionDefiningReceiver;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.odt.ODTModelSlot;
import org.openflexo.technologyadapter.odt.model.ODTDocument;
import org.openflexo.technologyadapter.odt.model.ODTParagraph;

/**
 * {@link TechnologySpecificAction} dedicated to ODT paragraph creation
 * 
 * @author sylvain
 * 
 */

@ModelEntity
@ImplementationClass(AddODTParagraph.AddODTParagraphImpl.class)
@XMLElement
public interface AddODTParagraph extends TechnologySpecificActionDefiningReceiver<ODTModelSlot, ODTDocument, ODTParagraph> {

	public static abstract class AddODTParagraphImpl
			extends TechnologySpecificActionDefiningReceiverImpl<ODTModelSlot, ODTDocument, ODTParagraph> implements AddODTParagraph {

		private static final Logger logger = Logger.getLogger(AddODTParagraph.class.getPackage().getName());

		public AddODTParagraphImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return ODTParagraph.class;
		}

		@Override
		public ODTParagraph execute(RunTimeEvaluationContext evaluationContext) {
			ODTParagraph result = null;
			// TODO : Implement Action
			return result;
		}

	}
}
