package exp.libs.algorithm.struct.queue.loop;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LoopQueue<TYPE> {

	private TYPE[] arrays;
	
	private int len;
	
	private int size;
	
	private int head;
	
	private int tail;
	
	@SuppressWarnings("unchecked")
	public LoopQueue(final int capacity) {
		this.len =  (capacity <= 0 ? 1 : capacity);
		this.arrays = (TYPE[]) new Object[len];
		clear();
	}
	
	public boolean add(TYPE e) {
		if(e == null) {
			return false;
		}
		
		if(isFull()) {
			tail = head;
			head = (++head % len);
			
		} else {
			tail = (++tail % len);
		}
		arrays[tail] = e;
		
		size++;
		size = (size > len ? len : size);
		return true;
	}
	
	public TYPE get() {
		return get(0);
	}
	
	public TYPE get(int idx) {
		TYPE e = null;
		if(idx < len) {
			int offset = (idx + head) % len;
			e = arrays[offset];
		}
		return e;
	}
	
	public TYPE take() {
		TYPE e = arrays[head];
		if(!isEmpty()) {
			arrays[head] = null;
			head = (++head % len);
			size--;
		}
		return e;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}

	public boolean isFull() {
		return size == len;
	}
	
	public int size() {
		return size;
	}
	
	public int length() {
		return len;
	}
	
	public void clear() {
		Arrays.fill(arrays, null);
		this.size = 0;
		this.head = 0;
		this.tail = len - 1;
	}
	
	private List<TYPE> toList() {
		List<TYPE> list = new LinkedList<TYPE>();
		for(int i = 0; i < size(); i++) {
			int offset = (i + head) % len;
			list.add(arrays[offset]);
		}
		return list;
	}
	
	public Iterator<TYPE> iterator() {
		return toList().iterator();
	}
	
	@Override
	public String toString() {
		return toList().toString();
	} 
	
}
