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

package org.openflexo.technologyadapter.diagram.metamodel;

import org.openflexo.foundation.DefaultPamelaResourceModelFactory;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.converter.RelativePathResourceConverter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;

/**
 * Diagram specification factory<br>
 * 
 * @author sylvain
 * 
 */
public class DiagramSpecificationFactory extends DefaultPamelaResourceModelFactory<DiagramSpecificationResource> {

	private RelativePathResourceConverter relativePathResourceConverter;

	public DiagramSpecificationFactory(DiagramSpecificationResource resource, EditingContext editingContext)
			throws ModelDefinitionException {
		super(resource, ModelContextLibrary.getModelContext(DiagramSpecification.class));
		setEditingContext(editingContext);
		addConverter(relativePathResourceConverter = new RelativePathResourceConverter(null));
		if (resource != null && resource.getFlexoIODelegate() != null
				&& resource.getFlexoIODelegate().getSerializationArtefactAsResource() != null) {
			relativePathResourceConverter
					.setContainerResource(resource.getFlexoIODelegate().getSerializationArtefactAsResource().getContainer());
		}
	}

	public DiagramSpecification makeNewDiagramSpecification() {
		return newInstance(DiagramSpecification.class);
	}

}
