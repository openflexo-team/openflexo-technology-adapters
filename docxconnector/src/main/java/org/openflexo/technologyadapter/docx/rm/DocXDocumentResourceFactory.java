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

/**
 * Implementation of PamelaResourceFactory for {@link DocXDocumentResource}
 * 
 * @author sylvain
 *
 */
public class DocXDocumentResourceFactory
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
				technologyContextManager.getTechnologyAdapter().getDefaultIDStrategy());
	}

	@Override
	public DocXDocument makeEmptyResourceData(DocXDocumentResource resource) {
		return resource.getFactory().makeNewDocXDocument();
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(DOCX_FILE_EXTENSION);
	}

	@Override
	protected <I> DocXDocumentResource registerResource(DocXDocumentResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<DocXTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the DocXDocumentRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getDocXDocumentRepository(resourceCenter));

		return resource;
	}

	/*public static DocXDocumentResource makeDocXDocumentResource(File modelFile, DocXTechnologyContextManager technologyContextManager,
	FlexoResourceCenter<?> resourceCenter, IdentifierManagementStrategy idStrategy) {
	try {
	ModelFactory factory = new ModelFactory(
			ModelContextLibrary.getCompoundModelContext(DocXDocumentResource.class, FileFlexoIODelegate.class));
	DocXDocumentResourceImpl returned = (DocXDocumentResourceImpl) factory.newInstance(DocXDocumentResource.class);
	returned.initName(modelFile.getName());
	returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
	DocXFactory docXFactory = new DocXFactory(returned, technologyContextManager.getServiceManager().getEditingContext(),
			idStrategy);
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
	
	public static DocXDocumentResource retrieveDocXDocumentResource(File modelFile, DocXTechnologyContextManager technologyContextManager,
	FlexoResourceCenter<?> resourceCenter, IdentifierManagementStrategy idStrategy) {
	try {
	ModelFactory factory = new ModelFactory(
			ModelContextLibrary.getCompoundModelContext(DocXDocumentResource.class, FileFlexoIODelegate.class));
	DocXDocumentResourceImpl returned = (DocXDocumentResourceImpl) factory.newInstance(DocXDocumentResource.class);
	returned.initName(modelFile.getName());
	returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
	DocXFactory docXFactory = new DocXFactory(returned, technologyContextManager.getServiceManager().getEditingContext(),
			idStrategy);
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
	
	// TODO : have more optimized code between methods referring to different kind of resources
	public static DocXDocumentResource retrieveDocXDocumentResource(InJarResourceImpl jarResource,
	DocXTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter,
	IdentifierManagementStrategy idStrategy) {	try {
		ModelFactory factory = new ModelFactory(
				ModelContextLibrary.getCompoundModelContext(DocXDocumentResource.class, InJarFlexoIODelegate.class));
		DocXDocumentResourceImpl returned = (DocXDocumentResourceImpl) factory.newInstance(DocXDocumentResource.class);
		returned.initName(jarResource.getURL().getFile());
		returned.setFlexoIODelegate(InJarFlexoIODelegateImpl.makeInJarFlexoIODelegate(jarResource, factory));
		DocXFactory docXFactory = new DocXFactory(returned, technologyContextManager.getServiceManager().getEditingContext(),
				idStrategy);
		returned.setFactory(docXFactory);
	
		System.out.println("J'initialise le RC!!");
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
	}*/

}
