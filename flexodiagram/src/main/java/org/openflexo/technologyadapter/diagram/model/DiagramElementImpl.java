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

package org.openflexo.technologyadapter.diagram.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

import org.openflexo.connie.BindingVariable;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.model.factory.ProxyMethodHandler;
import org.openflexo.technologyadapter.diagram.DiagramModelSlot;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.binding.DiagramBehaviourBindingModel;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.toolbox.StringUtils;

public abstract class DiagramElementImpl<G extends GraphicalRepresentation> extends FlexoObjectImpl
		implements DiagramElement<G>, PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(DiagramElementImpl.class.getPackage().getName());

	public DiagramElementImpl() {
	}

	@Override
	public DiagramTechnologyAdapter getTechnologyAdapter() {
		if (getDiagram() != null && getDiagram().getResource() != null) {
			return ((DiagramResource) getDiagram().getResource()).getTechnologyAdapter();
		}
		return null;
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getTechnologyAdapter() != null) {
			return getTechnologyAdapter().getLocales();
		}
		return super.getLocales();
	}

	@Override
	public void setGraphicalRepresentation(G graphicalRepresentation) {
		if (graphicalRepresentation != getGraphicalRepresentation()) {
			if (getGraphicalRepresentation() != null) {
				if (getGraphicalRepresentation().getPropertyChangeSupport() != null) {
					getGraphicalRepresentation().getPropertyChangeSupport().removePropertyChangeListener(this);
				}
			}
			performSuperSetter(GRAPHICAL_REPRESENTATION, graphicalRepresentation);
			if (graphicalRepresentation != null) {
				if (graphicalRepresentation.getPropertyChangeSupport() != null) {
					graphicalRepresentation.getPropertyChangeSupport().addPropertyChangeListener(this);
				}
			}
		}
	}

	@Override
	public boolean delete(Object... context) {
		if (getGraphicalRepresentation() != null) {
			if (getGraphicalRepresentation().getPropertyChangeSupport() != null) {
				getGraphicalRepresentation().getPropertyChangeSupport().removePropertyChangeListener(this);
			}
		}
		return performSuperDelete(context);
	}

	@Override
	public Diagram getResourceData() {
		return getDiagram();
	}

	@Override
	public Diagram getDiagram() {
		if (getParent() != null) {
			return getParent().getDiagram();
		}
		return null;
	}

	@Override
	public boolean isContainedIn(DiagramElement<?> element) {
		if (element == this) {
			return true;
		}
		if (getParent() != null && getParent() == element) {
			return true;
		}
		if (getParent() != null) {
			return getParent().isContainedIn(element);
		}
		return false;
	}

	@Override
	public DiagramElement<G> clone() {
		return (DiagramElement<G>) cloneObject();

	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == getGraphicalRepresentation()) {
			if (getDiagram() != null) {
				getDiagram().setChanged();
			}
		}
	}

	/*@Override
	public void setChanged() {
		super.setChanged();
	}*/

	/*@Override
	public String getName() {
		// System.out.println("On me demande mon nom, je retourne " + performSuperGetter(NAME));
		return (String) performSuperGetter(NAME);
	}*/

	@Override
	public synchronized void setChanged() {
		super.setChanged();
	}

	@Override
	public Object getValue(BindingVariable variable) {
		if (variable.getVariableName().equals(DiagramBehaviourBindingModel.TOP_LEVEL)) {
			return this;
		}
		return null;
	}

	@Override
	public List<DiagramContainerElement<?>> getAncestors() {
		List<DiagramContainerElement<?>> ancestors = new ArrayList<DiagramContainerElement<?>>();
		DiagramContainerElement<?> current = getParent();
		while (current != null) {
			ancestors.add(current);
			current = current.getParent();
		}
		return ancestors;
	}

	public static DiagramContainerElement<?> getFirstCommonAncestor(DiagramContainerElement<?> child1, DiagramContainerElement<?> child2) {
		List<DiagramContainerElement<?>> ancestors1 = child1.getAncestors();
		List<DiagramContainerElement<?>> ancestors2 = child2.getAncestors();
		for (int i = 0; i < ancestors1.size(); i++) {
			DiagramContainerElement<?> o1 = ancestors1.get(i);
			if (ancestors2.contains(o1)) {
				return o1;
			}
		}
		return null;
	}

	/**
	 * Return {@link FlexoConceptInstance} where this {@link DiagramElement} is involved, asserting that this {@link DiagramElement} is
	 * contained in a {@link Diagram} which is the bound diagram of a {@link DiagramModelSlot} declared in {@link VirtualModel} of supplied
	 * {@link VirtualModelInstance}
	 * 
	 * @param vmInstance
	 *            instance of {@link VirtualModel} where is declared a {@link DiagramModelSlot}
	 * @return
	 */
	@Override
	public FlexoConceptInstance getFlexoConceptInstance(VirtualModelInstance vmInstance) {
		ModelSlotInstance<DiagramModelSlot, Diagram> diagramModelSlotInstance = null;
		for (ModelSlotInstance<?, ?> msInstance : vmInstance.getModelSlotInstances()) {
			if (msInstance.getModelSlot() instanceof DiagramModelSlot && msInstance.getAccessedResourceData() == getDiagram()) {
				diagramModelSlotInstance = (ModelSlotInstance<DiagramModelSlot, Diagram>) msInstance;
			}
		}
		if (diagramModelSlotInstance == null) {
			logger.warning("Cannot find DiagramModelSlot instance where related diagram is accessed");
			return null;
		}
		// TODO: optimize this, use FlexoObjectReference<FlexoConceptInstance> in FlexoObject
		for (FlexoConceptInstance fci : vmInstance.getFlexoConceptInstances()) {
			if (fci.getPropertyForActor(this) != null) {
				return fci;
			}
		}
		return null;
	}

	/**
	 * Return {@link GraphicalElementRole} played by this {@link DiagramElement} in related {@link FlexoConceptInstance}, asserting that
	 * this {@link DiagramElement} is contained in a {@link Diagram} which is the bound diagram of a {@link DiagramModelSlot} declared in
	 * {@link VirtualModel} of supplied {@link VirtualModelInstance}
	 * 
	 * @param vmInstance
	 *            : instance of {@link VirtualModel} where is declared a {@link DiagramModelSlot}
	 * @return
	 */
	@Override
	public GraphicalElementRole<?, ?> getPatternRole(VirtualModelInstance vmInstance) {
		FlexoConceptInstance epi = getFlexoConceptInstance(vmInstance);
		if (epi != null) {
			return (GraphicalElementRole<?, ?>) epi.getPropertyForActor(this);
		}
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() != null) {
			if (evt.getPropertyName().equals(ProxyMethodHandler.MODIFIED)) {
				setModified(true);
			}
		}
	}

	@Override
	public String getIdentifier() {
		String returned = getName();
		if (StringUtils.isEmpty(returned)) {
			return getDefaultName();
		}
		return returned;
	}

	protected abstract String getDefaultName();
}
