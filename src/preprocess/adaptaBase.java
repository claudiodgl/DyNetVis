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
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jean
 */
public class adaptaBase {
    
    static String claudio = ""; //D:\\Claudio\\Dropbox\\Artigo Claudio e Jean\\Bases\\Fabiola Brazilian Twitter";
    
    static String jean = ""; //D:\\Dropbox\\UFU\\Doutorado\\Artigo Claudio e Jean\\Bases\\Fabiola Brazilian Twitter";
    static int resolucao = 1;
    public static void main(String args[]) {
         ArrayList bd = leBaseOriginal();
         System.out.println("Base lida.");
         Map<String,Integer> mapeamentoUsuarios = leIdsUsuariosOriginais();
         System.out.println("Ids dos usuários mapeados.");
         ArrayList bdAdaptado = new ArrayList();
      
        try {
            
            int tempo = 0;
            Collections.sort(bd);
            
            Instancia a = (Instancia) bd.get(0);
            Date tempo_salvo = a.getTime();
            Date tempo_novo;
            for(Object conexao : bd)
            {
                Instancia inst = (Instancia) conexao;
                Instancia nova_inst = new Instancia();
                nova_inst.setUser1(mapeamentoUsuarios.get(inst.getUser1()) + "");
                nova_inst.setUser2(mapeamentoUsuarios.get(inst.getUser2()) + "");

                tempo_novo = inst.getTime();
                if(!tempo_salvo.equals(tempo_novo))
                {
                    long secs = Math.abs(tempo_salvo.getTime() - tempo_novo.getTime()) / 1000;
                    int hours = (int) (secs / 3600);    
                    secs = secs % 3600;
                    int mins = (int) (secs / 60);
                    secs = secs % 60;
                    tempo = tempo + hours;
                    if(hours != 1)
                        System.out.println("HORA DIFERENTE");
                    tempo_salvo = tempo_novo;
                }
                nova_inst.setTempoDyNetVis(tempo);
                nova_inst.setTopic(inst.getTopic());
                nova_inst.setTime(inst.getTime());
                bdAdaptado.add(nova_inst);

            }
            
            
            System.out.println("Ordenação finalizada. O arquivo será gerado agora.");
        //    CriaArquivoBD(bdAdaptado); 
        //    CriaArquivoConversaoID(mapeamentoUsuarios); 
            CriaArquivoDataHora(bdAdaptado);
            System.out.println("Arquivo gerado. Fim.");
            
            
            
            
            
            /*menorDiaDaBase = formataData("25/08/2015");
            maiorDia = formataData("16/12/2015");

            while(menorDiaDaBase.before(maiorDia))
            {
                String intervalo = convertTime(menorDiaDaBase);
                menorDiaDaBase = removeTime(addDays(menorDiaDaBase, resolucao));
                if(resolucao != 1)
                    intervalo += ";" + convertTime(menorDiaDaBase);
                datasConsideradas.add(intervalo);

            }

             for(Object conexao : bd)
             {
                 Instancia inst = (Instancia) conexao;
           //      String diaIncr = convertTime(inst.getStartTime());
            //     diaIncremental = formataData(diaIncr);
            //     while(diaIncremental.before(inst.getEndTime()))
                 {
               //      if(datasConsideradas.indexOf(diaIncremental) == -1)
               //          System.out.println("bla");
                 //    {
                        Instancia novaConexao = new Instancia();
                //        novaConexao.setUser1(usuariosOriginais.indexOf(inst.getUser1()) + "");
                //        novaConexao.setUser2(usuariosOriginais.indexOf(inst.getUser2()) + "");
                        novaConexao.setUser1(mapeamentoUsuarios.get(inst.getUser1()) + "");
                        novaConexao.setUser2(mapeamentoUsuarios.get(inst.getUser2()) + "");
                        //novaConexao.setUser2(inst.getUser2());
               //         novaConexao.setStartTime(inst.getStartTime());
                //        novaConexao.setEndTime(inst.getEndTime());
                if(resolucao == 1)
                //        novaConexao.setTempoDyNetVis(datasConsideradas.indexOf(convertTime(diaIncremental)));
             //   else
                {
                    for(int i = 0; i < datasConsideradas.size(); i++)
                    {
                        String[] intervalo = ((String)datasConsideradas.get(i)).split(";");
                        Date dataInicialIntervalo = formataData(intervalo[0]);
                        Date dataFinalIntervalo = formataData(intervalo[1]);
                 //       if(diaIncremental.compareTo(dataInicialIntervalo) >= 0 && diaIncremental.compareTo(dataFinalIntervalo) <= 0)
                        {
                            novaConexao.setTempoDyNetVis(i);
                            break;
                        }
                    }
                }
                  //      if(datasConsideradas.indexOf(diaIncremental) == -1)
                  //          System.out.println("bla");
               //         diaIncremental = removeTime(addDays(diaIncremental, resolucao));
                      //  diaIncremental = formataData(convertTime(addDays(diaIncremental, 1)));
                       // diaIncremental = addDays(diaIncremental, 1);
                       if(!bdAdaptado.contains(novaConexao)) 
                            bdAdaptado.add(novaConexao);
                 }
             }
             System.out.println("Gerou o bd adaptado. Vai ordená-lo agora.");
            Collections.sort(bdAdaptado, new Comparator<Instancia>() {
            @Override
            public int compare(Instancia a, Instancia b)
            {
                   return Integer.compare(a.getTempoDyNetVis(), b.getTempoDyNetVis());
            }
             });
            */
             
            
        } catch (Exception ex) {
            Logger.getLogger(adaptaBase.class.getName()).log(Level.SEVERE, null, ex);
        }
            
}
    
   private static ArrayList leBaseOriginal() {
        ArrayList objetos = new ArrayList();
        BufferedReader br = null;
        String sCurrentLine;
        try {
            //br = new BufferedReader(new FileReader("E:\\Dropbox\\UFU\\Doutorado\\Pesquisa\\Implementacao\\HelloWorld Claudio\\JavaApplication1\\BaseMOA.txt"));
            br = new BufferedReader(new FileReader(claudio+"\\edges.txt"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        try {
            while ((sCurrentLine = br.readLine()) != null) {
                Instancia inst = new Instancia();
                String[] spl = sCurrentLine.split(",");
                if(spl.length == 8)
                {
                    inst.setUser1(spl[1]); //Inverte as colunas pois a primeira coluna da fabiola é o target
                    inst.setUser2(spl[0]);
                    
                    String timestamp = spl[5].replace("\"<[", "").trim();
                 //   String ble = spl[6].replace("]>\"", "").trim();
                 // Foi validado que o timeset possui os dois tempos iguais.
                 //   if(!timestamp.equals(ble))
                 //       System.out.println("Diferente!");
                    inst.setTime(convertLongToDate(Long.parseLong(timestamp)));
                    inst.setTempoDyNetVis(-1);
                    inst.setTopic(spl[7]);
                    objetos.add(inst);
                }
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
			File file = new File(claudio+"\\edges_properties.txt");
			file.createNewFile();
		//    	   gravarCabecalho = true;
		    	
			//Here true is to append the content to file
	    	FileWriter fw = new FileWriter(file,false);
	    	//BufferedWriter writer give better performance
	    	BufferedWriter bw = new BufferedWriter(fw);
	    //	if(gravarCabecalho)
	    //		bw.write(cabecalho);
            for(Object conexao : bd)
            {
                Instancia inst = (Instancia) conexao;
          //      bw.write(inst.getUser1() + " " + inst.getUser2() + " "  + convertTime(inst.getStartTime()) + " " + convertTime(inst.getEndTime()) +  " " + inst.getTempoDyNetVis() + System.getProperty("line.separator"));
                bw.write(inst.getUser1() + " " + inst.getUser2() + " " + inst.getTempoDyNetVis() + " "+ inst.getTopic() +  System.getProperty("line.separator"));
            }
	   // 	bw.write(conteudo);
	    	//Closing BufferedWriter Stream
	    	bw.close();
		}
		catch(IOException ioe)
		{
         System.out.println("Exception occurred:");
    	 ioe.printStackTrace();
       }
	
     }
     
     private static void CriaArquivoConversaoID(Map<String,Integer> mapeamento) throws IOException
     {
        File file = new File(claudio+"\\BD_Fabiola_IDs.dat");
        file.createNewFile();
        
        FileWriter fw = new FileWriter(file,false);
        BufferedWriter bw = new BufferedWriter(fw);
        
        for (Map.Entry<String, Integer> entry : mapeamento.entrySet()) {
                //System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                bw.write(entry.getKey()+ ";" + entry.getValue() + System.getProperty("line.separator"));
               
        }
         bw.close();

        
     }
     
     private static void CriaArquivoDataHora(ArrayList bd)
     {
		//boolean gravarCabecalho = false;
		try
		{
			File file = new File(jean+"\\data_hora.txt");
			file.createNewFile();
		//    	   gravarCabecalho = true;
		    	
			//Here true is to append the content to file
	    	FileWriter fw = new FileWriter(file,false);
	    	//BufferedWriter writer give better performance
	    	BufferedWriter bw = new BufferedWriter(fw);
	    //	if(gravarCabecalho)
	    //		bw.write(cabecalho);
            for(Object conexao : bd)
            {
                Instancia inst = (Instancia) conexao;
          //      bw.write(inst.getUser1() + " " + inst.getUser2() + " "  + convertTime(inst.getStartTime()) + " " + convertTime(inst.getEndTime()) +  " " + inst.getTempoDyNetVis() + System.getProperty("line.separator"));
     //     String current = SimpleDateFormat("dd/MM/yyyy HH:mm").format(inst.getTime()).toString();    
          bw.write(inst.getTempoDyNetVis() + " " + convertTime2(inst.getTime()) +  System.getProperty("line.separator"));
            }
	   // 	bw.write(conteudo);
	    	//Closing BufferedWriter Stream
	    	bw.close();
		}
		catch(IOException ioe)
		{
         System.out.println("Exception occurred:");
    	 ioe.printStackTrace();
       }
	
     }
     
     private static Map<String,Integer> leIdsUsuariosOriginais() {
        ArrayList usuariosOriginaisLong = new ArrayList();
        Map<String,Integer> usuariosOriginaisXDyNetVis = new HashMap<>();
        BufferedReader br = null;
        String sCurrentLine;

        try {
            br = new BufferedReader(new FileReader(claudio+"\\nodes.txt"));
        
            int i = 1;
            while ((sCurrentLine = br.readLine()) != null) {
                
                String[] spl = sCurrentLine.split(",");
                if(spl.length == 3)
                {
                    if(!usuariosOriginaisXDyNetVis.containsKey(spl[0]))
                    {
                        usuariosOriginaisXDyNetVis.put(spl[0], i);
                        i++;
                    }
              //      usuariosOriginaisLong.add(spl[0]);
                }
            }
            return usuariosOriginaisXDyNetVis;

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
        return null;
    }
     
    public static String convertTime(long time){
    Date date = new Date(time);
    Format format = new SimpleDateFormat("dd/MM/yyyy");
    return format.format(date);
    }
     public static String convertTime(Date date){
    Format format = new SimpleDateFormat("dd/MM/yyyy");
    return format.format(date);
    }
      public static String convertTime2(Date date){
    Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    return format.format(date);
    }
    
    public static long getDateDiff(long diffInMillies, TimeUnit timeUnit) {
    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
}
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
    long diffInMillies = date2.getTime() - date1.getTime();
    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
}
    
    public static Date formataData(String data) throws Exception { 
		if (data == null || data.equals(""))
			return null;
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH");
          //  DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH");
            date = (java.util.Date)formatter.parse(data);
            return date;
        } catch (ParseException e) {            
            
        }
        return null;
	}
    
    public static Date convertLongToDate(long time){
    Date date = new Date(time);
    //Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Format format = new SimpleDateFormat("dd/MM/yyyy HH");
        try {
            return formataData(format.format(date));
        } catch (Exception ex) {
            Logger.getLogger(adaptaBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
    
    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}

