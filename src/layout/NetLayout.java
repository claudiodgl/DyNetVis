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
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import forms.MainForm;
import forms.OpenDataSetDialog;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.filechooser.FileNameExtensionFilter;
import layout.NetLayoutInlineNew.JGraphStyle;
import util.FileHandler;
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

/**
 *
 * @author Claudio Linhares
 */
public class NetLayout extends Observable{

    /**
     * @return the sizeIdNode
     */
    public String getSizeIdNode() {
        return sizeIdNode;
    }

    /**
     * @param sizeIdNode the sizeIdNode to set
     */
    public void setSizeIdNode(String sizeIdNode) {
        this.sizeIdNode = sizeIdNode;
    }
    
    public NetLayout(){
        
    }
    
    private ArrayList<NetInstance> nodes;
    private ArrayList<Integer> selectedNodes;
    public mxGraph graph2;
    private ArrayList<NetEdge> edges;
    int maxTime;
    int minTime;
    private int depth;
    private String openFile,layoutJGraphX;
    private Dimension size;
    private ArrayList<ArrayList> matrizData;
    public ArrayList<mxCell> listEdgesJgraph;
    
    //Layout presentation properties
    public boolean showEdges, showNodes, showInstanceWeight , selectSimilar, showNodesNotSelected;
    public boolean showEdgesHorizontalLines;
    private boolean typeGraph;
    private float alpha;
    private int currentTime, inicialTime;
    private String typeColor,typeColorEdge,color,colorEdge,sizeNode, sizeEdge, weightEdge = "Degree",sizeIdNode;
   
    public void setColor(String color){
        this.color = color;
    }
    
    public void changeTypeGraph(){
        graph2.getModel().beginUpdate(); 
        if(typeGraph)
        {
            for(mxCell ed : listEdgesJgraph)
            {
                String styleEdge = ed.getStyle();
                styleEdge = styleEdge.replaceAll(mxConstants.STYLE_ENDARROW+"=0;","");
                ed.setStyle(styleEdge);
            }
        }
        else
        {          
            for(mxCell ed : listEdgesJgraph)
            {
                String styleEdge = ed.getStyle();
                styleEdge += mxConstants.STYLE_ENDARROW+"=0;";
                ed.setStyle(styleEdge);
            }
        }
        graph2.getModel().endUpdate();
        graph2.refresh();
        graph2.repaint();   
    }
    
    final public void changeSizeNodes(){
        
        if(getSizeNode().equals("Original")){
           
            //Scalar Nodes
            Object[] roots = graphComponentScalarNodeSize.getGraph().getChildCells(graphComponentScalarNodeSize.getGraph().getDefaultParent(), true, false);
            graphComponentScalarNodeSize.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                g.setHeight(15);
                g.setWidth(15);
                cell.setGeometry(g);
                
            }
            graphComponentScalarNodeSize.getGraph().getModel().endUpdate();
            
            //All nodes graph
            roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            graph2.getModel().beginUpdate();
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
               
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                g.setHeight(15);
                g.setWidth(15);
                cell.setGeometry(g);
                
            }
            
            graph2.getModel().endUpdate();
        }
        else if(getSizeNode().equals("Big")){
           
            //Scalar Nodes
            Object[] roots = graphComponentScalarNodeSize.getGraph().getChildCells(graphComponentScalarNodeSize.getGraph().getDefaultParent(), true, false);
            graphComponentScalarNodeSize.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                g.setHeight(30);
                g.setWidth(30);
                cell.setGeometry(g);
                
            }
            graphComponentScalarNodeSize.getGraph().getModel().endUpdate();
            
            //All nodes graph
            roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            graph2.getModel().beginUpdate();
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
               
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                g.setHeight(30);
                g.setWidth(30);
                cell.setGeometry(g);
                
            }
            
            graph2.getModel().endUpdate();
        }
        else{
            
            float maxSize = 30;
            float minSize = 15;
           
            //All other nodes
            Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            graph2.getModel().beginUpdate();
         
            mxCell firstNode = (mxCell) roots[0];
            
            float vMax = Float.parseFloat(firstNode.getEdgeCount()+"");
            float vMin = Float.parseFloat(firstNode.getEdgeCount()+"");

            for (Object root1 : roots) {
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
                mxCell cell = (mxCell) root1;
                CustomAttributes att = (CustomAttributes) cell.getValue();
                
                if(att.isNode())
                {
                    //nós do grafo
                    int weightNode = cell.getEdgeCount();
                    
                    float insize = (int) (weightNode * (maxSize - minSize) / (vMax - vMin)) + minSize;
                    
                    float f = Integer.MAX_VALUE;
                    int ia = (int) f;
                    
                    if(insize == ia)
                        insize = 15;
                    
                    
                    mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                    g.setHeight(insize);
                    g.setWidth(insize);
                    cell.setGeometry(g);

                }
            }
            
            graph2.getModel().endUpdate();
            
            
             //Scalar Nodes
            roots = graphComponentScalarNodeSize.getGraph().getChildCells(graphComponentScalarNodeSize.getGraph().getDefaultParent(), true, false);
            graphComponentScalarNodeSize.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                
                float insize = (int) (Integer.parseInt(idCell) * (30 - minSize) / (10 - 0)) + minSize;
                
                mxGeometry g = (mxGeometry) cell.getGeometry().clone();
                g.setHeight(insize);
                g.setWidth(insize);
                cell.setGeometry(g);
                
            }
            graphComponentScalarNodeSize.getGraph().getModel().endUpdate();
            
        }
    }
    
    
    final public void changeSizeIdNodes(){
        graph2.getModel().beginUpdate();
        int sizeIdNode = 1;
        if(getSizeIdNode().equals("Original")){
            sizeIdNode = 10;
        }
        else if(getSizeIdNode().equals("Big")){
            sizeIdNode = 100;
        }
        //All nodes graph
        Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;

            String styleNode = cell.getStyle();
            if(!styleNode.contains(mxConstants.STYLE_FONTSIZE))
                styleNode += mxConstants.STYLE_FONTSIZE+"="+sizeIdNode+";";
            else
                styleNode = styleNode.replaceAll(mxConstants.STYLE_FONTSIZE+"=[^;]*;",mxConstants.STYLE_FONTSIZE+"="+sizeIdNode+";");
            cell.setStyle(styleNode);
        }
        graph2.getModel().endUpdate();
    }
    
    
    final public void changeColorNodes(){
        
        if(getColor().equals("Original") || getColor().equals("Bipartite") ){
           
            //Scalar Nodes
            Object[] roots = graphComponentScalar.getGraph().getChildCells(graphComponentScalar.getGraph().getDefaultParent(), true, false);
            graphComponentScalar.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String styleNode = mxConstants.STYLE_MOVABLE+"=0;";
                styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                String hexColor = "#C3D9FF";
                styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                //styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
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

                graphComponentScalar.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalar.getGraph().getModel().endUpdate();
            
            //All nodes graph
            roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            graph2.getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                
                JGraphStyle style = NetLayoutInlineNew.JGraphStyle.ROUNDED;

                String styleShape = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                styleShape += mxConstants.STYLE_RESIZABLE+"=0;";
                styleShape += mxConstants.STYLE_OPACITY+"=100;";
                
                CustomAttributes att = (CustomAttributes) cell.getValue();
                if(getColor().equals("Bipartite"))
                {
                    if(att.isIsFirst())
                        styleShape += mxConstants.STYLE_FILLCOLOR+"="+Color.YELLOW+";";
                }
                if(showInstanceWeight)
                    styleShape += mxConstants.STYLE_NOLABEL+"=0;";
                else
                    styleShape += mxConstants.STYLE_NOLABEL+"=1;";
                
                graph2.setCellStyle(styleShape+style.mxStyle , root);

            }
            
            graph2.getModel().endUpdate();
        }
        else if(getColor().equals("Metadata File"))
        {
            //String[] communitiesColors = new String[100000];
            HashMap<String,Integer> communitiesColors = new HashMap();
            
            //String[] colorsTable = new String[10000];
            //String[] colorsLabel = new String[10000];
            HashMap<Integer,String> colorsTable = new HashMap();
            HashMap<Integer,String> colorsLable = new HashMap();

            JFileChooser openDialog = new JFileChooser();
            String filename = "";
            filename = f.getPathDataset();

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
                    
                    if(tokens.length != 2)
                    {
                        JOptionPane.showMessageDialog(null,"File format different from the expected. Check the Information button for details.","Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {

                        //colorsTable.put(1,"000 000 255"); //blue
                        //colorsTable.put(2,"000 255 000"); //green
                        //colorsTable.put(0,"255 000 000"); //red
                        
                        colorsTable.put(0,"000 000 255"); //blue
                        colorsTable.put(1,"000 255 000"); //green
                        colorsTable.put(2,"255 000 000"); //red
                        colorsTable.put(3,"255 185 60"); //orange
                        colorsTable.put(4,"255 255 000"); //yellow
                        colorsTable.put(5,"255 000 255"); //pink
                        colorsTable.put(6,"100 070 000"); //brown
                        colorsTable.put(7,"205 092 092"); //light pink
                        colorsTable.put(8,"148 000 211"); //purple
                        colorsTable.put(9,"000 255 255"); //cyan
                        
                        colorsTable.put(10,"000 000 000"); //Black
                        colorsTable.put(11,"064 224 208"); //Turquoise
                        colorsTable.put(12,"000 128 128"); //Teal
                        colorsTable.put(13,"000 100 000"); //DarkGreen
                        colorsTable.put(14,"085 107 047"); //DarkOliveGreen
                        colorsTable.put(15,"218 165 032"); //Goldenrod
                        colorsTable.put(16,"245 222 179"); //Wheat
                        colorsTable.put(17,"075 000 130"); //Indigo
                        colorsTable.put(18,"128 000 000"); //Maroon
                        colorsTable.put(19,"255 215 000"); //Gold
                        colorsTable.put(20,"240 230 140"); //Khaki
                        
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
                        */
                        
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


                        int cont= 0;

                        communitiesColors.put(tokens[0], cont);
                        colorsLable.put(0, tokens[1]);
                        //communitiesColors[Integer.parseInt(tokens[0])] = colorsTable[cont];
                        //colorsLabel[0] = tokens[1];

                        while ((tmp = file.readLine()) != null)
                        {

                            strLine = tmp;
                            String[] tokens2 = strLine.split(" ");
                            if(!tokens[1].equals(tokens2[1]))
                            {
                                tokens[1] = tokens2[1];
                                cont++;
                                colorsLable.put(cont, tokens2[1]);
                                //colorsLabel[cont] = tokens2[1];
                            }
                            communitiesColors.put(tokens2[0], cont);
                            //communitiesColors[Integer.parseInt(tokens2[0])] = colorsTable[cont];
                        }

                        for(int i = 21; i <= cont;i++)
                        {
                            
                            Random rand = new Random();
                            // Java 'Color' class takes 3 floats, from 0 to 1.
                            float r = rand.nextFloat();
                            float g = rand.nextFloat();
                            float b = rand.nextFloat();

                            Color color = new Color(r, g, b);

                            colorsTable.put(i,color.getRed()+" "+color.getGreen()+" "+color.getBlue()+"");
                        }


                        //All other nodes
                        Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
                        graph2.getModel().beginUpdate();

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
                            CustomAttributes att = (CustomAttributes) cell.getValue();

                            if(att.isNode())
                            {

                                int r = 0;
                                int g = 0;
                                int b = 0;
                                if(communitiesColors.get(idCell) != null)
                                {
                                    String color2 = colorsTable.get(communitiesColors.get(idCell));
                                    tokens = color2.split(" ");
                                    r = Integer.parseInt(tokens[0]);
                                    g = Integer.parseInt(tokens[1]);
                                    b = Integer.parseInt(tokens[2]);
                                }
                                else
                                {
                                    System.out.println("no sem cor"); //os nós que possuem rotulo no arquivo de metadado vão ser pretos. Como preto já é a 11a primeira cor, vai ficar misturado.
                                }
                                Color colorNode = new Color(r,g,b);
                                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                                String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                                //styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                                styleNode +=  mxConstants.STYLE_STROKECOLOR+"=black;";
                                styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                                styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                                if(showInstanceWeight)
                                    styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                                else
                                    styleNode += mxConstants.STYLE_NOLABEL+"=1;";


                                Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                                String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);

                                //styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                                
                                graph2.setCellStyle(styleNode, root);
                            }
                        }

                        graph2.getModel().endUpdate();



                         //Scalar Nodes
                        boolean firstBool = true;
                        String first = "";

                        int communityColorId = 0;

                        roots = graphComponentScalar.getGraph().getChildCells(graphComponentScalar.getGraph().getDefaultParent(), true, false);
                        graphComponentScalar.getGraph().getModel().beginUpdate();
                        for (Object root1 : roots) {
                            Object[] root = {root1};
                            mxCell cell = (mxCell) root1;
                            String idCell = cell.getId();

                            String[] parts = idCell.split(" ");

                            int r = 0;
                            int g = 0;
                            int b = 0;

                            String color2 = colorsTable.get(communityColorId);
                            tokens = color2.split(" ");
                            r = Integer.parseInt(tokens[0]);
                            g = Integer.parseInt(tokens[1]);
                            b = Integer.parseInt(tokens[2]);

                            String label = "";
                            if(communityColorId < 10)
                                label = colorsLable.get(communityColorId);

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

                            graphComponentScalar.getGraph().setCellStyle(styleNode, root);
                        }
                        graphComponentScalar.getGraph().getModel().endUpdate();


                    }
               }catch (FileNotFoundException ex) {
                   Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
               } catch (IOException ex) {
                   Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
               }
            }

        }
        else if(getColor().equals("Community"))
        {
            //String[] communitiesColors = new String[100000];
            HashMap<String,Integer> communitiesColors = new HashMap();
            
            //String[] colorsTable = new String[10000];
            //String[] colorsLabel = new String[10000];
            HashMap<Integer,String> colorsTable = new HashMap();
            HashMap<Integer,String> colorsLable = new HashMap();

            colorsTable.put(0,"000 000 255"); //blue
            colorsTable.put(1,"000 255 000"); //green
            colorsTable.put(2,"255 000 000"); //red
            colorsTable.put(3,"255 185 60"); //orange
            colorsTable.put(4,"255 255 000"); //yellow
            colorsTable.put(5,"255 000 255"); //pink
            colorsTable.put(6,"100 070 000"); //brown
            colorsTable.put(7,"205 092 092"); //light pink
            colorsTable.put(8,"148 000 211"); //purple
            colorsTable.put(9,"000 255 255"); //cyan

            colorsTable.put(10,"000 000 000"); //Black
            colorsTable.put(11,"064 224 208"); //Turquoise
            colorsTable.put(12,"000 128 128"); //Teal
            colorsTable.put(13,"000 100 000"); //DarkGreen
            colorsTable.put(14,"085 107 047"); //DarkOliveGreen
            colorsTable.put(15,"218 165 032"); //Goldenrod
            colorsTable.put(16,"245 222 179"); //Wheat
            colorsTable.put(17,"075 000 130"); //Indigo
            colorsTable.put(18,"128 000 000"); //Maroon
            colorsTable.put(19,"255 215 000"); //Gold
            colorsTable.put(20,"240 230 140"); //Khaki

            
            Iterator it = communities.entrySet().iterator();
            int cont= communities.size();
            for(int i = 21; i <= cont;i++)
            {

                Random rand = new Random();
                // Java 'Color' class takes 3 floats, from 0 to 1.
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();

                Color color = new Color(r, g, b);

                colorsTable.put(i,color.getRed()+" "+color.getGreen()+" "+color.getBlue()+"");
            }

            
            //communitiesColors.put(tokens[0], cont);
            //colorsLable.put(0, tokens[1]);
            //communitiesColors[Integer.parseInt(tokens[0])] = colorsTable[cont];
            //colorsLabel[0] = tokens[1];

            
            int newKey = 0, firstIteraction = 0;
            String firstLabel = "";
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                ArrayList<InlineNodeAttribute> arr = (ArrayList) pair.getValue();
                for(InlineNodeAttribute a : arr)
                {
                    communitiesColors.put(a.getId_original()+"",newKey);

                    firstLabel = pair.getKey()+"";
                    colorsLable.put(newKey, pair.getKey()+"");
                }
                newKey++;
            }

            /*
            while ((tmp = file.readLine()) != null)
            {

                strLine = tmp;
                String[] tokens2 = strLine.split(" ");
                if(!tokens[1].equals(tokens2[1]))
                {
                    tokens[1] = tokens2[1];
                    cont++;
                    colorsLable.put(cont, tokens2[1]);
                    //colorsLabel[cont] = tokens2[1];
                }
                communitiesColors.put(tokens2[0], cont);
                //communitiesColors[Integer.parseInt(tokens2[0])] = colorsTable[cont];
            }*/



            //All other nodes
            Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            graph2.getModel().beginUpdate();

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
                CustomAttributes att = (CustomAttributes) cell.getValue();

                if(att.isNode())
                {

                    int r = 0;
                    int g = 0;
                    int b = 0;
                    if(communitiesColors.get(idCell) != null)
                    {
                        String color2 = colorsTable.get(communitiesColors.get(idCell));
                        String[] tokens = color2.split(" ");
                        r = Integer.parseInt(tokens[0]);
                        g = Integer.parseInt(tokens[1]);
                        b = Integer.parseInt(tokens[2]);
                    }
                    else
                    {
                        //System.out.println("no sem cor");
                    }
                    Color colorNode = new Color(r,g,b);
                    String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                    String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    //styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                    styleNode +=  mxConstants.STYLE_STROKECOLOR+"=black;";
                    styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                    styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                    styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    styleNode += mxConstants.STYLE_OPACITY+"=100;";
                    if(showInstanceWeight)
                        styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                    else
                        styleNode += mxConstants.STYLE_NOLABEL+"=1;";


                    Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                    String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);

                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";

                    graph2.setCellStyle(styleNode, root);
                }
            }

            graph2.getModel().endUpdate();



             //Scalar Nodes
            boolean firstBool = true;
            String first = "";

            int communityColorId = 0;

            roots = graphComponentScalar.getGraph().getChildCells(graphComponentScalar.getGraph().getDefaultParent(), true, false);
            graphComponentScalar.getGraph().getModel().beginUpdate();
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();

                String[] parts = idCell.split(" ");

                int r = 0;
                int g = 0;
                int b = 0;

                String color2 = colorsTable.get(communityColorId);
                String[] tokens = color2.split(" ");
                r = Integer.parseInt(tokens[0]);
                g = Integer.parseInt(tokens[1]);
                b = Integer.parseInt(tokens[2]);

                String label = "";
                if(communityColorId < 11)
                    label = colorsLable.get(communityColorId);

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

                graphComponentScalar.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalar.getGraph().getModel().endUpdate();

        }
        else if(getColor().equals("Random Color"))
        {
            
            Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            graph2.getModel().beginUpdate();
         

            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                CustomAttributes att = (CustomAttributes) cell.getValue();

                if(att.isNode())
                {


                    Random rand = new Random();
                    // Java 'Color' class takes 3 floats, from 0 to 1.
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();

                    Color color = new Color(r, g, b);
                    String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);

                    String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleNode +=  mxConstants.STYLE_STROKECOLOR+"=black;";
                    styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                    styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                    styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    styleNode += mxConstants.STYLE_OPACITY+"=100;";
                    if(showInstanceWeight)
                        styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                    else
                        styleNode += mxConstants.STYLE_NOLABEL+"=1;";
                    
                    
                    Color corEmNegativo = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
                    String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);
                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                    

                    graph2.setCellStyle(styleNode, root);
                }
            }

            graph2.getModel().endUpdate();

            
            
            //Scalar Nodes
            roots = graphComponentScalar.getGraph().getChildCells(graphComponentScalar.getGraph().getDefaultParent(), true, false);
            graphComponentScalar.getGraph().getModel().beginUpdate();
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

                styleNode += mxConstants.STYLE_FONTSIZE+"=15;";
                styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";

                graphComponentScalar.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalar.getGraph().getModel().endUpdate();

            
        }
        else{
            
            ColorScale colorScale;
            
            switch (typeColor) {
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
                    "Choose the Inicial Node Color", java.awt.Color.BLACK);
                    java.awt.Color color2 = javax.swing.JColorChooser.showDialog(graphComponent,
                    "Choose the Final Node Color", java.awt.Color.BLACK);
                    if(color1 != null && color2 != null)
                        colorScale = new CustomColor(color1,color2);
                    else
                        colorScale = new CustomColor(Color.BLACK,Color.BLACK);
                    break;
                //case "Blue Scale":
                  //  colorScale = new BlueScale();
                    //break;
                default:
                    colorScale = new OrangeToBlueSky();
                    break;
            }
            
            //All other nodes
            Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            graph2.getModel().beginUpdate();
         
            mxCell firstNode = (mxCell) roots[0];
            
            double vMax = Float.parseFloat(firstNode.getEdgeCount()+"");
            double vMin = Float.parseFloat(firstNode.getEdgeCount()+"");
            float maxColor = 1;
            float minColor = 0;

            
            
            nodesCount = 0;
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                String[] parts = idCell.split(" ");
                if(parts.length != 2 )
                {
                    int weightNode = cell.getEdgeCount();
                    nodesCount++;
                    if(weightNode > vMax)
                        vMax = weightNode;
                    if(weightNode < vMin)
                        vMin = weightNode;
                    
                }
            }
            
            int mediaDados = 0;
            double stdDev = 0;
            
            
            
            if(getColor().equals("Color Std Dev"))
            {
                degreeNodes = new int[nodesCount];
                degreeNormalizedNodes = new double[nodesCount];
                double[] stdDevNodes = new double[nodesCount];
                
                int i=0;
                for (Object root1 : roots) {
                    Object[] root = {root1};
                    mxCell cell = (mxCell) root1;
                    String idCell = cell.getId();
                    String[] parts = idCell.split(" ");
                    if(parts.length != 2 )
                    {
                        degreeNodes[i] = cell.getEdgeCount();
                        stdDevNodes[i] = getStdDev();
                        mediaDados += cell.getEdgeCount();
                    }
                    i++;
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
                String idCell = cell.getId();
                String[] parts = idCell.split(" ");
                CustomAttributes att = (CustomAttributes) cell.getValue();
                
                if(att.isNode())
                {
                    //nós do grafo
                    double weightNode = cell.getEdgeCount();
                    
                    if(getColor().equals("Color Std Dev"))
                    {
                        weightNode = ( weightNode - mediaDados ) / stdDev;
                    }
                    
                    //float incolor = (weightNode * (maxColor - minColor)/ (vMax - vMin) + minColor);
                    double incolor = (weightNode - vMin) / (vMax - vMin);
                    
                    if(getColor().equals("Color Std Dev"))
                    {
                        //Function Sigmoid
                        incolor = 1.0 / (1.0 + Math.exp(-weightNode));
                    }
                    Color colorNode = colorScale.getColor((float)incolor);
                    String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                    String styleNode =  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                    styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                    styleNode += mxConstants.STYLE_EDITABLE+"=0;";
                    styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
                    styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    styleNode += mxConstants.STYLE_OPACITY+"=100;";
                    if(showInstanceWeight)
                        styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                    else
                        styleNode += mxConstants.STYLE_NOLABEL+"=1;";
                    
                    Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                    String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);
                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                    
                    graph2.setCellStyle(styleNode, root);
                }
            }
            
            graph2.getModel().endUpdate();
            
            
            
             //Scalar Nodes
            boolean firstBool = true;
            String first = "";
            
            roots = graphComponentScalar.getGraph().getChildCells(graphComponentScalar.getGraph().getDefaultParent(), true, false);
            graphComponentScalar.getGraph().getModel().beginUpdate();
            float scalar_count = 0;
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                String[] parts = idCell.split(" ");
                
                Color colorNode = colorScale.getColor(scalar_count);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                scalar_count += 0.1;
                if(firstBool)
                {
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
                    cell.setValue((int)vMin+"");
                }
                else if(cell.getId().equals("color 1")){
                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
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
                
                graphComponentScalar.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalar.getGraph().getModel().endUpdate();
            
        }
        graphComponentScalar.getGraph().repaint();
        graphComponentScalar.getGraph().refresh();
    }
    
    float eMaxSizeEdges = 2;
    float eMinSizeEdges = 1;
    
    final public void changeSizeEdges(){
        
        if(getSizeEdge().equals("Original")){
           
            graph2.getModel().beginUpdate();
            
            for(mxCell ed : listEdgesJgraph)
            {
                
                String styleEdge = ed.getStyle();
                
                styleEdge = styleEdge.replaceAll("strokeWidth=[^;]*;","strokeWidth=1;");
                int indexStyleEdge = styleEdge.indexOf(mxConstants.STYLE_STROKEWIDTH);
            
                Object[] edd = {ed};
                graph2.setCellStyle(styleEdge, edd);
               
            }
            graph2.getModel().endUpdate();
        }
        else if(getSizeEdge().equals("Stroke Edges")){
            graph2.getModel().beginUpdate();
            
            CustomAttributes att = (CustomAttributes) listEdgesJgraph.get(0).getValue();
            eMaxSizeEdges = att.getWeight();
            eMinSizeEdges = att.getWeight();

            for(mxCell edge : listEdgesJgraph){
                att = (CustomAttributes) edge.getValue();
                float ia = att.getWeight();
                if(ia > eMaxSizeEdges)
                    eMaxSizeEdges = ia;
                if(ia < eMinSizeEdges)
                    eMinSizeEdges = ia;
            }
            
            //Edges
            for(mxCell ed : listEdgesJgraph)
            {
                    
                float maxSize = 8f;
                float minSize = 0.5f;

                
                att = (CustomAttributes) ed.getValue();
                
                //x = y * ( max - min) / (vMax - vMin) + min
                //float insize = att.getWeight() * (maxSize - minSize) / (eMaxSizeEdges - eMinSizeEdges) + minSize;
                
                //v’ = (v-min)/(max-min) * (newmax-newmin) + newmin
                float insize = (att.getWeight() - eMinSizeEdges) / (eMaxSizeEdges - eMinSizeEdges) * (maxSize - minSize) + minSize;
                
                if(insize == Float.POSITIVE_INFINITY)
                    insize = 1f;

                
                String styleEdge = ed.getStyle();
                styleEdge = styleEdge.replaceAll("strokeWidth=[^;]*;","strokeWidth="+insize+";");
                
                Object[] edd = {ed};
                graph2.setCellStyle(styleEdge, edd);
               
            }
            graph2.getModel().endUpdate();
        }
    }
    
    ColorScale colorScaleEdges = new GreenToRed();
    float eMaxColorEdges = 1;
    float eMinColorEdges = 1;
    String noFBColor = "";
    String hasFBColor = "";
    
    final public void changeColorEdges(){
        
        if(getColorEdge().equals("Original")){
           
            graph2.getModel().beginUpdate();
            
             //Scalar Edges
            Object[] roots = graphComponentScalarEdge.getGraph().getChildCells(graphComponentScalarEdge.getGraph().getDefaultParent(), true, false);
            graphComponentScalarEdge.getGraph().getModel().beginUpdate();
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

                graphComponentScalarEdge.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalarEdge.getGraph().getModel().endUpdate();
            
            
            for(mxCell ed : listEdgesJgraph)
            {
                
                String styleShapeEdge = ed.getStyle();
                int indexStyleEdge = styleShapeEdge.indexOf(mxConstants.STYLE_FILLCOLOR);
                String piece = "";
                if(indexStyleEdge != -1){
                    piece = styleShapeEdge.substring(indexStyleEdge, indexStyleEdge+18);
                }
                int indexStyleEdge2 = styleShapeEdge.indexOf(mxConstants.STYLE_STROKECOLOR);
                String piece2 = "";
                if(indexStyleEdge2 != -1){
                    piece2 = styleShapeEdge.substring(indexStyleEdge2, indexStyleEdge2+20);
                }
                styleShapeEdge = styleShapeEdge.replaceAll(piece,"");
                styleShapeEdge = styleShapeEdge.replaceAll(piece2,"");
                
                Object[] edd = {ed};
                graph2.setCellStyle(styleShapeEdge, edd);
               
            }
            graph2.getModel().endUpdate();
        }
        else if(getColorEdge().equals("Random Color"))
        {
            
            Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            graph2.getModel().beginUpdate();
         
            //Edges
            for(mxCell ed : listEdgesJgraph)
            {
                    
                Random rand = new Random();
                // Java 'Color' class takes 3 floats, from 0 to 1.
                float r = rand.nextFloat();
                float g = rand.nextFloat();
                float b = rand.nextFloat();

                Color color = new Color(r, g, b);
                String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);

                String styleShapeEdge = ed.getStyle();
                int indexStyleEdge = styleShapeEdge.indexOf(mxConstants.STYLE_FILLCOLOR);
                String piece = "";
                if(indexStyleEdge != -1){
                    piece = styleShapeEdge.substring(indexStyleEdge, indexStyleEdge+18);
                }
                int indexStyleEdge2 = styleShapeEdge.indexOf(mxConstants.STYLE_STROKECOLOR);
                String piece2 = "";
                if(indexStyleEdge2 != -1){
                    piece2 = styleShapeEdge.substring(indexStyleEdge2, indexStyleEdge2+20);
                }
                if(!piece.isEmpty())
                    styleShapeEdge = styleShapeEdge.replace(piece,mxConstants.STYLE_FILLCOLOR+"="+hexColor+";");
                else
                    styleShapeEdge += mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                if(!piece2.isEmpty())
                    styleShapeEdge = styleShapeEdge.replace(piece2,mxConstants.STYLE_STROKECOLOR+"="+hexColor+";");
                else
                    styleShapeEdge += mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

                Object[] edd = {ed};
                graph2.setCellStyle(styleShapeEdge, edd);
               
            }
            
           
            graph2.getModel().endUpdate();
            
                        
            //Scalar Edges
            roots = graphComponentScalarEdge.getGraph().getChildCells(graphComponentScalarEdge.getGraph().getDefaultParent(), true, false);
            graphComponentScalarEdge.getGraph().getModel().beginUpdate();
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

                graphComponentScalarEdge.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalarEdge.getGraph().getModel().endUpdate();

           
        }
        else if(getColorEdge().equals("Metadata File"))
        {
            String[] communitiesColors = new String[100000];
            
            String[] colorsTable = new String[100];
            String[] colorsLabel = new String[10000];

            JFileChooser openDialog = new JFileChooser();
            String filename = "";
            filename = f.getPathDataset();

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

                        String[] splitColor = colorsTable[0].split(" ");
                        Color colorT = new Color(Integer.parseInt(splitColor[0]),Integer.parseInt(splitColor[1]),Integer.parseInt(splitColor[2]));
                        String hex = "#"+Integer.toHexString(colorT.getRGB()).substring(2);
                        noFBColor = hex;

                        splitColor = colorsTable[1].split(" ");
                        colorT = new Color(Integer.parseInt(splitColor[0]),Integer.parseInt(splitColor[1]),Integer.parseInt(splitColor[2]));
                        hex = "#"+Integer.toHexString(colorT.getRGB()).substring(2);
                        hasFBColor = hex;

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

                        graph2.getModel().beginUpdate();

                        String[] colorRGB = colorsTable[Integer.parseInt(tokens[2])].split(" ");
                        Color color = new Color(Integer.parseInt(colorRGB[0]),Integer.parseInt(colorRGB[1]),Integer.parseInt(colorRGB[2]));


                        //int t = (int) Math.floor(Integer.parseInt(tokens[2]) / resolution);

                        mxCell v1 = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(tokens[0]+" "+tokens[1]);
                        if(v1 == null)
                            v1 = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(tokens[1]+" "+tokens[0]);
                        if(v1 != null)
                        {
                            String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);
                            String styleShapeInline = v1.getStyle();
                            styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                            if(!styleShapeInline.contains("strokeColor"))
                                    styleShapeInline = styleShapeInline.concat("strokeColor="+hexColor+";");
                            else
                                styleShapeInline = styleShapeInline.replaceAll("strokeColor=[^;]*;","strokeColor="+hexColor+";");
                            styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");

                            v1.setStyle(styleShapeInline);
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

                            v1 = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(tokens2[0]+" "+tokens2[1]);
                            if(v1 == null)
                                v1 = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(tokens2[1]+" "+tokens2[0]);
                            if(v1 != null)
                            {
                                String hexColor = "#"+Integer.toHexString(color.getRGB()).substring(2);

                                String styleShapeInline = v1.getStyle();
                                styleShapeInline = styleShapeInline.replaceAll("fillColor=[^;]*;","fillColor="+hexColor+";");
                                if(!styleShapeInline.contains("strokeColor"))
                                    styleShapeInline = styleShapeInline.concat("strokeColor="+hexColor+";");
                                else
                                    styleShapeInline = styleShapeInline.replaceAll("strokeColor=[^;]*;","strokeColor="+hexColor+";");
                                styleShapeInline = styleShapeInline.replaceAll("gradientColor=[^;]*;","gradientColor="+hexColor+";");

                                v1.setStyle(styleShapeInline);
                            }

                        }
                        graph2.getModel().endUpdate();



                        Object[] roots = graphComponentScalarEdge.getGraph().getChildCells(graphComponentScalarEdge.getGraph().getDefaultParent(), true, false);
                        graphComponentScalarEdge.getGraph().getModel().beginUpdate();

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

                            graphComponentScalarEdge.getGraph().setCellStyle(styleNode, root);

                            scalarInt++;
                        }
                        graphComponentScalarEdge.getGraph().getModel().endUpdate();
                    }
                    
                }catch (FileNotFoundException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else if(getColorEdge().equals("Scalar Color")){
            graph2.getModel().beginUpdate();
            
            
            switch (typeColorEdge) {
                case "Blue to Cyan":
                    colorScaleEdges = new BlueToCyanScale();
                    break;
                case "Blue to Yellow":
                    colorScaleEdges = new BlueToYellowScale();
                    break;
                case "Gray Scale":
                    colorScaleEdges = new GrayScale();
                    break;
                case "Green To White Scale":
                    colorScaleEdges = new GreenToWhiteScale();
                    break;
                case "Heated Object Scale":
                    colorScaleEdges = new HeatedObjectScale();
                    break;
                case "Linear Gray Scale":
                    colorScaleEdges = new LinearGrayScale();
                    break;
                case "Locs Scale":
                    colorScaleEdges = new LocsScale();
                    break;
                case "Green to Red":
                    colorScaleEdges = new GreenToRed();
                    break;
                //case "Blue Scale":
                  //  colorScaleEdges = new BlueScale();
                    //break;
                case "Rainbow Scale":
                    colorScaleEdges = new RainbowScale();
                    break;
                case "Blue Sky To Orange":
                    colorScaleEdges = new OrangeToBlueSky();
                    break;
                case "Orange To Blue Sky":
                    colorScaleEdges = new BlueSkyToOrange();
                    break;
                case "Blue To Red":
                    colorScaleEdges = new BlueToRed();
                    break;   
                case "Custom Color":
                    java.awt.Color color1 = javax.swing.JColorChooser.showDialog(graphComponent,
                    "Choose the Inicial Edge Color", java.awt.Color.BLACK);
                    java.awt.Color color2 = javax.swing.JColorChooser.showDialog(graphComponent,
                    "Choose the Final Edge Color", java.awt.Color.BLACK);
                    if(color1 != null && color2 != null)
                        colorScaleEdges = new CustomColor(color1,color2);
                    else
                        colorScaleEdges = new CustomColor(Color.BLACK,Color.BLACK);
                    break;
                default:
                    colorScaleEdges = new GreenToRed();
                    break;
            }
            
            CustomAttributes att = (CustomAttributes) listEdgesJgraph.get(0).getValue();
            eMaxColorEdges = att.getWeight();
            eMinColorEdges = att.getWeight();

            for(mxCell edge : listEdgesJgraph){
                att = (CustomAttributes) edge.getValue();
                float ia = att.getWeight();
                if(ia > eMaxColorEdges)
                    eMaxColorEdges = ia;
                if(ia < eMinColorEdges)
                    eMinColorEdges = ia;
            }
            
            //Edges
            for(mxCell ed : listEdgesJgraph)
            {
                    
                float maxColor = 1;
                float minColor = 0;

                //x = y * ( max - min) / (vMax - vMin) + min
                att = (CustomAttributes) ed.getValue();
                //float incolor = att.getWeight()  * (maxColor - minColor)/ (eMax - eMin) + minColor;
                float incolor = (att.getWeight() - eMinColorEdges) / (eMaxColorEdges - eMinColorEdges);

                if(incolor > maxColor)
                    incolor = maxColor;

                Color colorNode = colorScaleEdges.getColor((float)incolor);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleShapeEdge = ed.getStyle();
                int indexStyleEdge = styleShapeEdge.indexOf(mxConstants.STYLE_FILLCOLOR);
                String piece = "";
                if(indexStyleEdge != -1){
                    piece = styleShapeEdge.substring(indexStyleEdge, indexStyleEdge+18);
                }
                int indexStyleEdge2 = styleShapeEdge.indexOf(mxConstants.STYLE_STROKECOLOR);
                String piece2 = "";
                if(indexStyleEdge2 != -1){
                    piece2 = styleShapeEdge.substring(indexStyleEdge2, indexStyleEdge2+20);
                }
                if(!piece.isEmpty())
                    styleShapeEdge = styleShapeEdge.replace(piece,mxConstants.STYLE_FILLCOLOR+"="+hexColor+";");
                else
                    styleShapeEdge += mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                if(!piece2.isEmpty())
                    styleShapeEdge = styleShapeEdge.replace(piece2,mxConstants.STYLE_STROKECOLOR+"="+hexColor+";");
                else
                    styleShapeEdge += mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

                Object[] edd = {ed};
                graph2.setCellStyle(styleShapeEdge, edd);
               
                
            }
            
            graph2.getModel().endUpdate();
            
             //Scalar Edges
            Object[] roots = graphComponentScalarEdge.getGraph().getChildCells(graphComponentScalarEdge.getGraph().getDefaultParent(), true, false);
            graphComponentScalarEdge.getGraph().getModel().beginUpdate();
            
            boolean firstBool = true;
            String first = "";
            
            for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                String[] parts = idCell.split(" ");
                
                Color colorNode = colorScaleEdges.getColor(Float.parseFloat(parts[1]));
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);
                
                if(firstBool)
                {
                    first = hexColor;
                    firstBool = false;
                }

                String styleNode = "";
                if(eMinColorEdges == eMaxColorEdges)
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
                    cell.setValue((int)eMinColorEdges+"");
                }
                else if(cell.getId().equals("color 1")){
                    styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                    cell.setValue((int)eMaxColorEdges+"");
                }
                styleNode += mxConstants.STYLE_FONTSIZE+"=15;";
                styleNode += mxConstants.STYLE_FONTSTYLE+"="+mxConstants.FONT_BOLD+";";
                styleNode += mxConstants.STYLE_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                
                graphComponentScalarEdge.getGraph().setCellStyle(styleNode, root);
            }
            graphComponentScalarEdge.getGraph().getModel().endUpdate();

        }
        graphComponentScalarEdge.getGraph().repaint();
        graphComponentScalarEdge.getGraph().refresh();
    }
    
    
    public int resolution;
    
    public ArrayList<ArrayList> changeResolutionFile(ArrayList<ArrayList> matrizDate){
        ArrayList<ArrayList> matrizD = new ArrayList();
        ArrayList<Integer> coluna = new ArrayList();
        Integer minTimeGraph = Integer.parseInt(matrizDate.get(0).get(2).toString());
        for(ArrayList<Integer> column : matrizDate){
            coluna.add(column.get(0));
            coluna.add(column.get(1));
            Double iaa;
            if(minTimeGraph == 0)
                iaa = Math.floor((double)column.get(2)/(double)resolution)*resolution;
            else
                iaa = Math.ceil((double)column.get(2)/(double)resolution)*resolution;
            coluna.add(iaa.intValue());
           
            matrizD.add(coluna);
            coluna = new ArrayList();
        }
        
        return matrizD;
    }
    
    public Dimension getSize() {
        return this.size;
    }
    
  
    public void setSize(Dimension size){
        this.size = size;
    }
    
    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String TypeColor) {
        this.typeColor = TypeColor;
    }
  
    public ArrayList<NetInstance> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<NetInstance> nodes) {
        this.nodes = nodes;
    }
 
    
    public ArrayList<Integer> getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(ArrayList<Integer> selectedNodes) {
        this.selectedNodes = selectedNodes;
    }
   
    @Override
    public void setChanged() {
        super.setChanged();
    }
    
    public int limitColor(int color){
         if(color > 255)
               color = 255;
         else if(color < 0)
               color = 0;
        return color;
    }
    
    public ArrayList<NetEdge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<NetEdge> edges) {
        this.edges = edges;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public boolean isShowEdges() {
        return showEdges;
    }

    public void setShowEdges(boolean showEdges) {
        graph2.getModel().beginUpdate(); 
        if(showEdges)
        {
            for(mxCell ed : listEdgesJgraph)
            {
                ed.setVisible(true);
            }
        }
        else
        {          
            for(mxCell ed : listEdgesJgraph)
            {
                ed.setVisible(false);
            }
        }
        graph2.getModel().endUpdate();
        graph2.refresh();
        this.showEdges = showEdges;
    }
    
    
    public void setShowHasFBEdges(boolean showEdges) {
        graph2.getModel().beginUpdate(); 
        if(showEdges)
        {
            for(mxCell ed : listEdgesJgraph)
            {
                if(ed.getStyle().contains("strokeColor="+hasFBColor))
                    ed.setVisible(true);
                else
                    ed.setVisible(false);
            }
        }
        else
        {          
            for(mxCell ed : listEdgesJgraph)
            {
                if(ed.getStyle().contains("strokeColor="+noFBColor))
                    ed.setVisible(true);
                else
                    ed.setVisible(false);
            }
        }
        graph2.getModel().endUpdate();
        graph2.refresh();
        this.showEdges = showEdges;
    }
    
    private ArrayList<Integer> selectedEdges = new ArrayList(); 
    
    public void setShowWeight(boolean showWeight) {
        graph2.getModel().beginUpdate(); 
        if(showWeight)
        {
            for(mxCell ed : listEdgesJgraph)
            {
                String styleEdge = ed.getStyle();
                if(styleEdge.contains(mxConstants.STYLE_OPACITY+"=100"))
                {
                    Object[] root = {ed};
                    //show the weight
                    if(styleEdge.contains(mxConstants.STYLE_NOLABEL))
                        styleEdge = styleEdge.replace(mxConstants.STYLE_NOLABEL+"=1;",mxConstants.STYLE_NOLABEL+"=0;");
                    else
                        styleEdge += mxConstants.STYLE_NOLABEL+"=0;";
                    ed.setStyle(styleEdge);
                    graph2.orderCells(false,root);
                }
                else
                {
                    //hide the weight
                    if(styleEdge.contains(mxConstants.STYLE_NOLABEL))
                       styleEdge = styleEdge.replace(mxConstants.STYLE_NOLABEL+"=0;",mxConstants.STYLE_NOLABEL+"=1;");
                   else
                       styleEdge += mxConstants.STYLE_NOLABEL+"=1;";
                   ed.setStyle(styleEdge);
                }
            }
        }
        else
        {          
            for(mxCell ed : listEdgesJgraph)
            {
                String styleEdge = ed.getStyle();
                if(styleEdge.contains(mxConstants.STYLE_NOLABEL))
                    styleEdge = styleEdge.replace(mxConstants.STYLE_NOLABEL+"=0;",mxConstants.STYLE_NOLABEL+"=1;");
                else
                    styleEdge += mxConstants.STYLE_NOLABEL+"=1;";
                ed.setStyle(styleEdge);
            }
        }
        graph2.getModel().endUpdate();
        graph2.refresh();
        graph2.repaint();   
    }
    
    public void setShowInstanceWeight(boolean showInstanceWeight) {
        graph2.getModel().beginUpdate(); 
        if(showInstanceWeight)
        {
            //All nodes graph
            Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                
                String styleNode = cell.getStyle();
                if(styleNode.contains(mxConstants.STYLE_NOLABEL))
                    styleNode = styleNode.replace(mxConstants.STYLE_NOLABEL+"=1;",mxConstants.STYLE_NOLABEL+"=0;");
                else
                    styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                cell.setStyle(styleNode);
            }
        }
        else
        {          
            //All nodes graph
            Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                
                String styleNode = cell.getStyle();
                if(styleNode.contains(mxConstants.STYLE_NOLABEL))
                    styleNode = styleNode.replace(mxConstants.STYLE_NOLABEL+"=0;",mxConstants.STYLE_NOLABEL+"=1;");
                else
                    styleNode += mxConstants.STYLE_NOLABEL+"=1;";
                cell.setStyle(styleNode);
            }
        }
        this.showInstanceWeight = showInstanceWeight;
        graph2.getModel().endUpdate();
        graph2.refresh();
        graph2.repaint(); 
    }
    
    public boolean getShowInstanceWeight() {
        return this.showInstanceWeight;
    }
    
    public void setSelectSimilar(boolean selectSimilar) {
        this.selectSimilar = selectSimilar;
        setChanged();
    }
    
    public boolean getSelectSimilar() {
        return this.selectSimilar;
    }
    
    public void generateJson(String filename) {
        
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    public String getOpenFile() {
        return openFile;
    }

    public void setOpenFile(String openFile) {
        this.openFile = openFile;
    }
    
    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public boolean containsAtLeastOneTime(ArrayList<Integer> times){
        for(Integer time : times)
        {
            if(time >= inicialTime && time <= currentTime)
                return true;
        }
        return false;
    }
    
    public HashMap<String,Integer> edgeAging = new HashMap();
    public HashMap<String,Integer> nodeAging = new HashMap();
    public int maximumAgingValue = 10, divisorAgingValue = 100/maximumAgingValue;
    
    public boolean stop = true;
    
    public void changeSelectedEdges() {
        graph2.getModel().beginUpdate(); 
        
        if(stop){
            
            
            f.currentTime = 0;
            f.timeStructuralAnimationValue.setText(0+"");
            
            for(mxCell ed : listEdgesJgraph)
            {
                
                String[] ids = ed.getId().split(" ");
                
                mxCell orig = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(ids[0]+"");
                mxCell dest = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(ids[1]+"");
                
                String styleNode = ed.getStyle();
                styleNode = styleNode.replaceAll("opacity=[^;]*;","opacity=100;");
                ed.setStyle(styleNode);
                
                String style = orig.getStyle();
                style = style.replaceAll("opacity=[^;]*;","opacity=100;");
                orig.setStyle(style);
                
                style = dest.getStyle();
                style = style.replaceAll("opacity=[^;]*;","opacity=100;");
                dest.setStyle(style);
                
            }
            
            f.stateRunStrucutral = "run";
            ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/run.png"));
            f.runForceButton1.setIcon(icon);
            
            
            /*
            for(mxCell ed : listEdgesJgraph)
            {
                CustomAttributes att = (CustomAttributes) ed.getValue();
                att.setWeightTemp(1);
            }

            for(ArrayList<Integer> line : matrizData)
            {
                int idI = line.get(0);
                int idF = line.get(1);
                int time = line.get(2);
                
                mxCell ed = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(idI+" "+idF);
                if(ed == null)
                    ed = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(idF+" "+idI);

                CustomAttributes att = (CustomAttributes) ed.getValue();
                if(containsAtLeastOneTime(att.getTime()))
                {
                    ed.setVisible(true);
                }
                else
                    ed.setVisible(false);
                
                 
                if(time < inicialTime)
                    continue;
                if(time > currentTime)
                    continue;
                
                att.setWeightTemp(att.getWeightTemp()+1);
                
            }
        
            
            for(ArrayList<Integer> line : matrizData)
            {
                int idI = line.get(0);
                int idF = line.get(1);
                int time = line.get(2);
                
                mxCell ed = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(idI+" "+idF);
                if(ed == null)
                    ed = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(idF+" "+idI);

                CustomAttributes att = (CustomAttributes) ed.getValue();
                if(containsAtLeastOneTime(att.getTime()))
                {
                    
                    int weighttemp = 0;

                    if(!getColorEdge().equals("Original")){

                        float maxColor = 1;
                        weighttemp = att.getWeightTemp();
                        float incolor = (weighttemp - eMinColorEdges) / (eMaxColorEdges - eMinColorEdges);

                        if(incolor > maxColor)
                            incolor = maxColor;

                        Color colorNode = colorScaleEdges.getColor((float)incolor);
                        String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                        String styleShapeEdge = ed.getStyle();
                        int indexStyleEdge = styleShapeEdge.indexOf(mxConstants.STYLE_FILLCOLOR);
                        String piece = "";
                        if(indexStyleEdge != -1){
                            piece = styleShapeEdge.substring(indexStyleEdge, indexStyleEdge+18);
                        }
                        int indexStyleEdge2 = styleShapeEdge.indexOf(mxConstants.STYLE_STROKECOLOR);
                        String piece2 = "";
                        if(indexStyleEdge2 != -1){
                            piece2 = styleShapeEdge.substring(indexStyleEdge2, indexStyleEdge2+20);
                        }
                        if(!piece.isEmpty())
                            styleShapeEdge = styleShapeEdge.replace(piece,mxConstants.STYLE_FILLCOLOR+"="+hexColor+";");
                        else
                            styleShapeEdge += mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                        if(!piece2.isEmpty())
                            styleShapeEdge = styleShapeEdge.replace(piece2,mxConstants.STYLE_STROKECOLOR+"="+hexColor+";");
                        else
                            styleShapeEdge += mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

                        Object[] edd = {ed};
                        graph2.setCellStyle(styleShapeEdge, edd);
                    }

                    if(!getSizeEdge().equals("Original")){

                        float maxSize = 8f;
                        float minSize = 1f;

                        //x = y * ( max - min) / (vMax - vMin) + min
                        att = (CustomAttributes) ed.getValue();
                        if(weighttemp == 0)
                            weighttemp = att.getWeightTemp();
                        float insize = (weighttemp * (maxSize - minSize) / (eMaxSizeEdges - eMinSizeEdges)) + minSize;

                        BigDecimal insizeArredondando = new BigDecimal(insize); 
                        insizeArredondando = insizeArredondando.setScale(2, BigDecimal.ROUND_DOWN); 

                        insize = Float.parseFloat(insizeArredondando.toString());

                        String styleEdge = ed.getStyle();
                        int indexStyleEdge = styleEdge.indexOf(mxConstants.STYLE_STROKEWIDTH);
                        String piece = "";
                        if(indexStyleEdge != -1){
                            piece = styleEdge.substring(indexStyleEdge, styleEdge.length());
                            int indexFinalStyleEdge = piece.indexOf(";");
                            piece = styleEdge.substring(indexStyleEdge, indexFinalStyleEdge);
                        }
                        if(!piece.isEmpty())
                            styleEdge = styleEdge.replace(piece,mxConstants.STYLE_STROKEWIDTH+"="+insize+";");
                        else
                            styleEdge += mxConstants.STYLE_STROKEWIDTH+"="+insize+";";

                        Object[] edd = {ed};
                        graph2.setCellStyle(styleEdge, edd);
                    }
                }
            }
  
            for(mxCell ed : listEdgesJgraph)
            {
                CustomAttributes att = (CustomAttributes) ed.getValue();
                int time = att.getTime();
                if(time <= currentTime && time >= inicialTime)
                {
                    ed.setVisible(true);

                    int weighttemp = 0;

                    if(!getColorEdge().equals("Original")){

                        float maxColor = 1;
                        weighttemp = att.getWeightTemp();
                        float incolor = (weighttemp - eMinColorEdges) / (eMaxColorEdges - eMinColorEdges);

                        if(incolor > maxColor)
                            incolor = maxColor;

                        Color colorNode = colorScaleEdges.getColor((float)incolor);
                        String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                        String styleShapeEdge = ed.getStyle();
                        int indexStyleEdge = styleShapeEdge.indexOf(mxConstants.STYLE_FILLCOLOR);
                        String piece = "";
                        if(indexStyleEdge != -1){
                            piece = styleShapeEdge.substring(indexStyleEdge, indexStyleEdge+18);
                        }
                        int indexStyleEdge2 = styleShapeEdge.indexOf(mxConstants.STYLE_STROKECOLOR);
                        String piece2 = "";
                        if(indexStyleEdge2 != -1){
                            piece2 = styleShapeEdge.substring(indexStyleEdge2, indexStyleEdge2+20);
                        }
                        if(!piece.isEmpty())
                            styleShapeEdge = styleShapeEdge.replace(piece,mxConstants.STYLE_FILLCOLOR+"="+hexColor+";");
                        else
                            styleShapeEdge += mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                        if(!piece2.isEmpty())
                            styleShapeEdge = styleShapeEdge.replace(piece2,mxConstants.STYLE_STROKECOLOR+"="+hexColor+";");
                        else
                            styleShapeEdge += mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";

                        Object[] edd = {ed};
                        graph2.setCellStyle(styleShapeEdge, edd);
                    }

                    if(!getSizeEdge().equals("Original")){

                        float maxSize = 8f;
                        float minSize = 1f;

                        //x = y * ( max - min) / (vMax - vMin) + min
                        att = (CustomAttributes) ed.getValue();
                        if(weighttemp == 0)
                            weighttemp = att.getWeightTemp();
                        float insize = (weighttemp * (maxSize - minSize) / (eMaxSizeEdges - eMinSizeEdges)) + minSize;

                        BigDecimal insizeArredondando = new BigDecimal(insize); 
                        insizeArredondando = insizeArredondando.setScale(2, BigDecimal.ROUND_DOWN); 

                        insize = Float.parseFloat(insizeArredondando.toString());

                        String styleEdge = ed.getStyle();
                        int indexStyleEdge = styleEdge.indexOf(mxConstants.STYLE_STROKEWIDTH);
                        String piece = "";
                        if(indexStyleEdge != -1){
                            piece = styleEdge.substring(indexStyleEdge, styleEdge.length());
                            int indexFinalStyleEdge = piece.indexOf(";");
                            piece = styleEdge.substring(indexStyleEdge, indexFinalStyleEdge);
                        }
                        if(!piece.isEmpty())
                            styleEdge = styleEdge.replace(piece,mxConstants.STYLE_STROKEWIDTH+"="+insize+";");
                        else
                            styleEdge += mxConstants.STYLE_STROKEWIDTH+"="+insize+";";

                        Object[] edd = {ed};
                        graph2.setCellStyle(styleEdge, edd);
                    }
                }
                else
                {
                    ed.setVisible(false);
                }
            }
            */
        }
        else
        {
            
            ArrayList<mxCell> nodeUsing = new ArrayList();
                
            for(mxCell ed : listEdgesJgraph)
            {
                
                String[] ids = ed.getId().split(" ");
                
                mxCell orig = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(ids[0]+"");
                mxCell dest = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(ids[1]+"");
                
                CustomAttributes att = (CustomAttributes) ed.getValue();
                if(att.getTime().contains(currentTime))
                {
                    
                    edgeAging.put(ed.getId(), maximumAgingValue);
                    nodeAging.put(orig.getId()+"", maximumAgingValue);
                    nodeAging.put(dest.getId()+"", maximumAgingValue);
                    
                    nodeUsing.remove(orig);
                    nodeUsing.remove(dest);
                    
                }
                
                //Reduces the aging value for all edges
                int aging = edgeAging.get(ed.getId());
                String styleNode = ed.getStyle();
                styleNode = styleNode.replaceAll("opacity=[^;]*;","opacity="+aging*divisorAgingValue+";");
                ed.setStyle(styleNode);
                
                if(aging > 0)
                    aging--;
                edgeAging.put(ed.getId(), aging);

                if(!nodeUsing.contains(orig))
                {
                    aging = nodeAging.get(orig.getId());
                    String style = orig.getStyle();
                    style = style.replaceAll("opacity=[^;]*;","opacity="+aging*divisorAgingValue+";");
                    orig.setStyle(style);
                    nodeUsing.add(orig);

                    if(aging > 0)
                        aging--;
                    nodeAging.put(orig.getId(), aging);
                }
                if(!nodeUsing.contains(dest))
                {
                    aging = nodeAging.get(dest.getId());
                    String style = dest.getStyle();
                    style = style.replaceAll("opacity=[^;]*;","opacity="+aging*divisorAgingValue+";");
                    dest.setStyle(style);
                    nodeUsing.add(dest);

                    if(aging > 0)
                        aging--;
                    nodeAging.put(dest.getId(), aging);
                }

            }
            
        }
        
        graph2.getModel().endUpdate();
        //graph2.refresh();
        graphComponent.refresh();
    }

    JSpinner timeSpinner = new JSpinner();
    


    /**
     * @return the showNodesNotSelected
     */
    public boolean isShowNodesNotSelected() {
        return showNodesNotSelected;
    }

    /**
     * @param showNodesNotSelected the showNodesNotSelected to set
     */
    public void setShowNodesNotSelected(boolean showNodesNotSelected) {
        this.showNodesNotSelected = showNodesNotSelected;
    }

    /**
     * @return the selectedEdges
     */
    public ArrayList<Integer> getSelectedEdges() {
        return selectedEdges;
    }

    /**
     * @param selectedEdges the selectedEdges to set
     */
    public void setSelectedEdges(ArrayList<Integer> selectedEdges) {
        this.selectedEdges = selectedEdges;
    }

    /**
     * @return the matrizData
     */
    public ArrayList<ArrayList> getMatrizData() {
        return matrizData;
    }

    /**
     * @param matrizData the matrizData to set
     */
    public void setMatrizData(ArrayList<ArrayList> matrizData) {
        this.matrizData = matrizData;
    }

    /**
     * @return the resolution
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * @param resolution the resolution to set
     */
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
    
    public mxGraphComponent graphComponent;
    
    
    public int maxId = 0;
    private MainForm f;
    
    public NetLayout(String filename, ArrayList<ArrayList> matrizData, MainForm f){
        
        this.f = f;
        color = "Original";
        colorEdge = "Original";
        sizeEdge = "Original";
        typeColor = "Gray Scale";
        typeColorEdge = "Gray Scale";
        layoutJGraphX = "RandomLayout";
        sizeNode = "Original";
        currentTime = 0;
        
        this.matrizData = matrizData;
        
        listEdgesJgraph = new ArrayList();
        
        showNodes = true;
        showEdges = true;
        showEdgesHorizontalLines = true;
        
        openFile = filename;
        
        mxCell v1,v2;
        //Make the label used in InlineNodeAttribute work in mxCellSate
        graph2 = new mxGraph();
        
        graph2.getModel().beginUpdate();  

        final Object parent = graph2.getDefaultParent();
        
        try
        {
            
            JGraphStyle style = NetLayoutInlineNew.JGraphStyle.ROUNDED;
            
            
            Map<String, Object> styleEdge = graph2.getStylesheet().getDefaultEdgeStyle();
            
            String styleShape = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
            styleShape += mxConstants.STYLE_RESIZABLE+"=0;";
            styleShape += mxConstants.STYLE_RESIZABLE+"=0;";
            styleShape += mxConstants.STYLE_OPACITY+"=100;";
            styleShape += mxConstants.STYLE_NOLABEL+"=1;";
            
            
            String styleShapeEdge = mxConstants.STYLE_STROKEWIDTH+"=1;";
            styleShapeEdge += mxConstants.EDGESTYLE_ORTHOGONAL+"=1;";
            styleShapeEdge += mxConstants.STYLE_ROUNDED+"=1;";
            styleShapeEdge += mxConstants.STYLE_MOVABLE+"=0;";
            styleShapeEdge += mxConstants.STYLE_EDITABLE+"=0;";
            styleShapeEdge += mxConstants.STYLE_CLONEABLE+"=0;";
            styleShapeEdge += mxConstants.STYLE_OPACITY+"=100;";
            styleShapeEdge += mxConstants.STYLE_ENDARROW+"=0;";
            styleShapeEdge += mxConstants.STYLE_NOLABEL+"=1;";
            styleShapeEdge += mxConstants.STYLE_LABEL_BACKGROUNDCOLOR+"=#ffffff;";
            styleShapeEdge += mxConstants.STYLE_LABEL_BORDERCOLOR+"=#000000;";

            Map<String,Object> a = ((mxGraphModel)graph2.getModel()).getCells();
            a.clear();
            boolean firstTime = true;
            for(ArrayList<Integer> column : matrizData){

                int time = (column.get(2));
                
                if(column.get(0) > maxId)
                    maxId = column.get(0);
                if(column.get(1) > maxId)
                    maxId = column.get(1);
                
                if(firstTime){
                    minTime = time;
                    firstTime = false;
                }
                
                //Source node
                v1 = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(column.get(0).toString());
                if(v1 == null)
                {
                    Random rand = new Random();
                    int randomNumX = rand.nextInt((1200 - 0) + 1) + 0;
                    int randomNumY = rand.nextInt((700 - 0) + 1) + 0;
                    CustomAttributes att = new CustomAttributes(time, 1, column.get(0).toString(), false, true);
                    att.setIsFirst(true);
                    v1 = (mxCell) graph2.insertVertex(parent , column.get(0).toString() , att , randomNumX, randomNumY , 15 , 15, styleShape);
                }
                else
                {
                    CustomAttributes att = (CustomAttributes) v1.getValue();
                    att.setWeight(v1.getEdgeCount());
                    att.setExternalWeight(v1.getEdgeCount());
                    v1.setValue(att);
                }
               

                //Target Node
                v2 = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(column.get(1).toString());
                if(v2 == null)
                {
                   Random rand = new Random();
                    int randomNumX = rand.nextInt((1200 - 0) + 1) + 0;
                    int randomNumY = rand.nextInt((700 - 0) + 1) + 0;
                    CustomAttributes att = new CustomAttributes(time, 1, column.get(1).toString(), false, true);
                    att.setIsFirst(false);
                    v2 = (mxCell) graph2.insertVertex(parent, column.get(1).toString(), att, randomNumX, randomNumY , 15 , 15, styleShape);
                }
                else
                {
                    CustomAttributes att = (CustomAttributes) v2.getValue();
                    att.setWeight(v2.getEdgeCount());
                    att.setExternalWeight(v2.getEdgeCount());
                    v2.setValue(att);
                }
                
                //Edge
                boolean existEdge = false;
                
                mxCell myCell = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(column.get(0)+" "+column.get(1));
                if(myCell == null)
                {
                    myCell = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(column.get(1)+" "+column.get(0));
                    if(myCell != null)
                    {
                        CustomAttributes att = (CustomAttributes) myCell.getValue();
                        att.setTime(time);
                        att.setWeight(att.getWeight() + 1);
                        att.setExternalWeight(att.getExternalWeight()+ 1);
                        myCell.setValue(att);
                        existEdge = true;
                        
                    }
                }
                else
                {
                    if(myCell.isEdge())
                    {
                        CustomAttributes att = (CustomAttributes) myCell.getValue();
                        att.setTime(time);
                        att.setWeight(att.getWeight() + 1);
                        att.setExternalWeight(att.getExternalWeight()+ 1);
                        myCell.setValue(att);
                        existEdge = true;
                    }
                }
                
                if(!existEdge)
                {
                    CustomAttributes att = new CustomAttributes(column.get(2), 1, "", true, false);
                    mxCell ed = (mxCell) graph2.insertEdge(parent,column.get(0)+" "+column.get(1),att, v1, v2,styleShapeEdge);
                    listEdgesJgraph.add(ed);
                    edgeAging.put(ed.getId(), 0);
                }

                if (time > maxTime) maxTime = time;
                
            }
            
            maxId++;
            
            //Scalar Color
            String styleNode = mxConstants.STYLE_MOVABLE+"=0;";
            styleNode += mxConstants.STYLE_EDITABLE+"=0;";
            styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
            styleNode += mxConstants.STYLE_NOLABEL+"=0;";
            styleNode += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_CENTER+";";
            styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
            //String hexColor = "#"+Integer.toHexString(Color.CYAN.getRGB()).substring(2);
            String hexColor = "#C3D9FF";
            styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
            //styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
           
            String styleNodeLeft = mxConstants.STYLE_MOVABLE+"=0;";
            styleNodeLeft += mxConstants.STYLE_EDITABLE+"=0;";
            styleNodeLeft += mxConstants.STYLE_RESIZABLE+"=0;";
            styleNodeLeft += mxConstants.STYLE_NOLABEL+"=0;";
            styleNodeLeft += mxConstants.STYLE_FONTCOLOR+"="+Color.BLACK+";";
            styleNodeLeft += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_LEFT+";";
            styleNodeLeft += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
            styleNodeLeft +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
            //styleNodeLeft +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
            
            String styleNodeRight = mxConstants.STYLE_MOVABLE+"=0;";
            styleNodeRight += mxConstants.STYLE_EDITABLE+"=0;";
            styleNodeRight += mxConstants.STYLE_RESIZABLE+"=0;";
            styleNodeRight += mxConstants.STYLE_NOLABEL+"=0;";
            styleNodeRight += mxConstants.STYLE_FONTCOLOR+"="+Color.BLACK+";";
            styleNodeRight += mxConstants.STYLE_VERTICAL_LABEL_POSITION+"="+mxConstants.ALIGN_RIGHT+";";
            styleNodeRight += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
            styleNodeRight +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
            //styleNodeRight +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
            
            
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
            
            graphComponentScalar = new mxGraphComponent(graphScalar);
            
            
            mxGraph graphScalarEdge = new mxGraph();
            graphScalarEdge.insertVertex(null, "color 0", "", 3 , 3, 30 , 20, styleNodeLeft);
            graphScalarEdge.insertVertex(null, "color 0.1", "", 33 , 3, 30 , 20, styleNode);
            graphScalarEdge.insertVertex(null, "color 0.2", "", 63 , 3, 30 , 20, styleNode);
            graphScalarEdge.insertVertex(null, "color 0.3", "", 93 , 3, 30 , 20, styleNode);
            graphScalarEdge.insertVertex(null, "color 0.4", "", 123 , 3, 30 , 20, styleNode);
            graphScalarEdge.insertVertex(null, "color 0.5", "", 153 , 3, 30 , 20, styleNode);
            graphScalarEdge.insertVertex(null, "color 0.6", "", 183 , 3, 30 , 20, styleNode);
            graphScalarEdge.insertVertex(null, "color 0.7", "", 213 , 3, 30 , 20, styleNode);
            graphScalarEdge.insertVertex(null, "color 0.8", "", 243 , 3, 30 , 20, styleNode);
            graphScalarEdge.insertVertex(null, "color 0.9", "", 273 , 3, 30 , 20, styleNode);
            graphScalarEdge.insertVertex(null, "color 1", "", 303 , 3, 30 , 20, styleNodeRight);
            
            graphComponentScalarEdge = new mxGraphComponent(graphScalarEdge);
            
            
            styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
            styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+Color.BLACK+";";
            
            mxGraph graphScalarNodeSize = new mxGraph();
            graphScalarNodeSize.insertVertex(null, "0", "", 3 , 3, 15 , 15, styleNode);
            graphScalarNodeSize.insertVertex(null, "2", "", 63 , 3, 15 , 15, styleNode);
            graphScalarNodeSize.insertVertex(null, "4", "", 123 , 3, 15 , 15, styleNode);
            graphScalarNodeSize.insertVertex(null, "6", "", 183 , 3, 15 , 15, styleNode);
            graphScalarNodeSize.insertVertex(null, "8", "", 243 , 3, 15 , 15, styleNode);
            graphScalarNodeSize.insertVertex(null, "10", "", 303 , 3, 15 , 15, styleNode);
            
            graphComponentScalarNodeSize = new mxGraphComponent(graphScalarNodeSize);
            
            
        }
        finally
        {
            graph2.getModel().endUpdate();
        }
        
        graph2.setAllowDanglingEdges(false);
        graph2.setCellsEditable(false);
        graph2.setCellsDisconnectable(false);
        graph2.cellsOrdered(graph2.getChildVertices(graph2.getDefaultParent()), false);
        
        graphComponent = new mxGraphComponent(graph2);

    }

    public mxGraphComponent graphComponentScalar,graphComponentScalarEdge, graphComponentScalarNodeSize;
    
    /**
     * @return the showNodes
     */
    public boolean isShowNodes() {
        return showNodes;
    }

    
    
    public void componentesConexas()
    {
        mxGraph g = graph2;
        ArrayList<mxICell> usedNodes = new ArrayList();
        ArrayList<mxICell> usedNodesPerLevel = new ArrayList();
        Object[] roots = g.getChildCells(g.getDefaultParent(), true, false);
        System.out.println("Componentes Conexas:");
        //ArrayList<mxCell> listEdgesNew = new ArrayList();
        //listEdgesNew.addAll(listEdgesJgraph);
        
        ArrayList<String> allNodes = new ArrayList();
        
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;
            String idCell = cell.getId();
            allNodes.add(idCell);
        }
        
        Object root = roots[0];
        mxCell cell = (mxCell) root;
        String idCell = cell.getId();
        
        ArrayList<String> nodesUsed = new ArrayList();
        ArrayList<String> AllnodesUsed = new ArrayList();
        String escreverArquivo = "";
        
        ArrayList<ArrayList> componentesConexas = new ArrayList();
        //Iterator<String> iter = listEdgesJgraph.iterator();
        ArrayList<mxCell> arestasParaRemover = new ArrayList();
        
        
        int i = 0;
        while(AllnodesUsed.size() != allNodes.size())
        {
            if(i == 0)
                nodesUsed.add(idCell);
            
            else
            {
                ArrayList<String> difNodes = new ArrayList();
                difNodes.addAll(allNodes);
                difNodes.removeAll(AllnodesUsed);
                nodesUsed = new ArrayList();
                nodesUsed.add(difNodes.get(0));
            }
            escreverArquivo += i+": ["+ nodesUsed.get(0)+", ";
            System.out.print(i+": ["+ nodesUsed.get(0)+", ");
            boolean adiciona = true;       
            ArrayList<String> nodesUsedBackup = new ArrayList();
            while(adiciona)
            {
                boolean valida = false;
                //nodesUsed.addAll(nodesUsedBackup);
                for(String backup : nodesUsedBackup)
                {
                    if(!nodesUsed.contains(backup))
                        nodesUsed.add(backup);
                }

                for (String id : nodesUsed) {

                    for(mxCell ed : listEdgesJgraph)
                    {
                        //if((ed.getSource().getId().equals(id) || ed.getTarget().getId().equals(id)) && !arestasParaRemover.contains(ed))
                            //arestasParaRemover.add(ed);
                        
                        if(ed.getTarget().getId().equals(id) && !nodesUsedBackup.contains(ed.getSource().getId()))
                        {
                            nodesUsedBackup.add(ed.getSource().getId());
                            escreverArquivo += ""+ed.getSource().getId()+", ";
                            System.out.print(""+ed.getSource().getId()+", ");
                            
                            valida = true;
                        }

                        if(ed.getSource().getId().equals(id) && !nodesUsedBackup.contains(ed.getTarget().getId()))
                        {
                            nodesUsedBackup.add(ed.getTarget().getId());
                            escreverArquivo += ""+ed.getTarget().getId()+", ";
                            System.out.print(""+ed.getTarget().getId()+", ");
                            valida = true;
                        }
                    }
                    
                    //listEdgesNew.removeAll(arestasParaRemover);
                }
                if(!valida)
                {
                    adiciona = false;
                    break;
                }
            }
            escreverArquivo += "]\r\n";
            System.out.println("]");
            AllnodesUsed.addAll(nodesUsed);
            //componentesConexas.add(nodesUsedBackup);
            i++;
            if(i == 1)
                break;
            
        }
        
        FileHandler.gravaArquivo(escreverArquivo, ".//Estatistica_Twitter//ComponentesConexas.txt", false);
        
        int x = 1;
        
    }
    
    /**
     * @param showNodes the showNodes to set
     */
    public void setShowNodes(boolean showNodes) {
        
        graph2.getModel().beginUpdate(); 
        if(showNodes)
        {
            Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                String[] parts = idCell.split(" ");
                
                if(parts.length != 2 )
                {
                    String styleShape = cell.getStyle();
                    styleShape = styleShape.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=100;");
                    cell.setStyle(styleShape);
                }
            }
        }
        else
        {    
            
            Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
            for (Object root1 : roots) {
                mxCell cell = (mxCell) root1;
                String idCell = cell.getId();
                String[] parts = idCell.split(" ");
                  
                if(parts.length != 2 )
                {
                    String styleShape = cell.getStyle();
                    styleShape = styleShape.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                    cell.setStyle(styleShape);
                }
            }
        }
        graph2.getModel().endUpdate();
        graph2.repaint();
        graph2.refresh();
        this.showNodes = showNodes;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @return the layoutJGraphX
     */
    public String getLayoutJGraphX() {
        return layoutJGraphX;
    }

    /**
     * @param layoutJGraphX the layoutJGraphX to set
     */
    public void setLayoutJGraphX(String layoutJGraphX) {
        this.layoutJGraphX = layoutJGraphX;
    }
    
    public void moveNodesRandom()
    {
        Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            Random rand = new Random();
            int randomNumX = rand.nextInt((1200 - 0) + 1) + 0;
            int randomNumY = rand.nextInt((700 - 0) + 1) + 0;
            graph2.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
            graph2.moveCells(root, randomNumX , randomNumY);
        }
    }
    
    
    /**
     * @return the typeColorEdge
     */
    public String getTypeColorEdge() {
        return typeColorEdge;
    }

    /**
     * @param typeColorEdge the typeColorEdge to set
     */
    public void setTypeColorEdge(String typeColorEdge) {
        this.typeColorEdge = typeColorEdge;
    }

    /**
     * @return the colorEdge
     */
    public String getColorEdge() {
        return colorEdge;
    }

    /**
     * @param colorEdge the colorEdge to set
     */
    public void setColorEdge(String colorEdge) {
        this.colorEdge = colorEdge;
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
     * @return the sizeEdge
     */
    public String getSizeEdge() {
        return sizeEdge;
    }

    /**
     * @param sizeEdge the sizeEdge to set
     */
    public void setSizeEdge(String sizeEdge) {
        this.sizeEdge = sizeEdge;
    }

    /**
     * @return the typeGraph
     */
    public boolean isTypeGraph() {
        return typeGraph;
    }

    /**
     * @param typeGraph the typeGraph to set
     */
    public void setTypeGraph(boolean typeGraph) {
        this.typeGraph = typeGraph;
    }

    /**
     * @return the showEdgesHorizontalLines
     */
    public boolean isShowEdgesHorizontalLines() {
        return showEdgesHorizontalLines;
    }

    /**
     * @param showEdgesHorizontalLines the showEdgesHorizontalLines to set
     */
    public void setShowEdgesHorizontalLines(boolean showEdgesHorizontalLines) {
        
        this.showEdgesHorizontalLines = showEdgesHorizontalLines;
    }

    /**
     * @return the inicialTime
     */
    public int getInicialTime() {
        return inicialTime;
    }

    /**
     * @param inicialTime the inicialTime to set
     */
    public void setInicialTime(int inicialTime) {
        this.inicialTime = inicialTime;
    }

    /**
     * @return the minTime
     */
    public int getMinTime() {
        return minTime;
    }

    /**
     * @param minTime the minTime to set
     */
    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    double getVariance()
    {
        double mean = getMean();
        double temp = 0;
        for(int a :degreeNodes)
            temp += (a-mean)*(a-mean);
        return temp/nodesCount;
    }
    
    int[] degreeNodes;
    double[] degreeNormalizedNodes;
    int nodesCount; 
    
    double getMean()
    {
        double sum = 0;
        for(double a : degreeNodes)
            sum += a;
        return sum/nodesCount;
    }
    
    double getStdDev()
    {
        return Math.sqrt(getVariance());
    }

    public void strongWeakTies() throws IOException
   {
       ArrayList<String> strongTies = new ArrayList();
       ArrayList<String> weakTies = new ArrayList();
       String resultingFile = "";
       int threshold = 38;
       
        //Edges
        for(mxCell ed : listEdgesJgraph)
        {
            CustomAttributes att = (CustomAttributes) ed.getValue();
            
            
            if(att.getWeight() > threshold)//Strong Tie = 0
            {
                resultingFile += ed.getId() + " 0\r\n";
                strongTies.add(att.getLabel());
            }
            else//Weak Tie = 1
            { 
                resultingFile += ed.getId() + " 1\r\n";
                weakTies.add(att.getLabel());
            }
        }
        
        // Cria arquivo
        File file = new File("teste.txt");

        // Se o arquivo nao existir, ele gera
        if (!file.exists()) {
            file.createNewFile();
        }
        
        // Prepara para escrever no arquivo
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        // Escreve e fecha arquivo
        bw.write(resultingFile);
        bw.close();
        
        int x = 1;
   }
    
    public HashMap<Integer, java.util.List<InlineNodeAttribute>> communities;
    public ArrayList<InlineNodeAttribute> listAttNodesMainForm;

    public void setShowIntraEdgesCommunities(boolean selected) {
        
        ArrayList<mxCell> listIntraEdges = new ArrayList();
        
        for(int j = 0; j < communities.size(); j++)
        {

            
            for(Object cell : this.listEdgesJgraph)
            {
                mxCell edge = (mxCell) cell;
                boolean achou1 = false, achou2 = false;
                
                InlineNodeAttribute attConvertido = null,attConvertido2 = null;
                for(InlineNodeAttribute a : listAttNodesMainForm)
                {
                    if(a.getId_original() == Integer.parseInt(edge.getSource().getId()))
                    {
                        attConvertido = a;
                        achou1 = true;
                    }
                    if(a.getId_original() == Integer.parseInt(edge.getTarget().getId()))
                    {
                        attConvertido2 = a;
                        achou2 = true;
                    }
                    if(achou1 && achou2)
                        break;
                }
                
                if(communities.get(j).contains(attConvertido) && communities.get(j).contains(attConvertido2))
                {
                    listIntraEdges.add(edge);
                    //conteudoArquivo2 += edge.getSource().getId() + " "+ edge.getTarget().getId()+" "+ (j+1) + System.getProperty("line.separator");
                    //conteudoArquivo2 += edge.getTarget().getId() + " "+ edge.getSource().getId()+" "+ (j+1) + System.getProperty("line.separator");
                }
            }
            
           
        }
                
        graph2.getModel().beginUpdate(); 
        if(selected)
        {
            for(mxCell ed : listEdgesJgraph)
            {
                if(listIntraEdges.contains(ed))
                    ed.setVisible(true);
                else
                    ed.setVisible(false);
            }
        }
        else
        {          
            for(mxCell ed : listEdgesJgraph)
            {
                ed.setVisible(false);
            }
        }
        graph2.getModel().endUpdate();
        graph2.refresh();
        this.showEdges = showEdges;
        
    }

    public void setShowInterEdgesCommunities(boolean selected) {
        
        
        ArrayList<Object> listInterEdges = new ArrayList();
        
        for(int j = 0; j < communities.size(); j++)
        {

            
            for(Object cell : this.listEdgesJgraph)
            {
                mxCell edge = (mxCell) cell;
                boolean achou1 = false, achou2 = false;
                
                InlineNodeAttribute attConvertido = null,attConvertido2 = null;
                for(InlineNodeAttribute a : listAttNodesMainForm)
                {
                    if(a.getId_original() == Integer.parseInt(edge.getSource().getId()))
                    {
                        attConvertido = a;
                        achou1 = true;
                    }
                    if(a.getId_original() == Integer.parseInt(edge.getTarget().getId()))
                    {
                        attConvertido2 = a;
                        achou2 = true;
                    }
                    if(achou1 && achou2)
                        break;
                }
                
                if(communities.get(j).contains(attConvertido) && communities.get(j).contains(attConvertido2))
                {
                    listInterEdges.add(edge);
                }
            }
            
           
        }
                
        graph2.getModel().beginUpdate(); 
        if(selected)
        {
            for(mxCell ed : listEdgesJgraph)
            {
                if(!listInterEdges.contains(ed))
                    ed.setVisible(true);
                else
                    ed.setVisible(false);
            }
        }
        else
        {          
            for(mxCell ed : listEdgesJgraph)
            {
                ed.setVisible(false);
            }
        }
        graph2.getModel().endUpdate();
        graph2.refresh();
        this.showEdges = showEdges;
        
    }
    
    public HashMap<String,Integer> edgeWeight = new HashMap();
        

    public void changeWeightEdges() {
        
        graph2.getModel().beginUpdate(); 
        if(getWeightEdge().equals("Degree"))
        {
            for(mxCell ed : listEdgesJgraph)
            {
                CustomAttributes att = (CustomAttributes) ed.getValue();
                att.setWeight(att.getExternalWeight()); //recupera o degree salvo em external weight
                
            }
        }
        else if(getWeightEdge().equals("Weight File"))
        {
            JFileChooser openDialog = new JFileChooser();
            String filename = "";
            filename = f.getPathDataset();

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
                    int BUFFER_SIZE = 1000;

                    file = new BufferedReader(new FileReader(new File(filename)));
                    
                    file.mark(BUFFER_SIZE);
                    
                    String line = file.readLine();
                    String[] tokens = line.split(" ");
                    String tmp, strLine = "";
                                        
                    if(tokens.length != 3) // node_origin | node_destiny | weight
                    {
                        JOptionPane.showMessageDialog(null,"File format different from the expected. Check the Information button for details.","Error",JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                        file.reset();
                        while ((tmp = file.readLine()) != null)
                        {
                            strLine = tmp;
                            String[] tokens2 = strLine.split(" ");
                            
                            //create file of weighted edges to calculate commmunities
                            String edge_string = tokens2[0]+" "+tokens2[1];
                            String edge_string_inv = tokens2[1]+" "+tokens2[0];
                            if(edgeWeight.get(edge_string) == null)
                            {
                                edgeWeight.put(edge_string_inv, Integer.parseInt(tokens2[2]));
                                
                            }
                            else
                            {
                                edgeWeight.put(edge_string, Integer.parseInt(tokens2[2]));
                            }
                            
                            boolean achou = false;
                            mxCell myCell = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(tokens2[0]+" "+tokens2[1]);
                            if(myCell == null)
                            {
                                myCell = (mxCell) ((mxGraphModel)graph2.getModel()).getCell(tokens2[1]+" "+tokens2[0]);
                                if(myCell != null)
                                {
                                    achou = true;
                                }
                            }
                            else
                                achou = true;
                            if(achou)
                            {
                                CustomAttributes att = (CustomAttributes) myCell.getValue();
                                att.setWeight(Integer.parseInt(tokens2[2]));
                            }
                        }
                    }
                }catch (FileNotFoundException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        graph2.getModel().endUpdate();
        graph2.refresh();
    }

    public void setWeightEdge(String toString) {
        this.weightEdge = toString;
    }

    public String getWeightEdge() {
        return this.weightEdge;
    }
    
    public HashMap<String, String> changeLabelNodes(){
        HashMap<String, String> newIdList = new HashMap();
        
        JFileChooser openDialog = new JFileChooser();
        String filename = "";
        filename = f.getPathDataset();

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
                String[] tokens = line.split(" ");
                String tmp, strLine = "";

                if(tokens.length != 2)
                {
                    JOptionPane.showMessageDialog(null,"File format different from the expected. Check the Information button for details.","Error",JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    newIdList.put(tokens[0],tokens[1]);
                    while ((tmp = file.readLine()) != null)
                    {
                        strLine = tmp;
                        String[] tokens2 = strLine.split(" ");
                        newIdList.put(tokens2[0],tokens2[1]);
                    }

                    graph2.getModel().beginUpdate(); 

                    //All nodes graph
                    Object[] roots = graph2.getChildCells(graph2.getDefaultParent(), true, false);
                    for (Object root1 : roots) {
                        mxCell cell = (mxCell) root1;
                        CustomAttributes att = (CustomAttributes) cell.getValue();
                        if(newIdList.get(att.getLabel()) != null)
                        {
                            att.setLabel(newIdList.get(att.getLabel()));
                        }
                    }
                    graph2.getModel().endUpdate();
                    graph2.refresh();
                    graph2.repaint(); 
                }
            }catch (FileNotFoundException ex) {
               Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(OpenDataSetDialog.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
        return newIdList;
    }
}