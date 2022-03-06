package exp.libs.algorithm.heuristic.qaca;

import java.util.concurrent.Callable;

/**
 * <PRE>
 * 量子蚂蚁行为执行线程
 * </PRE>
 * 
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-06-08
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
final class _QAntThread implements Callable<QRst> {

	private final _QAnt qAnt;
	
	private final QRst bestRst;
	
	protected _QAntThread(final _QAnt qAnt, final QRst bestRst) {
		this.qAnt = qAnt;
		this.bestRst = bestRst;
	}

	@Override
	public QRst call() throws Exception {
		qAnt.solve(bestRst);
		return qAnt.getResult();
	}
	
}
