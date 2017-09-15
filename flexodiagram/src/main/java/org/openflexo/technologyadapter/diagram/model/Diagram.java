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

package org.openflexo.technologyadapter.diagram.model;

import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.technologyadapter.FlexoModel;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;

/**
 * Represents a diagram in Openflexo build-in diagram technology<br>
 * 
 * Note that a {@link Diagram} may conform to a {@link DiagramSpecification}
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(DiagramImpl.class)
@XMLElement(xmlTag = "Diagram")
public interface Diagram
		extends DiagramContainerElement<DrawingGraphicalRepresentation>, FlexoModel<Diagram, DiagramSpecification>, ResourceData<Diagram> {

	public static final String URI = "uri";
	public static final String TITLE = "title";
	public static final String DIAGRAM_SPECIFICATION = "diagramSpecification";
	public static final String DIAGRAM_SPECIFICATION_URI = "diagramSpecificationURI";
	public static final String RESOURCE = "resource";

	/**
	 * Return title of this diagram
	 * 
	 * @return
	 */
	@Getter(value = TITLE)
	@XMLAttribute
	public String getTitle();

	/**
	 * Sets title of this diagram
	 * 
	 * @param aName
	 */
	@Setter(value = TITLE)
	public void setTitle(String aTitle);

	/**
	 * Return URI of this diagram
	 * 
	 * @return
	 */
	@Override
	@Getter(value = URI)
	@XMLAttribute
	public String getURI();

	/**
	 * Sets URI of this diagram
	 * 
	 * @param aName
	 */
	@Setter(value = URI)
	public void setURI(String anURI);

	/**
	 * Return the diagram specification of this diagram (might be null)
	 * 
	 * @return
	 */
	@Getter(value = DIAGRAM_SPECIFICATION, ignoreType = true)
	public DiagramSpecification getDiagramSpecification();

	/**
	 * Sets the diagram specification of this diagram (might be null)
	 * 
	 * @param aName
	 */
	@Setter(value = DIAGRAM_SPECIFICATION)
	public void setDiagramSpecification(DiagramSpecification aDiagramSpecification);

	/**
	 * Return the diagram specification URI of this diagram (might be null)
	 * 
	 * @return
	 */
	@Getter(value = DIAGRAM_SPECIFICATION_URI)
	@XMLAttribute
	public String getDiagramSpecificationURI();

	/**
	 * Sets the diagram specification URI of this diagram (might be null)
	 * 
	 * @param aName
	 */
	@Setter(value = DIAGRAM_SPECIFICATION_URI)
	public void setDiagramSpecificationURI(String aDiagramSpecificationURI);

	/**
	 * Return resource for this diagram
	 * 
	 * @return
	 */
	@Override
	@Getter(value = RESOURCE, ignoreType = true)
	// TODO: Ask Sylvain to investigate
	// Bug if we return DiagramResource instead of FlexoResource<Diagram>: investigate on PAMELA
	public FlexoResource<Diagram> getResource();

	/**
	 * Sets resource for this diagram
	 * 
	 * @param aName
	 */
	@Override
	@Setter(value = RESOURCE)
	public void setResource(FlexoResource<Diagram> aDiagramResource);

	public DiagramFactory getDiagramFactory();

	/**
	 * Return screenshot of this diagram, when available
	 * 
	 * @return
	 */
	public ScreenshotImage<Diagram> getScreenshotImage();

}
