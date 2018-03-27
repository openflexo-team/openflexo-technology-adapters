/**
 * 
 * Copyright (c) 2016-, Openflexo
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

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.AbstractCreateResource;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext.ReturnException;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXModelSlot;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.IdentifierManagementStrategy;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * {@link EditionAction} used to create an empty Excel resource
 * 
 * @author sylvain,xtof
 *
 */
@ModelEntity
@ImplementationClass(CreateEmptyDocXResource.CreateDocXResourceImpl.class)
@XMLElement
@FML("CreateEmptyDocXResource")
public interface CreateEmptyDocXResource extends AbstractCreateResource<DocXModelSlot, DocXDocument, DocXTechnologyAdapter> {

	public static abstract class CreateDocXResourceImpl
			extends AbstractCreateResourceImpl<DocXModelSlot, DocXDocument, DocXTechnologyAdapter> implements CreateEmptyDocXResource {

		private static final Logger logger = Logger.getLogger(CreateDocXResourceImpl.class.getPackage().getName());

		@Override
		public Type getAssignableType() {
			return DocXDocument.class;
		}

		@Override
		public DocXDocument execute(RunTimeEvaluationContext evaluationContext) throws ReturnException, FlexoException {

			System.out.println("OK, on cree un fichier DocX. ");

			String resourceName = getResourceName(evaluationContext);
			String resourceURI = getResourceURI(evaluationContext);
			FlexoResourceCenter<?> rc = getResourceCenter(evaluationContext);

			System.out.println("name=" + resourceName);
			System.out.println("uri=" + resourceURI);
			System.out.println("relative path=" + getRelativePath());
			System.out.println("rc=" + rc);

			DocXTechnologyAdapter docxTA = getServiceManager().getTechnologyAdapterService()
					.getTechnologyAdapter(DocXTechnologyAdapter.class);

			DocXDocumentResource newResource = docxTA.createNewDocXDocumentResource((FileSystemBasedResourceCenter) rc, getRelativePath(),
					resourceName, true, /*getInferedModelSlot().getIdStrategy()*/IdentifierManagementStrategy.Bookmark);

			System.out.println("New resource: " + newResource);

			if (newResource != null) {
				DocXDocument returned;
				try {
					returned = newResource.getResourceData();
					System.out.println("Return " + returned);
					return returned;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					e.printStackTrace();
				}
			}

			logger.warning("Cannot create DocXDocumentResource !");

			return null;

		}
	}
}
