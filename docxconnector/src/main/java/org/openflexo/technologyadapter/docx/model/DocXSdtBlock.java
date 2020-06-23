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
import org.docx4j.wml.SdtBlock;
import org.openflexo.foundation.doc.FlexoDocSdtBlock;
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
 * Implementation of {@link FlexoDocSdtBlock} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXSdtBlock.DocXSdtBlockImpl.class)
@XMLElement
public interface DocXSdtBlock extends DocXElement<SdtBlock>, FlexoDocSdtBlock<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = SdtBlock.class)
	public static final String SDT_BLOCK_KEY = "sdtBlock";

	@Getter(value = SDT_BLOCK_KEY, ignoreType = true)
	// We need to clone (reference) container first, in order to have container not null when executing setSdtBlock()
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneSdtBlock()", cloneAfterProperty = CONTAINER_KEY)
	public SdtBlock getSdtBlock();

	@Setter(SDT_BLOCK_KEY)
	public void setSdtBlock(SdtBlock sdtBlock);

	public SdtBlock cloneSdtBlock();

	/**
	 * This is the starting point for updating {@link DocXSdtBlock} with the SdtBlock provided from docx4j library<br>
	 * Take care that the supplied sdtBlock is the object we should update with, but that {@link #getSdtBlock()} is unsafe in this context,
	 * because return former value
	 */
	public void updateFromSdtBlock(SdtBlock sdtBlock, DocXFactory factory);

	public static abstract class DocXSdtBlockImpl extends FlexoDocSdtBlockImpl<DocXDocument, DocXTechnologyAdapter>implements DocXSdtBlock {

		private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
				.getLogger(DocXSdtBlockImpl.class.getPackage().getName());

		@Override
		public SdtBlock getDocXObject() {
			return getSdtBlock();
		}

		@Override
		public void setSdtBlock(SdtBlock sdtBlock) {

			// When called in cloning operation, container should NOT be null
			// That's why we use cloneAfterProperty feature
			// @CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneP()", cloneAfterProperty = CONTAINER_KEY)

			/*System.out.println("setP with " + p);
			System.out.println("getResourceData()=" + getResourceData());
			System.out.println("getContainer()=" + getContainer());
			System.out.println("getResourceData().getResource()=" + getResourceData().getResource());*/

			if ((sdtBlock == null && getSdtBlock() != null) || (sdtBlock != null && !sdtBlock.equals(getSdtBlock()))) {
				if (sdtBlock != null && getResourceData() != null && getResourceData().getResource() != null) {
					updateFromSdtBlock(sdtBlock, ((DocXDocumentResource) getResourceData().getResource()).getFactory());
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXSdtBlock} with the SdtBlock provided from docx4j library<br>
		 * Take care that the supplied sdtBlock is the object we should update with, but that {@link #getSdtBlock()} is unsafe in this
		 * context, because return former value
		 */
		@Override
		public void updateFromSdtBlock(SdtBlock sdtBlock, DocXFactory factory) {

			performSuperSetter(SDT_BLOCK_KEY, sdtBlock);
			// Take care at the previous line, since there is a risk for the notification not to be triggered,
			// if value of SdtBlock given by getSdtBlock() returns the new value

		}

		@Override
		public String getIdentifier() {
			if (getSdtBlock() != null) {
				// TODO
			}
			return null;
		}

		@Override
		public void setIdentifier(String identifier) {
			// TODO
		}

		@Override
		public SdtBlock cloneSdtBlock() {

			if (getSdtBlock() == null) {
				return null;
			}
			SdtBlock copiedSdtBlock = XmlUtils.deepCopy(getSdtBlock());

			return copiedSdtBlock;
		}

		@Override
		public void appendToWordprocessingMLPackage(ContentAccessor parent, int index) {

			parent.getContent().add(index, getSdtBlock());
			getFlexoDocument().setIsModified();

		}

	}

}
