/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import forms.MainForm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MARK2
 */
public class FluxoDeEntrada {
    
public static ArrayList<ObjetoRede> leRedeInicial() {
        ArrayList<ObjetoRede> redeInicial = new ArrayList();
        BufferedReader br = null;
        String sCurrentLine;
        try {
            String diretorio = MainForm.diretorioRedeStream.toLowerCase();
            if(diretorio.contains("facebook"))
                br = new BufferedReader(new FileReader(MainForm.diretorioRedeStream + "facebook_0001_graph.txt")); //"stack00_graph.txt"));
            else if(diretorio.contains("hepph"))
                br = new BufferedReader(new FileReader(MainForm.diretorioRedeStream + "hepPh0001_graph.txt")); //"stack00_graph.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                if(i <= 2) //file's header
                {
                    i++;
                    continue;
                }
                    
                String[] spl = sCurrentLine.split("\t");
                if(spl.length < 5 || spl.length > 6) //If edge, then 5; else if node, then 6.
                {
                    System.out.println("Tamanho invalido de linha do arquivo. Retorna null");
                    return null;
                }
                ObjetoRede obj;
                if(spl[1].equals("Node"))
                    obj = new ObjetoRede(spl[0], spl[1], Long.parseLong(spl[2]), -1, -1, spl[5],-1);
                else //Edge
                    obj = new ObjetoRede(spl[0], spl[1], -1, Long.parseLong(spl[3]), Long.parseLong(spl[4]), "", 0); //Rede inicial. Todas as arestas no tempo zero.
                redeInicial.add(obj);
                
            }
               System.out.println("Tamanho da rede = " + redeInicial.size());

        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redeInicial;
    }





}
