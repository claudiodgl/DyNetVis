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
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import layout.InlineNodeAttribute;


/**
 *
 * @author Account
 */
public class CPM {

//    private boolean cancel = false;

    int k = 3;
    private Set<Set<InlineNodeAttribute>> Cliques = new HashSet<Set<InlineNodeAttribute>>();
    GenQueue<TreeSet<InlineNodeAttribute>> Bk = new GenQueue<TreeSet<InlineNodeAttribute>>();

    public class SortByID implements Comparator<InlineNodeAttribute> {

        public int compare(InlineNodeAttribute n1, InlineNodeAttribute n2) {
            if (n1.getId_original() > n2.getId_original()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Queue Implementation">
    public Object getLastElement(final Collection c) {
        /*
         final Iterator itr = c.iterator();
         Object lastElement = itr.next();
         while (itr.hasNext()) {
         lastElement = itr.next();
         }
         return lastElement;
         */
        return null;
    }

    class GenQueue<E> {

        private LinkedList<E> list = new LinkedList<E>();

        public void enqueue(E item) {
            list.addLast(item);
        }

        public E dequeue() {
            return list.pollFirst();
        }

        public boolean hasItems() {
            return !list.isEmpty();
        }

        public int size() {
            return list.size();
        }

        public void addItems(GenQueue<? extends E> q) {
            while (q.hasItems()) {
                list.addLast(q.dequeue());
            }
        }
    }
    //</editor-fold>

    private boolean existeAresta(InlineNodeAttribute v1, InlineNodeAttribute v2, ArrayList listAllEdges)
    {
        String idV1;
        String idV2;
        for(Object edge : listAllEdges)
        {
            mxCell edgeCell = (mxCell) edge;
            if(edgeCell.getId() != null && !edgeCell.getId().isEmpty())
            {
                String[] infoConexao = edgeCell.getId().split(" ");
                //infoConexao tem tamanho 3 e suas posições representam o tempo, origem e destino. Nessa ordem.
                
                idV1 = String.valueOf(v1.getId_original());
                idV2 = String.valueOf(v2.getId_original());
                if(infoConexao[1].equals(idV1) &&  infoConexao[2].equals(idV2) ||
                        infoConexao[1].equals(idV2) &&  infoConexao[2].equals(idV1))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    private Vector<InlineNodeAttribute> getLargerIndexInlineNodeAttributes(List<InlineNodeAttribute> listAttNodes, ArrayList listAllEdges, mxGraph graph, InlineNodeAttribute vi) {
        Vector<InlineNodeAttribute> output = new Vector<InlineNodeAttribute>();
        for (InlineNodeAttribute n : listAttNodes) {
            if (n.getId_original() > vi.getId_original() && existeAresta(n, vi, listAllEdges)) {
//                //TODO check degree of n and vi
                output.addElement(n);
            }
        }
        return output;
    
    }

    private boolean checkBk1IsClique(ArrayList listAllEdges, TreeSet<InlineNodeAttribute> Bk1) {
        for (InlineNodeAttribute firstInlineNodeAttribute : Bk1) {
            for (InlineNodeAttribute secondInlineNodeAttribute : Bk1) {
                if (firstInlineNodeAttribute == secondInlineNodeAttribute) {
                    continue;
                }

                if (!existeAresta(firstInlineNodeAttribute, secondInlineNodeAttribute, listAllEdges)) { //One edge is missing in the Bk+1 clique
                    return false;
                }
            }
        }

        return true;
    }

    public  HashMap<Integer, List<InlineNodeAttribute>> execute(mxGraph graph, ArrayList listAllEdges, List<InlineNodeAttribute> listAttNodes) {

        //Firstly add each node as an item in Bk
        TreeSet<InlineNodeAttribute> tmp;
        
        for (InlineNodeAttribute n : listAttNodes) {
            //Trick: if the node's degree is less than k-1, it can not involve in k-clique
            if (n.getDegree() >= k - 1) {
                tmp = new TreeSet<InlineNodeAttribute>(new SortByID());
                tmp.add(n);
                Bk.enqueue(tmp); //Add the B1 (node itself) to the queue
            }
        }

        //Now start the iterative process for finding cliques
        tmp = Bk.dequeue();

        while (tmp != null) {

//            if (cancel) {
//                //Empty variables
//                Bk.list.clear();
//                tmp.clear();
//                Cliques.clear();
//                return null;
//            }

            //Search for Bk+1
            InlineNodeAttribute vi = tmp.last();
            Vector<InlineNodeAttribute> largerIndexes = getLargerIndexInlineNodeAttributes(listAttNodes, listAllEdges, graph, vi);

            for (InlineNodeAttribute vj : largerIndexes) {
                TreeSet<InlineNodeAttribute> Bk1 = new TreeSet<InlineNodeAttribute>(new SortByID());
                Bk1.addAll(tmp); //Clone current Bk into Bk+1
                Bk1.add(vj);
                if (Bk1.size() <= k && checkBk1IsClique(listAllEdges, Bk1)) {

                    if (Bk1.size() == k) { //A clique of size k found. Finish expanding this Bk+1 here.
                        Cliques.add(Bk1);
                    } else if (Bk1.size() < k) {
                        Bk.enqueue(Bk1); //k should be checked for finding cliques of size k.
                    } else { //Clique with larger size will be omitted.
                        System.out.println("<br>Larger Clique Found. It should not be here<br>");
                    }
                }
            }
           
            tmp = Bk.dequeue(); //Check next item
        }

        //Algorithm finished.
        
        //O trecho abaixo cria uma lista onde cada posição é uma string representando os ids dos vértices que estão
        //nos cliques. Ex: Clique 1 = nós "1,3,4,5". Essa lista será usada para ver se dois cliques compartilham nós.
            
        int cliqueId = 0;
        List<String> verticesPorClique = new ArrayList();
        for (Set<InlineNodeAttribute> clique : Cliques) 
        {
            cliqueId++;
            String verticesNoClique = "";
            for(InlineNodeAttribute vertice : clique)
                verticesNoClique += vertice.getId_original() + ",";

            verticesPorClique.add(verticesNoClique.substring(0, verticesNoClique.length() - 1)); //remove last ,);
            System.out.println("Clique " + cliqueId + ": " + verticesNoClique);
        }
      
      //Obtem uma lista de strings correspondentes às comunidades. 
      //Cada string do retorno representa uma comunidade e possui os ids dos vértices que estão nela.
      List<String> comunidades = obtemComunidades(verticesPorClique);
      
      
      //Monta tabela com os vértices (objetos do tipo InlineNodeAttribute) e suas respectivas comunidades
      System.out.println("Comunidades: ");
      HashMap<Integer, List<InlineNodeAttribute>> verticesPorComunidade = new HashMap<Integer, List<InlineNodeAttribute>>();
      for(int i=0; i < comunidades.size(); i++)
          verticesPorComunidade.put(i, new ArrayList());
      
      for(int j = 0; j < comunidades.size(); j++)
      {
          System.out.println(comunidades.get(j));
          String[] vertices = comunidades.get(j).split(",");
          //Pra cada vértice dessa comunidade
          for(String vertice : vertices)
          {
              //Encontra o objeto que possui esse id no grafo e o insere na tabela em sua respectiva comunidade.
              for(InlineNodeAttribute no : listAttNodes)
              {
                  if(no.getId_original() == Integer.parseInt(vertice))
                  {
                    verticesPorComunidade.get(j).add(no);
                    break;
                  }
              }
          }
      }
      return verticesPorComunidade;
    }
    
     private int getSharedInlineNodeAttributes(String vi, String vj) {
        String[] firstCliqueElements = vi.split(",");
        String[] secondCliqueElements = vj.split(",");

        int sharedInlineNodeAttributes = 0;

        for (String n1 : firstCliqueElements) {
            for (String n2 : secondCliqueElements) {
                if (n1.equals(n2)) {
                    sharedInlineNodeAttributes++;
                }
            }
        }

        return sharedInlineNodeAttributes;
    }
     
     //Devolve a união de dois cliques. Como eles compartilham k-1 vértices, eles formam uma comunidade.
     //Ex: Entrada: "1,2,3,4" e "1,3,4,5". Retorno: "1,2,3,4,5" (sem repetição)
     private String mesclaCliques(String c1, String c2)
     {
         String novoClique = "";
         String[] firstCliqueElements = c1.split(",");
         String[] secondCliqueElements = c2.split(",");
         Vector<Integer> uniao = new Vector();
        Integer element;
        //adicionar o primeiro vector eliminando repeticoes 
        for (int i = 0; i < firstCliqueElements.length; i++) {
             element = Integer.parseInt(firstCliqueElements[i]);
            if (!uniao.contains(element)) {
                uniao.add(element);
            }
        }

        //adicionar segundo vector eliminando repeticoes
        for (int i = 0; i < secondCliqueElements.length; i++) {
            element = Integer.parseInt(secondCliqueElements[i]);
            if (!uniao.contains(element)) {
                uniao.add(element);
            }
        }
        Collections.sort(uniao);
        //Gera string a partir do vetor uniao
        for (Integer elemento : uniao) {
            novoClique += elemento + ",";
            }
        
        return novoClique.substring(0, novoClique.length() - 1);
        
     }
     
     //Obtém as comunidades existentes nos cliques. 
     //Processo recursivo até que não seja feito nenhum novo merge, ou seja, até que todas as comunidades 
     // tenham sido encontradas.
     private List<String> obtemComunidades(List<String> cliques)
     {
         List<String> comunidades = new ArrayList();
          List<String> cliquesAnalisados = new ArrayList();
         boolean houveMerge = false;
           //Compara os cliques dois a dois para ver se compartilham k-1 vértices. Se sim, eles pertencem à mesma comunidade.
        for (String vi : cliques) {
            for (String vj : cliques) {
                //Se dois cliques diferentes compartilham k-1 nós, então eles formam uma comunidade.
                if ((!vi.equals(vj)) && (getSharedInlineNodeAttributes(vi, vj) == k - 1)) {
                    String comunidadeFormada = mesclaCliques(vi,vj);
                    if(!comunidades.contains(comunidadeFormada))
                        comunidades.add(comunidadeFormada);
                    houveMerge = true;
                    cliquesAnalisados.add(vi);
                    cliquesAnalisados.add(vj);
                }
            }
        }
        if(houveMerge)
        {
            //Remove os cliques já analisados e insere para análise as comunidades até aqui.
            //Isso é importante pois pode ser que a comunidade continue crescendo.
            cliques.removeAll(cliquesAnalisados);
            cliques.addAll(comunidades);
            obtemComunidades(cliques);
        }
        //Se percorreu a lista de cliques e não fez merge, então todas as comunidades foram encontradas.
        return cliques;  
     }
}
