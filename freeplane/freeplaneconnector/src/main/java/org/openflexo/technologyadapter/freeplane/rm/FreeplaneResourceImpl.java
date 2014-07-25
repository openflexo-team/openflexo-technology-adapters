package org.openflexo.technologyadapter.freeplane.rm;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.mindmapmode.MModeController;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.resource.FlexoFileResourceImpl;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyContextManager;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneMapImpl;
import org.openflexo.toolbox.IProgress;

/**
 * Freeplane resource management
 * 
 * @author eloubout
 * 
 */
public abstract class FreeplaneResourceImpl extends FlexoFileResourceImpl<IFreeplaneMap> implements IFreeplaneResource {

    private static final Logger LOGGER = Logger.getLogger(FreeplaneResourceImpl.class.getPackage().getName());
    private static ModelFactory MODEL_FACTORY;

    static {
        try {
            MODEL_FACTORY = new ModelFactory(IFreeplaneMap.class);
        } catch (final ModelDefinitionException e) {
            final String msg = "Error while initializing Freeplane model resource";
            LOGGER.log(Level.SEVERE, msg, e);
        }
    }

    public static IFreeplaneResource makeFreeplaneResource(final String modelURI, final File modelFile,
            final FreeplaneTechnologyContextManager technologyContextManager) {
        try {
            final ModelFactory factory = new ModelFactory(IFreeplaneResource.class);
            final FreeplaneResourceImpl returned = (FreeplaneResourceImpl) factory.newInstance(IFreeplaneResource.class);
            returned.setName(modelFile.getName());
            returned.setFile(modelFile);
            returned.setURI(modelURI);
            returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
            returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
            returned.setTechnologyContextManager(technologyContextManager);
            technologyContextManager.registerResource(returned);
            return returned;

        } catch (final ModelDefinitionException e) {
            final String msg = "Error while initializing Freeplane model resource";
            LOGGER.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    public static IFreeplaneResource makeFreeplaneResource(final File modelFile,
            final FreeplaneTechnologyContextManager technologyContextManager) {
        try {
            final ModelFactory factory = new ModelFactory(IFreeplaneResource.class);
            final FreeplaneResourceImpl returned = (FreeplaneResourceImpl) factory.newInstance(IFreeplaneResource.class);
            returned.setName(modelFile.getName());
            returned.setFile(modelFile);
            returned.setURI(modelFile.toURI().toString());
            returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
            returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
            returned.setTechnologyContextManager(technologyContextManager);
            technologyContextManager.registerResource(returned);
            return returned;
        } catch (final ModelDefinitionException e) {
            final String msg = "Error while initializing Freeplane model resource";
            LOGGER.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    @Override
    public FreeplaneTechnologyAdapter getTechnologyAdapter() {
        if (getServiceManager() != null) {
            return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(FreeplaneTechnologyAdapter.class);
        }
        return null;
    }

    @Override
    public IFreeplaneMap loadResourceData(final IProgress progress) {
        final FreeplaneMapImpl map = (FreeplaneMapImpl) MODEL_FACTORY.newInstance(IFreeplaneMap.class);
        map.setTechnologyAdapter(getTechnologyAdapter());
        map.setMapModel(FreeplaneBasicAdapter.getInstance().loadMapFromFile(getFile()));
		map.setResource(this);

        return map;
    }

    @Override
    public Class<IFreeplaneMap> getResourceDataClass() {
        return IFreeplaneMap.class;
    }

    /**
     * Save modification. A unique call to Freeplane save is done. A boolean
     * could be return, but we stay correct to TA API.<br>
     * Freeplane save only on last opened map it seems. TODO investigate
     */
    @Override
    public void save(final IProgress progress) {
        if (progress != null) {
            ((MModeController) Controller.getCurrentModeController()).save();
            this.resourceData.clearIsModified(false);
            notifyResourceSaved();
        }
    }
}
