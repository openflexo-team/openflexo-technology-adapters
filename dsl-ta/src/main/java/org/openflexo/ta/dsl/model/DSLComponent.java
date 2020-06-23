/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.ta.dsl.model;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Finder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.Getter.Cardinality;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.XMLElement;

/**
 * Represents a component of a {@link DSLSystem}
 * 
 * Note: Purpose of that class is to demonstrate API of a {@link TechnologyAdapter}, thus the semantics is here pretty simple: a
 * {@link DSLSystem} is a plain text file contents, serialized as a {@link String}, and a {@link DSLComponent} is a line of that file,
 * represented as a String
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = DSLComponent.DSLComponentImpl.class)
@XMLElement
public interface DSLComponent extends DSLAbstractComponent {

	@PropertyIdentifier(type = DSLSlot.class, cardinality = Cardinality.LIST)
	public static final String SLOTS_KEY = "slots";

	/**
	 * Return all {@link DSLSlot} defined in this {@link DSLComponent}
	 * 
	 * @return
	 */
	@Getter(value = SLOTS_KEY, cardinality = Cardinality.LIST, inverse = DSLSlot.COMPONENT_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(StrategyType.CLONE)
	public List<DSLSlot> getSlots();

	@Adder(SLOTS_KEY)
	@PastingPoint
	public void addToSlots(DSLSlot aSlot);

	@Remover(SLOTS_KEY)
	public void removeFromSlots(DSLSlot aSlot);

	@Finder(collection = SLOTS_KEY, attribute = DSLSlot.NAME_KEY)
	public DSLSlot getSlot(String slotName);

	/**
	 * Default base implementation for {@link DSLComponent}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class DSLComponentImpl extends DSLAbstractComponentImpl implements DSLComponent {

		private static final Logger logger = Logger.getLogger(DSLComponent.class.getPackage().getName());

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[Component]" + getName());
			for (DSLSlot dslSlot : getSlots()) {
				sb.append("\n" + dslSlot.toString());
			}
			return sb.toString();
		}

	}
}
