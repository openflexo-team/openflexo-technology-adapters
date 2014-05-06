/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.technologyadapter.diagram.metamodel;

import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.foundation.FlexoModelFactory;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoUndoManager;
import org.openflexo.foundation.resource.PamelaResource;
import org.openflexo.foundation.resource.PamelaResourceImpl.IgnoreLoadingEdits;
import org.openflexo.model.converter.RelativePathFileConverter;
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
public class DiagramPaletteFactory extends FGEModelFactoryImpl implements FlexoModelFactory {

	public DiagramPaletteFactory(EditingContext editingContext, DiagramPaletteResource paletteResource) throws ModelDefinitionException {
		super(DiagramPalette.class, DiagramPaletteElement.class);
		addConverter(new RelativePathFileConverter(paletteResource.getFile().getParentFile()));
		setEditingContext(editingContext);
	}

	public DiagramPaletteFactory(EditingContext editingContext) throws ModelDefinitionException {
		super(DiagramPalette.class, DiagramPaletteElement.class);
		setEditingContext(editingContext);
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

	private PamelaResource<?, ?> resourceBeeingDeserialized = null;
	private IgnoreLoadingEdits ignoreHandler = null;
	private FlexoUndoManager undoManager = null;

	@Override
	public synchronized void startDeserializing(PamelaResource<?, ?> resource) throws ConcurrentDeserializationException {
		if (resourceBeeingDeserialized == null) {
			resourceBeeingDeserialized = resource;
		} else {
			throw new ConcurrentDeserializationException(resource);
		}

		EditingContext editingContext = resource.getServiceManager().getEditingContext();

		if (editingContext != null && editingContext.getUndoManager() instanceof FlexoUndoManager) {
			undoManager = (FlexoUndoManager) editingContext.getUndoManager();
			undoManager.addToIgnoreHandlers(ignoreHandler = new IgnoreLoadingEdits(resource));
			// System.out.println("@@@@@@@@@@@@@@@@ START LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public synchronized void stopDeserializing(PamelaResource<?, ?> resource) {
		if (resourceBeeingDeserialized == resource) {
			resourceBeeingDeserialized = null;
		}

		if (ignoreHandler != null) {
			undoManager.removeFromIgnoreHandlers(ignoreHandler);
			// System.out.println("@@@@@@@@@@@@@@@@ END LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public <I> void objectHasBeenDeserialized(I newlyCreatedObject, Class<I> implementedInterface) {
		super.objectHasBeenDeserialized(newlyCreatedObject, implementedInterface);
		if (newlyCreatedObject instanceof FlexoObject) {
			resourceBeeingDeserialized.setLastID(((FlexoObject) newlyCreatedObject).getFlexoID());
		}
	}

}
