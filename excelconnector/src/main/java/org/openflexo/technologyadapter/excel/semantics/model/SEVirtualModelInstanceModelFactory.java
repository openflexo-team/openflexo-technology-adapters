/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.semantics.model;

import org.apache.poi.ss.usermodel.Row;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstanceModelFactory;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.excel.semantics.rm.SEVirtualModelInstanceResource;

/**
 * {@link ModelFactory} used to handle {@link SEVirtualModelInstance} models<br>
 * 
 * @author sylvain
 * 
 */
public class SEVirtualModelInstanceModelFactory extends AbstractVirtualModelInstanceModelFactory<SEVirtualModelInstanceResource> {

	public SEVirtualModelInstanceModelFactory(SEVirtualModelInstanceResource virtualModelInstanceResource, EditingContext editingContext,
			TechnologyAdapterService taService) throws ModelDefinitionException {
		super(virtualModelInstanceResource, SEVirtualModelInstance.class, editingContext, taService);
	}

	public SEFlexoConceptInstance newFlexoConceptInstance(SEVirtualModelInstance owner, FlexoConceptInstance container, Row row,
			FlexoConcept concept) {
		System.out.println("On construit un nouveau SEFlexoConceptInstance pour " + row.getRowNum());
		SEFlexoConceptInstance returned = newInstance(SEFlexoConceptInstance.class, concept);
		returned.setRowSupportObject(row);
		owner.addToFlexoConceptInstances(returned);
		if (container != null && container != owner) {
			container.addToEmbeddedFlexoConceptInstances(returned);
		}
		return returned;

	}

}
