package exp.libs.algorithm.heuristic.qaca;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <PRE>
 * 拓扑图参数
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-06-08
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
final class __QGraph {

	/** 拓扑图规模(节点数) */
	private int size;
	
	/** 拓扑图源点编号 */
	private int srcId;
	
	/** 拓扑图终点编号 */
	private int snkId;
	
	/** 拓扑图的必经点集 */
	private Set<Integer> includeIds;
	
	/** 拓扑图的必经点集 */
	private int[] _includeIds;
	
	/** 节点间距 */
	private int[][] dist;
	
	/**
	 * 某节点到其他节点的平均距离 
	 *  avgDist[i] = ∑distances[i][0~size] / size
	 */
	private double[] avgDist;
	
	/**
	 * 某节点到其他各个节点中的最大距离 
	 * 	maxDist[i] =  max{distances[i][0~size]} 
	 */
	private int[] maxDist;
	
	/**
	 * 构造函数
	 * @param dist 拓扑图节点间距
	 * @param srcId 拓扑图源点编号
	 * @param snkId 拓扑图终点编号
	 * @param includeIds 拓扑图必经点集
	 */
	protected __QGraph(int[][] dist, int srcId, int snkId, 
			Collection<Integer> includeIds) {
		this.size = (dist == null ? 0 : dist.length);
		this.srcId = srcId;
		this.snkId = snkId;
		this.includeIds = (includeIds == null ? 
				new HashSet<Integer>() : new HashSet<Integer>(includeIds));
		this._includeIds = new int[this.includeIds.size()];
		initIncludes();
		
		this.dist = dist;
		this.avgDist = new double[size];
		this.maxDist = new int[size];
		initDist();
	}
	
	private void initIncludes() {
		int idx = 0;
		Iterator<Integer> ids = includeIds.iterator();
		while(ids.hasNext()) {
			int id = ids.next();
			_includeIds[idx++] = id;
		}
		
		this.includeIds.add(srcId);
		this.includeIds.add(snkId);
	}
	
	private void initDist() {
		for(int i = 0; i < size; i++) {
			int sum = 0, cnt = 0;
			maxDist[i] = -1;
			for(int j = 0; j < size; j++) {
				if(dist[i][j] <= 0) {
					dist[i][j] = 0;
					
				} else if(!isLinked(i, j)) {
					continue;
					
				} else {
					cnt++;
					sum += dist[i][j];
					if(maxDist[i] < dist[i][j]) {
						maxDist[i] = dist[i][j];
					}
				}
			}
			avgDist[i] = sum / (cnt * 1.0D);
		}
	}
	
	protected int size() {
		return size;
	}

	protected int srcId() {
		return srcId;
	}

	protected int snkId() {
		return snkId;
	}

	protected int dist(int srcId, int snkId) {
		return dist[srcId][snkId];
	}

	protected double avgDist(int nodeId) {
		return avgDist[nodeId];
	}

	protected int maxDist(int nodeId) {
		return maxDist[nodeId];
	}
	
	/**
	 * 检查节点是否在必经点集中（包括源宿点）
	 * @param nodeId
	 * @return
	 */
	protected boolean isInclude(int nodeId) {
		return includeIds.contains(nodeId);
	}
	
	/**
	 * 获取必经点集
	 * @return 不包括源宿点的必经点集
	 */
	protected int[] getIncludes() {
		return _includeIds;
	}
	
	protected boolean isLinked(int srcId, int snkId) {
		return dist[srcId][snkId] < Integer.MAX_VALUE;
	}

}
