/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.siminfrastructure.planner.path;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.siminfrastructure.planner.TripPlannerException;
import cz.agents.agentpolis.siminfrastructure.planner.path.ShortestPathPlanner.ShortestPathPlannerFactory;
import cz.agents.agentpolis.siminfrastructure.planner.trip.VehicleTrip;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author fido
 */
@Singleton
public class ShortestPathPlanners {
    private final HashMap<Set<GraphType>, ShortestPathPlanner> shortestPathPlannersMappedByGraphTypes;
    
    private final ShortestPathPlannerFactory shortestPathPlannerFactory;

    @Inject
    public ShortestPathPlanners(ShortestPathPlannerFactory shortestPathPlannerFactory) {
        this.shortestPathPlannerFactory = shortestPathPlannerFactory;
        this.shortestPathPlannersMappedByGraphTypes = new HashMap<>();
    }
    
    public VehicleTrip findTrip(String vehicleId, int startNodeById, int destinationNodeById, Set<GraphType> graphTypes) 
            throws TripPlannerException{
        if(!shortestPathPlannersMappedByGraphTypes.containsKey(graphTypes)){
            createShortestPathPlanner(graphTypes);
        }
        
        ShortestPathPlanner planner = shortestPathPlannersMappedByGraphTypes.get(graphTypes);
        
        return planner.findTrip(vehicleId, startNodeById, destinationNodeById);
    }
    
    public ShortestPathPlanner getPathPlanner(Set<GraphType> graphTypes){
        if(!shortestPathPlannersMappedByGraphTypes.containsKey(graphTypes)){
            createShortestPathPlanner(graphTypes);
        }
        return shortestPathPlannersMappedByGraphTypes.get(graphTypes);
    }

    private void createShortestPathPlanner(Set<GraphType> graphTypes) {
        ShortestPathPlanner planner = shortestPathPlannerFactory.create(graphTypes);
        
        shortestPathPlannersMappedByGraphTypes.put(graphTypes, planner);
    }
    
}
