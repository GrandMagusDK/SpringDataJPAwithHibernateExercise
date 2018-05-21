package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import model.PieChartData;

public class PieChartGenerator extends ApplicationFrame {

	private static Logger logger = LoggerFactory.getLogger("root");
	private static final long serialVersionUID = -2171235022443465432L;

	private static final int WIDTH = 500;
	private static final int HEIGHT = 400;

	public PieChartGenerator(String title, List<PieChartData> dataList) {
		super(title);
		logger.debug("Start Creating Pie Chart");
		PieDataset dataset = createDataset(dataList);
		JFreeChart chart = ChartFactory.createPieChart("Pie Chart", dataset, true, true, false);
		((PiePlot) chart.getPlot()).setLabelGenerator(null);

		JPanel rootPanel = new JPanel();
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		rootPanel.add(chartPanel);

		setContentPane(chartPanel);
		try {
			createJPEG(chart);
			createPDF(chart);
		} catch (IOException e) {
			logger.error("Error while creating Images and PDfs:");
			e.printStackTrace();
		} catch(DocumentException e){
			logger.error("Error while creating PDf:");
			e.printStackTrace();
		}
		logger.debug("Finished Creating Pie Chart");
	}

	private PieDataset createDataset(List<PieChartData> dataList) {
		final DefaultPieDataset dataset = new DefaultPieDataset();

		for (PieChartData entry : dataList) {
			dataset.setValue(entry.getCountry(), entry.getWeight());
		}
		return dataset;
	}

	private void createJPEG(JFreeChart chart) throws IOException {
		String path = "generatedFiles/PieChart.jpeg";
		File pieChart = new File(path);
		ChartUtils.saveChartAsJPEG(pieChart, chart, WIDTH, HEIGHT);
		logger.debug("Pie Chart saved as Image: " + path);
	}

	private void createPDF(JFreeChart chart) throws DocumentException, IOException {

		String path = "generatedFiles/PieChart.pdf";
		Document document = null;
		PdfWriter writer = null;

		try {
			document = new Document();
			writer = PdfWriter.getInstance(document, new FileOutputStream(new File(path)));

			document.open();
			BufferedImage bufferedImage = chart.createBufferedImage(WIDTH, HEIGHT);
			Image image = Image.getInstance(writer, bufferedImage, 1.0f);
			document.add(image);

			document.close();
			writer.close();
			logger.debug("Pie Chart saved as PDF: " + path);
		} catch (DocumentException de) {
			throw de;
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			if (document.isOpen()) {
				document.close();
				writer.close();
			}
		}
	}
}
