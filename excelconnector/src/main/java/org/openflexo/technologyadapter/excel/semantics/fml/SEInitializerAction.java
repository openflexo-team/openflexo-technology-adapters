/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.semantics.fml;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.foundation.fml.rt.action.AbstractActionSchemeAction;
import org.openflexo.technologyadapter.excel.semantics.model.SEVirtualModelInstance;

/**
 * Provides execution environment of a {@link SEInitializer} on a given {@link HbnVirtualModelInstance} as a {@link FlexoAction}
 * 
 * @author sylvain
 *
 */
public class SEInitializerAction extends AbstractActionSchemeAction<SEInitializerAction, SEInitializer, SEVirtualModelInstance> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SEInitializerAction.class.getPackage().getName());

	/**
	 * Constructor to be used for creating a new action without factory
	 * 
	 * @param flexoBehaviour
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	public SEInitializerAction(SEInitializer behaviour, SEVirtualModelInstance focusedObject,
			List<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
		super(behaviour, focusedObject, globalSelection, editor);
	}

	/**
	 * Constructor to be used for creating a new action as an action embedded in another one
	 * 
	 * @param flexoBehaviour
	 * @param focusedObject
	 * @param globalSelection
	 * @param ownerAction
	 *            Action in which action to be created will be embedded
	 */
	public SEInitializerAction(SEInitializer behaviour, SEVirtualModelInstance focusedObject,
			List<VirtualModelInstanceObject> globalSelection, FlexoAction<?, ?, ?> ownerAction) {
		super(behaviour, focusedObject, globalSelection, ownerAction);
	}

	@Override
	protected void doAction(Object context) throws FlexoException {
		SEInitializer applicableActionScheme = getApplicableFlexoBehaviour();
		if (applicableActionScheme != null && applicableActionScheme.evaluateCondition(getFlexoConceptInstance())) {
			executeControlGraph();
		}
	}

}
