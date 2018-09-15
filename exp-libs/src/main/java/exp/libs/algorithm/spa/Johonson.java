package exp.libs.algorithm.spa;

/**
 * <PRE>
 * 最短路径算法: Johonson算法.
 * 适用范围: 全源最短路问题, 有向图/无向图均可, 无负权环（但可检测负权环）
 * 时间复杂度: O(V * E * lgV)
 * 空间复杂度: 
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2016-07-25
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Johonson {

	/**
	 * 先使用BellmanFord算法，使所有的边的权重变为非负值，  
	 * 然后运用dijkstra算法依次求出每个节点的最短路径  
	 */
}
