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

package preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Claudio Linhares
 */
public class FiltrarArquivoTwitter {

    /**
     * @param args the command line arguments
     */
    static String claudio = ""; //C:\\Users\\Claudio Linhares\\Documents\\twitter";
    
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList bd = leBaseOriginal();
        CriaArquivoBD(bd);
    
        
    }
    
   private static ArrayList leBaseOriginal() {
        ArrayList objetos = new ArrayList();
        BufferedReader br = null;
        String sCurrentLine;
        
        
        try {
            int i=0;
            br = new BufferedReader(new FileReader(claudio+"\\nodesVisual-undirected-noGlobal.dat"));
            while ((sCurrentLine = br.readLine()) != null) {
                if(i == 0)
                {
                    i++;
                    continue;
                }
                ArrayList<String> coluna = new ArrayList<>();
                int inicial = sCurrentLine.indexOf("[");
                int fin = sCurrentLine.indexOf("]");
                
                String topicos = sCurrentLine.substring(inicial, fin);
             
                String coluna_final = "[";
             
                boolean todosTopicosZero = true;
                
                String[] topico = topicos.split(",");
                for(String topic : topico)
                {
                    String[] inic = topic.split("=");
                    double top = Double.parseDouble(inic[1]);
                    if(top != 0)
                        todosTopicosZero = false;
                    coluna_final += inic[1]+",";
                }
                coluna_final = coluna_final.substring(0,coluna_final.length()-1);
                //coluna_final += "]";
                
                String[] tokens = sCurrentLine.split(";");
                double clo = Double.parseDouble(tokens[2]);
                double bet = Double.parseDouble(tokens[3]);
                double dee = Double.parseDouble(tokens[4]);
                double cloG = Double.parseDouble(tokens[6]);
                double betG = Double.parseDouble(tokens[7]);
                double deeG = Double.parseDouble(tokens[8]);
                //if(bet == 0 && clo == 0)
                if(bet == 0 && clo == 0  && dee == 0  && cloG == 0  && betG == 0 && deeG == 0 && todosTopicosZero)
                    continue;
                objetos.add(sCurrentLine.substring(0, inicial) + coluna_final + sCurrentLine.substring(fin, sCurrentLine.length()) + "\r\n");
            }
            return objetos;

//               System.out.println("Tamanho = " + objetos.size());
//               
//            Collections.sort(objetos, new Comparator<long[]>() {
//            @Override
//            public int compare(long[] i1, long[] i2)
//            {
//                   return Long.compare(i1[2], i2[2]);
//            }
//    });

        } catch (IOException ex) {
            Logger.getLogger(adaptaBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(adaptaBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objetos;
    }
   
   
   
    private static void CriaArquivoBD(ArrayList bd)
    {
        //boolean gravarCabecalho = false;
        try
        {
            File file = new File(claudio+"\\nodesVisual-undirected-noGlobal-filtrado.dat");
            file.createNewFile();
            //gravarCabecalho = true;

            //Here true is to append the content to file
            FileWriter fw = new FileWriter(file,false);
            //BufferedWriter writer give better performance
            BufferedWriter bw = new BufferedWriter(fw);
            for(Object line : bd)
            {
                String l = (String) line;
                bw.write(l);
            }
            //Closing BufferedWriter Stream
            bw.close();
        }
        catch(IOException ioe)
        {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
        }
	
    }
    
}
