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

package org.openflexo.technologyadapter.pdf.rm;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResourceRepository;
import org.openflexo.technologyadapter.pdf.PDFTechnologyAdapter;
import org.openflexo.technologyadapter.pdf.model.PDFDocument;

public class PDFDocumentRepository extends TechnologyAdapterResourceRepository<PDFDocumentResource, PDFTechnologyAdapter, PDFDocument> {

	public PDFDocumentRepository(PDFTechnologyAdapter adapter, FlexoResourceCenter<?> resourceCenter) {
		super(adapter, resourceCenter);
	}

	private static final String DEFAULT_BASE_URI = "http://www.openflexo.org/DOCXTechnologyAdapter/Documents";

	@Override
	public String getDefaultBaseURI() {
		return DEFAULT_BASE_URI;
	}

}
