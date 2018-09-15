package exp.libs.algorithm.spa;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <PRE>
 * 最短路径算法: Dijkstra算法 (FIXME: 用Fibonacci堆优化).
 * 适用范围: 单源最短路问题, 有向图/无向图均可, 无负权环
 * 时间复杂度: O(V * lgV + E)
 * 空间复杂度: 
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-07-25
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Dijkstra {

	public final static int MAX_WEIGHT = Integer.MAX_VALUE;
	
	private int size;
	
	private int[][] matrix;
	
	private int srcId;
	
	private int[] sDists;
	
	private _IDPath[] sPaths;
	
	public Dijkstra(int[][] matrix) {
		this.matrix = (matrix == null ? new int[0][0] : matrix);
		this.size = this.matrix.length;
		clear();
	}
	
	private void clear() {
		this.srcId = -1;
		this.sDists = new int[size];
		this.sPaths = new _IDPath[size];
	}
	
	private boolean inRange(final int idx) {
		return (idx >= 0 && idx < size);
	}
	
	public boolean calculate(final int srcId) {
		return calculate(srcId, null);
	}
	
	public boolean calculate(final int srcId, Set<Integer> tabu) {
		if(!inRange(srcId)) {
			return false;
		}
		clear();
		this.srcId = srcId;
		tabu = (tabu == null ? new HashSet<Integer>() : tabu);
		for(int i = 0; i < size; i++) {
			sDists[i] = matrix[srcId][i];
		}
		
		boolean[] visit = new boolean[size];
		Arrays.fill(visit, false);
		visit[srcId] = true;
		
		while(true) {
			int next = -1;
			int minDist = MAX_WEIGHT;
			for(int i = 0; i < size; i++) {
				if(!visit[i] && !tabu.contains(i) && sDists[i] < minDist) {
					minDist = sDists[i];
					next = i;
				}
			}
			
			if(next < 0) {
				break;
			} else {
				visit[next] = true;
			}
			
			for(int i = 0; i < size; i++) {
				if(visit[i] || matrix[next][i] == MAX_WEIGHT) {
					continue;
				}
				
				int relex = add(sDists[next], matrix[next][i]);
				if(sDists[i] > relex) {
					sDists[i] = relex;
					if(sPaths[i] == null) {
						sPaths[i] = new _IDPath();
					}
					sPaths[i].add(next);
				}
			}
		}
		return true;
	}
	
	private int add(int a, int b) {
		return ((a == MAX_WEIGHT || b == MAX_WEIGHT) ? MAX_WEIGHT : (a + b));
	}
	
	public List<Integer> getShortPaths(final int snkId) {
		List<Integer> routeIds = new LinkedList<Integer>();
		if(inRange(srcId) && getShortPathWeight(snkId) < MAX_WEIGHT) {
			int endId = snkId;
			routeIds.add(0, endId);
			do {
				_IDPath idPath = sPaths[endId];
				if(idPath != null && !idPath.isEmpty()) {
					List<Integer> ids = idPath.getIds();
					for(int i = ids.size() - 1; i >= 0; i--) {
						int id = ids.get(i);
						routeIds.add(0, id);
						endId = id;
					}
				} else {
					break;	// 源点的IDPath必定为null
				}
			} while(true);
			routeIds.add(0, srcId);
		}
		return routeIds;
	}
	
	public int getShortPathWeight(final int snkId) {
		return (inRange(snkId) ? sDists[snkId] : MAX_WEIGHT);
	}
	
}
