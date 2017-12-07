/*
 * Copyright (c) 2013-2017, Openflexo
 *
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
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
 *           Additional permission under GNU GPL version 3 section 7
 *           If you modify this Program, or any covered work, by linking or
 *           combining it with software containing parts covered by the terms
 *           of EPL 1.0, the licensors of this Program grant you additional permission
 *           to convey the resulting work.
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

package org.openflexo.technologyadapter.excel.semantics.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelCellRange;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.semantics.fml.SEDataAreaRole;
import org.openflexo.toolbox.StringUtils;

/**
 * Run-time concept referencing all {@link SEFlexoConceptInstance} encoded in a data area in an excel workbook<br>
 * Model concept is {@link SEDataAreaRole}
 * 
 */
public class SEDataArea<FCI extends SEFlexoConceptInstance> extends ArrayList<FCI> {

	private final SEDataAreaRole dataAreaRole;
	private final SEVirtualModelInstance virtualModelInstance;
	private final FlexoConceptInstance container;
	private ExcelCellRange cellRange;

	// All FCI are stored according to the index of their row
	// private final List<FCI> instances = new ArrayList<>();

	public SEDataArea(SEDataAreaRole dataAreaRole, SEVirtualModelInstance virtualModelInstance, FlexoConceptInstance container) {
		super();
		this.dataAreaRole = dataAreaRole;
		this.virtualModelInstance = virtualModelInstance;
		this.container = container;
	}

	public ExcelCellRange getCellRange() {
		return cellRange;
	}

	public SEDataAreaRole getDataAreaRole() {
		return dataAreaRole;
	}

	public FlexoConcept getFlexoConceptType() {
		return getDataAreaRole().getFlexoConceptType();
	}

	public ExcelTechnologyAdapter getTechnologyAdapter() {
		return dataAreaRole.getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(ExcelTechnologyAdapter.class);
	}

	/*public List<FCI> getInstances() {
		return instances;
	}*/

	public FCI getFlexoConceptInstance(Row row) {

		int foundIndex = indexedBinarySearch(row);
		if (foundIndex >= 0) {
			return get(foundIndex);
		}
		return null;
	}

	public FCI insertFlexoConceptInstanceAtIndex(Integer index) {

		int insertedRowIndex;
		if (index != null && index >= 0) {
			insertedRowIndex = getCellRange().getTopLeftCell().getRowIndex() + index;
		}
		else {
			// Last row
			insertedRowIndex = getCellRange().getBottomRightCell().getRowIndex() + 1;
		}

		ExcelSheet sheet = getCellRange().getExcelSheet();
		ExcelRow excelRow = sheet.insertRowAt(insertedRowIndex);

		int startColIndex = getCellRange().getTopLeftCell().getColumnIndex();
		int endColIndex = getCellRange().getBottomRightCell().getColumnIndex();
		for (int i = startColIndex; i <= endColIndex; i++) {
			excelRow.createCellAt(i);
		}

		FCI returned = makeNewInstance(excelRow.getRow());

		return returned;

	}

	public void removeFlexoConceptInstance(FCI fci) {

		Row deletedRow = fci.getRowSupportObject();
		ExcelSheet sheet = getCellRange().getExcelSheet();
		sheet.removeRowAt(deletedRow.getRowNum());

		removeInstance(fci);
	}

	public boolean update() {

		boolean isModified = false;

		try {
			cellRange = buildCellRange();
		} catch (ExcelMappingException e) {
			e.printStackTrace();
		}

		List<FCI> instancesToRemove = new ArrayList<>(this);

		int startRowIndex = cellRange.getTopLeftCell().getRowIndex();
		int endRowIndex = cellRange.getBottomRightCell().getRowIndex();

		for (int currentIndex = startRowIndex; currentIndex <= endRowIndex; currentIndex++) {
			ExcelRow excelRow = cellRange.getExcelSheet().getRowAt(currentIndex);
			// int fciIndex = currentIndex - startRowIndex;
			// Trying to find existing concept
			FCI seFCI = getFlexoConceptInstance(excelRow.getRow());
			if (seFCI != null) {
				instancesToRemove.remove(seFCI);
			}
			else {
				// Create new instance
				seFCI = makeNewInstance(excelRow.getRow());
				isModified = true;
			}
		}
		// What about instances to be deleted
		for (FCI deletedInstance : instancesToRemove) {
			removeInstance(deletedInstance);
			deletedInstance.delete();
			isModified = true;
		}

		return isModified;
	}

	@SuppressWarnings("unchecked")
	private FCI makeNewInstance(Row row) {
		FCI newFCI = (FCI) virtualModelInstance.makeNewFlexoConceptInstance(getFlexoConceptType(), container);
		newFCI.setRowSupportObject(row);
		int insertionIndex = insertionPoint(row);
		add(insertionIndex, newFCI);
		virtualModelInstance.addToFlexoConceptInstances(newFCI);
		if (container != null) {
			container.addToEmbeddedFlexoConceptInstances(newFCI);
		}
		return newFCI;
	}

	private void removeInstance(FCI fci) {
		remove(fci);
		if (container != null) {
			container.removeFromEmbeddedFlexoConceptInstances(fci);
		}
		virtualModelInstance.removeFromFlexoConceptInstances(fci);
	}

	private int indexedBinarySearch(Row row) {
		int low = 0;
		int high = size() - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			SEFlexoConceptInstance midVal = get(mid);
			// int cmp = midVal.compareTo(key);
			int cmp = midVal.getRowSupportObject().getRowNum() - row.getRowNum();

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found
	}

	private int insertionPoint(Row row) {
		int low = 0;
		int high = size() - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			SEFlexoConceptInstance midVal = get(mid);
			// int cmp = midVal.compareTo(key);
			int cmp = midVal.getRowSupportObject().getRowNum() - row.getRowNum();

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return -1; // row was found
		}
		return high + 1; // this is the insertion point
	}

	private ExcelCellRange buildCellRange() throws ExcelMappingException {
		// System.out.println("Computing cell range for " + dataAreaRole.getCellRange());
		// System.out.println("Template: " + dataAreaRole.getCellRange().getExcelWorkbook().getResource());
		// System.out.println("Working on: " + getExcelWorkbookResource());

		if (virtualModelInstance.getExcelWorkbookResource() == null) {
			throw new ExcelMappingException("Could not find workbook resource");
		}

		ExcelCellRange templateRange = dataAreaRole.getCellRange();

		System.out.println("getExcelWorkbookResource=" + virtualModelInstance.getExcelWorkbookResource());
		System.out.println("dataAreaRole=" + dataAreaRole);
		System.out.println("templateRange=" + templateRange);

		ExcelSheet sheet = virtualModelInstance.getExcelWorkbookResource().getExcelWorkbook()
				.getExcelSheetByName(templateRange.getExcelSheet().getName());
		if (sheet == null) {
			throw new ExcelMappingException("Could not find sheet: " + templateRange.getExcelSheet().getName());
		}

		int startIndex = templateRange.getTopLeftCell().getRowIndex();
		int currentRowIndex = startIndex;
		while (isSignificative(currentRowIndex, sheet)) {
			currentRowIndex++;
		}

		ExcelCell topLeftCell = sheet.getCellAt(startIndex, templateRange.getTopLeftCell().getColumnIndex());
		ExcelCell bottomRightCell = sheet.getCellAt(currentRowIndex - 1, templateRange.getBottomRightCell().getColumnIndex());
		return sheet.getExcelWorkbook().getFactory().makeExcelCellRange(topLeftCell, bottomRightCell);
	}

	private boolean isSignificative(int rowIndex, ExcelSheet sheet) {
		if (sheet.getExcelRows().size() <= rowIndex) {
			// This row does not exists, do not create it
			return false;
		}
		ExcelRow row = sheet.getRowAt(rowIndex);
		ExcelCellRange templateRange = dataAreaRole.getCellRange();
		for (int i = templateRange.getTopLeftCell().getColumnIndex(); i <= templateRange.getBottomRightCell().getColumnIndex(); i++) {
			ExcelCell cell = row.getExcelCellAt(i);
			if (StringUtils.isNotEmpty(cell.getCellValueAsString())) {
				return true;
			}
		}
		return false;
	}

	/*
	
	public static void main(String[] args) {
	
		List<StoredObject> l = new ArrayList<>();
	
		Object o3, o4, o5, o8, o9;
		StoredObject so3 = new StoredObject(3, o3 = new Object());
		StoredObject so4 = new StoredObject(4, o4 = new Object());
		StoredObject so5 = new StoredObject(5, o5 = new Object());
		StoredObject so8 = new StoredObject(8, o8 = new Object());
		StoredObject so9 = new StoredObject(9, o9 = new Object());
	
		l.add(so3);
		l.add(so4);
		l.add(so5);
		l.add(so8);
		l.add(so9);
	
		int id3 = getSearchedIndex(so3, l);
		int id4 = getSearchedIndex(so4, l);
		int id5 = getSearchedIndex(so5, l);
		int id8 = getSearchedIndex(so8, l);
		int id9 = getSearchedIndex(so9, l);
	
		StoredObject so1 = new StoredObject(1, new Object());
		int id1 = insertionPoint(so1, l);
		System.out.println("Insertion point for 1: " + id1);
		l.add(id1, so1);
		System.out.println("list = " + l);
	
		StoredObject so7 = new StoredObject(7, new Object());
		int id7 = insertionPoint(so7, l);
		System.out.println("Insertion point for 7: " + id7);
		l.add(id7, so7);
		System.out.println("list = " + l);
	
		StoredObject so6 = new StoredObject(6, new Object());
		int id6 = insertionPoint(so6, l);
		System.out.println("Insertion point for 6: " + id6);
		l.add(id6, so6);
		System.out.println("list = " + l);
	
		StoredObject so10 = new StoredObject(10, new Object());
		int id10 = insertionPoint(so10, l);
		System.out.println("Insertion point for 10: " + id10);
		l.add(id10, so10);
		System.out.println("list = " + l);
	
		StoredObject so2 = new StoredObject(2, new Object());
		int id2 = insertionPoint(so2, l);
		System.out.println("Insertion point for 2: " + id2);
		l.add(id2, so2);
		System.out.println("list = " + l);
	
	}
	
	private static int insertionPoint(StoredObject row, List<StoredObject> list) {
		int low = 0;
		int high = list.size() - 1;
		int mid = 0;
	
		while (low <= high) {
			mid = (low + high) >>> 1;
			StoredObject midVal = list.get(mid);
			// int cmp = midVal.compareTo(key);
			int cmp = midVal.index - row.index;
	
			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		System.out.println("low=" + low + " high=" + high);
		return high + 1; // key not found
	}
	
	public static int getSearchedIndex(StoredObject o, List<StoredObject> list) {
		int idx = Collections.binarySearch(list, o, new Comparator<StoredObject>() {
			@Override
			public int compare(StoredObject o1, StoredObject o2) {
				return o1.index - o2.index;
			}
		});
		if (idx >= 0) {
			System.out.println("idx=" + idx + " / " + list.get(idx).index);
		}
		else {
			System.out.println("idx=" + idx + " / ???");
		}
		return idx;
	}
	
	public static class StoredObject {
		int index;
		Object object;
	
		public StoredObject(int index, Object object) {
			super();
			this.index = index;
			this.object = object;
		}
	
		@Override
		public String toString() {
			return "O" + index;
		}
	}
	
	 */
}
