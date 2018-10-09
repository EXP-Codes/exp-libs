package exp.libs.warp.ui.topo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

import com.realpersist.gef.layout.NodeJoiningDirectedGraphLayout;

import exp.libs.algorithm.struct.graph.Edge;
import exp.libs.algorithm.struct.graph.Node;
import exp.libs.algorithm.struct.graph.TopoGraph;
import exp.libs.utils.num.NumUtils;
import exp.libs.utils.other.RandomUtils;
import exp.libs.warp.ui.SwingUtils;
import exp.libs.warp.ui.cpt.win.PopChildWindow;

/**
 * <PRE>
 * 拓扑图绘制器
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2015-12-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TopoGraphUI extends PopChildWindow {

	/** serialVersionUID */
	private static final long serialVersionUID = -8326111607034563163L;

	/** 建议最大的节点数（超过这个数量时GEF演算速度会极慢, 导致陷入长时间无法生成拓扑图的假死状态） */
	private final static int MAX_NODE_NUM = 32;
	
	/** 所绘制拓扑图的边长放大倍率 */
	private final static int MANIFICATION = 150;
	
	/** 归一化公式常量 */
	private final static double NORM_ARG = 2 / Math.PI * MANIFICATION;
	
	/** 源端颜色 */
	private final static Color COLOR_SRC = Color.BLUE;
	
	/** 宿端颜色 */
	private final static Color COLOR_SNK = Color.MAGENTA;
	
	/** 必经点颜色 */
	private final static Color COLOR_INCLUSIVE = Color.ORANGE;
	
	/** 普通节点颜色 */
	private final static Color COLOR_NORMAL = Color.GREEN;
	
	/** 拓扑图数据 */
	private TopoGraph topoGraph;
	
	/** 拓扑图展示模型 */
	private GraphModel viewGraphModel;
	
	/** 拓扑图绘制组件 */
	private JGraph jGraph;
	
	/** 是否使用GEF组件计算节点坐标 */
	private boolean useGEF;
	
	public TopoGraphUI(String name, int width, int height, TopoGraph topoGraph) {
		super(name, width, height, false, topoGraph);
	}

	@Override
	protected void initComponents(Object... args) {
		if(args != null && args.length == 1) {
			this.topoGraph = (TopoGraph) args[0];
		} else {
			this.topoGraph = new TopoGraph();
		}
		
		this.useGEF = (topoGraph.nodeSize() <= MAX_NODE_NUM);
		this.viewGraphModel = new DefaultGraphModel();
		this.jGraph = new JGraph(viewGraphModel);
		this.jGraph.setJumpToDefaultPort(true);
		this.jGraph.setSelectionEnabled(true);
	}

	@Override
	protected void setComponentsLayout(JPanel root) {
		paintGraph(topoGraph);	// 绘图
		
		JPanel tips = new JPanel(new FlowLayout()); {
			JLabel srcTips = new JLabel("[ ■ : 源端 ]");
			srcTips.setForeground(COLOR_SRC);
			tips.add(srcTips);
			
			JLabel snkTips = new JLabel("[ ■ : 宿端 ]");
			snkTips.setForeground(COLOR_SNK);
			tips.add(snkTips);
			
			JLabel inclusiveTips = new JLabel("[ ■ : 必经节点 ]");
			inclusiveTips.setForeground(COLOR_INCLUSIVE);
			tips.add(inclusiveTips);
			
			JLabel normalTips = new JLabel("[ ■ : 普通节点 ]");
			normalTips.setForeground(COLOR_NORMAL);
			tips.add(normalTips);
		}
		rootPanel.add(tips, BorderLayout.NORTH);
		rootPanel.add(SwingUtils.addAutoScroll(jGraph), BorderLayout.CENTER);
	}

	@Override
	protected void setComponentsListener(JPanel root) {}
	
	@Override
	protected void AfterView() {}

	@Override
	protected void beforeHide() {}
	
	/**
	 * 绘图
	 * @param topoGraph 拓扑图数据
	 */
	private void paintGraph(TopoGraph topoGraph) {
		if(topoGraph == null || topoGraph.isEmpty()) {
			SwingUtils.warn("输入的拓扑图为空, 无法演算.");
			return;
			
		} else if(topoGraph.nodeSize() > MAX_NODE_NUM) {
			if(!SwingUtils.confirm("输入的拓扑图规模过大(NODE=" + topoGraph.nodeSize() 
					+ "), 可能导致演算过程假死, 是否继续?")) {
				return;
			}
		}
		
		// 计算节点坐标
		List<GraphEdge> viewEgdes = !useGEF ? 
				calculatePositionByPolar(topoGraph) : 	// 使用极坐标反算直角坐标
				calculatePositionByGEF(topoGraph);		// 使用GEF组件计算直角坐标
		createViewModel(viewEgdes, topoGraph.isArrow());	// 绘制视图
	}
	
	/**
	 * 通过拓扑图的节点间关系在极坐标系构造相对位置， 再使用极坐标反算直角坐标
	 * @param topoGraph
	 * @return 用于实际呈现的拓扑图边集（每条边的源宿节点具有实际的XY坐标值）
	 */
	private List<GraphEdge> calculatePositionByPolar(TopoGraph topoGraph) {
		int size = topoGraph.nodeSize();// 拓扑图规模
		Node src = topoGraph.getSrc();	// 拓扑图源点
		
		GraphNode cellEdgeSrc = new GraphNode(src.getAliasName(), 0, 0);// 绘制图原点
		List<GraphEdge> viewEdges = new LinkedList<GraphEdge>();	// 绘制图边集
		Map<String, GraphNode> viewNodeMap = new HashMap<String, GraphNode>(); { // 绘制图节点集
			viewNodeMap.put(src.getName(), cellEdgeSrc);
			cellEdgeSrc.markGraphSrc();	// 标记为绘制源点
		}
		
		// 已访问节点的标识数组
		boolean[] visit = new boolean[size]; {
			Arrays.fill(visit, false);
			visit[src.getId()] = true;
		}
		
		// 待访问节点的队列
		List<Node> queue = new ArrayList<Node>(size); {
			queue.add(src);
		}
		
		// BFS遍历[拓扑图]
		for(int idx = 0; idx < size; idx++) {
			if(idx >= queue.size()) {
				SwingUtils.warn("输入的拓扑图不是连通图， 部分节点无法绘制");
				break;
			}
			
			Node edgeSrc = queue.get(idx);
			List<Node> edgeSnks = edgeSrc.getNeighborList();
			
			// 以edgeSrc为圆心，根据邻接点数目，对圆周角进行等分, 计算每一等分相对于极轴的角度
			int split = (idx == 0 ? edgeSnks.size() : edgeSnks.size() - 1);	// 父节点不算在内
			final double[] thetas = _subMultipleAngle(split);
			
			// 遍历邻接点
			GraphNode viewEdgeSrc = viewNodeMap.get(edgeSrc.getName());
			for(int i = 0, j = 0; i < edgeSnks.size(); i++) {
				Node egdeSnk = edgeSnks.get(i);
				if(visit[egdeSnk.getId()]) {
					continue;
				}
				queue.add(egdeSnk);
				visit[egdeSnk.getId()] = true;
				
				int weight = topoGraph.getWeight(edgeSrc, egdeSnk);
				GraphNode viewEdgeSnk = new GraphNode(egdeSnk.getAliasName());
				GraphEdge viewEdge = new GraphEdge(viewEdgeSrc, viewEdgeSnk, weight);
				
				// 利用极坐标计算各个邻接点节点在[绘制图]的直角坐标
				_calculatSnkXY(viewEdgeSrc, viewEdgeSnk, weight, thetas[j++], MANIFICATION);
				
				viewNodeMap.put(egdeSnk.getName(), viewEdgeSnk);
				viewEdges.add(viewEdge);
			}
		}
		
		_offsetPos(viewNodeMap);	// 对所有节点做整体偏移
		markViewNodeColor(topoGraph, viewNodeMap);	// 特殊节点颜色标记
		
		viewNodeMap.clear();
		return viewEdges;
	}
	
	/**
	 * 等分圆周角(随机逆时针旋转[0,90]度)
	 * 
	 * 	若拓扑图相邻的两个节点，其邻接点集数量具有最小公倍数，
	 * 	那么直接等分这两个节点的圆周角时，很容易使得某些邻接点在绘图时重合.
	 * 
	 * 	引入随机旋转角，可以使得这两个邻接点集随机旋转一定角度，在一定程度上避免重合.
	 * @param split 切割数
	 * @return 等分圆周角
	 */
	private double[] _subMultipleAngle(int split) {
		final int CIRCLE = 360;
		if(split <= 0) {
			return new double[0];
		} else if(split == 1) {
			return new double[] { RandomUtils.genInt(CIRCLE) };
		}
		
		double subTheta = 360D / split;	// 等分子角
		subTheta = (RandomUtils.genBoolean() ? subTheta : -subTheta); // 随机正向/反向
		final int ROTATION = RandomUtils.genInt(90);	// 90度以内的随机旋转角
		
		// 相对于极轴的等分角集
		double[] thetas = new double[split]; {
			thetas[0] = 0 + ROTATION;
		}
		for(int i = 1; i < split; i++) {
			thetas[i] = thetas[i - 1] + subTheta;
			if(thetas[i] > CIRCLE) {
				thetas[i] -= CIRCLE;
			} else if(thetas[i] < 0) {
				thetas[i] += CIRCLE;
			}
		}
		return thetas;
	}
	
	/**
	 * 以src作为极坐标系原点，利用snk相对于极轴的旋转角计算snk在直角坐标系的相对位置
	 * 
	 * @param viewEdgeSrc 极坐标系原点
	 * @param viewEdgeSnk 目标点
	 * @param weight src与snk的边权（长度）
	 * @param theta snk相对于极轴在极坐标系中的旋转角
	 * @param magnification 所绘制图形的放大倍率
	 */
	private void _calculatSnkXY(GraphNode viewEdgeSrc, GraphNode viewEdgeSnk, 
			int weight, double theta, int magnification) {
		
		// 归一化边权， 计算极轴长度
		double rou = _toNormalization(weight);
		
		// 假设参考点src为原点，把目标点snk的极坐标转换为直角坐标(Math的三角函数为弧度制)
		int x = (int) (rou * Math.cos(NumUtils.toRadian(theta)) + 0.5);	
		int y = (int) (rou * Math.sin(NumUtils.toRadian(theta)) + 0.5);
		
		// 根据src实际的直角坐标，对snk的直角坐标进行相对偏移
		x += viewEdgeSrc.getX();
		y += viewEdgeSrc.getY();
		viewEdgeSnk.setPos(x, y);
	}

	/**
	 * 归一化边权（使得绘制的拓扑边长度接近等长, 目的是使得绘制图形更美观， 不会因为边权差值过大或过小造成绘制图形节点间距稀疏不一）
	 * 并在归一化后按一定倍率放大（归一化后边权长度在[0,1]之间，放大会使得绘制的图形更清晰）
	 * @param weight 边权
	 * @return
	 */
	private double _toNormalization(int weight) {
		return Math.atan(weight) * NORM_ARG;	// NORM_ARG已包含放大倍数
	}
	
	/**
	 * 根据最左和最顶节点的坐标以及节点宽高，对所有节点做整体偏移
	 * 	(绘制图的原点在绘制面板左上角， 向右为X正向，向下为Y正向, 即与真正的直角坐标系相对于原点上下反转)
	 * @param viewNodeMap 绘制节点集
	 */
	private void _offsetPos(Map<String, GraphNode> viewNodeMap) {
		int minLeftX = Integer.MAX_VALUE;	// 最小左边界坐标
		int minBottomY = Integer.MAX_VALUE;	// 最小下边界坐标
		
		// 计算左边界和上边界的偏差值
		Iterator<GraphNode> viewNodes = viewNodeMap.values().iterator();
		while(viewNodes.hasNext()) {
			GraphNode viewNode = viewNodes.next();
			int leftX = viewNode.getX() - (viewNode.getWidth() / 2);
			int bottomY = viewNode.getY() - (viewNode.getHeight() / 2);
			
			minLeftX = (minLeftX > leftX ? leftX : minLeftX);
			minBottomY = (minBottomY > bottomY ? bottomY : minBottomY);
		}
		
		// 对所有节点的直角坐标做整体偏移
		final int X_OFFSET = Math.abs(minLeftX);
		final int Y_OFFSET = Math.abs(minBottomY);
		viewNodes = viewNodeMap.values().iterator();
		while(viewNodes.hasNext()) {
			GraphNode viewNode = viewNodes.next();
			viewNode.setX(viewNode.getX() + X_OFFSET);
			viewNode.setY(viewNode.getY() + Y_OFFSET);
		}
	}
	
	/**
	 * 利用GEF框架内置功能自动计算拓扑图各个节点的XY坐标
	 *  (当节点数超过30时，计算非常慢)
	 * @param topoGraph
	 * @return 用于实际呈现的拓扑图边集（每条边的源宿节点具有实际的XY坐标值）
	 */
	@SuppressWarnings("unchecked")
	private List<GraphEdge> calculatePositionByGEF(TopoGraph topoGraph) {
		DirectedGraph graphCalculator = new DirectedGraph(); // 拓扑图点边坐标计算器
		Map<String, GraphNode> viewNodeMap = // 唯一性点集，避免重复放入同一节点到GEF造成拓扑图离散
				new HashMap<String, GraphNode>();
		
		Set<Edge> edges = topoGraph.getAllEdges();	// 拓扑图的抽象边集（利用边权衡量的相对距离）
		List<GraphEdge> viewEgdes = new LinkedList<GraphEdge>();	// 绘制图的边集
		
		// 枚举每条边的源宿点，存储到拓扑图的坐标计算模型
		for(Edge edge : edges) {
			
			// 源端放入GEF模型
			Node edgeSrc = edge.getSrc();
			GraphNode viewEdgeSrc = viewNodeMap.get(edgeSrc.getName());
			if(viewEdgeSrc == null) {
				viewEdgeSrc = new GraphNode(edgeSrc.getAliasName());
				graphCalculator.nodes.add(viewEdgeSrc.getGefNode());
				viewNodeMap.put(edgeSrc.getName(), viewEdgeSrc);
			}
			
			// 宿端放入GEF模型
			Node edgeSnk = edge.getSnk();
			GraphNode viewEdgeSnk = viewNodeMap.get(edgeSnk.getName());
			if(viewEdgeSnk == null) {
				viewEdgeSnk = new GraphNode(edgeSnk.getAliasName());
				graphCalculator.nodes.add(viewEdgeSnk.getGefNode());	
				viewNodeMap.put(edgeSnk.getName(), viewEdgeSnk);
			}
			
			// 边放入GEF模型
			GraphEdge viewEdge = new GraphEdge(viewEdgeSrc, viewEdgeSnk, edge.getWeight());
			graphCalculator.edges.add(viewEdge.getGefEdge());	
			viewEgdes.add(viewEdge);
		}
		
		markViewNodeColor(topoGraph, viewNodeMap);	// 特殊节点颜色标记
		viewNodeMap.clear();
		
		// 自动计算GEF模型内各个点边的坐标
		try {
			// 仅适用于连通图的自动布局（推荐，计算效果最好， 但非连通图会抛出异常）
			new DirectedGraphLayout().visit(graphCalculator);
			
		} catch(Throwable e){
			try {
				// 适用于非连通图（原理是填充虚拟边使图连通后再计算，最后删除虚拟边，但效果略差）
				new NodeJoiningDirectedGraphLayout().visit(graphCalculator);
				
			} catch(Throwable ex) {
				SwingUtils.error(ex, "计算拓扑图坐标失败");
			}
		}
		return viewEgdes;
	}
	
	/**
	 * 根据源点/宿点/必经点对所绘制拓扑图的节点进行颜色标记
	 * @param topoGraph 拓扑图
	 * @param viewNodeMap 绘制图的节点集
	 */
	private void markViewNodeColor(TopoGraph topoGraph, 
			Map<String, GraphNode> viewNodeMap) {
		Node src = topoGraph.getSrc();	// 拓扑图源点
		Node snk = topoGraph.getSnk();	// 拓扑图宿点
		Set<String> includes = topoGraph.getIncludeNames();	// 必经点名称集
		
		Iterator<String> names = viewNodeMap.keySet().iterator();
		while(names.hasNext()) {
			String name = names.next();
			GraphNode viewNode = viewNodeMap.get(name);
			
			if(name.equals(src.getName())) { viewNode.markGraphSrc(); }
			if(name.equals(snk.getName())) { viewNode.markGraphSnk(); }
			if(includes.contains(name)) { viewNode.markInclusive(); }
		}
	}
	
	/**
	 * 创建拓扑图的展示模型
	 * @param viewEgdes 拓扑图边集（每条边的源宿节点具有实际的XY坐标值）
	 * @param arrow 是否为有向图
	 */
	private void createViewModel(List<GraphEdge> viewEgdes, boolean arrow) {
		Map<DefaultGraphCell, Object> graphAttribute = 
				new Hashtable<DefaultGraphCell, Object>();	// 拓扑图属性集
		final Map<DefaultGraphCell, Object> EDGE_ATTRIBUTE = 
				getEdgeAttribute(arrow); // 边属性集（所有边可共用同一个属性集）
		
		// 设置每条边的 点、边 属性， 并写到 拓扑图展示模型
		for(GraphEdge viewEdge : viewEgdes) {
			GraphNode viewEdgeSrc = viewEdge.getSrc();
			GraphNode viewEdgeSnk = viewEdge.getSnk();
			
			DefaultEdge cellEdge = viewEdge.getCellEdge();
			DefaultGraphCell cellEdgeSrc = viewEdgeSrc.getCellNode();
			DefaultGraphCell cellEdgeSnk = viewEdgeSnk.getCellNode();
			
			// 设置边、点属性
			graphAttribute.put(cellEdge, EDGE_ATTRIBUTE);
			graphAttribute.put(cellEdgeSrc, getNodeAttribute(viewEdgeSrc));
			graphAttribute.put(cellEdgeSnk, getNodeAttribute(viewEdgeSnk));
			
			// 把边、点约束集写到展示模型
			ConnectionSet set = new ConnectionSet(cellEdge, 
					cellEdgeSrc.getChildAt(0), cellEdgeSnk.getChildAt(0));
			Object[] cells = new Object[] { cellEdge, cellEdgeSrc, cellEdgeSnk };
			viewGraphModel.insert(cells, graphAttribute, set, null, null);
		}
	}
	
	/**
	 * 获取边属性集（所有边可共用同一个属性集）
	 * @param arrow 是否存在方向
	 * @return
	 */
	private Map<DefaultGraphCell, Object> getEdgeAttribute(boolean arrow) {
		Map<DefaultGraphCell, Object> edgeAttribute = 
				new Hashtable<DefaultGraphCell, Object>();
		GraphConstants.setLineColor(edgeAttribute, Color.LIGHT_GRAY);	// 线体颜色
		if(arrow == true) {
			GraphConstants.setLineEnd(edgeAttribute, GraphConstants.ARROW_CLASSIC);	// 线末增加箭头样式
			GraphConstants.setEndFill(edgeAttribute, true);	// 实心箭头
		}
		return edgeAttribute;
	}
	
	/**
	 * 获取节点属性集
	 * @param node 拓扑图节点
	 * @return
	 */
	private Map<DefaultGraphCell, Object> getNodeAttribute(GraphNode node) {
		Map<DefaultGraphCell, Object> nodeAttribute = 
				new Hashtable<DefaultGraphCell, Object>();
		
		Rectangle2D bound = null;
		if(useGEF == true) {
			final int OFFSET_Y = 10;	// Y轴方向的坐标偏移量（主要为了生成的拓扑图不要贴近X轴）
			bound = new Rectangle2D.Double(
					node.getY(), (node.getX() + OFFSET_Y), // 节点左上角的顶点坐标（反转XY是为了使得拓扑图整体成横向呈现）
					node.getWidth(), node.getHeight());	// 强制设定所呈现节点的宽高
		} else {
			bound = new Rectangle2D.Double(node.getX(), node.getY(), 
					node.getWidth(), node.getHeight());
		}
		GraphConstants.setBounds(nodeAttribute, bound);	// 设置节点坐标
		
		// 设置节点边框颜色
		Color backGround = (node.isGraphSrc() ? COLOR_SRC : 
				(node.isGraphSnk() ? COLOR_SNK : 
				(node.isInclusive() ? COLOR_INCLUSIVE : COLOR_NORMAL)));
		GraphConstants.setBorderColor(nodeAttribute, backGround);
		return nodeAttribute;
	}

}