/*
 * (c) Copyright 2013-2014 Openflexo
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
package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.openflexo.antar.binding.BindingValueChangeListener;
import org.openflexo.antar.expr.NotSettableContextException;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fmlrt.FlexoConceptInstance;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementSpecification;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

/**
 * Represents a {@link DiagramElement} seen in federated context<br>
 * Instead of just referencing the {@link DiagramElement}, we address it from a {@link FlexoConceptInstance} and a
 * {@link GraphicalElementRole} (which references the {@link DiagramElement}).
 * 
 * 
 * @author sylvain
 * 
 */
@ModelEntity(isAbstract = true)
@ImplementationClass(FMLControlledDiagramElement.FMLControlledDiagramElementImpl.class)
public interface FMLControlledDiagramElement<E extends DiagramElement<GR>, GR extends GraphicalRepresentation> extends FlexoObject {

	public static final String DRAWING_KEY = "drawing";
	public static final String FLEXO_CONCEPT_INSTANCE_KEY = "flexoConceptInstance";
	public static final String DIAGRAM_ELEMENT_KEY = "diagramElement";
	public static final String ROLE_KEY = "role";

	/**
	 * Return the {@link FMLControlledDiagramDrawing} where this {@link FMLControlledDiagramElement} is referenced
	 * 
	 * @return
	 */
	@Getter(value = DRAWING_KEY, ignoreType = true)
	public FMLControlledDiagramDrawing getDrawing();

	/**
	 * Sets the {@link FMLControlledDiagramDrawing} where this {@link FMLControlledDiagramElement} is referenced
	 * 
	 * @param aFlexoConceptInstance
	 */
	@Setter(DRAWING_KEY)
	public void setDrawing(FMLControlledDiagramDrawing aDrawing);

	/**
	 * Return the {@link FlexoConceptInstance} where {@link DiagramElement} is referenced
	 * 
	 * @return
	 */
	@Getter(FLEXO_CONCEPT_INSTANCE_KEY)
	public FlexoConceptInstance getFlexoConceptInstance();

	/**
	 * Sets the {@link FlexoConceptInstance} where {@link DiagramElement} is referenced
	 * 
	 * @param aFlexoConceptInstance
	 */
	@Setter(FLEXO_CONCEPT_INSTANCE_KEY)
	public void setFlexoConceptInstance(FlexoConceptInstance aFlexoConceptInstance);

	/**
	 * Return the addressed {@link DiagramElement}
	 * 
	 * @return
	 */
	@Getter(DIAGRAM_ELEMENT_KEY)
	public E getDiagramElement();

	/**
	 * 
	 * @param diagramElement
	 */
	@Setter(DIAGRAM_ELEMENT_KEY)
	public void setDiagramElement(E diagramElement);

	/**
	 * Return the role from which related {@link FlexoConceptInstance} addresses the {@link DiagramElement}
	 * 
	 * @return
	 */
	@Getter(ROLE_KEY)
	public GraphicalElementRole<E, GR> getRole();

	/**
	 * Sets the role from which related {@link FlexoConceptInstance} addresses the {@link DiagramElement}
	 * 
	 * @return
	 */
	@Setter(ROLE_KEY)
	public void setRole(GraphicalElementRole<E, GR> aRole);

	public String getLabel();

	public void setLabel(String aName);

	public static abstract class FMLControlledDiagramElementImpl<E extends DiagramElement<GR>, GR extends GraphicalRepresentation>
			implements FMLControlledDiagramElement<E, GR>, PropertyChangeListener {

		private final Map<GraphicalElementSpecification<?, GR>, BindingValueChangeListener<?>> listeners = new HashMap<GraphicalElementSpecification<?, GR>, BindingValueChangeListener<?>>();

		@Override
		public void setDiagramElement(E diagramElement) {
			if (getDiagramElement() != diagramElement) {
				E oldValue = getDiagramElement();
				if (getDiagramElement() != null) {
					getDiagramElement().getPropertyChangeSupport().removePropertyChangeListener(this);
				}
				performSuperSetter(DIAGRAM_ELEMENT_KEY, diagramElement);
				if (diagramElement != null) {
					diagramElement.getPropertyChangeSupport().addPropertyChangeListener(this);
				}

				if (oldValue == null && diagramElement != null) {
					// We handle here the fact that a FMLControlledDiagramElement was identified and managed
					// But a its creation, GR is null because diagramElement was null
					// Now that we have access to the diagram element, GR could be retrieved
					// But we also need to notify the parent that this diagram element is now to be managed
					if (diagramElement.getParent() != null) {
						diagramElement.getParent().getPropertyChangeSupport()
								.firePropertyChange(DiagramContainerElement.SHAPES, null, diagramElement.getParent().getShapes());
					}
				}

			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(DiagramElement.INVALIDATE)) {
				// We detect here that the shape which is the graphical facet of this FMLControlledDiagramElement
				// changed it's hierarchy. We have here to propagate this event from this FMLControlledDiagramElement.
				// This notification will be caught by the FMLControlledDiagramDrawing, and relevant GRStructureVisitor
				// will be called
				getPropertyChangeSupport().firePropertyChange(DiagramElement.INVALIDATE, null, getDiagramElement());
			}
		}

		@Override
		public void setRole(GraphicalElementRole<E, GR> aRole) {
			if (aRole != getRole()) {
				for (BindingValueChangeListener<?> l : listeners.values()) {
					l.stopObserving();
					l.delete();
				}
				listeners.clear();
				performSuperSetter(ROLE_KEY, aRole);
				if (aRole != null) {
					for (GraphicalElementSpecification<?, GR> grSpec : aRole.getGrSpecifications()) {
						listenToGRSpecification(grSpec);
					}
				}
			}
		}

		private <T> void listenToGRSpecification(final GraphicalElementSpecification<T, GR> grSpec) {
			BindingValueChangeListener<T> l = new BindingValueChangeListener<T>(grSpec.getValue(), getFlexoConceptInstance()) {
				@Override
				public void bindingValueChanged(Object source, T newValue) {
					System.out.println("value changed for " + grSpec + " newValue=" + newValue);
					getPropertyChangeSupport().firePropertyChange(grSpec.getFeatureName(), null, newValue);
				}
			};
			listeners.put(grSpec, l);

		}

		@Override
		public boolean delete(Object... context) {
			for (BindingValueChangeListener<?> l : listeners.values()) {
				l.stopObserving();
				l.delete();
			}
			listeners.clear();
			return performSuperDelete(context);
		}

		@Override
		public boolean undelete(boolean restoreProperties) {
			return performSuperUndelete(restoreProperties);
		}

		// TODO: do it generically for all GRSpecs
		@Override
		public String getLabel() {
			if (getRole() != null && getRole().getLabel() != null) {

				try {

					/*if (getRole().getLabel().toString().equals("company.companyName")) {
						System.out.println("OK, faut que je calcule le label pour " + getFlexoConceptInstance().getStringRepresentation());
						System.out.println("Je tombe sur " + getRole().getLabel().getBindingValue(getFlexoConceptInstance()));
						System.out.println("value=" + getRole().getLabel());*/
					// System.out.println("valid=" + getRole().getLabel().isValid());
					// System.out.println("reason=" + getRole().getLabel().invalidBindingReason());
					// Thread.dumpStack();

					/*if (!getRole().getLabel().isValid()) {
						getRole().getLabel().markedAsToBeReanalized();
						System.out.println("et maintenant pour value=" + getRole().getLabel());
						System.out.println("valid=" + getRole().getLabel().isValid());
						System.out.println("reason=" + getRole().getLabel().invalidBindingReason());
					}*/

					// }

					return getRole().getLabel().getBindingValue(getFlexoConceptInstance());
				} catch (TypeMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullReferenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		// TODO: to it generically for all GRSpecs
		@Override
		public void setLabel(String aLabel) {
			if (getRole().getLabel() != null) {
				try {
					getRole().getLabel().setBindingValue(aLabel, getFlexoConceptInstance());
				} catch (TypeMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullReferenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotSettableContextException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}