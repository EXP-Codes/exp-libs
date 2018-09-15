package exp.libs.warp.conf.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import exp.libs.utils.other.StrUtils;

class _XTree {
	
	private final static String PATH_SPLIT = "/";
	
	/** 虚构根节点 */
	private final XNode TREE_ROOT;
	
	/** 节点集合 */
	private Set<XNode> xNodes;
	
	/** node position(name@id) -> node xPaths */
	private Map<String, String> xPaths;
	
	protected _XTree() {
		this.TREE_ROOT = new XNode(null, null);
		this.xNodes = new HashSet<XNode>();
		this.xPaths = new HashMap<String, String>();
	}
	
	/**
	 * 更新xml配置树
	 * @param root 根节点
	 */
	protected void update(Element root) {
		updateValues(root);
		updateXPaths();
	}
	
	private void updateValues(Element root) {
		Map<Element, XNode> parents = new HashMap<Element, XNode>();
		parents.put(root, TREE_ROOT);
		
		List<Element> bfsQueue = new ArrayList<Element>();
		bfsQueue.add(root);
		int head = 0;
		int tail = 0;
		while(head <= tail) {
			Element element = bfsQueue.get(head++);
			XNode parent = parents.get(element);
			
			XNode node = null;
			if(parent.isEnum()) {
				node = new XNode(parent, element);
				parent.addChild(node);
				xNodes.add(node);
				
			} else {
				String pos = XNode.toPosition(element);
				node = parent.getChild(pos);
				if(node == null) {
					node = new XNode(parent, element);
					parent.addChild(node);
					xNodes.add(node);
					
				} else {
					node.update(element);
				}
			}
			
			// 更新BFS队列
			@SuppressWarnings("unchecked")
			Iterator<Element> childs = element.elementIterator();
			while(childs.hasNext()) {
				Element child = childs.next();
				parents.put(child, node);
				bfsQueue.add(child);
				tail++;
			}
		}
	}
	
	// 遍历树， 计算所有节点的xPath
	private void updateXPaths() {
		Map<XNode, String> xPathPrefixs = new HashMap<XNode, String>();
		xPathPrefixs.put(TREE_ROOT, "");
		
		List<XNode> bfsQueue = new ArrayList<XNode>();
		bfsQueue.add(TREE_ROOT);
		int head = 0;
		int tail = 0;
		while(head <= tail) {
			XNode node = bfsQueue.get(head++);
			
			String xPathPrefix = xPathPrefixs.get(node);
			String xPath = StrUtils.concat(xPathPrefix, PATH_SPLIT, node.POS());
			updateXPath(node, xPath);
			
			// 更新BFS队列
			Iterator<XNode> childs = node.getChilds();
			while(childs.hasNext()) {
				XNode child = childs.next();
				xPathPrefixs.put(child, xPath);
				bfsQueue.add(child);
				tail++;
			}
		}
	}
	
	private void updateXPath(XNode node, String xPath) {
		if(xPaths.get(node.POS()) == null) {
			xPaths.put(node.POS(), xPath);
		}
		
		if(xPaths.get(node.NAME()) == null) {
			xPaths.put(node.NAME(), xPath);
		}
	}
	
	/**
	 * 通过xPath从根节点开始查找节点
	 * @param xPath  /foo/bar@id/tmp@id/xxx/yyy
	 * @return
	 */
	protected XNode findXNode(String xPath) {
		if(xPath == null) {
			return null;
			
		} else if(!xPath.contains(PATH_SPLIT)) {
			xPath = toXPath(xPath, null);
		}
		
		XNode node = TREE_ROOT;
		String[] poses = xPath.replace("\\", PATH_SPLIT).split(PATH_SPLIT);
		for(String pos : poses) {
			if(StrUtils.isEmpty(pos)) {
				continue;
			}
			
			XNode child = node.getChild(pos);
			if(child == null) {
				child = node.getChild(XNode.toName(pos));
				if(child == null) {
					break;
				}
			}
			node = child;
		}
		return (node == TREE_ROOT ? null : node);
	}
	
	protected String toXPath(String name, String id) {
		String xPath = "";
		String pos = XNode.toPosition(name, id);
		if(!pos.contains(PATH_SPLIT)) {
			xPath = xPaths.get(pos);
		}
		return (xPath == null ? "" : xPath);
	}
	
	protected void clear() {
		for(XNode xNode : xNodes) {
			xNode.clear();
		}
		xNodes.clear();
	}
	
}
