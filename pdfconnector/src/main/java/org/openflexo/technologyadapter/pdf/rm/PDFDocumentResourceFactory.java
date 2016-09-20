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

package org.openflexo.technologyadapter.pdf.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.PamelaResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.pdf.PDFTechnologyAdapter;
import org.openflexo.technologyadapter.pdf.model.PDFDocument;
import org.openflexo.technologyadapter.pdf.model.PDFFactory;

/**
 * Implementation of PamelaResourceFactory for {@link DocXDocumentResource}
 * 
 * @author sylvain
 *
 */
public class PDFDocumentResourceFactory extends PamelaResourceFactory<PDFDocumentResource, PDFDocument, PDFTechnologyAdapter, PDFFactory> {

	private static final Logger logger = Logger.getLogger(PDFDocumentResourceFactory.class.getPackage().getName());

	public static String PDF_FILE_EXTENSION = ".pdf";

	public PDFDocumentResourceFactory() throws ModelDefinitionException {
		super(PDFDocumentResource.class);
	}

	@Override
	public PDFFactory makeResourceDataFactory(PDFDocumentResource resource,
			TechnologyContextManager<PDFTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		return new PDFFactory(resource, technologyContextManager.getServiceManager().getEditingContext());
	}

	@Override
	public PDFDocument makeEmptyResourceData(PDFDocumentResource resource) {
		return resource.getFactory().makeNewPDFDocument();
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		System.out.println("Pour " + serializationArtefact);
		return resourceCenter.retrieveName(serializationArtefact).endsWith(PDF_FILE_EXTENSION);
	}

	@Override
	protected <I> PDFDocumentResource registerResource(PDFDocumentResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<PDFTechnologyAdapter> technologyContextManager) {

		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the PDFDocumentRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getPDFDocumentRepository(resourceCenter));

		return resource;
	}

	/*
	 public static PDFDocumentResource makePDFDocumentResource(File modelFile, PDFTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(PDFDocumentResource.class, FileFlexoIODelegate.class));
			PDFDocumentResourceImpl returned = (PDFDocumentResourceImpl) factory.newInstance(PDFDocumentResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			PDFFactory docXFactory = new PDFFactory(returned, technologyContextManager.getServiceManager().getEditingContext());
			returned.setFactory(docXFactory);
	
			// returned.setURI(modelURI);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
	
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PDFDocumentResource retrievePDFDocumentResource(File modelFile, PDFTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(PDFDocumentResource.class, FileFlexoIODelegate.class));
			PDFDocumentResourceImpl returned = (PDFDocumentResourceImpl) factory.newInstance(PDFDocumentResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			PDFFactory docXFactory = new PDFFactory(returned, technologyContextManager.getServiceManager().getEditingContext());
			returned.setFactory(docXFactory);
	
			// returned.setURI(modelFile.toURI().toString());
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	*/
}
