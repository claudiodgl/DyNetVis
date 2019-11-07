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
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.awt.Color;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import visualizationbasics.color.BlueSkyToOrange;
import visualizationbasics.color.BlueToCyanScale;
import visualizationbasics.color.BlueToRed;
import visualizationbasics.color.BlueToYellowScale;
import visualizationbasics.color.ColorScale;
import visualizationbasics.color.CustomColor;
import visualizationbasics.color.GrayScale;
import visualizationbasics.color.GreenToRed;
import visualizationbasics.color.GreenToWhiteScale;
import visualizationbasics.color.HeatedObjectScale;
import visualizationbasics.color.LinearGrayScale;
import visualizationbasics.color.LocsScale;
import visualizationbasics.color.OrangeToBlueSky;
import visualizationbasics.color.RainbowScale;
import communities.*;
import forms.MainForm;			
import forms.OpenDataSetDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import util.FileHandler;


public class NetLayoutInlineNew extends NetLayout {
    
    public ArrayList<HashMap<Integer, List<InlineNodeAttribute>>> comunidadesROCMultilevel = new ArrayList<>();
    public ArrayList<HashMap<Integer, List<InlineNodeAttribute>>> orderNodesEdgeLength = new ArrayList<>();
    public static String txtComunidadesROCMultilevel = ".//Softwares_Comunidade//ROC_Multilevel_Comunidades.txt";
    private static String txtOutliersROCMultilevel = ".//Softwares_Comunidade//Nos_Sem_Comunidades_ROC_Multilevel_Comunidades.txt";
    public boolean imprimirTxtComunidadesROCMultilevel = true;
    public int qtosNiveisCNOUsuarioQuer = 1;
    public int stateCommunityROCMultiLevel = 0;
    private int deslocamento,shiftX;
    public final ArrayList<Integer> lineNodes = new ArrayList<>();
    public HashMap<Integer, Integer> currentTemporalNodeOrder = new HashMap<>();
    public mxGraph graph;
    public ArrayList<ArrayList> matrizDataInline;
    public String styleShape,styleShapeEdge, styleShapeEdgeLines,styleShapeEdgeLines2, styleShapeEdgeInvisible,styleShapeLines,styleShapeNumbers,styleShapeInvisibleNodes,styleShapeInvisibleNodesandLabel;
    Map<String, Object> styleEdge, styleNode;
    public mxGraphComponent graphComponentScalarInline, graphComponentLine , graphComponentScalarEdgeInline;
    
    MainForm f;
    
    public double pesoPt4LineGraphStream = 0.5, pesoPt3LineGraphStream = 0.3, pesoPt2LineGraphStream = 0.15, pesoPt1LineGraphStream = 0.05;
    
    public NetLayoutInlineNew() {
        
    }

    /**
     * @return the colorInline
     */
    public String getColorInline() {
        return colorInline;
    }

    /**
     * @param colorInline the colorInline to set
     */
    public void setColorInline(String colorInline) {
        this.colorInline = colorInline;
    }

    /**
     * @return the typeColorInline
     */
    public String getTypeColorInline() {
        return typeColorInline;
    }

    /**
     * @param typeColorInline the typeColorInline to set
     */
    public void setTypeColorInline(String typeColorInline) {
        this.typeColorInline = typeColorInline;
    }

    /**
     * @return the formNode
     */
    public String getFormNode() {
        return formNode;
    }

    /**
     * @param formNode the formNode to set
     */
    public void setFormNode(String formNode) {
        this.formNode = formNode;
    }

    /**
     * @return the sizeNode
     */
    public String getSizeNode() {
        return sizeNode;
    }

    /**
     * @param sizeNode the sizeNode to set
     */
    public void setSizeNode(String sizeNode) {
        this.sizeNode = sizeNode;
    }

    /**
     * @param linesSpace the linesSpace to set
     */
    public void setLinesSpace(int linesSpace) {
        spaceBetweenLines = linesSpace;
    }

    /**
     * @return the templateColor
     */
    public String getTemplateColor() {
        return templateColor;
    }

    /**
     * @param templateColor the templateColor to set
     */
    public void setTemplateColor(String templateColor) {
        this.templateColor = templateColor;
    }

    /**
     * @return the templateMat
     */
    public String getTemplateMat() {
        return templateMat;
    }

    /**
     * @param templateMat the templateMat to set
     */
    public void setTemplateMat(String templateMat) {
        this.templateMat = templateMat;
    }

    /**
     * @return the colorEdgeInline
     */
    public String getColorEdgeInline() {
        return colorEdgeInline;
    }

    /**
     * @param colorEdgeInline the colorEdgeInline to set
     */
    public void setColorEdgeInline(String colorEdgeInline) {
        this.colorEdgeInline = colorEdgeInline;
    }

    /**
     * @return the typeEdgeColorInline
     */
    public String getTypeEdgeColorInline() {
        return typeEdgeColorInline;
    }

    /**
     * @param typeEdgeColorInline the typeEdgeColorInline to set
     */
    public void setTypeEdgeColorInline(String typeEdgeColorInline) {
        this.typeEdgeColorInline = typeEdgeColorInline;
    }

    private void recalculateMediumEdgesColors() {
        
        String[] colorsTable = new String[2];

        graph.getModel().beginUpdate();

        for(Object ed : listAllEdges)
        {
            mxCell edge = (mxCell) ed;
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            String styleShapeInline = edge.getStyle();
            String[] colorRGB = null;
            CustomColor colorScale;
            double edgeSize = att.getEdgeSize();
            if(edgeSize > mediumEdgeSize)
            {   
                colorScale = new CustomColor(Color.RED,Color.RED);
            }
            else
            {
                colorScale = new CustomColor(Color.BLUE,Color.BLUE);
            }
            
            Color color = colorScale.getColor((float)1);
            String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);
            String styleEdge = edge.getStyle();
            styleEdge = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
            styleEdge = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");

            edge.setStyle(styleEdge);
        }
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }

    private HashMap<Integer, List<InlineNodeAttribute>> degreeAlgorithm(HashMap<Integer, List<InlineNodeAttribute>> sequenciaDeEntrada, int[][] contactMatrix, int passo) 
    {
        HashMap<Integer, List<InlineNodeAttribute>> sequenciaResultante = new HashMap<>();
        Map<Integer, Integer> communityIdxDegree = new HashMap<>();
        
        if(passo == 2) //inter
        {
            for(Entry<Integer,List<InlineNodeAttribute>> comunidade : sequenciaDeEntrada.entrySet())
            {
                int communityDegree = 0; 
                
                for(int i = 0; i < comunidade.getValue().size(); i++)
                    communityDegree += comunidade.getValue().get(i).getDegree();
                
                communityIdxDegree.put(comunidade.getKey(), communityDegree);
                
            }
            
            communityIdxDegree = sortByComparator(communityIdxDegree, true);
            int i =0;
            for(Entry<Integer,Integer> comunidade : communityIdxDegree.entrySet())
            {
                sequenciaResultante.put(i++, sequenciaDeEntrada.get(comunidade.getKey()));
            }
        }
        else
        {
            for(Entry<Integer,List<InlineNodeAttribute>> comunidade : sequenciaDeEntrada.entrySet())
            {
                Collections.sort(comunidade.getValue());
                sequenciaResultante.put(comunidade.getKey(), comunidade.getValue());
            }
               
        }
        return sequenciaResultante;
    }

   

    

    
    public enum JGraphShape {
        RECTANGLE(mxConstants.SHAPE_RECTANGLE),
        ELLIPSE(mxConstants.SHAPE_ELLIPSE),
        DOUBLE_ELLIPSE(mxConstants.SHAPE_DOUBLE_ELLIPSE),
        RHOMBUS(mxConstants.SHAPE_RHOMBUS),
        LINE(mxConstants.SHAPE_LINE),
        IMAGE(mxConstants.SHAPE_IMAGE),
        CURVE(mxConstants.SHAPE_CURVE),
        LABEL(mxConstants.SHAPE_LABEL),
        CILINDER(mxConstants.SHAPE_CYLINDER),
        SWIMLANE(mxConstants.SHAPE_SWIMLANE),
        CONNECTOR(mxConstants.SHAPE_CONNECTOR),
        ACTOR(mxConstants.SHAPE_ACTOR),
        CLOUD(mxConstants.SHAPE_CLOUD),
        TRIANGLE(mxConstants.SHAPE_TRIANGLE),
        HEXAGON(mxConstants.SHAPE_HEXAGON);

        public String mxShapeConstantValue;

        JGraphShape(String mxShapeConstantValue) {
          this.mxShapeConstantValue = mxShapeConstantValue;
        }
   }
    
    public enum JGraphStyle {
        OPACITY(mxConstants.STYLE_OPACITY, 50.0),
        TEXT_OPACITY(mxConstants.STYLE_TEXT_OPACITY, 50.0),
        OVERFLOW_1(mxConstants.STYLE_OVERFLOW, "visible"),
        OVERFLOW_2(mxConstants.STYLE_OVERFLOW, "hidden"),
        OVERFLOW_3(mxConstants.STYLE_OVERFLOW, "fill"),
        ROTATION(mxConstants.STYLE_ROTATION, 45),
        FILLCOLOR(mxConstants.STYLE_FILLCOLOR, mxUtils.getHexColorString(Color.RED)),
        GRADIENTCOLOR(mxConstants.STYLE_GRADIENTCOLOR, mxUtils.getHexColorString(Color.BLUE)),
        //GRADIENT_DIRECTION(mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_EAST, mxConstants.STYLE_GRADIENTCOLOR, mxUtils.getHexColorString(Color.YELLOW)),
        STROKECOLOR(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.GREEN)),
        STROKEWIDTH(mxConstants.STYLE_STROKEWIDTH, 5),
        ALIGN(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_LEFT),
        VERTICAL_ALIGN(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_BOTTOM),
        LABEL_POSITION(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_LEFT),
        VERTICAL_LABEL_POSITION(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM),
        GLASS(mxConstants.STYLE_GLASS, 1),
        LABEL_BACKGROUNDCOLOR(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, mxUtils.getHexColorString(Color.CYAN)),
        LABEL_BORDERCOLOR(mxConstants.STYLE_LABEL_BORDERCOLOR, mxUtils.getHexColorString(Color.PINK)),
        SHADOW(mxConstants.STYLE_SHADOW, true),
        DASHED(mxConstants.STYLE_DASHED, true),
        ROUNDED(mxConstants.STYLE_ROUNDED, true),
        HORIZONTAL(mxConstants.STYLE_HORIZONTAL, false),
        FONTCOLOR(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.ORANGE)),
        FONTFAMILY(mxConstants.STYLE_FONTFAMILY, "Times New Roman"),
        FONTSIZE(mxConstants.STYLE_FONTSIZE, 15),
        FONTSTYLE(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD),        
        ;

        public String mxStyle;

        JGraphStyle(Object... values) {
            mxStyle = ""; 
            for (int i = 0; i < values.length; i++) {
                if(i%2==0) {
                    mxStyle += values[i] + "=";
                } else {
                    mxStyle += values[i] + ";";
                }
            }
        }
    }
    
    public String interCommunityReorderingAlgorithm = "Recurrent Neighbors";
    public String intraCommunityReorderingAlgorithm = "Recurrent Neighbors";
    public String communityDetectionAlgorithm;
    public boolean openFileCNO = false;
    public String pathFileCNO = "";
    
    public void orderNodesInline(String order, mxGraphComponent matriz){
        
        switch (order) {
            case "Appearance":
                orderBirth();
                //verifyAvgEdgesSize();
                break;
            case "Random":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                randomNodeOrdering();
                //verifyAvgEdgesSize();
                break;
            case "Lexicographic":
                orderLexicographic();
                //verifyAvgEdgesSize();
                break;
            case "Degree":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                Collections.sort(listAttNodes);
                orderDegree();
                //verifyAvgEdgesSize();
                break;
            case "In-degree":
                if(listInDegreeNodes.isEmpty())
                    calculateInOutDegreeNodes();
                Map<Integer, Integer> sortedInDegreeAsc = sortByComparator(listInDegreeNodes, true);
                HashMap<Integer,List<InlineNodeAttribute>> visIn = transformHashMapToVisualization(sortedInDegreeAsc);
                visualizaNos(visIn);
                //Collections.sort(listAttNodes);
                //orderDegree();
                //verifyAvgEdgesSize();
                break;
            case "Out-degree":
                if(listOutDegreeNodes.isEmpty())
                    calculateInOutDegreeNodes();
                Map<Integer, Integer> sortedOutDegreeAsc = sortByComparator(listOutDegreeNodes, true);
                HashMap<Integer,List<InlineNodeAttribute>> visOut = transformHashMapToVisualization(sortedOutDegreeAsc);
                visualizaNos(visOut);
                //Collections.sort(listAttNodes);
                //orderDegree();
                //verifyAvgEdgesSize();
                break;
            case "Activity":
                calculateActivityDegree();
                Map<Integer, Integer> sortedActivityAsc = sortByComparator(listActivityNodes, true);
                HashMap<Integer,List<InlineNodeAttribute>> vis = transformHashMapToVisualization(sortedActivityAsc);
                visualizaNos(vis);
                break;
            case "Recurrent Neighbors":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                Collections.sort(listAttNodes);
                orderRecurrentNeighbors();
                //verifyAvgEdgesSize();
                break;
            case "Minimize Edge Length":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                if(openFileEdgeLength)
                {
                    try {
                        orderNodesEdgeLength = FileHandler.leArquivoROC(pathFileEdgeLength, listAttNodes);
                    } catch (Exception ex) {
                        Logger.getLogger(NetLayoutInlineNew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                    SimulatedAnnealing();
                //verifyAvgEdgesSize();
                break;
            case "Inverse Recurrent Neighbors":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                Collections.sort(listAttNodes);
                orderInverseRecurrentNeighbors();
                //verifyAvgEdgesSize();
                break;
            //case "Minimize Edge Length":
                //orderMinimizeEdgeLength();
                //verifyAvgEdgesSize();
                //break;
            case "CPM":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                Collections.sort(listAttNodes);
                orderCPM();
                //verifyAvgEdgesSize();
                break;  
            case "CNO":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                Collections.sort(listAttNodes);
                if(openFileCNO)
                {
                    try {
                        comunidadesROCMultilevel = FileHandler.leArquivoROC(pathFileCNO, listAttNodes);
                    } catch (Exception ex) {
                        Logger.getLogger(NetLayoutInlineNew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                    executeCNOMultilevel(null, communityDetectionAlgorithm , interCommunityReorderingAlgorithm, intraCommunityReorderingAlgorithm, 0);
                System.out.println("Fim da deteccao Multilevel "+communityDetectionAlgorithm);
                
                visualizaNos(comunidadesROCMultilevel.get(0));
                
                //verifyAvgEdgesSize();
                break;
            case "File":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                Collections.sort(listAttNodes);
                
                ArrayList<Integer> nosRede = FileHandler.GetNosOrdenacaoPrevia();
                HashMap<Integer, List<InlineNodeAttribute>> ordemNos = new HashMap<>();
                ordemNos.put(0, new ArrayList<InlineNodeAttribute>());
				if(nosRede != null)


                {
                    for(Integer no : nosRede)

                    {
                        for(InlineNodeAttribute nodeAtt : listAttNodes)

                        {
                            if(nodeAtt.getId_original() == no)
                            {
                                ordemNos.get(0).add(nodeAtt);
                                break;
                            }

                        }
                    }
                    visualizaNos(ordemNos);
                }

                break;
            /*
            case "CNO Mult Louvain":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                Collections.sort(listAttNodes);
                if(veioDoStream)
                    ConverteIds(true);
                communityDetectionAlgorithm = "Original Louvain";
                executeCNOMultilevel(null, communityDetectionAlgorithm , interCommunityReorderingAlgorithm, intraCommunityReorderingAlgorithm, 0);
                System.out.println("Fim da deteccao Multilevel Louvain.");
                
                if(veioDoStream)
                    ConverteIds(false);
                visualizaNos(comunidadesROCMultilevel.get(0));
                //verifyAvgEdgesSize();
                //orderLouvainOrSLM("Multilevel Louvain", false,false);
                break;
            case "CNO Mult Infomap":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                Collections.sort(listAttNodes);
                if(veioDoStream)
                    ConverteIds(true);
                communityDetectionAlgorithm = "Infomap";
                executeCNOMultilevel(null, communityDetectionAlgorithm, interCommunityReorderingAlgorithm, intraCommunityReorderingAlgorithm, 0);
                System.out.println("Fim da deteccao Multilevel Infomap.");
                
                if(veioDoStream)
                    ConverteIds(false);
                visualizaNos(comunidadesROCMultilevel.get(0));
                //verifyAvgEdgesSize();
                break;
            case "R-CNO File":
                if(listAttNodes.isEmpty())
                    getWeightEstruturalToInline(matriz);
                
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(".//Softwares_Comunidade//"));
                int result = fileChooser.showOpenDialog(matriz);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    try
                    {
                        comunidadesROCMultilevel = FileHandler.leArquivoROC(selectedFile.getAbsolutePath(), listAttNodes);
                        visualizaNos(comunidadesROCMultilevel.get(0));
                        //verifyAvgEdgesSize();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
                */
            default:
                break;
        }
    }
    
    
    
    private HashMap<Integer,List<InlineNodeAttribute>> transformHashMapToVisualization(Map<Integer,Integer> listDegreeNodes)
    {
        
        List<InlineNodeAttribute> listDegreeNodeAttribute = new ArrayList();
        HashMap<Integer,List<InlineNodeAttribute>> sequenciaEntrada = new HashMap();
        
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        
        for(Map.Entry<Integer,Integer> node : listDegreeNodes.entrySet())
        {
            for (Object root1 : roots) 
            {
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isLineNode())
                {
                    if(att.isLeft())
                    {
                        if(att.getId_original() == node.getKey())
                        {
                            listDegreeNodeAttribute.add(att);
                            break;
                        }
                    }
                }
            }
        }
        sequenciaEntrada.put(0, listDegreeNodeAttribute);
        return sequenciaEntrada;
    }
    
    
    public List<InlineNodeAttribute> listAttNodes = new ArrayList();
    
    public void getWeightEstruturalToInline(mxGraphComponent matriz)
    {
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            if(att.isLineNode())
            {
                mxCell myCell = (mxCell) ((mxGraphModel)matriz.getGraph().getModel()).getCell(att.getId_original()+"");
                att.setDegree(myCell.getEdgeCount());
                if(att.isLeft())
                    listAttNodes.add(att);
            }
        }
    }
    
    public HashMap<Integer,Integer> listInDegreeNodes = new HashMap();
    public HashMap<Integer,Integer> listOutDegreeNodes = new HashMap();
    
    public void calculateInOutDegreeNodes()
    {
        for(Object ed : listAllEdges)
        {
            mxCell edge = (mxCell) ed;
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            
            //Prenche todos com 0 para existir um no na hora de ordenar
            if(!listInDegreeNodes.containsKey(att.getDestiny()))
                listInDegreeNodes.put(att.getDestiny(), 0);
            if(!listInDegreeNodes.containsKey(att.getOrigin()))
                listInDegreeNodes.put(att.getOrigin(), 0);
            if(!listOutDegreeNodes.containsKey(att.getDestiny()))
                listOutDegreeNodes.put(att.getDestiny(), 0);
            if(!listOutDegreeNodes.containsKey(att.getOrigin()))
                listOutDegreeNodes.put(att.getOrigin(), 0);
            
            if(listInDegreeNodes.containsKey(att.getDestiny()))
                listInDegreeNodes.put(att.getDestiny(),listInDegreeNodes.get(att.getDestiny())+1);
            
            if(listOutDegreeNodes.containsKey(att.getOrigin()))
                listOutDegreeNodes.put(att.getOrigin(),listOutDegreeNodes.get(att.getOrigin())+1);

        }
    }
    
    public Map<Integer,Integer> listActivityNodes = new HashMap();
    
    public void calculateActivityDegree()
    {
        
        listActivityNodes = new HashMap();
        
        //set thresholds for ActivityDegree
        //time Initial: 0 - 33% timestamps
        //time Final: 66% - 100% timestamps
        int initialPortionTime = lastTime;
        initialPortionTime = initialPortionTime / 3;
        int finalPortionTime = initialPortionTime * 2;
        
        Map<Integer,Integer> listFirstDegreesNodes = new HashMap();
        Map<Integer,Integer> listLastDegreesNodes = new HashMap();
        
        for(Object ed : listAllEdges)
        {
            mxCell edge = (mxCell) ed;
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            
            if(!listFirstDegreesNodes.containsKey(att.getDestiny()))
                listFirstDegreesNodes.put(att.getDestiny(), 0);
            else if(att.getTime() > 0 && att.getTime() < initialPortionTime)
                listFirstDegreesNodes.put(att.getDestiny(), listFirstDegreesNodes.get(att.getDestiny())+1);
            
            if(!listFirstDegreesNodes.containsKey(att.getOrigin()))
                listFirstDegreesNodes.put(att.getOrigin(), 0);
             else if(att.getTime() > 0 && att.getTime() < initialPortionTime)
                listFirstDegreesNodes.put(att.getOrigin(), listFirstDegreesNodes.get(att.getOrigin())+1);
            
            if(!listLastDegreesNodes.containsKey(att.getDestiny()))
                listLastDegreesNodes.put(att.getDestiny(), 0);
            else if(att.getTime() > finalPortionTime && att.getTime() < lastTime)
                listLastDegreesNodes.put(att.getDestiny(), listLastDegreesNodes.get(att.getDestiny())+1);
            
            if(!listLastDegreesNodes.containsKey(att.getOrigin()))
                listLastDegreesNodes.put(att.getOrigin(), 0);
             else if(att.getTime() > finalPortionTime && att.getTime() < lastTime)
                listLastDegreesNodes.put(att.getOrigin(), listLastDegreesNodes.get(att.getOrigin())+1);
            
        }
        
        listFirstDegreesNodes = sortByComparator(listFirstDegreesNodes, false);
        listLastDegreesNodes = sortByComparator(listLastDegreesNodes, false);
        
        int initialPortionNodes = listFirstDegreesNodes.size() / 3;
        int finalPortionNodes = initialPortionNodes * 2;
        
        ArrayList<Integer> listMissingNodes = new ArrayList();
        listMissingNodes.addAll(lineNodes);
        
        int pos = 0;
        for(Integer node : listFirstDegreesNodes.keySet())
        {
            if(pos > initialPortionNodes)
                break;
            listActivityNodes.put(node,pos);
            listMissingNodes.remove(node);
            pos++;
        }
        
        int finalX = finalPortionNodes;
        for(Integer node : listLastDegreesNodes.keySet())
        {
            if(finalX > listFirstDegreesNodes.size())
                break;
            if(listMissingNodes.contains(node))
            {
                listActivityNodes.put(node,finalX);
                listMissingNodes.remove(node);
                finalX++;
            }
        }
        
        
        for(Integer node : listMissingNodes)
        {
            listActivityNodes.put(node,pos);
            pos++;
        }
        
    }
    
    public void orderInverseRecurrentNeighbors()
    {
        ArrayList<Integer> nodesLeft = new ArrayList();
        nodesLeft.addAll(lineNodes);
        
        int[] nodesFinalOrderHeuristic = new int[lineNodes.size()+1];
        
        graph.getModel().beginUpdate();
        int position_up, position_down;
        InlineNodeAttribute lastNode = listAttNodes.get(listAttNodes.size()-1);
        nodesLeft.remove(Integer.valueOf(lastNode.getId_original()));
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
            //Move cells to origin point
            if(att.isNode()){
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                if(lastNode.getId_original() == att.getId_original())
                {
                    att.setY_atual(0);
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5 , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                }
            }
            if(att.isLineNode()){
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                if(lastNode.getId_original() == att.getId_original())
                {
                    nodesFinalOrderHeuristic[0] = att.getId_original();
                    att.setY_atual(0);
                    if(att.isLeft())
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    else
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                }
                
                currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
            }
        }
        
        position_up = 0;
        position_down = (int)(listAttNodes.size()) ;
        getNodesWithMoreConnections(lastNode.getId_original(),nodesLeft);
        int id_up = lastNode.getId_original(), id_down = 0;
        if(nodesWithConnections.size() != 1)
        {
            id_down = nodesWithConnections.get(0).getId();
            nodesLeft.remove(nodesWithConnections.get(0).getId());
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
        
        //preenchendo a parte de cima dos nós do meio
        while(position_up != listAttNodes.size())
        {
            getNodeWithMoreConnections(id_up,nodesLeft);
            id_up = nodesWithConnections.get(0).getId();
            nodesLeft.remove(nodesWithConnections.get(0).getId());
            
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                //Move cells to origin point
                if(att.isNode()){
                    if(nodesWithConnections.get(0).getId() == att.getId_original())
                    {
                        att.setY_atual(position_up);
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    }
                }
                if(att.isLineNode()){
                    if(nodesWithConnections.get(0).getId() == att.getId_original())
                    {
                        nodesFinalOrderHeuristic[position_up] = att.getId_original();
                        att.setY_atual(position_up);
                        if(att.isLeft())
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        else
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    }
                }
            }
            position_up++;
        }
        
        //preenchendo a parte de baixo dos nós do meio
        while(position_down != listAttNodes.size())
        {
            getNodeWithMoreConnections(id_down,nodesLeft);
            id_down = nodesWithConnections.get(0).getId();
            nodesLeft.remove(nodesWithConnections.get(0).getId());
            
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                //Move cells to origin point
                if(att.isNode()){
                    if(nodesWithConnections.get(0).getId() == att.getId_original())
                    {
                        att.setY_atual(position_down);
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    }
                }
                if(att.isLineNode()){
                    if(nodesWithConnections.get(0).getId() == att.getId_original())
                    {
                        nodesFinalOrderHeuristic[position_down] = att.getId_original();
                        att.setY_atual(position_down);
                        if(att.isLeft())
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        else
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    }
                }
            }
            position_down--;
        }
        
        //organizando as arestas após organizar os nós
        for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                
                if(att.isEdge())
                {
                    graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
	
	
                    int y_inicial ;

                    int i=0;
                    for(int id : nodesFinalOrderHeuristic)
                    {
                            if(id == att.getOrigin() && i != 0)
                               break;
                            i++;
                    }
                    i++;
                    int iD=0;
                    for(int id : nodesFinalOrderHeuristic)
                    {
                            if(id == att.getDestiny() && iD != 0)
                               break;
                            iD++;
                    }
                    iD++;
                    
                    String styleShapeInline = cell.getStyle();
                    
                    if(i < iD)
                    {
                            y_inicial = i;

                            styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_SOUTH,mxConstants.DIRECTION_NORTH);
                            att.setIsNorth(true);
                    }
                    else
                    {
                            y_inicial = iD;
                            styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_NORTH,mxConstants.DIRECTION_SOUTH);
                            att.setIsNorth(false);
                            
                    }
                    
                    cell.setStyle(styleShapeInline);
                    
                    att.setY_atual(y_inicial);

                    mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                    att.setHeightEdge_atual(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20)));
                    g.setHeight(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20))); 
                    cell.setGeometry(g);
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX  - moveEdgeSizeX, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
                }
        }
        
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }
    
    
    public void orderRecurrentNeighbors()
    {
        ArrayList<Integer> nodesLeft = new ArrayList();
        nodesLeft.addAll(lineNodes);
        
        int[] nodesFinalOrderHeuristic = new int[lineNodes.size()+1];
        
        graph.getModel().beginUpdate();
        int position_up, position_down;
        InlineNodeAttribute lastNode = listAttNodes.get(listAttNodes.size()-1);
        nodesLeft.remove(Integer.valueOf(lastNode.getId_original()));
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
            //Move cells to origin point
            if(att.isNode()){
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                if(lastNode.getId_original() == att.getId_original())
                {
                    att.setY_atual((int)listAttNodes.size()/2);
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5 , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                }
            }
            if(att.isLineNode()){
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                if(lastNode.getId_original() == att.getId_original())
                {
                    nodesFinalOrderHeuristic[(int)listAttNodes.size()/2] = att.getId_original();
                    att.setY_atual((int)listAttNodes.size()/2);
                    if(att.isLeft())
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    else
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                }
                currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
            }
        }
        position_up = (int)(listAttNodes.size()/2) - 1;
        position_down = (int)(listAttNodes.size()/2) + 1;
        getNodesWithMoreConnections(lastNode.getId_original(),nodesLeft);
        Collections.sort(nodesWithConnections);
        int id_up = nodesWithConnections.get(0).getId(), id_down = 0;
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
        
        
        
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            //Move cells to origin point
            if(att.isNode()){
                if(id_up == att.getId_original())
                {
                    att.setY_atual(position_up);
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                
                }
                else if(id_down == att.getId_original())
                {
                    att.setY_atual(position_down);
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                }
            }
            if(att.isLineNode()){
                if(id_up == att.getId_original())
                {
                    nodesFinalOrderHeuristic[position_up] = att.getId_original();
                    att.setY_atual(position_up);
                    if(att.isLeft())
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    else
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    
                    currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
                }
                else if(id_down == att.getId_original())
                {
                    nodesFinalOrderHeuristic[position_down] = att.getId_original();
                    att.setY_atual(position_down);
                    if(att.isLeft())
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    else
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    
                    currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
                }
            }
        }
        position_up--;
        position_down++;
        //preenchendo a parte de cima dos nós do meio
        while(position_up != 0)
        {
            getNodeWithMoreConnections(id_up,nodesLeft);
            id_up = nodesWithConnections.get(0).getId();
            nodesLeft.remove(nodesWithConnections.get(0).getId());
            
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                //Move cells to origin point
                if(att.isNode()){
                    if(nodesWithConnections.get(0).getId() == att.getId_original())
                    {
                        att.setY_atual(position_up);
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    }
                }
                if(att.isLineNode()){
                    if(nodesWithConnections.get(0).getId() == att.getId_original())
                    {
                        nodesFinalOrderHeuristic[position_up] = att.getId_original();
                        att.setY_atual(position_up);
                        if(att.isLeft())
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        else
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        
                        currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
                    }
                }
            }
            position_up--;
        }
        
        //preenchendo a parte de baixo dos nós do meio
        while(position_down <= listAttNodes.size())
        {
            getNodeWithMoreConnections(id_down,nodesLeft);
            id_down = nodesWithConnections.get(0).getId();
            nodesLeft.remove(nodesWithConnections.get(0).getId());
            
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                //Move cells to origin point
                if(att.isNode()){
                    if(nodesWithConnections.get(0).getId() == att.getId_original())
                    {
                        att.setY_atual(position_down);
                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    }
                }
                if(att.isLineNode()){
                    if(nodesWithConnections.get(0).getId() == att.getId_original())
                    {
                        nodesFinalOrderHeuristic[position_down] = att.getId_original();
                        att.setY_atual(position_down);
                        if(att.isLeft())
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        else
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        
                        currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
                    }
                }
            }
            position_down++;
        }
        
        //organizando as arestas após organizar os nós
        for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                
                if(att.isEdge())
                {
                    graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
	
	
                    int y_inicial ;

                    int i=0;
                    for(int id : nodesFinalOrderHeuristic)
                    {
                            if(id == att.getOrigin() && i != 0)
                               break;
                            i++;
                    }
                    i++;
                    int iD=0;
                    for(int id : nodesFinalOrderHeuristic)
                    {
                            if(id == att.getDestiny() && iD != 0)
                               break;
                            iD++;
                    }
                    iD++;
                    
                    String styleShapeInline = cell.getStyle();
                    
                    if(i < iD)
                    {
                            y_inicial = i;

                            styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_SOUTH,mxConstants.DIRECTION_NORTH);
                            att.setIsNorth(true);
                    }
                    else
                    {
                            y_inicial = iD;
                            styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_NORTH,mxConstants.DIRECTION_SOUTH);
                            att.setIsNorth(false);
                            
                    }
                    
                    cell.setStyle(styleShapeInline);
                    
                    att.setY_atual(y_inicial);

                    mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                    att.setHeightEdge_atual(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20)));
                    g.setHeight(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20))); 
                    cell.setGeometry(g);
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX  - moveEdgeSizeX, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
                }
        }
        
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
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
    
    private ArrayList<OccurrenceMap> nodesWithConnections = new ArrayList();
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
    
    public void getNodeWithMoreConnections(Integer id, ArrayList<Integer> nodesLeft)
    {
        nodesWithConnections = new ArrayList();
        
        int idGreaterNode = -1;
        int weightGreaterNode = 0;
        
        for(Object ed : listEdgesJgraph)
        {
            mxCell edge = (mxCell) ed;
            String idEdge = edge.getId();
            String[] parts = idEdge.split(" ");
            String idSource = parts[0];
            String idTarget = parts[1];
            CustomAttributes att = (CustomAttributes) edge.getValue();
            
            if(att.isEdge())
            {
                if(idSource.equals(id+"") && nodesLeft.contains(Integer.valueOf(idTarget)))
                {
                    if(att.getWeight() > weightGreaterNode)
                    {
                        weightGreaterNode = att.getWeight();
                        idGreaterNode = Integer.parseInt(idTarget);
                    }
                }
                if(idTarget.equals(id+"") && nodesLeft.contains(Integer.valueOf(idSource)))
                {
                    if(att.getWeight() > weightGreaterNode)
                    {
                        weightGreaterNode = att.getWeight();
                        idGreaterNode = Integer.parseInt(idSource);
                    }
                }
            }
        }
        if(idGreaterNode != -1)
        {
            
            OccurrenceMap om = new OccurrenceMap(idGreaterNode,weightGreaterNode);
            nodesWithConnections.add(om);
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
                    break;
                }

            }
        }
    }
    
    
    public void orderLexicographic(){
        ArrayList<Integer> orderLexic = new ArrayList();
        orderLexic.addAll(lineNodes);
        Collections.sort(orderLexic);
        
        graph.getModel().beginUpdate();
        
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
            //Move cells to origin point
            if(att.isLineNode()){
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                int i=1;
                for(int at : orderLexic)
                {
                    if(at == att.getId_original())
                       break;
                    i++;
                }

                att.setY_atual(i);
                if(att.isLeft())
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                else
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                
                currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
            }
            if(att.isNode() ){
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                int i=1;
                for(int at : orderLexic)
                {
                    if(at == att.getId_original())
                       break;
                    i++;
                }
                
                att.setY_atual(i);
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
            }
            if(att.isEdge())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                
                int y_inicial ;
                
                int i=2;
                for(int at : orderLexic)
                {
                    if(at == att.getOrigin())
                       break;
                    i++;
                }
                
                int iD=2;
                for(int at : orderLexic)
                {
                    if(at == att.getDestiny())
                       break;
                    iD++;
                }
                
                String styleShapeInline = cell.getStyle();
                
                if(i < iD)
                {
                    y_inicial = i;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_SOUTH, mxConstants.DIRECTION_NORTH);
                    att.setIsNorth(true);
                }
                else
                {
                    y_inicial = iD;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_NORTH, mxConstants.DIRECTION_SOUTH);
                    
                    att.setIsNorth(false);
                }
                
                cell.setStyle(styleShapeInline);
                
                att.setY_atual(y_inicial);
                
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                att.setHeightEdge_atual(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20)));
                g.setHeight(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20))); 
                cell.setGeometry(g);
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX  - moveEdgeSizeX, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
            }
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
        
    }
    
    public void orderDegree(){
        graph.getModel().beginUpdate();
        
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
            //Move cells to origin point
            if(att.isLineNode()){
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                int i=1;
                for(InlineNodeAttribute attNode : listAttNodes)
                {
                    if(attNode.getId_original() == att.getId_original())
                       break;
                    i++;
                }
                
                att.setY_atual(i);
                if(att.isLeft())
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                else
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                
                currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
            }
            if(att.isNode() ){
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                int i=1;
                for(InlineNodeAttribute attNode : listAttNodes)
                {
                    if(attNode.getId_original() == att.getId_original())
                       break;
                    i++;
                }
                
                att.setY_atual(i);
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
            }
            if(att.isEdge())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                
                int y_inicial ;
                
                int i=2;
                for(InlineNodeAttribute attNode : listAttNodes)
                {
                    if(attNode.getId_original() == att.getOrigin())
                       break;
                    i++;
                }
                
                int iD=2;
                for(InlineNodeAttribute attNode : listAttNodes)
                {
                    if(attNode.getId_original() == att.getDestiny())
                       break;
                    iD++;
                }
                
                String styleShapeInline = cell.getStyle();
                
                if(i < iD)
                {
                    y_inicial = i;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_SOUTH, mxConstants.DIRECTION_NORTH);
                    att.setIsNorth(true);
                }
                else
                {
                    y_inicial = iD;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_NORTH, mxConstants.DIRECTION_SOUTH);
                    
                    att.setIsNorth(false);
                }
                
                cell.setStyle(styleShapeInline);
                
                att.setY_atual(y_inicial);
                
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                att.setHeightEdge_atual(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20)));
                g.setHeight(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20))); 
                cell.setGeometry(g);
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX  - moveEdgeSizeX, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
            }
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }
     
    
    public void orderMinimizeEdgeLength(){
        
        graph.getModel().beginUpdate();
        
        Object[] roots2 = graph.getChildCells(graph.getDefaultParent(), true, true);
        
        int matrizLengthEdge[][] = new int[lineNodes.size()+1][lineNodes.size()+1];
        
        for (int i=0; i<lineNodes.size()+1; i++) {
            for (int j=0; j<lineNodes.size()+1; j++) {
                matrizLengthEdge[i][j] = 0;
            }
        }
        
        ArrayList<Integer> containsIdGeneratedNodes = new ArrayList();
        
        for (Object root1 : roots2) {
            mxCell cell = (mxCell) root1;
            if(cell.isEdge())
            {
                String idEdge = (String) cell.getValue();
                String[] parts = idEdge.split(" ");
                if(parts.length == 2)
                {
                    
                    String idEdge1 = (String) cell.getId();
                    String[] parts2 = idEdge1.split(" ");
                    
                    mxCell v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(parts[0]+" "+parts2[0]);
                    mxCell v2 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(parts[1]+" "+parts2[0]);
                    InlineNodeAttribute attV1 = (InlineNodeAttribute) v1.getValue();
                    InlineNodeAttribute attV2 = (InlineNodeAttribute) v2.getValue();
                    
                    if(!containsIdGeneratedNodes.contains(attV1.getY_original()))
                        containsIdGeneratedNodes.add(attV1.getY_original());
                    if(!containsIdGeneratedNodes.contains(attV2.getY_original()))
                        containsIdGeneratedNodes.add(attV2.getY_original());
                    
                    matrizLengthEdge[attV1.getY_original()][attV2.getY_original()] += Math.abs(attV1.getY_original() - attV2.getY_original());
                    matrizLengthEdge[attV2.getY_original()][attV1.getY_original()] += Math.abs(attV1.getY_original() - attV2.getY_original());
                }
            }
        }
        
        
        int orderNodeNumber = 1;
        ArrayList<Integer> containsNodesUsed = new ArrayList();
        while(true)
        {
            int maior = matrizLengthEdge[0][0];
            int iMaior = 0, jMaior = 0;
            for (int i=0; i<lineNodes.size()+1; i++) {
                for (int j=0; j<lineNodes.size()+1; j++) {
                    if(matrizLengthEdge[i][j] > maior)
                    {
                        maior = matrizLengthEdge[i][j];
                        iMaior = i;
                        jMaior = j;
                    }
                }
            }
            
            if(maior == 0)
                break;

            
            if(containsNodesUsed.contains(iMaior) || containsNodesUsed.contains(jMaior))
            {
                matrizLengthEdge[iMaior][jMaior] = 0;
                matrizLengthEdge[jMaior][iMaior] = 0;
                continue;
            }
            else
            {
                
                containsNodesUsed.add(iMaior);
                containsNodesUsed.add(jMaior);
            }
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
                if(att.getY_original() == iMaior )
                {

                    //Move cells to origin point
                    if(att.isLineNode())
                    {
                        graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                        att.setX_atual(att.getX_original());
                        att.setY_atual(orderNodeNumber);
                        if(att.isLeft())
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        else
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        
                        currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
                    }
                    else if(att.isNode())
                    {
                        graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                        att.setX_atual(att.getX_original());
                        att.setY_atual(orderNodeNumber);


                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
                    }
                }
                else if(att.getY_original() == jMaior)
                {

                    //Move the second node

                    //Move cells to origin point
                    if(att.isLineNode())
                    {
                        graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                        att.setX_atual(att.getX_original());
                        att.setY_atual(orderNodeNumber+1);
                        if(att.isLeft())
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        else
                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                    }
                    else if(att.isNode())
                    {
                        graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                        att.setX_atual(att.getX_original());
                        att.setY_atual(orderNodeNumber+1);


                        graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
                    }
                }
                
            }
            orderNodeNumber++;
            orderNodeNumber++;
            matrizLengthEdge[iMaior][jMaior] = 0;
            matrizLengthEdge[jMaior][iMaior] = 0;
        }
        
        ArrayList<Integer> containsNodesNotUsed = new ArrayList();
        for(Integer lineNode : containsIdGeneratedNodes)
        {
            if(!containsNodesUsed.contains(lineNode))
                containsNodesNotUsed.add(lineNode);
        }
        
        if(!containsNodesNotUsed.isEmpty())
        {
            for(Integer nodeNotUsed : containsNodesNotUsed)
            {
                Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
                for (Object root1 : roots) {
                    Object[] root = {root1};
                    mxCell cell = (mxCell) root1;
                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                    if(nodeNotUsed == att.getY_original())
                    {
                        //Move cells to origin point
                        if(att.isLineNode())
                        {
                            graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                            att.setX_atual(att.getX_original());
                            att.setY_atual(orderNodeNumber);
                            if(att.isLeft())
                                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                            else
                                graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                        }
                        else if(att.isNode())
                        {
                            graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                            att.setX_atual(att.getX_original());
                            att.setY_atual(orderNodeNumber);


                            graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
                        }
                    }
                }
                orderNodeNumber++;
            }
            
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }
    
    int moveEdgeSizeX = 1;
    
    
    public void orderCPM(){

        //CALCULO DO ALGORITMO DE COMUNIDADE

        comunidades = new CPM().execute(graph, listAllEdges, listAttNodes);
            
         //FIM CALCULO ALGORITMO DE COMUNIDADE
             
        Integer[] hasPosition = new Integer[(listAttNodes.size()/2)+100];

        int countPosition = 1;
        
        int i,j;
        for(i = 0; i < comunidades.size(); i++){
            boolean addNewNode = false;
            for(j = 0; j < comunidades.get(i).size(); j++){
                int position = countPosition;
                if(hasPosition[comunidades.get(i).get(j).getId_original()] == null)
                {
                    hasPosition[comunidades.get(i).get(j).getId_original()] = position;
                    countPosition++;
                    addNewNode = true;
                }
            }
            if(addNewNode)
                countPosition++;
        }
        
        for(i = 1; i < hasPosition.length;i++)
        {
            if(hasPosition[i] == null)
            {
                hasPosition[i] = countPosition;
                countPosition++;
            }
        }
        
        graph.getModel().beginUpdate();
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            if(att.isGraphLine())
            {
                //Move cells to origin point
                double x = cell.getGeometry().getX();
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                att.setY_atual(att.getY_original()+150);
                
                graph.moveCells(root, x, att.getY_atual());
                
            }
            if(att.isLineNode())
            {
                //Move cells to origin point
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                att.setY_atual(hasPosition[att.getId_original()]);
                
                if(att.isLeft())
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                else
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                
                currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
            }
            else if(att.isNode())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());

                att.setY_atual(hasPosition[att.getId_original()]);
                
                
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5 , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
            }
            else if(att.isEdge())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
        
                
                int y_inicial ;
                
                i=1;
                i+=hasPosition[att.getOrigin()];
                int iD=1;
                iD+=hasPosition[att.getDestiny()];
                String styleShapeInline = cell.getStyle();
                
                if(i < iD)
                {
                    y_inicial = i;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_SOUTH, mxConstants.DIRECTION_NORTH);
                    att.setIsNorth(true);
                }
                else
                {
                    y_inicial = iD;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_NORTH, mxConstants.DIRECTION_SOUTH);
                    
                    att.setIsNorth(false);
                }
                
                cell.setStyle(styleShapeInline);
                
                //i/2 because listAttNodes has duplicate values
                att.setY_atual(y_inicial);
                
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                att.setHeightEdge_atual(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20)));
                g.setHeight(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20))); 
                cell.setGeometry(g);
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX  - moveEdgeSizeX, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
            }
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }
     
    
    
    private String obtemOutLiers(HashMap<Integer, List<InlineNodeAttribute>> comunidades, List<InlineNodeAttribute> listAttNodesWithoutDuplicates)
    {
        
        String outliers = "";
        for (int i = 0; i < listAttNodesWithoutDuplicates.size(); i++)
        {
            boolean oNoEhOutlier = true;
            for(int j = 0; j < comunidades.keySet().size(); j++)
            {
                if(comunidades.get(j).contains(listAttNodesWithoutDuplicates.get(i)))
                {
                    oNoEhOutlier = false;
                    break;
                }
            }
            if(oNoEhOutlier)
                outliers += listAttNodesWithoutDuplicates.get(i).getId_original() + ", ";
        }
        if(outliers == "")
            return "";
        return outliers.substring(0,outliers.length() - 2);
    }
    
    @Deprecated
    public void orderInfoMap(boolean ordered, boolean RN){

        //CALCULO DO ALGORITMO DE COMUNIDADE
        
      
        comunidades = new InfoMap().execute(listAllEdges, listAttNodes);
            
        System.out.println("Ouliers =   " + obtemOutLiers(comunidades,listAttNodes));
        
        new Statistic().calculaFMeasure(comunidades, "Infomap");
        
        double q = modularity();
        System.out.println("Modularity Measure =   " + BigDecimal.valueOf(q).toPlainString());
        
         //FIM CALCULO ALGORITMO DE COMUNIDADE
           
        Integer[] hasPosition = new Integer[maxId];

        int countPosition = 1;
        
        int i,j;
        
                
        int[] comunityPosition = new int[comunidades.size()];
        
        if(ordered)
        {

            int[][] matrizComunityRelation = new int[comunidades.size()][comunidades.size()];

            for(i = 0; i < matrizComunityRelation.length; i++)
            {
                for(j = 0; j < matrizComunityRelation.length; j++)
                {
                    matrizComunityRelation[i][j] = 0;
                }
            }

            for(Object ed : listAllEdges)
            {
                mxCell edge = (mxCell) ed;
                InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();

                boolean findOrigin = false, findDestiny = false;
                boolean finished = false;

                int comunityOrigin = 0, comunityDestiny = 0;

                for(i = 0; i < comunidades.size() && !finished; i++){
                    for(j = 0; j < comunidades.get(i).size(); j++){
                        if(findOrigin && findDestiny)
                        {
                            finished = true;
                            break;
                        }
                        if(att.getOrigin() == comunidades.get(i).get(j).getId_original())
                        {
                            findOrigin = true;
                            comunityOrigin = i;
                        }
                        if(att.getDestiny() == comunidades.get(i).get(j).getId_original())
                        {
                            findDestiny = true;
                            comunityDestiny = i;
                        }
                    }
                }
                if(comunityOrigin == comunityDestiny)
                    matrizComunityRelation[comunityOrigin][comunityDestiny] += 1;
                else
                {
                    matrizComunityRelation[comunityOrigin][comunityDestiny] += 1;
                    matrizComunityRelation[comunityDestiny][comunityOrigin] += 1;
                }
            }

            

            for(i = 0; i < comunityPosition.length; i++)
            {
                if(i == 0)
                    comunityPosition[i] = 1;
                else
                    comunityPosition[i] = 0;
            }

            
            ArrayList<Integer> usedCommunities = new ArrayList();
            usedCommunities.add(0);
            
            int mostConnectedComunity = 0;
            int idCommunity = 1;
            for(i = 0; i < matrizComunityRelation.length; i++)
            {
                boolean communityDetected = false;
                int biggestConnection = 0, cc = 0;
                for(j = 0; j < matrizComunityRelation.length; j++)
                {
                    if(matrizComunityRelation[mostConnectedComunity][j] > biggestConnection && mostConnectedComunity != j && !usedCommunities.contains(j))
                    {
                        
                        biggestConnection = matrizComunityRelation[i][j];
                        cc = j;
                        
                        communityDetected = true;

                    }
                }
                if(communityDetected)
                {
                    mostConnectedComunity = cc;
                    usedCommunities.add(mostConnectedComunity);
                    idCommunity++;
                    comunityPosition[mostConnectedComunity] = idCommunity;
                }
                else if(!usedCommunities.contains(i))
                {
                    idCommunity++;
                    comunityPosition[i] = idCommunity;
                }
            }

            
            //Printing the contact matrices
            System.out.println("Contact matrices:");
            
            for(i = 0; i < matrizComunityRelation.length; i++)
            {
                
                for(j = 0; j < matrizComunityRelation.length; j++)
                {
                    System.out.print(matrizComunityRelation[i][j]+" ");
                }
                System.out.println();
            }
            //END Printing

        }
        
        int[] nodesFinalRNOrder = new int[maxId];
        
        if(RN)
        {
            nodesFinalRNOrder = recurrentNeighborsAlgorithm(comunidades, comunityPosition);
        }
        
        
        
        for(i = 1; i <= comunidades.size(); i++){
            boolean addNewNode = false;
            int orderComunity = 0;
            
            if(ordered)
            {
                for(int aa = 0; aa < comunityPosition.length; aa++)
                {
                    if(comunityPosition[aa] == i)
                    {
                        orderComunity = aa;
                        break;
                    }
                }
            }
            else
                orderComunity = i-1;
            
            for(j = 0; j < comunidades.get(orderComunity).size(); j++){
                int position = countPosition;
                
                if(hasPosition[comunidades.get(orderComunity).get(j).getId_original()] == null)
                {
                    hasPosition[comunidades.get(orderComunity).get(j).getId_original()] = position;
                    countPosition++;
                    addNewNode = true;
                }
            }
            if(addNewNode)
                countPosition++;
            
        }
        
        for(i = 1; i < hasPosition.length;i++)
        {
            if(hasPosition[i] == null)
            {
                hasPosition[i] = countPosition;
                countPosition++;
            }
        }
        
        graph.getModel().beginUpdate();
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            if(att.isGraphLine())
            {
                //Move cells to origin point
                double x = cell.getGeometry().getX();
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                att.setY_atual(att.getY_original()+150);
                
                graph.moveCells(root, x, att.getY_atual());
                
            }
            if(att.isLineNode())
            {
                //Move cells to origin point
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                
                if(RN)
                    att.setY_atual(nodesFinalRNOrder[att.getId_original()]);
                else
                    att.setY_atual(hasPosition[att.getId_original()]);
                    
                
                if(att.isLeft())
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                else
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                
                currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
            }
            else if(att.isNode())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());

                if(RN)
                    att.setY_atual(nodesFinalRNOrder[att.getId_original()]);
                else
                    att.setY_atual(hasPosition[att.getId_original()]);
                
                
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5 , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
            }
            else if(att.isEdge())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
        
                
                int y_inicial ;
                
                i=1;
                if(RN)
                    i+=nodesFinalRNOrder[att.getOrigin()];
                else
                    i+=hasPosition[att.getOrigin()];
                int iD=1;
                if(RN)
                    iD+=nodesFinalRNOrder[att.getDestiny()];
                else
                    iD+=hasPosition[att.getDestiny()];
                String styleShapeInline = cell.getStyle();
                
                if(i < iD)
                {
                    y_inicial = i;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_SOUTH, mxConstants.DIRECTION_NORTH);
                    att.setIsNorth(true);
                }
                else
                {
                    y_inicial = iD;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_NORTH, mxConstants.DIRECTION_SOUTH);
                    
                    att.setIsNorth(false);
                }
                
                cell.setStyle(styleShapeInline);
                
                //i/2 because listAttNodes has duplicate values
                att.setY_atual(y_inicial);
                
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                att.setHeightEdge_atual(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20)));
                g.setHeight(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20))); 
                cell.setGeometry(g);
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX  - moveEdgeSizeX, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
            }
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }
    
    
    public void addLabelLineGraph(int res)
    {
       
            String[] timeLabels = new String[100000];

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

                    String tmp, strLine = "";
                    while ((tmp = file.readLine()) != null)
                    {
                        strLine = tmp;
                        String[] tokens = strLine.split(" ");
                        
                        if(res != 1)
                        {
                            int t = (int) Math.floor(Integer.parseInt(tokens[0]) / res);
                            tokens[0] = t + "";
                        }
                        
                        timeLabels[Integer.parseInt(tokens[0])] = "     "+ tokens[1] + " " + tokens[2];
                    }
                    
                } catch (FileNotFoundException ex) {
                Logger.getLogger(NetLayoutInlineNew.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NetLayoutInlineNew.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    
        graph.getModel().beginUpdate();
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            if(att.isGraphLineNodes())
            {
                att.setLabel(timeLabels[att.getTime()]);
                
            }
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }
    @Deprecated
    public int[] recurrentNeighborsAlgorithm(HashMap<Integer, List<InlineNodeAttribute>> comunidades, int[] comunityPosition)
    {
        
        int[] nodesFinalRNOrder = new int[maxId];
        
        ArrayList<Integer> usedComunity = new ArrayList<>();
            int positionTotal = 1;
            
            //Recurrent Neighbors
            for(int i = 1; i <= comunidades.size(); i++){
                
                if(comunidades.get(i) == null)
                    continue;
                else if(comunidades.get(i).isEmpty())
                    continue;
                
                int orderComunity = -1;
                
                for(int aa = 0; aa < comunityPosition.length; aa++)
                {
                    if(comunityPosition[aa] == i)
                    {
                        if(!usedComunity.contains(aa))
                        {
                            usedComunity.add(aa);
                            orderComunity = aa;
                            break;
                        }
                    }
                }
                
                if(orderComunity == -1)
                    continue;
                
                if(comunidades.get(orderComunity).size() <= 3)
                {
                    for(int j = 0; j < comunidades.get(orderComunity).size(); j++){
                        nodesFinalRNOrder[comunidades.get(orderComunity).get(j).getId_original()] = positionTotal;
                        positionTotal++;
                    }
                    positionTotal++;
                }
                else
                {

                    ArrayList<Integer> nodesLeft = new ArrayList();
                    for(int j = 0; j < comunidades.get(orderComunity).size(); j++){
                        nodesLeft.add(comunidades.get(orderComunity).get(j).getId_original());
                    }
                    int biggestWeight = 0;
                    int id_biggestWeight = 0;
                    //Get node with biggest weight inside the first community
                    for(InlineNodeAttribute att : listAttNodes)
                    {
                        if(nodesLeft.contains(att.getId_original()))
                        {
                            if(att.getDegree() > biggestWeight)
                            {
                                biggestWeight = att.getDegree();
                                id_biggestWeight = att.getId_original();
                            }
                        }
                    }

                    int position_up, position_down;
                    int lastNode = id_biggestWeight;
                    
                    nodesFinalRNOrder[lastNode] = (int) comunidades.get(orderComunity).size()/2 + positionTotal;
                    position_up = ((int) comunidades.get(orderComunity).size()/2) -1 + positionTotal;
                    position_down = ((int) comunidades.get(orderComunity).size()/2) +1 + positionTotal;
                    
                    nodesLeft.remove(Integer.valueOf(lastNode));
                    getNodesWithMoreConnections(lastNode,nodesLeft);
                    Collections.sort(nodesWithConnections);
                    int id_up = 0;
                    if(!nodesWithConnections.isEmpty())
                        id_up = nodesWithConnections.get(0).getId();
                    else
                    {
                        //Get node with biggest weight inside the first community
                        for(InlineNodeAttribute att : listAttNodes)
                        {
                            if(nodesLeft.contains(att.getId_original()))
                            {
                                if(att.getDegree() > biggestWeight)
                                {
                                    biggestWeight = att.getDegree();
                                    id_biggestWeight = att.getId_original();
                                }
                            }
                        }
                        id_up = id_biggestWeight;
                    }
                    int id_down = 0;
                    nodesLeft.remove(Integer.valueOf(id_up));
                    if(nodesWithConnections.size() != 1)
                    {
                        id_down = nodesWithConnections.get(1).getId();
                        nodesLeft.remove(nodesWithConnections.get(1).getId());
                    }
                    else
                    {
                        for(int ik=listAttNodes.size()-1;ik>=0;ik--)
                        {
                            InlineNodeAttribute att = listAttNodes.get(ik);
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
                    nodesFinalRNOrder[id_up] = position_up;
                    nodesFinalRNOrder[id_down] = position_down;
                    position_up--;
                    position_down++;


                    //preenchendo a parte de cima dos nós do meio
                    while(position_up >= positionTotal)
                    {
                        getNodeWithMoreConnections(id_up,nodesLeft);
                        if(!nodesWithConnections.isEmpty())
                            id_up = nodesWithConnections.get(0).getId();
                        else
                        {
                            //Get node with biggest weight inside the first community
                            for(InlineNodeAttribute att : listAttNodes)
                            {
                                if(nodesLeft.contains(att.getId_original()))
                                {
                                    if(att.getDegree() > biggestWeight)
                                    {
                                        biggestWeight = att.getDegree();
                                        id_biggestWeight = att.getId_original();
                                    }
                                }
                            }
                            id_up = id_biggestWeight;
                        }
                        nodesLeft.remove(Integer.valueOf(id_up));
                        nodesFinalRNOrder[id_up] = position_up;
                        position_up--;
                    }

                    //preenchendo a parte de baixo dos nós do meio

                    while(position_down < ( comunidades.get(orderComunity).size() + positionTotal ))
                    {

                        getNodeWithMoreConnections(id_down,nodesLeft);
                        if(!nodesWithConnections.isEmpty())
                            id_down = nodesWithConnections.get(0).getId();
                        else
                        {
                            //Get node with biggest weight inside the first community
                            for(InlineNodeAttribute att : listAttNodes)
                            {
                                if(nodesLeft.contains(att.getId_original()))
                                {
                                    if(att.getDegree() > biggestWeight)
                                    {
                                        biggestWeight = att.getDegree();
                                        id_biggestWeight = att.getId_original();
                                    }
                                }
                            }
                            id_down = id_biggestWeight;
                        }
                        nodesLeft.remove(Integer.valueOf(id_down));
                        nodesFinalRNOrder[id_down] = position_down;
                        position_down++;
                    }
                    //ADD BLANK SPACE IN THE TEMPORAL NETWORK
                    if(blankSpaceCommunities)
                        position_down++;
                    positionTotal = position_down;
                }
                
               
            }
            
            //positionTotal--;
            //NODES WITHOUT COMUNITY
            for(int aa = 0; aa < nodesFinalRNOrder.length; aa++)
            {
                if(nodesFinalRNOrder[aa] == 0 && lineNodes.contains(aa) )
                {
                   nodesFinalRNOrder[aa] = positionTotal;
                   positionTotal++;
                }
            }
            
            //END Recurrent Neighbors
            
            return nodesFinalRNOrder;
    }
	
	//HashMap<Integer, List<InlineNodeAttribute>> sequenciaDeEntrada
    //int passo
    //Community id é o id da comunidade que terá seus nós reordenados no passo 3, esse metodo por default é chamado para todas as comunidades. Assim, esse parametro é importante caso o usuario deseje "quebrar" em uma comunidade específica
    public HashMap<Integer, List<InlineNodeAttribute>> recurrentNeighborsAlgorithm(HashMap<Integer, List<InlineNodeAttribute>> sequenciaDeEntrada, int[][] matrizComunityRelation, int step)
    {
        HashMap<Integer, List<InlineNodeAttribute>> sequenciaResultante = new HashMap<>();
       if(step == 2)
        {
            //STEP 2 - INTER COMMUNITIES
            int i,j;
            ArrayList<Integer> usedCommunities = new ArrayList();
            int[] comunityPosition = new int[sequenciaDeEntrada.size()];
           
            for(i = 0; i < comunityPosition.length; i++)
            {
                comunityPosition[i] = 0;
            }

            int mostConnectedComunity = 0;
            int positionMiddle = sequenciaDeEntrada.size()/2;
            int biggestCommunity = 0, connectionBiggestCommunity = 0;
            for(i = 0; i < matrizComunityRelation.length; i++)
            {
                for(j = 0; j < matrizComunityRelation.length; j++)
                {
                    if(i == j)
                    {
                        if(connectionBiggestCommunity < matrizComunityRelation[i][j])
                        {
                            connectionBiggestCommunity = matrizComunityRelation[i][j];
                            biggestCommunity = i;
                        }
                    }
                }
            }
            
            /*
            PARTE ALTERNATIVA DO CODIGO PARA RN INTER FUNCIONAR DE ACORDO COM O SOMATORIO DOS GRAUS DOS NOS
            Map<Integer,Integer> communityIdxDegree = new HashMap();
            for(Entry<Integer,List<InlineNodeAttribute>> comunidade : sequenciaDeEntrada.entrySet())
            {
                int communityDegree = 0; 
                
                for(int x = 0; x < comunidade.getValue().size(); x++)
                    communityDegree += comunidade.getValue().get(x).getDegree();
                
                communityIdxDegree.put(comunidade.getKey(), communityDegree);
                
            }
            
            communityIdxDegree = sortByComparator(communityIdxDegree, false);
            for(Entry<Integer,Integer> comunidade : communityIdxDegree.entrySet())
            {
                biggestCommunity = comunidade.getKey();
                connectionBiggestCommunity = comunidade.getValue();
                break;
            }
            */
            
            //the biggest community is dispose in the center of the layout
            comunityPosition[biggestCommunity] = positionMiddle;
            usedCommunities.add(biggestCommunity);
            //detect the two mosted connected communities to the biggest one
            int firstCommunity = 0, secondCommunity = 0, valueFirst = -1, valueSecond = -1;
            for(i = 0; i < matrizComunityRelation.length; i++)
            {
                if(i != biggestCommunity && matrizComunityRelation[biggestCommunity][i] > valueFirst) 
                {
                    valueFirst = matrizComunityRelation[biggestCommunity][i];
                    firstCommunity = i;
                }
            }
            
            int position_up = positionMiddle-1;
            int position_down = positionMiddle+1;
            
            if(sequenciaDeEntrada.size() > 2)
            {
                for(i = 0; i < matrizComunityRelation.length; i++)
                {
                    if(i != biggestCommunity && i != firstCommunity  && matrizComunityRelation[biggestCommunity][i] > valueSecond )
                    {
                        valueSecond = matrizComunityRelation[biggestCommunity][i];
                        secondCommunity = i;
                    }
                }
                
                comunityPosition[secondCommunity] = position_down;
                usedCommunities.add(secondCommunity);
                
            }
            
            //set the first and second communities in the first step of RN algorithm
            comunityPosition[firstCommunity] = position_up;
            usedCommunities.add(firstCommunity);
            
            while(position_up > 0)
            {
                biggestCommunity = firstCommunity;
                valueFirst = -1;
                firstCommunity = 0;
                for(i = 0; i < matrizComunityRelation.length; i++)
                {
                    if(i != biggestCommunity && matrizComunityRelation[biggestCommunity][i] > valueFirst && !usedCommunities.contains(i)) 
                    {
                        valueFirst = matrizComunityRelation[biggestCommunity][i];
                        firstCommunity = i;
                    }
                }
                position_up--;
                comunityPosition[firstCommunity] = position_up;
                usedCommunities.add(firstCommunity);
                
            }
            while(position_down < sequenciaDeEntrada.size()-1)
            {
                biggestCommunity = secondCommunity;
                valueFirst = -1;
                secondCommunity = 0;
                for(i = 0; i < matrizComunityRelation.length; i++)
                {
                    if(i != biggestCommunity && matrizComunityRelation[biggestCommunity][i] > valueFirst && !usedCommunities.contains(i)) 
                    {
                        valueFirst = matrizComunityRelation[biggestCommunity][i];
                        secondCommunity = i;
                    }
                }
                position_down++;
                comunityPosition[secondCommunity] = position_down;
                usedCommunities.add(secondCommunity); 
            }
            
            //MONTAR O HASHMAP APARTIR DO comunityPosition
            for(int idx = 0; idx < comunityPosition.length; idx++)
            {
                sequenciaResultante.put(idx, sequenciaDeEntrada.get(comunityPosition[idx]));
            }
            
            //END STEP 2 - INTER COMMUNITIES
        }
        else if(step == 3)
        {
            //STEP 3 - INTRA COMMUNITIES
            int i,j;
            int[] nodesFinalRNOrder = new int[maxId];

            ArrayList<Integer> nodesAdd = new ArrayList();
            
            int positionTotal = 1;
            
            for(i = 0; i < sequenciaDeEntrada.size(); i++){
                Map<Integer, Integer> sequenciaComunidade = new HashMap<>();
                
                if(sequenciaDeEntrada.get(i) == null)
                    continue;
                else if(sequenciaDeEntrada.get(i).isEmpty())
                    continue;

                ArrayList<Integer> nodesLeft = new ArrayList();
                
                for(j = 0; j < sequenciaDeEntrada.get(i).size(); j++){
                    nodesLeft.add(sequenciaDeEntrada.get(i).get(j).getId_original());
                }
                int biggestWeight = 0;
                int id_biggestWeight = 0;
                //Get node with biggest weight inside the first community
                for(InlineNodeAttribute att : listAttNodes)
                {
                    if(nodesLeft.contains(att.getId_original()))
                    {
                        if(att.getDegree() > biggestWeight)
                        {
                            biggestWeight = att.getDegree();
                            id_biggestWeight = att.getId_original();
                        }
                    }
                }

                int position_up, position_down;
                int lastNode = id_biggestWeight;

                nodesFinalRNOrder[lastNode] = (int) sequenciaDeEntrada.get(i).size()/2 + positionTotal;
                nodesAdd.add(lastNode);
                sequenciaComunidade.put(lastNode, (int) sequenciaDeEntrada.get(i).size()/2 + positionTotal);
                
                position_up = ((int) sequenciaDeEntrada.get(i).size()/2) -1 + positionTotal;
                position_down = ((int) sequenciaDeEntrada.get(i).size()/2) +1 + positionTotal;

                nodesLeft.remove(Integer.valueOf(lastNode));
                getNodesWithMoreConnections(lastNode,nodesLeft);
                Collections.sort(nodesWithConnections);
                int id_up = 0;
                if(!nodesWithConnections.isEmpty())
                    id_up = nodesWithConnections.get(0).getId();
                else
                {
                    //Get node with biggest weight inside the first community
                    for(InlineNodeAttribute att : listAttNodes)
                    {
                        if(nodesLeft.contains(att.getId_original()))
                        {
                            if(att.getDegree() > biggestWeight)
                            {
                                biggestWeight = att.getDegree();
                                id_biggestWeight = att.getId_original();
                            }
                        }
                    }
                    id_up = id_biggestWeight;
                }
                int id_down = 0;
                nodesLeft.remove(Integer.valueOf(id_up));
                if(nodesWithConnections.size() > 1)
                {
                    id_down = nodesWithConnections.get(1).getId();
                    nodesLeft.remove(nodesWithConnections.get(1).getId());
                }
                else
                {
                    for(int ik=listAttNodes.size()-1;ik>=0;ik--)
                    {
                        InlineNodeAttribute att = listAttNodes.get(ik);
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
                nodesFinalRNOrder[id_up] = position_up;
                nodesFinalRNOrder[id_down] = position_down;
                nodesAdd.add(id_up);
                nodesAdd.add(id_down);
                sequenciaComunidade.put(id_up, position_up);
                sequenciaComunidade.put(id_down, position_down);
                position_up--;
                position_down++;


                //preenchendo a parte de cima dos nós do meio
                while(position_up >= positionTotal)
                {
                    getNodeWithMoreConnections(id_up,nodesLeft);
                    if(!nodesWithConnections.isEmpty())
                        id_up = nodesWithConnections.get(0).getId();
                    else
                    {
                        //Get node with biggest weight inside the first community
                        for(InlineNodeAttribute att : listAttNodes)
                        {
                            if(nodesLeft.contains(att.getId_original()))
                            {
                                if(att.getDegree() > biggestWeight)
                                {
                                    biggestWeight = att.getDegree();
                                    id_biggestWeight = att.getId_original();
                                }
                            }
                        }
                        id_up = id_biggestWeight;
                    }
                    nodesLeft.remove(Integer.valueOf(id_up));
                    nodesFinalRNOrder[id_up] = position_up;
                    sequenciaComunidade.put(id_up, position_up);
                    nodesAdd.add(id_up);
                    position_up--;
                }

                //preenchendo a parte de baixo dos nós do meio

                while(position_down < ( sequenciaDeEntrada.get(i).size() + positionTotal ))
                {

                    getNodeWithMoreConnections(id_down,nodesLeft);
                    if(!nodesWithConnections.isEmpty())
                        id_down = nodesWithConnections.get(0).getId();
                    else
                    {
                        //Get node with biggest weight inside the first community
                        for(InlineNodeAttribute att : listAttNodes)
                        {
                            if(nodesLeft.contains(att.getId_original()))
                            {
                                if(att.getDegree() > biggestWeight)
                                {
                                    biggestWeight = att.getDegree();
                                    id_biggestWeight = att.getId_original();
                                }
                            }
                        }
                        id_down = id_biggestWeight;
                    }
                    nodesLeft.remove(Integer.valueOf(id_down));
                    nodesFinalRNOrder[id_down] = position_down;
                    sequenciaComunidade.put(id_down, position_down);
                    nodesAdd.add(id_down);
                    position_down++;
                }
                //ADD BLANK SPACE IN THE TEMPORAL NETWORK
                if(blankSpaceCommunities)
                    position_down++;
                positionTotal = position_down;
                
                sequenciaComunidade = sortByComparator(sequenciaComunidade,true);
                Set<Integer> IdsNosDaComunidade =  sequenciaComunidade.keySet();
                List<InlineNodeAttribute> nosResultantesComunidade = new ArrayList();
                for(Integer nodeIdDaComunidade : IdsNosDaComunidade)
                {
                    for(InlineNodeAttribute noOriginal : sequenciaDeEntrada.get(i))
                    {
                        if(noOriginal.getId_original() == nodeIdDaComunidade)
                        {
                            nosResultantesComunidade.add(noOriginal);
                            break;
                        }
                    }
                }
                
                sequenciaResultante.put(i, nosResultantesComunidade);
                
            }

            
            
            //positionTotal--;
            //NODES WITHOUT COMUNITY
            for(int aa = 0; aa < nodesFinalRNOrder.length; aa++)
            {
                if(nodesFinalRNOrder[aa] == 0 && lineNodes.contains(aa) )
                {
                   nodesFinalRNOrder[aa] = positionTotal;
                   positionTotal++;
                }
            }

            //END Recurrent Neighbors

            //CRIAR HASHMAP do nodesFinalRNOrder
            
            
            // END STEP 3 - INTRA COMMUNITIES
        }
        
        return sequenciaResultante;
    }
  
    
    
    public static  Map<Integer, Integer> sortByComparator(Map<Integer, Integer> unsortMap, final boolean asc) //asc = true se ascendente
   {
      List<Entry<Integer, Integer>> list = new LinkedList<Entry<Integer, Integer>>(unsortMap.entrySet());
      // Sorting the list based on values
      Collections.sort(list, new Comparator<Entry<Integer, Integer>>()
      {
          public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2)
          {
                  int resultCompare;
                  if (asc)
                  {
                          resultCompare = o1.getValue().compareTo(o2.getValue());
                          if(resultCompare != 0)
                                  return resultCompare;
                          return Integer.compare(o1.getKey(), o2.getKey());
                  }
                  else
                  {
                          resultCompare = o2.getValue().compareTo(o1.getValue());
                          if(resultCompare != 0)
                                  return resultCompare;
                          return Integer.compare(o2.getKey(), o1.getKey());
                  }
          }
      });

      // Maintaining insertion order with the help of LinkedList
      Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
      for (Entry<Integer, Integer> entry : list)
          sortedMap.put(entry.getKey(), entry.getValue());

      return sortedMap;
  }
    
    
    public void executeCNOMultilevel(HashMap<Integer, List<InlineNodeAttribute>> comunidadesAtuais, String metodoDeteccao, String metodoOrdenacaoPasso2, String metodoOrdenacaoPasso3, int nivelRecursao)
    {
        List<InlineNodeAttribute> nosQueSeraoUsadosNesseNivel;
        ArrayList arestasQueSeraoUsadasNesseNivel;
        HashMap<Integer, List<InlineNodeAttribute>> comunidadesXNosPrimeiroNivel = new HashMap<>();
        HashMap<Integer, List<InlineNodeAttribute>> novasComunidades = new HashMap<>();
        HashMap<Integer, List<InlineNodeAttribute>> resultadoROC = new HashMap<>();
        boolean naoDaPraQuebrarMaisNenhumaComunidade = true;
         
        if(nivelRecursao == 0)
        {
            comunidadesROCMultilevel = new ArrayList<>();
            if(imprimirTxtComunidadesROCMultilevel)
            {
                File ROCMultilevelCommunities = new File(txtComunidadesROCMultilevel);
                if(ROCMultilevelCommunities.exists())
                    ROCMultilevelCommunities.delete();
            }
            
            //Nesse nível todos os nós e todas as arestas da rede são considerados.
            
            nosQueSeraoUsadosNesseNivel = listAttNodes;
            arestasQueSeraoUsadasNesseNivel = listAllEdges;
            
            //Executa o ROC para o primeiro nível. 
            System.out.println(">>>>>>>>>>>>> Nível 0 <<<<<<<<<<<<<<<<");
            comunidadesXNosPrimeiroNivel = executeCNO(nosQueSeraoUsadosNesseNivel, arestasQueSeraoUsadasNesseNivel, metodoDeteccao, metodoOrdenacaoPasso2, metodoOrdenacaoPasso3);
            comunidadesAtuais = comunidadesXNosPrimeiroNivel;
            comunidadesROCMultilevel.add(comunidadesXNosPrimeiroNivel);
            if(imprimirTxtComunidadesROCMultilevel)
                imprimeComunidadesArquivo(nivelRecursao, comunidadesXNosPrimeiroNivel); //a primeira detecção de comunidades resultará nas comunidades no nível 1. (O nivel zero é a sequencia inicial, sem comunidades)
        }
        
        if(nivelRecursao+1 >= qtosNiveisCNOUsuarioQuer && qtosNiveisCNOUsuarioQuer != -1)
            return;
        
        System.out.println(">>>>>>>>>>>>> Nível " + (++nivelRecursao) + " <<<<<<<<<<<<<<<<");
        for(Integer comunidade : comunidadesAtuais.keySet())
        {
            nosQueSeraoUsadosNesseNivel = comunidadesAtuais.get(comunidade);
            arestasQueSeraoUsadasNesseNivel = obtemArestasDentroDaComunidade(nosQueSeraoUsadosNesseNivel);
            
            resultadoROC = executeCNO(nosQueSeraoUsadosNesseNivel, arestasQueSeraoUsadasNesseNivel, metodoDeteccao, metodoOrdenacaoPasso2, metodoOrdenacaoPasso3);
            
            //Se a quebra da comunidade resultou em uma só, então não foi possível quebrá-la. Nesse caso, guarda essa comunidade.
            if(resultadoROC.size() == 1)// && resultadoROC.get(0).equals(nosQueSeraoUsadosNesseNivel))
            {
                novasComunidades.put(novasComunidades.size(), nosQueSeraoUsadosNesseNivel);
                //continue;
            }
            else
            {
                for(int i = 0; i < resultadoROC.size(); i++)
                    novasComunidades.put(novasComunidades.size(), resultadoROC.get(i)); //Incrementa o id da comunidade para colocar no hashmap sem substituir o que já tem lá

                naoDaPraQuebrarMaisNenhumaComunidade = false;
            }
        }
        
        if(!naoDaPraQuebrarMaisNenhumaComunidade)
        {
            if(imprimirTxtComunidadesROCMultilevel)
                imprimeComunidadesArquivo(nivelRecursao, novasComunidades);
            comunidadesROCMultilevel.add(novasComunidades);
            executeCNOMultilevel(novasComunidades, metodoDeteccao, metodoOrdenacaoPasso2, metodoOrdenacaoPasso3, nivelRecursao);
        }
        else
            System.out.println("Outliers =   " + obtemOutLiers(novasComunidades,listAttNodes));
        
    }
    
    private ArrayList obtemArestasDentroDaComunidade(List<InlineNodeAttribute> nosDaComunidade)
    {
        ArrayList arestasDentroDaComunidade = new ArrayList();
        //Cria array com os ids dos nós para facilitar a busca
        ArrayList idsNosDaComunidade = new ArrayList();
        for(int i = 0; i < nosDaComunidade.size(); i++)
            idsNosDaComunidade.add(nosDaComunidade.get(i).getId_original());
    
         for(Object ed : listAllEdges)
        {
            mxCell edge = (mxCell) ed;
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            
            if(idsNosDaComunidade.contains(att.getOrigin()) && idsNosDaComunidade.contains(att.getDestiny()))
                arestasDentroDaComunidade.add(ed);
        }
        return arestasDentroDaComunidade;
    }
    
    
    private void imprimeComunidadesArquivo(int nivel,  HashMap<Integer, List<InlineNodeAttribute>> comunidadesXNos)
    {
        String conteudoArquivo = "Comunidades no nível " + nivel + ":" + System.getProperty("line.separator");
        for(int j = 0; j < comunidadesXNos.size(); j++)
            conteudoArquivo += j + ": " +  comunidadesXNos.get(j).toString().replace(" ", "") + System.getProperty("line.separator");
        conteudoArquivo += System.getProperty("line.separator");
        FileHandler.gravaArquivo(conteudoArquivo, txtComunidadesROCMultilevel, true);
    }
    
    //Se metodoOrdenacaoPasso2 == null, então não é pra executar o passo 2.
    //Se metodoOrdenacaoPasso3 == null, então não é pra executar o passo 3.
    //O passo 1 é obrigatório.
    public HashMap<Integer, List<InlineNodeAttribute>> executeCNO(List<InlineNodeAttribute> nos, ArrayList arestas, String metodoDeteccao, String metodoOrdenacaoPasso2, String metodoOrdenacaoPasso3)
    {
        HashMap<Integer, List<InlineNodeAttribute>> sequenciaROC = new HashMap<>();
        
     //   if(nos.size() < tamanhoMinimoComunidadeROCMultilevel)
     //       return null;
        
        //Sleep para dar tempo de criar o arquivo com a saida do método de detecção
        for(long i = 0; i < 99999999; i++)
        {
            
        }
        
        //STEP 1: DETECTAR COMUNIDADES
        if(metodoDeteccao.equals("Original Louvain") || metodoDeteccao.equals("Multilevel Louvain") || metodoDeteccao.equals("SLM"))
        {
            sequenciaROC = new SLM_Louvain().execute(nos, arestas, metodoDeteccao);
            //new Statistic().calculaFMeasure(comunidades, "Louvain");
        }
        else if(metodoDeteccao.equals("Infomap"))
        {
            sequenciaROC = new InfoMap().execute(arestas, nos);
           // new MainForm().imprimeComunidadesArquivo(sequenciaROC, "Infomap");
            //new Statistic().calculaFMeasure(comunidades, "Louvain");
        }
        //Verifica se algum nó ficou sem comunidade
        String nosSemComunidades = obtemOutLiers(sequenciaROC,nos);
        System.out.println("Ouliers =   " + nosSemComunidades);
        if(nosSemComunidades != null && !nosSemComunidades.equals(("")))
        {
            nosSemComunidades += System.getProperty("line.separator");
            FileHandler.gravaArquivo(nosSemComunidades, txtOutliersROCMultilevel, true);
        }
      
        int [][] contactMatrix = FindContactMatrices(sequenciaROC);
        
        //STEP 2: ORDENACAO INTER-COMUNIDADE
        if(metodoOrdenacaoPasso2 != null && sequenciaROC.size() > 1) //Se só houver uma comunidade, não tem como executar esse passo.
           sequenciaROC = reordenaNos(sequenciaROC, metodoOrdenacaoPasso2, 2, contactMatrix);
        
        //STEP 3: ORDENACAO INTRA-COMUNIDADE
        if(metodoOrdenacaoPasso3 != null)
           sequenciaROC = reordenaNos(sequenciaROC, metodoOrdenacaoPasso3, 3, contactMatrix);
        
        //Se não executou os passos 2 e 3, retornar a sequencia original resultante da detecção.
        return sequenciaROC;
    }
    
    //Esse metodo vai chamar o RN ou o MSV
    private HashMap<Integer, List<InlineNodeAttribute>> reordenaNos(HashMap<Integer, List<InlineNodeAttribute>> sequenciaDeEntrada, String metodoOrdenacao, int passo, int [][] contactMatrix) //passo = 2 ou 3
    {
	HashMap<Integer, List<InlineNodeAttribute>> novaSequencia = new HashMap<Integer, List<InlineNodeAttribute>>();
        
        if(metodoOrdenacao.equals("Recurrent Neighbors"))
        {
            novaSequencia = recurrentNeighborsAlgorithm(sequenciaDeEntrada, contactMatrix, passo);
        }
        else if(metodoOrdenacao.equals("Optimized MSV"))
        {
            //Chama método que faz o MSV
        }
        else if(metodoOrdenacao.equals("Degree"))
        {
            novaSequencia = degreeAlgorithm(sequenciaDeEntrada, contactMatrix, passo);
        }
        return novaSequencia;
    }
    

    
    public void visualizaNos(HashMap<Integer, List<InlineNodeAttribute>> sequenciaDeEntrada)
    {
        //Recebe sequencia de nós de entrada (tanto faz como essa sequencia surgiu (passo 1, 2 ou 3) e plota ela no layout temporal).
        ArrayList<Integer> nosSequenciaVisualizacao = getPositionVisualizationById(sequenciaDeEntrada);
        
        int positionLineGraph = 0;
        //Pega a posicao do ultimo no da ordenação;
        positionLineGraph += nosSequenciaVisualizacao.size();
        //Soma a qtdd de comunidades detectada a posicao do ultimo nó
        if(blankSpaceCommunities)
            positionLineGraph += sequenciaDeEntrada.size();
        else
             positionLineGraph -= sequenciaDeEntrada.size();
        int i;
        //PARTE DO ALGORITMO DE VISUALIZACAO E PLOTAGEM DOS NÓS
        graph.getModel().beginUpdate();
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            if(att.isGraphLine())
            {
                //Move cells to origin point
                double x = cell.getGeometry().getX();
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                att.setY_atual(att.getY_original()+ positionLineGraph);
                
                graph.moveCells(root, x, att.getY_atual());
                
            }
            else if(att.isLineNode())
            {
                //Move cells to origin point
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                //if(RN)
                  //  att.setY_atual(nodesFinalRNOrder[att.getId_original()]);
                //else
                  //  att.setY_atual(hasPosition[att.getId_original()]);
                
                att.setY_atual(nosSequenciaVisualizacao.indexOf(att.getId_original()));
                
                if(att.isLeft())
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                else
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                
                currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
            }
            else if(att.isNode())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());

                //if(RN)
                  //  att.setY_atual(nodesFinalRNOrder[att.getId_original()]);
               // else
                   // att.setY_atual(hasPosition[att.getId_original()]);
                
                att.setY_atual(nosSequenciaVisualizacao.indexOf(att.getId_original()));
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5 , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
            }
            else if(att.isEdge())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
        
                
                int y_inicial ;
                
                i = nosSequenciaVisualizacao.indexOf(att.getOrigin()) +1;
                int iD = nosSequenciaVisualizacao.indexOf(att.getDestiny()) +1;
                
                /*if(RN)
                    i+=nodesFinalRNOrder[att.getOrigin()];
                else
                    i+=hasPosition[att.getOrigin()];
                
                if(RN)
                    iD+=nodesFinalRNOrder[att.getDestiny()];
                else
                    iD+=hasPosition[att.getDestiny()];
                */
                
                String styleShapeInline = cell.getStyle();
                
                if(i < iD)
                {
                    y_inicial = i;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_SOUTH, mxConstants.DIRECTION_NORTH);
                    att.setIsNorth(true);
                }
                else
                {
                    y_inicial = iD;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_NORTH, mxConstants.DIRECTION_SOUTH);
                    
                    att.setIsNorth(false);
                }
                
                cell.setStyle(styleShapeInline);
                
                //i/2 because listAttNodes has duplicate values
                att.setY_atual(y_inicial);
                
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                att.setHeightEdge_atual(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20)));
                g.setHeight(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20))); 
                cell.setGeometry(g);
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX  - moveEdgeSizeX, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
            }
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }
    
    private ArrayList<Integer> getPositionVisualizationById(HashMap<Integer, List<InlineNodeAttribute>> sequenciaDeEntrada) 
    {
        ArrayList<Integer> nosSequenciaVisualizacao = new ArrayList<>();
        nosSequenciaVisualizacao.add(-1);
        for(int i = 0; i < sequenciaDeEntrada.size(); i++)
        {
            for(int j=0; j < sequenciaDeEntrada.get(i).size(); j++)
            {
                nosSequenciaVisualizacao.add(sequenciaDeEntrada.get(i).get(j).getId_original());
            }
            //ADD BLANK SPACE IN THE TEMPORAL NETWORK
            if(blankSpaceCommunities)
                nosSequenciaVisualizacao.add(-1);
        }
        return nosSequenciaVisualizacao;
    }
    
    public int[][] FindContactMatrices(HashMap<Integer, List<InlineNodeAttribute>> comunidades)
    {
        int[][] matrizComunityRelation = new int[comunidades.size()][comunidades.size()];

        for(int i = 0; i < matrizComunityRelation.length; i++)
        {
            for(int j = 0; j < matrizComunityRelation.length; j++)
            {
                matrizComunityRelation[i][j] = 0;
            }
        }

        for(Object ed : listAllEdges)
        {
            mxCell edge = (mxCell) ed;
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();

            boolean findOrigin = false, findDestiny = false;
            boolean finished = false;

            int comunityOrigin = 0, comunityDestiny = 0;

            for(int i = 0; i < comunidades.size() && !finished; i++){
                for(int j = 0; j < comunidades.get(i).size(); j++){
                    if(findOrigin && findDestiny)
                    {
                        finished = true;
                        break;
                    }
                    if(att.getOrigin() == comunidades.get(i).get(j).getId_original())
                    {
                        findOrigin = true;
                        comunityOrigin = i;
                    }
                    if(att.getDestiny() == comunidades.get(i).get(j).getId_original())
                    {
                        findDestiny = true;
                        comunityDestiny = i;
                    }
                }
            }

            matrizComunityRelation[comunityOrigin][comunityDestiny] += 1;
            matrizComunityRelation[comunityDestiny][comunityOrigin] += 1;
        }
        
        return matrizComunityRelation;
    }
    
    public HashMap<Integer, List<InlineNodeAttribute>> comunidades;
    
    @Deprecated
    public void orderLouvainOrSLM(String metodo, boolean ordered, boolean RN){

        //CALCULO DO ALGORITMO DE COMUNIDADE
        
        comunidades = new SLM_Louvain().execute(listAttNodes, listAllEdges, metodo);
            
        System.out.println("Ouliers =   " + obtemOutLiers(comunidades,listAttNodes));
        //new Statistic().calculaFMeasure(comunidades, "Louvain");
        
        //System.out.println("Modularity Measure =   " + modularity());
         //FIM CALCULO ALGORITMO DE COMUNIDADE
           
        Integer[] hasPosition = new Integer[maxId];

        int countPosition = 1;
        
        
        int i,j;
        
        int[] comunityPosition = new int[comunidades.size()];
        
        if(ordered)
        {

            int[][] matrizComunityRelation = new int[comunidades.size()][comunidades.size()];

            for(i = 0; i < matrizComunityRelation.length; i++)
            {
                for(j = 0; j < matrizComunityRelation.length; j++)
                {
                    matrizComunityRelation[i][j] = 0;
                }
            }

            for(Object ed : listAllEdges)
            {
                mxCell edge = (mxCell) ed;
                InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();

                boolean findOrigin = false, findDestiny = false;
                boolean finished = false;

                int comunityOrigin = 0, comunityDestiny = 0;

                for(i = 0; i < comunidades.size() && !finished; i++){
                    for(j = 0; j < comunidades.get(i).size(); j++){
                        if(findOrigin && findDestiny)
                        {
                            finished = true;
                            break;
                        }
                        if(att.getOrigin() == comunidades.get(i).get(j).getId_original())
                        {
                            findOrigin = true;
                            comunityOrigin = i;
                        }
                        if(att.getDestiny() == comunidades.get(i).get(j).getId_original())
                        {
                            findDestiny = true;
                            comunityDestiny = i;
                        }
                    }
                }

                if(comunityOrigin == comunityDestiny)
                    matrizComunityRelation[comunityOrigin][comunityDestiny] += 1;
                else
                {
                    matrizComunityRelation[comunityOrigin][comunityDestiny] += 1;
                    matrizComunityRelation[comunityDestiny][comunityOrigin] += 1;
                }
            }

            

            for(i = 0; i < comunityPosition.length; i++)
            {
                if(i == 0)
                    comunityPosition[i] = 1;
                else
                    comunityPosition[i] = 0;
            }

            
            ArrayList<Integer> usedCommunities = new ArrayList();
            usedCommunities.add(0);
            
            int mostConnectedComunity = 0;
            int idCommunity = 1;
            for(i = 0; i < matrizComunityRelation.length; i++)
            {
                boolean communityDetected = false;
                int biggestConnection = 0, cc = 0;
                for(j = 0; j < matrizComunityRelation.length; j++)
                {
                    if(matrizComunityRelation[mostConnectedComunity][j] > biggestConnection && mostConnectedComunity != j && !usedCommunities.contains(j))
                    {
                        
                        biggestConnection = matrizComunityRelation[i][j];
                        cc = j;
                        
                        communityDetected = true;

                    }
                }
                if(communityDetected)
                {
                    mostConnectedComunity = cc;
                    usedCommunities.add(mostConnectedComunity);
                    idCommunity++;
                    comunityPosition[mostConnectedComunity] = idCommunity;
                }
                else if(!usedCommunities.contains(i))
                {
                    idCommunity++;
                    comunityPosition[i] = idCommunity;
                }
            }
            
            
            
            //Printing the contact matrices
            System.out.println("Contact matrices:");
            
            for(i = 0; i < matrizComunityRelation.length; i++)
            {
                
                for(j = 0; j < matrizComunityRelation.length; j++)
                {
                    System.out.print(matrizComunityRelation[i][j]+" ");
                }
                System.out.println();
            }
            //END Printing
        
            
        }
        
         
        int[] nodesFinalRNOrder = new int[maxId];
        
        if(RN)
        {
            nodesFinalRNOrder = recurrentNeighborsAlgorithm(comunidades, comunityPosition);
        }
        
        for(i = 1; i <= comunidades.size(); i++){
            boolean addNewNode = false;
            int orderComunity = 0;
            
            if(ordered)
            {
                for(int aa = 0; aa < comunityPosition.length; aa++)
                {
                    if(comunityPosition[aa] == i)
                    {
                        orderComunity = aa;
                        break;
                    }
                }
            }
            else
                orderComunity = i-1;
            
            for(j = 0; j < comunidades.get(orderComunity).size(); j++){
                int position = countPosition;
                
                if(hasPosition[comunidades.get(orderComunity).get(j).getId_original()] == null)
                {
                    hasPosition[comunidades.get(orderComunity).get(j).getId_original()] = position;
                    countPosition++;
                    addNewNode = true;
                }
            }
            if(addNewNode)
                countPosition++;
            
        }
        
        for(i = 1; i < hasPosition.length;i++)
        {
            if(hasPosition[i] == null && lineNodes.contains(i))
            {
                hasPosition[i] = countPosition;
                countPosition++;
            }
        }
        
        //PARTE DO ALGORITMO DE VISUALIZACAO E PLOTAGEM DOS NÓS
        graph.getModel().beginUpdate();
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            if(att.isGraphLine())
            {
                //Move cells to origin point
                double x = cell.getGeometry().getX();
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                att.setY_atual(att.getY_original()+150);
                
                graph.moveCells(root, x, att.getY_atual());
                
            }
            else if(att.isLineNode())
            {
                //Move cells to origin point
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                if(RN)
                    att.setY_atual(nodesFinalRNOrder[att.getId_original()]);
                else
                    att.setY_atual(hasPosition[att.getId_original()]);
                
                if(att.isLeft())
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                else
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                
                currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
            }
            else if(att.isNode())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());

                if(RN)
                    att.setY_atual(nodesFinalRNOrder[att.getId_original()]);
                else
                    att.setY_atual(hasPosition[att.getId_original()]);
                
                
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5 , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
            }
            else if(att.isEdge())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
        
                
                int y_inicial ;
                
                i=1;
                if(RN)
                    i+=nodesFinalRNOrder[att.getOrigin()];
                else
                    i+=hasPosition[att.getOrigin()];
                int iD=1;
                if(RN)
                    iD+=nodesFinalRNOrder[att.getDestiny()];
                else
                    iD+=hasPosition[att.getDestiny()];
                String styleShapeInline = cell.getStyle();
                
                if(i < iD)
                {
                    y_inicial = i;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_SOUTH, mxConstants.DIRECTION_NORTH);
                    att.setIsNorth(true);
                }
                else
                {
                    y_inicial = iD;
                    styleShapeInline = styleShapeInline.replace(mxConstants.DIRECTION_NORTH, mxConstants.DIRECTION_SOUTH);
                    
                    att.setIsNorth(false);
                }
                
                cell.setStyle(styleShapeInline);
                
                //i/2 because listAttNodes has duplicate values
                att.setY_atual(y_inicial);
                
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                att.setHeightEdge_atual(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20)));
                g.setHeight(Math.abs((zoom* (iD+deslocamento) + 20) - (zoom* (i+deslocamento) + 20))); 
                cell.setGeometry(g);
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX  - moveEdgeSizeX, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
            }
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }
    
    
    public void orderBirth(){

        graph.getModel().beginUpdate();
        
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
            if(att.isLineNode())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                att.setX_atual(att.getX_original());
                att.setY_atual(att.getY_original());
                if(att.isLeft())
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                else
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+5+deslocamento)+(shiftX/3), zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35 );
                
                currentTemporalNodeOrder.put(att.getId_original(),att.getY_atual());
            }
            else if(att.isNode())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                att.setX_atual(att.getX_original());
                att.setY_atual(att.getY_original());
                
                
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX -2.5 , zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 35);
            }
            else if(att.isEdge())
            {
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                att.setX_atual(att.getX_original());
                att.setY_atual(att.getY_original()+2);

                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                att.setHeightEdge_atual(att.getOriginalHeightEdge());
                g.setHeight(att.getOriginalHeightEdge()); 
                cell.setGeometry(g);
                
                //Inverter a cor se preciso

                int i=4;
                for(InlineNodeAttribute attNode : listAttNodes)
                {
                    if(attNode.getId_original() == att.getOrigin())
                       break;
                    i++;
                }
                
                int iD=4;
                for(InlineNodeAttribute attNode : listAttNodes)
                {
                    if(attNode.getId_original() == att.getDestiny())
                       break;
                    iD++;
                }
                
                String styleGradientEdge = cell.getStyle();

                if(i > iD)
                {
                    styleGradientEdge = styleGradientEdge.replace(mxConstants.DIRECTION_SOUTH, mxConstants.DIRECTION_NORTH);
                    att.setIsNorth(true);
                }
                else
                {
                    styleGradientEdge = styleGradientEdge.replace(mxConstants.DIRECTION_NORTH, mxConstants.DIRECTION_SOUTH);
                    
                    att.setIsNorth(false);
                }
                
                graph.setCellStyle(styleGradientEdge, root);
                
                graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX - moveEdgeSizeX, zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
                
            }
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
    }
     
    
    int spacing, zoom;
   
    public void NetLayoutInlineNew(ArrayList<ArrayList> matrizData, int spacing, int zoom, boolean veioDoStream) {
        
        setColor("Original");
        colorInline = "Original";
        colorEdgeInline = "Original";
        formNode = "Circular";
        sizeNode = "Small";
        typeColorInline = "Gray Scale";
        setTypeColorEdge("Gray Scale");
        spaceBetweenLines = 1;
        templateMat = "TAM White";
        
        showNodes = true;
        showEdges = true;
        
        BufferedReader file;
        String line;
        String[] tokens;
        
        //try{
            
            //file = new BufferedReader(new FileReader(new File(filename)));

            int id = 0;
            matrizDataInline = new ArrayList<>();
            ArrayList<Integer> coluna = new ArrayList<>();
            deslocamento = 1;  
            shiftX = 50;
            /*
            while((line = file.readLine()) != null) {
                tokens = line.split(" ");
                coluna.add(Integer.parseInt(tokens[0]));
                coluna.add(Integer.parseInt(tokens[1]));
                coluna.add(Integer.parseInt(tokens[2]));
                matrizDataInline.add(id, coluna);
                coluna = new ArrayList<>();
                id++;
            }*/
            
            matrizDataInline = matrizData;
            
            changePositionNodes(spacing,zoom, veioDoStream);
            
            
            String styleNode = mxConstants.STYLE_MOVABLE+"=0;";
            styleNode += mxConstants.STYLE_EDITABLE+"=0;";
            styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
            styleNode += mxConstants.STYLE_NOLABEL+"=0;";
            styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
            styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
           
            String styleNodeLeft = mxConstants.STYLE_MOVABLE+"=0;";
            styleNodeLeft += mxConstants.STYLE_EDITABLE+"=0;";
            styleNodeLeft += mxConstants.STYLE_RESIZABLE+"=0;";
            styleNodeLeft += mxConstants.STYLE_NOLABEL+"=0;";
            styleNodeLeft += mxConstants.STYLE_FONTCOLOR+"="+Color.BLACK+";";
            styleNodeLeft += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_LEFT+";";
            styleNodeLeft += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
            
            String styleNodeRight = mxConstants.STYLE_MOVABLE+"=0;";
            styleNodeRight += mxConstants.STYLE_EDITABLE+"=0;";
            styleNodeRight += mxConstants.STYLE_RESIZABLE+"=0;";
            styleNodeRight += mxConstants.STYLE_NOLABEL+"=0;";
            styleNodeRight += mxConstants.STYLE_FONTCOLOR+"="+Color.BLACK+";";
            styleNodeRight += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_RIGHT+";";
            styleNodeRight += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
           
            mxGraph graphScalar = new mxGraph();
            graphScalar.insertVertex(null, "color 0", "", 3 , 3, 30 , 20, styleNodeLeft);
            graphScalar.insertVertex(null, "color 0.1", "", 33 , 3, 30 , 20, styleNode);
            graphScalar.insertVertex(null, "color 0.2", "", 63 , 3, 30 , 20, styleNode);
            graphScalar.insertVertex(null, "color 0.3", "", 93 , 3, 30 , 20, styleNode);
            graphScalar.insertVertex(null, "color 0.4", "", 123 , 3, 30 , 20, styleNode);
            graphScalar.insertVertex(null, "color 0.5", "", 153 , 3, 30 , 20, styleNode);
            graphScalar.insertVertex(null, "color 0.6", "", 183 , 3, 30 , 20, styleNode);
            graphScalar.insertVertex(null, "color 0.7", "", 213 , 3, 30 , 20, styleNode);
            graphScalar.insertVertex(null, "color 0.8", "", 243 , 3, 30 , 20, styleNode);
            graphScalar.insertVertex(null, "color 0.9", "", 273 , 3, 30 , 20, styleNode);
            graphScalar.insertVertex(null, "color 1", "", 303 , 3, 30 , 20, styleNodeRight);
            
            graphComponentScalarInline = new mxGraphComponent(graphScalar);
            
            mxGraph graphScalar2 = new mxGraph();
            graphScalar2.insertVertex(null, "color 0", "", 3 , 3, 30 , 20, styleNodeLeft);
            graphScalar2.insertVertex(null, "color 0.1", "", 33 , 3, 30 , 20, styleNode);
            graphScalar2.insertVertex(null, "color 0.2", "", 63 , 3, 30 , 20, styleNode);
            graphScalar2.insertVertex(null, "color 0.3", "", 93 , 3, 30 , 20, styleNode);
            graphScalar2.insertVertex(null, "color 0.4", "", 123 , 3, 30 , 20, styleNode);
            graphScalar2.insertVertex(null, "color 0.5", "", 153 , 3, 30 , 20, styleNode);
            graphScalar2.insertVertex(null, "color 0.6", "", 183 , 3, 30 , 20, styleNode);
            graphScalar2.insertVertex(null, "color 0.7", "", 213 , 3, 30 , 20, styleNode);
            graphScalar2.insertVertex(null, "color 0.8", "", 243 , 3, 30 , 20, styleNode);
            graphScalar2.insertVertex(null, "color 0.9", "", 273 , 3, 30 , 20, styleNode);
            graphScalar2.insertVertex(null, "color 1", "", 303 , 3, 30 , 20, styleNodeRight);
            
            graphComponentScalarEdgeInline = new mxGraphComponent(graphScalar2);
            
            
            
           /* 
        }catch (java.io.IOException e) {
            Logger.getLogger(NetLayout.class.getName()).log(Level.SEVERE, null, e);
        }*/
    }
    
    
    private int lastTime;
    
    public void NetLayoutLine(){

        String styleNode = mxConstants.STYLE_MOVABLE+"=0;";
        styleNode += mxConstants.STYLE_EDITABLE+"=0;";
        styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
        styleNode += mxConstants.STYLE_NOLABEL+"=0;";
        styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
        styleNode += mxConstants.STYLE_FILLCOLOR+"=#000000;";
        styleNode += mxConstants.STYLE_STROKECOLOR+"=#000000;";

        //mxGraph graphScalar = new mxGraph();

        int weightBottomLine = 0, weightTopLine = 20;
        
        int shiftYAllInline = lineNodes.size()+3;
        
        
        mxCell v1,v2;
        int tempo = matrizGraphLine.length-1;
        //int tempo = Integer.parseInt(matrizDataInline.get(matrizDataInline.size() -1).get(2).toString());
        int lastTime = 0;
        
        graph.getModel().beginUpdate();  
        
        int x = spacing*zoom* (0+deslocamento)+15;
        int y = zoom* (weightTopLine+1+deslocamento+shiftYAllInline);
        
        InlineNodeAttribute att = new InlineNodeAttribute(tempo,0,x,y,"",false,false,false);
        att.setIsGraphLine(true);
        //Add Horizontal Line
        Object barraH1 = graph.insertVertex(null, "barra horizontal 1", att,x,y, 1 , 1, styleShapeInvisibleNodes.replace(mxConstants.STYLE_OPACITY+"=100;", mxConstants.STYLE_OPACITY+"=0;")+style.mxStyle);
        
        x = spacing*zoom* (tempo+deslocamento)+shiftX+30;
        y = zoom* (weightTopLine+1+deslocamento+shiftYAllInline);
        
        att = new InlineNodeAttribute(tempo,0,x,y,"",false,false,false);
        att.setIsGraphLine(true);
        Object barraH2 = graph.insertVertex(null, "barra horizontal 2", att,x,y, 1 , 1, styleShapeInvisibleNodes.replace(mxConstants.STYLE_OPACITY+"=100;", mxConstants.STYLE_OPACITY+"=0;")+style.mxStyle);
        
        graph.insertEdge(null, null,"edge horizontal 3", barraH1, barraH2, styleShapeEdgeLines2);
        
        //Add Vertical Line
        x = spacing*zoom* (0+deslocamento)+15;
        y = zoom* (weightTopLine+1+deslocamento+shiftYAllInline);
        
        att = new InlineNodeAttribute(tempo,0,x,y,"",false,false,false);
        att.setIsGraphLine(true);
        Object barraV1 = graph.insertVertex(null, "barra vertical 1", att,x,y, 1 , 1, styleShapeInvisibleNodes.replace(mxConstants.STYLE_OPACITY+"=100;", mxConstants.STYLE_OPACITY+"=0;")+style.mxStyle);
        
        x = spacing*zoom* (0+deslocamento)+15;
        y =  zoom* (weightBottomLine+deslocamento+shiftYAllInline);
        
        att = new InlineNodeAttribute(tempo,0,x,y,"",false,false,false);
        att.setIsGraphLine(true);
        
        Object barraV2 = graph.insertVertex(null, "barra vertical 2", att,x,y, 1 , 1, styleShapeInvisibleNodes.replace(mxConstants.STYLE_OPACITY+"=100;", mxConstants.STYLE_OPACITY+"=0;")+style.mxStyle);
        graph.insertEdge(null, null,"edge vertical 3", barraV1, barraV2,styleShapeEdgeLines2);

        mxCell cellAnt=null, cellAtual=null;
        
        int maiorValorTempo = matrizGraphLine[0], menorValorTempo = matrizGraphLine[0];
        for(int j=0; j< matrizGraphLine.length;j++)
        {
            if(maiorValorTempo < matrizGraphLine[j])
                maiorValorTempo = matrizGraphLine[j];
            if(menorValorTempo > matrizGraphLine[j])
                menorValorTempo = matrizGraphLine[j];
        }
        
        
        x = spacing*zoom* (0+deslocamento)+10;
        y =  zoom* (weightTopLine+deslocamento+shiftYAllInline);

        att = new InlineNodeAttribute(menorValorTempo,menorValorTempo,x,y,""+menorValorTempo,false,false,false);
        att.setIsGraphLine(true);
        mxCell cell = (mxCell) graph.insertVertex(null, null, att,x,y, 1,1, styleShapeInvisibleNodes.replace(mxConstants.STYLE_OPACITY+"=100;", mxConstants.STYLE_OPACITY+"=0;")+style.mxStyle);

        
        x = spacing*zoom* (0+deslocamento)+10;
        y = zoom* (weightBottomLine+1+deslocamento+shiftYAllInline);
        
        att = new InlineNodeAttribute(maiorValorTempo/2,maiorValorTempo/2,x,y,""+maiorValorTempo/2,false,false,false);
        att.setIsGraphLine(true);
        cell = (mxCell) graph.insertVertex(null, null, att,x,y, 1,1, styleShapeInvisibleNodes.replace(mxConstants.STYLE_OPACITY+"=100;", mxConstants.STYLE_OPACITY+"=0;")+style.mxStyle);

        
        //Insert Horizontal Times
        for(int j=0;j<=tempo;j++)
        {
            
            int valorConexaoNoPorTempo = matrizGraphLine[j];
            
            float insize = (valorConexaoNoPorTempo * (weightTopLine - weightBottomLine) / ((maiorValorTempo - menorValorTempo)+1)) + weightBottomLine;

            float f = Integer.MAX_VALUE;
            int ia = (int) f;

            if(insize == ia)
                insize = 0;

            insize = Math.abs(insize - weightTopLine);
            
            if(insize == 20 && valorConexaoNoPorTempo != 0)
            {
                insize -= 0.5;
            }
            insize = (float) (insize + 0.8);
            
            x = spacing*zoom* (j+deslocamento)+shiftX;
            y = (int) (zoom* (insize+deslocamento+shiftYAllInline));
            
            att = new InlineNodeAttribute(j,0,x,y,"",false,false,false);
            att.setIsGraphLine(true);
            att.setIsGraphLineNodes(true);
            
            //Random generator = new Random();
            //int i = generator.nextInt(9);

            if(j == 0)
            {
                cellAnt = (mxCell) graph.insertVertex(null, null, att,x,y, 3,3,mxConstants.STYLE_FONTSIZE+"=15;"+mxConstants.STYLE_HORIZONTAL+"=0;" + mxConstants.STYLE_NOLABEL+"=0;" + mxConstants.STYLE_ALIGN+"="+mxConstants.ALIGN_LEFT+";"+mxConstants.STYLE_FILLCOLOR+"=#000000;"+mxConstants.STYLE_STROKECOLOR+"=#000000;");
            }

            x =  spacing*zoom* (j+deslocamento)+shiftX;
            y = (int) ( zoom* (insize+deslocamento+shiftYAllInline));
            
            att = new InlineNodeAttribute(j,0,x,y,"",false,false,false);
            att.setIsGraphLine(true);
            att.setIsGraphLineNodes(true);
            cellAtual = (mxCell) graph.insertVertex(null, null, att,x,y,3,3, mxConstants.STYLE_FONTSIZE+"=15;"+mxConstants.STYLE_HORIZONTAL+"=0;" + mxConstants.STYLE_NOLABEL+"=0;" + mxConstants.STYLE_ALIGN+"="+mxConstants.ALIGN_LEFT+";"+ mxConstants.STYLE_FILLCOLOR+"=#000000;"+mxConstants.STYLE_STROKECOLOR+"=#000000;");
            
            att = new InlineNodeAttribute(j,0,x,y,"",false,false,false);
            att.setIsGraphLine(true);
            graph.insertEdge(null, null,"edge hor 11", cellAnt, cellAtual,styleShapeEdgeLines+";"+mxConstants.STYLE_ENDARROW+"=0;");
            cellAnt = cellAtual;


            x = spacing*zoom* (j+deslocamento)+shiftX+5;
            y =  zoom* (weightTopLine+2+deslocamento+shiftYAllInline);
            
            att = new InlineNodeAttribute(j,j,x,y,""+j,false,false,false);
            att.setIsGraphLine(true);
            cell = (mxCell) graph.insertVertex(null, null, att,x,y,1,1, styleShapeInvisibleNodes.replace(mxConstants.STYLE_OPACITY+"=100;", mxConstants.STYLE_OPACITY+"=0;")+style.mxStyle);

            //att = new InlineNodeAttribute(j,j,j,1,"node "+j,false,false,true);

        }
        graph.getModel().endUpdate();
        //graphComponentLine = new mxGraphComponent(graphScalar);

    }
    
    
    
    JGraphStyle style;
    
    public void defineStyles()
    {
        
        style = JGraphStyle.ROUNDED;
        
        styleShape = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
        if(zoom < 4){
            styleShape += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_LINE+";";
        }
        styleShape += mxConstants.STYLE_OPACITY+"=100;";

        styleEdge = graph.getStylesheet().getDefaultEdgeStyle();
        styleEdge.put(mxConstants.STYLE_MOVABLE, "0");
        styleEdge.put(mxConstants.STYLE_NOLABEL,"1");
        //styleEdge.put(mxConstants.STYLE_ENDARROW,"0");
        styleEdge.put(mxConstants.STYLE_OPACITY,"100");
        //styleEdge.put(mxConstants.STYLE_ROUNDED,"true");
        //styleEdge.put(mxConstants.STYLE_EDGE,mxConstants.EDGESTYLE_ENTITY_RELATION);
        
        //styleEdge.put(mxConstants.STYLE_ROUNDED,true);
        //styleEdge.put(mxConstants.EDGESTYLE_ORTHOGONAL,"true");
        
        styleNode = graph.getStylesheet().getDefaultVertexStyle();
        styleNode.put(mxConstants.STYLE_MOVABLE,"0");
        styleNode.put(mxConstants.STYLE_NOLABEL,"1");
        styleNode.put(mxConstants.STYLE_RESIZABLE,"0");
        //styleNode.put(mxConstants.STYLE_OPACITY,"100");
        //styleNode.put(mxConstants.STYLE_FILLCOLOR,"#7F7F7F");
        styleNode.put(mxConstants.STYLE_OPACITY,"100");
        
        styleShapeEdge = mxConstants.STYLE_STROKEWIDTH+"=5;";
        //styleShapeEdge += mxConstants.STYLE_FILLCOLOR+"=#7F7F7F;";
        styleShapeEdge += mxConstants.STYLE_OPACITY+"=25;";
        //styleShapeEdge += mxConstants.EDGESTYLE_ORTHOGONAL+"=1;";
        //styleShapeEdge += mxConstants.STYLE_ROUNDED+"=1;";
        //styleShapeEdge = mxConstants.STYLE_EDGE+"="+mxConstants.EDGESTYLE_ENTITY_RELATION;
        //styleShapeEdge += mxConstants.STYLE_ROUNDED+"=true";
       // styleShapeEdge += mxConstants.STYLE_STROKECOLOR+"=red";
        
        styleShapeEdgeLines = mxConstants.STYLE_STROKECOLOR+"=black;";
        styleShapeEdgeLines += mxConstants.STYLE_MOVABLE+"=1;";
        styleShapeEdgeLines += mxConstants.STYLE_OPACITY+"=100;";
        
        styleShapeEdgeLines2 = mxConstants.STYLE_STROKECOLOR+"=black;";
        styleShapeEdgeLines2 += mxConstants.STYLE_MOVABLE+"=0;";
        styleShapeEdgeLines2 += mxConstants.STYLE_OPACITY+"=100;";
        //styleShapeEdgeLines2 += mxConstants.STYLE_ENDARROW+"=1;";
        
        
        styleShapeLines = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_LINE+";";
        styleShapeLines += mxConstants.STYLE_STROKECOLOR+"=black;";
        styleShapeLines += mxConstants.STYLE_OPACITY+"=100;";
        
        styleShapeInvisibleNodes = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_LINE+";";
        styleShapeInvisibleNodes += mxConstants.STYLE_STROKECOLOR+"=white;";
        styleShapeInvisibleNodes += mxConstants.STYLE_HORIZONTAL+"=0;";
        styleShapeInvisibleNodes += mxConstants.STYLE_NOLABEL+"=0;";
        styleShapeInvisibleNodes += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
        styleShapeInvisibleNodes += mxConstants.STYLE_OPACITY+"=100;";
        
        styleShapeInvisibleNodesandLabel = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_LINE+";";
        styleShapeInvisibleNodesandLabel += mxConstants.STYLE_STROKECOLOR+"=blue;";
        styleShapeInvisibleNodesandLabel += mxConstants.STYLE_HORIZONTAL+"=0;";
        styleShapeInvisibleNodesandLabel += mxConstants.STYLE_NOLABEL+"=1;";
        styleShapeInvisibleNodesandLabel += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
        styleShapeInvisibleNodesandLabel += mxConstants.STYLE_OPACITY+"=100;";
        
        styleShapeNumbers = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
        styleShapeNumbers += mxConstants.STYLE_STROKECOLOR+"=red;";
        styleShapeNumbers += mxConstants.STYLE_ROTATION+"=90;";
        styleShapeNumbers += mxConstants.STYLE_NOLABEL+"=0;";
        styleShapeNumbers += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_LEFT+";";
        styleShapeNumbers += mxConstants.STYLE_OPACITY+"=100;";
        
        styleShapeEdgeInvisible = mxConstants.STYLE_STROKEWIDTH+"=5;";
        styleShapeEdgeInvisible += mxConstants.EDGESTYLE_ORTHOGONAL+"=1;";
        styleShapeEdgeInvisible += mxConstants.STYLE_ROUNDED+"=1;";
        styleShapeEdgeInvisible += mxConstants.STYLE_STROKECOLOR+"=white;";
        styleShapeEdgeInvisible += mxConstants.STYLE_OPACITY+"=100;";
            
        
    }
    
    int matrizGraphLine[];
    
    public ArrayList<Object> listAllEdges;
                
    int sizeEdgeTemporalInline = 8;
    
    public void changePositionNodes(int spacing, int zoom, boolean veioDoStream)
    {
        
        this.spacing = spacing;
        this.zoom = zoom;
        
        listAllEdges = new ArrayList();
            
        //Make the label used in InlineNodeAttribute work in mxCellSate
        //graph = new mxGraphExtended();
        
        
         //mxGraphics2DCanvas.putShape(CurvedShape.KEY, new CurvedShape());
         //mxStyleRegistry.putValue(CurvedEdgeStyle.KEY, new CurvedEdgeStyle());
         
         graph = new mxGraph() {
             @Override
             protected mxGraphView createGraphView() {
                 return new CurveGraphView(this);
             }
         };
         
        final Object parent = graph.getDefaultParent();
        
        /*
        graph = new mxGraph(){
            @Override
            public void drawState(mxICanvas canvas, mxCellState state, boolean drawLabel)
            {
               Object userValue = graph.getModel().getValue(state.getCell());
               if(userValue != null && !userValue.equals(""))
               {
                   InlineNodeAttribute  v = (InlineNodeAttribute) userValue;
                   String textToDisplay = v.getLabel();

                   if (!textToDisplay.equals(""))
                   {
                       state.setLabel(textToDisplay);
                   }   
               }
               super.drawState(canvas,state,drawLabel);
            }            
        };*/
        
        
        defineStyles();
        
        Collections.sort(matrizDataInline, new Comparator<ArrayList>()
      {
          public int compare(ArrayList o1, ArrayList o2)
          {
              
                  if((int)o1.get(2) < (int)o2.get(2))
                      return -1;
                  return 1;
                 
          }
      });
        
        
        graph.getModel().beginUpdate();  
        
        try
        {
            mxCell v1,v2;
            int tempo = 0;
      /*      int lastTime = 0;
            
            for(ArrayList<Integer> column : matrizDataInline){
                if(column.get(2) > lastTime)
                    lastTime = column.get(2);
            }
            
        */    
            
            
            //Pega a ultima linha da matriz para verificar o tempo máximo
            ArrayList<Integer> columnM = matrizDataInline.get(matrizDataInline.size()-1);
            lastTime = columnM.get(2);
            
            matrizGraphLine = new int[lastTime+1];
            
            //Inicializa matrizgraphLine
            for(int i=0;i<=lastTime;i++)
            {
                matrizGraphLine[i] = 0;
            }
            for(int i = 0; i < matrizDataInline.size(); i++){
            //for(ArrayList<Integer> column : matrizDataInline){
                ArrayList<Integer> column = matrizDataInline.get(i);
                String[] tokens =  new String[3];
                //lastTime = tempo;

                tokens[0] = column.get(0).toString();
                tokens[1] = column.get(1).toString();
                tokens[2] = column.get(2).toString();

                int idSource = Integer.parseInt(tokens[0]);
                int idTarget = Integer.parseInt(tokens[1]);
                tempo = Integer.parseInt(tokens[2]);
                
                if(!lineNodes.contains(idSource)){
                    lineNodes.add(idSource);
                }
                if(!lineNodes.contains(idTarget)){
                    lineNodes.add(idTarget);
                }
                int indexSource = lineNodes.indexOf(idSource);
                int indexTarget = lineNodes.indexOf(idTarget);
                
                //Edge
                String styleShapeInline = styleShapeEdge+mxConstants.STYLE_ENDARROW+"=0;strokeColor=none;fillColor=#C3D9FF;"+mxConstants.STYLE_GRADIENTCOLOR+"=#C3D9FF;";
                

                boolean isNorth = false;
                
                int y_inicial ;
                if(indexSource < indexTarget)
                {
                    y_inicial = indexSource;
                    styleShapeInline += mxConstants.STYLE_GRADIENT_DIRECTION+"="+mxConstants.DIRECTION_NORTH+";";
                    isNorth = true;
                }
                else
                {
                    y_inicial = indexTarget;
                    styleShapeInline += mxConstants.STYLE_GRADIENT_DIRECTION+"="+mxConstants.DIRECTION_SOUTH+";";
                }
                
                InlineNodeAttribute attEdge = new InlineNodeAttribute(tempo,indexSource,tempo,y_inicial,"",false,false,false);
                attEdge.setIsEdge(true,idSource,idTarget,Math.abs((zoom* (indexTarget+1+deslocamento) + 20) - (zoom* (indexSource+1+deslocamento) + 20)),isNorth);
                
                Object edge = graph.insertVertex(null, tempo+" "+idSource+" "+idTarget, attEdge , spacing*zoom* (tempo+deslocamento)+shiftX +0, (zoom* (y_inicial+1+deslocamento) + 20) +4, sizeEdgeTemporalInline, Math.abs((zoom* (indexTarget+1+deslocamento) + 20) - (zoom* (indexSource+1+deslocamento) + 20)) , styleShapeInline);
                
                listAllEdges.add(edge);
                
                //edge.setParent(v2);
                
                //Source node
                v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(idSource+" "+tempo);
                if(v1 == null)
                {
                    
                    indexSource++;
                    InlineNodeAttribute att = new InlineNodeAttribute(tempo,idSource,tempo,indexSource,"",false,false,true);
                    att.setWeightInTime(1);
                    
                    //Nós dentro do layout					   
                    v1 = (mxCell) graph.insertVertex(parent, idSource+" "+tempo, att , spacing*zoom* (att.getX_atual()+deslocamento)+shiftX , zoom* (indexSource+deslocamento) + 20, 10,10, styleShape+style.mxStyle);
                    matrizGraphLine[tempo] = matrizGraphLine[tempo]+1;
                    
                }
                else
                {
                    InlineNodeAttribute att = (InlineNodeAttribute) v1.getValue();
                    att.setWeightInTime(att.getWeightInTime()+1);
                    
                    matrizGraphLine[tempo] = matrizGraphLine[tempo]+1;
                }
                
                //Target node
                v2 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(idTarget+" "+tempo);
                if(v2 == null)
                {
                    
                    indexTarget++;
                    InlineNodeAttribute att = new InlineNodeAttribute(tempo,idTarget,tempo,indexTarget,"",false,false,true);
                    att.setWeightInTime(1);
                    
                    v2 = (mxCell) graph.insertVertex(parent, idTarget+" "+tempo, att , spacing*zoom* (att.getX_atual()+deslocamento)+shiftX , zoom* (indexTarget+deslocamento) + 20, 10,10, styleShape+style.mxStyle);
                    matrizGraphLine[tempo] = matrizGraphLine[tempo]+1;
                }
                else
                {
                    InlineNodeAttribute att = (InlineNodeAttribute) v2.getValue();
                    att.setWeightInTime(att.getWeightInTime()+1);
                    
                    matrizGraphLine[tempo] = matrizGraphLine[tempo]+1;
                }

                //styleShapeEdge += mxConstants.STYLE_FILLCOLOR+"=#FF0000;";
                //styleShapeEdge += mxConstants.STYLE_STROKECOLOR+"=#FF0000;";
                //styleShapeEdge += mxConstants.STYLE_GRADIENT_DIRECTION+"=north;";

                
                //graph.insertEdge(parent, tempo+" "+idSource+" "+idTarget, idSource+" "+idTarget, v1, v2,styleGradientEdge);

                //graph.setCellStyle(styleGradientEdge);
                
                //Insert Inline Histogram
                
                Object[] a = new Object[1];
                a[0] = edge;
                graph.cellsOrdered(a,true); //comment HERE to put the nodes in the background
                
                mxCell edgeCell = (mxCell) edge;
                edgeCell.setStyle(styleShapeInline);
                
                //Scalar Color
                /*String styleNodeHistogram = mxConstants.STYLE_MOVABLE+"=0;";
                styleNodeHistogram += mxConstants.STYLE_EDITABLE+"=0;";
                styleNodeHistogram += mxConstants.STYLE_RESIZABLE+"=0;";
                styleNodeHistogram += mxConstants.STYLE_NOLABEL+"=0;";
                styleNodeHistogram += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNodeHistogram += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNodeHistogram += mxConstants.STYLE_OPACITY+"=40;";

                InlineNodeAttribute att = new InlineNodeAttribute(idSource,idSource,idSource,idTarget,"",false,false,false);
                att.setIsHistogram(true);*/
               // graph.insertVertex(parent, idSource+" histogram "+idTarget, att , spacing*zoom* (att.getX_atual()+deslocamento) +shiftX - 5, zoom* (att.getY_atual()+deslocamento) + 20, 15,15, styleNodeHistogram);

                
            }
            

            //Insert Horizontal Lines
            for(Integer net : lineNodes ){
                
                JGraphStyle style = NetLayoutInlineNew.JGraphStyle.ROUNDED;

                String styleShape = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                styleShape += mxConstants.STYLE_NOLABEL+"=0;";
                styleShape += mxConstants.STYLE_RESIZABLE+"=0;";
                styleShape += mxConstants.STYLE_OPACITY+"=100;";
                styleShape += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_MIDDLE+";";
                styleShape += mxConstants.STYLE_FONTCOLOR+"=black;";
                //styleShape += mxConstants.STYLE_ALIGN+"=100px;";
                
                //Jeann
                String styleShapeLeft = mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_LEFT+";" + mxConstants.LABEL_INSET+"=100px;";// +  mxConstants.STYLE_ALIGN+"=100px;";
                String styleShapeRight = mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_RIGHT+";" +  mxConstants.STYLE_ALIGN+"=100px;";

                int index = lineNodes.indexOf(net);
                index++;
                
                InlineNodeAttribute att = new InlineNodeAttribute(tempo,net,0,index,"  "+net+"      ",false,true,false);
                att.setLeft(true);
                v1 = (mxCell) graph.insertVertex(null, "   ", att, spacing*zoom* (0+deslocamento)+(shiftX/3), zoom* (index+deslocamento) + 20, 10,10, style.mxStyle+styleShape+styleShapeLeft);
                
                //Nó e rótulo à direita do layout temporal
                att = new InlineNodeAttribute(tempo,net,tempo ,index,"      "+net+"  ",false,true,false);
            //    att = new InlineNodeAttribute(tempo,net,tempo,index,"      "+net+"  ",false,true,false);
                att.setLeft(false);
                v2 = (mxCell) graph.insertVertex(null, "   ", att, spacing*zoom* ( tempo  +5+deslocamento)+(shiftX/3), zoom* (index+deslocamento) + 20, 10,10, style.mxStyle+styleShape+styleShapeRight);
            //    v2 = (mxCell) graph.insertVertex(null, "   ", att, spacing*zoom* (tempo +5+deslocamento)+(shiftX/3), zoom* (index+deslocamento) + 20, 10,10, style.mxStyle+styleShape+styleShapeRight);
          //      v2 = (mxCell) graph.insertVertex(null, "   ", att, spacing*zoom* (100 +5+deslocamento)+(shiftX/3), zoom* (index+deslocamento) + 20, 10,10, style.mxStyle+styleShape+styleShapeRight);

                graph.insertEdge(null, null,net+"", v1, v2,styleShapeEdgeLines+";"+mxConstants.STYLE_ENDARROW+"=0;");
            }

            
            
            
            //Insert Horizontal Times
       /*     if(veioDoStream)
            {
                for(int j=0;j<qtdInstantesPlotadasSeForStream;j++)
                {
                    InlineNodeAttribute att = new InlineNodeAttribute(j,j,j,1,""+j,true,false,false);
                    mxCell cell = (mxCell) graph.insertVertex(null, null, att, spacing*zoom* (j+deslocamento)+shiftX+5, zoom* (1+deslocamento), 1,1, styleShapeInvisibleNodes.replace(mxConstants.STYLE_OPACITY+"=100;", mxConstants.STYLE_OPACITY+"=0;")+style.mxStyle);
                }
            }*/
        //    else
        //    {
        //Insert Horizontal Times
            for(int j=0;j<=tempo;j++)
            {
                InlineNodeAttribute att = new InlineNodeAttribute(j,j,j,1,""+j,true,false,false);
                mxCell cell = (mxCell) graph.insertVertex(null, j + "T", att, spacing*zoom* (j+deslocamento)+shiftX+5, zoom* (1+deslocamento), 1,1, styleShapeInvisibleNodes.replace(mxConstants.STYLE_OPACITY+"=100;", mxConstants.STYLE_OPACITY+"=0;")+style.mxStyle);
              //  InlineNodeAttribute att2 = (InlineNodeAttribute) cell.getValue();
            }
       //     }
                
        List<Integer> matrizGraphLineList = new ArrayList<>(matrizGraphLine.length);

		for (Integer i : matrizGraphLine) {
			matrizGraphLineList.add(i/2); //Divide por 2 pq há no linegraph (dois nós) a cada aresta. Não posso simplesmente fazer +1 ao invés de +2 lá em cima nesse método por causa da compatibiliadde com o que já existia no dynetvis
		}
            
           
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            graph.getModel().endUpdate();
            graphComponent = new mxGraphComponent(graph);

        }

        graph.setAllowDanglingEdges(false);
        graph.setCellsEditable(false);
        graph.setCellsDisconnectable(false);
        graph.cellsOrdered(graph.getChildVertices(graph.getDefaultParent()), false); //comment HERE to put the nodes in the background
        graph.setEdgeLabelsMovable(false);
        
        //mxGraphics2DCanvas.putShape(CurvedShape.KEY, new CurvedShape());
        //mxStyleRegistry.putValue(CurvedEdgeStyle.KEY, new CurvedEdgeStyle()); 
        
        //graph.getStylesheet().getDefaultEdgeStyle().put( mxConstants.STYLE_SHAPE, CurvedShape.KEY );
        //graph.getStylesheet().getDefaultEdgeStyle().put( mxConstants.STYLE_EDGE, CurvedEdgeStyle.KEY );

    }
    
    private boolean listAttNodesLocalJaTemNo(ArrayList<InlineNodeAttribute> listAttNodesLocal, int idNo)
    {
        for(InlineNodeAttribute attNo : listAttNodesLocal)
            if(attNo.getId_original() == idNo)
                return true;
        return false;
    }
    
    
     
     
     public void changeColorLineNodesWithoutStructural()
     {
         if(getColor().equals("Original"))
             changeColorInlineNodesLeft(null);
         else if(getColor().equals("Metadata File"))
        {
            String[] communitiesColors = new String[100000];
            
            String[] colorsTable = new String[100];
            String[] colorsLabel = new String[100];

            JFileChooser openDialog = new JFileChooser();
            String filename = "";

            openDialog.setMultiSelectionEnabled(false);
            openDialog.setDialogTitle("Open file");

            openDialog.setSelectedFile(new File(filename));
            openDialog.setCurrentDirectory(new File(filename));

            int contColorsLabel = 0;
            
            int result = openDialog.showOpenDialog(openDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                filename = openDialog.getSelectedFile().getAbsolutePath();
                openDialog.setSelectedFile(new File(""));
                BufferedReader file;
                try {
                    file = new BufferedReader(new FileReader(new File(filename)));
                    String line = file.readLine();
                    String[] tokens = line.split(" ");
                    String tmp, strLine = "";
                    
                    
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
                    
                    
                    /*
                    colorsTable[0] = "229 115 115";
                    colorsTable[1] = "213 000 000";

                    colorsTable[2] = "149 117 205";
                    colorsTable[3] = "098 000 234";

                    colorsTable[4] = "079 195 247"; 
                    colorsTable[5] = "000 145 234";

                    colorsTable[6] = "129 199 132";
                    colorsTable[7] = "000 200 083";

                    colorsTable[8] = "255 213 079";
                    colorsTable[9] = "255 171 000";

                    colorsTable[10] = "033 033 033";
                   
                    
                    
                    colorsTable[0] = "041 098 255";
                    colorsTable[1] = "000 145 234"; 
                    colorsTable[2] = "000 184 212";

                    colorsTable[3] = "213 000 000";
                    colorsTable[4] = "197 017 098";
                    colorsTable[5] = "255 109 000";

                    colorsTable[6] = "000 200 083";
                    colorsTable[7] = "100 221 023";
                    colorsTable[8] = "174 231 000";

                    colorsTable[9] = "000 000 000";
                    colorsTable[10] = "000 000 000";
                     */
                    
                    for(int i = 10; i < colorsTable.length;i++)
                    {
                        colorsTable[i] = "000 000 000";
                    }
                    
                    int cont= 0;
                    
                    communitiesColors[Integer.parseInt(tokens[0])] = colorsTable[cont];
                    colorsLabel[0] = tokens[1];
                    
                    while ((tmp = file.readLine()) != null)
                    {
                        
                        strLine = tmp;
                        String[] tokens2 = strLine.split(" ");
                        if(!tokens[1].equals(tokens2[1]))
                        {
                            tokens[1] = tokens2[1];
                            cont++;
                            colorsLabel[cont] = tokens2[1];
                        }
                        communitiesColors[Integer.parseInt(tokens2[0])] = colorsTable[cont];
                    }
                }catch (FileNotFoundException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            



                //All other nodes
                Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
                graph.getModel().beginUpdate();

                mxCell firstNode = (mxCell) roots[0];

                float vMax = Float.parseFloat(firstNode.getEdgeCount()+"");
                float vMin = Float.parseFloat(firstNode.getEdgeCount()+"");
                float maxColor = 1;
                float minColor = 0;

                for (Object root1 : roots) {
                    Object[] root = {root1};
                    mxCell cell = (mxCell) root1;
                    String idCell = cell.getId();
                    String[] parts = idCell.split(" ");
                    if(parts.length != 2 )
                    {
                        int weightNode = cell.getEdgeCount();

                        if(weightNode > vMax)
                            vMax = weightNode;
                        if(weightNode < vMin)
                            vMin = weightNode;

                    }
                }


                for (Object root1 : roots) {
                    Object[] root = {root1};
                    mxCell cell = (mxCell) root1;
                    String idCell = cell.getId();
                    idCell = idCell.substring(0, idCell.length()-1);
                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                    if(att.isLineNode())
                    {

                        int r = 0;
                        int g = 0;
                        int b = 0;
                        if(communitiesColors[Integer.parseInt(idCell)] != null) //-1 pra tirar o 'L' ou 'R'
                        {
                            String color2 = communitiesColors[Integer.parseInt(idCell)];
                            String[] tokens = color2.split(" ");
                            r = Integer.parseInt(tokens[0]);
                            g = Integer.parseInt(tokens[1]);
                            b = Integer.parseInt(tokens[2]);
                        }
                        Color colorNode = new Color(r,g,b);
                        String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                        
                        String styleNode = cell.getStyle();
                        styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                        styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                        
                        cell.setStyle(styleNode);
                        
                    //    styleNode = styleNode.replaceAll("fillcolor=[^;]*;","fillcolor="+hexColor+";");
                    //    styleNode = styleNode.replaceAll("strokecolor=[^;]*;","strokecolor="+hexColor+";");


                      /*  String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                        styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                        styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                        styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                        styleNode += mxConstants.STYLE_OPACITY+"=100;";
                        styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                      
                        graph.setCellStyle(styleNode, root);*/
                    }
                }

                graph.getModel().endUpdate();



                 //Scalar Nodes
                boolean firstBool = true;
                String first = "";

                int communityColorId = 0;

                roots = graphComponentScalarInline.getGraph().getChildCells(graphComponentScalarInline.getGraph().getDefaultParent(), true, false);
                graphComponentScalarInline.getGraph().getModel().beginUpdate();
                for (Object root1 : roots) {
                    Object[] root = {root1};
                    mxCell cell = (mxCell) root1;

                    int r = 0;
                    int g = 0;
                    int b = 0;

                    String color2 = colorsTable[communityColorId];
                    String[] tokens = color2.split(" ");
                    r = Integer.parseInt(tokens[0]);
                    g = Integer.parseInt(tokens[1]);
                    b = Integer.parseInt(tokens[2]);

                    String label = "";
                    if(communityColorId < 10)
                        label = colorsLabel[communityColorId];

                    communityColorId++;
                    Color colorNode = new Color(r,g,b);
                    String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                    cell.setId(hexColor);

                    String styleNode = "";

                    styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

                    styleNode += mxConstants.STYLE_MOVABLE+"=0;";
                    styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                    styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                    styleNode += mxConstants.STYLE_NOLABEL+"=0;";

                    Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                    String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);

                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                    cell.setValue(label);

                    styleNode += mxConstants.STYLE_FONTSIZE+"=10;";
                    styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                    styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                    styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                    styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                    styleNode += mxConstants.STYLE_OPACITY+"=100;";

                    graphComponentScalarInline.getGraph().setCellStyle(styleNode, root);
                }
                graphComponentScalarInline.getGraph().getModel().endUpdate();
            }

        }
     }
     
     
     
     private int[] getPosicoesNos(int idSource, int idTarget, List<Integer> posicaoAtualNos) //Obtem as atuais posições dos nós que vão gerar a aresta
     {
         int [] retorno = {-1,-1};

         if(posicaoAtualNos != null) //Já há uma ordenação prévia dos nós, ie. trata-se de um layout de rede estática (plota toda a rede de uma vez) ao invés de stream (instante por instante)
         {
            for(int i = 0; i < posicaoAtualNos.size(); i++)
            {
                if(posicaoAtualNos.get(i) == idSource)
                    retorno[0] = i;
                else if(posicaoAtualNos.get(i) == idTarget)
                    retorno[1] = i;

                if(retorno[0] != -1 && retorno[1] != -1)
                    return retorno;
            }
         }
         else
         {
              for(int i = 0; i < listAttNodes.size(); i++)
                {
                    InlineNodeAttribute att = listAttNodes.get(i);
                    if(att.getId_original() == idSource)
                        retorno[0] = i;
                    else if(att.getId_original() == idTarget)
                        retorno[1] = i;

                    if(retorno[0] != -1 && retorno[1] != -1)
                        return retorno;
                }
         }
         return retorno;
     }
     
     @Deprecated
     private int[] getPosicoesNosOld(int idSource, int idTarget) //Obtem posições dos nós que vão gerar a aresta
     {
         int [] retorno = {-1,-1};
         
         for(int i = 0; i < listAttNodes.size(); i++)
         {
             InlineNodeAttribute att = listAttNodes.get(i);
             if(att.getId_original() == idSource)
                 retorno[0] = i;
             else if(att.getId_original() == idTarget)
                 retorno[1] = i;
             
             if(retorno[0] != -1 && retorno[1] != -1)
                 return retorno;
         }
         return retorno;
     }
     
     
     
     private void changeOpacityCell(String idCell, int newOpacity)
     {
         mxCell cell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(idCell);
         if(cell != null)
         {
             String style = cell.getStyle();
             style = style.replaceAll("opacity=[^;]*;","opacity="+newOpacity+";");
             graph.getModel().beginUpdate();
             cell.setStyle(style);
             graph.getModel().endUpdate();
         }
     }
     
     
    
    
    private int spaceBetweenLines;
    
    public void scrollToNodeId(String idNode, boolean center)
    {
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        graph.getModel().beginUpdate();
        Object a = roots[0];
        mxCell fisrtCell = (mxCell) roots[0];
        idNode = idNode.replaceAll(" ","");
        if(idNode.matches("[0-9]+"))
        {

            Integer idNod = Integer.parseInt(idNode);


            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                Double tempo;
                if(cell.isVertex())



                {
                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                    if(att.isLineNode() && att.getId_original() == idNod && att.isLeft())
                    {
                        graphComponent.scrollCellToVisible(cell,center);
                    }
                }
            }
        }
    }
    
    public void changeSpaceLinesFile(){
        
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        graph.getModel().beginUpdate();
        Object a = roots[0];
        mxCell fisrtCell = (mxCell) roots[0];
        String idCellFirst = fisrtCell.getId();
        String[] partsFirst = idCellFirst.split(" ");
        
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            String idCell = cell.getId();
            String[] parts = idCell.split(" ");
            Double tempo;
            
            if(cell.isVertex())
            {
                Integer minTimeGraph = Integer.parseInt(partsFirst[1]);
                
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isGraphLine())
                {
                    graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                    graph.moveCells(root, att.getX_atual() , att.getY_atual() * spaceBetweenLines );
                }
                else 
                    graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                
                if(att.isNode())
                {
                    int x = spacing * zoom* (att.getX_atual() + deslocamento)+ shiftX;
                    int y =  zoom* (att.getY_atual()*deslocamento * spaceBetweenLines) + 35 ;
                    graph.moveCells(root, x , y );
                }
                else if(att.isLineNode())
                {
                    if(att.getX_original() == 0)
                        graph.moveCells(root, spacing * zoom* (att.getX_atual() + deslocamento) +(shiftX/3) ,  zoom* (att.getY_atual()*deslocamento * spaceBetweenLines ) + 35  );
                    else
                        graph.moveCells(root, spacing * zoom* (att.getX_atual() + deslocamento) + 35 + shiftX,  zoom* (att.getY_atual()*deslocamento * spaceBetweenLines ) + 35 );
                }
                else if(att.isTimeNode())
                {
                    graph.moveCells(root, spacing * zoom* (att.getX_atual() + deslocamento)+ shiftX + 5 ,  zoom* (deslocamento ) +15  );
                }
                else if(att.isEdge())
                {
                    
                    mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                    //att.setHeightEdge_atual(att.getHeightEdge_atual()*spaceBetweenLines);
                    g.setHeight(att.getHeightEdge_atual()*spaceBetweenLines); 
                    cell.setGeometry(g);
                    graph.moveCells(root, spacing*zoom* (att.getX_atual()+deslocamento)+shiftX ,zoom* (att.getY_atual() * deslocamento * spaceBetweenLines) + 24);
                }
            }
        }
        
        graph.getModel().endUpdate();
        graph.refresh();
        graph.repaint();
       
    }
    
    private String colorInline; 
    private String typeColorInline; 
    private String colorEdgeInline; 
    private String typeEdgeColorInline; 
    private String formNode;
    private String sizeNode;
    private String templateColor;
    private String templateMat;
    
    
    final public void changeFormNodeInline(){
        if(getFormNode().equals("Circular")){
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isNode() )
                {
                    JGraphStyle style = NetLayoutInlineNew.JGraphStyle.ROUNDED;
                    String styleS = cell.getStyle();
                    styleS = styleS.replace(mxConstants.SHAPE_RECTANGLE, mxConstants.SHAPE_ELLIPSE);

                    graph.setCellStyle(styleS, root);
                    
                }
            }
        }
        else if(getFormNode().equals("Square"))
        {
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isNode() )
                {
                    JGraphStyle style = NetLayoutInlineNew.JGraphStyle.ROUNDED;
                    String styleS = cell.getStyle();
                    styleS = styleS.replace(mxConstants.SHAPE_ELLIPSE, mxConstants.SHAPE_RECTANGLE);

                    graph.setCellStyle(styleS, root);

                }
            }
        }
    }
    
    
    
    final public void changeSizeNodeInline(){
        if(getSizeNode().equals("Small")){
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isNode() )
                {
                    
                    mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                    g.setHeight(10);
                    g.setWidth(10);
                    cell.setGeometry(g);
                }
            }
        }
        else if(getSizeNode().equals("Big"))
        {
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isNode() )
                {
                    
                    mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                    g.setHeight(15);
                    g.setWidth(15);
                    cell.setGeometry(g);
                }
            }
        }
    }
    
    public Color firstColor;
    
    java.awt.Color color1Node;
    
    public void setOpacityTAM(boolean status)
    {

        //All other nodes
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        graph.getModel().beginUpdate();

        mxCell firstNode = (mxCell) roots[0];
                        InlineNodeAttribute attFirstNode = (InlineNodeAttribute) firstNode.getValue();

        float vMax = Float.parseFloat(attFirstNode.getWeightInTime()+"");
        float vMin = Float.parseFloat(attFirstNode.getWeightInTime()+"");
        float maxOpacity = 100;
        float minOpacity = 10;

        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;
            String idCell = cell.getId();
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

            if(att.isNode())
            {
                int weightNode = att.getWeightInTime();
                if(weightNode > vMax)
                    vMax = weightNode;
                if(weightNode < vMin)
                    vMin = weightNode;

            }
        }


        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

            if(att.isNode())
            {
                String style = cell.getStyle();
                
                int weightNode = att.getWeightInTime();
                float opacity = (int) (weightNode * (maxOpacity - minOpacity) / (vMax - vMin)) + minOpacity;
                if(status)
                    style = style.replaceAll("opacity=[^;]*;","opacity="+opacity+";");
                else
                    style = style.replaceAll("opacity=[^;]*;","opacity="+100+";");
                
                graph.setCellStyle(style, root);
            }
        }

        graph.getModel().endUpdate();
        graph.repaint();
        graph.refresh();
    }
    
    final public void changeColorInline(){
        
        if(getColorInline().equals("Original")){
            
             //Scalar Inline
            Object[] roots = graphComponentScalarInline.getGraph().getChildCells(graphComponentScalarInline.getGraph().getDefaultParent(), true, false);
            graphComponentScalarInline.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String styleNode = mxConstants.STYLE_MOVABLE+"=0;";
                styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                if(cell.getId().equals("color 0")){
                    styleNode += mxConstants.STYLE_FONTCOLOR+"="+Color.white+";";
                    cell.setValue("");
                }
                else if(cell.getId().equals("color 1")){
                    cell.setValue("");
                }
                
                styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                
                graphComponentScalarInline.getGraph().setCellStyle(styleNode, root);
                
            }
            graphComponentScalarInline.getGraph().getModel().endUpdate();
            
            
            roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isNode() )
                {
                    JGraphStyle style = NetLayoutInlineNew.JGraphStyle.ROUNDED;

                     
                    String styleShape = mxConstants.STYLE_NOLABEL+"=1;";
                    styleShape += mxConstants.STYLE_RESIZABLE+"=0;";
                    styleShape += mxConstants.STYLE_OPACITY+"=100;";
                    
                    if(formNode.equals("Circular"))
                        styleShape += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    else if(formNode.equals("Square"))
                        styleShape += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";

                    graph.setCellStyle(styleShape+style.mxStyle, root);

                    mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                    if(getSizeNode().equals("Small")){
                        g.setHeight(10);
                        g.setWidth(10);
                    }
                    else{
                        g.setHeight(15);
                        g.setWidth(15);
                    }
                    
                    cell.setGeometry(g);
                }
            }
            graph.getModel().endUpdate();
            
        }
        else if(getColorInline().equals("Random Color"))
        {
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isNode() )
                {
                    JGraphStyle style = NetLayoutInlineNew.JGraphStyle.ROUNDED;

                    
                    Random rand = new Random();
                    // Java 'Color' class takes 3 floats, from 0 to 1.
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();

                    Color color = new Color(r, g, b);
                    String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);

                     String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                    styleNode += mxConstants.STYLE_NOLABEL+"=1;";
                    styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                    styleNode += mxConstants.STYLE_OPACITY+"=100;";
                    
                    if(formNode.equals("Circular"))
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    else if(formNode.equals("Square"))
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";

                    graph.setCellStyle(styleNode+style.mxStyle, root);

                    mxGeometry ge = (mxGeometry) cell.getGeometry().clone();
                    if(getSizeNode().equals("Small")){
                        ge.setHeight(10);
                        ge.setWidth(10);
                    }
                    else{
                        ge.setHeight(15);
                        ge.setWidth(15);
                    }
                    
                    cell.setGeometry(ge);
                }
            }
            graph.getModel().endUpdate();
            
            
            //Scalar Nodes
            boolean firstBool = true;
            String first = "";

            int communityColorId = 0;

            roots = graphComponentScalarInline.getGraph().getChildCells(graphComponentScalarInline.getGraph().getDefaultParent(), true, false);
            graphComponentScalarInline.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
               Object[] root = {root1};
               mxCell cell = (mxCell) root1;

               Random rand = new Random();
               // Java 'Color' class takes 3 floats, from 0 to 1.
               float r = rand.nextFloat();
               float g = rand.nextFloat();
               float b = rand.nextFloat();

               Color colorNode = new Color(r, g, b);
               String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

               //cell.setId(hexColor);

               String styleNode = "";

               styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
               styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

               styleNode += mxConstants.STYLE_MOVABLE+"=0;";
               styleNode += mxConstants.STYLE_EDITABLE+"=0;";
               styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
               styleNode += mxConstants.STYLE_NOLABEL+"=0;";


               styleNode += mxConstants.STYLE_FONTSIZE+"=10;";
               styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
               styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
               styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
               styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
               styleNode += mxConstants.STYLE_OPACITY+"=100;";

               graphComponentScalarInline.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalarInline.getGraph().getModel().endUpdate();

            
        }
        else if(getColorInline().equals("Random Line"))
        {
            //HashMap<idCell,hexColor>
            HashMap<String,String> mapColorsNode = new HashMap(); 
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isNode() )
                {
                    JGraphStyle style = NetLayoutInlineNew.JGraphStyle.ROUNDED;
                    
                    Random rand = new Random();
                    // Java 'Color' class takes 3 floats, from 0 to 1.
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();

                    Color color = new Color(r, g, b);
                    String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);
                    //if()
                    String[] id = idCell.split(" ");
                    
                    if(mapColorsNode.get(id[0]) == null)
                        mapColorsNode.put(id[0], hexColor);
                    else
                        hexColor = mapColorsNode.get(id[0]);
                    
                     String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                    styleNode += mxConstants.STYLE_NOLABEL+"=1;";
                    styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                    styleNode += mxConstants.STYLE_OPACITY+"=100;";
                    
                    if(formNode.equals("Circular"))
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    else if(formNode.equals("Square"))
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";

                    graph.setCellStyle(styleNode+style.mxStyle, root);

                    mxGeometry ge = (mxGeometry) cell.getGeometry().clone();
                    if(getSizeNode().equals("Small")){
                        ge.setHeight(10);
                        ge.setWidth(10);
                    }
                    else{
                        ge.setHeight(15);
                        ge.setWidth(15);
                    }
                    
                    cell.setGeometry(ge);
                }
            }
            graph.getModel().endUpdate();
            
            
            
            //Scalar Nodes
            boolean firstBool = true;
            String first = "";

            int communityColorId = 0;

            roots = graphComponentScalarInline.getGraph().getChildCells(graphComponentScalarInline.getGraph().getDefaultParent(), true, false);
            graphComponentScalarInline.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
               Object[] root = {root1};
               mxCell cell = (mxCell) root1;

               Random rand = new Random();
               // Java 'Color' class takes 3 floats, from 0 to 1.
               float r = rand.nextFloat();
               float g = rand.nextFloat();
               float b = rand.nextFloat();

               Color colorNode = new Color(r, g, b);
               String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

               //cell.setId(hexColor);

               String styleNode = "";

               styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
               styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

               styleNode += mxConstants.STYLE_MOVABLE+"=0;";
               styleNode += mxConstants.STYLE_EDITABLE+"=0;";
               styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
               styleNode += mxConstants.STYLE_NOLABEL+"=0;";


               styleNode += mxConstants.STYLE_FONTSIZE+"=10;";
               styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
               styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
               styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
               styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
               styleNode += mxConstants.STYLE_OPACITY+"=100;";

               graphComponentScalarInline.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalarInline.getGraph().getModel().endUpdate();

            
            
        }
        else if(getColorInline().equals("Metadata File") || getColorInline().equals("Metadata TAM"))
        {
            String[] communitiesColors = new String[100000];
            
            String[] colorsTable = new String[100];
            String[] colorsLabel = new String[100];

            JFileChooser openDialog = new JFileChooser();
            String filename = "";

            openDialog.setMultiSelectionEnabled(false);
            openDialog.setDialogTitle("Open file");

            openDialog.setSelectedFile(new File(filename));
            openDialog.setCurrentDirectory(new File(filename));

            int contColorsLabel = 0;
            
            
            int result = openDialog.showOpenDialog(openDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                filename = openDialog.getSelectedFile().getAbsolutePath();
                openDialog.setSelectedFile(new File(""));
                BufferedReader file;
                try {
                    file = new BufferedReader(new FileReader(new File(filename)));
                    String line = file.readLine();
                    String[] tokens = line.split(" ");
                    
                    if(tokens.length != 2)
                    {
                        JOptionPane.showMessageDialog(null,"File format different from the expected. Check the Information button for details.","Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        String tmp, strLine = "";

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
                        colorsTable[10] = "000 000 000"; //Black

                        /*
                        colorsTable[10] = "000 000 000"; //Black
                        colorsTable[11] = "064 224 208"; //Turquoise
                        colorsTable[12] = "000 128 128"; //Teal
                        colorsTable[13] = "000 100 000"; //DarkGreen
                        colorsTable[14] = "085 107 047"; //DarkOliveGreen
                        colorsTable[15] = "218 165 032"; //Goldenrod
                        colorsTable[16] = "245 222 179"; //Wheat
                        colorsTable[17] = "075 000 130"; //Indigo
                        colorsTable[18] = "128 000 000"; //Maroon
                        colorsTable[19] = "255 215 000"; //Gold
                        colorsTable[20] = "240 230 140"; //Khaki



                        colorsTable[0] = "040 078 162";
                        colorsTable[1] = "096 187 070";
                        colorsTable[2] = "237 028 036";
                        colorsTable[3] = "253 183 041";
                        colorsTable[4] = "244 236 009";
                        colorsTable[5] = "185 076 156";
                        colorsTable[6] = "086 064 024";
                        colorsTable[7] = "207 082 084";
                        colorsTable[8] = "124 062 152";
                        colorsTable[9] = "106 204 220";
                        colorsTable[10] = "035 031 032";
                */        

                        /*
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


                        colorsTable[0] = "229 115 115";
                        colorsTable[1] = "213 000 000";

                        colorsTable[2] = "149 117 205";
                        colorsTable[3] = "098 000 234";

                        colorsTable[4] = "079 195 247"; 
                        colorsTable[5] = "000 145 234";

                        colorsTable[6] = "129 199 132";
                        colorsTable[7] = "000 200 083";

                        colorsTable[8] = "255 213 079";
                        colorsTable[9] = "255 171 000";

                        colorsTable[10] = "033 033 033";


                        colorsTable[0] = "041 098 255";
                        colorsTable[1] = "000 145 234"; 
                        colorsTable[2] = "000 184 212";

                        colorsTable[3] = "213 000 000";
                        colorsTable[4] = "197 017 098";
                        colorsTable[5] = "255 109 000";

                        colorsTable[6] = "000 200 083";
                        colorsTable[7] = "100 221 023";
                        colorsTable[8] = "148 000 211";
                        //colorsTable[8] = "174 231 000";

                        colorsTable[9] = "000 000 000";
                        colorsTable[10] = "000 000 000";
                        */
                        for(int i = 11; i < colorsTable.length;i++)
                        {
                            colorsTable[i] = "000 000 000";
                        }

                        int cont= 0;

                        communitiesColors[Integer.parseInt(tokens[0])] = colorsTable[cont];
                        colorsLabel[0] = tokens[1];

                        while ((tmp = file.readLine()) != null)
                        {

                            strLine = tmp;
                            String[] tokens2 = strLine.split(" ");
                            if(!tokens[1].equals(tokens2[1]))
                            {
                                tokens[1] = tokens2[1];
                                cont++;
                                colorsLabel[cont] = tokens2[1];
                            }
                            communitiesColors[Integer.parseInt(tokens2[0])] = colorsTable[cont];
                        }




                    //All other nodes
                    Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
                    graph.getModel().beginUpdate();

                    mxCell firstNode = (mxCell) roots[0];
                                    InlineNodeAttribute attFirstNode = (InlineNodeAttribute) firstNode.getValue();

                    float vMax = Float.parseFloat(attFirstNode.getWeightInTime()+"");
                    float vMin = Float.parseFloat(attFirstNode.getWeightInTime()+"");
                    float maxOpacity = 100;
                    float minOpacity = 10;

                    for (Object root1 : roots) {
                        Object[] root = {root1};
                        mxCell cell = (mxCell) root1;
                        String idCell = cell.getId();
                        String[] parts = idCell.split(" ");
                        InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                        if(att.isNode())
                        {
                            int weightNode = att.getWeightInTime();
                            if(weightNode > vMax)
                                vMax = weightNode;
                            if(weightNode < vMin)
                                vMin = weightNode;

                        }
                    }


                    for (Object root1 : roots) {
                        Object[] root = {root1};
                        mxCell cell = (mxCell) root1;
                        InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                        if(att.isNode())
                        {

                            int r = 0;
                            int g = 0;
                            int b = 0;
                            if(communitiesColors[att.getId_original()] != null)
                            {
                                String color2 = communitiesColors[att.getId_original()];
                                tokens = color2.split(" ");
                                r = Integer.parseInt(tokens[0]);
                                g = Integer.parseInt(tokens[1]);
                                b = Integer.parseInt(tokens[2]);
                            }
                            Color colorNode = new Color(r,g,b);
                            String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                            String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                            styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                            styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                            styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                            styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                                                    int weightNode = att.getWeightInTime();
                            float opacity = (int) (weightNode * (maxOpacity - minOpacity) / (vMax - vMin)) + minOpacity;

                            if(getColorInline().equals("Metadata File"))
                                opacity = 100;

                            styleNode += mxConstants.STYLE_OPACITY+"="+opacity+";";
                                                    if(showInstanceWeight)
                                styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                            else
                                styleNode += mxConstants.STYLE_NOLABEL+"=1;";

                            graph.setCellStyle(styleNode, root);
                        }
                    }

                    graph.getModel().endUpdate();



                     //Scalar Nodes
                    boolean firstBool = true;
                    String first = "";

                    int communityColorId = 0;

                    roots = graphComponentScalarInline.getGraph().getChildCells(graphComponentScalarInline.getGraph().getDefaultParent(), true, false);
                    graphComponentScalarInline.getGraph().getModel().beginUpdate();
                    for (Object root1 : roots) {
                        Object[] root = {root1};
                        mxCell cell = (mxCell) root1;

                        int r = 0;
                        int g = 0;
                        int b = 0;

                        String color2 = colorsTable[communityColorId];
                        tokens = color2.split(" ");
                        r = Integer.parseInt(tokens[0]);
                        g = Integer.parseInt(tokens[1]);
                        b = Integer.parseInt(tokens[2]);

                        String label = "";
                        if(communityColorId < 10)
                            label = colorsLabel[communityColorId];

                        communityColorId++;
                        Color colorNode = new Color(r,g,b);
                        String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                        //cell.setId(hexColor);

                        String styleNode = "";

                        styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                        styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

                        styleNode += mxConstants.STYLE_MOVABLE+"=0;";
                        styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                        styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                        styleNode += mxConstants.STYLE_NOLABEL+"=0;";

                        Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                        String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);

                        styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                        cell.setValue(label);
                        styleNode += mxConstants.STYLE_FONTSIZE+"=10;";
                        styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                        styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                        styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                        styleNode += mxConstants.STYLE_OPACITY+"=100;";

                        graphComponentScalarInline.getGraph().setCellStyle(styleNode, root);
                    }
                    graphComponentScalarInline.getGraph().getModel().endUpdate();

                    }
                }catch (FileNotFoundException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            }
        }
        else{
            
            ColorScale colorScale;
            
            switch (getTypeColorInline()) {
                case "Blue to Cyan":
                    colorScale = new BlueToCyanScale();
                    break;
                case "Blue to Yellow":
                    colorScale = new BlueToYellowScale();
                    break;
                case "Gray Scale":
                    colorScale = new GrayScale();
                    break;
                //case "Blue Scale":
                    //colorScale = new BlueScale();
                    //break;
                case "Green To White Scale":
                    colorScale = new GreenToWhiteScale();
                    break;
                case "Heated Object Scale":
                    colorScale = new HeatedObjectScale();
                    break;
                case "Linear Gray Scale":
                    colorScale = new LinearGrayScale();
                    break;
                case "Locs Scale":
                    colorScale = new LocsScale();
                    break;
                case "Green to Red":
                    colorScale = new GreenToRed();
                    break;
                case "Rainbow Scale":
                    colorScale = new RainbowScale();
                    break;
                case "Blue Sky To Orange":
                    colorScale = new OrangeToBlueSky();
                    break;
                case "Orange To Blue Sky":
                    colorScale = new BlueSkyToOrange();
                    break;
                case "Blue To Red":
                    colorScale = new BlueToRed();
                    break;    
                case "Custom Color":
                    java.awt.Color color1 = javax.swing.JColorChooser.showDialog(graphComponent,
                    "Choose the Inicial Inline Node Color", java.awt.Color.BLACK);
                    java.awt.Color color2 = javax.swing.JColorChooser.showDialog(graphComponent,
                    "Choose the Final Inline Node Color", java.awt.Color.BLACK);
                    if(color1 != null && color2 != null)
                        colorScale = new CustomColor(color1,color2);
                    else
                        colorScale = new CustomColor(Color.BLACK,Color.BLACK);
                    break;
                default:
                    colorScale = new GreenToRed();
                    break;
            }
            
            float maxSize = 12;
            float minSize = 8;
           
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
         
            double vMax = 0;
            double vMin = 0;

            int i = 0;
            nodesCount = 0;
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isNode())
                {
                    nodesCount++;
                    int weightNode = att.getWeightInTime();
                    
                    if(i == 0)
                    {
                        vMax = weightNode;
                        vMin = weightNode;
                    }
                    else
                    {
                        if(weightNode > vMax)
                            vMax = weightNode;
                        if(weightNode < vMin)
                            vMin = weightNode;
                    }
                    i++;
                }
                
            }
            
            double mediaDados = 0;
            double stdDev = 0;
            
            if(getColorInline().equals("Color Std Dev"))
            {
                degreeNodes = new int[nodesCount];
                degreeNormalizedNodes = new double[nodesCount];
                
                i=0;
                for (Object root1 : roots) {
                    Object[] root = {root1};
                    mxCell cell = (mxCell) root1;
                    String idCell = cell.getId();
                    String[] parts = idCell.split(" ");
                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                    if(att.isNode())
                    {
                        degreeNodes[i] = att.getWeightInTime();
                        mediaDados += att.getWeightInTime();
                        i++;
                    }
                    
                }
                
                
                
                mediaDados = mediaDados / nodesCount;
                stdDev = getStdDev();
                
                
                
                i=0;
                for(double degreeNode : degreeNodes)
                {
                    degreeNormalizedNodes[i] = 1.0 / (1.0 + Math.exp(-degreeNode));;
                    i++;
                }
                
                vMax = degreeNormalizedNodes[0];
                vMin = degreeNormalizedNodes[0];
                for(double degreeNode : degreeNormalizedNodes)
                {
                    if(degreeNode > vMax)
                        vMax = degreeNode;
                    if(degreeNode < vMin)
                        vMin = degreeNode;
                }
                
                
            }
            
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isNode())
                {
                    
                    double weightNode = att.getWeightInTime();
                    
                    if(getColorInline().equals("Color Std Dev"))
                    {
                        weightNode = ( weightNode - mediaDados ) / stdDev;
                    }
                    
                    float insize = (int) (weightNode * (maxSize - minSize) / (vMax - vMin)) + minSize;
                    double incolor = (weightNode - vMin) / (vMax - vMin);

                    if(getColorInline().equals("Color Std Dev"))
                    {
                        //Function Sigmoid
                        incolor = 1.0 / (1.0 + Math.exp(-weightNode));
                    }
                    
                    //if(Float.isNaN(incolor))
                      //  incolor = 0;
                    
                    //float f = Integer.MAX_VALUE;
                    //int ia = (int) f;
                    
                    //if(insize == ia)
                      //  insize = 10;
                    
                    Color colorNode = colorScale.getColor((float)incolor);
                    String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                    mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                    if(getSizeNode().equals("Small")){
                        g.setHeight(10);
                        g.setWidth(10);
                    }
                    else{
                        g.setHeight(15);
                        g.setWidth(15);
                    }
                    cell.setGeometry(g);

                    String styleNode2 =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleNode2 += mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                    //String styleNode2 = mxConstants.STYLE_GRADIENTCOLOR+"="+hexColor+";";
                    styleNode2 += mxConstants.STYLE_EDITABLE+"=0;";
                    styleNode2 += mxConstants.STYLE_RESIZABLE+"=0;";
                    styleNode2 += mxConstants.STYLE_NOLABEL+"=1;";
                    
                    if(formNode.equals("Circular"))
                        styleNode2 += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    else if(formNode.equals("Square"))
                        styleNode2 += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";

                    styleNode2 += mxConstants.STYLE_OPACITY+"=100;";
                    
                    graph.setCellStyle(styleNode2, root);
                }
            }
            
            graph.getModel().endUpdate();
            
            
             //Scalar Nodes Inline
            boolean firstBool = true;
            String first = "";
            
            roots = graphComponentScalarInline.getGraph().getChildCells(graphComponentScalarInline.getGraph().getDefaultParent(), true, false);
            graphComponentScalarInline.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                String[] parts = idCell.split(" ");
                Color colorNode = colorScale.getColor(Float.parseFloat(parts[1]));
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                if(firstBool)
                {
                    firstColor = colorNode;
                    first = hexColor;
                    firstBool = false;
                }
                
                String styleNode = "";
                if(vMin == vMax)
                {
                    styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+first+";";
                    styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+first+";";
                }
                else
                {
                    styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                }
                styleNode += mxConstants.STYLE_MOVABLE+"=0;";
                styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                
                Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);
                
                if(cell.getId().equals("color 0")){
                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                    //styleNode = styleNode.replace(mxConstants.STYLE_FILLCOLOR+"="+first+";",mxConstants.STYLE_FILLCOLOR+"="+"#"+Integer.toHexString(Color.WHITE.getRGB()).substring(2)+";");
                    cell.setValue((int)vMin+"");
                }
                else if(cell.getId().equals("color 1")){
                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                    //styleNode = styleNode.replace(mxConstants.STYLE_FILLCOLOR+"="+hexColor+";",mxConstants.STYLE_FILLCOLOR+"="+"#"+Integer.toHexString(Color.WHITE.getRGB()).substring(2)+";");
                    cell.setValue((int)vMax+"");
                }
                else
                {
                    cell.setValue("");
                }
                styleNode += mxConstants.STYLE_FONTSIZE+"=15;";
                styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                
                graphComponentScalarInline.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalarInline.getGraph().getModel().endUpdate();
            
        }
    }
    
    
    final public void changeColorEdgeInline(){
        
        if(getColorEdgeInline().equals("Original")){
            
             //Scalar Edges Inline
            Object[] roots = graphComponentScalarEdgeInline.getGraph().getChildCells(graphComponentScalarEdgeInline.getGraph().getDefaultParent(), true, false);
            graphComponentScalarEdgeInline.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                
                String styleNode = mxConstants.STYLE_MOVABLE+"=0;";
                styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                if(cell.getId().equals("color 0")){
                    cell.setValue("");
                }
                else if(cell.getId().equals("color 1")){
                    cell.setValue("");
                }
                
                styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                
                graphComponentScalarEdgeInline.getGraph().setCellStyle(styleNode, root);
                
            }
            graphComponentScalarEdgeInline.getGraph().getModel().endUpdate();
            
            
            roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge() )
                {
                    

                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor=#C3D9FF;");
                    styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor=#C3D9FF;");
                    
                    cell.setStyle(styleShapeInline);

                    
                    //styleGradientEdge = styleShapeEdge+mxConstants.STYLE_ENDARROW+"=0;strokeColor=none;fillColor=#C3D9FF;"+mxConstants.STYLE_GRADIENTCOLOR+"=#C3D9FF;";
                    //graph.setCellStyle(styleGradientEdge, root);

                }
            }
        }
	else if(getColorEdgeInline().equals("Random Edge"))
        {
        
            HashMap<String,String> mapColorsNode = new HashMap(); 
            
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
         
            
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge())
                {
                    
                    Random rand = new Random();
                    // Java 'Color' class takes 3 floats, from 0 to 1.
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    
                    Color color = new Color(r, g, b);
                    String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);
                    
                    
                    if(mapColorsNode.get(att.getOrigin()+ " "+att.getDestiny()) == null)
                    {
                        if(mapColorsNode.get(att.getDestiny()+ " "+att.getOrigin()) == null)
                        {
                            mapColorsNode.put(att.getDestiny()+ " "+att.getOrigin(), hexColor);
                            mapColorsNode.put(att.getOrigin()+ " "+att.getDestiny(), hexColor);
                        }
                    }
                    else
                        hexColor = mapColorsNode.get(att.getOrigin()+ " "+att.getDestiny());
                    
                    
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                    styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");
                    
                    cell.setStyle(styleShapeInline);

                }
            }
            
            graph.getModel().endUpdate();
            
            
            
            //Scalar Edges
            boolean firstBool = true;
            String first = "";

            roots = graphComponentScalarEdgeInline.getGraph().getChildCells(graphComponentScalarEdgeInline.getGraph().getDefaultParent(), true, false);
            graphComponentScalarEdgeInline.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
               Object[] root = {root1};
               mxCell cell = (mxCell) root1;

               Random rand = new Random();
               // Java 'Color' class takes 3 floats, from 0 to 1.
               float r = rand.nextFloat();
               float g = rand.nextFloat();
               float b = rand.nextFloat();

               Color colorNode = new Color(r, g, b);
               String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                styleNode += mxConstants.STYLE_MOVABLE+"=0;";
                styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                styleNode += mxConstants.STYLE_NOLABEL+"=0;";

                styleNode += mxConstants.STYLE_FONTSIZE+"=10;";
                styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";

               graphComponentScalarEdgeInline.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalarEdgeInline.getGraph().getModel().endUpdate();

            
        }
        else if(getColorEdgeInline().equals("Random Color"))
        {
        
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
         
            
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge())
                {
                    
                    Random rand = new Random();
                    // Java 'Color' class takes 3 floats, from 0 to 1.
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    
                    Color color = new Color(r, g, b);
                    String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);
                    
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                    styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");
                    
                    cell.setStyle(styleShapeInline);

                }
            }
            
            graph.getModel().endUpdate();
           
            
            //Scalar Edges
            boolean firstBool = true;
            String first = "";

            roots = graphComponentScalarEdgeInline.getGraph().getChildCells(graphComponentScalarEdgeInline.getGraph().getDefaultParent(), true, false);
            graphComponentScalarEdgeInline.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
               Object[] root = {root1};
               mxCell cell = (mxCell) root1;

               Random rand = new Random();
               // Java 'Color' class takes 3 floats, from 0 to 1.
               float r = rand.nextFloat();
               float g = rand.nextFloat();
               float b = rand.nextFloat();

               Color colorNode = new Color(r, g, b);
               String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                styleNode += mxConstants.STYLE_MOVABLE+"=0;";
                styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                styleNode += mxConstants.STYLE_NOLABEL+"=0;";

                styleNode += mxConstants.STYLE_FONTSIZE+"=10;";
                styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";

               graphComponentScalarEdgeInline.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalarEdgeInline.getGraph().getModel().endUpdate();

            
        }
        else if(getColorEdgeInline().equals("Metadata File"))
        {
            String[] communitiesColors = new String[100000];
            
            String[] colorsTable = new String[100];
            String[] colorsLabel = new String[100];

            JFileChooser openDialog = new JFileChooser();
            String filename = "";

            openDialog.setMultiSelectionEnabled(false);
            openDialog.setDialogTitle("Open file");

            openDialog.setSelectedFile(new File(filename));
            openDialog.setCurrentDirectory(new File(filename));

            int contColorsLabel = 0;
            
            int result = openDialog.showOpenDialog(openDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                filename = openDialog.getSelectedFile().getAbsolutePath();
                openDialog.setSelectedFile(new File(""));
                BufferedReader file;
                try {
                    file = new BufferedReader(new FileReader(new File(filename)));
                    String line = file.readLine();
                    String[] tokens = line.split(" ");
                    String tmp, strLine = "";
                    
                    
                    if(tokens.length != 4)
                    {
                        JOptionPane.showMessageDialog(null,"File format different from the expected. Check the Information button for details.","Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {

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
                        colorsTable[10] = "255 255 255"; //black


                         /*


                        colorsTable[2] = "255 000 000"; //red
                        colorsTable[6] = "000 255 000"; //green
                        colorsTable[0] = "229 115 115"; //cyan
                        colorsTable[3] = "255 185 60"; //orange
                        colorsTable[4] = "255 255 000"; //yellow
                        colorsTable[1] = "255 000 255"; //pink
                        colorsTable[5] = "100 070 000"; //brown
                        colorsTable[7] = "205 092 092"; //light pink
                        colorsTable[8] = "148 000 211"; //purple
                        colorsTable[9] = "000 255 255"; //cyan
                        colorsTable[10] = "000 000 255"; //blue



                        colorsTable[0] = "229 115 115";
                        colorsTable[1] = "213 000 000";

                        colorsTable[2] = "149 117 205";
                        colorsTable[3] = "098 000 234";

                        colorsTable[4] = "079 195 247"; 
                        colorsTable[5] = "000 145 234";

                        colorsTable[6] = "129 199 132";
                        colorsTable[7] = "000 200 083";

                        colorsTable[8] = "255 213 079";
                        colorsTable[9] = "255 171 000";

                        colorsTable[10] = "033 033 033";


                        colorsTable[0] = "041 098 255";
                        colorsTable[1] = "000 145 234"; 
                        colorsTable[2] = "000 184 212";

                        colorsTable[3] = "213 000 000";
                        colorsTable[4] = "197 017 098";
                        colorsTable[5] = "255 109 000";

                        colorsTable[6] = "000 200 083";
                        colorsTable[7] = "100 221 023";
                        colorsTable[8] = "174 231 000";

                        colorsTable[9] = "000 000 000";
                        colorsTable[10] = "000 000 000";
                         */
                        for(int i = 11; i < colorsTable.length;i++)
                        {
                            colorsTable[i] = "000 000 000";
                        }

                        int cont= 0;

                        colorsLabel[0] = tokens[3];

                        graph.getModel().beginUpdate();

                        String[] colorRGB = colorsTable[Integer.parseInt(tokens[3])].split(" ");
                        Color color = new Color(Integer.parseInt(colorRGB[0]),Integer.parseInt(colorRGB[1]),Integer.parseInt(colorRGB[2]));


                        int t = (int) Math.floor(Integer.parseInt(tokens[2]) / resolution);


                        mxCell v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(t+" "+tokens[0]+" "+tokens[1]);

                        String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);

                        String styleShapeInline = v1.getStyle();
                        styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                        styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");

                        v1.setStyle(styleShapeInline);

                        while ((tmp = file.readLine()) != null)
                        {
                            strLine = tmp;
                            String[] tokens2 = strLine.split(" ");
                            if(!tokens[3].equals(tokens2[3]))
                            {
                                tokens[3] = tokens2[3];
                                cont++;
                                colorsLabel[cont] = tokens2[3];
                            }

                            colorRGB = colorsTable[Integer.parseInt(tokens2[3])].split(" ");
                            color = new Color(Integer.parseInt(colorRGB[0]),Integer.parseInt(colorRGB[1]),Integer.parseInt(colorRGB[2]));

                            t = (int) Math.floor(Integer.parseInt(tokens2[2]) / resolution);

                            v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(t+" "+tokens2[0]+" "+tokens2[1]);

                            hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);
                            if(v1 == null)
                                continue;
                            styleShapeInline = v1.getStyle();
                            if(v1.getStyle() == null)
                                continue;
                            styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                            styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");

                            v1.setStyle(styleShapeInline);

                        }
                        graph.getModel().endUpdate();



                        Object[] roots = graphComponentScalarEdgeInline.getGraph().getChildCells(graphComponentScalarEdgeInline.getGraph().getDefaultParent(), true, false);
                        graphComponentScalarEdgeInline.getGraph().getModel().beginUpdate();

                        int scalarInt = 0;

                        for (Object root1 : roots) {
                            Object[] root = {root1};
                            mxCell cell = (mxCell) root1;
                            String idCell = cell.getId();

                            colorRGB = colorsTable[scalarInt].split(" ");
                            color = new Color(Integer.parseInt(colorRGB[0]),Integer.parseInt(colorRGB[1]),Integer.parseInt(colorRGB[2]));

                            hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);

                            String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                            styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                            styleNode += mxConstants.STYLE_MOVABLE+"=0;";
                            styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                            styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                            styleNode += mxConstants.STYLE_NOLABEL+"=0;";

                            Color corEmNegativo = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
                            String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);

                            styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                            cell.setValue(colorsLabel[scalarInt]);

                            styleNode += mxConstants.STYLE_FONTSIZE+"=10;";
                            styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                            styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                            styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                            styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                            styleNode += mxConstants.STYLE_OPACITY+"=100;";

                            graphComponentScalarEdgeInline.getGraph().setCellStyle(styleNode, root);

                            scalarInt++;
                        }
                        graphComponentScalarEdgeInline.getGraph().getModel().endUpdate();
                    }
                    
                }catch (FileNotFoundException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else if(getColorEdgeInline().equals("Metadata Without Time"))
        {
            String[] communitiesColors = new String[100000];
            
            String[] colorsTable = new String[100];
            String[] colorsLabel = new String[10000];

            JFileChooser openDialog = new JFileChooser();
            String filename = "";

            openDialog.setMultiSelectionEnabled(false);
            openDialog.setDialogTitle("Open file");

            openDialog.setSelectedFile(new File(filename));
            openDialog.setCurrentDirectory(new File(filename));

            int contColorsLabel = 0;
            
            int result = openDialog.showOpenDialog(openDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                filename = openDialog.getSelectedFile().getAbsolutePath();
                openDialog.setSelectedFile(new File(""));
                BufferedReader file;
                try {
                    file = new BufferedReader(new FileReader(new File(filename)));
                    String line = file.readLine();
                    String[] tokens = line.split(" ");
                    String tmp, strLine = "";
                    
                    
                    if(tokens.length != 3)
                    {
                        JOptionPane.showMessageDialog(null,"File format different from the expected. Check the Information button for details.","Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {


                        colorsTable[0] = "000 255 000"; //green
                        colorsTable[1] = "000 000 000"; //black
                        colorsTable[2] = "000 000 255"; //blue
                        colorsTable[3] = "255 185 60"; //orange
                        colorsTable[4] = "255 255 000"; //yellow
                        colorsTable[5] = "255 000 255"; //pink
                        colorsTable[6] = "100 070 000"; //brown
                        colorsTable[7] = "205 092 092"; //light pink
                        colorsTable[8] = "148 000 211"; //purple
                        colorsTable[9] = "000 255 255"; //cyan
                         /*
                        colorsTable[0] = "229 115 115";
                        colorsTable[1] = "213 000 000";

                        colorsTable[2] = "149 117 205";
                        colorsTable[3] = "098 000 234";

                        colorsTable[4] = "079 195 247"; 
                        colorsTable[5] = "000 145 234";

                        colorsTable[6] = "129 199 132";
                        colorsTable[7] = "000 200 083";

                        colorsTable[8] = "255 213 079";
                        colorsTable[9] = "255 171 000";

                        colorsTable[10] = "033 033 033";


                        colorsTable[0] = "041 098 255";
                        colorsTable[1] = "000 145 234"; 
                        colorsTable[2] = "000 184 212";

                        colorsTable[3] = "213 000 000";
                        colorsTable[4] = "197 017 098";
                        colorsTable[5] = "255 109 000";

                        colorsTable[6] = "000 200 083";
                        colorsTable[7] = "100 221 023";
                        colorsTable[8] = "174 231 000";

                        colorsTable[9] = "000 000 000";
                        colorsTable[10] = "000 000 000";
                         */
                        for(int i = 10; i < colorsTable.length;i++)
                        {
                            colorsTable[i] = "000 000 000";
                        }

                        int cont= 0;

                        colorsLabel[0] = tokens[2];

                        graph.getModel().beginUpdate();

                        String[] colorRGB = colorsTable[Integer.parseInt(tokens[2])].split(" ");
                        Color color = new Color(Integer.parseInt(colorRGB[0]),Integer.parseInt(colorRGB[1]),Integer.parseInt(colorRGB[2]));


                        //int t = (int) Math.floor(Integer.parseInt(tokens[2]) / resolution);

                        for(int t=0;t < getMaxTime(); t++)
                        {
                            mxCell v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(t+" "+tokens[0]+" "+tokens[1]);
                            if(v1 == null)
                            {
                                v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(t+" "+tokens[1]+" "+tokens[0]);
                            }
                            if(v1 != null)
                            {
                                String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);
                                String styleShapeInline = v1.getStyle();
                                styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                                styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");

                                v1.setStyle(styleShapeInline);
                            }
                        }
                        while ((tmp = file.readLine()) != null)
                        {
                            strLine = tmp;
                            String[] tokens2 = strLine.split(" ");
                            if(!tokens[2].equals(tokens2[2]))
                            {
                                tokens[2] = tokens2[2];
                                cont++;
                                colorsLabel[cont] = tokens2[2];
                            }

                            colorRGB = colorsTable[Integer.parseInt(tokens2[2])].split(" ");
                            color = new Color(Integer.parseInt(colorRGB[0]),Integer.parseInt(colorRGB[1]),Integer.parseInt(colorRGB[2]));

                            //t = (int) Math.floor(Integer.parseInt(tokens2[2]) / resolution);
                            for(int t=0;t < getMaxTime(); t++)
                            {
                                mxCell v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(t+" "+tokens2[0]+" "+tokens2[1]);
                                if(v1 == null)
                                {
                                    v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(t+" "+tokens2[1]+" "+tokens2[0]);
                                }
                                if(v1 != null)
                                {
                                    String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);

                                    String styleShapeInline = v1.getStyle();
                                    styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                                    styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");

                                    v1.setStyle(styleShapeInline);
                                }
                            }
                        }
                        graph.getModel().endUpdate();



                        Object[] roots = graphComponentScalarEdgeInline.getGraph().getChildCells(graphComponentScalarEdgeInline.getGraph().getDefaultParent(), true, false);
                        graphComponentScalarEdgeInline.getGraph().getModel().beginUpdate();

                        int scalarInt = 0;

                        for (Object root1 : roots) {
                            Object[] root = {root1};
                            mxCell cell = (mxCell) root1;
                            String idCell = cell.getId();

                            colorRGB = colorsTable[scalarInt].split(" ");
                            color = new Color(Integer.parseInt(colorRGB[0]),Integer.parseInt(colorRGB[1]),Integer.parseInt(colorRGB[2]));

                            String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);

                            String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                            styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                            styleNode += mxConstants.STYLE_MOVABLE+"=0;";
                            styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                            styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                            styleNode += mxConstants.STYLE_NOLABEL+"=0;";

                            Color corEmNegativo = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
                            String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);

                            styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                            cell.setValue(colorsLabel[scalarInt]);

                            styleNode += mxConstants.STYLE_FONTSIZE+"=10;";
                            styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                            styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                            styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                            styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                            styleNode += mxConstants.STYLE_OPACITY+"=100;";

                            graphComponentScalarEdgeInline.getGraph().setCellStyle(styleNode, root);

                            scalarInt++;
                        }
                        graphComponentScalarEdgeInline.getGraph().getModel().endUpdate();
                    }
                    
                }catch (FileNotFoundException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else if(getColorEdgeInline().equals("Medium Size Color"))
        {
            recalculateMediumEdgesColors();
        }
        else{
            
            ColorScale colorScale;
            
            switch (getTypeColorEdge()) {
                case "Blue to Cyan":
                    colorScale = new BlueToCyanScale();
                    break;
                case "Blue to Yellow":
                    colorScale = new BlueToYellowScale();
                    break;
                case "Gray Scale":
                    colorScale = new GrayScale();
                    break;
                case "Green To White Scale":
                    colorScale = new GreenToWhiteScale();
                    break;
                case "Heated Object Scale":
                    colorScale = new HeatedObjectScale();
                    break;
                case "Linear Gray Scale":
                    colorScale = new LinearGrayScale();
                    break;
                case "Locs Scale":
                    colorScale = new LocsScale();
                    break;
                case "Green to Red":
                    colorScale = new GreenToRed();
                    break;
                case "Rainbow Scale":
                    colorScale = new RainbowScale();
                    break;
                case "Blue Sky To Orange":
                    colorScale = new OrangeToBlueSky();
                    break;
                case "Orange To Blue Sky":
                    colorScale = new BlueSkyToOrange();
                    break;
                case "Blue To Red":
                    colorScale = new BlueToRed();
                    break;    
                case "Custom Color":
                    java.awt.Color color1 = javax.swing.JColorChooser.showDialog(graphComponent,
                    "Choose the Inicial Inline Node Color", java.awt.Color.BLACK);
                    java.awt.Color color2 = javax.swing.JColorChooser.showDialog(graphComponent,
                    "Choose the Final Inline Node Color", java.awt.Color.BLACK);
                    if(color1 != null && color2 != null)
                        colorScale = new CustomColor(color1,color2);
                    else
                        colorScale = new CustomColor(Color.BLACK,Color.BLACK);
                    break;
                default:
                    colorScale = new GreenToRed();
                    break;
            }
            
            float maxSize = 12;
            float minSize = 8;
           
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
         
            
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge())
                {
                    Color colorNodeMin = colorScale.getColor(colorScale.getMin());
                    String hexColorMin = "#"+Integer.toHexString(colorNodeMin.getRGB()).substring(2);

                    Color colorNodeMax = colorScale.getColor(colorScale.getMax());
                    String hexColorMax = "#"+Integer.toHexString(colorNodeMax.getRGB()).substring(2);

                    
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColorMax+";");
                    styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColorMin+";");
                    
                    cell.setStyle(styleShapeInline);

                }
            }
            
            graph.getModel().endUpdate();
           
             //Scalar Edges Inline
            boolean firstBool = true;
            String first = "";
            
            roots = graphComponentScalarEdgeInline.getGraph().getChildCells(graphComponentScalarEdgeInline.getGraph().getDefaultParent(), true, false);
            graphComponentScalarEdgeInline.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                String[] parts = idCell.split(" ");
                
                Color colorNode = colorScale.getColor(Float.parseFloat(parts[1]));
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                if(firstBool)
                {
                    firstColor = colorNode;
                    first = hexColor;
                    firstBool = false;
                }
                
                String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                styleNode += mxConstants.STYLE_MOVABLE+"=0;";
                styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                
                Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);
                
                if(cell.getId().equals("color 0")){
                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                    cell.setValue("From");
                }
                if(cell.getId().equals("color 1")){
                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                    cell.setValue("To");
                }
                styleNode += mxConstants.STYLE_FONTSIZE+"=10;";
                styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                
                graphComponentScalarEdgeInline.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalarEdgeInline.getGraph().getModel().endUpdate();
            
            
        }
    }
    
    
    final public void changeColorInlineNodesLeft(Object[] rootsEstrutural){
        
        if(getColor().equals("Original")){
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isLineNode())
                {
                    
                    JGraphStyle style = NetLayoutInlineNew.JGraphStyle.ROUNDED;

                    String styleShape = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    styleShape += mxConstants.STYLE_NOLABEL+"=0;";
                    styleShape += mxConstants.STYLE_RESIZABLE+"=0;";
                    styleShape += mxConstants.STYLE_OPACITY+"=100;";
                    
                    String styleShapeLeftRight;
                    if(att.isLeft())
                         styleShapeLeftRight = mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_LEFT+";";
                    else
                        styleShapeLeftRight = mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_RIGHT+";";
                    
                    graph.setCellStyle(styleShape+style.mxStyle+styleShapeLeftRight, root);

                    mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                    g.setHeight(10);
                    g.setWidth(10);
                    cell.setGeometry(g);
                    
                }
            }
            
        }
        else{
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            graph.getModel().beginUpdate();
         
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isLineNode())
                {
                    String styleEstrutural = "";
                    for(Object nodeEstrutural : rootsEstrutural){
                        mxCell node = (mxCell) nodeEstrutural;
                        if(att.getId_original() == Integer.parseInt(node.getId())){
                            styleEstrutural = node.getStyle();
                            styleEstrutural += mxConstants.STYLE_NOLABEL+"=0;";
                            break;
                        }
                    }

                    
                    String styleShapeLeftRight;
                    if(att.isLeft())
                         styleShapeLeftRight = mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_LEFT+";";
                    else
                        styleShapeLeftRight = mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_RIGHT+";";
                    
                    styleEstrutural = styleEstrutural.replace(mxConstants.STYLE_NOLABEL+"=1;",mxConstants.STYLE_NOLABEL+"=0;");
                    
                    graph.setCellStyle(styleEstrutural+styleShapeLeftRight, root);
                }
            }
            
            graph.getModel().endUpdate();
        }
    }
    
    
    @Override
    public void setShowNodes(boolean showNodes) {
        
        graph.getModel().beginUpdate(); 
        if(showNodes)
        {
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                if(cell.isVertex())
                {
                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                    if(att.isNode())
                    {
                        String styleShapeInline = cell.getStyle();
                        styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=100;");
                        cell.setStyle(styleShapeInline);
                    }
                }
            }
        }
        else
        {    
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                if(cell.isVertex())
                {
                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                    if(att.isNode())
                    {
                        String styleShapeInline = cell.getStyle();
                        styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                        cell.setStyle(styleShapeInline);
                    }
                }
            }
        }
        graph.getModel().endUpdate();

        graph.repaint();
        graph.refresh();

        this.showNodes = showNodes;
    }
    
    @Override
    public void setShowEdgesHorizontalLines(boolean showEdgesHorizontalLines) {
        
        
        if(showEdgesHorizontalLines)
        {
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, true);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                if(cell.isEdge())
                {
                    String idEdge = (String) cell.getValue();
                    String[] parts = idEdge.split(" ");
                    if(parts.length == 1)
                    {
                        String styleShapeInline = cell.getStyle();
                        styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=100;");
                        graph.getModel().beginUpdate(); 
                        cell.setStyle(styleShapeInline);
                        graph.getModel().endUpdate(); 
                    }
                }
            }
        }
        else
        {    
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, true);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                if(cell.isEdge())
                {
                    String idEdge = (String) cell.getValue();
                    String[] parts = idEdge.split(" ");
                    if(parts.length == 1)
                    {
                        String styleShapeInline = cell.getStyle();
                        styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                        styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=0;");
                        graph.getModel().beginUpdate(); 
                        cell.setStyle(styleShapeInline);
                        graph.getModel().endUpdate(); 
                    }
                }
            }
        }
      //  graph.getModel().endUpdate();
       // graph.repaint();
       graph.refresh();
     //   graphComponent.getGraph().refresh();
        
        this.showEdgesHorizontalLines = showEdgesHorizontalLines;
    }
    
    @Override
    public void setShowEdges(boolean showEdges) {
        
        graph.getModel().beginUpdate(); 
        if(showEdges)
        {
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge())
                {
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=25;");
                    cell.setStyle(styleShapeInline);

                }
            }
        }
        else
        {    
            
            for(int y = 0; y < listAllEdges.size(); y++)
            {
                mxCell cell = (mxCell) listAllEdges.get(y);
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge())
                {
                    String styleShapeInline = cell.getStyle();
                    
                    styleShapeInline = styleShapeInline.replaceAll("opacity=[^;]*;","opacity=0;");
                    cell.setStyle(styleShapeInline);

                }
            }
        }
        graph.getModel().endUpdate();

        graph.repaint();
        graph.refresh();

        this.showEdges = showEdges;
    }
    
    public boolean showEdgesCommunities;
    public boolean blankSpaceCommunities = false;
    
    public void setShowEdgesCommunities(boolean showEdgesCommunities) {
        
        HashMap<Integer, List<InlineNodeAttribute>> comunidadesAtuais = comunidadesROCMultilevel.get(stateCommunityROCMultiLevel);
        List<InlineNodeAttribute> nosQueSeraoUsadosNesseNivel;
        ArrayList<mxCell> arestasQueSeraoUsadasNesseNivel = new ArrayList();
        ArrayList<mxCell> arestasTotais = new ArrayList();
        for(Integer comunidade : comunidadesAtuais.keySet())
        {
            nosQueSeraoUsadosNesseNivel = comunidadesAtuais.get(comunidade);
            arestasQueSeraoUsadasNesseNivel = obtemArestasDentroDaComunidade(nosQueSeraoUsadosNesseNivel);
            arestasTotais.addAll(arestasQueSeraoUsadasNesseNivel);
        }

        
        graph.getModel().beginUpdate(); 
        if(showEdgesCommunities)
        {
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge() && arestasTotais.contains(cell))
                {
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=25;");
                    cell.setStyle(styleShapeInline);

                }
                else if(att.isEdge() && !arestasTotais.contains(cell))
                {
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replaceAll("opacity=[^;]*;","opacity=0;");
                    cell.setStyle(styleShapeInline);
                }
            }
        }
        else
        {    
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge())
                {
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replaceAll("opacity=[^;]*;","opacity=0;");
                    cell.setStyle(styleShapeInline);

                }
            }
        }
        graph.getModel().endUpdate();
        graph.repaint();
        graph.refresh();
        this.showEdgesCommunities = showEdgesCommunities;
    }
    
    public void setShowEdgesExtraCommunities(boolean showEdgesCommunities) {
        
        HashMap<Integer, List<InlineNodeAttribute>> comunidadesAtuais = comunidadesROCMultilevel.get(stateCommunityROCMultiLevel);
        List<InlineNodeAttribute> nosQueSeraoUsadosNesseNivel;
        ArrayList<mxCell> arestasQueSeraoUsadasNesseNivel = new ArrayList();
        ArrayList<mxCell> arestasTotais = new ArrayList();
        for(Integer comunidade : comunidadesAtuais.keySet())
        {
            nosQueSeraoUsadosNesseNivel = comunidadesAtuais.get(comunidade);
            arestasQueSeraoUsadasNesseNivel = obtemArestasDentroDaComunidade(nosQueSeraoUsadosNesseNivel);
            arestasTotais.addAll(arestasQueSeraoUsadasNesseNivel);
        }

        
        graph.getModel().beginUpdate(); 
        if(showEdgesCommunities)
        {
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge() && !arestasTotais.contains(cell))
                {
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=25;");
                    cell.setStyle(styleShapeInline);

                }
                else if(att.isEdge() && arestasTotais.contains(cell))
                {
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replaceAll("opacity=[^;]*;","opacity=0;");
                    cell.setStyle(styleShapeInline);
                }
            }
        }
        else
        {    
            
            Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                if(att.isEdge())
                {
                    String styleShapeInline = cell.getStyle();
                    styleShapeInline = styleShapeInline.replaceAll("opacity=[^;]*;","opacity=0;");
                    cell.setStyle(styleShapeInline);

                }
            }
        }
        graph.getModel().endUpdate();
        graph.repaint();
        graph.refresh();
        this.showEdgesCommunities = showEdgesCommunities;
    }
 
    
    int[] degreeNodes;
    double[] degreeNormalizedNodes;
    int nodesCount; 
    
    String adress_taxonomy = "Estatistica_Hospital";
    
    public void getTaxonomy()
    {
        //Le o arquivo com os parametros.
        HashMap<String,Double> parametros = new HashMap<>();
        String arquivoParametros = FileHandler.leArquivo(".//"+adress_taxonomy+"//parametros.config");
        if(arquivoParametros != null)
        {
            String[] linhasArquivo = arquivoParametros.split("\r\n");
            if(linhasArquivo != null && linhasArquivo.length != 0)
            {
                for(String linha : linhasArquivo)
                {
                    if(!linha.equals("") && !linha.startsWith("--"))
                    {
                        String[] parametroString = linha.split(",");
                        parametros.put(parametroString[0], Double.parseDouble(parametroString[1]));
                    }
                }
            }
            for(String parametro : parametros.keySet())
                System.out.println("Parâmetro = " + parametro + " -> " + parametros.get(parametro));
        }
        
        
        
        /*String idComunidadesTesteS = FileHandler.leArquivo(".//"+adress_taxonomy+"//idComunidadesTeste.txt");
        ArrayList<Integer> idComunidadesTeste = new ArrayList<>();
        if(idComunidadesTesteS != null)
        {
            String[] linhasArquivo = idComunidadesTesteS.split("\r\n");
            if(linhasArquivo != null && linhasArquivo.length != 0)
            {
                for(String linha : linhasArquivo)
                {
                     if(!linha.equals(""))
                        idComunidadesTeste.add(Integer.parseInt(linha));
                }
                System.out.println(idComunidadesTeste);
            }
        }*/
        
        //stateCommunityROCMultiLevel = setado como 2 para pegar somente o ultimo nivel do Twitter
        HashMap<Integer, List<InlineNodeAttribute>> comunidadesAtuais = comunidadesROCMultilevel.get(parametros.get("QueroEstatisticasParaONivel").intValue());
        List<InlineNodeAttribute> nosQueSeraoUsadosNesseNivel;
        ArrayList<mxCell> arestasQueSeraoUsadasNesseNivel = new ArrayList();
        ArrayList<mxCell> arestasTotais = new ArrayList();
        String fileContentQtdNosPorComunidade = "";
        String fileContentQtdTopicosComunidade = "";
        String fileContentQtdArestaPorComunidade = "";
        String fileContentQtdMediaDistânciaArestaPorComunidade = "";
        String fileContentCommunityTaxonomy = "";
        int tempoInicial = 0, tempoFinal = 223;
        int comunidadesNaoDescartadas = 0;
        int tamanhoDivisao = 0, esparsa = 0, densa = 0, homogenea = 0, heterogenea = 0,parcial = 0, total = 0;
        int homogParcialDensa = 0, homogParcialEsparsa = 0, homogTotalDensa = 0, homogTotalEsparsa = 0;
        int heterParcialDensa = 0, heterParcialEsparsa = 0, heterTotalDensa = 0, heterTotalEsparsa = 0;
        
        //ArrayList<Integer> nosComponenteConexa = leMaiorComponenteConexa();
        
        for(Integer comunidade : comunidadesAtuais.keySet())
        {
            
            //if(idComunidadesTeste.contains(comunidade))
                //System.out.println("idComunidadeTeste");
            
            boolean contemUmNo = false, naoContemUmNo = false;
            ArrayList<Integer> idsNosQueSeraoUsadosNesseNivel = new ArrayList<>();
            int somaDistancias = 0, distancia = 0;
            ArrayList<Integer> distancias = new ArrayList();
            ArrayList<Integer> distanciasComTempoInicialFinal = new ArrayList();
            nosQueSeraoUsadosNesseNivel = comunidadesAtuais.get(comunidade);
            /*for(InlineNodeAttribute node : nosQueSeraoUsadosNesseNivel)
            {
             //   idsNosQueSeraoUsadosNesseNivel.add(node.getId_original());
                if(nosComponenteConexa.contains(node.getId_original()))
                    contemUmNo = true;
                else
                    naoContemUmNo = true;
            }
            
            if(contemUmNo && naoContemUmNo)
                System.out.println("--------------------Temos um problema --------------------");
            
            if(!contemUmNo)
            {
            //    System.out.println("Comunidade " + comunidade + " ignorada");
                continue;
            }
            */
            comunidadesNaoDescartadas++;
            
            fileContentQtdNosPorComunidade += comunidade+"\t"+ comunidadesAtuais.get(comunidade).size()+"\r\n";
            
            arestasQueSeraoUsadasNesseNivel = obtemArestasDentroDaComunidade(nosQueSeraoUsadosNesseNivel);
            HashMap<String, Double> colorsXfrequencia = new HashMap<>(); //Relaçao de tópicos x frequencia deles na comunidade 
            ArrayList<Integer> edgePerTime = new ArrayList();
            
            int qtdArestasFrequencia = 0;
            for(mxCell cell : arestasQueSeraoUsadasNesseNivel)
            {
                String styleEdge = cell.getStyle();
                int positionWord = styleEdge.lastIndexOf("fillColor");
                String color = styleEdge.substring(positionWord+11, positionWord+17);
                
                if(!color.equals("C3D9FF")) //cor default de nos e arestas azul claro
                {
                    qtdArestasFrequencia++;
                    if(!colorsXfrequencia.keySet().contains(color)) 
                        colorsXfrequencia.put(color, 1.0);
                    else
                       colorsXfrequencia.put(color, colorsXfrequencia.get(color) + 1.0);
                }
                InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                
                if(!edgePerTime.contains(att.getTime()))
                {
                    edgePerTime.add(att.getTime());
                }
                
            }
            
            double maiorFrequencia = 0;
            
            for(String color : colorsXfrequencia.keySet())
            {
                double frequencia = colorsXfrequencia.get(color) / (float)qtdArestasFrequencia * 100;
                colorsXfrequencia.put(color, frequencia); //arestasQueSeraoUsadasNesseNivel tem a qtd de arestas
                if(frequencia > maiorFrequencia)
                    maiorFrequencia = frequencia;
            }
            
            
            //arestasTotais.addAll(arestasQueSeraoUsadasNesseNivel);
            fileContentQtdArestaPorComunidade += comunidade+"\t"+ edgePerTime.size()+"\r\n";
            fileContentQtdTopicosComunidade += comunidade+"\t"+ colorsXfrequencia.keySet().size()+"\r\n";

            
            //double mediaDados, desvPadraoDados;
            
            //mediaDados = 88.8083507;
            //desvPadraoDados = 41.08746371;
            
            
            
            if(!edgePerTime.contains(tempoInicial))
                edgePerTime.add(tempoInicial);
            if(!edgePerTime.contains(tempoFinal))
                edgePerTime.add(tempoFinal);
            
            Collections.sort(edgePerTime);
            
            tamanhoDivisao = 0;

         //   if(edgePerTime.size() > parametros.get("QtdMinimaArestasPraSerDensa"))
            if(edgePerTime.size() > 3) //Ignora comunidades com uma só aresta, pois a distancia é máxima.
            {
                String dists = "";

                for(int i=0;i<edgePerTime.size()-1;i++)
                {
                    distancia = edgePerTime.get(i+1) - edgePerTime.get(i);
                    if(i != 0 && i != edgePerTime.size()-2 )
                    {
                        somaDistancias += distancia; //Usado pra calcular a média
                        distancias.add(distancia); //Array usado para calcular moda e mediana
                        dists += distancia + ", ";
                        tamanhoDivisao++;
                        
                    }
                    distanciasComTempoInicialFinal.add(distancia);
                }
                System.out.println("Comunidade " + comunidade + ": " + dists);
                
            }
            else
            {
                //Coloca a distancia como maxima para transformar as comunidades que possuirem menos de 'QtdMinimaArestasPraSerDensa' em Esparsas
                somaDistancias = tempoFinal;
                tamanhoDivisao = 1;
            }
            
            Collections.sort(edgePerTime);
            
            boolean isParcial = false;
            
            for(int i=0;i<edgePerTime.size()-1;i++)
            {
            //    if((edgePerTime.get(i+1) - edgePerTime.get(i)) > (desvioPadraoArestas + mediaArestas))
                if((edgePerTime.get(i+1) - edgePerTime.get(i)) > parametros.get("ThresholdParcialOuTotal"))
                {    
                    isParcial = true;
                }
            }
            float media = (float) somaDistancias / (float) tamanhoDivisao;
            int moda = getModa(distancias);
            double mediana = 0;

            DecimalFormat df = new DecimalFormat("#.00");
            
            //Descarta comunidades com uma só aresta
            if(distanciasComTempoInicialFinal.size() >= 3)
            {
                mediana = getMediana(distanciasComTempoInicialFinal); //Só calcula mediana das comunidades duas ou mais arestas (distancia >= 1)
                //String.valueOf(Math.log10(media)).replace(".",",") - LOG
                fileContentQtdMediaDistânciaArestaPorComunidade += comunidade+"\t"+ df.format(media)+ "\t" + distancias.size() + "\t" + (edgePerTime.size() - 2) + "\t" + df.format(moda) + "\t" + df.format(mediana) + "\r\n";
            
            }
            else
                mediana = tempoFinal;
            
            fileContentCommunityTaxonomy += comunidade;
            String oQueAComunidadeEh = "";

            //Inicio Parcial ou Total -- VALIDADO, ESTÁ OK
            if(isParcial)
            {
                oQueAComunidadeEh += "\t"+"Parcial";
                if(edgePerTime.size() > parametros.get("QtdMinimaArestasPraClassificarParcialInicialOuFinal"))
                {
                    int distanciaAoTempoFinal = edgePerTime.get(edgePerTime.size()-1) - edgePerTime.get(edgePerTime.size()-2);
                    int distanciaAoTempoInicial = edgePerTime.get(1) - edgePerTime.get(0);

                    if(distanciaAoTempoFinal > distanciaAoTempoInicial && distanciaAoTempoFinal > (tempoFinal*0.25)) //Ou seja, os últimos 25% da rede nao tem aresta
                        oQueAComunidadeEh += "_Final";
                    else if(distanciaAoTempoInicial >= distanciaAoTempoFinal && distanciaAoTempoInicial > (tempoFinal*0.25))
                        oQueAComunidadeEh += "_Inicial";
                }
                parcial++;
            }
            else
            {
                oQueAComunidadeEh += "\t"+"Total";
                total++;
            }
            //Fim Parcial ou Total

        /*
        Resumindo:
            Mais que y arestas e mediana  <= threshold   -> densa
            Mais que y e mediana  > threshold -> esparsa
            Menos que y -> esparsa
        */
            //if(edgePerTime.size() > parametros.get("QtdMinimaArestasPraSerDensa"))
            //{
            
            if(mediana > parametros.get("ThresholdEsparsaOuDensa"))
            {
                oQueAComunidadeEh += "\t"+"Esparsa";
                esparsa++;
            }
            else
            {
                float grau = (float) ( edgePerTime.size() * 100 ) / (float) tempoFinal;
                oQueAComunidadeEh += "\t"+"Densa\t"+df.format(grau)+"%";
                densa++;
            }
            
            /*
            }
            else
            {
                oQueAComunidadeEh += "\t"+"Esparsa";
                esparsa++;
            }*/
            
            //Fim Esparsa ou Densa
            
            //Inicio Homogenea ou Heterogenea
           // if(colorsXfrequencia.keySet().size() > 1)
           //if(maiorFrequencia < 90)
            if(maiorFrequencia < parametros.get("ThresholdHomogeneaOuHeterogenea"))
            {
                oQueAComunidadeEh += "\t"+"Heteregonea";
                heterogenea++;
            }
            else
            {
                oQueAComunidadeEh += "\t"+"Homogenea";
                homogenea++;
            }
            //Fim Homogenea ou Heterogenea
            
            if(oQueAComunidadeEh.contains("Homogenea") && oQueAComunidadeEh.contains("Parcial") && oQueAComunidadeEh.contains("Densa"))
                homogParcialDensa++;
            else if(oQueAComunidadeEh.contains("Homogenea") && oQueAComunidadeEh.contains("Parcial") && oQueAComunidadeEh.contains("Esparsa"))
                homogParcialEsparsa++;
            else if(oQueAComunidadeEh.contains("Homogenea") && oQueAComunidadeEh.contains("Total") && oQueAComunidadeEh.contains("Densa"))
                homogTotalDensa++;
            else if(oQueAComunidadeEh.contains("Homogenea") && oQueAComunidadeEh.contains("Total") && oQueAComunidadeEh.contains("Esparsa"))
                homogTotalEsparsa++;
            
            else if(oQueAComunidadeEh.contains("Heteregonea") && oQueAComunidadeEh.contains("Parcial") && oQueAComunidadeEh.contains("Densa"))
                heterParcialDensa++;
            else if(oQueAComunidadeEh.contains("Heteregonea") && oQueAComunidadeEh.contains("Parcial") && oQueAComunidadeEh.contains("Esparsa"))
                heterParcialEsparsa++;
            else if(oQueAComunidadeEh.contains("Heteregonea") && oQueAComunidadeEh.contains("Total") && oQueAComunidadeEh.contains("Densa"))
                heterTotalDensa++;
            else if(oQueAComunidadeEh.contains("Heteregonea") && oQueAComunidadeEh.contains("Total") && oQueAComunidadeEh.contains("Esparsa"))
                heterTotalEsparsa++;
            
            fileContentCommunityTaxonomy += oQueAComunidadeEh + "\r\n";
        }
        
        
        FileHandler.gravaArquivo(fileContentQtdNosPorComunidade, ".//"+adress_taxonomy+"//QtdNosPorComunidade.txt", false);
        FileHandler.gravaArquivo(fileContentQtdTopicosComunidade, ".//"+adress_taxonomy+"//QtdTopicosComunidade.txt", false);
        FileHandler.gravaArquivo(fileContentQtdArestaPorComunidade, ".//"+adress_taxonomy+"//QtdArestaPorComunidade.txt", false);
        FileHandler.gravaArquivo(fileContentQtdMediaDistânciaArestaPorComunidade, ".//"+adress_taxonomy+"//QtdMediaDistânciaArestaPorComunidade.txt", false);
        
        System.out.println("Comunidades Parciais: "+((float)parcial/(float)comunidadesNaoDescartadas)*100+"%");
        System.out.println("Comunidades Totais: "+((float)total/(float)comunidadesNaoDescartadas)*100+"%");
        System.out.println("Comunidades Homogeneas: "+((float)homogenea/(float)comunidadesNaoDescartadas)*100+"%");
        System.out.println("Comunidades Heterogeneas: "+((float)heterogenea/(float)comunidadesNaoDescartadas)*100+"%");
        System.out.println("Comunidades Esparsas: "+((float)esparsa/(float)comunidadesNaoDescartadas)*100+"%");
        System.out.println("Comunidades Densas: "+((float)densa/(float)comunidadesNaoDescartadas)*100+"%");
        
        fileContentCommunityTaxonomy += "\r\nComunidades Parciais: "+((float)parcial/(float)comunidadesNaoDescartadas)*100+"%\r\n";
        fileContentCommunityTaxonomy += "Comunidades Totais: "+((float)total/(float)comunidadesNaoDescartadas)*100+"%\r\n";
        fileContentCommunityTaxonomy += "Comunidades Homogeneas: "+((float)homogenea/(float)comunidadesNaoDescartadas)*100+"%\r\n";
        fileContentCommunityTaxonomy += "Comunidades Heterogeneas: "+((float)heterogenea/(float)comunidadesNaoDescartadas)*100+"%\r\n";
        fileContentCommunityTaxonomy += "Comunidades Esparsas: "+((float)esparsa/(float)comunidadesNaoDescartadas)*100+"%\r\n";
        fileContentCommunityTaxonomy += "Comunidades Densas: "+((float)densa/(float)comunidadesNaoDescartadas)*100+"%\r\n";
        
        fileContentCommunityTaxonomy += "\r\nQtd comunidades Homogeneas, parciais e densas: " + homogParcialDensa + "\r\n";
        fileContentCommunityTaxonomy += "Qtd comunidades Homogeneas, parciais e esparsas: " + homogParcialEsparsa + "\r\n";
        fileContentCommunityTaxonomy += "Qtd comunidades Homogeneas, totais e densas: " + homogTotalDensa + "\r\n";
        fileContentCommunityTaxonomy += "Qtd comunidades Homogeneas, totais e esparsas: " + homogTotalEsparsa + "\r\n";
        
        fileContentCommunityTaxonomy += "Qtd comunidades Heterogeneas, parciais e densas: " + heterParcialDensa + "\r\n";
        fileContentCommunityTaxonomy += "Qtd comunidades Heterogeneas, parciais e esparsas: " + heterParcialEsparsa + "\r\n";
        fileContentCommunityTaxonomy += "Qtd comunidades Heterogeneas, totais e densas: " + heterTotalDensa + "\r\n";
        fileContentCommunityTaxonomy += "Qtd comunidades Heterogeneas, totais e esparsas: " + heterTotalEsparsa + "\r\n";
        
        FileHandler.gravaArquivo(fileContentCommunityTaxonomy, ".//"+adress_taxonomy+"//CommunityTaxonomy.txt", false);
        
    }
    
    private ArrayList<Integer> leMaiorComponenteConexa()
    {
        String arquivo = FileHandler.leArquivo(".//"+adress_taxonomy+"//maiorComponenteConexaTwitter.txt");
        ArrayList<Integer> nosComponenteConexa = new ArrayList<>();
        int no;
        if(arquivo != null)
        {
            String[] linhasArquivo = arquivo.split("\r\n");
            if(linhasArquivo != null && linhasArquivo.length != 0)
            {
                for(String linha : linhasArquivo)
                {
                    no = Integer.parseInt(linha.trim());
                    if(!nosComponenteConexa.contains(no))
                        nosComponenteConexa.add(no);
                }
            }
        }
        return nosComponenteConexa;
    }
    
    public double getMedia(ArrayList<Integer> array) 
    {
        int soma = 0;
        for(Integer elemento : array)
            soma += elemento;
        
        return (double)soma / (double)array.size();
    }
    
    public double getMediana(ArrayList<Integer> array) 
    {

            Collections.sort(array);
            int tipo = array.size() % 2;
            if (tipo == 1) {
                  return array.get(((array.size() + 1) / 2) - 1);
            } else {
                  int m = array.size() / 2;
                  return (array.get(m - 1) + array.get(m)) / 2;
            }
      }
    
    public int getModa(ArrayList<Integer> array) 
    {
            HashMap<Integer,Integer> map = new HashMap();
            Integer i;
            int moda = 0;
            Integer numAtual = 0, numMaior = 0;
            for (int count = 0; count < array.size(); count++) 
            {
                if(!map.containsKey(array.get(count)))
                    map.put(array.get(count), 1);
                    
                else
                {
                    i = map.get(array.get(count));
                    map.put(array.get(count), i + 1);
                    //numAtual = i + 1;
                }
                numAtual = map.get(array.get(count));
                if (numAtual > numMaior) {
                        numMaior = numAtual;
                        moda = array.get(count);
                   }
            }
        //    System.out.println("\n Moda = " + moda + "; Eis o mapa: "+map.toString());
    
            return moda;

      }
    
    double getVarianceInline()
    {
        double mean = getMean();
        double temp = 0;
        for(int a :degreeNodes)
            temp += (a-mean)*(a-mean);
        return temp/nodesCount;
    }
    
    @Override
    double getMean()
    {
        double sum = 0;
        for(double a : degreeNodes)
            sum += a;
        return sum/nodesCount;
    }
    
    @Override
    double getStdDev()
    {
        return Math.sqrt(getVarianceInline());
    }
    
    
    public HashMap<Integer,Integer> vetorIdNodesNormalizados;
    public HashMap<Integer, Integer> nodeDegreeByAdjacencyMatrixId;
    
    public double modularity()
    {
        /*The modularity of a graph with respect to some division (or vertex types) measures how good the division is, or how separated are the different vertex types from each other. It defined as
        Q=1/(2m) * sum( (Aij-ki*kj/(2m) ) delta(ci,cj),i,j),
        here m is the number of edges, 
        Aij is the element of the A adjacency matrix in row i and column j, 
        ki is the degree of i, 
        kj is the degree of j
        , ci is the type (or component) of i, cj that of j, the sum goes over all i and j pairs of vertices, and delta(x,y) is 1 if x=y and 0 otherwise.
        If edge weights are given, then these are considered as the element of the A adjacency matrix, and ki is the sum of weights of adjacent edges for vertex i.
        modularity_matrix calculates the modularity matrix.
        This is a dense matrix, and it is defined as the difference of the adjacency matrix and the configuration model null model matrix. 
        In other words element M[i,j] is given as A[i,j]-d[i]d[j]/(2m), where A[i,j] is the (possibly weighted) adjacency matrix, d[i] is the degree of vertex i, and m is the number of edges (or the total weights in the graph, if it is weighed). 
        */
        
        double m = CountOfEdges;
        double sum = 0;
        
        for(int i = 0 ; i < adjacencyMatrix.length; i++)
        {
            for(int j = 0 ; j < adjacencyMatrix.length; j++)
            {
                if(i == j)
                    continue;
                double delta = 0;
                InlineNodeAttribute attOrigin = null;
                Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
                for (Object root1 : roots) {
                    mxCell cell = (mxCell) root1;
                    attOrigin = (InlineNodeAttribute) cell.getValue();
                    
                    if(attOrigin.isLineNode() && attOrigin.getId_original() == getKeyByValue(vetorIdNodesNormalizados,i))
                    {
                        break;
                    }
                }
                
                InlineNodeAttribute attDestiny = null;
                for (Object root1 : roots) {
                    mxCell cell = (mxCell) root1;
                    attDestiny = (InlineNodeAttribute) cell.getValue();
                    if(attDestiny.isLineNode() && attDestiny.getId_original() == getKeyByValue(vetorIdNodesNormalizados,j))
                    {
                        break;
                    }
                }
                int ci = 0, cj = 0;
                if(attOrigin != null && attDestiny != null )
                {
                    ci = idCommunityNode(attOrigin);
                    cj = idCommunityNode(attDestiny);
                    if(ci == cj)
                        delta = 1;
                }
                
                
                
                sum += ( adjacencyMatrix[i][j] - nodeDegreeByAdjacencyMatrixId.get(i) * nodeDegreeByAdjacencyMatrixId.get(j) / (2*m) ) * delta;
            }
        }
        
        double q = 1 / (2*m) * sum ;
        
        //Printing the contact matrices
            /*System.out.println("adjacency Matrix:");
            
            for(int i = 0; i < adjacencyMatrix.length; i++)
            {
                
                for(int j = 0; j < adjacencyMatrix.length; j++)
                {
                    System.out.print(adjacencyMatrix[i][j]+" ");
                }
                System.out.println();
            }*/
            //END Printing
        
        return q;
    }
    
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public Integer idCommunityNode(InlineNodeAttribute node)
    {
        //HashMap<Integer, List<InlineNodeAttribute>> comunidades;
        Set set = comunidades.entrySet();
        Integer idCommunityNode = 0;
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            List<InlineNodeAttribute> com = (List<InlineNodeAttribute>) mentry.getValue();
            if(com.contains(node))
                idCommunityNode = (Integer) mentry.getKey();
         }
        return idCommunityNode;
    }
    
    public long countEdgesCrossing = 0;
    public long uniqueEdgesCrossing = 0;
    
    public void verifyEdgesOverlapping()
    {
        
        boolean ASC = true;
        Map<Integer, Integer> sortedMapAsc = sortByComparator(currentTemporalNodeOrder, ASC);
        //printMap(sortedMapAsc);
        
        long countEdgesOverlapping = 0;
        long uniqueEdgesOverlapping = 0;
        
        HashSet<mxCell> edgesOverlapping = new HashSet();
        for(int x = 0; x < listAllEdges.size(); x++)
        {
            mxCell edge = (mxCell) listAllEdges.get(x);
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            String styleShapeInline = edge.getStyle();
            if(styleShapeInline.contains(mxConstants.STYLE_OPACITY+"=0;"))
            {
                continue;
            }
            
            for(int y = x+1; y < listAllEdges.size(); y++)
            {
                mxCell edgeCross = (mxCell) listAllEdges.get(y);
                InlineNodeAttribute attCross = (InlineNodeAttribute) edgeCross.getValue();
                String styleShapeCross = edgeCross.getStyle();
                if(styleShapeCross.contains(mxConstants.STYLE_OPACITY+"=0;"))
                {
                    continue;
                }
                
                if(att.getTime() != attCross.getTime())
                    break;
                
                if(!edge.getId().equals(edgeCross.getId()) && att.getTime() == attCross.getTime())
                {
                    int idAresta_origin;
                    int idAresta_destiny;
                    
                    //Validate if idAresta_origin is smaller than idAresta_destiny for edgeCross
                    if(sortedMapAsc.get(attCross.getOrigin()) < sortedMapAsc.get(attCross.getDestiny()))
                    {
                        idAresta_origin = sortedMapAsc.get(attCross.getOrigin());
                        idAresta_destiny = sortedMapAsc.get(attCross.getDestiny());
                    }
                    else
                    {
                        idAresta_origin = sortedMapAsc.get(attCross.getDestiny());
                        idAresta_destiny = sortedMapAsc.get(attCross.getOrigin());
                    }
                    
                    //Create the vector vecNodesEdC with the position of the nodes between the origin and destiny of the edge edC
                    HashSet<Integer> vecNodesPosEdC = new HashSet();
                    for(int i = idAresta_origin; i <= idAresta_destiny; i++)
                    {
                        vecNodesPosEdC.add(i);
                    }
                    
                    //Validate if idAresta_origin is smaller than idAresta_destiny for edge
                    if(sortedMapAsc.get(att.getOrigin()) < sortedMapAsc.get(att.getDestiny()))
                    {
                        idAresta_origin = sortedMapAsc.get(att.getOrigin());
                        idAresta_destiny = sortedMapAsc.get(att.getDestiny());
                    }
                    else
                    {
                        idAresta_origin = sortedMapAsc.get(att.getDestiny());
                        idAresta_destiny = sortedMapAsc.get(att.getOrigin());
                    }
                    
                    //Create the vector vecNodesEd with the position of the nodes between the origin and destiny of the edge ed
                    HashSet<Integer> vecNodesPosEd = new HashSet();
                    for(int i = idAresta_origin; i <= idAresta_destiny; i++)
                    {
                        vecNodesPosEd.add(i);
                    }
                    
                    HashSet<Integer> commonEdgesNodes = intersectionList(vecNodesPosEd,vecNodesPosEdC);
                    
                    if(commonEdgesNodes.size() > 1)
                    {
                        if(!edgesOverlapping.contains(edgeCross))
                        {
                            edgesOverlapping.add(edgeCross);
                            uniqueEdgesOverlapping++;
                        }
                        if(!edgesOverlapping.contains(edge))
                        {
                            edgesOverlapping.add(edge);
                            uniqueEdgesOverlapping++;
                        }
                        
                    }
                    
                    //Divide com 2 pois cada aresta tem 2 nos e queremos contar somente quantos trechos de aresta tem entre 2 arestas
                    int result = 0;
                    if(commonEdgesNodes.size() > 1)
                        result = (int) Math.ceil((double)commonEdgesNodes.size() / 2);
                    
                    if(result < 0)
                        System.out.println("result negativo");
                    
                    countEdgesOverlapping += result;
                    
                    /*      
                    int edgeCrossLocalValue = 0; 
                    for(Integer vecId : vecNodesPosEd)
                    {
                        if(vecNodesPosEdC.contains(vecId))
                            edgeCrossLocalValue++;
                        
                    }
                    
                    for(int i = idAresta_origin; i <= idAresta_destiny; i++)
                    {
                        //if(vecNodesPosEdC.contains(sortedMapAsc.get(i)) && !edgesOverlapping.contains(edgeCross.getId()))
                        if(vecNodesPosEdC.contains(sortedMapAsc.get(i)))
                        {
                            countEdgesOverlapping++;
                        }
                    }
                    */
                }
            }
            if(x == 0 || (x % 10000 == 0))
            {
                System.out.println("Quantidade de arestas ja executadas pelo Edge Overlapping:"+x);
                System.out.println("countEdgesOverlapping até aqui:"+countEdgesOverlapping);
                System.out.println("uniqueEdgesOverlapping até aqui:"+uniqueEdgesOverlapping);
            }
        } 

        //Divide por 2 pq o calculo ta contando as arestas de todas com todas, entao conta 2 vezes a sobreposicao
        //countEdgesOverlapping /= 2;
        countEdgesCrossing = countEdgesOverlapping;
        uniqueEdgesCrossing = uniqueEdgesOverlapping;
        System.out.println("countEdgesOverlapping: "+countEdgesOverlapping);
        System.out.println("uniqueEdgesOverlapping: "+uniqueEdgesOverlapping);
    }
    
    public double mediumEdgeSize = 0;
    public double stdEdgeSize = 0;
    public boolean stdDeviation = false;
    public boolean imprimirTxtEdgeLength = false;
    public String txtEdgeLength = "";
    public boolean openFileEdgeLength = false;
    public String pathFileEdgeLength = "";
    
     public <T> HashSet<T> intersectionList(HashSet<T> list1, HashSet<T> list2) {
        HashSet<T> list = new HashSet<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
    
     
    public void verifyAvgEdgesSize()
    {
        ArrayList<Integer> savedEdgeSize = new ArrayList();
        int countTotalEdgeSize = 0;
        //int countTotalEdgeSize2 = 0;
        mediumEdgeSize = 0;
        int ignoredTransparentEdges = 0;
        stdEdgeSize = 0;
        
        mxCell v1,v2;
        for(Object ed : listAllEdges)
        {
            mxCell edge = (mxCell) ed;
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            String styleShapeInline = edge.getStyle();
            if(styleShapeInline.contains(mxConstants.STYLE_OPACITY+"=0;"))
            {
                ignoredTransparentEdges++;
                att.setEdgeSize(0);
                continue;
            }
            v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(att.getOrigin()+" "+att.getTime());
            v2 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(att.getDestiny()+" "+att.getTime());
            if(v1 != null && v2 != null)
            {
                InlineNodeAttribute attNodeOrigin = (InlineNodeAttribute) v1.getValue();
                InlineNodeAttribute attNodeDestiny = (InlineNodeAttribute) v2.getValue();
                countTotalEdgeSize += Math.abs(attNodeOrigin.getY_atual() - attNodeDestiny.getY_atual()); // ESSE COUNT LEVA EM CONTA O ESPACO, A ALTURA REAL DA ARESTA
                savedEdgeSize.add(Math.abs(attNodeOrigin.getY_atual() - attNodeDestiny.getY_atual()));
                att.setEdgeSize(Math.abs(attNodeOrigin.getY_atual() - attNodeDestiny.getY_atual()));
                
                //countTotalEdgeSize2 += Math.abs(currentTemporalNodeOrder.get(attNodeOrigin.getId_original()) - currentTemporalNodeOrder.get(attNodeDestiny.getId_original()));
                //att.setEdgeSize(Math.abs(currentTemporalNodeOrder.get(attNodeOrigin.getId_original()) - currentTemporalNodeOrder.get(attNodeDestiny.getId_original())));
                
            }
        }
        if((listAllEdges.size() - ignoredTransparentEdges) == 0)
            mediumEdgeSize = 0;
        else
        {
            /*
            System.out.println("MEDIA ARESTAS:");
            System.out.println("WEIGHT ARESTAS: "+(double) countTotalEdgeSize / (double) (listAllEdges.size() - ignoredTransparentEdges ));
            System.out.println("DISTANCIA ENTRE NOS: "+(double) countTotalEdgeSize2 / (double) (listAllEdges.size() - ignoredTransparentEdges ));
            System.out.println("ORDER DOS NOS NA TELA:");
            
            boolean ASC = true;
            Map<Integer, Integer> sortedMapAsc = sortByComparator(currentTemporalNodeOrder, ASC);
            printMap(sortedMapAsc);
            */
            
            mediumEdgeSize = (double) countTotalEdgeSize / (double) (listAllEdges.size() - ignoredTransparentEdges );
            BigDecimal valorExato = new BigDecimal(mediumEdgeSize).setScale(2, RoundingMode.HALF_DOWN);
            mediumEdgeSize = Double.parseDouble(valorExato.toString());
            
            stdEdgeSize = getDesvioPadrao(savedEdgeSize);
            valorExato = new BigDecimal(stdEdgeSize).setScale(2, RoundingMode.HALF_DOWN);
            stdEdgeSize = Double.parseDouble(valorExato.toString());
        }
        
        if(getColorEdgeInline().equals("Medium Size Color"))
        {
            recalculateMediumEdgesColors();
        }
        
        System.out.println("Tamanho medio das arestas: "+mediumEdgeSize);
        System.out.println("Tamanho desvio padrão das arestas: "+stdEdgeSize);
        
        long tempoExecucaoEstatistica = System.currentTimeMillis();
        verifyEdgesOverlapping();
        tempoExecucaoEstatistica = System.currentTimeMillis() - tempoExecucaoEstatistica;
        String minutos = String.format("%d min, %d sec", new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(tempoExecucaoEstatistica)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(tempoExecucaoEstatistica) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tempoExecucaoEstatistica))) });

        System.out.println("Tempo de calculo estatisticas (exceto avgEdgeSize): " + minutos);
        
    }
    


    public static void printMap(Map<Integer, Integer> map)
    {
        for (Entry<Integer, Integer> entry : map.entrySet())
        {
            System.out.println(" Posicao : "+ entry.getValue()+ " No : " + entry.getKey());
        }
    }
    
    public Integer[][] adjacencyMatrix;
    public double CountOfEdges;
    //Set initial temp
    public double initialTemp = 100;
    //Cooling rate
    public double coolingRate = 0.003;

    
    public void SimulatedAnnealing() {
        
        if(imprimirTxtEdgeLength)
        {
            File edgeLengthOrder = new File(txtEdgeLength);
            if(edgeLengthOrder.exists())
                    edgeLengthOrder.delete();
        }
    
        List<InlineNodeAttribute> currentNodesIdOrder = new ArrayList();
        currentNodesIdOrder.addAll(listAttNodes);
        
        HashMap<Integer,List<InlineNodeAttribute>> sequenciaDeEntrada = new HashMap();
        
        //create random intial solution
        Collections.shuffle(currentNodesIdOrder);
        
        double bestDistance = verifyAvgEdgesSize(currentNodesIdOrder);
        
        System.out.println("Avg Edge Size of initial solution: " + bestDistance);
        //System.out.println("Order nodes: ");
        //for(int i = 0; i < nodesIdOrder.size();i++)
            //System.out.print(nodesIdOrder.get(i)+ "," );
        //System.out.println("End Order nodes: ");

        // We would like to keep track if the best solution
        // Assume best solution is the current solution
        List<InlineNodeAttribute> bestNodeOrder = new ArrayList();
        bestNodeOrder.addAll(currentNodesIdOrder);
        
        int iterations = 0;
        
        // Loop until system has cooled
        while (initialTemp > 1) {
            // Create new neighbour tour
            List<InlineNodeAttribute> newSolution = new ArrayList();
            newSolution.addAll(currentNodesIdOrder);

            /*System.out.println("Solution");
            for(int i = 0; i< newSolution.size();i++)
            {
                System.out.print(newSolution.get(i).getId_original()+" ");
            }
            System.out.println("End Solution");
              */ 
            
            // Get random positions
            Random r = new Random();
            int positionRandom1 = r.nextInt(newSolution.size()-1);
            int positionRandom2 = r.nextInt(newSolution.size()-1);
            
            while(positionRandom1 == positionRandom2)
            {
                positionRandom1 = r.nextInt(newSolution.size()-1);
                positionRandom2 = r.nextInt(newSolution.size()-1);
            }
            
            //System.out.println("Change values "+n1.getId_original()+ " with "+n2.getId_original());
            
            Collections.swap(newSolution, positionRandom1, positionRandom2);
            
            /*
            System.out.println("Solution");
            for(int i = 0; i< newSolution.size();i++)
            {
                System.out.print(newSolution.get(i).getId_original()+" ");
            }
            System.out.println("End Solution");
            */
            //Collections.shuffle(newSolution);
            
            //to make sure that newSolution and currentNodesIdOrder are different
            while(newSolution.equals(currentNodesIdOrder)) {
                
                positionRandom1 = r.nextInt(newSolution.size()-1);
                positionRandom2 = r.nextInt(newSolution.size()-1);
                
                while(positionRandom1 == positionRandom2)
                {
                    positionRandom1 = r.nextInt(newSolution.size()-1);
                    positionRandom2 = r.nextInt(newSolution.size()-1);
                }

                Collections.swap(newSolution, positionRandom1, positionRandom2);
                

            }
            
            // Get energy of solutions
            double currentDistance = verifyAvgEdgesSize(currentNodesIdOrder);
            double newDistance = verifyAvgEdgesSize(newSolution);
            
            // Decide if we should accept the neighbour
            double rand = randomDouble();
            if (acceptanceProbability(currentDistance, newDistance, initialTemp) > rand) {
                currentNodesIdOrder = newSolution;
                currentDistance = newDistance;
            }

            // Keep track of the best solution found
            if (currentDistance < bestDistance) {
                bestNodeOrder = new ArrayList();
                bestNodeOrder.addAll(currentNodesIdOrder);
                bestDistance = currentDistance;
            }
            
            // Cool system
            initialTemp *= 1 - coolingRate;
            iterations++;
        }

        System.out.println("Best Avg Edge Size solution: " + bestDistance + " in "+iterations+" iterations.");
        //System.out.println("Order nodes: ");
        //for(int i = 0; i < bestNodeOrder.size();i++)
           // System.out.print(bestNodeOrder.get(i)+",");
        //System.out.println("End Order nodes: ");
        sequenciaDeEntrada.put(0, bestNodeOrder);
        if(imprimirTxtEdgeLength)
            imprimeComunidadesArquivo(0, sequenciaDeEntrada);
        visualizaNos(sequenciaDeEntrada);
    }
    
    
	 public void randomNodeOrdering() {
        
        List<InlineNodeAttribute> currentNodesIdOrder = new ArrayList();
        currentNodesIdOrder.addAll(listAttNodes);
        
        HashMap<Integer,List<InlineNodeAttribute>> sequenciaDeEntrada = new HashMap();
        
        //create random intial solution
        Collections.shuffle(currentNodesIdOrder);
        
        sequenciaDeEntrada.put(0, currentNodesIdOrder);
        
        visualizaNos(sequenciaDeEntrada);
    }
	
    public double verifyAvgEdgesSize(List<InlineNodeAttribute> nodesOrder)
    {
        //long tempoExecucaoEstatistica = System.currentTimeMillis();
        ArrayList<Integer> savedEdgeSize = new ArrayList();
        int countTotalEdgeSize = 0;
        //int countTotalEdgeSize2 = 0;
        double mediumEdgeSize = 0;
        mxCell v1,v2;
        for(Object ed : listAllEdges)
        {
            mxCell edge = (mxCell) ed;
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            
            v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(att.getOrigin()+" "+att.getTime());
            v2 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(att.getDestiny()+" "+att.getTime());
            InlineNodeAttribute attNodeOrigin = (InlineNodeAttribute) v1.getValue();
            InlineNodeAttribute attNodeDestiny = (InlineNodeAttribute) v2.getValue();
            
            int originY = -1;
            int destinyY = -1;
            for(int i = 0; i < nodesOrder.size(); i++)
            {
                if(originY != -1 && destinyY != -1)
                    break;
                
                if (nodesOrder.get(i).getId_original() == attNodeOrigin.getId_original())
                    originY = i;
                if (nodesOrder.get(i).getId_original() == attNodeDestiny.getId_original())
                    destinyY = i;
                
            }

            if(stdDeviation)
                savedEdgeSize.add(Math.abs(originY - destinyY)); // ESSE COUNT NAO LEVA EM CONTA O ESPACO, A ALTURA REAL DA ARESTA
            else
                countTotalEdgeSize += Math.abs(originY - destinyY);
            
        }
        
        //mediumEdgeSize = (double) countTotalEdgeSize / (double) (listAllEdges.size() );
        //Calculate de Stardart Deviation of the size of Edges
        if(stdDeviation)
            mediumEdgeSize = getDesvioPadrao(savedEdgeSize);
        else
            mediumEdgeSize = (double) countTotalEdgeSize / (double) (listAllEdges.size() );
        
        BigDecimal valorExato = new BigDecimal(mediumEdgeSize).setScale(2, RoundingMode.HALF_DOWN);
        mediumEdgeSize = Double.parseDouble(valorExato.toString());

        //tempoExecucaoEstatistica = System.currentTimeMillis() - tempoExecucaoEstatistica;
        //String minutos = String.format("%d min, %d sec", new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(tempoExecucaoEstatistica)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(tempoExecucaoEstatistica) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tempoExecucaoEstatistica))) });

        //System.out.println("Tempo de calculo do avgEdgeSize: " + minutos);
        
        return mediumEdgeSize;
    }
    
    
    public static double getDesvioPadrao(ArrayList<Integer> numArray)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();

        for(double num : numArray) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

   /**
    * Calculates the acceptance probability
    * @param currentDistance the total distance of the current tour
    * @param newDistance the total distance of the new tour
    * @param temperature the current temperature
    * @return value the probability of whether to accept the new tour
    */
   public static double acceptanceProbability(double currentDistance, double newDistance, double temperature) {
           // If the new solution is better, accept it
           if (newDistance < currentDistance) {
                   return 1.0;
           }
           // If the new solution is worse, calculate an acceptance probability
           return Math.exp((currentDistance - newDistance) / temperature);
   }

   /**
    * this method returns a random number n such that
    * 0.0 <= n <= 1.0
    * @return random such that 0.0 <= random <= 1.0
    */
   
   
   static double randomDouble()
   {
        Random r = new Random();
        //r.setSeed(1);
        return r.nextInt(1000) / 1000.0;
        //return r.nextDouble();
   }
   
   double generateDouble()
   {
       Random r = new Random(1);
       return r.nextDouble();
   }       
   

   /**
    * returns a random int value within a given range
    * min inclusive .. max not inclusive
    * @param min the minimum value of the required range (int)
    * @param max the maximum value of the required range (int)
    * @return rand a random int value between min and max [min,max)
    */ 
   public static int randomInt(int min , int max) {
           Random r = new Random();
           double d = min + r.nextDouble() * (max - min);
           return (int)d;
   }
   //Random Walker
   public void randomWalker(String id, int time, double p)
   {
       if(listAttNodes.isEmpty())
            getWeightEstruturalToInline(graphComponent);
       
       //Set all nodes i to empty, i.e. state[i, t]=E for all t
       Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
            if(att.isNode())
                att.setStateBusy(false);
            if(att.isEdge())
                att.setStateBusy(false);
       }
       
       // User chooses starting time t0 and starting node i0

       //Select random node
       //int max = listAttNodes.size();
       //int min = 0;
       //InlineNodeAttribute attNodeSelected = listAttNodes.get(randomInt(min,max));
       
       //Select random time
       //int time = randomInt(0, lastTime/4);
       
       //boolean oneNode = true;
        mxCell v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(id+" "+time);
        
        InlineNodeAttribute attNodeSelected = null;
        
        while(time < lastTime)
        {
            attNodeSelected = (InlineNodeAttribute) v1.getValue();
            
            //t = t+1
            time++;

            ArrayList<mxCell> adjactentNodesAfterTime = new ArrayList();
            ArrayList<String> idAdjactentNodesAfterTime = new ArrayList();

            //Verify SelectedNodes Edges with Time greater then T
            for(Object ed : listEdgesJgraph)
            {
                mxCell edge = (mxCell) ed;
                CustomAttributes att = (CustomAttributes) edge.getValue();
                if(edge.getSource().getId().equals(attNodeSelected.getId_original()+""))
                {
                    ArrayList<Integer> times = att.getTime();
                    for(Integer timeI : times)
                    {
                        if(timeI == time)
                        {
                            adjactentNodesAfterTime.add((mxCell) edge.getTarget());
                            idAdjactentNodesAfterTime.add(edge.getTarget().getId()+" "+timeI);
                            break;
                        }
                    }
                }
                else if(edge.getTarget().getId().equals(attNodeSelected.getId_original()+""))
                {
                    ArrayList<Integer> times = att.getTime();
                    for(Integer timeI : times)
                    {
                        if(timeI == time)
                        {
                            adjactentNodesAfterTime.add((mxCell) edge.getSource());
                            idAdjactentNodesAfterTime.add(edge.getSource().getId()+" "+timeI);
                            break;
                        }
                    }
                }
            }
            
            /*
            for(int i = time; i <= lastTime; i++)
            {
                //roots = graph.getChildCells(graph.getDefaultParent(), true, false);
                for (Object root1 : roots) {
                    Object[] root = {root1};
                    mxCell cell = (mxCell) root1;
                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                    if(att.isNode() && att.getTime() >= i)
                    {
                        
                    }
            }
            */
            
            /*
            System.out.println("nos adjacentes do id: "+attNodeSelected.getId_original()+ " "+(time-1));
            for(String a : idAdjactentNodesAfterTime)
            {
                System.out.println(a);
            }
            System.out.println("");
            */
            
            if(randomDouble() < p)
            {
                //time++;
            }
            else{
                //oneNode = false;
                if(!idAdjactentNodesAfterTime.isEmpty())
                {
                    
                    //Set the state of i0 to O, i.e. state[i0, t]=I
                    attNodeSelected.setStateBusy(true);
                    
                    //Get random adjactent node
                    int max = idAdjactentNodesAfterTime.size();
                    int min = 0;
                    String idRandomAdjacentNode = idAdjactentNodesAfterTime.get(randomInt(min,max));
                    mxCell v2 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(idRandomAdjacentNode);
                    InlineNodeAttribute attTargetNodeSelected = (InlineNodeAttribute) v2.getValue();
                    //Set the state of i0 to O, i.e. state[i0, t]=I
                    attTargetNodeSelected.setStateBusy(true);
                    
                    //Update v1 node status to busy until connects to v2
                    for(int x = attNodeSelected.getTime(); x <= attTargetNodeSelected.getTime(); x++)
                    {
                        mxCell node = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attNodeSelected.getId_original()+" "+x);
                        if(node != null)
                        {
                            InlineNodeAttribute attNode = (InlineNodeAttribute) node.getValue();
                            attNode.setStateBusy(true);
                        }
                    }
                    
                    //t = t+1
                    //time = attTargetNodeSelected.getTime();
                    
                    //Change edge to infected
                    mxCell edge = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attTargetNodeSelected.getTime()+" "+attNodeSelected.getId_original()+" "+attTargetNodeSelected.getId_original());
                    if(edge == null)
                    {
                        edge = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attTargetNodeSelected.getTime()+" "+attTargetNodeSelected.getId_original()+" "+attNodeSelected.getId_original());
                        InlineNodeAttribute attEdge = (InlineNodeAttribute) edge.getValue();
                        attEdge.setSourceDirectionInfection(true);
                    }
                    else
                    {
                        InlineNodeAttribute attEdge = (InlineNodeAttribute) edge.getValue();
                        attEdge.setSourceDirectionInfection(false);
                    }
                    
                    InlineNodeAttribute attEdge = (InlineNodeAttribute) edge.getValue();
                    attEdge.setStateBusy(true);
                    
                    v1 = v2;
                }
                else
                {
                    //Completes the node status to busy to the last node
                    /*for(int x = attNodeSelected.getTime(); x <= lastTime; x++)
                    {
                        mxCell node = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attNodeSelected.getId_original()+" "+x);
                        if(node != null)
                        {
                            InlineNodeAttribute attNode = (InlineNodeAttribute) node.getValue();
                            attNode.setStateBusy(true);
                        }
                    }
                    break;
                    */
                }
            }
        }
        
        //Completes the node status to busy to the last node
        for(int x = attNodeSelected.getTime(); x <= lastTime; x++)
        {
            mxCell node = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attNodeSelected.getId_original()+" "+x);
            if(node != null)
            {
                InlineNodeAttribute attNode = (InlineNodeAttribute) node.getValue();
                attNode.setStateBusy(true);
            }
        }
        
        //if(oneNode)
        //{
            attNodeSelected = (InlineNodeAttribute) v1.getValue();
            //Completes the node status to busy to the last node
            for(int x = attNodeSelected.getTime(); x <= lastTime; x++)
            {
                mxCell node = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attNodeSelected.getId_original()+" "+x);
                if(node != null)
                {
                    InlineNodeAttribute attNode = (InlineNodeAttribute) node.getValue();
                    attNode.setStateBusy(true);
                }
            }
       // }
        
        //Visualization
        //Coloring nodes according to Busy or Empty state
        graph.getModel().beginUpdate();
        
        roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
            if(att.isNode())
            {
                
                String[] colorsTable = new String[2];

                colorsTable[0] = "000 000 255"; //blue
                colorsTable[1] = "255 000 000"; //red

                int r = 0;
                int g = 0;
                int b = 0;
                
                String color2;
                String styleNode = cell.getStyle();
                if(att.isStateBusy())
                {
                    color2 = colorsTable[1];
                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=100;");
                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=100;");
                }
                else
                {
                    color2 = colorsTable[0];
                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=10;");
                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=10;");
                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=10;");
                }
                String[] tokens = color2.split(" ");
                r = Integer.parseInt(tokens[0]);
                g = Integer.parseInt(tokens[1]);
                b = Integer.parseInt(tokens[2]);

                Color colorNode = new Color(r,g,b);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                
                styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                //styleNode +=  mxConstants.STYLE_OPACITY+"=10;";
                

                cell.setStyle(styleNode);

            }
            else if(att.isEdge())
            {
                
                
                String[] colorsTable = new String[2];

                colorsTable[0] = "000 000 255"; //blue
                colorsTable[1] = "255 000 000"; //red

                int r = 0;
                int g = 0;
                int b = 0;
                
                String styleEdge = cell.getStyle();
                if(att.isStateBusy())
                {
                    String color2 = colorsTable[1];
                    
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=100;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=100;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=100;");
                    
                    String[] tokens = color2.split(" ");
                    r = Integer.parseInt(tokens[0]);
                    g = Integer.parseInt(tokens[1]);
                    b = Integer.parseInt(tokens[2]);

                    Color colorNodeMin = new Color(r,g,b);
                    String hexColorMin = "#"+Integer.toHexString(colorNodeMin.getRGB()).substring(2);

                    color2 = colorsTable[0];
                    
                    
                    tokens = color2.split(" ");
                    r = Integer.parseInt(tokens[0]);
                    g = Integer.parseInt(tokens[1]);
                    b = Integer.parseInt(tokens[2]);

                    
                    Color colorNodeMax = new Color(r,g,b);
                    String hexColorMax = "#"+Integer.toHexString(colorNodeMax.getRGB()).substring(2);

                    
                    String styleShapeInline = cell.getStyle();
                    //styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColorMax+";");
                    //styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColorMin+";");
                    
                    cell.setStyle(styleShapeInline);
                    
                    if(att.isSourceDirectionInfection())
                    {
                        styleEdge +=  mxConstants.STYLE_FILLCOLOR+"="+hexColorMin+";";
                        styleEdge +=  mxConstants.STYLE_GRADIENTCOLOR+"="+hexColorMin+";";
                    }
                    else
                    {
                        styleEdge +=  mxConstants.STYLE_FILLCOLOR+"="+hexColorMin+";";
                        styleEdge +=  mxConstants.STYLE_GRADIENTCOLOR+"="+hexColorMin+";";
                    }
                    //styleNode +=  mxConstants.STYLE_OPACITY+"=10;";

                    
                    Object[] a = new Object[1];
                    a[0] = cell;
                    graph.cellsOrdered(a,true);



                }
                else
                {
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=0;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=0;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=0;");
                    
                    String color2 = colorsTable[0];
                    
                    String[] tokens = color2.split(" ");
                    r = Integer.parseInt(tokens[0]);
                    g = Integer.parseInt(tokens[1]);
                    b = Integer.parseInt(tokens[2]);

                    Color colorNode = new Color(r,g,b);
                    String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                    
                    styleEdge +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleEdge +=  mxConstants.STYLE_GRADIENTCOLOR+"="+hexColor+";";
                }
                
                cell.setStyle(styleEdge);
            }
        }
        
        graph.getModel().endUpdate();
       graphComponent.refresh();
   }
   
   //Epidemiology
   public void infectionDynamics(String id, int time, int tr, double p, int seed, boolean changeOrdering, String model, double lossImmunity)
   {
       
       
       if(model.equals("SIRS") || model.equals("SIR") || model.equals("SI") || model.equals("SIS"))
       {
           
           if(model.equals("SIR"))
               lossImmunity = 0;
           
           if(model.equals("SI"))
               tr = lastTime + 1;

            Random ran = new Random(seed);
            Random ran2 = new Random(seed);
             //User defines recovery time tr (integer number) and infection probability p -> p =[0,1] (real number)
             //int tr = 10;
             //double p = randomDouble();
             //double p = 0.9;
             //Set all nodes i to state S, i.e. state[i, t]=S for all t
             //1 - (S)usceptible
             //2 - (I)nfected
             //3 - (R)ecovered
             int nodesCount = lineNodes.size();

             Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
             for (Object root1 : roots) {
                 mxCell cell = (mxCell) root1;
                 InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                 if(att.isNode())
                 {
                     att.setStateInfection(1); //1 - (S)usceptible
                 }
                 if(att.isEdge())
                     att.setStateBusy(false);

             }

             HashMap<Integer,List<InlineNodeAttribute>> sequenciaDeEntrada = new HashMap();
             Integer positionTop = nodesCount/2, positionBottom = nodesCount/2;

             /*
             3. User chooses starting time t0 and starting node i0
                 3.1. t = t0
                 3.2. Set the state of i0 to I, i.e. state[i0, t]=I
                 3.3. Set the clock of i0 to tr[i] = tr
                 3.4. t = t+1
            */

             mxCell v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(id+" "+time);
             InlineNodeAttribute attNodeSelected = (InlineNodeAttribute) v1.getValue();
             attNodeSelected.setStateInfection(2); //2 - (I)nfected

             HashMap<String,Integer> mapClockNodes = new HashMap<>();
             mapClockNodes.put(attNodeSelected.getId_original()+"", tr);
             //for(Integer node : lineNodes)
             //{
             //    mapClockNodes.put(node+"", tr);
             //}
             time++;

             //List<InlineNodeAttribute> msvReordering = new ArrayList();

             InlineNodeAttribute[] array= new InlineNodeAttribute[nodesCount];
             array[nodesCount/2]= attNodeSelected;
             ArrayList<Integer> idNodesInfected = new ArrayList();
             idNodesInfected.add(attNodeSelected.getId_original());

             //msvReordering.add(nodesCount/2, attNodeSelected);
             positionTop--;
             positionBottom++;
             boolean isTop = true;


             /*
             4. At each time step t
                 4.1. For each node i
                   4.1.1. If state[i, t-1]=I      -> se o vertice estiver infectado no tempo t-1, ele pode infectar um amigo no tempo t
                     4.1.1.1. Check all contacts (i,j)
                        4.1.1.1.1 If state[j, t-1]=S & RAND(0,1) < p       -> RAND(0,1) = uniform real random number between [0,1]
                           4.1.1.1.1.1 Set the state of node j to I, i.e. state[j, t] = I
                           4.1.1.1.1.2 Set the clock of node j to tr[j] = tr
                     4.1.1.2. tr[i] = tr[i] - 1
                     4.1.1.3. if tr[i] = 0
                        4.1.1.3.1 Set the state of node i to R, i.e. state[i, t] = R   -> the state R will not change from this time
                   4.1.2 elseif state[i, t-1] = R
                     4.1.2.1 state[i, t] = R
             */
             
            ArrayList<String> recoveredNodes = new ArrayList();
            ArrayList<String> removedRecovered = new ArrayList();
             for(int i = time; i <= lastTime; i++)
             {

                 
                 
                 ArrayList<String> clockRemoved = new ArrayList();
                 
                 //ArrayList<String> removedClock = new ArrayList();
                 for(Entry<String,Integer> clock : mapClockNodes.entrySet())
                 {
                     int tempTr = clock.getValue() - 1;
                     if(tempTr >= 0)
                     {
                        mapClockNodes.put(clock.getKey(), tempTr); //updating the map with clocks
                         mxCell clockNode2 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(clock.getKey()+" "+i);
                        if(clockNode2 != null)
                        {
                            InlineNodeAttribute attClockNode2 = (InlineNodeAttribute) clockNode2.getValue();
                            attClockNode2.setStateInfection(2); //2 - (I)nfected
                        }
                     }
                     if(tempTr == 0)
                     {
                        
                        for(int x = i; x <= lastTime; x++)
                        {
                            mxCell clockNode2 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(clock.getKey()+" "+x);
                            if(clockNode2 != null)
                            {
                                InlineNodeAttribute attClockNode2 = (InlineNodeAttribute) clockNode2.getValue();

                                if(model.equals("SIS"))
                                {
                                    attClockNode2.setStateInfection(1); //1 - (S)usceptible
                                }
                                else
                                {
                                    attClockNode2.setStateInfection(3); //3 - (R)ecovered
                                    if(!recoveredNodes.contains(clock.getKey()))
                                        recoveredNodes.add(clock.getKey());
                                } 
                            }
                        }
                        
                        clockRemoved.add(clock.getKey());
                         
                     }
                 }
                 
                 for(String clock : clockRemoved)
                 {
                    mapClockNodes.remove(clock);
                 }
                 
                 //Probability of a recover node to be susceptible again
                 
                for(String nodes : recoveredNodes)
                 {
                    double r = ran2.nextDouble();
                    if(r < lossImmunity)
                    {
                        for(int x = i; x <= lastTime; x++)
                        {
                           mxCell verifyInfection = (mxCell) ((mxGraphModel)graph.getModel()).getCell(nodes+" "+x);
                           if(verifyInfection != null)
                           {
                                InlineNodeAttribute att2 = (InlineNodeAttribute) verifyInfection.getValue();
                                att2.setStateInfection(1); //1 - (S)usceptible
                                removedRecovered.add(nodes);
                                //break;
                           }

                        }
                    }
                 }
                 recoveredNodes.removeAll(removedRecovered);
                

                 //roots = graph.getChildCells(graph.getDefaultParent(), true, false);
                 for (Object root1 : roots) {
                     Object[] root = {root1};
                     mxCell cell = (mxCell) root1;
                     InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                     if(att.isNode() && att.getTime() >= i && att.getStateInfection() == 2)
                     {

                         //boolean infectMoreNodes = false;
                         for(int x = i; x >= 0; x--)
                         {
                            mxCell verifyInfection = (mxCell) ((mxGraphModel)graph.getModel()).getCell(att.getId_original()+" "+x);
                            if(verifyInfection != null)
                            {
                                InlineNodeAttribute attInfection = (InlineNodeAttribute) verifyInfection.getValue();
                                if(attInfection.getStateInfection() == 2) //2 - (I)nfected
                                {
                                    att.setStateInfection(2); //2 - (I)nfected
                                    /*
                                     4.1.1.1. Check all contacts (i,j)
                                         4.1.1.1.1 If state[j, t-1]=S & RAND(0,1) < p       -> RAND(0,1) = uniform real random number between [0,1]
                                            4.1.1.1.1.1 Set the state of node j to I, i.e. state[j, t] = I
                                            4.1.1.1.1.2 Set the clock of node j to tr[j] = tr
                                     */

                                     ArrayList<mxCell> listNodesThatConnectsWithSelected = new ArrayList();

                                     //Verify SelectedNode Edges with Time equals T
                                     for(Object ed : listEdgesJgraph)
                                     {
                                         mxCell edge = (mxCell) ed;
                                         CustomAttributes attEdge = (CustomAttributes) edge.getValue();
                                         if(edge.getSource().getId().equals(att.getId_original()+""))
                                         {
                                             ArrayList<Integer> times = attEdge.getTime();
                                             for(Integer timeI : times)
                                             {
                                                 if(timeI == i)
                                                 {
                                                     mxCell target = (mxCell) ((mxGraphModel)graph.getModel()).getCell(edge.getTarget().getId()+" "+timeI);
                                                     listNodesThatConnectsWithSelected.add(target);
                                                 }
                                             }
                                         }
                                         if(edge.getTarget().getId().equals(att.getId_original()+""))
                                         {
                                             ArrayList<Integer> times = attEdge.getTime();
                                             for(Integer timeI : times)
                                             {
                                                 if(timeI == i)
                                                 {
                                                     mxCell source = (mxCell) ((mxGraphModel)graph.getModel()).getCell(edge.getSource().getId()+" "+timeI);
                                                     listNodesThatConnectsWithSelected.add(source);
                                                 }
                                             }
                                         }

                                     }

                                     for(mxCell possibleInfectedNode : listNodesThatConnectsWithSelected)
                                     {
                                         InlineNodeAttribute attPossibleInfection = (InlineNodeAttribute) possibleInfectedNode.getValue();

                                         double r = ran.nextDouble();
                                         //System.out.println(r);
                                         if(attPossibleInfection.getStateInfection() == 1 && (r < p))
                                         {
                                             if(isTop)
                                             {
                                                 if(!idNodesInfected.contains(attPossibleInfection.getId_original()))
                                                 {
                                                     isTop = false;
                                                     array[positionTop]= attPossibleInfection;
                                                     idNodesInfected.add(attPossibleInfection.getId_original());
                                                     //msvReordering.add(positionTop, attPossibleInfection);
                                                     positionTop--;
                                                 }
                                             }
                                             else
                                             {
                                                 if(!idNodesInfected.contains(attPossibleInfection.getId_original()))
                                                 {
                                                     isTop = true;
                                                     array[positionBottom]= attPossibleInfection;
                                                     idNodesInfected.add(attPossibleInfection.getId_original());
                                                     //msvReordering.add(positionBottom, attPossibleInfection);
                                                     positionBottom++;
                                                 }
                                             }


                                             //Change edge to infected
                                             mxCell edge = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attPossibleInfection.getTime()+" "+att.getId_original()+" "+attPossibleInfection.getId_original());
                                             if(edge == null)
                                                 edge = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attPossibleInfection.getTime()+" "+attPossibleInfection.getId_original()+" "+att.getId_original());
                                             InlineNodeAttribute attEdge = (InlineNodeAttribute) edge.getValue();
                                             attEdge.setStateBusy(true);


                                             attPossibleInfection.setStateInfection(2);
                                             mapClockNodes.put(attPossibleInfection.getId_original()+"", tr);

                                         }
                                     }

                                     break;
                                }

                             }


                         }

                      }
                 }


                }



                //Visualization
                //Coloring nodes according to (S)usceptible, (I)nfected or (R)ecovered state
                //1 - (S)usceptible
                //2 - (I)nfected
                //3 - (R)ecovered
                graph.getModel().beginUpdate();

                roots = graph.getChildCells(graph.getDefaultParent(), true, false);
                for (Object root1 : roots) {
                    Object[] root = {root1};
                    mxCell cell = (mxCell) root1;
                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                    if(att.isNode())
                    {

                        String[] colorsTable = new String[3];

                        colorsTable[0] = "000 000 255"; //blue    1 - (S)usceptible
                        colorsTable[1] = "255 000 000"; //red     2 - (I)nfected
                        colorsTable[2] = "255 255 000"; //yellow  3 - (R)ecovered

                        int r = 0;
                        int g = 0;
                        int b = 0;

                        String color2 = colorsTable[att.getStateInfection()-1];
                        String[] tokens = color2.split(" ");
                        r = Integer.parseInt(tokens[0]);
                        g = Integer.parseInt(tokens[1]);
                        b = Integer.parseInt(tokens[2]);

                        Color colorNode = new Color(r,g,b);
                        String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                        String styleNode = cell.getStyle();
                        styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                        styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

                        //if(att.getStateInfection() == 2)
                            //styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                        //else
                            styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                        //styleNode +=  mxConstants.STYLE_OPACITY+"=10;";



                        Object[] a = new Object[1];
                        a[0] = cell;
                        graph.cellsOrdered(a,false);

                        cell.setStyle(styleNode);

                    }
                    if(att.isEdge())
                    {

                        String[] colorsTable = new String[2];

                        colorsTable[0] = "000 000 255"; //blue
                        colorsTable[1] = "255 000 000"; //red

                        int r = 0;
                        int g = 0;
                        int b = 0;

                        String styleEdge = cell.getStyle();
                        if(att.isStateBusy())
                        {
                            String color2 = colorsTable[1];

                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=50;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=50;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=50;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=50;");

                            String[] tokens = color2.split(" ");
                            r = Integer.parseInt(tokens[0]);
                            g = Integer.parseInt(tokens[1]);
                            b = Integer.parseInt(tokens[2]);

                            Color colorNode = new Color(r,g,b);
                            String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);


                            styleEdge +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                            styleEdge +=  mxConstants.STYLE_GRADIENTCOLOR+"="+hexColor+";";
                            //styleNode +=  mxConstants.STYLE_OPACITY+"=10;";


                            Object[] a = new Object[1];
                            a[0] = cell;
                            graph.cellsOrdered(a,true);



                        }
                        else
                        {
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=75;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=0;");

                            String color2 = colorsTable[0];

                            String[] tokens = color2.split(" ");
                            r = Integer.parseInt(tokens[0]);
                            g = Integer.parseInt(tokens[1]);
                            b = Integer.parseInt(tokens[2]);

                            Color colorNode = new Color(r,g,b);
                            String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);



                            styleEdge +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                            styleEdge +=  mxConstants.STYLE_GRADIENTCOLOR+"="+hexColor+";";
                        }

                        cell.setStyle(styleEdge);
                    }
                }

                graph.getModel().endUpdate();
                graphComponent.refresh();

                //Changing Reorder infection dynamics sequence 

                for (Object root1 : roots) {
                        Object[] root = {root1};
                        mxCell cell = (mxCell) root1;
                        InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                        if(att.isNode() && !idNodesInfected.contains(att.getId_original()))
                        {
                            idNodesInfected.add(att.getId_original());
                            for(int i = 0; i < array.length;i++)
                            {
                                if(array[i] == null)
                                {
                                    array[i] = att;
                                    break;
                                }
                            }
                        }
                }

                List<InlineNodeAttribute> listMsvReordering= Arrays.asList(array);
                sequenciaDeEntrada.put(0, listMsvReordering);
                if(changeOrdering)
                    visualizaNos(sequenciaDeEntrada);
       }
       else if(model.equals("SIR_old")) //Version published on the bookchapter
       {

            Random ran = new Random(seed);
             //User defines recovery time tr (integer number) and infection probability p -> p =[0,1] (real number)
             //int tr = 10;
             //double p = randomDouble();
             //double p = 0.9;
             //Set all nodes i to state S, i.e. state[i, t]=S for all t
             //1 - (S)usceptible
             //2 - (I)nfected
             //3 - (R)ecovered
             int nodesCount = lineNodes.size();

             Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
             for (Object root1 : roots) {
                 mxCell cell = (mxCell) root1;
                 InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                 if(att.isNode())
                 {
                     att.setStateInfection(1); //1 - (S)usceptible
                 }
                 if(att.isEdge())
                     att.setStateBusy(false);

             }

             HashMap<Integer,List<InlineNodeAttribute>> sequenciaDeEntrada = new HashMap();
             Integer positionTop = nodesCount/2, positionBottom = nodesCount/2;

             /*
             3. User chooses starting time t0 and starting node i0
                 3.1. t = t0
                 3.2. Set the state of i0 to I, i.e. state[i0, t]=I
                 3.3. Set the clock of i0 to tr[i] = tr
                 3.4. t = t+1
            */

             mxCell v1 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(id+" "+time);
             InlineNodeAttribute attNodeSelected = (InlineNodeAttribute) v1.getValue();
             attNodeSelected.setStateInfection(2); //2 - (I)nfected

             HashMap<String,Integer> mapClockNodes = new HashMap<>();
             mapClockNodes.put(attNodeSelected.getId_original()+"", tr);
             //for(Integer node : lineNodes)
             //{
             //    mapClockNodes.put(node+"", tr);
             //}
             time++;

             //List<InlineNodeAttribute> msvReordering = new ArrayList();

             InlineNodeAttribute[] array= new InlineNodeAttribute[nodesCount];
             array[nodesCount/2]= attNodeSelected;
             ArrayList<Integer> idNodesInfected = new ArrayList();
             idNodesInfected.add(attNodeSelected.getId_original());

             //msvReordering.add(nodesCount/2, attNodeSelected);
             positionTop--;
             positionBottom++;
             boolean isTop = true;


             /*
             4. At each time step t
                 4.1. For each node i
                   4.1.1. If state[i, t-1]=I      -> se o vertice estiver infectado no tempo t-1, ele pode infectar um amigo no tempo t
                     4.1.1.1. Check all contacts (i,j)
                        4.1.1.1.1 If state[j, t-1]=S & RAND(0,1) < p       -> RAND(0,1) = uniform real random number between [0,1]
                           4.1.1.1.1.1 Set the state of node j to I, i.e. state[j, t] = I
                           4.1.1.1.1.2 Set the clock of node j to tr[j] = tr
                     4.1.1.2. tr[i] = tr[i] - 1
                     4.1.1.3. if tr[i] = 0
                        4.1.1.3.1 Set the state of node i to R, i.e. state[i, t] = R   -> the state R will not change from this time
                   4.1.2 elseif state[i, t-1] = R
                     4.1.2.1 state[i, t] = R
             */

             for(int i = time; i <= lastTime; i++)
             {


                 //ArrayList<String> removedClock = new ArrayList();
                 for(Entry<String,Integer> clock : mapClockNodes.entrySet())
                 {
                     int tempTr = clock.getValue() - 1;
                     if(tempTr >= 0)
                         mapClockNodes.put(clock.getKey(), tempTr); //updating the map with clocks
                     if(tempTr == 0)
                     {
                         mxCell clockNode = (mxCell) ((mxGraphModel)graph.getModel()).getCell(clock.getKey()+" "+i);
                         if(clockNode != null)
                         {
                             InlineNodeAttribute attClockNode = (InlineNodeAttribute) clockNode.getValue();
                             attClockNode.setStateInfection(3); //3 - (R)ecovered
                             //removedClock.add(clock.getKey());
                         }
                         else
                         {
                             for(int x = i; x <= lastTime; x++)
                             {
                                 mxCell clockNode2 = (mxCell) ((mxGraphModel)graph.getModel()).getCell(clock.getKey()+" "+x);
                                 if(clockNode2 != null)
                                 {
                                     InlineNodeAttribute attClockNode2 = (InlineNodeAttribute) clockNode2.getValue();
                                     attClockNode2.setStateInfection(3); //3 - (R)ecovered
                                     //break;
                                 }
                             }
                         }
                     }
                 }
                 //for(String clock : removedClock)
                 //{
                 //    mapClockNodes.remove(clock);
                 //}


                 //roots = graph.getChildCells(graph.getDefaultParent(), true, false);
                 for (Object root1 : roots) {
                     Object[] root = {root1};
                     mxCell cell = (mxCell) root1;
                     InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                     if(att.isNode() && att.getTime() >= i)
                     {

                         if(mapClockNodes.get(att.getId_original()+"") != null) //3 - (R)ecovered
                         {
                             if(mapClockNodes.get(att.getId_original()+"") == 0)
                             {
                                  att.setStateInfection(3); //3 - (R)ecovered
                                  continue;
                             }
                         }

                         //boolean infectMoreNodes = false;
                         for(int x = i; x >= 0; x--)
                         {
                            mxCell verifyInfection = (mxCell) ((mxGraphModel)graph.getModel()).getCell(att.getId_original()+" "+x);
                            if(verifyInfection != null)
                            {
                                InlineNodeAttribute attInfection = (InlineNodeAttribute) verifyInfection.getValue();
                                if(attInfection.getStateInfection() == 2) //2 - (I)nfected
                                {
                                    att.setStateInfection(2); //2 - (I)nfected
                                    /*
                                     4.1.1.1. Check all contacts (i,j)
                                         4.1.1.1.1 If state[j, t-1]=S & RAND(0,1) < p       -> RAND(0,1) = uniform real random number between [0,1]
                                            4.1.1.1.1.1 Set the state of node j to I, i.e. state[j, t] = I
                                            4.1.1.1.1.2 Set the clock of node j to tr[j] = tr
                                     */

                                     ArrayList<mxCell> listNodesThatConnectsWithSelected = new ArrayList();

                                     //Verify SelectedNode Edges with Time equals T
                                     for(Object ed : listEdgesJgraph)
                                     {
                                         mxCell edge = (mxCell) ed;
                                         CustomAttributes attEdge = (CustomAttributes) edge.getValue();
                                         if(edge.getSource().getId().equals(att.getId_original()+""))
                                         {
                                             ArrayList<Integer> times = attEdge.getTime();
                                             for(Integer timeI : times)
                                             {
                                                 if(timeI == i)
                                                 {
                                                     mxCell target = (mxCell) ((mxGraphModel)graph.getModel()).getCell(edge.getTarget().getId()+" "+timeI);
                                                     listNodesThatConnectsWithSelected.add(target);
                                                 }
                                             }
                                         }
                                         if(edge.getTarget().getId().equals(att.getId_original()+""))
                                         {
                                             ArrayList<Integer> times = attEdge.getTime();
                                             for(Integer timeI : times)
                                             {
                                                 if(timeI == i)
                                                 {
                                                     mxCell source = (mxCell) ((mxGraphModel)graph.getModel()).getCell(edge.getSource().getId()+" "+timeI);
                                                     listNodesThatConnectsWithSelected.add(source);
                                                 }
                                             }
                                         }

                                     }

                                     for(mxCell possibleInfectedNode : listNodesThatConnectsWithSelected)
                                     {
                                         InlineNodeAttribute attPossibleInfection = (InlineNodeAttribute) possibleInfectedNode.getValue();

                                         double r = ran.nextDouble();
                                         //System.out.println(r);
                                         if(attPossibleInfection.getStateInfection() == 1 && (r < p))
                                         {
                                             if(isTop)
                                             {
                                                 if(!idNodesInfected.contains(attPossibleInfection.getId_original()))
                                                 {
                                                     isTop = false;
                                                     array[positionTop]= attPossibleInfection;
                                                     idNodesInfected.add(attPossibleInfection.getId_original());
                                                     //msvReordering.add(positionTop, attPossibleInfection);
                                                     positionTop--;
                                                 }
                                             }
                                             else
                                             {
                                                 if(!idNodesInfected.contains(attPossibleInfection.getId_original()))
                                                 {
                                                     isTop = true;
                                                     array[positionBottom]= attPossibleInfection;
                                                     idNodesInfected.add(attPossibleInfection.getId_original());
                                                     //msvReordering.add(positionBottom, attPossibleInfection);
                                                     positionBottom++;
                                                 }
                                             }


                                             //Change edge to infected
                                             mxCell edge = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attPossibleInfection.getTime()+" "+att.getId_original()+" "+attPossibleInfection.getId_original());
                                             if(edge == null)
                                                 edge = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attPossibleInfection.getTime()+" "+attPossibleInfection.getId_original()+" "+att.getId_original());
                                             InlineNodeAttribute attEdge = (InlineNodeAttribute) edge.getValue();
                                             attEdge.setStateBusy(true);


                                             attPossibleInfection.setStateInfection(2);
                                             mapClockNodes.put(attPossibleInfection.getId_original()+"", tr);

                                         }
                                     }

                                    /*
                                        4.1.1.2. tr[i] = tr[i] - 1
                                           4.1.1.3. if tr[i] = 0
                                              4.1.1.3.1 Set the state of node i to R, i.e. state[i, t] = R   -> the state R will not change from this time
                                     */

                                    /*if(mapClockNodes.get(att.getId_original()+"") != null)
                                     {
                                         int tempTr = mapClockNodes.get(att.getId_original()+"");
                                         tempTr--;
                                         if(tempTr >= 0)
                                             mapClockNodes.put(att.getId_original()+"", tempTr);
                                         if(tempTr == 0)
                                             att.setStateInfection(3); //3 - (R)ecovered
                                     }
                                     */
                                     break;
                                }

                             }


                         }

                      }
                 }


                }



                //Visualization
                //Coloring nodes according to (S)usceptible, (I)nfected or (R)ecovered state
                //1 - (S)usceptible
                //2 - (I)nfected
                //3 - (R)ecovered
                graph.getModel().beginUpdate();

                roots = graph.getChildCells(graph.getDefaultParent(), true, false);
                for (Object root1 : roots) {
                    Object[] root = {root1};
                    mxCell cell = (mxCell) root1;
                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                    if(att.isNode())
                    {

                        String[] colorsTable = new String[3];

                        colorsTable[0] = "000 000 255"; //blue    1 - (S)usceptible
                        colorsTable[1] = "255 000 000"; //red     2 - (I)nfected
                        colorsTable[2] = "255 255 000"; //yellow  3 - (R)ecovered

                        int r = 0;
                        int g = 0;
                        int b = 0;

                        String color2 = colorsTable[att.getStateInfection()-1];
                        String[] tokens = color2.split(" ");
                        r = Integer.parseInt(tokens[0]);
                        g = Integer.parseInt(tokens[1]);
                        b = Integer.parseInt(tokens[2]);

                        Color colorNode = new Color(r,g,b);
                        String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                        String styleNode = cell.getStyle();
                        styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                        styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

                        //if(att.getStateInfection() == 2)
                            //styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                        //else
                            styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                        //styleNode +=  mxConstants.STYLE_OPACITY+"=10;";



                        Object[] a = new Object[1];
                        a[0] = cell;
                        graph.cellsOrdered(a,false);

                        cell.setStyle(styleNode);

                    }
                    if(att.isEdge())
                    {

                        String[] colorsTable = new String[2];

                        colorsTable[0] = "000 000 255"; //blue
                        colorsTable[1] = "255 000 000"; //red

                        int r = 0;
                        int g = 0;
                        int b = 0;

                        String styleEdge = cell.getStyle();
                        if(att.isStateBusy())
                        {
                            String color2 = colorsTable[1];

                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=50;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=50;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=50;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=50;");

                            String[] tokens = color2.split(" ");
                            r = Integer.parseInt(tokens[0]);
                            g = Integer.parseInt(tokens[1]);
                            b = Integer.parseInt(tokens[2]);

                            Color colorNode = new Color(r,g,b);
                            String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);


                            styleEdge +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                            styleEdge +=  mxConstants.STYLE_GRADIENTCOLOR+"="+hexColor+";";
                            //styleNode +=  mxConstants.STYLE_OPACITY+"=10;";


                            Object[] a = new Object[1];
                            a[0] = cell;
                            graph.cellsOrdered(a,true);



                        }
                        else
                        {
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=75;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=0;");
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=0;");

                            String color2 = colorsTable[0];

                            String[] tokens = color2.split(" ");
                            r = Integer.parseInt(tokens[0]);
                            g = Integer.parseInt(tokens[1]);
                            b = Integer.parseInt(tokens[2]);

                            Color colorNode = new Color(r,g,b);
                            String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);



                            styleEdge +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                            styleEdge +=  mxConstants.STYLE_GRADIENTCOLOR+"="+hexColor+";";
                        }

                        cell.setStyle(styleEdge);
                    }
                }

                graph.getModel().endUpdate();
                graphComponent.refresh();

                //Changing Reorder infection dynamics sequence 

                for (Object root1 : roots) {
                        Object[] root = {root1};
                        mxCell cell = (mxCell) root1;
                        InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();

                        if(att.isNode() && !idNodesInfected.contains(att.getId_original()))
                        {
                            idNodesInfected.add(att.getId_original());
                            for(int i = 0; i < array.length;i++)
                            {
                                if(array[i] == null)
                                {
                                    array[i] = att;
                                    break;
                                }
                            }
                        }
                }

                List<InlineNodeAttribute> listMsvReordering= Arrays.asList(array);
                sequenciaDeEntrada.put(0, listMsvReordering);
                if(changeOrdering)
                    visualizaNos(sequenciaDeEntrada);
       }
        
   }
   
   
   boolean changeVisualinfectionDynamics = true;
   public void infectionDynamicsChangeVisualization()
   {
       
       if(changeVisualinfectionDynamics)
           changeVisualinfectionDynamics = false;
       else
           changeVisualinfectionDynamics = true;
       
       graph.getModel().beginUpdate();
        
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
            if(att.isNode())
            {
                
                String[] colorsTable = new String[3];

                colorsTable[0] = "000 000 255"; //blue    1 - (S)usceptible
                colorsTable[1] = "255 000 000"; //red     2 - (I)nfected
                colorsTable[2] = "255 255 000"; //yellow  3 - (R)ecovered

                int r = 0;
                int g = 0;
                int b = 0;
                
                String color2 = colorsTable[att.getStateInfection()-1];
                String[] tokens = color2.split(" ");
                r = Integer.parseInt(tokens[0]);
                g = Integer.parseInt(tokens[1]);
                b = Integer.parseInt(tokens[2]);

                Color colorNode = new Color(r,g,b);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleNode = cell.getStyle();
                styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
               
                if(changeVisualinfectionDynamics)
                {
                    if(att.getStateInfection() == 2)
                       styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                    else
                        styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=10;");
                }
                else
                {
                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                }
                
                Object[] a = new Object[1];
                a[0] = cell;
                graph.cellsOrdered(a,false);

                cell.setStyle(styleNode);

            }
            if(att.isEdge())
            {
                
                String[] colorsTable = new String[2];

                colorsTable[0] = "000 000 255"; //blue
                colorsTable[1] = "255 000 000"; //red

                int r = 0;
                int g = 0;
                int b = 0;
                
                String styleEdge = cell.getStyle();
                if(att.isStateBusy())
                {
                    String color2 = colorsTable[1];
                    
                    
                    if(changeVisualinfectionDynamics)
                    {
                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=50;");
                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=50;");
                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=50;");
                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=50;");
                    }
                    else
                    {
                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=0;");
                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=0;");
                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=0;");
                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=0;");
                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=0;");
                    }
                    
                    
                    
                    String[] tokens = color2.split(" ");
                    r = Integer.parseInt(tokens[0]);
                    g = Integer.parseInt(tokens[1]);
                    b = Integer.parseInt(tokens[2]);

                    Color colorNode = new Color(r,g,b);
                    String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);


                    styleEdge +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleEdge +=  mxConstants.STYLE_GRADIENTCOLOR+"="+hexColor+";";
                    //styleNode +=  mxConstants.STYLE_OPACITY+"=10;";

                    
                    Object[] a = new Object[1];
                    a[0] = cell;
                    graph.cellsOrdered(a,true);



                }
                else
                {
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=0;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=0;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=75;",mxConstants.STYLE_OPACITY+"=0;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=0;");
                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=15;",mxConstants.STYLE_OPACITY+"=0;");
                    
                    String color2 = colorsTable[0];
                    
                    String[] tokens = color2.split(" ");
                    r = Integer.parseInt(tokens[0]);
                    g = Integer.parseInt(tokens[1]);
                    b = Integer.parseInt(tokens[2]);

                    Color colorNode = new Color(r,g,b);
                    String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                    
                    
                    styleEdge +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleEdge +=  mxConstants.STYLE_GRADIENTCOLOR+"="+hexColor+";";
                }
                
                cell.setStyle(styleEdge);
            }
        }
        
        graph.getModel().endUpdate();
        graphComponent.refresh();
   }
   
   ArrayList<Double> qtdArestasEmCadaTimestampEOD = null;
   ArrayList<Double> qtdArestasEmCadaTimestampNone = null;
   ArrayList<Double> qtdArestasEmCadaTimestampRandom = null;
   
   int kNodes = 0;
   
   
   public ArrayList edgeSampling(String method, String numberNodes)
   {
       ArrayList retorno = new ArrayList<>();
       this.nodesCount = Integer.parseInt(numberNodes);
       ArrayList<Double> qtdArestasEmCadaTimestamp = null;
       ArrayList<Double> qtdArestasEmCadaTimestampNone = null;
       mxCell ed = (mxCell) listAllEdges.get(0);
       InlineNodeAttribute attE = (InlineNodeAttribute) ed.getValue();
       minTime = attE.getTime();
       
       ed = (mxCell) listAllEdges.get(listAllEdges.size()-1);
       attE = (InlineNodeAttribute) ed.getValue();
       maxTime = attE.getTime();
       String linegraphColor = "#000000";
       
       int qtdNodesSample;
       
         Map<Integer, Integer> sortedMapAsc = sortByComparator(currentTemporalNodeOrder, true);
         List<Integer> posicaoAtualNosLayout = new ArrayList<>(sortedMapAsc.keySet());
       
       qtdArestasEmCadaTimestampNone = showAllEdges();
       
       switch (method) {
           
           case "None":
               qtdArestasEmCadaTimestamp = showAllEdges();
               linegraphColor = "#0000FF";
               break;
            case "Random":
                qtdArestasEmCadaTimestamp = randomEdgeSampling();
                linegraphColor = "#00FF00";
                break;
            case "AR":
                qtdArestasEmCadaTimestamp = AREdgeSampling();
                linegraphColor = "#8d69c7"; //roxo
                break;
            case "EOD":
                //qtdArestasEmCadaTimestampNone = AREdgeSampling();+
                
                //Tem um bug ao mudar a cor dos nós/arestas após o sampling. O código atual torna visível os nós ocultados pelo sampling. Mexer no método changeColorInline
                qtdArestasEmCadaTimestamp = EODEdgeSampling();
              
                linegraphColor = "#FF0000";
                break;
            
            default:
                break;
                
       }
       
        
       graphComponent.refresh();
       return retorno;
       
   }
   
   
   public ArrayList<mxCell> getListEdgesBetweenTwoNodes(int node, int node2)
   {
       
       ArrayList<mxCell> listEdges = new ArrayList();
       
       Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            
            if(att.isEdge())
            {
                if ( (att.getOrigin() == node && att.getDestiny() == node2)  || (att.getDestiny() == node && att.getOrigin() == node2))
                {
                    listEdges.add(cell);
                }
            }
        }
       
       return listEdges;
   }
   
   public double calculatef(int sigma, ArrayList<mxCell> edgesBetweenTwoNodes, int t)
   {
        double fx = (1/(sigma*Math.sqrt(2*Math.PI)));
        double somatorio = 0;

        //4: Compute the target PDF f (vp; vq; t) by using KDE
        for(int i=0; i < edgesBetweenTwoNodes.size(); i++)
        {
            mxCell edgeA = (mxCell) edgesBetweenTwoNodes.get(i);
            InlineNodeAttribute attA = (InlineNodeAttribute) edgeA.getValue();

            somatorio += Math.pow(Math.E, -(Math.pow(t - attA.getTime(), 2))/(2*Math.pow(sigma, 2)));

        }

        return fx *= somatorio;
   }
   
    private ArrayList<Double> AREdgeSampling()
    {
        int sigma = 5;
        double F = 2;
        
        
        int arestas_mostradas = 0;
        int arestas_ocultas = 0;
        HashMap<Integer,Integer> visibleEdgesPerTimeStamp = new HashMap();
        for(int y = 0; y <= maxTime; y++)
        {
            visibleEdgesPerTimeStamp.put(y, 0);
        }
        
        for(int x = 0; x < listAllEdges.size(); x++)
        {
            mxCell edge = (mxCell) listAllEdges.get(x);
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            String styleShapeInline = edge.getStyle();
            
            if(styleShapeInline.contains(mxConstants.STYLE_OPACITY+"=0;"))
            {
                continue;
            }
            
            ArrayList<mxCell> edgesBetweenTwoNodes = getListEdgesBetweenTwoNodes(att.getOrigin(), att.getDestiny());
            
            double maiorFx = -1;
            for(int t= minTime; t < maxTime; t++)
            {
               
                double fx = calculatef(sigma,edgesBetweenTwoNodes,t);
                
                //System.out.println(fx);
                if(fx > maiorFx )
                    maiorFx = fx;
                
            }
            
            double gf = F * maiorFx;
            
            double u = randomDouble();
            double fx = calculatef(sigma, edgesBetweenTwoNodes, att.getTime());

            if(u <= (fx/gf))
            {
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=50;");
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=50;");
                arestas_mostradas++;
                
                visibleEdgesPerTimeStamp.put(att.getTime(), visibleEdgesPerTimeStamp.get(att.getTime())+1);
                
            }
            else
            {
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=0;");
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=0;");
                arestas_ocultas++;
            }
            edge.setStyle(styleShapeInline);
        }
        System.out.println("Arestas ocultas:"+arestas_ocultas+ " Arestas mostradas:"+arestas_mostradas);
        
        graph.getModel().endUpdate();
        graph.repaint();
        graph.refresh();
        
        Object[] map = visibleEdgesPerTimeStamp.values().toArray();
        ArrayList<Double> qtdArestasEmCadaTimestamp = new ArrayList();
        for(int x = 0; x < map.length;x++)
        {
            qtdArestasEmCadaTimestamp.add(Double.parseDouble(map[x].toString())/arestas_mostradas);
        }
        
        return qtdArestasEmCadaTimestamp;
        
    }
   
   //PAPER: EOD Edge Sampling for Visualizing Dynamic Network via Massive Sequence View, 2018
   public ArrayList<Double> EODEdgeSampling()
   {
        int sigma = 5;
        double F = 2;
        
        ArrayList<String> visibleEdges = new ArrayList<>();
       
        Map<Integer, Integer> sortedMapAsc = sortByComparator(currentTemporalNodeOrder, true);
        
        HashMap<Integer,Integer> sizeAllEdges = new HashMap();
        for(int y = 0; y < sortedMapAsc.size(); y++)
        {
            sizeAllEdges.put(y, 0);
        }
        for(int y = 0; y < listAllEdges.size(); y++)
        {
            mxCell edgeCross = (mxCell) listAllEdges.get(y);
            InlineNodeAttribute attCross = (InlineNodeAttribute) edgeCross.getValue();
            String styleShapeInline = edgeCross.getStyle();
            if(styleShapeInline.contains(mxConstants.STYLE_OPACITY+"=0;"))
            {
                continue;
            }
            
            int idAresta_origin;
            int idAresta_destiny;

            //Validate if idAresta_origin is smaller than idAresta_destiny for edgeCross
            if(sortedMapAsc.get(attCross.getOrigin()) < sortedMapAsc.get(attCross.getDestiny()))
            {
                idAresta_origin = sortedMapAsc.get(attCross.getOrigin());
                idAresta_destiny = sortedMapAsc.get(attCross.getDestiny());
            }
            else
            {
                idAresta_origin = sortedMapAsc.get(attCross.getDestiny());
                idAresta_destiny = sortedMapAsc.get(attCross.getOrigin());
            }

            //Create the vector vecNodesEdC with the position of the nodes between the origin and destiny of the edge edC
            HashSet<Integer> vecNodesPosEdC = new HashSet();
            Object[] map = sortedMapAsc.keySet().toArray();
            for(int i = idAresta_origin; i <= idAresta_destiny; i++)
            {
                //Insert nodes ids between position idAresta_origin and idAresta_destiny
                vecNodesPosEdC.add((Integer) map[i-1]);
            }
            sizeAllEdges.put(vecNodesPosEdC.size()-1, sizeAllEdges.get(vecNodesPosEdC.size()-1)+1);

        }
        
        HashMap<Integer,Integer> visibleEdgesPerTimeStamp = new HashMap();
        for(int y = 0; y <= maxTime; y++)
        {
            visibleEdgesPerTimeStamp.put(y, 0);
        }
        
        int arestas_ocultas = 0, arestas_mostradas = 0;
        //Trivial, Similar, Overlapping Edge
        for(int x = 0; x < listAllEdges.size(); x++)
        {
            mxCell edge = (mxCell) listAllEdges.get(x);
            InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
            String styleShapeInline = edge.getStyle();
            
            if(styleShapeInline.contains(mxConstants.STYLE_OPACITY+"=0;"))
            {
                continue;
            }
            
            ArrayList<mxCell> edgesBetweenTwoNodes = getListEdgesBetweenTwoNodes(att.getOrigin(), att.getDestiny());
            
            double maiorFx = -1;
            for(int t= minTime; t < maxTime; t++)
            {
               
                double fx = calculatef(sigma,edgesBetweenTwoNodes,t);
                
                //System.out.println(fx);
                if(fx > maiorFx )
                    maiorFx = fx;
            }
            
            int idArestaoriginal_origin;
            int idArestaoriginal_destiny;
            Object[] map = sortedMapAsc.keySet().toArray();
            
            //Validate if idAresta_origin is smaller than idAresta_destiny for edge
            if(sortedMapAsc.get(att.getOrigin()) < sortedMapAsc.get(att.getDestiny()))
            {
                idArestaoriginal_origin = sortedMapAsc.get(att.getOrigin());
                idArestaoriginal_destiny = sortedMapAsc.get(att.getDestiny());
            }
            else
            {
                idArestaoriginal_origin = sortedMapAsc.get(att.getDestiny());
                idArestaoriginal_destiny = sortedMapAsc.get(att.getOrigin());
            }

            //Create the vector vecNodesEd with the position of the nodes between the origin and destiny of the edge ed
            //HashSet<Integer> vecNodesPosEd = new HashSet();
            //HashSet<Integer> vecNodesPosEdC = new HashSet();
            //ArrayList<HashSet> allIntersectionEdges = new ArrayList();
            ArrayList<ArrayList> tuplasArestaBase = new ArrayList();
            
            ArrayList<ArrayList> tuplasArestaDestino = new ArrayList();  
                    
            //ArrayList<ArrayList> todasTuplasArestasDestino = new ArrayList();
            
            for(int i = idArestaoriginal_origin; i < idArestaoriginal_destiny; i++)
            {
                ArrayList<Integer> tupla = new ArrayList();
                tupla.add(i);
                tupla.add(i+1);
                tupla.add(0); // boolean para decidir se a tupla tem overlapping ou não
                tuplasArestaBase.add(tupla);
                
                //Insert nodes ids between position idAresta_origin and idAresta_destiny
                //vecNodesPosEd.add((Integer) map[i-1]);
            }

            int lengthe = 0;
            double EOD = 0;
            //5: Compute the EOD(e) of each edge e
            for(int y = 0; y < listAllEdges.size(); y++)
            {
                mxCell edgeCross = (mxCell) listAllEdges.get(y);
                InlineNodeAttribute attCross = (InlineNodeAttribute) edgeCross.getValue();
                String styleShapeCross = edgeCross.getStyle();
                if(styleShapeCross.contains(mxConstants.STYLE_OPACITY+"=0;"))
                {
                    continue;
                }
                
                if(att.getTime() != attCross.getTime())
                    break;
                
                if(!edge.getId().equals(edgeCross.getId()) && att.getTime() == attCross.getTime())
                {
                    int idAresta_origin;
                    int idAresta_destiny;
                    
                    //Validate if idAresta_origin is smaller than idAresta_destiny for edgeCross
                    if(sortedMapAsc.get(attCross.getOrigin()) < sortedMapAsc.get(attCross.getDestiny()))
                    {
                        idAresta_origin = sortedMapAsc.get(attCross.getOrigin());
                        idAresta_destiny = sortedMapAsc.get(attCross.getDestiny());
                    }
                    else
                    {
                        idAresta_origin = sortedMapAsc.get(attCross.getDestiny());
                        idAresta_destiny = sortedMapAsc.get(attCross.getOrigin());
                    }
                    
                    //Create the vector vecNodesEdC with the position of the nodes between the origin and destiny of the edge edC
                    
                    for(int i = idAresta_origin; i < idAresta_destiny; i++)
                    {
                        
                        ArrayList<Integer> tupla = new ArrayList();
                        tupla.add(i);
                        tupla.add(i+1);
                        if(!tuplasArestaDestino.contains(tupla))
                            tuplasArestaDestino.add(tupla);

                        //Insert nodes ids between position idAresta_origin and idAresta_destiny
                        //vecNodesPosEdC.add((Integer) map[i-1]);
                    }
                    
                }
            }
            
            //tuplasArestaBase
            //tuplasArestaDestino
            int tuplasMarcadas = 0;
            
            for(ArrayList tuplaBase : tuplasArestaBase)
            {
                for(ArrayList tuplaDestino : tuplasArestaDestino)
                {
                    if((tuplaBase.get(0) == tuplaDestino.get(0)) && (tuplaBase.get(1) == tuplaDestino.get(1)) && (Integer.parseInt(tuplaBase.get(2).toString()) == 0))
                    {
                        tuplaBase.set(2, 1);
                        tuplasMarcadas++;
                    }
                }
            }
            
            //HashSet<Integer> commonEdgesNodes = intersectionList(vecNodesPosEd,vecNodesPosEdC);

            //EOD = (double) commonEdgesNodes.size() / (double) vecNodesPosEd.size();
            //if(EOD > 0)
                //System.out.println(EOD);

            EOD = (double) tuplasMarcadas / (double) tuplasArestaBase.size();
                
            lengthe = tuplasArestaBase.size();
            
            double Hmsv = sortedMapAsc.size();
            //Calculate le
            double le = lengthe/Hmsv;
            int num_lengthe = sizeAllEdges.get(lengthe);
            
            //Calculate wle
            double wle = (double) num_lengthe / (double) listAllEdges.size();
            
            // 6: Design proposal PDF gF (vp; vq; t) by using EOD, le and wle
            double gf = F * (maiorFx + EOD) + (1-wle) * le;
            
            double u = randomDouble();
            double fx = calculatef(sigma, edgesBetweenTwoNodes, att.getTime());
            
            if(u <= (fx/gf))
            {
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=50;");
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=50;");
                arestas_mostradas++;
                
                String[] componentesAresta = edge.getId().split(" ");
                //util.FileHandler.gravaArquivo(componentesAresta[1] + " " + componentesAresta[2] + " " + componentesAresta[0] + "\r\n", "F:\\redeAposEOD.txt", true);
                
                //Make the nodes of the edge visible as well JEAN
                changeOpacityCell(componentesAresta[1] + " " + componentesAresta[0], 100);
                changeOpacityCell(componentesAresta[2] + " " + componentesAresta[0], 100);
                
                if(!visibleEdges.contains(componentesAresta[1] + " " + componentesAresta[0]))
                    visibleEdges.add(componentesAresta[1] + " " + componentesAresta[0]);
                if(!visibleEdges.contains(componentesAresta[2] + " " + componentesAresta[0]))
                    visibleEdges.add(componentesAresta[2] + " " + componentesAresta[0]);
                
                visibleEdgesPerTimeStamp.put(att.getTime(), visibleEdgesPerTimeStamp.get(att.getTime())+1);
                //edge.setStyle(styleShapeInline);
            }
            else
            {
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=0;");
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=0;");
                arestas_ocultas++;
                
                String[] componentesAresta = edge.getId().split(" ");
                
                //graph.getModel().remove(edge);
                //graph.getModel().remove(((mxGraphModel)graph.getModel()).getCell(componentesAresta[1] + " " + componentesAresta[0]));
                
                
                //(mxCell) ((mxGraphModel)graph.getModel()).getCell(componentesAresta[1] + " " + componentesAresta[0]);
                
                //Make the nodes of the edge invisible as well - JEAN COMENTOU IF
                if(!visibleEdges.contains(componentesAresta[1] + " " + componentesAresta[0]))
                    changeOpacityCell(componentesAresta[1] + " " + componentesAresta[0], 0);
                if(!visibleEdges.contains(componentesAresta[2] + " " + componentesAresta[0]))
                    changeOpacityCell(componentesAresta[2] + " " + componentesAresta[0], 0);
                /*
                graph.getModel().remove(edge);
                
                mxCell cell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(componentesAresta[1] + " " + componentesAresta[0]);
                if(cell != null)
                {
                    graph.getModel().remove(cell);
                }
                cell = (mxCell) ((mxGraphModel)graph.getModel()).getCell(componentesAresta[2] + " " + componentesAresta[0]);
                if(cell != null)
                {
                    graph.getModel().remove(cell);
                }*/
            }
            edge.setStyle(styleShapeInline);
            
            //JEAN pra nao exibir os nos das arestas mantidas
          //  String[] componentesAresta = edge.getId().split(" ");
          //  changeOpacityCell(componentesAresta[1] + " " + componentesAresta[0], 0);
          //  changeOpacityCell(componentesAresta[2] + " " + componentesAresta[0], 0);
        }
        
        System.out.println("Arestas ocultas:"+arestas_ocultas+ " Arestas mostradas:"+arestas_mostradas);
        
        graph.getModel().endUpdate();
        graph.repaint();
        graph.refresh();
        
        Object[] map = visibleEdgesPerTimeStamp.values().toArray();
        ArrayList<Double> qtdArestasEmCadaTimestamp = new ArrayList();
        for(int x = 0; x < map.length;x++)
        {
            qtdArestasEmCadaTimestamp.add(Double.parseDouble(map[x].toString())/arestas_mostradas);
        }
        
        return qtdArestasEmCadaTimestamp;
   }
   
   public ArrayList<Double> randomEdgeSampling()
   {
       
       HashMap<Integer,Integer> visibleEdgesPerTimeStamp = new HashMap();
        for(int y = 0; y <= maxTime; y++)
        {
            visibleEdgesPerTimeStamp.put(y, 0);
        }
       
        int arestas_mostradas = 0;
        
        graph.getModel().beginUpdate(); 
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;

            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            if(att.isEdge())
            {
                String styleShapeInline = cell.getStyle();
                
                if(Math.random() < 0.5)
                {
                    styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=50;");
                    visibleEdgesPerTimeStamp.put(att.getTime(), visibleEdgesPerTimeStamp.get(att.getTime())+1);
                    arestas_mostradas++;
                }
                else
                {
                    styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                    styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=0;");
                    styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=0;");
                }
                cell.setStyle(styleShapeInline);

            }
        }
        graph.getModel().endUpdate();
        graph.repaint();
        graph.refresh();
        
        Object[] map = visibleEdgesPerTimeStamp.values().toArray();
        ArrayList<Double> qtdArestasEmCadaTimestamp = new ArrayList();
        for(int x = 0; x < map.length;x++)
        {
            qtdArestasEmCadaTimestamp.add(Double.parseDouble(map[x].toString())/arestas_mostradas);
        }
        
        return qtdArestasEmCadaTimestamp;
   }
   
   public ArrayList<Double> showAllEdges()
   {
       
       HashMap<Integer,Integer> visibleEdgesPerTimeStamp = new HashMap();
        for(int y = 0; y <= maxTime; y++)
        {
            visibleEdgesPerTimeStamp.put(y, 0);
        }
       
        graph.getModel().beginUpdate(); 
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;

            InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
            if(att.isEdge())
            {
                String styleShapeInline = cell.getStyle();
                
                styleShapeInline = styleShapeInline.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=50;");
                visibleEdgesPerTimeStamp.put(att.getTime(), visibleEdgesPerTimeStamp.get(att.getTime())+1);
                cell.setStyle(styleShapeInline);
                
                
                //Make the nodes of the edge visible as well
                String[] componentesAresta = cell.getId().split(" ");
                changeOpacityCell(componentesAresta[1] + " " + componentesAresta[0], 50);
                changeOpacityCell(componentesAresta[2] + " " + componentesAresta[0], 50);

            }
        }
        graph.getModel().endUpdate();
        graph.repaint();
        graph.refresh();
        
        Object[] map = visibleEdgesPerTimeStamp.values().toArray();
        ArrayList<Double> qtdArestasEmCadaTimestamp = new ArrayList();
        for(int x = 0; x < map.length;x++)
        {
            qtdArestasEmCadaTimestamp.add(Double.parseDouble(map[x].toString())/listAllEdges.size());
        }
        
        return qtdArestasEmCadaTimestamp;
   }
   
}