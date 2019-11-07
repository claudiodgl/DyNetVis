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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.FileHandler;

/**
 *
 * @author Jean
 * 
 * After generating random modular networks with Python code from https://github.com/prathasah/random-modular-network-generator,
 * this code adapts the files of the networks to work with DyNetVis by including repetition of edges throughout time.
 * 
 */
public class RandomModularNetworkGenerator {
    
    static double porcentagemArestasDaRedeACadaInstante = 0.05;
    static int qtdTimestamps = 500;
    
    //Exemplo: 1500 arestas na rede lida. Isso dá 75 arestas por instante de tempo = 37500 arestas = 25x a quantidade original de arestas.
    
    
//Converte a rede complexa gerada no python para uma rede temporal com repetição de arestas ao longo do tempo
    public static void main(String a[]) {
        //String novaRede = converteRedeInput();
        
        //if(novaRede == "")
        //    return;
        
        //Usado tanto na resolução dinâmica quanto no edge sampling
        //NetLayoutInlineNew.windowSizeValue = 100;
        
        //ArrayList arestasRede = new ArrayList<String>(Arrays.asList(novaRede.split("\r\n")));
        
    }
  
    public static String converteRedeInput(String nomeRede, int qtdArquivo)
    {
        ArrayList<String> arestasRedeLida = new ArrayList<>();
        ArrayList<String> novaRede = new ArrayList<>();
      
        
        String filename = "F:\\2.7\\random-modular-network-generator-master\\redes\\" + nomeRede;
        
        BufferedReader file;
        String strLine = "";
        String novaRedeString = "";
        try {
            file = new BufferedReader(new FileReader(new File(filename)));

                
                while ((strLine = file.readLine()) != null)
                {

                   if(!strLine.equals(("")))
                   {
                        if(!Character.isDigit(strLine.charAt(0)))
                            continue;
                        String[] linha = strLine.split("[ \\t]");

                        arestasRedeLida.add((Integer.parseInt(linha[0]) + 1) + "\t" + (Integer.parseInt(linha[1]) + 1)); //+1 pois a rede gerada começa em zero.
                   }
                }

                Random r = new Random();
                
                    
                for(int timestamp = 1; timestamp < qtdTimestamps; timestamp++)
                {
                    int random1 = r.nextInt((int) (arestasRedeLida.size()*porcentagemArestasDaRedeACadaInstante));
                    for(int i = 0; i < random1; i++)
                    {
                        int random = r.nextInt(arestasRedeLida.size());
                        novaRedeString += arestasRedeLida.get(random) + "\t" + timestamp + "\r\n";
                        //novaRede.add(arestasRedeLida.get(random) + "\t" + timestamp);
                    }
                }
                 FileHandler.gravaArquivo(novaRedeString, "F:\\redesdynetvis\\" + qtdArquivo + "_" + nomeRede, true);
            } catch (IOException ex) {
        Logger.getLogger(RandomModularNetworkGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            return novaRedeString;
        }
    }
    
    
}
