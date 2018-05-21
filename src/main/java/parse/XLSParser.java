package parse;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.PieChartData;

public class XLSParser {

	private static Logger logger = LoggerFactory.getLogger("root");

	public static List<PieChartData> parseXLS() {
		List<PieChartData> dataList = readXLS("dataFiles/piechart-data.xls");
		return dataList;
	}

	public static List<PieChartData> parseXLS(String filePath) {
		List<PieChartData> dataList = readXLS(filePath);
		return dataList;
	}

	private static List<PieChartData> readXLS(String filePath) {
		logger.debug("Start Parsing XLS File: " + filePath);

		List<PieChartData> dataList = new ArrayList<>();

		try {
			FileInputStream file = new FileInputStream(new File(filePath));

			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				String country = "";
				double weight = 0;

				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellTypeEnum()) {
					case NUMERIC:
						weight = cell.getNumericCellValue();
						break;
					case STRING:
						country = cell.getStringCellValue();
						break;
					default:
						break;
					}
				}
				if (weight > 0 && !country.isEmpty()) {
					dataList.add(new PieChartData(country, weight));
				}
			}
			file.close();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("Finished Parsing XLS File: " + filePath);
		return dataList;
	}
}
