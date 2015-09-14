/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexodocument, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.nature;

import org.openflexo.foundation.doc.nature.FMLControlledDocumentVirtualModelInstanceNature;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.technologyadapter.docx.DocXModelSlot;
import org.openflexo.technologyadapter.docx.model.DocXDocument;

/**
 * Define the "controlled-document" nature of a {@link VirtualModelInstance}<br>
 * 
 * A {@link FMLControlledDocXVirtualModelInstanceNature} might be seen as an interpretation of a given {@link VirtualModelInstance}
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDocXVirtualModelInstanceNature extends
		FMLControlledDocumentVirtualModelInstanceNature<DocXModelSlot, DocXDocument> {

	public static FMLControlledDocXVirtualModelInstanceNature INSTANCE = new FMLControlledDocXVirtualModelInstanceNature();

	// Prevent external instantiation
	private FMLControlledDocXVirtualModelInstanceNature() {
	}

	@Override
	public Class<DocXModelSlot> getModelSlotClass() {
		return DocXModelSlot.class;
	}

	/**
	 * Return boolean indicating if supplied {@link VirtualModelInstance} might be interpreted as a FML-Controlled document
	 */
	@Override
	public boolean hasNature(VirtualModelInstance virtualModelInstance) {

		return hasNature(virtualModelInstance, FMLControlledDocXVirtualModelNature.INSTANCE);
	}

	public static ModelSlotInstance<DocXModelSlot, DocXDocument> getModelSlotInstance(VirtualModelInstance virtualModelInstance) {
		return INSTANCE._getModelSlotInstance(virtualModelInstance);

	}

	public static DocXDocument getDocument(VirtualModelInstance virtualModelInstance) {
		return INSTANCE._getDocument(virtualModelInstance);
	}
}
