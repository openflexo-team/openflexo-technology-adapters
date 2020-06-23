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

package org.openflexo.technologyadapter.gina.model;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.PamelaResourceModelFactory;
import org.openflexo.foundation.action.FlexoUndoManager;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.PamelaResourceImpl.IgnoreLoadingEdits;
import org.openflexo.gina.model.FIBModelFactory;
import org.openflexo.gina.model.container.FIBPanel;
import org.openflexo.pamela.converter.RelativePathFileConverter;
import org.openflexo.pamela.converter.RelativePathResourceConverter;
import org.openflexo.pamela.converter.TypeConverter;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.EditingContext;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;

/**
 * A factory dealing with GINAFIBComponent <br>
 * 
 * One instance of this class should be used for each GINAFIBComponentResource
 * 
 * @author sylvain
 * 
 */
public class GINAFactory extends FIBModelFactory implements PamelaResourceModelFactory<GINAFIBComponentResource> {

	private static final Logger logger = Logger.getLogger(GINAFactory.class.getPackage().getName());

	private final GINAFIBComponentResource resource;
	private IgnoreLoadingEdits ignoreHandler = null;
	private FlexoUndoManager undoManager = null;

	private TypeConverter typeConverter;

	private RelativePathResourceConverter relativePathResourceConverter;

	public GINAFactory(GINAFIBComponentResource resource, FlexoServiceManager serviceManager) throws ModelDefinitionException {
		super(serviceManager.getTechnologyAdapterService(), GINAFIBComponent.class);

		addConverter(typeConverter = new TypeConverter(serviceManager.getTechnologyAdapterService().getCustomTypeFactories()));
		this.resource = resource;
		if (resource != null) {
			if (resource.getIODelegate() instanceof FileIODelegate) {
				addConverter(new RelativePathFileConverter(((FileIODelegate) resource.getIODelegate()).getFile().getParentFile()));
			}
			setEditingContext(serviceManager.getEditingContext());
			addConverter(relativePathResourceConverter = new RelativePathResourceConverter(null));
			if (resource.getIODelegate() != null && resource.getIODelegate().getSerializationArtefactAsResource() != null) {
				relativePathResourceConverter
						.setContainerResource(resource.getIODelegate().getSerializationArtefactAsResource().getContainer());
			}
		}
	}

	@Override
	public GINAFIBComponentResource getResource() {
		return resource;
	}

	public GINAFIBComponent makeNewGINAFIBComponent() {
		GINAFIBComponent returned = newInstance(GINAFIBComponent.class);
		FIBPanel panel = newFIBPanel();
		returned.setComponent(panel);
		return returned;
	}

	@Override
	public synchronized void startDeserializing() {

		if (typeConverter != null) {
			typeConverter.startDeserializing();
		}

		EditingContext editingContext = getResource().getServiceManager().getEditingContext();

		if (editingContext != null && editingContext.getUndoManager() instanceof FlexoUndoManager) {
			undoManager = (FlexoUndoManager) editingContext.getUndoManager();
			undoManager.addToIgnoreHandlers(ignoreHandler = new IgnoreLoadingEdits(resource));
			// System.out.println("@@@@@@@@@@@@@@@@ START LOADING RESOURCE " + resource.getURI());
		}

	}

	@Override
	public synchronized void stopDeserializing() {

		if (typeConverter != null) {
			typeConverter.stopDeserializing();
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
			if (getResource() != null) {
				getResource().setLastID(((FlexoObject) newlyCreatedObject).getFlexoID());
			}
			else {
				logger.warning("Could not access resource beeing deserialized");
			}
		}
	}

}
