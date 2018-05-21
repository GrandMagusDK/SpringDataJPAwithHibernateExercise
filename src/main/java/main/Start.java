package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import config.SpringConfig;
import dao.PieChartDataDAO;
import dao.RingChartDataDAO;
import model.PieChartData;
import model.RingChartData;
import parse.CSVParser;
import parse.XLSParser;

public class Start {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
		
		PieChartDataDAO pcdDAO = context.getBean(PieChartDataDAO.class);
		RingChartDataDAO rcdDAO = context.getBean(RingChartDataDAO.class);
		
		cleanupPrevious(pcdDAO, rcdDAO);
		
		List<PieChartData> PCDSaveList = XLSParser.parseXLS();
		List<RingChartData> RCDSaveList = CSVParser.parseCSV();
		
		pcdDAO.save(PCDSaveList);
		rcdDAO.save(RCDSaveList);
		pcdDAO.flush();
		rcdDAO.flush();
		
		List<PieChartData> PCDList = pcdDAO.findAll();
		List<RingChartData> RCDList = rcdDAO.findAll();
		
		PieChartGenerator PieChartGen = new PieChartGenerator("Pie Chart", PCDList);
		RingChartGenerator RingChartGen = new RingChartGenerator("Ring Chart", RCDList);
		
		context.close();
	}
	
	private static void cleanupPrevious(PieChartDataDAO pcdDAO, RingChartDataDAO rcdDAO) {
		//cleanup DB
		pcdDAO.deleteAllInBatch();
		rcdDAO.deleteAllInBatch();
		
		//cleanup Images
		File folder = new File("generatedImages/");
		List<File> listOfFiles = new ArrayList<>(Arrays.asList(folder.listFiles()));
		for(File file : listOfFiles) 
		{
			file.delete();
		}
		
		//cleanup log file
		File file = new File("Log.log");
		file.deleteOnExit();
 	}
}
