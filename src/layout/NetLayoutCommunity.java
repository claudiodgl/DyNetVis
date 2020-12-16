/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layout;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import communities.InfoMap;
import communities.SLM_Louvain;
import communities.Statistic;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Claudio Linhares
 */
public class NetLayoutCommunity extends Observable{
    public mxGraphComponent graphComponent;
    public ArrayList<ArrayList> matrizDataInline;
    public HashMap<Long,String> matrizRolutos;
    mxGraph graph;
    String styleNode;
    
    int tamanho_maximo_no = 50;
    int tamanho_minimo_no = 10;
    int espacamento = tamanho_maximo_no + 10;
    double espaco_inicial = 20;
    HashMap<Integer, List<InlineNodeAttribute>> comunidadesLouvain;
    HashMap<Integer, List<InlineNodeAttribute>> comunidadesInfomap;
    String[] colorsTable;
    HashMap<String,Integer> listAllRotulos;
    
    public NetLayoutCommunity()
    {
    
    }
    
    String[] rotulosOrdenados;
    public void NetLayoutCommunity(ArrayList<ArrayList> matrizDataInline, HashMap<Long,String> matrizRolutos)
    {
        this.matrizDataInline = matrizDataInline;
        this.matrizRolutos = matrizRolutos;
        ArrayList listAllEdges = new ArrayList();
        List<InlineNodeAttribute> listAttNodes = new ArrayList();
        listAllRotulos = new HashMap();
        
        graph = new mxGraph() {
             @Override
             protected mxGraphView createGraphView() {
                 return new CurveGraphView(this);
             }
         };
         
        //final Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();  
        
        try
        {
            defineStyles();
            int chave = 0;
            //Entry<Integer, List<InlineNodeAttribute>> comunidade : comunidadesLouvain.entrySet()
            
            for(Entry<Long,String> rotulo : matrizRolutos.entrySet())
            {
                if(!listAllRotulos.containsKey(rotulo.getValue()))
                {
                    listAllRotulos.put(rotulo.getValue(),chave);
                    chave++;
                }
            }
            rotulosOrdenados = new String[listAllRotulos.size()];
            
            int indiceRotulo =0;
            for(Entry<String,Integer> rotulo : listAllRotulos.entrySet())
            {
                rotulosOrdenados[indiceRotulo] = rotulo.getKey();
                indiceRotulo++;
            }
            
            Arrays.sort(rotulosOrdenados);
            
            //Organizar as cores para padronizar os artigos: Rede InVS
            /*
            rotulosOrdenados[0] = "DISQ";
            rotulosOrdenados[1] = "DSE";
            rotulosOrdenados[2] = "SFLE";
            rotulosOrdenados[3] = "DMCT";
            rotulosOrdenados[4] = "SRH";
            */
            //Organizar as cores para padronizar os artigos: Rede Hospital
            /*
            rotulosOrdenados[0] = "ADM";
            rotulosOrdenados[1] = "MED";
            rotulosOrdenados[2] = "NUR";
            rotulosOrdenados[3] = "PAT";
            */
            //Organizar as cores para padronizar os artigos: Rede High School
            /*
            rotulosOrdenados[0] = "2BIO1";
            rotulosOrdenados[1] = "2BIO2";
            rotulosOrdenados[2] = "2BIO3";
            rotulosOrdenados[3] = "MP";
            rotulosOrdenados[4] = "MP*1";
            rotulosOrdenados[5] = "MP*2";
            rotulosOrdenados[6] = "PC";
            rotulosOrdenados[7] = "PC*";
            rotulosOrdenados[8] = "PSI*"; 
            */
            for(ArrayList<Integer> column : matrizDataInline){
                String[] tokens =  new String[9];
                tokens[0] = column.get(0).toString(); //origem
                tokens[1] = column.get(1).toString(); //destino
                tokens[2] = column.get(2).toString(); //tempo
               
                long origem = Long.parseLong(tokens[0]);
                long destino = Long.parseLong(tokens[1]);
                long time = Long.parseLong(tokens[2]);
                
                int x = 0;
                int y = 0;
                
                int insize = 50;
                
                CommunityNodeAttribute att;
                mxCell verticeOrigem;
                mxCell verticeDestino;
                
                //Source node
                verticeOrigem = (mxCell) ((mxGraphModel)graph.getModel()).getCell(origem+"");
                if(verticeOrigem == null)
                {
                     att = new CommunityNodeAttribute(origem, time, x,y,matrizRolutos.get(origem));
                     verticeOrigem = (mxCell) graph.insertVertex(null, origem+"", att , x, y, insize,insize, styleShape+mxConstants.STYLE_NOLABEL+"=0;");
                     InlineNodeAttribute attConvertido = new InlineNodeAttribute(0, (int) origem, 0, 0, origem+"", false, false, true);
                     listAttNodes.add(attConvertido);
                     //x++;
                }
                else
                {
                    att = (CommunityNodeAttribute) verticeOrigem.getValue();
                    //att.setTimes(time);
                }
                
                //Target node
                verticeDestino = (mxCell) ((mxGraphModel)graph.getModel()).getCell(destino+"");
                if(verticeDestino == null)
                {
                    att = new CommunityNodeAttribute(destino, time, x,y,matrizRolutos.get(destino));
                    verticeDestino =  (mxCell) graph.insertVertex(null, destino+"", att , x, y, insize,insize, styleShape+mxConstants.STYLE_NOLABEL+"=0;");
                    InlineNodeAttribute attConvertido = new InlineNodeAttribute(0, (int) destino, 0, 0, destino+"", false, false, true);
                    listAttNodes.add(attConvertido);
                    //x++;
                }
                else
                {
                    att = (CommunityNodeAttribute) verticeDestino.getValue();
                    //att.setTimes(time);
                }
                
                //Insert Edge
                if(verticeDestino != null && verticeOrigem != null){
                    Object edgeExist = (mxCell) ((mxGraphModel)graph.getModel()).getCell(time+" "+origem+" "+destino);
                    if(edgeExist == null)
                    {
                        mxCell edge = (mxCell) graph.insertEdge(null,time+" "+origem+" "+destino, null, verticeOrigem, verticeDestino, "");
                        edge.setVisible(false);
                        listAllEdges.add(edge);
                    }
                }
            }
            
            //CALCULO DO ALGORITMO DE COMUNIDADE INFOMAP E LOUVAIN
            
            boolean weight_external_file = false;
            HashMap<String,Integer> edgeWeight_external_file = new HashMap();
            
            comunidadesLouvain = new SLM_Louvain().execute(listAttNodes, listAllEdges, "Original Louvain",false,weight_external_file,edgeWeight_external_file);
            comunidadesInfomap = new InfoMap().execute(listAllEdges, listAttNodes,false,weight_external_file,edgeWeight_external_file);
            
             //FIM CALCULO ALGORITMO DE COMUNIDADE INFOMAP E LOUVAIN

            colorsTable = new String[100];
            /*colorsTable[0] = "000 000 255"; //blue
            colorsTable[1] = "000 255 000"; //green
            colorsTable[2] = "255 000 000"; //red
            colorsTable[3] = "255 185 60"; //orange
            colorsTable[4] = "255 255 000"; //yellow
            colorsTable[5] = "255 000 255"; //pink
            colorsTable[6] = "100 070 000"; //brown
            colorsTable[7] = "205 092 092"; //light pink
            colorsTable[8] = "148 000 211"; //purple
            colorsTable[9] = "000 255 255"; //cyan
            */
            colorsTable[0] = "040 078 162";
            colorsTable[1] = "096 187 070";
            colorsTable[2] = "237 028 036";
            colorsTable[3] = "253 183 041";
            colorsTable[4] = "244 236 009";
            colorsTable[5] = "185 076 156";
            colorsTable[6] = "086 064 024";
            colorsTable[7] = "207 082 084";
            colorsTable[8] = "124 062 152";
            colorsTable[9] = "106 204 220";
            colorsTable[10] = "035 031 032";
            
            for(int i=11; i<colorsTable.length;i++)
            {
                colorsTable[i] = "000 000 000";
            }
             
             //ATRIBUI O ID DAS COMUNIDADES AO NÓ
            for(InlineNodeAttribute attNode :listAttNodes)
            {
                 mxCell vertice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attNode.getId_original()+"");
                 CommunityNodeAttribute att = (CommunityNodeAttribute) vertice.getValue();
                 
                 for(Entry<Integer, List<InlineNodeAttribute>> comunidade : comunidadesLouvain.entrySet())
                 {
                     if(comunidade.getValue().contains(attNode))
                     {
                         att.setIdLouvain(comunidade.getKey());
                         break;
                     }
                 }
                 for(Entry<Integer, List<InlineNodeAttribute>> comunidade : comunidadesInfomap.entrySet())
                 {
                     if(comunidade.getValue().contains(attNode))
                     {
                         att.setIdInfomap(comunidade.getKey());
                         break;
                     }
                 }
            }
            
            changeMethodCommunity("Louvain");
            
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        graph.setAllowDanglingEdges(false);
        graph.setCellsEditable(false);
        graph.setCellsDisconnectable(false);
        graph.cellsOrdered(graph.getChildVertices(graph.getDefaultParent()), false);
        graph.setEdgeLabelsMovable(false);
        
        graphComponent = new mxGraphComponent(graph);
    }
    
    
    public void scrollToNodeId(String idNode, boolean center)
    {
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        graph.getModel().beginUpdate();
        idNode = idNode.replaceAll(" ","");
        
        long idNod = Long.parseLong(idNode);
        
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;
            if(cell.isVertex())
            {
                CentralityNodeAttribute att = (CentralityNodeAttribute) cell.getValue();
                if(att.getId_original() == idNod)
                {
                    graphComponent.scrollCellToVisible(cell,center);
                    break;
                }
            }
        }
        graph.getModel().endUpdate();
    }
    
    NetLayoutInlineNew.JGraphStyle style;
    public String styleShape;
    
    public void defineStyles()
    {
        
        style = NetLayoutInlineNew.JGraphStyle.ROUNDED;
        
        styleShape = mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";

        styleNode = mxConstants.STYLE_EDITABLE+"=0;";
        styleNode += mxConstants.STYLE_RESIZABLE+"=0;";
        styleNode += mxConstants.STYLE_NOLABEL+"=0;";
        
    }
    
    final public void changeMethodCommunity(String method){
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        graph.getModel().beginUpdate();
        
        for (Object root1 : roots) {
                Object[] root = {root1};
                mxCell cell = (mxCell) root1;
                if(cell.isVertex()){
                    CommunityNodeAttribute att = (CommunityNodeAttribute) cell.getValue();
                    graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                    att.setX_atual(0);
                    att.setY_atual(0);
                }
        }
        
        HashMap<Integer, List<InlineNodeAttribute>> comunidades = new HashMap();
        if(method.equals("Louvain"))
        {
            comunidades = comunidadesLouvain;
        }
        else if(method.equals("Infomap"))
        {
            comunidades = comunidadesInfomap;
        }
        
        //POSICIONA OS NÓS DE ACORDO COM AS COMUNIDADES
        for(Entry<Integer, List<InlineNodeAttribute>> comunidade : comunidades.entrySet())
        {
            int x=0;
            for(InlineNodeAttribute attNode : comunidade.getValue())
            {
                mxCell vertice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(attNode.getId_original()+"");
                CommunityNodeAttribute att = (CommunityNodeAttribute) vertice.getValue();

                att.setX_atual(x);
                att.setX_original(x);
                att.setY_original(comunidade.getKey());
                att.setY_atual(comunidade.getKey());

                double insize = 50;
                double alinhar_nos = (espacamento - insize) / 2;
                double xa = alinhar_nos + espaco_inicial+ espacamento*att.getX_original();
                double ya = alinhar_nos + espaco_inicial+ espacamento*(att.getY_original()+1);

                mxGeometry geom = (mxGeometry) vertice.getGeometry().clone();
                geom.setX(xa);
                geom.setY(ya);
                vertice.setGeometry(geom);

                int r = 0;
                int g = 0;
                int b = 0;

                //Integer key = listAllRotulos.get(att.getLabel());
                Integer key = 0;
                for(int i=0;i<rotulosOrdenados.length;i++)
                {
                    if(rotulosOrdenados[i].equals(att.getLabel()))
                        key = i;
                }
                String[] tokens = colorsTable[key].split(" ");
                r = Integer.parseInt(tokens[0]);
                g = Integer.parseInt(tokens[1]);
                b = Integer.parseInt(tokens[2]);

                Color colorNode = new Color(r,g,b);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleNode = this.styleNode;
                if(comunidadesLouvain.size() <= comunidadesInfomap.size())
                {
                    if(method.equals("Louvain"))
                    {
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                    }
                    else if(method.equals("Infomap"))
                    {
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    }
                }
                else
                {
                    if(method.equals("Louvain"))
                    {
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                    }
                    else if(method.equals("Infomap"))
                    {
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                    }
                }
                styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                
                Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);
                styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                
                Object vert = (Object) vertice;
                Object[] root = {vert};
                graph.setCellStyle(styleNode, root);
                x++;
            }
        }
        graph.getModel().endUpdate();
    }
    
    public void calculaEquivalenciaVariandoThreshold()
    {
        for(int i=0;i<=100;i++)
        {
            System.out.println(i+"\t"+calculaEquivalencia(i+""));
        }
    }
    
    final public Integer calculaEquivalencia(String equivalenceThreshold)
    {
        
        Integer qtddComunidadesEquivalentes = 0;
        
        thresholdIgualdadeNos = Double.parseDouble(equivalenceThreshold)/(double)100;
        thresholdRangeTamanhoComunidades = 1 - thresholdIgualdadeNos;
        
        String menorComunidade, maiorComunidade;
        HashMap<Integer, List<InlineNodeAttribute>> comunidadesBase, comunidadesComparacao;
        //Determina primeiro passo da equivalencia, utilizando a menor comunidade como base
        List<InlineNodeAttribute> comunidadeComparacaoNaoutilizados = new ArrayList();
        if(comunidadesLouvain.size() <= comunidadesInfomap.size())
        {
            comunidadesBase = comunidadesLouvain;
            comunidadesComparacao = comunidadesInfomap;
            menorComunidade = "Louvain";
            maiorComunidade = "Infomap";
        }
        else
        {
            comunidadesBase = comunidadesInfomap;
            comunidadesComparacao = comunidadesLouvain;
            menorComunidade = "Infomap";
            maiorComunidade = "Louvain";
        }
        
        double[][] matrizNosIguais = new double[comunidadesBase.size()][comunidadesComparacao.size()];
        int xBase = 0,xComparacao = 0;
        for(Entry<Integer, List<InlineNodeAttribute>> comunidadeBase : comunidadesBase.entrySet())
        {
            xComparacao = 0;
            //Para cada comunidade base verifica a equivalencia com todas as outras comunidades de comparacao
            for(Entry<Integer, List<InlineNodeAttribute>> comunidadeComparacao : comunidadesComparacao.entrySet())
            {
                if(((double)comunidadeComparacao.getValue().size() / (double)comunidadeBase.getValue().size()) < (1 - thresholdRangeTamanhoComunidades) || ((double)comunidadeComparacao.getValue().size() / (double)comunidadeBase.getValue().size()) > (1 + thresholdRangeTamanhoComunidades) )
                {
                    matrizNosIguais[xBase][xComparacao] = 0;
                    xComparacao++;
                    continue;
                }
                //Parametro 1 da Equivalencia de comunidades: Quantidade de nós iguais nas comunidades
                double qtdd = qtddDeNosIguais(comunidadeBase.getValue(),comunidadeComparacao.getValue());
                matrizNosIguais[xBase][xComparacao] = qtdd >= thresholdIgualdadeNos ? qtdd : 0 ;
                xComparacao++;
            }
            xBase++;
        }
        
        //adiciona no vetor de comunidadeComparacaoNaoutilizados para remover os que ja utilizaram
        List<InlineNodeAttribute> todosNosComunidadeComparacao = new ArrayList();
        for(Entry<Integer, List<InlineNodeAttribute>> comunidadeComparacao : comunidadesComparacao.entrySet())
        {
            todosNosComunidadeComparacao.addAll(comunidadeComparacao.getValue());
        }
        comunidadeComparacaoNaoutilizados = todosNosComunidadeComparacao;
        
        
        //DUPLICA OS NOS PARA DESENHAR INFOMAP E LOUVAIN
        
        Object[] roots = graph.getChildCells(graph.getDefaultParent(), true, false);
        graph.getModel().beginUpdate();
        
        for (Object root1 : roots) {
            mxCell cell = (mxCell) root1;
            if(cell.isVertex())
            {
                Object[] root = {root1};
                mxCell node = (mxCell) root1;
                CommunityNodeAttribute att = (CommunityNodeAttribute) node.getValue();
                
                //Integer key = listAllRotulos.get(att.getLabel());
                Integer key = 0;
                for(int i=0;i<rotulosOrdenados.length;i++)
                {
                    if(rotulosOrdenados[i].equals(att.getLabel()))
                        key = i;
                }
                String[] tokens = colorsTable[key].split(" ");
                int r = Integer.parseInt(tokens[0]);
                int g = Integer.parseInt(tokens[1]);
                int b = Integer.parseInt(tokens[2]);

                Color colorNode = new Color(r,g,b);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleNode = this.styleNode;
                if(comunidadesLouvain.size() <= comunidadesInfomap.size())
                {
                    styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                }
                else
                {
                    styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                }
                styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                styleNode += mxConstants.STYLE_NOLABEL+"=1;";
                styleNode +=  mxConstants.STYLE_FONTSIZE+"=20;";
                Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);
                styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                
                
                att.setLabelTela(att.getLabel()+"\n"+att.getId_original());
                
                graph.setCellStyle(styleNode, root);
                
                styleNode = this.styleNode;
                styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                styleNode += mxConstants.STYLE_NOLABEL+"=1;";
                styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                styleNode +=  mxConstants.STYLE_FONTSIZE+"=20;";
                
                CommunityNodeAttribute att2 = new CommunityNodeAttribute(att.getId_original(),att.getTimes().get(0),att.getX_original(),att.getY_original(),att.getLabel());
                att2.setLabelTela(att2.getLabel()+"\n"+att2.getId_original());
                att2.setIdInfomap(att.getIdInfomap());
                att2.setIdLouvain(att.getIdLouvain());
                mxCell vertice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(att2.getId_original()+"_2");
                if(vertice == null)
                {
                    graph.insertVertex(null, att2.getId_original()+"_2", att2 , att2.getX_original(), att2.getY_original(), 50,50, styleNode);
                }
                
            }
        }
        
        //DESENHA OS NOS EQUIVALENTES, COM SEMELHANTES EM OPACO E DIFERENTES DESTACADOS
        
        //POSICIONA TODOS OS NÓS PARA A POSICAO 0
        for (Object root1 : roots) {
            Object[] root = {root1};
            mxCell cell = (mxCell) root1;
            if(cell.isVertex())
            {
                CommunityNodeAttribute att = (CommunityNodeAttribute) cell.getValue();
                String styleCell = cell.getStyle();
                graph.moveCells(root, -cell.getGeometry().getX() , -cell.getGeometry().getY());
                att.setX_atual(0);
                att.setX_original(0);
                att.setY_atual(0);
                att.setY_original(0);
            }
        }
        
        int posicaoYNosEquivalentes = -1;
        int indice = 0;
        ArrayList<Integer> matrizNosDiferentes = new ArrayList();
        
        for(double[] comunidadeIgual : matrizNosIguais)
        {
            int posicaoMaiorNo = -1;
            int percorrerComunidadeIgual = 0;
            double maiorValor = matrizNosIguais[indice][0];
            boolean vetorZerado = true;
            for(double valor : comunidadeIgual)
            {
                if(valor > 0)
                    vetorZerado = false;
                if(valor >= maiorValor)
                {
                    maiorValor = valor;
                    posicaoMaiorNo = percorrerComunidadeIgual;
                }
                 percorrerComunidadeIgual++;
            }
           
            List<InlineNodeAttribute> comun1_equivalente = comunidadesBase.get(indice);
            List<InlineNodeAttribute> comun2_equivalente = comunidadesComparacao.get(posicaoMaiorNo);
            
            if(comun2_equivalente != null)
            {
                
                List<InlineNodeAttribute> nosEmComum = ComumNodes(comun1_equivalente,comun2_equivalente);
                
               
                
                if(!nosEmComum.isEmpty() && !vetorZerado)
                {
                    
                    //Remove os nós em comum da comunidade de comparação
                    comunidadeComparacaoNaoutilizados.removeAll(nosEmComum);
                
                    int posicaoXNosEquivalentes = 1;
                    if(nosEmComum.size() > 0)
                        posicaoYNosEquivalentes++;
                    //POSICIONA OS NÓS DO ALGORITMO DE COMUNIDADE DE COMPARACAO EM DESTAQUE
                    for(InlineNodeAttribute comun2 : comun2_equivalente)
                    {
                        if(!comun1_equivalente.contains(comun2))
                        {
                            mxCell vertice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(comun2.getId_original()+"_2");
                            CommunityNodeAttribute att = (CommunityNodeAttribute) vertice.getValue();

                            att.setX_atual(posicaoXNosEquivalentes);
                            att.setX_original(posicaoXNosEquivalentes);
                            att.setY_original(posicaoYNosEquivalentes);
                            att.setY_atual(posicaoYNosEquivalentes);

                            //Remove os nós em comum da comunidade de comparação
                            comunidadeComparacaoNaoutilizados.removeAll(comun2_equivalente);
                            
                            posicaoXNosEquivalentes++;

                            double insize = 50;
                            double alinhar_nos = (espacamento - insize) / 2;
                            double xa = alinhar_nos + espaco_inicial+ espacamento*att.getX_original();
                            double ya = alinhar_nos + espaco_inicial+ espacamento*(att.getY_original());

                            mxGeometry geom = (mxGeometry) vertice.getGeometry().clone();
                            geom.setX(xa);
                            geom.setY(ya);
                            vertice.setGeometry(geom);

                            //Integer key = listAllRotulos.get(att.getLabel());
                            Integer key = 0;
                            for(int i=0;i<rotulosOrdenados.length;i++)
                            {
                                if(rotulosOrdenados[i].equals(att.getLabel()))
                                    key = i;
                            }
                            String[] tokens = colorsTable[key].split(" ");
                            int r = Integer.parseInt(tokens[0]);
                            int g = Integer.parseInt(tokens[1]);
                            int b = Integer.parseInt(tokens[2]);

                            Color colorNode = new Color(r,g,b);
                            String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                            String styleNode = this.styleNode;
                            styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                            styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                            styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                            styleNode += mxConstants.STYLE_OPACITY+"=100;";
                            Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                            String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);
                            styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";

                            Object vert = (Object) vertice;
                            Object[] root = {vert};
                            graph.setCellStyle(styleNode, root);


                        }
                    }
                    //POSICIONA OS NÓS DO ALGORITMO DE COMUNIDADE BASE EM DESTAQUE
                    for(InlineNodeAttribute comun1 : comun1_equivalente)
                    {
                        if(!comun2_equivalente.contains(comun1))
                        {
                            mxCell vertice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(comun1.getId_original()+"");
                            CommunityNodeAttribute att = (CommunityNodeAttribute) vertice.getValue();

                            att.setX_atual(posicaoXNosEquivalentes);
                            att.setX_original(posicaoXNosEquivalentes);
                            att.setY_original(posicaoYNosEquivalentes);
                            att.setY_atual(posicaoYNosEquivalentes);

                            posicaoXNosEquivalentes++;

                            double insize = 50;
                            double alinhar_nos = (espacamento - insize) / 2;
                            double xa = alinhar_nos + espaco_inicial+ espacamento*att.getX_original();
                            double ya = alinhar_nos + espaco_inicial+ espacamento*(att.getY_original());

                            mxGeometry geom = (mxGeometry) vertice.getGeometry().clone();
                            geom.setX(xa);
                            geom.setY(ya);
                            vertice.setGeometry(geom);

                            //Integer key = listAllRotulos.get(att.getLabel());
                            Integer key = 0;
                            for(int i=0;i<rotulosOrdenados.length;i++)
                            {
                                if(rotulosOrdenados[i].equals(att.getLabel()))
                                    key = i;
                            }
                            String[] tokens = colorsTable[key].split(" ");
                            int r = Integer.parseInt(tokens[0]);
                            int g = Integer.parseInt(tokens[1]);
                            int b = Integer.parseInt(tokens[2]);

                            Color colorNode = new Color(r,g,b);
                            String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                            String styleNode = this.styleNode;
                            styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                            styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                            styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                            styleNode += mxConstants.STYLE_OPACITY+"=100;";
                            Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                            String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);
                            styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";

                            Object vert = (Object) vertice;
                            Object[] root = {vert};
                            graph.setCellStyle(styleNode, root);


                        }
                    }
                    
                    //POSICIONA OS NÓS EM COMUM DAS DUAS COMUNIDADES (EM OPACO)
                    for(InlineNodeAttribute noEmComum : nosEmComum)
                    {
                        mxCell vertice = (mxCell) ((mxGraphModel)graph.getModel()).getCell(noEmComum.getId_original()+"_2");
                        CommunityNodeAttribute att = (CommunityNodeAttribute) vertice.getValue();

                        mxCell verticeBase = (mxCell) ((mxGraphModel)graph.getModel()).getCell(noEmComum.getId_original()+"");
                        CommunityNodeAttribute attBase = (CommunityNodeAttribute) verticeBase.getValue();

                        att.setX_atual(posicaoXNosEquivalentes);
                        att.setX_original(posicaoXNosEquivalentes);
                        att.setY_original(posicaoYNosEquivalentes);
                        att.setY_atual(posicaoYNosEquivalentes);
                        
                        attBase.setX_atual(posicaoXNosEquivalentes);
                        attBase.setX_original(posicaoXNosEquivalentes);
                        attBase.setY_original(posicaoYNosEquivalentes);
                        attBase.setY_atual(posicaoYNosEquivalentes);

                        double insize = 50;
                        double alinhar_nos = (espacamento - insize) / 2;
                        double xa = alinhar_nos + espaco_inicial+ espacamento*posicaoXNosEquivalentes;
                        double ya = alinhar_nos + espaco_inicial+ espacamento*(posicaoYNosEquivalentes);

                        mxGeometry geom = (mxGeometry) vertice.getGeometry().clone();
                        geom.setX(xa);
                        geom.setY(ya);
                        vertice.setGeometry(geom);
                        verticeBase.setGeometry(geom);

                        //Integer key = listAllRotulos.get(att.getLabel());
                        Integer key = 0;
                        for(int i=0;i<rotulosOrdenados.length;i++)
                        {
                            if(rotulosOrdenados[i].equals(att.getLabel()))
                                key = i;
                        }
                        String[] tokens = colorsTable[key].split(" ");
                        int r = Integer.parseInt(tokens[0]);
                        int g = Integer.parseInt(tokens[1]);
                        int b = Integer.parseInt(tokens[2]);

                        Color colorNode = new Color(r,g,b);
                        String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                        String styleNode = this.styleNode;
                        styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                        styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                        styleNode += mxConstants.STYLE_OPACITY+"=10;";
                        styleNode += mxConstants.STYLE_NOLABEL+"=1;";
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                        Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                        String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);
                        styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";

                        Object vert = (Object) vertice;
                        Object[] root = {vert};
                        graph.setCellStyle(styleNode, root);
                        
                        styleNode = this.styleNode;
                        styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                        styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                        styleNode += mxConstants.STYLE_OPACITY+"=20;";
                        styleNode += mxConstants.STYLE_NOLABEL+"=1;";
                        styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";

                        vert = (Object) verticeBase;
                        Object[] root2 = {vert};
                        graph.setCellStyle(styleNode, root2);

                        posicaoXNosEquivalentes++;
                    }

                    
                }
                else
                {
                    matrizNosDiferentes.add(indice);
                }
            }
            indice++;
        }
        
        qtddComunidadesEquivalentes = posicaoYNosEquivalentes+1;
        
        //Shift para dar espaço para os primeiros nós da menor comunidade sem equivalencia
        posicaoYNosEquivalentes = posicaoYNosEquivalentes+3;
        //DESENHA AS COMUNIDADES DA MENOR COMUNIDADE SEM EQUIVALENCIA
        for(Integer idComunidadeDiferente : matrizNosDiferentes)
        {
            int posicaoXNosEquivalentes = 1;
            List<InlineNodeAttribute> comunbase_semEquivalencia = comunidadesBase.get(idComunidadeDiferente);
            for(InlineNodeAttribute nodeBase_semEquvalencia : comunbase_semEquivalencia)
            {
                mxCell verticeBase = (mxCell) ((mxGraphModel)graph.getModel()).getCell(nodeBase_semEquvalencia.getId_original()+"");
                CommunityNodeAttribute attBase = (CommunityNodeAttribute) verticeBase.getValue();

                attBase.setX_atual(posicaoXNosEquivalentes);
                attBase.setX_original(posicaoXNosEquivalentes);
                attBase.setY_original(posicaoYNosEquivalentes);
                attBase.setY_atual(posicaoYNosEquivalentes);

                double insize = 50;
                double alinhar_nos = (espacamento - insize) / 2;
                double xa = alinhar_nos + espaco_inicial+ espacamento*posicaoXNosEquivalentes;
                double ya = alinhar_nos + espaco_inicial+ espacamento*(posicaoYNosEquivalentes);

                mxGeometry geom = (mxGeometry) verticeBase.getGeometry().clone();
                geom.setX(xa);
                geom.setY(ya);
                verticeBase.setGeometry(geom);

                //Integer key = listAllRotulos.get(att.getLabel());
                Integer key = 0;
                for(int i=0;i<rotulosOrdenados.length;i++)
                {
                    if(rotulosOrdenados[i].equals(attBase.getLabel()))
                        key = i;
                }
                String[] tokens = colorsTable[key].split(" ");
                int r = Integer.parseInt(tokens[0]);
                int g = Integer.parseInt(tokens[1]);
                int b = Integer.parseInt(tokens[2]);

                Color colorNode = new Color(r,g,b);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleNode = this.styleNode;
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_RECTANGLE+";";
                styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                

                Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);

                styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";
                
                Object vert = (Object) verticeBase;
                Object[] root = {vert};
                graph.setCellStyle(styleNode, root);
                posicaoXNosEquivalentes++;
            }
            posicaoYNosEquivalentes++;
        }
        
        //Organiza os nós nao utilizados em comunidades
        HashMap<Integer, List<mxCell>> maiorComunidade_semEquivalencia = new HashMap();
        for (InlineNodeAttribute node : comunidadeComparacaoNaoutilizados) {
            mxCell verticeBase = (mxCell) ((mxGraphModel)graph.getModel()).getCell(node.getId_original()+"_2");
            CommunityNodeAttribute att = (CommunityNodeAttribute) verticeBase.getValue();
            
            int id;
            if(maiorComunidade.equals("Infomap"))
                id = att.getIdInfomap();
            else
                id = att.getIdLouvain();
            
            List<mxCell> lista = maiorComunidade_semEquivalencia.get(id);
            if(lista == null)
            {
                lista = new ArrayList();
            }
            lista.add(verticeBase);
            maiorComunidade_semEquivalencia.put(id, lista);
        }
        
        //Shift para dar espaço para os primeiros nós da maior comunidade sem equivalencia
        posicaoYNosEquivalentes = posicaoYNosEquivalentes+2;
        //DESENHA AS COMUNIDADES DA MAIOR COMUNIDADE SEM EQUIVALENCIA
        for(Entry<Integer, List<mxCell>> comunidadeComparacao : maiorComunidade_semEquivalencia.entrySet())
        {
            int posicaoXNosEquivalentes = 1;
            for(mxCell verticeBase : comunidadeComparacao.getValue())
            {
                CommunityNodeAttribute attBase = (CommunityNodeAttribute) verticeBase.getValue();

                attBase.setX_atual(posicaoXNosEquivalentes);
                attBase.setX_original(posicaoXNosEquivalentes);
                attBase.setY_original(posicaoYNosEquivalentes);
                attBase.setY_atual(posicaoYNosEquivalentes);

                double insize = 50;
                double alinhar_nos = (espacamento - insize) / 2;
                double xa = alinhar_nos + espaco_inicial+ espacamento*posicaoXNosEquivalentes;
                double ya = alinhar_nos + espaco_inicial+ espacamento*(posicaoYNosEquivalentes);

                mxGeometry geom = (mxGeometry) verticeBase.getGeometry().clone();
                geom.setX(xa);
                geom.setY(ya);
                verticeBase.setGeometry(geom);

                //Integer key = listAllRotulos.get(att.getLabel());
                Integer key = 0;
                for(int i=0;i<rotulosOrdenados.length;i++)
                {
                    if(rotulosOrdenados[i].equals(attBase.getLabel()))
                        key = i;
                }
                String[] tokens = colorsTable[key].split(" ");
                int r = Integer.parseInt(tokens[0]);
                int g = Integer.parseInt(tokens[1]);
                int b = Integer.parseInt(tokens[2]);

                Color colorNode = new Color(r,g,b);
                String hexColor = "#"+Integer.toHexString(colorNode.getRGB()).substring(2);

                String styleNode = this.styleNode;
                styleNode += mxConstants.STYLE_SHAPE+"="+mxConstants.SHAPE_ELLIPSE+";";
                styleNode +=  mxConstants.STYLE_FILLCOLOR+"="+hexColor+";";
                styleNode +=  mxConstants.STYLE_STROKECOLOR+"="+hexColor+";";
                styleNode += mxConstants.STYLE_OPACITY+"=100;";
                styleNode += mxConstants.STYLE_NOLABEL+"=0;";
                
                Color corEmNegativo = new Color(255 - colorNode.getRed(), 255 - colorNode.getGreen(), 255 - colorNode.getBlue());
                String hexColorNegative = "#"+Integer.toHexString(corEmNegativo.getRGB()).substring(2);

                styleNode +=  mxConstants.STYLE_FONTCOLOR+"="+hexColorNegative+";";

                Object vert = (Object) verticeBase;
                Object[] root = {vert};
                graph.setCellStyle(styleNode, root);
                posicaoXNosEquivalentes++;
            }
            posicaoYNosEquivalentes++;
        }
        
        graph.getModel().endUpdate();
        
        return qtddComunidadesEquivalentes;
        
    }

    double thresholdIgualdadeNos;
    double thresholdRangeTamanhoComunidades;
    
    private double qtddDeNosIguais(List<InlineNodeAttribute> nosBase, List<InlineNodeAttribute> nosComparacao) 
    {
        int qtddNosIguais = 0;
        for(InlineNodeAttribute attNode : nosBase)
        {
            if(nosComparacao.contains(attNode))
                qtddNosIguais++;
        }
        double porcentagemAcerto = (double)qtddNosIguais/ (double)nosBase.size();
        return porcentagemAcerto;
    }

    private List<InlineNodeAttribute> ComumNodes(List<InlineNodeAttribute> comun1_equivalente, List<InlineNodeAttribute> comun2_equivalente) {
        List<InlineNodeAttribute> vetorComum = new ArrayList();
        for(int i = 0; i < comun1_equivalente.size();i++)
        {
            for(int j = 0; j < comun2_equivalente.size();j++)
            {
                if(comun1_equivalente.get(i).getId_original() == comun2_equivalente.get(j).getId_original())
                    vetorComum.add(comun1_equivalente.get(i));
            }
        }
        return vetorComum;
    }

    
    
}
