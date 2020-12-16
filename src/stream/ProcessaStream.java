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
public class ProcessaStream {
    
    public static ArrayList<ObjetoRede> calculaPosicoesRedeInicial() {
        
        ArrayList<ObjetoRede> redeInicial = FluxoDeEntrada.leRedeInicial();
        
        
    //  Calcula posições iniciais
    //          \/
    
    
        BufferedReader br = null;
        String sCurrentLine;
        try {
            br = new BufferedReader(new FileReader(MainForm.diretorioArquivosStreamPosicoes +  "teste_0000.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
         //   int i = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] spl = sCurrentLine.split("\t");
                for(int i = 0; i < redeInicial.size(); i++)
                {
                    ObjetoRede obj = redeInicial.get(i);
                    if(obj.getNodeId() == Long.parseLong(spl[0]))
                    {
                        obj.setPosX(Double.parseDouble(spl[1]));
                        obj.setPosY(Double.parseDouble(spl[2]));
                        break;
                    }
                        
                }
            }
               System.out.println("Tamanho da rede = " + redeInicial.size());

        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return redeInicial;
    }
    
    
    //Lê as redes tradicionais do Claudio (redes com inicio e fim conhecidos a fim de simular stream)

    /*
    private LinkedList lePosicoesFluxo() {
        LinkedList objetos = new LinkedList();
        BufferedReader br = null;
        String sCurrentLine;
        
        try {
            
            File folder = new File(".\\Arquivos\\outputCodigo-Copia");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) 
            {
                File file = listOfFiles[i];
                if (file.isFile() && i != 0) 
                {
                    System.out.println(file.getName());
                    br = new BufferedReader(new FileReader(folder.getPath() + "\\" + file.getName()));
                    
                    while ((sCurrentLine = br.readLine()) != null) 
                    {
            //            String[] atributos = new String[6];
                        String[] spl = sCurrentLine.split("\t");
             //           atributos[0] = spl[0]; //noId
             //           atributos[1] = spl[1]; //posicaoX
             //           atributos[2] = spl[2]; //posicaoY
                        objetos.add(spl);
                        
                    }
                    System.out.println("Tamanho da rede = " + objetos.size());
              //      AtualizaLayoutComEsseArquivo(objetos);
                    objetos = new LinkedList();
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objetos;*/
}
