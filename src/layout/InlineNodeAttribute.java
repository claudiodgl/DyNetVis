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

/**
 *
 * @author Claudio Linhares
 */
public class InlineNodeAttribute implements Comparable {

    /**
     * @return the sourceDirectionInfection
     */
    public boolean isSourceDirectionInfection() {
        return sourceDirectionInfection;
    }

    /**
     * @param sourceDirectionInfection the sourceDirectionInfection to set
     */
    public void setSourceDirectionInfection(boolean sourceDirectionInfection) {
        this.sourceDirectionInfection = sourceDirectionInfection;
    }

    /**
     * @return the stateBusy
     */
    public boolean isStateBusy() {
        return stateBusy;
    }

    /**
     * @param stateBusy the stateBusy to set
     */
    public void setStateBusy(boolean stateBusy) {
        this.stateBusy = stateBusy;
    }

    /**
     * @return the stateInfection
     */
    public int getStateInfection() {
        return stateInfection;
    }

    /**
     * @param stateInfection the stateInfection to set
     */
    public void setStateInfection(int stateInfection) {
        this.stateInfection = stateInfection;
    }

    /**
     * @return the inDegree
     */
    public int getIndegree() {
        return inDegree;
    }

    /**
     * @param indegree the inDegree to set
     */
    public void setIndegree(int indegree) {
        this.inDegree = indegree;
    }

    /**
     * @return the outDegree
     */
    public int getOutdegree() {
        return outDegree;
    }

    /**
     * @param outdegree the outDegree to set
     */
    public void setOutdegree(int outdegree) {
        this.outDegree = outdegree;
    }

    /**
     * @return the activityDegree
     */
    public int getActivityDegree() {
        return activityDegree;
    }

    /**
     * @param activityDegree the activityDegree to set
     */
    public void setActivityDegree(int activityDegree) {
        this.activityDegree = activityDegree;
    }

    /**
     * @return the edgeSize
     */
    public int getEdgeSize() {
        return edgeSize;
    }

    /**
     * @param edgeSize the edgeSize to set
     */
    public void setEdgeSize(int edgeSize) {
        this.edgeSize = edgeSize;
    }

    public InlineNodeAttribute(int time, int id_original, int x_original, int y_original, String label, boolean isTimeNode, boolean isLineNode, boolean isNode) {
        this.time = time;
        this.id_original = id_original;
        this.x_original = x_original;
        this.y_original = y_original;
        this.x_atual = x_original;
        this.y_atual = y_original;
        this.isTimeNode = isTimeNode;
        this.isLineNode = isLineNode;
        this.isNode = isNode;
        this.label = label;
        
    }
    

    
    private int time, degree, id_original, x_original, y_original, x_atual, y_atual,weightInTime , heightEdge , heightEdge_atual, edgeSize, inDegree, outDegree, activityDegree;
    private String label;
    private boolean isTimeNode, isLineNode, isNode,left , isHistogram, isGraphLine , isEdge, isGraphLineNodes, sourceDirectionInfection;
    
    //Epidemiology
    private boolean stateBusy;
    private int stateInfection;
    //1 - (S)usceptible
    //2 - (I)nfected
    //3 - (R)ecovered

    
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

    /**
     * @return the id_original
     */
    public int getId_original() {
        return id_original;
    }

    /**
     * @param id_original the id_original to set
     */
    public void setId_original(int id_original) {
        this.id_original = id_original;
    }

    /**
     * @return the x_original
     */
    public int getX_original() {
        return x_original;
    }

    /**
     * @param x_original the x_original to set
     */
    public void setX_original(int x_original) {
        this.x_original = x_original;
    }

    /**
     * @return the y_original
     */
    public int getY_original() {
        return y_original;
    }

    /**
     * @param y_original the y_original to set
     */
    public void setY_original(int y_original) {
        this.y_original = y_original;
    }

    /**
     * @return the x_atual
     */
    public int getX_atual() {
        return x_atual;
    }

    /**
     * @param x_atual the x_atual to set
     */
    public void setX_atual(int x_atual) {
        this.x_atual = x_atual;
    }

    /**
     * @return the y_atual
     */
    public int getY_atual() {
        return y_atual;
    }

    /**
     * @param y_atual the y_atual to set
     */
    public void setY_atual(int y_atual) {
        this.y_atual = y_atual;
    }

    /**
     * @return the isTimeNode
     */
    public boolean isTimeNode() {
        return isTimeNode;
    }

    /**
     * @param isTimeNode the isTimeNode to set
     */
    public void setIsTimeNode(boolean isTimeNode) {
        this.isTimeNode = isTimeNode;
    }

    /**
     * @return the isLineNode
     */
    public boolean isLineNode() {
        return isLineNode;
    }

    /**
     * @param isLineNode the isLineNode to set
     */
    public void setIsLineNode(boolean isLineNode) {
        this.isLineNode = isLineNode;
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
    public void setIsNode(boolean isNode) {
        this.isNode = isNode;
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

    /**
     * @return the weight
     */
    public int getDegree() {
        return degree;
    }

    /**
     * @param degree the weight to set
     */
    public void setDegree(int degree) {
        this.degree = degree;
    }
    
    @Override
    public String toString() {
        return this.label;
    }
    
    @Override
    public int compareTo(Object o) {
        InlineNodeAttribute weight = (InlineNodeAttribute) o;
        if (this.degree > weight.getDegree()) {
             return 1;  
        }  
        if (this.degree < weight.getDegree()) {  
             return -1;  
        }  
        return 0; 
    }

    /**
     * @return the weightInTime
     */
    public int getWeightInTime() {
        return weightInTime;
    }

    /**
     * @param weightInTime the weightInTime to set
     */
    public void setWeightInTime(int weightInTime) {
        this.weightInTime = weightInTime;
    }

    /**
     * @return the left
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(boolean left) {
        this.left = left;
    }

    /**
     * @return the isHistogram
     */
    public boolean isIsHistogram() {
        return isHistogram;
    }

    /**
     * @param isHistogram the isHistogram to set
     */
    public void setIsHistogram(boolean isHistogram) {
        this.isHistogram = isHistogram;
    }

    /**
     * @return the isGraphLine
     */
    public boolean isGraphLine() {
        return isGraphLine;
    }

    /**
     * @param isGraphLine the isGraphLine to set
     */
    public void setIsGraphLine(boolean isGraphLine) {
        this.isGraphLine = isGraphLine;
    }

    /**
     * @return the isEdge
     */
    public boolean isEdge() {
        return isEdge;
    }
    
    private int origin;
    private int destiny;
    private boolean isNorth;
    
    /**
     * @param isEdge the isEdge to set
     */
    public void setIsEdge(boolean isEdge, int origin, int destiny, int heightEdge, boolean isNorth) {
        this.isEdge = isEdge;
        this.setOrigin(origin);
        this.setDestiny(destiny);
        this.heightEdge = heightEdge;
        this.heightEdge_atual = heightEdge;
        this.isNorth = isNorth;
    }

    /**
     * @return the origin
     */
    public int getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(int origin) {
        this.origin = origin;
    }

    /**
     * @return the destiny
     */
    public int getDestiny() {
        return destiny;
    }

    /**
     * @param destiny the destiny to set
     */
    public void setDestiny(int destiny) {
        this.destiny = destiny;
    }

    /**
     * @return the heightEdge
     */
    public int getOriginalHeightEdge() {
        return heightEdge;
    }

    /**
     * @param heightEdge the heightEdge to set
     */
    public void setHeightEdge(int heightEdge) {
        this.heightEdge = heightEdge;
    }

    /**
     * @return the heightEdge_atual
     */
    public int getHeightEdge_atual() {
        return heightEdge_atual;
    }

    /**
     * @param heightEdge_atual the heightEdge_atual to set
     */
    public void setHeightEdge_atual(int heightEdge_atual) {
        this.heightEdge_atual = heightEdge_atual;
    }

    /**
     * @return the isNorth
     */
    public boolean isIsNorth() {
        return isNorth;
    }

    /**
     * @param isNorth the isNorth to set
     */
    public void setIsNorth(boolean isNorth) {
        this.isNorth = isNorth;
    }

    /**
     * @return the isGraphLineNodes
     */
    public boolean isGraphLineNodes() {
        return isGraphLineNodes;
    }

    /**
     * @param isGraphLineNodes the isGraphLineNodes to set
     */
    public void setIsGraphLineNodes(boolean isGraphLineNodes) {
        this.isGraphLineNodes = isGraphLineNodes;
    }
}
