#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * (c) Copyright 2013- Openflexo
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

package ${package};

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import ${package}.rm.${technologyPrefix}Resource;
import ${package}.rm.${technologyPrefix}ResourceImpl;
import ${package}.${technologyPrefix}ModelSlot;
import ${package}.rm.${technologyPrefix}ResourceRepository;


/**
 * This class defines and implements the ${technologyPrefix} technology adapter
 * 
 * @author ${author}
 * 
 */

@DeclareModelSlots({${technologyPrefix}ModelSlot.class})
@DeclareRepositoryType({${technologyPrefix}ResourceRepository.class })
public class ${technologyPrefix}TechnologyAdapter extends TechnologyAdapter {

    private static String ${technologyPrefix}_FILE_EXTENSION = ".${technologyExtension}";

    private static final Logger LOGGER = Logger.getLogger(${technologyPrefix}TechnologyAdapter.class.getPackage().getName());

    public ${technologyPrefix}TechnologyAdapter() throws TechnologyAdapterInitializationException {
    }

    @Override
    public String getName() {
        return new String("${technologyPrefix} Technology Adapter");
    }

    @Override
    public ${technologyPrefix}TechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
        return new ${technologyPrefix}TechnologyContextManager(this, service);
    }

    @Override
    public ${technologyPrefix}TechnologyContextManager getTechnologyContextManager() {
        // TODO Auto-generated method stub
        return (${technologyPrefix}TechnologyContextManager) super.getTechnologyContextManager();
    }

    @Override
    public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
        // TODO Auto-generated method stub
        ${technologyPrefix}ResourceRepository currentRepository = resourceCenter.getRepository(${technologyPrefix}ResourceRepository.class, this);
        if (currentRepository == null) {
            currentRepository = this.createNew${technologyPrefix}Repository(resourceCenter);
        }

        for (final I item : resourceCenter) {
            if (item instanceof File) {
                this.initialize${technologyPrefix}File(resourceCenter, (File) item);
            }
        }

    }

    /**
     * Register file if it is a ${technologyPrefix} file, and
     * reference resource to <code>this</code>
     * 
     * @param resourceCenter
     * @param candidateFile
     */
    private <I> void initialize${technologyPrefix}File(final FlexoResourceCenter<I> resourceCenter, final File candidateFile) {
        if (!this.isValid${technologyPrefix}File(candidateFile)) {
            return;
        }
        final ${technologyPrefix}ResourceImpl ${rootArtifactId}ResourceFile = (${technologyPrefix}ResourceImpl) ${technologyPrefix}ResourceImpl.retrieve${technologyPrefix}Resource(
                candidateFile, this.getTechnologyContextManager());
        final ${technologyPrefix}ResourceRepository resourceRepository = resourceCenter.getRepository(${technologyPrefix}ResourceRepository.class, this);
        if (${rootArtifactId}ResourceFile != null) {
            try {
                final RepositoryFolder<${technologyPrefix}Resource> folder = resourceRepository.getRepositoryFolder(candidateFile, true);
                resourceRepository.registerResource(${rootArtifactId}ResourceFile, folder);
                this.referenceResource(${rootArtifactId}ResourceFile, resourceCenter);
            } catch (final IOException e) {
                final String msg = "Error during getting ${technologyPrefix} resource folder";
                LOGGER.log(Level.SEVERE, msg, e);
            }
        }

    } 
    
    /**
     * 
     * @param candidateFile
     * @return true if extension of file match
     *         <code>${technologyPrefix}_FILE_EXTENSION</code>
     */
    public boolean isValid${technologyPrefix}File(final File candidateFile) {
        return candidateFile.getName().endsWith(${technologyPrefix}_FILE_EXTENSION);
    }


    @Override
    public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <I> void contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
        // TODO Auto-generated method stub
        if (contents instanceof File) {
            this.initialize${technologyPrefix}File(resourceCenter, (File) contents);
        }
    }

    @Override
    public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
        // TODO Auto-generated method stub
    }
    

    public ${technologyPrefix}Resource createNew${technologyPrefix}Model(FlexoProject project, String filename, String modelUri) {
        // TODO Auto-generated method stub
        final File file = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);
        final ${technologyPrefix}ResourceImpl ${rootArtifactId}ResourceFile = (${technologyPrefix}ResourceImpl) ${technologyPrefix}ResourceImpl.make${technologyPrefix}Resource(modelUri, file, this.getTechnologyContextManager());
        this.getTechnologyContextManager().registerResource(${rootArtifactId}ResourceFile);
        return ${rootArtifactId}ResourceFile;
    }

     /**
     * Create a new ${technologyPrefix}ResourceRepository and register it in the given
     * resource center.
     * 
     * @param resourceCenter
     * @return the repository
     */
    private ${technologyPrefix}ResourceRepository createNew${technologyPrefix}Repository(final FlexoResourceCenter<?> resourceCenter) {
        final ${technologyPrefix}ResourceRepository repo = new ${technologyPrefix}ResourceRepository(this, resourceCenter);
        resourceCenter.registerRepository(repo, ${technologyPrefix}ResourceRepository.class, this);
        return repo;
    }

    
}
