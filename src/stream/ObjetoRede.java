/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stream;

import java.util.ArrayList;

/**
 *
 * @author MARK2
 * 
 * Operation	Type	NodeId	From	To	VType
add	Node	196017			Question
add	Node	3886			user
add	Edge		196017	3886	
add	Node	503093			Question
add	Node	44984			user
add	Edge		503093	44984	
add	Node	3088059			Question
add	Node	372516			user
add	Edge		3088059	372516	
 */

public class ObjetoRede {
    private String operation; //add or delete
    private String type; //node or edge
    private long nodeId;
    private long from; //if edge
    private long to; //if edge
    private String VType; //Campo para dizer, por ex, se o nó é um usuário ou uma questão
    private double posX; //Node
    private double posY; //Node
    private long timestep; //Em que instante houve a conexao
 
    public ObjetoRede(String op, String type, long nodeId, long from, long to, String vType, long timestep)
    {
        this.operation = op;
        this.type = type;
        this.nodeId = nodeId;
        this.from = from;
        this.to = to;
        this.VType = vType;
        this.posX = -1;
        this.posY = -1;
        this.timestep = timestep;
    }
    
    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param operation the operation to set
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the nodeId
     */
    public long getNodeId() {
        return nodeId;
    }

    /**
     * @param nodeId the nodeId to set
     */
    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * @return the from
     */
    public long getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(long from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public long getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(long to) {
        this.to = to;
    }

    /**
     * @return the VType
     */
    public String getVType() {
        return VType;
    }

    /**
     * @param VType the VType to set
     */
    public void setVType(String VType) {
        this.VType = VType;
    }

    /**
     * @return the posX
     */
    public double getPosX() {
        return posX;
    }

    /**
     * @param posX the posX to set
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * @return the posY
     */
    public double getPosY() {
        return posY;
    }

    /**
     * @param posY the posY to set
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }
    
    
   // public void AtualizaLayout(ArrayList<ObjetoRede> rede)
   // {
  //      AtualizaLayoutIncremental(rede);
  //  }

    /**
     * @return the timestep
     */
    public long getTimestep() {
        return timestep;
    }

    /**
     * @param timestep the timestep to set
     */
    public void setTimestep(long timestep) {
        this.timestep = timestep;
    }
    
}
