package exp.libs.algorithm.np.ispa.test;

import exp.libs.algorithm.np.ispa.ISPA;
import exp.libs.algorithm.np.ispa.ISPARst;
import exp.libs.algorithm.struct.graph.TopoGraph;
import exp.libs.warp.ui.BeautyEyeUtils;
import exp.libs.warp.ui.topo.TopoGraphUI;

public class TestISPA {

	public static void main(String[] args) {
		testNoneInclusive();
		testOrderInclusive();
		testExistInclusive();
	}
	
	private static void testNoneInclusive() {
		TopoGraph graph = toGraph(false);
		draw(graph, "无必经点");
		ISPARst rst = ISPA.solve(graph);
		System.out.println("无必经点（使用最短路算法求解）：");
		System.out.println(rst.toString());
		System.out.println("========================\r\n");
	}
	
	private static void testOrderInclusive() {
		TopoGraph graph = toGraph(true);
		graph.setInclude("C");
		graph.setInclude("K");
		draw(graph, "必经点有序");
		ISPARst rst = ISPA.solve(graph);
		System.out.println("必经点有序（使用分段最短路算法求解）：");
		System.out.println(rst.toString());
		System.out.println("========================\r\n");
	}
	
	private static void testExistInclusive() {
		TopoGraph graph = toGraph(false);
		graph.setInclude("C");
		graph.setInclude("K");
		draw(graph, "必经点无序");
		ISPARst rst = ISPA.solve(graph);
		System.out.println("必经点无序（使用启发式算法求解）：");
		System.out.println(rst.toString());
		System.out.println("========================\r\n");
	}
	
	private static TopoGraph toGraph(boolean order) {
		TopoGraph graph = new TopoGraph(order);
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
		return graph;
	}
	
	private static void draw(TopoGraph graph, String title) {
		BeautyEyeUtils.init();
		TopoGraphUI ui = new TopoGraphUI(title, 700, 300, graph);
		ui._view();
	}
	
}
