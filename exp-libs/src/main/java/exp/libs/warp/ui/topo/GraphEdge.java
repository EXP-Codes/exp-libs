package exp.libs.warp.ui.topo;

import org.eclipse.draw2d.graph.Edge;
import org.jgraph.graph.DefaultEdge;

public class GraphEdge {

	private GraphNode src;
	
	private GraphNode snk;
	
	private DefaultEdge cellEdge;
	
	private Edge gefEdge;
	
	protected GraphEdge(GraphNode src, GraphNode snk, int weight) {
		this.src = src;
		this.snk = snk;
		
		String edgeDesc = "(".concat(String.valueOf(weight)).concat(")");
		this.cellEdge = new DefaultEdge(edgeDesc);
		this.gefEdge = new Edge(src.getGefNode(), snk.getGefNode());
		this.gefEdge.weight = weight;
	}

	public GraphNode getSrc() {
		return src;
	}

	public GraphNode getSnk() {
		return snk;
	}

	public DefaultEdge getCellEdge() {
		return cellEdge;
	}

	public Edge getGefEdge() {
		return gefEdge;
	}
	
	public int getWeight() {
		return gefEdge.weight;
	}
	
}
