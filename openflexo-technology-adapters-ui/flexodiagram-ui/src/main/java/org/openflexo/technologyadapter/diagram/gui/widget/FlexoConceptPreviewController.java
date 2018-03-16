/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.gui.widget;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.diana.FGEModelFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.selection.SelectionManager;
import org.openflexo.selection.SelectionManagingDianaEditor;

public class FlexoConceptPreviewController extends SelectionManagingDianaEditor<FlexoConcept> {

	private static final Logger logger = Logger.getLogger(FlexoConceptPreviewController.class.getPackage().getName());

	// We share here instances of FlexoConceptPreviewRepresentation because they can be accessed from multiple
	// FlexoConceptPreviewComponent
	private static final Map<FlexoConcept, FlexoConceptPreviewRepresentation> flexoConceptPreviewRepresentations = new Hashtable<>();

	/**
	 * Obtain or build stored FlexoConceptPreviewRepresentation (they are all shared because they can be accessed from multiple
	 * FlexoConceptPreviewComponent)
	 * 
	 * @param flexoConcept
	 * @return
	 */
	private static final FlexoConceptPreviewRepresentation obtainFlexoConceptPreviewRepresentations(FlexoConcept flexoConcept,
			FGEModelFactory factory) {
		FlexoConceptPreviewRepresentation returned = flexoConceptPreviewRepresentations.get(flexoConcept);
		if (returned == null) {
			returned = new FlexoConceptPreviewRepresentation(flexoConcept, factory);
			flexoConceptPreviewRepresentations.put(flexoConcept, returned);
		}
		return returned;
	}

	public FlexoConceptPreviewController(FlexoConcept flexoConcept, SelectionManager sm, FGEModelFactory factory) {
		super(obtainFlexoConceptPreviewRepresentations(flexoConcept, factory), sm, factory, null);
	}

	@Override
	public void delete() {
		// Drawing is no more deleted since we keep all instances for sharing !!!!
		// getDrawing().delete();
		super.delete();
	}

	public FlexoConcept getFlexoConcept() {
		return getDrawing().getModel();
	}

}
