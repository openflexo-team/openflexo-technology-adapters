/*
 * (c) Copyright 2013 Openflexo
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
package org.openflexo.technologyadapter.excel;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareActorReference;
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionAction;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequest;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.fml.ExcelActorReference;
import org.openflexo.technologyadapter.excel.fml.ExcelCellRole;
import org.openflexo.technologyadapter.excel.fml.ExcelColumnRole;
import org.openflexo.technologyadapter.excel.fml.ExcelRowRole;
import org.openflexo.technologyadapter.excel.fml.ExcelSheetRole;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelCell;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelRow;
import org.openflexo.technologyadapter.excel.fml.editionaction.AddExcelSheet;
import org.openflexo.technologyadapter.excel.fml.editionaction.CellStyleAction;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectExcelCell;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectExcelRow;
import org.openflexo.technologyadapter.excel.fml.editionaction.SelectExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelObject;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;

/**
 * Implementation of a basic ModelSlot class for the Excel technology adapter<br>
 * This model slot reflects a basic interpretation of a workbook, with basic excel notions, such as workbook, sheet, row, col, and cell
 * 
 * @author Vincent LeildÃ©, Sylvain GuÃ©rin
 * 
 */
@DeclareActorReferences({ // All actor references available through this model slot
@DeclareActorReference(FML = "ExcelActorReference", actorReferenceClass = ExcelActorReference.class) })
@DeclareFlexoRoles({ // All pattern roles available through this model slot
@DeclareFlexoRole(FML = "ExcelSheet", flexoRoleClass = ExcelSheetRole.class), // Sheet
		@DeclareFlexoRole(FML = "ExcelColumn", flexoRoleClass = ExcelColumnRole.class), // Sheet
		@DeclareFlexoRole(FML = "ExcelRow", flexoRoleClass = ExcelRowRole.class), // Row
		@DeclareFlexoRole(FML = "ExcelCell", flexoRoleClass = ExcelCellRole.class) // Cell
})
@DeclareEditionActions({ // All edition actions available through this model
// slot
@DeclareEditionAction(FML = "AddExcelCell", editionActionClass = AddExcelCell.class), // Add
																						// cell
		@DeclareEditionAction(FML = "AddExcelRow", editionActionClass = AddExcelRow.class), // Add
																							// row
		@DeclareEditionAction(FML = "AddExcelSheet", editionActionClass = AddExcelSheet.class), // Add
																								// sheet
		@DeclareEditionAction(FML = "CellStyleAction", editionActionClass = CellStyleAction.class) // Cell
// Style
})
@DeclareFetchRequests({ // All requests available through this model slot
@DeclareFetchRequest(FML = "SelectExcelSheet", fetchRequestClass = SelectExcelSheet.class), // Select
																							// Excel
																							// Sheet
		@DeclareFetchRequest(FML = "SelectExcelRow", fetchRequestClass = SelectExcelRow.class), // Select
																								// Excel
																								// Row
		@DeclareFetchRequest(FML = "SelectExcelCell", fetchRequestClass = SelectExcelCell.class) // Select
// Excel
// Cell
})
@ModelEntity
@ImplementationClass(BasicExcelModelSlot.BasicExcelModelSlotImpl.class)
@XMLElement
public interface BasicExcelModelSlot extends FreeModelSlot<ExcelWorkbook> {

	public static abstract class BasicExcelModelSlotImpl extends FreeModelSlotImpl<ExcelWorkbook> implements BasicExcelModelSlot {

		private static final Logger logger = Logger.getLogger(BasicExcelModelSlot.class.getPackage().getName());

		// private BasicExcelModelSlotURIProcessor uriProcessor;

		private final Map<String, ExcelObject> uriCache = new HashMap<String, ExcelObject>();

		/*public BasicExcelModelSlotURIProcessor getUriProcessor() {
			if (uriProcessor == null && getFMLModelFactory() != null) {
				uriProcessor = getFMLModelFactory().newInstance(BasicExcelModelSlotURIProcessor.class);
			}
			return uriProcessor;
		}*/

		@Override
		public Class<ExcelTechnologyAdapter> getTechnologyAdapterClass() {
			return ExcelTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (ExcelCellRole.class.isAssignableFrom(patternRoleClass)) {
				return "cell";
			} else if (ExcelRowRole.class.isAssignableFrom(patternRoleClass)) {
				return "row";
			} else if (ExcelSheetRole.class.isAssignableFrom(patternRoleClass)) {
				return "sheet";
			}
			return null;
		}

		@Override
		public Type getType() {
			return ExcelWorkbook.class;
		}

		@Override
		public ModelSlotInstanceConfiguration<BasicExcelModelSlot, ExcelWorkbook> createConfiguration(CreateVirtualModelInstance action) {
			return new BasicExcelModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public String getURIForObject(FreeModelSlotInstance<ExcelWorkbook, ? extends FreeModelSlot<ExcelWorkbook>> msInstance, Object o) {
			ExcelObject excelObject = (ExcelObject) o;

			String builtURI = null;

			try {
				builtURI = URLEncoder.encode(excelObject.getUri(), "UTF-8");
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
		public Object retrieveObjectWithURI(FreeModelSlotInstance<ExcelWorkbook, ? extends FreeModelSlot<ExcelWorkbook>> msInstance,
				String objectURI) {

			try {
				String builtURI = URLEncoder.encode(objectURI, "UTF-8");
				ExcelObject o = uriCache.get(builtURI);
				if (o != null) {
					return o;
				} else {
					TechnologyAdapterResource<ExcelWorkbook, ?> resource = msInstance.getResource();
					if (!resource.isLoaded()) {
						resource.loadResourceData(null);
					}
					ArrayList<ExcelObject> excelObject = (ArrayList<ExcelObject>) msInstance.getAccessedResourceData()
							.getAccessibleExcelObjects();
					for (ExcelObject obj : excelObject) {
						if (obj.getUri().equals(URLDecoder.decode(objectURI, "UTF-8"))) {
							return obj;
						}
					}
				}

				return o;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public ExcelTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (ExcelTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public ExcelWorkbookResource createProjectSpecificEmptyResource(View view, String filename, String modelUri) {
			return getModelSlotTechnologyAdapter().createNewWorkbook(view.getProject(), filename, modelUri);
		}

		@Override
		public TechnologyAdapterResource<ExcelWorkbook, ExcelTechnologyAdapter> createSharedEmptyResource(
				FlexoResourceCenter<?> resourceCenter, String relativePath, String filename, String modelUri) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
