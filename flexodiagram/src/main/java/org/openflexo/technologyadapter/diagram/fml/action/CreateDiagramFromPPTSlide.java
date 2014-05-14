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
package org.openflexo.technologyadapter.diagram.fml.action;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.resource.InvalidFileNameException;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;

public class CreateDiagramFromPPTSlide extends AbstractCreateDiagramFromPPTSlide<CreateDiagramFromPPTSlide, RepositoryFolder> {

	private static final Logger logger = Logger.getLogger(CreateDiagramFromPPTSlide.class.getPackage().getName());

	public static FlexoActionType<CreateDiagramFromPPTSlide, RepositoryFolder, ViewPointObject> actionType = new FlexoActionType<CreateDiagramFromPPTSlide, RepositoryFolder, ViewPointObject>(
			"create_diagram_from_ppt_slide", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateDiagramFromPPTSlide makeNewAction(RepositoryFolder focusedObject, Vector<ViewPointObject> globalSelection,
				FlexoEditor editor) {
			return new CreateDiagramFromPPTSlide(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(RepositoryFolder object, Vector<ViewPointObject> globalSelection) {
			if (object != null && object.getResourceRepository() instanceof DiagramRepository) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isEnabledForSelection(RepositoryFolder object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateDiagramFromPPTSlide.actionType, RepositoryFolder.class);
	}

	CreateDiagramFromPPTSlide(RepositoryFolder focusedObject, Vector<ViewPointObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException, SaveResourceException,
			InvalidFileNameException {
		logger.info("Add diagram from ppt slide");

		if (getDiagram() == null) {
			DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(
					DiagramTechnologyAdapter.class);

			setDiagramResource(diagramTA.createNewDiagram(getDiagramName(), getDiagramURI(), getDiagramFile(), null));
			getFocusedObject().addToResources(getDiagramResource());
			getDiagramResource().save(null);
		}

		if (getSlide() != null) {
			convertSlideToDiagram(getSlide());
		} else {
			System.out.println("Error: no Slide");
		}
	}

	@Override
	public String getDiagramURI() {
		return getDefaultDiagramURI();
	}

	public String getDefaultDiagramURI() {
		return getFocusedObject().getResourceRepository().generateURI(getDiagramName());
	}

	@Override
	public File getDiagramFile() {
		return getDefaultDiagramFile();
	}

	public File getDefaultDiagramFile() {
		return new File(getFocusedObject().getFile(), getDiagramName() + DiagramResource.DIAGRAM_SUFFIX);
	}

}
