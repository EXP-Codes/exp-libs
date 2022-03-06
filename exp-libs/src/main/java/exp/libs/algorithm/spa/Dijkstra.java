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
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Dijkstra {

	// 节点间距为最大值时表示不可达
	public final static int MAX_WEIGHT = Integer.MAX_VALUE;
	
	// 节点数
	private int size;
	
	// 邻接矩阵
	private int[][] matrix;
	
	// 源点ID
	private int srcId;
	
	// sDists[i] : 源点到节点i的距离
	private int[] sDists;
	
	// sNode[i] : 松弛节点： 源点通过 节点sNode[i] 到达 节点i 使得路径松弛
	private int[] sNode;
	
	public Dijkstra(int[][] matrix) {
		this.matrix = (matrix == null ? new int[0][0] : matrix);
		this.size = this.matrix.length;
		clear();
	}
	
	private void clear() {
		this.srcId = -1;
		this.sDists = new int[size];
		this.sNode = new int[size];
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
			sNode[i] = i;
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
					sNode[i] = next;
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
			do {
				routeIds.add(0, endId);
				if(endId != sNode[endId]) {
					endId = sNode[endId];
				} else {
					break;
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
