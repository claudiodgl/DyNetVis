/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layout;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Claudio Linhares
 */
public class CentralityNodeAttribute implements Comparable {

    /**
     * @return the globalRetweet
     */
    public long getGlobalRetweet() {
        return globalRetweet;
    }

    /**
     * @param globalRetweet the globalRetweet to set
     */
    public void setGlobalRetweet(long globalRetweet) {
        this.globalRetweet = globalRetweet;
    }

    /**
     * @return the degree
     */
    public double getDegree() {
        return degree;
    }

    /**
     * @param degree the degree to set
     */
    public void setDegree(double degree) {
        this.degree = degree;
    }

    /**
     * @return the closeness
     */
    public double getCloseness() {
        return closeness;
    }

    /**
     * @param closeness the closeness to set
     */
    public void setCloseness(double closeness) {
        this.closeness = closeness;
    }

    /**
     * @return the betweness
     */
    public double getBetweness() {
        return betweness;
    }

    /**
     * @param betweness the betweness to set
     */
    public void setBetweness(double betweness) {
        this.betweness = betweness;
    }

    /**
     * @return the vetorTopicosOcorrencias
     */
    public String getVetorTopicosOcorrencias() {
        return vetorTopicosOcorrencias;
    }

    /**
     * @param vetorTopicosOcorrencias the vetorTopicosOcorrencias to set
     */
    public void setVetorTopicosOcorrencias(String vetorTopicosOcorrencias) {
        this.vetorTopicosOcorrencias = vetorTopicosOcorrencias;
    }

    public CentralityNodeAttribute(int time, long id_original, int x_original, int y_original, String label, double closeness, double betweness, double degree, String vetorTopicosOcorrencias, double closenessGlobal, double betwenessGlobal, double degreeGlobal) {
        this.time = time;
        this.id_original = id_original;
        this.x_original = x_original;
        this.y_original = y_original;
        this.x_atual = x_original;
        this.y_atual = y_original;
        this.label = label;
        this.closeness = closeness;
        this.betweness = betweness;
        this.degree = degree;
        this.closenessGlobal = closenessGlobal;
        this.betwenessGlobal = betwenessGlobal;
        this.degreeGlobal = degreeGlobal;
        this.globalRetweet = 0;
        String topicos[] = vetorTopicosOcorrencias.substring(1, vetorTopicosOcorrencias.length()-1).split(",");
        
        String colorsTable[] = new String[7];
        colorsTable[0] = "000 000 255"; //blue Sports
        colorsTable[1] = "000 255 000"; //green Celebrity
        colorsTable[2] = "255 000 000"; //red Corruption
        colorsTable[3] = "255 185 60"; //orange Politics
        colorsTable[4] = "255 255 000"; //yellow Education
        colorsTable[5] = "255 000 255"; //pink Security
        colorsTable[6] = "100 070 000"; //brown International

                    
        for(int i = 0; i <topicos.length; i++)
        {
            this.topicosOcorrencias.put(colorsTable[i], Integer.parseInt(topicos[i]));
        }
        setPorcentagemMaiorOcorrenciaTopicos();
    }
    private double closeness, betweness, degree, porcentagemMaiorOcorrenciaTopicos, closenessGlobal, betwenessGlobal, degreeGlobal ;
    private int time, weight, x_original, y_original, x_atual, y_atual,weightInTime , heightEdge , heightEdge_atual;
    private long id_original, globalRetweet;
    private String label,vetorTopicosOcorrencias, topicoComMaiorOcorrencia;
    private HashMap<String,Integer> topicosOcorrencias = new HashMap<>();
    
    
    /**
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return the id_original
     */
    public long getId_original() {
        return id_original;
    }

    /**
     * @param id_original the id_original to set
     */
    public void setId_original(long id_original) {
        this.id_original = id_original;
    }

    /**
     * @return the x_original
     */
    public int getX_original() {
        return x_original;
    }

    /**
     * @param x_original the x_original to set
     */
    public void setX_original(int x_original) {
        this.x_original = x_original;
    }

    /**
     * @return the y_original
     */
    public int getY_original() {
        return y_original;
    }

    /**
     * @param y_original the y_original to set
     */
    public void setY_original(int y_original) {
        this.y_original = y_original;
    }

    /**
     * @return the x_atual
     */
    public int getX_atual() {
        return x_atual;
    }

    /**
     * @param x_atual the x_atual to set
     */
    public void setX_atual(int x_atual) {
        this.x_atual = x_atual;
    }

    /**
     * @return the y_atual
     */
    public int getY_atual() {
        return y_atual;
    }

    /**
     * @param y_atual the y_atual to set
     */
    public void setY_atual(int y_atual) {
        this.y_atual = y_atual;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    @Override
    public String toString() {
        return this.label;
    }
    
    @Override
    public int compareTo(Object o) {
        InlineNodeAttribute weight = (InlineNodeAttribute) o;
        if (this.weight > weight.getDegree()) {
             return 1;  
        }  
        if (this.weight < weight.getDegree()) {  
             return -1;  
        }  
        return 0; 
    }

    /**
     * @return the weightInTime
     */
    public int getWeightInTime() {
        return weightInTime;
    }

    /**
     * @param weightInTime the weightInTime to set
     */
    public void setWeightInTime(int weightInTime) {
        this.weightInTime = weightInTime;
    }

    /**
     * @return the heightEdge
     */
    public int getOriginalHeightEdge() {
        return heightEdge;
    }

    /**
     * @param heightEdge the heightEdge to set
     */
    public void setHeightEdge(int heightEdge) {
        this.heightEdge = heightEdge;
    }

    /**
     * @return the heightEdge_atual
     */
    public int getHeightEdge_atual() {
        return heightEdge_atual;
    }

    /**
     * @param heightEdge_atual the heightEdge_atual to set
     */
    public void setHeightEdge_atual(int heightEdge_atual) {
        this.heightEdge_atual = heightEdge_atual;
    }

    /**
     * @return the topicosOcorrencias
     */
    public HashMap<String,Integer> getTopicosOcorrencias() {
        return topicosOcorrencias;
    }

    /**
     * @param topicosOcorrencias the topicosOcorrencias to set
     */
    public void setTopicosOcorrencias(HashMap<String,Integer> topicosOcorrencias) {
        this.topicosOcorrencias = topicosOcorrencias;
    }

    /**
     * @return the porcentagemMaiorOcorrenciaTopicos
     */
    public double getPorcentagemMaiorOcorrenciaTopicos() {
       return this.porcentagemMaiorOcorrenciaTopicos;
        
    }

    /**
     * @param porcentagemMaiorOcorrenciaTopicos the porcentagemMaiorOcorrenciaTopicos to set
     */
    public void setPorcentagemMaiorOcorrenciaTopicos() {
        int totalOcorrencias = 0, maiorOcorrencia = 0;
        for(Entry<String,Integer> ocorrencia : getTopicosOcorrencias().entrySet())
        {
            totalOcorrencias += ocorrencia.getValue();
            if(ocorrencia.getValue() > maiorOcorrencia)
            {
                maiorOcorrencia = ocorrencia.getValue();
                setTopicoComMaiorOcorrencia(ocorrencia.getKey());
            }
        }

        this.porcentagemMaiorOcorrenciaTopicos = (double)maiorOcorrencia / (double)totalOcorrencias;
    }

    /**
     * @return the topicoComMaiorOcorrencia
     */
    public String getTopicoComMaiorOcorrencia() {
        return topicoComMaiorOcorrencia;
    }

    /**
     * @param topicoComMaiorOcorrencia the topicoComMaiorOcorrencia to set
     */
    public void setTopicoComMaiorOcorrencia(String topicoComMaiorOcorrencia) {
        this.topicoComMaiorOcorrencia = topicoComMaiorOcorrencia;
    }

    /**
     * @return the closenessGlobal
     */
    public double getClosenessGlobal() {
        return closenessGlobal;
    }

    /**
     * @param closenessGlobal the closenessGlobal to set
     */
    public void setClosenessGlobal(double closenessGlobal) {
        this.closenessGlobal = closenessGlobal;
    }

    /**
     * @return the betwenessGlobal
     */
    public double getBetwenessGlobal() {
        return betwenessGlobal;
    }

    /**
     * @param betwenessGlobal the betwenessGlobal to set
     */
    public void setBetwenessGlobal(double betwenessGlobal) {
        this.betwenessGlobal = betwenessGlobal;
    }

    /**
     * @return the degreeGlobal
     */
    public double getDegreeGlobal() {
        return degreeGlobal;
    }

    /**
     * @param degreeGlobal the degreeGlobal to set
     */
    public void setDegreeGlobal(double degreeGlobal) {
        this.degreeGlobal = degreeGlobal;
    }

}
