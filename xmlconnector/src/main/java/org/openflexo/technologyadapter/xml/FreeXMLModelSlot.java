/*
 * (c) Copyright 2010-2012 AgileBirds
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

package org.openflexo.technologyadapter.xml;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.editionaction.AddXMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.virtualmodel.XMLIndividualRole;

/**
 * 
 * An XML ModelSlot used to edit freely an XML document and simultaneously produce the corresponding MetaModel
 * 
 * @author xtof
 * 
 */
@DeclarePatternRoles({ @DeclarePatternRole(flexoRoleClass = XMLIndividualRole.class, FML = "XMLIndividual"), // Instances
})
@DeclareEditionActions({ @DeclareEditionAction(editionActionClass = AddXMLIndividual.class, FML = "AddXMLIndividual"), // Add
	// instance
})
@ModelEntity
@XMLElement
@ImplementationClass(FreeXMLModelSlot.FreeXMLModelSlotImpl.class)
public interface FreeXMLModelSlot extends FreeModelSlot<XMLModel>,AbstractXMLModelSlot<FreeXMLURIProcessor> {


	// public static abstract class FreeXMLModelSlotImpl extends AbstractXMLModelSlot.AbstractXMLModelSlotImpl<FreeXMLURIProcessor> implements FreeXMLModelSlot {
	public static abstract class FreeXMLModelSlotImpl extends FreeModelSlotImpl<XMLModel> implements FreeXMLModelSlot {


		 private static ModelFactory MF;
		 
		    static{
		    	try {
					MF = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FreeXMLModelSlot.class,
											  										  FreeXMLURIProcessor.class));
				} catch (ModelDefinitionException e) {
					e.printStackTrace();
				}
		    }

		    
		public FreeXMLModelSlotImpl(){
			super();
		}
		
		@Override
		public Class<? extends TechnologyAdapter> getTechnologyAdapterClass() {
			return XMLTechnologyAdapter.class;
		}
		
		
		@Override
		public TechnologyAdapterResource<XMLModel, ?> createProjectSpecificEmptyResource(View view, String filename, String modelUri) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public TechnologyAdapterResource<XMLModel, ?> createSharedEmptyResource(FlexoResourceCenter<?> resourceCenter, String relativePath,
				String filename, String modelUri) {
			// TODO Auto-generated method stub
			return null;
		}


		private static final Logger logger = Logger.getLogger(FreeXMLModelSlot.class.getPackage().getName());


		@Override
		public FreeXMLURIProcessor createURIProcessor() {
			FreeXMLURIProcessor xsuriProc = getVirtualModelFactory().newInstance(FreeXMLURIProcessor.class);
			xsuriProc.setModelSlot(this);
			this.addToUriProcessorsList(xsuriProc);
			return xsuriProc;
		}


		@Override
		public ModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new FreeXMLModelSlotInstanceConfiguration(this, action);
		}

	}

}
