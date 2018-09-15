package exp.libs.algorithm.math;

/**
 * <PRE>
 * 数学算法工具
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2017-08-17
 * @author    EXP: 272629724@qq.com
 * @since     jdk版本：jdk1.6
 */
public class MathUtils {

	/** 归一化公式常量 */
	private final static double NORM_ARG = 2 / Math.PI;
	
	/** 私有化构造函数 */
	protected MathUtils() {}
	
	/**
	 * 归一化函数：
	 *  使得给定的任意稀疏整数映射到 [-1, 1] 的范围
	 * @param num 整数
	 * @return [-1, 1] 范围内的值
	 */
	public static double toNormalization(int num) {
		return Math.atan(num) * NORM_ARG;
	}
	
	/**
	 * 归一化函数：
	 *  使得给定的任意稀疏整数映射到 [-1, 1] 的范围
	 * @param num 整数
	 * @return [-1, 1] 范围内的值
	 */
	public static double toNormalization(long num) {
		return Math.atan(num) * NORM_ARG;
	}
	
	/**
	 * <pre>
	 * 求num平方根的倒数.
	 *  此方法只有float版本的, 对应魔鬼常数是32位的(int) 0x5F375A86
	 *  并不存在double版本算法, 需要对应64位(long) 的魔鬼常数.
	 *  
	 *  64位double可用Math库的sqrt函数求解.
	 * 
	 * ==============================================
	 * 
	 * 原算法源于卡马克的32位浮点数的快速算法(C++版本)：
	 * 
	 * float Q_rsqrt(float number) {
	 *     long i;
	 *     float x2, y;
	 *     const float threehalfs = 1.5F;
	 *     
	 *     x2 = number * 0.5F;
	 *     y  = number;
	 *     i  = * ( long * ) &y;            // evil floating point bit level hacking
	 *     i  = 0x5f3759df - ( i >> 1 );    // what the fuck?
	 *     y  = * ( float * ) &i;
	 *     y  = y * ( threehalfs - ( x2 * y * y ) );        // 1st iteration
	 *     //	y  = y * ( threehalfs - ( x2 * y * y ) );   // 2nd iteration, this can be removed
	 *     return y;
	 * }
	 * 
	 * 算法的原理就是通过选定猜测值a, 用牛顿迭代法 x-f(x)/f'(x) 来不断的逼近f(x)=a的根.
	 * 一般而言, 迭代次数越多, 精度越接近准确值
	 * 
	 * 但是卡马克选择了一个神秘的常数 0x5f3759df 来计算那个猜测值, 
	 * 使得算出的值非常接近1/sqrt(n), 只需要2次牛顿迭代就可以达到所需要的精度.
	 * 
	 * 
	 * 后来Lomont通过暴力测试得到了另一个稍微更好的数字: 0x5f375a86
	 * 于是有了另一个求 1/sqrt(n)的算法版本:
	 * 
	 * float InvSqrt(float x) {
	 *     float xhalf = 0.5f * x;
	 *     int i = *(int *)&x;
	 *     i = 0x5f375a86 - (i>>1);
	 *     x = *(float *)&i;
	 *     x = x * (1.5f - xhalf * x * x);
	 *     return x;
	 * }
	 * 
	 * </pre>
	 * @param num 浮点数
	 * @return 1 / sqrt(num)
	 */
	public static float toRsqrt(float n) {
		if(n <= 0) {
			return 0;
		}
		
		final float N_HALF = n * 0.5F;
		final float T_HALF = 1.5F;
		
		int i = Float.floatToRawIntBits(n);		// int i = *(int *)&n;
		i = 0x5F375A86 - (i >> 1);				// 魔鬼常数猜测值
		n = Float.intBitsToFloat(i);			// n = *(float *)&i;
		n = n * (T_HALF - N_HALF * n * n);		// 牛顿1轮迭代(已达到期望精度)
//		n = n * (T_HALF - N_HALF * n * n);		// 牛顿2轮迭代(精度更高)
		return n;
	}
	
	/**
	 * 计算一个正整数的二进制数中1出现的次数.
	 * ===================================
	 * 
	 * 算法精粹在于 n = (n & (n - 1))
	 * 最好的时间复杂度为O(1)，最坏的时间复杂度为O(n)。
	 * 
	 * @param n 正整数
	 * @return 二进制数中1出现的次数
	 */
	public static int countBitOne(int n) {
		int cnt = 0;
		if(n <= 0) {
			return cnt;
		}
		
		while(n > 0) {
			n = (n & (n - 1));
			cnt++;
		}
		return cnt;
	}
	
	/**
	 * 计算一个正整数的二进制数中1出现的次数.
	 * ===================================
	 * 
	 * 算法精粹在于 n = (n & (n - 1))
	 * 最好的时间复杂度为O(1)，最坏的时间复杂度为O(n)。
	 * 
	 * @param n 正整数
	 * @return 二进制数中1出现的次数
	 */
	public static int countBitOne(long n) {
		int cnt = 0;
		if(n <= 0) {
			return cnt;
		}
		
		while(n > 0) {
			n = (n & (n - 1));
			cnt++;
		}
		return cnt;
	}
	
	/**
	 * 交换a与b的值(参考用):
	 *  a = a ^ b;
	 *  b = a ^ b;
	 *  a = a ^ b;
	 * =========================
	 * 
	 * 此算法在Java中并无实质性的意义(Java函数无法对基本类型入参做任何修改)
	 * 算法原理是利用异或运算的特点:任意一个数与任意一个给定的值连续异或两次，值不变
	 * 
	 * @param a
	 * @param b
	 */
	@Deprecated
	public static void swap(int a, int b) {
		a = a ^ b;
		b = a ^ b;
		a = a ^ b;
	}
	
}
