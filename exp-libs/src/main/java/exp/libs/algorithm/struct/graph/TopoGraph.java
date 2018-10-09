package exp.libs.algorithm.struct.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <PRE>
 * 含源宿、必经点标记的拓扑图
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-12-25
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TopoGraph extends Graph {

	private Node src;
	
	private Node snk;
	
	private boolean order;
	
	private List<Node> includes;
	
	public TopoGraph() {
		this(false, false);
	}
	
	/**
	 * 
	 * @param order 必经点有序(默认false, 即无序)
	 */
	public TopoGraph(boolean order) {
		this(order, false);
	}
	
	/**
	 * 
	 * @param order 必经点有序(默认false, 即无序)
	 * @param arrow 有向图(默认false, 即无向图)
	 */
	public TopoGraph(boolean order, boolean arrow) {
		super(arrow);
		this.src = Node.NULL;
		this.snk = Node.NULL;
		this.order = order;
		this.includes = (order ? new LinkedList<Node>() : new ArrayList<Node>());
	}

	public Node getSrc() {
		if(src == Node.NULL) {
			src = getNode(0);
		}
		return src;
	}

	public void setSrc(String name) {
		this.src = addNode(name);
	}

	public Node getSnk() {
		if(snk == Node.NULL) {
			snk = getNode(nodeSize() - 1);
		}
		return snk;
	}

	public void setSnk(String name) {
		this.snk = addNode(name);
	}
	
	/**
	 * 是否存在必经点
	 * @return
	 */
	public boolean existInclusive() {
		return !includes.isEmpty();
	}
	
	/**
	 * 必经点是否有序
	 * @return
	 */
	public boolean isOrderInclusive() {
		return order;
	}
	
	/**
	 * 必经点数量
	 * @return
	 */
	public int inclusiveSize() {
		return includes.size();
	}
	
	public List<Node> getIncludes() {
		return (order ? new LinkedList<Node>(includes) : 
			new ArrayList<Node>(includes));
	}
	
	public Set<String> getIncludeNames() {
		Set<String> names = new HashSet<String>();
		for(Node include : includes) {
			names.add(include.getName());
		}
		return names;
	}
	
	public Set<Integer> getIncludeIds() {
		Set<Integer> ids = new HashSet<Integer>();
		for(Node include : includes) {
			ids.add(include.getId());
		}
		return ids;
	}
	
	public boolean setIncludes(Collection<String> names) {
		boolean isOk = false;
		if(names != null) {
			isOk = true;
			for(String name : names) {
				isOk &= setInclude(name);
			}
		}
		return isOk;
	}
	
	public boolean setInclude(String name) {
		boolean isOk = false;
		Node node = getNode(name);
		if(node != Node.NULL) {
			includes.add(node);
			isOk = true;
		}
		return isOk;
	}
	
}
