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

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.InconsistentDataException;
import org.openflexo.foundation.InvalidModelDefinitionException;
import org.openflexo.foundation.InvalidXMLException;
import org.openflexo.foundation.resource.FlexoFileNotFoundException;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
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

	String diagramSpecificationURI = null;
	private DiagramSpecificationResource diagramSpecificationResource = null;

	@Override
	public DiagramSpecificationResource getMetaModelResource() {
		if (diagramSpecificationResource == null && StringUtils.isNotEmpty(diagramSpecificationURI)) {
			// DiagramSpecificationResource not set yet, but URI is available, try to lookup it now
			diagramSpecificationResource = (DiagramSpecificationResource) getTechnologyAdapter().getTechnologyAdapterService()
					.getServiceManager().getResourceManager().getResource(diagramSpecificationURI);
			// System.out.println("****** Retrieved and found later: " + diagramSpecificationResource);
		}
		return diagramSpecificationResource;
	}

	@Override
	public void setMetaModelResource(
			FlexoMetaModelResource<Diagram, DiagramSpecification, DiagramTechnologyAdapter> diagramSpecificationResource) {
		if (diagramSpecificationResource != this.diagramSpecificationResource) {
			DiagramSpecificationResource oldValue = this.diagramSpecificationResource;
			this.diagramSpecificationResource = (DiagramSpecificationResource) diagramSpecificationResource;
			getPropertyChangeSupport().firePropertyChange("metaModelResource", oldValue, diagramSpecificationResource);
		}
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

}
