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

import java.awt.Rectangle;

/**
 *
 * @author Claudio Linhares
 */
public class NetEdge {
    
    private int source, target, time;
    private float weight; 
    protected int inssize;
    private boolean selected;
    
    public NetEdge(int source, int target, int time, float weight) {
        this.source = source;
        this.target = target;
        this.time = time;
        this.weight = weight;
        this.selected = false;
    }
    
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    
    public double isInside(float xSource, float ySource, float xTarget, float yTarget, int xClick, int yClick) {

        //m = y2 - y1 / x2 - x1
        float m = (yTarget - ySource) / (xTarget - xSource);    

        //m0 = -1 / m ;
        float m0 = -1 / m;
        
        //y - y0 = m0 (x - x0)
        float a0 = m0;
        float b0 = -1;
        float c0 = yClick - (xClick * m0);
        
        //y1 – y2 = a
        //x2 – x1 = b
        //x1y2 – x2y1 = c

        float a = ySource - yTarget;
        float b = xTarget - xSource;
        float c = (xSource * yTarget) - (xTarget * ySource);
        
        float xInter = ((c * b0) - (c0 * b)) / ((a0 * b) - (a * b0));
        float yInter = (- c - c0 - (a + a0)* xInter) / (b + b0);
        
        //d = | ax0 + by0 + c | / sqrt(a^2 + b^2)
        double d = Math.abs( (a * xClick) + (b * yClick) + c ) / Math.sqrt(Math.pow(a,2) + Math.pow(b,2));

        if(xSource > xTarget){
            float temp = xTarget;
            xTarget = xSource;
            xSource = temp;
        }
        if(ySource > yTarget){
            float temp2 = yTarget;
            yTarget = ySource;
            ySource = temp2;
        }
        
        if(xInter >= xSource && xInter <= xTarget && yInter >= ySource && yInter <= yTarget){
              return d;
         }
         else
              return d = 999999;

    }
    
    public boolean isInside(Rectangle rect) {
        return false;
        //return ((x >= rect.x) && (x - rect.x < rect.width)) &&
              // ((y >= rect.y) && (y - rect.y < rect.height));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NetEdge) {
            return (((NetEdge)o).getSource() == source && ((NetEdge)o).getTarget() == target);
        }else return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.source;
        hash = 71 * hash + this.target;
        return hash;
    }
    
}
