package cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.networks;

import com.google.inject.Singleton;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.GraphType;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationEdge;
import cz.agents.agentpolis.simmodel.environment.model.citymodel.transportnetwork.elements.SimulationNode;
import cz.agents.basestructures.Graph;

import java.util.Map;

/**
 * 
 * The wrapper for all initialized graph in a simulation model
 * 
 * @author Zbynek Moler
 * 
 */
@Singleton
public class TransportNetworks {

    private final Map<GraphType, Graph<SimulationNode, SimulationEdge>> transportNetworks;

    public TransportNetworks(Map<GraphType, Graph<SimulationNode, SimulationEdge>> transportNetworks) {
        super();
        this.transportNetworks = transportNetworks;
    }

    public Map<GraphType, Graph<SimulationNode, SimulationEdge>> getGraphsByType() {
        return transportNetworks;
    }

    public Graph<SimulationNode, SimulationEdge> getGraph(GraphType graphType) {
        return transportNetworks.get(graphType);
    }
}
