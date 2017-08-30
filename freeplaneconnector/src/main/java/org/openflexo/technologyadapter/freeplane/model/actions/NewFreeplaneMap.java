/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.freeplane.model.actions;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.rm.FreeplaneResourceFactory;
import org.openflexo.technologyadapter.freeplane.rm.IFreeplaneResource;

public class NewFreeplaneMap
		extends FlexoAction<NewFreeplaneMap, RepositoryFolder<IFreeplaneResource, ?>, RepositoryFolder<IFreeplaneResource, ?>> {

	private static final class CreateFreeplaneMapActionType
			extends FlexoActionFactory<NewFreeplaneMap, RepositoryFolder<IFreeplaneResource, ?>, RepositoryFolder<IFreeplaneResource, ?>> {

		protected CreateFreeplaneMapActionType() {
			super("create_mind_map", FlexoActionFactory.newMenu, FlexoActionFactory.editGroup, FlexoActionFactory.ADD_ACTION_TYPE);
		}

		@Override
		public NewFreeplaneMap makeNewAction(final RepositoryFolder<IFreeplaneResource, ?> folder,
				final Vector<RepositoryFolder<IFreeplaneResource, ?>> maps, final FlexoEditor flexoEditor) {
			return new NewFreeplaneMap(folder, maps, flexoEditor);
		}

		@Override
		public boolean isVisibleForSelection(final RepositoryFolder<IFreeplaneResource, ?> folder,
				final Vector<RepositoryFolder<IFreeplaneResource, ?>> map) {
			return folder.getResourceRepository().getResourceClass().equals(IFreeplaneResource.class);
		}

		@Override
		public boolean isEnabledForSelection(final RepositoryFolder<IFreeplaneResource, ?> folder,
				final Vector<RepositoryFolder<IFreeplaneResource, ?>> map) {
			return true;
		}
	}

	public static final FlexoActionFactory<NewFreeplaneMap, RepositoryFolder<IFreeplaneResource, ?>, RepositoryFolder<IFreeplaneResource, ?>> ACTION_TYPE = new CreateFreeplaneMapActionType();

	private static final Logger LOGGER = Logger.getLogger(NewFreeplaneMap.class.getSimpleName());

	/**
	 * Property for fibs
	 */
	private String mapName;

	public void setMapName(final String mapName) {
		this.mapName = mapName;
	}

	public String getMapName() {
		return mapName;
	}

	private NewFreeplaneMap(final RepositoryFolder<IFreeplaneResource, ?> focusedObject,
			final Vector<RepositoryFolder<IFreeplaneResource, ?>> globalSelection, final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
	}

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, RepositoryFolder.class);
	}

	@Override
	protected void doAction(final Object objet) throws FlexoException {

		FreeplaneTechnologyAdapter freeplaneTA = getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(FreeplaneTechnologyAdapter.class);

		FlexoResourceCenter<?> rc = getFocusedObject().getResourceRepository().getResourceCenter();
		FreeplaneResourceFactory factory = freeplaneTA.getFreeplaneResourceFactory();

		String artefactName = getMapName().endsWith(FreeplaneResourceFactory.FREEPLANE_FILE_EXTENSION) ? getMapName()
				: getMapName() + FreeplaneResourceFactory.FREEPLANE_FILE_EXTENSION;

		Object serializationArtefact = ((FlexoResourceCenter) rc).createEntry(artefactName, getFocusedObject().getSerializationArtefact());

		IFreeplaneResource newFreeplaneResource;
		try {
			newFreeplaneResource = freeplaneTA.getFreeplaneResourceFactory().makeResource(serializationArtefact, (FlexoResourceCenter) rc,
					freeplaneTA.getTechnologyContextManager(), true);
		} catch (SaveResourceException e) {
			e.printStackTrace();
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
	}
}
