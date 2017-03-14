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

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBElement;

import org.docx4j.dml.Graphic;
import org.docx4j.dml.GraphicData;
import org.docx4j.dml.picture.CTPictureNonVisual;
import org.docx4j.dml.picture.Pic;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.R;
import org.openflexo.foundation.doc.FlexoDocRun;
import org.openflexo.foundation.doc.FlexoDrawingRun;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument.DocXDocumentImpl;
import org.openflexo.toolbox.StringUtils;

/**
 * Implementation of {@link FlexoDocRun} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXDrawingRun.DocXDrawingRunImpl.class)
@XMLElement
public interface DocXDrawingRun extends FlexoDrawingRun<DocXDocument, DocXTechnologyAdapter>, DocXRun {

	public static abstract class DocXDrawingRunImpl extends DocXRunImpl implements DocXDrawingRun {

		private Drawing drawing;
		private String imageName;
		private Image image;
		private File imageFile;
		private String embedId;

		@Override
		public Image getImage() {
			if (image == null && StringUtils.isNotEmpty(embedId)) {

				DocXDocument document = getFlexoDocument();

				if (document != null && document instanceof DocXDocumentImpl
						&& document.getResource().getIODelegate().getSerializationArtefact() instanceof File) {
					MainDocumentPart documentPart = document.getWordprocessingMLPackage().getMainDocumentPart();
					Relationship r = documentPart.getRelationshipsPart().getRelationshipByID(embedId);
					RelationshipsPart relsPart = documentPart.getRelationshipsPart();

					BinaryPartAbstractImage binaryImage = (BinaryPartAbstractImage) relsPart.getPart(embedId);

					try {
						image = ImageIO.read(new ByteArrayInputStream(binaryImage.getBytes()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			return image;
		}

		public void setImage(Image image) {
			if (image != this.image) {
				Image oldValue = this.image;
				this.image = image;
				getPropertyChangeSupport().firePropertyChange("image", oldValue, image);
			}
		}

		protected String getEmbedId() {
			return embedId;
		}

		@Deprecated
		@Override
		public File getImageFile() {

			if (imageFile == null && StringUtils.isNotEmpty(embedId)) {

				DocXDocument document = getFlexoDocument();

				if (document != null && document instanceof DocXDocumentImpl
						&& document.getResource().getIODelegate().getSerializationArtefact() instanceof File) {
					MainDocumentPart documentPart = document.getWordprocessingMLPackage().getMainDocumentPart();
					Relationship r = documentPart.getRelationshipsPart().getRelationshipByID(embedId);
					RelationshipsPart relsPart = documentPart.getRelationshipsPart();

					BinaryPartAbstractImage image = (BinaryPartAbstractImage) relsPart.getPart(embedId);

					imageFile = new File(((DocXDocumentImpl) document).getTempDirectory(), r.getTarget());
					System.out.println("imageFile=" + imageFile);
					imageFile.getParentFile().mkdirs();

					FileOutputStream out;
					try {
						out = new FileOutputStream(imageFile);
						image.writeDataToOutputStream(out);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			return imageFile;
		}

		/**
		 * This is the starting point for updating {@link DocXDrawingRun} with the paragraph provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getP()} is unsafe in this context, because
		 * return former value
		 */
		@Override
		public void updateFromR(R r, DocXFactory factory) {

			super.updateFromR(r, factory);

			for (Object o : r.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				if (o instanceof Drawing) {
					drawing = (Drawing) o;
					for (Object o2 : drawing.getAnchorOrInline()) {
						if (o2 instanceof JAXBElement) {
							o2 = ((JAXBElement) o2).getValue();
						}
						if (o2 instanceof Inline) {
							Inline inline = (Inline) o2;
							if (inline.getDocPr() != null) {
								imageName = inline.getDocPr().getName();
								Graphic graphic = inline.getGraphic();
								if (graphic != null && graphic.getGraphicData() != null && graphic.getGraphicData().getPic() != null
										&& graphic.getGraphicData().getPic().getNvPicPr() != null) {
									GraphicData graphicData = graphic.getGraphicData();
									Pic pic = graphicData.getPic();
									CTPictureNonVisual nvPicPr = pic.getNvPicPr();
									imageName = imageName + " (" + nvPicPr.getCNvPr().getName() + ")";

									embedId = pic.getBlipFill().getBlip().getEmbed();
									// System.out.println("embedId=" + embedId);

								}
							}
						}
					}
				}
			}

		}

		@Override
		public String getImageName() {
			if (imageName != null) {
				return imageName;
			}
			return "IMAGE";
		}
	}

}
