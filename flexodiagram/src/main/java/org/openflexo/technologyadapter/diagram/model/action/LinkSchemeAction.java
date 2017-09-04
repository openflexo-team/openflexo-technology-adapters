/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.technologyadapter.diagram.model.action;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.BindingVariable;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.binding.LinkSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * Represents an instance of a fired {@link LinkScheme} action
 * 
 * @author sylvain
 * 
 */
public class LinkSchemeAction extends DiagramFlexoBehaviourAction<LinkSchemeAction, LinkScheme, FMLRTVirtualModelInstance> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(LinkSchemeAction.class.getPackage().getName());

	private DiagramShape _fromShape;
	private DiagramShape _toShape;
	private DiagramConnector _newConnector;

	/**
	 * Constructor to be used for creating a new action without factory
	 * 
	 * @param linkScheme
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	public LinkSchemeAction(LinkScheme linkScheme, FMLRTVirtualModelInstance focusedObject,
			Vector<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
		super(linkScheme, focusedObject, globalSelection, editor);
	}

	/**
	 * Constructor to be used for creating a new action as an action embedded in another one
	 * 
	 * @param linkScheme
	 * @param focusedObject
	 * @param globalSelection
	 * @param ownerAction
	 */
	public LinkSchemeAction(LinkScheme linkScheme, FMLRTVirtualModelInstance focusedObject,
			Vector<VirtualModelInstanceObject> globalSelection, FlexoAction<?, ?, ?> ownerAction) {
		super(linkScheme, focusedObject, globalSelection, ownerAction);
	}

	public DiagramConnector getNewConnector() {
		return _newConnector;
	}

	public DiagramShape getFromShape() {
		return _fromShape;
	}

	public void setFromShape(DiagramShape fromShape) {
		_fromShape = fromShape;
	}

	public DiagramShape getToShape() {
		return _toShape;
	}

	public void setToShape(DiagramShape toShape) {
		_toShape = toShape;
	}

	@Override
	public Diagram getDiagram() {
		if (getFromShape() != null) {
			return getFromShape().getDiagram();
		}
		if (getToShape() != null) {
			return getToShape().getDiagram();
		}
		return null;
	}

	@Override
	public <T> void hasPerformedAction(EditionAction anAction, T object) {
		super.hasPerformedAction(anAction, object);
		if (anAction instanceof AddConnector) {
			// AddConnector action = (AddConnector) anAction;
			_newConnector = (DiagramConnector) object;
		}
	}

	@Override
	public Object getValue(BindingVariable variable) {
		if (variable.getVariableName().equals(LinkSchemeBindingModel.FROM_TARGET)
				&& getFlexoBehaviour().getFromTargetFlexoConcept() != null) {
			List<FlexoConceptInstance> fcis = getVirtualModelInstance().getFlexoConceptInstances();
			for (FlexoConceptInstance fci : fcis) {
				for (ActorReference<?> actor : fci.getActors()) {
					Object me = actor.getModellingElement();
					if (me != null && me.equals(getFromShape()))
						return fci;
				}
			}

		}
		if (variable.getVariableName().equals(LinkSchemeBindingModel.TO_TARGET) && getFlexoBehaviour().getToTargetFlexoConcept() != null) {
			List<FlexoConceptInstance> fcis = getVirtualModelInstance().getFlexoConceptInstances();
			for (FlexoConceptInstance fci : fcis) {
				for (ActorReference<?> actor : fci.getActors()) {
					Object me = actor.getModellingElement();
					if (me != null && me.equals(getToShape()))
						return fci;
				}
			}
		}
		return super.getValue(variable);
	}

}
