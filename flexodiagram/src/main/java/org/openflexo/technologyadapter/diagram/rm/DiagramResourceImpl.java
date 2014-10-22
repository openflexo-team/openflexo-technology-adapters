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
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.InconsistentDataException;
import org.openflexo.foundation.InvalidModelDefinitionException;
import org.openflexo.foundation.InvalidXMLException;
import org.openflexo.foundation.ProjectDataResource;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.openflexo.foundation.resource.FlexoFileNotFoundException;
import org.openflexo.foundation.resource.InJarFlexoIODelegate;
import org.openflexo.foundation.resource.InJarFlexoIODelegate.InJarFlexoIODelegateImpl;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.viewpoint.rm.VirtualModelResource;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.rm.InJarResourceImpl;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.toolbox.IProgress;
import org.openflexo.toolbox.StringUtils;

/**
 * Default implementation for {@link ProjectDataResource}
 * 
 * 
 * @author Sylvain
 * 
 */
public abstract class DiagramResourceImpl extends PamelaResourceImpl<Diagram, DiagramFactory> implements DiagramResource {

	static final Logger logger = Logger.getLogger(DiagramResourceImpl.class.getPackage().getName());

	public static DiagramResource makeDiagramResource(String name, String uri, File diagramFile, FlexoServiceManager serviceManager) {
		return makeDiagramResource(name, uri, diagramFile, null, serviceManager);
	}

	public static DiagramResource makeDiagramResource(String name, String uri, File diagramFile,
			DiagramSpecificationResource diagramSpecificationResource, FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,DiagramResource.class));
			DiagramResourceImpl returned = (DiagramResourceImpl) factory.newInstance(DiagramResource.class);
			DiagramFactory diagramFactory = new DiagramFactory(returned, serviceManager.getEditingContext());
			returned.setFactory(diagramFactory);
			returned.setName(name);
			//returned.setFile(diagramFile);
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(diagramFile, factory));
			
			returned.setURI(uri);
			returned.setServiceManager(serviceManager);
			if (diagramSpecificationResource != null) {
				returned.setMetaModelResource(diagramSpecificationResource);
			}
			Diagram newDiagram = returned.getFactory().makeNewDiagram();
			newDiagram.setResource(returned);
			returned.setResourceData(newDiagram);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DiagramResource retrieveDiagramResource(File diagramFile, FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,DiagramResource.class));
			DiagramResourceImpl returned = (DiagramResourceImpl) factory.newInstance(DiagramResource.class);
			DiagramFactory diagramFactory = new DiagramFactory(returned, serviceManager.getEditingContext());
			returned.setFactory(diagramFactory);
			String baseName = diagramFile.getName().substring(0, diagramFile.getName().length() - DiagramResource.DIAGRAM_SUFFIX.length());
			returned.setName(baseName);
			//returned.setFile(diagramFile);
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(diagramFile, factory));
			
			DiagramInfo info=null;
			try {
				info = findDiagramInfo(new FileInputStream(diagramFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (info == null) {
				// Unable to retrieve infos, just abort
				logger.warning("Cannot retrieve info for diagram " + diagramFile);
				return null;
			}
			returned.setURI(info.uri);
			if (StringUtils.isNotEmpty(info.diagramSpecificationURI)) {
				DiagramTechnologyAdapter ta = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
						DiagramTechnologyAdapter.class);
				// System.out.println("diagramSpecificationURI=" + info.diagramSpecificationURI);
				DiagramSpecificationResource dsResource = (DiagramSpecificationResource) ta.getTechnologyContextManager()
						.getResourceWithURI(info.diagramSpecificationURI);
				returned.setMetaModelResource(dsResource);
			}
			returned.setServiceManager(serviceManager);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DiagramResource retrieveDiagramResource(InJarResourceImpl inJarResource, FlexoServiceManager serviceManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext( 
					InJarFlexoIODelegate.class,DiagramResource.class));
			DiagramResourceImpl returned = (DiagramResourceImpl) factory.newInstance(DiagramResource.class);
			DiagramFactory diagramFactory = new DiagramFactory(returned, serviceManager.getEditingContext());
			returned.setFactory(diagramFactory);
			
			returned.setFlexoIODelegate(InJarFlexoIODelegateImpl.makeInJarFlexoIODelegate(inJarResource, factory));
			DiagramInfo info = findDiagramInfo(inJarResource.openInputStream());
			if (info == null) {
				// Unable to retrieve infos, just abort
				//logger.warning("Cannot retrieve info for diagram " + diagramFile);
				return null;
			}
			returned.setURI(info.uri);
			if (StringUtils.isNotEmpty(info.diagramSpecificationURI)) {
				DiagramTechnologyAdapter ta = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
						DiagramTechnologyAdapter.class);
				// System.out.println("diagramSpecificationURI=" + info.diagramSpecificationURI);
				DiagramSpecificationResource dsResource = (DiagramSpecificationResource) ta.getTechnologyContextManager()
						.getResourceWithURI(info.diagramSpecificationURI);
				returned.setMetaModelResource(dsResource);
			}
			returned.setServiceManager(serviceManager);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Diagram getDiagram() {
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

	@Override
	public Diagram getModel() {
		return getModelData();
	}

	@Override
	public Diagram getModelData() {
		return getDiagram();
	}

	@Override
	public Class<Diagram> getResourceDataClass() {
		return Diagram.class;
	}

	@Override
	public Diagram loadResourceData(IProgress progress) throws FlexoFileNotFoundException, IOFlexoException, InvalidXMLException,
			InconsistentDataException, InvalidModelDefinitionException {

		Diagram returned = super.loadResourceData(progress);
		/*if (returned.isSynchronizable()) {
			returned.synchronize(null);
		}
		getContainer().getView().addToVirtualModelInstances(returned);*/
		returned.clearIsModified();
		return returned;
	}

	@Override
	public DiagramTechnologyAdapter getTechnologyAdapter() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}
		return null;
	}

	private static class DiagramInfo {
		public String uri;
		public String name;
		public String version;
		public String title;
		public String modelVersion;
		public String diagramSpecificationURI;
	}

	private static DiagramInfo findDiagramInfo(InputStream diagramInputStream) {
		Document document;
		try {
		//	if (diagramFile.exists()) {

				document = readXMLInputStream(diagramInputStream);
				Element root = getElement(document, "Diagram");
				if (root != null) {
					DiagramInfo returned = new DiagramInfo();
					Iterator<Attribute> it = root.getAttributes().iterator();
					while (it.hasNext()) {
						Attribute at = it.next();
						if (at.getName().equals("uri")) {
							logger.fine("Returned " + at.getValue());
							returned.uri = at.getValue();
						} else if (at.getName().equals("title")) {
							logger.fine("Returned " + at.getValue());
							returned.title = at.getValue();
						} else if (at.getName().equals("name")) {
							logger.fine("Returned " + at.getValue());
							returned.name = at.getValue();
						} else if (at.getName().equals("version")) {
							logger.fine("Returned " + at.getValue());
							returned.version = at.getValue();
						} else if (at.getName().equals("modelVersion")) {
							logger.fine("Returned " + at.getValue());
							returned.modelVersion = at.getValue();
						} else if (at.getName().equals(Diagram.DIAGRAM_SPECIFICATION_URI)) {
							logger.fine("Returned " + at.getValue());
							returned.diagramSpecificationURI = at.getValue();
						}
					}
					if (StringUtils.isEmpty(returned.title)) {
						returned.title = "NoName";//diagramFile.getName();
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

}
