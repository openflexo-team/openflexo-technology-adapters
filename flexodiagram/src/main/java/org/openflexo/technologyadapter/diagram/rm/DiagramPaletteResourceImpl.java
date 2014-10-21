package org.openflexo.technologyadapter.diagram.rm;

import java.io.File;
import java.io.FileNotFoundException;

import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.InconsistentDataException;
import org.openflexo.foundation.InvalidModelDefinitionException;
import org.openflexo.foundation.InvalidXMLException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.openflexo.foundation.resource.FlexoFileNotFoundException;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.AccessibleProxyObject;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;
import org.openflexo.toolbox.IProgress;

public abstract class DiagramPaletteResourceImpl extends PamelaResourceImpl<DiagramPalette, DiagramPaletteFactory> implements
		DiagramPaletteResource, AccessibleProxyObject {

	public static DiagramPaletteResource makeDiagramPaletteResource(DiagramSpecificationResource dsResource, String diagramPaletteName,
			FlexoServiceManager serviceManager) {
		try {
			File diagramPaletteFile = new File(ResourceLocator.retrieveResourceAsFile(dsResource.getDirectory()), diagramPaletteName + ".palette");
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext( 
					FileFlexoIODelegate.class,DiagramPaletteResource.class));
			DiagramPaletteResourceImpl returned = (DiagramPaletteResourceImpl) factory.newInstance(DiagramPaletteResource.class);
			returned.setName(diagramPaletteFile.getName());
			//returned.setFile(diagramPaletteFile);
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(diagramPaletteFile, factory));
			
			returned.setURI(dsResource.getURI() + "/" + diagramPaletteFile.getName());
			returned.setServiceManager(serviceManager);
			returned.setFactory(new DiagramPaletteFactory(serviceManager.getEditingContext(), returned));
			dsResource.addToContents(returned);
			DiagramPalette newPalette = returned.getFactory().makeNewDiagramPalette();
			newPalette.setResource(returned);
			returned.setResourceData(newPalette);
			dsResource.getDiagramSpecification().addToPalettes(newPalette);
			dsResource.getDiagramPaletteResources().add(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DiagramPaletteResource retrieveDiagramPaletteResource(DiagramSpecificationResource dsResource, File diagramPaletteFile,
			FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext( 
					FileFlexoIODelegate.class,DiagramPaletteResource.class));
			DiagramPaletteResourceImpl returned = (DiagramPaletteResourceImpl) factory.newInstance(DiagramPaletteResource.class);
			returned.setName(diagramPaletteFile.getName());
			//returned.setFile(diagramPaletteFile);
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(diagramPaletteFile, factory));
			
			returned.setURI(dsResource.getURI() + "/" + diagramPaletteFile.getName());
			returned.setServiceManager(serviceManager);
			returned.setFactory(new DiagramPaletteFactory(serviceManager.getEditingContext(), returned));
			dsResource.addToContents(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<DiagramPalette> getResourceDataClass() {
		return DiagramPalette.class;
	}

	/**
	 * Return diagram palette stored by this resource<br>
	 * Load the resource data when unloaded
	 */
	@Override
	public DiagramPalette getDiagramPalette() {
		try {
			return getResourceData(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return diagram palette stored by this resource<br>
	 * Do not force load the resource data
	 * 
	 * @return
	 */
	@Override
	public DiagramPalette getLoadedDiagramPalette() {
		if (isLoaded()) {
			return getDiagramPalette();
		}
		return null;
	}

	/**
	 * Load the &quot;real&quot; load resource data of this resource.
	 * 
	 * @param progress
	 *            a progress monitor in case the resource data is not immediately available.
	 * @return the resource data.
	 * @throws ResourceLoadingCancelledException
	 * @throws ResourceDependencyLoopException
	 * @throws FileNotFoundException
	 */
	@Override
	public DiagramPalette loadResourceData(IProgress progress) throws FlexoFileNotFoundException, IOFlexoException, InvalidXMLException,
			InconsistentDataException, InvalidModelDefinitionException {

		DiagramPalette returned = super.loadResourceData(progress);
		returned.setName(getFile().getName().substring(0, getFile().getName().length() - 8));
		// returned.init(getContainer().getDiagramSpecification(), getFile().getName().substring(0, getFile().getName().length() - 8));
		if (!getContainer().getDiagramSpecification().getPalettes().contains(returned)) {
			getContainer().getDiagramSpecification().addToPalettes(returned);
			setChanged();
			notifyObservers(new DataModification("diagramPalette", null, returned));
		}
		setChanged();
		notifyObservers(new DataModification("loadedDiagramPalette", null, returned));
		returned.clearIsModified();
		return returned;
	}

	private File getFile(){
		return getFileFlexoIODelegate().getFile();
	}
	
	public FileFlexoIODelegate getFileFlexoIODelegate() {
		return (FileFlexoIODelegate)getFlexoIODelegate();
	}
	
	@Override
	public DiagramSpecificationResource getContainer() {
		return (DiagramSpecificationResource) performSuperGetter(CONTAINER);
	}

	@Override
	public DiagramTechnologyAdapter getTechnologyAdapter() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}
		return null;
	}
}
