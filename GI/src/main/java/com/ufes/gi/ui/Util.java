/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufes.gi.ui;

import java.awt.Color;
import java.text.ParseException;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Joever
 */
public class Util {
    public static int Busca(int codigo, Integer[] vCodigo) {
        int n = 0;
        while(n < vCodigo.length) {
            if (vCodigo[n].equals(codigo)) {
                return n;
            }
            n++;
        }
        return -1;
    }
    
    public static int Busca(String text, String[] vNome) {
        int n = 0;
        while(n < vNome.length) {
            if (vNome[n].equals(text)) {
                return n;
            }
            n++;
        }
        return -1;
    }
    
    public static void centralizar(JInternalFrame janela, JDesktopPane Desktop) {
        int x = (int) Desktop.getWidth() / 2;
        int y = (int) Desktop.getHeight() / 2;
        janela.setLocation(x - (janela.getWidth()/2), y - (janela.getHeight()/2));
    }
    
    public static void tamanhoGrafico(JPanel grafico, ChartPanel cPanel) {
        cPanel.setSize(grafico.getWidth(),grafico.getHeight());
        cPanel.setVisible(true);
        grafico.removeAll();
        grafico.add(cPanel);
        grafico.revalidate();
        grafico.repaint();
    }
    
    public static ChartPanel criaGrafico(JPanel grafico, int nRegistro, String nTabela, String Titulo, int cTipo, String dTipo, int tGrafico, String Periodo[]) throws ParseException {
        if (tGrafico == 1) {
            CategoryDataset dados = createDataset(nRegistro,nTabela,cTipo,Periodo);
            if (dados != null) {
                String titulo = "Gráfico da " + Titulo;
                String eixoy = dTipo;
                String txt_legenda = null;
                boolean legenda = false;
                boolean tooltips = true;
                boolean urls = true;
                JFreeChart graf = ChartFactory.createBarChart3D(titulo, txt_legenda, eixoy, dados, PlotOrientation.VERTICAL, legenda, tooltips, urls);
                ChartPanel myChartPanel = new ChartPanel(graf, true);
                tamanhoGrafico(grafico,myChartPanel);
                return myChartPanel;
            }
        }
        else if (tGrafico == 2){
            XYSeriesCollection dados = createSeriesCollection(nRegistro,nTabela,cTipo,Periodo);
            if (dados != null) {
                String titulo = "Gráfico da " + Titulo;
                String eixoy = dTipo;
                String txt_legenda = null;
                boolean legenda = true;
                boolean tooltips = true;
                boolean urls = true;
                JFreeChart graf = ChartFactory.createXYLineChart(titulo, txt_legenda, eixoy, dados, PlotOrientation.VERTICAL, legenda, tooltips, urls);
                ChartPanel myChartPanel = new ChartPanel(graf, true);
                tamanhoGrafico(grafico,myChartPanel);
                return myChartPanel;
            }
        }
        else if (tGrafico == 3) {
            DefaultPieDataset dados = createPieDataset(nRegistro,nTabela,cTipo,Periodo);
            if (dados != null) {
                String titulo = "Gráfico da " + Titulo;
                boolean legenda = true;
                boolean tooltips = true;
                boolean urls = true;
                JFreeChart graf = ChartFactory.createPieChart(titulo, dados, legenda, tooltips, urls);
                ChartPanel myChartPanel = new ChartPanel(graf, true);
                tamanhoGrafico(grafico,myChartPanel);
                return myChartPanel;
            }
        }
        grafico.removeAll();
        grafico.revalidate();
        grafico.repaint();
        return null;
    }
    
    private static CategoryDataset createDataset(int nRegistro, String nTabela, int cTipo, String Periodo[]) throws ParseException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int i = 0;
        String valores[][];
        if (Periodo == null)
            valores = null;//BancoUtil.cTabela(nTabela,cTipo,nRegistro);
        else
            valores = null;//BancoUtil.cTabela(nTabela,cTipo,nRegistro,Periodo);
        if (valores != null){
            while (i < valores.length) {
                dataset.addValue(Double.parseDouble(valores[i][1]),Integer.toString(i),"registro");
                i++;
            }
            return dataset;
        }
        return null;
    }

    private static XYSeriesCollection createSeriesCollection(int nRegistro, String nTabela, int cTipo, String Periodo[]) throws ParseException {
        XYSeries series = new XYSeries("");
        XYSeriesCollection dataset = new XYSeriesCollection();
        int i = 0;
        String valores[][];
        if (Periodo == null)
            valores = null;//BancoUtil.cTabela(nTabela,cTipo,nRegistro);
        else
            valores = null;//BancoUtil.cTabela(nTabela,cTipo,nRegistro,Periodo);
        if (valores != null){
            while (i < valores.length) {
                series.add(i,Double.parseDouble(valores[i][1]));
                i++;
            }
            dataset.addSeries(series);
            return dataset;
        }
        return null;
    }

    private static DefaultPieDataset createPieDataset(int nRegistro, String nTabela, int cTipo, String Periodo[]) throws ParseException {
        DefaultPieDataset dados = new DefaultPieDataset();
        int i = 0;
        String valores[][];
        if (Periodo == null)
            valores = null;//BancoUtil.cTabela(nTabela,cTipo,nRegistro,true);
        else
            valores = null;//BancoUtil.cTabela(nTabela,cTipo,nRegistro,true,Periodo);
        if (valores != null){
            while (i < valores.length) {
                dados.setValue(valores[i][0],Integer.parseInt(valores[i][1]));
                i++;
            }
            return dados;
        }
        return null;
    }

    public static void Seleciona(String[][] ListOrigem, JComboBox lTipoOrigem, String[][] ListTipoValida, JComboBox lTipoTValida, String[] Tipo) {
        String CodigoOrigem = Tipo[1];
        String CodigoTipoValida = Tipo[2];
        int i = 0;
        while (i < ListOrigem.length){
            if (ListOrigem[i][0].equals(CodigoOrigem)){
                lTipoOrigem.setSelectedIndex(i);
                break;
            }
            i++;
        }
        i = 0;
        while (i < ListTipoValida.length){
            if (ListTipoValida[i][0].equals(CodigoTipoValida)){
                lTipoTValida.setSelectedIndex(i);
                break;
            }
            i++;
        }        
    }
    
    public static ChartPanel criaGraficoMetricas(JPanel grafico, int nRegistro, String nTabela, String Titulo, int cTipo, String dTipo, int tGrafico, boolean metricas[], String Periodo[]) throws ParseException {
        switch (tGrafico) {
            case 1:
                {
                    CategoryDataset dados = createDatasetM(nRegistro,nTabela,cTipo,metricas,Periodo);
                    if (dados != null) {
                        String titulo = Titulo;
                        String eixoy = dTipo;
                        String txt_legenda = null;
                        boolean legenda = false;
                        boolean tooltips = true;
                        boolean urls = true;
                        JFreeChart graf = ChartFactory.createBarChart3D(titulo, txt_legenda, eixoy, dados, PlotOrientation.VERTICAL, legenda, tooltips, urls);
                        ChartPanel myChartPanel = new ChartPanel(graf, true);
                        tamanhoGrafico(grafico,myChartPanel);
                        return myChartPanel;
                    }       break;
                }
            case 2:
                {
                    DefaultCategoryDataset dados = CreateCategoryDataset(nRegistro,nTabela,cTipo,metricas,Periodo);
                    if (dados != null) {
                        String titulo = Titulo;
                        String eixoy = dTipo;
                        String txt_legenda = null;
                        boolean legenda = true;
                        boolean tooltips = true;
                        boolean urls = true;
                        JFreeChart graf = ChartFactory.createLineChart(titulo, txt_legenda, eixoy, dados, PlotOrientation.VERTICAL, legenda, tooltips, urls);
                        CategoryItemRenderer renderer = graf.getCategoryPlot().getRenderer();
                        renderer.setSeriesPaint( 0, Color.BLACK );
                        renderer.setSeriesPaint( 1, Color.YELLOW );
                        renderer.setSeriesPaint( 2, Color.RED );
                        renderer.setSeriesPaint( 3, Color.BLUE );
                        renderer.setSeriesPaint( 4, Color.GREEN );
                        renderer.setSeriesPaint( 5, Color.WHITE );
                        ChartPanel myChartPanel = new ChartPanel(graf, true);
                        tamanhoGrafico(grafico,myChartPanel);
                        return myChartPanel;
                    }       break;
                }
            case 3:
                {
                    DefaultPieDataset dados = null;//createPieDatasetM(nRegistro,nTabela,cTipo,metricas);
                    if (dados != null) {
                        String titulo = Titulo;
                        boolean legenda = true;
                        boolean tooltips = true;
                        boolean urls = true;
                        JFreeChart graf = ChartFactory.createPieChart(titulo, dados, legenda, tooltips, urls);
                        ChartPanel myChartPanel = new ChartPanel(graf, true);
                        tamanhoGrafico(grafico,myChartPanel);
                        return myChartPanel;
                    }       break;
                }
            default:
                break;
        }
        grafico.removeAll();
        grafico.revalidate();
        grafico.repaint();
        return null;
    }
    
    private static CategoryDataset createDatasetM(int nRegistro, String nTabela, int cTipo, boolean metricas[], String Periodo[]) throws ParseException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int i = 0;
        String valores[][];
        if (Periodo == null)
            valores = null;//BancoUtil.cTabelaMetricas(nTabela,cTipo,nRegistro);
        else
            valores = null;//BancoUtil.cTabelaMetricas(nTabela,cTipo,nRegistro,Periodo);
        if (valores != null){
            while (i < valores.length) {
                if (metricas[0])
                    dataset.addValue(Integer.parseInt(valores[i][0]),"COVERAGE",Integer.toString(i+1));
                if (metricas[1])
                    dataset.addValue(Double.parseDouble(valores[i][1]),"UP_TO_DATENESS",Integer.toString(i+1));
                if (metricas[2])
                    dataset.addValue(Integer.parseInt(valores[i][2]),"ACCURACY",Integer.toString(i+1));
                if (metricas[3])
                    dataset.addValue(Integer.parseInt(valores[i][3]),"FREQUENCY",Integer.toString(i+1));
                if (metricas[4])
                    dataset.addValue(Integer.parseInt(valores[i][4]),"SIGNIFICANCE",Integer.toString(i+1));
                if (metricas[5])
                    dataset.addValue(Double.parseDouble(valores[i][5]),"QOC_GERAL",Integer.toString(i+1));
                i++;
            }
            return dataset;
        }
        return null;
    }
    
    private static DefaultCategoryDataset CreateCategoryDataset(int nRegistro, String nTabela, int cTipo, boolean metricas[], String Periodo[]) throws ParseException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int i = 0;
        String valores[][];
        if (Periodo == null)
            valores = null;//BancoUtil.cTabelaMetricas(nTabela,cTipo,nRegistro);
        else
            valores = null;//BancoUtil.cTabelaMetricas(nTabela,cTipo,nRegistro,Periodo);
        if (valores != null){
            while (i < valores.length) {
                if (metricas[0])
                    dataset.addValue(Integer.parseInt(valores[i][0]),"COVERAGE",Integer.toString(i+1));
                if (metricas[1])
                    dataset.addValue(Double.parseDouble(valores[i][1]),"UP_TO_DATENESS",Integer.toString(i+1));
                if (metricas[2])
                    dataset.addValue(Integer.parseInt(valores[i][2]),"ACCURACY",Integer.toString(i+1));
                if (metricas[3])
                    dataset.addValue(Integer.parseInt(valores[i][3]),"FREQUENCY",Integer.toString(i+1));
                if (metricas[4])
                    dataset.addValue(Integer.parseInt(valores[i][4]),"SIGNIFICANCE",Integer.toString(i+1));
                if (metricas[5])
                    dataset.addValue(Double.parseDouble(valores[i][5]),"QOC_GERAL",Integer.toString(i+1));
                i++;
            }
            return dataset;
        }
        return null;
    }

    private static DefaultPieDataset createPieDatasetM(int nRegistro, String nTabela, int cTipo, boolean metricas[]) {
        DefaultPieDataset dados = new DefaultPieDataset();
        int i = 0;
        String valores[][];
        valores = null;//BancoUtil.cTabelaMetricas(nTabela,cTipo,nRegistro);
        if (valores != null){
            while (i < valores.length) {
                if (metricas[0])
                    dados.setValue("COVERAGE",Integer.parseInt(valores[i][0]));
                if (metricas[1])
                    dados.setValue("UP_TO_DATENESS",Double.parseDouble(valores[i][1]));
                if (metricas[2])
                    dados.setValue("ACCURACY",Integer.parseInt(valores[i][2]));
                if (metricas[3])
                    dados.setValue("FREQUENCY",Integer.parseInt(valores[i][3]));
                if (metricas[4])
                    dados.setValue("SIGNIFICANCE",Integer.parseInt(valores[i][4]));
                if (metricas[5])
                    dados.setValue("QOC_GERAL",Double.parseDouble(valores[i][5]));
                i++;
            }
            return dados;
        }
        return null;
    }
}
