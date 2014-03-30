#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * (c) Copyright 2013 Openflexo
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

package ${package}.metamodel;

import java.util.List;
import org.openflexo.foundation.ontology.FlexoOntologyObjectImpl;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyAnnotation;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyContainer;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyDataType;
import org.openflexo.foundation.ontology.IFlexoOntologyIndividual;
import org.openflexo.foundation.ontology.IFlexoOntologyMetaModel;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import ${package}.${technologyPrefix}TechnologyAdapter;

public class ${technologyPrefix}MetaModel extends FlexoOntologyObjectImpl<${technologyPrefix}TechnologyAdapter>
implements FlexoMetaModel<${technologyPrefix}MetaModel>, IFlexoOntologyMetaModel<${technologyPrefix}TechnologyAdapter>
{
	public FlexoResource<${technologyPrefix}MetaModel> getResource()
	{
		return null;
	}

	public void setResource(FlexoResource<${technologyPrefix}MetaModel> resource)
	{
	}

	public String getVersion()
	{
		return null;
	}

	public List<? extends IFlexoOntology<${technologyPrefix}TechnologyAdapter>> getImportedOntologies()
	{
		return null;
	}

	public List<? extends IFlexoOntologyAnnotation> getAnnotations()
	{
		return null;
	}

	public List<? extends IFlexoOntologyClass<${technologyPrefix}TechnologyAdapter>> getAccessibleClasses()
	{
		return null;
	}

	public List<? extends IFlexoOntologyIndividual<${technologyPrefix}TechnologyAdapter>> getAccessibleIndividuals()
	{
		return null;
	}

	public List<? extends IFlexoOntologyObjectProperty<${technologyPrefix}TechnologyAdapter>> getAccessibleObjectProperties()
	{
		return null;
	}

	public List<? extends IFlexoOntologyDataProperty<${technologyPrefix}TechnologyAdapter>> getAccessibleDataProperties()
	{
		return null;
	}

	public IFlexoOntologyConcept<${technologyPrefix}TechnologyAdapter> getDeclaredOntologyObject(String objectURI)
	{
		return null;
	}

	public IFlexoOntologyClass<${technologyPrefix}TechnologyAdapter> getDeclaredClass(String classURI)
	{
		return null;
	}

	public IFlexoOntologyIndividual<${technologyPrefix}TechnologyAdapter> getDeclaredIndividual(String individualURI)
	{
		return null;
	}

	public IFlexoOntologyObjectProperty<${technologyPrefix}TechnologyAdapter> getDeclaredObjectProperty(String propertyURI)
	{
		return null;
	}

	public IFlexoOntologyDataProperty<${technologyPrefix}TechnologyAdapter> getDeclaredDataProperty(String propertyURI)
	{
		return null;
	}

	public IFlexoOntologyStructuralProperty<${technologyPrefix}TechnologyAdapter> getDeclaredProperty(String objectURI)
	{
		return null;
	}

	public IFlexoOntologyClass<${technologyPrefix}TechnologyAdapter> getRootConcept()
	{
		return null;
	}

	public void setName(String name)
			throws Exception
			{
			}

	public List<? extends IFlexoOntologyContainer<${technologyPrefix}TechnologyAdapter>> getSubContainers()
	{
		return null;
	}

	public List<? extends IFlexoOntologyConcept<${technologyPrefix}TechnologyAdapter>> getConcepts()
	{
		return null;
	}

	public List<? extends IFlexoOntologyDataType<${technologyPrefix}TechnologyAdapter>> getDataTypes()
	{
		return null;
	}

	public IFlexoOntologyConcept<${technologyPrefix}TechnologyAdapter> getOntologyObject(String objectURI)
	{
		return null;
	}

	public IFlexoOntologyClass<${technologyPrefix}TechnologyAdapter> getClass(String classURI)
	{
		return null;
	}

	public IFlexoOntologyIndividual<${technologyPrefix}TechnologyAdapter> getIndividual(String individualURI)
	{
		return null;
	}

	public IFlexoOntologyObjectProperty<${technologyPrefix}TechnologyAdapter> getObjectProperty(String propertyURI)
	{
		return null;
	}

	public IFlexoOntologyDataProperty<${technologyPrefix}TechnologyAdapter> getDataProperty(String propertyURI)
	{
		return null;
	}

	public IFlexoOntologyStructuralProperty<${technologyPrefix}TechnologyAdapter> getProperty(String objectURI)
	{
		return null;
	}

	public List<? extends IFlexoOntologyClass<${technologyPrefix}TechnologyAdapter>> getClasses()
	{
		return null;
	}

	public List<? extends IFlexoOntologyIndividual<${technologyPrefix}TechnologyAdapter>> getIndividuals()
	{
		return null;
	}

	public List<? extends IFlexoOntologyDataProperty<${technologyPrefix}TechnologyAdapter>> getDataProperties()
	{
		return null;
	}

	public List<? extends IFlexoOntologyObjectProperty<${technologyPrefix}TechnologyAdapter>> getObjectProperties()
	{
		return null;
	}

	public String getURI()
	{
		return null;
	}

	public boolean isReadOnly()
	{
		return false;
	}

	public void setIsReadOnly(boolean b)
	{
	}

	public Object getObject(String objectURI)
	{
		return null;
	}

	public ${technologyPrefix}TechnologyAdapter getTechnologyAdapter()
	{
		return null;
	}

	public String getName()
	{
		return null;
	}

	public IFlexoOntology getFlexoOntology()
	{
		return null;
	}

	public String getDisplayableDescription()
	{
		return null;
	}
	public String getFullyQualifiedName()
	{
		return null;
	}
}
