/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Openflexo-technologyadapter-archetype-simplemodel, a component of the software infrastructure 
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


package org.openflexo.technologyadapter.csv;

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
import org.openflexo.technologyadapter.csv.rm.CSVResource;
import org.openflexo.technologyadapter.csv.rm.CSVResourceImpl;
import org.openflexo.technologyadapter.csv.CSVModelSlot;
import org.openflexo.technologyadapter.csv.rm.CSVResourceRepository;


/**
 * This class defines and implements the CSV technology adapter
 * 
 * @author Jean Le Paon
 * 
 */

@DeclareModelSlots({CSVModelSlot.class})
@DeclareRepositoryType({CSVResourceRepository.class })
public class CSVTechnologyAdapter extends TechnologyAdapter {

    private static String CSV_FILE_EXTENSION = ".csv";

    private static final Logger LOGGER = Logger.getLogger(CSVTechnologyAdapter.class.getPackage().getName());

    public CSVTechnologyAdapter() throws TechnologyAdapterInitializationException {
    }

    @Override
    public String getName() {
        return new String("CSV Technology Adapter");
    }

    @Override
    public CSVTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
        return new CSVTechnologyContextManager(this, service);
    }

    @Override
    public CSVTechnologyContextManager getTechnologyContextManager() {
        // TODO Auto-generated method stub
        return (CSVTechnologyContextManager) super.getTechnologyContextManager();
    }

    @Override
    public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
        // TODO Auto-generated method stub
        CSVResourceRepository currentRepository = resourceCenter.getRepository(CSVResourceRepository.class, this);
        if (currentRepository == null) {
            currentRepository = this.createNewCSVRepository(resourceCenter);
        }

        for (final I item : resourceCenter) {
            if (item instanceof File) {
                this.initializeCSVFile(resourceCenter, (File) item);
            }
        }

    }

    /**
     * Register file if it is a CSV file, and
     * reference resource to <code>this</code>
     * 
     * @param resourceCenter
     * @param candidateFile
     */
    private <I> void initializeCSVFile(final FlexoResourceCenter<I> resourceCenter, final File candidateFile) {
        if (!this.isValidCSVFile(candidateFile)) {
            return;
        }
        final CSVResourceImpl csvResourceFile = (CSVResourceImpl) CSVResourceImpl.retrieveCSVResource(
                candidateFile, this.getTechnologyContextManager());
        final CSVResourceRepository resourceRepository = resourceCenter.getRepository(CSVResourceRepository.class, this);
        if (csvResourceFile != null) {
            try {
                final RepositoryFolder<CSVResource> folder = resourceRepository.getRepositoryFolder(candidateFile, true);
                resourceRepository.registerResource(csvResourceFile, folder);
                this.referenceResource(csvResourceFile, resourceCenter);
            } catch (final IOException e) {
                final String msg = "Error during getting CSV resource folder";
                LOGGER.log(Level.SEVERE, msg, e);
            }
        }

    } 
    
    /**
     * 
     * @param candidateFile
     * @return true if extension of file match
     *         <code>CSV_FILE_EXTENSION</code>
     */
    public boolean isValidCSVFile(final File candidateFile) {
        return candidateFile.getName().endsWith(CSV_FILE_EXTENSION);
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
            this.initializeCSVFile(resourceCenter, (File) contents);
        }
    }

    @Override
    public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
        // TODO Auto-generated method stub
    }
    

    public CSVResource createNewCSVModel(FlexoProject project, String filename, String modelUri) {
        // TODO Auto-generated method stub
        final File file = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);
        final CSVResourceImpl csvResourceFile = (CSVResourceImpl) CSVResourceImpl.makeCSVResource(modelUri, file, this.getTechnologyContextManager());
        this.getTechnologyContextManager().registerResource(csvResourceFile);
        return csvResourceFile;
    }

     /**
     * Create a new CSVResourceRepository and register it in the given
     * resource center.
     * 
     * @param resourceCenter
     * @return the repository
     */
    private CSVResourceRepository createNewCSVRepository(final FlexoResourceCenter<?> resourceCenter) {
        final CSVResourceRepository repo = new CSVResourceRepository(this, resourceCenter);
        resourceCenter.registerRepository(repo, CSVResourceRepository.class, this);
        return repo;
    }

    
}
