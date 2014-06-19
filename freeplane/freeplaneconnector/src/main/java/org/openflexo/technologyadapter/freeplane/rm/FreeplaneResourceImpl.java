package org.openflexo.technologyadapter.freeplane.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.mindmapmode.MModeController;
import org.freeplane.main.application.BasicFreeplaneAdapter;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoFileResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
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

    private static final Logger logger = Logger.getLogger(FreeplaneResourceImpl.class.getPackage().getName());

    public static IFreeplaneResource makeFreeplaneResource(final String modelURI, final File modelFile, final FreeplaneTechnologyContextManager technologyContextManager) {
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
            logger.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    public static IFreeplaneResource makeFreeplaneResource(final File modelFile, final FreeplaneTechnologyContextManager technologyContextManager) {
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
            logger.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    @Override
    public FreeplaneTechnologyAdapter getTechnologyAdapter() {
        if (this.getServiceManager() != null) {
            return this.getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(FreeplaneTechnologyAdapter.class);
        }
        return null;
    }

    @Override
    public IFreeplaneMap loadResourceData(final IProgress progress) throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
        // TODO: IMPLEMENT THIS, and don't call getResourceData : infinite loop
        final BasicFreeplaneAdapter freeplaneAdapter = BasicFreeplaneAdapter.getInstance();
        try {
            final ModelFactory factory = new ModelFactory(IFreeplaneMap.class);
            final FreeplaneMapImpl map = (FreeplaneMapImpl) factory.newInstance(IFreeplaneMap.class);
            map.setTechnologyAdapter(this.getTechnologyAdapter());
            map.setMapModel(freeplaneAdapter.loadMapFromFile(this.getFile()));
            return map;
        } catch (final Exception e) {
            final String msg = "Error while loading loading freeplane map";
            logger.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    @Override
    public Class<IFreeplaneMap> getResourceDataClass() {
        return IFreeplaneMap.class;
    }

    @Override
    public void save(final IProgress progress) {
        ((MModeController) Controller.getCurrentModeController()).save();
    }
}
