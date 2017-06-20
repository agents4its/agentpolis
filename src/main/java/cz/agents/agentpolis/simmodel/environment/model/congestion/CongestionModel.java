/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.agents.agentpolis.simmodel.environment.model.congestion;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cz.agents.agentpolis.agentpolis.config.Config;
import cz.agents.agentpolis.siminfrastructure.planner.trip.Trip;
import cz.agents.agentpolis.siminfrastructure.time.TimeProvider;
import cz.agents.agentpolis.simmodel.entity.vehicle.PhysicalVehicle;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.EGraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks.TransportNetworks;
import cz.agents.agentpolis.simulator.SimulationProvider;
import cz.agents.basestructures.Graph;
import java.security.ProviderException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fido
 */
@Singleton
public class CongestionModel {
    private final Graph<SimulationNode, SimulationEdge> graph;
    
    protected final Map<SimulationNode,Connection> connectionsMappedByNodes;
	
	private final List<Link> links;
    
    protected final Map<SimulationEdge,Link> linksMappedByEdges;
    
    private final Config config;
    
    private final SimulationProvider simulationProvider;
    
    private final TimeProvider timeProvider;

    @Inject
    public CongestionModel(TransportNetworks transportNetworks, Config config, 
            SimulationProvider simulationProvider, TimeProvider timeProvider) throws ModelConstructionFailedException, 
            ProviderException {
        this.graph = transportNetworks.getGraph(EGraphType.HIGHWAY);
        this.config = config;
        this.simulationProvider = simulationProvider;
        this.timeProvider = timeProvider;
        connectionsMappedByNodes = new HashMap<>();
        linksMappedByEdges = new HashMap<>();
		links = new LinkedList<>();
        buildCongestionGraph();
    }

    private void buildCongestionGraph() throws ModelConstructionFailedException {
        buildConnections(graph.getAllNodes());
        buildLinks(graph.getAllEdges());
		buildLanes();
    }
    
    public void drive(PhysicalVehicle vehicle, Trip<SimulationNode> trip){
        VehicleTripData vehicleData = new VehicleTripData(vehicle, trip);
        SimulationNode startLocation = trip.getAndRemoveFirstLocation();
        Connection startConnection = connectionsMappedByNodes.get(startLocation);
        startConnection.startDriving(vehicleData);        
    }

    private void buildConnections(Collection<SimulationNode> allNodes) {
        for (SimulationNode node : allNodes) {
			if(graph.getInEdges(node).size() > 2){
				Crossroad crossroad = new Crossroad(config, simulationProvider, this);
				connectionsMappedByNodes.put(node, crossroad);
			}
			else{
				Connection connection = new Connection(simulationProvider, config, this);
				connectionsMappedByNodes.put(node, connection);
			}
        }
    }

    private void buildLinks(Collection<SimulationEdge> allEdges) {
        for (SimulationEdge edge : allEdges) {
			Link link = new Link(edge);
			links.add(link);
            linksMappedByEdges.put(edge, link);
		}
    }

	private void buildLanes() throws ModelConstructionFailedException {
		for (Link link : links) {
			SimulationNode targetNode = graph.getNode(link.getEdge().toId);
			List<SimulationEdge> outEdges = graph.getOutEdges(targetNode);
            if(outEdges.isEmpty()){
                throw new ModelConstructionFailedException("Dead end detected - this is prohibited in road graph");
            }
            Connection targetConnection = connectionsMappedByNodes.get(targetNode);
            for (SimulationEdge outEdge : outEdges) {
                SimulationNode nextNode = graph.getNode(outEdge.toId);
                Lane newLane = new Lane(link, link.getLength(), timeProvider);
                link.addLane(newLane, nextNode);
                Link outLink = linksMappedByEdges.get(outEdge);
                if(targetConnection instanceof Crossroad){
                    ((Crossroad) targetConnection).addNextLink(outLink, newLane, connectionsMappedByNodes.get(nextNode));
                }
                else{
                    targetConnection.setOutLink(outLink);
                }
            }
		}
	}
    
    
}