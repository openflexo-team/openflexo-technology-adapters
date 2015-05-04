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

package org.openflexo.technologyadapter.docx.rm;

import org.openflexo.foundation.resource.PamelaResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.DocXTechnologyContextManager;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFactory;

@ModelEntity
@ImplementationClass(DocXDocumentResourceImpl.class)
public abstract interface DocXDocumentResource
		extends TechnologyAdapterResource<DocXDocument, DocXTechnologyAdapter>, PamelaResource<DocXDocument, DocXFactory> {
	public static final String TECHNOLOGY_CONTEXT_MANAGER = "technologyContextManager";

	public DocXDocument getDocXDocument();

	@Getter(value = "technologyContextManager", ignoreType = true)
	public abstract DocXTechnologyContextManager getTechnologyContextManager();

	@Setter("technologyContextManager")
	public abstract void setTechnologyContextManager(DocXTechnologyContextManager paramDOCXTechnologyContextManager);
}
