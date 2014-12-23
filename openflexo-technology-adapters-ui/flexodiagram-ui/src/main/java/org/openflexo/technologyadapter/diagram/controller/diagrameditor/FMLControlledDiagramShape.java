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

import java.util.ArrayList;
import java.util.List;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
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
	 * Build and return a new list of available LinkScheme where this {@link FMLControlledDiagramShape} may plays the role of starting shape
	 * 
	 * @return a list of {@link LinkScheme}
	 */
	public List<LinkScheme> getAvailableLinkSchemes();

	/**
	 * Build and return a new list of available LinkScheme where this {@link FMLControlledDiagramShape} may plays the role of starting
	 * shape, and where toTarget is compatible with supplied targetFlexoConcept
	 * 
	 * @return a list of {@link LinkScheme}
	 */
	public List<LinkScheme> getAvailableLinkSchemes(FlexoConcept targetFlexoConcept);

	/**
	 * Build and return a new list of available DropAndLinkScheme where this {@link FMLControlledDiagramShape} may plays the role of
	 * starting shape, and where target concept of DropScheme is compatible with supplied targetFlexoConcept
	 * 
	 * @return a list of {@link DropAndLinkScheme}
	 */
	public List<DropAndLinkScheme> getAvailableDropAndLinkSchemes(FlexoConcept targetFlexoConcept);

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
		 * Build and return a new list of available DropAndLinkScheme where this {@link FMLControlledDiagramShape} may plays the role of
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

			List<DropAndLinkScheme> availableDropAndLinkSchemeFromThisShape = new ArrayList<DropAndLinkScheme>();
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
		 * Build and return a new list of available LinkScheme where this {@link FMLControlledDiagramShape} may plays the role of starting
		 * shape
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

			List<LinkScheme> availableLinkSchemeFromThisShape = new ArrayList<LinkScheme>();
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
		 * Build and return a new list of available LinkScheme where this {@link FMLControlledDiagramShape} may plays the role of starting
		 * shape, and where toTarget is compatible with supplied targetFlexoConcept
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

			List<LinkScheme> availableLinkSchemeFromThisShape = new ArrayList<LinkScheme>();
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

	}

}