package networkmodeling.core.modelgraph;

import java.io.Serializable;

public class NetworkGraphEdge implements Serializable {
    
    public NetworkGraphEdge(NetworkGraphNode first, NetworkGraphNode second)
    {
        this.firstNode = first;
        this.secondNode = second;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof NetworkGraphEdge))
            return false;
        
        NetworkGraphEdge otherEdge = (NetworkGraphEdge)o;
        
        return (otherEdge.getFirstNode().equals(firstNode) &&
                otherEdge.getSecondNode().equals(secondNode)) ||
                (otherEdge.getFirstNode().equals(secondNode) &&
                otherEdge.getSecondNode().equals(firstNode));
    }
    
    public boolean ContainsNode(NetworkGraphNode node)
    {
        return node.equals(firstNode) || node.equals(secondNode);
    }
    
    public NetworkGraphNode getFirstNode() {
        return firstNode;
    }

    public NetworkGraphNode getSecondNode() {
        return secondNode;
    }
    
    private NetworkGraphNode firstNode;
    private NetworkGraphNode secondNode;
}