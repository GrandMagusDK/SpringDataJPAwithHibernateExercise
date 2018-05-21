package main;

import java.io.File;
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

import model.PieChartData;

public class PieChartGenerator extends ApplicationFrame{
	
	private static Logger logger = LoggerFactory.getLogger("root");
	private static final long serialVersionUID = -2171235022443465432L;
	
	public PieChartGenerator(String title, List<PieChartData> dataList) {
		super(title);
		logger.debug("Start Creating Pie Chart");
        PieDataset dataset = createDataset(dataList);
        JFreeChart chart = createChart(dataset);
        ((PiePlot) chart.getPlot()).setLabelGenerator(null);
        
        JPanel rootPanel = new JPanel();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        rootPanel.add(chartPanel);
        
        setContentPane(chartPanel);
        createJPEG(chart);
        logger.debug("Finished Creating Pie Chart");
	}
	
	private PieDataset createDataset(List<PieChartData> dataList) {
		final DefaultPieDataset dataset = new DefaultPieDataset();
        
		for(PieChartData entry : dataList) {
			dataset.setValue(entry.getCountry(), entry.getWeight());
		}
        return dataset;        
    }
	
	private JFreeChart createChart(final PieDataset dataset) {
        
        final JFreeChart chart = ChartFactory.createPieChart(
            "Pie Chart",  // chart title
            dataset,             // data
            true,               // include legend
            true,
            false
        );

        return chart;
    }
	
	private void createJPEG(JFreeChart chart) {
		int width = 640;   
		int height = 480;
		String path = "generatedImages/PieChart.jpeg";
	    File pieChart = new File(path); 
	    try 
	    {
			ChartUtils.saveChartAsJPEG( pieChart , chart , width , height );
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	    logger.debug("Pie Chart saved as Image: " + path);
	}
}
