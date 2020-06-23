/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookRepository;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestCompareExcelSheet extends OpenflexoProjectAtRunTimeTestCase {
	protected static final Logger logger = Logger.getLogger(TestCompareExcelSheet.class.getPackage().getName());

	// Unused private static FlexoEditor editor;
	// Unused private static FlexoProject project;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(ExcelTechnologyAdapter.class);
	}

	@Test
	@TestOrder(3)
	public void testLoadExcelWorkbook() {
		ExcelTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(ExcelTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			ExcelWorkbookRepository<?> excelWorkbookRepository = technologicalAdapter.getExcelWorkbookRepository(resourceCenter);
			assertNotNull(excelWorkbookRepository);
			Collection<ExcelWorkbookResource> workbooks = excelWorkbookRepository.getAllResources();
			for (ExcelWorkbookResource excelWorkbook : workbooks) {
				if (excelWorkbook.getName().contains("exemple")) {
					System.out.println("Testing on " + excelWorkbook);
					try {
						ExcelWorkbook excelModel = excelWorkbook.loadResourceData();
						assertNotNull(excelWorkbook.getLoadedResourceData());
						assertNotNull(excelModel);
						assertEquals(excelModel, excelWorkbook.getLoadedResourceData());

						List<ExcelSheet> sheets = excelModel.getExcelSheets();

						for (ExcelSheet refSheet : sheets) {
							// System.out.println("refSheet=" + refSheet);
							for (ExcelRow refRow : refSheet.getExcelRows()) {
								// System.out.println("refRow=" + refRow);
								for (ExcelSheet sheet : sheets) {
									for (ExcelRow row : sheet.getExcelRows()) {
										if (sheet != refSheet) {
											assertNotNull(row.getRow());
											assertNotNull(row.getExcelCellAt(0));
											// System.out.println("row.getRow()=" + row.getRow() + " index=" + row.getRowIndex());
											// System.out.println("refRow.getRow()=" + refRow.getRow() + " index=" + row.getRowIndex());
											assertFalse(row.getRow() == refRow.getRow());
											assertNotSame(row.getRow(), refRow.getRow());
											// Row do have the same hashcode
											// assertNotSame(row.getRow().hashCode(), refRow.getRow().hashCode());
											assertNotSame(row, refRow);
											assertNotSame(row.hash(), refRow.hash());
											assertFalse(row == refRow);
										}
										if (row != refRow) {
											for (ExcelCell c : row.getExcelCells()) {
												for (ExcelCell rc : refRow.getExcelCells()) {
													assertNotNull(c.getCell());
													assertFalse(c.getCell() == rc.getCell());
													assertNotSame(c.getCell().hashCode(), rc.getCell().hashCode());
													assertNotSame(c.hash(), rc.hash());
													assertNotSame(c, rc);
													assertNotSame(c.hash(), rc.hash());
													assertFalse(c == rc);
												}
											}
										}
									}
								}
							}
						}

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (ResourceLoadingCancelledException e) {
						e.printStackTrace();
					} catch (FlexoException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
