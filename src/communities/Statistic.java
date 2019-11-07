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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import layout.InlineNodeAttribute;

/**
 *
 * @author Jean
 */
public class Statistic {
    
    private static int communityMinSizeThreshold = 1;
   // private static String pathGT = "D:\\Dropbox\\Claudio\\Networks\\Escola\\Primary School\\GT.txt";
    private static String pathGT = "C:\\Users\\Claudio Linhares\\Dropbox\\Doutorado\\Pesquisa Claudio (1)\\Networks\\inVS\\GT.txt";
  //  private static String pathGT = "D:\\Dropbox\\Claudio\\dados\\hospital\\GT.txt";
    private static HashMap<Integer, List<Integer>> comunidadesGT;
    private boolean verbose = false;
    
    //Assume cada departamento como sendo uma comunidade, vinculando ids a eles.
    // Relação DPTO / Id: DISQ = 0, DSE = 1, SFLE = 2, DMCT = 3, SRH = 4.
    private static void leArquivoGroundTruth()
    {
       BufferedReader br = null;
      comunidadesGT = new HashMap<>();
        try {
            String sCurrentLine;

            File file = new File(pathGT);
            br = new BufferedReader(new FileReader(file));
            while ((sCurrentLine = br.readLine()) != null) 
            {
                if(sCurrentLine.startsWith("//"))
                    continue;
                String[] no = sCurrentLine.split(" ");
                if(no.length != 2)
                {
                    System.out.println("Informacoes erradas no arquivo do Ground Truth.");
                    return;
                }
                int comunidadeDoNoGT = Integer.parseInt(no[1]);
                if(!comunidadesGT.containsKey(comunidadeDoNoGT))
                    comunidadesGT.put((comunidadeDoNoGT), new ArrayList());
                comunidadesGT.get(comunidadeDoNoGT).add(Integer.parseInt(no[0]));       
            }
            br.close();

        } catch (IOException e) {
                e.printStackTrace();
                return;
        } finally {
                try {
                        if (br != null)
                                br.close();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }

        }
    }
    
    public double calculaPrecisao(HashMap<Integer, List<InlineNodeAttribute>> comunidades)
    {
        int maiorNumeroDeIntersecoes = -1;
        double somaPrecisao = 0;
        double valorPrecisao = -1.0; //valor = somaPrecisao / qtd de comunidades
        //Para cada comunidade detectada
        for(int i = 0; i < comunidades.size(); i++)
        {
            if (comunidades.get(i).size() < communityMinSizeThreshold)
                continue;
            if(verbose)
            {
                Collections.sort (comunidades.get(i), new Comparator() { //zero se igual, -1 se 1 < 2; +1 se 2 < 1
                public int compare(Object o1, Object o2) {

                    InlineNodeAttribute v1 = (InlineNodeAttribute) o1;
                    InlineNodeAttribute v2 = (InlineNodeAttribute) o2;

                    if(v1.getId_original() < v2.getId_original())
                        return -1;
                    if(v1.getId_original() > v2.getId_original())
                        return 1;
                    if(Objects.equals(v1.getId_original(), v2.getId_original()))
                        return 0;
                    return 0;
                    }
                });
            }
            //Pra cada comunidade do ground truth
            for(int j = 0; j < comunidadesGT.size(); j++)
            {

                //Pega a maior interseção, caso no qual haverá match entre a comunidadeGT e a detectada.
                List<Integer> intersecao = intersection(comunidades.get(i), comunidadesGT.get(j));
                if(maiorNumeroDeIntersecoes < intersecao.size())
                {
                    maiorNumeroDeIntersecoes = intersecao.size();
                    if(verbose)
                    {
                        System.out.println("Match:");
                        System.out.println(comunidades.get(i));
                        Collections.sort(comunidadesGT.get(j));
                        System.out.println(comunidadesGT.get(j));
                    }
                }
            }
            somaPrecisao += (double)maiorNumeroDeIntersecoes/(double)comunidades.get(i).size();
            maiorNumeroDeIntersecoes = -1;
        }
        valorPrecisao = somaPrecisao / (double) comunidades.size();
        System.out.println("Valor da precisao: " + valorPrecisao);
        return valorPrecisao;
    }
    
    public double calculaRevocacao(HashMap<Integer, List<InlineNodeAttribute>> comunidades)
    {
        System.out.println("Começando o calculo da revocação:");
        int maiorNumeroDeIntersecoes = -1;
        double somaRevocacao = 0;
        double valorRevocacao = -1.0; //valor = somaPrecisao / qtd de comunidades
        //Para cada comunidade do ground truth
        for(int i = 0; i < comunidadesGT.size(); i++)
        {
            Collections.sort(comunidadesGT.get(i));
            //Pra cada comunidade detectada
            for(int j = 0; j < comunidades.size(); j++)
            {
                if (comunidades.get(j).size() < communityMinSizeThreshold)
                    continue;
                //Pega a maior interseção, caso no qual haverá match entre a comunidadeGT e a detectada.
                List<Integer> intersecao = intersection(comunidades.get(j), comunidadesGT.get(i));
                if(maiorNumeroDeIntersecoes < intersecao.size())
                {
                    maiorNumeroDeIntersecoes = intersecao.size();
                    if(verbose)
                    {
                        System.out.println("Match:");

                        Collections.sort (comunidades.get(j), new Comparator() { //zero se igual, -1 se 1 < 2; +1 se 2 < 1
                        public int compare(Object o1, Object o2) {

                            InlineNodeAttribute v1 = (InlineNodeAttribute) o1;
                            InlineNodeAttribute v2 = (InlineNodeAttribute) o2;

                            if(v1.getId_original() < v2.getId_original())
                                return -1;
                            if(v1.getId_original() > v2.getId_original())
                                return 1;
                            if(Objects.equals(v1.getId_original(), v2.getId_original()))
                                return 0;
                            return 0;
                            }
                        });
                        System.out.println(comunidades.get(j));
                        System.out.println(comunidadesGT.get(i));
                    }
                }
            }
            somaRevocacao += (double)maiorNumeroDeIntersecoes/(double)comunidadesGT.get(i).size();
            maiorNumeroDeIntersecoes = -1;
        }
        valorRevocacao = somaRevocacao / (double) comunidadesGT.size();
        System.out.println("Valor da revocação: " + valorRevocacao);
        return valorRevocacao;
    }
    
    public List<Integer> intersection(List<InlineNodeAttribute> list1, List<Integer> list2) {
        List<Integer> list = new ArrayList<Integer>();

        for (InlineNodeAttribute t : list1) {
            if(list2.contains(t.getId_original())) {
                list.add(t.getId_original());
            }
        }
        if(verbose)
            System.out.println("Interseção (" + list.size() + "): " + list);
        return list;
    }
    
    public double calculaFMeasure(HashMap<Integer, List<InlineNodeAttribute>> comunidades, String algoritmoDeteccao)
    {
        leArquivoGroundTruth();
        double precisao = calculaPrecisao(comunidades);
        double revocacao = calculaRevocacao(comunidades);
        
        double fmeasure = (2*precisao*revocacao)/(precisao+revocacao);
        System.out.println("F-Measure para o " + algoritmoDeteccao + " = " + fmeasure);
        return fmeasure;
    }
}
