/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.app1;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;
import com.ufes.app1.xml.SaidaRede;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author joever
 */
public class BuscaXML {
    public static void main(String[] args) {
        SAWebService_Service myWS = new SAWebService_Service();
        SAWebService SA = myWS.getSAWebServiceImplPort();
        String xml = SA.getXML(1, 1, "08603179786", "01/01/1900 00:00:00", "31/12/9999 23:59:59", true);
        //System.out.println(xml);
        XStream stream = new XStream(new Xpp3Driver());
        stream.autodetectAnnotations(true);
        stream.alias("InformaçõesDaColeta", SaidaRede.class);
        List<SaidaRede> mySaidasRede = (List<SaidaRede>) stream.fromXML(xml);
        DefaultCategoryDataset datasetValor = new DefaultCategoryDataset();
        Integer i = 0;
        for (SaidaRede mySaidaRede : mySaidasRede) {
            datasetValor.addValue(Double.parseDouble(mySaidaRede.getValor()), mySaidaRede.getTipoMonitoramento(), Integer.toString(i+1));
            datasetValor.addValue(Double.parseDouble(mySaidaRede.getAccuracy())*20, "Accuracy", Integer.toString(i+1));
            datasetValor.addValue(Double.parseDouble(mySaidaRede.getQoc_geral())*10, "QoC Geral", Integer.toString(i+1));
            i++;
        }
        JFreeChart grafico = ChartFactory.createLineChart("Gráfico da Rede :: "+mySaidasRede.get(0).getNomeRede(), null, "Valor", datasetValor, PlotOrientation.VERTICAL, true, true, true);
        CategoryItemRenderer renderer = grafico.getCategoryPlot().getRenderer();  
        renderer.setSeriesPaint( 0, Color.BLACK );
        renderer.setSeriesPaint( 1, Color.YELLOW );
        renderer.setSeriesPaint( 3, Color.BLUE );
        OutputStream arquivoValor;
        try {
            arquivoValor = new FileOutputStream("graficoValor.png");
            ChartUtilities.writeChartAsPNG(arquivoValor, grafico, 550, 400);
            java.awt.Desktop.getDesktop().open( new File( "graficoValor.png" ) );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BuscaXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BuscaXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
