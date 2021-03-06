/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.agent;

import cz.agents.agentpolis.simmodel.entity.vehicle.Vehicle;

/**
 *
 * @author fido
 * @param <V>
 */
public interface Driver<V extends Vehicle> extends MovingAgent{
    public Vehicle getVehicle();
    
    public void startDriving(V vehicle);
    
    public void endDriving();
}
