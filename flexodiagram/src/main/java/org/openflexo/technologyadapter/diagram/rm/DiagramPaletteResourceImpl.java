/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.rm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Logger;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
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
import org.openflexo.foundation.resource.InJarFlexoIODelegate;
import org.openflexo.foundation.resource.InJarFlexoIODelegate.InJarFlexoIODelegateImpl;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.AccessibleProxyObject;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.rm.InJarResourceImpl;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;
import org.openflexo.toolbox.IProgress;

public abstract class DiagramPaletteResourceImpl extends PamelaResourceImpl<DiagramPalette, DiagramPaletteFactory> implements
		DiagramPaletteResource, AccessibleProxyObject {

	static final Logger logger = Logger.getLogger(DiagramPaletteResourceImpl.class.getPackage().getName());

	public static DiagramPaletteResource makeDiagramPaletteResource(DiagramSpecificationResource dsResource, String diagramPaletteName,
			FlexoServiceManager serviceManager) {
		try {
			File diagramPaletteFile = new File(ResourceLocator.retrieveResourceAsFile(dsResource.getDirectory()), diagramPaletteName
					+ ".palette");
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,
					DiagramPaletteResource.class));
			DiagramPaletteResourceImpl returned = (DiagramPaletteResourceImpl) factory.newInstance(DiagramPaletteResource.class);
			returned.initName(diagramPaletteFile.getName());
			// returned.setFile(diagramPaletteFile);
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
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,
					DiagramPaletteResource.class));
			DiagramPaletteResourceImpl returned = (DiagramPaletteResourceImpl) factory.newInstance(DiagramPaletteResource.class);
			returned.initName(diagramPaletteFile.getName());
			// returned.setFile(diagramPaletteFile);
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(diagramPaletteFile, factory));

			PaletteInfo info = null;
			try {
				info = findPaletteInfo(new FileInputStream(diagramPaletteFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (info == null) {
				// Unable to retrieve infos, just abort
				logger.warning("Cannot retrieve info for palette " + diagramPaletteFile);
				return null;
			}
			// TODO: we already have set the name ???
			returned.initName(info.name);

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

	public static DiagramPaletteResource retrieveDiagramPaletteResource(DiagramSpecificationResource dsResource,
			InJarResourceImpl diagramPaletteInJarResource, FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(InJarFlexoIODelegate.class,
					DiagramPaletteResource.class));
			DiagramPaletteResourceImpl returned = (DiagramPaletteResourceImpl) factory.newInstance(DiagramPaletteResource.class);
			// returned.setFile(diagramPaletteFile);
			returned.setFlexoIODelegate(InJarFlexoIODelegateImpl.makeInJarFlexoIODelegate(diagramPaletteInJarResource, factory));
			PaletteInfo info = findPaletteInfo(diagramPaletteInJarResource.openInputStream());
			if (info == null) {
				return null;
			}
			returned.initName(info.name);

			returned.setURI(dsResource.getURI() + "/" + returned.getName() + ".palette");
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

	private static class PaletteInfo {
		public String name;
	}

	private static PaletteInfo findPaletteInfo(InputStream paletteInputStream) {
		Document document;
		try {
			// if (diagramFile.exists()) {

			document = readXMLInputStream(paletteInputStream);
			Element root = getElement(document, "DiagramPalette");
			if (root != null) {
				PaletteInfo returned = new PaletteInfo();
				Iterator<Attribute> it = root.getAttributes().iterator();
				while (it.hasNext()) {
					Attribute at = it.next();
					if (at.getName().equals("name")) {
						logger.fine("Returned " + at.getValue());
						returned.name = at.getValue();
					}
				}
				return returned;
			}
			/*} else {
				logger.warning("While analysing diagram candidate cannot find file " + diagramFile.getAbsolutePath());
			}*/
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.fine("Returned null");
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
		// returned.setName(getFile().getName().substring(0, getFile().getName().length() - 8));
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
