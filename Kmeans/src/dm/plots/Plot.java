package dm.plots;

import java.awt.Color;
import java.util.Iterator;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Fuente:http://www.adictosaltrabajo.com/tutoriales/tutoriales.php?pagina=graficaSeriesJFreeChart
 * @author spolex
 *
 */

public class Plot 
{
	private int[][] matrixMembership;
	
	private static Color BCK_COLOR = Color.white;
	
	private static Color GRID_COLOR = new Color(31, 87, 4);
	
	private static Plot miPlot = new Plot();
	
	private XYSeriesCollection collection= new XYSeriesCollection();
	
	private Plot(){
		
	}
	
	public int[][] getMatrixMembership() {
		return matrixMembership;
	}

	public void setMatrixMembership(int[][] matrixMembership) {
		this.matrixMembership = matrixMembership;
	}

	public static Plot getMiPlot(){
		return miPlot;
	}
	
	private void initializeCollection(){
		int[][] traspuesta=transpuesta(getMatrixMembership());
		for(int i=0;i<traspuesta.length;i++){
			XYSeries series = new XYSeries(i);
			for(int j=0;j<traspuesta[i].length;j++){
				if(traspuesta[i][j]==1)
	    		{
					System.out.println(i);
	    			double x = i;
	    			double y = j;
	    			series.add(x, y);
	    		}
			}
			this.collection.addSeries(series);
		}		
	}
	
	/**
	 * 
	 * @return
	 */
	public Color randomColor(){
		Random rand = new Random();
		
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		
		return new Color(r,g,b);
	}
	public JFreeChart plotting(String title,String X, String Y)
	{
		initializeCollection();
		final JFreeChart chart = ChartFactory.createScatterPlot(title, X, Y, 
				this.collection,
				PlotOrientation.VERTICAL, 
				false, // uso de leyenda
				false, // uso de tooltips  
				false // uso de urls
				);
		// color de fondo de la gráfica
		chart.setBackgroundPaint(BCK_COLOR);

		final XYPlot plot = (XYPlot) chart.getPlot();
		configurarPlot(plot);

		final NumberAxis domainAxis = (NumberAxis)plot.getDomainAxis();
		configurarDomainAxis(domainAxis);

		final NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
		//configurarRangeAxis(rangeAxis);

		final XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();
		configurarRendered(renderer);

		return chart;
	}
	// configuramos el contenido del gráfico (damos un color a las líneas que sirven de guía)
    private void configurarPlot (XYPlot plot) 
    {
        plot.setDomainGridlinePaint(GRID_COLOR);
        plot.setRangeGridlinePaint(GRID_COLOR);
    }
    // configuramos el eje X de la gráfica (se muestran números enteros y de uno en uno)
    private void configurarDomainAxis (NumberAxis domainAxis) {
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setTickUnit(new NumberTickUnit(1));
    }
    
    //configuramos el eje y de la gráfica (números enteros de uno en uno y rango entre 0 y 25)
    private void configurarRangeAxis (NumberAxis rangeAxis) {
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setTickUnit(new NumberTickUnit(5));
        //rangeAxis.setRange(0, 25);
    }
    // configuramos las líneas de las series (añadimos formas en los puntos y asignamos el color de cada serie)
    private void configurarRendered (XYLineAndShapeRenderer renderer) {
        Iterator<XYSeries> itr = this.collection.getSeries().iterator();
        while(itr.hasNext()){
        	int index =this.collection.indexOf(itr.next());
        	renderer.setSeriesPaint(index, randomColor());
        	renderer.setSeriesShapesVisible(index, true);
        }
    }
    /**
     * pre:the matrix isn't empty.
     * @param B
     * @return transpose matrix
     */
    private int[][] transpuesta (int B[][])
    {     
    	int i, j;
    	int[][] traspuesta = new int[B[0].length][B.length];
        for (i =0;i< B.length;i++)
        {
            for (j =0;j<B[i].length;j++){
                traspuesta[j][i]=B[i][j];
            }
            System.out.println(traspuesta[0][i]);
        }
        return traspuesta;
    }
}
