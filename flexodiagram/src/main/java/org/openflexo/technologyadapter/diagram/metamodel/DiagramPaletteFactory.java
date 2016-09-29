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

import java.util.logging.Logger;

import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.PamelaResourceModelFactory;
import org.openflexo.foundation.action.FlexoUndoManager;
import org.openflexo.foundation.resource.PamelaResourceImpl.IgnoreLoadingEdits;
import org.openflexo.model.converter.RelativePathResourceConverter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;

/**
 * Diagram factory<br>
 * Only one instance of this class should be used
 * 
 * @author sylvain
 * 
 */
public class DiagramPaletteFactory extends FGEModelFactoryImpl implements PamelaResourceModelFactory<DiagramPaletteResource> {

	private static final Logger logger = Logger.getLogger(DiagramPaletteFactory.class.getPackage().getName());

	private final DiagramPaletteResource resource;
	private IgnoreLoadingEdits ignoreHandler = null;
	private FlexoUndoManager undoManager = null;

	private RelativePathResourceConverter relativePathResourceConverter;

	public DiagramPaletteFactory(DiagramPaletteResource paletteResource, EditingContext editingContext) throws ModelDefinitionException {
		super(DiagramPalette.class, DiagramPaletteElement.class);
		addConverter(relativePathResourceConverter = new RelativePathResourceConverter(null));
		if (paletteResource != null && paletteResource.getFlexoIODelegate() != null
				&& paletteResource.getFlexoIODelegate().getSerializationArtefactAsResource() != null) {
			relativePathResourceConverter
					.setContainerResource(paletteResource.getFlexoIODelegate().getSerializationArtefactAsResource().getContainer());
		}
		setEditingContext(editingContext);
		this.resource = paletteResource;
	}

	@Override
	public DiagramPaletteResource getResource() {
		return resource;
	}

	public DiagramPalette makeNewDiagramPalette() {
		DiagramPalette returned = newInstance(DiagramPalette.class);
		return returned;
	}

	public DiagramPaletteElement makeDiagramPaletteElement() {
		DiagramPaletteElement returned = newInstance(DiagramPaletteElement.class);
		return returned;
	}

	public ShapeGraphicalRepresentation makeNewShapeGR(ShapeGraphicalRepresentation aGR) {
		ShapeGraphicalRepresentation returned = newInstance(ShapeGraphicalRepresentation.class, true, true);
		returned.setFactory(this);
		returned.setsWith(aGR);
		returned.setIsFocusable(true);
		returned.setIsSelectable(true);
		returned.setIsReadOnly(false);
		returned.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);
		return returned;
	}

	@Override
	public synchronized void startDeserializing() {

		EditingContext editingContext = getResource().getServiceManager().getEditingContext();

		if (editingContext != null && editingContext.getUndoManager() instanceof FlexoUndoManager) {
			undoManager = (FlexoUndoManager) editingContext.getUndoManager();
			undoManager.addToIgnoreHandlers(ignoreHandler = new IgnoreLoadingEdits(getResource()));
			// System.out.println("@@@@@@@@@@@@@@@@ START LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public synchronized void stopDeserializing() {
		if (ignoreHandler != null) {
			undoManager.removeFromIgnoreHandlers(ignoreHandler);
			// System.out.println("@@@@@@@@@@@@@@@@ END LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public <I> void objectHasBeenDeserialized(I newlyCreatedObject, Class<I> implementedInterface) {
		super.objectHasBeenDeserialized(newlyCreatedObject, implementedInterface);
		if (getResource() != null) {
			if (newlyCreatedObject instanceof FlexoObject) {
				getResource().setLastID(((FlexoObject) newlyCreatedObject).getFlexoID());
			}
		}
		else {
			logger.warning("Could not access resource beeing deserialized");
		}
	}

}
