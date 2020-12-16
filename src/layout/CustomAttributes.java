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

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Claudio Linhares
 */
public class CustomAttributes implements Serializable{

    /**
     * @return the externalWeight
     */
    public int getExternalWeight() {
        return externalWeight;
    }

    /**
     * @param externalWeight the externalWeight to set
     */
    public void setExternalWeight(int externalWeight) {
        this.externalWeight = externalWeight;
    }
    
private int weight, weightTemp, externalWeight;
private ArrayList<Integer> time;
private boolean isNode, isEdge, isFirst;
private String label;

public CustomAttributes(int time, int weight, String label, boolean isEdge, boolean isNode) {
    this.time = new ArrayList();
    this.time.add(time);
    
    this.weight = weight;
    this.externalWeight = weight;
    this.label = label;
    this.isEdge = isEdge;
    this.isNode = isNode;
    this.weightTemp = 0;
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

    /**
     * @return the isNode
     */
    public boolean isNode() {
        return isNode;
    }

    /**
     * @param isNode the isNode to set
     */
    public void setNode(boolean isNode) {
        this.isNode = isNode;
    }

    /**
     * @return the isEdge
     */
    public boolean isEdge() {
        return isEdge;
    }

    /**
     * @param isEdge the isEdge to set
     */
    public void setEdge(boolean isEdge) {
        this.isEdge = isEdge;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    @Override
    public String toString() {
        if(isEdge)
            return this.weight+"";
        else
            return this.label+"";
    }

    /**
     * @return the weightTemp
     */
    public int getWeightTemp() {
        return weightTemp;
    }

    /**
     * @param weightTemp the weightTemp to set
     */
    public void setWeightTemp(int weightTemp) {
        this.weightTemp = weightTemp;
    }

    /**
     * @return the isFirst
     */
    public boolean isIsFirst() {
        return isFirst;
    }

    /**
     * @param isFirst the isFirst to set
     */
    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    /**
     * @return the time
     */
    public ArrayList<Integer> getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(int time) {
        this.time.add(time);
    }

    
   
    
    
}