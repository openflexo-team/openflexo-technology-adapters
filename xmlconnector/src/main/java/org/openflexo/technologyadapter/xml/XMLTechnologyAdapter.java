/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareResourceTypes;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.technologyadapter.xml.fml.binding.XMLBindingFactory;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelFactory;
import org.openflexo.technologyadapter.xml.rm.XMLFileResourceFactory;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResourceFactory;

/**
 * 
 * @author sylvain, luka, Christophe
 * 
 */

@DeclareModelSlots({ FreeXMLModelSlot.class, XMLModelSlot.class, XMLMetaModelSlot.class })
@DeclareResourceTypes({ XSDMetaModelResourceFactory.class, XMLFileResourceFactory.class })
public class XMLTechnologyAdapter extends TechnologyAdapter<XMLTechnologyAdapter> {

	protected static final Logger logger = Logger.getLogger(XMLTechnologyAdapter.class.getPackage().getName());

	private static final String TAName = "XML technology adapter";

	private XMLModelFactory xmlModelFactory = null;

	private static final XMLBindingFactory BINDING_FACTORY = new XMLBindingFactory();

	protected HashMap<String, XMLMetaModel> privateMetamodels = null;

	public XMLTechnologyAdapter() {
		super();
		xmlModelFactory = new XMLModelFactory();
		privateMetamodels = new HashMap<>();
	}

	@Override
	public String getName() {
		return TAName;
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/XMLTechnologyAdapter";
	}

	public String retrieveModelURI(File aModelFile, FlexoResource<XMLModel> metaModelResource) {
		return aModelFile.toURI().toString();
	}

	@Override
	public XMLTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {

		return new XMLTechnologyContextManager(this, service);
	}

	@Override
	public XMLTechnologyContextManager getTechnologyContextManager() {
		return (XMLTechnologyContextManager) super.getTechnologyContextManager();
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	public <I> XSDMetaModelRepository<I> getXSDMetaModelRepository(FlexoResourceCenter<I> resourceCenter) {
		XSDMetaModelRepository<I> returned = resourceCenter.retrieveRepository(XSDMetaModelRepository.class, this);
		if (returned == null) {
			returned = XSDMetaModelRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, XSDMetaModelRepository.class, this);
		}
		return returned;
	}

	public <I> XMLModelRepository<I> getXMLModelRepository(FlexoResourceCenter<I> resourceCenter) {
		XMLModelRepository<I> returned = resourceCenter.retrieveRepository(XMLModelRepository.class, this);
		if (returned == null) {
			returned = XMLModelRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, XMLModelRepository.class, this);
		}
		return returned;
	}

	public XMLModelFactory getXMLModelFactory() {
		return xmlModelFactory;
	}

	@Override
	public XMLBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	@Override
	public String getIdentifier() {
		return "XML";
	}

	public XMLFileResourceFactory getXMLFileResourceFactory() {
		return getResourceFactory(XMLFileResourceFactory.class);
	}

	public XSDMetaModelResourceFactory getXSDMetaModelResourceFactory() {
		return getResourceFactory(XSDMetaModelResourceFactory.class);
	}

}
