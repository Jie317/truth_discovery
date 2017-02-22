package ema.mission.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {

	private String path;
	private Map<String, List<Couple>> donnees;

	public Excel(String path) {
		this.path = path;
		this.donnees = new HashMap<String, List<Couple>>();
	}

	public Map<String, List<Couple>> readExcel() {

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
							if(currentCell.getStringCellValue().contains("ontology"))
								continue;
							Pattern p = Pattern.compile("http://dbpedia.org/\\w+/");
							String[] item = p.split(currentCell.getStringCellValue());
							List<Couple> valeurs = new ArrayList<Couple>();
							for (int i = 1; i < item.length; i++) {
								item[i] = item[i].substring(0, item[i].length() - 1);
								if (i > 1) {

									String val = item[i].substring(0, item[i].indexOf("\t"));
									String conf = item[i].substring(item[i].indexOf("\t") + 1);
									
									try{
										val = java.net.URLDecoder.decode(val, "UTF-8");
										conf = java.net.URLDecoder.decode(conf, "UTF-8");
									}catch(UnsupportedEncodingException e){
										System.out.println(e.getMessage().toString());
									}
									
									valeurs.add(new Couple(val, conf));
								}
							}
							donnees.put(java.net.URLDecoder.decode(item[1], "UTF-8"), valeurs);
						} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {

						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return donnees;
	}

	public void display() {
		for (Entry<String, List<Couple>> entry : donnees.entrySet()) {
			System.out.print(entry.getKey() + "->");
			for (Couple c : entry.getValue()) {
				System.out.print(c.toString());
			}
			System.out.println();
		}
	}
}
