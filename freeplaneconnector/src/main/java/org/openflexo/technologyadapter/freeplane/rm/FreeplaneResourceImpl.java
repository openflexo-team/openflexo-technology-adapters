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

package org.openflexo.technologyadapter.freeplane.rm;

import java.io.File;
import java.util.logging.Logger;

import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.mindmapmode.MModeController;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneMapImpl;
import org.openflexo.toolbox.IProgress;

/**
 * Freeplane resource management
 * 
 * @author eloubout
 * 
 */
public abstract class FreeplaneResourceImpl extends FlexoResourceImpl<IFreeplaneMap> implements IFreeplaneResource {

	private static final Logger LOGGER = Logger.getLogger(FreeplaneResourceImpl.class.getPackage().getName());

	@Override
	public FreeplaneTechnologyAdapter getTechnologyAdapter() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(FreeplaneTechnologyAdapter.class);
		}
		return null;
	}

	@Override
	public IFreeplaneMap loadResourceData(final IProgress progress) throws FlexoException {

		if (getFileFlexoIODelegate() == null) {
			throw new FlexoException("Cannot load FreePlane document with this IO/delegate: " + getFlexoIODelegate());
		}

		final FreeplaneMapImpl map = (FreeplaneMapImpl) FreeplaneResourceFactory.MODEL_FACTORY.newInstance(IFreeplaneMap.class);
		map.setTechnologyAdapter(getTechnologyAdapter());
		map.setMapModel(FreeplaneBasicAdapter.getInstance().loadMapFromFile(getFile()));
		map.setResource(this);

		return map;
	}

	@Override
	public Class<IFreeplaneMap> getResourceDataClass() {
		return IFreeplaneMap.class;
	}

	/**
	 * Save modification. A unique call to Freeplane save is done. A boolean could be return, but we stay correct to TA API.<br>
	 * Freeplane save only on last opened map it seems. TODO investigate
	 */
	@Override
	public void save(final IProgress progress) {
		if (progress != null) {
			((MModeController) Controller.getCurrentModeController()).save();
			this.resourceData.clearIsModified(false);
			notifyResourceSaved();
		}
	}

	@Override
	public FileFlexoIODelegate getFileFlexoIODelegate() {
		if (getFlexoIODelegate() instanceof FileFlexoIODelegate) {
			return (FileFlexoIODelegate) getFlexoIODelegate();
		}
		return null;
	}

	private File getFile() {
		if (getFileFlexoIODelegate() != null) {
			return getFileFlexoIODelegate().getFile();
		}
		return null;
	}
}
