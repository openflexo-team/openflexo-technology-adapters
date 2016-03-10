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

package org.openflexo.technologyadapter.pdf;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.FreeModelSlotInstanceConfiguration;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.pdf.model.PDFDocument;

public class PDFModelSlotInstanceConfiguration extends FreeModelSlotInstanceConfiguration<PDFDocument, PDFModelSlot> {

	protected PDFModelSlotInstanceConfiguration(PDFModelSlot ms, AbstractVirtualModelInstance<?, ?> virtualModelInstance,
			FlexoProject project) {
		super(ms, virtualModelInstance, project);
		setResourceUri(project.getURI() + "/Models/myPDF");
		setRelativePath("/");
		setFilename("myPDF.pdf");
	}

	@Override
	protected boolean checkValidFileName() {
		if (!super.checkValidFileName()) {
			return false;
		}
		if (!getFilename().endsWith(".pdf")) {
			setErrorMessage(FlexoLocalization.localizedForKey("file_name_should_end_with_.pdf_suffix"));
			return false;
		}
		return true;
	}
}
