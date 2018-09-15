package exp.libs.algorithm.heuristic.qaca.test;

import exp.libs.algorithm.heuristic.qaca.QACA;
import exp.libs.algorithm.struct.graph.TopoGraph;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.topo.TopoGraphUI;

public class TestQACA {

	public static void main(String[] args) {
		TopoGraph graph = toGraph();
		draw(graph);
		
		final int ANT_NUM = 10;	// 蚂蚁数量
		final int GN_NUM = 10; // 蚂蚁代数
		QACA qaca = new QACA(graph.getAdjacencyMatrix(), 
				graph.getSrc().getId(), graph.getSnk().getId(), 
				graph.getIncludeIds(), ANT_NUM, GN_NUM, true);
		qaca.exec();
		System.out.println(qaca.getBestRstInfo());
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
		graph.setInclude("K");
//		graph.setInclude("D");
		return graph;
	}
	
	private static void draw(TopoGraph graph) {
		BeautyEyeUtils.init();
		TopoGraphUI ui = new TopoGraphUI("拓扑图展示器", 700, 300, graph);
		ui._view();
	}
	
}
