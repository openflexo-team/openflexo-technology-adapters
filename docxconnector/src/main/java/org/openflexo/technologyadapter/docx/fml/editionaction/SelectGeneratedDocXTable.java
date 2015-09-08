/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.fml.editionaction;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.fml.DocXTableRole;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXTable;
import org.openflexo.toolbox.StringUtils;

/**
 * This {@link EditionAction} allows to lookup a table in a generated document matching a template table
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(SelectGeneratedDocXTable.SelectGeneratedDocXTableImpl.class)
@XMLElement
@FML("SelectGeneratedDocXTable")
public interface SelectGeneratedDocXTable extends DocXTableAction {

	@PropertyIdentifier(type = String.class)
	public static final String TABLE_ID_KEY = "tableId";

	/**
	 * Return identifier of table in the template resource<br>
	 * Note that is not the identifier of table that is to be managed at run-time
	 * 
	 * @return
	 */
	@Getter(TABLE_ID_KEY)
	@XMLAttribute
	public String getTableId();

	/**
	 * Sets identifier of table in the template resource<br>
	 * 
	 * @param fragment
	 */
	@Setter(TABLE_ID_KEY)
	public void setTableId(String identifier);

	public static abstract class SelectGeneratedDocXTableImpl extends DocXTableActionImpl implements SelectGeneratedDocXTable {

		private static final Logger logger = Logger.getLogger(SelectGeneratedDocXTable.class.getPackage().getName());

		@Override
		public String getTableId() {
			if (getAssignedFlexoProperty() instanceof DocXTableRole) {
				return ((DocXTableRole) getAssignedFlexoProperty()).getTableId();
			}
			return (String) performSuperGetter(TABLE_ID_KEY);
		}

		@Override
		public DocXTable execute(RunTimeEvaluationContext evaluationContext) throws FlexoException {

			if (getModelSlotInstance(evaluationContext) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(evaluationContext).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			DocXDocument document = (DocXDocument) getModelSlotInstance(evaluationContext).getAccessedResourceData();

			if (document != null && StringUtils.isNotEmpty(getTableId())) {
				return (DocXTable) document.getElementWithIdentifier(getTableId());
			}

			logger.warning("Could not find table matching template table. Abort.");
			return null;
		}
	}
}
