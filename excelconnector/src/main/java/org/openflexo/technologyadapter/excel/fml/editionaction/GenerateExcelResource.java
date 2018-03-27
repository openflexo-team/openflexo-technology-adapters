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

package org.openflexo.technologyadapter.excel.fml.editionaction;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResourceFactory;

@ModelEntity
@ImplementationClass(GenerateExcelResource.GenerateExcelResourceImpl.class)
@XMLElement
@FML("GenerateDocXDocument")
public interface GenerateExcelResource extends CreateExcelResource {

	/*@PropertyIdentifier(type = File.class)
	public static final String FILE_KEY = "file";
	
	@Getter(value = FILE_KEY)
	@XMLAttribute
	public File getFile();
	
	@Setter(FILE_KEY)
	public void setFile(File aFile);*/

	public static abstract class GenerateExcelResourceImpl extends CreateExcelResourceImpl implements GenerateExcelResource {

		private static final Logger logger = Logger.getLogger(GenerateExcelResource.class.getPackage().getName());

		@Override
		public ExcelWorkbook execute(RunTimeEvaluationContext evaluationContext) throws FlexoException {

			if (getAssignedModelSlot() != null) {
				ExcelWorkbookResource templateResource = getAssignedModelSlot().getTemplateResource();
				if (templateResource == null) {
					return super.execute(evaluationContext);
				}

				System.out.println("OK, on cree un fichier excel avec un template");

				String resourceName = getResourceName(evaluationContext);
				String resourceURI = getResourceURI(evaluationContext);
				FlexoResourceCenter<?> rc = getResourceCenter(evaluationContext);

				System.out.println("name=" + resourceName);
				System.out.println("uri=" + resourceURI);
				System.out.println("relative path=" + getRelativePath());
				System.out.println("rc=" + rc);

				ExcelTechnologyAdapter excelTA = getServiceManager().getTechnologyAdapterService()
						.getTechnologyAdapter(ExcelTechnologyAdapter.class);

				ExcelWorkbookResource newResource;
				try {

					ExcelWorkbook templateDocument = templateResource.getResourceData();

					newResource = createResource(excelTA, ExcelWorkbookResourceFactory.class, rc, resourceName, resourceURI,
							getRelativePath(), ".xlsx", false);
					System.out.println("Return new excel workbook resource: " + newResource);

					newResource.setResourceData(templateDocument);
					newResource.save();
					newResource.unloadResourceData(false);
					templateResource.unloadResourceData(false);
					newResource.loadResourceData();

					System.out.println("Et hop on relit la RD");

					ExcelWorkbook generatedDocument = newResource.getResourceData();

					System.out.println("Et hop, en fin de generate, on retourne " + generatedDocument);
					System.out.println("-------> generatedDocument.getResource()=" + generatedDocument.getResource());
					System.out.println("-------> newResource=" + newResource);

					// Very important: we must now set ModelSlotInstance !
					/*if (getModelSlotInstance(evaluationContext) != null) {
						getModelSlotInstance(evaluationContext).setAccessedResourceData(generatedDocument);
					}*/

					// System.out.println("Return " + generatedDocument);
					return generatedDocument;
				} catch (ModelDefinitionException e) {
					new FlexoException(e);
				} catch (FileNotFoundException e) {
					new FlexoException(e);
				} catch (ResourceLoadingCancelledException e) {
					new FlexoException(e);
				}
			}
			logger.warning("Could not create resource!");
			return null;

		}
	}
}
