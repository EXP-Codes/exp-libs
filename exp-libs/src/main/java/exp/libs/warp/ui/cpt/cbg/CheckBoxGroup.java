package exp.libs.warp.ui.cpt.cbg;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * <PRE>
 * swing复选框组件
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-08-17
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class CheckBoxGroup<T> {

	/** 选项数 */
	private int size;
	
	/** 选项对象集合 */
	private T[] items;
	
	/** 选项对象容器集合 */
	private List<JCheckBox> cbs;
	
	/** 选项对象索引表 */
	private Map<T, Integer> cbIdx;
	
	/**
	 * 构造函数
	 * @param items 复选框选项
	 */
	public CheckBoxGroup(T[] items) {
		this.items = items;
		this.size = (items == null ? 0 : items.length);
		this.cbs = new ArrayList<JCheckBox>(size);
		this.cbIdx = new HashMap<T, Integer>();
		
		for(int i = 0; i < size; i++) {
			T item = items[i];
			JCheckBox cb = new JCheckBox(item.toString());
			cbs.add(cb);
			cbIdx.put(item, i);
		}
	}
	
	/**
	 * 检查一个选项是否被选中
	 * @param item 选项对象
	 * @return true:是； false:否
	 */
	public boolean isSelected(T item) {
		Integer itemIdx = cbIdx.get(item);
		return isSelected(itemIdx);
	}
	
	/**
	 * 检查一个选项是否被选中
	 * @param itemIdx 选项对象索引
	 * @return true:是； false:否
	 */
	public boolean isSelected(int itemIdx) {
		boolean isSelected = false;
		JCheckBox cb = cbs.get(itemIdx);
		if(cb != null) {
			isSelected = cb.isSelected();
		}
		return isSelected;
	}
	
	/**
	 * 反选一个选项
	 * @param item 选项对象
	 */
	public void inverse(T item) {
		Integer itemIdx = cbIdx.get(item);
		if(itemIdx != null) {
			inverse(itemIdx);
		}
	}
	
	/**
	 * 反选一个选项
	 * @param itemIdx 选项对象索引
	 */
	public void inverse(int itemIdx) {
		JCheckBox cb = getCheckBox(itemIdx);
		if(cb != null) {
			cb.setSelected(!cb.isSelected());
		}
	}
	
	/**
	 * 反选所有
	 */
	public void inverseAll() {
		for(int i = 0; i < size; i++) {
			JCheckBox cb = cbs.get(i);
			cb.setSelected(!cb.isSelected());
		}
	}
	
	/**
	 * 选择一个选项
	 * @param item 选项对象
	 */
	public void select(T item) {
		Integer itemIdx = cbIdx.get(item);
		if(itemIdx != null) {
			select(itemIdx);
		}
	}
	
	/**
	 * 选择一个选项
	 * @param itemIdx 选项对象索引
	 */
	public void select(int itemIdx) {
		JCheckBox cb = getCheckBox(itemIdx);
		if(cb != null) {
			cb.setSelected(true);
		}
	}
	
	/**
	 * 选择所有
	 */
	public void selectAll() {
		for(int i = 0; i < size; i++) {
			JCheckBox cb = cbs.get(i);
			cb.setSelected(true);
		}
	}
	
	/**
	 * 取消选择一个选项
	 * @param item 选项对象
	 */
	public void unselect(T item) {
		Integer itemIdx = cbIdx.get(item);
		if(itemIdx != null) {
			unselect(itemIdx);
		}
	}
	
	/**
	 * 取消选择一个选项
	 * @param itemIdx 选项对象索引
	 */
	public void unselect(int itemIdx) {
		JCheckBox cb = getCheckBox(itemIdx);
		if(cb != null) {
			cb.setSelected(false);
		}
	}
	
	/**
	 * 取消选择所有
	 */
	public void unselectAll() {
		for(int i = 0; i < size; i++) {
			JCheckBox cb = cbs.get(i);
			cb.setSelected(false);
		}
	}
	
	/**
	 * 启用/禁用复选框组件
	 * @param enable true:启用; false:禁用
	 */
	public void setEnable(boolean enable) {
		for(int i = 0; i < size; i++) {
			JCheckBox cb = cbs.get(i);
			cb.setEnabled(enable);
		}
	}
	
	/**
	 * 获取一个选项对象
	 * @param itemIdx 选项对象索引
	 * @return 选项对象
	 */
	public T getItem(int itemIdx) {
		T item = null;
		if(items != null && itemIdx >= 0 && itemIdx < size) {
			item = items[itemIdx];
		}
		return item;
	}
	
	/**
	 * 获取一个选项对象的容器
	 * @param itemIdx 选项对象索引
	 * @return 选项对象容器
	 */
	public JCheckBox getCheckBox(int itemIdx) {
		JCheckBox cb = null;
		if(itemIdx >= 0 && itemIdx < size) {
			cb = cbs.get(itemIdx);
		}
		return cb;
	}
	
	/**
	 * 获取当前 被选中/没有选中 的所有选项对象
	 * @param isSelected true:被选中的; false:没有选中的
	 * @return 选项对象集合
	 */
	public List<T> getItems(boolean isSelected) {
		List<T> selectedItems = new LinkedList<T>();
		if(items != null) {
			for(int i = 0; i < size; i++) {
				JCheckBox cb = cbs.get(i);
				if(cb.isSelected() == isSelected) {
					selectedItems.add(items[i]);
				}
			}
		}
		return selectedItems;
	}
	
	/**
	 * 获取当前 被选中/没有选中 的所有选项对象容器
	 * @param isSelected true:被选中的; false:没有选中的
	 * @return 选项对象容器集合
	 */
	public List<JCheckBox> getCheckBoxs(boolean isSelected) {
		List<JCheckBox> selectedCBs = new LinkedList<JCheckBox>();
		for(int i = 0; i < size; i++) {
			JCheckBox cb = cbs.get(i);
			if(cb.isSelected() == isSelected) {
				selectedCBs.add(cb);
			}
		}
		return selectedCBs;
	}
	
	/**
	 * 获取所有选项对象的容器
	 * @return 所有选项对象容器的集合
	 */
	public List<JCheckBox> getAllCheckBoxs() {
		return new LinkedList<JCheckBox>(cbs);
	}
	
	/**
	 * 把复选框生成 [row=1, col=size] 的 Grid布局面板
	 * @return [row=1, col=size] 的 Grid布局面板
	 */
	public JPanel toHGridPanel() {
		int col = (size <= 0 ? 1 : size);
		JPanel panel = new JPanel(new GridLayout(1, col));
		for(int i = 0; i < size; i++) {
			JCheckBox cb = cbs.get(i);
			panel.add(cb, i);
		}
		return panel;
	}
	
	/**
	 * 把复选框生成 [row=size, col=1] 的 Grid布局面板
	 * @return [row=size, col=1] 的 Grid布局面板
	 */
	public JPanel toVGridPanel() {
		int row = (size <= 0 ? 1 : size);
		JPanel panel = new JPanel(new GridLayout(row, 1));
		for(int i = 0; i < size; i++) {
			JCheckBox cb = cbs.get(i);
			panel.add(cb, i);
		}
		return panel;
	}
	
	/**
	 * 把复选框生成 [row=row, col=size/row] 的 Grid布局面板
	 * @return
	 */
	public JPanel toGridPanel(int row) {
		row = (row <= 0 ? 1 : row);
		int col = size / row;
		col += (size % row == 0 ? 0 : 1);
		
		JPanel panel = new JPanel(new GridLayout(row, (col <= 0 ? 1 : col)));
		for(int i = 0; i < size; i++) {
			JCheckBox cb = cbs.get(i);
			panel.add(cb, i);
		}
		return panel;
	}
	
}
