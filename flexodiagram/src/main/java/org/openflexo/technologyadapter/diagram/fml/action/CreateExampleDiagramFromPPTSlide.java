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

package org.openflexo.technologyadapter.diagram.fml.action;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.resource.InvalidFileNameException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;

public class CreateExampleDiagramFromPPTSlide extends AbstractCreateDiagramFromPPTSlide<CreateExampleDiagramFromPPTSlide, DiagramSpecification> {

	private static final Logger logger = Logger.getLogger(CreateExampleDiagramFromPPTSlide.class.getPackage().getName());

	public static FlexoActionType<CreateExampleDiagramFromPPTSlide, DiagramSpecification, FMLObject> actionType = new FlexoActionType<CreateExampleDiagramFromPPTSlide, DiagramSpecification, FMLObject>(
			"create_diagram_from_ppt_slide", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateExampleDiagramFromPPTSlide makeNewAction(DiagramSpecification focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new CreateExampleDiagramFromPPTSlide(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramSpecification object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(DiagramSpecification object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateExampleDiagramFromPPTSlide.actionType, DiagramSpecification.class);
	}

	CreateExampleDiagramFromPPTSlide(DiagramSpecification focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException, SaveResourceException,
			InvalidFileNameException {
		logger.info("Add diagram from ppt slide");
		String newDiagramURI = getFocusedObject().getURI() + "/" + getDiagramName();
		DiagramTechnologyAdapter diagramTA = getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(
				DiagramTechnologyAdapter.class);

		setDiagramResource(diagramTA.createNewDiagram(getDiagramName(), newDiagramURI, getDiagramFile(), getFocusedObject().getResource()));
		getFocusedObject().getResource().addToContents(getDiagramResource());
		getFocusedObject().addToExampleDiagrams(getDiagramResource().getDiagram());
		getDiagramResource().save(null);
		if (getSlide() != null){
		super.convertSlideToDiagram(getSlide());
		}
		else {
			System.out.println("Error: no Slide");
		}
	}
	
	@Override
	public File getDiagramFile() {
		if (super.getDiagramFile() == null) {
			return getDefaultDiagramFile();
		}
		return super.getDiagramFile();
	}

	@Override
	public void setDiagramFile(File diagramFile) {
		setDiagramFile(diagramFile);
	}

	public File getDefaultDiagramFile() {
		return new File(ResourceLocator.retrieveResourceAsFile(getFocusedObject().getResource().getDirectory()), getDiagramName() + DiagramResource.DIAGRAM_SUFFIX);
	}
}
