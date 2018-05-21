package main;

import java.io.File;
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

import model.RingChartData;

public class RingChartGenerator extends ApplicationFrame{

	private static Logger logger = LoggerFactory.getLogger("root");
	private static final long serialVersionUID = -2171235022443465432L;
	
	public RingChartGenerator(String title, List<RingChartData> dataList) {
		super(title);
		logger.debug("Start Creating Ring Chart");
        PieDataset dataset = createDataset(dataList);
        JFreeChart chart = createChart(dataset);
        ((RingPlot) chart.getPlot()).setLabelGenerator(null);
        
        JPanel rootPanel = new JPanel();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        rootPanel.add(chartPanel);
        
        setContentPane(chartPanel);
        createJPEG(chart);
        logger.debug("Finished Creating Ring Chart");
	}
	
	private PieDataset createDataset(List<RingChartData> dataList) {
		final DefaultPieDataset dataset = new DefaultPieDataset();
        
		for(RingChartData entry : dataList) {
			dataset.setValue(entry.getSecurity(), entry.getWeight());
		}
        return dataset;        
    }
	
	private JFreeChart createChart(final PieDataset dataset) {
        
        final JFreeChart chart = ChartFactory.createRingChart(
            "Ring Chart",  // chart title
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
		String path = "generatedImages/RingChart.jpeg";
	    File ringChart = new File(path); 
	    try 
	    {
			ChartUtils.saveChartAsJPEG( ringChart , chart , width , height );
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
	    logger.debug("Ring Chart saved as Image: " + path);
	}
}
