package exp.libs.algorithm.heuristic.qaca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exp.libs.utils.format.ESCUtils;

/**
 * <PRE>
 * 可行解
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-06-08
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class QRst {

	/** 无效结果 */
	public final static QRst NULL_RST = new QRst(-1, 0);
	
	/** 无效节点ID */
	protected final static int INVAILD_ID = -1;
	
	/** 所属蚂蚁编号 */
	private int antId;
	
	/** 拓扑图规模 */
	private int size;
	
	/** 蚂蚁所携带的所有路径信息素的概率幅(量子基因编码, 每代求解遗传) */
	private __QPA[][] _QPAs;
	
	/** 蚂蚁在当代的移动轨迹的总开销 */
	private int cost;
	
	/** 蚂蚁在当代的移动步长 */
	private int step;
	
	/** 蚂蚁在当代的移动轨迹 */
	private int[] routes;
	
	/** 是否为可行解 */
	private boolean isVaild;
	
	private QRst(final int antId, final int size) {
		this.size = size;
		this.routes = new int[size];
		this._QPAs = new __QPA[size][size];
	}
	
	protected QRst(final int antId, final _QEnv env) {
		this.size = env.size();
		this.routes = new int[size];
		initQPAs(env);
		reset();
	}
	
	/**
	 * 初始化蚂蚁的量子编码
	 */
	private void initQPAs(final _QEnv env) {
		this._QPAs = new __QPA[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j <= i; j++) {
				_QPAs[i][j] = new __QPA();
				if(env.eta(i, j) == 0) {
					_QPAs[i][j].setAlpha(1D);
					_QPAs[i][j].setBeta(0D);
				}
				
				if(i != j) {
					_QPAs[j][i] = new __QPA();
					if(env.eta(j, i) == 0) {
						_QPAs[j][i].setAlpha(1D);
						_QPAs[j][i].setBeta(0D);
					}
				}
			}
		}
	}
	
	protected void reset() {
		this.isVaild = false;
		this.cost = 0;
		this.step = 0;
		Arrays.fill(routes, -1);
	}
	
	protected boolean move(int nextRouteId, int routeCost) {
		boolean isOk = false;
		if(step < size && nextRouteId >= 0 && routeCost >= 0) {
			routes[step++] = nextRouteId;
			cost += routeCost;
			isVaild = (step == size);
			isOk = true;
		}
		return isOk;
	}
	
	public int getAntId() {
		return antId;
	}
	
	protected __QPA QPA(int srcId, int snkId) {
		return _QPAs[srcId][snkId];
	}

	public int getCost() {
		return cost;
	}
	
	protected void setCost(int cost) {
		this.cost = cost;
	}

	public int getStep() {
		return step;
	}
	
	public int[] getRoutes() {
		return routes;
	}
	
	protected int getCurId() {
		int curId = INVAILD_ID;
		if(step > 0) {
			curId = routes[step - 1];
		}
		return curId;
	}
	
	protected int getLastId() {
		int lastId = INVAILD_ID;
		if(step > 1) {
			lastId = routes[step - 2];
		}
		return lastId;
	}
	
	public boolean isVaild() {
		return isVaild;
	}

	protected void markVaild() {
		this.isVaild = true;
	}

	protected QRst clone() {
		QRst other = new QRst(this.antId, this.size);
		other.copy(this);
		return other;
	}
	
	protected void copy(QRst other) {
		if(other != null && this.size == other.size) {
			this.isVaild = other.isVaild;
			this.cost = other.cost;
			this.step = other.step;
			for(int i = 0; i < size; i++) {
				this.routes[i] = other.routes[i];
				for(int j = 0; j < size; j++) {
					if(this._QPAs[i][j] == null) {
						this._QPAs[i][j] = new __QPA();
					}
					this._QPAs[i][j].setAlpha(other._QPAs[i][j].getAlpha());
					this._QPAs[i][j].setBeta(other._QPAs[i][j].getBeta());
				}
			}
		}
	}

	public String toQPAInfo() {
		List<List<Object>> table = new ArrayList<List<Object>>(size + 1);
		List<Object> head = new ArrayList<Object>(size + 1);
		head.add("");
		for(int i = 0; i < size; i++) {
			head.add(i);
		}
		table.add(head);
		
		for(int i = 0; i < size; i++) {
			List<Object> row = new ArrayList<Object>(size + 1);
			row.add(i);
			for(int j = 0; j < size; j++) {
				row.add(_QPAs[i][j].getBeta());
			}
			table.add(row);
		}
		return ESCUtils.toTXT(table, true);
	}
	
	public String toRouteInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("  [vaild] : ").append(isVaild);
		sb.append("\r\n  [step/node] : ").append(step).append("/").append(size);
		sb.append("\r\n  [cost] : ").append(cost);
		sb.append("\r\n  [route] : ");
		if(step > 0) {
			for(int i = 0; i < step - 1; i++) {
				sb.append(routes[i]).append("->");
			}
			sb.append(routes[step - 1]);
		} else {
			sb.append("null");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(toQPAInfo()).append(toRouteInfo());
		return sb.toString();
	}
	
}
