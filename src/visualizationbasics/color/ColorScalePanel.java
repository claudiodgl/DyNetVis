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

package visualizationbasics.color;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class ColorScalePanel extends JPanel {

    public ColorScalePanel(JPanel gv) {
//        this.gv = gv;

        this.scale = new ColorScalePanel.ColorScale();

        this.maxLabel.setForeground(java.awt.Color.GRAY);
        this.maxLabel.setFont(new java.awt.Font("Verdana", Font.BOLD, 10));

        this.minLabel.setForeground(java.awt.Color.GRAY);
        this.minLabel.setFont(new java.awt.Font("Verdana", Font.BOLD, 10));

//        this.scale.setBorder(new javax.swing.border.LineBorder(java.awt.Color.GRAY, 1, true));
//        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        this.setToolTipText("Click to change the color scale");

        this.setLayout(new java.awt.BorderLayout(5, 5));
        this.add(this.scale, java.awt.BorderLayout.CENTER);
        this.add(this.maxLabel, java.awt.BorderLayout.EAST);
        this.add(this.minLabel, java.awt.BorderLayout.WEST);

        this.addMouseListener(new MouseClickedListener());
    }

    public void setColorTable(ColorTable colorTable) {
        this.colorTable = colorTable;
    }

    class MouseClickedListener extends MouseAdapter {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            super.mouseClicked(evt);
//            if (ColorScalePanel.this.gv != null) {
//                ColorScaleChange.getInstance(gv.getProjectionExplorerView(), gv).display();
//            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            ColorScalePanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            ColorScalePanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

    }

    public class ColorScale extends javax.swing.JPanel {

        @Override
        public void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);

            if (colorTable != null) {
//                minLabel.setForeground(colorTable.getColor(0.0f));
//                maxLabel.setForeground(colorTable.getColor(1.0f));

                //Getting the panel dimension - horizontal fill
                java.awt.Dimension size = this.getSize();
                int height = size.height;
                int width = size.width;

                for (int i = 0; i <= width; i++) {
                    float index = ((float) i) / ((float) width);
                    g.setColor(colorTable.getColor(index));
                    g.drawRect(i, 0, i, height);
                    g.fillRect(i, 0, i, height);
                }
            }
        }

    }

    private javax.swing.JLabel maxLabel = new javax.swing.JLabel("Max");
    private javax.swing.JLabel minLabel = new javax.swing.JLabel("Min");
    private ColorScalePanel.ColorScale scale;
    private ColorTable colorTable;
//    private Viewer gv;
}
