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

import java.awt.*;
import java.util.ArrayList;

public class NetInstance implements Comparable{
    
    private int id,idOriginal,weight;
    private NetLayout layout;
    private float x, y;
    private ArrayList<PositionNode> coordinates;
    protected int inssize;
    private boolean selected;
    private boolean showinstanceWeight;
    
    private int time;
    
    public NetInstance(int id) {
        this.id = id;
        this.weight = 0;
    }
    
    public NetInstance(int id, PositionNode coordinate, int weight) {
        this(id);
        this.coordinates.add(coordinate);
        this.weight = weight;
        
    }
    
    public NetInstance(int id, int idOriginal, NetLayout layout, PositionNode coordinate, int time, int weight) {
        this(id);
        this.time = time;
        this.idOriginal = idOriginal;
        this.layout = layout;
        this.coordinates.add(coordinate);
        this.weight = weight;
        
    }
    
    public NetInstance(int id, int idOriginal, NetLayout layout, int time, int weight) {
        this(id);
        this.time = time;
        this.idOriginal = idOriginal;
        this.layout = layout;
        this.weight = weight;
        
    }
    
    public NetInstance createClone(NetLayout layout) {
        NetInstance newNode = new NetInstance(getId());
        newNode.setLayout(layout);
        newNode.setX(x);
        newNode.setY(y);
        return newNode;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        if (layout != null) layout.setChanged();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        if (layout != null) layout.setChanged();
    }
    

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
        
    public int limitColor(float color){
         if(color > 255)
               color = 255;
         else if(color < 0)
               color = 0;
        return (int) color;
    }
    
    public void draw(Graphics2D g2, boolean highquality, float alpha) {
        
        for(PositionNode pn : coordinates){
            Rectangle boundingBox = new Rectangle(getLayout().getSize());

            if (highquality) 
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //alpha = 1.0f;

            int xaux = (((int) pn.getX()) <= 0) ? 1 : (((int) this.x) < (int)boundingBox.getWidth()) ? (int) pn.getX() : (int)boundingBox.getWidth() - 1;
            int yaux = (((int) pn.getY()) <= 0) ? 1 : (((int) this.y) < (int)boundingBox.getHeight()) ? (int) pn.getY() : (int)boundingBox.getHeight() - 1;

            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha));
            float vMax = getWeight();
            float vMin = getWeight();

            int numberEdges = 0;

            //Inicializando o vetor;            
            for(NetInstance instance : getLayout().getNodes()){
                    Integer i = instance.getWeight();
                    if(i > vMax)
                        vMax = i;
                    if(i < vMin)
                        vMin = i;
            }

            
            float maxColor = 1;
            float minColor = 0;

            float maxSize = 20;
            float minSize = 5;

            inssize = 5;

            //this.instanceWeight = numberEdges;

            //x = y * ( max - min) / (vMax - vMin) + min
            double insize = getWeight() * (maxColor - minColor)/ (vMax - vMin) + minColor;
            if(insize > 1){
                insize = 1;
            }

            g2.setColor(Color.BLACK);


            g2.fillOval(xaux-inssize,yaux-inssize,inssize*2,inssize*2);
            g2.setColor(Color.BLACK);
            g2.drawOval(xaux-inssize,yaux-inssize,inssize*2,inssize*2);

            if (this.getLayout().getShowInstanceWeight()) {
                String label = Integer.toString(numberEdges);
                float x = 5 + (float) Math.abs(getX());
                float y = (float) Math.abs(getY());
                //Getting the font information
                FontMetrics metrics = g2.getFontMetrics(g2.getFont());
                //Getting the label size
                int width = metrics.stringWidth(label);
                int height = metrics.getAscent();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
                g2.setPaint(Color.WHITE);
                g2.fill(new Rectangle((int) x - 2, (int) y - height, width + 4, height + 4));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                g2.setColor(Color.BLACK);
                g2.drawRect((int) x - 2, (int) y - height, width + 4, height + 4);
                g2.drawString(label, x, y);
            }



            g2.setStroke(new BasicStroke(1.0f));
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    public NetLayout getLayout() {
        return layout;
    }

    public void setLayout(NetLayout layout) {
        this.layout = layout;
    }
    
    public boolean isInside(int x, int y) {
        return (Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2)) <= inssize);
    }
    
    public boolean isInside(Rectangle rect) {
        return ((x >= rect.x) && (x - rect.x < rect.width)) &&
               ((y >= rect.y) && (y - rect.y < rect.height));
    }
    
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the idOriginal
     */
    public int getIdOriginal() {
        return idOriginal;
    }

    /**
     * @param idOriginal the idOriginal to set
     */
    public void setIdOriginal(int idOriginal) {
        this.idOriginal = idOriginal;
    }

    /**
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(int time) {
        this.time = time;
    }
    
    @Override
    public int compareTo(Object o) {
        NetInstance weight = (NetInstance) o;
        if (this.weight > weight.getWeight()) {  
             return 1;  
        }  
        if (this.weight < weight.getWeight()) {  
             return -1;  
        }  
        return 0; 
    }

    /**
     * @return the coordinates
     */
    public ArrayList<PositionNode> getCoordinates() {
        return coordinates;
    }

    /**
     * @param coordinates the coordinates to set
     */
    public void setCoordinates(PositionNode coordinates) {
        this.coordinates.add(coordinates);
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

}
