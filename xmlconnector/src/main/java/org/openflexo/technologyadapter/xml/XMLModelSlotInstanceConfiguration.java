/**
 * 
 * Copyright (c) 2014, Openflexo
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

import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.rm.XMLFileResourceFactory;

@Deprecated
public class XMLModelSlotInstanceConfiguration extends TypeAwareModelSlotInstanceConfiguration<XMLModel, XMLMetaModel, XMLModelSlot> {

	protected XMLModelSlotInstanceConfiguration(XMLModelSlot ms, FlexoConceptInstance fci, FlexoResourceCenter<?> rc) {
		super(ms, fci, rc);
		setModelUri(rc.getDefaultBaseURI() + "/Models/myXMLFile");
		setRelativePath("/");
		setFilename("myXMLFile" + XMLFileResourceFactory.XML_EXTENSION);

		// ((XMLTechnologyAdapter) getModelSlot().getModelSlotTechnologyAdapter())
		// .getExpectedModelExtension(getModelSlot().getMetaModelResource()));
	}

	/*@Override
	public void setOption(org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		if (option == DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel) {
			modelUri = getAction().getFocusedObject().getProject().getURI() + "/Models/myXMLFile";
			relativePath = "/";
			filename = "myXMLFile"
					+ ((XMLTechnologyAdapter) getModelSlot().getTechnologyAdapter()).getExpectedModelExtension(getModelSlot()
							.getMetaModelResource());
		} else if (option == DefaultModelSlotInstanceConfigurationOption.CreateSharedNewModel) {
			modelUri = "ResourceCenter/Models/";
			relativePath = "/";
			filename = "myXMLFile"
					+ getModelSlot().getTechnologyAdapter().getExpectedModelExtension(
							(XSDMetaModelResource) getModelSlot().getMetaModelResource());
			}
	}*/

	@Override
	public boolean isURIEditable() {
		return false;
	}

	@Override
	protected boolean checkValidFileName() {
		if (!super.checkValidFileName()) {
			return false;
		}
		String expectedSuffix = XMLFileResourceFactory.XML_EXTENSION;
		// ((XMLTechnologyAdapter) getModelSlot().getModelSlotTechnologyAdapter())
		// .getExpectedModelExtension(getModelSlot().getMetaModelResource());
		if (!getFilename().endsWith(expectedSuffix)) {
			setErrorMessage(
					getModelSlot().getModelSlotTechnologyAdapter().getLocales().localizedForKey("file_name_should_end_with_right_suffix")
							+ " : " + expectedSuffix);
			return false;
		}
		return true;
	}
}
