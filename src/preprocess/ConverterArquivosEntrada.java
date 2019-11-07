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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static preprocess.FiltrarArquivoTwitter.claudio;

/**
 *
 * @author Claudio Linhares
 */
public class ConverterArquivosEntrada {

    /**
     * @param args the command line arguments
     */
    
    static String claudio = "C:\\Users\\Claudio Linhares\\Documents\\escola";
    
    public static void main(String[] args) {
        
        BufferedReader br;
        String sCurrentLine;
        String arquivoConvertido = "";
        Date tempo_salvo = null;
        long tempo = 0;
        try {
            br = new BufferedReader(new FileReader(claudio+"\\HighSchool.txt"));
            
            int x=0;
            int tempoInicial =0;
            long tempoFinal = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] tokens = sCurrentLine.split(" ");
                
                /*if( x == 0 )
                {
                    tempoInicial = Integer.parseInt(tokens[0]);
                    x++;
                }*/
                
                if( x == 0 )
                {
                    long timestamp = Long.parseLong(tokens[0]);
                    timestamp *= 1000;
                    tempo_salvo = new Date(timestamp);
                    x++;
                }
                
                long timestamp = Long.parseLong(tokens[0]);
                
                timestamp *= 1000;
                Date tempo_novo = new Date(timestamp);
                
                
                
                long secs = Math.abs(tempo_salvo.getTime() - tempo_novo.getTime()) / 1000;
                tempoFinal = tempoFinal + secs/20;
                tempo_salvo = tempo_novo;
                
                //long tempoFinal = (Integer.parseInt(tokens[0]) - tempoInicial)/20;
                
                arquivoConvertido += tokens[1] + " "+ tokens[2] +" "+tempoFinal+"\r\n";
            }
            
            
            File file = new File(claudio+"\\HighSchoolConvertida.dat");
            file.createNewFile();
            //gravarCabecalho = true;

            //Here true is to append the content to file
            FileWriter fw = new FileWriter(file,false);
            //BufferedWriter writer give better performance
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(arquivoConvertido);
            //Closing BufferedWriter Stream
            bw.close();
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConverterArquivosEntrada.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConverterArquivosEntrada.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
