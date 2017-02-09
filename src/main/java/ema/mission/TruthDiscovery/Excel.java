package ema.mission.TruthDiscovery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

	private String path;
	private Map<String, Couple> donnees;

	public Excel(String path) {
		this.path = path;
		this.donnees = new HashMap<String, Couple>();
	}

	@SuppressWarnings("deprecation")
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
							Pattern p = Pattern.compile("http://dbpedia.org/\\w+/");
							String[] item = p.split(currentCell.getStringCellValue());
							for (int i = 1; i < item.length; i++) {
								item[i] = item[i].substring(0, item[i].length() - 1);
								if(i>1){
									String val = item[i].substring(0, item[i].indexOf("\t"));
									String conf =item[i].substring( item[i].indexOf("\t")+1);
									System.out.println(conf+"-");
									donnees.put(item[1], new Couple(val,Float.parseFloat(conf)));
								}
							}
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
