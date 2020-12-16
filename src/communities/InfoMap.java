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
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import layout.InlineNodeAttribute;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;

/**
 *
 * @author Jean
 */
public class InfoMap {

    Executor currentExecutor = null;
    private static String diretorioExeInfomap = ".//Softwares_Comunidade//InfoMap//";
    private static String nomeBDInfomapSemExtensao = "redeInfoMap";

    //parâmetros do infomap:
    //Seed: A seed (integer) to the random number generator.
    //No infomap, o seed é um parâmetro opcional. No código de http://webdocs.cs.ualberta.ca/~rabbanyk/criteriaComparison/SNAMJournal/src/
    // caso o seed seja null, é atribuído a ele o valor 1. Esse é o valor usado aqui.
    private static int seed = 1;
    //Number of attempts: The number of outer-most loops to run before picking the best solution. (Default: 1)
    //Se o valor é 10, por ex, a saída será o melhor resultado em 10 execuções.
    // No código de http://webdocs.cs.ualberta.ca/~rabbanyk/criteriaComparison/SNAMJournal/src/
    // caso o numero seja null, é atribuído a ele o valor 2. Porém no exemplo de uso do infomap em http://www.mapequation.org/code.html#Link-list-format,
    // o valor é 10.
    private static int numberAttempts = 10;
    boolean calculate_with_weight = false;
    boolean weight_external_file = false;
    HashMap<String,Integer> edgeWeight_external_file = new HashMap();
    
    public InfoMap() {
    }

    /*
    O executável do infomap precisa receber um arquivo .net no formato Pajek (http://www.mapequation.org/code.html#Pajek-format)
    Ex:    *Vertices 4
            1 "1"
            2 "2"
            3 "3"
            4 "4"
            *Edges 4
            1 2
            1 3
            1 4
            2 3
        Assim, a partir das arestas do grafo do DyNetVis, esse método gera um novo arquivo com as origens
    e destinos (sem explicitar o peso, situação na qual o InfoMap adota peso 1 para todas as arestas).
     */
    public void criaArquivoDaRede(ArrayList listAllEdges, List<InlineNodeAttribute> listAttNodes) {

        boolean nodeIdstartsInZero = false;
        StringBuilder sb = new StringBuilder();
         ArrayList arestas = new ArrayList();
        Integer[] aresta = new Integer[2];
        
         int maiorIdDeVerticeDaRede = -1;
        int menorIdDeVerticeDaRede = Integer.MAX_VALUE;
        for(int i = 0; i < listAttNodes.size(); i++)
        {
            if(listAttNodes.get(i).getId_original() > maiorIdDeVerticeDaRede)
                maiorIdDeVerticeDaRede = listAttNodes.get(i).getId_original();
            if(listAttNodes.get(i).getId_original() < menorIdDeVerticeDaRede)
                menorIdDeVerticeDaRede = listAttNodes.get(i).getId_original();
        }
        
        if(menorIdDeVerticeDaRede == 0)
            nodeIdstartsInZero = true;
            
        
        //sb.append("*Vertices " + listAttNodes.size());
        sb.append("*Vertices " + (maiorIdDeVerticeDaRede + (nodeIdstartsInZero ? 1 : 0))); //If there is nodeId == 0, add 1 in the total number of nodes

        sb.append(System.getProperty("line.separator"));
        
//         Collections.sort (listAttNodes, new Comparator() { //zero se igual, -1 se 1 < 2; +1 se 2 < 1
//            public int compare(Object o1, Object o2) {
//                
//                InlineNodeAttribute v1 = (InlineNodeAttribute) o1;
//                InlineNodeAttribute v2 = (InlineNodeAttribute) o2;
//
//                if(v1.getId_original() < v2.getId_original())
//                    return -1;
//                if(v1.getId_original() > v2.getId_original())
//                    return 1;
//                if(Objects.equals(v1.getId_original(), v2.getId_original()))
//                    return 0;
//                return 0;
//                }
//            });
//            
//        for (InlineNodeAttribute n : listAttNodes) {
//            sb.append(n.getId_original() + " \"" + n.getId_original() + "\"");
//            sb.append(System.getProperty("line.separator"));
//        }

        int k = 0;
        for(int i = 1; i <= maiorIdDeVerticeDaRede; i++)
        {
           // if(nodeIdstartsInZero)
            //    k = i+1;
           // else
            //    k = i;
            //sb.append(k + " \"" + k + "\""); //If nodeId starts in 0, add 1 in the network file (Infomap does not accept nodeId 0)
            sb.append(i + " \"" + i + "\"");
            sb.append(System.getProperty("line.separator"));
        }
        if(nodeIdstartsInZero)
        {
            sb.append((maiorIdDeVerticeDaRede + 1) + " \"" + (maiorIdDeVerticeDaRede + 1) + "\""); //If nodeId starts in 0, add 1 in the network file (Infomap does not accept nodeId 0)
            sb.append(System.getProperty("line.separator"));
        }
         int i = 0;
        sb.append("*Edges " + listAllEdges.size());
        sb.append(System.getProperty("line.separator"));
        for (Object edge : listAllEdges) {
            mxCell edgeCell = (mxCell) edge;
            if (edgeCell.getId() != null && !edgeCell.getId().isEmpty()) {
                String[] infoConexao = edgeCell.getId().split(" ");
                //infoConexao tem tamanho 3 e suas posições representam o tempo, origem e destino. Nessa ordem.

                if (infoConexao.length != 3) {
                    i++;
                    continue;
                }
                int no1daAresta = Integer.parseInt(infoConexao[1]);
                int no2daAresta = Integer.parseInt(infoConexao[2]);
                aresta = new Integer[2];
                if(no1daAresta < no2daAresta) //no1 = origem e no2 = destino
                {
                    //If there is nodeId == 0, add 1 (infomap does not accept nodeId == 0)
                    aresta[0] = nodeIdstartsInZero ? no1daAresta + 1 : no1daAresta;
                    aresta[1] = nodeIdstartsInZero ? no2daAresta + 1 : no2daAresta;
                }
                else //no1 = destino e no2 = origem
                {
                    //If there is nodeId == 0, add 1 (infomap does not accept nodeId == 0)
                    aresta[0] = nodeIdstartsInZero ? no2daAresta + 1 : no2daAresta;
                    aresta[1] = nodeIdstartsInZero ? no1daAresta + 1 : no1daAresta;
                }
            //    sb.append(System.getProperty("line.separator"));
            }
            arestas.add(aresta);
        }
        System.out.println("Arestas ignoradas: " + i);
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
   /*     for (Object edge : listAllEdges) {
            mxCell edgeCell = (mxCell) edge;
            if (edgeCell.getId() != null && !edgeCell.getId().isEmpty()) {
                String[] infoConexao = edgeCell.getId().split(" ");
                //infoConexao tem tamanho 3 e suas posições representam o tempo, origem e destino. Nessa ordem.

                if (infoConexao.length != 3) {
                    return;
                }
                sb.append(infoConexao[1]);
                sb.append(" ");
                sb.append(infoConexao[2]);
                sb.append(System.getProperty("line.separator"));
            }
        }
*/
        util.FileHandler.gravaArquivo(sb.toString(), diretorioExeInfomap + nomeBDInfomapSemExtensao + ".net", false);
    }

    public HashMap<Integer, List<InlineNodeAttribute>> execute(ArrayList listAllEdges, List<InlineNodeAttribute> listAttNodes, boolean calculate_with_weight, boolean weight_external_file, HashMap<String,Integer> edgeWeight_external_file) {
        
        this.calculate_with_weight = calculate_with_weight;
        this.weight_external_file = weight_external_file;
        this.edgeWeight_external_file = edgeWeight_external_file;
        
        File treeOut = null, cluOut = null, mapOut = null, mapNetOut = null, mapVecOut = null; //Arquivos gerados pelo Infomap
        File arqRede = null; //Objeto file para o arquivo de entrada com os nós e arestas (redeInfoMap.net)
        try {
            //Cria o arquivo da rede que será usado na chamada ao infomap abaixo.
            criaArquivoDaRede(listAllEdges, listAttNodes);

            //Executa o .exe do infomap (64 bits) para grafo não direcionado. A escolha automatica entre 32 e 64 bits pode ser feita com o código de https://webdocs.cs.ualberta.ca/~rabbanyk/criteriaComparison/SNAMJournal/src/mining/MiningDispatcher.java (Enum Platform)
            File execFile = new File(diretorioExeInfomap + "infomap_undirected64.exe");
            arqRede = new File(diretorioExeInfomap + nomeBDInfomapSemExtensao + ".net");

            if (!execFile.exists()) {
                System.out.println("Cannot find mining executable. Did you move or delete the 'mining' directory? ");
            } else if (!execFile.canExecute()) {
                // If possible make sure it is executable...
                if (!execFile.setExecutable(true)) {
                    System.out.println("Execute permissions denied on mining executable. Please change that file's permissions to allow execution. ");
                }
            }
            //      if(!arqRede.exists())
            //          System.out.println("bla");
            
        int menorIdDeVerticeDaRede = Integer.MAX_VALUE;
        for(int i = 0; i < listAttNodes.size(); i++)
        {
            if(listAttNodes.get(i).getId_original() < menorIdDeVerticeDaRede)
                menorIdDeVerticeDaRede = listAttNodes.get(i).getId_original();
        }
        
        boolean nodeIdStartsInZero = menorIdDeVerticeDaRede == 0 ? true : false;

            String execLocation = execFile.getCanonicalPath();
            CommandLine comm = new CommandLine(execLocation);
            comm.addArgument(String.valueOf(seed));
            comm.addArgument(arqRede.getCanonicalPath());
            comm.addArgument(String.valueOf(numberAttempts));
            this.currentExecutor = new DefaultExecutor();
            this.currentExecutor.setWatchdog(new ExecuteWatchdog(-1));
            this.currentExecutor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
            //executor.setExitValue(0);
            this.currentExecutor.setStreamHandler(new PumpStreamHandler(new ByteArrayOutputStream())); // Hate stdout to Eclipse console
            this.currentExecutor.execute(comm);

            // Open output file for processing
            try {

                treeOut = new File(diretorioExeInfomap + nomeBDInfomapSemExtensao + ".tree");
                mapOut = new File(diretorioExeInfomap + nomeBDInfomapSemExtensao + ".map");
                mapNetOut = new File(diretorioExeInfomap + nomeBDInfomapSemExtensao + "_map.net");
                mapVecOut = new File(diretorioExeInfomap + nomeBDInfomapSemExtensao + "_map.vec");
                cluOut = new File(diretorioExeInfomap + nomeBDInfomapSemExtensao + ".clu");

                BufferedReader resultsBuffer;
                //Lê o arquivo .tree com a saída
                resultsBuffer = new BufferedReader(new FileReader(diretorioExeInfomap + nomeBDInfomapSemExtensao + ".tree"));
                String finalNodesInClusters = parseRosvallInfomapTreeFile(resultsBuffer);

                resultsBuffer.close();

                return montaComunidades(finalNodesInClusters, listAttNodes, nodeIdStartsInZero);

            } catch (FileNotFoundException fe) {
                // If the algorithm doesn't find results, it doesn't make a groups file. Nor does it have a STEP value. I could possibly check that too, but this is more certain.
                // The php server version just dies and Meerkat recovers, so we can probably just pass on through here.
                // NOOP!
                System.out.println("Nao existe o arquivo .tree. Isso quer dizer que a execucao do metodo InfoMap deu errado.");
            }
        } catch (ExecuteException ex) {
            System.out.println(ex.getCause());
        } catch (Exception e) {
            System.out.println("Erro.");
        } finally {
            // Clean up the files we made
            if (null != arqRede) {
                arqRede.delete();
            }
            if (null != cluOut) {
                cluOut.delete();
            }
            if (null != treeOut) {
                treeOut.delete();
            }
            if (null != mapOut) {
                mapOut.delete();
            }
            if (null != mapNetOut) {
                mapNetOut.delete();
            }
            if (null != mapVecOut) {
                mapVecOut.delete();
            }
        }
        return null;
    }

    private static String parseRosvallInfomapTreeFile(BufferedReader resultsBuffer)
            throws IOException {
        String finalNodesInClusters = "";
        String strLine;
        //Read File Line By Line
        int previousCommunity = 0;
        boolean emitComma = false;
        while ((strLine = resultsBuffer.readLine()) != null) {
            if (0 == strLine.indexOf("#")) {
                continue;
            }
            String[] tokens = strLine.split(":| ");
            int currentCommunity = Integer.parseInt(tokens[0]);
            // String nodeRankInCommunity = tokens[1]; // We don't care about this
            //String steadyStateRandomWalkers = tokens[2]; // We don't care about this.
            String nodeId = tokens[3].substring(1, tokens[3].length() - 1); // Trim quotation marks off

            if (previousCommunity != 0 && previousCommunity != currentCommunity) {
                finalNodesInClusters += "@";
                emitComma = false;
            }
            previousCommunity = currentCommunity;
            if (emitComma) {
                finalNodesInClusters += ",";
            } else {
                emitComma = true;
            }
            finalNodesInClusters += nodeId;
        }
        return finalNodesInClusters;
    }

    private HashMap<Integer, List<InlineNodeAttribute>> montaComunidades(String resultadoInfoMap, List<InlineNodeAttribute> listAttNodes, boolean nodeIdStartsInZero) {
        if (resultadoInfoMap == null || resultadoInfoMap.isEmpty()) {
            return null;
        }
        String[] comunidades = resultadoInfoMap.split("@");
        
        HashMap<Integer, List<InlineNodeAttribute>> verticesPorComunidade = new HashMap<Integer, List<InlineNodeAttribute>>();
        for (int i = 0; i < comunidades.length; i++) {
            verticesPorComunidade.put(i, new ArrayList());
        }

        for (int i = 0; i < comunidades.length; i++) {
            
            String[] vertices = comunidades[i].split(",");
            //Pra cada vértice dessa comunidade
            for (String vertice : vertices) {
                //Encontra o objeto que possui esse id no grafo e o insere na tabela em sua respectiva comunidade.
                int verticeInt = Integer.parseInt(vertice);
                if(nodeIdStartsInZero)
                    verticeInt--; //Undo the nodId mapping (to compute communities, each nodeId was incremented (infomap does not accept nodeId == 0))
                for (InlineNodeAttribute no : listAttNodes) {
                    if (no.getId_original() == verticeInt) {
                        verticesPorComunidade.get(i).add(no);
                        break;
                    }
                }
            }
        }
        HashMap<Integer, List<InlineNodeAttribute>> verticesPorComunidadeSemComunidadeVazia = new HashMap<Integer, List<InlineNodeAttribute>>();
        
        int iFinal = 0;
        //System.out.println("Comunidades: ");
        for(int i = 0; i < verticesPorComunidade.keySet().size(); i++)
                if(verticesPorComunidade.get(i).size() != 0)
                {
                    verticesPorComunidadeSemComunidadeVazia.put(iFinal, verticesPorComunidade.get(i));
                    //System.out.println(verticesPorComunidadeSemComunidadeVazia.get(iFinal));
                    iFinal++;
                }
        //verticesPorComunidadeSemComunidadeVazia.put(verticesPorComunidadeSemComunidadeVazia.size(), new ArrayList());
        //verticesPorComunidadeSemComunidadeVazia.put(verticesPorComunidadeSemComunidadeVazia.size(), new ArrayList());
        
        return verticesPorComunidadeSemComunidadeVazia;
    }

}
