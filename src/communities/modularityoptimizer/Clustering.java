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

package communities.modularityoptimizer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class Clustering implements Cloneable, Serializable
{
    private static final long serialVersionUID = 1;

    protected int nNodes;
    protected int nClusters;
    protected int[] cluster;

    public static Clustering load(String fileName) throws ClassNotFoundException, IOException
    {
        Clustering clustering;
        ObjectInputStream objectInputStream;

        objectInputStream = new ObjectInputStream(new FileInputStream(fileName));

        clustering = (Clustering)objectInputStream.readObject();

        objectInputStream.close();

        return clustering;
    }

    public Clustering(int nNodes)
    {
        this.nNodes = nNodes;
        cluster = new int[nNodes];
        nClusters = 1;
    }

    public Clustering(int[] cluster)
    {
        nNodes = cluster.length;
        this.cluster = (int[])cluster.clone();
        nClusters = Arrays2.calcMaximum(cluster) + 1;
    }

    public Object clone()
    {
        Clustering clonedClustering;

        try
        {
            clonedClustering = (Clustering)super.clone();
            clonedClustering.cluster = getClusters();
            return clonedClustering;
        }
        catch (CloneNotSupportedException e)
        {
            return null;
        }
    }

    public void save(String fileName) throws IOException
    {
        ObjectOutputStream objectOutputStream;

        objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));

        objectOutputStream.writeObject(this);

        objectOutputStream.close();
    }

    public int getNNodes()
    {
        return nNodes;
    }

    public int getNClusters()
    {
        return nClusters;
    }

    public int[] getClusters()
    {
        return (int[])cluster.clone();
    }

    public int getCluster(int node)
    {
        return cluster[node];
    }

    public int[] getNNodesPerCluster()
    {
        int i;
        int[] nNodesPerCluster;

        nNodesPerCluster = new int[nClusters];
        for (i = 0; i < nNodes; i++)
            nNodesPerCluster[cluster[i]]++;
        return nNodesPerCluster;
    }

    public int[][] getNodesPerCluster()
    {
        int i;
        int[] nNodesPerCluster;
        int[][] nodePerCluster;

        nodePerCluster = new int[nClusters][];
        nNodesPerCluster = getNNodesPerCluster();
        for (i = 0; i < nClusters; i++)
        {
            nodePerCluster[i] = new int[nNodesPerCluster[i]];
            nNodesPerCluster[i] = 0;
        }
        for (i = 0; i < nNodes; i++)
        {
            nodePerCluster[cluster[i]][nNodesPerCluster[cluster[i]]] = i;
            nNodesPerCluster[cluster[i]]++;
        }
        return nodePerCluster;
    }

    public void setCluster(int node, int cluster)
    {
        this.cluster[node] = cluster;
        nClusters = Math.max(nClusters, cluster + 1);
    }

    public void initSingletonClusters()
    {
        int i;

        for (i = 0; i < nNodes; i++)
            cluster[i] = i;
        nClusters = nNodes;
    }

    public void orderClustersByNNodes()
    {
        class ClusterNNodes implements Comparable<ClusterNNodes>
        {
            public int cluster;
            public int nNodes;

            public ClusterNNodes(int cluster, int nNodes)
            {
                this.cluster = cluster;
                this.nNodes = nNodes;
            }

            public int compareTo(ClusterNNodes clusterNNodes)
            {
                return (clusterNNodes.nNodes > nNodes) ? 1 : ((clusterNNodes.nNodes < nNodes) ? -1 : 0);
            }
        }

        ClusterNNodes[] clusterNNodes;
        int i;
        int[] newCluster, nNodesPerCluster;

        nNodesPerCluster = getNNodesPerCluster();
        clusterNNodes = new ClusterNNodes[nClusters];
        for (i = 0; i < nClusters; i++)
            clusterNNodes[i] = new ClusterNNodes(i, nNodesPerCluster[i]);

        Arrays.sort(clusterNNodes);

        newCluster = new int[nClusters];
        i = 0;
        do
        {
            newCluster[clusterNNodes[i].cluster] = i;
            i++;
        }
        while ((i < nClusters) && (clusterNNodes[i].nNodes > 0));
        nClusters = i;
        for (i = 0; i < nNodes; i++)
            cluster[i] = newCluster[cluster[i]];
    }

    public void mergeClusters(Clustering clustering)
    {
        int i;

        for (i = 0; i < nNodes; i++)
            cluster[i] = clustering.cluster[cluster[i]];
        nClusters = clustering.nClusters;
    }
}
