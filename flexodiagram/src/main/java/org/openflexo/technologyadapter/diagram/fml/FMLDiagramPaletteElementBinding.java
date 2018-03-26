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

package org.openflexo.technologyadapter.diagram.fml;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.BindingModel;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.control.PaletteElement;
import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoBehaviourParameter;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelObject;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Finder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;

/**
 * Encodes the binding between {@link DiagramPaletteElement} (part of {@link DiagramSpecification}) and the current {@link VirtualModel}<br>
 * 
 * The goal is for example to associate a {@link PaletteElement} to a given {@link DropScheme} of a particular {@link FlexoConcept}
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(FMLDiagramPaletteElementBinding.FMLDiagramPaletteElementBindingImpl.class)
@XMLElement
public interface FMLDiagramPaletteElementBinding extends VirtualModelObject {

	@PropertyIdentifier(type = TypedDiagramModelSlot.class)
	public static final String DIAGRAM_MODEL_SLOT_KEY = "diagramModelSlot";
	@PropertyIdentifier(type = DiagramPaletteElement.class)
	public static final String PALETTE_ELEMENT_KEY = "paletteElement";
	@PropertyIdentifier(type = String.class)
	public static final String PALETTE_ELEMENT_ID_KEY = "paletteElementId";
	@PropertyIdentifier(type = DropScheme.class)
	public static final String DROP_SCHEME_KEY = "dropScheme";
	@PropertyIdentifier(type = List.class)
	public static final String PARAMETERS_KEY = "parameters";
	@PropertyIdentifier(type = List.class)
	public static final String OVERRIDING_GRAPHICAL_REPRESENTATIONS_KEY = "overridingGraphicalRepresentations";

	@Getter(value = DIAGRAM_MODEL_SLOT_KEY, inverse = TypedDiagramModelSlot.PALETTE_ELEMENTS_BINDING_KEY)
	public TypedDiagramModelSlot getDiagramModelSlot();

	@Setter(DIAGRAM_MODEL_SLOT_KEY)
	public void setDiagramModelSlot(TypedDiagramModelSlot diagramModelSlot);

	@Getter(value = PALETTE_ELEMENT_KEY)
	public DiagramPaletteElement getPaletteElement();

	@Setter(PALETTE_ELEMENT_KEY)
	public void setPaletteElement(DiagramPaletteElement aPaletteElement);

	@Getter(value = PALETTE_ELEMENT_ID_KEY)
	@XMLAttribute
	public String getPaletteElementId();

	@Setter(PALETTE_ELEMENT_ID_KEY)
	public void setPaletteElementId(String aPaletteElementId);

	@Getter(value = DROP_SCHEME_KEY)
	@XMLElement(primary = false)
	public DropScheme getDropScheme();

	@Setter(DROP_SCHEME_KEY)
	public void setDropScheme(DropScheme aDropScheme);

	@Getter(
			value = PARAMETERS_KEY,
			cardinality = Cardinality.LIST,
			inverse = FMLDiagramPaletteElementBindingParameter.PALETTE_ELEMENT_BINDING_KEY)
	@XMLElement
	public List<FMLDiagramPaletteElementBindingParameter> getParameters();

	@Setter(PARAMETERS_KEY)
	public void setParameters(List<FMLDiagramPaletteElementBindingParameter> parameters);

	@Adder(PARAMETERS_KEY)
	public void addToParameters(FMLDiagramPaletteElementBindingParameter aParameter);

	@Remover(PARAMETERS_KEY)
	public void removeFromParameters(FMLDiagramPaletteElementBindingParameter aParameter);

	@Finder(collection = PARAMETERS_KEY, attribute = FMLDiagramPaletteElementBindingParameter.NAME_KEY)
	public FMLDiagramPaletteElementBindingParameter getParameter(String parameterName);

	@Getter(
			value = OVERRIDING_GRAPHICAL_REPRESENTATIONS_KEY,
			cardinality = Cardinality.LIST,
			inverse = OverridingGraphicalRepresentation.PALETTE_ELEMENT_BINDING_KEY)
	@XMLElement
	public List<OverridingGraphicalRepresentation<?>> getOverridingGraphicalRepresentations();

	@Setter(OVERRIDING_GRAPHICAL_REPRESENTATIONS_KEY)
	public void setOverridingGraphicalRepresentations(List<OverridingGraphicalRepresentation<?>> overridingGraphicalRepresentations);

	@Adder(OVERRIDING_GRAPHICAL_REPRESENTATIONS_KEY)
	public void addToOverridingGraphicalRepresentations(OverridingGraphicalRepresentation<?> anOverridingGraphicalRepresentation);

	@Remover(OVERRIDING_GRAPHICAL_REPRESENTATIONS_KEY)
	public void removeFromOverridingGraphicalRepresentations(OverridingGraphicalRepresentation<?> anOverridingGraphicalRepresentation);

	public GraphicalRepresentation getOverridingGraphicalRepresentation(GraphicalElementRole<?, ?> patternRole);

	public FlexoConcept getBoundFlexoConcept();

	public void setBoundFlexoConcept(FlexoConcept anFlexoConcept);

	public boolean getBoundLabelToElementName();

	public void setBoundLabelToElementName(boolean boundLabelToElementName);

	public List<FlexoConcept> allAvailableFlexoConcepts();

	public List<DropScheme> allAvailableDropSchemes();

	public DiagramTechnologyAdapter getDiagramTechnologyAdapter();

	public abstract class FMLDiagramPaletteElementBindingImpl extends FlexoConceptObjectImpl implements FMLDiagramPaletteElementBinding {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(FMLDiagramPaletteElementBinding.class.getPackage().getName());

		/**
		 * The addressed palette element we want to bind to something in {@link VirtualModel}
		 */
		private DiagramPaletteElement paletteElement = null;

		private TypedDiagramModelSlot diagramModelSlot;

		private String _boundFlexoConceptId;
		private String _dropSchemeName;

		private FlexoConcept boundFlexoConcept;
		private DropScheme dropScheme;
		// private Vector<FMLDiagramPaletteElementBindingParameter> parameters;
		private String patternRoleName;

		private boolean boundLabelToElementName = true;

		// Represent graphical representation to be used as overriding representation
		// private final Vector<OverridingGraphicalRepresentation> overridingGraphicalRepresentations;

		// private Vector<DiagramPaletteElement> childs;

		public FMLDiagramPaletteElementBindingImpl() {
			super();
			// overridingGraphicalRepresentations = new Vector<OverridingGraphicalRepresentation>();
			// parameters = new Vector<FMLDiagramPaletteElementBindingParameter>();
		}

		@Override
		public String getURI() {
			return null;
		}

		@Override
		public FlexoConcept getFlexoConcept() {
			if (getDiagramModelSlot() != null) {
				return getDiagramModelSlot().getFlexoConcept();
			}
			return null;
		}

		@Override
		public VirtualModel getVirtualModel() {
			if (getDiagramModelSlot() != null) {
				return getDiagramModelSlot().getVirtualModel();
			}
			return null;
		}

		@Override
		public TypedDiagramModelSlot getDiagramModelSlot() {
			if (diagramModelSlot == null && dropScheme != null) {
				VirtualModel vm = dropScheme.getOwningVirtualModel();
				if (vm != null && vm.getModelSlots(TypedDiagramModelSlot.class).size() > 0) {
					diagramModelSlot = vm.getModelSlots(TypedDiagramModelSlot.class).get(0);
				}
			}
			return diagramModelSlot;
		}

		@Override
		public void setDiagramModelSlot(TypedDiagramModelSlot diagramModelSlot) {
			this.diagramModelSlot = diagramModelSlot;
		}

		// Deserialization only, do not use
		public String _getBoundFlexoConceptId() {
			if (getBoundFlexoConcept() != null) {
				return getBoundFlexoConcept().getName();
			}
			return _boundFlexoConceptId;
		}

		// Deserialization only, do not use
		public void _setBoundFlexoConceptId(String flexoConceptId) {
			_boundFlexoConceptId = flexoConceptId;
		}

		// Deserialization only, do not use
		public String _getDropSchemeName() {
			if (getDropScheme() != null) {
				return getDropScheme().getName();
			}
			return _dropSchemeName;
		}

		// Deserialization only, do not use
		public void _setDropSchemeName(String _dropSchemeName) {
			this._dropSchemeName = _dropSchemeName;
		}

		@Override
		public DiagramPaletteElement getPaletteElement() {
			decodePaletteElementId();
			return paletteElement;
		}

		@Override
		public void setPaletteElement(DiagramPaletteElement paletteElement) {
			this.paletteElement = paletteElement;
		}

		private String paletteElementId = null;

		@Override
		public String getPaletteElementId() {
			if (getPaletteElement() != null) {
				return getPaletteElement().getPalette().getURI() + "#" + getPaletteElement().getName();
			}
			return paletteElementId;
		}

		@Override
		public void setPaletteElementId(String aPaletteElementId) {
			this.paletteElementId = aPaletteElementId;
			decodePaletteElementId();
		}

		private void decodePaletteElementId() {
			if (paletteElementId != null) {
			}
			if (paletteElement == null && getDiagramModelSlot() != null && paletteElementId != null && paletteElementId.indexOf("#") > 0) {
				String paletteURI = paletteElementId.substring(0, paletteElementId.indexOf("#"));
				String elementName = paletteElementId.substring(paletteElementId.indexOf("#") + 1);
				if (getDiagramModelSlot().getMetaModelResource() != null) {
					DiagramSpecification ds = getDiagramModelSlot().getMetaModelResource().getMetaModelData();
					if (ds != null) {
						DiagramPalette palette = ds.getPalette(paletteURI);
						if (palette != null) {
							paletteElement = palette.getPaletteElement(elementName);
						}
					}
				}
			}
		}

		@Override
		public FlexoConcept getBoundFlexoConcept() {
			if (boundFlexoConcept != null) {
				return boundFlexoConcept;
			}
			if (_boundFlexoConceptId != null && getOwningVirtualModel() != null) {
				boundFlexoConcept = getOwningVirtualModel().getFlexoConcept(_boundFlexoConceptId);
				updateParameters();
			}
			if (boundFlexoConcept == null && dropScheme != null) {
				boundFlexoConcept = dropScheme.getFlexoConcept();
			}
			return boundFlexoConcept;
		}

		@Override
		public void setBoundFlexoConcept(FlexoConcept anFlexoConcept) {
			if (anFlexoConcept != boundFlexoConcept) {
				boundFlexoConcept = anFlexoConcept;
				updateParameters();
			}
		}

		@Override
		public DropScheme getDropScheme() {
			if (dropScheme != null) {
				return dropScheme;
			}
			if (_dropSchemeName != null && getBoundFlexoConcept() != null
					&& getBoundFlexoConcept().getFlexoBehaviour(_dropSchemeName) instanceof DropScheme) {
				dropScheme = (DropScheme) getBoundFlexoConcept().getFlexoBehaviour(_dropSchemeName);
				updateParameters();
			}
			if (dropScheme == null && getBoundFlexoConcept() != null
					&& getBoundFlexoConcept().getFlexoBehaviours(DropScheme.class).size() > 0) {
				dropScheme = getBoundFlexoConcept().getFlexoBehaviours(DropScheme.class).get(0);
			}
			return dropScheme;
		}

		@Override
		public void setDropScheme(DropScheme aDropScheme) {
			if (dropScheme != aDropScheme) {
				dropScheme = aDropScheme;
				updateParameters();
			}
		}

		/*public Vector<FMLDiagramPaletteElementBindingParameter> getParameters() {
			return parameters;
		}
		
		public void setParameters(Vector<FMLDiagramPaletteElementBindingParameter> parameters) {
			this.parameters = parameters;
		}
		
		public void addToParameters(FMLDiagramPaletteElementBindingParameter parameter) {
			parameter.setElementBinding(this);
			parameters.add(parameter);
		}
		
		public void removeFromParameters(FMLDiagramPaletteElementBindingParameter parameter) {
			parameter.setElementBinding(null);
			parameters.remove(parameter);
		}
		
		public FMLDiagramPaletteElementBindingParameter getParameter(String name) {
			for (FMLDiagramPaletteElementBindingParameter p : parameters) {
				if (p.getName().equals(name)) {
					return p;
				}
			}
			return null;
		}*/

		private void updateParameters() {
			if (boundFlexoConcept == null) {
				return;
			}
			List<FMLDiagramPaletteElementBindingParameter> unusedParameterInstances = new ArrayList<>();
			unusedParameterInstances.addAll(getParameters());

			for (FlexoBehaviour es : boundFlexoConcept.getFlexoBehaviours()) {
				for (FlexoBehaviourParameter parameter : es.getParameters()) {
					FMLDiagramPaletteElementBindingParameter parameterInstance = getParameter(parameter.getName());
					if (parameterInstance != null) {
						unusedParameterInstances.remove(parameterInstance);
						parameterInstance.setParameter(parameter);
					}
					else if (getOwningVirtualModel() != null && getFMLModelFactory() != null) {
						FMLModelFactory factory = getFMLModelFactory();
						parameterInstance = factory.newInstance(FMLDiagramPaletteElementBindingParameter.class);
						parameterInstance.setParameter(parameter);
						addToParameters(parameterInstance);
					}
				}
			}

			for (FMLDiagramPaletteElementBindingParameter p : unusedParameterInstances) {
				removeFromParameters(p);
			}
		}

		@Override
		public FMLModelFactory getFMLModelFactory() {
			if (getOwningVirtualModel() != null) {
				return getOwningVirtualModel().getFMLModelFactory();
			}
			return getDeserializationFactory();
		}

		/*@Override
		public Vector<OverridingGraphicalRepresentation> getOverridingGraphicalRepresentations() {
			return overridingGraphicalRepresentations;
		}
		
		public void setOverridingGraphicalRepresentations(Vector<OverridingGraphicalRepresentation> overridingGraphicalRepresentations) {
			this.overridingGraphicalRepresentations.addAll(overridingGraphicalRepresentations);
		}
		
		@Override
		public void addToOverridingGraphicalRepresentations(OverridingGraphicalRepresentation anOverridingGraphicalRepresentation) {
			overridingGraphicalRepresentations.add(anOverridingGraphicalRepresentation);
			anOverridingGraphicalRepresentation.paletteElementBinding = this;
			setChanged();
			notifyObservers();
		}
		
		@Override
		public void removeFromOverridingGraphicalRepresentations(OverridingGraphicalRepresentation anOverridingGraphicalRepresentation) {
			overridingGraphicalRepresentations.remove(anOverridingGraphicalRepresentation);
			anOverridingGraphicalRepresentation.paletteElementBinding = null;
			setChanged();
			notifyObservers();
		}*/

		@Override
		public GraphicalRepresentation getOverridingGraphicalRepresentation(GraphicalElementRole<?, ?> patternRole) {
			for (OverridingGraphicalRepresentation<?> ogr : getOverridingGraphicalRepresentations()) {
				if (ogr.getPatternRole() == patternRole) {
					return ogr.getGraphicalRepresentation();
				}
			}
			return null;
		}

		@Override
		public void finalizeDeserialization() {
			getBoundFlexoConcept();
			updateParameters();
		}

		@Override
		public DiagramTechnologyAdapter getDiagramTechnologyAdapter() {
			return (DiagramTechnologyAdapter) getDiagramModelSlot().getModelSlotTechnologyAdapter();
		}

		/*@Override
		public void setChanged() {
			super.setChanged();
			if (getPalette() != null) {
				getPalette().setIsModified();
			}
		}*/

		/*@Override
		public boolean delete() {
			if (getPalette() != null) {
				getPalette().removeFromElements(this);
			}
			super.delete();
			deleteObservers();
			return true;
		}*/

		@Override
		public List<FlexoConcept> allAvailableFlexoConcepts() {
			if (getOwningVirtualModel() != null) {
				List<FlexoConcept> returned = new ArrayList<>();
				for (FlexoConcept ep : getOwningVirtualModel().getFlexoConcepts()) {
					if (ep.getFlexoBehaviours(DropScheme.class).size() > 0) {
						returned.add(ep);
					}
				}
				return returned;
			}
			return null;
		}

		@Override
		public List<DropScheme> allAvailableDropSchemes() {
			if (getBoundFlexoConcept() != null) {
				return getBoundFlexoConcept().getFlexoBehaviours(DropScheme.class);
			}
			return null;
		}

		@Override
		public BindingModel getBindingModel() {
			return getOwningVirtualModel().getBindingModel();
		}

		public String getPatternRoleName() {
			return patternRoleName;
		}

		public void setPatternRoleName(String patternRoleName) {
			this.patternRoleName = patternRoleName;
		}

		@Override
		public boolean getBoundLabelToElementName() {
			return boundLabelToElementName;
		}

		@Override
		public void setBoundLabelToElementName(boolean boundLabelToElementName) {
			if (this.boundLabelToElementName != boundLabelToElementName) {
				this.boundLabelToElementName = boundLabelToElementName;
				setChanged();
				notifyObservers(new DataModification("boundLabelToElementName", !boundLabelToElementName, boundLabelToElementName));
			}
		}

	}

}
