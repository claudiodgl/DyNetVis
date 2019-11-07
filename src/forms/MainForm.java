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

package forms;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraphSelectionModel;
import com.mxgraph.view.mxGraphView;
import communities.InfoMap;
import communities.SLM_Louvain;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.filechooser.FileNameExtensionFilter;
import layout.InlineNodeAttribute;
import layout.NetLayout;
import layout.NetLayoutInlineNew;
import layout.NetLayoutMatrix;

/**
 *
 * @author Claudio Linhares
 */
public class MainForm extends JFrame implements Runnable {

    public String nameFile;
    
    public JPanel getTemporalPanel()
    {
        return temporalPanel;
    }

    										
    private ViewInlinePanel view2;
    private ViewLinePanel view3;
    private ViewMatrixPanel viewMatrix;
    private int zoom = 15,spacing = 1,resolution = 1;
    public Color weak, strong;
    
    public final int qtdElementosPlotadosMax = 200;
    public final int qtdElementosPlotadosMin = 0;
    
    public MainForm() {
        this.scalarComboModel = new DefaultComboBoxModel();
        this.edgeComboModel = new DefaultComboBoxModel();
        this.view = new ViewPanel();
        this.view2 = new ViewInlinePanel();
        this.view3 = new ViewLinePanel();
        this.viewMatrix = new ViewMatrixPanel();
        
        //weak = new Color(204,204,255);
        //strong = new Color(153,153,255);
        
        weak = new Color(240,240,240);
        strong = new Color(204,204,204);
        
        initComponents();
        
        //menuBar.add(new JLabel("   DyNetVis ....  "));
        
        timeAnimationValue.setDisabledTextColor(Color.BLACK);
        timeStructuralAnimationValue.setDisabledTextColor(Color.BLACK);
        openDataSetDialog.getRootPane().setOpaque(false);
        openDataSetDialog.getContentPane().setBackground(strong);
        openDataSetDialog.setBackground(strong);
        
        expImg.getRootPane().setOpaque(false);
        expImg.getContentPane().setBackground(strong);
        expImg.setBackground(strong);
        expImg.cancelButton1.setBackground(strong);
        expImg.pointsButton.setBackground(strong);
        
        openDataSetDialog.OKButton1.setBackground(strong);
        openDataSetDialog.cancelButton1.setBackground(strong);
        openDataSetDialog.pointsButton.setBackground(strong);
        
        openDataSetDialog.sourcePanel.setBackground(weak);
        openDataSetDialog.jPanel1.setBackground(weak);
        openDataSetDialog.buttonPanel1.setBackground(weak);
        openDataSetDialog.maximumTime.setBackground(weak);
        openDataSetDialog.minimumTime.setBackground(weak);
        
        jTabbedPane.setIconAt(2, new javax.swing.ImageIcon(getClass().getResource("/imgs/matrixLabel.png")));
        jTabbedPane.setIconAt(1, new javax.swing.ImageIcon(getClass().getResource("/imgs/structuralLabel.png")));
        jTabbedPane.setIconAt(0, new javax.swing.ImageIcon(getClass().getResource("/imgs/temporalButton3.png")));
        
        ImageIcon img = new javax.swing.ImageIcon(getClass().getResource("/imgs/iconDyNetVis.png"));
        setIconImage(img.getImage());
        
        //GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //Dimension screenDimension = env.getMaximumWindowBounds().getSize();
        //Dimension screenAdjusted = new Dimension((int)screenDimension.getWidth()-10,(int)scrollPanel.getHeight());
       // scrollPanel.setPreferredSize(screenAdjusted);
        //inlinePanel.setPreferredSize(screenAdjusted);
        
        //screenAdjusted = new Dimension((int)screenDimension.getWidth()-10,(int)zoomPanelTemporal.getHeight());
        
       // zoomPanelTemporal.setPreferredSize(screenAdjusted);
        //zoomPanelStructure.setPreferredSize(screenAdjusted);
        
        //HIDE Funcionalities that are not ready - to publish
		                
        jTabbedPane.setEnabledAt(0, false);
        jTabbedPane.setEnabledAt(1, false);
        jTabbedPane.setEnabledAt(2, false);
        //jTabbedPane.remove(2);
        jLabel21.setVisible(false);
        jButton3.setVisible(false);
        //jLabel11.setVisible(false);
        //backgroundButton1.setVisible(false);
        detectCommunitiesStructural.setVisible(false);
        compConexas.setVisible(false);
        settings.setVisible(false);
        exportImgCentrality.setVisible(false);
        exportImgCommunity.setVisible(false);
        //exportImgMatrix.setVisible(false);
        exportImgRobots1.setVisible(false);
        exportImgStream.setVisible(false);
        //jLabel19.setVisible(false);
        //edgeSamplingCombo.setVisible(false);
        //orderComboBox.removeItemAt(orderComboBox.getItemCount() - 1);
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectionButtonGroup = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        tam = new javax.swing.ButtonGroup();
        controlPanel = new javax.swing.JPanel();
        jTabbedPane = new javax.swing.JTabbedPane();
        msvPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        orderComboBox = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        formCombo1 = new javax.swing.JComboBox(this.scalarComboModel);
        jLabel19 = new javax.swing.JLabel();
        edgeSamplingCombo = new javax.swing.JComboBox(this.scalarComboModel);
        jLabel7 = new javax.swing.JLabel();
        nodeSizeCombo1 = new javax.swing.JComboBox(this.scalarComboModel);
        jPanel14 = new javax.swing.JPanel();
        tamWhite = new javax.swing.JRadioButton();
        tamColored = new javax.swing.JRadioButton();
        tamOpacity = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        colorLabel4 = new javax.swing.JLabel();
        scalarCombo7 = new javax.swing.JComboBox();
        colorLabel5 = new javax.swing.JLabel();
        scalarCombo8 = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        colorLabel6 = new javax.swing.JLabel();
        scalarCombo5 = new javax.swing.JComboBox();
        colorLabel7 = new javax.swing.JLabel();
        scalarCombo6 = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        backgroundButton1 = new javax.swing.JButton();
        temporalPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        zoomPanelTemporal = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        cnoSettingsPanel = new javax.swing.JPanel();
        multiLevelLabel = new javax.swing.JLabel();
        leftArrowCommunity = new javax.swing.JButton();
        stateMultiLevel = new javax.swing.JLabel();
        RightArrowCommunity = new javax.swing.JButton();
        showEdgesIntraCommunitiesCheckBox = new javax.swing.JCheckBox();
        showEdgesInterCommunitiesCheckBox = new javax.swing.JCheckBox();
        temporalSettings = new javax.swing.JPanel();
        showNodesCheckBox1 = new javax.swing.JCheckBox();
        showEdgesTemporalCheckBox = new javax.swing.JCheckBox();
        showEdgesCheckBox3 = new javax.swing.JCheckBox();
        findNodePanel = new javax.swing.JPanel();
        idNodeToFind = new javax.swing.JTextField();
        scrollToNodeCenter = new javax.swing.JCheckBox();
        findNodeTemporal = new javax.swing.JButton();
        zoomAndScroolTemporalPanel = new javax.swing.JPanel();
        zoomToTemporal = new javax.swing.JButton();
        zoomToLineGraph = new javax.swing.JButton();
        zoomToFit = new javax.swing.JButton();
        zoomOutButtonTemporal = new javax.swing.JButton();
        zoomInButtonTemporal = new javax.swing.JButton();
        zoomToDefault = new javax.swing.JButton();
        structurePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        nodeSizeCombo = new javax.swing.JComboBox(this.scalarComboModel);
        jLabel4 = new javax.swing.JLabel();
        EdgeStrokeCombo = new javax.swing.JComboBox(this.scalarComboModel);
        jLabel2 = new javax.swing.JLabel();
        layoutCombo = new javax.swing.JComboBox(this.scalarComboModel);
        jPanel2 = new javax.swing.JPanel();
        colorLabel = new javax.swing.JLabel();
        scalarCombo1 = new javax.swing.JComboBox();
        colorLabel2 = new javax.swing.JLabel();
        scalarCombo2 = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        colorLabel1 = new javax.swing.JLabel();
        scalarCombo3 = new javax.swing.JComboBox();
        colorLabel3 = new javax.swing.JLabel();
        scalarCombo4 = new javax.swing.JComboBox();
        jPanel24 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        backgroundButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        zoomPanelStructure = new javax.swing.JPanel();
        animationSettings1 = new javax.swing.JPanel();
        agingLabel1 = new javax.swing.JLabel();
        agingjSlider1 = new javax.swing.JSlider();
        speedLabel1 = new javax.swing.JLabel();
        speedjSlider1 = new javax.swing.JSlider();
        runForceButton1 = new javax.swing.JButton();
        stopButton1 = new javax.swing.JButton();
        timeLabel1 = new javax.swing.JLabel();
        timeStructuraljSlider = new javax.swing.JSlider();
        timeStructuralAnimationValue = new javax.swing.JTextField();
        structuralSettings = new javax.swing.JPanel();
        depthSelectionLevel = new javax.swing.JLabel();
        depthSelectionLevelSpinner = new javax.swing.JSpinner();
        showNodesCheckBox = new javax.swing.JCheckBox();
        showEdgesCheckBox = new javax.swing.JCheckBox();
        showEdgesWeightCheckBox = new javax.swing.JCheckBox();
        showInstanceWeightCheckBox = new javax.swing.JCheckBox();
        detectCommunitiesStructural = new javax.swing.JButton();
        compConexas = new javax.swing.JButton();
        zoomAndScroolStructural = new javax.swing.JPanel();
        zoomToFit1 = new javax.swing.JButton();
        zoomOutButton = new javax.swing.JButton();
        zoomInButton = new javax.swing.JButton();
        zoomToDefault1 = new javax.swing.JButton();
        matrixPanel = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        nodeOrderMatrixLabel = new javax.swing.JLabel();
        nodeOrderMatrixComboBox = new javax.swing.JComboBox();
        nodeColorMatrixLabel = new javax.swing.JLabel();
        nodeColorMatrixComboBox = new javax.swing.JComboBox();
        zoomPanelMatrix = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        animationSettings = new javax.swing.JPanel();
        agingLabel = new javax.swing.JLabel();
        agingjSlider = new javax.swing.JSlider();
        speedLabel = new javax.swing.JLabel();
        speedjSlider = new javax.swing.JSlider();
        runButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        timeLabel = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        timeAnimationValue = new javax.swing.JTextField();
        settingsMatrix = new javax.swing.JPanel();
        upperTriangularMatrix = new javax.swing.JCheckBox();
        zoomAndScroolMatrix = new javax.swing.JPanel();
        zoomToFit7 = new javax.swing.JButton();
        zoomOutButtonMatrix = new javax.swing.JButton();
        zoomInButtonMatrix = new javax.swing.JButton();
        zoomToDefault7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        removeSelection = new javax.swing.JButton();
        numberOfSelectedNodesText = new javax.swing.JLabel();
        CountOfSelectedNodes = new javax.swing.JLabel();
        seeSelectedNodes = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        File = new javax.swing.JMenu();
        openDatasetMenuItem = new javax.swing.JMenuItem();
        exportImageMenu = new javax.swing.JMenu();
        exportImgTemporal = new javax.swing.JMenuItem();
        exportImgStructural = new javax.swing.JMenuItem();
        exportImgMatrix = new javax.swing.JMenuItem();
        exportImgCentrality = new javax.swing.JMenuItem();
        exportImgCommunity = new javax.swing.JMenuItem();
        exportImgStream = new javax.swing.JMenuItem();
        exportImgRobots1 = new javax.swing.JMenuItem();
        settings = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();
        Data = new javax.swing.JMenu();
        statistic = new javax.swing.JMenuItem();
        DynamicProcesses = new javax.swing.JMenu();
        randomWalker = new javax.swing.JMenuItem();
        epidemiologyProcesses = new javax.swing.JMenuItem();
        Help = new javax.swing.JMenu();
        help = new javax.swing.JMenuItem();
        about = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DyNetVis: A system for visualization of dynamic networks");
        setBackground(weak);
        setMinimumSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        controlPanel.setBackground(weak);
        controlPanel.setLayout(new java.awt.BorderLayout());

        jTabbedPane.setBackground(strong);
        jTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane.setToolTipText("");
        jTabbedPane.setAutoscrolls(true);
        jTabbedPane.setMaximumSize(null);
        jTabbedPane.setPreferredSize(new java.awt.Dimension(1437, 925));
        jTabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPaneMouseClicked(evt);
            }
        });

        msvPanel.setBackground(new java.awt.Color(255, 255, 255));
        msvPanel.setEnabled(false);
        msvPanel.setMinimumSize(new java.awt.Dimension(300, 59));
        msvPanel.setPreferredSize(new java.awt.Dimension(1600, 130));
        msvPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(null);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel4.setBackground(strong);
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setAutoscrolls(true);
        jPanel4.setMaximumSize(null);
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jPanel6.setBackground(strong);
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setMaximumSize(null);
        jPanel6.setPreferredSize(new java.awt.Dimension(480, 80));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jPanel19.setBackground(strong);
        jPanel19.setPreferredSize(new java.awt.Dimension(370, 75));
        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Node Order ");
        jLabel6.setEnabled(false);
        jPanel19.add(jLabel6);

        orderComboBox.setBackground(weak);
        orderComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Appearance", "Random", "Lexicographic", "Degree", "Recurrent Neighbors", "CNO", "File" }));
        orderComboBox.setEnabled(false);
        orderComboBox.setPreferredSize(new java.awt.Dimension(100, 27));
        orderComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderComboBoxActionPerformed(evt);
            }
        });
        jPanel19.add(orderComboBox);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Node Form");
        jLabel8.setEnabled(false);
        jLabel8.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel19.add(jLabel8);

        formCombo1.setBackground(weak);
        formCombo1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Circular", "Square" }));
        formCombo1.setEnabled(false);
        formCombo1.setMaximumSize(new java.awt.Dimension(50, 27));
        formCombo1.setMinimumSize(new java.awt.Dimension(50, 27));
        formCombo1.setPreferredSize(new java.awt.Dimension(100, 27));
        formCombo1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                formCombo1ItemStateChanged(evt);
            }
        });
        formCombo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formCombo1MouseClicked(evt);
            }
        });
        formCombo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formCombo1ActionPerformed(evt);
            }
        });
        jPanel19.add(formCombo1);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Edge Sampling");
        jLabel19.setEnabled(false);
        jLabel19.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel19.add(jLabel19);

        edgeSamplingCombo.setBackground(weak);
        edgeSamplingCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Random", "AR", "EOD" }));
        edgeSamplingCombo.setEnabled(false);
        edgeSamplingCombo.setMaximumSize(new java.awt.Dimension(50, 27));
        edgeSamplingCombo.setMinimumSize(new java.awt.Dimension(50, 27));
        edgeSamplingCombo.setPreferredSize(new java.awt.Dimension(100, 27));
        edgeSamplingCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                edgeSamplingComboItemStateChanged(evt);
            }
        });
        edgeSamplingCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                edgeSamplingComboMouseClicked(evt);
            }
        });
        edgeSamplingCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edgeSamplingComboActionPerformed(evt);
            }
        });
        jPanel19.add(edgeSamplingCombo);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Node Size");
        jLabel7.setEnabled(false);
        jLabel7.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel19.add(jLabel7);

        nodeSizeCombo1.setBackground(weak);
        nodeSizeCombo1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Small", "Big" }));
        nodeSizeCombo1.setEnabled(false);
        nodeSizeCombo1.setMaximumSize(new java.awt.Dimension(50, 27));
        nodeSizeCombo1.setMinimumSize(new java.awt.Dimension(50, 27));
        nodeSizeCombo1.setPreferredSize(new java.awt.Dimension(100, 27));
        nodeSizeCombo1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                nodeSizeCombo1ItemStateChanged(evt);
            }
        });
        nodeSizeCombo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nodeSizeCombo1MouseClicked(evt);
            }
        });
        nodeSizeCombo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeSizeCombo1ActionPerformed(evt);
            }
        });
        jPanel19.add(nodeSizeCombo1);

        jPanel6.add(jPanel19);

        jPanel14.setBackground(strong);
        jPanel14.setPreferredSize(new java.awt.Dimension(90, 75));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        tamWhite.setBackground(strong);
        tam.add(tamWhite);
        tamWhite.setText("TAM White");
        tamWhite.setEnabled(false);
        tamWhite.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tamWhite.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        tamWhite.setPreferredSize(new java.awt.Dimension(90, 23));
        tamWhite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tamWhiteActionPerformed(evt);
            }
        });
        jPanel14.add(tamWhite);

        tamColored.setBackground(strong);
        tam.add(tamColored);
        tamColored.setText("TAM Color");
        tamColored.setEnabled(false);
        tamColored.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tamColored.setPreferredSize(new java.awt.Dimension(90, 23));
        tamColored.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tamColoredActionPerformed(evt);
            }
        });
        jPanel14.add(tamColored);

        tamOpacity.setBackground(strong);
        tamOpacity.setText("TAM Opacity");
        tamOpacity.setEnabled(false);
        tamOpacity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tamOpacityActionPerformed(evt);
            }
        });
        jPanel14.add(tamOpacity);

        jPanel6.add(jPanel14);

        jPanel4.add(jPanel6);

        jPanel8.setBackground(strong);
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setPreferredSize(new java.awt.Dimension(370, 80));
        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        colorLabel4.setText("Node Inline");
        colorLabel4.setEnabled(false);
        colorLabel4.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel8.add(colorLabel4);

        scalarCombo7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Original", "Color", "Color Std Dev", "Random Color", "Random Line", "Metadata File" }));
        scalarCombo7.setEnabled(false);
        scalarCombo7.setMaximumSize(new java.awt.Dimension(85, 27));
        scalarCombo7.setMinimumSize(new java.awt.Dimension(85, 27));
        scalarCombo7.setPreferredSize(new java.awt.Dimension(90, 27));
        scalarCombo7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scalarCombo7ItemStateChanged(evt);
            }
        });
        scalarCombo7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scalarCombo7MouseClicked(evt);
            }
        });
        scalarCombo7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scalarCombo7ActionPerformed(evt);
            }
        });
        jPanel8.add(scalarCombo7);

        colorLabel5.setText("Inline Color");
        colorLabel5.setEnabled(false);
        colorLabel5.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel8.add(colorLabel5);

        scalarCombo8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gray Scale", "Green to Red", "Blue To Red", "Rainbow Scale", "Blue to Cyan", "Blue to Yellow", "Green To White Scale", "Heated Object Scale", "Linear Gray Scale", "Locs Scale", "Orange To Blue Sky", "Blue Sky To Orange", "Custom Color" }));
        scalarCombo8.setEnabled(false);
        scalarCombo8.setMaximumSize(new java.awt.Dimension(85, 27));
        scalarCombo8.setMinimumSize(new java.awt.Dimension(85, 27));
        scalarCombo8.setPreferredSize(new java.awt.Dimension(90, 27));
        scalarCombo8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scalarCombo8ItemStateChanged(evt);
            }
        });
        scalarCombo8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scalarCombo8MouseClicked(evt);
            }
        });
        scalarCombo8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scalarCombo8ActionPerformed(evt);
            }
        });
        jPanel8.add(scalarCombo8);

        jPanel4.add(jPanel8);

        jPanel9.setBackground(strong);
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setPreferredSize(new java.awt.Dimension(370, 80));
        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        colorLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colorLabel6.setText("Edge Color");
        colorLabel6.setEnabled(false);
        colorLabel6.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel9.add(colorLabel6);

        scalarCombo5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Original", "Color", "Metadata File", "Random Color", "Random Edge", "Metadata Without Time" }));
        scalarCombo5.setEnabled(false);
        scalarCombo5.setMaximumSize(new java.awt.Dimension(90, 27));
        scalarCombo5.setMinimumSize(new java.awt.Dimension(85, 27));
        scalarCombo5.setPreferredSize(new java.awt.Dimension(90, 27));
        scalarCombo5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scalarCombo5ItemStateChanged(evt);
            }
        });
        scalarCombo5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scalarCombo5MouseClicked(evt);
            }
        });
        scalarCombo5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scalarCombo5ActionPerformed(evt);
            }
        });
        jPanel9.add(scalarCombo5);

        colorLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colorLabel7.setText("Edge Scalar");
        colorLabel7.setEnabled(false);
        colorLabel7.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel9.add(colorLabel7);

        scalarCombo6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gray Scale", "Green to Red", "Blue To Red", "Blue to Yellow", "Linear Gray Scale", "Orange To Blue Sky", "Blue Sky To Orange", "Custom Color" }));
        scalarCombo6.setEnabled(false);
        scalarCombo6.setMaximumSize(new java.awt.Dimension(90, 27));
        scalarCombo6.setMinimumSize(new java.awt.Dimension(85, 27));
        scalarCombo6.setPreferredSize(new java.awt.Dimension(90, 27));
        scalarCombo6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scalarCombo6ItemStateChanged(evt);
            }
        });
        scalarCombo6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scalarCombo6MouseClicked(evt);
            }
        });
        scalarCombo6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scalarCombo6ActionPerformed(evt);
            }
        });
        jPanel9.add(scalarCombo6);

        jLabel20.setText("          ");
        jPanel9.add(jLabel20);

        jPanel4.add(jPanel9);

        jPanel23.setBackground(strong);
        jPanel23.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel23.setPreferredSize(new java.awt.Dimension(130, 80));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Set Background");
        jLabel11.setEnabled(false);
        jLabel11.setPreferredSize(null);
        jPanel23.add(jLabel11);

        backgroundButton1.setBackground(weak);
        backgroundButton1.setText("Background");
        backgroundButton1.setEnabled(false);
        backgroundButton1.setFocusable(false);
        backgroundButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        backgroundButton1.setPreferredSize(null);
        backgroundButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        backgroundButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backgroundButton1ActionPerformed(evt);
            }
        });
        jPanel23.add(backgroundButton1);

        jPanel4.add(jPanel23);

        jScrollPane2.setViewportView(jPanel4);

        msvPanel.add(jScrollPane2, java.awt.BorderLayout.PAGE_START);

        temporalPanel.setBackground(new java.awt.Color(255, 255, 255));
        temporalPanel.setLayout(new java.awt.BorderLayout());
        msvPanel.add(temporalPanel, java.awt.BorderLayout.CENTER);

        jScrollPane4.setBorder(null);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(1715, 80));

        zoomPanelTemporal.setBackground(new java.awt.Color(255, 255, 255));
        zoomPanelTemporal.setRequestFocusEnabled(false);
        zoomPanelTemporal.setLayout(new java.awt.BorderLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(1715, 65));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        cnoSettingsPanel.setBackground(new java.awt.Color(255, 255, 255));
        cnoSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("CNO Settings"));
        cnoSettingsPanel.setEnabled(false);

        multiLevelLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        multiLevelLabel.setText("Level");
        multiLevelLabel.setEnabled(false);
        multiLevelLabel.setPreferredSize(new java.awt.Dimension(60, 14));
        cnoSettingsPanel.add(multiLevelLabel);

        leftArrowCommunity.setBackground(weak);
        leftArrowCommunity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/setaEsquerda.png"))); // NOI18N
        leftArrowCommunity.setToolTipText("Previous");
        leftArrowCommunity.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        leftArrowCommunity.setEnabled(false);
        leftArrowCommunity.setFocusable(false);
        leftArrowCommunity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        leftArrowCommunity.setMargin(null);
        leftArrowCommunity.setMaximumSize(new java.awt.Dimension(10, 23));
        leftArrowCommunity.setMinimumSize(new java.awt.Dimension(10, 23));
        leftArrowCommunity.setPreferredSize(new java.awt.Dimension(25, 27));
        leftArrowCommunity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        leftArrowCommunity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftArrowCommunityActionPerformed(evt);
            }
        });
        cnoSettingsPanel.add(leftArrowCommunity);

        stateMultiLevel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        stateMultiLevel.setText("0/0");
        stateMultiLevel.setToolTipText("");
        stateMultiLevel.setEnabled(false);
        stateMultiLevel.setPreferredSize(new java.awt.Dimension(30, 14));
        stateMultiLevel.setRequestFocusEnabled(false);
        cnoSettingsPanel.add(stateMultiLevel);

        RightArrowCommunity.setBackground(weak);
        RightArrowCommunity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/setaDireita.png"))); // NOI18N
        RightArrowCommunity.setToolTipText("Next");
        RightArrowCommunity.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        RightArrowCommunity.setEnabled(false);
        RightArrowCommunity.setFocusable(false);
        RightArrowCommunity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RightArrowCommunity.setMargin(null);
        RightArrowCommunity.setMaximumSize(new java.awt.Dimension(10, 23));
        RightArrowCommunity.setMinimumSize(new java.awt.Dimension(10, 23));
        RightArrowCommunity.setPreferredSize(new java.awt.Dimension(25, 27));
        RightArrowCommunity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        RightArrowCommunity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RightArrowCommunityActionPerformed(evt);
            }
        });
        cnoSettingsPanel.add(RightArrowCommunity);

        showEdgesIntraCommunitiesCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        showEdgesIntraCommunitiesCheckBox.setText(" Intra Comm.");
        showEdgesIntraCommunitiesCheckBox.setEnabled(false);
        showEdgesIntraCommunitiesCheckBox.setFocusable(false);
        showEdgesIntraCommunitiesCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showEdgesIntraCommunitiesCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        showEdgesIntraCommunitiesCheckBox.setPreferredSize(new java.awt.Dimension(100, 30));
        showEdgesIntraCommunitiesCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showEdgesIntraCommunitiesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEdgesIntraCommunitiesCheckBoxActionPerformed(evt);
            }
        });
        cnoSettingsPanel.add(showEdgesIntraCommunitiesCheckBox);

        showEdgesInterCommunitiesCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        showEdgesInterCommunitiesCheckBox.setText(" Inter Comm.");
        showEdgesInterCommunitiesCheckBox.setEnabled(false);
        showEdgesInterCommunitiesCheckBox.setFocusable(false);
        showEdgesInterCommunitiesCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showEdgesInterCommunitiesCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        showEdgesInterCommunitiesCheckBox.setPreferredSize(new java.awt.Dimension(100, 30));
        showEdgesInterCommunitiesCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showEdgesInterCommunitiesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEdgesInterCommunitiesCheckBoxActionPerformed(evt);
            }
        });
        cnoSettingsPanel.add(showEdgesInterCommunitiesCheckBox);

        jPanel13.add(cnoSettingsPanel);

        temporalSettings.setBackground(new java.awt.Color(255, 255, 255));
        temporalSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Temporal Settings"));
        temporalSettings.setEnabled(false);
        temporalSettings.setMinimumSize(new java.awt.Dimension(330, 56));
        temporalSettings.setPreferredSize(new java.awt.Dimension(370, 60));

        showNodesCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        showNodesCheckBox1.setText("Show Nodes");
        showNodesCheckBox1.setEnabled(false);
        showNodesCheckBox1.setFocusable(false);
        showNodesCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showNodesCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        showNodesCheckBox1.setPreferredSize(new java.awt.Dimension(100, 30));
        showNodesCheckBox1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showNodesCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNodesCheckBox1ActionPerformed(evt);
            }
        });
        temporalSettings.add(showNodesCheckBox1);

        showEdgesTemporalCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        showEdgesTemporalCheckBox.setText("Show Edges");
        showEdgesTemporalCheckBox.setEnabled(false);
        showEdgesTemporalCheckBox.setFocusable(false);
        showEdgesTemporalCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showEdgesTemporalCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        showEdgesTemporalCheckBox.setPreferredSize(new java.awt.Dimension(100, 30));
        showEdgesTemporalCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showEdgesTemporalCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEdgesTemporalCheckBoxActionPerformed(evt);
            }
        });
        temporalSettings.add(showEdgesTemporalCheckBox);

        showEdgesCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        showEdgesCheckBox3.setText("Show Nodes Lines");
        showEdgesCheckBox3.setEnabled(false);
        showEdgesCheckBox3.setFocusable(false);
        showEdgesCheckBox3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showEdgesCheckBox3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        showEdgesCheckBox3.setPreferredSize(new java.awt.Dimension(130, 30));
        showEdgesCheckBox3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showEdgesCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEdgesCheckBox3ActionPerformed(evt);
            }
        });
        temporalSettings.add(showEdgesCheckBox3);

        jPanel13.add(temporalSettings);

        findNodePanel.setBackground(new java.awt.Color(255, 255, 255));
        findNodePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Find Node"));
        findNodePanel.setEnabled(false);

        idNodeToFind.setToolTipText("");
        idNodeToFind.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        idNodeToFind.setEnabled(false);
        idNodeToFind.setPreferredSize(new java.awt.Dimension(50, 20));
        idNodeToFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idNodeToFindActionPerformed(evt);
            }
        });
        findNodePanel.add(idNodeToFind);

        scrollToNodeCenter.setBackground(new java.awt.Color(255, 255, 255));
        scrollToNodeCenter.setSelected(true);
        scrollToNodeCenter.setText("Center");
        scrollToNodeCenter.setEnabled(false);
        findNodePanel.add(scrollToNodeCenter);

        findNodeTemporal.setBackground(weak);
        findNodeTemporal.setText("OK");
        findNodeTemporal.setToolTipText("");
        findNodeTemporal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        findNodeTemporal.setEnabled(false);
        findNodeTemporal.setFocusable(false);
        findNodeTemporal.setMaximumSize(new java.awt.Dimension(100, 21));
        findNodeTemporal.setPreferredSize(new java.awt.Dimension(40, 22));
        findNodeTemporal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findNodeTemporalActionPerformed(evt);
            }
        });
        findNodePanel.add(findNodeTemporal);

        jPanel13.add(findNodePanel);

        zoomAndScroolTemporalPanel.setBackground(new java.awt.Color(255, 255, 255));
        zoomAndScroolTemporalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Zoom and Scroll"));
        zoomAndScroolTemporalPanel.setEnabled(false);

        zoomToTemporal.setBackground(weak);
        zoomToTemporal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/temporalButton3.png"))); // NOI18N
        zoomToTemporal.setToolTipText("View Temporal Layout");
        zoomToTemporal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomToTemporal.setEnabled(false);
        zoomToTemporal.setFocusable(false);
        zoomToTemporal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomToTemporal.setMargin(new java.awt.Insets(2, 14, 2, 500));
        zoomToTemporal.setPreferredSize(new java.awt.Dimension(50, 27));
        zoomToTemporal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomToTemporal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomToTemporalActionPerformed(evt);
            }
        });
        zoomAndScroolTemporalPanel.add(zoomToTemporal);

        zoomToLineGraph.setBackground(weak);
        zoomToLineGraph.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/LineGraphButton2.png"))); // NOI18N
        zoomToLineGraph.setToolTipText("View Line Graph Layout");
        zoomToLineGraph.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomToLineGraph.setEnabled(false);
        zoomToLineGraph.setFocusable(false);
        zoomToLineGraph.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomToLineGraph.setMargin(null);
        zoomToLineGraph.setPreferredSize(new java.awt.Dimension(50, 27));
        zoomToLineGraph.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomToLineGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomToLineGraphActionPerformed(evt);
            }
        });
        zoomAndScroolTemporalPanel.add(zoomToLineGraph);

        zoomToFit.setBackground(weak);
        zoomToFit.setText("Zoom Fit");
        zoomToFit.setToolTipText("Fit to the size of screen");
        zoomToFit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomToFit.setEnabled(false);
        zoomToFit.setFocusable(false);
        zoomToFit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomToFit.setPreferredSize(null);
        zoomToFit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomToFit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomToFitActionPerformed(evt);
            }
        });
        zoomAndScroolTemporalPanel.add(zoomToFit);

        zoomOutButtonTemporal.setBackground(weak);
        zoomOutButtonTemporal.setText("-");
        zoomOutButtonTemporal.setToolTipText("Zoom out");
        zoomOutButtonTemporal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomOutButtonTemporal.setEnabled(false);
        zoomOutButtonTemporal.setMaximumSize(new java.awt.Dimension(45, 22));
        zoomOutButtonTemporal.setMinimumSize(new java.awt.Dimension(45, 22));
        zoomOutButtonTemporal.setPreferredSize(new java.awt.Dimension(45, 22));
        zoomOutButtonTemporal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutButtonTemporalActionPerformed(evt);
            }
        });
        zoomAndScroolTemporalPanel.add(zoomOutButtonTemporal);

        zoomInButtonTemporal.setBackground(weak);
        zoomInButtonTemporal.setText("+");
        zoomInButtonTemporal.setToolTipText("Zoom in");
        zoomInButtonTemporal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomInButtonTemporal.setEnabled(false);
        zoomInButtonTemporal.setMaximumSize(new java.awt.Dimension(45, 22));
        zoomInButtonTemporal.setMinimumSize(new java.awt.Dimension(45, 22));
        zoomInButtonTemporal.setPreferredSize(new java.awt.Dimension(45, 22));
        zoomInButtonTemporal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInButtonTemporalActionPerformed(evt);
            }
        });
        zoomAndScroolTemporalPanel.add(zoomInButtonTemporal);

        zoomToDefault.setBackground(weak);
        zoomToDefault.setText("Zoom Default");
        zoomToDefault.setToolTipText("Set the default zoom size");
        zoomToDefault.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomToDefault.setEnabled(false);
        zoomToDefault.setFocusable(false);
        zoomToDefault.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomToDefault.setMargin(new java.awt.Insets(2, 14, 3, 14));
        zoomToDefault.setPreferredSize(null);
        zoomToDefault.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomToDefaultActionPerformed(evt);
            }
        });
        zoomAndScroolTemporalPanel.add(zoomToDefault);

        jPanel13.add(zoomAndScroolTemporalPanel);

        zoomPanelTemporal.add(jPanel13, java.awt.BorderLayout.PAGE_START);

        jScrollPane4.setViewportView(zoomPanelTemporal);

        msvPanel.add(jScrollPane4, java.awt.BorderLayout.PAGE_END);

        jTabbedPane.addTab("Temporal", msvPanel);

        structurePanel.setBackground(new java.awt.Color(255, 255, 255));
        structurePanel.setEnabled(false);
        structurePanel.setPreferredSize(new java.awt.Dimension(1432, 900));
        structurePanel.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setBorder(null);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel3.setBackground(strong);
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setMaximumSize(null);
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jPanel1.setBackground(strong);
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setPreferredSize(new java.awt.Dimension(380, 80));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Node Size");
        jLabel3.setEnabled(false);
        jLabel3.setPreferredSize(new java.awt.Dimension(70, 14));
        jLabel3.setRequestFocusEnabled(false);
        jPanel1.add(jLabel3);

        nodeSizeCombo.setBackground(weak);
        nodeSizeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Original", "Degree", "Big" }));
        nodeSizeCombo.setEnabled(false);
        nodeSizeCombo.setMaximumSize(new java.awt.Dimension(50, 27));
        nodeSizeCombo.setMinimumSize(new java.awt.Dimension(50, 27));
        nodeSizeCombo.setPreferredSize(new java.awt.Dimension(100, 27));
        nodeSizeCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                nodeSizeComboItemStateChanged(evt);
            }
        });
        nodeSizeCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nodeSizeComboMouseClicked(evt);
            }
        });
        nodeSizeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeSizeComboActionPerformed(evt);
            }
        });
        jPanel1.add(nodeSizeCombo);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Edge Stroke");
        jLabel4.setEnabled(false);
        jLabel4.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel1.add(jLabel4);

        EdgeStrokeCombo.setBackground(weak);
        EdgeStrokeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Original", "Stroke Edges" }));
        EdgeStrokeCombo.setEnabled(false);
        EdgeStrokeCombo.setMaximumSize(new java.awt.Dimension(50, 27));
        EdgeStrokeCombo.setMinimumSize(new java.awt.Dimension(50, 27));
        EdgeStrokeCombo.setPreferredSize(new java.awt.Dimension(100, 27));
        EdgeStrokeCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                EdgeStrokeComboItemStateChanged(evt);
            }
        });
        EdgeStrokeCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EdgeStrokeComboMouseClicked(evt);
            }
        });
        EdgeStrokeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EdgeStrokeComboActionPerformed(evt);
            }
        });
        jPanel1.add(EdgeStrokeCombo);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Set Order");
        jLabel2.setEnabled(false);
        jLabel2.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel1.add(jLabel2);

        layoutCombo.setBackground(weak);
        layoutCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "RandomLayout", "mxFastOrganicLayout", "mxCircleLayout", "mxCompactTreeLayout", "mxHierarchicalLayout" }));
        layoutCombo.setEnabled(false);
        layoutCombo.setMaximumSize(new java.awt.Dimension(50, 27));
        layoutCombo.setMinimumSize(new java.awt.Dimension(50, 27));
        layoutCombo.setPreferredSize(new java.awt.Dimension(100, 27));
        layoutCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                layoutComboItemStateChanged(evt);
            }
        });
        layoutCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                layoutComboMouseClicked(evt);
            }
        });
        layoutCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                layoutComboActionPerformed(evt);
            }
        });
        jPanel1.add(layoutCombo);

        jPanel3.add(jPanel1);

        jPanel2.setBackground(strong);
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(370, 80));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        colorLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colorLabel.setText("Node Color");
        colorLabel.setEnabled(false);
        colorLabel.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel2.add(colorLabel);

        scalarCombo1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Original", "Color", "Color Std Dev", "Random Color", "Metadata File" }));
        scalarCombo1.setEnabled(false);
        scalarCombo1.setMaximumSize(new java.awt.Dimension(90, 27));
        scalarCombo1.setMinimumSize(new java.awt.Dimension(85, 27));
        scalarCombo1.setPreferredSize(new java.awt.Dimension(90, 27));
        scalarCombo1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scalarCombo1ItemStateChanged(evt);
            }
        });
        scalarCombo1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scalarCombo1MouseClicked(evt);
            }
        });
        scalarCombo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scalarCombo1ActionPerformed(evt);
            }
        });
        jPanel2.add(scalarCombo1);

        colorLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colorLabel2.setText("Node Scalar");
        colorLabel2.setEnabled(false);
        colorLabel2.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel2.add(colorLabel2);

        scalarCombo2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gray Scale", "Green to Red", "Blue To Red", "Rainbow Scale", "Blue to Cyan", "Blue to Yellow", "Green To White Scale", "Heated Object Scale", "Linear Gray Scale", "Locs Scale", "Orange To Blue Sky", "Blue Sky To Orange", "Custom Color" }));
        scalarCombo2.setEnabled(false);
        scalarCombo2.setMaximumSize(new java.awt.Dimension(90, 27));
        scalarCombo2.setMinimumSize(new java.awt.Dimension(85, 27));
        scalarCombo2.setPreferredSize(new java.awt.Dimension(90, 27));
        scalarCombo2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scalarCombo2ItemStateChanged(evt);
            }
        });
        scalarCombo2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scalarCombo2MouseClicked(evt);
            }
        });
        scalarCombo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scalarCombo2ActionPerformed(evt);
            }
        });
        jPanel2.add(scalarCombo2);

        jPanel3.add(jPanel2);

        jPanel5.setBackground(strong);
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setPreferredSize(new java.awt.Dimension(370, 80));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        colorLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colorLabel1.setText("Edge Color");
        colorLabel1.setEnabled(false);
        colorLabel1.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel5.add(colorLabel1);

        scalarCombo3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Original", "Color", "Random Color", "Metadata Without Time" }));
        scalarCombo3.setEnabled(false);
        scalarCombo3.setMaximumSize(new java.awt.Dimension(90, 27));
        scalarCombo3.setMinimumSize(new java.awt.Dimension(85, 27));
        scalarCombo3.setPreferredSize(new java.awt.Dimension(90, 27));
        scalarCombo3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scalarCombo3ItemStateChanged(evt);
            }
        });
        scalarCombo3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scalarCombo3MouseClicked(evt);
            }
        });
        scalarCombo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scalarCombo3ActionPerformed(evt);
            }
        });
        jPanel5.add(scalarCombo3);

        colorLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colorLabel3.setText("Edge Scalar");
        colorLabel3.setEnabled(false);
        colorLabel3.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel5.add(colorLabel3);

        scalarCombo4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gray Scale", "Green to Red", "Blue To Red", "Rainbow Scale", "Blue to Cyan", "Blue to Yellow", "Green To White Scale", "Heated Object Scale", "Linear Gray Scale", "Locs Scale", "Orange To Blue Sky", "Blue Sky To Orange", "Custom Color" }));
        scalarCombo4.setEnabled(false);
        scalarCombo4.setMaximumSize(new java.awt.Dimension(90, 27));
        scalarCombo4.setMinimumSize(new java.awt.Dimension(85, 27));
        scalarCombo4.setPreferredSize(new java.awt.Dimension(90, 27));
        scalarCombo4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                scalarCombo4ItemStateChanged(evt);
            }
        });
        scalarCombo4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scalarCombo4MouseClicked(evt);
            }
        });
        scalarCombo4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scalarCombo4ActionPerformed(evt);
            }
        });
        jPanel5.add(scalarCombo4);

        jPanel3.add(jPanel5);

        jPanel24.setBackground(strong);
        jPanel24.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel24.setPreferredSize(new java.awt.Dimension(130, 80));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Set Background");
        jLabel10.setEnabled(false);
        jLabel10.setPreferredSize(null);
        jLabel10.setVerifyInputWhenFocusTarget(false);
        jPanel24.add(jLabel10);

        backgroundButton.setBackground(weak);
        backgroundButton.setText("Background");
        backgroundButton.setEnabled(false);
        backgroundButton.setFocusable(false);
        backgroundButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        backgroundButton.setPreferredSize(null);
        backgroundButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        backgroundButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backgroundButtonActionPerformed(evt);
            }
        });
        jPanel24.add(backgroundButton);

        jPanel3.add(jPanel24);

        jScrollPane3.setViewportView(jPanel3);

        structurePanel.add(jScrollPane3, java.awt.BorderLayout.PAGE_START);

        jScrollPane5.setBorder(null);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane5.setPreferredSize(new java.awt.Dimension(1720, 80));

        zoomPanelStructure.setBackground(new java.awt.Color(255, 255, 255));
        zoomPanelStructure.setMaximumSize(null);
        zoomPanelStructure.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        animationSettings1.setBackground(new java.awt.Color(255, 255, 255));
        animationSettings1.setBorder(javax.swing.BorderFactory.createTitledBorder("Animation Settings"));
        animationSettings1.setEnabled(false);
        animationSettings1.setMinimumSize(new java.awt.Dimension(300, 56));
        animationSettings1.setPreferredSize(new java.awt.Dimension(550, 60));

        agingLabel1.setText("Aging");
        agingLabel1.setEnabled(false);
        animationSettings1.add(agingLabel1);

        agingjSlider1.setBackground(new java.awt.Color(255, 255, 255));
        agingjSlider1.setForeground(new java.awt.Color(255, 255, 255));
        agingjSlider1.setMaximum(10);
        agingjSlider1.setMinimum(1);
        agingjSlider1.setToolTipText("");
        agingjSlider1.setValue(1);
        agingjSlider1.setEnabled(false);
        agingjSlider1.setPreferredSize(new java.awt.Dimension(75, 20));
        animationSettings1.add(agingjSlider1);

        speedLabel1.setText("Speed");
        speedLabel1.setEnabled(false);
        animationSettings1.add(speedLabel1);

        speedjSlider1.setBackground(new java.awt.Color(255, 255, 255));
        speedjSlider1.setForeground(new java.awt.Color(255, 255, 255));
        speedjSlider1.setMaximum(10);
        speedjSlider1.setMinimum(1);
        speedjSlider1.setToolTipText("");
        speedjSlider1.setValue(1);
        speedjSlider1.setEnabled(false);
        speedjSlider1.setPreferredSize(new java.awt.Dimension(75, 20));
        animationSettings1.add(speedjSlider1);

        runForceButton1.setBackground(new java.awt.Color(255, 255, 255));
        runForceButton1.setForeground(new java.awt.Color(255, 255, 255));
        runForceButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/run.png"))); // NOI18N
        runForceButton1.setToolTipText("Run");
        runForceButton1.setBorder(null);
        runForceButton1.setEnabled(false);
        runForceButton1.setFocusable(false);
        runForceButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runForceButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        runForceButton1.setMaximumSize(new java.awt.Dimension(80, 22));
        runForceButton1.setMinimumSize(new java.awt.Dimension(80, 22));
        runForceButton1.setPreferredSize(new java.awt.Dimension(30, 30));
        runForceButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runForceButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runForceButton1ActionPerformed(evt);
            }
        });
        animationSettings1.add(runForceButton1);

        stopButton1.setBackground(new java.awt.Color(255, 255, 255));
        stopButton1.setForeground(new java.awt.Color(255, 255, 255));
        stopButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/stop.png"))); // NOI18N
        stopButton1.setToolTipText("Stop");
        stopButton1.setBorder(null);
        stopButton1.setEnabled(false);
        stopButton1.setFocusable(false);
        stopButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton1.setMaximumSize(new java.awt.Dimension(80, 22));
        stopButton1.setMinimumSize(new java.awt.Dimension(80, 22));
        stopButton1.setPreferredSize(new java.awt.Dimension(30, 30));
        stopButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stopButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButton1ActionPerformed(evt);
            }
        });
        animationSettings1.add(stopButton1);

        timeLabel1.setText("Time");
        timeLabel1.setEnabled(false);
        animationSettings1.add(timeLabel1);

        timeStructuraljSlider.setBackground(new java.awt.Color(255, 255, 255));
        timeStructuraljSlider.setForeground(new java.awt.Color(255, 255, 255));
        timeStructuraljSlider.setToolTipText("");
        timeStructuraljSlider.setValue(0);
        timeStructuraljSlider.setEnabled(false);
        timeStructuraljSlider.setPreferredSize(new java.awt.Dimension(75, 20));
        animationSettings1.add(timeStructuraljSlider);

        timeStructuralAnimationValue.setEditable(false);
        timeStructuralAnimationValue.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        timeStructuralAnimationValue.setEnabled(false);
        timeStructuralAnimationValue.setOpaque(false);
        timeStructuralAnimationValue.setPreferredSize(new java.awt.Dimension(70, 25));
        animationSettings1.add(timeStructuralAnimationValue);

        zoomPanelStructure.add(animationSettings1);

        structuralSettings.setBackground(new java.awt.Color(255, 255, 255));
        structuralSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Structural Settings"));
        structuralSettings.setEnabled(false);
        structuralSettings.setMinimumSize(new java.awt.Dimension(450, 56));
        structuralSettings.setPreferredSize(new java.awt.Dimension(580, 60));

        depthSelectionLevel.setText("Depth Sel.:");
        depthSelectionLevel.setEnabled(false);
        structuralSettings.add(depthSelectionLevel);

        depthSelectionLevelSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 50, 1));
        depthSelectionLevelSpinner.setToolTipText("");
        depthSelectionLevelSpinner.setEnabled(false);
        depthSelectionLevelSpinner.setMinimumSize(new java.awt.Dimension(60, 20));
        depthSelectionLevelSpinner.setPreferredSize(new java.awt.Dimension(40, 20));
        depthSelectionLevelSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                depthSelectionLevelSpinnerStateChanged(evt);
            }
        });
        structuralSettings.add(depthSelectionLevelSpinner);

        showNodesCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        showNodesCheckBox.setText("Show Nodes");
        showNodesCheckBox.setEnabled(false);
        showNodesCheckBox.setFocusable(false);
        showNodesCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showNodesCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        showNodesCheckBox.setPreferredSize(new java.awt.Dimension(100, 30));
        showNodesCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showNodesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showNodesCheckBoxActionPerformed(evt);
            }
        });
        structuralSettings.add(showNodesCheckBox);

        showEdgesCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        showEdgesCheckBox.setText("Show Edges");
        showEdgesCheckBox.setEnabled(false);
        showEdgesCheckBox.setFocusable(false);
        showEdgesCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showEdgesCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        showEdgesCheckBox.setPreferredSize(new java.awt.Dimension(100, 30));
        showEdgesCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showEdgesCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEdgesCheckBoxActionPerformed(evt);
            }
        });
        structuralSettings.add(showEdgesCheckBox);

        showEdgesWeightCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        showEdgesWeightCheckBox.setText(" Edge Weight");
        showEdgesWeightCheckBox.setEnabled(false);
        showEdgesWeightCheckBox.setFocusable(false);
        showEdgesWeightCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showEdgesWeightCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        showEdgesWeightCheckBox.setPreferredSize(new java.awt.Dimension(100, 30));
        showEdgesWeightCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showEdgesWeightCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEdgesWeightCheckBoxActionPerformed(evt);
            }
        });
        structuralSettings.add(showEdgesWeightCheckBox);

        showInstanceWeightCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        showInstanceWeightCheckBox.setText("Instance Id");
        showInstanceWeightCheckBox.setEnabled(false);
        showInstanceWeightCheckBox.setFocusable(false);
        showInstanceWeightCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        showInstanceWeightCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        showInstanceWeightCheckBox.setPreferredSize(new java.awt.Dimension(90, 30));
        showInstanceWeightCheckBox.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        showInstanceWeightCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInstanceWeightCheckBoxActionPerformed(evt);
            }
        });
        structuralSettings.add(showInstanceWeightCheckBox);

        zoomPanelStructure.add(structuralSettings);

        detectCommunitiesStructural.setText("Det Comm");
        detectCommunitiesStructural.setMaximumSize(new java.awt.Dimension(100, 60));
        detectCommunitiesStructural.setMinimumSize(new java.awt.Dimension(93, 60));
        detectCommunitiesStructural.setPreferredSize(new java.awt.Dimension(90, 22));
        detectCommunitiesStructural.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detectCommunitiesStructuralActionPerformed(evt);
            }
        });
        zoomPanelStructure.add(detectCommunitiesStructural);

        compConexas.setBackground(weak);
        compConexas.setText("Comp Conexas");
        compConexas.setToolTipText("");
        compConexas.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        compConexas.setEnabled(false);
        compConexas.setFocusable(false);
        compConexas.setMaximumSize(new java.awt.Dimension(100, 60));
        compConexas.setMinimumSize(new java.awt.Dimension(93, 60));
        compConexas.setPreferredSize(new java.awt.Dimension(90, 22));
        compConexas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compConexasActionPerformed(evt);
            }
        });
        zoomPanelStructure.add(compConexas);

        zoomAndScroolStructural.setBackground(new java.awt.Color(255, 255, 255));
        zoomAndScroolStructural.setBorder(javax.swing.BorderFactory.createTitledBorder("Zoom and Scroll"));
        zoomAndScroolStructural.setEnabled(false);
        zoomAndScroolStructural.setMinimumSize(new java.awt.Dimension(263, 60));

        zoomToFit1.setBackground(weak);
        zoomToFit1.setText("Zoom Fit");
        zoomToFit1.setToolTipText("Fit to the size of screen");
        zoomToFit1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomToFit1.setEnabled(false);
        zoomToFit1.setFocusable(false);
        zoomToFit1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomToFit1.setPreferredSize(null);
        zoomToFit1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomToFit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomToFit1ActionPerformed(evt);
            }
        });
        zoomAndScroolStructural.add(zoomToFit1);

        zoomOutButton.setBackground(weak);
        zoomOutButton.setText("-");
        zoomOutButton.setToolTipText("Zoom out");
        zoomOutButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomOutButton.setEnabled(false);
        zoomOutButton.setMaximumSize(new java.awt.Dimension(45, 22));
        zoomOutButton.setMinimumSize(new java.awt.Dimension(45, 22));
        zoomOutButton.setPreferredSize(new java.awt.Dimension(45, 22));
        zoomOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutButtonActionPerformed(evt);
            }
        });
        zoomAndScroolStructural.add(zoomOutButton);

        zoomInButton.setBackground(weak);
        zoomInButton.setText("+");
        zoomInButton.setToolTipText("Zoom in");
        zoomInButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomInButton.setEnabled(false);
        zoomInButton.setMaximumSize(new java.awt.Dimension(45, 22));
        zoomInButton.setMinimumSize(new java.awt.Dimension(45, 22));
        zoomInButton.setPreferredSize(new java.awt.Dimension(45, 22));
        zoomInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInButtonActionPerformed(evt);
            }
        });
        zoomAndScroolStructural.add(zoomInButton);

        zoomToDefault1.setBackground(weak);
        zoomToDefault1.setText("Zoom Default");
        zoomToDefault1.setToolTipText("Set the default zoom size");
        zoomToDefault1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomToDefault1.setEnabled(false);
        zoomToDefault1.setFocusable(false);
        zoomToDefault1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomToDefault1.setPreferredSize(null);
        zoomToDefault1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomToDefault1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomToDefault1ActionPerformed(evt);
            }
        });
        zoomAndScroolStructural.add(zoomToDefault1);

        zoomPanelStructure.add(zoomAndScroolStructural);

        jScrollPane5.setViewportView(zoomPanelStructure);

        structurePanel.add(jScrollPane5, java.awt.BorderLayout.PAGE_END);

        jTabbedPane.addTab("Structural", structurePanel);

        matrixPanel.setBackground(new java.awt.Color(255, 255, 255));
        matrixPanel.setEnabled(false);
        matrixPanel.setPreferredSize(new java.awt.Dimension(1432, 900));
        matrixPanel.setLayout(new java.awt.BorderLayout());

        jPanel20.setBackground(strong);
        jPanel20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel20.setMaximumSize(null);
        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jPanel21.setBackground(strong);
        jPanel21.setMaximumSize(null);
        jPanel21.setMinimumSize(null);
        jPanel21.setPreferredSize(null);
        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jPanel20.add(jPanel21);

        nodeOrderMatrixLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nodeOrderMatrixLabel.setText("  Node Order");
        nodeOrderMatrixLabel.setEnabled(false);
        jPanel20.add(nodeOrderMatrixLabel);

        nodeOrderMatrixComboBox.setBackground(weak);
        nodeOrderMatrixComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Appearance", "Random", "Degree", "Recurrent Neighbors", "Louvain", "Infomap" }));
        nodeOrderMatrixComboBox.setEnabled(false);
        nodeOrderMatrixComboBox.setPreferredSize(new java.awt.Dimension(100, 27));
        nodeOrderMatrixComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeOrderMatrixComboBoxActionPerformed(evt);
            }
        });
        jPanel20.add(nodeOrderMatrixComboBox);

        nodeColorMatrixLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nodeColorMatrixLabel.setText("  Node Color");
        nodeColorMatrixLabel.setEnabled(false);
        jPanel20.add(nodeColorMatrixLabel);

        nodeColorMatrixComboBox.setBackground(weak);
        nodeColorMatrixComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Random", "Communities", "Intra Communities", "Only Communities", "Metadata File" }));
        nodeColorMatrixComboBox.setEnabled(false);
        nodeColorMatrixComboBox.setPreferredSize(new java.awt.Dimension(100, 27));
        nodeColorMatrixComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeColorMatrixComboBoxActionPerformed(evt);
            }
        });
        jPanel20.add(nodeColorMatrixComboBox);

        matrixPanel.add(jPanel20, java.awt.BorderLayout.PAGE_START);

        zoomPanelMatrix.setBackground(new java.awt.Color(255, 255, 255));
        zoomPanelMatrix.setRequestFocusEnabled(false);
        zoomPanelMatrix.setLayout(new java.awt.BorderLayout());

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setPreferredSize(new java.awt.Dimension(1715, 65));
        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        animationSettings.setBackground(new java.awt.Color(255, 255, 255));
        animationSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Animation Settings"));
        animationSettings.setEnabled(false);
        animationSettings.setMinimumSize(new java.awt.Dimension(330, 56));
        animationSettings.setPreferredSize(new java.awt.Dimension(700, 60));

        agingLabel.setText("Aging");
        agingLabel.setEnabled(false);
        animationSettings.add(agingLabel);

        agingjSlider.setBackground(new java.awt.Color(255, 255, 255));
        agingjSlider.setForeground(new java.awt.Color(255, 255, 255));
        agingjSlider.setMinimum(1);
        agingjSlider.setToolTipText("");
        agingjSlider.setValue(1);
        agingjSlider.setEnabled(false);
        agingjSlider.setPreferredSize(new java.awt.Dimension(125, 20));
        animationSettings.add(agingjSlider);

        speedLabel.setText("Speed");
        speedLabel.setEnabled(false);
        animationSettings.add(speedLabel);

        speedjSlider.setBackground(new java.awt.Color(255, 255, 255));
        speedjSlider.setForeground(new java.awt.Color(255, 255, 255));
        speedjSlider.setMinimum(1);
        speedjSlider.setToolTipText("");
        speedjSlider.setValue(1);
        speedjSlider.setEnabled(false);
        speedjSlider.setPreferredSize(new java.awt.Dimension(125, 20));
        animationSettings.add(speedjSlider);

        runButton.setBackground(new java.awt.Color(255, 255, 255));
        runButton.setForeground(new java.awt.Color(255, 255, 255));
        runButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/run.png"))); // NOI18N
        runButton.setToolTipText("Run");
        runButton.setBorder(null);
        runButton.setEnabled(false);
        runButton.setFocusable(false);
        runButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        runButton.setMaximumSize(new java.awt.Dimension(80, 22));
        runButton.setMinimumSize(new java.awt.Dimension(80, 22));
        runButton.setPreferredSize(new java.awt.Dimension(30, 30));
        runButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });
        animationSettings.add(runButton);

        stopButton.setBackground(new java.awt.Color(255, 255, 255));
        stopButton.setForeground(new java.awt.Color(255, 255, 255));
        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/stop.png"))); // NOI18N
        stopButton.setToolTipText("Stop");
        stopButton.setBorder(null);
        stopButton.setEnabled(false);
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setMaximumSize(new java.awt.Dimension(80, 22));
        stopButton.setMinimumSize(new java.awt.Dimension(80, 22));
        stopButton.setPreferredSize(new java.awt.Dimension(30, 30));
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        animationSettings.add(stopButton);

        timeLabel.setText("Time");
        timeLabel.setEnabled(false);
        animationSettings.add(timeLabel);

        jSlider1.setBackground(new java.awt.Color(255, 255, 255));
        jSlider1.setForeground(new java.awt.Color(255, 255, 255));
        jSlider1.setToolTipText("");
        jSlider1.setValue(0);
        jSlider1.setEnabled(false);
        jSlider1.setPreferredSize(new java.awt.Dimension(125, 20));
        animationSettings.add(jSlider1);

        timeAnimationValue.setEditable(false);
        timeAnimationValue.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        timeAnimationValue.setEnabled(false);
        timeAnimationValue.setOpaque(false);
        timeAnimationValue.setPreferredSize(new java.awt.Dimension(70, 25));
        animationSettings.add(timeAnimationValue);

        jPanel22.add(animationSettings);

        settingsMatrix.setBackground(new java.awt.Color(255, 255, 255));
        settingsMatrix.setBorder(javax.swing.BorderFactory.createTitledBorder("Matrix Settings"));
        settingsMatrix.setEnabled(false);
        settingsMatrix.setMinimumSize(new java.awt.Dimension(100, 56));
        settingsMatrix.setPreferredSize(new java.awt.Dimension(200, 60));

        upperTriangularMatrix.setBackground(new java.awt.Color(255, 255, 255));
        upperTriangularMatrix.setText("Show Triangular Matrix");
        upperTriangularMatrix.setEnabled(false);
        upperTriangularMatrix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upperTriangularMatrixActionPerformed(evt);
            }
        });
        settingsMatrix.add(upperTriangularMatrix);

        jPanel22.add(settingsMatrix);

        zoomAndScroolMatrix.setBackground(new java.awt.Color(255, 255, 255));
        zoomAndScroolMatrix.setBorder(javax.swing.BorderFactory.createTitledBorder("Zoom and Scroll"));
        zoomAndScroolMatrix.setEnabled(false);
        zoomAndScroolMatrix.setPreferredSize(new java.awt.Dimension(400, 60));

        zoomToFit7.setBackground(weak);
        zoomToFit7.setText("Zoom to Fit");
        zoomToFit7.setToolTipText("Fit to the size of screen");
        zoomToFit7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomToFit7.setEnabled(false);
        zoomToFit7.setFocusable(false);
        zoomToFit7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomToFit7.setPreferredSize(new java.awt.Dimension(100, 22));
        zoomToFit7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoomToFit7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomToFit7ActionPerformed(evt);
            }
        });
        zoomAndScroolMatrix.add(zoomToFit7);

        zoomOutButtonMatrix.setBackground(weak);
        zoomOutButtonMatrix.setText("-");
        zoomOutButtonMatrix.setToolTipText("Zoom out");
        zoomOutButtonMatrix.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomOutButtonMatrix.setEnabled(false);
        zoomOutButtonMatrix.setMaximumSize(new java.awt.Dimension(45, 22));
        zoomOutButtonMatrix.setMinimumSize(new java.awt.Dimension(45, 22));
        zoomOutButtonMatrix.setPreferredSize(new java.awt.Dimension(45, 22));
        zoomOutButtonMatrix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutButtonMatrixActionPerformed(evt);
            }
        });
        zoomAndScroolMatrix.add(zoomOutButtonMatrix);

        zoomInButtonMatrix.setBackground(weak);
        zoomInButtonMatrix.setText("+");
        zoomInButtonMatrix.setToolTipText("Zoom in");
        zoomInButtonMatrix.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomInButtonMatrix.setEnabled(false);
        zoomInButtonMatrix.setMaximumSize(new java.awt.Dimension(45, 22));
        zoomInButtonMatrix.setMinimumSize(new java.awt.Dimension(45, 22));
        zoomInButtonMatrix.setPreferredSize(new java.awt.Dimension(45, 22));
        zoomInButtonMatrix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInButtonMatrixActionPerformed(evt);
            }
        });
        zoomAndScroolMatrix.add(zoomInButtonMatrix);

        zoomToDefault7.setBackground(weak);
        zoomToDefault7.setText("Zoom to Default");
        zoomToDefault7.setToolTipText("Set the default zoom size");
        zoomToDefault7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        zoomToDefault7.setEnabled(false);
        zoomToDefault7.setFocusable(false);
        zoomToDefault7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoomToDefault7.setMargin(new java.awt.Insets(2, 14, 3, 14));
        zoomToDefault7.setPreferredSize(new java.awt.Dimension(100, 22));
        zoomToDefault7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomToDefault7ActionPerformed(evt);
            }
        });
        zoomAndScroolMatrix.add(zoomToDefault7);

        jPanel22.add(zoomAndScroolMatrix);

        zoomPanelMatrix.add(jPanel22, java.awt.BorderLayout.PAGE_START);

        matrixPanel.add(zoomPanelMatrix, java.awt.BorderLayout.PAGE_END);

        jTabbedPane.addTab("Matrix", matrixPanel);

        controlPanel.add(jTabbedPane, java.awt.BorderLayout.CENTER);
        jTabbedPane.getAccessibleContext().setAccessibleName("Structure");

        getContentPane().add(controlPanel, java.awt.BorderLayout.CENTER);

        jScrollPane1.setBorder(null);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel7.setBackground(strong);
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel7.setMaximumSize(null);
        jPanel7.setName(""); // NOI18N
        jPanel7.setVerifyInputWhenFocusTarget(false);
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButton1.setBackground(strong);
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/leftClickMouse.png"))); // NOI18N
        jButton1.setText("Select");
        jButton1.setBorder(null);
        jButton1.setEnabled(false);
        jButton1.setMargin(null);
        jButton1.setPreferredSize(new java.awt.Dimension(80, 30));
        jPanel7.add(jButton1);

        removeSelection.setBackground(strong);
        removeSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/rightClickMouse.png"))); // NOI18N
        removeSelection.setText("Remove Selection");
        removeSelection.setBorder(null);
        removeSelection.setEnabled(false);
        removeSelection.setPreferredSize(new java.awt.Dimension(130, 30));
        removeSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSelectionActionPerformed(evt);
            }
        });
        jPanel7.add(removeSelection);

        numberOfSelectedNodesText.setText("Selected Nodes: ");
        numberOfSelectedNodesText.setEnabled(false);
        jPanel7.add(numberOfSelectedNodesText);

        CountOfSelectedNodes.setForeground(new java.awt.Color(0, 0, 255));
        CountOfSelectedNodes.setText("0");
        CountOfSelectedNodes.setEnabled(false);
        jPanel7.add(CountOfSelectedNodes);

        seeSelectedNodes.setText("See Selected Nodes");
        seeSelectedNodes.setEnabled(false);
        seeSelectedNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seeSelectedNodesActionPerformed(evt);
            }
        });
        jPanel7.add(seeSelectedNodes);

        jLabel21.setText("Taxonomy");
        jLabel21.setEnabled(false);
        jPanel7.add(jLabel21);

        jButton3.setText("Generate");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton3);

        jScrollPane1.setViewportView(jPanel7);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.PAGE_END);

        menuBar.setBackground(strong);
        menuBar.setForeground(strong);
        menuBar.setMaximumSize(new java.awt.Dimension(100, 33));
        menuBar.setPreferredSize(new java.awt.Dimension(100, 33));

        File.setBackground(strong);
        File.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        File.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/openIcon.png"))); // NOI18N
        File.setText("File");
        File.setBorderPainted(true);
        File.setPreferredSize(new java.awt.Dimension(80, 20));

        openDatasetMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openDatasetMenuItem.setBackground(weak);
        openDatasetMenuItem.setText("<html><u>O</u>pen Data set</html>");
        openDatasetMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDatasetMenuItemActionPerformed(evt);
            }
        });
        File.add(openDatasetMenuItem);

        exportImageMenu.setBackground(weak);
        exportImageMenu.setBorder(null);
        exportImageMenu.setText("Export Image");
        exportImageMenu.setEnabled(false);

        exportImgTemporal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        exportImgTemporal.setBackground(weak);
        exportImgTemporal.setMnemonic('D');
        exportImgTemporal.setText("<html><u>E</u>xport Image Temporal</html>");
        exportImgTemporal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportImgTemporalActionPerformed(evt);
            }
        });
        exportImageMenu.add(exportImgTemporal);

        exportImgStructural.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportImgStructural.setBackground(weak);
        exportImgStructural.setMnemonic('D');
        exportImgStructural.setText("<html><u>E</u>xport Image Structure</html>");
        exportImgStructural.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportImgStructuralActionPerformed(evt);
            }
        });
        exportImageMenu.add(exportImgStructural);

        exportImgMatrix.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportImgMatrix.setBackground(weak);
        exportImgMatrix.setMnemonic('D');
        exportImgMatrix.setText("Export Image Matrix");
        exportImgMatrix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportImgMatrixActionPerformed(evt);
            }
        });
        exportImageMenu.add(exportImgMatrix);

        exportImgCentrality.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportImgCentrality.setBackground(weak);
        exportImgCentrality.setMnemonic('D');
        exportImgCentrality.setText("<html><u>E</u>xport Image Centrality</html>");
        exportImgCentrality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportImgCentralityActionPerformed(evt);
            }
        });
        exportImageMenu.add(exportImgCentrality);

        exportImgCommunity.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportImgCommunity.setBackground(weak);
        exportImgCommunity.setMnemonic('D');
        exportImgCommunity.setText("Export Image Community");
        exportImgCommunity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportImgCommunityActionPerformed(evt);
            }
        });
        exportImageMenu.add(exportImgCommunity);

        exportImgStream.setText("Export Image Stream");
        exportImgStream.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportImgStreamActionPerformed(evt);
            }
        });
        exportImageMenu.add(exportImgStream);

        exportImgRobots1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportImgRobots1.setBackground(weak);
        exportImgRobots1.setMnemonic('D');
        exportImgRobots1.setText("Export Image Robots");
        exportImgRobots1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportImgRobots1ActionPerformed(evt);
            }
        });
        exportImageMenu.add(exportImgRobots1);

        File.add(exportImageMenu);

        settings.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        settings.setBackground(weak);
        settings.setText("<html><u>S</u>ettings</html>");
        settings.setEnabled(false);
        settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsActionPerformed(evt);
            }
        });
        File.add(settings);

        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exit.setBackground(weak);
        exit.setText("<html><u>E</u>xit</html>");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        File.add(exit);

        menuBar.add(File);

        Data.setBackground(strong);
        Data.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Data.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/iconData.png"))); // NOI18N
        Data.setText("Data");
        Data.setBorderPainted(true);
        Data.setEnabled(false);
        Data.setMinimumSize(null);

        statistic.setText("Network Properties");
        statistic.setToolTipText("");
        statistic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statisticActionPerformed(evt);
            }
        });
        Data.add(statistic);

        menuBar.add(Data);

        DynamicProcesses.setBackground(strong);
        DynamicProcesses.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        DynamicProcesses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/iconDynamicProcesses.png"))); // NOI18N
        DynamicProcesses.setText("Dynamic Processes");
        DynamicProcesses.setBorderPainted(true);
        DynamicProcesses.setEnabled(false);
        DynamicProcesses.setMinimumSize(null);

        randomWalker.setText("Random Walker");
        randomWalker.setToolTipText("");
        randomWalker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomWalkerActionPerformed(evt);
            }
        });
        DynamicProcesses.add(randomWalker);

        epidemiologyProcesses.setText("Epidemiology");
        epidemiologyProcesses.setToolTipText("");
        epidemiologyProcesses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                epidemiologyProcessesActionPerformed(evt);
            }
        });
        DynamicProcesses.add(epidemiologyProcesses);

        menuBar.add(DynamicProcesses);

        Help.setBackground(strong);
        Help.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Help.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/iconInfo2.png"))); // NOI18N
        Help.setText("Information");
        Help.setBorderPainted(true);
        Help.setMinimumSize(null);

        help.setText("Help (Readme.pdf)");
        help.setToolTipText("");
        help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        Help.add(help);

        about.setText("About (dynetvis.com)");
        about.setToolTipText("");
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutActionPerformed(evt);
            }
        });
        Help.add(about);

        menuBar.add(Help);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    OpenDataSetDialog openDataSetDialog = OpenDataSetDialog.getInstance(this);
    public NetworkProperties networkProperties;
    public RandomWalker randomWalkerProperties;
    public EpidemiologyProcesses epidemiologyProcessesProperties;
    CNOMultilevelSettings cnoMultiLevelSettings = CNOMultilevelSettings.getInstance(this);
    MinimizeEdgeLengthSettings minimizeEdgeLengthSettings = MinimizeEdgeLengthSettings.getInstance(this);
    
    ExportImg expImg = ExportImg.getInstance(this);
    
    
    private void openDatasetMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDatasetMenuItemActionPerformed
        
        openDataSetDialog.display();
        
    }//GEN-LAST:event_openDatasetMenuItemActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    public String stateRunStrucutral = "run";
    
      
    public int currentTime = 0;
    
    @Override
    public void run() {
        //String[] times = new String[netLayout.getMaxTime()+1];
        //for (int k=0;k<=netLayout.getMaxTime();k++)
            //times[k] = Integer.toString(k);
        //SpinnerListModel timeModel = new SpinnerListModel(times);
        //timeSpinner.setModel(timeModel);
        
        netLayout.stop = false;
        
        try {
            
            jSlider1.setMaximum(netLayout.getMaxTime());
            
            //Initialize all edges/nodes with opacity = 0
            for(mxCell ed : netLayout.listEdgesJgraph)
            {
                
                String[] ids = ed.getId().split(" ");
                
                mxCell orig = (mxCell) ((mxGraphModel)netLayout.graph2.getModel()).getCell(ids[0]+"");
                mxCell dest = (mxCell) ((mxGraphModel)netLayout.graph2.getModel()).getCell(ids[1]+"");
                
                //CustomAttributes att = (CustomAttributes) ed.getValue();
                netLayout.edgeAging.put(ed.getId(), 0);
                netLayout.nodeAging.put(orig.getId(), 0);
                netLayout.nodeAging.put(dest.getId(), 0);
                String style = ed.getStyle();
                style = style.replaceAll("opacity=[^;]*;","opacity=0;");
                ed.setStyle(style);
                
                style = orig.getStyle();
                style = style.replaceAll("opacity=[^;]*;","opacity=0;");
                orig.setStyle(style);
                
                style = dest.getStyle();
                style = style.replaceAll("opacity=[^;]*;","opacity=0;");
                dest.setStyle(style);
                
            }
            
            //int i= currentTime;
            while(currentTime <= (int) netLayout.getMaxTime() && !Thread.currentThread().isInterrupted()){
                
                //timeAnimationValue.setText(""+tempoAtual);
                
                int velocity = speedjSlider1.getValue();
                
                timeStructuraljSlider.setValue(currentTime);
                timeStructuralAnimationValue.setText(currentTime+"");   
                netLayout.changeSelectedEdges();
                
                try {
                    Integer changedTime = currentTime;
                    netLayout.setCurrentTime(changedTime);

                }catch (NumberFormatException e) {}
                
                
                Thread.currentThread().sleep(1000/velocity);
                if(stateRunStrucutral.equals("run"))
                {
                    Thread.currentThread().interrupt();
                    break;
                }
                currentTime++;
            }

            Thread.currentThread().interrupt();
            //this.runForceButton1.setToolTipText("Run Force Directed Layout");
            
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } 
    }
    
    
    public ArrayList<ArrayList> matrizData;
    
    
    private void exportImgTemporalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportImgTemporalActionPerformed
        if (view != null) {
            removeSelectedInstances();
        }
    }//GEN-LAST:event_exportImgTemporalActionPerformed

    private void exportImgStructuralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportImgStructuralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportImgStructuralActionPerformed

    private void settingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsActionPerformed
        OptionsForm.getInstance(this).display(this);
    }//GEN-LAST:event_settingsActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed

        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to close?", "", JOptionPane.YES_NO_OPTION);
        
        if (reply == JOptionPane.YES_OPTION)
            System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void removeSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSelectionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_removeSelectionActionPerformed

    ArrayList<String> orderGlobalRetweet;
    
    private void exportImgCentralityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportImgCentralityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportImgCentralityActionPerformed

    private void exportImgCommunityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportImgCommunityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportImgCommunityActionPerformed

    private void exportImgStreamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportImgStreamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportImgStreamActionPerformed
	
	private void exportImgRobots1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportImgRobots1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportImgRobots1ActionPerformed

        
   

    private void statisticActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statisticActionPerformed
        networkProperties.display();
    }//GEN-LAST:event_statisticActionPerformed

							
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        netLayoutInlineNew.getTaxonomy();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void seeSelectedNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seeSelectedNodesActionPerformed
        seeNodes.display();
    }//GEN-LAST:event_seeSelectedNodesActionPerformed

    private void exportImgMatrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportImgMatrixActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportImgMatrixActionPerformed

    //run, pause, stop
    public String stateAnimation = "run";
    
    private void randomWalkerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomWalkerActionPerformed
        randomWalkerProperties.display();
    }//GEN-LAST:event_randomWalkerActionPerformed

    private void epidemiologyProcessesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_epidemiologyProcessesActionPerformed
        epidemiologyProcessesProperties.display();
    }//GEN-LAST:event_epidemiologyProcessesActionPerformed

    private void helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpActionPerformed
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("readme.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                File myFile = new File("readme.txt");
                try {
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex1) {
                    
                }
            }
        }
    }//GEN-LAST:event_helpActionPerformed

    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutActionPerformed
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("https://sites.google.com/view/dynetvis/"));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_aboutActionPerformed

    private void jTabbedPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPaneMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPaneMouseClicked

    private void zoomToDefault7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomToDefault7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zoomToDefault7ActionPerformed

    private void zoomInButtonMatrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInButtonMatrixActionPerformed
        long tempoInicio = System.currentTimeMillis();
        if (viewMatrix != null) {
            viewMatrix.zoomIn();
        }
        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time Zoom In: "+minutos);
    }//GEN-LAST:event_zoomInButtonMatrixActionPerformed

    private void zoomOutButtonMatrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutButtonMatrixActionPerformed
        long tempoInicio = System.currentTimeMillis();
        if (viewMatrix != null) {
            viewMatrix.zoomOut();
        }
        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time Zoom Out: "+minutos);
    }//GEN-LAST:event_zoomOutButtonMatrixActionPerformed

    private void zoomToFit7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomToFit7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zoomToFit7ActionPerformed

    private void upperTriangularMatrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upperTriangularMatrixActionPerformed
        netLayoutMatrix.showTriangularMatrix(upperTriangularMatrix.isSelected());
    }//GEN-LAST:event_upperTriangularMatrixActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        stateAnimation = "stop";
        netLayoutMatrix.startAnimation(this);
    }//GEN-LAST:event_stopButtonActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        netLayoutMatrix.startAnimation(this);
    }//GEN-LAST:event_runButtonActionPerformed

    private void nodeColorMatrixComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeColorMatrixComboBoxActionPerformed
        netLayoutMatrix.nodeColor(nodeColorMatrixComboBox.getSelectedItem().toString());
    }//GEN-LAST:event_nodeColorMatrixComboBoxActionPerformed

    private void nodeOrderMatrixComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeOrderMatrixComboBoxActionPerformed
        netLayoutMatrix.orderNodes(nodeOrderMatrixComboBox.getSelectedItem().toString());
    }//GEN-LAST:event_nodeOrderMatrixComboBoxActionPerformed

    private void zoomToDefault1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomToDefault1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zoomToDefault1ActionPerformed

    private void zoomInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInButtonActionPerformed
        long tempoInicio = System.currentTimeMillis();
        if (view != null) {
            this.getView().zoomIn();
        }
        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time Zoom In: "+minutos);
    }//GEN-LAST:event_zoomInButtonActionPerformed

    private void zoomOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutButtonActionPerformed
        long tempoInicio = System.currentTimeMillis();
        if (view != null) {
            this.getView().zoomOut();
        }
        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time Zoom Out: "+minutos);
    }//GEN-LAST:event_zoomOutButtonActionPerformed

    private void zoomToFit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomToFit1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zoomToFit1ActionPerformed

    private void compConexasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compConexasActionPerformed
        netLayout.componentesConexas();
    }//GEN-LAST:event_compConexasActionPerformed

    private void detectCommunitiesStructuralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detectCommunitiesStructuralActionPerformed
        ArrayList<InlineNodeAttribute> listAttNodes = new ArrayList<InlineNodeAttribute>();
        ArrayList listAllEdges = new ArrayList();
        InlineNodeAttribute attConvertido;

        //AttListNodes ainda nao ta preenchido no layout temporal, entao preciso preencher na mao aqui.
        for(ArrayList<Integer> column : netLayoutInlineNew.matrizDataInline)
        {
            String[] tokens =  new String[9];
            tokens[0] = column.get(0).toString(); //origem
            tokens[1] = column.get(1).toString(); //destino
            tokens[2] = column.get(2).toString(); //tempo

            long origem = Long.parseLong(tokens[0]);
            long destino = Long.parseLong(tokens[1]);
            long time = Long.parseLong(tokens[2]);

            //Source node
            attConvertido = new InlineNodeAttribute(0, (int) origem, 0, 0, origem+"", false, false, true);
            if(!listAttNodesLocalJaTemNo(listAttNodes, (int) origem))
            listAttNodes.add(attConvertido);

            attConvertido = new InlineNodeAttribute(0, (int) destino, 0, 0, destino+"", false, false, true);
            if(!listAttNodesLocalJaTemNo(listAttNodes, (int) destino))
            listAttNodes.add(attConvertido);
        }

        //CALCULO DO ALGORITMO DE COMUNIDADE INFOMAP E LOUVAIN
        HashMap<Integer, java.util.List<InlineNodeAttribute>> comunidadesLouvain = new SLM_Louvain().execute(listAttNodes, netLayoutInlineNew.listAllEdges, "Original Louvain");
        if(comunidadesLouvain != null)
        imprimeComunidadesArquivo(comunidadesLouvain, "Louvain");
        HashMap<Integer, java.util.List<InlineNodeAttribute>> comunidadesInfomap = new InfoMap().execute(netLayoutInlineNew.listAllEdges, listAttNodes);
        if(comunidadesInfomap != null)
        imprimeComunidadesArquivo(comunidadesInfomap, "Infomap");

        //FIM CALCULO ALGORITMO DE COMUNIDADE INFOMAP E LOUVAIN

        JOptionPane.showMessageDialog(null, "Community Detection Done.", "Info", 1);
    }//GEN-LAST:event_detectCommunitiesStructuralActionPerformed

    private void showInstanceWeightCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInstanceWeightCheckBoxActionPerformed
        netLayout.setShowInstanceWeight(showInstanceWeightCheckBox.isSelected());
        //runForceButton.setEnabled(showInstanceWeightCheckBox.isSelected());
        netLayout.notifyObservers();
    }//GEN-LAST:event_showInstanceWeightCheckBoxActionPerformed

    private void showEdgesWeightCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEdgesWeightCheckBoxActionPerformed
        netLayout.setShowWeight(showEdgesWeightCheckBox.isSelected());
        //runForceButton.setEnabled(showEdgesWeightCheckBox.isSelected());
        netLayout.notifyObservers();
    }//GEN-LAST:event_showEdgesWeightCheckBoxActionPerformed

    private void showEdgesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEdgesCheckBoxActionPerformed
        netLayout.setShowEdges(showEdgesCheckBox.isSelected());
    }//GEN-LAST:event_showEdgesCheckBoxActionPerformed

    private void showNodesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showNodesCheckBoxActionPerformed

        netLayout.setShowNodes(showNodesCheckBox.isSelected());
    }//GEN-LAST:event_showNodesCheckBoxActionPerformed

    private void depthSelectionLevelSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_depthSelectionLevelSpinnerStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_depthSelectionLevelSpinnerStateChanged

    private void stopButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButton1ActionPerformed
        netLayout.stop = true;
        agingjSlider1.setEnabled(true);
        netLayout.changeSelectedEdges();
    }//GEN-LAST:event_stopButton1ActionPerformed

    private void runForceButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runForceButton1ActionPerformed

        Thread thread = new Thread(this);

        if (stateRunStrucutral.equals("run")) {
            netLayout.stop = false;
            stateRunStrucutral = "pause";
            ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/pause.png"));
            runForceButton1.setIcon(icon);
            netLayout.maximumAgingValue = agingjSlider1.getValue();
            netLayout.divisorAgingValue = 100/netLayout.maximumAgingValue;
            agingjSlider1.setEnabled(false);
            thread.start();
        } else {
            thread.interrupt();
            netLayout.stop = false;
            stateRunStrucutral = "run";
            ImageIcon icon = new ImageIcon(getClass().getResource("/imgs/run.png"));
            runForceButton1.setIcon(icon);
            agingjSlider1.setEnabled(false);
            //this.runForceButton1.setToolTipText("Run Force Directed Layout");
        }

    }//GEN-LAST:event_runForceButton1ActionPerformed

    private void backgroundButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backgroundButtonActionPerformed
        long tempoInicio = System.currentTimeMillis();

        if (view != null) {

            java.awt.Color color = javax.swing.JColorChooser.showDialog(null,"Choose the Backgroud Color", java.awt.Color.BLACK);

            if (color != null) {
                vv.getViewport().setBackground(color);
                vv.getGraph().repaint();
                vv.getGraph().refresh();

                zoomPanelStructure.setBackground(color);
                showNodesCheckBox.setBackground(color);
                showEdgesCheckBox.setBackground(color);
                showEdgesWeightCheckBox.setBackground(color);
                showInstanceWeightCheckBox.setBackground(color);

                animationSettings1.setBackground(color);
                structuralSettings.setBackground(color);
                zoomAndScroolStructural.setBackground(color);
                timeStructuraljSlider.setBackground(color);
                speedjSlider1.setBackground(color);
                agingjSlider1.setBackground(color);
                runForceButton1.setBackground(color);
                stopButton1.setBackground(color);

            }

            long milliseconds = (System.currentTimeMillis()-tempoInicio);
            String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
            System.out.println("Time change color: "+minutos);
        }
    }//GEN-LAST:event_backgroundButtonActionPerformed

    private void scalarCombo4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scalarCombo4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo4ActionPerformed

    private void scalarCombo4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scalarCombo4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo4MouseClicked

    private void scalarCombo4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_scalarCombo4ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo4ItemStateChanged

    private void scalarCombo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scalarCombo3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo3ActionPerformed

    private void scalarCombo3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scalarCombo3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo3MouseClicked

    private void scalarCombo3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_scalarCombo3ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo3ItemStateChanged

    private void scalarCombo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scalarCombo2ActionPerformed

    }//GEN-LAST:event_scalarCombo2ActionPerformed

    private void scalarCombo2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scalarCombo2MouseClicked
        /*if (evt.getClickCount() == 2) {
            Scalar scalar = (Scalar) this.scalarCombo.getSelectedItem();
            if (!scalar.getName().equals("...")) {
                scalarComboModel.removeElement(scalar);
                scalarCombo.setSelectedIndex(0);
                netLayout.removeScalar(scalar);
                netLayout.notifyObservers();
            }
        }*/
    }//GEN-LAST:event_scalarCombo2MouseClicked

    private void scalarCombo2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_scalarCombo2ItemStateChanged

    }//GEN-LAST:event_scalarCombo2ItemStateChanged

    private void scalarCombo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scalarCombo1ActionPerformed

    }//GEN-LAST:event_scalarCombo1ActionPerformed

    private void scalarCombo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scalarCombo1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo1MouseClicked

    private void scalarCombo1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_scalarCombo1ItemStateChanged

    }//GEN-LAST:event_scalarCombo1ItemStateChanged

    private void layoutComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_layoutComboActionPerformed

        mxGraphLayout layout = new mxFastOrganicLayout(vv.getGraph());
        // define layout
        switch (netLayout.getLayoutJGraphX()) {
            case "mxFastOrganicLayout":
            layout = new mxFastOrganicLayout(vv.getGraph());
            mxFastOrganicLayout lay = (mxFastOrganicLayout) layout;
            lay.setForceConstant(100);
            break;
            case "mxCircleLayout":
            layout = new mxCircleLayout(vv.getGraph());
            break;
            case "mxCompactTreeLayout":
            layout = new mxCompactTreeLayout(vv.getGraph());
            break;
            case "mxHierarchicalLayout":
            layout = new mxHierarchicalLayout(vv.getGraph());
            break;
            case "RandomLayout":

            break;
        }

        // layout using morphing
        vv.getGraph().getModel().beginUpdate();
        try {
            if(netLayout.getLayoutJGraphX().equals("RandomLayout"))
            netLayout.moveNodesRandom();
            else
            layout.execute(vv.getGraph().getDefaultParent());
        } finally {
            mxMorphing morph = new mxMorphing(vv, 20, 1.2, 20);
            morph.addListener(mxEvent.DONE, new mxIEventListener() {

                @Override
                public void invoke(Object arg0, mxEventObject arg1) {
                    vv.getGraph().getModel().endUpdate();
                    vv.getGraph().repaint();
                    vv.getGraph().refresh();
                }

            });
            morph.startAnimation();
        }
    }//GEN-LAST:event_layoutComboActionPerformed

    private void layoutComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_layoutComboMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_layoutComboMouseClicked

    private void layoutComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_layoutComboItemStateChanged
        netLayout.setLayoutJGraphX(evt.getItem().toString());
    }//GEN-LAST:event_layoutComboItemStateChanged

    private void EdgeStrokeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EdgeStrokeComboActionPerformed
        long tempoInicio = System.currentTimeMillis();

        if (view != null) {
            netLayout.changeSizeEdges();
            vv.getGraph().setSelectionCells(vv.getGraph().getSelectionCells());
            vv.getGraph().repaint();
            vv.getGraph().refresh();
        }

        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time change size: "+minutos);
    }//GEN-LAST:event_EdgeStrokeComboActionPerformed

    private void EdgeStrokeComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EdgeStrokeComboMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_EdgeStrokeComboMouseClicked

    private void EdgeStrokeComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_EdgeStrokeComboItemStateChanged
        netLayout.setSizeEdge(evt.getItem().toString());
    }//GEN-LAST:event_EdgeStrokeComboItemStateChanged

    private void nodeSizeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeSizeComboActionPerformed
        long tempoInicio = System.currentTimeMillis();

        if (view != null) {
            netLayout.changeSizeNodes();
            vv.getGraph().setSelectionCells(vv.getGraph().getSelectionCells());
            vv.getGraph().repaint();
            vv.getGraph().refresh();
        }

        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time change size: "+minutos);
    }//GEN-LAST:event_nodeSizeComboActionPerformed

    private void nodeSizeComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nodeSizeComboMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_nodeSizeComboMouseClicked

    private void nodeSizeComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_nodeSizeComboItemStateChanged
        netLayout.setSizeNode(evt.getItem().toString());
    }//GEN-LAST:event_nodeSizeComboItemStateChanged

    private void zoomToDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomToDefaultActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zoomToDefaultActionPerformed

    private void zoomInButtonTemporalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInButtonTemporalActionPerformed
        long tempoInicio = System.currentTimeMillis();
        if (view != null) {
            this.getView2().zoomIn();
        }
        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time Zoom In: "+minutos);
    }//GEN-LAST:event_zoomInButtonTemporalActionPerformed

    private void zoomOutButtonTemporalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutButtonTemporalActionPerformed
        long tempoInicio = System.currentTimeMillis();
        if (view != null) {
            this.getView2().zoomOut();
        }
        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time Zoom Out: "+minutos);

    }//GEN-LAST:event_zoomOutButtonTemporalActionPerformed

    private void zoomToFitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomToFitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_zoomToFitActionPerformed

    private void zoomToLineGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomToLineGraphActionPerformed
        JScrollBar sb = estruturaGraphInline.getVerticalScrollBar();
        //HIDE Funcionalities that are not ready - to publish
        //netLayoutInlineNew.addLabelLineGraph(Integer.parseInt(resolutionS));
        sb.setValue( sb.getMaximum() );
        estruturaGraphInline.repaint();
        estruturaGraphInline.refresh();
        repaint();
    }//GEN-LAST:event_zoomToLineGraphActionPerformed

    private void zoomToTemporalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomToTemporalActionPerformed
        JScrollBar sb = estruturaGraphInline.getVerticalScrollBar();
        sb.setValue( sb.getMinimum());
        estruturaGraphInline.repaint();
        estruturaGraphInline.refresh();
        repaint();
    }//GEN-LAST:event_zoomToTemporalActionPerformed

    private void findNodeTemporalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findNodeTemporalActionPerformed
        if(vvCentrality != null)
        {
            String idToFind = idNodeToFind.getText();
            boolean center = scrollToNodeCenter.isSelected();
        }
        else
        {
            String idToFind = idNodeToFind.getText();
            boolean center = scrollToNodeCenter.isSelected();
            netLayoutInlineNew.scrollToNodeId(idToFind,center);
        }
    }//GEN-LAST:event_findNodeTemporalActionPerformed

    private void idNodeToFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idNodeToFindActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idNodeToFindActionPerformed

    private void showEdgesCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEdgesCheckBox3ActionPerformed
        if(temporalPanel.isShowing()){
            netLayoutInlineNew.setShowEdgesHorizontalLines(showEdgesCheckBox3.isSelected());
        }
    }//GEN-LAST:event_showEdgesCheckBox3ActionPerformed

    private void showEdgesTemporalCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEdgesTemporalCheckBoxActionPerformed
        netLayoutInlineNew.setShowEdges(showEdgesTemporalCheckBox.isSelected());

        if(showEdgesInterCommunitiesCheckBox.isSelected())
        showEdgesInterCommunitiesCheckBox.setSelected(false);
        if(showEdgesIntraCommunitiesCheckBox.isSelected())
        showEdgesIntraCommunitiesCheckBox.setSelected(false);
        if(networkProperties != null)
        networkProperties.resetEdgeTemporalStatistic();
    }//GEN-LAST:event_showEdgesTemporalCheckBoxActionPerformed

    private void showNodesCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showNodesCheckBox1ActionPerformed
        netLayoutInlineNew.setShowNodes(showNodesCheckBox1.isSelected());
    }//GEN-LAST:event_showNodesCheckBox1ActionPerformed

    private void showEdgesInterCommunitiesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEdgesInterCommunitiesCheckBoxActionPerformed
        netLayoutInlineNew.setShowEdgesExtraCommunities(showEdgesInterCommunitiesCheckBox.isSelected());
        showEdgesTemporalCheckBox.setSelected(false);
        if(showEdgesInterCommunitiesCheckBox.isSelected())
        showEdgesIntraCommunitiesCheckBox.setSelected(false);
        if(networkProperties != null)
        networkProperties.resetEdgeTemporalStatistic();
    }//GEN-LAST:event_showEdgesInterCommunitiesCheckBoxActionPerformed

    private void showEdgesIntraCommunitiesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEdgesIntraCommunitiesCheckBoxActionPerformed
        netLayoutInlineNew.setShowEdgesCommunities(showEdgesIntraCommunitiesCheckBox.isSelected());
        showEdgesTemporalCheckBox.setSelected(false);
        if(showEdgesIntraCommunitiesCheckBox.isSelected())
        showEdgesInterCommunitiesCheckBox.setSelected(false);
        if(networkProperties != null)
        networkProperties.resetEdgeTemporalStatistic();
    }//GEN-LAST:event_showEdgesIntraCommunitiesCheckBoxActionPerformed

    private void RightArrowCommunityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RightArrowCommunityActionPerformed

        int totalNiveis = netLayoutInlineNew.comunidadesROCMultilevel.size()-1;
        if(netLayoutInlineNew.stateCommunityROCMultiLevel < netLayoutInlineNew.comunidadesROCMultilevel.size() -1){
            netLayoutInlineNew.stateCommunityROCMultiLevel += 1;
            netLayoutInlineNew.visualizaNos(netLayoutInlineNew.comunidadesROCMultilevel.get(netLayoutInlineNew.stateCommunityROCMultiLevel));
            stateMultiLevel.setText(netLayoutInlineNew.stateCommunityROCMultiLevel+"/" + totalNiveis);
            if(netLayoutInlineNew.stateCommunityROCMultiLevel == netLayoutInlineNew.comunidadesROCMultilevel.size() -1)
            {
                RightArrowCommunity.setEnabled(false);
                leftArrowCommunity.setEnabled(true);
            }
            else
            {
                leftArrowCommunity.setEnabled(true);
                RightArrowCommunity.setEnabled(true);
            }
        }

        //netLayoutInlineNew.verifyAvgEdgesSize();
        //setEdgeTemporalStatistic();

        if(showEdgesTemporalCheckBox.isSelected())
        netLayoutInlineNew.setShowEdges(showEdgesTemporalCheckBox.isSelected());
        if(showEdgesIntraCommunitiesCheckBox.isSelected())
        netLayoutInlineNew.setShowEdgesCommunities(showEdgesIntraCommunitiesCheckBox.isSelected());
        else if(showEdgesInterCommunitiesCheckBox.isSelected())
        netLayoutInlineNew.setShowEdgesExtraCommunities(showEdgesInterCommunitiesCheckBox.isSelected());
    }//GEN-LAST:event_RightArrowCommunityActionPerformed

    private void leftArrowCommunityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftArrowCommunityActionPerformed
        int totalNiveis = netLayoutInlineNew.comunidadesROCMultilevel.size()-1;
        if(netLayoutInlineNew.stateCommunityROCMultiLevel > 0){
            netLayoutInlineNew.stateCommunityROCMultiLevel -= 1;
            netLayoutInlineNew.visualizaNos(netLayoutInlineNew.comunidadesROCMultilevel.get(netLayoutInlineNew.stateCommunityROCMultiLevel));
            stateMultiLevel.setText(netLayoutInlineNew.stateCommunityROCMultiLevel+"/"+totalNiveis);
            if(netLayoutInlineNew.stateCommunityROCMultiLevel == 0)
            {
                leftArrowCommunity.setEnabled(false);
                RightArrowCommunity.setEnabled(true);
            }
            else
            {
                RightArrowCommunity.setEnabled(true);
                leftArrowCommunity.setEnabled(true);
            }
        }

        //netLayoutInlineNew.verifyAvgEdgesSize();
        //setEdgeTemporalStatistic();

        if(showEdgesTemporalCheckBox.isSelected())
        netLayoutInlineNew.setShowEdges(showEdgesTemporalCheckBox.isSelected());
        if(showEdgesIntraCommunitiesCheckBox.isSelected())
        netLayoutInlineNew.setShowEdgesCommunities(showEdgesIntraCommunitiesCheckBox.isSelected());
        else if(showEdgesInterCommunitiesCheckBox.isSelected())
        netLayoutInlineNew.setShowEdgesExtraCommunities(showEdgesInterCommunitiesCheckBox.isSelected());

    }//GEN-LAST:event_leftArrowCommunityActionPerformed

    private void backgroundButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backgroundButton1ActionPerformed

        long tempoInicio = System.currentTimeMillis();

        if (view != null) {

            java.awt.Color color = javax.swing.JColorChooser.showDialog(null,"Choose the Backgroud Color", java.awt.Color.BLACK);
            if (color != null) {
                estruturaGraphInline.getViewport().setBackground(color);
                estruturaGraphInline.getGraph().repaint();
                estruturaGraphInline.getGraph().refresh();

                zoomPanelTemporal.setBackground(color);
                showNodesCheckBox1.setBackground(color);
                showEdgesTemporalCheckBox.setBackground(color);
                showEdgesCheckBox3.setBackground(color);

                cnoSettingsPanel.setBackground(color);
                temporalSettings.setBackground(color);
                findNodePanel.setBackground(color);
                zoomAndScroolTemporalPanel.setBackground(color);
                jPanel13.setBackground(color);
                showEdgesInterCommunitiesCheckBox.setBackground(color);
                showEdgesIntraCommunitiesCheckBox.setBackground(color);
                scrollToNodeCenter.setBackground(color);


            }

            long milliseconds = (System.currentTimeMillis()-tempoInicio);
            String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
            System.out.println("Time change color: "+minutos);
        }
    }//GEN-LAST:event_backgroundButton1ActionPerformed

    private void scalarCombo6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scalarCombo6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo6ActionPerformed

    private void scalarCombo6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scalarCombo6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo6MouseClicked

    private void scalarCombo6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_scalarCombo6ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo6ItemStateChanged

    private void scalarCombo5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scalarCombo5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo5ActionPerformed

    private void scalarCombo5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scalarCombo5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo5MouseClicked

    private void scalarCombo5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_scalarCombo5ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo5ItemStateChanged

    private void scalarCombo8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scalarCombo8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo8ActionPerformed

    private void scalarCombo8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scalarCombo8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo8MouseClicked

    private void scalarCombo8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_scalarCombo8ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo8ItemStateChanged

    private void scalarCombo7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scalarCombo7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo7ActionPerformed

    private void scalarCombo7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scalarCombo7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo7MouseClicked

    private void scalarCombo7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_scalarCombo7ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_scalarCombo7ItemStateChanged

    private void tamOpacityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tamOpacityActionPerformed
        netLayoutInlineNew.setOpacityTAM(tamOpacity.isSelected());
    }//GEN-LAST:event_tamOpacityActionPerformed

    private void tamColoredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tamColoredActionPerformed
        netLayoutInlineNew.setTemplateMat("TAM Colored");
        scalarCombo7.setSelectedIndex(1);
        //nodeSizeCombo1.setSelectedIndex(0);
        formCombo1.setSelectedIndex(1);
        showEdgesTemporalCheckBox.setSelected(false);
        showEdgesCheckBox3.setSelected(false);
        Color color = netLayoutInlineNew.firstColor;
        if (color != null) {
            estruturaGraphInline.getViewport().setBackground(color);
            zoomPanelTemporal.setBackground(color);
            showNodesCheckBox1.setBackground(color);
            showEdgesTemporalCheckBox.setBackground(color);
            showEdgesCheckBox3.setBackground(color);

            cnoSettingsPanel.setBackground(color);
            temporalSettings.setBackground(color);
            findNodePanel.setBackground(color);
            zoomAndScroolTemporalPanel.setBackground(color);
            jPanel13.setBackground(color);
            showEdgesInterCommunitiesCheckBox.setBackground(color);
            showEdgesIntraCommunitiesCheckBox.setBackground(color);
            scrollToNodeCenter.setBackground(color);

        }
        netLayoutInlineNew.setShowEdges(showEdgesTemporalCheckBox.isSelected());
        netLayoutInlineNew.setShowEdgesHorizontalLines(showEdgesCheckBox3.isSelected());
    }//GEN-LAST:event_tamColoredActionPerformed

    private void tamWhiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tamWhiteActionPerformed
        netLayoutInlineNew.setTemplateMat("TAM White");
        scalarCombo7.setSelectedIndex(1);
        //nodeSizeCombo1.setSelectedIndex(0);
        formCombo1.setSelectedIndex(1);
        showEdgesTemporalCheckBox.setSelected(false);
        showEdgesCheckBox3.setSelected(false);
        Color color = Color.WHITE;
        if (color != null) {
            estruturaGraphInline.getViewport().setBackground(color);
            zoomPanelTemporal.setBackground(color);
            showNodesCheckBox1.setBackground(color);
            showEdgesTemporalCheckBox.setBackground(color);
            showEdgesCheckBox3.setBackground(color);

            cnoSettingsPanel.setBackground(color);
            temporalSettings.setBackground(color);
            findNodePanel.setBackground(color);
            zoomAndScroolTemporalPanel.setBackground(color);
            jPanel13.setBackground(color);
            showEdgesInterCommunitiesCheckBox.setBackground(color);
            showEdgesIntraCommunitiesCheckBox.setBackground(color);
            scrollToNodeCenter.setBackground(color);

        }
        netLayoutInlineNew.setShowEdges(showEdgesTemporalCheckBox.isSelected());
        netLayoutInlineNew.setShowEdgesHorizontalLines(showEdgesCheckBox3.isSelected());
    }//GEN-LAST:event_tamWhiteActionPerformed

    private void nodeSizeCombo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeSizeCombo1ActionPerformed
        long tempoInicio = System.currentTimeMillis();

        if (view != null) {
            netLayoutInlineNew.changeSizeNodeInline();
            estruturaGraphInline.getGraph().setSelectionCells(estruturaGraphInline.getGraph().getSelectionCells());
            estruturaGraphInline.getGraph().repaint();
            estruturaGraphInline.getGraph().refresh();
        }

        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time change color: "+minutos);
    }//GEN-LAST:event_nodeSizeCombo1ActionPerformed

    private void nodeSizeCombo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nodeSizeCombo1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_nodeSizeCombo1MouseClicked

    private void nodeSizeCombo1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_nodeSizeCombo1ItemStateChanged
        netLayoutInlineNew.setSizeNode(evt.getItem().toString());
    }//GEN-LAST:event_nodeSizeCombo1ItemStateChanged

    private void edgeSamplingComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edgeSamplingComboActionPerformed
        //NetLayoutInlineNew netInline = new NetLayoutInlineNew();
        long tempoInicio = System.currentTimeMillis();

        netLayoutInlineNew.edgeSampling(edgeSamplingCombo.getSelectedItem().toString(), numberNodes);

        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time change order "+edgeSamplingCombo.getSelectedItem().toString()+": "+minutos);
    }//GEN-LAST:event_edgeSamplingComboActionPerformed

    private void edgeSamplingComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edgeSamplingComboMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_edgeSamplingComboMouseClicked

    private void edgeSamplingComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_edgeSamplingComboItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_edgeSamplingComboItemStateChanged

    private void formCombo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formCombo1ActionPerformed
        long tempoInicio = System.currentTimeMillis();

        if (view != null) {
            netLayoutInlineNew.changeFormNodeInline();
            estruturaGraphInline.getGraph().setSelectionCells(estruturaGraphInline.getGraph().getSelectionCells());
            estruturaGraphInline.getGraph().repaint();
            estruturaGraphInline.getGraph().refresh();
        }

        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time change color: "+minutos);
    }//GEN-LAST:event_formCombo1ActionPerformed

    private void formCombo1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formCombo1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formCombo1MouseClicked

    private void formCombo1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_formCombo1ItemStateChanged
        netLayoutInlineNew.setFormNode(evt.getItem().toString());
    }//GEN-LAST:event_formCombo1ItemStateChanged

    private void orderComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderComboBoxActionPerformed
        //NetLayoutInlineNew netInline = new NetLayoutInlineNew();
        long tempoInicio = System.currentTimeMillis();

        netLayoutInlineNew.listEdgesJgraph = netLayout.listEdgesJgraph;

        if(orderComboBox.getSelectedItem().toString().equals("CNO"))
        {
            cnoMultiLevelSettings.directoryTextField.setText("");
            cnoMultiLevelSettings.display();
            enableToolsCNO();
        }
        else if(orderComboBox.getSelectedItem().toString().equals("Minimize Edge Length"))
        {
            minimizeEdgeLengthSettings.directoryTextField.setText("");
            minimizeEdgeLengthSettings.display();
        }
        else
        {
            netLayoutInlineNew.orderNodesInline(orderComboBox.getSelectedItem().toString(), netLayout.graphComponent);
            disableToolsCNO();
            //setEdgeTemporalStatistic();
        }
        if(networkProperties != null)
        networkProperties.resetEdgeTemporalStatistic();

        long milliseconds = (System.currentTimeMillis()-tempoInicio);
        String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        System.out.println("Time change order "+orderComboBox.getSelectedItem().toString()+": "+minutos);
    }//GEN-LAST:event_orderComboBoxActionPerformed

    public void imprimeComunidadesArquivo(HashMap<Integer, java.util.List<InlineNodeAttribute>> comunidadesXNos, String metodo)
    {
        String conteudoArquivo = "";
        for(int j = 0; j < comunidadesXNos.size(); j++)
        {
            for(InlineNodeAttribute no : comunidadesXNos.get(j))
            {
                conteudoArquivo += no.getId_original() + " " + (j+1) + System.getProperty("line.separator");
            }
        }
        
        final JFileChooser openDialog = new JFileChooser();
        
        String filename;
        openDialog.setDialogTitle("Save");
        
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        openDialog.setSelectedFile(new File("communities_" + metodo + ".txt"));
        openDialog.setFileFilter(new FileNameExtensionFilter("txt file","txt"));
       
        int result = openDialog.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) 
        {
            filename = openDialog.getSelectedFile().getAbsolutePath();
            if (!filename .endsWith(".txt"))
                filename += ".txt";
            openDialog.setSelectedFile(new File(""));
        
            util.FileHandler.gravaArquivo(conteudoArquivo, filename, false);    
            
             
        }
    }
    
    private boolean listAttNodesLocalJaTemNo(ArrayList<InlineNodeAttribute> listAttNodesLocal, int idNo)
    {
        for(InlineNodeAttribute attNo : listAttNodesLocal)
            if(attNo.getId_original() == idNo)
                return true;
        return false;
    }
    
    public String numberNetworkResolution, numberNodes, numberEdges, numberTimestamps;
   
   
    
    
    
    public mxCell createVertex(Object parent, String id, Object value, double x, double y, double width, double height, String style, boolean relative) {
        mxGeometry geometry = new mxGeometry(x, y, width, height);
        geometry.setRelative(relative);
        mxCell vertex = new mxCell(value, geometry, style);
        vertex.setId(id);
        vertex.setVertex(true);
        vertex.setConnectable(true);
        return vertex;
    }
    public static mxGraphSelectionModel selectionCells = null;
    mxGraphSelectionModel selectionCellsInline = null;

    
    mxGraphComponent vvCentrality, vvCommunity, vvRobots, vvMatrix;
    
    
    
    private NetLayoutMatrix netLayoutMatrix;
    
    public void setNetLayoutMatrix(final NetLayoutMatrix netLayoutMatrix)
    {
        expImg.isMatrix = true;
        if (netLayoutMatrix != null) {
            
            vvMatrix = netLayoutMatrix.graphComponent;
            this.netLayoutMatrix = netLayoutMatrix;
            
            BorderLayout layout = (BorderLayout) matrixPanel.getLayout();
            if(layout.getLayoutComponent(BorderLayout.CENTER) != null)
                matrixPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            
            vvMatrix.getViewport().setOpaque(true);
            vvMatrix.getViewport().setBackground(Color.WHITE);
            vvMatrix.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
            vvMatrix.getGraphControl().addMouseWheelListener(new MyMouseWheelListener());
            
            mxRubberband rubberBand = new mxRubberband(vvMatrix);
            
           //vv.getGraphHandler().setMarkerEnabled(false);
           
            vvMatrix.setConnectable(false);
            vvMatrix.getGraph().setAllowDanglingEdges(false);
            vvMatrix.getGraph().setAllowLoops(false);
            vvMatrix.getGraph().setAutoOrigin(false);
            vvMatrix.getGraph().setConnectableEdges(false);
            vvMatrix.getGraph().setCellsDisconnectable(false);
            vvMatrix.getGraph().setCellsBendable(false);
            vvMatrix.getGraph().setDropEnabled(false);
            vvMatrix.getGraph().setSplitEnabled(false);
            vvMatrix.getGraph().setCellsEditable(false);
            vvMatrix.setBorder(null);
            
            
            zoomToFit7.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {

                    mxGraphView view = vvMatrix.getGraph().getView();
                    double compLen = vvMatrix.getWidth();
                    double viewLen = view.getGraphBounds().getWidth();
                    
                    double compHei = vvMatrix.getHeight();
                    double viewHei = view.getGraphBounds().getHeight();
                    
                    view.setScale(compHei/viewHei * view.getScale());
                    
                    vvMatrix.repaint();
                    vvMatrix.refresh();

                }
            });
            
            
            zoomToDefault7.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    
                    vvMatrix.zoomActual();
                    vvMatrix.repaint();
                    vvMatrix.refresh();

                    repaint();

                }
            });
            
            exportImgMatrix.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    expImg.isMatrix = true;
                    
                    expImg.display();

                }
            });
            
            /*
            showInstanceRobots.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {

                    netLayoutRobots.showInstanceRobots(showInstanceRobots.isSelected());
                    
                    vvRobots.repaint();
                    vvRobots.refresh();
                    
                }
            });
            */
            

            matrixPanel.add(vvMatrix,BorderLayout.CENTER);
            
            matrixPanel.validate();
            matrixPanel.repaint();
            
        }
            
    }
    
    public NetLayout getNetLayout() {
        return netLayout;
    }
    
    private ResolutionForm rf;
    mxGraphComponent vv;
    SelectedNodes seeNodes = new SelectedNodes(this);
    
    
    
    public void setNetLayout(final NetLayout netLayout) {
       
        if (netLayout != null) {
            
            
            
            vv = netLayout.graphComponent;
            this.netLayout = netLayout;
            
            BorderLayout layout = (BorderLayout) structurePanel.getLayout();
            if(layout.getLayoutComponent(BorderLayout.CENTER) != null)
                structurePanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            
            
            
            vv.getViewport().setOpaque(true);
            vv.getViewport().setBackground(Color.WHITE);
            vv.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
            vv.getGraphControl().addMouseWheelListener(new MyMouseWheelListener());
            
            
                    
            mxRubberband rubberBand = new mxRubberband(vv);
            
           //vv.getGraphHandler().setMarkerEnabled(false);
            vv.setConnectable(false);
            vv.getGraph().setAllowDanglingEdges(false);
            vv.getGraph().setAllowLoops(false);
            vv.getGraph().setAutoOrigin(false);
            vv.getGraph().setConnectableEdges(false);
            vv.getGraph().setCellsDisconnectable(false);
            vv.getGraph().setCellsBendable(false);
            vv.getGraph().setDropEnabled(false);
            vv.getGraph().setSplitEnabled(false);
            vv.getGraph().setCellsEditable(false);
            vv.setBorder(null);
           // vv.getGraphHandler().setRemoveCellsFromParent(false);
            
            //vv.getGraph().setMaximumGraphBounds(new mxRectangle(0, 0, this.view.getX(), this.view.getY()));
            
            //REMOVE SELECTION ON RIGHT MOUSE CLICK MOUSE
            vv.getGraphControl().addMouseListener(new MouseAdapter() {
                 @Override
                 public void mousePressed(MouseEvent e) {
                     if(e.getModifiers() == MouseEvent.BUTTON3_MASK) 
                    {
                        removeSelectedInstances();
                        selectionCells = null;
                        CountOfSelectedNodes.setText("0");
                        seeSelectedNodes.setEnabled(false);
                        seeNodes.countSelectedNodes.setText("0");
                                
                        seeNodes.selectedNodes.setText("[]");
                        

                        if(structurePanel.isShowing())
                            estruturaGraphInline.getGraph().setSelectionCell(null);

                        Object[] roots = vv.getGraph().getChildCells(vv.getGraph().getDefaultParent(), false, false);
                        seeNodes.setRoots(roots);
                        for(Object root1 : roots)
                        {
                            mxCell cell = (mxCell) root1;
                            String styleEdge = cell.getStyle();
                            styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");

                            cell.setStyle(styleEdge);
                        }
                        
                    }
                 }
            });
            
            
            vv.getGraph().getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
                @Override
                public void invoke(Object sender, mxEventObject evt) {
                    
                        vv.getGraph().getModel().beginUpdate();

                        mxGraphSelectionModel selectedCells = (mxGraphSelectionModel) sender;

                        
                            
                        
                        
                        //If have no cells selected
                        if(selectedCells.getCells().length == 0)
                        {
                            selectionCells = null;
                            CountOfSelectedNodes.setText("0");
                            seeSelectedNodes.setEnabled(false);
                            seeNodes.countSelectedNodes.setText("0");
                            seeNodes.selectedNodes.setText("[]");
                            

                            if(structurePanel.isShowing())
                                estruturaGraphInline.getGraph().setSelectionCell(null);

                            Object[] roots = vv.getGraph().getChildCells(vv.getGraph().getDefaultParent(), false, false);
                            seeNodes.setRoots(roots);

                            for(Object root1 : roots)
                            {
                                mxCell cell = (mxCell) root1;
                                String styleEdge = cell.getStyle();
                                styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");

                                cell.setStyle(styleEdge);
                            }

                        }
                        //Selected cells
                        else
                        {
                            ArrayList<Integer> selectCells = new ArrayList<>();
                            String numSelec = "[";
                            int countSelected = 0;
                            
                            //Verify if there is a depth selection
                            if(Integer.parseInt(depthSelectionLevelSpinner.getValue().toString()) != 0)
                            {
                                if(selectionCells == null || selectionCells.size() != selectedCells.size())
                                    selectionCells = findAdjacentNodes(selectedCells,Integer.parseInt(depthSelectionLevelSpinner.getValue().toString()));
                            }
                            else
                                selectionCells = selectedCells;
                            
                            for(Object selectedCell : selectionCells.getCells())
                            {
                                mxCell cell = (mxCell) selectedCell;
                                if(cell.isVertex()){
                                    selectCells.add(Integer.parseInt(cell.getValue().toString()));
                                    numSelec += cell.getValue()+", ";
                                    countSelected++;
                                }
                            }
                            if(selectCells.isEmpty())
                            {
                                 CountOfSelectedNodes.setText("0");
                                 seeSelectedNodes.setEnabled(false);
                                 seeNodes.countSelectedNodes.setText("0");
                                seeNodes.selectedNodes.setText("[]");

                                if(structurePanel.isShowing())
                                    estruturaGraphInline.getGraph().setSelectionCell(null);

                                Object[] roots = vv.getGraph().getChildCells(vv.getGraph().getDefaultParent(), false, false);
                                seeNodes.setRoots(roots);

                                for(Object root1 : roots)
                                {
                                    mxCell cell = (mxCell) root1;
                                    String styleEdge = cell.getStyle();
                                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");

                                    cell.setStyle(styleEdge);
                                }
                            }
                            else
                            {
                                numSelec = numSelec.substring(0, numSelec.length()-2);
                                numSelec += "]";
                                CountOfSelectedNodes.setText(countSelected+"");
                                seeSelectedNodes.setEnabled(true);
                                seeNodes.countSelectedNodes.setText(countSelected+"");
                                seeNodes.selectedNodes.setText(numSelec);
                                Object[] roots = vv.getGraph().getChildCells(vv.getGraph().getDefaultParent(), true, false);
                                seeNodes.setRoots(roots);
                                for (Object root1 : roots) 
                                {
                                    mxCell cell = (mxCell) root1;
                                    String idCell = cell.getId();
                                    String[] parts = idCell.split(" ");

                                    String styleNode = cell.getStyle();
                                    //styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=10;");

                                    if(parts.length != 2 )
                                    {
                                        cell.setStyle(styleNode);
                                    }


                                }

                                if(estruturaGraphInline.getGraph().getSelectionCells().length != selectCells.size()*2)
                                {
                                    //Colocar os nós da visão inline selecionados
                                     Object[] nosSelecionados = new mxCell[selectCells.size()*2];
                                    int i =0;
                                    Object[] rootsInline = estruturaGraphInline.getGraph().getChildCells(estruturaGraphInline.getGraph().getDefaultParent(), true, true);
                                    for (Object rootInline : rootsInline) 
                                    {
                                        mxCell cellInline = (mxCell) rootInline;
                                        String a = cellInline.getValue().toString();
                                        if(!cellInline.getValue().equals(""))
                                        {
                                            if(cellInline.isVertex()){
                                                InlineNodeAttribute att = (InlineNodeAttribute) cellInline.getValue();

                                                if(att.isLineNode())
                                                {
                                                    if(selectCells.contains(att.getId_original()))
                                                    {
                                                        nosSelecionados[i] = (mxCell) cellInline;
                                                        i++;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    estruturaGraphInline.getGraph().setSelectionCells(nosSelecionados);

                                }

                                for(mxCell ed : netLayout.listEdgesJgraph)
                                {
                                    String styleEdge = ed.getStyle();
                                    String idSource = ed.getSource().getId();
                                    String idTarget = ed.getTarget().getId();
                                    if(!(selectCells.contains(Integer.parseInt(idSource)) && selectCells.contains(Integer.parseInt(idTarget))))
                                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=10;");
                                    else
                                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                                    ed.setStyle(styleEdge);

                                }
                                
                                
                                for(Object selectedCell : selectionCells.getCells())
                                {
                                    mxCell cell = (mxCell) selectedCell;
                                    String styleEdge = cell.getStyle();
                                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                                    cell.setStyle(styleEdge);
                                }

                                vv.getGraph().orderCells(false,vv.getGraph().getSelectionCells());
                            }
                        }

                        vv.getGraph().getModel().endUpdate();
                        vv.getGraph().refresh();
                        vv.getGraph().repaint();
                        estruturaGraphInline.getGraph().refresh();
                        estruturaGraphInline.getGraph().repaint();
                        if(showEdgesCheckBox.isSelected())
                            netLayout.setShowWeight(showEdgesWeightCheckBox.isSelected());
                    }

                
                
            });
            
            
            
            zoomToFit1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {

                    Dimension graphSize = vv.getGraphControl().getPreferredSize();
                    Dimension viewPortSize = structurePanel.getSize();
                    
                    mxGraphView view = vv.getGraph().getView();
                    
                    int biggestLayoutSize = -1, biggestGraphSize = -1;
                    
                    int t_width = vv.getWidth() - (int)view.getGraphBounds().getWidth();
                    int t_height = vv.getHeight() - (int)view.getGraphBounds().getHeight();
                    
                    if(t_width < t_height)
                    {
                        biggestLayoutSize = vv.getWidth();
                        biggestGraphSize = (int)view.getGraphBounds().getWidth();
                    }
                    else
                    {
                        biggestLayoutSize = vv.getHeight();
                        biggestGraphSize = (int)view.getGraphBounds().getHeight();
                    }
                    
                    int compLen = biggestLayoutSize;
                    int viewLen = biggestGraphSize;
                    view.setScale((double)compLen/viewLen * view.getScale());
                    
                    vv.repaint();
                    vv.refresh();


                }
            });
            
            
            zoomToDefault1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    
                    vv.zoomActual();
                    vv.repaint();
                    vv.refresh();

                    repaint();

                }
            });

            
            /*
             vv.getGraph().getModel().beginUpdate();
             Object[] a = mxGraphModel.getChildCells(estruturaGraphInline.getGraph().getModel(), estruturaGraphInline.getGraph().getDefaultParent(), true, true);
            for(Object ed: a)
            {
                mxCell edge = (mxCell) ed;
                if(edge.isEdge()){
                    mxPoint pontos = new mxPoint();
                    if(!edge.getValue().equals("")){
                        InlineNodeAttribute att = (InlineNodeAttribute) edge.getValue();
                        mxPoint pt = estruturaGraphInline.getGraph().getView().getRelativePoint(estruturaGraphInline.getGraph().getView().getState(edge), att.getX_atual(), att.getY_atual());
                        pt.setX(att.getX_atual());
                        pt.setY(att.getY_atual());
                        mxGeometry geometry = new mxGeometry();
                        List<mxPoint> p;
                        p = new ArrayList<>();
                        p.add(pt);
                        geometry.setPoints(p);
                        edge.setGeometry(geometry);
                    }
                }
            }
             vv.getGraph().getModel().endUpdate();
             vv.getGraph().refresh();
                    vv.getGraph().repaint();
            */
            
            //vv.getGraph().setSelectionModel(graphSelectionModel);
            
            //new mxFastOrganicLayout(vv.getGraph()).execute(vv.getGraph().getDefaultParent());
            
            //mxCompactTreeLayout m = new mxCompactTreeLayout(vv.getGraph());
            
            //mxCircleLayout m = new mxCircleLayout (vv.getGraph());
            //m.setUseBoundingBox(true); 
            //m.execute(vv.getGraph().getDefaultParent());
            
            
            
            //new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
            //vv.zoom(10);
            
            
            //JToolBar structurePanelToolbar = new javax.swing.JToolBar();
            //structurePanelToolbar.add(vv);
            //structurePanelToolbar.setOrientation(javax.swing.SwingConstants.HORIZONTAL);
            structurePanel.add(vv,BorderLayout.CENTER);
            
            //scrollPanel.add(vv);
            
            //getView().add(vv);
            //getView().setSize(vv.getSize());
            //getView().setPreferredSize(vv.getPreferredSize());
            structurePanel.validate();
            structurePanel.repaint();
            
            timeStructuralAnimationValue.setText(netLayout.getMaxTime()+"");
            timeStructuraljSlider.setMaximum(netLayout.getMaxTime());
            //CountOfTimestamps.setText(netLayout.getMaxTime()+"");
            
            numberTimestamps = netLayout.getMaxTime()+"";
            
            //((NetworkProperties)reportPanel).setObjects(netLayoutInlineNew.lineNodes.size());
            
        }
        else{
            this.view.removeAll();
            this.view.repaint();
        }
        
    }
    
    private mxGraphSelectionModel findAdjacentNodes(mxGraphSelectionModel selectedCells, int depth) {
        mxGraphSelectionModel selectCells = new mxGraphSelectionModel(vv.getGraph());
        selectCells.addCells(selectedCells.getCells());

        for(int i=0;i<depth;i++)
        {
            mxGraphSelectionModel selectCells3 = selectCells;
            for(Object selectCell : selectCells3.getCells())
            {
                mxCell selectC = (mxCell) selectCell;
                for(mxCell ed : netLayout.listEdgesJgraph)
                {
                    if(ed.getSource() == selectC)
                    {
                        selectCells.addCell(ed.getTarget());
                    }
                    if(ed.getTarget() == selectC)
                    {
                        selectCells.addCell(ed.getSource());
                    }
                }
            }
        }
        return selectCells;
    }
    
    
    private ArrayList<ArrayList> ma;
    mxGraphComponent estruturaGraphInline, estruturaGraphLine;
	public String idAndTimeNodeSelectedEpidemiology;												 
    
    public void setNetLayoutInline(final mxGraphComponent vv2, ArrayList<ArrayList> mm) {
        if (vv2 != null) {
            
            
            BorderLayout layout = (BorderLayout) temporalPanel.getLayout();
            if(layout.getLayoutComponent(BorderLayout.CENTER) != null)
                temporalPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            
            ma = mm;
            
            estruturaGraphInline = vv2;
  
            
            estruturaGraphInline.getGraphControl().addMouseWheelListener(new MyMouseWheelListener());
            //mxRubberband rubberBand = new mxRubberband(estruturaGraphInline);
            

            // add rubberband zoom
            mxRubberband m = new mxRubberband(estruturaGraphInline);
            
            //netLayoutInline.setSize(netLayoutInline.adjustSize());

            //netLayoutInline.zoom(netLayoutInline.getRate());

            //getView2().setSize(new Dimension(1600,1200));
            //getView2().setPreferredSize(new Dimension(1600,800));
            
            //this.getView2().setNetLayout(netLayoutInlineNew);
            //getView2().addMouseWheelListener(new MyMouseWheelListener());
            
            //final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
            //panel.setSize(getView2().getSize());
            //panel.setPreferredSize(getView2().getSize());
            //vv.setSize(getView2().getSize());
            //vv.setPreferredSize(getView2().getSize());
            
            //estruturaGraphInline.setSize(new Dimension(getView2().getWidth()- 10, getView2().getHeight()- 10));
            //estruturaGraphInline.setPreferredSize(new Dimension(getView2().getWidth() - 10, getView2().getHeight()- 10));
            estruturaGraphInline.getViewport().setOpaque(true);
            estruturaGraphInline.getViewport().setBackground(Color.WHITE);
            estruturaGraphInline.getSelectionCellsHandler().setVisible(true);
            estruturaGraphInline.getGraph().setMaximumGraphBounds(new mxRectangle(0, 0, getView2().getX(), getView2().getY()));
            estruturaGraphInline.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
            estruturaGraphInline.setBorder(null);
            //estruturaGraphInline.getGraph().setHtmlLabels(true);
            
            //vv.setAntiAlias(true);
            //vv.setTripleBuffered(true);
            //vv.setDoubleBuffered(true);
            //estruturaGraphInline.getGraph().setDropEnabled(true);
            estruturaGraphInline.setConnectable(false);
            //Create a regular old label
           
            temporalPanel.add(estruturaGraphInline,BorderLayout.CENTER);
            
            //getView2().add(estruturaGraphInline);
            
            estruturaGraphInline.validate();
            estruturaGraphInline.repaint();
            
            
            
            removeSelection.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    
                        removeSelectedInstances();
                        
                        CountOfSelectedNodes.setText("0");
                        seeSelectedNodes.setEnabled(false);
                        seeNodes.countSelectedNodes.setText("0");
                        seeNodes.selectedNodes.setText("[]");
                        
                        vv.getGraph().setSelectionCell(null);
                        
                        //Inline Nodes
                        estruturaGraphInline.getGraph().getModel().beginUpdate();
                        Object[] rootsInline = estruturaGraphInline.getGraph().getChildCells(estruturaGraphInline.getGraph().getDefaultParent(), true, true);
                        seeNodes.setRoots(rootsInline);
                        for (Object rootInline : rootsInline) 
                        {
                            mxCell cellInline = (mxCell) rootInline;
                            String styleNode = cellInline.getStyle();
                            styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                            if(cellInline.isVertex())
                            {
                                InlineNodeAttribute att = (InlineNodeAttribute) cellInline.getValue();
                                if(att.isEdge()){
                                    String styleEdge = cellInline.getStyle();

                                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=25;");
                                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=25;");

                                    cellInline.setStyle(styleEdge);
                                }
                                else{
                                    if(att.getTime() % resolution == 0)
                                    {
                                        styleNode = styleNode.replace(mxConstants.STYLE_NOLABEL+"=1;",mxConstants.STYLE_NOLABEL+"=0;");
                                        cellInline.setStyle(styleNode);
                                    }
                                }
                            }
                            if(cellInline.isEdge()){
                                String styleEdge = cellInline.getStyle();
                                
                                styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=100;");
                                styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                                
                                cellInline.setStyle(styleEdge);
                            }
                        }
                        
                        //timeSpinnerInicial.setValue(netLayout.getMinTime());
                        //timeSpinner.setValue(netLayout.getMaxTime());
                        
                        
                        estruturaGraphInline.getGraph().setSelectionCell(null);
                        
                        estruturaGraphInline.getGraph().getModel().endUpdate();
                        vv.getGraph().getModel().endUpdate();
                        vv.getGraph().refresh();
                        vv.getGraph().repaint();
                        estruturaGraphInline.getGraph().refresh();
                        estruturaGraphInline.getGraph().repaint();
                        

                }
            });

            
            
            //REMOVE SELECTION ON RIGHT MOUSE CLICK MOUSE
            estruturaGraphInline.getGraphControl().addMouseListener(new MouseAdapter() {
                 @Override
                 public void mousePressed(MouseEvent e) {
                     if(e.getModifiers() == MouseEvent.BUTTON3_MASK) 
                    {
                        removeSelection.doClick();
                    }
                 }
            });
            
            
            //Selection on inline nodes
            estruturaGraphInline.getGraph().getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
                @Override
                public void invoke(Object sender, mxEventObject evt) {
                    
                    vv.getGraph().getModel().beginUpdate();
                    
                    mxGraphSelectionModel selectedCells = (mxGraphSelectionModel) sender;
                    
                    //If have no cells selected
                    if(selectedCells.getCells().length == 0)
                    {
                        removeSelection.doClick();
                        idAndTimeNodeSelectedEpidemiology = null;
                    }
                    //Selected cells
                    else
                    {
                        ArrayList<String> selectCellsNodes = new ArrayList<>();
                        ArrayList<String> selectCellsLineNode = new ArrayList<>();
                        ArrayList<String> selectCellsTimeNode = new ArrayList<>();
                        String numSelec = "[";
                        int countSelected = 0;
                        boolean isTimeNode = false;
                        int minTimeSelected = -1 , maxTimeSelected = 0;
                        
                        
                        for(Object selectedCell : selectedCells.getCells())
                        {
                            mxCell cell = (mxCell) selectedCell;
                            if(cell.isVertex()){
                                if(!cell.getValue().equals(""))
                                {
                                    InlineNodeAttribute att = (InlineNodeAttribute) cell.getValue();
                                    if(att.isLineNode())
                                    {
                                        selectCellsLineNode.add(att.getId_original()+"");
                                        numSelec += att.getId_original()+",";
                                        countSelected++;
                                    }
                                    else if(att.isNode())
                                    {
                                        idAndTimeNodeSelectedEpidemiology = cell.getId();												 
                                        selectCellsNodes.add(cell.getId()+"");
                                        //Verificando os nos 
                                        if(minTimeSelected == -1)
                                        {
                                            minTimeSelected = att.getTime();
                                            maxTimeSelected = att.getTime();
                                        }
                                        if(att.getTime() < minTimeSelected)
                                        {
                                            minTimeSelected = att.getTime();
                                        }
                                        if(att.getTime()> maxTimeSelected)
                                        {
                                            maxTimeSelected = att.getTime();
                                        }
                                        selectCellsTimeNode.add(att.getTime()+"");
                                    }
                                    else
                                    {
                                        
                                        isTimeNode = true;
                                        //Verificando os nos 
                                        if(minTimeSelected == -1)
                                        {
                                            minTimeSelected = att.getTime();
                                            maxTimeSelected = att.getTime();
                                        }
                                        if(att.getTime() < minTimeSelected)
                                        {
                                            minTimeSelected = att.getTime();
                                        }
                                        if(att.getTime()> maxTimeSelected)
                                        {
                                            maxTimeSelected = att.getTime();
                                        }
                                        selectCellsTimeNode.add(att.getLabel()+"");
                                    }
                                }
                            }
                            else
                            {
                                selectCellsLineNode.add(cell.getValue().toString());
                                numSelec += cell.getValue()+", ";
                                countSelected++;
                            }
                        }
                                                
                        if(!selectCellsTimeNode.isEmpty())
                        {
                            //timeSpinnerInicial.setValue(minTimeSelected);
                            //timeSpinner.setValue(maxTimeSelected);
                        }
                        
                        if(numSelec.length() > 1)
                        {
                            numSelec = numSelec.substring(0, numSelec.length()-2);
                            numSelec += "]";
                        }
                        CountOfSelectedNodes.setText(countSelected+"");
                        seeSelectedNodes.setEnabled(true);
                        seeNodes.countSelectedNodes.setText(countSelected+"");
                        seeNodes.selectedNodes.setText(numSelec);
                        Object[] roots = vv.getGraph().getChildCells(vv.getGraph().getDefaultParent(), true, false);
                        seeNodes.setRoots(roots);
                        
                        
                        ArrayList<Integer> selectCellsLineNodevv = new ArrayList();
                        for(Object select : vv.getGraph().getSelectionCells())
                        {
                            mxCell s = (mxCell) select;
                            if(s.isVertex())
                                selectCellsLineNodevv.add(Integer.parseInt(s.getId()));
                        }
                        
                        //Colocar os nós da visão inline selecionados
                        estruturaGraphInline.getGraph().getModel().beginUpdate();

                        Object[] rootsInline = estruturaGraphInline.getGraph().getChildCells(estruturaGraphInline.getGraph().getDefaultParent(), true, true);
                        for (Object rootInline : rootsInline) 
                        {
                            mxCell cellInline = (mxCell) rootInline;
                            if(cellInline.isEdge()){

                                InlineNodeAttribute att = (InlineNodeAttribute) cellInline.getSource().getValue();
                                InlineNodeAttribute att2 = (InlineNodeAttribute) cellInline.getTarget().getValue();
                                if(att.isGraphLine())
                                {
                                    continue;
                                }
                                else if(selectCellsLineNode.contains(att.getId_original()+"") && selectCellsLineNode.contains(att2.getId_original()+""))
                                {
                                    String styleEdge = cellInline.getStyle();

                                    if(att.isLineNode())
                                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                                    else
                                        styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=100;");

                                    cellInline.setStyle(styleEdge);
                                    continue;
                                }
                                else{

                                    String styleEdge = cellInline.getStyle();

                                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=10;");
                                    styleEdge = styleEdge.replace(mxConstants.STYLE_OPACITY+"=50;",mxConstants.STYLE_OPACITY+"=10;");

                                    cellInline.setStyle(styleEdge);
                                    continue;

                                }

                            }
                            InlineNodeAttribute att = (InlineNodeAttribute) cellInline.getValue();
                            if(att.isNode())
                            {
                                if(selectCellsNodes.contains(cellInline.getId()+"") || selectCellsLineNode.contains(att.getId_original()+"") || ((selectCellsNodes.isEmpty()) && selectCellsTimeNode.contains(att.getTime()+"")))
                                {
                                    String styleNode = cellInline.getStyle();
                                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                                    cellInline.setStyle(styleNode);
                                }
                                else
                                {
                                    String styleNode = cellInline.getStyle();
                                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=10;");
                                    cellInline.setStyle(styleNode);
                                }
                            }
                            if(att.isLineNode())
                            {
                                if(selectCellsLineNode.contains(att.getId_original()+""))
                                {
                                    String styleNode = cellInline.getStyle();
                                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=10;",mxConstants.STYLE_OPACITY+"=100;");
                                    cellInline.setStyle(styleNode);
                                }
                                else
                                {
                                    String styleNode = cellInline.getStyle();
                                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=10;");
                                    cellInline.setStyle(styleNode);
                                }
                            }
                            if(att.isTimeNode())
                            {
                                if(selectCellsTimeNode.isEmpty() || selectCellsTimeNode.contains(att.getId_original()+""))
                                {
                                    String styleNode = cellInline.getStyle();
                                    if(att.getTime() % resolution == 0)
                                    {
                                        styleNode = styleNode.replace(mxConstants.STYLE_NOLABEL+"=1;",mxConstants.STYLE_NOLABEL+"=0;");
                                    }
                                    cellInline.setStyle(styleNode);
                                }
                                else
                                {
                                    String styleNode = cellInline.getStyle();
                                    if(att.getTime() % resolution == 0)
                                    {
                                        styleNode = styleNode.replace(mxConstants.STYLE_NOLABEL+"=0;",mxConstants.STYLE_NOLABEL+"=1;");
                                    }
                                    cellInline.setStyle(styleNode);
                                }
                            }
                            if(att.isEdge())
                            {
                                //Se a seleção for feita pela linha
                                if((selectCellsLineNode.contains(att.getOrigin()+"") && selectCellsLineNode.contains(att.getDestiny()+"")) || (selectCellsLineNode.contains(att.getDestiny()+"") && selectCellsLineNode.contains(att.getOrigin()+"")))
                                {
                                    String styleNode = cellInline.getStyle();
                                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=100;");
                                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=100;");
                                    cellInline.setStyle(styleNode);
                                }
                                else
                                {
                                    String styleNode = cellInline.getStyle();
                                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=0;");
                                    styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=100;",mxConstants.STYLE_OPACITY+"=0;");
                                    cellInline.setStyle(styleNode);
                                }
                                //Se a seleção for feita pelo tempo
                                if(selectCellsTimeNode.contains(att.getTime()+""))
                                {
                                    String styleNode = cellInline.getStyle();
                                    //styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=25;",mxConstants.STYLE_OPACITY+"=100;");
                                    //styleNode = styleNode.replace(mxConstants.STYLE_OPACITY+"=0;",mxConstants.STYLE_OPACITY+"=100;");
                                    cellInline.setStyle(styleNode);
                                }
                            }
                            estruturaGraphInline.getGraph().getModel().endUpdate();
                        }
                        
                        if(!selectCellsLineNodevv.containsAll(selectCellsLineNode))
                        {
                            Object[] nosSelecionados = new mxCell[selectCellsLineNode.size()];
                            int i =0;
                            for (Object root1 : roots) 
                            {
                                mxCell cell = (mxCell) root1;
                                String idCell = cell.getId();

                                if(selectCellsLineNode.contains(idCell))
                                {
                                    nosSelecionados[i] = (mxCell) cell;
                                    i++;
                                }
                            }
                            
                            vv.getGraph().setSelectionCells(nosSelecionados);
                        }
                        
                    }
                    
                    vv.getGraph().getModel().endUpdate();

                    vv.getGraph().refresh();
                    vv.getGraph().repaint();
                    estruturaGraphInline.getGraph().refresh();
                    estruturaGraphInline.getGraph().repaint();

                    
                    
                }
            });
            

            // graphOutline
            /*
            graphOutline = new mxGraphOutline(estruturaGraphInline);
            graphOutline.setFitPage(false);
            graphOutline.setSize(new Dimension(100, 100));
            graphOutline.setVisible(false);
            
            toolBar.add(graphOutline, BorderLayout.EAST);
            */
            

            // zoom to fit
            
            zoomToFit.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {

                    double newScale = 1;

                    Dimension graphSize = estruturaGraphInline.getGraphControl().getPreferredSize();
                    Dimension viewPortSize = temporalPanel.getSize();
                    
                    /*
                    int gw = (int) graphSize.getWidth();
                    int gh = (int) graphSize.getHeight();

                    if (gw > 0 && gh > 0) {
                        int w = (int) viewPortSize.getWidth();
                        int h = (int) viewPortSize.getHeight();

                        newScale = Math.min((double) w / gw, (double) h / gh);
                    }
                    estruturaGraphInline.zoom(newScale);
                    */
                    
                    mxGraphView view = estruturaGraphInline.getGraph().getView();
                    
                    int biggestLayoutSize = -1, biggestGraphSize = -1;
                    if((int)view.getGraphBounds().getWidth() > (int)view.getGraphBounds().getHeight())
                    {
                        biggestLayoutSize = estruturaGraphInline.getWidth();
                        biggestGraphSize = (int)view.getGraphBounds().getWidth();
                    }
                    else
                    {
                        biggestLayoutSize = estruturaGraphInline.getHeight();
                        biggestGraphSize = (int)view.getGraphBounds().getHeight();
                    }
                    
                    int compLen = biggestLayoutSize;
                    int viewLen = biggestGraphSize;
                    view.setScale((double)compLen/viewLen * view.getScale());
                    
                    netLayoutInlineNew.graphComponent.repaint();
                    netLayoutInlineNew.graphComponent.refresh();


                }
            });
            
            zoomToDefault.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    
                    estruturaGraphInline.zoomActual();
                    //estruturaGraphInline.zoom(10);
                    
                    netLayoutInlineNew.graphComponent.repaint();
                    netLayoutInlineNew.graphComponent.refresh();

                    repaint();

                }
            });

            exportImgTemporal.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    expImg.isTemporal = true;
                    
                    expImg.display();

                }
            });
            
            
            exportImgStructural.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    expImg.isTemporal = false;
                     expImg.display();
                    
                }
            });
            
            //this.view2.setPreferredSize(new Dimension(2000,700));
            //this.view2.setSize(new Dimension(2000,700));

            //CountOfNodes.setText(netLayoutInlineNew.lineNodes.size()+"");
            //CountOfEdges.setText(matrizData.size()+"");
            
            numberNodes = netLayoutInlineNew.lineNodes.size()+"";
            numberEdges = matrizData.size()+"";
            
            netLayoutInlineNew.CountOfEdges = matrizData.size();
            
            //Change the size of the panel
            
            //this.view2.setPreferredSize(new Dimension(1500,700));
            //this.view2.setSize(new Dimension(1500,700));

            //setEdgeTemporalStatistic();
            
        }
        else{
            this.getView2().removeAll();
        }
    }
    
    public void setNetLayoutLine(final mxGraphComponent vv3){

        view3.removeAll();

        estruturaGraphLine = vv3;

        estruturaGraphLine.getGraphControl().addMouseWheelListener(new MyMouseWheelListener());

        mxRubberband m = new mxRubberband(estruturaGraphLine);

        estruturaGraphLine.setSize(new Dimension(view3.getWidth()- 10, view3.getHeight()- 10));
        estruturaGraphLine.setPreferredSize(new Dimension(view3.getWidth() - 10, view3.getHeight()- 10));
        estruturaGraphLine.getViewport().setOpaque(true);
        estruturaGraphLine.getViewport().setBackground(Color.WHITE);
        estruturaGraphLine.getSelectionCellsHandler().setVisible(true);
        estruturaGraphLine.getGraph().setMaximumGraphBounds(new mxRectangle(0, 0, view3.getX(), view3.getY()));
        estruturaGraphLine.getViewport().setScrollMode(0);
        
        
        estruturaGraphLine.setConnectable(false);

        
        view3.add(estruturaGraphLine);

        estruturaGraphLine.getGraph().getModel().endUpdate();
        estruturaGraphLine.getGraph().refresh();
        estruturaGraphLine.getGraph().repaint();

    }
     
    MouseListener mL,mYmL;
    
    public void setViewerBackground(Color bg) {
        if (view != null) {
            view.setBackground(bg);
            view.repaint();
        }
        if (view2 != null) {
            view2.setBackground(bg);
            view2.repaint();
        }
    }


    public ViewPanel getView() {
        return view;
    }

    public int isHighQualityRender() {
        return highqualityrender;
    }

    public void setHighQualityRender(int highqualityrender) {

        if (highqualityrender == 1){
            vv.setDoubleBuffered(false);
            estruturaGraphInline.setDoubleBuffered(false);
            vv.setTripleBuffered(true);
            estruturaGraphInline.setTripleBuffered(true);
            //netLayout.getGraph().addAttribute("ui.antialias");
        }
        else if (highqualityrender == 2){
            vv.setDoubleBuffered(true);
            estruturaGraphInline.setDoubleBuffered(true);
            vv.setTripleBuffered(false);
            estruturaGraphInline.setTripleBuffered(false);
            //netLayout.getGraph().removeAttribute("ui.antialias");
        }
        else if (highqualityrender == 3){
            vv.setTripleBuffered(false);
            estruturaGraphInline.setTripleBuffered(false);
            vv.setDoubleBuffered(false);
            estruturaGraphInline.setDoubleBuffered(false);
        }
        
        this.highqualityrender = highqualityrender;
        
    }

    public boolean isMoveInstances() {
        return moveinstances;
    }

    public void setMoveInstance(boolean moveinstances) {
        this.moveinstances = moveinstances;
    }

    public void showHideMatrixButtons(boolean option) {
        zoomInButtonMatrix.setEnabled(option);
        zoomOutButtonMatrix.setEnabled(option);
        zoomToFit7.setEnabled(option);
        zoomToDefault7.setEnabled(option);
        zoomAndScroolMatrix.setEnabled(option);
        runButton.setEnabled(option);
        stopButton.setEnabled(option);
        //jSlider1.setEnabled(option);
        //timeSpinnerMatrix.setEnabled(option);
        animationSettings.setEnabled(option);
        speedjSlider.setEnabled(option);
        speedLabel.setEnabled(option);
        agingjSlider.setEnabled(option);
        agingLabel.setEnabled(option);
        timeLabel.setEnabled(option);
        exportImageMenu.setEnabled(option);
        nodeOrderMatrixComboBox.setEnabled(option);
        nodeOrderMatrixLabel.setEnabled(option);
        nodeColorMatrixComboBox.setEnabled(option);
        nodeColorMatrixLabel.setEnabled(option);
        settingsMatrix.setEnabled(option);
        upperTriangularMatrix.setEnabled(option);
    }
	
    public void showHideButtons(boolean option) {
        Data.setEnabled(option);
        DynamicProcesses.setEnabled(option);
        jTabbedPane.setEnabled(option);
        zoomInButton.setEnabled(option);
        zoomOutButton.setEnabled(option);
        zoomInButtonTemporal.setEnabled(option);
        zoomOutButtonTemporal.setEnabled(option);
        zoomToFit.setEnabled(option);
        zoomToFit1.setEnabled(option);
        zoomToDefault.setEnabled(option);
        zoomToDefault1.setEnabled(option);
        zoomToLineGraph.setEnabled(option);
        zoomToTemporal.setEnabled(option);
        runForceButton1.setEnabled(option);
        //timeSpinner.setEnabled(option);
        //timeSpinnerInicial.setEnabled(option);
        //maxTimeButton.setEnabled(option);
        depthSelectionLevel.setEnabled(option);
        depthSelectionLevelSpinner.setEnabled(option);
        stopButton1.setEnabled(option);
        //scalarCombo2.setEnabled(option);
        showEdgesCheckBox.setEnabled(option);
        //showHasFBEdgesCheckBox.setEnabled(option);
        //spaceBetweenLinesSlider.setEnabled(option);
        //lineSpaceLabel.setEnabled(option);
        showEdgesWeightCheckBox.setEnabled(option);
        showInstanceWeightCheckBox.setEnabled(option);
        showNodesCheckBox.setEnabled(option);
        layoutCombo.setEnabled(option);
        //colorButton.setEnabled(option);
        backgroundButton.setEnabled(option);
        //tamCombo.setEnabled(option);
        //colorButton1.setEnabled(option);
        backgroundButton1.setEnabled(option);
        nodeSizeCombo.setEnabled(option);
        EdgeStrokeCombo.setEnabled(option);
        nodeSizeCombo1.setEnabled(option);
        showNodesCheckBox1.setEnabled(option);
        showEdgesTemporalCheckBox.setEnabled(option);
        formCombo1.setEnabled(option);
       // jLabel1.setEnabled(option);
        jLabel2.setEnabled(option);
        jLabel3.setEnabled(option);
        jLabel4.setEnabled(option);
        //jLabel5.setEnabled(option);
        jLabel6.setEnabled(option);
        jLabel7.setEnabled(option);
        jLabel8.setEnabled(option);
        //jLabel9.setEnabled(option);
        /*
        numberOfNodesText.setEnabled(option);
        CountOfNodes.setEnabled(option);
        numberOfTimestamps.setEnabled(option);
        CountOfTimestamps.setEnabled(option);
        networkResolution.setEnabled(option);
        CountOfNetworkResolution.setEnabled(option);
        networkName.setEnabled(option);
        networkNameLabel.setEnabled(option);
        numberOfEdges.setEnabled(option);
        CountOfEdges.setEnabled(option);
        */
        seeNodes.selectedNodes.setEnabled(option);
        numberOfSelectedNodesText.setEnabled(option);
        CountOfSelectedNodes.setEnabled(option);
        exportImageMenu.setEnabled(option);
        settings.setEnabled(option);
        //filtersMenu1.setEnabled(option);
        jLabel10.setEnabled(option);
        jLabel11.setEnabled(option);
        //Color form
        colorLabel.setEnabled(option);
        colorLabel1.setEnabled(option);
        colorLabel2.setEnabled(option);
        colorLabel3.setEnabled(option);
        colorLabel4.setEnabled(option);
        colorLabel5.setEnabled(option);
        colorLabel6.setEnabled(option);
        colorLabel7.setEnabled(option);
        scalarCombo1.setEnabled(option);
        scalarCombo3.setEnabled(option);
        scalarCombo5.setEnabled(option);
        scalarCombo7.setEnabled(option);
        orderComboBox.setEnabled(option);
        showEdgesCheckBox3.setEnabled(option);
        jLabel19.setEnabled(option);
        edgeSamplingCombo.setEnabled(option);
        
        jButton1.setEnabled(option);
        removeSelection.setEnabled(option);
        
        
        tamWhite.setEnabled(option);
        tamColored.setEnabled(option);
        tamOpacity.setEnabled(option);
        
        temporalSettings.setEnabled(option);
        findNodePanel.setEnabled(option);
        idNodeToFind.setEnabled(option);
        scrollToNodeCenter.setEnabled(option);
        findNodeTemporal.setEnabled(option);
        zoomAndScroolTemporalPanel.setEnabled(option);
        
        speedLabel1.setEnabled(option);
        speedjSlider1.setEnabled(option);
        //timeStructuraljSlider.setEnabled(option);
        timeLabel1.setEnabled(option);
        agingjSlider1.setEnabled(option);
        agingLabel1.setEnabled(option);
        animationSettings1.setEnabled(option);
        zoomAndScroolStructural.setEnabled(option);
        structuralSettings.setEnabled(option);
        jLabel21.setEnabled(option);
        jButton3.setEnabled(option);
    }
    
    public void resetFlags() {
        /*showNodesCheckBox.setSelected(true);
        showEdgesCheckBox.setSelected(true);
        showNodesCheckBox1.setSelected(true);
        showEdgesCheckBox2.setSelected(true);
        showEdgesCheckBox3.setSelected(true);
        acumulateTime.setSelected(true);
        spaceBetweenLinesSlider.setValue(1);
        showEdgesWeightCheckBox.setSelected(false);
        showInstanceWeightCheckBox.setSelected(false);
        directedGraph.setSelected(false);
        showEdgesCheckBox3.setSelected(true);
        showEdgesCheckBox3.setEnabled(true);
        //layoutCombo.setSelectedIndex(0);
        orderComboBox.setSelectedIndex(0);
        orderComboBox.setEnabled(true);
        */
        //depthGraph.setText("0");
        
        //spacingcomboBox1.setSelectedItem("15");
        //resolutionomboBox.setSelectedIndex(0);
        //spacingcomboBox.setSelectedIndex(0);
        
        selectedNodes.clear();
        lastSelectedNodesHere.clear();
        

        zoomPanelTemporal.setBackground(Color.WHITE);
        showNodesCheckBox1.setBackground(Color.WHITE);
        showEdgesTemporalCheckBox.setBackground(Color.WHITE);
        showEdgesCheckBox3.setBackground(Color.WHITE);


        zoomPanelStructure.setBackground(Color.WHITE);
        showNodesCheckBox.setBackground(Color.WHITE);
        showEdgesCheckBox.setBackground(Color.WHITE);
        showEdgesWeightCheckBox.setBackground(Color.WHITE);
        showInstanceWeightCheckBox.setBackground(Color.WHITE);
        timeStructuralAnimationValue.setBackground(Color.WHITE);
        
        if(jPanel2.getComponents().length == 5)
            jPanel2.remove(4);
        if(jPanel5.getComponents().length == 5)
            jPanel5.remove(4);
        if(jPanel8.getComponents().length == 5)
            jPanel8.remove(4);
        if(jPanel9.getComponents().length == 5)
            jPanel9.remove(4);
        
        //Reset Structural Components
        nodeSizeCombo.setSelectedIndex(0);
        EdgeStrokeCombo.setSelectedIndex(0);
        layoutCombo.setSelectedIndex(0);
        scalarCombo1.setSelectedIndex(0);
        scalarCombo2.setSelectedIndex(0);
        scalarCombo3.setSelectedIndex(0);
        scalarCombo4.setSelectedIndex(0);
        
        //Reset Flags Structural
        showNodesCheckBox.setSelected(true);
        showEdgesCheckBox.setSelected(true);
        
        
        //Reset Temporal Components
        nodeSizeCombo1.setSelectedIndex(0);
        formCombo1.setSelectedIndex(0);
     //   orderComboBox.setSelectedIndex(0);
        //tamCombo.setSelectedIndex(0);
        scalarCombo7.setSelectedIndex(0);
        scalarCombo8.setSelectedIndex(0);
        scalarCombo5.setSelectedIndex(0);
        scalarCombo4.setSelectedIndex(0);
        scalarCombo6.setSelectedIndex(0);
        
        //Reset Flags Temporal
        showNodesCheckBox1.setSelected(true);
        showEdgesTemporalCheckBox.setSelected(true);
        showEdgesCheckBox3.setSelected(true);
        
        tam.clearSelection();
        
    }

    public void setPathDataset(String pathDataset) {
        this.pathDataset = pathDataset;
    }
    
    public String getPathDataset() {
        return pathDataset;
    }

    ArrayList<Integer> selectedNodes = new ArrayList();

    private void removeSelectedInstances() {
        
        selectedNodes.clear();
        
        /*for(Edge edge : graph.getEachEdge()){
            edge.addAttribute("ui.style",edge.getAttribute("ui.style")+"stroke-color:black;");
        }
        for(Node node : graph.getEachNode()){
            node.addAttribute("ui.style",node.getAttribute("ui.style")+"stroke-color:black;");
        }*/
    }

    /**
     * @return the view2
     */
    public ViewInlinePanel getView2() {
        return view2;
    }

    /**
     * @param view2 the view2 to set
     */
    public void setView2(ViewInlinePanel view2) {
        this.view2 = view2;
    }

    /**
     * @param netInline the netInline to set
     */
    public void setNetInline(NetLayoutInlineNew netInline) {
        this.netLayoutInlineNew = netInline;
    }
    
    public NetLayoutInlineNew getNetInline() {
        return netLayoutInlineNew;
    }

    /**
     * @param cf the cf to set
     */
    public void setCf() {
        
        final mxGraphComponent graphComponentScalar = netLayout.graphComponentScalar;
        graphComponentScalar.setConnectable(false);
        graphComponentScalar.getGraph().setAllowDanglingEdges(false);
        graphComponentScalar.getGraph().setAllowLoops(false);
        graphComponentScalar.getGraph().setAutoOrigin(false);
        graphComponentScalar.getGraph().setConnectableEdges(false);
        graphComponentScalar.getGraph().setCellsDisconnectable(false);
        graphComponentScalar.getGraph().setCellsBendable(false);
        graphComponentScalar.getGraph().setDropEnabled(false);
        graphComponentScalar.getGraph().setSplitEnabled(false);
        graphComponentScalar.getGraph().setCellsEditable(false);
        graphComponentScalar.setSize(new Dimension(360, 30));
        graphComponentScalar.setPreferredSize(new Dimension(360, 30));
        graphComponentScalar.setBorder(null);
        graphComponentScalar.getViewport().setBackground(strong);
        
        
        graphComponentScalar.getGraph().getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
            @Override
            public void invoke(Object sender, mxEventObject evt) {

                    vv.getGraph().getModel().beginUpdate();

                    mxGraphSelectionModel selectedCells = (mxGraphSelectionModel) sender;
                    
                    if(!selectedCells.isEmpty())
                    {
                        Object[] cells = new Object[99999];
                        int countCells = 0;
                         for(Object selectedCell : selectedCells.getCells())
                        {
                            mxCell cell2 = (mxCell) selectedCell;
                            if(cell2.isVertex()){
                                String colorSearch = cell2.getId();
                                //selectiona todos os nos com a cor X
                                
                                    Object[] roots = vv.getGraph().getChildCells(vv.getGraph().getDefaultParent(), true, false);
                                    for (Object root1 : roots) 
                                    {
                                        mxCell cell = (mxCell) root1;
                                        String idCell = cell.getId();
                                        String[] parts = idCell.split(" ");

                                        String styleNode = cell.getStyle();
                                        int indexFillColor = styleNode.lastIndexOf("fillColor=");
                                        indexFillColor += 10;
                                        
                                        String colorNode = styleNode.substring(indexFillColor, indexFillColor+7);
                                        
                                        if(colorNode.equals(colorSearch))
                                        {
                                            cells[countCells] = cell;
                                            countCells++;
                                        }
                                    }
                                    vv.selectCellsForEvent(cells, null);
                            }
                        }
                    }
                    
                    vv.getGraph().getModel().endUpdate();
                    vv.getGraph().refresh();
                    vv.getGraph().repaint();
                    
            }});
        
        
        jPanel2.add(graphComponentScalar);
        
        scalarCombo1.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent evt) {
                netLayout.setColor(evt.getItem().toString());
                netLayoutInlineNew.setColor(evt.getItem().toString());
                
                if(!evt.getItem().toString().equals("Color") && !evt.getItem().toString().equals("Color Std Dev"))
                    scalarCombo2.setEnabled(false);
                else
                    scalarCombo2.setEnabled(true);
            }
        });
        
        scalarCombo1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  
                if (view != null) {

                    netLayout.changeColorNodes();

                    vv.getGraph().setSelectionCells(vv.getGraph().getSelectionCells());
                    vv.getGraph().repaint();
                    vv.getGraph().refresh();
                    
                    netLayoutInlineNew.changeColorInlineNodesLeft(netLayout.graph2.getChildCells(netLayout.graph2.getDefaultParent(), true, false));
                    estruturaGraphInline.getGraph().setSelectionCells(estruturaGraphInline.getGraph().getSelectionCells());
                    estruturaGraphInline.getGraph().repaint();
                    estruturaGraphInline.getGraph().refresh();

                }
                long milliseconds = (System.currentTimeMillis()-tempoInicio);
                String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                System.out.println("Time change color: "+minutos);
            }
        });
        
        
        
        scalarCombo2.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent evt) {
                netLayout.setTypeColor(evt.getItem().toString());
                netLayoutInlineNew.setColorInline(evt.getItem().toString());

            }
        });
        
        
        scalarCombo2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  
        
                if (view != null) {
                        netLayout.changeColorNodes();
                        vv.getGraph().setSelectionCells(vv.getGraph().getSelectionCells());
                        vv.getGraph().repaint();
                        vv.getGraph().refresh();
                        netLayoutInlineNew.changeColorInlineNodesLeft(netLayout.graph2.getChildCells(netLayout.graph2.getDefaultParent(), true, false));
                        estruturaGraphInline.getGraph().setSelectionCells(estruturaGraphInline.getGraph().getSelectionCells());
                        estruturaGraphInline.getGraph().repaint();
                        estruturaGraphInline.getGraph().refresh();

                    }

                    long milliseconds = (System.currentTimeMillis()-tempoInicio);
                    String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                    System.out.println("Time change color: "+minutos);
                }
        });
        
        
        scalarCombo3.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent evt) {
                netLayout.setColorEdge(evt.getItem().toString());
                
                if(!evt.getItem().toString().equals("Color"))
                    scalarCombo4.setEnabled(false);
                else
                    scalarCombo4.setEnabled(true);
            }
        });
        
        
        scalarCombo3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  
                if (view != null) {

                    netLayout.changeColorEdges();

                    vv.getGraph().setSelectionCells(vv.getGraph().getSelectionCells());
                    vv.getGraph().repaint();
                    vv.getGraph().refresh();

                }


                long milliseconds = (System.currentTimeMillis()-tempoInicio);
                String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                System.out.println("Time change color: "+minutos);
            }
        });
        
        
        scalarCombo4.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent evt) {
                netLayout.setTypeColorEdge(evt.getItem().toString());

            }
        });
        
        
        scalarCombo4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  
        
                if (view != null) {
                        netLayout.changeColorEdges();
                        vv.getGraph().setSelectionCells(vv.getGraph().getSelectionCells());
                        vv.getGraph().repaint();
                        vv.getGraph().refresh();
                    }

                    long milliseconds = (System.currentTimeMillis()-tempoInicio);
                    String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                    System.out.println("Time change color: "+minutos);
                }
        });
        
        
        scalarCombo7.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent evt) {
                netLayoutInlineNew.setColorInline(evt.getItem().toString());
                
                if(!evt.getItem().toString().equals("Color") && !evt.getItem().toString().equals("Color Std Dev"))
                    scalarCombo8.setEnabled(false);
                else
                    scalarCombo8.setEnabled(true);
            }
        });
        
        scalarCombo7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  
                if (view != null) {

                    netLayoutInlineNew.changeColorInline();
                    estruturaGraphInline.getGraph().setSelectionCells(estruturaGraphInline.getGraph().getSelectionCells());
                    estruturaGraphInline.getGraph().repaint();
                    estruturaGraphInline.getGraph().refresh();

                }


                long milliseconds = (System.currentTimeMillis()-tempoInicio);
                String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                System.out.println("Time change color: "+minutos);
            }
        });
        
        
        
        
        
        scalarCombo8.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent evt) {
                netLayoutInlineNew.setTypeColorInline(evt.getItem().toString());

            }
        });
        
        
        scalarCombo8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  
        
                if (view != null) {
                        netLayoutInlineNew.changeColorInline();
                        estruturaGraphInline.getGraph().setSelectionCells(estruturaGraphInline.getGraph().getSelectionCells());
                        estruturaGraphInline.getGraph().repaint();
                        estruturaGraphInline.getGraph().refresh();
                    }

                    long milliseconds = (System.currentTimeMillis()-tempoInicio);
                    String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                    System.out.println("Time change color: "+minutos);
                }
        });
        
        
        
        scalarCombo5.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent evt) {
                netLayoutInlineNew.setColorEdgeInline(evt.getItem().toString());
                
                if(!evt.getItem().toString().equals("Color"))
                    scalarCombo6.setEnabled(false);
                else
                    scalarCombo6.setEnabled(true);
            }
        });
        
        scalarCombo5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  
                if (view != null) {
                    netLayoutInlineNew.setMaxTime(netLayout.getMaxTime());
                    netLayoutInlineNew.setResolution(netLayout.resolution);
                    netLayoutInlineNew.changeColorEdgeInline();
                    estruturaGraphInline.getGraph().setSelectionCells(estruturaGraphInline.getGraph().getSelectionCells());
                    estruturaGraphInline.getGraph().repaint();
                    estruturaGraphInline.getGraph().refresh();

                }


                long milliseconds = (System.currentTimeMillis()-tempoInicio);
                String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                System.out.println("Time change color: "+minutos);
            }
        });
        
        
        scalarCombo6.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent evt) {
                netLayoutInlineNew.setTypeColorEdge(evt.getItem().toString());

            }
        });
        
        
        scalarCombo6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  
        
                if (view != null) {
                        netLayoutInlineNew.changeColorEdgeInline();
                        estruturaGraphInline.getGraph().setSelectionCells(estruturaGraphInline.getGraph().getSelectionCells());
                        estruturaGraphInline.getGraph().repaint();
                        estruturaGraphInline.getGraph().refresh();
                    }

                    long milliseconds = (System.currentTimeMillis()-tempoInicio);
                    String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                    System.out.println("Time change color: "+minutos);
                }
        });
        
        
        /*
        this.cf.backgroundColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  

                if (view != null) {
                   
                   Color color = netLayoutInlineNew.firstColor;
                   if (color != null) {
                       estruturaGraphInline.getViewport().setBackground(color);
                       estruturaGraphInline.getGraph().repaint();
                       estruturaGraphInline.getGraph().refresh();
                   }
               }

                long milliseconds = (System.currentTimeMillis()-tempoInicio);
                String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                System.out.println("Time change color: "+minutos);
            }
            
        });
        */
        
        
        /*
        this.cf.templateCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  
        
                    if (view != null) {
                        if(netLayoutInlineNew.getTemplateColor().equals("Original"))
                        {
                            //cf.sizeCombo.setSelectedIndex(0);
                            //cf.formCombo.setSelectedIndex(0);
                            scalarCombo7.setSelectedIndex(0);
                            scalarCombo1.setSelectedIndex(0);
                            scalarCombo3.setSelectedIndex(0);
                            estruturaGraphInline.getViewport().setBackground(Color.WHITE);
                            showEdgesCheckBox.setSelected(true);
                            showEdgesCheckBox3.setSelected(true);
                        }
                        else if(netLayoutInlineNew.getTemplateColor().equals("Colored"))
                        {
                            //cf.sizeCombo.setSelectedIndex(0);
                            //cf.formCombo.setSelectedIndex(0);
                            scalarCombo7.setSelectedIndex(1);
                            scalarCombo1.setSelectedIndex(2);
                            scalarCombo3.setSelectedIndex(1);
                            estruturaGraphInline.getViewport().setBackground(Color.WHITE);
                            showEdgesCheckBox.setSelected(true);
                            showEdgesCheckBox3.setSelected(true);
                        }
                    }

                    long milliseconds = (System.currentTimeMillis()-tempoInicio);
                    String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                    System.out.println("Time change color: "+minutos);
                }
        });*/
        
        /*
        this.cf.autoAjustButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                long tempoInicio = System.currentTimeMillis();  

                if (view != null) {
                   
                    cf.scalarCombo7.setSelectedIndex(1);
                    cf.sizeCombo.setSelectedIndex(1);
                    cf.formCombo.setSelectedIndex(1);
                    showEdgesCheckBox.setSelected(false);
                    showEdgesCheckBox1.setSelected(false);
                    cf.backgroundColor.doClick();
                    netLayoutInlineNew.setShowEdges(showEdgesCheckBox.isSelected());
                    netLayoutInlineNew.setShowEdgesHorizontalLines(showEdgesCheckBox1.isSelected());
                    //zoomToFit.doClick();
                    
               }

                long milliseconds = (System.currentTimeMillis()-tempoInicio);
                String minutos = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(milliseconds),TimeUnit.MILLISECONDS.toSeconds(milliseconds) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
                System.out.println("Time change color: "+minutos);
            }
            
        });*/
        
        
        
        mxGraphComponent graphComponentScalarEdge = netLayout.graphComponentScalarEdge;
        graphComponentScalarEdge.setConnectable(false);
        graphComponentScalarEdge.getGraph().setAllowDanglingEdges(false);
        graphComponentScalarEdge.getGraph().setAllowLoops(false);
        graphComponentScalarEdge.getGraph().setAutoOrigin(false);
        graphComponentScalarEdge.getGraph().setConnectableEdges(false);
        graphComponentScalarEdge.getGraph().setCellsDisconnectable(false);
        graphComponentScalarEdge.getGraph().setCellsBendable(false);
        graphComponentScalarEdge.getGraph().setDropEnabled(false);
        graphComponentScalarEdge.getGraph().setSplitEnabled(false);
        graphComponentScalarEdge.getGraph().setCellsEditable(false);
        graphComponentScalarEdge.setSize(new Dimension(360, 30));
        graphComponentScalarEdge.setPreferredSize(new Dimension(360, 30));
        graphComponentScalarEdge.setBorder(null);
        graphComponentScalarEdge.getViewport().setBackground(strong);
        
        jPanel5.add(graphComponentScalarEdge);
        
        jPanel3.revalidate();
        jPanel3.repaint();
        
        
        mxGraphComponent graphComponentScalarInline = netLayoutInlineNew.graphComponentScalarInline;
        graphComponentScalarInline.setConnectable(false);
        graphComponentScalarInline.getGraph().setAllowDanglingEdges(false);
        graphComponentScalarInline.getGraph().setAllowLoops(false);
        graphComponentScalarInline.getGraph().setAutoOrigin(false);
        graphComponentScalarInline.getGraph().setConnectableEdges(false);
        graphComponentScalarInline.getGraph().setCellsDisconnectable(false);
        graphComponentScalarInline.getGraph().setCellsBendable(false);
        graphComponentScalarInline.getGraph().setDropEnabled(false);
        graphComponentScalarInline.getGraph().setSplitEnabled(false);
        graphComponentScalarInline.getGraph().setCellsEditable(false);
        graphComponentScalarInline.setSize(new Dimension(360, 30));
        graphComponentScalarInline.setPreferredSize(new Dimension(360, 30));
        graphComponentScalarInline.setBorder(null);
        graphComponentScalarInline.getViewport().setBackground(strong);
        
        jPanel8.add(graphComponentScalarInline);
        
        
        
        
        mxGraphComponent graphComponentScalarEdgeInline = netLayoutInlineNew.graphComponentScalarEdgeInline;
        graphComponentScalarEdgeInline.setConnectable(false);
        graphComponentScalarEdgeInline.getGraph().setAllowDanglingEdges(false);
        graphComponentScalarEdgeInline.getGraph().setAllowLoops(false);
        graphComponentScalarEdgeInline.getGraph().setAutoOrigin(false);
        graphComponentScalarEdgeInline.getGraph().setConnectableEdges(false);
        graphComponentScalarEdgeInline.getGraph().setCellsDisconnectable(false);
        graphComponentScalarEdgeInline.getGraph().setCellsBendable(false);
        graphComponentScalarEdgeInline.getGraph().setDropEnabled(false);
        graphComponentScalarEdgeInline.getGraph().setSplitEnabled(false);
        graphComponentScalarEdgeInline.getGraph().setCellsEditable(false);
        graphComponentScalarEdgeInline.setSize(new Dimension(360, 30));
        graphComponentScalarEdgeInline.setPreferredSize(new Dimension(360, 30));
        graphComponentScalarEdgeInline.setBorder(null);
        graphComponentScalarEdgeInline.getViewport().setBackground(strong);
        
        jPanel9.add(graphComponentScalarEdgeInline);
        
        
        jPanel4.revalidate();
        jPanel4.repaint();
        
    }

    /**
     * @return the rf
     */
    public ResolutionForm getRf() {
        return rf;
    }

    /**
     * @param rf the rf to set
     */
    public String resolutionS;
    
    public void setRf(int max, int min, int minSelected, int maxSelected, String res) {
        ResolutionForm rf = new ResolutionForm(this,min,max,res, minSelected, maxSelected);
        
        resolutionS = res;
        
        rf.setSize(new Dimension(499,270));
        rf.setPreferredSize(new Dimension(499,270));
        this.rf = rf;
    }

    
    
   

    private void enableToolsCNO() {
        cnoSettingsPanel.setEnabled(true);
        showEdgesInterCommunitiesCheckBox.setEnabled(true);
        showEdgesIntraCommunitiesCheckBox.setEnabled(true);
        multiLevelLabel.setEnabled(true);
        leftArrowCommunity.setEnabled(true);
        RightArrowCommunity.setEnabled(true);
        stateMultiLevel.setEnabled(true);
        stateMultiLevel.setText(netLayoutInlineNew.stateCommunityROCMultiLevel+"/"+(netLayoutInlineNew.comunidadesROCMultilevel.size()-1));
    }
    
    private void disableToolsCNO() {
        cnoSettingsPanel.setEnabled(false);
        showEdgesInterCommunitiesCheckBox.setEnabled(false);
        showEdgesInterCommunitiesCheckBox.setSelected(false);
        showEdgesIntraCommunitiesCheckBox.setEnabled(false);
        showEdgesIntraCommunitiesCheckBox.setSelected(false);
        //showEdgesTemporalCheckBox.setSelected(true);
        //netLayoutInlineNew.setShowEdges(showEdgesTemporalCheckBox.isSelected());
        netLayoutInlineNew.showEdgesCommunities = false;
        netLayoutInlineNew.showEdges = true;
        multiLevelLabel.setEnabled(false);
        leftArrowCommunity.setEnabled(false);
        RightArrowCommunity.setEnabled(false);
        stateMultiLevel.setEnabled(false);
        stateMultiLevel.setText("0/0");
    }
    
    private class MyMouseWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if(e.getPreciseWheelRotation() == 1.0){//Rotated Down
                if (view != null) {
                    if(structurePanel.isShowing())
                        getView().zoomOut();
                    if(temporalPanel.isShowing())
                        getView2().zoomOut();
                }
                
                if (viewMatrix != null) {
                    if(matrixPanel.isShowing())
                            viewMatrix.zoomOut();
                }
                
            }
            else if(e.getPreciseWheelRotation() == -1.0){
                if (view != null) {
                    if(structurePanel.isShowing())
                        getView().zoomIn();
                    if(temporalPanel.isShowing())
                        getView2().zoomIn();
                }
                
                if (viewMatrix != null) {
                    if(matrixPanel.isShowing())
                            viewMatrix.zoomIn();
                }
                
            }
        }
        
    }
   
    private ArrayList<Integer> selectedEdges = new ArrayList();
    
    
    ArrayList<Integer> lastSelectedNodesHere = new ArrayList<>();
    
    private Point selectedSource;
    private Point selectedTarget;
    private Polygon selectionPolygon;
    private float alpha = 1.0f;
    private int minWidthInline = 6000;
     
    public class ViewLinePanel extends JPanel {
        

        public ViewLinePanel() {
            this.setBackground(java.awt.Color.WHITE);
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
        }
        
        
        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            if(estruturaGraphLine != null){
                estruturaGraphLine.getViewport().setBackground(bg);
            }
        }
         
        
        public void zoomIn() {
            
            estruturaGraphLine.zoomIn();

      
        }

        public void zoomOut() {
            
            estruturaGraphLine.zoomOut();
            
        }
            
    }
    
    public class ViewInlinePanel extends JPanel { 
        

        public ViewInlinePanel() {
            this.setBackground(java.awt.Color.WHITE);
            //this.setLayout(new FlowLayout(FlowLayout.LEFT));
        }
        
        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            if(estruturaGraphInline != null){
                estruturaGraphInline.getViewport().setBackground(bg);
            }
        }
         
        public void setNetLayout(NetLayout layout) {            
//            layout.setSize(layout.adjustSize());
 //           Dimension size = layout.getSize();
//            setPreferredSize(new Dimension(size.width , size.height ));
//            setSize(new Dimension(size.width , size.height ));
            
            //view2.cleanImage();
            //view2.repaint();
        }
        
        
        public void zoomIn() {
            
            estruturaGraphInline.zoomIn();
            
           /*
            zoom = zoom + 2;
            
            //netLayoutInlineNew.changePositionNodes(spacing, zoom);
            netLayoutInlineNew.moveNodesEdges(spacing, zoom);
            
            //setNetLayoutInline(netLayoutInlineNew.graphComponent,ma,false);

            
                netLayoutInlineNew.graphComponent.setPreferredSize(new Dimension(600,600));
                
                netLayoutInlineNew.graphComponent.repaint();
                netLayoutInlineNew.graphComponent.refresh();
                
            estruturaGraphInline.getGraph().repaint();
            estruturaGraphInline.getGraph().refresh();
            */
        }

        public void zoomOut() {
            
            estruturaGraphInline.zoomOut();
            
            
            /*
            zoom = zoom - 2;
            if(zoom > 0){
                
                netLayoutInlineNew.moveNodesEdges(spacing, zoom);

                
                getView2().removeAll();
                
                estruturaGraphInline = netLayoutInlineNew.graphComponent;
            
                //vv.setSize(getView().getSize());
                //vv.setPreferredSize(getView().getSize());
                estruturaGraphInline.getViewport().setOpaque(true);
                estruturaGraphInline.getViewport().setBackground(Color.WHITE);
                estruturaGraphInline.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

                mxRubberband rubberBand = new mxRubberband(estruturaGraphInline);

                estruturaGraphInline.getGraphHandler().setMarkerEnabled(false);
                estruturaGraphInline.setConnectable(false);
                estruturaGraphInline.getGraph().setAllowDanglingEdges(false);
                estruturaGraphInline.getGraph().setAllowLoops(false);
                estruturaGraphInline.getGraph().setAutoOrigin(false);
                estruturaGraphInline.getGraph().setConnectableEdges(false);
                estruturaGraphInline.getGraph().setCellsDisconnectable(false);
                estruturaGraphInline.getGraph().setCellsBendable(false);
                estruturaGraphInline.getGraph().setDropEnabled(false);
                estruturaGraphInline.getGraph().setSplitEnabled(false);
                estruturaGraphInline.getGraph().setCellsEditable(false);
                estruturaGraphInline.getGraphHandler().setRemoveCellsFromParent(false);

                //vv.getGraph().setMaximumGraphBounds(new mxRectangle(0, 0, view.getX(), view.getY()));
                
                getView2().add(estruturaGraphInline);
                
                getView2().setSize(estruturaGraphInline.getGraphControl().getSize());
                getView2().setPreferredSize(estruturaGraphInline.getGraphControl().getSize());
                getView2().validate();
                getView2().repaint();

                //netLayoutInlineNew.changePositionNodes(spacing, zoom);
                
                //setNetLayoutInline(netLayoutInlineNew.graphComponent,ma,false);
                
                
                netLayoutInlineNew.graphComponent.repaint();
                netLayoutInlineNew.graphComponent.refresh();
                
                //estruturaGraphInline.getGraph().repaint();
                //estruturaGraphInline.getGraph().refresh();
            }
            else zoom = 1;
            */
        }
            
    }
    
    public class ViewCommunityPanel extends JPanel { 

        public ViewCommunityPanel() {
            this.setBackground(java.awt.Color.WHITE);
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
        }

        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            
        }

        

        public void zoomIn() {
             
            vvCommunity.zoomIn();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void zoomOut() {
            
            vvCommunity.zoomOut();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void adjustPanel() {
            repaint();
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            if(vvCommunity != null){
                vvCommunity.getViewport().setBackground(bg);
            }
        }


        private boolean labelBold = false;
        private int labelSize = 12;
    }
	
    public class ViewRobotsPanel extends JPanel {

        public ViewRobotsPanel() {
            this.setBackground(java.awt.Color.WHITE);
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
        }

        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            
        }

        

        public void zoomIn() {
             
            vvRobots.zoomIn();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void zoomOut() {
            
            vvRobots.zoomOut();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void adjustPanel() {
            repaint();
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            if(vvRobots != null){
                vvRobots.getViewport().setBackground(bg);
            }
        }


        private boolean labelBold = false;
        private int labelSize = 12;
    }
    
    
    public class ViewMatrixPanel extends JPanel {

        public ViewMatrixPanel() {
            this.setBackground(java.awt.Color.WHITE);
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
        }

        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            
        }

        

        public void zoomIn() {
             
            vvMatrix.zoomIn();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void zoomOut() {
            
            vvMatrix.zoomOut();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void adjustPanel() {
            repaint();
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            if(vvRobots != null){
                vvRobots.getViewport().setBackground(bg);
            }
        }


        private boolean labelBold = false;
        private int labelSize = 12;
    }
    
    
    public class ViewCentralityPanel extends JPanel { 

        public ViewCentralityPanel() {
            this.setBackground(java.awt.Color.WHITE);
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
        }

        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            
        }

        

        public void zoomIn() {
             
            vvCentrality.zoomIn();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void zoomOut() {
            
            vvCentrality.zoomOut();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void adjustPanel() {
            repaint();
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            if(vvCentrality != null){
                vvCentrality.getViewport().setBackground(bg);
            }
        }


        private boolean labelBold = false;
        private int labelSize = 12;
    }
        
    public class ViewPanel extends JPanel { 

        public ViewPanel() {
            this.setBackground(java.awt.Color.WHITE);
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
        }

        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            
        }

        

        public void zoomIn() {
             
            vv.zoomIn();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void zoomOut() {
            
            vv.zoomOut();
            
            //scrollPanel.validate();
            //scrollPanel.repaint();
            
        }

        public void adjustPanel() {
            repaint();
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            if(vv != null){
                vv.getViewport().setBackground(bg);
            }
        }


        private boolean labelBold = false;
        private int labelSize = 12;
    }

    public void setLabelSize(int size) {
        this.view.labelSize = size;
    }

    public void setLabelBold(boolean o) {
        this.view.labelBold = o;
    }

    public int getLabelSize() {
        return this.view.labelSize;
    }

    public boolean isLabelBold() {
        return this.view.labelBold;
    }
    
    private NetLayout netLayout;
    public NetLayoutInlineNew netLayoutInlineNew;
    protected DefaultComboBoxModel scalarComboModel;
    protected DefaultComboBoxModel edgeComboModel;
    protected int highqualityrender = 3;
    protected boolean moveinstances = true;
    protected ViewPanel view;
    protected boolean start = true;
    private String pathDataset;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CountOfSelectedNodes;
    private javax.swing.JMenu Data;
    private javax.swing.JMenu DynamicProcesses;
    private javax.swing.JComboBox EdgeStrokeCombo;
    private javax.swing.JMenu File;
    private javax.swing.JMenu Help;
    private javax.swing.JButton RightArrowCommunity;
    private javax.swing.JMenuItem about;
    public javax.swing.JLabel agingLabel;
    private javax.swing.JLabel agingLabel1;
    public javax.swing.JSlider agingjSlider;
    private javax.swing.JSlider agingjSlider1;
    private javax.swing.JPanel animationSettings;
    private javax.swing.JPanel animationSettings1;
    private javax.swing.JButton backgroundButton;
    private javax.swing.JButton backgroundButton1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel cnoSettingsPanel;
    private javax.swing.JLabel colorLabel;
    private javax.swing.JLabel colorLabel1;
    private javax.swing.JLabel colorLabel2;
    private javax.swing.JLabel colorLabel3;
    private javax.swing.JLabel colorLabel4;
    private javax.swing.JLabel colorLabel5;
    private javax.swing.JLabel colorLabel6;
    private javax.swing.JLabel colorLabel7;
    private javax.swing.JButton compConexas;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JLabel depthSelectionLevel;
    private javax.swing.JSpinner depthSelectionLevelSpinner;
    private javax.swing.JButton detectCommunitiesStructural;
    private javax.swing.JComboBox edgeSamplingCombo;
    private javax.swing.JMenuItem epidemiologyProcesses;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenu exportImageMenu;
    protected javax.swing.JMenuItem exportImgCentrality;
    protected javax.swing.JMenuItem exportImgCommunity;
    protected javax.swing.JMenuItem exportImgMatrix;
    protected javax.swing.JMenuItem exportImgRobots1;
    protected javax.swing.JMenuItem exportImgStream;
    protected javax.swing.JMenuItem exportImgStructural;
    protected javax.swing.JMenuItem exportImgTemporal;
    private javax.swing.JPanel findNodePanel;
    private javax.swing.JButton findNodeTemporal;
    private javax.swing.JComboBox formCombo1;
    private javax.swing.JMenuItem help;
    private javax.swing.JTextField idNodeToFind;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    public javax.swing.JSlider jSlider1;
    public javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JComboBox layoutCombo;
    private javax.swing.JButton leftArrowCommunity;
    private javax.swing.JPanel matrixPanel;
    protected javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel msvPanel;
    private javax.swing.JLabel multiLevelLabel;
    public javax.swing.JComboBox nodeColorMatrixComboBox;
    private javax.swing.JLabel nodeColorMatrixLabel;
    public javax.swing.JComboBox nodeOrderMatrixComboBox;
    private javax.swing.JLabel nodeOrderMatrixLabel;
    private javax.swing.JComboBox nodeSizeCombo;
    private javax.swing.JComboBox nodeSizeCombo1;
    private javax.swing.JLabel numberOfSelectedNodesText;
    private javax.swing.JMenuItem openDatasetMenuItem;
    public javax.swing.JComboBox orderComboBox;
    private javax.swing.JMenuItem randomWalker;
    private javax.swing.JButton removeSelection;
    public javax.swing.JButton runButton;
    public javax.swing.JButton runForceButton1;
    private javax.swing.JComboBox scalarCombo1;
    private javax.swing.JComboBox scalarCombo2;
    private javax.swing.JComboBox scalarCombo3;
    private javax.swing.JComboBox scalarCombo4;
    private javax.swing.JComboBox scalarCombo5;
    private javax.swing.JComboBox scalarCombo6;
    private javax.swing.JComboBox scalarCombo7;
    private javax.swing.JComboBox scalarCombo8;
    private javax.swing.JCheckBox scrollToNodeCenter;
    private javax.swing.JButton seeSelectedNodes;
    protected javax.swing.ButtonGroup selectionButtonGroup;
    private javax.swing.JMenuItem settings;
    private javax.swing.JPanel settingsMatrix;
    private javax.swing.JCheckBox showEdgesCheckBox;
    private javax.swing.JCheckBox showEdgesCheckBox3;
    private javax.swing.JCheckBox showEdgesInterCommunitiesCheckBox;
    private javax.swing.JCheckBox showEdgesIntraCommunitiesCheckBox;
    private javax.swing.JCheckBox showEdgesTemporalCheckBox;
    private javax.swing.JCheckBox showEdgesWeightCheckBox;
    private javax.swing.JCheckBox showInstanceWeightCheckBox;
    private javax.swing.JCheckBox showNodesCheckBox;
    private javax.swing.JCheckBox showNodesCheckBox1;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JLabel speedLabel1;
    public javax.swing.JSlider speedjSlider;
    private javax.swing.JSlider speedjSlider1;
    private javax.swing.JLabel stateMultiLevel;
    private javax.swing.JMenuItem statistic;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton stopButton1;
    private javax.swing.JPanel structuralSettings;
    private javax.swing.JPanel structurePanel;
    private javax.swing.ButtonGroup tam;
    private javax.swing.JRadioButton tamColored;
    private javax.swing.JCheckBox tamOpacity;
    private javax.swing.JRadioButton tamWhite;
    private javax.swing.JPanel temporalPanel;
    private javax.swing.JPanel temporalSettings;
    public javax.swing.JTextField timeAnimationValue;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JLabel timeLabel1;
    public javax.swing.JTextField timeStructuralAnimationValue;
    private javax.swing.JSlider timeStructuraljSlider;
    private javax.swing.JCheckBox upperTriangularMatrix;
    private javax.swing.JPanel zoomAndScroolMatrix;
    private javax.swing.JPanel zoomAndScroolStructural;
    private javax.swing.JPanel zoomAndScroolTemporalPanel;
    private javax.swing.JButton zoomInButton;
    private javax.swing.JButton zoomInButtonMatrix;
    private javax.swing.JButton zoomInButtonTemporal;
    private javax.swing.JButton zoomOutButton;
    private javax.swing.JButton zoomOutButtonMatrix;
    private javax.swing.JButton zoomOutButtonTemporal;
    private javax.swing.JPanel zoomPanelMatrix;
    private javax.swing.JPanel zoomPanelStructure;
    private javax.swing.JPanel zoomPanelTemporal;
    private javax.swing.JButton zoomToDefault;
    private javax.swing.JButton zoomToDefault1;
    private javax.swing.JButton zoomToDefault7;
    private javax.swing.JButton zoomToFit;
    private javax.swing.JButton zoomToFit1;
    private javax.swing.JButton zoomToFit7;
    private javax.swing.JButton zoomToLineGraph;
    private javax.swing.JButton zoomToTemporal;
    // End of variables declaration//GEN-END:variables
}
