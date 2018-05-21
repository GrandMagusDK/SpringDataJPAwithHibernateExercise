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
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import model.RingChartData;

public class RingChartGenerator extends ApplicationFrame {

	private static Logger logger = LoggerFactory.getLogger("root");
	private static final long serialVersionUID = -2171235022443465432L;

	private static final int WIDTH = 500;
	private static final int HEIGHT = 400;

	public RingChartGenerator(String title, List<RingChartData> dataList) {
		super(title);
		logger.debug("Start Creating Ring Chart");
		PieDataset dataset = createDataset(dataList);
		JFreeChart chart = ChartFactory.createRingChart("Ring Chart", dataset, true, true, false);
		((RingPlot) chart.getPlot()).setLabelGenerator(null);

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
		} catch (DocumentException e) {
			logger.error("Error while creating PDf:");
			e.printStackTrace();
		}
		logger.debug("Finished Creating Ring Chart");
	}

	private PieDataset createDataset(List<RingChartData> dataList) {
		final DefaultPieDataset dataset = new DefaultPieDataset();

		for (RingChartData entry : dataList) {
			dataset.setValue(entry.getSecurity(), entry.getWeight());
		}
		return dataset;
	}

	private void createJPEG(JFreeChart chart) throws IOException {
		String path = "generatedFiles/RingChart.jpeg";
		File ringChart = new File(path);
		ChartUtils.saveChartAsJPEG(ringChart, chart, WIDTH, HEIGHT);
		logger.debug("Ring Chart saved as Image: " + path);
	}

	private void createPDF(JFreeChart chart) throws DocumentException, IOException {

		String path = "generatedFiles/RingChart.pdf";
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
			logger.debug("Ring Chart saved as PDF: " + path);
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
