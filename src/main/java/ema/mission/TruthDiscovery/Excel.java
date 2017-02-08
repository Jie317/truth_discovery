package ema.mission.TruthDiscovery;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class Excel {

	private String path;

	public Excel(String path) {
		this.path = path;
	}

	public void readExcel() {
		FileInputStream excelFile;
		try {
			excelFile = new FileInputStream(new File(this.path));
			Workbook workbook;
			try {
				workbook = new XSSFWorkbook(excelFile);
				Sheet datatypeSheet = workbook.getSheetAt(0);
				Iterator<Row> iterator = datatypeSheet.iterator();

				while (iterator.hasNext()) {

					Row currentRow = iterator.next();
					Iterator<Cell> cellIterator = currentRow.iterator();

					while (cellIterator.hasNext()) {

						Cell currentCell = cellIterator.next();
						// getCellTypeEnum shown as deprecated for version 3.15
						// getCellTypeEnum ill be renamed to getCellType
						// starting from
						// version 4.0
						if (currentCell.getCellTypeEnum() == CellType.STRING) {
							System.out.print(currentCell.getStringCellValue() + "--");
						} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
							System.out.print(currentCell.getNumericCellValue() + "--");
						}

					}
					System.out.println();

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
