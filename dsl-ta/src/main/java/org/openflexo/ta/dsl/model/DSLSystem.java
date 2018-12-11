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

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Finder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.Getter.Cardinality;
import org.openflexo.ta.dsl.rm.DSLResource;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.XMLElement;

/**
 * Represents the {@link ResourceData} deserialized from a {@link DSLResource}<br>
 * 
 * Note: Purpose of that class is to demonstrate API of a {@link TechnologyAdapter}, thus the semantics is here trivial
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = DSLSystem.DSLSystemImpl.class)
public interface DSLSystem extends DSLObject, ResourceData<DSLSystem> {

	@PropertyIdentifier(type = DSLComponent.class, cardinality = Cardinality.LIST)
	public static final String COMPONENTS_KEY = "components";
	@PropertyIdentifier(type = DSLLink.class, cardinality = Cardinality.LIST)
	public static final String LINKS_KEY = "links";

	/**
	 * Return all {@link DSLComponent} defined in this {@link DSLSystem}
	 * 
	 * @return
	 */
	@Getter(value = COMPONENTS_KEY, cardinality = Cardinality.LIST, inverse = DSLComponent.SYSTEM_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(StrategyType.CLONE)
	public List<DSLComponent> getComponents();

	@Adder(COMPONENTS_KEY)
	@PastingPoint
	public void addToComponents(DSLComponent aComponent);

	@Remover(COMPONENTS_KEY)
	public void removeFromComponents(DSLComponent aComponent);

	@Finder(collection = COMPONENTS_KEY, attribute = DSLComponent.NAME_KEY)
	public DSLComponent getComponent(String componentName);

	/**
	 * Return all {@link DSLLink} defined in this {@link DSLSystem}
	 * 
	 * @return
	 */
	@Getter(value = LINKS_KEY, cardinality = Cardinality.LIST, inverse = DSLLink.SYSTEM_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(StrategyType.CLONE)
	public List<DSLLink> getLinks();

	@Adder(LINKS_KEY)
	@PastingPoint
	public void addToLinks(DSLLink aLink);

	@Remover(LINKS_KEY)
	public void removeFromLinks(DSLLink aLink);

	@Override
	public DSLResource getResource();

	/**
	 * Retrieve object with supplied serialization identifier, asserting this object resides in this {@link DSLSystem}
	 * 
	 * @param objectId
	 * @return
	 */
	public DSLObject getObjectWithSerializationIdentifier(String objectId);

	/**
	 * Default base implementation for {@link DSLSystem}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class DSLSystemImpl extends DSLObjectImpl implements DSLSystem {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(DSLSystemImpl.class.getPackage().getName());

		@Override
		public DSLSystem getResourceData() {
			return this;
		}

		@Override
		public DSLResource getResource() {
			return (DSLResource) performSuperGetter(FLEXO_RESOURCE);
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("[System]\n");
			for (DSLComponent component : getComponents()) {
				sb.append(component.toString() + "\n");
			}
			for (DSLLink link : getLinks()) {
				sb.append(link.toString() + "\n");
			}
			return sb.toString();
		}

	}

}
