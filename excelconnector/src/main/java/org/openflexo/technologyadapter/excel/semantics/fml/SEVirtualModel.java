/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.semantics.fml;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.toolbox.StringUtils;

/**
 * Specialization of a {@link VirtualModel} to define a FML-contractualized access to an {@link ExcelWorkbook}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(SEVirtualModel.SEVirtualModelImpl.class)
@XMLElement
@Imports({ @Import(SEFlexoConcept.class) })
public interface SEVirtualModel extends VirtualModel {

	@PropertyIdentifier(type = ExcelWorkbookResource.class)
	String TEMPLATE_EXCEL_WORKBOOK_RESOURCE = "templateExcelWorkbookResource";
	@PropertyIdentifier(type = String.class)
	String TEMPLATE_EXCEL_WORKBOOK_URI = "templateExcelWorkbookURI";

	@Getter(TEMPLATE_EXCEL_WORKBOOK_RESOURCE)
	public ExcelWorkbookResource getTemplateExcelWorkbookResource();

	@Setter(TEMPLATE_EXCEL_WORKBOOK_RESOURCE)
	public void setTemplateExcelWorkbookResource(ExcelWorkbookResource excelWorkbookResource);

	@Getter(TEMPLATE_EXCEL_WORKBOOK_URI)
	@XMLAttribute
	public String getTemplateExcelWorkbookURI();

	@Setter(TEMPLATE_EXCEL_WORKBOOK_URI)
	public void setTemplateExcelWorkbookURI(String excelWorkbook);

	public static abstract class SEVirtualModelImpl extends VirtualModelImpl implements SEVirtualModel {

		private static final Logger logger = Logger.getLogger(SEVirtualModelImpl.class.getPackage().getName());

		private ExcelWorkbookResource templateWBResource;
		private String templateWBURI;

		private SEVirtualModelInstanceType vmInstanceType = new SEVirtualModelInstanceType(this);

		@Override
		public ExcelWorkbookResource getTemplateExcelWorkbookResource() {
			if (templateWBResource == null && StringUtils.isNotEmpty(templateWBURI) && getServiceManager() != null
					&& getServiceManager().getResourceManager() != null) {
				templateWBResource = (ExcelWorkbookResource) getServiceManager().getResourceManager().getResource(templateWBURI,
						ExcelWorkbook.class);
				logger.info("Looked-up " + templateWBResource + " for " + templateWBURI);
			}
			return templateWBResource;
		}

		@Override
		public void setTemplateExcelWorkbookResource(ExcelWorkbookResource excelWorkbookResource) {
			this.templateWBResource = excelWorkbookResource;
		}

		@Override
		public String getTemplateExcelWorkbookURI() {
			if (templateWBResource != null) {
				return templateWBResource.getURI();
			}
			return templateWBURI;
		}

		@Override
		public void setTemplateExcelWorkbookURI(String excelWorkbookURI) {
			this.templateWBURI = excelWorkbookURI;
		}

		@Override
		public SEVirtualModelInstanceType getInstanceType() {
			return vmInstanceType;
		}

	}
}
