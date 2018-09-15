package exp.libs.warp.ui.test;

import exp.libs.algorithm.struct.graph.TopoGraph;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.topo.TopoGraphUI;

public class TestTopoGraphUI {

	public static void main(String[] args) {
		TopoGraph graph = toGraph2();
		
		BeautyEyeUtils.init();
		TopoGraphUI ui = new TopoGraphUI("拓扑图展示器", 700, 500, graph);
		ui._view();
	}
	
	private static TopoGraph toGraph2() {
		TopoGraph graph = new TopoGraph();
		graph.setSrc("A"); 
		graph.setSnk("G");
		graph.addEdge("A", "B", 2);
		graph.addEdge("A", "C", 3);
		graph.addEdge("A", "D", 4);
		graph.addEdge("A", "E", 5);
		graph.addEdge("A", "F", 6);
		graph.addEdge("A", "G", 1000);
		
		graph.addEdge("E", "X", 4);
		graph.addEdge("E", "Y", 4);
		graph.addEdge("E", "Z", 4);
		
		graph.addEdge("F", "O", 4);
		return graph;
	}
	
	private static TopoGraph toGraph() {
		TopoGraph graph = new TopoGraph();
		graph.setSrc("A"); 
		graph.setSnk("F");
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
		
		graph.setInclude("C");
		graph.setInclude("G");
		graph.setInclude("K");
		graph.setInclude("I");
		return graph;
	}
	
}
