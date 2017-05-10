/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.entity.vehicle;

import cz.agents.agentpolis.simmodel.agent.TransportEntity;
import cz.agents.agentpolis.simmodel.entity.TransportableEntity;
import cz.agents.basestructures.Node;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author fido
 */
public abstract class PersonalVehicle extends Vehicle implements TransportEntity{
    
    protected final List<TransportableEntity> transportedPersons;
    
    public void pickUp(TransportableEntity person){
        transportedPersons.add(person);
        person.setTransportingEntity(this);
    }
    
    public void dropOff(TransportableEntity person){
        transportedPersons.remove(person);
        person.setTransportingEntity(null);
    }
    
    public PersonalVehicle(String id, Node position) {
        super(id, position);
        transportedPersons = new LinkedList<>();
    }
    
    
    @Override
    public List<TransportableEntity> getTransportedEntities() {
        return transportedPersons;
    }
    
    public void dropOffAll() {
        Iterator<TransportableEntity> transportedPersonsIterator = transportedPersons.iterator();
        while(transportedPersonsIterator.hasNext()){
            TransportableEntity person = transportedPersonsIterator.next();
            transportedPersonsIterator.remove();
            person.setTransportingEntity(null);
        }
    }
    
}
