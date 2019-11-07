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

package randomNetworks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author Jean
 * 
 * Based on https://github.com/gabrielet/Network-Randomizer/blob/master/src/main/java/network/randomizer/internal/BarabasiAlbertModel.java
 * but including the multiple_node idea from https://rdrr.io/github/thongphamthe/PAFit/man/generate_BA.html
 * which allows more than one node at each timestamp, being suitable for random temporal network  generator.
 * 
 * 
 * 
 * The algorithm begins by constructing a connected graph with m0 nodes and m*m0/2 edges. 
 * It then iteratively adds one node at a time until there are N nodes in total. Each new node has the initial degree of m,
 * with its m neighbours chosen with probabilities proportional to their degrees. This type of node addition is called
 * the "preferential attachment", meaning that the more connected the node is, the more likely it is to receive new neighbors.
 * https://github.com/gabrielet/Network-Randomizer/blob/master/NetworkRandomizer_User_Manual.pdf
 * 
 * 
 * Jean TODO: Usar Community affiliation graph do link acima?
 * 
 */
public class TemporalBarabasiAlbertModel {
    private int N; //total number of nodes
    private int m; //initial node degree (m <= m0)
    private int m0; //number of initial nodes (m0 << N).
    
    private int multiple_nodes; //Positive integer. The number of new nodes at each time-step. Default value is 1.

    public TemporalBarabasiAlbertModel(int N, int m, int multiple_nodes) {
        this.N = N;
        this.m = m;
        this.m0 = 2*m;
        if(m0 < 3) m0 = 6;
        
        this.multiple_nodes = multiple_nodes;
        
    }
    
    public String Execute() {
        if(N < 0 || m >= N || m < 0){
            return null;
        }
        Random random = new Random();
        StringBuilder networkData = new StringBuilder();
        
        int timestamp = 0;
        
        // array of each edge-node incidence, saving nodes only
        // this makes preferential attachment O(1) per edge
        ArrayList<Integer> incidences = new ArrayList<>(2*N*m);
        
        // connect initial m0 nodes
        for (Integer i = 0; i < m0; i++) {
            Integer j = (i+1)%m0;
            incidences.add(i);
            incidences.add(j);
            
            networkData.append((i+1) + "\t" + (j+1) + "\t" + timestamp + "\r\n");
        }
        
        timestamp++;
        
        int numberNodesThisTimestamp = 0;
        
        // preferential attachment
        for (Integer i = m0; i < N; i++) {
            if(numberNodesThisTimestamp == multiple_nodes)
            {
                timestamp++;
                numberNodesThisTimestamp = 0;
            }
            HashSet<Integer> currentNodeNeighbours = new HashSet<>(m);
            while(currentNodeNeighbours.size() != m){
                int incPos = random.nextInt(incidences.size());
                int j = incidences.get(incPos);
                currentNodeNeighbours.add(j);
            }
            for (Integer j : currentNodeNeighbours) {
                incidences.add(i);
                incidences.add(j);
                networkData.append((i+1) + "\t" + (j+1) + "\t" + timestamp + "\r\n");
            }
            numberNodesThisTimestamp++;
        }
        // return network
        return networkData.toString();
    }

}
