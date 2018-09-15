package exp.libs.warp.net.ping;

import java.util.LinkedList;
import java.util.List;

import exp.libs.envm.Delimiter;
import exp.libs.utils.num.NumUtils;
import exp.libs.warp.net.http.HttpUtils;

/**
 * <PRE>
 * ping测试返回的结果对象
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-06-27
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class Pong {

	/** 发起ping请求的机器IP */
	private String srcIp;
			
	/** 被ping的机器IP */
	private String snkIp;
	
	/** 发出的单个ping的数据包大小(单位:byte) */
	private int packetBytes;
	
	/** 数据包生存时间(单位:路由跳数) */
	private int ttl;
	
	/** 已发送的ping数据包个数 */
	private int sent;
	
	/** 已接收的ping数据包个数 */
	private int recv;
	
	/** 已丢失的ping数据包个数 */
	private int lost;
	
	/** ping数据包的丢失率(单位:%) */
	private double loss;
			
	/** 执行ping的次数 */
	private int pingCnt;
	
	/** 每次ping的往返时间(单位:ms) */
	private List<Double> rtts;
	
	/** 最小往返时间(单位:ms) */
	private double minRTT;
	
	/** 最大往返时间(单位:ms) */
	private double maxRTT;
	
	/** 平均往返时间(单位:ms) */
	private double avgRTT;
	
	/** 总往返时间(单位:ms) */
	private double totalRTT;
	
	/**
	 * RTT平均偏差, 体现了 ICMP 包的 RTT 偏离平均值的程度, 这个值越大说明网速越不稳定.
	 * 其计算公式为：mdev = SQRT( SUM(RTT*RTT)/N – (SUM(RTT)/N)^2 ) 
	 */
	private double mdevRTT;
			
	/**
	 * 构造函数
	 */
	protected Pong() {
		this.srcIp = HttpUtils.getLocalIP();
		this.snkIp = "";
		this.packetBytes = 0; 
		this.ttl = 0; 
		this.sent = 0; 
		this.recv = 0; 
		this.lost = 0; 
		this.loss = 0;
		this.pingCnt = 0; 
		this.rtts = new LinkedList<Double>();
		this.minRTT = 0; 
		this.maxRTT = 0; 
		this.avgRTT = 0; 
		this.totalRTT = 0;
		this.mdevRTT = 0;
	}
	
	public String getSrcIp() {
		return srcIp;
	}

	public String getSnkIp() {
		return snkIp;
	}

	protected void setSnkIp(String snkIp) {
		this.snkIp = snkIp;
	}

	public int getPacketBytes() {
		return packetBytes;
	}

	protected void setPacketBytes(int packetBytes) {
		this.packetBytes = packetBytes;
	}

	public int getTTL() {
		return ttl;
	}

	protected void setTTL(int ttl) {
		this.ttl = ttl;
	}

	public int getSent() {
		return sent;
	}

	protected void setSent(int sent) {
		this.sent = sent;
	}

	public int getRecv() {
		return recv;
	}

	protected void setRecv(int recv) {
		this.recv = recv;
	}

	public int getLost() {
		return lost;
	}

	protected void setLost(int lost) {
		this.lost = lost;
	}

	public double getLoss() {
		return loss;
	}

	protected void setLoss(double loss) {
		this.loss = loss;
	}

	public int getPingCnt() {
		return pingCnt;
	}

	public List<Double> getRTTs() {
		return rtts;
	}

	protected void addRTT(double rtt) {
		this.rtts.add(rtt);
		pingCnt++;
		totalRTT += (rtt >= 0 ? rtt : 0);
	}

	public double getMinRTT() {
		return minRTT;
	}

	protected void setMinRTT(double minRTT) {
		this.minRTT = minRTT;
	}

	public double getMaxRTT() {
		return maxRTT;
	}

	protected void setMaxRTT(double maxRTT) {
		this.maxRTT = maxRTT;
	}

	public double getAvgRTT() {
		return avgRTT;
	}

	protected void setAvgRTT(double avgRTT) {
		this.avgRTT = avgRTT;
	}

	public double getTotalRTT() {
		return totalRTT;
	}

	public void setTotalRTT(double totalRTT) {
		this.totalRTT = totalRTT;
	}

	public double getMdevRTT() {
		if(mdevRTT <= 0 && pingCnt > 0) {
			double sumRTT2 = 0;
			for(double rtt : rtts) {
				rtt = (rtt < 0 ? 0 : rtt);
				sumRTT2 += rtt * rtt;
			}
			double square = sumRTT2 / pingCnt - avgRTT * avgRTT;
			if(square >= 0) {
				mdevRTT = Math.sqrt(square);
				mdevRTT = NumUtils.setPrecision(mdevRTT, 3);
			}
		}
		return mdevRTT;
	}

	public void setMdevRTT(double mdevRTT) {
		this.mdevRTT = mdevRTT;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(srcIp).append(" Pinging ").append(snkIp).append(" with ");
		sb.append(packetBytes).append("(x").append(pingCnt);
		sb.append(") bytes of data:").append(Delimiter.CRLF);
		for(int seq = 0; seq < rtts.size(); seq++) {
			double rtt = rtts.get(seq);
			if(rtt < 0) {
				sb.append("    Request timed out.").append(Delimiter.CRLF);
				
			} else {
				sb.append("    Reply from ").append(snkIp).append(": icmp_seq=").append(seq + 1);
				sb.append(" bytes=").append(packetBytes).append(" TTL=").append(ttl);
				sb.append(" RTT=").append(rtt).append("ms").append(Delimiter.CRLF);
			}
		}
		sb.append(Delimiter.CRLF);
		
		sb.append("Ping statistics for ").append(snkIp).append(":").append(Delimiter.CRLF);
		sb.append("    Packets: Sent = ").append(sent);
		sb.append(", Received = ").append(recv);
		sb.append(", Lost = ").append(lost);
		sb.append(" (").append(loss).append("% loss)").append(Delimiter.CRLF);
		sb.append(Delimiter.CRLF);
		
		sb.append("Approximate round trip times in milli-seconds:").append(Delimiter.CRLF);
		sb.append("    RTT: Min = ").append(minRTT);
		sb.append("ms, Max = ").append(maxRTT);
		sb.append("ms, Avg = ").append(avgRTT);
		sb.append("ms, Total = ").append(totalRTT);
		sb.append("ms, Mdev = ").append(getMdevRTT()).append("ms").append(Delimiter.CRLF);
		sb.append(Delimiter.CRLF);
		return sb.toString();
	}
	
}
