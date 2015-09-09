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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.docx4j.XmlUtils;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;
import org.openflexo.foundation.doc.FlexoDocumentElement;
import org.openflexo.foundation.doc.FlexoTableCell;
import org.openflexo.foundation.doc.FlexoTableRow;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * Implementation of {@link FlexoTableRow} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXTableRow.DocXTableRowImpl.class)
@XMLElement
public interface DocXTableRow extends FlexoTableRow<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = Tr.class)
	public static final String TR_KEY = "tr";

	@Getter(value = TR_KEY, ignoreType = true)
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneTr()")
	public Tr getTr();

	@Setter(TR_KEY)
	public void setTr(Tr tr);

	public Tr cloneTr();

	// TRICKY AREA
	// We override here the PAMELA definition of this property by declaring CloningStrategy as IGNORE
	// We do that becauseTrP is beeing cloned and the setting of new Tr value will cause creation of DocXTextRun
	// We definitely want to avoid double instanciation of DocXTableCell in a cloned row !!!!
	@Override
	@CloningStrategy(StrategyType.IGNORE)
	public List<FlexoTableCell<DocXDocument, DocXTechnologyAdapter>> getTableCells();

	/**
	 * This is the starting point for updating {@link DocXTableRow} with the Tr provided from docx4j library<br>
	 * Take care that the supplied tr is the object we should update with, but that {@link #getTr()} is unsafe in this context, because
	 * return former value
	 */
	public void updateFromTr(Tr tr, DocXFactory factory);

	public static abstract class DocXTableRowImpl extends FlexoTableRowImpl<DocXDocument, DocXTechnologyAdapter>implements DocXTableRow {

		private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
				.getLogger(DocXTableRowImpl.class.getPackage().getName());

		private final Map<Tc, DocXTableCell> cells = new HashMap<Tc, DocXTableCell>();

		public DocXTableRowImpl() {
			super();
		}

		@Override
		public void setTr(Tr tr) {
			if ((tr == null && getTr() != null) || (tr != null && !tr.equals(getTr()))) {
				if (tr != null) {
					updateFromTr(tr, (getResourceData() != null && getResourceData().getResource() != null
							? ((DocXDocumentResource) getResourceData().getResource()).getFactory() : null));
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXTableRow} with the Tr provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getTr()} is unsafe in this context, because
		 * return former value
		 */
		@Override
		public void updateFromTr(Tr tr, DocXFactory factory) {

			List<FlexoTableCell> cellsToRemove = new ArrayList<FlexoTableCell>(getTableCells());

			int currentIndex = 0;

			for (Object o : tr.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				if (o instanceof Tc) {
					DocXTableCell cell = cells.get(o);
					if (cell == null) {
						cell = factory.makeNewDocXTableCell((Tc) o);
						internallyInsertTableCellAtIndex(cell, currentIndex);
					}
					else {
						// OK row was found
						if (getTableCells().indexOf(cell) != currentIndex) {
							// Cell was existing but is not at the right position
							internallyMoveTableCellToIndex(cell, currentIndex);
						}
						else {
							// System.out.println("# Found existing paragraph for " + o);
						}
						cellsToRemove.remove(cell);
					}
					currentIndex++;
				}
			}

			for (FlexoTableCell<DocXDocument, DocXTechnologyAdapter> row : cellsToRemove) {
				// System.out.println("# Remove row for " + e);
				internallyRemoveFromTableCells(row);
			}

			performSuperSetter(TR_KEY, tr);

		}

		@Override
		public DocXDocument getFlexoDocument() {
			if (getTable() != null) {
				return getTable().getFlexoDocument();
			}
			return null;
		}

		/**
		 * Insert cell to this {@link DocXTableRow} at supplied index (public API).<br>
		 * Element will be inserted to underlying {@link Tr} and {@link DocXTableRow} will be updated accordingly
		 */
		@Override
		public void insertTableCellAtIndex(FlexoTableCell<DocXDocument, DocXTechnologyAdapter> aTableCell, int index) {
			System.out.println("Add cell ");
			Tr tr = getTr();
			if (aTableCell instanceof DocXTableCell) {
				Tc tc = ((DocXTableCell) aTableCell).getTc();
				tr.getContent().add(index, tc);
				internallyInsertTableCellAtIndex(aTableCell, index);
			}
			else {
				logger.warning("Unexpected cell: " + aTableCell);
			}
		}

		/**
		 * Internally used to update {@link DocXTableRow} object according to wrapped model in the context of cell inserting (calling this
		 * assume that added cell is already present in underlying {@link Tr})
		 * 
		 * @param addedElement
		 */
		private void internallyInsertTableCellAtIndex(FlexoTableCell<DocXDocument, DocXTechnologyAdapter> aTableCell, int index) {

			performSuperAdder(TABLE_CELLS_KEY, aTableCell, index);
			internallyHandleTableCellAdding(aTableCell);
		}

		/**
		 * Internally handle cell adding
		 * 
		 * @param aTableCell
		 */
		private void internallyHandleTableCellAdding(FlexoTableCell<DocXDocument, DocXTechnologyAdapter> aTableCell) {

			if (aTableCell instanceof DocXTableCell) {
				DocXTableCell cell = (DocXTableCell) aTableCell;
				if (cell.getTc() != null) {
					cells.put(cell.getTc(), cell);
				}
			}
		}

		/**
		 * Move cell in this {@link DocXTableRow} at supplied index (public API).<br>
		 * Element will be moved inside underlying {@link Tr} and {@link DocXTableRow} will be updated accordingly
		 */
		@Override
		public void moveTableCellToIndex(FlexoTableCell<DocXDocument, DocXTechnologyAdapter> aTableCell, int index) {
			// TODO: do it in Tr
			internallyMoveTableCellToIndex(aTableCell, index);
		}

		/**
		 * Internally used to update {@link DocXTableRow} object according to wrapped model in the context of cell moving (calling this
		 * assume that moved cell has already been moved in underlying {@link Tr})
		 * 
		 * @param addedElement
		 */
		private void internallyMoveTableCellToIndex(FlexoTableCell<DocXDocument, DocXTechnologyAdapter> aTableCell, int index) {
			List<FlexoTableCell<DocXDocument, DocXTechnologyAdapter>> cells = getTableCells();
			cells.remove(aTableCell);
			cells.add(index, aTableCell);
		}

		/**
		 * Add cell to this {@link DocXTableRow} (public API).<br>
		 * Element will be added to underlying {@link Tr} and {@link DocXTableRow} will be updated accordingly
		 */
		@Override
		public void addToTableCells(FlexoTableCell<DocXDocument, DocXTechnologyAdapter> aTableCell) {

			Tr tr = getTr();
			if (aTableCell instanceof DocXTableCell) {
				Tc tc = ((DocXTableCell) aTableCell).getTc();
				tr.getContent().add(tc);
				internallyAddToTableCells(aTableCell);
			}
			else {
				logger.warning("Unexpected cell: " + aTableCell);
			}
		}

		/**
		 * Internally used to update {@link DocXTableRow} object according to wrapped model in the context of cell adding (calling this
		 * assume that added cell is already present in underlying {@link Tr})
		 * 
		 * @param addedTableCell
		 */
		private void internallyAddToTableCells(FlexoTableCell<DocXDocument, DocXTechnologyAdapter> addedTableCell) {
			performSuperAdder(TABLE_CELLS_KEY, addedTableCell);
			internallyHandleTableCellAdding(addedTableCell);
		}

		/**
		 * Remove cell from this {@link DocXTableRow} (public API).<br>
		 * Element will be removed from underlying {@link Tr} and {@link DocXTableRow} will be updated accordingly
		 */
		@Override
		public void removeFromTableCells(FlexoTableCell<DocXDocument, DocXTechnologyAdapter> aTableCell) {

			Tr tr = getTr();
			if (aTableCell instanceof DocXTableCell) {
				Tc tc = ((DocXTableCell) aTableCell).getTc();
				if (!DocXUtils.removeFromList(tc, tr.getContent())) {
					logger.warning("Tc item not present in Tr. Please investigate...");
				}
				internallyRemoveFromTableCells(aTableCell);
			}
			else {
				logger.warning("Unexpected cell: " + aTableCell);
			}
		}

		/**
		 * Internally used to update {@link DocXTableRow} object according to wrapped model in the context of cell removing (calling this
		 * assume that removed cell has already been removed from underlying {@link Tr})
		 * 
		 * @param removedTableCell
		 */
		private void internallyRemoveFromTableCells(FlexoTableCell<DocXDocument, DocXTechnologyAdapter> removedTableCell) {
			if (removedTableCell instanceof DocXTableCell) {
				DocXTableCell cell = (DocXTableCell) removedTableCell;
				if (cell.getTc() != null) {
					cells.remove(cell.getTc());
				}
			}
			performSuperRemover(TABLE_CELLS_KEY, removedTableCell);
		}

		@Override
		public FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> getElementWithIdentifier(String identifier) {
			FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> returned;
			for (DocXTableCell cell : cells.values()) {
				returned = cell.getElementWithIdentifier(identifier);
				if (returned != null) {
					return returned;
				}
			}
			return null;
		}

		@Override
		public Tr cloneTr() {

			if (getTr() == null) {
				return null;
			}
			Tr copiedTr = XmlUtils.deepCopy(getTr());

			return copiedTr;
		}

		@Override
		public String getIdentifier() {
			if (getTableCells().size() > 0 && getTableCells().get(0).getParagraphs().size() > 0) {
				return "Row" + getTableCells().get(0).getParagraphs().get(0).getIdentifier();
			}
			return "Row" + getIndex();
		}

		@Override
		public String toString() {
			return "DocXTableRow\n" + (getTr() != null ? DocXUtils.printContents(getTr(), 2) : "null");
		}
	}

}
