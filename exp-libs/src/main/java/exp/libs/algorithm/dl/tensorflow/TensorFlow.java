package exp.libs.algorithm.dl.tensorflow;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import exp.libs.utils.img.ImageUtils;

/**
 * <PRE>
 * TensorFlow深度学习训练模型调用接口
 * </PRE>
 * <br/><B>PROJECT : </B> exp-libs
 * <br/><B>SUPPORT : </B> <a href="http://www.exp-blog.com" target="_blank">www.exp-blog.com</a> 
 * @version   2018-03-04
 * @author    EXP: ***REMOVED***@qq.com
 * @since     jdk版本：jdk1.6
 */
public class TensorFlow {
	
	/** 模型接口 */
	private TensorFlowAPI tfAPI;
	
	/**
     * 构造函数
     * @param pbModelFilePath 已训练好的PB模型文件路径
     */
	public TensorFlow(String pbModelFilePath) {
		this.tfAPI = new TensorFlowAPI(pbModelFilePath);
	}
	
	/**
	 * 设置训练模型的输入变量矩阵值 （即输入张量）
	 * @param feedName 变量名称（在python训练模型时定义）
	 * @param feedValue 变量矩阵（N维输入矩阵降维到一维矩阵的值）
	 * @param dims 变量矩阵的维度值列表， 如 2x3矩阵，则此处为 {2, 3}
	 */
	public void setInput(final String feedName, float[] feedValue, long... dims) {
		tfAPI.feed(feedName, feedValue, dims);	// 设置输入张量
	}
	
	/**
	 * 获取输出矩阵（即输出张量）
	 * @param fetchName
	 * @return
	 */
	public float[] getOutput(final String fetchName) {
		tfAPI.run(fetchName);			// 执行模型运算
		return tfAPI.fetch(fetchName);	// 获取输出张量的矩阵值
	}
	
	/**
	 * 加载单通道2D图像，并降维到一维数组
	 * 	(模拟python的PIL组件所读取的单通道图像数据格式, 其中黑色标记值为0， 白色标记值为1)
	 * @param binaryImage 单通道图像
	 * @return
	 */
	public float[] loadImage(BufferedImage binaryImage) {
		float[] pixels = new float[0];
		if(binaryImage != null && 
				binaryImage.getType() == BufferedImage.TYPE_BYTE_BINARY) {
			final int H = binaryImage.getHeight();	// 高（即行数）
			final int W = binaryImage.getWidth();	// 宽（即列数）
			pixels = new float[H * W];
			
			for (int h = 0; h < H; h++) {
				int offset = h * W;
				for (int w = 0; w < W; w++) {
					int RGB = binaryImage.getRGB(w, h);
					float val = (RGB == ImageUtils.RGB_BLACK ? 0.0f : 1.0f);
					pixels[offset + w] = val;
				}
			}
		}
		return pixels;
	}
	
	/**
	 * 仿照python的numpy.argmax功能.
	 * 	求 matrix 第axis维 的最大值的索引矩阵
	 * 
	 * 此方法暂时只针对二维矩阵
	 *  当axis=0时，求每列的的最大值的索引
	 *  当axis=1时，求每行的的最大值的索引
	 * 
	 * @param matrix
	 * @param row
	 * @param col
	 * @param axis 0或1
	 * @return
	 */
	public int[] argmax(float[] matrix, int row, int col, int axis) {
		int[] idxs = new int[0];
		if(matrix.length == row * col) {
			
			final int DIM = (axis == 1 ? row : col);
			idxs = new int[DIM];
			float[] maxs = new float[DIM];
			
			Arrays.fill(idxs, 0);
			Arrays.fill(maxs, -Float.MAX_VALUE);
			
			for(int i = 0; i < matrix.length; i++) {
				int rOffset = i / col;
				int cOffset = i % col;
				int offset = (axis == 1 ? rOffset : cOffset);
				float val = matrix[i];
				
				if(maxs[offset] < val) {
					maxs[offset] = val;
					idxs[offset] = (axis == 1 ? cOffset : rOffset);
				}
			}
		}
		return idxs;
	}
	
}
