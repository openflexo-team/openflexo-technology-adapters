package org.openflexo.technologyadapter.diagram.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.converter.RelativePathFileConverter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.DiagramSpecificationFactory;
import org.openflexo.toolbox.FlexoVersion;
import org.openflexo.toolbox.StringUtils;

public abstract class DiagramSpecificationResourceImpl extends PamelaResourceImpl<DiagramSpecification, DiagramSpecificationFactory>
		implements DiagramSpecificationResource {

	static final Logger logger = Logger.getLogger(DiagramSpecificationResourceImpl.class.getPackage().getName());

	public static DiagramSpecificationResource makeDiagramSpecificationResource(String name, RepositoryFolder<?> folder, String uri,
			FlexoServiceManager serviceManager) {
		try {
			File diagramSpecificationDirectory = new File(folder.getFile(), name + DIAGRAM_SPECIFICATION_SUFFIX);
			ModelFactory factory = new ModelFactory(DiagramSpecificationResource.class);
			DiagramSpecificationResourceImpl returned = (DiagramSpecificationResourceImpl) factory
					.newInstance(DiagramSpecificationResource.class);
			DiagramSpecificationFactory diagramSpecificationFactory = new DiagramSpecificationFactory(serviceManager.getEditingContext());
			returned.setFactory(diagramSpecificationFactory);
			String baseName = name;
			File diagramSpecificationXMLFile = new File(diagramSpecificationDirectory, baseName + ".xml");
			returned.setName(name);
			returned.setDirectory(diagramSpecificationDirectory);
			returned.setFile(diagramSpecificationXMLFile);
			returned.setURI(uri);
			returned.setServiceManager(serviceManager);
			returned.setRelativePathFileConverter(new RelativePathFileConverter(diagramSpecificationDirectory));
			// viewPointResource.addToContents(returned);
			// viewPointResource.notifyContentsAdded(returned);
			DiagramSpecification newDiagram = returned.getFactory().makeNewDiagramSpecification();
			newDiagram.setResource(returned);
			returned.setResourceData(newDiagram);
			newDiagram.setURI(uri);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DiagramSpecificationResource retrieveDiagramSpecificationResource(File diagramSpecificationDirectory,
			RepositoryFolder<?> folder, FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(DiagramSpecificationResource.class);
			DiagramSpecificationResourceImpl returned = (DiagramSpecificationResourceImpl) factory
					.newInstance(DiagramSpecificationResource.class);
			DiagramSpecificationFactory diagramSpecificationFactory = new DiagramSpecificationFactory(serviceManager.getEditingContext());
			returned.setFactory(diagramSpecificationFactory);
			String baseName = diagramSpecificationDirectory.getName().substring(0,
					diagramSpecificationDirectory.getName().length() - DIAGRAM_SPECIFICATION_SUFFIX.length());

			returned.setName(baseName);
			File diagramSpecificationXMLFile = new File(diagramSpecificationDirectory, baseName + ".xml");
			// returned.setFile(diagramSpecificationDirectory);

			// System.out.println("Looking infos in " + diagramSpecificationXMLFile);

			DiagramSpecificationInfo vpi = findDiagramSpecificationInfo(diagramSpecificationXMLFile);
			if (vpi == null) {
				// Unable to retrieve infos, just abort
				logger.warning("Cannot retrieve info for diagram specification " + diagramSpecificationDirectory);
				return null;
			}
			returned.setURI(vpi.uri);
			returned.setFile(diagramSpecificationXMLFile);
			returned.setDirectory(diagramSpecificationDirectory);
			returned.setName(vpi.name);
			if (StringUtils.isNotEmpty(vpi.version)) {
				returned.setVersion(new FlexoVersion(vpi.version));
			}
			returned.setModelVersion(new FlexoVersion(StringUtils.isNotEmpty(vpi.modelVersion) ? vpi.modelVersion : "0.1"));

			returned.setServiceManager(serviceManager);

			logger.fine("DiagramSpecificationResource " + diagramSpecificationDirectory.getAbsolutePath() + " version "
					+ returned.getModelVersion());

			// Now look for example diagrams
			if (diagramSpecificationDirectory.exists() && diagramSpecificationDirectory.isDirectory()) {
				for (File f : diagramSpecificationDirectory.listFiles()) {
					if (f.getName().endsWith(".diagram")) {

						// System.out.println("Found example diagram: " + f);

						DiagramResource exampleDiagramResource = DiagramResourceImpl.retrieveDiagramResource(f, serviceManager);
						returned.addToContents(exampleDiagramResource);
						logger.fine("ExampleDiagramResource " + exampleDiagramResource.getFile().getAbsolutePath() + " version "
								+ exampleDiagramResource.getModelVersion());
					}
				}
			}

			// Now look for palettes
			if (diagramSpecificationDirectory.exists() && diagramSpecificationDirectory.isDirectory()) {
				for (File f : diagramSpecificationDirectory.listFiles()) {
					if (f.getName().endsWith(".palette")) {

						// System.out.println("Found a palette : " + f);

						DiagramPaletteResource diagramPaletteResource = DiagramPaletteResourceImpl.retrieveDiagramPaletteResource(returned,
								f, serviceManager);
						logger.fine("DiagramPaletteResource " + diagramPaletteResource.getFile().getAbsolutePath() + " version "
								+ diagramPaletteResource.getModelVersion());
					}
				}
			}

			returned.setRelativePathFileConverter(new RelativePathFileConverter(diagramSpecificationDirectory));

			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<DiagramSpecification> getResourceDataClass() {
		return DiagramSpecification.class;
	}

	/**
	 * Return virtual model stored by this resource<br>
	 * Load the resource data when unloaded
	 */
	@Override
	public DiagramSpecification getDiagramSpecification() {
		try {
			return getResourceData(null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return virtual model stored by this resource when loaded<br>
	 * Do not force the resource data to be loaded
	 */
	@Override
	public DiagramSpecification getLoadedDiagramSpecification() {
		if (isLoaded()) {
			return getDiagramSpecification();
		}
		return null;
	}

	@Override
	public DiagramTechnologyAdapter getTechnologyAdapter() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}
		return null;
	}

	private static class DiagramSpecificationInfo {
		public String uri;
		public String version;
		public String name;
		public String modelVersion;
	}

	private static DiagramSpecificationInfo findDiagramSpecificationInfo(File diagramSpecification) {
		Document document;
		try {
			logger.fine("Try to find infos for " + diagramSpecification);

			/*String baseName = diagramSpecificationDirectory.getName();
			File xmlFile = new File(diagramSpecificationDirectory, baseName + ".xml");*/

			if (diagramSpecification.exists()) {

				document = readXMLFile(diagramSpecification);
				Element root = getElement(document, "DiagramSpecification");
				if (root != null) {
					DiagramSpecificationInfo returned = new DiagramSpecificationInfo();
					Iterator<Attribute> it = root.getAttributes().iterator();
					while (it.hasNext()) {
						Attribute at = it.next();
						if (at.getName().equals("uri")) {
							logger.fine("Returned " + at.getValue());
							returned.uri = at.getValue();
						} else if (at.getName().equals("name")) {
							logger.fine("Returned " + at.getValue());
							returned.name = at.getValue();
						} else if (at.getName().equals("version")) {
							logger.fine("Returned " + at.getValue());
							returned.version = at.getValue();
						} else if (at.getName().equals("modelVersion")) {
							logger.fine("Returned " + at.getValue());
							returned.modelVersion = at.getValue();
						}
					}
					if (StringUtils.isEmpty(returned.name)) {
						returned.name = diagramSpecification.getName();
					}
					return returned;
				}
			} else {
				logger.warning("While analysing diagram-spec candidate: " + diagramSpecification.getAbsolutePath() + " cannot find file "
						+ diagramSpecification.getAbsolutePath());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.fine("Returned null");
		return null;
	}

	@Override
	public List<DiagramResource> getExampleDiagramResources() {
		return getContents(DiagramResource.class);
	}

	@Override
	public List<DiagramPaletteResource> getDiagramPaletteResources() {
		DiagramSpecification ds = getDiagramSpecification();
		return getContents(DiagramPaletteResource.class);
	}

	@Override
	public DiagramSpecification getMetaModelData() {
		return getDiagramSpecification();
	}

	@Override
	public boolean delete() {
		if (super.delete()) {
			getServiceManager().getResourceManager().addToFilesToDelete(getDirectory());
			isDeleted = true;
			// also remove the parent folder if empty, created by openflexo
			/*if (!(getDirectory().length() > 0)) {
				getDirectory().delete();
			} else {
				logger.warning("Diagram specification folder cannot be deleted because it is not empty");
			}*/
			return true;
		}

		return false;
	}

}
