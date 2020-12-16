/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2014-2019 Bruno Augusto Nassif Travencolo.
 * All Rights Reserved.
 *
 * This file is part of DyNetVis Project (DyNetVis).
 *
 * How to cite this software:
 *  
@inproceedings{Linhares:2017:DSV:3019612.3019686,
 author = {Linhares, Claudio D. G. and Traven\c{c}olo, Bruno A. N. and Paiva, Jose Gustavo S. and Rocha, Luis E. C.},
 title = {DyNetVis: A System for Visualization of Dynamic Networks},
 booktitle = {Proceedings of the Symposium on Applied Computing},
 series = {SAC '17},
 year = {2017},
 isbn = {978-1-4503-4486-9},
 location = {Marrakech, Morocco},
 pages = {187--194},
 numpages = {8},
 url = {http://doi.acm.org/10.1145/3019612.3019686},
 doi = {10.1145/3019612.3019686},
 acmid = {3019686},
 publisher = {ACM},
 address = {New York, NY, USA},
 keywords = {complex networks, dynamic graph visualization, dynamic networks, recurrent neighbors, temporal activity map},
} 
 *  
 * DyNetVis is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * DyNetVis is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Lab of Complex Network Visualization at 
 * Federal University of Uberlândia, Brazil - (https://sites.google.com/view/dynetvis/team?authuser=0). 
 * The initial developer of the original code is Claudio D. G. Linhares <claudiodgl@gmail.com>.
 *
 * Contributor(s): Jean R. Ponciano -- jeanrobertop@gmail.com, Luis E. C. Rocha -- luis.rocha@ugent.be, 
 * José Gustavo S. Paiva -- gustavo@ufu.br, Bruno A. N. Travençolo -- travencolo@gmail.com
 *
 * You should have received a copy of the GNU General Public License along 
 * with DyNetVis. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package layout;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import communities.SLM_Louvain;
import forms.MainForm;
import forms.OpenDataSetDialog;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import visualizationbasics.color.ColorScale;
import visualizationbasics.color.CustomColor;

/**
 *
 * @author Claudio Linhares
 */
public class NetLayoutMatrix extends Observable implements Runnable{
    public mxGraphComponent graphComponent;
    public ArrayList<ArrayList> matrizDataInline;
    //public Integer[][][] cubicMatrix;
    mxGraph graph, graphWithEdges;
    String styleNode;
    public int uniqueIds;
    public int lastTime;
    int colorScaleMaximumValue = 10;
    int tamanho_maximo_no = 50;
    int tamanho_minimo_no = 10;
    int espacamento = tamanho_maximo_no + 10;
    double espaco_inicial = 20;
    int alinhar_nos = 1;
    String[] colorsTable;
    public MainForm f;
    HashMap<Integer,Integer> nodeOrder = new HashMap();
    HashMap<Integer,Integer> nodeDegree = new HashMap();
    HashMap<String,Integer> edgeAging = new HashMap();
    HashMap<Integer,Integer> nodeOriginalId = new HashMap(); //key: converted sequencial id (0,1,2...) value: Original id;
    List<Integer> nodeOrderingVector = new ArrayList();
    HashMap<Integer, List<InlineNodeAttribute>> comunidades, comunidadesLouvain, comunidadesInfomap;
    private int maximumAgingValue = 1, divisorAgingValue = 100/maximumAgingValue;
    ArrayList<InlineNodeAttribute> listAttNodes;
    ArrayList listAllEdges;
    
    public NetLayoutMatrix()
    {
    
    }
    
    public void NetLayoutMatrix(ArrayList<ArrayList> matrizDataInline, HashMap<Integer,Integer> vetorIdNodesNormalizados)
    {
        this.matrizDataInline = matrizDataInline;
        defineStyles();
        
        graph = new mxGraph();
        graphWithEdges = new mxGraph();
        graph.getModel().beginUpdate();  
        graphWithEdges.getModel().beginUpdate(); 

        listAttNodes = new ArrayList<InlineNodeAttribute>();
        listAllEdges = new ArrayList();
        InlineNodeAttribute attConvertido;

        try
        {
            
            ArrayList<Integer> lastLine = matrizDataInline.get(matrizDataInline.size()-1);
            lastTime = lastLine.get(2); //get Last Time in the network
           
            //Create cubic matrices
            /*cubicMatrix = new Integer[lastTime+1][uniqueIds][uniqueIds];

            for(int i=0;i<lastTime+1;++i){
		for(int j=0;j<uniqueIds;++j){
                    for(int k=0;k<uniqueIds;++k){
                        cubicMatrix[i][j][k] = 0;
                    }
		}
            }*/
            
            int positionNodeOrdering = 0;
            //tem q verificar se os nos e o tempo comecam em 0 e sao sequenciais para esse codigo funcionar
            for(ArrayList<Integer> column : matrizDataInline){
                
                //cubicMatrix[column.get(2)][column.get(0)-1][column.get(1)-1] = 1;
                //cubicMatrix[column.get(2)][column.get(1)-1][column.get(0)-1] = 1;
                
                nodeOriginalId.put(vetorIdNodesNormalizados.get(column.get(0)),column.get(0));
                nodeOriginalId.put(vetorIdNodesNormalizados.get(column.get(1)),column.get(1));
                
                
                Integer node1 = vetorIdNodesNormalizados.get(column.get(0));
                Integer node2 = vetorIdNodesNormalizados.get(column.get(1));
                Integer time = column.get(2);
                
                if(!nodeOrder.containsKey(node1))
                {
                    nodeOrder.put(node1, positionNodeOrdering);
                    nodeOrderingVector.add(node1);
                    nodeDegree.put(node1, 1);
                    positionNodeOrdering++;
                }
                else
                {
                    nodeDegree.put(node1, nodeDegree.get(node1)+1);
                }
                if(!nodeOrder.containsKey(node2))
                {
                    nodeOrder.put(node2, positionNodeOrdering);
                    nodeOrderingVector.add(node2);
                    nodeDegree.put(node2, 1);
                    positionNodeOrdering++;
                }
                else
                {
                    nodeDegree.put(node2, nodeDegree.get(node2)+1);
                }
                if(!edgeAging.containsKey(node1+" "+node2))
                {
                    edgeAging.put(node1+" "+node2, 0);
                    edgeAging.put(node2+" "+node1, 0);
                }
                
                //Source node
                
                attConvertido = new InlineNodeAttribute(0, (int) node1, 0, 0, node1+"", false, false, false);
                if(!listAttNodesLocalJaTemNo(listAttNodes, (int) node1))
                    listAttNodes.add(attConvertido);
                     
                attConvertido = new InlineNodeAttribute(0, (int) node2, 0, 0, node2+"", false, false, false);
                if(!listAttNodesLocalJaTemNo(listAttNodes, (int) node2))
                    listAttNodes.add(attConvertido);
                
                
                InlineNodeAttribute attEdge = new InlineNodeAttribute(time,node1,time,0,"",false,false,false);
                attEdge.setIsEdge(true,node1,node2,0,false);
                
                Object edge = graphWithEdges.insertVertex(null, time +" "+ node1+" "+node2, attEdge, 0, 0, 0, 0);
               
                listAllEdges.add(edge);
                
                int insize = 55;
                
                MatrixNodeAttribute att,att2;
                mxCell vertice,vertice2, v1_indice, v2_indice;
                
                
                vertice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(node1+" "+node2);
                vertice2 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(node2+" "+node1);
                if(vertice == null && vertice2 == null)
                {
                    
                    //ColorScale colorScale = new GrayScale();
                    ColorScale colorScale = new CustomColor(Color.WHITE,Color.BLACK);

                    double vMin = 0;
                    double vMax = colorScaleMaximumValue;

                    //double incolor = ((double)tokens[i] - vMin) / (vMax - vMin);
                    double incolor = 1;

                    Color colorNode = colorScale.getColor((float)incolor);
                    String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                    att = new MatrixNodeAttribute(node1+" "+node2, node1+1,node2+1,false, true);

                    ArrayList<Integer> times = att.getListTime();
                    if(!times.contains(time))
                        times.add(time);
                    att.setListTime(times);
                    
                    
                    att2 = new MatrixNodeAttribute(node2+" "+node1, node2+1,node1+1,false, false);
                    
                    ArrayList<Integer> times2 = att2.getListTime();
                    if(!times2.contains(time))
                        times2.add(time);
                    att2.setListTime(times2);
                    
                    
                    
                    double y = alinhar_nos + espaco_inicial + espacamento*(node1+1);
                    double x = alinhar_nos + espaco_inicial + espacamento*(node2+1);
                    
                    if(att.getY_original() >= att.getX_original())
                        att.setUpperTriangularNode(true);
                    else
                        att.setUpperTriangularNode(false);
                    
                    if(att2.getY_original() >= att2.getX_original())
                        att2.setUpperTriangularNode(true);
                    else
                        att2.setUpperTriangularNode(false);
                    
                    graph.insertVertex(null, node1+" "+node2, att , x, y, insize,insize, styleShape+mxConstants.STYLE_NOLABEL+"=1;"+mxConstants.STYLE_FONTSIZE+"=15;");
                    graph.insertVertex(null, node2+" "+node1, att2 , y, x, insize,insize, styleShape+mxConstants.STYLE_NOLABEL+"=1;"+mxConstants.STYLE_FONTSIZE+"=15;");
                    
                }
                else
                {
                    
                    att = (MatrixNodeAttribute) vertice.getValue();
                    
                    ArrayList<Integer> times = att.getListTime();
                    if(!times.contains(time))
                        times.add(time);
                    att.setListTime(times);
                    
                    att2 = (MatrixNodeAttribute) vertice2.getValue();
                    
                    ArrayList<Integer> times2 = att2.getListTime();
                    if(!times2.contains(time))
                        times2.add(time);
                    att.setListTime(times2);
                    
                }
                
                
                String style_indice = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                style_indice += mxConstants.STYLE_NOLABEL+"=0;";
                style_indice += mxConstants.STYLE_RESIZABLE+"=0;";
                style_indice += mxConstants.STYLE_OPACITY+"=100;";
                style_indice += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_MIDDLE+";";
                style_indice += mxConstants.STYLE_FONTCOLOR+"=black;";
                style_indice += mxConstants.STYLE_FONTSIZE+"=15;";
                style_indice += mxConstants.STYLE_FILLCOLOR+"=white;";
                style_indice += mxConstants.STYLE_STROKECOLOR+"=white;";
                
                v1_indice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(node1+" v");
                if(v1_indice == null)
                {
                    
                    double y = alinhar_nos + espaco_inicial + espacamento*(node1+1);
                    double x = alinhar_nos + espaco_inicial + espacamento*0;
                    
                    att = new MatrixNodeAttribute(node1+" v", 0,node1,true, true);
                    graph.insertVertex(null, node1+" v", att , x, y, insize,insize, style_indice);
                    att = new MatrixNodeAttribute(node1+" h", node1,0,true, true);
                    graph.insertVertex(null, node1+" h", att , y, x, insize,insize, style_indice);
                }
                
                v2_indice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(node2+" v");
                if(v2_indice == null)
                {
                    
                    double y = alinhar_nos + espaco_inicial + espacamento*(node2+1);
                    double x = alinhar_nos + espaco_inicial + espacamento*0;

                    att = new MatrixNodeAttribute(node2+" v", 0,node2,true, true);
                    graph.insertVertex(null, node2+" v", att , x, y, insize,insize, style_indice);
                    att = new MatrixNodeAttribute(node2+" h", node2,0,true, true);
                    graph.insertVertex(null, node2+" h", att , y, x, insize,insize, style_indice);
                }
                
                
            }
            
            
            
            //imprimir a matriz
            /*
            for(int i=0;i<lastTime+1;++i){
                 
                System.out.println("Time "+i);
                 
		for(int j=0;j<uniqueIds;++j){
                    for(int k=0;k<uniqueIds;++k){
                        System.out.print(" "+cubicMatrix[i][j][k]+" ");
                    }
                    System.out.println("");
		}
            }*/
            
            //Get bigger value in number of times
            int vMax = 0;
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                MatrixNodeAttribute att = (MatrixNodeAttribute) cell.getValue();
                if(att.getListTime().size() > vMax)
                    vMax = att.getListTime().size();
                
            }

            //All nodes
            roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            int vMin = 0;
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                MatrixNodeAttribute att = (MatrixNodeAttribute) cell.getValue();
                
                ColorScale colorScale = new CustomColor(Color.WHITE,Color.BLACK);

                double incolor = ((double)att.getListTime().size() - vMin) / (vMax - vMin);

                Color colorNode = colorScale.getColor((float)incolor);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                if(!att.isNodeIndice())
                    cell.setStyle(cell.getStyle()+styleNode);
            }
            
            
        }
        finally
        {
            graph.getModel().endUpdate();
            graphWithEdges.getModel().endUpdate();
        }
        

        boolean weight_external_file = false;
        HashMap<String,Integer> edgeWeight_external_file = new HashMap();


        //CALCULO DO ALGORITMO DE COMUNIDADE INFOMAP E LOUVAIN
        comunidadesLouvain = new SLM_Louvain().execute(listAttNodes, listAllEdges, "Original Louvain", false,weight_external_file,edgeWeight_external_file);
        comunidades = comunidadesLouvain;

        comunidadesInfomap = new communities.InfoMap().execute(listAllEdges, listAttNodes, false,weight_external_file,edgeWeight_external_file);
        //new MainForm().imprimeComunidadesArquivo(comunidadesLouvain, "Infomap");
         //FIM CALCULO ALGORITMO DE COMUNIDADE INFOMAP E LOUVAIN

        graph.setAllowDanglingEdges(false);
        graph.setCellsEditable(false);
        graph.setCellsDisconnectable(false);
        graph.setCellsMovable(false);
        graph.cellsOrdered(graph.getChildVertices(graph.getDefaultParent()), false);
        graph.setEdgeLabelsMovable(false);
        
        graphComponent = new mxGraphComponent(graph);
        
        nodeColor("None");
        
    }
    
    private boolean listAttNodesLocalJaTemNo(ArrayList<InlineNodeAttribute> listAttNodesLocal, int idNo)
    {
        for(InlineNodeAttribute attNo : listAttNodesLocal)
            if(attNo.getId_original() == idNo)
                return true;
        return false;
    }
    
    //boolean firstTimeAggregated = false;
    
    
    public void matrixAnimation(int currentTime)
    {
        /*int vMax = 0, vMin = 0;
        
        if(aggregated)
        {
            //Get bigger value in number of times
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                MatrixNodeAttribute att = (MatrixNodeAttribute) cell.getValue();
                if(att.getListTime().size() > vMax)
                    vMax = att.getListTime().size();
                
            }
        }
        
        if(!aggregated)
            firstTimeAggregated = false;
        
        if(!firstTimeAggregated)
        {
            //initialize all colors with White
            //All nodes
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                MatrixNodeAttribute att = (MatrixNodeAttribute) cell.getValue();
                ColorScale colorScale = new CustomColor(Color.WHITE,Color.BLACK);

                double incolor = 0;

                Color colorNode = colorScale.getColor((float)incolor);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleNode = styleShape;
                styleNode += mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode += mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                
                if(showTriangularMatrix)
                {
                    if(att.isUpperTriangularNode())
                    {
                        styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=100;");   
                    }
                    else
                    {
                        styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                    }
                }
                else
                {
                    if(att.isUpperTriangularNode())
                    {
                        styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=100;");   
                    }
                    else
                    {
                        styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=100;");
                    }
                }
                
                if(!att.isNodeIndice())
                    cell.setStyle(styleNode+mxConstants.STYLE_NOLABEL+"=1;"+mxConstants.STYLE_FONTSIZE+"=15;");
            }
        }
        if(aggregated)
            firstTimeAggregated = true;
        */
        
        graph.getModel().beginUpdate();  
        //All nodes
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;
            MatrixNodeAttribute att = (MatrixNodeAttribute) cell.getValue();
            ArrayList<Integer> times = att.getListTime();
            
            String hexColor = "#000000",hexBorderColor = "#000000";
            String styleNode = cell.getStyle();
            if(times.contains(currentTime))
            {

                ColorScale colorScale = new CustomColor(Color.WHITE,Color.BLACK);
                double incolor = 0;

                if(att.getCommunityColor() != null)
                {
                    hexColor = att.getCommunityColor();
                }
                if(att.getNodeBorderColor() != null)
                {
                    hexBorderColor = att.getNodeBorderColor();
                }

                styleNode = styleNode.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                styleNode = styleNode.replaceAll("strokeColor=[^;]*;","strokeColor="+hexBorderColor+";");
                //styleNode += mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                //styleNode += mxConstants.STYLE_STROKECOLOR+"="+hexBorderColor+";";

                //Calculate the aging effect
                edgeAging.put(att.getId_original(), maximumAgingValue);
            }
            
            //Aging process
            if(showTriangularMatrix)
            {
                if(att.isUpperTriangularNode())
                {
                    if(!att.isNodeIndice())
                    {
                        int aging = edgeAging.get(att.getId_original())*divisorAgingValue;
                        styleNode = styleNode.replaceAll("opacity=[^;]*;","opacity="+aging+";");
                    }
                    
                }
            }
            else
            {
                if(!att.isNodeIndice())
                {
                    int aging = edgeAging.get(att.getId_original())*divisorAgingValue;
                    styleNode = styleNode.replaceAll("opacity=[^;]*;","opacity="+aging+";");
                }
            }

            if(!att.isNodeIndice())
            {
                cell.setStyle(styleNode);

                //Reduces the aging value for all edges
                int aging = edgeAging.get(att.getId_original());
                if(aging > 0)
                    aging--;
                edgeAging.put(att.getId_original(), aging);
            }
        }

        
        
        
        graph.getModel().endUpdate();
        graphComponent.refresh();
        
    }
    
    public int qtddTimesFewerThen(int currentTime, ArrayList<Integer> times)
    {
        int count = 0;
        for(Integer time : times)
        {
            if(time <= currentTime)
                count++;
        }
        return count;
    }
    
    int velocity = 1;
    Thread thread;
    public void startAnimation(MainForm f)
    {
        
        velocity = f.speedjSlider.getValue();
        
        if(f.stateAnimation.equals("run"))
        {
            if(tempoAtual > lastTime)
                tempoAtual = 0;
            thread = new Thread(this);
            f.stateAnimation = "pause";
            this.f = f;
            //f.timeAnimationValue.setText("0");
            f.jSlider1.setMaximum(lastTime);

            ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/pause.png"));
            f.runButton.setIcon(icon);
            f.agingjSlider.setEnabled(false);
            f.agingLabel.setEnabled(false);
            
            thread.start();
        }
        else if(f.stateAnimation.equals("pause"))
        {
            thread.interrupt();
            f.stateAnimation = "run";
            ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/run.png"));
            f.runButton.setIcon(icon);
        }
        else if(f.stateAnimation.equals("stop"))
        {
            thread.interrupt();
            ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/run.png"));
            f.runButton.setIcon(icon);
            f.jSlider1.setValue(0);
            f.timeAnimationValue.setText("0");
            matrixAnimation(0);
            f.agingjSlider.setEnabled(true);
            f.agingLabel.setEnabled(true);
            f.stateAnimation = "run";
            tempoAtual= 0;
            for(Map.Entry<String,Integer> pair : edgeAging.entrySet())
            {
                edgeAging.put(pair.getKey(), 0);
            }
            
            showTriangularMatrix(showTriangularMatrix);
            
        }
        
    }
    
    
    int tempoAtual= 0;
    
    @Override
    public void run() {

        try {
            
            maximumAgingValue = f.agingjSlider.getValue();
            divisorAgingValue = 100/maximumAgingValue;
            
            while(tempoAtual <= lastTime && !Thread.currentThread().isInterrupted()){
                
                f.timeAnimationValue.setText(""+tempoAtual);
                f.jSlider1.setValue(tempoAtual);
                velocity = f.speedjSlider.getValue();
                //System.out.println(velocity);
                
                try {
                    Integer changedTime = tempoAtual;
                    matrixAnimation(changedTime);

                }catch (NumberFormatException e) {}
                
                Thread.currentThread().sleep(1000/velocity);
                
                tempoAtual++;
            }
            
            ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/run.png"));
            f.runButton.setIcon(icon);
            f.stateAnimation = "run";
            f.agingjSlider.setEnabled(true);
            f.agingLabel.setEnabled(true);
            /*
            f.jSlider1.setValue(0);
            f.timeAnimationValue.setText("0");
            matrixAnimation(0);
            f.agingjSlider.setEnabled(true);
            f.agingLabel.setEnabled(true);
            */
            
            for(Map.Entry<String,Integer> pair : edgeAging.entrySet())
            {
                edgeAging.put(pair.getKey(), 0);
            }

            showTriangularMatrix(showTriangularMatrix);

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } 
    }
    
    public void orderNodes(String order)
    {
        switch(order)
        {
            case "Appearance":
                nodeOrderAppearance();
                break;
            case "Random":
                nodeOrderRandom();
                break;
            case "Degree":
                nodeOrderDegree();
                break;
            case "Recurrent Neighbors":
                nodeOrderRecurrentNeighbors();
                break;
            case "Louvain":
                nodeOrderCommunities("Louvain");
                nodeColor(f.nodeColorMatrixComboBox.getSelectedItem().toString());
                break;
            case "Infomap":
                nodeOrderCommunities("Infomap");
                nodeColor(f.nodeColorMatrixComboBox.getSelectedItem().toString());
                break;
            default:
                break;
        }
    }
    
    public void nodeOrderAppearance()
    {
        Collections.sort(nodeOrderingVector);
        
        for(int i = 0;i < nodeOrderingVector.size();i++)
        {
            nodeOrder.put(nodeOrderingVector.get(i), i);
        }
        
        visualizeOrder(nodeOrder);
    }
    
    public void nodeOrderDegree()
    {
        HashMap<Integer, Integer> nodeDegreeOrder = sortByValue(nodeDegree);
        int i = 0;
        for(Map.Entry<Integer,Integer> pair : nodeDegreeOrder.entrySet())
        {
            nodeOrder.put(pair.getKey(), i);
            i++;
        }
        
        visualizeOrder(nodeOrder);
    }
    
    //nodeOrder : key = idNode, value = positionNode
    //nodeOrderingVector : positionVector = positionNodeLayout, value = idNode
    public void nodeOrderRandom()
    {
        Collections.shuffle(nodeOrderingVector);
        
        for(int i = 0;i < nodeOrderingVector.size();i++)
        {
            nodeOrder.put(nodeOrderingVector.get(i), i);
        }
        
        visualizeOrder(nodeOrder);
    }
    
    private void nodeOrderCommunities(String metodo) 
    {
        
        HashMap<Integer,List<InlineNodeAttribute>> comunidadesReordenada = new HashMap();
        HashMap<Integer,Integer> comunidadesTamanhoOrdenada = new HashMap();
        
        if(metodo.equals("Louvain"))
            comunidades = (HashMap) comunidadesLouvain.clone();
        else if (metodo.equals("Infomap"))
            comunidades = (HashMap) comunidadesInfomap.clone();
        
        //ordena as comunidades pelo tamanho
        for(Map.Entry<Integer,List<InlineNodeAttribute>> pair : comunidades.entrySet())
        {
            comunidadesTamanhoOrdenada.put(pair.getKey(), pair.getValue().size());
        }
        
        Map<Integer, Integer> sortedMapAsc = sortByValue(comunidadesTamanhoOrdenada);
        
        int y=0;
        for(Map.Entry<Integer, Integer> pair : sortedMapAsc.entrySet())
        {
            comunidadesReordenada.put(y, comunidades.get(pair.getKey()));
            y++;
        }
        
        HashMap<Integer,List<Integer>> comunidadesReordenadasInternas = new HashMap();
        y=0;
        //ordena os nós dentro da comunidade pelo grau
        for(Map.Entry<Integer, List<InlineNodeAttribute>> pair : comunidadesReordenada.entrySet())
        {
            List<Integer> nodes = new ArrayList();
            for(InlineNodeAttribute node : pair.getValue())
            {
                nodes.add(node.getId_original());
            }
            
            HashMap<Integer,Integer> nodeDegreeInsideCommunity = new HashMap();
            for(Integer nodeId : nodes)
            {
                nodeDegreeInsideCommunity.put(nodeId, nodeDegree.get(nodeId));
            }
            
            nodeDegreeInsideCommunity = sortByValue(nodeDegreeInsideCommunity);
            
            List<Integer> nodesOrderedInside = new ArrayList();
            for(Map.Entry<Integer, Integer> pairInside : nodeDegreeInsideCommunity.entrySet())
            {
                nodesOrderedInside.add(pairInside.getKey());
            }
            
            comunidadesReordenadasInternas.put(pair.getKey(), nodesOrderedInside);
        
            //comunidadesReordenada.put(y, comunidadesLouvain.get(pair.getKey()));
            y++;
        }
        
        int x = 0;
        for(List<Integer> community : comunidadesReordenadasInternas.values())
        {
            for(Integer node : community)
            {
                
                nodeOrder.put(node, x);
                x++;
            }
            
        }
        /*
        for(List<InlineNodeAttribute> community : comunidadesReordenada.values())
        {
            for(InlineNodeAttribute node : community)
            {
                
                nodeOrder.put(node.getId_original(), x);
                x++;
            }
            
        }*/
        
        visualizeOrder(nodeOrder);
        
    }
    
    //Article: EISEN M. B., SPELLMAN P. T., BROWN P. O., BOTSTEIN D.: Cluster analysis and display of genome wide expression patterns. 1998
    //Implementation adapted from: Matrix Reordering Methods for Table and Network Visualization, Computer Graphics Forum, 2016
    //https://gitlab.dbvis.de/behrisch/matrixsort/tree/5421c8f02478dffc0eecc027148f2d1b4e5d1d79/src/main/java/de/dbvis/matrix/matrixsort
    public void nodeOrderHierarquicalCluster()
    {
        //NOT IMPLEMENTED YET
    }
    
    public void nodeOrderRecurrentNeighbors()
    {
        
        /*HashMap<Integer, Integer> nodeDegreeOrder = sortByValue(nodeDegree);
        int i = 0;
        for(Map.Entry<Integer,Integer> pair : nodeDegreeOrder.entrySet())
        {
            nodeOrder.put(pair.getKey(), i);
            i++;
        }
        
        visualizeOrder(nodeOrder);
        */
        
        orderRecurrentNeighbors();
        
       
    }
    
    private ArrayList<OccurrenceMap> nodesWithConnections = new ArrayList();
    public void orderRecurrentNeighbors()
    {
        
        ArrayList<Integer> nodesLeft = new ArrayList();
        nodesLeft.addAll(nodeOrderingVector);
        
        HashMap<Integer, Integer> nodeDegreeOrder = sortByValue(nodeDegree);
        int higherDegreeNode = -1;
        for(Map.Entry<Integer,Integer> pair : nodeDegreeOrder.entrySet())
        {
            higherDegreeNode = pair.getKey();
            break;
        }
        
        nodeOrder.put(higherDegreeNode,(int)(listAttNodes.size()/2));
        nodesLeft.remove(higherDegreeNode);
        
        int position_up = (int)(listAttNodes.size()/2) - 1;
        int position_down = (int)(listAttNodes.size()/2) + 1;
        getNodesWithMoreConnections(higherDegreeNode,nodesLeft);
        Collections.sort(nodesWithConnections);
        int id_up = nodesWithConnections.get(0).getId(), id_down = -1;
        nodeOrder.put(id_up,position_up);
        nodesLeft.remove(nodesWithConnections.get(0).getId());
        if(nodesWithConnections.size() != 1)
        {
            id_down = nodesWithConnections.get(1).getId();
            nodesLeft.remove(nodesWithConnections.get(1).getId());
        }
        else
        {
            for(int i=listAttNodes.size()-1;i>=0;i--)
            {
                InlineNodeAttribute att = listAttNodes.get(i);
                if(nodesLeft.contains(att.getId_original()))
                {
                    OccurrenceMap om = new OccurrenceMap(att.getId_original(),1);
                    nodesWithConnections.add(om);
                    id_down = att.getId_original();
                    nodesLeft.remove(Integer.valueOf(id_down));
                    
                    break;
                }
            }
        }
        nodeOrder.put(id_down,position_down);
        position_up--;
        position_down++;
        
        while(position_up != 0)
        {
            getNodesWithMoreConnections(id_up,nodesLeft);
            id_up = nodesWithConnections.get(0).getId();
            nodesLeft.remove(nodesWithConnections.get(0).getId());
            
            nodeOrder.put(id_up,position_up);
            
            getNodesWithMoreConnections(id_down,nodesLeft);
            id_down = nodesWithConnections.get(0).getId();
            nodesLeft.remove(nodesWithConnections.get(0).getId());
            
            nodeOrder.put(id_down,position_down);
            
            position_down++;
            
            position_up--;
        }
        
        if(position_down != nodeOrderingVector.size()|| position_up != 0)
        {
            System.out.println("Erro RN");
        }
        
        visualizeOrder(nodeOrder);
        
    }
    
    public void getNodesWithMoreConnections(Integer id, ArrayList<Integer> nodesLeft)
    {
        nodesWithConnections = new ArrayList();
        
        for(Object ed : listAllEdges)
        {
            mxCell edge = (mxCell) ed;
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            
            if(att.getOrigin() == id && nodesLeft.contains(att.getDestiny()))
            {
                OccurrenceMap oc = hasNodeInOccurrenceMap(att.getDestiny());
                if(oc == null)
                {
                    OccurrenceMap om = new OccurrenceMap(att.getDestiny(),1);
                    nodesWithConnections.add(om);
                }
                else
                {
                    int index = nodesWithConnections.lastIndexOf(oc);
                    oc.setOccurrence(oc.getOccurrence()+1);
                    nodesWithConnections.set(index, oc);
                }
            }

            if(att.getDestiny() == id && nodesLeft.contains(att.getDestiny()))
            {
                OccurrenceMap oc = hasNodeInOccurrenceMap(att.getOrigin());
                if(oc == null)
                {
                    OccurrenceMap om = new OccurrenceMap(att.getOrigin(),1);
                    nodesWithConnections.add(om);
                }
                else
                {
                    int index = nodesWithConnections.lastIndexOf(oc);
                    oc.setOccurrence(oc.getOccurrence()+1);
                    nodesWithConnections.set(index, oc);
                }
            }


        }
        if(nodesWithConnections.isEmpty())
        {
            for(int i=listAttNodes.size()-1;i>=0;i--)
            {
                InlineNodeAttribute att = listAttNodes.get(i);
                if(nodesLeft.contains(att.getId_original()))
                {
                    OccurrenceMap om = new OccurrenceMap(att.getId_original(),1);
                    nodesWithConnections.add(om);
                    break;
                }

            }
        }
    }
    
    public OccurrenceMap hasNodeInOccurrenceMap(Integer id)
    {
        for(OccurrenceMap oc : nodesWithConnections)
        {
            if(Objects.equals(oc.getId(), id))
            {
                return oc;
            }
        }
        return null;
    }
    
    //nodeOrder : key = idNode, value = positionNode
    public void visualizeOrder(HashMap<Integer,Integer> nodeOrder)
    {
        graph.getModel().beginUpdate();
        //All nodes
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;
            Object[] root = {root1};
            MatrixNodeAttribute att = (MatrixNodeAttribute) cell.getValue();
            String[] parts = att.getId_original().split(" ");
            
            //Move cells to origin point
            double y = 0;
            double x = 0;
            if(att.isNodeIndice())
            {
                if(parts[1].equals("v"))
                {
                    y = alinhar_nos + espaco_inicial + espacamento*(nodeOrder.get(Integer.parseInt(parts[0]))+1);
                    x = alinhar_nos + espaco_inicial + espacamento*0;
                }
                else
                {
                    y = alinhar_nos + espaco_inicial + espacamento*0;
                    x = alinhar_nos + espaco_inicial + espacamento*(nodeOrder.get(Integer.parseInt(parts[0]))+1);
                }
            }
            else
            {
                y = alinhar_nos + espaco_inicial + espacamento*(nodeOrder.get(Integer.parseInt(parts[0]))+1);
                x = alinhar_nos + espaco_inicial + espacamento*(nodeOrder.get(Integer.parseInt(parts[1]))+1);
            }
            
            
            
            //att.setY_atual(nodeOrder.get(Integer.parseInt(parts[0])));
            //att.setX_atual(nodeOrder.get(Integer.parseInt(parts[1])));
            
            graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());

            //att.setY_atual(att.getY_original()+ positionLineGraph);

            graph.moveCells(root, x, y);
            
            
            if(x >= y)
                att.setUpperTriangularNode(true);
            else
                att.setUpperTriangularNode(false);

            String styleNode = cell.getStyle();
            
            styleNode = setOpacity(styleNode, att);
            
            if(!att.isNodeIndice())
                cell.setStyle(styleNode);
        }
        
        graph.getModel().endUpdate();
        graphComponent.refresh();
    }
    
    public static HashMap<Integer, Integer> sortByValue(HashMap<Integer, Integer> hm) 
    {
        // Create a list from elements of HashMap 
        List<Map.Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() { 
            public int compare(Map.Entry<Integer, Integer> o1,  
                               Map.Entry<Integer, Integer> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>(); 
        for (Map.Entry<Integer, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    }
      
    NetLayoutInlineNew.JGraphStyle style;
    public String styleShape;
    
    public void defineStyles()
    {
        
        //style = NetLayoutInlineNew.JGraphStyle.ROUNDED;
        
        styleShape = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
        styleShape += mxConstants.STYLE_EDITABLE+"=0;";
        styleShape += mxConstants.STYLE_RESIZABLE+"=0;";
        styleShape += mxConstants.STYLE_NOLABEL+"=0;";
        styleShape += mxConstants.STYLE_OPACITY+"=100;";
        styleShape += mxConstants.STYLE_STROKEWIDTH+"=5;";

        styleNode = mxConstants.STYLE_EDITABLE+"=0;";
        styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
        styleNode += mxConstants.STYLE_NOLABEL+"=0;";
        styleNode += mxConstants.STYLE_OPACITY+"=100;";
        
        
    }
    
    private boolean showTriangularMatrix = false;
    
    public void showTriangularMatrix(Boolean showTriangularMatrix)
    {
        
        this.showTriangularMatrix = showTriangularMatrix;
        graph.getModel().beginUpdate();  
        //All nodes
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;
            MatrixNodeAttribute att = (MatrixNodeAttribute) cell.getValue();
            String styleShapeInline = cell.getStyle();
            
            styleShapeInline = setOpacity(styleShapeInline, att);
            
            cell.setStyle(styleShapeInline);
        }

        graph.getModel().endUpdate();
        graphComponent.refresh();
    }
    
    private boolean fileColorChoosen = false;
    private String nodeColor = "None";
    private HashMap<Integer, Integer> mapMetadata = new HashMap(); // key: ID node, value: metadata (in number)

    public void nodeColor(String color) 
    {
        nodeColor = color;
        
        
        if(nodeColor.equals("Metadata File") && !fileColorChoosen)
        {
           
            JFileChooser openDialog = new JFileChooser();
            String filename = "";

            openDialog.setMultiSelectionEnabled(false);
            openDialog.setDialogTitle("Open file");

            openDialog.setSelectedFile(new File(filename));
            openDialog.setCurrentDirectory(new File(filename));

            int result = openDialog.showOpenDialog(openDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                filename = openDialog.getSelectedFile().getAbsolutePath();
                openDialog.setSelectedFile(new File(""));
                BufferedReader file;
                try {
                    file = new BufferedReader(new FileReader(new File(filename)));
                    String line = file.readLine();
                    String tmp;
                    String[] tokens = line.split(" ");
                    int count = 0;
                    
                    while ((tmp = file.readLine()) != null)
                    {
                        String[] tokens2 = tmp.split(" ");
                        if(!tokens[1].equals(tokens2[1]))
                        {
                            tokens[1] = tokens2[1];
                            count++;
                        }
                        mapMetadata.put(Integer.parseInt(tokens2[0]), count);
                    }
                }catch (FileNotFoundException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            
                fileColorChoosen = true;
            }
            
        }
        
        graph.getModel().beginUpdate();  
        
        String[] colorsTable = new String[10000]; 
        for(int i=0; i < colorsTable.length;i++)
            colorsTable[i] = "000 000 000";

        colorsTable[0] = "000 000 255"; //blue
        colorsTable[1] = "000 255 000"; //green
        colorsTable[2] = "255 000 000"; //red
        colorsTable[3] = "255 185 60"; //orange
        colorsTable[4] = "255 255 000"; //yellow
        colorsTable[5] = "255 000 255"; //pink
        colorsTable[6] = "100 070 000"; //brown
        colorsTable[7] = "205 092 092"; //light pink
        colorsTable[8] = "148 000 211"; //purple
        colorsTable[9] = "000 255 255"; //cyan

        //All nodes
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;
            MatrixNodeAttribute att = (MatrixNodeAttribute) cell.getValue();
            String styleShapeInline = cell.getStyle();
            if(nodeColor.equals("None"))
            {
                if(!att.isNodeIndice())
                {
                    styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor=black;");
                    styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor=black;");
                    styleShapeInline = styleShapeInline.replaceAll("strokeColor=[^;]*;","strokeColor=black;");
                    
                    att.setCommunityColor("#000000");
                    att.setNodeBorderColor("#000000");
                
                }
            }
            else if(nodeColor.equals("Metadata File"))
            {
                if(!fileColorChoosen)
                    break;
                if(!att.isNodeIndice())
                {
                    String[] origin_destiny = att.getId_original().split(" ");

                    int original_id0 = nodeOriginalId.get(Integer.parseInt(origin_destiny[0]));
                    int original_id1 = nodeOriginalId.get(Integer.parseInt(origin_destiny[1]));
                    
                    int metadata1 = -1;
                    if(mapMetadata.get(original_id0) != null)
                        metadata1 = mapMetadata.get(original_id0);
                    
                    int metadata2 = -2;
                    if(mapMetadata.get(original_id1) != null)
                        metadata2 = mapMetadata.get(original_id1);
                    
                    boolean sameMetadata = false;
                    if(metadata1 == metadata2)
                    {
                        sameMetadata = true;
                    }
                    
                    //Edges with different metadata are light gray because of hexColor = "#D3D3D3";
                    String hexBorderColor = "#D3D3D3";
                    String hexColor = "#D3D3D3";
                    
                    if(sameMetadata && colorsTable[metadata1] != null)
                    {
                        String color2 = colorsTable[metadata1];
                        String[] tokens = color2.split(" ");
                        int r = Integer.parseInt(tokens[0]);
                        int g = Integer.parseInt(tokens[1]);
                        int b = Integer.parseInt(tokens[2]);
                        Color colorNode = new Color(r,g,b);
                        hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                        hexBorderColor = hexColor;
                    }
                    
                    att.setCommunityColor(hexColor);
                    att.setNodeBorderColor(hexBorderColor);
               
                    styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                    styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");
                    styleShapeInline = styleShapeInline.replaceAll("strokeColor=[^;]*;","strokeColor="+hexBorderColor+";");
                    
                }
            }
            else
            {
                if(!att.isNodeIndice())
                {
                    
                    String[] origin_destiny = att.getId_original().split(" ");

                    int community1 = getKey(comunidades,origin_destiny[0]);
                    int community2 = getKey(comunidades,origin_destiny[1]);

                    boolean sameCommunity = false;
                    if(community1 == community2)
                    {
                        sameCommunity = true;
                    }
                    
                    String hexBorderColor = "#000000";
                    String hexColor = "#000000";
                    
                    //Inter-community edges are black because of the r,g,b=0;
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    if(nodeColor.equals("Intra Communities"))
                    {
                        r = 0;
                        g = 0;
                        b = 0;
                        Color colorNode = new Color(r,g,b);
                        hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                        hexBorderColor = hexColor;
                    }
                    else if(nodeColor.equals("Only Communities"))
                    {
                        r = 255;
                        g = 255;
                        b = 255;
                        Color colorNode = new Color(r,g,b);
                        hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                        hexBorderColor = hexColor;
                    }
                    else if(nodeColor.equals("Random"))
                    {

                        sameCommunity = false;
                        
                        Random rand = new Random();
                        
                        // Java 'Color' class takes 3 floats, from 0 to 1.
                        float r1 = rand.nextFloat();
                        float g1 = rand.nextFloat();
                        float b1 = rand.nextFloat();

                        Color colorNode = new Color(r1,g1,b1);
                        hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                        hexBorderColor = hexColor;
                    }
                    else
                    {
                        
                        if(colorsTable[community1] != null && colorsTable[community2] != null)
                        {
                            int auxCommunity = -1;
                            if(community1 > community2)
                            {
                                auxCommunity = community1;
                                community1 = community2; 
                                community2 = auxCommunity;
                            }
                            String color2 = colorsTable[community1];
                            String[] tokens = color2.split(" ");
                            r = Integer.parseInt(tokens[0]);
                            g = Integer.parseInt(tokens[1]);
                            b = Integer.parseInt(tokens[2]);
                            Color colorNode = new Color(r,g,b);
                            hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                            
                            color2 = colorsTable[community2];
                            tokens = color2.split(" ");
                            r = Integer.parseInt(tokens[0]);
                            g = Integer.parseInt(tokens[1]);
                            b = Integer.parseInt(tokens[2]);
                            colorNode = new Color(r,g,b);
                            hexBorderColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                        }
                        
                    }
                    
                    if(sameCommunity && colorsTable[community1] != null)
                    {
                        String color2 = colorsTable[community1];
                        String[] tokens = color2.split(" ");
                        r = Integer.parseInt(tokens[0]);
                        g = Integer.parseInt(tokens[1]);
                        b = Integer.parseInt(tokens[2]);
                        Color colorNode = new Color(r,g,b);
                        hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                        hexBorderColor = hexColor;
                    }
                    
                    att.setCommunityColor(hexColor);
                    att.setNodeBorderColor(hexBorderColor);
               
                    styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                    styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");
                    styleShapeInline = styleShapeInline.replaceAll("strokeColor=[^;]*;","strokeColor="+hexBorderColor+";");
                    
                    
                }
            }
            
            styleShapeInline = setOpacity(styleShapeInline, att);
            
            cell.setStyle(styleShapeInline);
        }

        graph.getModel().endUpdate();
        graphComponent.refresh();
        
    }

    private int getKey(HashMap<Integer, List<InlineNodeAttribute>> comunidadesLouvain, String value) {
        
        int key = -1;
        for(Map.Entry<Integer,List<InlineNodeAttribute>> pair : comunidadesLouvain.entrySet())
        {
            for(InlineNodeAttribute att :pair.getValue())
            {
                if(att.getId_original() == Integer.parseInt(value))
                {
                    key = pair.getKey();
                    break;
                }
            }
            if(key != -1)
                break;
        }
        return key;
    }
    
    public String setOpacity(String styleNode, MatrixNodeAttribute att)
    {
    
        //without animation
        if(tempoAtual == 0)
        {
            if(showTriangularMatrix)
            {
                if(att.isUpperTriangularNode())
                {
                    styleNode = styleNode.replaceAll("opacity=[^;]*;","opacity=100;");
                }
                else
                {
                    styleNode = styleNode.replaceAll("opacity=[^;]*;","opacity=0;");
                }
            }
            else
            {
                styleNode = styleNode.replaceAll("opacity=[^;]*;","opacity=100;");

            }
        }
        else //with animation
        {
            if(showTriangularMatrix)
            {
                if(!att.isUpperTriangularNode())
                {
                    styleNode = styleNode.replaceAll("opacity=[^;]*;","opacity=0;");
                }
            }
        }
        return styleNode;
    }

}
