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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.openflexo.connie.type.CustomTypeFactory;
import org.openflexo.foundation.fml.TechnologyAdapterTypeFactory;
import org.openflexo.foundation.fml.TechnologySpecificType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.toolbox.StringUtils;

public class DiagramType implements TechnologySpecificType<TechnologyAdapter> {

	protected DiagramSpecification diagramSpecification;
	protected String diagramSpecificationURI;
	// factory stored for unresolved types
	// should be nullified as quickly as possible (nullified when resolved)
	protected CustomTypeFactory<?> customTypeFactory;

	private DiagramType(DiagramSpecification diagramSpecification) {
		this.diagramSpecification = diagramSpecification;
	}

	protected DiagramType(String diagramSpecificationURI, CustomTypeFactory<?> customTypeFactory) {
		this.diagramSpecificationURI = diagramSpecificationURI;
		this.customTypeFactory = customTypeFactory;
	}

	public DiagramSpecification getDiagramSpecification() {
		return diagramSpecification;
	}

	@Override
	public Class<Diagram> getBaseClass() {
		return Diagram.class;
	}

	@Override
	public boolean isTypeAssignableFrom(Type aType, boolean permissive) {
		// System.out.println("isTypeAssignableFrom " + aType + " (i am a " + this + ")");
		if (aType instanceof DiagramType) {
			return diagramSpecification == null || diagramSpecification.equals(((DiagramType) aType).getDiagramSpecification());
		}
		return false;
	}

	@Override
	public boolean isOfType(Object object, boolean permissive) {
		if (!(object instanceof Diagram)) {
			return false;
		}
		return getDiagramSpecification() == ((Diagram) object).getDiagramSpecification();
	}

	@Override
	public String simpleRepresentation() {
		return getClass().getSimpleName() + "<"
				+ (diagramSpecification != null ? diagramSpecification.getName() : "NotFound:" + diagramSpecificationURI) + ">";
	}

	@Override
	public String fullQualifiedRepresentation() {
		return getClass().getName() + "<" + getSerializationRepresentation() + ">";
	}

	@Override
	public String toString() {
		return simpleRepresentation();
	}

	@Override
	public TechnologyAdapter getSpecificTechnologyAdapter() {
		return getDiagramSpecification().getTechnologyAdapter();
	}

	private static Map<DiagramSpecification, DiagramType> dsMap = new HashMap<DiagramSpecification, DiagramType>();

	public static DiagramType getDiagramType(DiagramSpecification diagramSpecification) {

		DiagramType returned = dsMap.get(diagramSpecification);
		if (returned == null) {
			returned = new DiagramType(diagramSpecification);
			dsMap.put(diagramSpecification, returned);
		}
		return returned;
	}

	@Override
	public String getSerializationRepresentation() {
		return (diagramSpecification != null ? diagramSpecification.getURI() : diagramSpecificationURI);
	}

	@Override
	public boolean isResolved() {
		return diagramSpecification != null || StringUtils.isEmpty(diagramSpecificationURI);
	}

	@Override
	public void resolve(CustomTypeFactory<?> factory) {
		// System.out.println("******* resolve " + getSerializationRepresentation() + " with " + factory);
		if (factory instanceof DiagramTypeFactory) {
			DiagramSpecificationResource dsSpecResource = null;
			dsSpecResource = (DiagramSpecificationResource) ((DiagramTypeFactory) factory).getTechnologyAdapter()
					.getTechnologyAdapterService().getServiceManager().getResourceManager()
					.getResource(diagramSpecificationURI, DiagramSpecification.class);
			if (dsSpecResource != null) {
				try {
					diagramSpecification = dsSpecResource.getResourceData(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.customTypeFactory = null;
			}
			else {
				this.customTypeFactory = factory;
			}
		}
	}

	/**
	 * Factory for DiagramType instances
	 * 
	 * @author sylvain
	 * 
	 */
	public static class DiagramTypeFactory extends TechnologyAdapterTypeFactory<DiagramType> {

		@Override
		public Class<DiagramType> getCustomType() {
			return DiagramType.class;
		}

		public DiagramTypeFactory(DiagramTechnologyAdapter technologyAdapter) {
			super(technologyAdapter);
		}

		@Override
		public DiagramType makeCustomType(String configuration) {

			DiagramSpecification dsSpec = null;
			DiagramSpecificationResource dsSpecResource = null;

			if (configuration != null) {
				dsSpecResource = (DiagramSpecificationResource) getTechnologyAdapter().getTechnologyAdapterService().getServiceManager()
						.getResourceManager().getResource(configuration, DiagramSpecification.class);
				if (dsSpecResource != null) {
					try {
						dsSpec = dsSpecResource.getResourceData(null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else {
				dsSpec = getDiagramSpecificationType();
			}

			if (dsSpec != null) {
				return getDiagramType(dsSpec);
			}
			else {
				// We don't return UNDEFINED_FLEXO_CONCEPT_INSTANCE_TYPE because we want here a mutable type
				// if FlexoConcept might be resolved later
				return new DiagramType(configuration, this);
			}
		}

		private DiagramSpecification diagramSpecType;

		public DiagramSpecification getDiagramSpecificationType() {
			return diagramSpecType;
		}

		public void setDiagramSpecificationType(DiagramSpecification diagramSpecType) {
			if (diagramSpecType != this.diagramSpecType) {
				DiagramSpecification oldDiagramSpecType = this.diagramSpecType;
				this.diagramSpecType = diagramSpecType;
				getPropertyChangeSupport().firePropertyChange("diagramSpecificationType", oldDiagramSpecType, diagramSpecType);
			}
		}

		@Override
		public String toString() {
			return "Diagram conform to DiagramSpecification";
		}

		@Override
		public void configureFactory(DiagramType type) {
			if (type != null) {
				setDiagramSpecificationType(type.getDiagramSpecification());
			}
		}
	}

}
