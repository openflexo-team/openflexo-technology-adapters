/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.docx;

import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FreeModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.docx.model.DocXDocument;

@Deprecated
public class DocXModelSlotInstanceConfiguration extends FreeModelSlotInstanceConfiguration<DocXDocument, DocXModelSlot> {

	protected DocXModelSlotInstanceConfiguration(DocXModelSlot ms, FlexoConceptInstance fci, FlexoResourceCenter<?> resourceCenter) {
		super(ms, fci, resourceCenter);
		/*setResourceUri(getAction().getFocusedObject().getProject().getURI() + "/DocX/MyDocument");
		setRelativePath("/");
		setFilename("MyDocument.docx");*/
	}

	@Override
	public boolean isValidConfiguration() {
		return super.isValidConfiguration();
	}

	@Override
	public void setOption(ModelSlotInstanceConfigurationOption option) {
		super.setOption(option);
		// TODO : add specific options here
	}

	@Override
	public String getResourceUri() {
		String returned = super.getResourceUri();
		if (returned == null && getOption() == DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewResource) {
			return getResourceCenter().getDefaultBaseURI() + getRelativePath() + getFilename();
		}
		return returned;
	}

	@Override
	public FreeModelSlotInstance<DocXDocument, DocXModelSlot> createModelSlotInstance(FlexoConceptInstance fci,
			VirtualModelInstance<?, ?> view) {

		System.out.println("createModelSlotInstance for " + fci + " view=" + view);

		return super.createModelSlotInstance(fci, view);
	}

	@Override
	protected FreeModelSlotInstance<DocXDocument, DocXModelSlot> configureModelSlotInstance(
			FreeModelSlotInstance<DocXDocument, DocXModelSlot> msInstance) {
		System.out.println("Et on configure avec l'option " + getOption());
		return super.configureModelSlotInstance(msInstance);
	}
}
