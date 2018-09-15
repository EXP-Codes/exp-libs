package exp.libs.algorithm.spa.test;

import java.util.List;

import exp.libs.algorithm.spa.Dijkstra;
import exp.libs.algorithm.struct.graph.TopoGraph;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.topo.TopoGraphUI;

public class TestDijkstra {

	public static void main(String[] args) {
		TopoGraph graph = toGraph();
		draw(graph);
		
		int B_ID = graph.getNode("B").getId();
		int J_ID = graph.getNode("J").getId();
		Dijkstra dijkstra = new Dijkstra(graph.getAdjacencyMatrix());
		dijkstra.calculate(B_ID);
		int BK_COST = dijkstra.getShortPathWeight(J_ID);
		List<Integer> BK_ROUTES = dijkstra.getShortPaths(J_ID);
		System.out.println(BK_COST);
		System.out.println(BK_ROUTES);
	}
	
	private static TopoGraph toGraph() {
		TopoGraph graph = new TopoGraph();
		graph.addEdge("A", "B", 2);
		graph.addEdge("B", "C", 2);
		graph.addEdge("C", "D", 1);
		graph.addEdge("D", "E", 1);
		graph.addEdge("E", "F", 2);
		graph.addEdge("C", "G", 2);
		graph.addEdge("G", "E", 2);
		graph.addEdge("C", "G", 3);
		graph.addEdge("G", "J", 1);
		graph.addEdge("J", "E", 2);
		graph.addEdge("G", "I", 3);
		graph.addEdge("I", "J", 2);
		graph.addEdge("I", "K", 2);
		graph.addEdge("B", "K", 3);
		graph.addEdge("K", "F", 3);
		return graph;
	}
	
	private static void draw(TopoGraph graph) {
		BeautyEyeUtils.init();
		TopoGraphUI ui = new TopoGraphUI("拓扑图展示器", 700, 300, graph);
		ui._view();
	}
	
}
