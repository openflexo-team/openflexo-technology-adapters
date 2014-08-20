/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2013 Openflexo
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
package org.openflexo.technologyadapter.xml;

import static org.openflexo.foundation.technologyadapter.TechnologyAdapter.logger;

import java.io.File;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoFileResource;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.ResourceRepository;
import org.openflexo.foundation.technologyadapter.DeclareModelSlot;
import org.openflexo.foundation.technologyadapter.DeclareModelSlots;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.technologyadapter.xml.binding.XSDBindingFactory;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResource;

/**
 * This class defines and implements the XSD/XML technology adapter
 * 
 * @author sylvain, luka, Christophe
 * 
 */

@DeclareModelSlots({ // ModelSlot(s) declaration
@DeclareModelSlot(FML = "XSDModelSlot", modelSlotClass = XSDModelSlot.class) // Classical
// type-safe
// interpretation
})
public class XSDTechnologyAdapter extends TechnologyAdapter {



   
    /**
     * Instantiate new model resource stored in supplied model file
     * 
     * @param aMetaModelFile
     * @return
     */
    public FlexoResource<XMLXSDModel> retrieveModelResource(File aModelFile, FlexoResource<XMLMetaModel> metaModelResource) {

        XSDTechnologyContextManager xsdContextManager = getTechnologyContextManager();

        String uri = retrieveMetaModelURI(aModelFile);

        XMLXSDFileResource XMLXSDFileResource = (XMLXSDFileResource) xsdContextManager.getResourceWithURI(uri);

        // If there is no metaModel Resource, we can not parse the file
        if (XMLXSDFileResource == null && metaModelResource != null) {

            XMLXSDFileResource = XMLXSDFileResourceImpl.makeXMLXSDFileResource(uri, aModelFile, (XSDMetaModelResource) metaModelResource,
                    xsdContextManager);

            XMLXSDFileResource.setName(aModelFile.getName());

            XMLXSDFileResource.setServiceManager(getTechnologyAdapterService().getServiceManager());
            xsdContextManager.registerResource(XMLXSDFileResource);
        }

        return XMLXSDFileResource;
    }

    public XMLXSDFileResource retrieveModelResource(File aModelFile) {

        XSDTechnologyContextManager xsdContextManager = getTechnologyContextManager();

        for (FlexoResourceCenter rc : xsdContextManager.getResourceCenterService().getResourceCenters()) {

            ResourceRepository<FlexoFileResource<?>> mmRepository = rc.getRepository(XSDMetaModelRepository.class,
                    xsdContextManager.getTechnologyAdapter());

            if (rc != null) {
                for (FlexoFileResource<?> mmRes : mmRepository.getAllResources()) {

                    // If there is no metaModel Resource, we can not parse the
                    // file
                    if (isValidModelFile(aModelFile, (FlexoResource<XMLMetaModel>) mmRes, xsdContextManager) && mmRes != null) {
                        XMLXSDFileResource xmlXSDFileResource = (XMLXSDFileResource) retrieveModelResource(aModelFile,
                                (FlexoResource<XMLMetaModel>) mmRes);
                        xmlXSDFileResource.setMetaModelResource((XSDMetaModelResource) mmRes);
                        xsdContextManager.registerResource(xmlXSDFileResource);

                        return xmlXSDFileResource;
                    }
                }
            }
        }
        return null;

    }

    /**
     * Create empty model.
     * 
     * @param modelFile
     * @param modelUri
     * @param metaModelResource
     * @param technologyContextManager
     * @return
     */
    public XMLXSDFileResource createNewXMLFile(File modelFile, String modelUri, FlexoResource<XMLMetaModel> metaModelResource) {

        modelUri = modelFile.toURI().toString();

        XMLXSDFileResource modelResource = XMLXSDFileResourceImpl.makeXMLXSDFileResource(modelUri, modelFile,
                (XSDMetaModelResource) metaModelResource, getTechnologyContextManager());

        getTechnologyContextManager().registerResource(modelResource);

        return modelResource;

    }

    /**
     * Creates new model conform to the supplied meta model
     * 
     * @param project
     * @param metaModel
     * @return
     */
    public XMLXSDFileResource createNewXMLFile(FlexoProject project, String filename, String modelUri, FlexoResource<XMLMetaModel> metaModel) {

        File modelFile = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);

        // TODO: modelURI is not used here!!!! => check the API, as it is
        // processed by TA
        logger.warning("modelURI are not useful in this context");

        return createNewXMLFile(modelFile, modelUri, metaModel);

    }

    public FlexoResource<XMLXSDModel> createNewXMLFile(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
            FlexoResource<XMLMetaModel> metaModelResource) {

        File modelDirectory = new File(resourceCenter.getRootDirectory(), relativePath);
        File modelFile = new File(modelDirectory, filename);

        String modelUri = modelFile.toURI().toString();

        return createNewXMLFile(modelFile, modelUri, metaModelResource);
    }

    public FlexoResource<XMLXSDModel> createNewXMLFile(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
            String modelUri, FlexoResource<XMLMetaModel> metaModelResource) {

        File modelDirectory = new File(resourceCenter.getRootDirectory(), relativePath);
        File modelFile = new File(modelDirectory, filename);

        return createNewXMLFile(modelFile, modelUri, metaModelResource);
    }

    @Override
    public XSDTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
        return new XSDTechnologyContextManager(this, service);
    }

    @Override
    public XSDBindingFactory getTechnologyAdapterBindingFactory() {
        return BINDING_FACTORY;
    }



}
