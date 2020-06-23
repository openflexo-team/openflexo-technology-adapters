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

package org.openflexo.technologyadapter.docx.model;

import org.docx4j.XmlUtils;
import org.docx4j.wml.ContentAccessor;
import org.openflexo.foundation.doc.FlexoDocUnmappedElement;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * Implementation of {@link DocXUnmappedElement} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXUnmappedElement.DocXUnmappedElementImpl.class)
@XMLElement
public interface DocXUnmappedElement<T> extends DocXElement<T>, FlexoDocUnmappedElement<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = Object.class)
	public static final String DOCX_OBJECT_KEY = "docXObject";

	@Override
	@Getter(value = DOCX_OBJECT_KEY, ignoreType = true)
	// We need to clone (reference) container first, in order to have container not null when executing setDocXObject()
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneDocXObject()", cloneAfterProperty = CONTAINER_KEY)
	public T getDocXObject();

	@Setter(DOCX_OBJECT_KEY)
	public void setDocXObject(T docXObject);

	public T cloneDocXObject();

	/**
	 * Update {@link DocXUnmappedElement} with the docx object provided from docx4j library<br>
	 */
	public void updateFromDocXObject(T docXObject, DocXFactory factory);

	public static abstract class DocXUnmappedElementImpl<T> extends FlexoDocUnmappedElementImpl<DocXDocument, DocXTechnologyAdapter>
			implements DocXUnmappedElement<T> {

		private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
				.getLogger(DocXUnmappedElement.class.getPackage().getName());

		public DocXUnmappedElementImpl() {
			super();
		}

		@Override
		public void setDocXObject(T docXObject) {

			if ((docXObject == null && getDocXObject() != null) || (docXObject != null && !docXObject.equals(getDocXObject()))) {
				if (docXObject != null && getResourceData() != null && getResourceData().getResource() != null) {
					updateFromDocXObject(docXObject, ((DocXDocumentResource) getResourceData().getResource()).getFactory());
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXSdtBlock} with the SdtBlock provided from docx4j library<br>
		 * Take care that the supplied sdtBlock is the object we should update with, but that {@link #getSdtBlock()} is unsafe in this
		 * context, because return former value
		 */
		@Override
		public void updateFromDocXObject(T docXObject, DocXFactory factory) {

			performSuperSetter(DocXUnmappedElement.DOCX_OBJECT_KEY, docXObject);

		}

		@Override
		public String getIdentifier() {
			if (getDocXObject() != null) {
				// TODO
			}
			return null;
		}

		@Override
		public void setIdentifier(String identifier) {
			// TODO
		}

		@Override
		public T cloneDocXObject() {

			if (getDocXObject() == null) {
				return null;
			}
			T copiedDocXObject = XmlUtils.deepCopy(getDocXObject());

			return copiedDocXObject;
		}

		@Override
		public void appendToWordprocessingMLPackage(ContentAccessor parent, int index) {

			parent.getContent().add(index, getDocXObject());
			getFlexoDocument().setIsModified();

		}

	}

}
