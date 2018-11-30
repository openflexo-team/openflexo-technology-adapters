/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.openflexo.diana.Drawing.ShapeNode;
import org.openflexo.diana.ShapeGraphicalRepresentation;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * Represents a {@link DiagramShape} seen in federated context<br>
 * Instead of just referencing the {@link DiagramShape}, we address it from a {@link FlexoConceptInstance} and a {@link ShapeRole} (which
 * references the {@link DiagramShape}).
 * 
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(FMLControlledDiagramShape.FMLControlledDiagramShapeImpl.class)
public interface FMLControlledDiagramShape extends FMLControlledDiagramElement<DiagramShape, ShapeGraphicalRepresentation> {

	/**
	 * Build and return a new list of available LinkScheme where this {@link FMLControlledDiagramShape} may plays the property of starting
	 * shape
	 * 
	 * @return a list of {@link LinkScheme}
	 */
	public List<LinkScheme> getAvailableLinkSchemes();

	/**
	 * Build and return a new list of available LinkScheme where this {@link FMLControlledDiagramShape} may plays the property of starting
	 * shape, and where toTarget is compatible with supplied targetFlexoConcept
	 * 
	 * @return a list of {@link LinkScheme}
	 */
	public List<LinkScheme> getAvailableLinkSchemes(FlexoConcept targetFlexoConcept);

	/**
	 * Build and return a new list of available DropAndLinkScheme where this {@link FMLControlledDiagramShape} may plays the property of
	 * starting shape, and where target concept of DropScheme is compatible with supplied targetFlexoConcept
	 * 
	 * @return a list of {@link DropAndLinkScheme}
	 */
	public List<DropAndLinkScheme> getAvailableDropAndLinkSchemes(FlexoConcept targetFlexoConcept);

	/**
	 * Called to listen notification that may change floating palette of related shape<br>
	 * Basically we listen to the right VirtualModel for a new or deleted FlexoConcept
	 */
	public void listenFloatingPaletteChanges();

	/**
	 * Represents the concatenation of a DropScheme and a LinkScheme, together compatible
	 * 
	 * @author sylvain
	 *
	 */
	public static class DropAndLinkScheme {
		public DropAndLinkScheme(DropScheme dropScheme, LinkScheme linkScheme) {
			super();
			this.dropScheme = dropScheme;
			this.linkScheme = linkScheme;
		}

		public DropScheme dropScheme;
		public LinkScheme linkScheme;

	}

	public abstract class FMLControlledDiagramShapeImpl extends FMLControlledDiagramElementImpl<DiagramShape, ShapeGraphicalRepresentation>
			implements FMLControlledDiagramShape {

		/**
		 * Build and return a new list of available DropAndLinkScheme where this {@link FMLControlledDiagramShape} may plays the property of
		 * starting shape, and where target concept of DropScheme is compatible with supplied targetFlexoConcept
		 * 
		 * @return a list of {@link DropAndLinkScheme}
		 */
		@Override
		public List<DropAndLinkScheme> getAvailableDropAndLinkSchemes(FlexoConcept targetFlexoConcept) {
			if (getFlexoConceptInstance() == null) {
				return null;
			}
			if (getFlexoConceptInstance().getFlexoConcept() == null) {
				return null;
			}

			List<DropAndLinkScheme> availableDropAndLinkSchemeFromThisShape = new ArrayList<>();
			VirtualModel virtualModel = getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel();

			for (FlexoConcept c : virtualModel.getFlexoConcepts()) {
				for (DropScheme ds : c.getFlexoBehaviours(DropScheme.class)) {
					if (ds.getTargetFlexoConcept() == targetFlexoConcept || (ds.getTopTarget() && targetFlexoConcept == null)) {
						for (FlexoConcept c2 : virtualModel.getFlexoConcepts()) {
							for (LinkScheme ls : c2.getFlexoBehaviours(LinkScheme.class)) {
								if (ls.isValidTarget(getFlexoConceptInstance().getFlexoConcept(), ds.getFlexoConcept())
										&& ls.getIsAvailableWithFloatingPalette()) {
									// This candidate is acceptable
									availableDropAndLinkSchemeFromThisShape.add(new DropAndLinkScheme(ds, ls));
								}
							}
						}
					}
				}
			}
			return availableDropAndLinkSchemeFromThisShape;
		}

		/**
		 * Build and return a new list of available LinkScheme where this {@link FMLControlledDiagramShape} may plays the property of
		 * starting shape
		 * 
		 * @return a list of {@link LinkScheme}
		 */
		@Override
		public List<LinkScheme> getAvailableLinkSchemes() {
			if (getFlexoConceptInstance() == null) {
				return null;
			}
			if (getFlexoConceptInstance().getFlexoConcept() == null) {
				return null;
			}

			List<LinkScheme> availableLinkSchemeFromThisShape = new ArrayList<>();
			VirtualModel virtualModel = getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel();

			for (FlexoConcept c : virtualModel.getFlexoConcepts()) {
				for (LinkScheme ls : c.getFlexoBehaviours(LinkScheme.class)) {
					for (FlexoConcept c2 : virtualModel.getFlexoConcepts()) {
						if (ls.isValidTarget(getFlexoConceptInstance().getFlexoConcept(), c2) && ls.getIsAvailableWithFloatingPalette()) {
							// This candidate is acceptable
							availableLinkSchemeFromThisShape.add(ls);
							break;
						}
					}
				}
			}
			return availableLinkSchemeFromThisShape;
		}

		/**
		 * Build and return a new list of available LinkScheme where this {@link FMLControlledDiagramShape} may plays the property of
		 * starting shape, and where toTarget is compatible with supplied targetFlexoConcept
		 * 
		 * @return a list of {@link LinkScheme}
		 */
		@Override
		public List<LinkScheme> getAvailableLinkSchemes(FlexoConcept targetFlexoConcept) {
			if (getFlexoConceptInstance() == null) {
				return null;
			}
			if (getFlexoConceptInstance().getFlexoConcept() == null) {
				return null;
			}

			List<LinkScheme> availableLinkSchemeFromThisShape = new ArrayList<>();
			VirtualModel virtualModel = getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel();

			for (FlexoConcept c : virtualModel.getFlexoConcepts()) {
				for (LinkScheme ls : c.getFlexoBehaviours(LinkScheme.class)) {
					if (ls.isValidTarget(getFlexoConceptInstance().getFlexoConcept(), targetFlexoConcept)
							&& ls.getIsAvailableWithFloatingPalette()) {
						// This candidate is acceptable
						availableLinkSchemeFromThisShape.add(ls);
					}
				}
			}
			return availableLinkSchemeFromThisShape;
		}

		/**
		 * Called to listen notification that may change floating palette of related shape<br>
		 * Basically we listen to the right VirtualModel for a new or deleted FlexoConcept
		 */
		@Override
		public void listenFloatingPaletteChanges() {
			if (getFlexoConceptInstance() != null && getFlexoConceptInstance().getVirtualModelInstance() != null
					&& getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel() != null) {
				getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel().getPropertyChangeSupport()
						.addPropertyChangeListener(this);
			}
		}

		@Override
		public boolean delete(Object... context) {
			if (getFlexoConceptInstance() != null && getFlexoConceptInstance().getVirtualModelInstance() != null
					&& getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel() != null) {
				getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel().getPropertyChangeSupport()
						.removePropertyChangeListener(this);
			}
			return super.delete(context);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getFlexoConceptInstance() != null && getFlexoConceptInstance().getVirtualModelInstance() != null
					&& getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel() != null) {
				if (evt.getSource() == getFlexoConceptInstance().getVirtualModelInstance().getVirtualModel()) {
					if (evt.getPropertyName().equals(VirtualModel.FLEXO_CONCEPTS_KEY)) {
						ShapeNode<FMLControlledDiagramShapeImpl> shapeNode = getDrawing().getShapeNode(this);
						if (shapeNode != null) {
							shapeNode.clearControlAreas();
						}
					}
				}
			}
			super.propertyChange(evt);
		}

	}

}
