/*
 * (c) Copyright 2013-2014 Openflexo
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
package org.openflexo.technologyadapter.diagram.controller;

import java.util.logging.Logger;

import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.model.action.DropSchemeAction;
import org.openflexo.view.controller.ParametersRetriever;

public class DropSchemeParametersRetriever extends ParametersRetriever<DropScheme> {

	private static final Logger logger = Logger.getLogger(ParametersRetriever.class.getPackage().getName());

	protected DiagramPaletteElement paletteElement;

	public DropSchemeParametersRetriever(DropSchemeAction action) {
		super(action);
		paletteElement = action.getPaletteElement();
	}

}
