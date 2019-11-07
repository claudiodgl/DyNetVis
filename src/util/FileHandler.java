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

import forms.MainForm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import layout.InlineNodeAttribute;

/**
 *
 * @author Jean
 */
public class FileHandler {
    public static void gravaArquivo(String conteudo, String path, boolean append)
	{
            try
            {
                File file = new File(path);
                if(!file.exists()){
                   file.createNewFile();

                }
                //Here true is to append the content to file
                FileWriter fw = new FileWriter(file,append);
                //BufferedWriter writer give better performance
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(conteudo);
                //Closing BufferedWriter Stream
                bw.close();
            }
            catch(IOException ioe)
            {
                System.out.println("Exception occurred:");
                ioe.printStackTrace();
            }
	}
    
    public static String leArquivo(String pathArquivo)
	{
		BufferedReader br = null;
		StringBuilder conteudo = new StringBuilder();
		try {
		String sCurrentLine;
		File file = new File(pathArquivo);
		br = new BufferedReader(new FileReader(file));
			while ((sCurrentLine = br.readLine()) != null) 
			{
				conteudo.append(sCurrentLine);
                                conteudo.append(System.getProperty("line.separator"));
			}
			br.close();
		return conteudo.toString();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
    
    public static ArrayList<Integer> GetNosOrdenacaoPrevia()
    {
        ArrayList<Integer> nosRede = new ArrayList<>();
         String line;
        
        JFileChooser openDialog = new JFileChooser();
            String filename = "";

            openDialog.setMultiSelectionEnabled(false);
            openDialog.setDialogTitle("Open file");


            openDialog.setSelectedFile(new File(filename));
            openDialog.setCurrentDirectory(new File(filename));

            int contColorsLabel = 0;
            
            int result = openDialog.showOpenDialog(openDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                filename = openDialog.getSelectedFile().getAbsolutePath();
                openDialog.setSelectedFile(new File(""));
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(new File(filename)));
        
                    int i = 0, x = 0;
                    while ((line = br.readLine()) != null) 
                    {
                        if(!line.equals(("")))
                        {
                            Integer no = Integer.parseInt(line);
                            nosRede.add(no);
                        }
                    }
                    return nosRede;
                
            } catch (Exception ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            return null;
    }
    
    
    public static ArrayList<Integer> GetNosRede(String arquivoRede)
    {
        ArrayList<Integer> nosRede = new ArrayList<>();
        BufferedReader br = null;
        String line;
        try {
                br = new BufferedReader(new FileReader(arquivoRede));
        
                int i = 0, x = 0;
                while ((line = br.readLine()) != null) {
                    if(!line.equals(("")))
                    {
                        if(!Character.isDigit(line.charAt(0)))
                            continue;
                    }
                    else
                        continue;
                    String[] spl = line.split("[ \\t]");

                    if(spl[0].equals(spl[1])) //Ignora arestas de um nó pra ele mesmo. Motivo: Não funciona no CNO pois a detecção de comunidade não aceita isso (no layout, esses nós ficam sobrepostos na posição origem)
                        continue;

                    int origem = Integer.parseInt(spl[0]);
                    int destino = Integer.parseInt(spl[1]);
                    if(!nosRede.contains(origem))
                        nosRede.add(origem);
                    if(!nosRede.contains(destino))
                        nosRede.add(destino);
                }
                return nosRede;
        
            } catch (Exception ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static ArrayList<HashMap<Integer, List<InlineNodeAttribute>>> leArquivoROC(String pathArquivo, List<InlineNodeAttribute> listAttNodes) throws Exception
	{
                HashMap<Integer, List<InlineNodeAttribute>> comunidadesNoNivel = new HashMap<>();   
                ArrayList<HashMap<Integer, List<InlineNodeAttribute>>> comunidadesTodosNiveis = new ArrayList<>();
		BufferedReader br = null;
		try {
		String sCurrentLine;
		File file = new File(pathArquivo);
		br = new BufferedReader(new FileReader(file));
                while ((sCurrentLine = br.readLine()) != null) 
                {
                    if(sCurrentLine.startsWith("Comunidades"))
                        continue;
                    else if(!sCurrentLine.contains(":")) //Ignora linhas em branco. Além disso, se chegou em linha em branco, então chegou no fim de um nivel. Nesse caso, salvar as comunidades no arraylsit.
                    {
                        comunidadesTodosNiveis.add(comunidadesNoNivel);
                        comunidadesNoNivel = new HashMap<>(); 
                        continue;
                    }
                      
                    String[] linha = sCurrentLine.split(": ");
                    int idComunidade = Integer.parseInt(linha[0]);
                    comunidadesNoNivel.put(idComunidade, new ArrayList<InlineNodeAttribute>());
                    
                    String nosStr = linha[1].substring(1, linha[1].length() - 1); //ignora o [ e o ]
                    String[] colecaoNosStr = nosStr.split(",");
                    for(String no : colecaoNosStr)
                    {
                        boolean achouNo = false;
                        Integer idNo = Integer.parseInt(no);
                        for(InlineNodeAttribute objNo : listAttNodes)
                        {
                            if(objNo.getId_original() == idNo)
                            {
                                comunidadesNoNivel.get(idComunidade).add(objNo);
                                achouNo = true;
                                break;
                            }
                        }
                        if(!achouNo)
                        {
                            throw new Exception("Não achou o nó " + no + ". Confira se o arquivo aberto corresponde à rede sendo analisada.");
                        }
                    }
                }
                br.close();
		return comunidadesTodosNiveis;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
		}
	}
}
