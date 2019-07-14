package exp.libs.algorithm.np.ispa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import exp.libs.algorithm.heuristic.qaca.QACA;
import exp.libs.algorithm.heuristic.qaca.QRst;
import exp.libs.algorithm.spa.Dijkstra;
import exp.libs.algorithm.struct.graph.Node;
import exp.libs.algorithm.struct.graph.TopoGraph;
import exp.libs.utils.other.StrUtils;

/**
 * <PRE>
 * TSP变种： 求解含必经点的最短路径问题(ISPA)
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-06-13
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class ISPA {

	private ISPA() {}
	
	/**
	 * 求解
	 * @param graph 拓扑图
	 * @return 求解结果
	 */
	public static ISPARst solve(final TopoGraph graph) {
		ISPARst rst = new ISPARst();
		if(graph == null || graph.isEmpty() || graph.isArrow() || // 暂不支持有向图
				graph.getSrc() == Node.NULL || graph.getSnk() == Node.NULL) {
			rst.setTips("拓扑图无效(图为空、或非无向图、或未指定源宿端)");
			return rst;
		}
		
		// 不存在必经点， 直接用Dijkstra算法求源端到宿端的最短路
		if(!graph.existInclusive()) {
			rst = solveBySPA(graph);
			
		// 必经点有序，使用Dijkstra算法按顺序分段求最短路， 然后拼接
		} else if(graph.isOrderInclusive()) {
			rst = solveBySegmentSPA(graph);
			
		// 必经点无序, 构造必经点子图后，使用启发式算法求最小连通通道
		} else {
			rst = solveByHeuristicAlgorithm(graph);
		}
		return rst;
	}
	
	/**
	 * 无必经点的最短路问题， 使用最短路算法求解
	 * @param graph 拓扑图
	 * @return 求解结果
	 */
	protected static ISPARst solveBySPA(final TopoGraph graph) {
		final Dijkstra dijkstra = new Dijkstra(graph.getAdjacencyMatrix());
		dijkstra.calculate(graph.getSrc().getId());
		int snkId = graph.getSnk().getId();
		int cost = dijkstra.getShortPathWeight(snkId);
		List<Integer> routes = dijkstra.getShortPaths(snkId);
		return _toISPARst(graph, cost, routes);
	}
	
	private static ISPARst _toISPARst(final TopoGraph graph, 
			final int cost, final List<Integer> routeIds) {
		ISPARst rst = new ISPARst();
		if(cost < Dijkstra.MAX_WEIGHT) {
			rst.setVaild(true);
			rst.setCost(cost);
			
			List<Node> routes = new LinkedList<Node>();
			for(int size = routeIds.size(), i = 0; i < size; i++) {
				routes.add(graph.getNode(routeIds.get(i)));
			}
			rst.setRoutes(routes);
			
		} else {
			rst.setTips("使用最短路算法求解失败: 源宿端不连通");
		}
		return rst;
	}
	
	/**
	 * 含有序必经点的最短路问题，使用分段最短路算法求解
	 * @param graph 拓扑图
	 * @return 求解结果
	 */
	protected static ISPARst solveBySegmentSPA(final TopoGraph graph) {
		List<Integer> includeIds = _getIncludeIds(graph);
		List<Integer> routeIds = _searchRouteIds(graph, includeIds);
		return _toISPARst(graph, routeIds);
	}
	
	private static List<Integer> _getIncludeIds(final TopoGraph graph) {
		List<Node> includes = graph.getIncludes();
		List<Integer> includeIds = new ArrayList<Integer>(includes.size() + 2);
		includeIds.add(graph.getSrc().getId());
		for(Node include : includes) {
			includeIds.add(include.getId());
		}
		includeIds.add(graph.getSnk().getId());
		return includeIds;
	}
	
	private static List<Integer> _searchRouteIds(
			final TopoGraph graph, final List<Integer> includeIds) {
		final Dijkstra dijkstra = new Dijkstra(graph.getAdjacencyMatrix());
		List<Integer> routeIds = new LinkedList<Integer>();
		routeIds.add(graph.getSrc().getId());
		for(int size = includeIds.size() - 1, i = 0; i < size; i++) {
			int srcId = includeIds.get(i);
			int snkId = includeIds.get(i + 1);
			List<Integer> segRoutes = __findSegRouteIds(
					dijkstra, srcId, snkId, includeIds, routeIds);
			segRoutes.remove(0);
			routeIds.addAll(segRoutes);
		}
		return routeIds;
	}
	
	private static List<Integer> __findSegRouteIds(final Dijkstra dijkstra, 
			final int srcId, final int snkId, 
			final List<Integer> includeIds, final List<Integer> curRouteIds) {
		Set<Integer> tabu = new HashSet<Integer>(); {
			tabu.addAll(includeIds);
			tabu.addAll(curRouteIds);
			tabu.remove(srcId);
			tabu.remove(snkId);
		}
		dijkstra.calculate(srcId, tabu);
		List<Integer> segRoutes = dijkstra.getShortPaths(snkId);
		if(segRoutes.isEmpty()) {
			segRoutes.add(srcId);
			segRoutes.add(Node.NULL.getId());
			segRoutes.add(snkId);
		}
		return segRoutes;
	}
	
	private static ISPARst _toISPARst(
			final TopoGraph graph, final List<Integer> routeIds) {
		int cost = 0;
		String tips = "";
		Set<Integer> repeats = new HashSet<Integer>();
		List<Node> routes = new LinkedList<Node>();
		routes.add(graph.getSrc());
		for(int i = 1; i < routeIds.size(); i++) {
			Node last = graph.getNode(routeIds.get(i - 1));
			Node cur = graph.getNode(routeIds.get(i));
			routes.add(cur);
			
			if(cur == Node.NULL) {
				Node next = graph.getNode(routeIds.get(i + 1));
				tips = StrUtils.concat(tips, "\r\n路径 [", last, "] -> [", next, "] 不连通.");
				
			} else {
				if(last != Node.NULL) {
					cost += graph.getWeight(last, cur);
				}
				
				if(!repeats.add(cur.getId())) {
					tips = StrUtils.concat(tips, "\r\n节点 [", cur, "] 被复用.");
				}
			}
		}
		
		boolean isVaild = true;
		if(StrUtils.isNotEmpty(tips)) {
			isVaild = false;
			tips = tips.replaceFirst("\r\n", "");
		}
		
		ISPARst rst = new ISPARst();
		rst.setVaild(isVaild);
		rst.setTips(tips);
		rst.setCost(cost);
		rst.setRoutes(routes);
		return rst;
	}
	
	/**
	 * 含无序必经点的最短路问题， 使用启发式算法求解
	 * @param graph 拓扑图
	 * @return 求解结果
	 */
	protected static ISPARst solveByHeuristicAlgorithm(final TopoGraph graph) {
		ISPARst rst = new ISPARst();
		final Dijkstra dijkstra = new Dijkstra(graph.getAdjacencyMatrix());
		
		TopoGraph subGraph = _compressGraph(graph, dijkstra);	// 压缩图
		if(subGraph.isEmpty()) {
			rst.setTips("使用启发式算法求解失败: 拓扑图不连通");
			
		} else if(subGraph != graph && // 当子图不是原图时， 对子图补边
				!_fillEdges(subGraph, graph, dijkstra)) {
			rst.setTips("使用启发式算法求解失败: 拓扑图不存在哈密顿通路");
			
		} else {
			// 求子图的哈密顿通路： 最坏的情况是过所有节点， 最好的情况是只过必经点
			QRst qRst = _toQACA(subGraph);
			
			// 当使用子图求哈密顿通路失败时，可能是构造子图时缺失边， 此时尝试用原图求解
			if(!qRst.isVaild()) {
				rst.setTips("所构造的子图不存在哈密顿通路, 切换原图做二次求解");
				subGraph.clear();
				subGraph = graph; // 目的是为了下面转换解操作时不会导致ID错位
				qRst = _toQACA(subGraph);
			}
			
			// 转换子图解为原图解（节点ID不同）
			_toISPARst(graph, rst, subGraph, qRst);
		}
		return rst;
	}
	
	/**
	 * 压缩图: 计算原图的必经点集（包括源宿点）中的任意两点的最短路, 合并所有相关路径, 得到压缩子图
	 * @param graph 原图
	 * @param dijkstra 根据原图构造的dijkstra算法对象
	 * @return 压缩后的子图（节点名称不变，但节点编号可能发生变化）
	 */
	private static TopoGraph _compressGraph(
			final TopoGraph graph, final Dijkstra dijkstra) {
		
		// 把源宿点作为必经点
		List<Node> includes = graph.getIncludes();
		includes.add(0, graph.getSrc());
		includes.add(includes.size(), graph.getSnk());
		
		// 若全图节点均为必经点， 无需构造子图
		if(includes.size() == graph.nodeSize()) {
			return graph;
		}
		
		// 计算两两必经点间的最短路，构造子图
		TopoGraph subGraph = new TopoGraph();
		for(int size = includes.size(), i = 0; i < size - 1; i++) {
			Node a = includes.get(i);
			dijkstra.calculate(a.getId());
			
			for(int j = i + 1; j < size; j++) {
				Node z = includes.get(j);
				List<Integer> azRoutes = dijkstra.getShortPaths(z.getId());
				if(!azRoutes.isEmpty()) {
					__addEdges(subGraph, graph, azRoutes);
					
				} else {
					subGraph.clear();
					break;
				}
			}
		}
		
		// 设置子图的源宿点和必经点
		if(!subGraph.isEmpty()) {
			subGraph.setSrc(graph.getSrc().getName());
			subGraph.setSnk(graph.getSnk().getName());
			subGraph.setIncludes(graph.getIncludeNames());
		}
		return subGraph;
	}
	
	/**
	 * 子图死路补边：
	 *  若存在非源宿、且度为1的节点X， 则先找到它的一个的邻居节点Y(度大于2, 可以是级联邻居), 
	 *    (若Y的度<2, 说明子图不连通, 但根据上下文这是不可能的; 
	 *     若Y的度=2, 说明X与Y同属一个死路支路， 则需要找下一个级联邻居Y)
	 *  对于Y的邻居节点集Zs（不包括X）， 依次求X不经过Y到达Z1、Z2、...Zn的最短路，
	 *  把最短的一条 X->Zk 添加到子图， 完成补边.
	 *  
	 * @param subGraph 子图
	 * @param graph 原图
	 * @param dijkstra 根据原图构造的dijkstra算法对象
	 * @return 是否补边成功 (根据上下文逻辑, X必定是一个必经点, 因此若存在任意一个X补边失败， 则此图必定无解)
	 */
	private static boolean _fillEdges(final TopoGraph subGraph, 
			final TopoGraph graph, final Dijkstra dijkstra) {
		boolean isOk = true;
		boolean exist = false;
		do {
			exist = false;
			Set<Node> subNodes = subGraph.getAllNodes(); // 重新获取补边后新的子图节点
			for(Node subNode : subNodes) {
				if(subNode.getDegree() < 1) {
					isOk = false;
					break;
					
				} else if(subNode.getDegree() > 1) {
					continue;
					
				} else if(subNode.getId() == subGraph.getSrc().getId() || 
						subNode.getId() == subGraph.getSnk().getId()) {
					continue;
				}
				
				exist = true;
				List<Integer> fillRoutes = __findFillRoutes(subNode, graph, dijkstra);
				if(fillRoutes == null) {
					isOk = false;
					
				} else {
					__addEdges(subGraph, graph, fillRoutes); // 补边
				}
				break; // 子图节点已变化，需要跳出循环
			}
		} while(isOk && exist);
		return isOk;
	}
	
	/**
	 * 获取子图死路节点级联到最近邻居的补边路径
	 * @param subNode 子图的一个死路节点
	 * @param graph 原图
	 * @param dijkstra
	 * @return
	 */
	private static List<Integer> __findFillRoutes(final Node subNode, 
			final TopoGraph graph, final Dijkstra dijkstra) {
		List<Integer> fillRoutes = null;
		Set<Integer> tabu = new HashSet<Integer>();
		Node lastNode = subNode;
		Node curNode = lastNode.getNeighborList().get(0);
		do {
			// 非连通图
			if(curNode.getDegree() <= 1) {
				break;
				
			} else {
				tabu.add(graph.getNode(curNode.getName()).getId());
				
				// 存在级联邻居
				if(curNode.getDegree() == 2) { 
					List<Node> neighbors = curNode.getNeighborList();
					Node nextNode = (lastNode.getId() == neighbors.get(0).getId() ? 
							neighbors.get(1) : neighbors.get(0));
					lastNode = curNode;
					curNode = nextNode;
					continue;
					
				// 找一条短的补边
				} else {
					int MIN_COST = Integer.MAX_VALUE;
					int aId = graph.getNode(subNode.getName()).getId();
					List<Node> neighbors = curNode.getNeighborList();
					for(Node z : neighbors) {
						int zId = graph.getNode(z.getName()).getId();
						dijkstra.calculate(aId, tabu);
						int cost = dijkstra.getShortPathWeight(zId);
						if(cost < MIN_COST) {
							MIN_COST = cost;
							fillRoutes = dijkstra.getShortPaths(zId);
						}
					}
					break;
				}
			}
		} while(true);
		return fillRoutes;
	}
	
	/**
	 * 把原图中某一段路由的相关边添加到子图中
	 * @param subGraph 子图
	 * @param graph 原图
	 * @param routes 原图中的一段路由
	 */
	private static void __addEdges(final TopoGraph subGraph, 
			final TopoGraph graph, final List<Integer> routes) {
		for(int size = routes.size(), i = 0; i < size - 1; i++) {
			Node src = graph.getNode(routes.get(i));
			Node snk = graph.getNode(routes.get(i + 1));
			int weight = graph.getWeight(src, snk);
			subGraph.addEdge(src.getName(), snk.getName(), weight);
		}
	}
	
	/**
	 * 使用量子蚁群算法求解
	 * @param graph
	 * @return
	 */
	private static QRst _toQACA(final TopoGraph graph) {
		final int GN_NUM = 10;	// 迭代代数
		int ANT_NUM = graph.nodeSize() / 20;	// 蚂蚁规模
		ANT_NUM = (ANT_NUM < 10 ? 10 : ANT_NUM);
		
		QACA qaca = new QACA(graph.getAdjacencyMatrix(), 
				graph.getSrc().getId(), 
				graph.getSnk().getId(), 
				graph.getIncludeIds(), ANT_NUM, GN_NUM, true);
		return qaca.exec();
	}
	
	/**
	 * 转换子图解为原图解
	 * @param graph 原图
	 * @param rst 原图解
	 * @param subGraph 子图
	 * @param subRst 子图解
	 * @return 原图解
	 */
	private static void _toISPARst(final TopoGraph graph, final ISPARst rst, 
			final TopoGraph subGraph, final QRst subRst) {
		if(subRst.isVaild()) {
			rst.setVaild(true);
			rst.setCost(subRst.getCost());
			
			List<Node> routes = new LinkedList<Node>();
			int[] subRoutes = subRst.getRoutes();
			for(int step = subRst.getStep(), i = 0; i < step; i++) {
				String name = subGraph.getNode(subRoutes[i]).getName();
				routes.add(graph.getNode(name));
			}
			
			if(routes.get(0).getId() == graph.getSnk().getId()) {
				Collections.reverse(routes);
			}
			rst.setRoutes(routes);
			
		} else {
			rst.setTips("使用启发式算法求解失败: 未能收敛到一个可行解");
		}
	}
	
}
