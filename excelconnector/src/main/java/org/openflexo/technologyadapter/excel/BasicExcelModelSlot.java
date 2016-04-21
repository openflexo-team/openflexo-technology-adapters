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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoBehaviourParameters;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.fml.ExcelActorReference;
import org.openflexo.technologyadapter.excel.fml.ExcelCellParameter;
import org.openflexo.technologyadapter.excel.fml.ExcelCellRole;
import org.openflexo.technologyadapter.excel.fml.ExcelColumnParameter;
import org.openflexo.technologyadapter.excel.fml.ExcelColumnRole;
import org.openflexo.technologyadapter.excel.fml.ExcelRowParameter;
import org.openflexo.technologyadapter.excel.fml.ExcelRowRole;
import org.openflexo.technologyadapter.excel.fml.ExcelSheetParameter;
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
@DeclareActorReferences({ ExcelActorReference.class })
@DeclareFlexoRoles({ ExcelSheetRole.class, ExcelColumnRole.class, ExcelRowRole.class, ExcelCellRole.class })
@DeclareEditionActions({ AddExcelCell.class, AddExcelRow.class, AddExcelSheet.class, CellStyleAction.class })
@DeclareFetchRequests({ SelectExcelSheet.class, SelectExcelRow.class, SelectExcelCell.class })
@DeclareFlexoBehaviourParameters({ ExcelSheetParameter.class, ExcelCellParameter.class, ExcelRowParameter.class, ExcelColumnParameter.class })
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
		public ModelSlotInstanceConfiguration<BasicExcelModelSlot, ExcelWorkbook> createConfiguration(
				AbstractVirtualModelInstance<?, ?> virtualModelInstance, FlexoProject project) {
			return new BasicExcelModelSlotInstanceConfiguration(this, virtualModelInstance, project);
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
				}
				else {
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
			return getModelSlotTechnologyAdapter().createNewWorkbook(view.getProject(), filename/*, modelUri*/);
		}

		@Override
		public TechnologyAdapterResource<ExcelWorkbook, ExcelTechnologyAdapter> createSharedEmptyResource(
				FlexoResourceCenter<?> resourceCenter, String relativePath, String filename, String modelUri) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
