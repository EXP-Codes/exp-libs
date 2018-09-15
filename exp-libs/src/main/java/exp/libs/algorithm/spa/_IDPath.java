package exp.libs.algorithm.spa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class _IDPath {

	protected final static _IDPath NULL = new _IDPath();
	
	private List<Integer> ids;
	
	protected _IDPath() {
		this.ids = new LinkedList<Integer>();
	}

	protected boolean isEmpty() {
		return ids.isEmpty();
	}
	
	protected List<Integer> getIds() {
		return new ArrayList<Integer>(ids);
	}

	protected void add(int id) {
		if(id >= 0) {
			this.ids.add(id);
		}
	}
	
	protected void clear() {
		this.ids.clear();
	}
	
}
