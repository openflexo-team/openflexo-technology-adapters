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
package org.openflexo.technologyadapter.diagram.metamodel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.viewpoint.TechnologySpecificCustomType;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.model.annotations.Adder;
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
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.dm.DiagramPaletteInserted;
import org.openflexo.technologyadapter.diagram.model.dm.DiagramPaletteRemoved;
import org.openflexo.technologyadapter.diagram.model.dm.ExampleDiagramInserted;
import org.openflexo.technologyadapter.diagram.model.dm.ExampleDiagramRemoved;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResourceImpl;
import org.openflexo.toolbox.ChainedCollection;

/**
 * A {@link DiagramSpecification} is the specification of a Diagram: this can be considered as the metamodel for a {@link Diagram} modelling
 * element<br>
 * 
 * A {@link DiagramSpecification} contains some palettes and example diagrams
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(DiagramSpecification.DiagramSpecificationImpl.class)
@XMLElement(xmlTag = "DiagramSpecification")
public interface DiagramSpecification extends TechnologyObject<DiagramTechnologyAdapter>, FlexoMetaModel<DiagramSpecification>,
		ResourceData<DiagramSpecification> {

	@PropertyIdentifier(type = String.class)
	public static final String NAME_KEY = "name";
	@PropertyIdentifier(type = String.class)
	public static final String URI_KEY = "URI";
	@PropertyIdentifier(type = DiagramPalette.class, cardinality = Cardinality.LIST)
	public static final String PALETTES_KEY = "palettes";
	@PropertyIdentifier(type = Diagram.class, cardinality = Cardinality.LIST)
	public static final String EXAMPLE_DIAGRAMS_KEY = "exampleDiagrams";

	/**
	 * Return title of this DiagramSpecification
	 * 
	 * @return
	 */
	@Getter(value = NAME_KEY)
	@XMLAttribute
	public String getName();

	/**
	 * Sets title of this DiagramSpecification
	 */
	@Setter(value = NAME_KEY)
	public void setName(String name);

	@Override
	@Getter(value = URI_KEY)
	@XMLAttribute(xmlTag = "uri")
	public String getURI();

	@Setter(value = URI_KEY)
	public void setURI(String uri);

	// Palettes are not serialized in DiagramSpecification
	@Getter(value = PALETTES_KEY, cardinality = Cardinality.LIST, ignoreType = true)
	public List<DiagramPalette> getPalettes();

	/**
	 * Return palette identified by its name or uri
	 */
	public DiagramPalette getPalette(String paletteId);

	@Setter(PALETTES_KEY)
	public void setPalettes(List<DiagramPalette> palettes);

	@Adder(PALETTES_KEY)
	public void addToPalettes(DiagramPalette aPalette);

	@Remover(PALETTES_KEY)
	public void removeFromPalettes(DiagramPalette aPalette);

	// Palettes are not serialized in DiagramSpecification
	@Getter(value = EXAMPLE_DIAGRAMS_KEY, cardinality = Cardinality.LIST, ignoreType = true)
	public List<Diagram> getExampleDiagrams();

	public Diagram getExampleDiagram(String diagramName);

	@Setter(EXAMPLE_DIAGRAMS_KEY)
	public void setExampleDiagrams(List<Diagram> exampleDiagrams);

	@Adder(EXAMPLE_DIAGRAMS_KEY)
	public void addToExampleDiagrams(Diagram aDiagram);

	@Remover(EXAMPLE_DIAGRAMS_KEY)
	public void removeFromExampleDiagrams(Diagram aDiagram);

	@Override
	public DiagramSpecificationResource getResource();

	public static abstract class DiagramSpecificationImpl extends FlexoObjectImpl implements DiagramSpecification {

		private static final Logger logger = Logger.getLogger(DiagramSpecification.class.getPackage().getName());

		private DiagramSpecificationResource resource;

		private List<DiagramPalette> palettes;
		private List<Diagram> exampleDiagrams;

		/**
		 * Stores a chained collections of objects which are involved in validation
		 */
		private final ChainedCollection<ViewPointObject> validableObjects = null;

		/**
		 * Creates a new VirtualModel on user request<br>
		 * Creates both the resource and the object
		 * 
		 * 
		 * @param baseName
		 * @param viewPoint
		 * @return
		 */
		public static DiagramSpecification newDiagramSpecification(String uri, String baseName, RepositoryFolder<?> folder,
				FlexoServiceManager serviceManager) {
			// File diagramSpecificationXMLFile = new File(diagramSpecificationDirectory, baseName +
			// DiagramSpecificationResource.DIAGRAM_SPECIFICATION_SUFFIX);
			DiagramSpecificationResource dsRes = DiagramSpecificationResourceImpl.makeDiagramSpecificationResource(baseName, folder, uri,
					serviceManager);
			DiagramSpecification diagramSpecification = dsRes.getFactory().newInstance(DiagramSpecification.class);
			dsRes.setResourceData(diagramSpecification);
			diagramSpecification.setResource(dsRes);
			try {
				dsRes.save(null);
			} catch (SaveResourceException e) {
				e.printStackTrace();
			}
			return diagramSpecification;
		}

		// Used during deserialization, do not use it
		public DiagramSpecificationImpl() {
			super();
			exampleDiagrams = null;
			palettes = null;
		}

		public FlexoServiceManager getServiceManager() {
			return getResource().getServiceManager();
		}

		/**
		 * Load eventually unloaded VirtualModels<br>
		 * After this call return, we can assert that all {@link VirtualModel} are loaded.
		 */
		private void loadDiagramPalettesWhenUnloaded() {
			palettes = new ArrayList<DiagramPalette>();
			if (getResource() != null) {
				for (org.openflexo.foundation.resource.FlexoResource<?> r : getResource().getContents()) {
					if (r instanceof DiagramPaletteResource) {
						DiagramPalette palette = ((DiagramPaletteResource) r).getDiagramPalette();
						if (!palettes.contains(palette)) {
							addToPalettes(palette);
						}
					}
				}
			}
		}

		/**
		 * Load eventually unloaded VirtualModels<br>
		 * After this call return, we can assert that all {@link VirtualModel} are loaded.
		 */
		private void loadExampleDiagramsWhenUnloaded() {
			exampleDiagrams = new ArrayList<Diagram>();
			if (getResource() != null) {
				for (org.openflexo.foundation.resource.FlexoResource<?> r : getResource().getContents()) {
					if (r instanceof DiagramResource) {
						Diagram exampleDiagram = ((DiagramResource) r).getDiagram();
						if (!exampleDiagrams.contains(exampleDiagram)) {
							addToExampleDiagrams(exampleDiagram);
							exampleDiagram.setDiagramSpecification(this);
						}
					}
				}
			}
		}

		@Override
		public DiagramSpecificationResource getResource() {
			return resource;
		}

		@Override
		public void setResource(FlexoResource<DiagramSpecification> resource) {
			this.resource = (DiagramSpecificationResource) resource;
		}

		@Override
		public String getURI() {
			if (getResource() != null) {
				return getResource().getURI();
			}
			return null;
		}

		@Override
		public void setURI(String uri) {
			if (getResource() != null) {
				getResource().setURI(uri);
			}
		}

		@Override
		public String toString() {
			return "DiagramSpecification:" + getURI();
		}

		@Override
		public List<DiagramPalette> getPalettes() {
			if (palettes == null) {
				loadDiagramPalettesWhenUnloaded();
			}
			return palettes;
		}

		/**
		 * Return palette identified by its name or uri
		 */
		@Override
		public DiagramPalette getPalette(String paletteId) {
			if (palettes == null) {
				loadDiagramPalettesWhenUnloaded();
			}
			if (paletteId == null) {
				return null;
			}
			loadDiagramPalettesWhenUnloaded();
			for (DiagramPalette p : getPalettes()) {
				if (paletteId.equals(p.getName())) {
					return p;
				}
				if (paletteId.equals(p.getURI())) {
					return p;
				}
			}
			return null;
		}

		@Override
		public void addToPalettes(DiagramPalette aPalette) {
			if (palettes == null) {
				loadDiagramPalettesWhenUnloaded();
			}
			if (!palettes.contains(aPalette)) {
				System.out.println("Adding palette " + aPalette.hashCode());
				palettes.add(aPalette);
				setChanged();
				notifyObservers(new DiagramPaletteInserted(aPalette, this));
			}
		}

		@Override
		public void removeFromPalettes(DiagramPalette aPalette) {
			if (palettes == null) {
				loadDiagramPalettesWhenUnloaded();
			}
			if (palettes.contains(aPalette)) {
				palettes.remove(aPalette);
				setChanged();
				notifyObservers(new DiagramPaletteRemoved(aPalette, this));
			}
		}

		@Override
		public List<Diagram> getExampleDiagrams() {
			if (exampleDiagrams == null) {
				loadExampleDiagramsWhenUnloaded();
			}
			return exampleDiagrams;
		}

		@Override
		public Diagram getExampleDiagram(String diagramName) {
			if (exampleDiagrams == null) {
				loadExampleDiagramsWhenUnloaded();
			}
			if (diagramName == null) {
				return null;
			}
			loadExampleDiagramsWhenUnloaded();
			for (Diagram s : getExampleDiagrams()) {
				if (diagramName.equals(s.getName())) {
					return s;
				}
			}
			return null;
		}

		@Override
		public void addToExampleDiagrams(Diagram aDiagram) {
			if (exampleDiagrams == null) {
				loadExampleDiagramsWhenUnloaded();
			}
			if (!exampleDiagrams.contains(aDiagram)) {
				exampleDiagrams.add(aDiagram);
				aDiagram.setDiagramSpecification(this);
				setChanged();
				notifyObservers(new ExampleDiagramInserted(aDiagram, this));
			}
		}

		@Override
		public void removeFromExampleDiagrams(Diagram aDiagram) {
			if (exampleDiagrams == null) {
				loadExampleDiagramsWhenUnloaded();
			}
			if (exampleDiagrams.contains(aDiagram)) {
				exampleDiagrams.remove(aDiagram);
				setChanged();
				notifyObservers(new ExampleDiagramRemoved(aDiagram, this));
			}
		}

		@Override
		public DiagramTechnologyAdapter getTechnologyAdapter() {
			if (getResource() != null) {
				return getResource().getTechnologyAdapter();
			}
			return null;
		}
		
		/*@Override
		protected void notifyEditionSchemeModified() {
		_allFlexoConceptWithDropScheme = null;
		_allFlexoConceptWithLinkScheme = null;
		}

		private Vector<FlexoConcept> _allFlexoConceptWithDropScheme;
		private Vector<FlexoConcept> _allFlexoConceptWithLinkScheme;

		public Vector<FlexoConcept> getAllFlexoConceptWithDropScheme() {
		if (_allFlexoConceptWithDropScheme == null) {
		_allFlexoConceptWithDropScheme = new Vector<FlexoConcept>();
		for (FlexoConcept p : getFlexoConcepts()) {
			if (p.hasDropScheme()) {
				_allFlexoConceptWithDropScheme.add(p);
			}
		}
		}
		return _allFlexoConceptWithDropScheme;
		}

		public Vector<FlexoConcept> getAllFlexoConceptWithLinkScheme() {
		if (_allFlexoConceptWithLinkScheme == null) {
		_allFlexoConceptWithLinkScheme = new Vector<FlexoConcept>();
		for (FlexoConcept p : getFlexoConcepts()) {
			if (p.hasLinkScheme()) {
				_allFlexoConceptWithLinkScheme.add(p);
			}
		}
		}
		return _allFlexoConceptWithLinkScheme;
		}

		@Override
		public void addToFlexoConcepts(FlexoConcept pattern) {
		_allFlexoConceptWithDropScheme = null;
		_allFlexoConceptWithLinkScheme = null;
		super.addToFlexoConcepts(pattern);
		}

		@Override
		public void removeFromFlexoConcepts(FlexoConcept pattern) {
		_allFlexoConceptWithDropScheme = null;
		_allFlexoConceptWithLinkScheme = null;
		super.removeFromFlexoConcepts(pattern);
		}

		public Vector<LinkScheme> getAllConnectors() {
		Vector<LinkScheme> returned = new Vector<LinkScheme>();
		for (FlexoConcept ep : getFlexoConcepts()) {
		for (LinkScheme s : ep.getLinkSchemes()) {
			returned.add(s);
		}
		}
		return returned;
		}

		public Vector<LinkScheme> getConnectorsMatching(FlexoConcept fromConcept, FlexoConcept toConcept) {
		Vector<LinkScheme> returned = new Vector<LinkScheme>();
		for (FlexoConcept ep : getFlexoConcepts()) {
		for (LinkScheme s : ep.getLinkSchemes()) {
			if (s.isValidTarget(fromConcept, toConcept)) {
				returned.add(s);
			}
		}
		}
		return returned;
		}

		@Override
		public boolean handleVariable(BindingVariable variable) {
		if (variable.getVariableName().equals(DiagramEditionScheme.TOP_LEVEL)) {
		return true;
		}
		return super.handleVariable(variable);
		}
		 */

		/*@Override
		public Collection<ViewPointObject> getEmbeddedValidableObjects() {
		if (validableObjects == null) {
		validableObjects = new ChainedCollection<ViewPointObject>(getFlexoConcepts(), getModelSlots(), getPalettes(),
				getExampleDiagrams());
		}
		return validableObjects;
		}*/

		/*@Override
		public final void finalizeDeserialization(Object builder) {
		super.finalizeDeserialization(builder);
		updateBindingModel();
		}*/

		@Override
		public String getName() {
			if (getResource() != null) {
				return getResource().getName();
			}
			return null;
		}

		@Override
		public void setName(String name) {
			if (requireChange(getName(), name)) {
				if (getResource() != null) {
					getResource().setName(name);
				}
			}
		}

		
		@Override
		public Object getObject(String objectURI) {
			return null;
		}
		
	}

}
