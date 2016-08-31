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

package org.openflexo.technologyadapter.docx.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.PamelaResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFactory;
import org.openflexo.technologyadapter.docx.model.IdentifierManagementStrategy;

/**
 * Implementation of PamelaResourceFactory for {@link DocXDocumentResource}
 * 
 * @author sylvain
 *
 */
public abstract class DocXDocumentResourceFactory
		extends PamelaResourceFactory<DocXDocumentResource, DocXDocument, DocXTechnologyAdapter, DocXFactory> {

	private static final Logger logger = Logger.getLogger(DocXDocumentResourceFactory.class.getPackage().getName());

	public static String DOCX_FILE_EXTENSION = ".docx";

	public DocXDocumentResourceFactory() throws ModelDefinitionException {
		super(DocXDocumentResource.class);
	}

	@Override
	public DocXFactory makeResourceDataFactory(DocXDocumentResource resource,
			TechnologyContextManager<DocXTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		return new DocXFactory(resource, technologyContextManager.getServiceManager().getEditingContext(),
				IdentifierManagementStrategy.Bookmark);
	}

	@Override
	public DocXDocument makeEmptyResourceData(DocXDocumentResource resource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(DOCX_FILE_EXTENSION);
	}

}
