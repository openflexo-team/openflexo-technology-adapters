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

package org.openflexo.technologyadapter.xx.model;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.Getter.Cardinality;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.xx.rm.XXTextResource;

/**
 * Represents the {@link ResourceData} deserialized from a {@link XXTextResource}<br>
 * 
 * Note: Purpose of that class is to demonstrate API of a {@link TechnologyAdapter}, thus the semantics is here pretty simple: a
 * {@link XXText} is a plain text file contents, serialized as a {@link String}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = XXText.XXTextImpl.class)
public interface XXText extends XXObject, ResourceData<XXText> {

	@PropertyIdentifier(type = XXLine.class, cardinality = Cardinality.LIST)
	public static final String LINES_KEY = "lines";

	/**
	 * Return contents of text file
	 * 
	 * @return
	 */
	public String getContents();

	/**
	 * Return all {@link XXLine} defined in this {@link XXText}
	 * 
	 * @return
	 */
	@Getter(value = LINES_KEY, cardinality = Cardinality.LIST, inverse = XXLine.XX_TEXT_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(StrategyType.CLONE)
	public List<XXLine> getLines();

	@Adder(LINES_KEY)
	@PastingPoint
	public void addToLines(XXLine aLine);

	@Remover(LINES_KEY)
	public void removeFromLines(XXLine aLine);

	@Override
	public XXTextResource getResource();

	/**
	 * Default base implementation for {@link XXText}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class XXTextImpl extends XXObjectImpl implements XXText {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(XXObjectImpl.class.getPackage().getName());

		public static final String ALL_KEY = "All";

		private String contents = null;

		@Override
		public XXText getResourceData() {
			return this;
		}

		@Override
		public XXTextResource getResource() {
			return (XXTextResource) performSuperGetter(FLEXO_RESOURCE);
		}

		@Override
		public String toString() {
			return super.toString() + "-" + getResource();
		}

		@Override
		public String getContents() {
			if (contents == null) {
				StringBuffer sb = new StringBuffer();
				for (XXLine xxLine : getLines()) {
					sb.append(xxLine.getValue() + "\n");
				}
				contents = sb.toString();
			}
			return contents;
		}

		private void clearContents() {
			contents = null;
		}

		@Override
		public void addToLines(XXLine aLine) {
			performSuperAdder(LINES_KEY, aLine);
			clearContents();
		}

		@Override
		public void removeFromLines(XXLine aLine) {
			performSuperRemover(LINES_KEY, aLine);
			clearContents();
		}

	}

}
