/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package layout;

import java.util.ArrayList;
/**
 *
 * @author Claudio Linhares
 */
public class CommunityNodeAttribute implements Comparable {

    private int weight, x_original, y_original, x_atual, y_atual,weightInTime , heightEdge , heightEdge_atual, idLouvain, idInfomap;
    private long id_original;
    private ArrayList<Long> times;
    private String rotulo;
    private String labelTela;
    
    
    public CommunityNodeAttribute(long id_original, long time, int x_original, int y_original, String label) {
        this.id_original = id_original;
        this.x_original = x_original;
        this.y_original = y_original;
        this.x_atual = x_original;
        this.y_atual = y_original;
        this.rotulo = label;
        this.times = new ArrayList();
        this.labelTela = "";
        times.add(time);
    }

    /**
     * @return the labelTela
     */
    public String getLabelTela() {
        return labelTela;
    }

    /**
     * @param labelTela the labelTela to set
     */
    public void setLabelTela(String labelTela) {
        this.labelTela = labelTela;
    }

    
    /**
     * @return the idLouvain
     */
    public int getIdLouvain() {
        return idLouvain;
    }

    /**
     * @param idLouvain the idLouvain to set
     */
    public void setIdLouvain(int idLouvain) {
        this.idLouvain = idLouvain;
    }

    /**
     * @return the idInfomap
     */
    public int getIdInfomap() {
        return idInfomap;
    }

    /**
     * @param idInfomap the idInfomap to set
     */
    public void setIdInfomap(int idInfomap) {
        this.idInfomap = idInfomap;
    }

    
    /**
     * @return the times
     */
    public ArrayList<Long> getTimes() {
        return times;
    }
    
    /**
     * @param time the time to set
     */
    public void setTimes(Long time) {
        this.times.add(time);
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
        return rotulo;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.rotulo = label;
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
        return this.labelTela;
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

}
