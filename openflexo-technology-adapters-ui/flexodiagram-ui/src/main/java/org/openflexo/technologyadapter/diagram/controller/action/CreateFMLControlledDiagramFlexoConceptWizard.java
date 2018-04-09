/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Fml-technologyadapter-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.controller.action;

import java.util.logging.Logger;

import org.openflexo.fml.controller.action.CreateFlexoConceptWizard;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptObject;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.view.controller.FlexoController;

/**
 * Specialize CreateFlexoConceptWizard by providing additional wizard step to create a palette element in a palette, if this new
 * {@link FlexoConcept} is about to be created in a FML-controlled diagram VirtualModel
 * 
 * @author sylvain
 *
 */
public class CreateFMLControlledDiagramFlexoConceptWizard extends CreateFlexoConceptWizard {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFMLControlledDiagramFlexoConceptWizard.class.getPackage().getName());

	private ConfigurePaletteElementForNewFlexoConcept<CreateFlexoConcept, FlexoConceptObject, FMLObject> configurePaletteElementForNewFlexoConcept;

	public CreateFMLControlledDiagramFlexoConceptWizard(CreateFlexoConcept action, FlexoController controller) {
		super(action, controller);
		// TODO, one day
		/*addStep(configurePaletteElementForNewFlexoConcept = new ConfigurePaletteElementForNewFlexoConcept<CreateFlexoConcept, VirtualModel, FMLObject>(
				action, this) {
		
			@Override
			public VirtualModel getVirtualModel() {
				if (getAction().getFocusedObject() instanceof VirtualModel) {
					return (VirtualModel) getAction().getFocusedObject();
				}
				return null;
			}
		
			@Override
			public FlexoConcept getFlexoConcept() {
				return CreateFMLControlledDiagramFlexoConceptWizard.this.getAction().getNewFlexoConcept();
			}
		
			@Override
			public String getFlexoConceptName() {
				return null;
			}
		
			@Override
			public DropScheme getDropScheme() {
				// TODO
				return null;
			}
		
			@Override
			public String getDropSchemeName() {
				return null;
			}
		
			@Override
			public String getDefaultPaletteElementName() {
				// TODO
				return null;
			}
		
			@Override
			public List<? extends org.openflexo.technologyadapter.diagram.controller.action.ConfigurePaletteElementForNewFlexoConcept.GraphicalElementEntry> getGraphicalElementEntries() {
				// TODO
				return null;
			}
		
			@Override
			public ScreenshotImage<DiagramShape> makeScreenshot() {
				// TODO
				return null;
			}
		});*/
	}

	public ConfigurePaletteElementForNewFlexoConcept<CreateFlexoConcept, FlexoConceptObject, FMLObject> getConfigurePaletteElementForNewFlexoConcept() {
		return configurePaletteElementForNewFlexoConcept;
	}

}
