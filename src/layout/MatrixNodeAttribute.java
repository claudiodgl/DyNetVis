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

import java.util.ArrayList;
/**
 *
 * @author Claudio Linhares
 */
public class MatrixNodeAttribute{

    private int x_original, y_original, x_atual, y_atual;
    private ArrayList<Integer> listTime;
    private String id_original, communityColor,nodeBorderColor;
    private boolean nodeIndice,upperTriangularNode;
    
    public MatrixNodeAttribute(String id_original, int x_original, int y_original, boolean nodeIndice, boolean upperTriangularNode) {
        this.id_original = id_original;
        this.x_original = x_original;
        this.y_original = y_original;
        this.x_atual = x_original;
        this.y_atual = y_original;
        listTime = new ArrayList();
        this.nodeIndice = nodeIndice;
        this.upperTriangularNode = upperTriangularNode;
    }
    
    @Override
    public String toString() {
        String[] n = id_original.split(" ");
        return n[0];
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
     * @return the listTime
     */
    public ArrayList<Integer> getListTime() {
        return listTime;
    }

    /**
     * @param listTime the listTime to set
     */
    public void setListTime(ArrayList<Integer> listTime) {
        this.listTime = listTime;
    }

    /**
     * @return the id_original
     */
    public String getId_original() {
        return id_original;
    }

    /**
     * @param id_original the id_original to set
     */
    public void setId_original(String id_original) {
        this.id_original = id_original;
    }

    /**
     * @return the nodeIndice
     */
    public boolean isNodeIndice() {
        return nodeIndice;
    }

    /**
     * @param nodeIndice the nodeIndice to set
     */
    public void setNodeIndice(boolean nodeIndice) {
        this.nodeIndice = nodeIndice;
    }

    
    /**
     * @return the upperTriangularNode
     */
    public boolean isUpperTriangularNode() {
        return upperTriangularNode;
    }

    /**
     * @param upperTriangularNode the upperTriangularNode to set
     */
    public void setUpperTriangularNode(boolean upperTriangularNode) {
        this.upperTriangularNode = upperTriangularNode;
    }

    /**
     * @return the communityColor
     */
    public String getCommunityColor() {
        return communityColor;
    }

    /**
     * @param communityColor the communityColor to set
     */
    public void setCommunityColor(String communityColor) {
        this.communityColor = communityColor;
    }

    /**
     * @return the nodeBorderColor
     */
    public String getNodeBorderColor() {
        return nodeBorderColor;
    }

    /**
     * @param nodeBorderColor the nodeBorderColor to set
     */
    public void setNodeBorderColor(String nodeBorderColor) {
        this.nodeBorderColor = nodeBorderColor;
    }

}
