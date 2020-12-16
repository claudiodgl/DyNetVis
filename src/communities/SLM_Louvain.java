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

package communities;

import com.mxgraph.model.mxCell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import layout.InlineNodeAttribute;
import communities.modularityoptimizer.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.FileHandler;


/**
 *
 * @author Jean
 * Para referência veja http://www.ludowaltman.nl/slm/
 */
public class SLM_Louvain {
    
    private static String txtTemporarioEntrada = ".//Softwares_Comunidade//redeSLM_Louvain_Entrada.txt";
    private static String txtTemporarioSaida = ".//Softwares_Comunidade//redeSLM_Louvain_Saida.txt";
    
    /*args é a entrada do método de detecção. Ele é composto por:
        args[0] é o arquivo de entrada para o método de detecção.
        args[1] é o arquivo de saída.
        args[2] = modularity_function (1 = standard; 2 = alternative). This is to indicate whether you want to optimize the standard modularity function 
            or an alternative modularity function. The standard modularity function has been proposed by 
            Newman and Girvan (2004) and Newman (2004). The alternative modularity function has been proposed by Traag, 
            Van Dooren, and Nesterov (2011).
        args[3] = resolution parameter. The resolution parameter determines the granularity level at 
            which communities are detected. Use a value of 1.0 for standard modularity-based community detection. 
            Use a value above (below) 1.0 if you want to obtain a larger (smaller) number of communities.
        args[4] = optimization_algorithm	Algorithm for modularity optimization (1 = original Louvain algorithm; 
            2 = Louvain algorithm with multilevel refinement; 3 = SLM algorithm)
        args[5] =    n_random_starts	Number of random starts
        args[6] =    n_iterations	Number of iterations per random start
        args[7] =    random_seed	Seed of the random number generator
        args[8] =    print_output	Whether or not to print output to the console (0 = no; 1 = yes)
    */
    int n_random_starts = 10;
    int n_iterations = 10;
    int random_seed = 0;
    int print_output = 0;
    int modularity_function = 1;
    int resolution_parameter = 1;
    boolean calculate_with_weight = false;
    boolean weight_external_file = false;
    HashMap<String,Integer> edgeWeight_external_file = new HashMap();
    //O args[4] define o método que será usado e será, portanto, preenchido em cada método.
     String[] args = {txtTemporarioEntrada, txtTemporarioSaida, modularity_function + "", resolution_parameter + "", "", n_random_starts + "", n_iterations + "", random_seed + "", print_output + ""};
    /*
    O jar dos algoritmos SLM e Louvain (clássico ou multilevel) precisa receber um arquivo .txt
    da seguinte maneira:
    
    The input file is a simple tab-delimited text file listing all pairs of nodes in a network that are connected by 
    an edge. An example of an input file for Zachary's karate club network is available here. Notice
    that the numbering of nodes starts at 0. For each pair of nodes, the node with the lower index is 
    listed first, followed by the node with the higher index. The lines in an input file are sorted based 
    on the node indices (first based on the indices in the first column, then based on the indices in the 
    second column). In the case of a weighted network, edge weights are provided in a third column.
    
    Um exemplo pode ser visto em http://www.ludowaltman.nl/slm/karate_club_network.txt
    
    Ex:     0	1
            0	2
            0	3
            0	4
            0	8
            0	10
            0	17
            1	2
            1	3
            1	30
            2	3
            2	7
            2	28
            2	32
            3	7
        
    O método criaArquivoDaRede gera o arquivo no formato apresentado acima.
     */
    public void criaArquivoDaRede(ArrayList listAllEdges, boolean listAllEdgesContainsMxCells) {
        ArrayList arestas = new ArrayList();
        Integer[] aresta = new Integer[2];
        StringBuilder sb = new StringBuilder();
        int i = 0;
        if(listAllEdgesContainsMxCells)
        {
            for (Object edge : listAllEdges) {
                mxCell edgeCell = (mxCell) edge;
                if (edgeCell.getId() != null && !edgeCell.getId().isEmpty()) {
                    String[] infoConexao = edgeCell.getId().split(" ");
                    //infoConexao tem tamanho 3 e suas posições representam o tempo, origem e destino. Nessa ordem.

                    if (infoConexao.length != 3) { //Isso acontece, por ex, com a graphline e as linhas horizontais na temporal.
                        i++;
                        continue;
                    }
                    int no1daAresta = Integer.parseInt(infoConexao[1]);
                    int no2daAresta = Integer.parseInt(infoConexao[2]);
                    aresta = new Integer[2];
                    if(no1daAresta < no2daAresta) //no1 = origem e no2 = destino
                    {
                        aresta[0] = no1daAresta;
                        aresta[1] = no2daAresta;
                    }
                    else //no1 = destino e no2 = origem
                    {
                        aresta[0] = no2daAresta;
                        aresta[1] = no1daAresta;
                    }
                //    sb.append(System.getProperty("line.separator"));
                }
                arestas.add(aresta);
            }
        }
        else //listAllEdges não foi preenchido com mxCells, mas sim com simples strings de arestas
        {
            for (Object edge : listAllEdges) {
                    String[] infoConexao = edge.toString().split(" ");
                    //infoConexao tem tamanho 3 e suas posições representam o tempo, origem e destino. Nessa ordem.

                    if (infoConexao.length != 3) { //Isso acontece, por ex, com a graphline e as linhas horizontais na temporal.
                        i++;
                        continue;
                    }
                    int no1daAresta = Integer.parseInt(infoConexao[1]);
                    int no2daAresta = Integer.parseInt(infoConexao[2]);
                    aresta = new Integer[2];
                    if(no1daAresta < no2daAresta) //no1 = origem e no2 = destino
                    {
                        aresta[0] = no1daAresta;
                        aresta[1] = no2daAresta;
                    }
                    else //no1 = destino e no2 = origem
                    {
                        aresta[0] = no2daAresta;
                        aresta[1] = no1daAresta;
                    }
                //    sb.append(System.getProperty("line.separator"));
                
                arestas.add(aresta);
            }
        }
    //    System.out.println("Arestas ignoradas: " + i);
        Collections.sort (arestas, new Comparator() { //zero se igual, -1 se 1 < 2; +1 se 2 < 1
            public int compare(Object aresta1, Object aresta2) {
                
                Integer[] o1 = (Integer[]) aresta1;
                Integer[] o2 = (Integer[]) aresta2;

                if(o1[0] < o2[0])
                    return -1;
                if(o1[0] > o2[0])
                    return 1;
                if(Objects.equals(o1[0], o2[0]) && Objects.equals(o1[1], o2[1]))
                    return 0;
                if(Objects.equals(o1[0], o2[0]) && o1[1] < o2[1])
                    return -1;
                return 1;
                
            }
        });
        
        HashMap<String,Integer> edgeWeight = new HashMap();
        if(calculate_with_weight)
        {
            if(weight_external_file)
            {
                edgeWeight = edgeWeight_external_file;
            }
            else
            {
                //Calculate edges weight
                for(Object edge : arestas)
                {
                    Integer[] infoEdge = (Integer[]) edge;
                    String edge_string = infoEdge[0]+" "+infoEdge[1];
                    String edge_string_inv = infoEdge[1]+" "+infoEdge[0];
                    if(edgeWeight.get(edge_string) == null)
                    {
                        if(edgeWeight.get(edge_string_inv) == null)
                        {
                            edgeWeight.put(edge_string, 1);
                        }
                        else
                        {
                            edgeWeight.put(edge_string_inv, edgeWeight.get(edge_string_inv)+1);
                        }
                    }
                    else
                    {
                        edgeWeight.put(edge_string, edgeWeight.get(edge_string)+1);
                    }
                }
            }
        }
        
        for(Object edge : arestas)
        {
            Integer[] ar = (Integer[]) edge;
            sb.append(ar[0]);
            sb.append("\t");
            sb.append(ar[1]);
            if(calculate_with_weight)
            {
                sb.append("\t");
                if(edgeWeight.get(ar[0]+" "+ar[1]) != null)
                    sb.append(edgeWeight.get(ar[0]+" "+ar[1]));
                else if(edgeWeight.get(ar[1]+" "+ar[0]) != null)
                    sb.append(edgeWeight.get(ar[1]+" "+ar[0]));
                else
                    System.out.println("error edge weights");
            }
            sb.append(System.getProperty("line.separator"));
        }
        util.FileHandler.gravaArquivo(sb.toString(), txtTemporarioEntrada, false);
    }
    
    
    
    public HashMap<Integer, List<InlineNodeAttribute>> execute(List<InlineNodeAttribute> listAttNodes, ArrayList listAllEdges, String metodoDeteccao, boolean calculate_with_weight, boolean weight_external_file, HashMap<String,Integer> edgeWeight_external_file) {
        
        this.calculate_with_weight = calculate_with_weight;
        this.weight_external_file = weight_external_file;
        this.edgeWeight_external_file = edgeWeight_external_file;
        
        File louvainEntrada = null, louvainSaida = null;
        try {
            criaArquivoDaRede(listAllEdges, metodoDeteccao.contains("sampling")? false : true);
            louvainEntrada = new File(txtTemporarioEntrada);
            louvainSaida = new File(txtTemporarioSaida);
            
            if(!louvainEntrada.exists())
            {
                System.out.println("O arquivo de entrada para o Louvain_SLM não foi criado corretamente.");
                return null;
            }
            
            if(metodoDeteccao.equals("Original Louvain sampling"))
                metodoDeteccao = "Original Louvain";
            
            switch (metodoDeteccao) {
                case "Original Louvain":
                    args[4] = "1";
                    break;
                case "Multilevel Louvain":
                    args[4] = "2";
                    break;
                case "SLM":
                    args[4] = "3";
                    break;
                default:
                    break;
            }
            
            ModularityOptimizer.main(args);
            return(interpretaResultado(listAttNodes));
        } catch (IOException ex) {
            Logger.getLogger(SLM_Louvain.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            if (louvainEntrada != null) {
                louvainEntrada.delete();
            }
            if (louvainSaida != null) {
                louvainSaida.delete();
            }
        }
        return null;
    }
    
    private HashMap<Integer, List<InlineNodeAttribute>> interpretaResultado(List<InlineNodeAttribute> listAttNodes)
    {
        HashMap<Integer, List<InlineNodeAttribute>> verticesPorComunidade = new HashMap<Integer, List<InlineNodeAttribute>>();
        HashMap<Integer, List<InlineNodeAttribute>> verticesPorComunidadeSemComunidadeVazia = new HashMap<Integer, List<InlineNodeAttribute>>();
        
        try
        {
            String resultadoMetodo = FileHandler.leArquivo(txtTemporarioSaida);

            String[] comunidadeDeCadaElemento = resultadoMetodo.split("\n"); //a posição 0 tem a comunidade do nó zero e assim por diante.

            for(int i=0; i < comunidadeDeCadaElemento.length; i++)
            {
                int idComunidade = Integer.parseInt(comunidadeDeCadaElemento[i].replace("\r", ""));
                if(!verticesPorComunidade.containsKey(idComunidade))
                    verticesPorComunidade.put(idComunidade, new ArrayList());
                for(int j = 0; j < listAttNodes.size(); j++)
                {
                    if(listAttNodes.get(j).getId_original() == i)
                    {
                        verticesPorComunidade.get(idComunidade).add(listAttNodes.get(j));
                        break;
                    }
                }
            }
            
            for(int i = 0; i < verticesPorComunidade.keySet().size(); i++)
                if(verticesPorComunidade.get(i).size() != 0)
                    verticesPorComunidadeSemComunidadeVazia.put(i, verticesPorComunidade.get(i));
            
        ///    System.out.println("Comunidades:");
        //    for(int j = 0; j < verticesPorComunidadeSemComunidadeVazia.size(); j++)
        //        System.out.println(verticesPorComunidadeSemComunidadeVazia.get(j));
            
            return verticesPorComunidadeSemComunidadeVazia;
        }
        
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
}
