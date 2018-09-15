package exp.libs.algorithm.heuristic.qaca;

import java.util.Collection;

import exp.libs.utils.num.NumUtils;

/**
 * <PRE>
 * 量子环境
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-06-08
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
final class _QEnv {

	/** 无效的环境 */
	protected final static _QEnv NULL_ENV = new _QEnv(
			new int[0][0], -1, -1, null, -1, false);
	
	/** 数学常量π */
	protected final static double PI = 3.141592654D;
	
	/** 最小旋转角 */
	protected final static double MIN_THETA = 0.001D * PI;
	
	/** 最大旋转角 */
	protected final static double MAX_THETA = 0.05D * PI;
	
	/** 旋转角 */
	protected final static double DELTA_THETA = MAX_THETA - MIN_THETA;
	
	/** 默认最大的蚂蚁代数（迭代次数） */
	private final static int DEFAULT_MAX_GENERATION = 10;
	
	/** 量子蚂蚁最大代数 */
	private int maxGeneration;
	
	/** 变异处理阀值: 当连续N次求解但没有更新最优解时, 则执行量子交叉, 避免搜索陷入停滞 */
	private int qCrossThreshold;
	
	/** 使用变异处理，可避免算法停滞到局部解或无解，但降低收敛速度 */
	public boolean useQCross;
	
	/** 拓扑图的量子参数 */
	private __QGraph qGraph;
	
	/** 蚂蚁从节点i->j移动的自启发常量(即节点i的能见度) */
	private double[][] eta;
	
	/** 用于计算蚂蚁移动时释放的信息素的其中一个常量参数 */
	private double[][] deltaBeta;
	
	/**
	 * 构造函数
	 * @param dist 拓扑图节点间距
	 * @param srcId 拓扑图源点编号
	 * @param snkId 拓扑图终点编号
	 * @param includeIds
	 * @param maxGeneration
	 * @param useQCross
	 */
	protected _QEnv(int[][] dist, int srcId, int snkId, 
			Collection<Integer> includeIds, int maxGeneration, 
			boolean useQCross) {
		this.qGraph = new __QGraph(dist, srcId, snkId, includeIds);
		this.maxGeneration = (maxGeneration <= 0 ? 
				DEFAULT_MAX_GENERATION : maxGeneration);
		this.qCrossThreshold = 1 - this.maxGeneration / 3;	// 当搜索代数超过2/3仍无解时才触发变异
		this.useQCross = useQCross;
		
		this.eta = new double[size()][size()];
		initEta();
		
		this.deltaBeta = new double[size()][size()];
		initBeta();
	}
	
	/**
	 * 初始化自启发量。
	 *  原本公式为 η(i, j) = 1 / dist[i][j]
	 *  但直接取倒数会导致路径权重对蚂蚁寻路形成决定性影响， 蚂蚁间释放的信息素无法左右寻路价值，
	 *  因此对 η(i, j)做归一化处理（即把η映射到 [0,1] 的区间范围内）， 修正公式为：
	 *   η(i, j) = 1 - arctan(dist[i][j]) * 2 / π
	 *  
	 *  又由于本算法设定的旋转角的范围为 [0.001π, 0.05π]（见本类定义的常量），
	 *  即一次寻路释放的信息素增量范围为[0.003, 0.15]之间, 
	 *  而当 dist[i][j] < 3 时， 归一化的η(i, j)的差值 大于 0.15，
	 *  导致一次寻路对路径信息素的改变几乎难以对下一次寻路形成参考价值,
	 *  因此再修正归一化公式，引入偏差值， 加速寻路收敛:
	 *   η(i, j) = 1 - arctan(dist[i][j] + offset) * 2 / π
	 *   其中 offset = 3
	 */
	private void initEta() {
		final double ARG = 2 / _QEnv.PI;
		final int OFFSET = 3;
		
		for(int i = 0; i < size(); i++) {
			for(int j = 0; j < size(); j++) {
				if(dist(i, j) <= 0) {
					eta[i][j] = 1.0D;
					
				} else if(!isLinked(i, j)) {
					eta[i][j] = 0.0D;
					
				} else {
					eta[i][j] = 1 - Math.atan(dist(i, j) + OFFSET) * ARG;	
				}
			}
		}
	}
	
	/**
	 * 初始化用于计算蚂蚁释放信息素的常量参数
	 */
	private void initBeta() {
		for(int i = 0; i < size(); i++) {
			for(int j = 0; j < size(); j++) {
				if(i == j) {
					deltaBeta[i][j] = 0.0D;
					
				} else if(NumUtils.isZero(maxDist(i) - avgDist(i))) {
					deltaBeta[i][j] = 0.5D;
					
				} else {
					deltaBeta[i][j] = (dist(i, j) - avgDist(i)) / 
						(2 * (maxDist(i) - avgDist(i))) + 0.5D;
				}
			}
		}
	}
	
	protected int size() {
		return qGraph.size();
	}

	protected int srcId() {
		return qGraph.srcId();
	}

	protected int snkId() {
		return qGraph.snkId();
	}

	protected int dist(int srcId, int snkId) {
		return qGraph.dist(srcId, snkId);
	}

	protected double avgDist(int nodeId) {
		return qGraph.avgDist(nodeId);
	}

	protected int maxDist(int nodeId) {
		return qGraph.maxDist(nodeId);
	}
	
	protected double eta(int srcId, int snkId) {
		return eta[srcId][snkId];
	}
	
	protected double deltaBeta(int srcId, int snkId) {
		return deltaBeta[srcId][snkId];
	}
	
	/**
	 * 检查节点是否在必经点集中（包括源宿点）
	 * @param nodeId
	 * @return
	 */
	protected boolean isInclude(int nodeId) {
		return qGraph.isInclude(nodeId);
	}

	/**
	 * 获取必经点集
	 * @return 不包括源宿点的必经点集
	 */
	protected int[] getIncludes() {
		return qGraph.getIncludes();
	}
	
	protected boolean isLinked(int srcId, int snkId) {
		return qGraph.isLinked(srcId, snkId);
	}
	
	protected int MAX_GENERATION() {
		return maxGeneration;
	}
	
	protected int QCROSS_THRESHOLD() {
		return qCrossThreshold;
	}

	protected boolean isUseQCross() {
		return useQCross;
	}

}
