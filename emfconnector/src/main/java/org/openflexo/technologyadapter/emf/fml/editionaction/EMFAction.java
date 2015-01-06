/*
 * (c) Copyright 2010-2011 AgileBirds
 * (c) Copyright 2012-2013 Openflexo
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
package org.openflexo.technologyadapter.emf.fml.editionaction;

import org.openflexo.foundation.fml.editionaction.TechnologySpecificAction;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.model.AEMFModelObjectImpl;

/**
 * Abstract action for {@link EMFModelSlot}
 * 
 * @author sylvain
 * 
 * @param <T>
 *            emf object type
 */

@ModelEntity(isAbstract = true)
public interface EMFAction<T extends AEMFModelObjectImpl<?>> extends TechnologySpecificAction<EMFModelSlot, T> {

}
