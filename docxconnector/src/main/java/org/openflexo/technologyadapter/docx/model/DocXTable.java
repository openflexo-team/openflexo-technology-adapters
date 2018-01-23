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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBElement;

import org.docx4j.XmlUtils;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblGrid;
import org.docx4j.wml.TblGridCol;
import org.docx4j.wml.Tc;
import org.docx4j.wml.Tr;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.doc.FlexoDocTable;
import org.openflexo.foundation.doc.FlexoDocTableRow;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * Implementation of {@link FlexoDocTable} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXTable.DocXTableImpl.class)
@XMLElement
@Imports({ @Import(DocXTableRow.class), @Import(DocXTableCell.class) })
public interface DocXTable extends DocXElement<Tbl>, FlexoDocTable<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = Tbl.class)
	public static final String TBL_KEY = "tbl";

	@Getter(value = TBL_KEY, ignoreType = true)
	// We need to clone (reference) container first, in order to have container not null when executing setTbl()
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneTbl()", cloneAfterProperty = CONTAINER_KEY)
	public Tbl getTbl();

	@Setter(TBL_KEY)
	public void setTbl(Tbl tbl);

	public Tbl cloneTbl();

	// TRICKY AREA
	// We override here the PAMELA definition of this property by declaring CloningStrategy as IGNORE
	// We do that because Tbl is beeing cloned and the setting of new Tbl value will cause creation of DocXTableRow
	// We definitely want to avoid double instanciation of DocXTableRow in a cloned paragraph !!!!
	@Override
	@CloningStrategy(StrategyType.IGNORE)
	List<FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter>> getTableRows();

	/**
	 * This is the starting point for updating {@link DocXTable} with the paragraph provided from docx4j library<br>
	 * Take care that the supplied tbl is the object we should update with, but that {@link #getTbl()} is unsafe in this context, because
	 * return former value
	 */
	public void updateFromTbl(Tbl tbl, DocXFactory factory);

	public DocXParagraph getParagraph(P p);

	public DocXFactory getFactory();

	public static abstract class DocXTableImpl extends FlexoTableImpl<DocXDocument, DocXTechnologyAdapter> implements DocXTable {

		private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
				.getLogger(DocXTableImpl.class.getPackage().getName());

		private final Map<Tr, DocXTableRow> rows = new HashMap<Tr, DocXTableRow>();

		// Factory used during initialization of DocXParagraph (either new or loaded document)
		protected DocXFactory _factory;

		public DocXTableImpl() {
			super();
		}

		@Override
		public Tbl getDocXObject() {
			return getTbl();
		}

		@Override
		public DocXFactory getFactory() {
			DocXFactory returned = null;
			if (getFlexoDocument() != null && getFlexoDocument().getFactory() != null) {
				returned = getFlexoDocument().getFactory();
			}
			if (returned == null) {
				return _factory;
			}
			return returned;
		}

		@Override
		public void setTbl(Tbl tbl) {

			// When called in cloning operation, container should NOT be null
			// That's why we use cloneAfterProperty feature
			// @CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneTbl()", cloneAfterProperty = CONTAINER_KEY)

			System.out.println("setTbl with " + tbl);
			System.out.println("getResourceData()=" + getResourceData());
			System.out.println("getContainer()=" + getContainer());
			// System.out.println("getResourceData().getResource()=" + getResourceData().getResource());

			if ((tbl == null && getTbl() != null) || (tbl != null && !tbl.equals(getTbl()))) {
				if (tbl != null && getResourceData() != null && getResourceData().getResource() != null) {
					updateFromTbl(tbl, ((DocXDocumentResource) getResourceData().getResource()).getFactory());
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXTable} with the Tbl object provided from docx4j library<br>
		 * Take care that the supplied Tbl is the object we should update with, but that {@link #getTbl()} is unsafe in this context,
		 * because return former value
		 */
		@Override
		public void updateFromTbl(Tbl tbl, DocXFactory factory) {

			performSuperSetter(TBL_KEY, tbl);
			// Take care at the previous line, since there is a risk for the notification not to be triggered,
			// if value of tbl given by getTbl() returns the new value

			List<FlexoDocTableRow> rowsToRemove = new ArrayList<>(getTableRows());

			int currentIndex = 0;

			for (Object o : tbl.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				if (o instanceof Tr) {
					DocXTableRow row = rows.get(o);
					if (row == null) {
						// System.out.println("# Create new row for " + o);
						row = factory.makeNewDocXTableRow((Tr) o);
						internallyInsertTableRowAtIndex(row, currentIndex);
					}
					else {
						// OK row was found
						if (getTableRows().indexOf(row) != currentIndex) {
							// Row was existing but is not at the right position
							internallyMoveTableRowToIndex(row, currentIndex);
						}
						else {
							// System.out.println("# Found existing paragraph for " + o);
						}
						rowsToRemove.remove(row);
					}
					currentIndex++;
				}
			}

			for (FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> row : rowsToRemove) {
				// System.out.println("# Remove row for " + e);
				internallyRemoveFromTableRows(row);
			}

			int cellCount = 0;
			// Unused int rowCount = 0;
			for (FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> row : getTableRows()) {
				// Unused rowCount++;
				cellCount = Math.max(cellCount, row.getTableCells().size());
			}

			TblGrid tblGrid = tbl.getTblGrid();
			columnWidths = new int[cellCount];
			int i = 0;
			for (TblGridCol col : tblGrid.getGridCol()) {
				columnWidths[i] = col.getW().intValue() / DocXFactory.INDENTS_MULTIPLIER;
				i++;
			}

		}

		private int[] columnWidths;

		@Override
		public int getColumnWidth(int colIndex) {
			if (colIndex < columnWidths.length) {
				return columnWidths[colIndex];
			}
			return 100;
		}

		@Override
		public String getIdentifier() {
			DocXTableCell cell = (DocXTableCell) getCell(0, 0);
			if (cell != null && cell.getElements().size() > 0) {
				return "Table" + cell.getElements().get(0).getIdentifier();
			}
			return "Table" + getIndex();
		}

		@Override
		public void setIdentifier(String identifier) {
			DocXTableCell cell = (DocXTableCell) getCell(0, 0);
			if (cell != null && cell.getElements().size() > 0) {
				cell.getElements().get(0).setIdentifier(identifier);
			}
		}

		@Override
		public Tbl cloneTbl() {
			if (getTbl() == null) {
				return null;
			}
			Tbl copiedTbl = XmlUtils.deepCopy(getTbl());

			// Change id of all paragraphs in this Tbl !!!!

			for (Object o : copiedTbl.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				if (o instanceof Tr) {
					Tr row = (Tr) o;
					for (Object o2 : row.getContent()) {
						if (o2 instanceof JAXBElement) {
							o2 = ((JAXBElement) o2).getValue();
						}
						if (o2 instanceof Tc) {
							Tc cell = (Tc) o2;
							for (Object o3 : cell.getContent()) {
								if (o3 instanceof JAXBElement) {
									o3 = ((JAXBElement) o3).getValue();
								}
								// TODO Add a parameter to ModelSlot to set identification strategy in ModelSlotInstance
								if (o3 instanceof P) {
									P paragraph = (P) o3;

									if (getFactory() != null) {
										// Translate ID
										String newId = getFactory().generateId();
										switch (getFactory().getIDStrategy()) {
											case ParaId:
												String oldId = paragraph.getParaId();
												paragraph.setParaId(newId);
												System.out.println("Change P id from " + oldId + " to " + newId);
												break;
											case Bookmark:
												for (Object o4 : paragraph.getContent()) {
													if (o4 instanceof JAXBElement) {
														o4 = ((JAXBElement) o4).getValue();
													}
													if (o4 instanceof CTBookmark) {
														CTBookmark bookmark = (CTBookmark) o4;
														if (bookmark.getName() != null && bookmark.getName().startsWith(BOOKMARK_PREFIX)) {
															String oldName = bookmark.getName();
															bookmark.setName(BOOKMARK_PREFIX + newId);
															BigInteger ID = BigInteger.valueOf(new Random().nextLong());
															bookmark.setId(ID);
															System.out.println("Translate CTBookmark ID for " + bookmark + " from name "
																	+ oldName + " to name=" + bookmark.getName());
															// setBookmark(bookmark);
														}
													}
												}
												break;
										}
									}
								}
							}
						}
					}
				}
			}

			return copiedTbl;
		}

		@Override
		public void appendToWordprocessingMLPackage(ContentAccessor parent, int index) {

			System.out.println("appendToWordprocessingMLPackage for " + this);
			System.out.println("parent: " + parent);
			System.out.println("index: " + index);
			System.out.println("tbl=" + getTbl());

			parent.getContent().add(index, getTbl());
			getFlexoDocument().setIsModified();

		}

		/**
		 * Insert row to this {@link DocXTable} at supplied index (public API).<br>
		 * Element will be inserted to underlying {@link Tbl} and {@link DocXTable} will be updated accordingly
		 */
		@Override
		public void insertTableRowAtIndex(FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> aTableRow, int index) {
			System.out.println("Add row ");
			Tbl tbl = getTbl();
			if (aTableRow instanceof DocXTableRow) {
				Tr tr = ((DocXTableRow) aTableRow).getTr();
				tbl.getContent().add(index, tr);
				internallyInsertTableRowAtIndex(aTableRow, index);
			}
			else {
				logger.warning("Unexpected row: " + aTableRow);
			}
		}

		/**
		 * Internally used to update {@link DocXTable} object according to wrapped model in the context of row inserting (calling this
		 * assume that added row is already present in underlying {@link Tbl})
		 * 
		 * @param addedElement
		 */
		private void internallyInsertTableRowAtIndex(FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> aTableRow, int index) {

			performSuperAdder(TABLE_ROWS_KEY, aTableRow, index);
			internallyHandleTableRowAdding(aTableRow);
		}

		/**
		 * Internally handle row adding
		 * 
		 * @param aTableRow
		 */
		private void internallyHandleTableRowAdding(FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> aTableRow) {

			if (aTableRow instanceof DocXTableRow) {
				DocXTableRow row = (DocXTableRow) aTableRow;
				if (row.getTr() != null) {
					rows.put(row.getTr(), row);
				}
			}
		}

		/**
		 * Move row in this {@link DocXTable} at supplied index (public API).<br>
		 * Element will be moved inside underlying {@link Tbl} and {@link DocXTable} will be updated accordingly
		 */
		@Override
		public void moveTableRowToIndex(FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> aTableRow, int index) {
			// TODO: do it in Tbl
			internallyMoveTableRowToIndex(aTableRow, index);
		}

		/**
		 * Internally used to update {@link DocXTable} object according to wrapped model in the context of row moving (calling this assume
		 * that moved row has already been moved in underlying {@link Tbl})
		 * 
		 * @param addedElement
		 */
		private void internallyMoveTableRowToIndex(FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> aTableRow, int index) {
			List<FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter>> rows = getTableRows();
			rows.remove(aTableRow);
			rows.add(index, aTableRow);
		}

		/**
		 * Add row to this {@link DocXTable} (public API).<br>
		 * Element will be added to underlying {@link Tbl} and {@link DocXTable} will be updated accordingly
		 */
		@Override
		public void addToTableRows(FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> aTableRow) {

			Tbl tbl = getTbl();
			if (aTableRow instanceof DocXTableRow) {
				Tr tr = ((DocXTableRow) aTableRow).getTr();
				tbl.getContent().add(tr);
				internallyAddToTableRows(aTableRow);
			}
			else {
				logger.warning("Unexpected row: " + aTableRow);
			}
		}

		/**
		 * Internally used to update {@link DocXTable} object according to wrapped model in the context of row adding (calling this assume
		 * that added row is already present in underlying {@link Tbl})
		 * 
		 * @param addedTableRow
		 */
		private void internallyAddToTableRows(FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> addedTableRow) {
			performSuperAdder(TABLE_ROWS_KEY, addedTableRow);
			internallyHandleTableRowAdding(addedTableRow);
		}

		/**
		 * Remove row from this {@link DocXTable} (public API).<br>
		 * Element will be removed from underlying {@link Tbl} and {@link DocXTable} will be updated accordingly
		 */
		@Override
		public void removeFromTableRows(FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> aTableRow) {

			Tbl tbl = getTbl();
			if (aTableRow instanceof DocXTableRow) {
				Tr tr = ((DocXTableRow) aTableRow).getTr();
				if (!DocXUtils.removeFromList(tr, tbl.getContent())) {
					logger.warning("Tr item not present in Tbl. Please investigate...");
				}
				internallyRemoveFromTableRows(aTableRow);
			}
			else {
				logger.warning("Unexpected row: " + aTableRow);
			}
		}

		/**
		 * Internally used to update {@link DocXTable} object according to wrapped model in the context of row removing (calling this assume
		 * that removed row has already been removed from underlying {@link Tbl})
		 * 
		 * @param removedTableRow
		 */
		private void internallyRemoveFromTableRows(FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> removedTableRow) {
			if (removedTableRow instanceof DocXTableRow) {
				DocXTableRow row = (DocXTableRow) removedTableRow;
				if (row.getTr() != null) {
					rows.remove(row.getTr());
				}
			}
			performSuperRemover(TABLE_ROWS_KEY, removedTableRow);
		}

		/**
		 * Return element identified by identifier, asserting that this element exists in the table (eg a paragraph in a cell), or null if
		 * no such element exists
		 */
		@Override
		public FlexoDocElement<DocXDocument, DocXTechnologyAdapter> getElementWithIdentifier(String identifier) {
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> returned;
			for (FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> row : getTableRows()) {
				returned = row.getElementWithIdentifier(identifier);
				if (returned != null) {
					return returned;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return DocXUtils.debugStructuredContents(this, 2);
		}

		@Override
		public DocXParagraph getParagraph(P p) {
			for (FlexoDocTableRow<DocXDocument, DocXTechnologyAdapter> c : getTableRows()) {
				DocXTableRow row = (DocXTableRow) c;
				DocXParagraph returned = row.getParagraph(p);
				if (returned != null) {
					return returned;
				}
			}
			return null;
		}

	}

}
