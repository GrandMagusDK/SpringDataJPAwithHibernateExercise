package parse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.RingChartData;

public class CSVParser {
	private static Logger logger = LoggerFactory.getLogger("root");

	public static List<RingChartData> parseCSV() {
		List<RingChartData> dataList = readCSV("dataFiles/Ring Chart Data.csv");
		return dataList;
	}

	public static List<RingChartData> parseCSV(String filePath) {
		List<RingChartData> dataList = readCSV(filePath);
		return dataList;
	}

	private static List<RingChartData> readCSV(String filePath) {
		logger.debug("Start Parsing CSV File: " + filePath);
		List<RingChartData> dataList = new ArrayList<>();
		Reader reader;
		try {
			Iterable<CSVRecord> records;
			reader = new FileReader(new File(filePath));
			records = CSVFormat.RFC4180.withHeader().parse(reader);

			for (CSVRecord record : records) {
				RingChartData data = processLine(record.get("Security"), record.get("Weighting "));
				if (data != null)
					dataList.add(data);
			}
		} catch (IOException e) {
			logger.error("Error while parsing CSV File: ");
			e.printStackTrace();
		}
		logger.debug("Finished Parsing CSV File: " + filePath);
		return dataList;
	}

	private static RingChartData processLine(String security, String weight) {
		Double number = 0.0;
		try {
			number = Double.parseDouble(weight);
		} catch (NumberFormatException e) {
			logger.error("Error while parsing CSV File: Can't convert given String to Double.");
			return null;
		}
		return new RingChartData(security, number);
	}
}
