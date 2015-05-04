/*
 * (c) Copyright 2013 Openflexo
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

package org.openflexo.technologyadapter.docx.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.docx.DocXTechnologyContextManager;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.toolbox.IProgress;
import org.openflexo.toolbox.StringUtils;

public abstract class DocXDocumentResourceImpl extends FlexoResourceImpl<DocXDocument>implements DocXDocumentResource {
	private static final Logger logger = Logger.getLogger(DocXDocumentResourceImpl.class.getPackage().getName());

	public static DocXDocumentResource makeDocXDocumentResource(String modelURI, File modelFile,
			DocXTechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(DocXDocumentResource.class, FileFlexoIODelegate.class));
			DocXDocumentResourceImpl returned = (DocXDocumentResourceImpl) factory.newInstance(DocXDocumentResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));

			returned.setURI(modelURI);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);

			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DocXDocumentResource retrieveDocXDocumentResource(File modelFile, DocXTechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(DocXDocumentResource.class, FileFlexoIODelegate.class));
			DocXDocumentResourceImpl returned = (DocXDocumentResourceImpl) factory.newInstance(DocXDocumentResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			returned.setURI(modelFile.toURI().toString());
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DocXDocument loadResourceData(IProgress progress)
			throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {

		System.out.println("Hop, on charge le document " + getFile());

		try {
			WordprocessingMLPackage result = WordprocessingMLPackage.load(getFile());
			System.out.println("result=" + result);
			/*MainDocumentPart mdp = result.getMainDocumentPart();
			for (Object o : mdp.getContent()) {
				System.out.println("> " + o + " of " + o.getClass());
				if (o instanceof P) {
					System.out.println("paraId=" + ((P) o).getParaId());
					System.out.println("textId=" + ((P) o).getTextId());
				}
			}*/
			System.out.println("Je lis ca:");
			System.out.println(printContents(result.getMainDocumentPart(), 0));
			System.out.println("Au hasard: " + UUID.randomUUID().toString());
		} catch (Docx4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// DocXDocument returned = new DocXDocument(getURI(), getFile(), (DocXTechnologyAdapter) getTechnologyAdapter());
		// returned.loadWhenUnloaded();
		// return returned;
		return null;
	}

	private static String printContents(Object obj, int indent) {
		StringBuffer result = new StringBuffer();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();

		result.append(
				StringUtils.buildWhiteSpaceIndentation(indent * 2) + " > " + "[" + obj.getClass().getSimpleName() + "] " + obj + "\n");

		if (obj instanceof ContentAccessor) {
			indent++;
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.append(printContents(child, indent));
			}

		}
		return result.toString();
	}

	private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
		List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();

		if (obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}

		}
		return result;
	}

	private void replacePlaceholder(WordprocessingMLPackage template, String name, String placeholder) {
		List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

		for (Object text : texts) {
			Text textElement = (Text) text;
			if (textElement.getValue().equals(placeholder)) {
				textElement.setValue(name);
			}
		}
	}

	@Override
	public void save(IProgress progress) throws SaveResourceException {
		try {
			resourceData = getResourceData(progress);
		} catch (FileNotFoundException e) {
			DocXDocument resourceData;
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (FlexoException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		}

		if (!getFlexoIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getFlexoIODelegate().toString());
			}
			throw new SaveResourcePermissionDeniedException(getFlexoIODelegate());
		}
		if (resourceData != null) {
			FileWritingLock lock = getFlexoIODelegate().willWriteOnDisk();
			writeToFile();
			getFlexoIODelegate().hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			resourceData.clearIsModified(false);
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Succeeding to save Resource " + getURI() + " : " + getFile().getName());
			}
		}
	}

	@Override
	public DocXDocument getDocXDocument() {
		try {
			return getResourceData(null);
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void writeToFile() throws SaveResourceException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(getFile());
			StreamResult result = new StreamResult(out);
			TransformerFactory factory = TransformerFactory
					.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);

			Transformer transformer = factory.newTransformer();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}

		logger.info("Wrote " + getFile());
	}

	@Override
	public Class<DocXDocument> getResourceDataClass() {
		return DocXDocument.class;
	}

	private File getFile() {
		return getFileFlexoIODelegate().getFile();
	}

	public FileFlexoIODelegate getFileFlexoIODelegate() {
		return (FileFlexoIODelegate) getFlexoIODelegate();
	}
}
