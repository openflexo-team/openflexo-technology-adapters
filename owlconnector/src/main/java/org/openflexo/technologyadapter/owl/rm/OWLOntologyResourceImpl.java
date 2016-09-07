/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.toolbox.IProgress;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.RDFWriter;

/**
 * Represents the resource associated to a {@link OWLOntology}
 * 
 * @author sguerin
 * 
 */
public abstract class OWLOntologyResourceImpl extends FlexoResourceImpl<OWLOntology>implements OWLOntologyResource {

	private static final Logger logger = Logger.getLogger(OWLOntologyResourceImpl.class.getPackage().getName());

	/**
	 * Load the &quot;real&quot; load resource data of this resource.
	 * 
	 * @param progress
	 *            a progress monitor in case the resource data is not immediately available.
	 * @return the resource data.
	 * @throws ResourceLoadingCancelledException
	 * @throws ResourceDependencyLoopException
	 * @throws FileNotFoundException
	 * @throws FlexoException
	 */
	@Override
	public OWLOntology loadResourceData(IProgress progress)
			throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {
		OWLOntology returned = new OWLOntology(getURI(), getFile(), getOntologyLibrary(), getTechnologyAdapter());
		returned.setResource(this);
		resourceData = returned;
		return returned;
	}

	/**
	 * Save the &quot;real&quot; resource data of this resource.
	 * 
	 * @throws SaveResourceException
	 */
	@Override
	public void save(IProgress progress) throws SaveResourceException {
		OWLOntology resourceData;
		try {
			resourceData = getResourceData(progress);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFileFlexoIODelegate());
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFileFlexoIODelegate());
		} catch (FlexoException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFileFlexoIODelegate());
		}

		if (!getFileFlexoIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getFile().getAbsolutePath());
			}
			throw new SaveResourcePermissionDeniedException(getFileFlexoIODelegate());
		}
		if (resourceData != null) {
			FileWritingLock lock = getFileFlexoIODelegate().willWriteOnDisk();
			_writeToFile();
			getFileFlexoIODelegate().hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			resourceData.clearIsModified(false);
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Succeeding to save Resource " + getURI() + " : " + getFile().getName());
			}
		}
	}

	private void _writeToFile() throws SaveResourceException {
		System.out.println("Saving OWL ontology to " + getFile().getAbsolutePath());
		FileOutputStream out = null;
		try {
			OWLOntology ontology = getResourceData(null);
			OntModel ontModel = ontology.getOntModel();
			ontModel.setNsPrefix("base", ontology.getURI());
			out = new FileOutputStream(getFile());
			RDFWriter writer = ontModel.getWriter("RDF/XML-ABBREV");
			writer.setProperty("xmlbase", ontology.getURI());
			writer.write(ontModel.getBaseModel(), out, ontology.getURI());
			// getOntModel().setNsPrefix("base", getOntologyURI());
			// getOntModel().write(out, "RDF/XML-ABBREV", getOntologyURI()); // "RDF/XML-ABBREV"
			clearIsModified(true);
			logger.info("Wrote " + getFile());
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFileFlexoIODelegate());
		} catch (FlexoException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFileFlexoIODelegate());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.warning("FileNotFoundException: " + e.getMessage());
			throw new SaveResourceException(getFileFlexoIODelegate());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.warning("IOException: " + e.getMessage());
				throw new SaveResourceException(getFileFlexoIODelegate());
			}
		}

	}

	@Override
	public FlexoMetaModelResource<OWLOntology, OWLOntology, OWLTechnologyAdapter> getMetaModelResource() {
		logger.warning("FlexoMetaModelResource() not fully implemented in OWLOntologyResource");
		// TODO: implement this and extends cardinality

		return getModel().getImportedOntologies().get(0).getResource();
	}

	@Override
	public void setMetaModelResource(FlexoMetaModelResource<OWLOntology, OWLOntology, OWLTechnologyAdapter> aMetaModelResource) {
		// TODO: implement this and extends cardinality
	}

	@Override
	public OWLOntology getModelData() {
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
			return null;
		}
	}

	@Override
	public OWLOntology getMetaModelData() {
		return getModelData();
	}

	@Override
	public OWLOntology getModel() {
		return getModelData();
	}

	@Override
	public Class<OWLOntology> getResourceDataClass() {
		return OWLOntology.class;
	}

	public FileFlexoIODelegate getFileFlexoIODelegate() {
		return (FileFlexoIODelegate) getFlexoIODelegate();
	}

	private File getFile() {
		return getFileFlexoIODelegate().getFile();
	}

}
