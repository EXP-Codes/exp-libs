package exp.libs.warp.conf.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;

import exp.libs.utils.other.StrUtils;

public class XNode {

	private final static String ID_SPLIT = "@";
	
	private final static String ATT_ID = "id";
	
	private final static String ATT_DEFAULT = "default";

	private final static String ATT_TYPE = "type";
	
	private final static String TYPE_ENUM = "enum";
	
	private final static String POS_REGEX = ID_SPLIT.concat(".*");
	
	/** 父节点(根节点的父节点为虚构节点) */
	private XNode parent;
	
	/** 所有子节点集（枚举用）*/ 
	private List<XNode> childs;
	
	/**
	 * 部分子节点坐标（精确搜索用）
	 * position(name@id) -> _XNode
	 */
	private Map<String, XNode> childPositions;
	
	private String name;
	
	private String id;
	
	/** name@id */
	private String position;
	
	private String value;
	
	private boolean isEnum;
	
	private Map<String, String> attributes;
	
	protected XNode(XNode parent, Element element) {
		this.parent = parent;
		this.childs = new LinkedList<XNode>();
		this.childPositions = new HashMap<String, XNode>();
		
		this.name = "";
		this.id = "";
		this.position = "";
		this.value = "";
		this.isEnum = false;
		this.attributes = new HashMap<String, String>();
		
		_update(element, true);
	}
	
	/**
	 * 更新xml配置节点
	 * @param element xml节点
	 * @param init 是否第一次更新（需要设置初始值）
	 */
	private void _update(Element element, boolean init) {
		if(element == null) {
			return;
		}
		
		if(init == false) {
			String name = element.getName();
			String id = element.attributeValue(ATT_ID);
			id = (id == null ? "" : id.trim());
			
			if(!this.name.equals(name) || !this.id.equals(id)) {
				return;
			}
		}
		
		@SuppressWarnings("unchecked")
		Iterator<DefaultAttribute> attributeIts = element.attributeIterator();
		while(attributeIts.hasNext()) {
			DefaultAttribute attribute = attributeIts.next();
			attributes.put(attribute.getName(), attribute.getText().trim());
		}
		
		if(init == true) {
			this.name = element.getName();
			this.id = _getAttribute(ATT_ID);
			this.position = toPosition(name, id);
			this.isEnum = TYPE_ENUM.equals(_getAttribute(ATT_TYPE));
		}
		
		this.value = element.getTextTrim();
		if(StrUtils.isEmpty(value)) {
			String defavlt = attributes.get(ATT_DEFAULT);
			value = (defavlt != null ? defavlt : value);
		}
	}
	
	private String _getAttribute(String key) {
		String val = attributes.get(key);
		return (val == null ? "" : val);
	}
	
	/**
	 * 更新xml配置节点
	 * @param element xml节点
	 */
	protected void update(Element element) {
		_update(element, false);
	}
	
	protected void addChild(XNode child) {
		if(child != null) {
			childs.add(child);
			
			// 同名子节点仅标记第一个
			if(childPositions.get(child.POS()) == null) {
				childPositions.put(child.POS(), child);
			}
		}
	}

	public XNode getChild(String posORname) {
		XNode child = childPositions.get(posORname);
		if(child == null) {
			
			// posORname is position （精确查找）
			if(posORname.contains(ID_SPLIT)) {	
				String name = toName(posORname);
				child = childPositions.get(name);
				
			// posORname is name （模糊查找）
			} else {
				for(XNode node : childs) {
					if(posORname.equals(node.NAME())) {
						child = node;
						break;
					}
				}
			}
		}
		return child;
	}
	
	public String getChildVal(String posORname) {
		XNode child = getChild(posORname);
		return (child == null ? "" : child.VAL());
	}
	
	public Iterator<XNode> getChilds() {
		return childs.iterator();
	}
	
	public String getAttribute(String attributeName) {
		String val = attributes.get(attributeName);
		return (val == null ? "" : val);
	}
	
	public Map<String, String> getAttributes() {
		return new HashMap<String, String>(attributes);
	}
	
	public String NAME() {
		return name;
	}
	
	public String ID() {
		return id;
	}
	
	public String VAL() {
		return value;
	}
	
	protected String POS() {
		return position;
	}
	
	public boolean isEnum() {
		return isEnum;
	}
	
	protected boolean isLeaf() {
		return childs.isEmpty();
	}
	
	public XNode PARENT() {
		return parent;
	}
	
	protected static String toPosition(Element element) {
		String name = element.getName();
		String id = element.attributeValue(ATT_ID);
		return toPosition(name, id);
	}
	
	protected static String toPosition(String name, String id) {
		name = (name == null ? "" : name);
		id = (id == null ? "" : id);
		return name.concat("".equals(id) ? "" : (ID_SPLIT.concat(id)));
	}
	
	protected static String toName(String position) {
		return position.replaceFirst(POS_REGEX, "");
	}
	
	protected void clear() {
		childs.clear();
		childPositions.clear();
		attributes.clear();
	}
	
}
