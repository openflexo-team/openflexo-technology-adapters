/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.util.logging.Logger;

import org.openflexo.diana.Drawing.DrawingTreeNode;
import org.openflexo.diana.PaletteElementSpecification;
import org.openflexo.diana.ShapeGraphicalRepresentation;
import org.openflexo.diana.control.PaletteElement;
import org.openflexo.diana.geom.DianaPoint;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.converter.RelativePathResourceConverter;
import org.openflexo.rm.ResourceLocator;

public class CommonPalette extends DiagramEditorPaletteModel {

	@SuppressWarnings("unused")
	private static final Logger logger = FlexoLogger.getLogger(CommonPalette.class.getPackage().getName());

	public CommonPalette(DiagramEditor editor) {
		super(editor, "default", 210, 210, 40, 30, 10, 10);
		FACTORY.addConverter(
				new RelativePathResourceConverter(ResourceLocator.locateResource("Palettes/Basic").getContainer().getContainer()));
		readFromDirectory(ResourceLocator.locateResource("Palettes/Basic"));
	}

	@Override
	protected PaletteElement buildPaletteElement(final PaletteElementSpecification paletteElement) {
		PaletteElement returned = new PaletteElement() {
			@Override
			public boolean acceptDragging(DrawingTreeNode<?, ?> target) {
				return shouldAcceptDrop(target);
			}

			@Override
			public boolean elementDragged(DrawingTreeNode<?, ?> target, DianaPoint dropLocation) {
				return handleBasicGraphicalRepresentationDrop(target, paletteElement.getGraphicalRepresentation(), dropLocation,
						paletteElement.getApplyCurrentForeground(), paletteElement.getApplyCurrentBackground(),
						paletteElement.getApplyCurrentTextStyle(), paletteElement.getApplyCurrentShadowStyle(),
						paletteElement.getAskForImage(), true);
			}

			@Override
			public String getName() {
				return paletteElement.getName();
			}

			@Override
			public ShapeGraphicalRepresentation getGraphicalRepresentation() {
				return paletteElement.getGraphicalRepresentation();
			}

			@Override
			public void delete(Object... context) {
				paletteElement.delete(context);
			}

		};
		return returned;
	}

}
