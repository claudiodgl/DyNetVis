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

package util;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSonParser {
    
    public static void main(String a[]) {
        
        BufferedReader file = null;
        String line;
        String[] tokens;
        System.out.println("Reading ");
        ArrayList<Integer> tempIds = new ArrayList<Integer>();
        ArrayList<String> edges = new ArrayList<String>();
        try{
            file = new BufferedReader(new FileReader(new File("")));
            while((line = file.readLine()) != null) {
                tokens = line.split(" ");
                try {
                    int idSource = Integer.parseInt(tokens[0]);
                    int idTarget = Integer.parseInt(tokens[1]);
                    int time = Integer.parseInt(tokens[2]);
                    if (!tempIds.contains(idSource))
                        tempIds.add(idSource);
                    if (!tempIds.contains(idTarget))
                        tempIds.add(idTarget);
                    edges.add("{\"source\":"+tempIds.indexOf(idSource)+",\"target\":"+tempIds.indexOf(idTarget)+",\"value\":"+time+"}");
                }catch (NumberFormatException e) {
                    continue;
                }
            }
            String ret = "{\n\"nodes\":[\n";
            for (int i=0;i<tempIds.size()-1;i++)
                ret += "{\"name\":"+tempIds.get(i)+",\"group\":1},\n";
            ret += "{\"name\":"+tempIds.get(tempIds.size()-1)+",\"group\":1}\n],\n\"links\":[\n";
            for (int i=0;i<edges.size()-1;i++)
                ret += edges.get(i)+",\n";
            ret += edges.get(edges.size()-1)+"\n]\n}";
            
            BufferedWriter escritor = null; //objeto escritor
            try{
                escritor = new BufferedWriter(new FileWriter(new File("")));
            //Instanciação do objeto escritor
            escritor.write(ret); //Gravação do texto
            escritor.flush(); //descarga do buffer de escrita
            escritor.close(); //fechamento do arquivo
            } catch(IOException e){
                e.printStackTrace();
            }
        }catch (java.io.IOException e) {
            Logger.getLogger(JSonParser.class.getName()).log(Level.SEVERE, null, e);
        }
                
    }
    
}
