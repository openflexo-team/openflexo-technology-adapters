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

package org.openflexo.technologyadapter.excel;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.fml.ExcelActorReference;
import org.openflexo.technologyadapter.excel.fml.ExcelCellRole;
import org.openflexo.technologyadapter.excel.fml.ExcelColumnRole;
import org.openflexo.technologyadapter.excel.fml.ExcelRowRole;
import org.openflexo.technologyadapter.excel.fml.ExcelSheetRole;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelCell;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelRow;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelSheet;
import org.openflexo.technologyadapter.excel.fml.editionaction.CellStyleAction;
import org.openflexo.technologyadapter.excel.fml.editionaction.CreateExcelResource;
import org.openflexo.technologyadapter.excel.fml.editionaction.GenerateExcelResource;
import org.openflexo.technologyadapter.excel.fml.editionaction.MergeCells;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectExcelCell;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectExcelRow;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectExcelSheet;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectUniqueExcelCell;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectUniqueExcelRow;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectUniqueExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelObject;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.toolbox.StringUtils;

/**
 * Implementation of a basic ModelSlot class for the Excel technology adapter<br>
 * This model slot reflects a basic interpretation of a workbook, with basic excel notions, such as workbook, sheet, row, col, and cell
 * 
 * @author Vincent Leildé, Sylvain Guérin
 * 
 */
@DeclareActorReferences({ ExcelActorReference.class })
@DeclareFlexoRoles({ ExcelSheetRole.class, ExcelColumnRole.class, ExcelRowRole.class, ExcelCellRole.class })
@DeclareEditionActions({ CreateExcelResource.class, GenerateExcelResource.class, AddExcelCell.class, AddExcelRow.class, AddExcelSheet.class,
		CellStyleAction.class, MergeCells.class })
@DeclareFetchRequests({ SelectExcelSheet.class, SelectExcelRow.class, SelectExcelCell.class, SelectUniqueExcelSheet.class,
		SelectUniqueExcelRow.class, SelectUniqueExcelCell.class })
@ModelEntity
@ImplementationClass(BasicExcelModelSlot.BasicExcelModelSlotImpl.class)
@XMLElement
public interface BasicExcelModelSlot extends FreeModelSlot<ExcelWorkbook> {

	@PropertyIdentifier(type = FlexoResource.class)
	public static final String TEMPLATE_RESOURCE_KEY = "templateResource";

	@PropertyIdentifier(type = String.class)
	public static final String TEMPLATE_WORKBOOK_URI_KEY = "templateWorkbookURI";

	@Getter(value = TEMPLATE_WORKBOOK_URI_KEY)
	@XMLAttribute
	public String getTemplateWorkbookURI();

	@Setter(TEMPLATE_WORKBOOK_URI_KEY)
	public void setTemplateWorkbookURI(String templateWorkbookURI);

	@Getter(TEMPLATE_RESOURCE_KEY)
	public ExcelWorkbookResource getTemplateResource();

	@Setter(TEMPLATE_RESOURCE_KEY)
	public void setTemplateResource(ExcelWorkbookResource templateResource);

	public static abstract class BasicExcelModelSlotImpl extends FreeModelSlotImpl<ExcelWorkbook> implements BasicExcelModelSlot {

		private static final Logger logger = Logger.getLogger(BasicExcelModelSlot.class.getPackage().getName());

		// private BasicExcelModelSlotURIProcessor uriProcessor;

		private final Map<String, ExcelObject> uriCache = new HashMap<>();

		private ExcelWorkbookResource templateResource;
		protected String templateWorkbookURI;

		@Override
		public String getTemplateWorkbookURI() {
			if (getTemplateResource() != null) {
				return getTemplateResource().getURI();
			}
			return templateWorkbookURI;
		}

		@Override
		public void setTemplateWorkbookURI(String templateWorkbookURI) {
			if ((templateWorkbookURI == null && this.templateWorkbookURI != null)
					|| (templateWorkbookURI != null && !templateWorkbookURI.equals(this.templateWorkbookURI))) {
				String oldValue = this.templateWorkbookURI;
				this.templateWorkbookURI = templateWorkbookURI;
				getPropertyChangeSupport().firePropertyChange(TEMPLATE_WORKBOOK_URI_KEY, oldValue, templateWorkbookURI);
			}
		}

		@Override
		public ExcelWorkbookResource getTemplateResource() {
			if (templateResource == null && StringUtils.isNotEmpty(templateWorkbookURI)
					&& getServiceManager().getResourceManager() != null) {
				// System.out.println("Looking up " + templateDocumentURI);
				templateResource = (ExcelWorkbookResource) getServiceManager().getResourceManager().getResource(templateWorkbookURI);
				// System.out.println("templateResource = " + returned);
				// for (FlexoResource r : getServiceManager().getResourceManager().getRegisteredResources()) {
				// System.out.println("> " + r.getURI());
				// }
			}
			return templateResource;
		}

		@Override
		public void setTemplateResource(ExcelWorkbookResource templateResource) {
			if (templateResource != this.templateResource) {
				ExcelWorkbookResource oldValue = this.templateResource;
				this.templateResource = templateResource;
				getPropertyChangeSupport().firePropertyChange("templateResource", oldValue, templateResource);
			}
		}

		@Override
		public Class<ExcelTechnologyAdapter> getTechnologyAdapterClass() {
			return ExcelTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (ExcelCellRole.class.isAssignableFrom(patternRoleClass)) {
				return "cell";
			}
			else if (ExcelRowRole.class.isAssignableFrom(patternRoleClass)) {
				return "row";
			}
			else if (ExcelSheetRole.class.isAssignableFrom(patternRoleClass)) {
				return "sheet";
			}
			return null;
		}

		@Override
		public Type getType() {
			return ExcelWorkbook.class;
		}

		@Override
		public String getURIForObject(ExcelWorkbook model, Object o) {
			ExcelObject excelObject = (ExcelObject) o;

			String builtURI = null;

			try {
				builtURI = URLEncoder.encode(excelObject.getSerializationIdentifier(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.warning("Cannot process URI - Unexpected encoding error");
				e.printStackTrace();
			}

			if (builtURI != null) {
				if (uriCache.get(builtURI) == null) {
					// TODO Manage the fact that URI May Change
					uriCache.put(builtURI, excelObject);
				}
			}
			return builtURI.toString();
		}

		@Override
		public Object retrieveObjectWithURI(ExcelWorkbook model, String objectURI) {

			try {
				String builtURI = URLEncoder.encode(objectURI, "UTF-8");
				ExcelObject o = uriCache.get(builtURI);
				if (o != null) {
					return o;
				}
				else {
					TechnologyAdapterResource<ExcelWorkbook, ?> resource = model.getResource();
					if (!resource.isLoaded()) {
						resource.loadResourceData();
					}
					System.out.println("Tiens, la faut retrouver " + URLDecoder.decode(objectURI, "UTF-8"));
					return null;
					/*ArrayList<ExcelObject> excelObject = (ArrayList<ExcelObject>) msInstance.getAccessedResourceData()
							.getAccessibleExcelObjects();
					if (excelObject.size() > 100) {
						logger.fine("WARNING: more than one hundred lines in Excel file");
					}
					for (ExcelObject obj : excelObject) {
						if (obj.getUri().equals(URLDecoder.decode(objectURI, "UTF-8"))) {
							return obj;
						}
					}*/
				}

				// return o;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public ExcelTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (ExcelTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		/*@Override
		public ExcelWorkbookResource createProjectSpecificEmptyResource(VirtualModelInstance<?, ?> view, String filename, String modelUri) {
			try {
				return getModelSlotTechnologyAdapter().createNewWorkbook(view.getResourceCenter(), filename);
			} catch (SaveResourceException e) {
				e.printStackTrace();
			} catch (ModelDefinitionException e) {
				e.printStackTrace();
			}
			return null;
		}*/

	}
}
