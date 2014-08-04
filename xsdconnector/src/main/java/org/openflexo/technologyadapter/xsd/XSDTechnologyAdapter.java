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
package org.openflexo.technologyadapter.xsd;

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
import org.openflexo.foundation.technologyadapter.DeclareRepositoryType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.technologyadapter.xml.XSDModelSlot;
import org.openflexo.technologyadapter.xml.binding.XSDBindingFactory;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLXSDModel;
import org.openflexo.technologyadapter.xml.model.XMLXSDModelFactory;
import org.openflexo.technologyadapter.xml.rm.XMLXSDFileResource;
import org.openflexo.technologyadapter.xml.rm.XMLXSDFileResourceImpl;
import org.openflexo.technologyadapter.xml.rm.XMLXSDModelRepository;
import org.openflexo.technologyadapter.xml.rm.XMLXSDNameSpaceFinder;
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
@DeclareRepositoryType({ XSDMetaModelRepository.class, XMLXSDModelRepository.class })
public class XSDTechnologyAdapter extends TechnologyAdapter {



    @Override
    public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
        return false;
    }

    @Override
    public <I> void contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
        if (contents instanceof File) {
            File candidateFile = (File) contents;
            if (tryToLookupMetaModel(resourceCenter, candidateFile) != null) {
                // This is a meta-model, this one has just been registered
            }
            else {
                tryToLookupModel(resourceCenter, candidateFile);
            }
        }
    }

    @Override
    public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
        if (contents instanceof File) {
            System.out
                    .println("File DELETED " + ((File) contents).getName() + " in " + ((File) contents).getParentFile().getAbsolutePath());
        }
    }



    /**
     * Retrieve and return URI for supplied meta model file, if supplied file
     * represents a valid XSD meta model
     * 
     * @param aMetaModelFile
     * @return
     */
    public String retrieveMetaModelURI(File aMetaModelFile) {

        String s = XMLMetaModel.findNamespaceURI(aMetaModelFile);

        return s;
    }

    /**
     * Follow the link.
     * 
     * @see org.openflexo.foundation.technologyadapter.TechnologyAdapter#retrieveModelURI(java.io.File,
     *      org.openflexo.foundation.resource.FlexoResource,
     *      org.openflexo.foundation.technologyadapter.TechnologyContextManager)
     */
    public String retrieveModelURI(File aModelFile, FlexoResource<XMLMetaModel> metaModelResource,
            TechnologyContextManager technologyContextManager) {

        return aModelFile.toURI().toString();
    }

    /**
     * Return flag indicating if supplied file represents a valid XML model
     * conform to supplied meta-model
     * 
     * @param aModelFile
     * @param metaModel
     * @return
     */
    public boolean isValidModelFile(File aModelFile, FlexoResource<XMLMetaModel> metaModelResource,
            TechnologyContextManager technologyContextManager) {

        if (aModelFile.getName().endsWith(".xml")) {
            String schemaURI = XMLXSDNameSpaceFinder.findNameSpace(aModelFile, false);

            String mmURI = metaModelResource.getURI();
            if (schemaURI != null && mmURI != null) {
                if (schemaURI.equals(mmURI)) {
                    logger.info("Found a conformant XML Model File [" + schemaURI + "]" + aModelFile.getAbsolutePath());
                    return !schemaURI.isEmpty();
                }
            }
        }

        return false;
    }

    public boolean isValidModelFile(File aModelFile, TechnologyContextManager technologyContextManager) {
        if (aModelFile.getName().endsWith(".xml")) {

            String schemaURI = XMLXSDNameSpaceFinder.findNameSpace(aModelFile, false);

            // FIXME: Check if this is ok!
            XSDMetaModelResource mm = (XSDMetaModelResource) technologyContextManager.getResourceWithURI(schemaURI);

            if (mm != null) {
                return true;
            }
        }
        return false;
    }

   
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

    public String getExpectedMetaModelExtension() {
        return ".xsd";
    }

    public String getExpectedModelExtension(FlexoResource<XMLMetaModel> metaModel) {
        return ".xml";
    }

    /**
     * 
     * Create a XMLModel repository for current {@link TechnologyAdapter} and
     * supplied {@link FlexoResourceCenter}
     * 
     */
    public XMLXSDModelRepository createXMLXSDModelRepository(FlexoResourceCenter<?> resourceCenter) {
        XMLXSDModelRepository returned = new XMLXSDModelRepository(this, resourceCenter);
        resourceCenter.registerRepository(returned, XMLXSDModelRepository.class, this);
        return returned;
    }

    public XMLXSDModelFactory getXMLXSDModelFactory() {
        return this.xmlModelFactory;
    }

}
